package com.gtocore.common.network

import com.gtocore.integration.ae.WirelessMachine

import appeng.api.networking.GridHelper
import appeng.api.networking.IGridConnection

import kotlin.math.*

// 双层结构，主节点为环形连接，子节点为星形连接
class WirelessNetworkTopologyManager {

    companion object {
        private const val MAX_NODES_PER_HUB = 15
    }

    data class NodeCluster(val hubNode: WirelessMachine, val leafNodes: MutableSet<WirelessMachine> = mutableSetOf(), val connections: MutableSet<IGridConnection> = mutableSetOf()) {
        fun addLeafNode(node: WirelessMachine): Boolean = if (leafNodes.size < MAX_NODES_PER_HUB) {
            leafNodes.add(node)
            true
        } else {
            false
        }

        fun removeLeafNode(node: WirelessMachine) {
            leafNodes.remove(node)
        }

        fun getLoadFactor(): Double = leafNodes.size.toDouble() / MAX_NODES_PER_HUB
    }

    private val clusters = mutableListOf<NodeCluster>()
    private val hubConnections = mutableListOf<IGridConnection>()

    fun addNode(node: WirelessMachine): List<IGridConnection> {
        if (!isNodeValid(node)) return emptyList()

        val targetCluster = findBestClusterForNode(node)

        return if (targetCluster != null && targetCluster.addLeafNode(node)) {
            createConnection(targetCluster.hubNode, node)?.let { conn ->
                targetCluster.connections.add(conn) // ← 关键修复：添加到cluster.connections
                listOf(conn)
            } ?: emptyList()
        } else {
            rebalanceAndAddNode(node)
        }
    }
    fun removeNode(node: WirelessMachine): List<IGridConnection> {
        val affectedConnections = mutableListOf<IGridConnection>()

        val cluster = clusters.find { it.hubNode == node || it.leafNodes.contains(node) }

        cluster?.let {
            if (it.hubNode == node) {
                affectedConnections.addAll(handleHubRemoval(it))
                rebuildHubConnections()
            } else {
                it.removeLeafNode(node)
                cleanupNodeConnections(node, it)
                cleanupHubConnections(node)
            }
        }

        return affectedConnections
    }

    private fun cleanupNodeConnections(node: WirelessMachine, cluster: NodeCluster) {
        cluster.connections.removeIf { conn ->
            try {
                if (isConnectionInvolving(conn, node)) {
                    conn.destroy()
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                true
            }
        }
    }

    private fun createIntraClusterConnections(): List<IGridConnection> {
        val connections = mutableListOf<IGridConnection>()

        clusters.forEach { cluster ->
            cluster.leafNodes.forEach { leafNode ->
                createConnection(cluster.hubNode, leafNode)?.let { conn ->
                    connections.add(conn)
                    cluster.connections.add(conn)
                }
            }
        }

        return connections
    }

    private fun createInterClusterConnections(): List<IGridConnection> {
        val connections = mutableListOf<IGridConnection>()

        if (clusters.size <= 1) {
            return connections
        }

        for (i in clusters.indices) {
            val currentHub = clusters[i].hubNode
            val nextHub = clusters[(i + 1) % clusters.size].hubNode

            createConnection(currentHub, nextHub)?.let { conn ->
                connections.add(conn)
                hubConnections.add(conn)
            }
        }

        return connections
    }

    fun rebuildTopology(nodes: List<WirelessMachine>): List<IGridConnection> {
        destroyAllConnections()

        if (nodes.isEmpty()) return emptyList()
        if (nodes.size == 1) return emptyList()

        val validNodes = nodes.filter { isNodeValid(it) }

        if (validNodes.isEmpty()) return emptyList()

        buildClusters(validNodes)

        val allConnections = mutableListOf<IGridConnection>()
        allConnections.addAll(createIntraClusterConnections())
        allConnections.addAll(createInterClusterConnections())

        return allConnections
    }

    private fun cleanupHubConnections(node: WirelessMachine) {
        hubConnections.removeIf { conn ->
            try {
                if (isConnectionInvolving(conn, node)) {
                    conn.destroy()
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                true
            }
        }
    }

    private fun isConnectionInvolving(connection: IGridConnection, node: WirelessMachine): Boolean = try {
        val nodeA = connection.a()
        val nodeB = connection.b()
        val targetNode = node.mainNode.node
        nodeA == targetNode || nodeB == targetNode
    } catch (e: Exception) {
        true // 安全起见返回true
    }

    private fun rebuildHubConnections() {
        // 清理所有现有hub连接
        hubConnections.forEach {
            try {
                it.destroy()
            } catch (e: Exception) { /* ignore */ }
        }
        hubConnections.clear()

        // 重新创建hub间连接
        hubConnections.addAll(createInterClusterConnections())
    }

    fun getNetworkStats(): NetworkStats {
        val totalNodes = clusters.sumOf { it.leafNodes.size + 1 } // +1 for hub
        val totalConnections = clusters.sumOf { it.connections.size } + hubConnections.size
        val avgLoadFactor = if (clusters.isNotEmpty()) {
            clusters.map { it.getLoadFactor() }.average()
        } else {
            0.0
        }

        return NetworkStats(
            totalNodes = totalNodes,
            totalClusters = clusters.size,
            totalConnections = totalConnections,
            averageLoadFactor = avgLoadFactor,
            maxPossibleConnections = calculateMaxPossibleConnections(totalNodes),
        )
    }

    // ////////////////////////////////
    // ****** TOOLS，禁止外部���用！！！！！！！ ******//
    // //////////////////////////////
    private fun destroyAllConnections() {
        clusters.forEach { cluster ->
            cluster.connections.forEach { it.destroy() }
            cluster.connections.clear()
        }
        hubConnections.forEach { it.destroy() }
        hubConnections.clear()
        clusters.clear()
    }

    private fun buildClusters(nodes: List<WirelessMachine>) {
        val remainingNodes = nodes.toMutableList()

        while (remainingNodes.isNotEmpty()) {
            // 选择
            val hub = selectOptimalHub(remainingNodes)
            remainingNodes.remove(hub)

            val cluster = NodeCluster(hub)
            // 分配
            val nearbyNodes = findNearbyNodes(hub, remainingNodes, MAX_NODES_PER_HUB)
            nearbyNodes.forEach { node ->
                cluster.addLeafNode(node)
                remainingNodes.remove(node)
            }

            clusters.add(cluster)
        }
    }

    private fun selectOptimalHub(nodes: List<WirelessMachine>): WirelessMachine {
        if (nodes.size == 1) return nodes[0]

        // 网格中心
        val centerX = nodes.map { it.self().pos.x }.average()
        val centerZ = nodes.map { it.self().pos.z }.average()

        // 计算数字距离
        return nodes.minByOrNull { node ->
            val pos = node.self().pos
            sqrt((pos.x - centerX).pow(2) + (pos.z - centerZ).pow(2))
        } ?: nodes[0]
    }

    private fun findNearbyNodes(hub: WirelessMachine, candidates: List<WirelessMachine>, maxCount: Int): List<WirelessMachine> {
        val hubPos = hub.self().pos

        return candidates
            .map { node ->
                val distance = sqrt(
                    (node.self().pos.x - hubPos.x).toDouble().pow(2) +
                        (node.self().pos.z - hubPos.z).toDouble().pow(2),
                )
                node to distance
            }
            .sortedBy { it.second }
            .take(maxCount)
            .map { it.first }
    }

    private fun findBestClusterForNode(node: WirelessMachine): NodeCluster? {
        if (clusters.isEmpty()) return null
        // 寻找负载最低主节点
        return clusters
            .filter { it.leafNodes.size < MAX_NODES_PER_HUB }
            .minByOrNull { cluster ->
                val distance = calculateDistance(node, cluster.hubNode)
                val loadPenalty = cluster.getLoadFactor() * 1000
                distance + loadPenalty
            }
    }

    private fun rebalanceAndAddNode(node: WirelessMachine): List<IGridConnection> {
        val newCluster = NodeCluster(node)
        clusters.add(newCluster)
        hubConnections.forEach { it.destroy() }
        hubConnections.clear()
        return createInterClusterConnections()
    }

    private fun handleHubRemoval(cluster: NodeCluster): List<IGridConnection> {
        clusters.remove(cluster)
        cluster.connections.forEach { it.destroy() }
        val orphanedNodes = cluster.leafNodes.toList()
        val newConnections = mutableListOf<IGridConnection>()
        orphanedNodes.forEach { node ->
            newConnections.addAll(addNode(node))
        }
        // 主节点重连
        hubConnections.forEach { it.destroy() }
        hubConnections.clear()
        newConnections.addAll(createInterClusterConnections())

        return newConnections
    }

    private fun createConnection(node1: WirelessMachine, node2: WirelessMachine): IGridConnection? {
        return try {
            if (!isNodeValid(node1) || !isNodeValid(node2)) return null
            GridHelper.createConnection(node1.mainNode.node, node2.mainNode.node)
        } catch (e: Exception) {
            null
        }
    }

    private fun isNodeValid(node: WirelessMachine): Boolean = try {
        node.mainNode.node?.level?.hasChunkAt(node.self().pos) ?: false
    } catch (e: Exception) {
        false
    }

    private fun calculateDistance(node1: WirelessMachine, node2: WirelessMachine): Double {
        val pos1 = node1.self().pos
        val pos2 = node2.self().pos
        return sqrt((pos1.x - pos2.x).toDouble().pow(2) + (pos1.z - pos2.z).toDouble().pow(2))
    }

    private fun calculateMaxPossibleConnections(nodeCount: Int): Int = if (nodeCount <= 1) 0 else nodeCount * (nodeCount - 1) / 2
}

data class NetworkStats(val totalNodes: Int, val totalClusters: Int, val totalConnections: Int, val averageLoadFactor: Double, val maxPossibleConnections: Int) {
    val connectionEfficiency: Double
        get() = if (maxPossibleConnections > 0) totalConnections.toDouble() / maxPossibleConnections else 0.0
}
