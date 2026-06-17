package com.gtocore.mixin.adastra;

import earth.terrarium.adastra.client.AdAstraClient;
import earth.terrarium.adastra.client.ClientPlatformUtils;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Consumer;

@Mixin(AdAstraClient.class)
public class AdAstraClientMixin {

    /**
     * @author
     * @reason
     */
    @org.spongepowered.asm.mixin.Overwrite(remap = false)
    public static void onRegisterHud(Consumer<ClientPlatformUtils.RenderHud> consumer) {}
}
