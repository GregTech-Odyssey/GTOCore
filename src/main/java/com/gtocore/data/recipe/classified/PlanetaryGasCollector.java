package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.data.Dimension;

import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;

import static com.gtocore.common.data.GTORecipeTypes.SPACE_GAS_COLLECTOR_RECIPES;

final class PlanetaryGasCollector {

    public static void init() {
        SPACE_GAS_COLLECTOR_RECIPES.recipeBuilder("1")
                .dimension(Dimension.OVERWORLD.getOrbit())
                .circuitMeta(1)
                .outputFluids(GTMaterials.Air, 256000)
                .EUt(120)
                .duration(40)
                .save();

        SPACE_GAS_COLLECTOR_RECIPES.recipeBuilder("2")
                .dimension(Dimension.OVERWORLD.getOrbit())
                .circuitMeta(2)
                .outputFluids(GTMaterials.EnderAir, 256000)
                .EUt(1920)
                .duration(40)
                .save();

        SPACE_GAS_COLLECTOR_RECIPES.recipeBuilder("3")
                .dimension(Dimension.OVERWORLD.getOrbit())
                .circuitMeta(3)
                .outputFluids(GTMaterials.NetherAir, 256000)
                .EUt(480)
                .duration(40)
                .save();

        SPACE_GAS_COLLECTOR_RECIPES.recipeBuilder("4")
                .dimension(Dimension.OVERWORLD.getOrbit())
                .notConsumable(GTMultiMachines.VACUUM_FREEZER.asItem())
                .circuitMeta(4)
                .outputFluids(GTMaterials.LiquidNetherAir, 256000)
                .EUt(7680)
                .duration(160)
                .save();

        SPACE_GAS_COLLECTOR_RECIPES.recipeBuilder("5")
                .dimension(Dimension.OVERWORLD.getOrbit())
                .notConsumable(GTMultiMachines.VACUUM_FREEZER.asItem())
                .circuitMeta(5)
                .outputFluids(GTMaterials.LiquidAir, 256000)
                .EUt(1920)
                .duration(160)
                .save();

        SPACE_GAS_COLLECTOR_RECIPES.recipeBuilder("6")
                .dimension(Dimension.OVERWORLD.getOrbit())
                .notConsumable(GTMultiMachines.VACUUM_FREEZER.asItem())
                .circuitMeta(6)
                .outputFluids(GTMaterials.LiquidEnderAir, 256000)
                .EUt(30720)
                .duration(160)
                .save();

        SPACE_GAS_COLLECTOR_RECIPES.recipeBuilder("bnd")
                .dimension(Dimension.BARNARDA_C.getOrbit())
                .circuitMeta(7)
                .outputFluids(GTOMaterials.BarnardaAir, 256000)
                .EUt(122880)
                .duration(40)
                .save();
    }
}
