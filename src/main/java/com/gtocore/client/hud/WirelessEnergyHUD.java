package com.gtocore.client.hud;

import com.gtocore.api.gui.helper.LineChartHelper;
import com.gtocore.config.GTOConfig;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.api.player.PlayerData;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import appeng.client.gui.Rects;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.math.BigInteger;

import static com.hepdd.gtmthings.utils.FormatUtil.formatBigIntegerNumberOrSic;

@OnlyIn(Dist.CLIENT)
@DataGeneratorScanned
public class WirelessEnergyHUD implements IMoveableHUD {

    public static final WirelessEnergyHUD INSTANCE = new WirelessEnergyHUD();

    @RegisterLanguage(en = "Wireless Energy HUD", cn = "无线能量 HUD")
    public static final String DISPLAY_NAME = "gtocore.hud.wireless_energy.name";

    @RegisterLanguage(en = "%s / %s EU (%d%%)", cn = "%s / %s EU (%d%%)")
    public static final String FORMAT_WIRELESS_ENERGY_HUD = "hud.gtocore.wireless_energy";

    private final static int width = 80;
    private final static int height = 40;

    private int realWidth = width;
    private int realHeight = height;

    private boolean isDragging = false;
    private int dragStartX = 0;
    private int dragStartY = 0;
    private int pendingMovedX = 0;
    private int pendingMovedY = 0;

    /// renderInContainerScreen
    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int i, int i1, float v) {
        Minecraft mc = Minecraft.getInstance();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(pendingMovedX, pendingMovedY, 1500);
        renderGeneral(guiGraphics, v,
                screenWidth,
                screenHeight);
        guiGraphics.pose().popPose();
    }

    @Override
    public void renderGeneral(GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        PlayerData playerData = getPlayerData(mc);
        if (playerData == null) return;
        if (playerData.electricityCapacityCache.compareTo(BigInteger.ZERO) <= 0) {
            return;
        }

        Component label = createLabel(playerData);
        updateDimensions(font, label);
        Rect2i bounds = getBounds(screenWidth, screenHeight);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(bounds.getX(), bounds.getY(), 0.0);

        // 调用你的 LineChartHelper 来绘制图表
        LineChartHelper.INSTANCE.builder(guiGraphics, playerData.getClientElectricityHistoryCache())
                .width(width)
                .height(height)
                .backgroundColor(0x8a404040)
                .borderColor(0x8a000000)
                .lineColor(0xbb000000 | Color.decode(GTOConfig.INSTANCE.client.hud.wirelessEnergyHUDLineColor).getRGB())
                .drawAreaFill(true)
                .areaFillColor(0x402ECC71)
                .drawAreaFill(false)
                .autoReboundY(false)
                .yBound(0, 100)
                .draw();
        // 绘制文本标签
        guiGraphics.drawString(font, label, 2, height + 2, 0xFFFFFF, false);

        guiGraphics.pose().popPose();
    }

    @Override
    public Rect2i getBounds(int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        PlayerData playerData = getPlayerData(mc);
        if (playerData == null) return Rects.ZERO;
        if (playerData.electricityCapacityCache.compareTo(BigInteger.ZERO) <= 0) {
            return Rects.ZERO;
        }
        updateDimensions(Minecraft.getInstance().font, getPreviewLabel());
        int absX = (int) (GTOConfig.INSTANCE.client.hud.wirelessEnergyHUDDefaultX / 100d * (screenWidth - realWidth));
        int absY = (int) (GTOConfig.INSTANCE.client.hud.wirelessEnergyHUDDefaultY / 100d * (screenHeight - realHeight));
        return new Rect2i(absX, absY, realWidth, realHeight);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable(DISPLAY_NAME);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            isDragging = true;
            dragStartX = (int) mouseX;
            dragStartY = (int) mouseY;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isDragging) {
            pendingMovedX = (int) (mouseX - dragStartX);
            pendingMovedY = (int) (mouseY - dragStartY);
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        boolean moved = pendingMovedX != 0 || pendingMovedY != 0;
        boolean handled = isDragging || moved;
        if (moved) {
            Minecraft mc = Minecraft.getInstance();
            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();

            Rect2i bounds = getBounds(screenWidth, screenHeight);
            setTopLeftPosition(bounds.getX() + pendingMovedX, bounds.getY() + pendingMovedY, screenWidth, screenHeight);
        }
        pendingMovedX = 0;
        pendingMovedY = 0;
        isDragging = false;
        return handled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        GTOConfig.set("wirelessEnergyHUDEnabled", enabled, "client", "hud");
    }

    @Override
    public void setTopLeftPosition(int x, int y, int screenWidth, int screenHeight) {
        updateDimensions(Minecraft.getInstance().font, getPreviewLabel());
        int maxX = Math.max(0, screenWidth - realWidth);
        int maxY = Math.max(0, screenHeight - realHeight);
        int clampedX = Mth.clamp(x, 0, maxX);
        int clampedY = Mth.clamp(y, 0, maxY);
        int widthRange = Math.max(1, screenWidth - realWidth);
        int heightRange = Math.max(1, screenHeight - realHeight);
        GTOConfig.set("wirelessEnergyHUDDefaultX", (int) Mth.clamp((clampedX * 100d) / widthRange, 0, 100), "client", "hud");
        GTOConfig.set("wirelessEnergyHUDDefaultY", (int) Mth.clamp((clampedY * 100d) / heightRange, 0, 100), "client", "hud");
        pendingMovedX = 0;
        pendingMovedY = 0;
        isDragging = false;
    }

    @Override
    public boolean isEnabled() {
        return GTOConfig.INSTANCE.client.hud.wirelessEnergyHUDEnabled;
    }

    @Override
    public boolean isPositionDragging() {
        return isDragging || pendingMovedX != 0 || pendingMovedY != 0;
    }

    @Override
    public Rect2i getPropertyAnchorBounds(int screenWidth, int screenHeight) {
        Rect2i bounds = getBounds(screenWidth, screenHeight);
        return new Rect2i(bounds.getX() + pendingMovedX, bounds.getY() + pendingMovedY,
                bounds.getWidth(), bounds.getHeight());
    }

    private PlayerData getPlayerData(Minecraft mc) {
        var player = IEnhancedPlayer.of(mc.player);
        return player == null ? null : player.getPlayerData();
    }

    private Component createLabel(PlayerData playerData) {
        return Component.translatable(FORMAT_WIRELESS_ENERGY_HUD,
                Component.literal(formatBigIntegerNumberOrSic(playerData.electricityStorageCache)).withStyle(ChatFormatting.GOLD),
                Component.literal(formatBigIntegerNumberOrSic(playerData.electricityCapacityCache)).withStyle(ChatFormatting.GOLD),
                playerData.electricityStorageCache.multiply(BigInteger.valueOf(100)).divide(playerData.electricityCapacityCache).intValue());
    }

    private Component getPreviewLabel() {
        PlayerData playerData = getPlayerData(Minecraft.getInstance());
        if (playerData != null && playerData.electricityCapacityCache.compareTo(BigInteger.ZERO) > 0) {
            return createLabel(playerData);
        }
        return Component.translatable(DISPLAY_NAME);
    }

    private void updateDimensions(Font font, Component label) {
        realWidth = Math.max(width, font.width(label) + 4);
        realHeight = height + font.lineHeight + 2;
    }
}
