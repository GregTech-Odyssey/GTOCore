package com.gto.gtocore.api.machine.feature;

import com.gto.gtocore.api.machine.feature.multiblock.IMEOutputMachine;
import com.gto.gtocore.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;

/**
 * 代理搜索匹配。
 */
public interface IRecipeSearchMachine extends IRecipeCapabilityHolder {

    default boolean matchRecipe(GTRecipe recipe) {
        if (this instanceof IMEOutputMachine machine && machine.gTOCore$DualMEOutput(recipe)) {
            return RecipeRunner.matchRecipeInput(this, recipe);
        }
        return RecipeHelper.matchRecipe(this, recipe).isSuccess();
    }

    default boolean matchTickRecipe(GTRecipe recipe) {
        if (!RecipeRunner.matchRecipeTickInput(this, recipe)) return false;
        return RecipeRunner.matchRecipeTickOutput(this, recipe);
    }
}
