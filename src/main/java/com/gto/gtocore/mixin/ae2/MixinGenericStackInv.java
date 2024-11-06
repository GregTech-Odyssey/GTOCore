package com.gto.gtocore.mixin.ae2;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.AEKey;
import appeng.helpers.externalstorage.GenericStackInv;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GenericStackInv.class)
public abstract class MixinGenericStackInv {

    @Inject(method = "getMaxAmount", at = @At("HEAD"), cancellable = true, remap = false)
    private void getMaxAmountInj(AEKey key, CallbackInfoReturnable<Long> cir) {
        if (key instanceof AEItemKey) cir.setReturnValue(2147483647L);
    }
}
