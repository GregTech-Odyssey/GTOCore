package com.gtocore.common.machine.multiblock.electric.assembly;

import com.gtocore.common.data.GTORecipeDataKeys;

import com.gtolib.api.machine.multiblock.TierCasingParallelMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

public final class PrecisionAssemblerMachine extends TierCasingParallelMultiblockMachine {

    public PrecisionAssemblerMachine(MetaMachineBlockEntity holder) {
        super(holder, m -> 1L << (2 * (m.getCasingTier(GTORecipeDataKeys.GLASS_TIER))), GTORecipeDataKeys.GLASS_TIER, GTORecipeDataKeys.MACHINE_CASING_TIER);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        tier = Math.min(getCasingTier(GTORecipeDataKeys.MACHINE_CASING_TIER), tier);
    }
}
