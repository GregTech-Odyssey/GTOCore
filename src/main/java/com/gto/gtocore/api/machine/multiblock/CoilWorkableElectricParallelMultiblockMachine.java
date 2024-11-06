package com.gto.gtocore.api.machine.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.CoilWorkableElectricMultiblockMachine;

public class CoilWorkableElectricParallelMultiblockMachine extends CoilWorkableElectricMultiblockMachine implements IParallelMachine {

    public CoilWorkableElectricParallelMultiblockMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public int getParallel() {
        return isFormed() ? Math.min(2147483647, (int) Math.pow(2, getCoilType().getCoilTemperature() / 900D)) : 0;
    }
}
