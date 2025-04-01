package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.common.data.GTOItems;

import static com.gto.gtocore.common.data.GTORecipeTypes.CLUSTER_RECIPES;

interface Cluster {

    static void init() {
        CLUSTER_RECIPES.builder("mica_insulator_foil")
                .inputItems(GTOItems.MICA_INSULATOR_SHEET.asItem())
                .outputItems(GTOItems.MICA_INSULATOR_FOIL.asStack(4))
                .EUt(30)
                .duration(100)
                .save();
    }
}
