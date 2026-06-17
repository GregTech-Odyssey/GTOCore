package com.gtocore.client.renderer.item;

import com.gtocore.client.renderer.PosestackHelper;

import com.gtolib.utils.ClientUtil;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import org.embeddedt.modernfix.render.RenderState;

public final class StereoscopicItemRenderer implements IRenderer {

    public static final StereoscopicItemRenderer INSTANCE = new StereoscopicItemRenderer();

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderItem(ItemStack stack, ItemDisplayContext transformType, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model) {
        poseStack.pushPose();
        if (transformType == ItemDisplayContext.GUI) {
            PosestackHelper.stereoTransformPosestack(poseStack, 0.3f, 0.5f, 0.2f, ((float) System.currentTimeMillis() / 25) % 360);
        }
        RenderState.IS_RENDERING_LEVEL = true;
        ClientUtil.vanillaRender(stack, transformType, leftHand, poseStack, buffer, combinedLight, combinedOverlay, ClientUtil.getVanillaModel(stack, null, null));
        RenderState.IS_RENDERING_LEVEL = false;
        poseStack.popPose();
    }
}
