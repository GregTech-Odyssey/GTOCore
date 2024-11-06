package com.gto.gtocore.client.renderer.item;

import com.gto.gtocore.client.ClientUtil;

import com.lowdragmc.lowdraglib.client.renderer.IRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Quaternionf;

public class StereoscopicItemRenderer implements IRenderer {

    public static final StereoscopicItemRenderer INSTANCE = new StereoscopicItemRenderer();

    @Override
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("all")
    public void renderItem(ItemStack stack, ItemDisplayContext transformType,
                           boolean leftHand, PoseStack poseStack,
                           MultiBufferSource buffer, int combinedLight,
                           int combinedOverlay, BakedModel model) {
        model = ClientUtil.getVanillaModel(stack, null, null);
        poseStack.pushPose();
        if (transformType == ItemDisplayContext.GUI) {
            poseStack.mulPose(new Quaternionf()
                    .fromAxisAngleDeg(0.3f, 0.5f, 0.2f, (System.currentTimeMillis() / 25) % 360));
        }
        ClientUtil.vanillaRender(stack, transformType, leftHand, poseStack,
                buffer, combinedLight, combinedOverlay, model);
        poseStack.popPose();
    }
}
