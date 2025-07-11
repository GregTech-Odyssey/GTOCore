package com.gtocore.api.gui.ktflexible

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

import com.lowdragmc.lowdraglib.gui.widget.Widget

interface DataSyncOperations<T> {
    fun writeToBuffer(buffer: FriendlyByteBuf, value: T)
    fun readFromBuffer(buffer: FriendlyByteBuf): T
}

class SyncField<T>(private val supplier: () -> T, private val operations: DataSyncOperations<T>, private val updateId: Int, initialValue: T) {

    var lastValue: T = initialValue
        private set

    fun updateValue(newValue: T) {
        lastValue = newValue
    }

    fun getLatestValue(): T = supplier()

    fun writeToBuffer(buffer: FriendlyByteBuf) {
        operations.writeToBuffer(buffer, lastValue)
    }

    fun readFromBuffer(buffer: FriendlyByteBuf): T = operations.readFromBuffer(buffer)

    fun hasChanged(): Boolean = getLatestValue() != lastValue

    fun getUpdateId(): Int = updateId
}

class DataSyncDelegate(private val widget: Widget) {
    private val syncFields = mutableListOf<SyncField<*>>()
    private val pendingUpdates = mutableMapOf<Int, (FriendlyByteBuf) -> Unit>()

    fun <T> addSyncField(supplier: () -> T, operations: DataSyncOperations<T>, updateId: Int, initialValue: T): SyncField<T> {
        val field = SyncField(supplier, operations, updateId, initialValue)
        syncFields.add(field)
        return field
    }

    fun writeInitialData(buffer: FriendlyByteBuf?) {
        if (!widget.isClientSideWidget) {
            buffer?.writeBoolean(true)
            syncFields.forEach { field ->
                updateFieldValueUnsafe(field, field.getLatestValue())
                field.writeToBuffer(buffer!!)
            }
        } else {
            buffer?.writeBoolean(false)
        }
    }

    fun readInitialData(buffer: FriendlyByteBuf?) {
        if (buffer?.readBoolean() == true) {
            syncFields.forEach { field ->
                val newValue = field.readFromBuffer(buffer)
                updateFieldValueUnsafe(field, newValue)
            }
        }
    }

    fun detectAndSendChanges() {
        if (!widget.isClientSideWidget) {
            pendingUpdates.clear()
            syncFields.forEach { field ->
                if (field.hasChanged()) {
                    val newValue = field.getLatestValue()
                    updateFieldValueUnsafe(field, newValue)
                    pendingUpdates[field.getUpdateId()] = { field.writeToBuffer(it) }
                }
            }
        }
    }

    fun readUpdateInfo(id: Int, buffer: FriendlyByteBuf) {
        syncFields.find { it.getUpdateId() == id }?.let { field ->
            val newValue = field.readFromBuffer(buffer)
            updateFieldValueUnsafe(field, newValue)
        }
    }

    fun updateScreen() {
        if (widget.isClientSideWidget) {
            syncFields.forEach { field ->
                if (field.hasChanged()) {
                    val newValue = field.getLatestValue()
                    updateFieldValueUnsafe(field, newValue)
                }
            }
        }
    }
    fun getPendingUpdates(): Map<Int, (FriendlyByteBuf) -> Unit> = pendingUpdates

    @Suppress("UNCHECKED_CAST")
    private fun updateFieldValueUnsafe(field: SyncField<*>, newValue: Any?) {
        (field as SyncField<Any?>).updateValue(newValue)
    }
}

object DataOperations {
    val INT = object : DataSyncOperations<Int> {
        override fun writeToBuffer(buffer: FriendlyByteBuf, value: Int) {
            buffer.writeInt(value)
        }
        override fun readFromBuffer(buffer: FriendlyByteBuf): Int = buffer.readInt()
    }

    val COMPONENT = object : DataSyncOperations<Component> {
        override fun writeToBuffer(buffer: FriendlyByteBuf, value: Component) {
            buffer.writeComponent(value)
        }
        override fun readFromBuffer(buffer: FriendlyByteBuf): Component = buffer.readComponent()
    }
}

abstract class SyncWidget(x: Int, y: Int, width: Int, height: Int) : Widget(x, y, width, height) {
    private val syncDelegate = DataSyncDelegate(this)

    // 添加同步字段的便捷方法
    protected fun <T> syncField(supplier: () -> T, operations: DataSyncOperations<T>, updateId: Int, initialValue: T): SyncField<T> = syncDelegate.addSyncField(supplier, operations, updateId, initialValue)

    protected fun syncInt(supplier: () -> Int, updateId: Int, initialValue: Int = 0): SyncField<Int> = syncField(supplier, DataOperations.INT, updateId, initialValue)

    protected fun syncComponent(supplier: () -> Component, updateId: Int, initialValue: Component): SyncField<Component> = syncField(supplier, DataOperations.COMPONENT, updateId, initialValue)

    override fun writeInitialData(buffer: FriendlyByteBuf?) {
        super.writeInitialData(buffer)
        syncDelegate.writeInitialData(buffer)
    }

    override fun readInitialData(buffer: FriendlyByteBuf?) {
        super.readInitialData(buffer)
        syncDelegate.readInitialData(buffer)
    }

    override fun detectAndSendChanges() {
        super.detectAndSendChanges()
        syncDelegate.detectAndSendChanges()

        val pendingUpdates = syncDelegate.getPendingUpdates()
        pendingUpdates.forEach { (id, writer) ->
            writeUpdateInfo(id, writer)
        }
    }

    @OnlyIn(Dist.CLIENT)
    override fun readUpdateInfo(id: Int, buffer: FriendlyByteBuf) {
        super.readUpdateInfo(id, buffer)
        syncDelegate.readUpdateInfo(id, buffer)
    }

    @OnlyIn(Dist.CLIENT)
    override fun updateScreen() {
        super.updateScreen()
        syncDelegate.updateScreen()
    }
}
