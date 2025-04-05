package com.gto.gtocore.api.fluid;

import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

public final class StrictFluidStack extends FluidStack {

    public StrictFluidStack(FluidStack fluid) {
        super(fluid, fluid.getAmount());
    }

    @Override
    public boolean isFluidEqual(@NotNull FluidStack other) {
        return getFluid() == other.getFluid() && getAmount() == other.getAmount() && getTag() == null ? other.getTag() == null : other.getTag() != null && getTag().equals(other.getTag());
    }
}
