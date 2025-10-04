package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import static com.gtocore.common.data.GTORecipeTypes.BEDROCK_DRILLING_RIG_RECIPES;

final class BedrockDrillingRig {

    public static void init() {
        BEDROCK_DRILLING_RIG_RECIPES.recipeBuilder("bedrock_dust_4")
                .chancedInput(GTOItems.BEDROCK_DRILL.asItem(), 256, 10, 0)
                .circuitMeta(5)
                .outputItems(TagPrefix.dust, GTOMaterials.Bedrockium, 256)
                .EUt(7864320)
                .duration(3200)
                .save();

        BEDROCK_DRILLING_RIG_RECIPES.recipeBuilder("bedrock_dust_3")
                .chancedInput(GTOItems.BEDROCK_DRILL.asItem(), 64, 10, 0)
                .circuitMeta(4)
                .outputItems(TagPrefix.dust, GTOMaterials.Bedrockium, 64)
                .EUt(7864320)
                .duration(1600)
                .save();

        BEDROCK_DRILLING_RIG_RECIPES.recipeBuilder("bedrock_dust_6")
                .chancedInput(GTOItems.BEDROCK_DRILL.asItem(), 4096, 10, 0)
                .circuitMeta(7)
                .outputItems(TagPrefix.dust, GTOMaterials.Bedrockium, 4096)
                .EUt(7864320)
                .duration(12800)
                .save();

        BEDROCK_DRILLING_RIG_RECIPES.recipeBuilder("bedrock_dust_5")
                .chancedInput(GTOItems.BEDROCK_DRILL.asItem(), 1024, 10, 0)
                .circuitMeta(6)
                .outputItems(TagPrefix.dust, GTOMaterials.Bedrockium, 1024)
                .EUt(7864320)
                .duration(6400)
                .save();

        BEDROCK_DRILLING_RIG_RECIPES.recipeBuilder("bedrock_dust_0")
                .chancedInput(GTOItems.BEDROCK_DRILL.asItem(), 10, 0)
                .circuitMeta(1)
                .outputItems(TagPrefix.dust, GTOMaterials.Bedrockium)
                .EUt(7864320)
                .duration(200)
                .save();

        BEDROCK_DRILLING_RIG_RECIPES.recipeBuilder("bedrock_dust_2")
                .chancedInput(GTOItems.BEDROCK_DRILL.asItem(), 16, 10, 0)
                .circuitMeta(3)
                .outputItems(TagPrefix.dust, GTOMaterials.Bedrockium, 16)
                .EUt(7864320)
                .duration(800)
                .save();

        BEDROCK_DRILLING_RIG_RECIPES.recipeBuilder("bedrock_dust_1")
                .chancedInput(GTOItems.BEDROCK_DRILL.asItem(), 4, 10, 0)
                .circuitMeta(2)
                .outputItems(TagPrefix.dust, GTOMaterials.Bedrockium, 4)
                .EUt(7864320)
                .duration(400)
                .save();

        BEDROCK_DRILLING_RIG_RECIPES.recipeBuilder("bedrock_dust_7")
                .chancedInput(GTOItems.BEDROCK_DRILL.asItem(), 16384, 10, 0)
                .circuitMeta(8)
                .outputItems(TagPrefix.dust, GTOMaterials.Bedrockium, 16384)
                .EUt(7864320)
                .duration(25600)
                .save();
    }
}
