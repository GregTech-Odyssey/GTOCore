package com.gtocore.client.hud;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import appeng.client.gui.Rects;

import com.teamresourceful.resourcefulconfig.common.config.impl.ConfigParser;
import earth.terrarium.adastra.client.config.AdAstraConfigClient;
import earth.terrarium.adastra.client.screens.player.OverlayScreen;
import earth.terrarium.adastra.common.items.armor.JetSuitItem;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@DataGeneratorScanned
public class AdAstraHUD implements IMoveableHUD {

    @RegisterLanguage(en = "Ad Astra Oxygen HUD", cn = "Ad Astra 氧气 HUD")
    public static final String DISPLAY_NAME = "gtocore.hud.adastra.name";

    private boolean gto$containerScreenEnv = false;
    private boolean gto$hudEnv = false;
    private boolean gto$dragging = false;
    private int gto$dragStartX = 0;
    private int gto$dragStartY = 0;
    private int gto$pendingMovedX = 0;
    private int gto$pendingMovedY = 0;
    private int gto$lastVisibleX = Math.max(10, AdAstraConfigClient.oxygenBarX);
    private int gto$lastVisibleY = Math.max(10, AdAstraConfigClient.oxygenBarY);
    public static final AdAstraHUD gto$INSTANCE = new AdAstraHUD();

    /// renderInContainerScreen

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int i, int i1, float v) {
        Minecraft mc = Minecraft.getInstance();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        try {
            gto$containerScreenEnv = true;
            renderGeneral(guiGraphics, v,
                    screenWidth,
                    screenHeight);
        } finally {
            gto$containerScreenEnv = false;
        }
    }

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        try {
            gto$hudEnv = true;
            renderGeneral(guiGraphics, partialTick, screenWidth, screenHeight);
        } finally {
            gto$hudEnv = false;
        }
    }

    @Override
    public void renderGeneral(GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        OverlayScreen.render(guiGraphics, partialTick);
    }

    @Override
    public boolean isEnabled() {
        return AdAstraConfigClient.oxygenBarX >= 0 && AdAstraConfigClient.oxygenBarY >= 0;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(DISPLAY_NAME);
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            setTopLeftPosition(gto$lastVisibleX, gto$lastVisibleY,
                    Minecraft.getInstance().getWindow().getGuiScaledWidth(),
                    Minecraft.getInstance().getWindow().getGuiScaledHeight());
            return;
        }
        if (isEnabled()) {
            gto$lastVisibleX = AdAstraConfigClient.oxygenBarX;
            gto$lastVisibleY = AdAstraConfigClient.oxygenBarY;
        }
        gto$pendingMovedX = 0;
        gto$pendingMovedY = 0;
        gto$dragging = false;
        set(-1000, -1000);
    }

    @Override
    public Rect2i getBounds(int screenWidth, int screenHeight) {
        if (Minecraft.getInstance().player != null && !JetSuitItem.hasFullSet(Minecraft.getInstance().player)) {
            return Rects.ZERO;
        }
        int x = AdAstraConfigClient.oxygenBarX;
        int y = AdAstraConfigClient.oxygenBarY;
        return new Rect2i(x, y, getHudWidth(), getHudHeight());
    }

    @Override
    public void setTopLeftPosition(int x, int y, int screenWidth, int screenHeight) {
        int maxX = Math.max(0, screenWidth - getHudWidth());
        int maxY = Math.max(0, screenHeight - getHudHeight());
        int clampedX = Math.clamp(x, 0, maxX);
        int clampedY = Math.clamp(y, 0, maxY);
        gto$lastVisibleX = clampedX;
        gto$lastVisibleY = clampedY;
        gto$pendingMovedX = 0;
        gto$pendingMovedY = 0;
        gto$dragging = false;
        set(clampedX, clampedY);
    }

    @Override
    public Rect2i getPropertyAnchorBounds(int screenWidth, int screenHeight) {
        Rect2i bounds = getBounds(screenWidth, screenHeight);
        return new Rect2i(bounds.getX() + gto$pendingMovedX, bounds.getY() + gto$pendingMovedY,
                bounds.getWidth(), bounds.getHeight());
    }

    @Override
    public boolean isPositionDragging() {
        return gto$dragging || gto$pendingMovedX != 0 || gto$pendingMovedY != 0;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            gto$dragging = true;
            gto$dragStartX = (int) mouseX;
            gto$dragStartY = (int) mouseY;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (gto$dragging) {
            gto$pendingMovedX = (int) (mouseX - gto$dragStartX);
            gto$pendingMovedY = (int) (mouseY - gto$dragStartY);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean moved = gto$pendingMovedX != 0 || gto$pendingMovedY != 0;
        boolean handled = gto$dragging || moved;
        if (moved) {
            setTopLeftPosition(
                    AdAstraConfigClient.oxygenBarX + gto$pendingMovedX,
                    AdAstraConfigClient.oxygenBarY + gto$pendingMovedY,
                    Minecraft.getInstance().getWindow().getGuiScaledWidth(),
                    Minecraft.getInstance().getWindow().getGuiScaledHeight());
        }
        gto$pendingMovedX = 0;
        gto$pendingMovedY = 0;
        gto$dragging = false;
        return handled;
    }

    private int getHudWidth() {
        return (int) (62 * AdAstraConfigClient.oxygenBarScale);
    }

    private int getHudHeight() {
        return (int) (52 * AdAstraConfigClient.oxygenBarScale);
    }

    private static void set(int oxygenBarX, int oxygenBarY) {
        AdAstraConfigClient.oxygenBarX = oxygenBarX;
        AdAstraConfigClient.oxygenBarY = oxygenBarY;
        try {
            ConfigParser.parseConfig(AdAstraConfigClient.class).save();
        } catch (Exception e) {
            GTOCore.LOGGER.error("Failed to save Ad Astra HUD config", e);
        }
    }
}
