package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import static com.gto.gtocore.common.data.GTORecipeTypes.UNPACKER_RECIPES;

interface Unpacker {

    static void init() {
        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("carrot"))
                .inputItems("farmersdelight:carrot_crate")
                .outputItems(new ItemStack(Items.CARROT.asItem(), 9))
                .EUt(12)
                .duration(10)
                .save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("potato"))
                .inputItems("farmersdelight:potato_crate")
                .outputItems(new ItemStack(Items.POTATO.asItem(), 9))
                .EUt(12)
                .duration(10)
                .save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("beetroot"))
                .inputItems("farmersdelight:beetroot_crate")
                .outputItems(new ItemStack(Items.BEETROOT.asItem(), 9))
                .EUt(12)
                .duration(10)
                .save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("cabbage"))
                .inputItems("farmersdelight:cabbage_crate")
                .outputItems("farmersdelight:cabbage", 9)
                .EUt(12)
                .duration(10)
                .save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("tomato"))
                .inputItems("farmersdelight:tomato_crate")
                .outputItems("farmersdelight:tomato", 9)
                .EUt(12)
                .duration(10)
                .save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("onion"))
                .inputItems("farmersdelight:onion_crate")
                .outputItems("farmersdelight:onion", 9)
                .EUt(12)
                .duration(10)
                .save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("rice_panicle"))
                .inputItems("farmersdelight:rice_bale")
                .outputItems("farmersdelight:rice_panicle", 9)
                .EUt(12)
                .duration(10)
                .save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("rice"))
                .inputItems("farmersdelight:rice_bag")
                .outputItems("farmersdelight:rice", 9)
                .EUt(12)
                .duration(10)
                .save();

        UNPACKER_RECIPES.recipeBuilder(GTOCore.id("straw"))
                .inputItems("farmersdelight:straw_bale")
                .outputItems("farmersdelight:straw", 9)
                .EUt(12)
                .duration(10)
                .save();
    }
}
