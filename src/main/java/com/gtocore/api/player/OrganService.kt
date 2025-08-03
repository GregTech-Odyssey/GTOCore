package com.gtocore.api.player

import com.gtocore.common.data.GTODamageTypes
import com.gtocore.common.data.GTOOrganItems.FAIRY_WING
import com.gtocore.common.data.GTOOrganItems.MANA_STEEL_WING
import com.gtocore.common.data.GTOOrganItems.MECHANICAL_WING
import com.gtocore.common.item.misc.TierData.Companion.BlockReachFunction
import com.gtocore.common.item.misc.TierData.Companion.MovementSpeedFunction
import com.gtocore.utils.ktGetOrganStack

import net.minecraft.network.chat.Component
import net.minecraft.server.TickTask
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
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

import java.util.UUID

interface IOrganService {
    fun tick(player: ServerPlayer)
}
class OrganService : IOrganService {
    override fun tick(player: ServerPlayer) {
        if (player.tickCount % 20 != 0) return
        val playerData = IEnhancedPlayer.of(player).playerData
        playerData.wingState = false
        // Night Vision
        when (playerData.organTierCache.contains(1)) {
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
            when (playerData.organTierCache.contains(tier)) {
                true -> run {
                    val modifierAmplify = MovementSpeedFunction(tier)
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
        // Block Reach
        run {
            val modifierNAME = "gtocore:organ_reach"
            val modifierUUID = UUID.nameUUIDFromBytes(modifierNAME.toByteArray())
            when (playerData.organTierCache.contains(2)) {
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
        when (playerData.organTierCache.contains(4)) { // 四级器官创造飞
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
            if (!GTODimensions.isPlanet(planet.dimension().location())) return@run

            val tier: Int = planet.tier()
            val lowerTierTag = ((tier - 1) / 2) + 1
            val cache = playerData

            if (!playerData.organTierCache.contains(lowerTierTag)) {
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
    }

    private fun whenUsingElectricWing(stack: ItemStack, player: ServerPlayer, playerData: PlayerData?): Boolean {
        val item = GTCapabilityHelper.getElectricItem(stack) ?: return false
        if (item.charge <= 0) return false
        playerData?.wingState = true
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
                return true
            }
        }
        return false
    }
}
