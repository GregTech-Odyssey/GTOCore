package com.gto.gtocore.common.machine.noenergy;

import com.gto.gtocore.api.machine.SimpleNoEnergyMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

public class HeaterMachine extends SimpleNoEnergyMachine {

    public HeaterMachine(IMachineBlockEntity holder) {
        super(holder, 0, i -> 8000);
    }
}
