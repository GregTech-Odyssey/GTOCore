package com.gto.gtocore.api.machine.multiblock;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import static com.gto.gtocore.common.data.GTORecipeModifiers.getHatchParallel;

public class WorkableElectricParallelHatchMultipleRecipesMachine extends WorkableElectricMultipleRecipesMachine {

    public WorkableElectricParallelHatchMultipleRecipesMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public int getParallel() {
        return isFormed() ? getHatchParallel(this) : 0;
    }
}
