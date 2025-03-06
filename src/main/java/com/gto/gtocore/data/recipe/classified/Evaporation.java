package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.EVAPORATION_RECIPES;

interface Evaporation {

    static void init() {
        EVAPORATION_RECIPES.recipeBuilder(GTOCore.id("salt_water"))
                .inputFluids(GTMaterials.Water.getFluid(50000))
                .outputFluids(GTMaterials.SaltWater.getFluid(1000))
                .EUt(30)
                .duration(600)
                .save();
    }
}
