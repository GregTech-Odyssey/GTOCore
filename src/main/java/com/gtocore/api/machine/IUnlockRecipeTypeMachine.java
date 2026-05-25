package com.gtocore.api.machine;

import com.gtolib.api.machine.feature.IEnhancedRecipeLogicMachine;

import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import java.util.ArrayList;

public interface IUnlockRecipeTypeMachine extends IEnhancedRecipeLogicMachine {

    boolean canProcess(GTRecipeType type);

    @Override
    default GTRecipeType[] getAvailableRecipeTypes() {
        var types = IEnhancedRecipeLogicMachine.super.getAvailableRecipeTypes();
        var list = new ArrayList<GTRecipeType>(types.length);
        for (var type : types) {
            if (canProcess(type)) list.add(type);
        }
        return list.toArray(new GTRecipeType[0]);
    }
}
