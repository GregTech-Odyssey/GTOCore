package com.gtocore.mixin.ftbxmodcompat;

import dev.ftb.mods.ftbxmodcompat.FTBXModCompat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FTBXModCompat.class)
public class FTBXModCompatMixin {

    @Inject(method = "detectLoadedMods", at = @At("TAIL"), remap = false)
    private static void detectLoadedMods(CallbackInfo ci) {
        FTBXModCompat.isJEILoaded = false;
    }
}
