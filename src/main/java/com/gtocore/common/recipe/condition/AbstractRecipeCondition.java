package com.gtocore.common.recipe.condition;

import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;

import org.jetbrains.annotations.NotNull;

abstract class AbstractRecipeCondition extends RecipeCondition {

    static final RecipeConditionType<ResearchCondition> RESEARCH = new RecipeConditionType<>(null, null);

    static final RecipeConditionType<GravityCondition> GRAVITY = new RecipeConditionType<>(null, null);

    static final RecipeConditionType<VacuumCondition> VACUUM = new RecipeConditionType<>(null, null);

    static final RecipeConditionType<RestrictedMachineCondition> RESTRICTED_MACHINE = new RecipeConditionType<>(null, null);

    static final RecipeConditionType<HeatCondition> HEAT = new RecipeConditionType<>(null, null);

    static final RecipeConditionType<RunLimitCondition> RUN_LIMIT = new RecipeConditionType<>(null, null);

    @Override
    public final RecipeCondition createTemplate() {
        return null;
    }

    @Override
    protected final boolean testCondition(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        return test((Recipe) recipe, recipeLogic);
    }

    protected abstract boolean test(@NotNull Recipe recipe, @NotNull RecipeLogic recipeLogic);
}
