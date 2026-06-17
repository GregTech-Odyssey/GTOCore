package com.gtocore.common.machine.multiblock.part;

import com.gtocore.api.machine.part.IVacuumPartMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;

public class VacuumInterfacePartMachine extends MultiblockPartMachine implements IVacuumPartMachine {

    public VacuumInterfacePartMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }
}
