package com.gtocore.api.player

import com.gtocore.common.data.GTODamageTypes
import com.gtocore.common.data.GTOOrganItems.FAIRY_WING
import com.gtocore.common.data.GTOOrganItems.MANA_STEEL_WING
import com.gtocore.common.data.GTOOrganItems.MECHANICAL_WING
import com.gtocore.common.item.misc.OrganType
import com.gtocore.common.item.misc.TierData.Companion.BlockReachFunction
import com.gtocore.common.item.misc.TierData.Companion.MovementSpeedFunction
import com.gtocore.utils.getSetOrganTier
import com.gtocore.utils.ktGetOrganStack

import net.minecraft.network.chat.Component
import net.minecraft.server.TickTask
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.common.ForgeMod.BLOCK_REACH

import com.gregtechceu.gtceu.api.GTValues
import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper
import com.gtolib.api.capability.IWirelessChargerInteraction
import com.gtolib.api.data.GTODimensions
import com.gtolib.api.player.IEnhancedPlayer
import com.gtolib.api.player.PlayerData
import earth.terrarium.adastra.api.planets.Planet
import earth.terrarium.adastra.api.planets.PlanetApi

import java.util.*

interface IOrganService {
    fun tick(player: ServerPlayer)
}
class OrganService : IOrganService {
    override fun tick(player: ServerPlayer) {
        val playerData = IEnhancedPlayer.of(player).playerData
        playerData.wingState = false
        // Movement Speed
        (0..4).forEach { tier ->
            val modifierNAME = "gtocore:organ_speed_tier_$tier"
            val modifierUUID = UUID.nameUUIDFromBytes(modifierNAME.toByteArray())
            when (playerData.organTierCache.getInt(OrganType.LeftLeg) > tier && playerData.organTierCache.getInt(OrganType.RightLeg) > tier) {
                true -> run {
                    val modifierAmplify = MovementSpeedFunction(tier)
                    val shouldAdd = player.getAttribute(Attributes.MOVEMENT_SPEED)?.modifiers?.all { it.name != modifierNAME } ?: true
                    if (!shouldAdd)return@run
                    player.getAttribute(Attributes.MOVEMENT_SPEED)?.addPermanentModifier(
                        AttributeModifier(modifierUUID, modifierNAME, modifierAmplify, AttributeModifier.Operation.ADDITION),
                    )
                }
                false -> run {
                    player.getAttribute(Attributes.MOVEMENT_SPEED)?.removeModifier(modifierUUID)
                }
            }
        }
        // Block Reach
        run {
            val modifierNAME = "gtocore:organ_reach"
            val modifierUUID = UUID.nameUUIDFromBytes(modifierNAME.toByteArray())
            when (playerData.organTierCache.getInt(OrganType.RightArm) > 1) {
                true -> run {
                    val modifierAmplify = BlockReachFunction
                    val shouldAdd = player.getAttribute(BLOCK_REACH.get())?.modifiers?.all { it.name != modifierNAME } ?: true
                    if (!shouldAdd)return@run
                    player.getAttribute(BLOCK_REACH.get())?.addPermanentModifier(
                        AttributeModifier(modifierUUID, modifierNAME, modifierAmplify.toDouble(), AttributeModifier.Operation.ADDITION),
                    )
                }
                false -> run {
                    player.getAttribute(BLOCK_REACH.get())?.removeModifier(modifierUUID)
                }
            }
        }
        // Fly
        when (playerData.getSetOrganTier() >= 4) { // 四级器官创造飞
            true -> run {
                playerData.wingState = true
            }
            false -> {}
        }
        // Wing
        playerData.ktGetOrganStack().flatMap { it.value }.let root@{
            it.firstOrNull { it.item.asItem() == FAIRY_WING.asItem() }.let {
                it?.let {
                    if (tryUsingDurabilityWing(it, player, playerData)) return@root
                }
            }
            it.firstOrNull { it.item.asItem() == MANA_STEEL_WING.asItem() }.let {
                it?.let {
                    if (tryUsingDurabilityWing(it, player, playerData)) return@root
                }
            }
            it.filter { it.item.asItem() == MECHANICAL_WING.asItem() }.forEach {
                if (whenUsingElectricWing(it, player, playerData)) return@root
            }
        }
        // 外星球伤害
        run {
            val planet: Planet = PlanetApi.API.getPlanet(player.level()) ?: return@run
            if (!player.gameMode.isSurvival) return@run
            if (GTODimensions.OVERWORLD.equals(planet.dimension().location())) return@run
            if (GTODimensions.GLACIO.equals(planet.dimension().location())) return@run
            if (!GTODimensions.isPlanet(planet.dimension().location())) return@run

            val tier: Int = planet.tier()
            val lowerTierTag = ((tier - 1) / 2) + 1
            val cache = playerData

            if (playerData.getSetOrganTier() < lowerTierTag) {
                val customComponent: Component = Component.translatable(
                    "gtocore.death.attack.turbulence_of_another_star",
                    player.name,
                    tier,
                    "最低Tier $lowerTierTag",
                )

                val currentCount = cache.floatCache.getOrPut("try_attack_count") { 0.0f } + 1.0f

                player.hurt(
                    GTODamageTypes.getGenericDamageSource(
                        player,
                        customComponent,
                    ) { cache.floatCache.put("try_attack_count", 0.0f) },
                    currentCount,
                )

                if (currentCount > 40.0f) {
                    player.server.tell(TickTask(1, player::kill))
                    player.server.playerList.broadcastSystemMessage(customComponent, true)
                    cache.floatCache.put("try_attack_count", 0.0f)
                } else {
                    cache.floatCache["try_attack_count"] = currentCount
                }
            }
        }
        // t3+ 永久饱和（不显示图标）
        run {
            val needsFood = playerData.getSetOrganTier() >= 3
            when (needsFood) {
                true -> {
                    player.getFoodData().foodLevel = 20
                    player.getFoodData().setSaturation(20.0f)
                    player.getFoodData().setExhaustion(0.0f)
                }
                false -> {}
            }
        }
        // t2+的肺部屏蔽中毒Effect效果
        run {
            val needsRemove = playerData.organTierCache.getInt(OrganType.Liver) >= 2
            when (needsRemove) {
                true -> {
                    if (player.hasEffect(net.minecraft.world.effect.MobEffects.POISON)) {
                        player.removeEffect(net.minecraft.world.effect.MobEffects.POISON)
                    }
                    if (player.hasEffect(net.minecraft.world.effect.MobEffects.WITHER)) {
                        player.removeEffect(net.minecraft.world.effect.MobEffects.WITHER)
                    }
                }
                false -> {}
            }
        }
        // t2+的肺部将提供水下无限氧气
        run {
            val needsWaterBreath = (playerData.organTierCache.getInt(OrganType.Lung) >= 2) && player.isInWater
            when (needsWaterBreath) {
                true -> player.airSupply = 300
                false -> {}
            }
        }
        // Tn提供5n护甲，5n韧性
        run {
            (1..4).forEach { tier ->
                val armorModifierNAME = "gtocore:organ_armor_tier_$tier"
                val armorModifierUUID = UUID.nameUUIDFromBytes(armorModifierNAME.toByteArray())
                val toughnessModifierNAME = "gtocore:organ_toughness_tier_$tier"
                val toughnessModifierUUID = UUID.nameUUIDFromBytes(toughnessModifierNAME.toByteArray())
                when (playerData.getSetOrganTier() == tier) {
                    true -> run {
                        val armorAmplify = 5.0 * (tier)
                        val toughnessAmplify = 5.0 * (tier)
                        val shouldAddArmor = player.getAttribute(Attributes.ARMOR)?.modifiers?.all { it.name != armorModifierNAME } ?: true
                        if (shouldAddArmor) {
                            player.getAttribute(Attributes.ARMOR)?.addPermanentModifier(
                                AttributeModifier(armorModifierUUID, armorModifierNAME, armorAmplify, AttributeModifier.Operation.ADDITION),
                            )
                        }
                        val shouldAddToughness = player.getAttribute(Attributes.ARMOR_TOUGHNESS)?.modifiers?.all { it.name != toughnessModifierNAME } ?: true
                        if (shouldAddToughness) {
                            player.getAttribute(Attributes.ARMOR_TOUGHNESS)
                                ?.addPermanentModifier(
                                    AttributeModifier(toughnessModifierUUID, toughnessModifierNAME, toughnessAmplify, AttributeModifier.Operation.ADDITION),
                                )
                        }
                    }
                    false -> run {
                        player.getAttribute(Attributes.ARMOR)?.removeModifier(armorModifierUUID)
                        player.getAttribute(Attributes.ARMOR_TOUGHNESS)?.removeModifier(toughnessModifierUUID)
                    }
                }
            }
        }
    }

    private fun whenUsingElectricWing(stack: ItemStack, player: ServerPlayer, playerData: PlayerData?): Boolean {
        val item = GTCapabilityHelper.getElectricItem(stack) ?: return false
        if (item.charge <= 0) return false
        playerData?.wingState = true
        playerData?.flySpeedAble = 0.25f
        IWirelessChargerInteraction.charge(playerData?.getNetMachine(), stack)
        if (player.abilities.flying && player.level().getBlockState(player.onPos.below(1)).block == Blocks.AIR) {
            item.discharge(GTValues.V[GTValues.EV], item.tier, true, false, false)
        }
        return true
    }

    private fun tryUsingDurabilityWing(stack: ItemStack, player: ServerPlayer, playerData: PlayerData?): Boolean {
        val durability = stack.maxDamage - stack.damageValue
        when {
            durability > 0 -> run {
                if (player.abilities.flying &&
                    player.level()
                        .getBlockState(player.onPos.below(1)).block == Blocks.AIR
                ) {
                    stack.hurtAndBreak(1, player) { player1: Player ->
                        player1.sendSystemMessage(
                            Component.translatable(
                                "gtocore.player.organ.you_wing_is_broken",
                            ),
                        )
                    }
                }
                playerData?.wingState = true
                playerData?.flySpeedAble = 0.15f
                return true
            }
        }
        return false
    }
}
