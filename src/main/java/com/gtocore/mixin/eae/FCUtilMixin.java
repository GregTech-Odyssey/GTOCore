package com.gtocore.mixin.eae;

import com.gtocore.integration.jech.PinYinUtils;

import com.glodblock.github.extendedae.util.FCUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FCUtil.class)
public class FCUtilMixin {

    @Redirect(method = "compareTokens", at = @At(value = "INVOKE", target = "Ljava/lang/String;contains(Ljava/lang/CharSequence;)Z"), remap = false)
    private static boolean redirectContains(String str, CharSequence s) {
        return PinYinUtils.match(str, s);
    }
}
