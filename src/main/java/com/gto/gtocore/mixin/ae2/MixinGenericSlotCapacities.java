package com.gto.gtocore.mixin.ae2;

import appeng.api.behaviors.GenericSlotCapacities;
import appeng.api.stacks.AEKeyType;
import appeng.util.CowMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GenericSlotCapacities.class)
public class MixinGenericSlotCapacities {

    @Final
    @Shadow(remap = false)
    private static CowMap<AEKeyType, Long> map;

    @Inject(method = "register", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void register(AEKeyType type, Long capacity, CallbackInfo ci) {
        if (!map.getMap().containsKey(AEKeyType.items())) {
            map.putIfAbsent(AEKeyType.items(), 2147483647L);
            map.putIfAbsent(AEKeyType.fluids(), 2147483647L);
        }
        ci.cancel();
    }
}
