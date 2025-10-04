package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;

import static com.gtocore.common.data.GTORecipeTypes.CLUSTER_RECIPES;

final class Cluster {

    public static void init() {
        CLUSTER_RECIPES.builder("mica_insulator_foil")
                .inputItems(GTOItems.MICA_INSULATOR_SHEET.asItem())
                .outputItems(GTOItems.MICA_INSULATOR_FOIL.asItem(), 4)
                .EUt(30)
                .duration(100)
                .save();
    }
}
