package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.GTOWorldGenLayers;
import com.gto.gtocore.init.GTOItems;
import com.gto.gtocore.init.GTORecipeTypes;

import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface LargeGasCollector {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.LARGE_GAS_COLLECTOR_RECIPES.recipeBuilder(GTOCore.id("1"))
                .notConsumable(GTOItems.DIMENSION_DATA.get().setDimension(GTOWorldGenLayers.OVERWORLD))
                .circuitMeta(1)
                .outputFluids(GTMaterials.Air.getFluid(100000))
                .EUt(120)
                .duration(200)
                .save(provider);

        GTORecipeTypes.LARGE_GAS_COLLECTOR_RECIPES.recipeBuilder(GTOCore.id("3"))
                .notConsumable(GTOItems.DIMENSION_DATA.get().setDimension(GTOWorldGenLayers.THE_END))
                .circuitMeta(1)
                .outputFluids(GTMaterials.EnderAir.getFluid(100000))
                .EUt(1920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.LARGE_GAS_COLLECTOR_RECIPES.recipeBuilder(GTOCore.id("2"))
                .notConsumable(GTOItems.DIMENSION_DATA.get().setDimension(GTOWorldGenLayers.THE_NETHER))
                .circuitMeta(1)
                .outputFluids(GTMaterials.NetherAir.getFluid(100000))
                .EUt(480)
                .duration(200)
                .save(provider);

        GTORecipeTypes.LARGE_GAS_COLLECTOR_RECIPES.recipeBuilder(GTOCore.id("5"))
                .notConsumable(GTOItems.DIMENSION_DATA.get().setDimension(GTOWorldGenLayers.THE_NETHER))
                .notConsumable(GTMultiMachines.VACUUM_FREEZER.asStack())
                .outputFluids(GTMaterials.LiquidNetherAir.getFluid(100000))
                .EUt(1920)
                .duration(2000)
                .save(provider);

        GTORecipeTypes.LARGE_GAS_COLLECTOR_RECIPES.recipeBuilder(GTOCore.id("4"))
                .notConsumable(GTOItems.DIMENSION_DATA.get().setDimension(GTOWorldGenLayers.OVERWORLD))
                .notConsumable(GTMultiMachines.VACUUM_FREEZER.asStack())
                .outputFluids(GTMaterials.LiquidAir.getFluid(100000))
                .EUt(480)
                .duration(2000)
                .save(provider);

        GTORecipeTypes.LARGE_GAS_COLLECTOR_RECIPES.recipeBuilder(GTOCore.id("6"))
                .notConsumable(GTOItems.DIMENSION_DATA.get().setDimension(GTOWorldGenLayers.THE_END))
                .notConsumable(GTMultiMachines.VACUUM_FREEZER.asStack())
                .outputFluids(GTMaterials.LiquidEnderAir.getFluid(100000))
                .EUt(7680)
                .duration(2000)
                .save(provider);
    }
}
