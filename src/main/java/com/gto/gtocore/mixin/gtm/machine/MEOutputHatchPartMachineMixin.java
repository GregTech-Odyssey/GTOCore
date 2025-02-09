package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.integration.ae2.machine.MEOutputHatchPartMachine;
import com.gregtechceu.gtceu.integration.ae2.utils.KeyStorage;

import com.hepdd.gtmthings.api.machine.trait.InaccessibleInfiniteTank;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MEOutputHatchPartMachine.class)
public class MEOutputHatchPartMachineMixin {

    @Shadow(remap = false)
    private KeyStorage internalBuffer;

    @Inject(method = "createTank", at = @At("HEAD"), remap = false, cancellable = true)
    private void createTank(int initialCapacity, int slots, Object[] args, CallbackInfoReturnable<NotifiableFluidTank> cir) {
        this.internalBuffer = new KeyStorage();
        cir.setReturnValue(new InaccessibleInfiniteTank((MEOutputHatchPartMachine) (Object) this, internalBuffer));
    }
}
