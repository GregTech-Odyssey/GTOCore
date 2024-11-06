package com.gto.gtocore.mixin.gtm.cover;

import com.gregtechceu.gtceu.common.cover.InfiniteWaterCover;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(InfiniteWaterCover.class)
public class InfiniteWaterCoverMixin {

    @ModifyArg(method = "update", at = @At(value = "INVOKE", target = "Lcom/lowdragmc/lowdraglib/side/fluid/FluidStack;create(Lnet/minecraft/world/level/material/Fluid;J)Lcom/lowdragmc/lowdraglib/side/fluid/FluidStack;"), index = 1, remap = false)
    private long modifyContainer(long constant) {
        return Integer.MAX_VALUE;
    }
}
