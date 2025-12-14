package com.gtocore.client.hud;

import com.gtocore.api.gui.helper.LineChartHelper;
import com.gtocore.config.GTOConfig;

import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.api.annotation.language.RegisterLanguage;
import com.gtolib.api.player.IEnhancedPlayer;
import com.gtolib.api.player.PlayerData;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.math.BigInteger;

import static com.hepdd.gtmthings.utils.FormatUtil.formatBigIntegerNumberOrSic;

@OnlyIn(Dist.CLIENT)
@DataGeneratorScanned
public class WirelessEnergyHUD implements IGuiOverlay {

    @RegisterLanguage(en = "%s / %s EU (%d%%)", cn = "%s / %s EU (%d%%)")
    public static final String FORMAT_WIRELESS_ENERGY_HUD = "hud.gtocore.wireless_energy";

    private final static int width = 80;
    private final static int height = 40;

    @Override
    public void render(ForgeGui forgeGui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        if (!GTOConfig.INSTANCE.hud.wirelessEnergyHUDEnabled || mc.level == null || mc.options.renderDebug || mc.options.hideGui) {
            return;
        }
        PlayerData playerData;
        if (IEnhancedPlayer.of(mc.player) != null) {
            playerData = IEnhancedPlayer.of(mc.player).getPlayerData();
        } else {
            return;
        }
        if (playerData.electricityCapacityCache.compareTo(BigInteger.ZERO) <= 0) {
            return;
        }

        Component label = Component.translatable(FORMAT_WIRELESS_ENERGY_HUD,
                Component.literal(formatBigIntegerNumberOrSic(playerData.electricityStorageCache)).withStyle(ChatFormatting.GOLD),
                Component.literal(formatBigIntegerNumberOrSic(playerData.electricityCapacityCache)).withStyle(ChatFormatting.GOLD),
                playerData.electricityStorageCache.multiply(BigInteger.valueOf(100)).divide(playerData.electricityCapacityCache).intValue());

        int realWidth = Math.max(width, forgeGui.getFont().width(label) + 4);
        int realHeight = height + forgeGui.getFont().lineHeight + 2;
        int absX = (int) (GTOConfig.INSTANCE.hud.wirelessEnergyHUDDefaultX / 100d * (screenWidth - realWidth));
        int absY = (int) (GTOConfig.INSTANCE.hud.wirelessEnergyHUDDefaultY / 100d * (screenHeight - realHeight));

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(absX, absY, 0.0);

        // 调用你的 LineChartHelper 来绘制图表
        LineChartHelper.INSTANCE.builder(guiGraphics, playerData.getClientElectricityHistoryCache())
                .width(width)
                .height(height)
                .backgroundColor(0x8a404040)
                .borderColor(0x8a000000)
                .lineColor(0xbbECEC71)
                .drawAreaFill(true)
                .areaFillColor(0x402ECC71)
                .drawAreaFill(false)
                .autoReboundY(false)
                .yBound(0, 100)
                .draw();
        // 绘制文本标签
        guiGraphics.drawString(forgeGui.getFont(), label, 2, height + 2, 0xFFFFFF, false);

        guiGraphics.pose().popPose();
    }

    public WirelessEnergyHUD() {}
}
