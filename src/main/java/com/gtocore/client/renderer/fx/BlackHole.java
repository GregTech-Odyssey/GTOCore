package com.gtocore.client.renderer.fx;

import com.gtocore.client.renderer.GTORenderTypes;
import com.gtocore.client.renderer.RenderHelper;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import org.joml.Matrix4f;

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
    private static final float DISTORTION_STRENGTH = 0.12F;
    private static final float CORE_MASK_INSET_MIN = 1.5F;
    private static final float CORE_MASK_INSET_MAX = 6.0F;
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

        ScreenSpaceProjection.Sphere screenSphere = ScreenSpaceProjection.projectSphere(this.center, core, eventHorizon, camera, poseStack, projectionMatrix);
        if (screenSphere == null) {
            return;
        }

        var sceneTarget = ScreenSpaceSceneCapture.capture(levelRenderer);
        renderEventHorizon(worldStack, eventHorizon, screenSphere, sceneTarget);
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

    private static void renderCore(PoseStack poseStack, float coreRadius, ScreenSpaceProjection.Sphere screenSphere) {
        ScreenSpaceMeshRenderer.renderScaled(poseStack, pickSphereMesh(CORE_MESHES, screenSphere.innerRadius()).getBuffer(),
                GTORenderTypes.BLACK_HOLE_CORE, GameRenderer.getPositionColorShader(),
                coreRadius, coreRadius, coreRadius,
                0.0F, 0.0F, 0.0F, 1.0F);
    }

    private static void renderEventHorizon(PoseStack poseStack, float eventHorizonRadius, ScreenSpaceProjection.Sphere screenSphere,
                                           com.mojang.blaze3d.pipeline.TextureTarget sceneTarget) {
        ShaderInstance shader = GTORenderTypes.getBlackHoleEventHorizonShader();
        if (shader == null || sceneTarget == null) {
            return;
        }

        shader.setSampler("DiffuseSampler", sceneTarget.getColorTextureId());
        shader.safeGetUniform("BlackHoleCenterScreen").set(screenSphere.centerX(), screenSphere.centerY());
        shader.safeGetUniform("BlackHoleRadiusScreen").set(getCoreMaskRadius(screenSphere));
        shader.safeGetUniform("EventHorizonRadiusScreen").set(screenSphere.outerRadius());
        shader.safeGetUniform("DistortionStrength").set(DISTORTION_STRENGTH);
        shader.safeGetUniform("ScreenSize").set((float) sceneTarget.viewWidth, (float) sceneTarget.viewHeight);

        ScreenSpaceMeshRenderer.renderScaled(poseStack, pickSphereMesh(EVENT_HORIZON_MESHES, screenSphere.outerRadius()).getBuffer(),
                GTORenderTypes.BLACK_HOLE_EVENT_HORIZON, shader,
                eventHorizonRadius, eventHorizonRadius, eventHorizonRadius,
                1.0F, 1.0F, 1.0F, 1.0F);
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

    private static float getCoreMaskRadius(ScreenSpaceProjection.Sphere screenSphere) {
        float inset = Mth.clamp(screenSphere.innerRadius() * 0.01F, CORE_MASK_INSET_MIN, CORE_MASK_INSET_MAX);
        return Math.max(screenSphere.innerRadius() - inset, MIN_VISIBLE_RADIUS);
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
}
