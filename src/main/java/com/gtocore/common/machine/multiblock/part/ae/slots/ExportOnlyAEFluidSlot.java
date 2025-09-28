package com.gtocore.common.machine.multiblock.part.ae.slots;

import com.gtolib.api.ae2.stacks.IAEFluidKey;
import com.gtolib.utils.MathUtil;

import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;
import com.gregtechceu.gtceu.utils.GTMath;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.GenericStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ExportOnlyAEFluidSlot extends ExportOnlyAESlot implements IFluidHandlerModifiable, IFluidTank {

    FluidStack stack = null;

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
            this.stack = null;
        } else {
            this.stock = GenericStack.sum(this.stock, stack);
            if (this.stack != null) {
                this.stack.setAmount(MathUtil.saturatedCast(this.stack.getAmount() + stack.amount()));
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
        this.stack = null;
        onContentsChanged();
    }

    public FluidStack getReadOnlyStack() {
        if (this.stock != null && this.stock.what() instanceof AEFluidKey fluidKey) {
            return ((IAEFluidKey) (Object) fluidKey).gtolib$getReadOnlyStack();
        }
        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack getFluid() {
        if (this.stock != null && this.stock.what() instanceof AEFluidKey fluidKey) {
            if (stack == null) stack = fluidKey.toStack(GTMath.saturatedCast(this.stock.amount()));
            return stack;
        }
        return FluidStack.EMPTY;
    }

    @Override
    public boolean isFluidValid(FluidStack stack) {
        return false;
    }

    @Override
    public int getFluidAmount() {
        return this.stock != null ? GTMath.saturatedCast(this.stock.amount()) : 0;
    }

    @Override
    public int getCapacity() {
        // Its capacity is always 0.
        return 0;
    }

    @Override
    public int getTanks() {
        return 0;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return getFluid();
    }

    @Override
    public void setFluidInTank(int tank, FluidStack stack) {}

    @Override
    public int getTankCapacity(int tank) {
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @Override
    public boolean supportsFill(int tank) {
        return false;
    }

    public long drain(long amount, boolean simulate, boolean notify) {
        if (this.stock == null || !(this.stock.what() instanceof AEFluidKey)) {
            return 0;
        }
        long drained = Math.min(this.stock.amount(), amount);
        if (!simulate) {
            this.stock = new GenericStack(this.stock.what(), this.stock.amount() - drained);
            if (this.stock.amount() == 0) {
                this.stock = null;
                stack = null;
            } else if (stack != null) stack.setAmount(MathUtil.saturatedCast(stock.amount()));
            if (notify) onContentsChanged();
        }
        return drained;
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (this.getFluid().isFluidEqual(resource)) {
            return this.drain(resource.getAmount(), action);
        }
        return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (this.stock == null || !(this.stock.what() instanceof AEFluidKey fluidKey)) {
            return FluidStack.EMPTY;
        }
        int drained = (int) Math.min(this.stock.amount(), maxDrain);
        FluidStack result = fluidKey.toStack(drained);
        if (action.execute()) {
            this.stock = new GenericStack(this.stock.what(), this.stock.amount() - drained);
            if (this.stock.amount() == 0) {
                this.stock = null;
                stack = null;
            } else if (stack != null) stack.setAmount(MathUtil.saturatedCast(stock.amount()));
            onContentsChanged();
        }
        return result;
    }

    @Override
    public boolean supportsDrain(int tank) {
        return tank == 0;
    }

    @Override
    public ExportOnlyAEFluidSlot copy() {
        return new ExportOnlyAEFluidSlot(
                this.config == null ? null : ExportOnlyAESlot.copy(this.config),
                this.stock == null ? null : ExportOnlyAESlot.copy(this.stock));
    }
}
