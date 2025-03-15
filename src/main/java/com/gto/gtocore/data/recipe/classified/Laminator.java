package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.LAMINATOR_RECIPES;

interface Laminator {

    static void init() {
        LAMINATOR_RECIPES.recipeBuilder(GTOCore.id("fluix_covered_cable_1"))
                .inputItems("ae2:fluix_glass_cable")
                .outputItems("ae2:fluix_covered_cable")
                .inputFluids(GTMaterials.Rubber.getFluid(72))
                .EUt(20)
                .duration(30)
                .save();

        LAMINATOR_RECIPES.recipeBuilder(GTOCore.id("fluix_covered_cable_2"))
                .inputItems("ae2:fluix_glass_cable", 4)
                .outputItems("ae2:fluix_covered_cable", 4)
                .inputFluids(GTMaterials.SiliconeRubber.getFluid(72))
                .EUt(20)
                .duration(30)
                .save();

        LAMINATOR_RECIPES.recipeBuilder(GTOCore.id("fluix_covered_cable_3"))
                .inputItems("ae2:fluix_glass_cable", 16)
                .outputItems("ae2:fluix_covered_cable", 16)
                .inputFluids(GTMaterials.StyreneButadieneRubber.getFluid(72))
                .EUt(20)
                .duration(30)
                .save();
    }
}
