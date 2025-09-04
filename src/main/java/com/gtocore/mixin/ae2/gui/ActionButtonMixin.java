package com.gtocore.mixin.ae2.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import appeng.client.gui.widgets.ActionButton;
import appeng.core.localization.ButtonToolTips;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ActionButton.class)
public class ActionButtonMixin {

    @Inject(method = "buildMessage", at = @At("RETURN"), remap = false, cancellable = true)
    private void initHook(ButtonToolTips displayName, ButtonToolTips displayValue, CallbackInfoReturnable<Component> cir) {
        if (displayValue == ButtonToolTips.EncodeDescription) {
            MutableComponent component = (MutableComponent) cir.getReturnValue();
            component.append("\n").append(Component.translatable("gtocore.gui.encoding_desc"));
            cir.setReturnValue(component);
        }
    }
}
