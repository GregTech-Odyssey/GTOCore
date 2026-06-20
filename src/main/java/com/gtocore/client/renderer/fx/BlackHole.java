package com.gtocore.client.renderer.fx;

import com.gtocore.client.renderer.GTORenderTypes;
import com.gtocore.client.renderer.RenderHelper;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import org.joml.Matrix4f;
import org.joml.Vector4f;

@OnlyIn(Dist.CLIENT)
public class BlackHole extends AbstractFX {

    private static final int GROW_DURATION = 60;
    private static final int FULL_DETAIL_RADIUS_PIXELS = 220;
    private static final int MEDIUM_DETAIL_RADIUS_PIXELS = 100;
    private static final int LOW_DETAIL_RADIUS_PIXELS = 40;
    private int stableEndAge = 0;
    private int shrinkEndAge = 0;
    private boolean markedEnding = false;
    private float endingFromScale = 0.0F;
    private static final float LENS_STRENGTH = 1.0F;
    private static final float PHOTON_RING_WIDTH = 0.075F;
    private static final float SHADOW_MASK_INSET_MIN = 1.5F;
    private static final float SHADOW_MASK_INSET_MAX = 6.0F;
    private static final float MIN_VISIBLE_RADIUS = 0.001F;
    private static final SphereMesh[] CORE_MESHES = new SphereMesh[] {
            new SphereMesh(16, 32),
            new SphereMesh(32, 64),
            new SphereMesh(64, 128)
    };
    private static final SphereMesh[] EVENT_HORIZON_MESHES = new SphereMesh[] {
            new SphereMesh(20, 40),
            new SphereMesh(40, 80),
            new SphereMesh(64, 128)
    };

    private static TextureTarget sceneTarget;
    private static boolean sceneCopiedThisFrame;

    public Vec3 center;
    public double coreRadius;
    public double eventHorizonRadius;

    public BlackHole(Vec3 center, double coreRadius, double eventHorizonRadius) {
        this.center = center;
        this.coreRadius = coreRadius;
        this.eventHorizonRadius = eventHorizonRadius;
    }

    @Override
    public boolean shouldDiscard() {
        return stableEndAge > 0 &&
                age >= shrinkEndAge;
    }

    public void markEnding() {
        this.markedEnding = true;
    }

    @Override
    public void render(RenderLevelStageEvent.Stage stage, LevelRenderer levelRenderer, PoseStack poseStack, Matrix4f projectionMatrix, float partialTick, Camera camera, Frustum frustum) {
        if (stage != RenderLevelStageEvent.Stage.AFTER_WEATHER) {
            return;
        }

        float scale = getAnimatedScale(partialTick);
        if (scale <= 0.0F) {
            return;
        }

        float core = (float) this.coreRadius * scale;
        float eventHorizon = (float) this.eventHorizonRadius * scale;
        if (core <= MIN_VISIBLE_RADIUS || eventHorizon <= core) {
            return;
        }

        PoseStack worldStack = new PoseStack();
        worldStack.mulPoseMatrix(poseStack.last().pose());
        Vec3 cameraPos = camera.getPosition();
        worldStack.translate(this.center.x - cameraPos.x, this.center.y - cameraPos.y, this.center.z - cameraPos.z);

        ScreenSpaceSphere screenSphere = ScreenSpaceSphere.project(this.center, core, eventHorizon, camera, poseStack, projectionMatrix);
        if (screenSphere == null) {
            return;
        }

        ensureSceneTarget();
        copySceneToTarget(levelRenderer, partialTick);
        renderEventHorizon(worldStack, eventHorizon, screenSphere);
        renderCore(worldStack, core, screenSphere);
    }

    private float getAnimatedScale(float partialTick) {
        float currentAge = this.age + partialTick;
        if (markedEnding && stableEndAge == 0) {
            stableEndAge = age;
            shrinkEndAge = age + GROW_DURATION;
        } else if (!markedEnding) {
            return endingFromScale = Mth.clamp(currentAge / GROW_DURATION, 0.0F, 1.0F);
        }
        if (currentAge < shrinkEndAge) {
            return endingFromScale - Mth.clamp((currentAge - stableEndAge) / (shrinkEndAge - stableEndAge), 0.0F, endingFromScale);
        }
        return 0.0F;
    }

    public static void beginFrame() {
        sceneCopiedThisFrame = false;
    }

    private static void renderCore(PoseStack poseStack, float coreRadius, ScreenSpaceSphere screenSphere) {
        renderSphereMesh(poseStack, coreRadius, pickSphereMesh(CORE_MESHES, screenSphere.coreRadius),
                GTORenderTypes.BLACK_HOLE_CORE, GameRenderer.getPositionColorShader(),
                0.0F, 0.0F, 0.0F, 1.0F);
    }

    private static void renderEventHorizon(PoseStack poseStack, float eventHorizonRadius, ScreenSpaceSphere screenSphere) {
        ShaderInstance shader = GTORenderTypes.getBlackHoleEventHorizonShader();
        if (shader == null || sceneTarget == null) {
            return;
        }

        shader.setSampler("DiffuseSampler", sceneTarget.getColorTextureId());
        shader.setSampler("DepthSampler", sceneTarget.getDepthTextureId());
        shader.safeGetUniform("BlackHoleCenterScreen").set(screenSphere.centerX, screenSphere.centerY);
        shader.safeGetUniform("ShadowRadiusScreen").set(getShadowMaskRadius(screenSphere));
        shader.safeGetUniform("LensingRadiusScreen").set(screenSphere.eventHorizonRadius);
        shader.safeGetUniform("BlackHoleFrontDepth").set(screenSphere.frontDepth);
        shader.safeGetUniform("LensStrength").set(LENS_STRENGTH);
        shader.safeGetUniform("PhotonRingWidth").set(PHOTON_RING_WIDTH);
        shader.safeGetUniform("ScreenSize").set((float) sceneTarget.viewWidth, (float) sceneTarget.viewHeight);

        renderSphereMesh(poseStack, eventHorizonRadius, pickSphereMesh(EVENT_HORIZON_MESHES, screenSphere.eventHorizonRadius),
                GTORenderTypes.BLACK_HOLE_EVENT_HORIZON, shader,
                1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void ensureSceneTarget() {
        Minecraft minecraft = Minecraft.getInstance();
        RenderTarget mainTarget = minecraft.getMainRenderTarget();
        if (sceneTarget == null || sceneTarget.width != mainTarget.width || sceneTarget.height != mainTarget.height) {
            if (sceneTarget != null) {
                sceneTarget.destroyBuffers();
            }
            sceneTarget = new TextureTarget(mainTarget.width, mainTarget.height, true, Minecraft.ON_OSX);
            sceneTarget.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            sceneTarget.setFilterMode(9729);
        }
    }

    private static void copySceneToTarget(LevelRenderer levelRenderer, float partialTick) {
        if (sceneCopiedThisFrame) {
            return;
        }

        RenderTarget mainTarget = Minecraft.getInstance().getMainRenderTarget();
        RenderTarget outputTarget = Minecraft.useShaderTransparency() && levelRenderer.getWeatherTarget() != null ? levelRenderer.getWeatherTarget() : mainTarget;
        sceneTarget.clear(Minecraft.ON_OSX);
        GlStateManager._glBindFramebuffer(36008, outputTarget.frameBufferId);
        GlStateManager._glBindFramebuffer(36009, sceneTarget.frameBufferId);
        GlStateManager._glBlitFrameBuffer(
                0, 0, outputTarget.width, outputTarget.height,
                0, 0, sceneTarget.width, sceneTarget.height,
                16640, 9728);
        GlStateManager._glBindFramebuffer(36160, 0);
        outputTarget.bindWrite(true);
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.viewport(0, 0, minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
        sceneCopiedThisFrame = true;
    }

    private static SphereMesh pickSphereMesh(SphereMesh[] meshes, float projectedRadiusPixels) {
        if (projectedRadiusPixels >= FULL_DETAIL_RADIUS_PIXELS) {
            return meshes[2];
        }
        if (projectedRadiusPixels >= MEDIUM_DETAIL_RADIUS_PIXELS) {
            return meshes[1];
        }
        if (projectedRadiusPixels >= LOW_DETAIL_RADIUS_PIXELS) {
            return meshes[0];
        }
        return meshes[0];
    }

    private static void renderSphereMesh(PoseStack poseStack, float radius, SphereMesh mesh,
                                         net.minecraft.client.renderer.RenderType renderType, ShaderInstance shader,
                                         float red, float green, float blue, float alpha) {
        VertexBuffer vertexBuffer = mesh.getBuffer();
        if (vertexBuffer == null) {
            return;
        }

        renderType.setupRenderState();
        RenderSystem.setShader(() -> shader);
        RenderSystem.setShaderColor(red, green, blue, alpha);
        poseStack.pushPose();
        poseStack.scale(radius, radius, radius);
        vertexBuffer.bind();
        vertexBuffer.drawWithShader(poseStack.last().pose(), RenderSystem.getProjectionMatrix(), shader);
        VertexBuffer.unbind();
        poseStack.popPose();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        renderType.clearRenderState();
    }

    private static float getShadowMaskRadius(ScreenSpaceSphere screenSphere) {
        float inset = Mth.clamp(screenSphere.coreRadius * 0.01F, SHADOW_MASK_INSET_MIN, SHADOW_MASK_INSET_MAX);
        return Math.max(screenSphere.coreRadius - inset, MIN_VISIBLE_RADIUS);
    }

    @Override
    protected void onDiscard() {
        super.onDiscard();
        if (FXManager.FX_LIST.stream().noneMatch(BlackHole.class::isInstance) && sceneTarget != null) {
            sceneTarget.destroyBuffers();
            sceneTarget = null;
            sceneCopiedThisFrame = false;
        }
    }

    private static final class SphereMesh {

        private final int latitudeSegments;
        private final int longitudeSegments;
        private VertexBuffer buffer;

        private SphereMesh(int latitudeSegments, int longitudeSegments) {
            this.latitudeSegments = latitudeSegments;
            this.longitudeSegments = longitudeSegments;
        }

        private VertexBuffer getBuffer() {
            if (buffer == null) {
                buffer = RenderHelper.buildUnitSphereBuffer(latitudeSegments, longitudeSegments);
            }
            return buffer;
        }
    }

    private record ScreenSpaceSphere(float centerX, float centerY, float coreRadius, float eventHorizonRadius, float frontDepth) {

        private static ScreenSpaceSphere project(Vec3 center, float coreRadius, float eventHorizonRadius, Camera camera, PoseStack poseStack, Matrix4f projectionMatrix) {
            Minecraft minecraft = Minecraft.getInstance();
            int screenWidth = minecraft.getWindow().getWidth();
            int screenHeight = minecraft.getWindow().getHeight();
            if (screenWidth <= 0 || screenHeight <= 0) {
                return null;
            }

            Vec3 cameraRelative = center.subtract(camera.getPosition());
            Matrix4f viewProjection = new Matrix4f(projectionMatrix);
            viewProjection.mul(poseStack.last().pose());

            Vector4f centerClip = new Vector4f((float) cameraRelative.x, (float) cameraRelative.y, (float) cameraRelative.z, 1.0F);
            viewProjection.transform(centerClip);
            if (centerClip.w <= 0.0F) {
                return null;
            }

            Vector4f horizonClip = createOffsetPoint(cameraRelative, camera, eventHorizonRadius);
            viewProjection.transform(horizonClip);
            if (horizonClip.w <= 0.0F) {
                return null;
            }

            Vector4f coreClip = createOffsetPoint(cameraRelative, camera, coreRadius);
            viewProjection.transform(coreClip);
            if (coreClip.w <= 0.0F) {
                return null;
            }

            float centerNdcX = centerClip.x / centerClip.w;
            float centerNdcY = centerClip.y / centerClip.w;

            float centerScreenX = (centerNdcX * 0.5F + 0.5F) * screenWidth;
            float centerScreenY = (centerNdcY * 0.5F + 0.5F) * screenHeight;

            float eventRadiusPixels = projectRadiusPixels(cameraRelative, camera, eventHorizonRadius, viewProjection, centerNdcX, centerNdcY, screenWidth, screenHeight);
            float coreRadiusPixels = projectRadiusPixels(cameraRelative, camera, coreRadius, viewProjection, centerNdcX, centerNdcY, screenWidth, screenHeight);
            if (eventRadiusPixels <= MIN_VISIBLE_RADIUS || coreRadiusPixels >= eventRadiusPixels) {
                return null;
            }

            Vector4f frontClip = createFrontPoint(cameraRelative, camera, eventHorizonRadius);
            viewProjection.transform(frontClip);
            float frontDepth = centerClip.z / centerClip.w * 0.5F + 0.5F;
            if (frontClip.w > 0.0F) {
                frontDepth = frontClip.z / frontClip.w * 0.5F + 0.5F;
            }

            return new ScreenSpaceSphere(centerScreenX, centerScreenY, coreRadiusPixels, eventRadiusPixels, Mth.clamp(frontDepth, 0.0F, 1.0F));
        }

        private static float projectRadiusPixels(Vec3 cameraRelative, Camera camera, float radius, Matrix4f viewProjection,
                                                 float centerNdcX, float centerNdcY, int screenWidth, int screenHeight) {
            float horizontalRadius = projectAxisRadiusPixels(cameraRelative, camera.getLeftVector(), radius, viewProjection, centerNdcX, centerNdcY, screenWidth, screenHeight);
            float verticalRadius = projectAxisRadiusPixels(cameraRelative, camera.getUpVector(), radius, viewProjection, centerNdcX, centerNdcY, screenWidth, screenHeight);
            return Math.max(horizontalRadius, verticalRadius);
        }

        private static float projectAxisRadiusPixels(Vec3 cameraRelative, org.joml.Vector3f axis, float radius, Matrix4f viewProjection,
                                                     float centerNdcX, float centerNdcY, int screenWidth, int screenHeight) {
            Vector4f clip = new Vector4f((float) (cameraRelative.x + axis.x() * radius),
                    (float) (cameraRelative.y + axis.y() * radius),
                    (float) (cameraRelative.z + axis.z() * radius), 1.0F);
            viewProjection.transform(clip);
            if (clip.w <= 0.0F) {
                return 0.0F;
            }

            float ndcX = clip.x / clip.w;
            float ndcY = clip.y / clip.w;
            return (float) Math.hypot((ndcX - centerNdcX) * 0.5F * screenWidth,
                    (ndcY - centerNdcY) * 0.5F * screenHeight);
        }

        private static Vector4f createOffsetPoint(Vec3 cameraRelative, Camera camera, float radius) {
            return new Vector4f((float) (cameraRelative.x + camera.getLeftVector().x() * radius),
                    (float) (cameraRelative.y + camera.getLeftVector().y() * radius),
                    (float) (cameraRelative.z + camera.getLeftVector().z() * radius), 1.0F);
        }

        private static Vector4f createFrontPoint(Vec3 cameraRelative, Camera camera, float radius) {
            org.joml.Vector3f look = camera.getLookVector();
            return new Vector4f((float) (cameraRelative.x - look.x() * radius),
                    (float) (cameraRelative.y - look.y() * radius),
                    (float) (cameraRelative.z - look.z() * radius), 1.0F);
        }
    }
}
