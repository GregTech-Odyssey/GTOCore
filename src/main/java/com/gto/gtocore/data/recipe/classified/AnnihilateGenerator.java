package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTORecipeTypes;

interface AnnihilateGenerator {

    static void init() {
        GTORecipeTypes.ANNIHILATE_GENERATOR_RECIPES.recipeBuilder(GTOCore.id("neutronium_antimatter_fuel_rod"))
                .inputItems(GTOItems.NEUTRONIUM_ANTIMATTER_FUEL_ROD.asStack())
                .chancedOutput(GTOItems.ANNIHILATION_CONSTRAINER.asStack(), 9000, 0)
                .EUt(-549755813888L)
                .duration(200)
                .save();

        GTORecipeTypes.ANNIHILATE_GENERATOR_RECIPES.recipeBuilder(GTOCore.id("draconium_antimatter_fuel_rod"))
                .inputItems(GTOItems.DRACONIUM_ANTIMATTER_FUEL_ROD.asStack())
                .chancedOutput(GTOItems.ANNIHILATION_CONSTRAINER.asStack(), 8000, 0)
                .EUt(-8796093022208L)
                .duration(200)
                .save();

        GTORecipeTypes.ANNIHILATE_GENERATOR_RECIPES.recipeBuilder(GTOCore.id("cosmic_neutronium_antimatter_fuel_rod"))
                .inputItems(GTOItems.COSMIC_NEUTRONIUM_ANTIMATTER_FUEL_ROD.asStack())
                .chancedOutput(GTOItems.ANNIHILATION_CONSTRAINER.asStack(), 7000, 0)
                .EUt(-140737488355328L)
                .duration(200)
                .save();

        GTORecipeTypes.ANNIHILATE_GENERATOR_RECIPES.recipeBuilder(GTOCore.id("infinity_antimatter_fuel_rod"))
                .inputItems(GTOItems.INFINITY_ANTIMATTER_FUEL_ROD.asStack())
                .chancedOutput(GTOItems.ANNIHILATION_CONSTRAINER.asStack(), 6000, 0)
                .EUt(-2251799813685248L)
                .duration(200)
                .save();
    }
}
