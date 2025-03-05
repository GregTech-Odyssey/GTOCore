package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.common.data.GTMaterials;

interface HeatExchanger {

    static void init() {
        GTORecipeTypes.HEAT_EXCHANGER_RECIPES.recipeBuilder(GTOCore.id("hot_sodium_potassium"))
                .inputFluids(GTOMaterials.HotSodiumPotassium.getFluid(1))
                .inputFluids(GTMaterials.Water.getFluid(160))
                .outputFluids(GTMaterials.SodiumPotassium.getFluid(1))
                .outputFluids(GTMaterials.Steam.getFluid(25600))
                .duration(200)
                .save();

        GTORecipeTypes.HEAT_EXCHANGER_RECIPES.recipeBuilder(GTOCore.id("supercritical_sodium_potassium"))
                .inputFluids(GTOMaterials.SupercriticalSodiumPotassium.getFluid(1))
                .inputFluids(GTMaterials.DistilledWater.getFluid(80))
                .outputFluids(GTMaterials.SodiumPotassium.getFluid(1))
                .outputFluids(GTMaterials.Steam.getFluid(12800))
                .outputFluids(GTOMaterials.SupercriticalSteam.getFluid(800))
                .duration(200)
                .save();
    }
}
