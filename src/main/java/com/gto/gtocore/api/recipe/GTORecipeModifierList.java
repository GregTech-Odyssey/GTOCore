package com.gto.gtocore.api.recipe;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class GTORecipeModifierList implements GTORecipeModifier {

    private final RecipeModifier[] modifiers;

    public GTORecipeModifierList(RecipeModifier... modifiers) {
        this.modifiers = modifiers;
    }

    public GTORecipeModifierList(GTORecipeModifier... modifiers) {
        this.modifiers = modifiers;
    }

    @Override
    public @Nullable GTRecipe applyModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe) {
        for (RecipeModifier modifier : modifiers) {
            recipe = modifier.applyModifier(machine, recipe);
            if (recipe == null) return null;
        }
        return recipe;
    }
}
