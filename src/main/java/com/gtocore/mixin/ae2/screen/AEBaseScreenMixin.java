package com.gtocore.mixin.ae2.screen;

import com.gtocore.client.renderer.RenderUtil;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.client.gui.AEBaseScreen;
import appeng.menu.AEBaseMenu;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AEBaseScreen.class)
public abstract class AEBaseScreenMixin<T extends AEBaseMenu> extends AbstractContainerScreen<T> {

    public AEBaseScreenMixin(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Inject(method = "fillRect", at = @At("HEAD"), remap = false, cancellable = true)
    private void gtolib$fillRect(GuiGraphics guiGraphics, Rect2i rect, int color, CallbackInfo ci) {
        if (color == 0x8A00FF00) {
            RenderUtil.drawRainbowBorder(guiGraphics, rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), 300, 1.0f);
            ci.cancel();
        }
    }
}
