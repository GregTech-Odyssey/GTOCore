package com.gtocore.common.machine.multiblock.electric.assembly;

import com.gtolib.api.machine.multiblock.TierCasingParallelMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import static com.gtolib.api.GTOValues.GLASS_TIER;
import static com.gtolib.api.GTOValues.MACHINE_CASING_TIER;

public final class PrecisionAssemblerMachine extends TierCasingParallelMultiblockMachine {

    public PrecisionAssemblerMachine(MetaMachineBlockEntity holder) {
        super(holder, true, m -> 1 << (m.getCasingTier(GLASS_TIER)), GLASS_TIER, MACHINE_CASING_TIER);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        tier = Math.min(getCasingTier(MACHINE_CASING_TIER), tier);
    }
}
