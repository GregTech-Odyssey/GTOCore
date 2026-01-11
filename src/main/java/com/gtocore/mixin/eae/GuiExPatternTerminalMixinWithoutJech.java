package com.gtocore.mixin.eae;

import com.gtolib.api.ae2.gui.hooks.IExtendedGuiEx;

import com.glodblock.github.extendedae.client.gui.GuiExPatternTerminal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiExPatternTerminal.class)
public abstract class GuiExPatternTerminalMixinWithoutJech implements IExtendedGuiEx {

    // redirector fails to match String.contains in some environments
    @Redirect(method = "refreshList",
              at = @At(
                       value = "INVOKE",
                       target = "Ljava/lang/String;contains(Ljava/lang/CharSequence;)Z"),
              remap = false)
    private boolean searchFunctionRedirecting(String instance, CharSequence s) {
        if (gto$getSearchProviderField() != null) {
            return instance.contains(gto$getSearchProviderField().getValue().toLowerCase()) && instance.contains(s);
        }
        return instance.contains(s);
    }
}
