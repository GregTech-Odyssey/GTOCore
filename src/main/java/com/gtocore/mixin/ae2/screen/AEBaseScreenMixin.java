package com.gtocore.mixin.ae2.screen;

import com.gtocore.client.renderer.RenderUtil;

import com.gtolib.api.ae2.gui.hooks.IAEBaseScreenLifecycle;
import com.gtolib.api.ae2.gui.hooks.IconSlot;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.inventory.Slot;

import appeng.client.gui.AEBaseScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AEBaseScreen.class)
public class AEBaseScreenMixin {

    @Inject(method = "init", at = @At("TAIL"))
    private void gtolib$onInitAfterWidgets(CallbackInfo ci) {
        if (this instanceof IAEBaseScreenLifecycle lifecycle) {
            lifecycle.gtolib$initAfterWidgetsInitialized();
        }
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lappeng/client/gui/AEBaseScreen;positionSlots()V", shift = At.Shift.AFTER, remap = false))
    private void gtolib$onInitAfterPositionSlot(CallbackInfo ci) {
        if (this instanceof IAEBaseScreenLifecycle lifecycle) {
            lifecycle.gtolib$initBeforeWidgetsInitialized();
        }
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lappeng/client/gui/AEBaseScreen;positionSlots()V", remap = false))
    private void gtolib$onInitBeforePositionSlot(CallbackInfo ci) {
        if (this instanceof IAEBaseScreenLifecycle lifecycle) {
            lifecycle.gtolib$initBeforePositionSlot();
        }
    }

    @Inject(method = "renderSlot", at = @At("HEAD"), cancellable = true)
    private void gtolib$onRenderSlot(GuiGraphics guiGraphics, Slot s, CallbackInfo ci) {
        if (s instanceof IconSlot iconSlot) {
            if (iconSlot.getIcon() != null) {
                iconSlot.getIcon().getBlitter()
                        .dest(s.x, s.y)
                        .opacity(iconSlot.getOpacityOfIcon())
                        .blit(guiGraphics);
                ci.cancel();
            }
        }
    }

    @Inject(method = "fillRect", at = @At("HEAD"), remap = false, cancellable = true)
    private void gtolib$fillRect(GuiGraphics guiGraphics, Rect2i rect, int color, CallbackInfo ci) {
        if (color == 0x8A00FF00) {
            RenderUtil.drawRainbowBorder(guiGraphics, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), 300, 1.0f);
            ci.cancel();
        }
    }
}
