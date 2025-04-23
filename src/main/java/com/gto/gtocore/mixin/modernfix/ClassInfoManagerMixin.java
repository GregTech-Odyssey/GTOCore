package com.gto.gtocore.mixin.modernfix;

import com.gto.gtocore.common.CommonProxy;

import com.gregtechceu.gtceu.GTCEu;

import org.embeddedt.modernfix.util.ClassInfoManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClassInfoManager.class)
public class ClassInfoManagerMixin {

    @Shadow(remap = false)
    private static boolean hasRun;

    @Inject(method = "clear", at = @At("HEAD"), remap = false, cancellable = true)
    private static void clear(CallbackInfo ci) {
        if (hasRun) return;
        CommonProxy.afterStartup();
        if (GTCEu.isClientSide()) return;
        ci.cancel();
    }
}
