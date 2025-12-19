package com.gtocore.client.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraftforge.client.gui.overlay.ForgeGui;

import com.teamresourceful.resourcefulconfig.common.config.impl.ConfigParser;
import earth.terrarium.adastra.client.config.AdAstraConfigClient;
import earth.terrarium.adastra.client.screens.player.OverlayScreen;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class AdAstraHUD implements IMoveableHUD {

    private boolean gto$containerScreenEnv = false;
    private boolean gto$hudEnv = false;
    private boolean gto$dragging = false;
    private int gto$dragStartX = 0;
    private int gto$dragStartY = 0;
    private int gto$pendingMovedX = 0;
    private int gto$pendingMovedY = 0;
    public static final AdAstraHUD gto$INSTANCE = new AdAstraHUD();

    /// renderInContainerScreen

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int i, int i1, float v) {
        try {
            gto$containerScreenEnv = true;
            renderGeneral(guiGraphics, v,
                    Minecraft.getInstance().getWindow().getGuiScaledWidth(),
                    Minecraft.getInstance().getWindow().getGuiScaledHeight());
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
        return AdAstraConfigClient.oxygenBarX > 0;
    }

    @Override
    public void toggleEnabled() {
        if (isEnabled()) {
            set(-1000, -1000);
        } else {
            set(10, 10);
        }
    }

    @Override
    public Rect2i getBounds(int screenWidth, int screenHeight) {
        int x = AdAstraConfigClient.oxygenBarX;
        int y = AdAstraConfigClient.oxygenBarY;
        float scale = AdAstraConfigClient.oxygenBarScale;
        int width = (int) (62 * scale);
        int height = (int) (52 * scale);
        return new Rect2i(x, y, width, height);
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
        if (gto$pendingMovedX != 0 || gto$pendingMovedY != 0) {

            set(
                    (int) Math.max(0, Math.min(Minecraft.getInstance().getWindow().getGuiScaledWidth() - (62 * AdAstraConfigClient.oxygenBarScale),
                            AdAstraConfigClient.oxygenBarX + gto$pendingMovedX)),
                    (int) Math.max(0, Math.min(Minecraft.getInstance().getWindow().getGuiScaledHeight() - (52 * AdAstraConfigClient.oxygenBarScale),
                            AdAstraConfigClient.oxygenBarY + gto$pendingMovedY)));

            gto$pendingMovedX = 0;
            gto$pendingMovedY = 0;
            gto$dragging = false;
            return true;
        }
        return false;
    }

    private static void set(int oxygenBarX, int oxygenBarY) {
        AdAstraConfigClient.oxygenBarX = oxygenBarX;
        AdAstraConfigClient.oxygenBarY = oxygenBarY;
        try {
            ConfigParser.parseConfig(AdAstraConfigClient.class).save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
