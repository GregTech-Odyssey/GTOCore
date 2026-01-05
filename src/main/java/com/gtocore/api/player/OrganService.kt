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
    @Override
    override fun tick(player: ServerPlayer) {
        val playerData = IEnhancedPlayer.of(player).playerData
        playerData.wingState = false
        
        applyMovementSpeedModifiers(player, playerData)
        applyBlockReachModifier(player, playerData)
        applyFlightAbility(playerData)
        handleWingUsage(player, playerData)
        handlePlanetDamage(player, playerData)
        handleSaturation(player, playerData)
        handlePoisonRemoval(player, playerData)
        handleWaterBreath(player, playerData)
        applyArmorModifiers(player, playerData)
    }

    private fun applyMovementSpeedModifiers(player: ServerPlayer, playerData: PlayerData) {
        (0..4).forEach { tier ->
            val modifierName = "gtocore:organ_speed_tier_$tier"
            val modifierUUID = UUID.nameUUIDFromBytes(modifierName.toByteArray())
            val hasRequiredLegs = playerData.organTierCache.getInt(OrganType.LeftLeg) >= tier && 
                                  playerData.organTierCache.getInt(OrganType.RightLeg) >= tier
            
            if (hasRequiredLegs) {
                val speedAttribute = player.getAttribute(Attributes.MOVEMENT_SPEED) ?: return@forEach
                val hasModifier = speedAttribute.modifiers.any { it.name == modifierName }
                if (hasModifier) return@forEach
                
                val modifierAmplify = MovementSpeedFunction(tier)
                speedAttribute.addPermanentModifier(
                    AttributeModifier(modifierUUID, modifierName, modifierAmplify, AttributeModifier.Operation.ADDITION)
                )
            } else {
                player.getAttribute(Attributes.MOVEMENT_SPEED)?.removeModifier(modifierUUID)
            }
        }
    }

    private fun applyBlockReachModifier(player: ServerPlayer, playerData: PlayerData) {
        val modifierName = "gtocore:organ_reach"
        val modifierUUID = UUID.nameUUIDFromBytes(modifierName.toByteArray())
        val hasRequiredArm = playerData.organTierCache.getInt(OrganType.RightArm) >= 2
        
        if (hasRequiredArm) {
            val reachAttribute = player.getAttribute(BLOCK_REACH.get()) ?: return
            val hasModifier = reachAttribute.modifiers.any { it.name == modifierName }
            if (hasModifier) return
            
            reachAttribute.addPermanentModifier(
                AttributeModifier(modifierUUID, modifierName, BlockReachFunction.toDouble(), AttributeModifier.Operation.ADDITION)
            )
        } else {
            player.getAttribute(BLOCK_REACH.get())?.removeModifier(modifierUUID)
        }
    }

    private fun applyFlightAbility(playerData: PlayerData) {
        if (playerData.getSetOrganTier() >= 4) {
            playerData.wingState = true
        }
    }

    private fun handleWingUsage(player: ServerPlayer, playerData: PlayerData) {
        val allOrgans = playerData.ktGetOrganStack().flatMap { it.value }
        
        // 卫语句：优先检查特殊翅膀
        allOrgans.firstOrNull { it.item.asItem() == FAIRY_WING.asItem() }?.let {
            if (tryUsingDurabilityWing(it, player, playerData)) return
        }
        
        allOrgans.firstOrNull { it.item.asItem() == MANA_STEEL_WING.asItem() }?.let {
            if (tryUsingDurabilityWing(it, player, playerData)) return
        }
        
        allOrgans.filter { it.item.asItem() == MECHANICAL_WING.asItem() }.forEach {
            if (whenUsingElectricWing(it, player, playerData)) return
        }
    }

    private fun handlePlanetDamage(player: ServerPlayer, playerData: PlayerData) {
        // 卫语句：优先排除非正常情况
        if (!player.gameMode.isSurvival) return
        
        val planet: Planet = PlanetApi.API.getPlanet(player.level()) ?: return
        if (GTODimensions.OVERWORLD.equals(planet.dimension().location())) return
        if (GTODimensions.GLACIO.equals(planet.dimension().location())) return
        if (!GTODimensions.isPlanet(planet.dimension().location())) return
        
        val tier: Int = planet.tier()
        val lowerTierTag = ((tier - 1) / 2) + 1
        if (playerData.getSetOrganTier() >= lowerTierTag) return
        
        val customComponent: Component = Component.translatable(
            "gtocore.death.attack.turbulence_of_another_star",
            player.name,
            tier,
            "最低Tier $lowerTierTag"
        )
        
        val currentCount = playerData.floatCache.getOrPut("try_attack_count") { 0.0f } + 1.0f
        
        player.hurt(
            GTODamageTypes.getGenericDamageSource(player, customComponent) { 
                playerData.floatCache.put("try_attack_count", 0.0f) 
            },
            currentCount
        )
        
        if (currentCount > 40.0f) {
            player.server.tell(TickTask(1, player::kill))
            player.server.playerList.broadcastSystemMessage(customComponent, true)
            playerData.floatCache.put("try_attack_count", 0.0f)
        } else {
            playerData.floatCache["try_attack_count"] = currentCount
        }
    }

    private fun handleSaturation(player: ServerPlayer, playerData: PlayerData) {
        if (playerData.getSetOrganTier() < 3) return
        
        player.foodData.foodLevel = 20
        player.foodData.setSaturation(20.0f)
        player.foodData.setExhaustion(0.0f)
    }

    private fun handlePoisonRemoval(player: ServerPlayer, playerData: PlayerData) {
        if (playerData.organTierCache.getInt(OrganType.Liver) < 2) return
        
        if (player.hasEffect(net.minecraft.world.effect.MobEffects.POISON)) {
            player.removeEffect(net.minecraft.world.effect.MobEffects.POISON)
        }
        if (player.hasEffect(net.minecraft.world.effect.MobEffects.WITHER)) {
            player.removeEffect(net.minecraft.world.effect.MobEffects.WITHER)
        }
    }

    private fun handleWaterBreath(player: ServerPlayer, playerData: PlayerData) {
        if (playerData.organTierCache.getInt(OrganType.Lung) < 2) return
        if (!player.isInWater) return
        
        player.airSupply = 300
    }

    private fun applyArmorModifiers(player: ServerPlayer, playerData: PlayerData) {
        (1..4).forEach { tier ->
            val armorModifierName = "gtocore:organ_armor_tier_$tier"
            val armorModifierUUID = UUID.nameUUIDFromBytes(armorModifierName.toByteArray())
            val toughnessModifierName = "gtocore:organ_toughness_tier_$tier"
            val toughnessModifierUUID = UUID.nameUUIDFromBytes(toughnessModifierName.toByteArray())
            
            if (playerData.getSetOrganTier() == tier) {
                val armorAmplify = 5.0 * tier
                val toughnessAmplify = 5.0 * tier
                
                val armorAttribute = player.getAttribute(Attributes.ARMOR) ?: return@forEach
                val hasArmorModifier = armorAttribute.modifiers.any { it.name == armorModifierName }
                if (!hasArmorModifier) {
                    armorAttribute.addPermanentModifier(
                        AttributeModifier(armorModifierUUID, armorModifierName, armorAmplify, AttributeModifier.Operation.ADDITION)
                    )
                }
                
                val toughnessAttribute = player.getAttribute(Attributes.ARMOR_TOUGHNESS) ?: return@forEach
                val hasToughnessModifier = toughnessAttribute.modifiers.any { it.name == toughnessModifierName }
                if (!hasToughnessModifier) {
                    toughnessAttribute.addPermanentModifier(
                        AttributeModifier(toughnessModifierUUID, toughnessModifierName, toughnessAmplify, AttributeModifier.Operation.ADDITION)
                    )
                }
            } else {
                player.getAttribute(Attributes.ARMOR)?.removeModifier(armorModifierUUID)
                player.getAttribute(Attributes.ARMOR_TOUGHNESS)?.removeModifier(toughnessModifierUUID)
            }
        }
    }

    private fun whenUsingElectricWing(stack: ItemStack, player: ServerPlayer, playerData: PlayerData?): Boolean {
        // 卫语句：防御性编程
        if (playerData == null) return false
        
        val item = GTCapabilityHelper.getElectricItem(stack) ?: return false
        if (item.charge <= 0) return false
        
        playerData.wingState = true
        playerData.flySpeedAble = 0.25f
        IWirelessChargerInteraction.charge(playerData.getNetMachine(), stack)
        
        if (player.abilities.flying && player.level().getBlockState(player.onPos.below(1)).block == Blocks.AIR) {
            item.discharge(GTValues.V[GTValues.EV], item.tier, true, false, false)
        }
        return true
    }

    private fun tryUsingDurabilityWing(stack: ItemStack, player: ServerPlayer, playerData: PlayerData?): Boolean {
        // 卫语句：防御性编程
        if (playerData == null) return false
        
        val durability = stack.maxDamage - stack.damageValue
        if (durability <= 0) return false
        
        if (player.abilities.flying && player.level().getBlockState(player.onPos.below(1)).block == Blocks.AIR) {
            stack.hurtAndBreak(1, player) { player1: Player ->
                player1.sendSystemMessage(
                    Component.translatable("gtocore.player.organ.you_wing_is_broken")
                )
            }
        }
        playerData.wingState = true
        playerData.flySpeedAble = 0.15f
        return true
    }
}
