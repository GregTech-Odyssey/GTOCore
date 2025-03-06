package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.GTODimensions;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.GAS_COLLECTOR_RECIPES;

interface GasCollector {

    static void init() {
        GAS_COLLECTOR_RECIPES.recipeBuilder(GTOCore.id("barnarda_c"))
                .circuitMeta(6)
                .outputFluids(GTOMaterials.BarnardaAir.getFluid(10000))
                .EUt(1024)
                .duration(200)
                .dimension(GTODimensions.BARNARDA_C)
                .save();

        GAS_COLLECTOR_RECIPES.recipeBuilder(GTOCore.id("flat"))
                .circuitMeta(5)
                .outputFluids(GTMaterials.Air.getFluid(10000))
                .EUt(16)
                .duration(200)
                .dimension(GTODimensions.FLAT)
                .save();

        GAS_COLLECTOR_RECIPES.recipeBuilder(GTOCore.id("void"))
                .circuitMeta(4)
                .outputFluids(GTMaterials.Air.getFluid(10000))
                .EUt(16)
                .duration(200)
                .dimension(GTODimensions.VOID)
                .save();
    }
}
