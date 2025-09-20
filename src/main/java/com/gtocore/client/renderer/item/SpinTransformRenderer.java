package com.gtocore.client.renderer.item;

import com.gtocore.client.renderer.PosestackHelper;

import com.gtolib.utils.ClientUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.mojang.blaze3d.vertex.PoseStack;

public enum SpinTransformRenderer implements IRenderer {

    INSTANCE;

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderItem(ItemStack stack, ItemDisplayContext transformType, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model) {
        poseStack.pushPose();
        if (transformType == ItemDisplayContext.GUI) {
            PosestackHelper.spinningTransformPosestack(
                    poseStack,
                    1.0f,
                    Math.max(0, (float) Math.sin(System.currentTimeMillis() / 6000.0)),
                    20,
                    Minecraft.getInstance().level.getGameTime(),
                    Minecraft.getInstance().getPartialTick());
        }
        // RenderState.IS_RENDERING_LEVEL = true;
        ClientUtil.vanillaRender(stack, transformType, leftHand, poseStack, buffer, combinedLight, combinedOverlay, ClientUtil.getVanillaModel(stack, null, null));
        // RenderState.IS_RENDERING_LEVEL = false;
        poseStack.popPose();
    }
}
