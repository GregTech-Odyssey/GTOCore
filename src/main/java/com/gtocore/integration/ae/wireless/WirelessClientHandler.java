package com.gtocore.integration.ae.wireless;

import com.gtocore.client.renderer.RenderHelper;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.item.MEWirelessMachineConfigurator;
import com.gtocore.common.saved.WirelessNetworkSavedData;

import com.gregtechceu.gtceu.api.item.MetaMachineItem;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.vertex.PoseStack;

import java.awt.*;
import java.util.List;
import java.util.Objects;

import static com.gtocore.common.data.GTOMachines.ME_WIRELESS_CONNECTION_MACHINE;
import static com.hepdd.gtmthings.data.CustomMachines.ME_EXPORT_BUFFER;

@OnlyIn(Dist.CLIENT)
public class WirelessClientHandler {

    public static void highlightMachines(Camera camera, PoseStack poseStack, MultiBufferSource bufferSource) {
        var player = Minecraft.getInstance().player;
        if (player == null) return;
        List<WirelessNetwork> grids = WirelessNetworkSavedData.getCLIENT_INSTANCE().getNetworkPool();
        for (WirelessNetwork grid : grids) {
            var gridName = grid.getNickname();
            var heldItemId = MEWirelessMachineConfigurator.getConfiguringNetworkId(player);
            boolean isDefault = !heldItemId.isEmpty() ?
                    heldItemId.equals(grid.getId()) :
                    Objects.equals(WirelessNetworkSavedData.getCLIENT_INSTANCE().getDefaultMap().get(player.getUUID()), grid.getId());
            float lineWidth = 4;
            Color color = getGridColor(grid, isDefault);
            for (var machine : grid.getNodeInfoTable()) {
                if (machine.getLevel() != GTUtil.getClientLevel().dimension()) {
                    continue;
                }
                var pos = machine.getPos();
                if (player.blockPosition().distSqr(pos) > 64 * 64) {
                    continue;
                }
                RenderHelper.highlightBlock(camera, poseStack, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, lineWidth, pos, pos);
                RenderHelper.renderSeeThroughText(camera, poseStack, pos, color.getRGB(), gridName, bufferSource);
            }
        }
    }

    public static boolean shouldHighlight() {
        var player = Minecraft.getInstance().player;
        if (player == null) return false;
        var heldItem = player.getMainHandItem();
        if (heldItem.getItem() instanceof MetaMachineItem item &&
                WirelessMachine.WIRELESS_MACHINE_DEFINITIONS.contains(item.getDefinition()))
            return true;
        return heldItem.getItem() == GTOItems.ME_WIRELESS_MACHINE_CONFIGURATOR.asItem();
    }

    private static Color getGridColor(WirelessNetwork grid, boolean specialFx) {
        int hash = grid.getId().hashCode();
        float hue = (hash % 360) / 360f;
        float brightness = 0.8f + (specialFx ? (float) Math.sin(System.currentTimeMillis() / 200.0) * 0.2f : 0);
        float saturation = 0.4f + (specialFx ? (float) -Math.sin(System.currentTimeMillis() / 200.0) * 0.4f : 0.4f);
        return Color.getHSBColor(hue, saturation, brightness);
    }

    static {
        WirelessMachine.WIRELESS_MACHINE_DEFINITIONS.add(ME_EXPORT_BUFFER);
        WirelessMachine.WIRELESS_MACHINE_DEFINITIONS.add(ME_WIRELESS_CONNECTION_MACHINE);
    }
}
