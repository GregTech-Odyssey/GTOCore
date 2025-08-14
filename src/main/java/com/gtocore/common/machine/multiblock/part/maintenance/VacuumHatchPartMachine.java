package com.gtocore.common.machine.multiblock.part.maintenance;

import com.gtolib.api.machine.feature.IVacuumMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IWorkableMultiController;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.machine.multiblock.part.AutoMaintenanceHatchPartMachine;

public final class VacuumHatchPartMachine extends AutoMaintenanceHatchPartMachine implements IVacuumMachine {

    public VacuumHatchPartMachine(MetaMachineBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    public GTRecipe modifyRecipe(GTRecipe recipe) {
        return recipe;
    }

    @Override
    public boolean afterWorking(IWorkableMultiController controller) {
        return true;
    }

    @Override
    public int getVacuumTier() {
        return 4;
    }
}
