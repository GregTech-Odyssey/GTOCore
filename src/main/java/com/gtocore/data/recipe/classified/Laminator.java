package com.gtocore.data.recipe.classified;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.LAMINATOR_RECIPES;

final class Laminator {

    public static void init() {
        LAMINATOR_RECIPES.recipeBuilder("fluix_covered_cable_1")
                .inputItems("ae2:fluix_glass_cable")
                .outputItems("ae2:fluix_covered_cable")
                .inputFluids(GTMaterials.Rubber, 72)
                .EUt(20)
                .duration(30)
                .save();

        LAMINATOR_RECIPES.recipeBuilder("fluix_covered_cable_2")
                .inputItems("ae2:fluix_glass_cable", 4)
                .outputItems("ae2:fluix_covered_cable", 4)
                .inputFluids(GTMaterials.SiliconeRubber, 72)
                .EUt(20)
                .duration(30)
                .save();

        LAMINATOR_RECIPES.recipeBuilder("fluix_covered_cable_3")
                .inputItems("ae2:fluix_glass_cable", 16)
                .outputItems("ae2:fluix_covered_cable", 16)
                .inputFluids(GTMaterials.StyreneButadieneRubber, 72)
                .EUt(20)
                .duration(30)
                .save();
    }
}
