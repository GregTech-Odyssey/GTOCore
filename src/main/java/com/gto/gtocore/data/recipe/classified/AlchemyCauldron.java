package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.ALCHEMY_CAULDRON_RECIPES;

interface AlchemyCauldron {

    static void init() {
        ALCHEMY_CAULDRON_RECIPES.recipeBuilder(GTOCore.id("coal_slurry"))
                .inputItems(TagPrefix.dust, GTMaterials.Coal, 2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputFluids(GTOMaterials.CoalSlurry.getFluid(1000))
                .duration(240)
                .temperature(340)
                .save();
    }
}
