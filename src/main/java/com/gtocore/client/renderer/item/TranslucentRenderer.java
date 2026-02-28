package com.gtocore.client.renderer.item;

import com.gtolib.utils.ClientUtil;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.client.renderer.IRenderer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import committee.nova.mods.renderblender.api.client.render.buffer.AlphaOverrideVertexConsumer;

public enum TranslucentRenderer implements IRenderer {

    INSTANCE;

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderItem(ItemStack stack, ItemDisplayContext transformType, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model) {
        if (!(stack.getItem() instanceof BlockItem)) {
            poseStack.pushPose();
            // RenderState.IS_RENDERING_LEVEL = true;
            RenderSystem.enableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float) (Math.sin(System.currentTimeMillis() / 10f) * 0.5f + 0.5f));
            // ClientUtil.vanillaRender(stack, transformType, leftHand, poseStack, buffer, combinedLight,
            // combinedOverlay, ClientUtil.getVanillaModel(stack, null, null));
            poseStack.translate(-0.5F, -0.5F, -0.5F);
            VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, ItemBlockRenderTypes.getRenderType(stack, true), true, stack.hasFoil());
            ClientUtil.getItemRenderer().renderModelLists(ClientUtil.getVanillaModel(stack, null, null), stack, combinedLight, combinedOverlay, poseStack, new AlphaOverrideVertexConsumer(vertexConsumer, (Math.sin(System.currentTimeMillis() * 0.0034) * 0.3 + 0.5)));
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            // RenderState.IS_RENDERING_LEVEL = false;
            poseStack.popPose();
        } else {
            ClientUtil.vanillaRender(stack, transformType, leftHand, poseStack, buffer, combinedLight, combinedOverlay, ClientUtil.getVanillaModel(stack, null, null));
        }
    }
}
