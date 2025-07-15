package com.gtocore.common.machine.multiblock.part.ae.widget;

import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEItemList;
import com.gtocore.common.machine.multiblock.part.ae.slots.ExportOnlyAEItemSlot;
import com.gtocore.common.machine.multiblock.part.ae.widget.slot.AEItemConfigSlotWidget;

import com.gregtechceu.gtceu.integration.ae2.slot.IConfigurableSlot;

import appeng.api.stacks.GenericStack;

public class AEItemConfigWidget extends ConfigWidget {

    private final ExportOnlyAEItemList itemList;

    public AEItemConfigWidget(int x, int y, ExportOnlyAEItemList list) {
        super(x, y, list.getInventory(), list.isStocking());
        this.itemList = list;
    }

    @Override
    void init() {
        int line;
        this.displayList = new IConfigurableSlot[this.config.length];
        this.cached = new IConfigurableSlot[this.config.length];
        for (int index = 0; index < this.config.length; index++) {
            this.displayList[index] = new ExportOnlyAEItemSlot();
            this.cached[index] = new ExportOnlyAEItemSlot();
            line = index / 8;
            this.addWidget(new AEItemConfigSlotWidget((index - (line << 3)) * 18, line * 38, this, index));
        }
    }

    @Override
    public boolean hasStackInConfig(GenericStack stack) {
        return itemList.hasStackInConfig(stack, true);
    }

    @Override
    public boolean isAutoPull() {
        return itemList.isAutoPull();
    }
}
