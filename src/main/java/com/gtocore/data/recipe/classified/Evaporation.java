package com.gtocore.data.recipe.classified;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.EVAPORATION_RECIPES;

final class Evaporation {

    public static void init() {
        EVAPORATION_RECIPES.recipeBuilder("salt_water")
                .inputFluids(GTMaterials.Water, 50000)
                .outputFluids(GTMaterials.SaltWater, 1000)
                .EUt(30)
                .duration(600)
                .save();
    }
}
