package com.gtocore.integration.ae.wireless

import com.gtocore.api.misc.codec.CodecAbleTyped
import com.gtocore.api.misc.codec.CodecAbleTypedCompanion
import com.gtocore.config.Difficulty
import com.gtocore.config.GTOConfig

import net.minecraft.core.BlockPos
import net.minecraft.core.UUIDUtil
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level

import appeng.api.networking.GridHelper
import appeng.api.networking.IGridConnection
import com.gregtechceu.gtceu.GTCEu
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

import java.util.*

/**
 * 无线网络。每个网络有源节点（提供AE网络）和子节点（使用AE网络）。
 * 子节点连接到一个源节点，源节点最多供应 [maxOutputsPerInput] 个子节点。
 */
class WirelessNetwork(val id: String, val owner: UUID, var nickname: String = id, val nodeInfoTable: MutableList<NodeInfo> = mutableListOf(), var maxOutputsPerInput: Int = defaultMaxOutputs()) : CodecAbleTyped<WirelessNetwork, WirelessNetwork.Companion> {

    companion object : CodecAbleTypedCompanion<WirelessNetwork> {
        /** 根据游戏难度计算默认最大子节点连接数。 */
        fun defaultMaxOutputs(): Int = if (GTOConfig.INSTANCE.gamePlay.difficulty == Difficulty.Expert) 32 else 990000

        val UNKNOWN: ResourceKey<Level> = ResourceKey.create(Registries.DIMENSION, GTCEu.id("unknown"))

        override fun getCodec(): Codec<WirelessNetwork> = RecordCodecBuilder.create { b ->
            b.group(
                Codec.STRING.fieldOf("id").forGetter { it.id },
                UUIDUtil.CODEC.fieldOf("owner").forGetter { it.owner },
                Codec.STRING.optionalFieldOf("nickname").forGetter { Optional.ofNullable(it.nickname) },
                NodeInfo.getCodec().listOf().fieldOf("nodes").forGetter { it.nodeInfoTable.toList() },
                Codec.INT.optionalFieldOf("maxOutputsPerInput").forGetter { Optional.of(it.maxOutputsPerInput) },
            ).apply(b) { id, owner, nicknameOpt, nodes, maxOpt ->
                WirelessNetwork(id, owner, nicknameOpt.orElse(id), nodes.toMutableList(), maxOpt.orElse(defaultMaxOutputs()))
            }
        }
    }

    // ==================== Runtime state (not persisted) ====================
    val inputNodes = mutableListOf<WirelessMachine>()
    val outputNodes = mutableListOf<WirelessMachine>()
    val assignments = mutableMapOf<WirelessMachine, WirelessMachine>() // output -> input
    val connections = mutableListOf<IGridConnection>()

    // ==================== Persisted node info ====================
    class NodeInfo(var pos: BlockPos = BlockPos.ZERO, var level: ResourceKey<Level> = UNKNOWN, var owner: String = "", var descriptionId: String = "", var nodeType: String = "CHILD") : CodecAbleTyped<NodeInfo, NodeInfo.Companion> {
        companion object : CodecAbleTypedCompanion<NodeInfo> {
            override fun getCodec(): Codec<NodeInfo> = RecordCodecBuilder.create { b ->
                b.group(
                    BlockPos.CODEC.optionalFieldOf("pos", BlockPos.ZERO).forGetter { it.pos },
                    ResourceKey.codec(Registries.DIMENSION).optionalFieldOf("level", UNKNOWN).forGetter { it.level },
                    Codec.STRING.optionalFieldOf("owner", "").forGetter { it.owner },
                    Codec.STRING.optionalFieldOf("descriptionId", "").forGetter { it.descriptionId },
                    Codec.STRING.optionalFieldOf("nodeType", "CHILD").forGetter { normalizeNodeType(it.nodeType) },
                ).apply(b, ::NodeInfo)
            }

            /** Normalize old OUTPUT/INPUT names to new SOURCE/CHILD names */
            private fun normalizeNodeType(raw: String): String = when (raw) {
                "INPUT" -> "SOURCE"
                "OUTPUT" -> "CHILD"
                else -> raw
            }
        }
    }

    // ==================== Node Management ====================

    fun addNode(node: WirelessMachine) {
        when (node.nodeType) {
            WirelessMachine.NodeType.SOURCE -> if (!inputNodes.contains(node)) inputNodes.add(node)
            WirelessMachine.NodeType.CHILD -> if (!outputNodes.contains(node)) outputNodes.add(node)
        }
        nodeInfoTable.add(
            NodeInfo(
                pos = node.self().pos,
                level = node.self().level?.dimension() ?: UNKNOWN,
                owner = node.self().playerOwner?.name ?: "unknown",
                descriptionId = node.self().blockState.block.descriptionId,
                nodeType = node.nodeType.name,
            ),
        )
        refreshConnections()
    }

    fun removeNode(node: WirelessMachine) {
        inputNodes.remove(node)
        outputNodes.remove(node)
        // Remove persisted info
        val levelKey = node.self().level?.dimension() ?: UNKNOWN
        nodeInfoTable.removeAll { it.pos == node.self().pos && it.level == levelKey }
        refreshConnections()
    }

    /**
     * 重建所有连接。将每个子节点分配给负载最低的源节点。
     */
    fun refreshConnections() {
        // Destroy existing
        connections.forEach {
            try {
                it.destroy()
            } catch (_: Exception) {
            }
        }
        connections.clear()
        assignments.clear()

        if (inputNodes.isEmpty() || outputNodes.isEmpty()) return

        val validInputs = inputNodes.filter { isNodeValid(it) }
        if (validInputs.isEmpty()) return

        // Assign each child to the least-loaded valid source
        for (output in outputNodes) {
            if (!isNodeValid(output)) continue
            val bestInput = validInputs
                .minByOrNull { input -> assignments.count { it.value == input } }
                ?: continue
            if (assignments.count { it.value == bestInput } >= maxOutputsPerInput) continue
            assignments[output] = bestInput
        }

        // Create AE grid connections
        for ((output, input) in assignments) {
            try {
                val conn = GridHelper.createConnection(output.mainNode.node, input.mainNode.node)
                connections.add(conn)
                if (GTOConfig.INSTANCE.devMode.aeLog) {
                    println("WirelessNetwork '$nickname': connected child ${output.self().pos} -> source ${input.self().pos}")
                }
            } catch (e: Exception) {
                if (GTOConfig.INSTANCE.devMode.aeLog) {
                    println("WirelessNetwork '$nickname': failed to connect ${output.self().pos} -> ${input.self().pos}: ${e.message}")
                }
            }
        }

        if (GTOConfig.INSTANCE.devMode.aeLog) {
            println(
                "WirelessNetwork '$nickname': ${inputNodes.size} sources, ${outputNodes.size} children, " +
                    "${assignments.size} assigned, ${getUnassignedOutputCount()} unassigned",
            )
        }
    }

    /**
     * 当某个源节点掉线时调用。尝试将其子节点重新分配到其他源节点。
     */
    fun handleInputOffline(offlineInput: WirelessMachine) {
        if (!inputNodes.contains(offlineInput)) return
        refreshConnections()
    }

    fun getUnassignedOutputCount(): Int {
        val validOutputs = outputNodes.count { isNodeValid(it) }
        return validOutputs - assignments.size
    }

    fun getInputCount(): Int = inputNodes.size
    fun getOutputCount(): Int = outputNodes.size
    fun getTotalCapacity(): Int = inputNodes.count { isNodeValid(it) } * maxOutputsPerInput

    private fun isNodeValid(node: WirelessMachine): Boolean = try {
        node.mainNode.node?.level?.hasChunkAt(node.self().pos) ?: false
    } catch (_: Exception) {
        false
    }
}
