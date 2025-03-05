package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

interface LargeBoiler {

    static void init() {
        GTORecipeTypes.LARGE_BOILER_RECIPES.recipeBuilder(GTOCore.id("tin_bucket"))
                .inputItems(TagPrefix.ingot, GTMaterials.Tin)
                .inputFluids(GTMaterials.Lava.getFluid(100))
                .outputFluids(GTMaterials.Tin.getFluid(72))
                .duration(5)
                .save();
    }
}
