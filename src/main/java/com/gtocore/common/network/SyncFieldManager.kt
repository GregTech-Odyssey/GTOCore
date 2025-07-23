package com.gtocore.common.network

import com.lowdragmc.lowdraglib.syncdata.IContentChangeAware
import com.lowdragmc.lowdraglib.syncdata.ITagSerializable
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.server.ServerLifecycleHooks
import java.util.function.Supplier

object SyncFieldManager {
    val syncFieldMap = FieldMap()
    class FieldMap : LinkedHashMap<Pair<Supplier<String>, LogicalSide>, SyncField<*>>() {
        override fun put(key: Pair<Supplier<String>, LogicalSide>, value: SyncField<*>): SyncField<*>? {
            require(syncFieldMap.filter { it.value.uniqueName === value.uniqueName && it.value.side == value.side }.isEmpty()) { "${value.errorPrefix} SyncField name is already registered" }
            return super.put(key, value)
        }

        fun match(key: Pair<String, LogicalSide>): SyncField<*>? {
            return syncFieldMap.filter { it.key.first.get() == key.first && it.key.second == key.second }.values.firstOrNull()
        }
        fun strictMatch(key: Pair<Supplier<String>, LogicalSide>): SyncField<*>? {
            return syncFieldMap.filter { it.key === key }.values.firstOrNull()
        }
    }
    fun clear() {
        syncFieldMap.clear()
    }
    fun <T> registerSyncField(syncField: SyncField<T>) {
        syncFieldMap.put(syncField.uniqueName to syncField.side, syncField)
    }
    //////////////////////////////////
    // ****** 服务器修改，客户端更新 ******//
    ////////////////////////////////
    fun syncToAllClients(uniqueName: String){
        val syncField = syncFieldMap.match(uniqueName to LogicalSide.SERVER)
        require(syncField != null) { "SyncField with name $uniqueName is not registered" }
        val server = ServerLifecycleHooks.getCurrentServer()
        server?.playerList?.players?.forEach{
            ServerMessage.send(server,it,"sync_field", { buf ->
                buf.writeUtf(uniqueName)
                syncField.writeToBuffer(buf)
            })
        }
    }
    fun handleFromServer(buffer: FriendlyByteBuf) {
        val uniqueName = buffer.readUtf()
        val syncField = syncFieldMap.match(uniqueName to LogicalSide.CLIENT)
        require(syncField != null) { "SyncField with name $uniqueName is not registered" }
        syncField.handleFromServer(buffer)
    }
    //////////////////////////////////
    // ****** 客户端修改，服务器更新 ******//
    //////////////////////////////////
    fun syncToAllServer(uniqueName: String) {
        val syncField = syncFieldMap.match(uniqueName to LogicalSide.CLIENT)
        require(syncField != null) { "SyncField with name $uniqueName is not registered" }
        ClientMessage.send("sync_field",{buf ->
            buf.writeUtf(uniqueName)
            syncField.writeToBuffer(buf)
        })
    }
    fun handleFromClient(buffer: FriendlyByteBuf) {
        val uniqueName = buffer.readUtf()
        val syncField = syncFieldMap.match(uniqueName to LogicalSide.SERVER)
        require(syncField != null) { "SyncField with name $uniqueName is not registered" }
        syncField.handleFromClient(buffer)
//        syncToAllClients(uniqueName)
    }
}
abstract class SyncField<T> (
    val side: LogicalSide,
    val uniqueName : Supplier<String>,
    var value: T,
    var onInitCallBack : (SyncField<T>,new:T)-> Unit = { _, _ -> },
    var onSyncCallBack : (SyncField<T>,old:T,new:T)-> Unit = { _, _, _ -> },
): ITagSerializable<CompoundTag>, IContentChangeAware
{
    var onContentChanged : Runnable? = null
    override fun setOnContentsChanged(onContentChanged: Runnable?) {
        this.onContentChanged = onContentChanged
    }

    override fun getOnContentsChanged(): Runnable? {
        return onContentChanged
    }

    val errorPrefix = "[SyncField ${uniqueName} in side ${side}] :"
    init {
        //init
        onInitCallBack(this,value)
        //register
        SyncFieldManager.registerSyncField(this)
    }
    fun unregister() {
        SyncFieldManager.syncFieldMap.remove(uniqueName to side)
    }
    //////////////////////////////////
    // ****** 服务器修改，客户端更新 ******//
    ////////////////////////////////
    fun updateInServer(newValue: T) {
        require(side==LogicalSide.SERVER){ "$errorPrefix This method can only be called in server side" }
        if (value != newValue) {
            val oldValue = value
            value = newValue
            onSyncCallBack(this, oldValue, newValue)
            SyncFieldManager.syncToAllClients(uniqueName.get())
        }
    }
    fun handleFromServer(buffer: FriendlyByteBuf) {
        require(side==LogicalSide.CLIENT){ "$errorPrefix This method can only be called in client side" }
        val oldValue = value
        value = readFromBuffer(buffer)
        onSyncCallBack(this,oldValue , value)
    }
    //////////////////////////////////
    // ****** 客户端修改，服务器更新 ******//
    ////////////////////////////////
    fun updateInClient(newValue: T) {
        require(side==LogicalSide.CLIENT){ "$errorPrefix This method can only be called in client side" }
        if (value != newValue) {
            val oldValue = value
            value = newValue
            onSyncCallBack(this,oldValue , value)
            SyncFieldManager.syncToAllServer(uniqueName.get())
        }
    }
    fun handleFromClient(buffer: FriendlyByteBuf) {
        require(side==LogicalSide.SERVER){ "$errorPrefix This method can only be called in server side" }
        val oldValue = value
        value = readFromBuffer(buffer)
        onSyncCallBack(this,oldValue , value)
    }
    abstract fun readFromBuffer(buffer: FriendlyByteBuf) : T
    abstract fun writeToBuffer(buffer: FriendlyByteBuf): FriendlyByteBuf
}
fun createLogicalSide(isRemote : Boolean):LogicalSide = if(isRemote) LogicalSide.CLIENT else LogicalSide.SERVER
class IntSyncField(
    side: LogicalSide,
    uniqueName: Supplier<String>,
    value: Int,
    onInitCallBack: (SyncField<Int>, Int) -> Unit = { _, _ -> },
    onSyncCallBack: (SyncField<Int>, Int, Int) -> Unit = { _, _, _ -> },
) : SyncField<Int>(side, uniqueName, value, onInitCallBack, onSyncCallBack) {
    override fun readFromBuffer(buffer: FriendlyByteBuf): Int =buffer.readInt()
    override fun writeToBuffer(buffer: FriendlyByteBuf): FriendlyByteBuf = let { buffer.writeInt(value);buffer }
    override fun serializeNBT(): CompoundTag {
        return CompoundTag().apply { putInt("value",value) }
    }

    override fun deserializeNBT(nbt: CompoundTag) {
        updateInServer(nbt.getInt("value"))
    }
}

class BooleanSyncField(
    side: LogicalSide,
    uniqueName: Supplier<String>,
    value: Boolean,
    onInitCallBack: (SyncField<Boolean>, Boolean) -> Unit = { _, _ -> },
    onSyncCallBack: (SyncField<Boolean>, Boolean, Boolean) -> Unit = { _, _, _ -> },
) : SyncField<Boolean>(side, uniqueName, value, onInitCallBack, onSyncCallBack) {
    override fun readFromBuffer(buffer: FriendlyByteBuf): Boolean =buffer.readBoolean()
    override fun writeToBuffer(buffer: FriendlyByteBuf): FriendlyByteBuf = let { buffer.writeBoolean(value);buffer }
    override fun serializeNBT(): CompoundTag {
        return CompoundTag().apply { putBoolean("value",value) }
    }
    override fun deserializeNBT(nbt: CompoundTag) {
        updateInServer(nbt.getBoolean("value"))
    }
}