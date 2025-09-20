package com.gtocore.mixin.ae2.gui;

import net.minecraft.client.gui.Font;

import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.ConfirmableTextField;
import com.almostreliable.merequester.client.widgets.NumberField;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(NumberField.class)
public abstract class MERequesterNumberFieldMixin extends ConfirmableTextField {

    public MERequesterNumberFieldMixin(ScreenStyle style, Font fontRenderer, int x, int y, int width, int height) {
        super(style, fontRenderer, x, y, width, height);
    }

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void gto$init(int x, int y, String name, ScreenStyle style, Consumer<Long> onConfirm, CallbackInfo ci) {
        setMaxLength(18);
    }
}
