package com.gtocore.mixin.adastra;

import com.gtocore.common.data.GTOGlobes;

import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@org.spongepowered.asm.mixin.Mixin(earth.terrarium.adastra.common.registry.ModItems.class)
public class ModItemsMixin {

    @Inject(method = "<clinit>", at = @org.spongepowered.asm.mixin.injection.At("TAIL"))
    private static void addItems(CallbackInfo ci) {
        GTOGlobes.Items.init();
    }
}
