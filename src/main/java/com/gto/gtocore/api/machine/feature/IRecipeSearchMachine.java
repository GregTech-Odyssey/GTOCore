package com.gto.gtocore.api.machine.feature;

import com.gto.gtocore.api.recipe.RecipeRunnerHelper;

import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

/**
 * 代理搜索匹配。
 */
public interface IRecipeSearchMachine extends IRecipeCapabilityHolder {

    default boolean matchRecipe(GTRecipe recipe) {
        return RecipeRunnerHelper.matchRecipeInput(this, recipe) && RecipeRunnerHelper.matchRecipeOutput(this, recipe);
    }

    default boolean matchTickRecipe(GTRecipe recipe) {
        return RecipeRunnerHelper.matchRecipeTickInput(this, recipe) && RecipeRunnerHelper.matchRecipeTickOutput(this, recipe);
    }
}
