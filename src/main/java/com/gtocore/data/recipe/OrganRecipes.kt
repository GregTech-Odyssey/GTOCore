package com.gtocore.data.recipe

import com.gtocore.api.data.tag.GTOTagPrefix
import com.gtocore.common.data.GTOItems
import com.gtocore.common.data.GTOMaterials
import com.gtocore.common.data.GTOOrganItems
import com.gtocore.common.data.GTORecipeTypes
import com.gtocore.common.item.misc.OrganType

import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

import com.gregtechceu.gtceu.api.GTValues
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry
import com.gregtechceu.gtceu.api.data.tag.TagPrefix
import com.gregtechceu.gtceu.common.data.GTItems
import com.gregtechceu.gtceu.common.data.GTMaterials
import com.gregtechceu.gtceu.data.recipe.CustomTags
import com.gregtechceu.gtceu.data.recipe.GTCraftingComponents
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper
import com.gtolib.GTOCore

import java.util.function.IntConsumer
import java.util.function.IntUnaryOperator
import java.util.stream.IntStream

object OrganRecipes {
    fun init() {
        // 内脏编辑器
        VanillaRecipeHelper.addShapedRecipe(
            GTOCore.id("organ_modifier"),
            GTOOrganItems.ORGAN_MODIFIER.asStack(),
            " B ",
            "CDC",
            "EFF",
            'B', GTItems.VOLTAGE_COIL_MV.asStack(),
            'C', GTItems.ROBOT_ARM_MV.asStack(),
            'D', ItemStack(Items.CRAFTING_TABLE.asItem()),
            'E', ItemStack(Items.SLIME_BALL.asItem()),
            'F', CustomTags.MV_CIRCUITS,
        )
        VanillaRecipeHelper.addShapedRecipe(
            GTOCore.id("mana_steel_wing"),
            GTOOrganItems.MANA_STEEL_WING.asStack(),
            "ABA",
            "CDC",
            "ABA",
            'A', MaterialEntry(TagPrefix.plateDouble, GTOMaterials.Manasteel),
            'B', MaterialEntry(TagPrefix.foil, GTOMaterials.Manasteel),
            'C', MaterialEntry(GTOTagPrefix.FIELD_GENERATOR_CASING, GTMaterials.Steel),
            'D', GTOItems.COLORFUL_MYSTICAL_FLOWER.asItem(),
        )
        VanillaRecipeHelper.addShapedRecipe(
            GTOCore.id("fairy_wing"),
            GTOOrganItems.FAIRY_WING.asStack(),
            "ABA",
            "ACA",
            "DDD",
            'A', MaterialEntry(TagPrefix.foil, GTOMaterials.Herbs),
            'B', MaterialEntry(TagPrefix.plateDouble, GTOMaterials.Herbs),
            'C', GTItems.FIELD_GENERATOR_MV.asStack(),
            'D', GTOItems.COLORFUL_MYSTICAL_FLOWER.asItem(),
        )
        VanillaRecipeHelper.addShapedRecipe(
            GTOCore.id("mechanical_wing"),
            GTOOrganItems.MECHANICAL_WING.asStack(),
            "ABA",
            "CBC",
            "DED",
            'A', GTItems.BATTERY_EV_VANADIUM.asStack(),
            'B', MaterialEntry(TagPrefix.foil, GTMaterials.Titanium),
            'C', MaterialEntry(TagPrefix.plateDouble, GTMaterials.Titanium),
            'D', GTItems.FIELD_GENERATOR_EV.asStack(),
            'E', MaterialEntry(TagPrefix.ingot, GTMaterials.Titanium),
        )
        // 器官组件
        IntStream.rangeClosed(1, 4).forEachOrdered(
            IntConsumer { OrganTier: Int ->
                val tier = OrganTier shl 1
                val scaleOperator =
                    IntUnaryOperator { n: Int -> if (OrganTier == 1) n shl GTOCore.difficulty - 1 else n shl GTOCore.difficulty } // OrganTier==1
                // 为MV,没自动化，降点难度
                val motor = GTCraftingComponents.MOTOR.get(tier) as ItemStack
                val conveyor = GTCraftingComponents.CONVEYOR.get(tier) as ItemStack
                val pump = GTCraftingComponents.PUMP.get(tier) as ItemStack
                val piston = GTCraftingComponents.PISTON.get(tier) as ItemStack
                val robot_arm = GTCraftingComponents.ROBOT_ARM.get(tier) as ItemStack
                val emitter = GTCraftingComponents.EMITTER.get(tier) as ItemStack
                val sensor = GTCraftingComponents.SENSOR.get(tier) as ItemStack
                val field_generator = GTCraftingComponents.FIELD_GENERATOR.get(tier - 1) as ItemStack // 超导超了一个阶段
                val circuitTag = GTCraftingComponents.CIRCUIT.get(tier) as TagKey<*>
                // arm leg
                GTORecipeTypes.ASSEMBLER_RECIPES.builder("organ_right_arm_tier_" + tier)
                    .inputItems(motor, scaleOperator.applyAsInt(2))
                    .inputItems(robot_arm, scaleOperator.applyAsInt(4))
                    .inputItems(sensor, scaleOperator.applyAsInt(2))
                    .inputItems(field_generator, scaleOperator.applyAsInt(1))
                    .inputItems(circuitTag, scaleOperator.applyAsInt(4))
                    .inputItems(piston, scaleOperator.applyAsInt(1))
                    .outputItems(GTOOrganItems.TierOrganMap[OrganType.RightArm]!![OrganTier].asStack())
                    .EUt(GTValues.VA[tier].toLong())
                    .duration(1200)
                    .circuitMeta(1)
                    .save()
                GTORecipeTypes.ASSEMBLER_RECIPES.builder("organ_left_arm_tier_" + tier)
                    .inputItems(motor, scaleOperator.applyAsInt(2))
                    .inputItems(robot_arm, scaleOperator.applyAsInt(4))
                    .inputItems(sensor, scaleOperator.applyAsInt(2))
                    .inputItems(field_generator, scaleOperator.applyAsInt(1))
                    .inputItems(circuitTag, scaleOperator.applyAsInt(4))
                    .inputItems(piston, scaleOperator.applyAsInt(1))
                    .outputItems(GTOOrganItems.TierOrganMap[OrganType.LeftArm]!![OrganTier].asStack())
                    .EUt(GTValues.VA[tier].toLong())
                    .duration(1200)
                    .circuitMeta(2)
                    .save()
                GTORecipeTypes.ASSEMBLER_RECIPES.builder("organ_right_leg_tier_" + tier)
                    .inputItems(motor, scaleOperator.applyAsInt(4))
                    .inputItems(conveyor, scaleOperator.applyAsInt(2))
                    .inputItems(robot_arm, scaleOperator.applyAsInt(2))
                    .inputItems(sensor, scaleOperator.applyAsInt(2))
                    .inputItems(field_generator, scaleOperator.applyAsInt(1))
                    .inputItems(circuitTag, scaleOperator.applyAsInt(4))
                    .inputItems(piston, scaleOperator.applyAsInt(1))
                    .outputItems(GTOOrganItems.TierOrganMap[OrganType.RightLeg]!![OrganTier].asStack())
                    .EUt(GTValues.VA[tier].toLong())
                    .duration(1200)
                    .circuitMeta(3)
                    .save()
                GTORecipeTypes.ASSEMBLER_RECIPES.builder("organ_left_leg_tier_" + tier)
                    .inputItems(motor, scaleOperator.applyAsInt(4))
                    .inputItems(conveyor, scaleOperator.applyAsInt(2))
                    .inputItems(robot_arm, scaleOperator.applyAsInt(2))
                    .inputItems(sensor, scaleOperator.applyAsInt(2))
                    .inputItems(field_generator, scaleOperator.applyAsInt(1))
                    .inputItems(circuitTag, scaleOperator.applyAsInt(4))
                    .inputItems(piston, scaleOperator.applyAsInt(1))
                    .outputItems(GTOOrganItems.TierOrganMap[OrganType.LeftLeg]!![OrganTier].asStack())
                    .EUt(GTValues.VA[tier].toLong())
                    .duration(1200)
                    .circuitMeta(4)
                    .save()
                // heart eyes lungs liver spine
                GTORecipeTypes.ASSEMBLER_RECIPES.builder("organ_heart_tier_" + tier)
                    .inputItems(motor, scaleOperator.applyAsInt(2))
                    .inputItems(emitter, scaleOperator.applyAsInt(2))
                    .inputItems(sensor, scaleOperator.applyAsInt(2))
                    .inputItems(field_generator, scaleOperator.applyAsInt(1))
                    .inputItems(circuitTag, scaleOperator.applyAsInt(4))
                    .inputItems(piston, scaleOperator.applyAsInt(2))
                    .inputItems(pump, scaleOperator.applyAsInt(2))
                    .outputItems(GTOOrganItems.TierOrganMap[OrganType.Heart]!![OrganTier].asStack())
                    .EUt(GTValues.VA[tier].toLong())
                    .duration(1200)
                    .circuitMeta(5)
                    .save()
                GTORecipeTypes.ASSEMBLER_RECIPES.builder("organ_eyes_tier_" + tier)
                    .inputItems(motor, scaleOperator.applyAsInt(1))
                    .inputItems(emitter, scaleOperator.applyAsInt(1))
                    .inputItems(sensor, scaleOperator.applyAsInt(2))
                    .inputItems(circuitTag, scaleOperator.applyAsInt(4))
                    .outputItems(GTOOrganItems.TierOrganMap[OrganType.Eye]!![OrganTier].asStack())
                    .EUt(GTValues.VA[tier].toLong())
                    .duration(1200)
                    .circuitMeta(6)
                    .save()
                GTORecipeTypes.ASSEMBLER_RECIPES.builder("organ_lungs_tier_" + tier)
                    .inputItems(motor, scaleOperator.applyAsInt(2))
                    .inputItems(robot_arm, scaleOperator.applyAsInt(4))
                    .inputItems(emitter, scaleOperator.applyAsInt(1))
                    .inputItems(sensor, scaleOperator.applyAsInt(1))
                    .inputItems(piston, scaleOperator.applyAsInt(2))
                    .inputItems(pump, scaleOperator.applyAsInt(2))
                    .inputItems(field_generator, scaleOperator.applyAsInt(1))
                    .inputItems(circuitTag, scaleOperator.applyAsInt(4))
                    .outputItems(GTOOrganItems.TierOrganMap[OrganType.Lung]!![OrganTier].asStack())
                    .EUt(GTValues.VA[tier].toLong())
                    .duration(1200)
                    .circuitMeta(7)
                    .save()
                GTORecipeTypes.ASSEMBLER_RECIPES.builder("organ_liver_tier_" + tier)
                    .inputItems(emitter, scaleOperator.applyAsInt(1))
                    .inputItems(sensor, scaleOperator.applyAsInt(2))
                    .inputItems(field_generator, scaleOperator.applyAsInt(2))
                    .inputItems(pump, scaleOperator.applyAsInt(2))
                    .inputItems(circuitTag, scaleOperator.applyAsInt(4))
                    .outputItems(GTOOrganItems.TierOrganMap[OrganType.Liver]!![OrganTier].asStack())
                    .EUt(GTValues.VA[tier].toLong())
                    .duration(1200)
                    .circuitMeta(8)
                    .save()
                GTORecipeTypes.ASSEMBLER_RECIPES.builder("organ_spine_tier_" + tier)
                    .inputItems(robot_arm, scaleOperator.applyAsInt(4))
                    .inputItems(emitter, scaleOperator.applyAsInt(1))
                    .inputItems(circuitTag, scaleOperator.applyAsInt(1))
                    .outputItems(GTOOrganItems.TierOrganMap[OrganType.Spine]!![OrganTier].asStack())
                    .EUt(GTValues.VA[tier].toLong())
                    .duration(1200)
                    .circuitMeta(9)
                    .save()
            },
        )
    }
}
