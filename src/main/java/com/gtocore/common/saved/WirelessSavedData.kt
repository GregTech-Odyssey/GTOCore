package com.gtocore.common.saved

import com.gtocore.common.network.SyncField
import com.gtocore.common.network.WirelessNetworkTopologyManager
import com.gtocore.integration.ae.WirelessMachine

import net.minecraft.core.BlockPos
import net.minecraft.core.UUIDUtil
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtOps
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import net.minecraft.world.level.saveddata.SavedData
import net.minecraftforge.fml.LogicalSide

import appeng.api.networking.GridHelper
import appeng.api.networking.IGridConnection
import com.gregtechceu.gtceu.GTCEu
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

import java.util.UUID
import java.util.function.Supplier

class WirelessSavedData : SavedData() {

    companion object {
        var INSTANCE: WirelessSavedData = WirelessSavedData()

        val UNKNOWN: ResourceKey<Level> = ResourceKey.create(Registries.DIMENSION, GTCEu.id("unknown"))

        @JvmStatic
        fun initialize(p0: CompoundTag): WirelessSavedData {
            val data = WirelessSavedData()
            data.load(p0)
            return data
        }

        // ////////////////////////////////
        // ****** SERVER API ******//
        // //////////////////////////////

        fun joinToGrid(gridName: String, machine: WirelessMachine, requester: UUID): STATUS {
            leaveGrid(machine)
            val grid = INSTANCE.gridPool.firstOrNull { it.name == gridName } ?: return STATUS.NOT_FOUND_GRID
            if (grid.owner != requester) return STATUS.NOT_PERMISSION
            if (grid.connectionPool.any { it == machine }) return STATUS.ALREADY_JOINT

            grid.connectionPoolTable.add(
                WirelessGrid.MachineInfo().apply {
                    level = machine.self().level?.dimension() ?: UNKNOWN
                    pos = machine.self().pos
                    owner = machine.self().playerOwner?.name ?: "unknown"
                    descriptionId = machine.self().blockState.block.descriptionId
                },
            )
            machine.addedToGrid(gridName) // 回调

            grid.addNodeToNetwork(machine)

            return STATUS.SUCCESS
        }

        fun leaveGrid(machine: WirelessMachine) {
            INSTANCE.gridPool.find { it -> it.connectionPool.any { it == machine } }
                ?.let { grid ->
                    machine.removedFromGrid(grid.name)
                    grid.connectionPoolTable.removeAll { it.pos == machine.self().pos }
                    grid.removeNodeFromNetwork(machine)
                }
        }
        fun createNewGrid(gridName: String, requester: UUID): WirelessGrid? {
            INSTANCE.gridPool.find { grid -> grid.name == gridName }?.let { return null }
            val newGrid = WirelessGrid(gridName, requester)
            INSTANCE.gridPool.add(newGrid)
            INSTANCE.setDirty()
            return newGrid
        }
        fun removeGrid(gridName: String, requester: UUID): STATUS {
            INSTANCE.gridPool.find { it.name == gridName }?.let { removed ->
                if (removed.owner != requester) return STATUS.NOT_PERMISSION
                removed.connectionPool.forEach { it.removedFromGrid(removed.name) }
                removed.connectionPool.clear()
                removed.refreshConnectionPool()
                INSTANCE.gridPool.remove(removed)
                INSTANCE.setDirty()
                return STATUS.SUCCESS
            }
            INSTANCE.setDirty()
            return STATUS.NOT_FOUND_GRID
        }
        fun setAsDefault(gridName: String, requester: UUID) {
            INSTANCE.defaultMap[requester] = gridName
            INSTANCE.gridPool.find { it.name == gridName && it.owner == requester }?.let { it.isDefault = true }
            INSTANCE.gridPool.filter { it.name != gridName && it.owner == requester }.forEach { it.isDefault = false }
            INSTANCE.setDirty()
        }
        fun cancelAsDefault(gridName: String, requester: UUID) {
            INSTANCE.defaultMap.remove(requester)
            INSTANCE.gridPool.find { it.name == gridName && it.owner == requester }?.let { it.isDefault = false }
            INSTANCE.setDirty()
        }
    }

    val gridPool: MutableList<WirelessGrid> = mutableListOf()
    val defaultMap = mutableMapOf<UUID, String>()

    // ///////////////////////////////
    // ****** SavedData To SyncField ******//
    // //////////////////////////////
    override fun save(p0: CompoundTag): CompoundTag {
        p0.put(
            "WirelessSavedData",
            ListTag().apply {
                println("${GTCEu.isClientSide()} Saving WirelessSavedData with ${gridPool.size} grids")
                for (grid in gridPool) {
                    add(grid.serializer())
                }
            },
        )
        p0.put(
            "defaultMap",
            ListTag().apply {
                println("${GTCEu.isClientSide()} Saving defaultMap with ${defaultMap.size} entries")
                for ((key, value) in defaultMap) {
                    add(
                        CompoundTag().apply {
                            putUUID("key", key)
                            putString("value", value)
                        },
                    )
                }
            },
        )
        return p0
    }

    private fun load(p0: CompoundTag): WirelessSavedData {
        gridPool.clear()
        defaultMap.clear()
        val res = mutableListOf<WirelessGrid>()
        val list = p0.getList("WirelessSavedData", 10)
        for (tag in list) {
            res.add(WirelessGrid.deserializer(tag as CompoundTag).takeIf { n -> gridPool.none { it.name == n?.name } } ?: continue)
        }
        println("${GTCEu.isClientSide()} Loading WirelessSavedData with ${res.size} grids")
        gridPool.addAll(res)
        val defaultList = p0.getList("defaultMap", 10)
        for (tag in defaultList) {
            val nbt = tag as CompoundTag
            defaultMap[nbt.getUUID("key")] = nbt.getString("value")
        }
        gridPool.forEach { grid ->
            defaultMap.forEach { if (grid.owner == it.key && grid.name == it.value) grid.isDefault = true }
            grid.connectionPoolTable.clear()
        }
        return this
    }
}
enum class STATUS {
    SUCCESS,
    ALREADY_JOINT,
    NOT_FOUND_GRID,
    NOT_PERMISSION,
}
class WirelessGrid(val name: String, val owner: UUID, var isDefault: Boolean = false, var connectionPoolTable: MutableList<MachineInfo> = mutableListOf()) {

    // ///////////////////////////////
    // ****** RUN TIME ******//
    // //////////////////////////////
    val connectionPool = mutableListOf<WirelessMachine>()
    val connectionHolderPool = mutableListOf<IGridConnection>()

    private val topologyManager = WirelessNetworkTopologyManager()

    companion object {
        val CODEC: Codec<WirelessGrid> = RecordCodecBuilder.create { b ->
            b.group(
                Codec.STRING.fieldOf("name").forGetter { it.name },
                UUIDUtil.CODEC.fieldOf("owner").forGetter { it.owner },
                Codec.BOOL.fieldOf("isDefault").forGetter { it.isDefault },
                MachineInfo.CODEC.listOf().fieldOf("connectionPoolTable").forGetter { it.connectionPoolTable.toList() },
            ).apply(b) { name, owner, isDefault, connectionPoolTable ->
                WirelessGrid(name, owner, isDefault, connectionPoolTable.toMutableList())
            }
        }
        fun deserializer(nbt: CompoundTag): WirelessGrid? = CODEC.parse(NbtOps.INSTANCE, nbt).resultOrPartial({ println("[WirelessGrid] deserializer error $it") }).orElse(null)
    }
    fun serializer(): CompoundTag = CODEC.encodeStart(NbtOps.INSTANCE, this).resultOrPartial({ println("[WirelessGrid] serializer error $it") }).orElse(CompoundTag()) as CompoundTag

    // ///////////////////////////////
    // ****** RUN TIME ******//
    // //////////////////////////////
    class MachineInfo(var pos: BlockPos = BlockPos.ZERO, var owner: String = "", var descriptionId: String = "", var level: ResourceKey<Level> = WirelessSavedData.UNKNOWN) {
        companion object {
            val CODEC: Codec<MachineInfo> = RecordCodecBuilder.create { b ->
                b.group(
                    BlockPos.CODEC.optionalFieldOf("pos", BlockPos.ZERO).forGetter { it.pos },
                    Codec.STRING.optionalFieldOf("owner", "").forGetter { it.owner },
                    Codec.STRING.optionalFieldOf("descriptionId", "").forGetter { it.descriptionId },
                    ResourceKey.codec(Registries.DIMENSION).optionalFieldOf("level", WirelessSavedData.UNKNOWN).forGetter { it.level },
                ).apply(b, ::MachineInfo)
            }
        }
    }

    // ////////////////////////////////
    // ****** 环形总线拓扑连接池API ！！内部API ******//
    // //////////////////////////////

    fun refreshConnectionPool() {
        connectionHolderPool.forEach { it.destroy() }
        connectionHolderPool.clear()

        if (connectionPool.isEmpty()) return

        try {
            val newConnections = topologyManager.rebuildTopology(connectionPool)
            connectionHolderPool.addAll(newConnections)

            val stats = topologyManager.getNetworkStats()
            println(
                "Grid '$name' topology rebuilt: ${stats.totalNodes} nodes, " +
                    "${stats.totalClusters} clusters, ${stats.totalConnections} connections, " +
                    "efficiency: ${String.format("%.2f", stats.connectionEfficiency * 100)}%",
            )
        } catch (e: Exception) {
            fallbackToSimpleConnections()
        }
    }
    fun addNodeToNetwork(machine: WirelessMachine) {
        if (!connectionPool.contains(machine)) {
            connectionPool.add(machine)
        }

        try {
            val newConnections = topologyManager.addNode(machine)
            connectionHolderPool.addAll(newConnections)

            println("Added node ${machine.self().pos} to grid '$name', ${newConnections.size} new connections")
        } catch (e: Exception) {
            println("Failed to add node to topology: ${e.message}")
            refreshConnectionPool()
        }
    }
    fun removeNodeFromNetwork(machine: WirelessMachine) {
        if (connectionPool.remove(machine)) {
            try {
                val affectedConnections = topologyManager.removeNode(machine)
                connectionHolderPool.removeAll { connection ->
                    affectedConnections.contains(connection)
                }

                println("Removed node ${machine.self().pos} from grid '$name'")
            } catch (e: Exception) {
                println("Failed to remove node from topology: ${e.message}")
                refreshConnectionPool()
            }
        }
    }

    // 网络降级
    private fun fallbackToSimpleConnections() {
        println("Grid '$name' topology rebuild failed, falling back to simple connections")
        connectionPool.windowed(2).forEach { windowedNodes ->
            try {
                val first = windowedNodes[0]
                val second = windowedNodes[1]

                if (!(first.mainNode.node?.level?.hasChunkAt(first.self().pos) ?: false)) return@forEach
                if (!(second.mainNode.node?.level?.hasChunkAt(second.self().pos) ?: false)) return@forEach

                val gridConnection = GridHelper.createConnection(first.mainNode.node, second.mainNode.node)
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
