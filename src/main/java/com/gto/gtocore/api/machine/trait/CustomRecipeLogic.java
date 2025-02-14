package com.gto.gtocore.api.machine.trait;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import java.util.function.Supplier;

public class CustomRecipeLogic extends RecipeLogic implements ILockableRecipe {

    private final Supplier<GTRecipe> recipeSupplier;

    public CustomRecipeLogic(IRecipeLogicMachine machine, Supplier<GTRecipe> recipeSupplier) {
        super(machine);
        this.recipeSupplier = recipeSupplier;
    }

    @Override
    public void findAndHandleRecipe() {
        lastRecipe = null;
        GTRecipe match = recipeSupplier.get();
        if (match != null) {
            setupRecipe(match);
        }
    }

    @Override
    public void onRecipeFinish() {
        machine.afterWorking();
        if (lastRecipe != null) {
            handleRecipeIO(lastRecipe, IO.OUT);
        }
        if (suspendAfterFinish) {
            setStatus(Status.SUSPEND);
            suspendAfterFinish = false;
        } else {
            GTRecipe match = recipeSupplier.get();
            if (match != null) {
                setupRecipe(match);
                return;
            }
            setStatus(Status.IDLE);
        }
        progress = 0;
        duration = 0;
        isActive = false;
    }
}
