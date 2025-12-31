package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.HEAT_EXCHANGER_RECIPES;

final class HeatExchanger {

    public static void init() {
        HEAT_EXCHANGER_RECIPES.recipeBuilder("hot_sodium_potassium")
                .inputFluids(GTOMaterials.HotSodiumPotassium, 1)
                .inputFluids(GTMaterials.Water, 160)
                .outputFluids(GTMaterials.SodiumPotassium, 1)
                .outputFluids(GTMaterials.Steam, 25600)
                .outputFluids(GTOMaterials.HighPressureSteam, 6400)
                .duration(200)
                .addData("eu", 12800)
                .save();

        HEAT_EXCHANGER_RECIPES.recipeBuilder("supercritical_sodium_potassium")
                .inputFluids(GTOMaterials.SupercriticalSodiumPotassium, 1)
                .inputFluids(GTMaterials.DistilledWater, 160)
                .outputFluids(GTMaterials.SodiumPotassium, 1)
                .outputFluids(GTOMaterials.HighPressureSteam, 6400)
                .outputFluids(GTOMaterials.SupercriticalSteam, 1600)
                .duration(200)
                .addData("eu", 12800)
                .save();
    }
}
