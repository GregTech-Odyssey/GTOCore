package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import static com.gtocore.common.data.GTORecipeTypes.SPACE_PROBE_SURFACE_RECEPTION_RECIPES;

final class SpaceProbeSurfaceReception {

    public static void init() {
        SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder("heavy_lepton_mixture1")
                .notConsumable(GTOItems.SPACE_PROBE_MK1.asItem())
                .circuitMeta(1)
                .outputFluids(GTOMaterials.HeavyLeptonMixture, 100)
                .EUt(31457280)
                .duration(200)
                .save();

        SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder("heavy_lepton_mixture2")
                .notConsumable(GTOItems.SPACE_PROBE_MK2.asItem())
                .circuitMeta(1)
                .outputFluids(GTOMaterials.HeavyLeptonMixture, 1000)
                .EUt(125829120)
                .duration(200)
                .save();

        SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder("heavy_lepton_mixture3")
                .notConsumable(GTOItems.SPACE_PROBE_MK3.asItem())
                .circuitMeta(1)
                .outputFluids(GTOMaterials.HeavyLeptonMixture, 10000)
                .EUt(503316480)
                .duration(200)
                .save();

        SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder("cosmic_element3")
                .notConsumable(GTOItems.SPACE_PROBE_MK3.asItem())
                .circuitMeta(3)
                .outputFluids(GTOMaterials.CosmicElement, 10000)
                .EUt(503316480)
                .duration(200)
                .save();

        SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder("starlight3")
                .notConsumable(GTOItems.SPACE_PROBE_MK3.asItem())
                .circuitMeta(2)
                .outputFluids(GTOMaterials.Starlight, 10000)
                .EUt(503316480)
                .duration(200)
                .save();

        SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder("starlight2")
                .notConsumable(GTOItems.SPACE_PROBE_MK2.asItem())
                .circuitMeta(2)
                .outputFluids(GTOMaterials.Starlight, 1000)
                .EUt(125829120)
                .duration(200)
                .save();
    }
}
