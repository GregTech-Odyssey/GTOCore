package com.gto.gtocore.data.recipe.generated;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.block.ColorBlockMap;
import com.gto.gtocore.common.data.GTOBlocks;

import com.gregtechceu.gtceu.common.data.GTRecipeCategories;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

import static com.gregtechceu.gtceu.common.data.GTMaterials.CHEMICAL_DYES;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.CHEMICAL_BATH_RECIPES;

public final class DyeRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        for (int i = 0; i < CHEMICAL_DYES.length; i++) {
            DyeColor color = DyeColor.values()[i];
            String colorName = color.getName();
            Block abs = ColorBlockMap.ABS_MAP.get(color);
            if (abs != null && color != DyeColor.WHITE) {
                CHEMICAL_BATH_RECIPES.recipeBuilder(GTOCore.id("abs_%s".formatted(colorName)))
                        .inputItems(GTOBlocks.ABS_WHITE_CASING.asStack())
                        .inputFluids(CHEMICAL_DYES[i].getFluid(144))
                        .outputItems(abs.asItem())
                        .EUt(7).duration(200)
                        .category(GTRecipeCategories.CHEM_DYES)
                        .save(provider);
            }
        }
    }
}
