package com.gtocore.common.machine.multiblock.electric.nano;

import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.machine.multiblock.CrossRecipeMultiblockMachine;
import com.gtolib.api.recipe.RecipeType;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import org.jetbrains.annotations.NotNull;

public final class NanoPhagocytosisPlantMachine extends CrossRecipeMultiblockMachine {

    public NanoPhagocytosisPlantMachine(MetaMachineBlockEntity holder) {
        super(holder, false, true, MachineUtils::getHatchParallelLong);
    }

    @Override
    public GTRecipeType @NotNull [] getRecipeTypes() {
        return formedCount > 0 ? super.getRecipeTypes() : new RecipeType[] { GTORecipeTypes.MACERATOR_RECIPES };
    }

    @NotNull
    public RecipeType getRecipeType() {
        return formedCount > 0 ? super.getRecipeType() : GTORecipeTypes.MACERATOR_RECIPES;
    }
}
