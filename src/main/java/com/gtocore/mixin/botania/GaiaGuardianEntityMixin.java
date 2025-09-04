package com.gtocore.mixin.botania;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.entity.GaiaGuardianEntity;

@Mixin(GaiaGuardianEntity.class)
public class GaiaGuardianEntityMixin {

    @Mutable
    @Shadow(remap = false)
    @Final
    private static int DAMAGE_CAP;

    @Inject(method = "<clinit>", at = @At("TAIL"), remap = false)
    private static void init(CallbackInfo ci) {
        DAMAGE_CAP = 10000;
    }
}
