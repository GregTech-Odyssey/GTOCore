package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.machines.MultiBlockG;

import com.gtolib.api.data.GTODimensions;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.SCANNER_RECIPES;

final class Scanner {

    public static void init() {
        SCANNER_RECIPES.recipeBuilder("nether_reactor_core")
                .notConsumable(GTOItems.DIMENSION_DATA.get().getDimensionData(GTODimensions.THE_NETHER))
                .inputItems(MultiBlockG.ANCIENT_REACTOR_CORE.asItem())
                .inputFluids(GTMaterials.Wax.getFluid(2304))
                .outputItems(MultiBlockG.NETHER_REACTOR_CORE.asItem())
                .EUt(120)
                .duration(1200)
                .save();

        SCANNER_RECIPES.recipeBuilder("planet_data_chip")
                .notConsumable(GTOItems.PLANET_DATA_CHIP.get())
                .EUt(120)
                .duration(600)
                .save();
    }
}
