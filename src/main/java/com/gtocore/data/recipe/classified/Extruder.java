package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;

import static com.gtocore.common.data.GTORecipeTypes.EXTRUDER_RECIPES;

final class Extruder {

    public static void init() {
        EXTRUDER_RECIPES.recipeBuilder("special_ceramics")
                .inputItems(TagPrefix.dust, GTOMaterials.SpecialCeramics, 2)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PLATE.asItem())
                .outputItems(GTOItems.SPECIAL_CERAMICS)
                .EUt(480)
                .duration(20)
                .save();
    }
}
