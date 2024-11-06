package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DualHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.ItemBusPartMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DualHatchPartMachine.class)
public class DualHatchPartMachineMixin extends ItemBusPartMachine {

    public DualHatchPartMachineMixin(IMachineBlockEntity holder, int tier, IO io, Object... args) {
        super(holder, tier, io, args);
    }

    @Inject(method = "getTankCapacity", at = @At("HEAD"), remap = false, cancellable = true)
    private static void getTankCapacity(long initialCapacity, int tier, CallbackInfoReturnable<Long> cir) {
        cir.setReturnValue(initialCapacity * (1L << tier));
    }

    @Inject(method = "getInventorySize", at = @At("HEAD"), remap = false, cancellable = true)
    public void getInventorySize(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(super.getInventorySize());
    }

    @Inject(method = "isDistinct", at = @At("HEAD"), remap = false, cancellable = true)
    public void isDistinct(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(super.isDistinct());
    }

    @Inject(method = "setDistinct", at = @At("HEAD"), remap = false, cancellable = true)
    public void setDistinct(boolean isDistinct, CallbackInfo ci) {
        super.setDistinct(isDistinct);
        ci.cancel();
    }
}
