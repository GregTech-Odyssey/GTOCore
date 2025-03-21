package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.api.machine.trait.InaccessibleInfiniteTank;
import com.gto.gtocore.integration.ae2.KeyMap;

import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.integration.ae2.machine.MEOutputHatchPartMachine;
import com.gregtechceu.gtceu.integration.ae2.utils.KeyStorage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MEOutputHatchPartMachine.class)
public class MEOutputHatchPartMachineMixin {

    @Shadow(remap = false)
    private KeyStorage internalBuffer;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    protected boolean shouldSubscribe() {
        return false;
    }

    @Inject(method = "createTank", at = @At("HEAD"), remap = false, cancellable = true)
    private void createTank(int initialCapacity, int slots, Object[] args, CallbackInfoReturnable<NotifiableFluidTank> cir) {
        this.internalBuffer = new KeyMap();
        cir.setReturnValue(new InaccessibleInfiniteTank((MEOutputHatchPartMachine) (Object) this, (KeyMap) internalBuffer));
    }
}
