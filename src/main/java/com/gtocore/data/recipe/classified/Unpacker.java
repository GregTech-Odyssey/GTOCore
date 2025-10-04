package com.gtocore.data.recipe.classified;

import net.minecraft.world.item.Items;

import static com.gtocore.common.data.GTORecipeTypes.UNPACKER_RECIPES;

final class Unpacker {

    public static void init() {
        UNPACKER_RECIPES.recipeBuilder("carrot")
                .inputItems("farmersdelight:carrot_crate")
                .outputItems(Items.CARROT.asItem(), 9)
                .EUt(12)
                .duration(10)
                .save();

        UNPACKER_RECIPES.recipeBuilder("potato")
                .inputItems("farmersdelight:potato_crate")
                .outputItems(Items.POTATO.asItem(), 9)
                .EUt(12)
                .duration(10)
                .save();

        UNPACKER_RECIPES.recipeBuilder("beetroot")
                .inputItems("farmersdelight:beetroot_crate")
                .outputItems(Items.BEETROOT.asItem(), 9)
                .EUt(12)
                .duration(10)
                .save();

        UNPACKER_RECIPES.recipeBuilder("cabbage")
                .inputItems("farmersdelight:cabbage_crate")
                .outputItems("farmersdelight:cabbage", 9)
                .EUt(12)
                .duration(10)
                .save();

        UNPACKER_RECIPES.recipeBuilder("tomato")
                .inputItems("farmersdelight:tomato_crate")
                .outputItems("farmersdelight:tomato", 9)
                .EUt(12)
                .duration(10)
                .save();

        UNPACKER_RECIPES.recipeBuilder("onion")
                .inputItems("farmersdelight:onion_crate")
                .outputItems("farmersdelight:onion", 9)
                .EUt(12)
                .duration(10)
                .save();

        UNPACKER_RECIPES.recipeBuilder("rice_panicle")
                .inputItems("farmersdelight:rice_bale")
                .outputItems("farmersdelight:rice_panicle", 9)
                .EUt(12)
                .duration(10)
                .save();

        UNPACKER_RECIPES.recipeBuilder("rice")
                .inputItems("farmersdelight:rice_bag")
                .outputItems("farmersdelight:rice", 9)
                .EUt(12)
                .duration(10)
                .save();

        UNPACKER_RECIPES.recipeBuilder("straw")
                .inputItems("farmersdelight:straw_bale")
                .outputItems("farmersdelight:straw", 9)
                .EUt(12)
                .duration(10)
                .save();
    }
}
