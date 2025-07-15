package com.gtocore.common.machine.multiblock.part.ae.widget;

import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEFluidList;
import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEFluidSlot;
import com.gtocore.common.machine.multiblock.part.ae.widget.slot.AEFluidConfigSlotWidget;

import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlot;

import appeng.api.stacks.GenericStack;

public class AEFluidConfigWidget extends ConfigWidget {

    private final ExportOnlyAEFluidList fluidList;

    public AEFluidConfigWidget(int x, int y, ExportOnlyAEFluidList list) {
        super(x, y, list.getInventory(), list.isStocking());
        this.fluidList = list;
    }

    @Override
    void init() {
        int line;
        this.displayList = new IConfigurableSlot[this.config.length];
        this.cached = new IConfigurableSlot[this.config.length];
        for (int index = 0; index < this.config.length; index++) {
            this.displayList[index] = new ExportOnlyAEFluidSlot();
            this.cached[index] = new ExportOnlyAEFluidSlot();
            line = index / 8;
            this.addWidget(new AEFluidConfigSlotWidget((index - (line << 3)) * 18, line * 38, this, index));
        }
    }

    @Override
    public boolean hasStackInConfig(GenericStack stack) {
        return fluidList.hasStackInConfig(stack, true);
    }

    @Override
    public boolean isAutoPull() {
        return fluidList.isAutoPull();
    }
}
