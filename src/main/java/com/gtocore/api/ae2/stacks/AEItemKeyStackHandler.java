package com.gtocore.api.ae2.stacks;

import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.transfer.item.ICustomItemStackHandler;

import net.minecraft.world.item.ItemStack;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyMap;
import appeng.api.storage.MEStorage;

import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.LongSupplier;

public class AEItemKeyStackHandler implements ICustomItemStackHandler {

    @Nullable
    @Setter
    protected MEStorage storage;
    @Setter
    protected AEKeyMap<AEKey> map;
    @Setter
    protected Runnable onChange;

    @Setter
    protected long capacity;
    @Setter
    protected LongSupplier storageSupplier;

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {}

    @Override
    public int getSlots() {
        if (storageSupplier == null) return 0;
        return storageSupplier.getAsLong() < capacity ? 2 : 1;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        if (map == null || slot != 0) return ItemStack.EMPTY;
        for (var e : map) {
            if (e.getKey() instanceof AEItemKey key) {
                return key.toStack(MathUtil.saturatedCast(e.getLongValue()));
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (storage == null || storageSupplier == null || storageSupplier.getAsLong() >= capacity) return stack;
        var count = stack.getCount();
        if (count < 1) return stack;
        var r = count - storage.insert(AEItemKey.of(stack), count, simulate ? Actionable.SIMULATE : Actionable.MODULATE, IActionSource.empty());
        if (r < 1) return ItemStack.EMPTY;
        return stack.copyWithCount((int) r);
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (storage == null || slot != 0 || onChange == null) return ItemStack.EMPTY;
        if (amount < 1) return ItemStack.EMPTY;
        for (var it = map.iterator(); it.hasNext();) {
            var e = it.next();
            if (e.getKey() instanceof AEItemKey key) {
                var value = e.getLongValue();
                amount = (int) Math.min(amount, value);
                if (amount < 1) return ItemStack.EMPTY;
                var stack = key.toStack(amount);
                if (!simulate) {
                    if (value == amount) {
                        it.remove();
                    } else {
                        e.setValue(value - amount);
                    }
                    onChange.run();
                }
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return storage == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return storage != null;
    }
}
