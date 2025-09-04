package com.gtocore.common.machine.multiblock.electric.space;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

public final class SuperSpaceElevatorMachine extends SpaceElevatorMachine {

    public SuperSpaceElevatorMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    void initialize() {}

    @Override
    public int getMaxSpoolCount() {
        return 1024;
    }

    @Override
    int getBaseHigh() {
        return 200;
    }
}
