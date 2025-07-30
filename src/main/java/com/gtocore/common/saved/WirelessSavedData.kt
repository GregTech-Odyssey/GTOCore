package com.gtocore.common.saved

import com.gtocore.common.network.SyncField
import com.gtocore.common.network.createLogicalSide
import com.gtocore.integration.ae.WirelessMachine

import net.minecraft.core.UUIDUtil
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.level.saveddata.SavedData
import net.minecraftforge.fml.LogicalSide
import net.minecraftforge.fml.loading.FMLEnvironment

import appeng.api.networking.GridHelper
import appeng.api.networking.IGridConnection
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

import java.util.UUID
import java.util.function.Supplier

object WirelessSavedData : SavedData() {
    val grid = WirelessSyncField(
        createLogicalSide(FMLEnvironment.dist.isClient),
        { "WirelessSavedData" },
        value = mutableListOf(),
        onInitCallBack = { _, _ -> },
        onSyncCallBack = { _, _, _ -> },
    )
    val defaultMap = mutableMapOf<UUID, String>()

    // ///////////////////////////////
    // ****** SavedData To SyncField ******//
    // //////////////////////////////
    override fun save(p0: CompoundTag): CompoundTag {
        p0.put(
            "WirelessSavedData",
            ListTag().also {
                for (grid in grid.value) {
                    it.add(grid.serializer())
                }
            },
        )
        p0.put(
            "defaultMap",
            ListTag().also {
                for ((key, value) in defaultMap) {
                    it.add(
                        CompoundTag().also {
                            it.putUUID("key", key)
                            it.putString("value", value)
                        },
                    )
                }
            },
        )
        return p0
    }
    fun load(p0: CompoundTag): WirelessSavedData {
        val list = p0.getList("WirelessSavedData", 10)
        for (tag in list) {
            grid.value.add(WirelessGrid.deserializer(tag as CompoundTag).takeIf { n -> grid.value.none { it.name == n?.name } } ?: continue)
        }
        val defaultList = p0.getList("defaultMap", 10)
        for (tag in defaultList) {
            val nbt = tag as CompoundTag
            defaultMap[UUID.fromString(nbt.getString("key"))] = nbt.getString("value")
        }
        return this
    }

    // ////////////////////////////////
    // ****** SERVER API ******//
    // //////////////////////////////
    fun joinToGrid(gridName: String, machine: WirelessMachine, requester: UUID): STATUS {
        leaveGrid(machine)
        val grid = grid.value.firstOrNull { it.name == gridName } ?: return STATUS.NOT_FOUND_GRID
        if (grid.owner != requester) return STATUS.NOT_PERMISSION
        if (grid.connectionPool.any { it == machine }) return STATUS.ALREADY_JOINT
        grid.connectionPool.add(machine)
        machine.addedToGrid(gridName)
        grid.refreshConnectionPool()
        return STATUS.SUCCESS
    }
    fun leaveGrid(machine: WirelessMachine) {
        grid.value.find { it -> it.connectionPool.any { it == machine } }
            ?.let { grid ->
                machine.removedFromGrid(grid.name)
                grid.connectionPool.remove(machine)
                grid.refreshConnectionPool()
            }
    }
    fun createNewGrid(gridName: String, requester: UUID): WirelessGrid? {
        grid.value.find { grid -> grid.name == gridName }?.let { return null }
        val newGrid = WirelessGrid(gridName, requester)
        grid.updateInServer(grid.value.also { it.add(newGrid) })
        setDirty()
        return newGrid
    }
    fun removeGrid(gridName: String, requester: UUID): STATUS {
        grid.value.find { it.name == gridName }?.let { removed ->
            if (removed.owner != requester) return STATUS.NOT_PERMISSION
            removed.connectionPool.forEach { it.removedFromGrid(removed.name) }
            removed.connectionPool.clear()
            removed.refreshConnectionPool()
            grid.updateInServer(grid.value.also { it.remove(removed) })
            return STATUS.SUCCESS
        }
        setDirty()
        return STATUS.NOT_FOUND_GRID
    }
    fun getGirdList(): List<WirelessGrid> = grid.value
    fun setAsDefault(gridName: String, requester: UUID) {
        defaultMap[requester] = gridName
        grid.value.find { it.name == gridName && it.owner == requester }?.let { it.isDefault = true }
        grid.value.filter { it.name != gridName && it.owner == requester }.forEach { it.isDefault = false }
        grid.updateInServer(grid.value)
    }
    fun cancelAsDefault(gridName: String, requester: UUID) {
        defaultMap.remove(requester)
        grid.value.find { it.name == gridName && it.owner == requester }?.let { it.isDefault = false }
        grid.updateInServer(grid.value)
    }
}
enum class STATUS {
    SUCCESS,
    ALREADY_JOINT,
    NOT_FOUND_GRID,
    NOT_PERMISSION,
}
class WirelessGrid(val name: String, val owner: UUID, var isDefault: Boolean = false) {
    companion object {
        val CODEC: Codec<WirelessGrid> = RecordCodecBuilder.create { b ->
            b.group(
                Codec.STRING.fieldOf("name").forGetter { it.name },
                UUIDUtil.CODEC.fieldOf("owner").forGetter { it.owner },
                Codec.BOOL.fieldOf("isDefault").forGetter { it.isDefault },
            ).apply(b, ::WirelessGrid)
        }
        fun deserializer(nbt: CompoundTag): WirelessGrid? = CODEC.parse(NbtOps.INSTANCE, nbt).result().orElse(null)
    }
    fun serializer(): CompoundTag = CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow(false, null) as CompoundTag

    // ///////////////////////////////
    // ****** RUN TIME ******//
    // //////////////////////////////
    val connectionPool = mutableListOf<WirelessMachine>()
    val connectionHolderPool = mutableListOf<IGridConnection>()

    // ////////////////////////////////
    // ****** TOOLS ******//
    // //////////////////////////////
    fun refreshConnectionPool() {
        // 1.摧毁网络
        connectionHolderPool.forEach { it.destroy() }
        connectionHolderPool.clear()
        // 2.重建网络
        connectionPool.windowed(2).forEach {
            try {
                val first = it[0]
                val second = it[1]
                if (!(first.mainNode.node?.level?.hasChunkAt(first.self().pos) ?: false)) return@forEach
                if (!(second.mainNode.node?.level?.hasChunkAt(second.self().pos) ?: false)) return@forEach
                val gridConnection = GridHelper.createConnection(first.mainNode.node, second.mainNode.node)
                println("create connection between ${first.self().pos} and ${second.self().pos}")
                connectionHolderPool.add(gridConnection)
            } catch (ignore: Exception) {
            }
        }
    }
}
class WirelessSyncField(side: LogicalSide, uniqueName: Supplier<String>, value: MutableList<WirelessGrid>, onInitCallBack: (SyncField<MutableList<WirelessGrid>>, MutableList<WirelessGrid>) -> Unit = { _, _ -> }, onSyncCallBack: (SyncField<MutableList<WirelessGrid>>, MutableList<WirelessGrid>, MutableList<WirelessGrid>) -> Unit = { _, _, _ -> }) : SyncField<MutableList<WirelessGrid>>(side, uniqueName, value, onInitCallBack, onSyncCallBack) {
    override fun readFromBuffer(buffer: FriendlyByteBuf): MutableList<WirelessGrid> {
        val size = buffer.readInt()
        val list = mutableListOf<WirelessGrid>()
        for (i in 0 until size) {
            list.add(WirelessGrid.deserializer(buffer.readNbt() as CompoundTag)!!)
        }
        return list
    }

    override fun writeToBuffer(buffer: FriendlyByteBuf): FriendlyByteBuf {
        buffer.writeInt(value.size)
        for (grid in value) {
            buffer.writeNbt(grid.serializer())
        }
        return buffer
    }
}
