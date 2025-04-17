package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.utils.TagUtils;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;

import appeng.core.definitions.AEItems;

import static com.gto.gtocore.common.data.GTORecipeTypes.RARITY_FORGE_RECIPES;

interface RarityForge {

    static void init() {
        RARITY_FORGE_RECIPES.builder("gem_dust")
                .inputItems(TagUtils.createTGTag("flawless_gems"))
                .inputItems("apotheosis:gem_fused_slate", 1)
                .outputItems("apotheosis:gem_dust", 2)
                .EUt(8)
                .duration(400)
                .save();

        RARITY_FORGE_RECIPES.builder("common_material")
                .inputItems("gtceu:small_iron_dust")
                .inputItems("gtceu:small_wrought_iron_dust")
                .inputItems("gtceu:small_steel_dust")
                .outputItems("apotheosis:common_material")
                .EUt(32)
                .duration(200)
                .save();

        RARITY_FORGE_RECIPES.builder("uncommon_material")
                .inputItems("apotheosis:common_material", 8)
                .inputItems(Items.LEATHER.asItem())
                .inputItems("apotheosis:gem_dust")
                .outputItems("apotheosis:uncommon_material")
                .EUt(8)
                .duration(200)
                .save();

        RARITY_FORGE_RECIPES.builder("rare_material")
                .inputItems("apotheosis:uncommon_material", 8)
                .inputItems(TagPrefix.gem, GTMaterials.Amethyst)
                .inputItems("apotheosis:gem_dust")
                .outputItems("apotheosis:rare_material")
                .EUt(16)
                .duration(200)
                .save();

        RARITY_FORGE_RECIPES.builder("epic_material")
                .inputItems("apotheosis:rare_material", 8)
                .inputItems(Items.PRISMARINE_CRYSTALS.asItem())
                .inputItems("apotheosis:gem_dust")
                .outputItems("apotheosis:epic_material")
                .EUt(32)
                .duration(200)
                .save();

        RARITY_FORGE_RECIPES.builder("mythic_material")
                .inputItems("apotheosis:epic_material", 8)
                .inputItems(AEItems.FLUIX_PEARL.asItem())
                .inputItems("apotheosis:gem_dust")
                .outputItems("apotheosis:mythic_material")
                .EUt(64)
                .duration(200)
                .save();

        RARITY_FORGE_RECIPES.builder("speed_upgrade_module")
                .inputItems(GTOItems.LV_POWER_AMPLIFIERS.asItem())
                .inputItems(TagPrefix.gem, GTOMaterials.Fluix)
                .outputItems(GTOItems.SPEED_UPGRADE_MODULE.asItem())
                .EUt(8)
                .duration(400)
                .save();

        RARITY_FORGE_RECIPES.builder("energy_upgrade_module")
                .inputItems(GTOItems.LV_POWER_AMPLIFIERS.asItem())
                .inputItems(AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED.asItem())
                .outputItems(GTOItems.ENERGY_UPGRADE_MODULE.asItem())
                .EUt(8)
                .duration(400)
                .save();
    }
}
