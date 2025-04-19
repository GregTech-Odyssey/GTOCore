package com.gto.gtocore.api.recipe;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface GTORecipeModifier extends RecipeModifier {

    default @NotNull ModifierFunction getModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe) {
        return ModifierFunction.IDENTITY;
    }

    GTRecipe applyModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe);
}
