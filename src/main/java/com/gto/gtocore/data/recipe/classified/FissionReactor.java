package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.init.GTOItems;
import com.gto.gtocore.init.GTORecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface FissionReactor {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.FISSION_REACTOR_RECIPES.recipeBuilder(GTOCore.id("reactor_uranium_dual"))
                .inputItems(GTOItems.REACTOR_URANIUM_DUAL.asStack())
                .outputItems(GTOItems.DEPLETED_REACTOR_URANIUM_DUAL.asStack())
                .EUt(4)
                .duration(144000)
                .addData("FRheat", 5)
                .save(provider);

        GTORecipeTypes.FISSION_REACTOR_RECIPES.recipeBuilder(GTOCore.id("reactor_uranium_quad"))
                .inputItems(GTOItems.REACTOR_URANIUM_QUAD.asStack())
                .outputItems(GTOItems.DEPLETED_REACTOR_URANIUM_QUAD.asStack())
                .EUt(5)
                .duration(180000)
                .addData("FRheat", 6)
                .save(provider);

        GTORecipeTypes.FISSION_REACTOR_RECIPES.recipeBuilder(GTOCore.id("reactor_naquadah_simple"))
                .inputItems(GTOItems.REACTOR_NAQUADAH_SIMPLE.asStack())
                .outputItems(GTOItems.DEPLETED_REACTOR_NAQUADAH_SIMPLE.asStack())
                .EUt(3)
                .duration(160000)
                .addData("FRheat", 7)
                .save(provider);

        GTORecipeTypes.FISSION_REACTOR_RECIPES.recipeBuilder(GTOCore.id("reactor_uranium_simple"))
                .inputItems(GTOItems.REACTOR_URANIUM_SIMPLE.asStack())
                .outputItems(GTOItems.DEPLETED_REACTOR_URANIUM_SIMPLE.asStack())
                .EUt(3)
                .duration(112000)
                .addData("FRheat", 4)
                .save(provider);

        GTORecipeTypes.FISSION_REACTOR_RECIPES.recipeBuilder(GTOCore.id("reactor_thorium_dual"))
                .inputItems(GTOItems.REACTOR_THORIUM_DUAL.asStack())
                .outputItems(GTOItems.DEPLETED_REACTOR_THORIUM_DUAL.asStack())
                .EUt(8)
                .duration(172800)
                .addData("FRheat", 2)
                .save(provider);

        GTORecipeTypes.FISSION_REACTOR_RECIPES.recipeBuilder(GTOCore.id("reactor_mox_quad"))
                .inputItems(GTOItems.REACTOR_MOX_QUAD.asStack())
                .outputItems(GTOItems.DEPLETED_REACTOR_MOX_QUAD.asStack())
                .EUt(3)
                .duration(128000)
                .addData("FRheat", 8)
                .save(provider);

        GTORecipeTypes.FISSION_REACTOR_RECIPES.recipeBuilder(GTOCore.id("reactor_thorium_quad"))
                .inputItems(GTOItems.REACTOR_THORIUM_QUAD.asStack())
                .outputItems(GTOItems.DEPLETED_REACTOR_THORIUM_QUAD.asStack())
                .EUt(10)
                .duration(216000)
                .addData("FRheat", 3)
                .save(provider);

        GTORecipeTypes.FISSION_REACTOR_RECIPES.recipeBuilder(GTOCore.id("reactor_mox_dual"))
                .inputItems(GTOItems.REACTOR_MOX_DUAL.asStack())
                .outputItems(GTOItems.DEPLETED_REACTOR_MOX_DUAL.asStack())
                .EUt(2)
                .duration(100800)
                .addData("FRheat", 7)
                .save(provider);

        GTORecipeTypes.FISSION_REACTOR_RECIPES.recipeBuilder(GTOCore.id("reactor_mox_simple"))
                .inputItems(GTOItems.REACTOR_MOX_SIMPLE.asStack())
                .outputItems(GTOItems.DEPLETED_REACTOR_MOX_SIMPLE.asStack())
                .EUt(1)
                .duration(78400)
                .addData("FRheat", 6)
                .save(provider);

        GTORecipeTypes.FISSION_REACTOR_RECIPES.recipeBuilder(GTOCore.id("reactor_naquadah_quad"))
                .inputItems(GTOItems.REACTOR_NAQUADAH_QUAD.asStack())
                .outputItems(GTOItems.DEPLETED_REACTOR_NAQUADAH_QUAD.asStack())
                .EUt(7)
                .duration(360000)
                .addData("FRheat", 9)
                .save(provider);

        GTORecipeTypes.FISSION_REACTOR_RECIPES.recipeBuilder(GTOCore.id("reactor_naquadah_dual"))
                .inputItems(GTOItems.REACTOR_NAQUADAH_DUAL.asStack())
                .outputItems(GTOItems.DEPLETED_REACTOR_NAQUADAH_DUAL.asStack())
                .EUt(5)
                .duration(240000)
                .addData("FRheat", 8)
                .save(provider);

        GTORecipeTypes.FISSION_REACTOR_RECIPES.recipeBuilder(GTOCore.id("reactor_thorium_simple"))
                .inputItems(GTOItems.REACTOR_THORIUM_SIMPLE.asStack())
                .outputItems(GTOItems.DEPLETED_REACTOR_THORIUM_SIMPLE.asStack())
                .EUt(6)
                .duration(134400)
                .addData("FRheat", 1)
                .save(provider);
    }
}
