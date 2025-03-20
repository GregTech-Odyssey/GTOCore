package com.gto.gtocore.common.machine.multiblock.part.maintenance;

import com.gto.gtocore.api.machine.feature.IVacuumMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.machine.multiblock.part.AutoMaintenanceHatchPartMachine;

public final class VacuumHatchPartMachine extends AutoMaintenanceHatchPartMachine implements IVacuumMachine {

    public VacuumHatchPartMachine(IMachineBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    public GTRecipe modifyRecipe(GTRecipe recipe) {
        return recipe;
    }

    @Override
    public int getVacuumTier() {
        return 4;
    }
}
