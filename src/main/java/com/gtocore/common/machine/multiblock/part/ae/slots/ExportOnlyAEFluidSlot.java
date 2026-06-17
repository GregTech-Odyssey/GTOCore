package com.gtocore.common.machine.multiblock.part.ae.slots;

import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.utils.GTMath;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.GenericStack;

import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ExportOnlyAEFluidSlot extends ExportOnlyAESlot implements IFluidHandler {

    FluidStack forgeStock = null;

    public ExportOnlyAEFluidSlot() {
        super();
    }

    ExportOnlyAEFluidSlot(@Nullable GenericStack config, @Nullable GenericStack stock) {
        super(config, stock);
    }

    @Override
    public void addStack(GenericStack stack) {
        if (this.stock == null) {
            this.stock = stack;
            this.forgeStock = null;
        } else {
            this.stock = GenericStack.sum(this.stock, stack);
            if (this.forgeStock != null) {
                this.forgeStock.setAmount(MathUtil.saturatedCast(this.forgeStock.getAmount() + stack.amount()));
            }
        }
        onContentsChanged();
    }

    @Override
    public void setStock(@Nullable GenericStack stack) {
        if (this.stock == null && stack == null) {
            return;
        } else if (stack == null) {
            this.stock = null;
        } else {
            if (stack.equals(stock)) return;
            this.stock = stack;
        }
        this.forgeStock = null;
        onContentsChanged();
    }

    public FluidStack getReadOnlyStack() {
        if (this.stock != null && this.stock.what() instanceof AEFluidKey fluidKey) {
            return fluidKey.getReadOnlyStack();
        }
        return FluidStack.EMPTY;
    }

    public FluidStack getStack() {
        if (this.stock != null && this.stock.what() instanceof AEFluidKey fluidKey) {
            if (forgeStock == null) forgeStock = fluidKey.toStack(GTMath.saturatedCast(this.stock.amount()));
            return forgeStock;
        }
        return FluidStack.EMPTY;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return getStack();
    }

    @Override
    public int getTankCapacity(int tank) {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return true;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    public long extract(long amount, boolean simulate, boolean notify) {
        if (this.stock == null || !(this.stock.what() instanceof AEFluidKey)) {
            return 0;
        }
        long drained = Math.min(this.stock.amount(), amount);
        if (!simulate) {
            this.stock = new GenericStack(this.stock.what(), this.stock.amount() - drained);
            if (this.stock.amount() == 0) {
                this.stock = null;
                forgeStock = null;
            } else if (forgeStock != null) forgeStock.setAmount(MathUtil.saturatedCast(stock.amount()));
            if (notify) onContentsChanged();
        }
        return drained;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (this.getStack().isFluidEqual(resource)) {
            return this.drain(resource.getAmount(), action);
        }
        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (this.stock == null || !(this.stock.what() instanceof AEFluidKey fluidKey)) return FluidStack.EMPTY;
        int drained = MathUtil.saturatedCast(extract(maxDrain, action.simulate(), true));
        if (drained < 1) return FluidStack.EMPTY;
        return fluidKey.toStack(drained);
    }

    @Override
    public ExportOnlyAEFluidSlot copy() {
        return new ExportOnlyAEFluidSlot(
                this.config == null ? null : ExportOnlyAESlot.copy(this.config),
                this.stock == null ? null : ExportOnlyAESlot.copy(this.stock));
    }
}
