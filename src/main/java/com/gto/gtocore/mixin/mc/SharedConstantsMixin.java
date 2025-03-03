package com.gto.gtocore.mixin.mc;

import net.minecraft.SharedConstants;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SharedConstants.class, priority = 0)
public final class SharedConstantsMixin {

    @Inject(method = "enableDataFixerOptimizations", at = @At("HEAD"), cancellable = true)
    private static void enableDataFixerOptimizations(CallbackInfo ci) {
        ci.cancel();
    }
}
