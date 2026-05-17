package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.api.machine.IUnlockRecipeTypeMachine;
import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.machine.multiblock.CoilCrossRecipeMultiblockMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import org.jetbrains.annotations.NotNull;

public final class MoltenCoreMachine extends CoilCrossRecipeMultiblockMachine implements IUnlockRecipeTypeMachine {

    public MoltenCoreMachine(MetaMachineBlockEntity holder) {
        super(holder, false, false, false, false, m -> m.isFormed() ? 1L << Math.min(60, (int) (m.getTemperature() / 900.0D)) : 0);
    }

    @Override
    public boolean canProcess(Recipe recipe) {
        return formedAmount > 0 || recipe.recipeType == GTORecipeTypes.FLUID_HEATER_RECIPES;
    }

    @Override
    public Recipe getRealRecipe(@NotNull Recipe recipe) {
        return validateRecipe(super.getRealRecipe(recipe));
    }
}
