package com.gtocore.common.machine.multiblock.electric.gcym;

import com.gtolib.api.GTOValues;
import com.gtolib.api.machine.multiblock.TierCasingMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

public class GCYMMultiblockMachine extends TierCasingMultiblockMachine {

    public GCYMMultiblockMachine(MetaMachineBlockEntity holder) {
        super(holder, GTOValues.INTEGRAL_FRAMEWORK_TIER);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        tier = Math.min(getCasingTier(GTOValues.INTEGRAL_FRAMEWORK_TIER), tier);
    }

    @Override
    public boolean gtolib$canUpgraded() {
        return true;
    }
}
