package com.gtocore.data.recipe.classified;

import com.gtolib.utils.TagUtils;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.LAVA_FURNACE_RECIPES;

final class LavaFurnace {

    public static void init() {
        LAVA_FURNACE_RECIPES.recipeBuilder("lava_furnace")
                .inputItems(TagUtils.createForgeItemTag("cobblestone"))
                .outputFluids(GTMaterials.Lava, 1000)
                .EUt(16)
                .duration(200)
                .save();

        LAVA_FURNACE_RECIPES.recipeBuilder("lava_furnace1")
                .inputItems(TagUtils.createForgeItemTag("stone"))
                .outputFluids(GTMaterials.Lava, 1000)
                .EUt(16)
                .duration(200)
                .save();
    }
}
