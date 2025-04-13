package com.gto.gtocore.common.machine.multiblock.noenergy;

import com.gto.gtocore.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gto.gtocore.common.data.GTOOres;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

public final class PrimitiveOreMachine extends NoEnergyMultiblockMachine {

    private int delay = 1;

    public PrimitiveOreMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public boolean onWorking() {
        if (!super.onWorking()) return false;
        if (getOffsetTimer() % delay == 0) {
            if (getLevel() == null) return false;
            if (outputItem(ChemicalHelper.get(TagPrefix.rawOre, GTOOres.selectMaterial(getLevel().dimension().location())).copyWithCount(64))) {
                delay = 1;
            } else if (delay < 20) {
                delay++;
            }
        }
        return true;
    }
}
