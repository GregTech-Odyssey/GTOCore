package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

interface SpaceProbeSurfaceReception {

    static void init() {
        GTORecipeTypes.SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder(GTOCore.id("heavy_lepton_mixture1"))
                .notConsumable(GTOItems.SPACE_PROBE_MK1.asStack())
                .circuitMeta(1)
                .outputFluids(GTOMaterials.HeavyLeptonMixture.getFluid(100))
                .EUt(31457280)
                .duration(200)
                .save();

        GTORecipeTypes.SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder(GTOCore.id("heavy_lepton_mixture2"))
                .notConsumable(GTOItems.SPACE_PROBE_MK2.asStack())
                .circuitMeta(1)
                .outputFluids(GTOMaterials.HeavyLeptonMixture.getFluid(1000))
                .EUt(125829120)
                .duration(200)
                .save();

        GTORecipeTypes.SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder(GTOCore.id("heavy_lepton_mixture3"))
                .notConsumable(GTOItems.SPACE_PROBE_MK3.asStack())
                .circuitMeta(1)
                .outputFluids(GTOMaterials.HeavyLeptonMixture.getFluid(10000))
                .EUt(503316480)
                .duration(200)
                .save();

        GTORecipeTypes.SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder(GTOCore.id("cosmic_element3"))
                .notConsumable(GTOItems.SPACE_PROBE_MK3.asStack())
                .circuitMeta(3)
                .outputFluids(GTOMaterials.CosmicElement.getFluid(10000))
                .EUt(503316480)
                .duration(200)
                .save();

        GTORecipeTypes.SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder(GTOCore.id("starlight3"))
                .notConsumable(GTOItems.SPACE_PROBE_MK3.asStack())
                .circuitMeta(2)
                .outputFluids(GTOMaterials.Starlight.getFluid(10000))
                .EUt(503316480)
                .duration(200)
                .save();

        GTORecipeTypes.SPACE_PROBE_SURFACE_RECEPTION_RECIPES.recipeBuilder(GTOCore.id("starlight2"))
                .notConsumable(GTOItems.SPACE_PROBE_MK2.asStack())
                .circuitMeta(2)
                .outputFluids(GTOMaterials.Starlight.getFluid(1000))
                .EUt(125829120)
                .duration(200)
                .save();
    }
}
