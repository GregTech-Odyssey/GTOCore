package com.gto.gtocore.mixin.gtm;

import com.gto.gtocore.utils.NumberUtils;

import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OverclockingLogic.class)
public class OverclockingLogicMixin {

    @Inject(method = "getOverclockForTier", at = @At("HEAD"), remap = false, cancellable = true)
    protected void getOverclockForTier(long voltage, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(NumberUtils.getFakeVoltageTier(voltage));
    }
}
