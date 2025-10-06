package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.GAS_COMPRESSOR_RECIPES;

final class GasCompressor {

    public static void init() {
        GAS_COMPRESSOR_RECIPES.builder("high_pressure_nitrogen")
                .inputFluids(GTMaterials.Nitrogen, 1000)
                .outputFluids(GTOMaterials.HighPressureNitrogen, 1000)
                .EUt(30)
                .duration(600)
                .save();

        GAS_COMPRESSOR_RECIPES.builder("high_pressure_hydrogen")
                .inputFluids(GTMaterials.Hydrogen, 1000)
                .outputFluids(GTOMaterials.HighPressureHydrogen, 1000)
                .EUt(480)
                .duration(400)
                .save();

        GAS_COMPRESSOR_RECIPES.builder("high_pressure_helium")
                .inputFluids(GTMaterials.Helium, 1000)
                .outputFluids(GTOMaterials.HighPressureHelium, 1000)
                .EUt(222)
                .duration(600)
                .save();

        GAS_COMPRESSOR_RECIPES.builder("high_pressure_neon")
                .inputFluids(GTMaterials.Neon, 1000)
                .outputFluids(GTOMaterials.HighPressureNeon, 1000)
                .EUt(222)
                .duration(600)
                .save();

        GAS_COMPRESSOR_RECIPES.builder("high_pressure_argon")
                .inputFluids(GTMaterials.Argon, 1000)
                .outputFluids(GTOMaterials.HighPressureArgon, 1000)
                .EUt(222)
                .duration(600)
                .save();

        GAS_COMPRESSOR_RECIPES.builder("high_pressure_krypton")
                .inputFluids(GTMaterials.Krypton, 1000)
                .outputFluids(GTOMaterials.HighPressureKrypton, 1000)
                .EUt(222)
                .duration(600)
                .save();

        GAS_COMPRESSOR_RECIPES.builder("high_pressure_oxygen")
                .inputFluids(GTMaterials.Oxygen, 1000)
                .outputFluids(GTOMaterials.HighPressureOxygen, 1000)
                .EUt(120)
                .duration(400)
                .save();

        GAS_COMPRESSOR_RECIPES.builder("high_pressure_steam")
                .inputFluids(GTMaterials.Steam, 120000)
                .outputFluids(GTOMaterials.HighPressureSteam, 30000)
                .EUt(120)
                .duration(1000)
                .save();
    }
}
