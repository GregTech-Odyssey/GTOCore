package com.gtocore.common.machine.multiblock.electric.nano;

import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.machine.multiblock.CrossRecipeMultiblockMachine;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

public final class NanoPhagocytosisPlantMachine extends CrossRecipeMultiblockMachine {

    public NanoPhagocytosisPlantMachine(MetaMachineBlockEntity holder) {
        super(holder, false, true, MachineUtils::getHatchParallel);
    }

    @Override
    public boolean recipeTypeAvailable(GTRecipeType type) {
        return formedAmount > 0 || type == GTORecipeTypes.MACERATOR_RECIPES;
    }
}
