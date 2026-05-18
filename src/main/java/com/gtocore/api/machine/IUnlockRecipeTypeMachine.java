package com.gtocore.api.machine;

import com.gtolib.api.machine.feature.IEnhancedRecipeLogicMachine;
import com.gtolib.api.recipe.Recipe;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import org.jetbrains.annotations.Nullable;

public interface IUnlockRecipeTypeMachine extends IEnhancedRecipeLogicMachine {

    boolean canProcess(Recipe recipe);

    @Nullable
    default Recipe validateRecipe(@Nullable Recipe recipe) {
        if (recipe == null) return null;
        if (!canProcess(recipe)) {
            getEnhancedRecipeLogic().gtolib$setIdleReason(getLockedReason()
                    .append(": ")
                    .append(Component.translatable("gtceu." + recipe.recipeType.registryName.getPath())));
            return null;
        }
        return recipe;
    }

    default MutableComponent getLockedReason() {
        return Component.translatable("gtocore.machine.module.null");
    }
}
