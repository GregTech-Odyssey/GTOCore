package com.gtocore.client.renderer.item;

import com.gtocore.utils.StxckUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.item.ItemEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class ItemCountRenderer {

    public void renderItemCount(ItemEntity entity, PoseStack poseStack, MultiBufferSource bufferSource, int light, EntityRenderDispatcher entityRenderDispatcher) {
        var player = Minecraft.getInstance().player;
        if (player != null && player.isShiftKeyDown()) return;
        var text = getOverlayText(entity);
        if (text == null) return;
        var scale = 0.025f * (float) StxckUtil.getOverlaySizeMultiplier();
        poseStack.pushPose();
        poseStack.translate(0d, entity.getBbHeight() + 0.75f, 0d);
        poseStack.mulPose(entityRenderDispatcher.cameraOrientation);
        poseStack.scale(-scale, -scale, scale);
        var component = Component.literal(text);
        var matrix4f = poseStack.last().pose();
        var f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        var font = entityRenderDispatcher.font;
        var j = (int) (f1 * 255f) << 24;
        var f2 = (float) (-font.width(component) / 2);
        font.drawInBatch(component, f2, 0, 553648127, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, j, light);
        font.drawInBatch(component, f2, 0, -1, false, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, light);
        poseStack.popPose();
    }

    @Nullable
    private String getOverlayText(ItemEntity entity) {
        return switch (StxckUtil.getCountTextMode()) {
            case 1 -> getTotalCountOverlayText(entity);
            case 2 -> {
                var maxStackSize = entity.getItem().getMaxStackSize();
                var stackCount = (int) Math.ceil((double) StxckUtil.getTotalCount(entity) / maxStackSize);
                var show = stackCount > 1 || StxckUtil.alwaysShowItemCount();
                yield show ? String.format("%dx", stackCount) : null;
            }
            default -> throw new IllegalStateException("Unexpected value: " + 2);
        };
    }

    @Nullable
    private String getTotalCountOverlayText(ItemEntity entity) {
        var total = StxckUtil.getTotalCount(entity);
        if (total >= 1_000_000_000) return String.format("%.3fB", total / 1_000_000_000f);
        if (total >= 1_000_000) return String.format("%.2fM", total / 1_000_000f);
        if (total >= 10_000) return String.format("%.1fK", total / 1_000f);
        if (StxckUtil.alwaysShowItemCount() || total > entity.getItem().getMaxStackSize()) return String.valueOf(total);
        return null;
    }
}
