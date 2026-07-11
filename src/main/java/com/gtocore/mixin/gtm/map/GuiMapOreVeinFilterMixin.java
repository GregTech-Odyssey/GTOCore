package com.gtocore.mixin.gtm.map;

import com.gtocore.client.OreVeinFilterPanel;

import com.gregtechceu.gtceu.integration.map.xaeros.worldmap.gui.GuiTexturedButtonWithSize;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.map.MapProcessor;
import xaero.map.gui.GuiMap;
import xaero.map.gui.IRightClickableElement;
import xaero.map.gui.ScreenBase;

/**
 * Adds the ore-vein filter control to Xaero's world map: a small button drawn just above GregTech's
 * ore-vein layer toggle, plus the overlay panel and its mouse/keyboard handling. The actual vein
 * hiding happens in {@link OreVeinElementRendererMixin}; this only drives the on-map UI. The button
 * is drawn and click-tested directly (no widget registration) so the wiring stays minimal.
 */
@Mixin(value = GuiMap.class, remap = false)
public abstract class GuiMapOreVeinFilterMixin extends ScreenBase implements IRightClickableElement {

    @Shadow
    private ResourceKey<Level> lastViewedDimensionId;

    @Shadow
    private ResourceKey<Level> lastNonNullViewedDimensionId;

    @Unique
    private boolean gtocore$opened;
    @Unique
    private int gtocore$btnX = -1;
    @Unique
    private int gtocore$btnY = -1;

    protected GuiMapOreVeinFilterMixin(Screen parent, Screen escape, MapProcessor mapProcessor, Entity player) {
        super(parent, escape, Component.translatable("gui.xaero_world_map_screen"));
    }

    // The Level key (e.g. minecraft:overworld, gtceu:...) of the dimension the map is currently showing;
    // matches the keys stored in GTOreDefinition.dimensionFilter().
    @Unique
    private ResourceKey<Level> gtocore$mapDim() {
        return lastViewedDimensionId != null ? lastViewedDimensionId : lastNonNullViewedDimensionId;
    }

    // Real cursor position in GUI-scaled coords (same space as render's mouseX/mouseY). Read straight
    // from the mouse handler so the panel still tracks the true cursor even when we feed the map body
    // off-screen coords to suppress its hover.
    @Unique
    private double gtocore$mouseX() {
        return this.minecraft.mouseHandler.xpos() * this.minecraft.getWindow().getGuiScaledWidth() / this.minecraft.getWindow().getScreenWidth();
    }

    @Unique
    private double gtocore$mouseY() {
        return this.minecraft.mouseHandler.ypos() * this.minecraft.getWindow().getGuiScaledHeight() / this.minecraft.getWindow().getScreenHeight();
    }

    // Locate the button position just above the top-most GregTech layer toggle. Stores it for click
    // testing and returns false (hiding the control) when the map integration adds no layer buttons.
    @Unique
    private boolean gtocore$anchor() {
        int ax = -1;
        int ay = Integer.MAX_VALUE;
        for (GuiEventListener child : this.children()) {
            if (child instanceof GuiTexturedButtonWithSize button && button.getY() < ay) {
                ay = button.getY();
                ax = button.getX();
            }
        }
        if (ax < 0) {
            gtocore$btnX = -1;
            gtocore$btnY = -1;
            return false;
        }
        gtocore$btnX = ax;
        gtocore$btnY = ay - 20;
        return true;
    }

    // A fresh GuiMap instance means the map was just opened: restore the panel's saved open/closed
    // state so an open panel re-appears. Guarded so a re-init from resize / layer toggle keeps the
    // live state instead of snapping back.
    @Inject(method = "init", at = @At("TAIL"), remap = true)
    private void gtocore$resetOnOpen(CallbackInfo ci) {
        if (!gtocore$opened) {
            gtocore$opened = true;
            OreVeinFilterPanel.syncOpenFromConfig();
        }
    }

    // While the cursor is over the open panel, hand the map body off-screen coords so its markers /
    // objects don't highlight or show tooltips through the panel. The panel itself uses the real
    // cursor (gtocore$mouseX/Y), so its own hover keeps working.
    @ModifyVariable(method = "render", at = @At("HEAD"), index = 2, argsOnly = true, remap = true)
    private int gtocore$blockMouseX(int mouseX) {
        return OreVeinFilterPanel.blocksMouse(gtocore$mouseX(), gtocore$mouseY(), this.width, this.height) ? -9999 : mouseX;
    }

    @ModifyVariable(method = "render", at = @At("HEAD"), index = 3, argsOnly = true, remap = true)
    private int gtocore$blockMouseY(int mouseY) {
        return OreVeinFilterPanel.blocksMouse(gtocore$mouseX(), gtocore$mouseY(), this.width, this.height) ? -9999 : mouseY;
    }

    @Inject(method = "render", at = @At("TAIL"), remap = true)
    private void gtocore$render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        // mouseX/mouseY args may have been zeroed to -9999 above; use the real cursor for our own UI.
        int rmx = (int) gtocore$mouseX();
        int rmy = (int) gtocore$mouseY();
        // The toggle button rides on GregTech's layer control; when that is momentarily gone (re-init,
        // dimension switch) just skip drawing the button. The panel stays open regardless -- once
        // opened it is persistent until the user closes it with the X.
        if (gtocore$anchor()) {
            OreVeinFilterPanel.renderButton(graphics, gtocore$btnX, gtocore$btnY, rmx, rmy);
        }
        if (OreVeinFilterPanel.OPEN) {
            OreVeinFilterPanel.render(graphics, this.font, this.width, this.height, rmx, rmy, gtocore$mapDim());
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true, remap = true)
    private void gtocore$panelClick(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (button == 0 && gtocore$btnX >= 0 && OreVeinFilterPanel.inButton(mouseX, mouseY, gtocore$btnX, gtocore$btnY)) {
            OreVeinFilterPanel.toggleOpen();
            cir.setReturnValue(true);
            return;
        }
        if (OreVeinFilterPanel.OPEN && OreVeinFilterPanel.mouseClicked(mouseX, mouseY, button, this.width, this.height, gtocore$mapDim())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"), cancellable = true, remap = true)
    private void gtocore$panelRelease(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (OreVeinFilterPanel.mouseReleased()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "mouseScrolled", at = @At("HEAD"), cancellable = true, remap = true)
    private void gtocore$panelScroll(double mouseX, double mouseY, double delta, CallbackInfoReturnable<Boolean> cir) {
        if (OreVeinFilterPanel.OPEN && OreVeinFilterPanel.mouseScrolled(mouseX, mouseY, delta, this.width, this.height, gtocore$mapDim())) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true, remap = true)
    private void gtocore$panelKey(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (OreVeinFilterPanel.OPEN && OreVeinFilterPanel.keyPressed(keyCode)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true, remap = true)
    private void gtocore$panelChar(char chr, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (OreVeinFilterPanel.OPEN && OreVeinFilterPanel.charTyped(chr)) {
            cir.setReturnValue(true);
        }
    }
}
