package com.gto.gtocore.api.machine.trait;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

public final class NotifiableProgrammableCircuitHandler extends NotifiableNotConsumableItemHandler {

    public NotifiableProgrammableCircuitHandler(MetaMachine machine) {
        super(machine, 1, IO.NONE, ItemStackHandler::new);
    }

    private static class ItemStackHandler extends CustomItemStackHandler {

        private ItemStackHandler(int size) {
            super(size);
        }

        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    }
}
