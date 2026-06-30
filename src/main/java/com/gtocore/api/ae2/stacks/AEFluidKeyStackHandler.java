package com.gtocore.api.ae2.stacks;

import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.transfer.fluid.ICustomFluidStackHandler;

import net.minecraftforge.fluids.FluidStack;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.AEKeyMap;
import appeng.api.storage.MEStorage;

import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.LongSupplier;

public class AEFluidKeyStackHandler implements ICustomFluidStackHandler {

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
    public void setFluidInTank(int i, FluidStack fluidStack) {}

    @Override
    public int getTanks() {
        if (storageSupplier == null) return 0;
        return storageSupplier.getAsLong() < capacity ? 2 : 1;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        if (map == null || tank != 0) return FluidStack.EMPTY;
        for (var e : map) {
            if (e.getKey() instanceof AEFluidKey key) {
                return key.toStack(MathUtil.saturatedCast(e.getLongValue()));
            }
        }
        return FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank) {
        return storage == null ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return storage != null;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (storage == null || storageSupplier == null || storageSupplier.getAsLong() >= capacity) return 0;
        var amount = resource.getAmount();
        if (amount < 1) return 0;
        return (int) storage.insert(AEFluidKey.of(resource), amount, action.simulate() ? Actionable.SIMULATE : Actionable.MODULATE, IActionSource.empty());
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        if (storage == null) return FluidStack.EMPTY;
        var amount = resource.getAmount();
        if (amount < 1) return FluidStack.EMPTY;
        amount = (int) storage.extract(AEFluidKey.of(resource), amount, action.simulate() ? Actionable.SIMULATE : Actionable.MODULATE, IActionSource.empty());
        if (amount < 1) return FluidStack.EMPTY;
        return ICustomFluidStackHandler.copy(resource, amount);
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        if (storage == null || onChange == null) return FluidStack.EMPTY;
        if (maxDrain < 1) return FluidStack.EMPTY;
        for (var it = map.iterator(); it.hasNext();) {
            var e = it.next();
            if (e.getKey() instanceof AEFluidKey key) {
                var value = e.getLongValue();
                maxDrain = (int) Math.min(maxDrain, value);
                if (maxDrain < 1) return FluidStack.EMPTY;
                var stack = key.toStack(maxDrain);
                if (action.execute()) {
                    if (value == maxDrain) {
                        it.remove();
                    } else {
                        e.setValue(value - maxDrain);
                    }
                    onChange.run();
                }
                return stack;
            }
        }
        return FluidStack.EMPTY;
    }
}
