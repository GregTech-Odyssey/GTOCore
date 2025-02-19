package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface Cracking {

    static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.CRACKING_RECIPES.recipeBuilder(GTOCore.id("radon_cracked_enriched_aquadah"))
                .circuitMeta(1)
                .inputFluids(GTMaterials.EnrichedNaquadahSolution.getFluid(1000))
                .inputFluids(GTMaterials.Radon.getFluid(1000))
                .outputFluids(GTOMaterials.RadonCrackedEnrichedAquadah.getFluid(1000))
                .EUt(1966080)
                .duration(160)
                .save(provider);

        GTRecipeTypes.CRACKING_RECIPES.recipeBuilder(GTOCore.id("steam_cracked_turpentine"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.LeachedTurpentine.getFluid(1000))
                .inputFluids(GTMaterials.Steam.getFluid(1000))
                .outputFluids(GTOMaterials.SteamCrackedTurpentine.getFluid(1000))
                .EUt(480)
                .duration(200)
                .save(provider);

        GTRecipeTypes.CRACKING_RECIPES.recipeBuilder(GTOCore.id("crackedradox"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.SuperLightRadox.getFluid(100))
                .inputFluids(GTMaterials.Silver.getFluid(FluidStorageKeys.PLASMA, 10))
                .outputFluids(GTOMaterials.CrackedRadox.getFluid(100))
                .EUt(491520)
                .duration(300)
                .save(provider);

        GTRecipeTypes.CRACKING_RECIPES.recipeBuilder(GTOCore.id("lightradox"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.HeavyRadox.getFluid(100))
                .inputFluids(GTMaterials.Silver.getFluid(FluidStorageKeys.PLASMA, 10))
                .outputFluids(GTOMaterials.LightRadox.getFluid(20))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTRecipeTypes.CRACKING_RECIPES.recipeBuilder(GTOCore.id("superlightradox"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.LightRadox.getFluid(100))
                .inputFluids(GTMaterials.Silver.getFluid(FluidStorageKeys.PLASMA, 10))
                .outputFluids(GTOMaterials.SuperLightRadox.getFluid(50))
                .EUt(491520)
                .duration(300)
                .save(provider);

        GTRecipeTypes.CRACKING_RECIPES.recipeBuilder(GTOCore.id("fluorine_cracked_aquadah"))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.NaquadahSolution.getFluid(1000))
                .inputFluids(GTMaterials.Fluorine.getFluid(1000))
                .outputFluids(GTOMaterials.FluorineCrackedAquadah.getFluid(1000))
                .EUt(491520)
                .duration(120)
                .save(provider);
    }
}
