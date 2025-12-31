package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeCategories;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.EnderPearl;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Iron;
import static com.gtocore.common.data.GTOMaterials.PulsatingAlloy;
import static com.gtocore.common.data.GTORecipeTypes.ALLOY_SMELTER_RECIPES;

final class AlloySmelter {

    public static void init() {
        ALLOY_SMELTER_RECIPES.builder("mica_insulator_sheet")
                .inputItems(GTOItems.MICA_BASED_SHEET, 5)
                .inputItems(TagPrefix.dust, GTMaterials.SiliconDioxide, 3)
                .outputItems(GTOItems.MICA_INSULATOR_SHEET, 5)
                .EUt(30)
                .duration(400)
                .save();

        ALLOY_SMELTER_RECIPES.recipeBuilder("soularium_ingot")
                .inputItems(TagPrefix.ingot, GTMaterials.Gold)
                .inputItems(Blocks.SOUL_SAND.asItem())
                .outputItems(TagPrefix.ingot, GTOMaterials.Soularium)
                .EUt(16)
                .duration(200)
                .save();

        ALLOY_SMELTER_RECIPES.recipeBuilder("netherite_ingot")
                .inputItems(Blocks.NETHERITE_BLOCK.asItem())
                .notConsumable(GTItems.SHAPE_MOLD_INGOT.asItem())
                .outputItems(TagPrefix.ingot, GTMaterials.Netherite, 9)
                .EUt(7)
                .duration(1188)
                .category(GTRecipeCategories.INGOT_MOLDING)
                .save();

        ALLOY_SMELTER_RECIPES.builder("slime_ball")
                .inputItems(GTItems.STICKY_RESIN)
                .inputItems(Items.CACTUS.asItem())
                .outputItems(Items.SLIME_BALL.asItem())
                .EUt(16)
                .duration(100)
                .save();

        ALLOY_SMELTER_RECIPES.builder("pulsating_alloy_ingot")
                .inputItems(ChemicalHelper.get(dust, Iron), ChemicalHelper.get(gem, EnderPearl))
                .outputItems(ChemicalHelper.get(ingot, PulsatingAlloy))
                .EUt(7)
                .duration(100)
                .save();

        ALLOY_SMELTER_RECIPES.builder("pulsating_alloy_ingot1")
                .inputItems(ChemicalHelper.get(ingot, Iron), ChemicalHelper.get(gem, EnderPearl))
                .outputItems(ChemicalHelper.get(ingot, PulsatingAlloy))
                .EUt(7)
                .duration(100)
                .save();
    }
}
