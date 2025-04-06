package com.gto.gtocore.api.machine.feature;

import com.gto.gtocore.api.machine.feature.multiblock.IMEOutputMachine;
import com.gto.gtocore.api.recipe.RecipeRunnerHelper;

import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;

/**
 * 代理搜索匹配。
 */
public interface IRecipeSearchMachine extends IRecipeCapabilityHolder {

    default boolean matchRecipe(GTRecipe recipe) {
        if (this instanceof IMEOutputMachine machine && machine.gTOCore$DualMEOutput(recipe)) {
            return RecipeRunnerHelper.matchRecipeInput(this, recipe);
        }
        return RecipeHelper.matchRecipe(this, recipe).isSuccess();
    }

    default boolean matchTickRecipe(GTRecipe recipe) {
        if (!RecipeRunnerHelper.matchRecipeTickInput(this, recipe)) return false;
        return RecipeRunnerHelper.matchRecipeTickOutput(this, recipe);
    }
}
