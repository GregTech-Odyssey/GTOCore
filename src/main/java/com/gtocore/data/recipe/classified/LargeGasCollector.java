package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;

import com.gtolib.api.data.GTODimensions;

import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;

import static com.gtocore.common.data.GTORecipeTypes.LARGE_GAS_COLLECTOR_RECIPES;

final class LargeGasCollector {

    public static void init() {
        LARGE_GAS_COLLECTOR_RECIPES.recipeBuilder("1")
                .notConsumable(GTOItems.DIMENSION_DATA.get().getDimensionData(GTODimensions.OVERWORLD))
                .circuitMeta(1)
                .outputFluids(GTMaterials.Air.getFluid(100000))
                .EUt(120)
                .duration(200)
                .save();

        LARGE_GAS_COLLECTOR_RECIPES.recipeBuilder("3")
                .notConsumable(GTOItems.DIMENSION_DATA.get().getDimensionData(GTODimensions.THE_END))
                .circuitMeta(1)
                .outputFluids(GTMaterials.EnderAir.getFluid(100000))
                .EUt(1920)
                .duration(200)
                .save();

        LARGE_GAS_COLLECTOR_RECIPES.recipeBuilder("2")
                .notConsumable(GTOItems.DIMENSION_DATA.get().getDimensionData(GTODimensions.THE_NETHER))
                .circuitMeta(1)
                .outputFluids(GTMaterials.NetherAir.getFluid(100000))
                .EUt(480)
                .duration(200)
                .save();

        LARGE_GAS_COLLECTOR_RECIPES.recipeBuilder("5")
                .notConsumable(GTOItems.DIMENSION_DATA.get().getDimensionData(GTODimensions.THE_NETHER))
                .notConsumable(GTMultiMachines.VACUUM_FREEZER.asItem())
                .outputFluids(GTMaterials.LiquidNetherAir.getFluid(100000))
                .EUt(1920)
                .duration(2000)
                .save();

        LARGE_GAS_COLLECTOR_RECIPES.recipeBuilder("4")
                .notConsumable(GTOItems.DIMENSION_DATA.get().getDimensionData(GTODimensions.OVERWORLD))
                .notConsumable(GTMultiMachines.VACUUM_FREEZER.asItem())
                .outputFluids(GTMaterials.LiquidAir.getFluid(100000))
                .EUt(480)
                .duration(2000)
                .save();

        LARGE_GAS_COLLECTOR_RECIPES.recipeBuilder("6")
                .notConsumable(GTOItems.DIMENSION_DATA.get().getDimensionData(GTODimensions.THE_END))
                .notConsumable(GTMultiMachines.VACUUM_FREEZER.asItem())
                .outputFluids(GTMaterials.LiquidEnderAir.getFluid(100000))
                .EUt(7680)
                .duration(2000)
                .save();
    }
}
