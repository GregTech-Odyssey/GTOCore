package com.gtocore.data.recipe.classified;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import static com.gtocore.common.data.GTORecipeTypes.CLUSTER_RECIPES;

final class Cluster {

    public static void init() {
        CLUSTER_RECIPES.builder("mica_insulator_foil")
                .inputItems(GTOItems.MICA_INSULATOR_SHEET.asItem())
                .outputItems(GTOItems.MICA_INSULATOR_FOIL.asStack(4))
                .EUt(30)
                .duration(100)
                .save();

        CLUSTER_RECIPES.builder("non_woven_fabric")
                .inputItems(TagPrefix.foil, GTOMaterials.Polypropylene, 4)
                .outputItems(GTOItems.NON_WOVEN_FABRIC.asItem())
                .EUt(30)
                .duration(100)
                .save();
    }
}
