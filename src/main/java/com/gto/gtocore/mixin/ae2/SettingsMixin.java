package com.gto.gtocore.mixin.ae2;

import com.gto.gtocore.integration.ae2.GTOSettings;

import appeng.api.config.Setting;
import appeng.api.config.Settings;
import appeng.api.config.YesNo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Settings.class)
public class SettingsMixin {

    @SafeVarargs
    @Shadow(remap = false)
    private static <T extends Enum<T>> Setting<T> register(String name, T firstOption, T... moreOptions) {
        return null;
    }

    @Inject(method = "<clinit>", at = @At("TAIL"), remap = false)
    private static void init(CallbackInfo ci) {
        GTOSettings.ENHANCED_BLOCKING_MODE = register("enhanced_blocking_mode", YesNo.YES, YesNo.NO);
    }
}
