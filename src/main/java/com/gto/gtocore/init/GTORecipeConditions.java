package com.gto.gtocore.init;

import com.gto.gtocore.common.recipe.condition.GravityCondition;
import com.gto.gtocore.common.recipe.condition.VacuumCondition;

import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.api.registry.GTRegistries;

public interface GTORecipeConditions {

    RecipeConditionType<GravityCondition> GRAVITY = GTRegistries.RECIPE_CONDITIONS.register("gravity",
            new RecipeConditionType<>(GravityCondition::new, GravityCondition.CODEC));

    RecipeConditionType<VacuumCondition> VACUUM = GTRegistries.RECIPE_CONDITIONS.register("vacuum",
            new RecipeConditionType<>(VacuumCondition::new, VacuumCondition.CODEC));

    static void init() {}
}
