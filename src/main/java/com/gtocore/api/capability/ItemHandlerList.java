package com.gtocore.api.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import org.jetbrains.annotations.NotNull;

public class ItemHandlerList implements IItemHandler {

    private final IItemHandler[] handlers;
    private final int size;

    public ItemHandlerList(IItemHandler... handlers) {
        this.handlers = handlers;
        int size = 0;
        for (IItemHandler handler : handlers) {
            size += handler.getSlots();
        }
        this.size = size;
    }

    @Override
    public int getSlots() {
        return size;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        for (IItemHandler handler : handlers) {
            if (slot < handler.getSlots()) {
                return handler.getStackInSlot(slot);
            }
            slot -= handler.getSlots();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        for (IItemHandler handler : handlers) {
            if (slot < handler.getSlots()) {
                return handler.insertItem(slot, stack, simulate);
            }
            slot -= handler.getSlots();
        }
        return stack;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        for (IItemHandler handler : handlers) {
            if (slot < handler.getSlots()) {
                return handler.extractItem(slot, amount, simulate);
            }
            slot -= handler.getSlots();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        for (IItemHandler handler : handlers) {
            if (slot < handler.getSlots()) {
                return handler.getSlotLimit(slot);
            }
            slot -= handler.getSlots();
        }
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        for (IItemHandler handler : handlers) {
            if (slot < handler.getSlots()) {
                return handler.isItemValid(slot, stack);
            }
            slot -= handler.getSlots();
        }
        return false;
    }
}
