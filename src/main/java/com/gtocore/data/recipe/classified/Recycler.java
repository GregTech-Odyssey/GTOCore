package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;

import com.gtolib.utils.TagUtils;

import static com.gtocore.common.data.GTORecipeTypes.RECYCLER_RECIPES;

final class Recycler {

    public static void init() {
        RECYCLER_RECIPES.recipeBuilder("recycler_a")
                .inputItems(TagUtils.createTGItemTag("ingots"))
                .outputItems(GTOItems.SCRAP)
                .EUt(30)
                .duration(200)
                .save();

        RECYCLER_RECIPES.recipeBuilder("recycler_b")
                .inputItems(TagUtils.createTGItemTag("storage_blocks"))
                .outputItems(GTOItems.SCRAP, 9)
                .EUt(120)
                .duration(200)
                .save();

        RECYCLER_RECIPES.recipeBuilder("recycler_c")
                .inputItems(TagUtils.createTGItemTag("gems"))
                .outputItems(GTOItems.SCRAP)
                .EUt(30)
                .duration(200)
                .save();
    }
}
