package com.gtocore.common.machine.multiblock.electric.space;

import com.gtolib.api.machine.multiblock.CustomParallelMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import java.util.function.ToLongFunction;

public class MegaSpaceElevatorModuleMachine extends SpaceElevatorModuleMachine {

    public MegaSpaceElevatorModuleMachine(MetaMachineBlockEntity holder, boolean powerModuleTier) {
        super(holder, powerModuleTier);
    }

    public MegaSpaceElevatorModuleMachine(MetaMachineBlockEntity holder, boolean powerModuleTier, ToLongFunction<CustomParallelMultiblockMachine> getParallel) {
        super(holder, powerModuleTier, getParallel);
    }
}
