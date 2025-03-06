package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;

import static com.gto.gtocore.common.data.GTORecipeTypes.POLARIZER_RECIPES;

interface Polarizer {

    static void init() {
        POLARIZER_RECIPES.recipeBuilder(GTOCore.id("attuned_tengam_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.PurifiedTengam)
                .outputItems(TagPrefix.dust, GTOMaterials.AttunedTengam)
                .EUt(125829120)
                .duration(400)
                .save();

        POLARIZER_RECIPES.recipeBuilder(GTOCore.id("magnetic_long_netherite_rod"))
                .inputItems(GTOItems.LONG_NETHERITE_ROD.asStack())
                .outputItems(GTOItems.MAGNETIC_LONG_NETHERITE_ROD.asStack())
                .EUt(1966080)
                .duration(400)
                .save();

        POLARIZER_RECIPES.recipeBuilder(GTOCore.id("magnetic_netherite_rod"))
                .inputItems(GTOItems.NETHERITE_ROD.asStack())
                .outputItems(GTOItems.MAGNETIC_NETHERITE_ROD.asStack())
                .EUt(1966080)
                .duration(200)
                .save();

        POLARIZER_RECIPES.recipeBuilder(GTOCore.id("small_attuned_tengam_dust"))
                .inputItems(TagPrefix.dustSmall, GTOMaterials.PurifiedTengam)
                .outputItems(TagPrefix.dustSmall, GTOMaterials.AttunedTengam)
                .EUt(31457280)
                .duration(400)
                .save();

        POLARIZER_RECIPES.recipeBuilder(GTOCore.id("triplet_neutronium_sphere"))
                .inputItems(GTOItems.NEUTRONIUM_SPHERE.asStack())
                .outputItems(GTOItems.TRIPLET_NEUTRONIUM_SPHERE.asStack())
                .EUt(5000000)
                .duration(200)
                .save();
    }
}
