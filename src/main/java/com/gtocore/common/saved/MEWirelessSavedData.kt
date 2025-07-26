package com.gtocore.common.saved

import com.gtocore.common.network.SyncField
import com.gtocore.common.network.createLogicalSide
import com.gtocore.integration.ae.MEWirelessConnectionMachine

import net.minecraft.core.UUIDUtil
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.level.saveddata.SavedData
import net.minecraftforge.fml.LogicalSide

import appeng.api.networking.GridHelper
import appeng.api.networking.IGridConnection
import com.gregtechceu.gtceu.GTCEu
import com.gtolib.GTOCore
import com.hepdd.gtmthings.utils.TeamUtil
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

import java.util.UUID
import java.util.function.Supplier
import kotlin.collections.forEach

private const val NBT_GRID = "MEWirelessConnectionGrids"

object MEWirelessSavedData : SavedData() {
    val SyncMEWirelessGrids = GridSyncField(
        createLogicalSide(GTCEu.isClientSide()),
        { "MEWirelessSavedData" },
        value = mutableListOf(),
    )
    fun findGridById(gridID: UUID): MEWirelessConnectionGrid? = SyncMEWirelessGrids.value.firstOrNull { it.gridID == gridID }
    fun findGridByPlayerId(playerId: UUID): List<MEWirelessConnectionGrid> = SyncMEWirelessGrids.value.filter {
        TeamUtil.getTeamUUID(playerId) == TeamUtil.getTeamUUID(it.gridOwnerUUid)
    }
    fun getGridCount(): Int = SyncMEWirelessGrids.value.size

    fun createNewGrid(gridName: String, playerId: UUID): MEWirelessConnectionGrid? {
        if (gridName.isEmpty()) return null
        if (SyncMEWirelessGrids.value.any { it.gridName == gridName }) return null
        val newGrid = MEWirelessConnectionGrid(gridName, playerId)
        SyncMEWirelessGrids.updateInServer(SyncMEWirelessGrids.value.also { it.add(newGrid) })
        setDirty()
        return newGrid
    }
    fun removeGrid(grid: MEWirelessConnectionGrid) {
        grid.connectionPool.forEach { it.clearSavedGridData() }
        grid.destroyAllConnection()
        SyncMEWirelessGrids.updateInServer(SyncMEWirelessGrids.value.also { it.remove(grid) })
        setDirty()
    }
    override fun save(tag: CompoundTag): CompoundTag {
        tag.put(
            NBT_GRID,
            ListTag().apply {
                SyncMEWirelessGrids.value.forEach {
                    add(it.serializeNBT())
                }
            },
        )
        return tag
    }
    fun load(tag: CompoundTag): MEWirelessSavedData {
        if (SyncMEWirelessGrids.side.isClient) return this
        val rawMEWirelessGrids = mutableListOf<MEWirelessConnectionGrid>()
        tag.getList(NBT_GRID, 10).forEach { compoundTag ->
            val element = MEWirelessConnectionGrid.deserializeNBT(compoundTag as CompoundTag) ?: return@forEach
            if (rawMEWirelessGrids.none { it.gridID == element.gridID }) {
                rawMEWirelessGrids.add(element)
            }
        }
        SyncMEWirelessGrids.updateInServer(rawMEWirelessGrids)
        return this
    }

    // 每个无线机器网络，处理无线连机的加入，退出，连接，刷新
    class MEWirelessConnectionGrid(var gridName: String, var gridOwnerUUid: UUID, var gridID: UUID = UUID.nameUUIDFromBytes(gridName.toByteArray())) {
        // ////////////////////////////////
        // ****** 网络元数据(保存在存档) ******//
        // //////////////////////////////
        companion object {
            val CODEC: Codec<MEWirelessConnectionGrid> = RecordCodecBuilder.create { builder ->
                builder.group(
                    Codec.STRING.fieldOf("gridName").forGetter { it.gridName },
                    UUIDUtil.CODEC.fieldOf("gridOwnerUUid").forGetter { it.gridOwnerUUid },
                    UUIDUtil.CODEC.fieldOf("gridID").forGetter { it.gridID },
                ).apply(builder, ::MEWirelessConnectionGrid)
            }
            fun deserializeNBT(p0: CompoundTag): MEWirelessConnectionGrid? = CODEC.parse(NbtOps.INSTANCE, p0)
                .resultOrPartial { it -> GTOCore.LOGGER.error(it) }
                .orElse(null)
        }
        fun serializeNBT(): CompoundTag = CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow(false) { GTOCore.LOGGER.error("MEWirelessConnectionGrid serializeNBT error") } as CompoundTag

        // ////////////////////////////////
        // ****** 运行时数据(不保存，由机器保存连接数据，并在加载的时候注册) ******//
        // //////////////////////////////
        val connectionPool = mutableListOf<MEWirelessConnectionMachine>()
        val mainMachine: MEWirelessConnectionMachine? = null // 如果有，则生成放射状网络，否则生成环形网络
        val connectionHolderPool = mutableListOf<IGridConnection>()
        fun getConnectionPair(): List<Pair<MEWirelessConnectionMachine, MEWirelessConnectionMachine>> {
            val machines = mutableListOf<MEWirelessConnectionMachine>()
            machines.addAll(connectionPool.filter { it.mainNode.node != null })

            mainMachine?.let { mainMachine ->
                machines.remove(mainMachine)
                return machines.map { mainMachine to it }
            }
            return machines.windowed(2) { it[0] to it[1] }
        }
        fun addToConnectionPool(machine: MEWirelessConnectionMachine) {
            connectionPool.add(machine)
            refreshConnectionPool()
        }
        fun removeFromConnectionPool(machine: MEWirelessConnectionMachine) {
            connectionPool.remove(machine)
            refreshConnectionPool()
        }
        fun refreshConnectionPool() {
            destroyAllConnection()
            createAllConnection()
        }

        fun destroyAllConnection() {
            connectionHolderPool.forEach { it.destroy() }
            connectionHolderPool.clear()
        }
        fun createAllConnection() {
            getConnectionPair().forEach {
                try {
                    if (!(it.first.mainNode.node?.level?.hasChunkAt(it.first.pos) ?: false)) return@forEach
                    if (!(it.second.mainNode.node?.level?.hasChunkAt(it.second.pos) ?: false)) return@forEach
                    val gridConnection = GridHelper.createConnection(it.first.mainNode.node, it.second.mainNode.node)
                    println("create connection between ${it.first.pos} and ${it.second.pos}")
                    connectionHolderPool.add(gridConnection)
                } catch (ignore: Exception) {
                }
            }
        }
    }
}

class GridSyncField(side: LogicalSide, uniqueName: Supplier<String>, value: MutableList<MEWirelessSavedData.MEWirelessConnectionGrid>, onInitCallBack: (SyncField<MutableList<MEWirelessSavedData.MEWirelessConnectionGrid>>, MutableList<MEWirelessSavedData.MEWirelessConnectionGrid>) -> Unit = { _, _ -> }, onSyncCallBack: (SyncField<MutableList<MEWirelessSavedData.MEWirelessConnectionGrid>>, MutableList<MEWirelessSavedData.MEWirelessConnectionGrid>, MutableList<MEWirelessSavedData.MEWirelessConnectionGrid>) -> Unit = { _, _, _ -> }) :
    SyncField<MutableList<MEWirelessSavedData.MEWirelessConnectionGrid>>(
        side,
        uniqueName,
        value,
        onInitCallBack,
        onSyncCallBack,
    ) {
    override fun readFromBuffer(buffer: FriendlyByteBuf): MutableList<MEWirelessSavedData.MEWirelessConnectionGrid> {
        val size = buffer.readInt()
        val list = mutableListOf<MEWirelessSavedData.MEWirelessConnectionGrid>()
        for (i in 0 until size) {
            val element = MEWirelessSavedData.MEWirelessConnectionGrid.deserializeNBT(buffer.readNbt() ?: continue)
            list.add(element ?: continue)
        }
        return list
    }

    override fun writeToBuffer(buffer: FriendlyByteBuf): FriendlyByteBuf {
        buffer.writeInt(value.size)
        for (element in value) {
            buffer.writeNbt(element.serializeNBT())
        }
        return buffer
    }
}
