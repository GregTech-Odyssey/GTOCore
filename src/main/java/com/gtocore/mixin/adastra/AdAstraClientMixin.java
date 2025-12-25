package com.gtocore.mixin.adastra;

import earth.terrarium.adastra.client.AdAstraClient;
import earth.terrarium.adastra.client.ClientPlatformUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(AdAstraClient.class)
public class AdAstraClientMixin {

    @Inject(method = "onRegisterHud", at = @At("HEAD"), remap = false, cancellable = true)
    private static void onRegisterHud(Consumer<ClientPlatformUtils.RenderHud> consumer, CallbackInfo ci) {
        ci.cancel();
    }
}
