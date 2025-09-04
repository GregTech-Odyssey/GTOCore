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

    companion object {
        private const val TRY_ATTACK_COUNT_KEY = "try_attack_count"

        // 修改为 Pair<String, UUID> 类型
        private val MOVEMENT_SPEED_MODIFIERS = (0..4).associateWith { tier ->
            val name = "gtocore:organ_speed_tier_$tier"
            name to UUID.nameUUIDFromBytes(name.toByteArray())
        }

        private const val BLOCK_REACH_MODIFIER_NAME = "gtocore:organ_reach"
        private val BLOCK_REACH_MODIFIER_UUID = UUID.nameUUIDFromBytes(BLOCK_REACH_MODIFIER_NAME.toByteArray())
    }

    override fun tick(player: ServerPlayer) {
        if (player.tickCount % 20 != 0) return
        val playerData = IEnhancedPlayer.of(player).playerData
        playerData.wingState = false

        handleNightVision(player, playerData)
        handleMovementSpeed(player, playerData)
        handleBlockReach(player, playerData)
        handleFly(playerData)
        handleWings(player, playerData)
        handlePlanetDamage(player, playerData)
    }

    private fun handleNightVision(player: ServerPlayer, playerData: PlayerData) {
        if (playerData.organTierCache.contains(1)) {
            val effect = player.getEffect(MobEffects.NIGHT_VISION)
            val shouldAdd = effect?.duration?.let { it < 20 * 45 - 20 * 15 } ?: true
            if (shouldAdd) {
                player.addEffect(MobEffectInstance(MobEffects.NIGHT_VISION, 20 * 45, 0, false, false, true))
            }
        }
    }

    private fun handleMovementSpeed(player: ServerPlayer, playerData: PlayerData) {
        (0..4).forEach { tier ->
            val modifierPair = MOVEMENT_SPEED_MODIFIERS[tier] ?: return@forEach
            val (modifierName, modifierUUID) = modifierPair
            val attribute = player.getAttribute(Attributes.MOVEMENT_SPEED) ?: return@forEach

            if (playerData.organTierCache.contains(tier)) {
                val shouldAdd = attribute.modifiers.all { it.name != modifierName }
                if (shouldAdd) {
                    val modifierAmplify = MovementSpeedFunction(tier)
                    attribute.addPermanentModifier(
                        AttributeModifier(modifierUUID, modifierName, modifierAmplify.toDouble(), AttributeModifier.Operation.ADDITION),
                    )
                }
            } else {
                attribute.removeModifier(modifierUUID)
            }
        }
    }

    private fun handleBlockReach(player: ServerPlayer, playerData: PlayerData) {
        val contains = playerData.organTierCache.contains(2)
        val attribute = player.getAttribute(BLOCK_REACH.get()) ?: return

        if (contains) {
            val shouldAdd = attribute.modifiers.all { it.name != BLOCK_REACH_MODIFIER_NAME }
            if (shouldAdd) {
                val modifierAmplify = BlockReachFunction
                attribute.addPermanentModifier(
                    AttributeModifier(BLOCK_REACH_MODIFIER_UUID, BLOCK_REACH_MODIFIER_NAME, modifierAmplify.toDouble(), AttributeModifier.Operation.ADDITION),
                )
            }
        } else {
            attribute.removeModifier(BLOCK_REACH_MODIFIER_UUID)
        }
    }

    private fun handleFly(playerData: PlayerData) {
        if (playerData.organTierCache.contains(4)) {
            playerData.wingState = true
        }
    }

    private fun handleWings(player: ServerPlayer, playerData: PlayerData) {
        val stacks = playerData.ktGetOrganStack().flatMap { it.value }
        stacks.firstOrNull { it.item.asItem() == FAIRY_WING.asItem() }?.let {
            if (tryUsingDurabilityWing(it, player, playerData)) return
        }
        stacks.firstOrNull { it.item.asItem() == MANA_STEEL_WING.asItem() }?.let {
            if (tryUsingDurabilityWing(it, player, playerData)) return
        }
        stacks.filter { it.item.asItem() == MECHANICAL_WING.asItem() }.forEach {
            if (whenUsingElectricWing(it, player, playerData)) return
        }
    }

    private fun handlePlanetDamage(player: ServerPlayer, playerData: PlayerData) {
        val planet: Planet = PlanetApi.API.getPlanet(player.level()) ?: return
        if (!player.gameMode.isSurvival) return
        if (GTODimensions.OVERWORLD.equals(planet.dimension().location())) return
        if (!GTODimensions.isPlanet(planet.dimension().location())) return

        val tier: Int = planet.tier()
        val lowerTierTag = ((tier - 1) / 2) + 1

        if (!playerData.organTierCache.contains(lowerTierTag)) {
            val customComponent: Component = Component.translatable(
                "gtocore.death.attack.turbulence_of_another_star",
                player.name,
                tier,
                "最低Tier $lowerTierTag",
            )

            val currentCount = playerData.floatCache.getOrPut(TRY_ATTACK_COUNT_KEY) { 0.0f } + 1.0f

            player.hurt(
                GTODamageTypes.getGenericDamageSource(player, customComponent) {},
                currentCount,
            )

            if (currentCount > 40.0f) {
                player.server.tell(TickTask(1, player::kill))
                player.server.playerList.broadcastSystemMessage(customComponent, true)
                playerData.floatCache[TRY_ATTACK_COUNT_KEY] = 0.0f
            } else {
                playerData.floatCache[TRY_ATTACK_COUNT_KEY] = currentCount
            }
        }
    }

    private fun whenUsingElectricWing(stack: ItemStack, player: ServerPlayer, playerData: PlayerData): Boolean {
        val item = GTCapabilityHelper.getElectricItem(stack) ?: return false
        if (item.charge <= 0) return false
        playerData.wingState = true
        playerData.getNetMachine()?.let { IWirelessChargerInteraction.charge(it, stack) }
        if (player.abilities.flying && player.level().getBlockState(player.onPos.below(1)).block == Blocks.AIR) {
            item.discharge(GTValues.V[GTValues.EV], item.tier, true, false, false)
        }
        return true
    }

    private fun tryUsingDurabilityWing(stack: ItemStack, player: ServerPlayer, playerData: PlayerData): Boolean {
        val durability = stack.maxDamage - stack.damageValue
        if (durability > 0) {
            if (player.abilities.flying && player.level().getBlockState(player.onPos.below(1)).block == Blocks.AIR) {
                stack.hurtAndBreak(1, player) { player1: Player ->
                    player1.sendSystemMessage(Component.translatable("gtocore.player.organ.you_wing_is_broken"))
                }
            }
            playerData.wingState = true
            return true
        }
        return false
    }
}
