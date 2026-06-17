package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import static com.gtocore.common.data.GTORecipeTypes.BEDROCK_DRILLING_RIG_RECIPES;

final class BedrockDrillingRig {

    public static void init() {
        for (int i = 1; i < 17; i++) {
            BEDROCK_DRILLING_RIG_RECIPES.recipeBuilder("bedrock_dust_" + i)
                    .chancedInput(GTOItems.BEDROCK_DRILL.asItem(), 1 << i, 10, 0)
                    .circuitMeta(i)
                    .outputItems(TagPrefix.dust, GTOMaterials.Bedrockium, 2 << i)
                    .save();
        }
    }
}
