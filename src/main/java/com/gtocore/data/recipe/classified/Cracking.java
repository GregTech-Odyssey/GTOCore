package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.CRACKING_RECIPES;

final class Cracking {

    public static void init() {
        CRACKING_RECIPES.recipeBuilder("radon_cracked_enriched_aquadah")
                .circuitMeta(1)
                .inputFluids(GTMaterials.EnrichedNaquadahSolution, 1000)
                .inputFluids(GTMaterials.Radon, 1000)
                .outputFluids(GTOMaterials.RadonCrackedEnrichedAquadah, 1000)
                .EUt(1966080)
                .duration(160)
                .save();

        CRACKING_RECIPES.recipeBuilder("steam_cracked_turpentine")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.LeachedTurpentine, 1000)
                .inputFluids(GTMaterials.Steam, 1000)
                .outputFluids(GTOMaterials.SteamCrackedTurpentine, 1000)
                .EUt(480)
                .duration(200)
                .save();

        CRACKING_RECIPES.recipeBuilder("crackedradox")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.SuperLightRadox, 100)
                .inputFluids(GTMaterials.Silver.getFluid(FluidStorageKeys.PLASMA, 10))
                .outputFluids(GTOMaterials.CrackedRadox, 100)
                .EUt(491520)
                .duration(300)
                .save();

        CRACKING_RECIPES.recipeBuilder("lightradox")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.HeavyRadox, 100)
                .inputFluids(GTMaterials.Silver.getFluid(FluidStorageKeys.PLASMA, 10))
                .outputFluids(GTOMaterials.LightRadox, 20)
                .EUt(491520)
                .duration(200)
                .save();

        CRACKING_RECIPES.recipeBuilder("superlightradox")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.LightRadox, 100)
                .inputFluids(GTMaterials.Silver.getFluid(FluidStorageKeys.PLASMA, 10))
                .outputFluids(GTOMaterials.SuperLightRadox, 50)
                .EUt(491520)
                .duration(300)
                .save();

        CRACKING_RECIPES.recipeBuilder("fluorine_cracked_aquadah")
                .circuitMeta(1)
                .inputFluids(GTOMaterials.NaquadahSolution, 1000)
                .inputFluids(GTMaterials.Fluorine, 1000)
                .outputFluids(GTOMaterials.FluorineCrackedAquadah, 1000)
                .EUt(491520)
                .duration(120)
                .save();
    }
}
