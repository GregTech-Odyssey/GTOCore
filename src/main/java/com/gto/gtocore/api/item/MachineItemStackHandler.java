package com.gto.gtocore.api.item;

import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import java.util.function.IntSupplier;

public class MachineItemStackHandler extends CustomItemStackHandler {

    private final IntSupplier supplier;

    public MachineItemStackHandler(IntSupplier slotLimit, Runnable onContentsChanged) {
        super(1);
        this.supplier = slotLimit;
        setOnContentsChanged(onContentsChanged);
    }

    @Override
    public int getSlotLimit(int slot) {
        return supplier.getAsInt();
    }
}
