package com.gtocore.common.machine.multiblock.electric.processing;

import com.gtolib.api.machine.multiblock.CrossRecipeMultiblockMachine;
import com.gtolib.utils.MachineUtils;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

public final class IntegratedOreProcessor extends CrossRecipeMultiblockMachine {

    public IntegratedOreProcessor(MetaMachineBlockEntity holder) {
        super(holder, false, true, MachineUtils::getHatchParallelLong);
    }

    @Override
    public int getThread() {
        return getSubFormedAmount() > 0 ? 8 : 1;
    }
}
