package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.machines.MultiBlockG;

import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface Scanner {

    static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.SCANNER_RECIPES.recipeBuilder(GTOCore.id("nether_reactor_core"))
                .notConsumable(GTOItems.DIMENSION_DATA.get().getDimensionData(GTODimensions.THE_NETHER))
                .inputItems(MultiBlockG.ANCIENT_REACTOR_CORE.asStack())
                .inputFluids(GTMaterials.Wax.getFluid(2304))
                .outputItems(MultiBlockG.NETHER_REACTOR_CORE.asStack())
                .EUt(480)
                .duration(1200)
                .save(provider);

        GTRecipeTypes.SCANNER_RECIPES.recipeBuilder(GTOCore.id("planet_data_chip"))
                .notConsumable(GTOItems.PLANET_DATA_CHIP.get())
                .EUt(120)
                .duration(600)
                .save(provider);
    }
}
