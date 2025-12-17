package com.gtocore.common.saved

import com.gtocore.config.GTOConfig
import com.gtocore.integration.ae.wireless.WirelessGrid
import com.gtocore.integration.ae.wireless.WirelessMachine

import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import net.minecraft.world.level.saveddata.SavedData

import com.gregtechceu.gtceu.GTCEu
import com.gtolib.api.capability.ISync
import com.hepdd.gtmthings.utils.TeamUtil

import java.util.*

class WirelessSavedData : SavedData() {

    companion object {
        fun checkPermission(requiredUUID: UUID, requester: UUID): Boolean = requiredUUID == requester ||
            TeamUtil.getTeamUUID(requiredUUID) == requester ||
            TeamUtil.getTeamUUID(requester) == requiredUUID ||
            TeamUtil.getTeamUUID(requester) == TeamUtil.getTeamUUID(requiredUUID)
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

        // 校验昵称是否全局占用（精确匹配，已 trim）。excludeGridId 用于重命名时忽略自身
        private fun isNicknameTaken(nickname: String, excludeGridId: String? = null): Boolean = INSTANCE.gridPool.any { it.nickname == nickname && (excludeGridId == null || it.name != excludeGridId) }

        // 统一对外能力：权限判断与可访问列表（按请求者动态标记默认网格与昵称）
        fun accessibleGridsFor(requester: UUID): MutableList<WirelessGrid> = INSTANCE.gridPool
            .filter { checkPermission(it.owner, requester) }
            .map { grid ->
                val isDef = INSTANCE.defaultMap[requester] == grid.name
                // 返回只用于同步到客户端的“视图副本”，避免污染服务端运行时状态
                WirelessGrid(
                    grid.name,
                    grid.owner,
                    isDef,
                    grid.connectionPoolTable.toMutableList(),
                    grid.nickname,
                )
            }
            .toMutableList()

        fun findGridOf(machine: WirelessMachine): WirelessGrid? = INSTANCE.gridPool.firstOrNull { grid -> grid.connectionPool.any { it == machine } }

        fun findGridByName(name: String): WirelessGrid? = INSTANCE.gridPool.firstOrNull { it.name == name }

        fun joinToGrid(gridName: String, machine: WirelessMachine, requester: UUID): STATUS {
            // 先验证目标网格与权限，避免失败时把原网断开
            val grid = INSTANCE.gridPool.firstOrNull { it.name == gridName } ?: return STATUS.NOT_FOUND_GRID
            if (!checkPermission(grid.owner, requester)) return STATUS.NOT_PERMISSION
            if (grid.connectionPool.any { it == machine }) return STATUS.ALREADY_JOINT

            // 验证通过后再从原网络退出
            leaveGrid(machine)

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
                    val levelKey = machine.self().level?.dimension() ?: UNKNOWN
                    grid.connectionPoolTable.removeAll { it.pos == machine.self().pos && it.level == levelKey }
                    grid.removeNodeFromNetwork(machine)
                }
        }
        fun createNewGrid(gridName: String, requester: UUID): WirelessGrid? {
            // gridName 作为昵称，唯一ID使用随机生成；昵称全局唯一（精确匹配），空白不允许
            val nick = gridName.trim()
            if (nick.isBlank()) return null
            if (isNicknameTaken(nick)) return null
            val id = UUID.randomUUID().toString()
            if (INSTANCE.gridPool.any { it.name == id }) return null
            val newGrid = WirelessGrid(id, requester)
            newGrid.nickname = nick
            INSTANCE.gridPool.add(newGrid)
            INSTANCE.setDirty()
            return newGrid
        }
        fun removeGrid(gridName: String, requester: UUID): STATUS {
            INSTANCE.gridPool.find { it.name == gridName }?.let { removed ->
                if (!checkPermission(removed.owner, requester)) return STATUS.NOT_PERMISSION
                removed.connectionPool.forEach { it.removedFromGrid(removed.name) }
                removed.connectionPool.clear()
                removed.refreshConnectionPool()
                // 清理默认映射中的无效引用
                INSTANCE.defaultMap.entries.removeIf { it.value == removed.name }
                INSTANCE.gridPool.remove(removed)
                INSTANCE.setDirty()
                return STATUS.SUCCESS
            }
            INSTANCE.setDirty()
            return STATUS.NOT_FOUND_GRID
        }
        fun setAsDefault(gridName: String, requester: UUID) {
            // 仅更新映射，实际 isDefault 在下发给客户端时按请求者动态设置
            INSTANCE.defaultMap[requester] = gridName
            INSTANCE.setDirty()
        }
        fun cancelAsDefault(gridName: String, requester: UUID) {
            // 仅更新映射
            INSTANCE.defaultMap.remove(requester)
            INSTANCE.setDirty()
        }
        fun setGridNickname(gridId: String, requester: UUID, nickname: String): STATUS {
            val grid = INSTANCE.gridPool.firstOrNull { it.name == gridId } ?: return STATUS.NOT_FOUND_GRID
            if (!checkPermission(grid.owner, requester)) return STATUS.NOT_PERMISSION
            val nick = nickname.trim()
            // 空白：回退为唯一ID，但若被其他网格使用为昵称则视为无变更
            val target = nick.ifBlank { grid.name }
            if (target == grid.nickname) return STATUS.SUCCESS
            if (isNicknameTaken(target, excludeGridId = gridId)) return STATUS.SUCCESS
            grid.nickname = target
            INSTANCE.setDirty()
            return STATUS.SUCCESS
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
                if (GTOConfig.INSTANCE.aeLog) println("${GTCEu.isClientSide()} Saving WirelessSavedData with ${gridPool.size} grids")
                for (grid in gridPool) {
                    add(grid.encodeToNbt())
                }
            },
        )
        p0.put(
            "defaultMap",
            ListTag().apply {
                if (GTOConfig.INSTANCE.aeLog) println("${GTCEu.isClientSide()} Saving defaultMap with ${defaultMap.size} entries")
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
            res.add(WirelessGrid.decodeFromNbt(tag as CompoundTag).takeIf { n -> res.none { it.name == n.name } } ?: continue)
        }
        if (GTOConfig.INSTANCE.aeLog) println("${GTCEu.isClientSide()} Loading WirelessSavedData with ${res.size} grids")
        gridPool.addAll(res)
        val defaultList = p0.getList("defaultMap", 10)
        for (tag in defaultList) {
            val nbt = tag as CompoundTag
            defaultMap[nbt.getUUID("key")] = nbt.getString("value")
        }
        // 不在此处修改 grid.isDefault，由 accessibleGridsFor 按请求者动态标记
        gridPool.forEach { grid ->
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

fun createWirelessSyncedField(sync: ISync): ISync.ObjectSyncedField<MutableList<WirelessGrid>> = ISync.createObjectField(
    sync,
    {
        val size = it.readInt()
        val list = mutableListOf<WirelessGrid>()
        for (i in 0 until size) {
            list.add(WirelessGrid.decodeFromNbt(it.readNbt() as CompoundTag))
        }
        list
    },
    { buffer, value ->
        buffer.writeInt(value.size)
        for (grid in value) {
            buffer.writeNbt(grid.encodeToNbt())
        }
    },
)
