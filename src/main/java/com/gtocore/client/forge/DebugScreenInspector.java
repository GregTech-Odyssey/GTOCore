package com.gtocore.client.forge;

import com.gtocore.client.KeyBind;

import com.gtocore.config.GTOConfig;
import com.gtolib.GTOCore;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfoUtils;

/// A useful debug tool to inspect the slot indices in container screens.
/// This file is under the MPL-2.0 license.
/// [link](https://github.com/TeamDman/SuperFactoryManager/blob/1.20.1/src/main/java/ca/teamdman/sfm/client/handler/ContainerScreenInspectorHandler.java)
/// @author teamdman
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = GTOCore.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DebugScreenInspector {

    private static final boolean isSFMLoaded = ModInfoUtils.isModLoaded("sfm");
    private static boolean visible = false;

    @SubscribeEvent
    public static void onGuiRender(ScreenEvent.Render.Post event) {
        if (!visible) return;
        if (event.getScreen() instanceof AbstractContainerScreen<?> screen) {
            AbstractContainerMenu menu = screen.getMenu();
            int containerSlotCount = 0;
            int inventorySlotCount = 0;
            GuiGraphics graphics = event.getGuiGraphics();
            PoseStack poseStack = graphics.pose();
            poseStack.pushPose();
            poseStack.translate(0, 0, 350); // render text over the items but under the tooltips

            // draw index on each slot
            Font font = Minecraft.getInstance().font;
            for (var slot : menu.slots) {
                int color;
                if (slot.container instanceof Inventory) {
                    // noinspection DataFlowIssue
                    color = ChatFormatting.YELLOW.getColor();
                    inventorySlotCount++;
                } else {
                    color = 0xFFF;
                    containerSlotCount++;
                }
                graphics.drawString(
                        font,
                        Component.literal(Integer.toString(slot.getSlotIndex())),
                        screen.getGuiLeft() + slot.x,
                        screen.getGuiTop() + slot.y,
                        color,
                        false);
            }

            // draw text for slot totals
            graphics.drawString(
                    font,
                    Component.literal(String.valueOf(containerSlotCount)).withStyle(ChatFormatting.BLUE),
                    5,
                    25,
                    0xFFFFFF,
                    true);
            graphics.drawString(
                    font,
                    Component.literal(String.valueOf(inventorySlotCount)).withStyle(ChatFormatting.YELLOW),
                    5,
                    40,
                    0xFFFFFF,
                    true);
            poseStack.popPose();
        }
    }

    @SubscribeEvent
    public static void onKeyDown(ScreenEvent.KeyPressed.Pre event) {
        if (isSFMLoaded) return; // Skip if SFM is loaded, as it handles the hotkey itself
        if (!GTOConfig.INSTANCE.dev) return; // Only enable in dev mode
        // Handle Ctrl+I hotkey to toggle overlay
        var toggleKey = KeyBind.debugInspectKey;
        var toggleKeyPressed = toggleKey.isActiveAndMatches(InputConstants.Type.KEYSYM.getOrCreate(event.getKeyCode()));
        if (toggleKeyPressed) {
            visible = !visible;
            event.setCanceled(true);
        }
    }
}
