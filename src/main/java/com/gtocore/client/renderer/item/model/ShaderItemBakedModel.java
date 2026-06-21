package com.gtocore.client.renderer.item.model;

import com.gtocore.client.renderer.GTORenderTypes;
import com.gtocore.client.renderer.item.model.ShaderItemModelLoader.UniformValue;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import com.mojang.blaze3d.shaders.AbstractUniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import org.joml.Matrix4f;

import java.util.Map;

public class ShaderItemBakedModel extends WrappedItemModel {

    private static final float OVERLAY_Z = 0.533F;

    private final ResourceLocation maskTexture;
    private final ResourceLocation shaderLocation;
    private final Map<String, UniformValue> params;

    public ShaderItemBakedModel(BakedModel wrapped, ResourceLocation maskTexture, ResourceLocation shaderLocation, Map<String, UniformValue> params) {
        super(wrapped);
        this.maskTexture = maskTexture;
        this.shaderLocation = shaderLocation;
        this.params = params;
    }

    @Override
    public void renderItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer,
                           int packedLight, int packedOverlay) {
        renderWrapped(stack, poseStack, buffer, packedLight, packedOverlay, true);
        if (buffer instanceof MultiBufferSource.BufferSource bufferSource) {
            bufferSource.endBatch();
        }

        ShaderInstance shader = GTORenderTypes.getShader(shaderLocation);
        if (shader == null) {
            return;
        }

        applyParams(shader);

        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, OVERLAY_Z);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(() -> shader);
        RenderSystem.setShaderTexture(0, maskTexture);

        var tesselator = Tesselator.getInstance();
        BufferBuilder builder = tesselator.getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        Matrix4f matrix = poseStack.last().pose();
        builder.vertex(matrix, 1.0F, 1.0F, 0.0F).uv(1.0F, 0.0F).endVertex();
        builder.vertex(matrix, 0.0F, 1.0F, 0.0F).uv(0.0F, 0.0F).endVertex();
        builder.vertex(matrix, 0.0F, 0.0F, 0.0F).uv(0.0F, 1.0F).endVertex();
        builder.vertex(matrix, 1.0F, 0.0F, 0.0F).uv(1.0F, 1.0F).endVertex();
        tesselator.end();

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        poseStack.popPose();
    }

    @Override
    public PerspectiveModelState getModelState() {
        if (parentState instanceof PerspectiveModelState state) {
            return state;
        }
        return PerspectiveModelState.IDENTITY;
    }

    private void applyParams(ShaderInstance shader) {
        for (Map.Entry<String, UniformValue> entry : params.entrySet()) {
            applyParam(entry.getKey(), shader, entry.getValue());
        }
        var minecraft = Minecraft.getInstance();
        float guiWidth = minecraft.getWindow().getGuiScaledWidth();
        float guiHeight = minecraft.getWindow().getGuiScaledHeight();
        float screenWidth = minecraft.getWindow().getScreenWidth();
        float screenHeight = minecraft.getWindow().getScreenHeight();
        applyParam("time", shader, new UniformValue(new float[] { (float) (System.currentTimeMillis() % 100000L) / 1000.0F }));
        float mouseX = (float) (minecraft.mouseHandler.xpos() * guiWidth / screenWidth);
        float mouseY = (float) (minecraft.mouseHandler.ypos() * guiHeight / screenHeight);
        applyParam("mousePos", shader, new UniformValue(new float[] { mouseX, mouseY }));
        applyParam("resolution", shader, new UniformValue(new float[] { screenWidth, screenHeight }));
    }

    private void applyParam(String name, ShaderInstance shader, UniformValue value) {
        AbstractUniform uniform = shader.safeGetUniform(name);
        applyUniform(uniform, value.values());
    }

    private static void applyUniform(AbstractUniform uniform, float... values) {
        switch (values.length) {
            case 1 -> uniform.set(values[0]);
            case 2 -> uniform.set(values[0], values[1]);
            case 3 -> uniform.set(values[0], values[1], values[2]);
            case 4 -> uniform.set(values[0], values[1], values[2], values[3]);
            default -> uniform.set(values);
        }
    }
}
