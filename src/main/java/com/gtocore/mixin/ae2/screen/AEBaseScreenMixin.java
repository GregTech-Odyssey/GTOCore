package com.gtocore.mixin.ae2.screen;

import com.gtocore.client.forge.DebugScreenInspector;
import com.gtocore.client.renderer.RenderUtil;
import com.gtocore.config.GTOConfig;
import com.gtocore.integration.ae.wtlib.WFTMenu;

import com.gtolib.api.ae2.gui.hooks.IWUTScreen;
import com.gtolib.api.ae2.wtlib.CycleTerminalButton;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.widgets.VerticalButtonBar;
import appeng.menu.AEBaseMenu;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AEBaseScreen.class)
public abstract class AEBaseScreenMixin<T extends AEBaseMenu> extends AbstractContainerScreen<T> implements IWUTScreen {

    @Shadow(remap = false)
    @Final
    private VerticalButtonBar verticalToolbar;

    @Shadow(remap = false)
    public abstract List<Rect2i> getExclusionZones();

    @Unique
    private CycleTerminalButton gtocore$cycleTerminalButton = null;

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

    @Override
    public CycleTerminalButton gto$getCycleTerminalButton() {
        if (gtocore$cycleTerminalButton != null) return gtocore$cycleTerminalButton;
        return gtocore$cycleTerminalButton = (CycleTerminalButton) verticalToolbar.buttons.stream().filter(button -> button instanceof CycleTerminalButton).findFirst().orElse(null);
    }

    @Inject(method = "addToLeftToolbar", at = @At("HEAD"), remap = false, cancellable = true)
    private <B extends Button> void gtolib$addToLeftToolbar(B button, CallbackInfoReturnable<B> cir) {
        if (button instanceof de.mari_023.ae2wtlib.wut.CycleTerminalButton && GTOConfig.INSTANCE.client.aeTerminalPageSwitchStyleSelector) {
            CycleTerminalButton b;
            verticalToolbar.add(b = new CycleTerminalButton(CycleTerminalButton.LayoutDirection.LEFT));
            if ((Object) this instanceof WFTMenu.WFTScreen fs) fs.filterModeGroup.add(b);
            cir.setReturnValue(button);
        }
    }

    @Inject(method = "renderLabels", at = @At(value = "INVOKE", target = "Lappeng/client/gui/AEBaseScreen;drawFG(Lnet/minecraft/client/gui/GuiGraphics;IIII)V", remap = false))
    private void gtolib$debugRenderExclusionZones(GuiGraphics guiGraphics, int x, int y, CallbackInfo ci) {
        if (!DebugScreenInspector.isVisible()) return;
        for (Rect2i rect : getExclusionZones()) {
            RenderUtil.drawRainbowBorder(guiGraphics, rect.getX() - leftPos, rect.getY() - topPos, rect.getWidth(), rect.getHeight(), 300, 1.0f);
        }
    }
}
