package com.gtocore.mixin.ae2.gui;

import com.gtocore.integration.ae.hooks.IExtendedPatternEncodingTerm;
import com.gtocore.integration.ae.hooks.IMouseNoRedirection;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import appeng.client.gui.widgets.ActionButton;
import appeng.client.gui.widgets.IconButton;
import appeng.core.localization.ButtonToolTips;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ActionButton.class)
public abstract class ActionButtonMixin extends IconButton implements IMouseNoRedirection {

    @Unique
    boolean gtocore$useOtherButton = false;

    public ActionButtonMixin(OnPress onPress) {
        super(onPress);
    }

    @Inject(method = "buildMessage", at = @At("RETURN"), remap = false, cancellable = true)
    private void initHook(ButtonToolTips displayName, ButtonToolTips displayValue, CallbackInfoReturnable<Component> cir) {
        if (displayValue == ButtonToolTips.EncodeDescription) {
            gtocore$useOtherButton = true;
            MutableComponent component = (MutableComponent) cir.getReturnValue();
            component.append("\n")
                    .append(Component.translatable("gtocore.gui.encoding_desc"))
                    .append("\n")
                    .append(Component.translatable("gtocore.ae.appeng.craft.encode_send"));
            cir.setReturnValue(component);
        }
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return button == 0 || button == 1;
    }

    @Override
    public boolean gtocore$shouldRedirectMouse() {
        return !gtocore$useOtherButton;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        var containerScreen = Minecraft.getInstance().screen;
        if (containerScreen instanceof IExtendedPatternEncodingTerm screen &&
                screen.gto$getEncodeButton() == (Object) this &&
                button == 1 && this.isMouseOver(mouseX, mouseY)) {
            screen.gto$getMenu().gtolib$sendEncodeRequest();
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
