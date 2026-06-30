package com.gtocore.client.renderer.fx;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

public final class ScreenSpaceSceneCapture {

    private static TextureTarget sceneTarget;
    private static boolean sceneCopiedThisFrame;

    private ScreenSpaceSceneCapture() {}

    public static void beginFrame() {
        sceneCopiedThisFrame = false;
    }

    public static TextureTarget capture(LevelRenderer levelRenderer) {
        ensureSceneTarget();
        if (sceneTarget == null) {
            return null;
        }
        if (sceneCopiedThisFrame) {
            return sceneTarget;
        }

        Minecraft minecraft = Minecraft.getInstance();
        RenderTarget mainTarget = minecraft.getMainRenderTarget();
        RenderTarget outputTarget = Minecraft.useShaderTransparency() && levelRenderer.getWeatherTarget() != null ? levelRenderer.getWeatherTarget() : mainTarget;
        sceneTarget.clear(Minecraft.ON_OSX);
        GlStateManager._glBindFramebuffer(36008, outputTarget.frameBufferId);
        GlStateManager._glBindFramebuffer(36009, sceneTarget.frameBufferId);
        GlStateManager._glBlitFrameBuffer(
                0, 0, outputTarget.width, outputTarget.height,
                0, 0, sceneTarget.width, sceneTarget.height,
                16384, 9728);
        GlStateManager._glBindFramebuffer(36160, 0);
        outputTarget.bindWrite(true);
        RenderSystem.viewport(0, 0, minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
        sceneCopiedThisFrame = true;
        return sceneTarget;
    }

    public static void release() {
        if (sceneTarget != null) {
            sceneTarget.destroyBuffers();
            sceneTarget = null;
        }
        sceneCopiedThisFrame = false;
    }

    private static void ensureSceneTarget() {
        Minecraft minecraft = Minecraft.getInstance();
        RenderTarget mainTarget = minecraft.getMainRenderTarget();
        if (mainTarget.width <= 0 || mainTarget.height <= 0) {
            release();
            return;
        }
        if (sceneTarget == null || sceneTarget.width != mainTarget.width || sceneTarget.height != mainTarget.height) {
            release();
            sceneTarget = new TextureTarget(mainTarget.width, mainTarget.height, false, Minecraft.ON_OSX);
            sceneTarget.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            sceneTarget.setFilterMode(9729);
        }
    }
}
