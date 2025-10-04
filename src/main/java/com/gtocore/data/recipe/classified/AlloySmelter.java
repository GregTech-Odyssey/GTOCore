package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeCategories;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import com.enderio.base.common.init.EIOItems;

import static com.gtocore.common.data.GTORecipeTypes.ALLOY_SMELTER_RECIPES;

final class AlloySmelter {

    public static void init() {
        ALLOY_SMELTER_RECIPES.builder("mica_insulator_sheet")
                .inputItems(GTOItems.MICA_BASED_SHEET.asItem(), 5)
                .inputItems(TagPrefix.dust, GTMaterials.SiliconDioxide, 3)
                .outputItems(GTOItems.MICA_INSULATOR_SHEET.asItem(), 5)
                .EUt(30)
                .duration(400)
                .save();

        ALLOY_SMELTER_RECIPES.recipeBuilder("vibrant_gear")
                .inputItems(EIOItems.GEAR_ENERGIZED.asItem())
                .inputItems(TagPrefix.ingot, GTOMaterials.VibrantAlloy, 4)
                .outputItems(EIOItems.GEAR_VIBRANT.asItem())
                .EUt(16)
                .duration(160)
                .save();

        ALLOY_SMELTER_RECIPES.recipeBuilder("infinity_gear")
                .inputItems(TagPrefix.gear, GTMaterials.Iron)
                .inputItems(EIOItems.GRAINS_OF_INFINITY.asItem(), 2)
                .outputItems(EIOItems.GEAR_IRON.asItem())
                .EUt(16)
                .duration(80)
                .save();

        ALLOY_SMELTER_RECIPES.recipeBuilder("soularium_ingot")
                .inputItems(TagPrefix.ingot, GTMaterials.Gold)
                .inputItems(Blocks.SOUL_SAND.asItem())
                .outputItems(TagPrefix.ingot, GTOMaterials.Soularium)
                .EUt(16)
                .duration(200)
                .save();

        ALLOY_SMELTER_RECIPES.recipeBuilder("dark_bimetal_gear")
                .inputItems(EIOItems.GEAR_IRON.asItem())
                .inputItems(TagPrefix.ingot, GTOMaterials.DarkSteel, 4)
                .outputItems(EIOItems.GEAR_DARK_STEEL.asItem())
                .EUt(16)
                .duration(160)
                .save();

        ALLOY_SMELTER_RECIPES.recipeBuilder("netherite_ingot")
                .inputItems(Blocks.NETHERITE_BLOCK.asItem())
                .notConsumable(GTItems.SHAPE_MOLD_INGOT.asItem())
                .outputItems(TagPrefix.ingot, GTMaterials.Netherite, 9)
                .EUt(7)
                .duration(1188)
                .category(GTRecipeCategories.INGOT_MOLDING)
                .save();

        ALLOY_SMELTER_RECIPES.recipeBuilder("energetic_gear")
                .inputItems(EIOItems.GEAR_IRON.asItem())
                .inputItems(TagPrefix.ingot, GTOMaterials.EnergeticAlloy, 4)
                .outputItems(EIOItems.GEAR_ENERGIZED.asItem())
                .EUt(16)
                .duration(120)
                .save();

        ALLOY_SMELTER_RECIPES.builder("slime_ball")
                .inputItems(GTItems.STICKY_RESIN.asItem())
                .inputItems(Items.CACTUS.asItem())
                .outputItems(Items.SLIME_BALL.asItem())
                .EUt(16)
                .duration(100)
                .save();

        ALLOY_SMELTER_RECIPES.builder("clayed_glowstone")
                .inputItems(TagPrefix.dust, GTMaterials.Clay)
                .inputItems(TagPrefix.dust, GTMaterials.Glowstone)
                .outputItems("enderio:clayed_glowstone", 2)
                .EUt(480)
                .duration(200)
                .save();
    }
}
