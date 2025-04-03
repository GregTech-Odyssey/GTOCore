package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.GAS_COMPRESSOR_RECIPES;

interface GasCompressor {

    static void init() {
        GAS_COMPRESSOR_RECIPES.builder("high_pressure_nitrogen")
                .inputFluids(GTMaterials.Nitrogen, 1000)
                .outputFluids(GTOMaterials.HighPressureNitrogen, 1000)
                .EUt(30)
                .duration(600)
                .save();

        GAS_COMPRESSOR_RECIPES.builder("high_pressure_steam")
                .inputFluids(GTMaterials.Steam, 120000)
                .outputFluids(GTOMaterials.HighPressureSteam, 30000)
                .EUt(120)
                .duration(1000)
                .save();
    }
}
