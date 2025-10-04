package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;

import com.gtolib.utils.TagUtils;

import static com.gtocore.common.data.GTORecipeTypes.RECYCLER_RECIPES;

final class Recycler {

    public static void init() {
        RECYCLER_RECIPES.recipeBuilder("recycler_a")
                .inputItems(TagUtils.createTGTag("ingots"))
                .outputItems(GTOItems.SCRAP.asItem())
                .EUt(30)
                .duration(200)
                .save();

        RECYCLER_RECIPES.recipeBuilder("recycler_b")
                .inputItems(TagUtils.createTGTag("storage_blocks"))
                .outputItems(GTOItems.SCRAP.asItem(), 9)
                .EUt(120)
                .duration(200)
                .save();

        RECYCLER_RECIPES.recipeBuilder("recycler_c")
                .inputItems(TagUtils.createTGTag("gems"))
                .outputItems(GTOItems.SCRAP.asItem())
                .EUt(30)
                .duration(200)
                .save();
    }
}
