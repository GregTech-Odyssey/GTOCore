package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.HEAT_EXCHANGER_RECIPES;

interface HeatExchanger {

    static void init() {
        HEAT_EXCHANGER_RECIPES.recipeBuilder(GTOCore.id("hot_sodium_potassium"))
                .inputFluids(GTOMaterials.HotSodiumPotassium.getFluid(1))
                .inputFluids(GTMaterials.Water.getFluid(160))
                .outputFluids(GTMaterials.SodiumPotassium.getFluid(1))
                .outputFluids(GTMaterials.Steam.getFluid(25600))
                .outputFluids(GTOMaterials.HighPressureSteam.getFluid(6400))
                .duration(200)
                .save();

        HEAT_EXCHANGER_RECIPES.recipeBuilder(GTOCore.id("supercritical_sodium_potassium"))
                .inputFluids(GTOMaterials.SupercriticalSodiumPotassium.getFluid(1))
                .inputFluids(GTMaterials.DistilledWater.getFluid(80))
                .outputFluids(GTMaterials.SodiumPotassium.getFluid(1))
                .outputFluids(GTOMaterials.HighPressureSteam.getFluid(3200))
                .outputFluids(GTOMaterials.SupercriticalSteam.getFluid(800))
                .duration(200)
                .save();
    }
}
