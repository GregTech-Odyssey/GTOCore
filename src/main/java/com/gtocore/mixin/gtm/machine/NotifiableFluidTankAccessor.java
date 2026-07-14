package com.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = NotifiableFluidTank.class, remap = false)
public interface NotifiableFluidTankAccessor {

    @Accessor("allowSameFluids")
    boolean gtocore$getAllowSameFluids();
}
