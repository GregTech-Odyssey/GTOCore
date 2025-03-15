package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeCategories;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import com.enderio.base.common.init.EIOItems;

import static com.gto.gtocore.common.data.GTORecipeTypes.ALLOY_SMELTER_RECIPES;

interface AlloySmelter {

    static void init() {
        ALLOY_SMELTER_RECIPES.recipeBuilder(GTOCore.id("vibrant_gear"))
                .inputItems(EIOItems.GEAR_ENERGIZED.asItem())
                .inputItems(TagPrefix.ingot, GTOMaterials.VibrantAlloy, 4)
                .outputItems(EIOItems.GEAR_VIBRANT.asItem())
                .EUt(16)
                .duration(160)
                .save();

        ALLOY_SMELTER_RECIPES.recipeBuilder(GTOCore.id("infinity_gear"))
                .inputItems(TagPrefix.gear, GTMaterials.Iron)
                .inputItems(EIOItems.GRAINS_OF_INFINITY.asStack(2))
                .outputItems(EIOItems.GEAR_IRON.asItem())
                .EUt(16)
                .duration(80)
                .save();

        ALLOY_SMELTER_RECIPES.recipeBuilder(GTOCore.id("soularium_ingot"))
                .inputItems(TagPrefix.ingot, GTMaterials.Gold)
                .inputItems(new ItemStack(Blocks.SOUL_SAND.asItem()))
                .outputItems(TagPrefix.ingot, GTOMaterials.Soularium)
                .EUt(16)
                .duration(200)
                .save();

        ALLOY_SMELTER_RECIPES.recipeBuilder(GTOCore.id("dark_bimetal_gear"))
                .inputItems(EIOItems.GEAR_IRON.asItem())
                .inputItems(TagPrefix.ingot, GTOMaterials.DarkSteel, 4)
                .outputItems(EIOItems.GEAR_DARK_STEEL.asItem())
                .EUt(16)
                .duration(160)
                .save();

        ALLOY_SMELTER_RECIPES.recipeBuilder(GTOCore.id("netherite_ingot"))
                .inputItems(new ItemStack(Blocks.NETHERITE_BLOCK.asItem()))
                .notConsumable(GTItems.SHAPE_MOLD_INGOT.asItem())
                .outputItems(TagPrefix.ingot, GTMaterials.Netherite, 9)
                .EUt(7)
                .duration(1188)
                .category(GTRecipeCategories.INGOT_MOLDING)
                .save();

        ALLOY_SMELTER_RECIPES.recipeBuilder(GTOCore.id("energetic_gear"))
                .inputItems(EIOItems.GEAR_IRON.asItem())
                .inputItems(TagPrefix.ingot, GTOMaterials.EnergeticAlloy, 4)
                .outputItems(EIOItems.GEAR_ENERGIZED.asItem())
                .EUt(16)
                .duration(120)
                .save();
    }
}
