package com.gtocore.utils

import appeng.api.stacks.AEKey
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap

import java.util.concurrent.ConcurrentHashMap

class AEKeySubstitutionMap(priorityGroups: List<List<AEKey>>) {

    // 缓存：从任意Key映射到其首选Key，用于极速查询。
    private val keyToPreferredCache = ConcurrentHashMap<AEKey, AEKey>()

    // 核心规则：从任意Key映射到其所在的整个优先级列表。
    private val keyToPriorityGroup: Map<AEKey, List<AEKey>>

    init {
        val mapBuilder = Reference2ReferenceOpenHashMap<AEKey, List<AEKey>>()
        priorityGroups.forEach { group ->
            if (group.isNotEmpty()) {
                group.forEach { key ->
                    mapBuilder[key] = group
                }
            }
        }
        this.keyToPriorityGroup = mapBuilder
    }

    /**
     * 获取给定AEKey的替换项（首选Key）。
     *
     * @param key 需要查询的原始AEKey。
     * @return 替换后的首选AEKey。如果该Key没有定义替换规则，则返回其自身。
     */
    fun getSubstitution(key: AEKey): AEKey {
        return keyToPreferredCache.computeIfAbsent(key) {
            val priorityGroup = keyToPriorityGroup[key]
            val preferredKey = priorityGroup?.firstOrNull() ?: key
            return@computeIfAbsent preferredKey
        }
    }

    companion object {
        @JvmField
        val EMPTY = AEKeySubstitutionMap(emptyList())
    }
}
