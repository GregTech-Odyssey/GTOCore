package com.gtocore.api.capability;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import org.jetbrains.annotations.NotNull;

public class FluidHandlerList implements IFluidHandler {

    private final IFluidHandler[] handlers;
    private final int size;

    public FluidHandlerList(IFluidHandler... handlers) {
        this.handlers = handlers;
        int size = 0;
        for (IFluidHandler handler : handlers) {
            size += handler.getTanks();
        }
        this.size = size;
    }

    @Override
    public int getTanks() {
        return size;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        for (IFluidHandler handler : handlers) {
            if (tank < handler.getTanks()) {
                return handler.getFluidInTank(tank);
            }
            tank -= handler.getTanks();
        }
        return FluidStack.EMPTY;
    }

    @Override
    public int getTankCapacity(int tank) {
        for (IFluidHandler handler : handlers) {
            if (tank < handler.getTanks()) {
                return handler.getTankCapacity(tank);
            }
            tank -= handler.getTanks();
        }
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        for (IFluidHandler handler : handlers) {
            if (tank < handler.getTanks()) {
                return handler.isFluidValid(tank, stack);
            }
            tank -= handler.getTanks();
        }
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty()) return 0;
        var copied = resource.copy();
        for (IFluidHandler handler : handlers) {
            copied.shrink(handler.fill(copied.copy(), action));
            if (copied.isEmpty()) break;
        }
        return resource.getAmount() - copied.getAmount();
    }

    @Override
    @NotNull
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty()) return FluidStack.EMPTY;
        var copied = resource.copy();
        for (IFluidHandler handler : handlers) {
            copied.shrink(handler.drain(copied.copy(), action).getAmount());
            if (copied.isEmpty()) break;
        }
        copied.setAmount(resource.getAmount() - copied.getAmount());
        return copied;
    }

    @Override
    @NotNull
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (maxDrain == 0) return FluidStack.EMPTY;
        FluidStack totalDrained = null;
        for (IFluidHandler handler : handlers) {
            if (totalDrained == null || totalDrained.isEmpty()) {
                totalDrained = handler.drain(maxDrain, action);
                if (totalDrained.isEmpty()) totalDrained = null;
                else maxDrain -= totalDrained.getAmount();
            } else {
                FluidStack copy = totalDrained.copy();
                copy.setAmount(maxDrain);
                FluidStack drain = handler.drain(copy, action);
                totalDrained.grow(drain.getAmount());
                maxDrain -= drain.getAmount();
            }
            if (maxDrain <= 0) break;
        }
        return totalDrained == null ? FluidStack.EMPTY : totalDrained;
    }
}
