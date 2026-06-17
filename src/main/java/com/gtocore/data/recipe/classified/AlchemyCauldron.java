package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.ALCHEMY_CAULDRON_RECIPES;

final class AlchemyCauldron {

    public static void init() {
        ALCHEMY_CAULDRON_RECIPES.recipeBuilder("coal_slurry")
                .inputItems(TagPrefix.dust, GTMaterials.Coal, 2)
                .inputFluids(GTMaterials.Water, 1000)
                .outputFluids(GTOMaterials.CoalSlurry, 1000)
                .duration(240)
                .temperature(340)
                .save();
    }
}
