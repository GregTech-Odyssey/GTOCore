package com.gtocore.common.recipe.condition;

import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.research.ResearchData;

import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;

import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;

public final class ResearchCondition extends AbstractRecipeCondition {

    public final ResearchData data;

    public ResearchCondition(ResearchData data) {
        this.data = data;
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("gtceu.recipe.research");
    }

    @Override
    public boolean test(@NotNull Recipe recipe, @NotNull RecipeLogic recipeLogic) {
        return true;
    }
}
