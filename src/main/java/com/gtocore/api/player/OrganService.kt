package com.gtocore.api.player

import com.gtocore.common.data.GTODamageTypes
import com.gtocore.common.data.OrganItems.*

import net.minecraft.network.chat.Component
import net.minecraft.server.TickTask
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.Blocks

import com.gregtechceu.gtceu.api.capability.GTCapabilityHelper
import com.gtolib.api.capability.IWirelessChargerInteraction
import com.gtolib.api.data.GTODimensions
import com.gtolib.api.player.IEnhancedPlayer
import com.gtolib.api.player.organ.capability.OrganCapability
import earth.terrarium.adastra.api.planets.Planet
import earth.terrarium.adastra.api.planets.PlanetApi

import java.util.*

interface IOrganService {
    fun tick(player: Player)
}
object OrganService : IOrganService {
    override fun tick(player: Player) {
        if (player !is ServerPlayer) return
        if (player.tickCount % 20 != 0) return
        val cap = OrganCapability.of(player)
        val playerData = IEnhancedPlayer.of(player).playerData

        playerData.wingState = false

        // Night Vision
        when (cap.ktMatchTierOrganSet(1)) {
            true -> run {
                val shouldAdd = player.getEffect(MobEffects.NIGHT_VISION)?.let { it.duration < 20 * 45 - 20 * 15 } ?: true
                if (!shouldAdd)return@run
                player.addEffect(MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 45, 0, false, false, true))
            }
            false -> {}
        }
        // Movement Speed
        (0..4).forEach { tier ->
            val modifierNAME = "gtocore:organ_speed_tier_$tier"
            val modifierUUID = UUID.nameUUIDFromBytes(modifierNAME.toByteArray())
            when (cap.ktMatchTierOrganSet(tier)) {
                true -> run {
                    val modifierAmplify = 0.1f * 0.1f * tier
                    val shouldAdd = player.getAttribute(Attributes.MOVEMENT_SPEED)?.modifiers?.all { it.name != modifierNAME } ?: true
                    if (!shouldAdd)return@run
                    player.getAttribute(Attributes.MOVEMENT_SPEED)?.addPermanentModifier(
                        AttributeModifier(modifierUUID, modifierNAME, modifierAmplify.toDouble(), AttributeModifier.Operation.ADDITION),
                    )
                }
                false -> run {
                    player.getAttribute(Attributes.MOVEMENT_SPEED)?.removeModifier(modifierUUID)
                }
            }
        }
        // Fly
        when (cap.ktMatchLowTierOrganSet(4)) { // 四级器官创造飞
            true -> run {
                playerData.wingState = true
            }
            false -> {}
        }
        // Wing
        cap.ktGetOrganStack().flatMap { it.value }.let root@{
            it.firstOrNull { it.item.asItem() == ORGAN_FAIRY_WING.asItem() }.let {
                it?.let {
                    val durability = it.maxDamage - it.damageValue
                    when {
                        durability > 0 -> run {
                            if (player.abilities.flying && player.level().getBlockState(player.onPos.below(1)).block == Blocks.AIR) {
                                it.hurtAndBreak(1, player) { player1: Player ->
                                    player1.sendSystemMessage(
                                        Component.translatable(
                                            "gtocore.player.organ.you_wing_is_broken",
                                        ),
                                    )
                                }
                            }
                            playerData.wingState = true
                            return@root
                        }
                    }
                }
            }
            it.firstOrNull { it.item.asItem() == ORGAN_FRAGILE_AND_CONTAMINATED_MANA_STEEL_WING.asItem() }.let {
                it?.let {
                    val durability = it.maxDamage - it.damageValue
                    when {
                        durability > 0 -> run {
                            if (player.abilities.flying && player.level().getBlockState(player.onPos.below(1)).block == Blocks.AIR) {
                                it.hurtAndBreak(1, player) { player1: Player ->
                                    player1.sendSystemMessage(
                                        Component.translatable(
                                            "gtocore.player.organ.you_wing_is_broken",
                                        ),
                                    )
                                }
                            }
                            playerData.wingState = true
                            return@root
                        }
                    }
                }
            }
            it.firstOrNull { it.item.asItem() == TRANSFORM_MECHANICAL_WING_TIER_1.asItem() }.let {
                it?.let {
                    val item = GTCapabilityHelper.getElectricItem(it) ?: return@let
                    if (item.charge <= 0)return@let
                    playerData.wingState = true
                    IWirelessChargerInteraction.charge(playerData.getNetMachine(), it)
                    if (player.abilities.flying && player.level().getBlockState(player.onPos.below(1)).block == Blocks.AIR) {
                        item.discharge(256, item.tier, true, false, false)
                    }
                }
            }
            it.firstOrNull { it.item.asItem() == TRANSFORM_MECHANICAL_WING_TIER_2.asItem() }.let {
                it?.let {
                    val item = GTCapabilityHelper.getElectricItem(it) ?: return@let
                    if (item.charge <= 0)return@let
                    playerData.wingState = true
                    IWirelessChargerInteraction.charge(playerData.getNetMachine(), it)
                    if (player.abilities.flying && player.level().getBlockState(player.onPos.below(1)).block == Blocks.AIR) {
                        item.discharge(256, item.tier, true, false, false)
                    }
                }
            }
        }
        // 外星球伤害
        run {
            val planet: Planet? = PlanetApi.API.getPlanet(player.level())
            if (planet == null) return
            if (!player.gameMode.isSurvival) return
            if (GTODimensions.OVERWORLD.equals(planet.dimension().location())) return
            if (!GTODimensions.isPlanet(planet.dimension().location())) return

            val tier: Int = planet.tier()
            val lowerTierTag = ((tier - 1) / 2) + 1
            val cache = playerData

            if (!cap.ktMatchLowTierOrganSet(lowerTierTag)) {
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
                    cache.floatCache?.remove("try_attack_count")
                } else {
                    cache.floatCache["try_attack_count"] = currentCount
                }
            }
        }
    }
}
