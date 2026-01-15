package com.gtocore.common.recipe.condition;

import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;

import org.jetbrains.annotations.NotNull;

abstract class AbstractRecipeCondition extends RecipeCondition {

    @Override
    protected final boolean testCondition(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        return test((Recipe) recipe, recipeLogic);
    }

    abstract boolean test(@NotNull Recipe recipe, @NotNull RecipeLogic recipeLogic);
}
