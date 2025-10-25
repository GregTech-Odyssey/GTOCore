package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.machine.multiblock.CoilCrossRecipeMultiblockMachine;
import com.gtolib.api.recipe.RecipeType;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import org.jetbrains.annotations.NotNull;

public final class MoltenCoreMachine extends CoilCrossRecipeMultiblockMachine {

    public MoltenCoreMachine(MetaMachineBlockEntity holder) {
        super(holder, false, false, false, false, m -> m.isFormed() ? 1L << Math.min(60, (int) (m.getTemperature() / 900.0D)) : 0);
    }

    @NotNull
    public RecipeType getRecipeType() {
        return formedCount > 0 ? super.getRecipeType() : GTORecipeTypes.FLUID_HEATER_RECIPES;
    }
}
