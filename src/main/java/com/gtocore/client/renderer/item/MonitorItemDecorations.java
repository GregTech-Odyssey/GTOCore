package com.gtocore.client.renderer.item;

import com.gtocore.common.machine.monitor.MonitorBlockItem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;

public class MonitorItemDecorations implements IItemDecorator {

    public static final MonitorItemDecorations DECORATOR = new MonitorItemDecorations();

    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack itemStack, int xOffset, int yOffset) {
        if (itemStack.getItem() instanceof MonitorBlockItem blockItem) {
            var rl = BuiltInRegistries.BLOCK.getKey(blockItem.getBlock());
            var textureRL = MonitorBlockItem.getTexturePath(rl);
            boolean exists = Minecraft.getInstance().getResourceManager().getResource(textureRL).isPresent();
            if (!exists) {
                return false; // Texture does not exist, do not render
            }
            var pose = guiGraphics.pose();
            pose.pushPose();
            pose.translate(0, 0, 199);
            pose.scale(0.5f, 0.5f, 1f);
            guiGraphics.blit(textureRL, 2 * (xOffset), 2 * (yOffset), 0, 0, 16, 16, 16, 16);
            pose.popPose();
            return true;
        }
        return false;
    }
}
