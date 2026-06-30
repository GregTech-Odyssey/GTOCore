package com.gtocore.common.machine.multiblock.part.maintenance;

import com.gtocore.api.machine.part.IVacuumPartMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.multiblock.part.AutoMaintenanceHatchPartMachine;

public final class VacuumHatchPartMachine extends AutoMaintenanceHatchPartMachine implements IVacuumPartMachine {

    public VacuumHatchPartMachine(MetaMachineBlockEntity blockEntity) {
        super(blockEntity);
    }

    @Override
    public int getVacuumTier() {
        return 4;
    }
}
