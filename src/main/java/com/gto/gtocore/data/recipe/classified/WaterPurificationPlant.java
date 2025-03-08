package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.WATER_PURIFICATION_PLANT_RECIPES;

interface WaterPurificationPlant {

    static void init() {
        WATER_PURIFICATION_PLANT_RECIPES.recipeBuilder(GTOCore.id("a"))
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputFluids(GTOMaterials.FilteredSater.getFluid(1000))
                .duration(2400)
                .addData("tier", 1)
                .save();

        WATER_PURIFICATION_PLANT_RECIPES.recipeBuilder(GTOCore.id("b"))
                .inputFluids(GTOMaterials.FilteredSater.getFluid(1000))
                .outputFluids(GTOMaterials.OzoneWater.getFluid(1000))
                .duration(2400)
                .addData("tier", 2)
                .save();

        WATER_PURIFICATION_PLANT_RECIPES.recipeBuilder(GTOCore.id("c"))
                .inputFluids(GTOMaterials.OzoneWater.getFluid(1000))
                .outputFluids(GTOMaterials.FlocculentWater.getFluid(1000))
                .duration(2400)
                .addData("tier", 3)
                .save();

        WATER_PURIFICATION_PLANT_RECIPES.recipeBuilder(GTOCore.id("d"))
                .inputFluids(GTOMaterials.FlocculentWater.getFluid(1000))
                .outputFluids(GTOMaterials.PHNeutralWater.getFluid(1000))
                .duration(2400)
                .addData("tier", 4)
                .save();

        WATER_PURIFICATION_PLANT_RECIPES.recipeBuilder(GTOCore.id("e"))
                .inputFluids(GTOMaterials.PHNeutralWater.getFluid(1000))
                .outputFluids(GTOMaterials.ExtremeTemperatureWater.getFluid(1000))
                .duration(2400)
                .addData("tier", 5)
                .save();

        WATER_PURIFICATION_PLANT_RECIPES.recipeBuilder(GTOCore.id("f"))
                .inputFluids(GTOMaterials.ExtremeTemperatureWater.getFluid(1000))
                .outputFluids(GTOMaterials.ElectricEquilibriumWater.getFluid(1000))
                .duration(2400)
                .addData("tier", 6)
                .save();

        WATER_PURIFICATION_PLANT_RECIPES.recipeBuilder(GTOCore.id("g"))
                .inputFluids(GTOMaterials.ElectricEquilibriumWater.getFluid(1000))
                .outputFluids(GTOMaterials.DegassedWater.getFluid(1000))
                .duration(2400)
                .addData("tier", 7)
                .save();

        WATER_PURIFICATION_PLANT_RECIPES.recipeBuilder(GTOCore.id("h"))
                .inputFluids(GTOMaterials.DegassedWater.getFluid(1000))
                .outputFluids(GTOMaterials.BaryonicPerfectionWater.getFluid(1000))
                .duration(2400)
                .addData("tier", 8)
                .save();
    }
}
