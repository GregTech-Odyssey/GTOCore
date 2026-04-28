package com.gtocore.common.machine.multiblock.electric.gcym;

import com.gtocore.common.data.GTORecipeDataKeys;

import com.gtolib.api.machine.multiblock.TierCasingMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

public class GCYMMultiblockMachine extends TierCasingMultiblockMachine {

    public GCYMMultiblockMachine(MetaMachineBlockEntity holder) {
        super(holder, GTORecipeDataKeys.INTEGRAL_FRAMEWORK_TIER);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        tier = Math.min(getCasingTier(GTORecipeDataKeys.INTEGRAL_FRAMEWORK_TIER), tier);
    }

    @Override
    public boolean gtolib$canUpgraded() {
        return true;
    }
}
