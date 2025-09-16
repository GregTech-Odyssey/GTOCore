package com.gtocore.mixin.ae2.wtlib;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
import appeng.helpers.WirelessTerminalMenuHost;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WirelessTerminalMenuHost.class)
public abstract class WirelessTerminalMenuHostMixin {

    @Shadow(remap = false)
    public abstract boolean rangeCheck();

    @Inject(method = "extractAEPower", at = @At("HEAD"), cancellable = true, remap = false)
    private void extractAEPower(double amt, Actionable mode, PowerMultiplier usePowerMultiplier, CallbackInfoReturnable<Double> cir) {
        if (!rangeCheck()) {
            cir.setReturnValue(0.0);
        }
    }

    @Inject(method = "onBroadcastChanges", at = @At("RETURN"), remap = false, cancellable = true)
    private void onBroadcastChanges(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
