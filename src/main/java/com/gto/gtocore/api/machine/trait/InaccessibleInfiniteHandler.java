package com.gto.gtocore.api.machine.trait;

import com.gto.gtocore.integration.ae2.KeyMap;
import com.gto.gtocore.utils.ItemUtils;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;
import com.gregtechceu.gtceu.api.transfer.item.CustomItemStackHandler;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import appeng.api.stacks.AEItemKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class InaccessibleInfiniteHandler extends NotifiableItemStackHandler {

    private final ItemStackHandlerDelegate delegate;

    public InaccessibleInfiniteHandler(MetaMachine holder, KeyMap internalBuffer) {
        super(holder, 1, IO.OUT, IO.NONE, i -> new ItemStackHandlerDelegate(internalBuffer));
        internalBuffer.setOnContentsChanged(this::onContentsChanged);
        delegate = ((ItemStackHandlerDelegate) storage);
    }

    @Override
    public List<Ingredient> handleRecipe(IO io, GTRecipe recipe, List<?> left, @Nullable String slotName, boolean simulate) {
        if (!simulate && io == IO.OUT) {
            for (Object ingredient : left) {
                if (((Ingredient) ingredient).isEmpty()) continue;
                ItemStack item;
                int count;
                if (ingredient instanceof SizedIngredient sizedIngredient) {
                    item = ItemUtils.getFirstSized(sizedIngredient);
                    count = sizedIngredient.getAmount();
                } else {
                    item = ItemUtils.getFirst((Ingredient) ingredient);
                    count = item.getCount();
                }
                if (item.isEmpty()) continue;
                delegate.insertItem(item, count);
            }
            delegate.internalBuffer.onChanged();
            return null;
        }
        return null;
    }

    @Override
    public List<Object> getContents() {
        return Collections.emptyList();
    }

    @Override
    public double getTotalContentAmount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    private static class ItemStackHandlerDelegate extends CustomItemStackHandler {

        private final KeyMap internalBuffer;

        private ItemStackHandlerDelegate(KeyMap internalBuffer) {
            super();
            this.internalBuffer = internalBuffer;
        }

        private void insertItem(ItemStack stack, int count) {
            var key = AEItemKey.of(stack);
            long oldValue = internalBuffer.storage.getOrDefault(key, 0);
            long changeValue = Math.min(Long.MAX_VALUE - oldValue, count);
            KeyMap.put(internalBuffer.storage, key, oldValue + changeValue);
        }

        @Override
        public int getSlots() {
            return Short.MAX_VALUE;
        }

        @Override
        public int getSlotLimit(int slot) {
            return Integer.MAX_VALUE;
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            return ItemStack.EMPTY;
        }

        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack stack) {}

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            var key = AEItemKey.of(stack);
            int count = stack.getCount();
            long oldValue = internalBuffer.storage.getOrDefault(key, 0);
            long changeValue = Math.min(Long.MAX_VALUE - oldValue, count);
            if (!simulate) {
                KeyMap.put(internalBuffer.storage, key, oldValue + changeValue);
            } else if (count != changeValue) {
                return stack.copyWithCount((int) (count - changeValue));
            }
            return ItemStack.EMPTY;
        }
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }
}
