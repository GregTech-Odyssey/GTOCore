package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.HEAT_EXCHANGER_RECIPES;

final class HeatExchanger {

    public static void init() {
        HEAT_EXCHANGER_RECIPES.recipeBuilder("hot_sodium_potassium")
                .inputFluids(GTOMaterials.HotSodiumPotassium.getFluid(1))
                .inputFluids(GTMaterials.Water.getFluid(160))
                .outputFluids(GTMaterials.SodiumPotassium.getFluid(1))
                .outputFluids(GTMaterials.Steam.getFluid(25600))
                .outputFluids(GTOMaterials.HighPressureSteam.getFluid(6400))
                .duration(200)
                .addData("eu", 12800)
                .save();

        HEAT_EXCHANGER_RECIPES.recipeBuilder("supercritical_sodium_potassium")
                .inputFluids(GTOMaterials.SupercriticalSodiumPotassium.getFluid(1))
                .inputFluids(GTMaterials.DistilledWater.getFluid(160))
                .outputFluids(GTMaterials.SodiumPotassium.getFluid(1))
                .outputFluids(GTOMaterials.HighPressureSteam.getFluid(6400))
                .outputFluids(GTOMaterials.SupercriticalSteam.getFluid(1600))
                .duration(200)
                .addData("eu", 12800)
                .save();
    }
}
