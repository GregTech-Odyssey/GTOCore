package com.gtocore.common.saved

import com.gtocore.config.GTOConfig
import com.gtocore.integration.ae.wireless.WirelessMachine
import com.gtocore.integration.ae.wireless.WirelessNetwork

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.saveddata.SavedData

import com.gregtechceu.gtceu.GTCEu
import com.gtolib.api.capability.ISync
import com.gtolib.api.network.NetworkPack
import com.hepdd.gtmthings.utils.TeamUtil

import java.util.*

/**
 * 无线网络持久化数据。管理所有无线网络的创建、加入、退出和持久化。
 *
 * 新架构：
 * - 每个网络有源节点（SOURCE）和子节点（CHILD）
 * - 子节点连接到一个源节点
 * - 源节点最多供应 [WirelessNetwork.maxOutputsPerInput] 个子节点
 * - 源节点掉线时自动重新分配
 */
class WirelessNetworkSavedData : SavedData() {

    companion object {
        @JvmStatic
        var INSTANCE: WirelessNetworkSavedData = WirelessNetworkSavedData()
        val gridCacheSYNCER: NetworkPack = NetworkPack.registerS2C(
            "wirelessClientInstanceSyncS2C",
        ) { _: Player?, buf: FriendlyByteBuf ->
            INSTANCE.load(buf.readNbt() ?: CompoundTag())
        }

        @JvmStatic
        fun write(to: Any) {
            assert(to is ServerPlayer || to is ServerLevel || to is MinecraftServer)
            if (to is ServerLevel) {
                gridCacheSYNCER.send({ buf: FriendlyByteBuf -> buf.writeNbt(INSTANCE.save(CompoundTag())) }, to.players())
            }
            gridCacheSYNCER.send({ buf: FriendlyByteBuf -> buf.writeNbt(INSTANCE.save(CompoundTag())) }, to)
        }

        @JvmStatic
        fun initialize(tag: CompoundTag): WirelessNetworkSavedData {
            val data = WirelessNetworkSavedData()
            data.load(tag)
            return data
        }

        fun checkPermission(owner: UUID, requester: UUID): Boolean {
            if (owner == requester) return true
            val ownerTeam = TeamUtil.getTeamUUID(owner) ?: return false
            val requesterTeam = TeamUtil.getTeamUUID(requester) ?: return false
            return ownerTeam == requesterTeam
        }

        // ==================== Server API ====================

        fun accessibleNetworks(requester: UUID): List<WirelessNetwork> = INSTANCE.networkPool
            .filter { checkPermission(it.owner, requester) }
            .map { net ->
                WirelessNetwork(
                    net.id,
                    net.owner,
                    net.nickname,
                    net.nodeInfoTable.toMutableList(),
                    net.maxOutputsPerInput,
                ).also { copy ->
                    // Copy runtime info for display
                }
            }

        fun findNetworkById(id: String): WirelessNetwork? = INSTANCE.networkPool.firstOrNull { it.id == id }

        fun findNetworkOf(node: WirelessMachine): WirelessNetwork? = INSTANCE.networkPool.firstOrNull { net ->
            net.inputNodes.any { it == node } || net.outputNodes.any { it == node }
        }

        /**
         * 生成不重复的昵称。如果 base 已被占用，尝试 "base (1)", "base (2)" ...
         */
        private fun deduplicateNickname(base: String, excludeId: String? = null): String {
            if (!isNicknameTaken(base, excludeId)) return base
            var i = 1
            while (true) {
                val candidate = "$base ($i)"
                if (!isNicknameTaken(candidate, excludeId)) return candidate
                i++
            }
        }

        private fun isNicknameTaken(nickname: String, excludeId: String? = null): Boolean = INSTANCE.networkPool.any { it.nickname == nickname && (excludeId == null || it.id != excludeId) }

        fun createNetwork(name: String, requester: UUID): WirelessNetwork? {
            val nick = name.trim()
            if (nick.isBlank()) return null
            if (isNicknameTaken(nick)) return null
            val id = UUID.randomUUID().toString()
            val net = WirelessNetwork(id, requester, nick)
            INSTANCE.networkPool.add(net)
            INSTANCE.setDirty()
            return net
        }

        fun removeNetwork(networkId: String, requester: UUID): STATUS {
            val net = INSTANCE.networkPool.firstOrNull { it.id == networkId }
                ?: return STATUS.NOT_FOUND_GRID
            if (!checkPermission(net.owner, requester)) return STATUS.NOT_PERMISSION
            // Notify all loaded nodes and clear their persisted connection ID.
            // Nodes in unloaded chunks will self-correct via linkNetwork() → NOT_FOUND_GRID on reload.
            val allNodes = net.inputNodes.toList() + net.outputNodes.toList()
            for (node in allNodes) {
                node.removedFromNetwork(net.id)
                node.setConnectedNetworkId("")
            }
            net.inputNodes.clear()
            net.outputNodes.clear()
            net.refreshConnections()
            INSTANCE.defaultMap.entries.removeIf { it.value == net.id }
            INSTANCE.networkPool.remove(net)
            INSTANCE.setDirty()
            return STATUS.SUCCESS
        }

        fun joinNetwork(networkId: String, node: WirelessMachine, requester: UUID): STATUS {
            val net = INSTANCE.networkPool.firstOrNull { it.id == networkId }
                ?: return STATUS.NOT_FOUND_GRID
            if (!checkPermission(net.owner, requester)) return STATUS.NOT_PERMISSION
            // Check if already joined
            val alreadyIn = when (node.nodeType) {
                WirelessMachine.NodeType.SOURCE -> net.inputNodes.any { it == node }
                WirelessMachine.NodeType.CHILD -> net.outputNodes.any { it == node }
            }
            if (alreadyIn) return STATUS.ALREADY_JOINT

            // Leave current network first
            leaveNetwork(node)

            net.addNode(node)
            node.addedToNetwork(networkId)
            INSTANCE.setDirty()
            return STATUS.SUCCESS
        }

        fun leaveNetwork(node: WirelessMachine) {
            findNetworkOf(node)?.let { net ->
                node.removedFromNetwork(net.id)
                net.removeNode(node)
                INSTANCE.setDirty()
            }
        }

        fun setDefault(networkId: String, requester: UUID) {
            INSTANCE.defaultMap[requester] = networkId
            INSTANCE.setDirty()
        }

        fun cancelDefault(networkId: String, requester: UUID) {
            INSTANCE.defaultMap.remove(requester)
            INSTANCE.setDirty()
        }

        fun isDefault(networkId: String, requester: UUID): Boolean = INSTANCE.defaultMap[requester] == networkId

        /**
         * 获取指定玩家的收藏（默认）网络ID，如果没有收藏则返回null。
         */
        fun getDefaultNetworkId(requester: UUID): String? = INSTANCE.defaultMap[requester]

        fun renameNetwork(networkId: String, requester: UUID, nickname: String): STATUS {
            val net = INSTANCE.networkPool.firstOrNull { it.id == networkId }
                ?: return STATUS.NOT_FOUND_GRID
            if (!checkPermission(net.owner, requester)) return STATUS.NOT_PERMISSION
            val nick = nickname.trim()
            val target = nick.ifBlank { net.id }
            if (target == net.nickname) return STATUS.SUCCESS
            if (isNicknameTaken(target, excludeId = networkId)) return STATUS.SUCCESS
            net.nickname = target
            INSTANCE.setDirty()
            return STATUS.SUCCESS
        }

        /**
         * 设置网络的源节点最大子节点连接数。
         */
        fun setMaxOutputsPerInput(networkId: String, requester: UUID, maxOutputs: Int): STATUS {
            val net = INSTANCE.networkPool.firstOrNull { it.id == networkId }
                ?: return STATUS.NOT_FOUND_GRID
            if (!checkPermission(net.owner, requester)) return STATUS.NOT_PERMISSION
            val clamped = maxOutputs.coerceIn(1, 990000)
            if (net.maxOutputsPerInput != clamped) {
                net.maxOutputsPerInput = clamped
                net.refreshConnections()
                INSTANCE.setDirty()
            }
            return STATUS.SUCCESS
        }

        /**
         * 获取指定网络的摘要信息列表供GUI同步显示。
         */
        @JvmOverloads
        fun getNetworkSummaries(requester: UUID, connectedId: String = ""): List<NetworkSummary> = INSTANCE.networkPool
            .filter { checkPermission(it.owner, requester) }
            .map { net ->
                NetworkSummary(
                    id = net.id,
                    nickname = net.nickname,
                    isDefault = INSTANCE.defaultMap[requester] == net.id,
                    inputCount = net.getInputCount(),
                    outputCount = net.getOutputCount(),
                    capacity = net.getTotalCapacity(),
                    unassignedCount = net.getUnassignedOutputCount(),
                    isConnected = net.id == connectedId,
                )
            }

        /**
         * 获取所有可访问网络的拓扑信息，供拓扑TAB同步显示。
         */
        fun getTopologySummaries(requester: UUID): List<TopologySummary> = INSTANCE.networkPool
            .filter { checkPermission(it.owner, requester) }
            .map { net ->
                // Build inverse map: source → list of children
                val inputToOutputs = mutableMapOf<WirelessMachine, MutableList<WirelessMachine>>()
                for (input in net.inputNodes) {
                    inputToOutputs.getOrPut(input) { mutableListOf() }
                }
                for ((output, input) in net.assignments) {
                    inputToOutputs.getOrPut(input) { mutableListOf() }.add(output)
                }

                val sources = inputToOutputs.map { (input, outputs) ->
                    TopologySourceEntry(
                        source = machineToNodeEntry(input),
                        children = outputs.map { machineToNodeEntry(it) },
                    )
                }

                // Find unassigned children
                val assignedOutputs = net.assignments.keys
                val unassigned = net.outputNodes
                    .filter { it !in assignedOutputs }
                    .map { machineToNodeEntry(it) }

                TopologySummary(
                    networkId = net.id,
                    networkNickname = net.nickname,
                    sources = sources,
                    unassigned = unassigned,
                    maxOutputsPerInput = net.maxOutputsPerInput,
                )
            }

        private fun machineToNodeEntry(machine: WirelessMachine): TopologyNodeEntry {
            val pos = machine.self().pos
            val dimId = try {
                machine.self().level?.dimension()?.location()?.toString() ?: "?"
            } catch (_: Exception) {
                "?"
            }
            val name = try {
                machine.self().blockState.block.descriptionId
            } catch (_: Exception) {
                "?"
            }
            return TopologyNodeEntry(pos.x, pos.y, pos.z, dimId, name)
        }
    }

    val networkPool: MutableList<WirelessNetwork> = mutableListOf()
    val defaultMap = mutableMapOf<UUID, String>()

    override fun save(tag: CompoundTag): CompoundTag {
        tag.put(
            "networks",
            ListTag().apply {
                if (GTOConfig.INSTANCE.devMode.aeLog) {
                    println("${GTCEu.isClientSide()} Saving WirelessNetworkSavedData with ${networkPool.size} networks")
                }
                for (net in networkPool) {
                    add(net.encodeToNbt())
                }
            },
        )
        tag.put(
            "defaultMap",
            ListTag().apply {
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
        return tag
    }

    private fun load(tag: CompoundTag) {
        networkPool.clear()
        defaultMap.clear()

        // ==================== Migration: old "WirelessSavedData" format ====================
        // TODO: 数据迁移 — 后续版本删除此迁移代码块（从此行到 "END Migration" 注释之间的所有内容）。
        // Old format: flat WirelessGrid with connectionPoolTable (all nodes equal, no SOURCE/CHILD distinction).
        // Migration: create new WirelessNetwork per old grid, all old nodes become CHILD nodes.
        // Duplicate nicknames get "(1)", "(2)" suffix via deduplicateNickname().
        //
        // Old WirelessGrid Codec fields:
        //   name: STRING (UUID-style unique ID)
        //   owner: UUIDUtil.CODEC (IntArray-encoded UUID)
        //   isDefault: BOOL
        //   connectionPoolTable: List<MachineInfo>  (field name differs from new "nodes"!)
        //   nickname: Optional<STRING>
        // Old MachineInfo Codec fields:
        //   pos: BlockPos (optional, default ZERO)
        //   owner: STRING (optional, default "")
        //   descriptionId: STRING (optional, default "")
        //   level: ResourceKey<Level> (optional, default UNKNOWN)
        //   (NO nodeType field!)
        //
        // New WirelessNetwork Codec fields:
        //   id: STRING, owner: UUIDUtil.CODEC, nickname: Optional<STRING>,
        //   nodes: List<NodeInfo>, maxOutputsPerInput: Optional<INT>
        // New NodeInfo has nodeType field.
        //
        // CANNOT use WirelessNetwork.decodeFromNbt() on old data — field names differ!
        // Must manually parse old NBT and construct new objects.
        if (tag.contains("WirelessSavedData") && !tag.contains("networks")) {
            val oldList = tag.getList("WirelessSavedData", 10)
            if (GTOConfig.INSTANCE.devMode.aeLog) {
                println("Migrating ${oldList.size} old WirelessGrid entries to new WirelessNetwork format")
            }

            for (entry in oldList) {
                val oldGrid = entry as CompoundTag
                val name = oldGrid.getString("name")

                // Parse owner UUID — old format uses UUIDUtil.CODEC (IntArray)
                val owner = try {
                    if (oldGrid.contains("owner")) {
                        net.minecraft.core.UUIDUtil.CODEC
                            .decode(net.minecraft.nbt.NbtOps.INSTANCE, oldGrid.get("owner"))
                            .map { it.first }
                            .result()
                            .orElse(UUID.randomUUID())
                    } else {
                        UUID.randomUUID()
                    }
                } catch (_: Exception) {
                    UUID.randomUUID()
                }

                // Parse nickname
                val rawNickname = if (oldGrid.contains("nickname")) {
                    // nickname is Optional<STRING> — stored as StringTag directly when present
                    oldGrid.getString("nickname")
                } else {
                    name
                }

                // Deduplicate nickname against already-migrated networks
                val finalNickname = deduplicateNickname(rawNickname.ifBlank { name })

                // Manually parse old connectionPoolTable → convert each MachineInfo to NodeInfo (CHILD)
                val migratedNodes = mutableListOf<WirelessNetwork.NodeInfo>()
                if (oldGrid.contains("connectionPoolTable")) {
                    val oldNodes = oldGrid.getList("connectionPoolTable", 10)
                    for (nodeTag in oldNodes) {
                        try {
                            val nodeNbt = nodeTag as CompoundTag
                            // Parse old MachineInfo fields
                            val pos = if (nodeNbt.contains("pos")) {
                                net.minecraft.core.BlockPos.CODEC
                                    .decode(net.minecraft.nbt.NbtOps.INSTANCE, nodeNbt.get("pos"))
                                    .map { it.first }
                                    .result()
                                    .orElse(net.minecraft.core.BlockPos.ZERO)
                            } else {
                                net.minecraft.core.BlockPos.ZERO
                            }
                            val nodeOwner = if (nodeNbt.contains("owner")) nodeNbt.getString("owner") else ""
                            val descId = if (nodeNbt.contains("descriptionId")) nodeNbt.getString("descriptionId") else ""
                            val level = if (nodeNbt.contains("level")) {
                                try {
                                    net.minecraft.resources.ResourceKey.codec(net.minecraft.core.registries.Registries.DIMENSION)
                                        .decode(net.minecraft.nbt.NbtOps.INSTANCE, nodeNbt.get("level"))
                                        .map { it.first }
                                        .result()
                                        .orElse(WirelessNetwork.UNKNOWN)
                                } catch (_: Exception) {
                                    WirelessNetwork.UNKNOWN
                                }
                            } else {
                                WirelessNetwork.UNKNOWN
                            }
                            migratedNodes.add(
                                WirelessNetwork.NodeInfo(
                                    pos = pos,
                                    level = level,
                                    owner = nodeOwner,
                                    descriptionId = descId,
                                    nodeType = "CHILD", // All old nodes become CHILD
                                ),
                            )
                        } catch (e: Exception) {
                            if (GTOConfig.INSTANCE.devMode.aeLog) {
                                println("Failed to parse old MachineInfo: ${e.message}")
                            }
                        }
                    }
                }

                val net = WirelessNetwork(
                    id = name,
                    owner = owner,
                    nickname = finalNickname,
                    nodeInfoTable = migratedNodes,
                )
                if (networkPool.none { it.id == net.id }) {
                    networkPool.add(net)
                }

                // Migrate isDefault — old format stored per-grid, we map owner → gridId
                if (oldGrid.getBoolean("isDefault")) {
                    defaultMap[owner] = name
                }
            }

            if (GTOConfig.INSTANCE.devMode.aeLog) {
                println("Migration complete: ${networkPool.size} networks created")
            }
            setDirty() // Save in new format
            // ==================== END Migration ====================
        } else {
            // ==================== Normal load: new format ====================
            val list = tag.getList("networks", 10)
            for (nbt in list) {
                val net = WirelessNetwork.decodeFromNbt(nbt as CompoundTag)
                if (networkPool.none { it.id == net.id }) {
                    networkPool.add(net)
                }
            }
        }

        val defaultList = tag.getList("defaultMap", 10)
        for (entry in defaultList) {
            val nbt = entry as CompoundTag
            defaultMap[nbt.getUUID("key")] = nbt.getString("value")
        }
        // Clear runtime connection tables on load (will rebuild when nodes load)
        networkPool.forEach { it.nodeInfoTable.clear() }
        if (GTOConfig.INSTANCE.devMode.aeLog) {
            println("${GTCEu.isClientSide()} Loaded WirelessNetworkSavedData with ${networkPool.size} networks")
        }
    }
}

/**
 * 网络摘要信息，用于同步到客户端GUI显示。
 */
data class NetworkSummary(val id: String, val nickname: String, val isDefault: Boolean, val inputCount: Int, val outputCount: Int, val capacity: Int, val unassignedCount: Int, val isConnected: Boolean = false)

/**
 * 拓扑中的节点信息。
 */
data class TopologyNodeEntry(val x: Int, val y: Int, val z: Int, val dim: String, val name: String)

/**
 * 一个源节点及其连接的子节点列表。
 */
data class TopologySourceEntry(val source: TopologyNodeEntry, val children: List<TopologyNodeEntry>)

/**
 * 一个网络的完整拓扑信息。
 */
data class TopologySummary(val networkId: String, val networkNickname: String, val sources: List<TopologySourceEntry>, val unassigned: List<TopologyNodeEntry>, val maxOutputsPerInput: Int = 32)

enum class STATUS {
    SUCCESS,
    ALREADY_JOINT,
    NOT_FOUND_GRID,
    NOT_PERMISSION,
}

/**
 * 创建用于同步网络摘要列表的 ISync.ObjectSyncedField。
 */
fun createNetworkSummarySyncField(sync: ISync): ISync.ObjectSyncedField<List<NetworkSummary>> = ISync.createObjectField(
    sync,
    { buf ->
        val size = buf.readInt()
        List(size) {
            NetworkSummary(
                id = buf.readUtf(),
                nickname = buf.readUtf(),
                isDefault = buf.readBoolean(),
                inputCount = buf.readInt(),
                outputCount = buf.readInt(),
                capacity = buf.readInt(),
                unassignedCount = buf.readInt(),
                isConnected = buf.readBoolean(),
            )
        }
    },
    { buf, list ->
        buf.writeInt(list.size)
        for (s in list) {
            buf.writeUtf(s.id)
            buf.writeUtf(s.nickname)
            buf.writeBoolean(s.isDefault)
            buf.writeInt(s.inputCount)
            buf.writeInt(s.outputCount)
            buf.writeInt(s.capacity)
            buf.writeInt(s.unassignedCount)
            buf.writeBoolean(s.isConnected)
        }
    },
)

/**
 * 创建用于同步拓扑信息列表的 ISync.ObjectSyncedField。
 */
fun createTopologySyncField(sync: ISync): ISync.ObjectSyncedField<List<TopologySummary>> = ISync.createObjectField(
    sync,
    { buf ->
        val netCount = buf.readInt()
        List(netCount) {
            val networkId = buf.readUtf()
            val nickname = buf.readUtf()
            val maxOutputs = buf.readInt()
            val sourceCount = buf.readInt()
            val sources = List(sourceCount) {
                val src = readNodeEntry(buf)
                val childCount = buf.readInt()
                val children = List(childCount) { readNodeEntry(buf) }
                TopologySourceEntry(src, children)
            }
            val unassignedCount = buf.readInt()
            val unassigned = List(unassignedCount) { readNodeEntry(buf) }
            TopologySummary(networkId, nickname, sources, unassigned, maxOutputs)
        }
    },
    { buf, list ->
        buf.writeInt(list.size)
        for (topo in list) {
            buf.writeUtf(topo.networkId)
            buf.writeUtf(topo.networkNickname)
            buf.writeInt(topo.maxOutputsPerInput)
            buf.writeInt(topo.sources.size)
            for (src in topo.sources) {
                writeNodeEntry(buf, src.source)
                buf.writeInt(src.children.size)
                for (child in src.children) {
                    writeNodeEntry(buf, child)
                }
            }
            buf.writeInt(topo.unassigned.size)
            for (u in topo.unassigned) {
                writeNodeEntry(buf, u)
            }
        }
    },
)

private fun readNodeEntry(buf: net.minecraft.network.FriendlyByteBuf): TopologyNodeEntry = TopologyNodeEntry(buf.readInt(), buf.readInt(), buf.readInt(), buf.readUtf(), buf.readUtf())

private fun writeNodeEntry(buf: net.minecraft.network.FriendlyByteBuf, entry: TopologyNodeEntry) {
    buf.writeInt(entry.x)
    buf.writeInt(entry.y)
    buf.writeInt(entry.z)
    buf.writeUtf(entry.dim)
    buf.writeUtf(entry.name)
}
