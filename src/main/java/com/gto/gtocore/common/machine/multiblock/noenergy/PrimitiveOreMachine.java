package com.gto.gtocore.common.machine.multiblock.noenergy;

import com.gto.gtocore.common.data.GTOOres;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;

public final class PrimitiveOreMachine extends WorkableMultiblockMachine {

    private int delay = 1;

    public PrimitiveOreMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public boolean onWorking() {
        if (!super.onWorking()) return false;
        if (getOffsetTimer() % delay == 0) {
            if (getLevel() == null) return false;
            if (MachineUtils.outputItem(this, ChemicalHelper.get(TagPrefix.rawOre, GTOOres.selectMaterial(getLevel().dimension().location())).copyWithCount(64))) {
                delay = 1;
            } else if (delay < 20) {
                delay++;
            }
        }
        return true;
    }
}
