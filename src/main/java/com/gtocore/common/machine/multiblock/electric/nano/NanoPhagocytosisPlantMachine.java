package com.gtocore.common.machine.multiblock.electric.nano;

import com.gtocore.api.machine.IUnlockRecipeTypeMachine;
import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.machine.multiblock.CrossRecipeMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import org.jetbrains.annotations.NotNull;

public final class NanoPhagocytosisPlantMachine extends CrossRecipeMultiblockMachine implements IUnlockRecipeTypeMachine {

    public NanoPhagocytosisPlantMachine(MetaMachineBlockEntity holder) {
        super(holder, false, true, MachineUtils::getHatchParallel);
    }

    @Override
    public boolean canProcess(Recipe recipe) {
        return formedAmount > 0 || recipe.recipeType == GTORecipeTypes.MACERATOR_RECIPES;
    }

    @Override
    public Recipe getRealRecipe(@NotNull Recipe recipe) {
        return validateRecipe(super.getRealRecipe(recipe));
    }
}
