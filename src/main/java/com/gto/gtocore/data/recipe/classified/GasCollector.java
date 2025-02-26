package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.GTOWorldGenLayers;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface GasCollector {

    static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.GAS_COLLECTOR_RECIPES.recipeBuilder(GTOCore.id("barnarda_c"))
                .circuitMeta(6)
                .outputFluids(GTOMaterials.BarnardaAir.getFluid(10000))
                .EUt(1024)
                .duration(200)
                .dimension(GTOWorldGenLayers.BARNARDA_C)
                .save(provider);

        GTRecipeTypes.GAS_COLLECTOR_RECIPES.recipeBuilder(GTOCore.id("flat"))
                .circuitMeta(5)
                .outputFluids(GTMaterials.Air.getFluid(10000))
                .EUt(16)
                .duration(200)
                .dimension(GTOWorldGenLayers.FLAT)
                .save(provider);

        GTRecipeTypes.GAS_COLLECTOR_RECIPES.recipeBuilder(GTOCore.id("void"))
                .circuitMeta(4)
                .outputFluids(GTMaterials.Air.getFluid(10000))
                .EUt(16)
                .duration(200)
                .dimension(GTOWorldGenLayers.VOID)
                .save(provider);
    }
}
