package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;

import static com.gtocore.common.data.GTORecipeTypes.FISSION_REACTOR_RECIPES;

final class FissionReactor {

    public static void init() {
        FISSION_REACTOR_RECIPES.recipeBuilder("reactor_uranium_dual")
                .inputItems(GTOItems.REACTOR_URANIUM_DUAL)
                .outputItems(GTOItems.DEPLETED_REACTOR_URANIUM_DUAL)
                .EUt(40)
                .duration(144000)
                .addData("FRheat", 5)
                .save();

        FISSION_REACTOR_RECIPES.recipeBuilder("reactor_uranium_quad")
                .inputItems(GTOItems.REACTOR_URANIUM_QUAD)
                .outputItems(GTOItems.DEPLETED_REACTOR_URANIUM_QUAD)
                .EUt(50)
                .duration(180000)
                .addData("FRheat", 6)
                .save();

        FISSION_REACTOR_RECIPES.recipeBuilder("reactor_naquadah_simple")
                .inputItems(GTOItems.REACTOR_NAQUADAH_SIMPLE)
                .outputItems(GTOItems.DEPLETED_REACTOR_NAQUADAH_SIMPLE)
                .EUt(300)
                .duration(320000)
                .addData("FRheat", 7)
                .save();

        FISSION_REACTOR_RECIPES.recipeBuilder("reactor_uranium_simple")
                .inputItems(GTOItems.REACTOR_URANIUM_SIMPLE)
                .outputItems(GTOItems.DEPLETED_REACTOR_URANIUM_SIMPLE)
                .EUt(30)
                .duration(112000)
                .addData("FRheat", 4)
                .save();

        FISSION_REACTOR_RECIPES.recipeBuilder("reactor_thorium_dual")
                .inputItems(GTOItems.REACTOR_THORIUM_DUAL)
                .outputItems(GTOItems.DEPLETED_REACTOR_THORIUM_DUAL)
                .EUt(80)
                .duration(172800)
                .addData("FRheat", 2)
                .save();

        FISSION_REACTOR_RECIPES.recipeBuilder("reactor_mox_quad")
                .inputItems(GTOItems.REACTOR_MOX_QUAD)
                .outputItems(GTOItems.DEPLETED_REACTOR_MOX_QUAD)
                .EUt(30)
                .duration(128000)
                .addData("FRheat", 8)
                .save();

        FISSION_REACTOR_RECIPES.recipeBuilder("reactor_thorium_quad")
                .inputItems(GTOItems.REACTOR_THORIUM_QUAD)
                .outputItems(GTOItems.DEPLETED_REACTOR_THORIUM_QUAD)
                .EUt(100)
                .duration(216000)
                .addData("FRheat", 3)
                .save();

        FISSION_REACTOR_RECIPES.recipeBuilder("reactor_mox_dual")
                .inputItems(GTOItems.REACTOR_MOX_DUAL)
                .outputItems(GTOItems.DEPLETED_REACTOR_MOX_DUAL)
                .EUt(20)
                .duration(100800)
                .addData("FRheat", 7)
                .save();

        FISSION_REACTOR_RECIPES.recipeBuilder("reactor_mox_simple")
                .inputItems(GTOItems.REACTOR_MOX_SIMPLE)
                .outputItems(GTOItems.DEPLETED_REACTOR_MOX_SIMPLE)
                .EUt(10)
                .duration(78400)
                .addData("FRheat", 6)
                .save();

        FISSION_REACTOR_RECIPES.recipeBuilder("reactor_naquadah_quad")
                .inputItems(GTOItems.REACTOR_NAQUADAH_QUAD)
                .outputItems(GTOItems.DEPLETED_REACTOR_NAQUADAH_QUAD)
                .EUt(700)
                .duration(720000)
                .addData("FRheat", 9)
                .save();

        FISSION_REACTOR_RECIPES.recipeBuilder("reactor_naquadah_dual")
                .inputItems(GTOItems.REACTOR_NAQUADAH_DUAL)
                .outputItems(GTOItems.DEPLETED_REACTOR_NAQUADAH_DUAL)
                .EUt(500)
                .duration(480000)
                .addData("FRheat", 8)
                .save();

        FISSION_REACTOR_RECIPES.recipeBuilder("reactor_thorium_simple")
                .inputItems(GTOItems.REACTOR_THORIUM_SIMPLE)
                .outputItems(GTOItems.DEPLETED_REACTOR_THORIUM_SIMPLE)
                .EUt(60)
                .duration(134400)
                .addData("FRheat", 1)
                .save();
    }
}
