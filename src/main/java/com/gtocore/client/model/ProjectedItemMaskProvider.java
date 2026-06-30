package com.gtocore.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

@OnlyIn(Dist.CLIENT)
public final class ProjectedItemMaskProvider implements ShaderItemMaskProvider {

    public static final ProjectedItemMaskProvider INSTANCE = new ProjectedItemMaskProvider();

    private static final int BUFFER_SIZE = 1024;

    @Nullable
    private static TextureTarget maskTarget;
    @Nullable
    private static CaptureBufferSource captureBufferSource;

    private ProjectedItemMaskProvider() {}

    @Override
    @Nullable
    public RuntimeMask capture(ShaderItemBakedModel model, ItemStack stack, PoseStack poseStack, int packedLight, int packedOverlay) {
        int[] viewport = new int[4];
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);
        int viewportWidth = Math.max(1, viewport[2]);
        int viewportHeight = Math.max(1, viewport[3]);
        ensureResources(viewportWidth, viewportHeight);
        if (maskTarget == null || captureBufferSource == null) {
            return null;
        }

        int previousDrawFramebuffer = GL11.glGetInteger(GL30.GL_DRAW_FRAMEBUFFER_BINDING);
        int previousReadFramebuffer = GL11.glGetInteger(GL30.GL_READ_FRAMEBUFFER_BINDING);

        try {
            maskTarget.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            maskTarget.clear(Minecraft.ON_OSX);
            maskTarget.bindWrite(false);
            RenderSystem.viewport(0, 0, viewportWidth, viewportHeight);

            BakedModel resolved = model.resolveWrappedModel(stack);
            model.forEachRenderLayer(resolved, stack, true, (renderPass, renderType) -> {
                model.renderWrappedPass(renderPass, stack, poseStack, captureBufferSource, packedLight, packedOverlay, renderType);
                captureBufferSource.endBatch(renderType);
            });
            captureBufferSource.endBatch();

            return new RuntimeMask(maskTarget.getColorTextureId(), maskTarget.width, maskTarget.height, viewport[0], viewport[1]);
        } finally {
            GlStateManager._glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, previousReadFramebuffer);
            GlStateManager._glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, previousDrawFramebuffer);
            RenderSystem.viewport(viewport[0], viewport[1], viewportWidth, viewportHeight);
        }
    }

    private static void ensureResources(int width, int height) {
        if (maskTarget != null && maskTarget.width == width && maskTarget.height == height && captureBufferSource != null) {
            return;
        }

        if (maskTarget != null) {
            maskTarget.destroyBuffers();
        }

        maskTarget = new TextureTarget(width, height, true, Minecraft.ON_OSX);
        maskTarget.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        maskTarget.setFilterMode(GL11.GL_NEAREST);
        captureBufferSource = new CaptureBufferSource(BUFFER_SIZE);
    }

    private static final class CaptureBufferSource extends MultiBufferSource.BufferSource {

        private CaptureBufferSource(int bufferSize) {
            super(new BufferBuilder(bufferSize), java.util.Collections.emptyMap());
        }
    }
}
