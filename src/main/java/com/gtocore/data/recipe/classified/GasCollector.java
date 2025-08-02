package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.data.GTODimensions;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.GAS_COLLECTOR_RECIPES;

final class GasCollector {

    public static void init() {
        GAS_COLLECTOR_RECIPES.recipeBuilder("barnarda_c")
                .circuitMeta(6)
                .outputFluids(GTOMaterials.BarnardaAir.getFluid(10000))
                .EUt(1024)
                .duration(20)
                .dimension(GTODimensions.BARNARDA_C)
                .save();

        GAS_COLLECTOR_RECIPES.recipeBuilder("flat")
                .circuitMeta(5)
                .outputFluids(GTMaterials.Air.getFluid(10000))
                .EUt(16)
                .duration(20)
                .dimension(GTODimensions.FLAT)
                .save();

        GAS_COLLECTOR_RECIPES.recipeBuilder("void")
                .circuitMeta(4)
                .outputFluids(GTMaterials.Air.getFluid(10000))
                .EUt(16)
                .duration(20)
                .dimension(GTODimensions.VOID)
                .save();
    }
}
