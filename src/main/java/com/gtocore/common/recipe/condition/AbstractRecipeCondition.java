package com.gtocore.common.recipe.condition;

import com.gtolib.api.recipe.RecipeDefinition;

import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;

import org.jetbrains.annotations.NotNull;

abstract class AbstractRecipeCondition extends RecipeCondition {

    @Override
    protected final boolean testCondition(@NotNull GTRecipeDefinition recipe, @NotNull RecipeLogic recipeLogic) {
        return test((RecipeDefinition) recipe, recipeLogic);
    }

    abstract boolean test(@NotNull RecipeDefinition recipe, @NotNull RecipeLogic recipeLogic);
}
