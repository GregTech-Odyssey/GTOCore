package com.gtocore.api.player

import com.gtocore.common.data.OrganItems

import net.minecraft.world.item.ItemStack

import com.gtolib.api.player.organ.capability.OrganCapability
import com.gtolib.api.player.organ.function.OrganItem
import com.gtolib.api.player.organ.function.OrganType
import com.gtolib.api.player.organ.function.item.impl.TierOrganItem
fun OrganCapability.ktGetOrganStack(): Map<OrganType, List<ItemStack>> = this.organStacks
    .groupBy { OrganType.of(it) }
    .filterKeys { it.isPresent }
    .mapKeys { it.key.get() }
    .mapValues { it.value.filterNotNull() }

fun OrganCapability.ktMatchTierOrganSet(tier: Int): Boolean = run {
    val mapValues: Map<OrganType, List<TierOrganItem>> = OrganItems.ORGAN_TIER_MAP
        .mapValues { it.value.slice(tier until it.value.size) }
        .mapValues { it.value.map { it.get() } }
    val organStack = ktGetOrganStack()
    mapValues.all { match ->
        organStack[match.key]?.any { match.value.map { it.asItem() }.contains(it.item.asItem()) } ?: false
    }
}

fun OrganCapability.ktMatchLowTierOrganSet(tierLow: Int): Boolean = (tierLow..4).any { ktMatchTierOrganSet(it) }

fun OrganCapability.ktMatchOrganItem(item: OrganItem): ItemStack = run {
    ktGetOrganStack().flatMap { it.value }.firstOrNull { (it.item is OrganItem) && (it.item as OrganItem) == item } ?: ItemStack.EMPTY
}
