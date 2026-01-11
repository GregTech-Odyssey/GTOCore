package com.gtocore.mixin.eae;

import com.gtocore.integration.jech.PinYinUtils;

import com.gtolib.api.ae2.gui.hooks.IExtendedGuiEx;

import com.glodblock.github.extendedae.client.gui.GuiExPatternTerminal;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.*;

@Mixin(GuiExPatternTerminal.class)
public abstract class GuiExPatternTerminalMixinWithJech implements IExtendedGuiEx {

    // redirector fails to match String.contains in some environments
    @Redirect(method = "refreshList",
              at = @At(
                       value = "INVOKE",
                       target = "Lme/towdium/jecharacters/utils/Match;contains(Ljava/lang/String;Ljava/lang/CharSequence;)Z"),
              remap = false)
    private boolean searchFunctionRedirecting(String candidate, CharSequence search) {
        if (gto$getSearchProviderField() != null) {
            return PinYinUtils.match(candidate, gto$getSearchProviderField().getValue().toLowerCase()) && PinYinUtils.match(candidate, search);
        }
        return PinYinUtils.match(candidate, search);
    }
}
