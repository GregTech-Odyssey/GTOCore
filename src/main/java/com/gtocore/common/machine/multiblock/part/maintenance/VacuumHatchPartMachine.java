package com.gtocore.common.machine.multiblock.part.maintenance;

import com.gtolib.api.machine.feature.IVacuumMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.multiblock.part.AutoMaintenanceHatchPartMachine;

public final class VacuumHatchPartMachine extends AutoMaintenanceHatchPartMachine implements IVacuumMachine {

    public VacuumHatchPartMachine(MetaMachineBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    public int getVacuumTier() {
        return 4;
    }
}
