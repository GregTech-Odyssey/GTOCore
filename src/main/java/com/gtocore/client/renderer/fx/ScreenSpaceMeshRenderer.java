package com.gtocore.client.renderer.fx;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;

public final class ScreenSpaceMeshRenderer {

    private ScreenSpaceMeshRenderer() {}

    public static void render(PoseStack poseStack, VertexBuffer vertexBuffer, RenderType renderType, ShaderInstance shader,
                              float red, float green, float blue, float alpha) {
        if (vertexBuffer == null || shader == null) {
            return;
        }

        renderType.setupRenderState();
        try {
            RenderSystem.setShader(() -> shader);
            RenderSystem.setShaderColor(red, green, blue, alpha);
            vertexBuffer.bind();
            vertexBuffer.drawWithShader(poseStack.last().pose(), RenderSystem.getProjectionMatrix(), shader);
        } finally {
            VertexBuffer.unbind();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            renderType.clearRenderState();
        }
    }

    public static void renderScaled(PoseStack poseStack, VertexBuffer vertexBuffer, RenderType renderType, ShaderInstance shader,
                                    float scaleX, float scaleY, float scaleZ,
                                    float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.scale(scaleX, scaleY, scaleZ);
        render(poseStack, vertexBuffer, renderType, shader, red, green, blue, alpha);
        poseStack.popPose();
    }
}
