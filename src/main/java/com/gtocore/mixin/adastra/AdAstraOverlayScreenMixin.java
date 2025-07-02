package com.gtocore.mixin.adastra;

import com.gtocore.common.item.armor.SpaceArmorComponentItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.adastra.api.systems.PlanetData;
import earth.terrarium.adastra.client.config.AdAstraConfigClient;
import earth.terrarium.adastra.client.screens.player.OverlayScreen;
import earth.terrarium.adastra.client.utils.ClientData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static earth.terrarium.adastra.client.screens.player.OverlayScreen.OXYGEN_TANK;
import static earth.terrarium.adastra.client.screens.player.OverlayScreen.OXYGEN_TANK_EMPTY;

@Mixin(OverlayScreen.class)
public abstract class AdAstraOverlayScreenMixin {

    @Inject(method = "render", at = @At("TAIL"), remap = false)
    private static void render(GuiGraphics graphics, float partialTick, CallbackInfo ci) {
        var player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator()) return;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options.renderDebug) return;
        var font = minecraft.font;
        PoseStack poseStack = graphics.pose();
        var chestplate = player.getInventory().getArmor(2);
        if (chestplate.getItem() instanceof SpaceArmorComponentItem spaceSuit) {
            long amount = SpaceArmorComponentItem.getOxygenAmount(player);
            long capacity = spaceSuit.getFluidContainer(chestplate).getTankCapacity(0);
            double ratio = (double) amount / capacity;
            int barHeight = (int) (ratio * 52);

            int x = AdAstraConfigClient.oxygenBarX;
            int y = AdAstraConfigClient.oxygenBarY;
            float scale = AdAstraConfigClient.oxygenBarScale;

            poseStack.pushPose();
            poseStack.scale(scale, scale, scale);
            graphics.blit(OXYGEN_TANK_EMPTY, x, y, 0, 0, 62, 52, 62, 52);
            graphics.blit(OXYGEN_TANK, x, y + 52 - barHeight, 0, 52 - barHeight, 62, barHeight, 62, 52);

            var text = String.format("%.1f%%", ratio * 100);
            int textWidth = font.width(text);
            int color = ratio <= 0 ? 0xDC143C : 0xFFFFFF;
            PlanetData localData = ClientData.getLocalData();
            if (localData != null && localData.oxygen()) {
                color = 0x55ff55;
            }
            graphics.drawString(font, text, (int) (x + (62 - textWidth) / 2f), y + 52 + 3, color);
            poseStack.popPose();
        }
    }
}
