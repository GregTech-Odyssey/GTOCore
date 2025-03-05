package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.common.data.GTMaterials;

interface Evaporation {

    static void init() {
        GTORecipeTypes.EVAPORATION_RECIPES.recipeBuilder(GTOCore.id("salt_water"))
                .inputFluids(GTMaterials.Water.getFluid(50000))
                .outputFluids(GTMaterials.SaltWater.getFluid(1000))
                .EUt(30)
                .duration(600)
                .save();
    }
}
