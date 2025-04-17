package com.gto.gtocore.common.machine.multiblock.electric.gcym;

import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.machine.feature.IUpgradeMachine;
import com.gto.gtocore.api.machine.multiblock.TierCasingMultiblockMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

public class GCYMMultiblockMachine extends TierCasingMultiblockMachine implements IUpgradeMachine {

    public GCYMMultiblockMachine(IMachineBlockEntity holder) {
        super(holder, GTOValues.INTEGRAL_FRAMEWORK_TIER);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        tier = Math.min(getCasingTier(GTOValues.INTEGRAL_FRAMEWORK_TIER), tier);
    }

    @Override
    @SuppressWarnings("all")
    public boolean gtocore$canUpgraded() {
        return true;
    }
}
