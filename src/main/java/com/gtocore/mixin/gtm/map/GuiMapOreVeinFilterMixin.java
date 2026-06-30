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

    // A fresh GuiMap instance means the map was just opened: start with the panel closed. Guarded so a
    // re-init from resize / layer toggle preserves the open state.
    @Inject(method = "init", at = @At("TAIL"), remap = true)
    private void gtocore$resetOnOpen(CallbackInfo ci) {
        if (!gtocore$opened) {
            gtocore$opened = true;
            OreVeinFilterPanel.OPEN = false;
        }
    }

    @Inject(method = "render", at = @At("TAIL"), remap = true)
    private void gtocore$render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        if (!gtocore$anchor()) {
            OreVeinFilterPanel.close();
            return;
        }
        OreVeinFilterPanel.renderButton(graphics, gtocore$btnX, gtocore$btnY, mouseX, mouseY);
        if (OreVeinFilterPanel.OPEN) {
            OreVeinFilterPanel.render(graphics, this.font, this.width, this.height, mouseX, mouseY, gtocore$mapDim());
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
