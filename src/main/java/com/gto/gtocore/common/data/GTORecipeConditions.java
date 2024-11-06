package com.gto.gtocore.common.data;

import com.gto.gtocore.common.recipe.condition.GravityCondition;
import com.gto.gtocore.common.recipe.condition.VacuumCondition;

import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

public final class GTORecipeConditions {

    private GTORecipeConditions() {}

    public static final RecipeConditionType<GravityCondition> GRAVITY = GTRegistries.RECIPE_CONDITIONS.register("gravity",
            new RecipeConditionType<>(GravityCondition::new, GravityCondition.CODEC));

    public static final RecipeConditionType<VacuumCondition> VACUUM = GTRegistries.RECIPE_CONDITIONS.register("vacuum",
            new RecipeConditionType<>(VacuumCondition::new, VacuumCondition.CODEC));

    public static void init() {}
}
