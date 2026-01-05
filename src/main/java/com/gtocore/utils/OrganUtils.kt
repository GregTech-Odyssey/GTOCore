package com.gtocore.utils

import com.gtocore.common.item.misc.OrganItemBase
import com.gtocore.common.item.misc.OrganType

import net.minecraft.world.item.ItemStack

import com.gtolib.api.player.PlayerData

import kotlin.math.min

/**
 * 获取玩家的器官物品栈，按器官类型分组
 */
fun PlayerData.ktGetOrganStack(): Map<OrganType, List<ItemStack>> = this.organItemStacks
    .filter { it.item is OrganItemBase }
    .groupBy { (it.item as OrganItemBase).organType }

/**
 * 检查玩家是否拥有指定等级的器官类型
 */
fun PlayerData.ktMatchOrganTier(tier: Int, type: OrganType): Boolean {
    val maxTier = ktGetOrganStack()[type]
        ?.asSequence()
        ?.mapNotNull { it.item as? OrganItemBase.TierOrganItem }
        ?.maxOfOrNull { it.tier }
        ?: return false
    
    return maxTier >= tier
}

/**
 * 刷新玩家的器官状态缓存
 * 生命周期对齐：加载时注册，卸载时清理
 */
fun PlayerData.ktFreshOrganState() {
    // 资源清理：清空旧缓存
    this.organTierCache.clear()
    
    // 重建缓存
    (0..4).forEach { tier ->
        OrganType.entries.forEach { type ->
            if (this.ktMatchOrganTier(tier, type)) {
                this.organTierCache.put(type, tier)
            }
        }
    }
}

/**
 * 获取玩家的器官套装等级（取所有器官的最小等级）
 * 卫语句优先处理特殊情况
 */
fun PlayerData.getSetOrganTier(): Int {
    var tier = Int.MAX_VALUE
    
    for (type in OrganType.entries) {
        // 卫语句：跳过第一个（Wing）
        if (type.ordinal == 0) continue
        
        tier = min(tier, this.organTierCache.getInt(type))
        
        // 卫语句：提前退出优化
        if (tier < 1) break
    }
    
    return tier
}
