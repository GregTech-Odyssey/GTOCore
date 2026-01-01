package com.gtocore.integration.ae.wireless

import com.gtocore.api.misc.WirelessNetworkTopologyManager
import com.gtocore.api.misc.codec.CodecAbleTyped
import com.gtocore.api.misc.codec.CodecAbleTypedCompanion
import com.gtocore.common.saved.WirelessSavedData
import com.gtocore.config.GTOConfig

import net.minecraft.core.BlockPos
import net.minecraft.core.UUIDUtil
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level

import appeng.api.networking.GridHelper
import appeng.api.networking.IGridConnection
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import org.jetbrains.annotations.ApiStatus

import java.util.*

class WirelessGrid(
    val name: String, // 唯一ID
    val owner: UUID,
    var isDefault: Boolean = false,
    var connectionPoolTable: MutableList<MachineInfo> = mutableListOf(),
    var nickname: String = name, // 新增：显示昵称
) : CodecAbleTyped<WirelessGrid, WirelessGrid.Companion> {

    // ///////////////////////////////
    // ****** RUN TIME ******//
    // //////////////////////////////
    val connectionPool = mutableListOf<WirelessMachine>()
    val connectionHolderPool = mutableListOf<IGridConnection>()

    private val topologyManager = WirelessNetworkTopologyManager()

    companion object : CodecAbleTypedCompanion<WirelessGrid> {
        override fun getCodec(): Codec<WirelessGrid> = RecordCodecBuilder.create { b ->
            b.group(
                Codec.STRING.fieldOf("name").forGetter { it.name },
                UUIDUtil.CODEC.fieldOf("owner").forGetter { it.owner },
                Codec.BOOL.fieldOf("isDefault").forGetter { it.isDefault },
                MachineInfo.getCodec().listOf().fieldOf("connectionPoolTable").forGetter { it.connectionPoolTable.toList() },
                Codec.STRING.optionalFieldOf("nickname").forGetter { Optional.ofNullable(it.nickname) },
            ).apply(b) { name, owner, isDefault, connectionPoolTable, nicknameOpt ->
                WirelessGrid(name, owner, isDefault, connectionPoolTable.toMutableList(), nicknameOpt.orElse(name))
            }
        }
    }

    // ///////////////////////////////
    // ****** RUN TIME ******//
    // //////////////////////////////
    class MachineInfo(var pos: BlockPos = BlockPos.ZERO, var owner: String = "", var descriptionId: String = "", var level: ResourceKey<Level> = WirelessSavedData.UNKNOWN) : CodecAbleTyped<MachineInfo, MachineInfo.Companion> {
        companion object : CodecAbleTypedCompanion<MachineInfo> {
            override fun getCodec(): Codec<MachineInfo> = RecordCodecBuilder.create { b ->
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

    @ApiStatus.Internal
    fun refreshConnectionPool() {
        connectionHolderPool.forEach { it.destroy() }
        connectionHolderPool.clear()

        if (connectionPool.isEmpty()) return

        try {
            val newConnections = topologyManager.rebuildTopology(connectionPool)
            connectionHolderPool.addAll(newConnections)

            val stats = topologyManager.getNetworkStats()
            if (GTOConfig.INSTANCE.aeLog) {
                println(
                    "Grid '$name' topology rebuilt: ${stats.totalNodes} nodes, " +
                        "${stats.totalClusters} clusters, ${stats.totalConnections} connections, " +
                        "efficiency: ${String.format("%.2f", stats.connectionEfficiency * 100)}%",
                )
            }
        } catch (e: Exception) {
            fallbackToSimpleConnections()
        }
    }
    fun getTotalUsedChannel(): Int = topologyManager.getTotalUsedChannels()
    fun addNodeToNetwork(machine: WirelessMachine) {
        if (!connectionPool.contains(machine)) {
            connectionPool.add(machine)
        }

        try {
            val newConnections = topologyManager.addNode(machine)
            connectionHolderPool.addAll(newConnections)

            if (GTOConfig.INSTANCE.aeLog) println("Added node ${machine.self().pos} to grid '$name', ${newConnections.size} new connections")
        } catch (e: Exception) {
            if (GTOConfig.INSTANCE.aeLog) println("Failed to add node to topology: ${e.message}")
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

                if (GTOConfig.INSTANCE.aeLog) println("Removed node ${machine.self().pos} from grid '$name'")
            } catch (e: Exception) {
                if (GTOConfig.INSTANCE.aeLog) println("Failed to remove node from topology: ${e.message}")
                refreshConnectionPool()
            }
        }
    }

    // 网络降级
    private fun fallbackToSimpleConnections() {
        if (GTOConfig.INSTANCE.aeLog) println("Grid '$name' topology rebuild failed, falling back to simple connections")
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
