package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DualHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.FluidHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.storage.BufferMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BufferMachine.class)
public class BufferMachineMixin extends TieredMachine {

    public BufferMachineMixin(IMachineBlockEntity holder, int tier) {
        super(holder, tier);
    }

    @Inject(method = "createTank", at = @At("HEAD"), remap = false, cancellable = true)
    protected void createTank(Object[] args, CallbackInfoReturnable<NotifiableFluidTank> cir) {
        cir.setReturnValue(new NotifiableFluidTank(this, tier + 2, FluidHatchPartMachine.getTankCapacity(
                DualHatchPartMachine.INITIAL_TANK_CAPACITY, tier), IO.BOTH));
    }
}
