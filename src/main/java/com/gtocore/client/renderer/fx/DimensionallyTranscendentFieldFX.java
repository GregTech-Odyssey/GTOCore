package com.gtocore.client.renderer.fx;

import com.gtocore.client.renderer.GTORenderTypes;
import com.gtocore.client.renderer.RenderHelper;
import com.gtocore.common.machine.multiblock.electric.smelter.DimensionallyTranscendentPlasmaForgeMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import org.joml.Matrix4f;

public class DimensionallyTranscendentFieldFX extends AbstractFX {

    private static final int STALE_TICKS = 3;
    private static final float MIN_PATTERN_RADIUS_PIXELS = 36.0F;
    private static final float PATTERN_RADIUS_MULTIPLIER = 1.6F;

    private static final CylinderMesh INNER_MESH = new CylinderMesh(20);
    private static final CylinderMesh OUTER_MESH = new CylinderMesh(40);

    private final ResourceKey<Level> dimension;
    private final BlockPos machinePos;
    private Vec3 baseCenter;
    private float radius;
    private float height;
    private int lastRefreshAge;

    public DimensionallyTranscendentFieldFX(ResourceKey<Level> dimension, BlockPos machinePos, Vec3 baseCenter, float radius, float height) {
        this.dimension = dimension;
        this.machinePos = machinePos.immutable();
        refresh(baseCenter, radius, height);
    }

    public void refresh(Vec3 baseCenter, float radius, float height) {
        this.baseCenter = baseCenter;
        this.radius = radius;
        this.height = height;
        this.lastRefreshAge = age;
    }

    @Override
    public boolean shouldDiscard() {
        if (age - lastRefreshAge > STALE_TICKS) {
            return true;
        }

        ClientLevel level = Minecraft.getInstance().level;
        if (level == null || !level.dimension().equals(dimension)) {
            return true;
        }
        if (!(level.getBlockEntity(machinePos) instanceof MetaMachineBlockEntity machineBlockEntity)) {
            return true;
        }
        MetaMachine metaMachine = machineBlockEntity.getMetaMachine();
        return !(metaMachine instanceof DimensionallyTranscendentPlasmaForgeMachine machine) || !machine.isFormed();
    }

    @Override
    public void render(RenderLevelStageEvent.Stage stage, LevelRenderer levelRenderer, PoseStack poseStack, Matrix4f projectionMatrix, float partialTick, Camera camera, Frustum frustum) {
        if (stage != RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS || baseCenter == null) {
            return;
        }

        ShaderInstance shader = GTORenderTypes.getDimensionallyTranscendentOverlayShader();
        if (shader == null) {
            return;
        }

        var sceneTarget = ScreenSpaceSceneCapture.capture(levelRenderer);
        if (sceneTarget == null) {
            return;
        }

        renderLayer(poseStack, projectionMatrix, partialTick, camera, sceneTarget, shader, INNER_MESH, radius, 1.0F, 1.0F);
        renderLayer(poseStack, projectionMatrix, partialTick, camera, sceneTarget, shader, OUTER_MESH, radius * 3.2F, 0.42F, 0.62F);
    }

    private void renderLayer(PoseStack poseStack, Matrix4f projectionMatrix, float partialTick, Camera camera,
                             com.mojang.blaze3d.pipeline.TextureTarget sceneTarget, ShaderInstance shader,
                             CylinderMesh mesh, float layerRadius, float alpha, float strength) {
        ScreenSpaceProjection.Cylinder screenCylinder = ScreenSpaceProjection.projectCylinder(baseCenter, layerRadius, height, camera, poseStack, projectionMatrix);
        if (screenCylinder == null) {
            return;
        }

        shader.setSampler("DiffuseSampler", sceneTarget.getColorTextureId());
        shader.safeGetUniform("ScreenSize").set((float) sceneTarget.viewWidth, (float) sceneTarget.viewHeight);
        shader.safeGetUniform("EffectCenterScreen").set(screenCylinder.centerX(), screenCylinder.centerY());
        shader.safeGetUniform("EffectRadiusScreen").set(screenCylinder.radius());
        shader.safeGetUniform("PatternRadiusScreen").set(Mth.clamp(screenCylinder.patternRadius() * PATTERN_RADIUS_MULTIPLIER, MIN_PATTERN_RADIUS_PIXELS, screenCylinder.radius()));
        ClientLevel level = Minecraft.getInstance().level;
        shader.safeGetUniform("Time").set((level == null ? 0.0F : level.getGameTime() + partialTick) / 60.0F);
        shader.safeGetUniform("OverlayStrength").set(strength);

        PoseStack worldStack = new PoseStack();
        worldStack.mulPoseMatrix(poseStack.last().pose());
        Vec3 cameraPos = camera.getPosition();
        worldStack.translate(baseCenter.x - cameraPos.x, baseCenter.y - cameraPos.y, baseCenter.z - cameraPos.z);
        ScreenSpaceMeshRenderer.renderScaled(worldStack, mesh.getBuffer(),
                GTORenderTypes.DIMENSIONALLY_TRANSCENDENT_OVERLAY, shader,
                layerRadius, height, layerRadius,
                1.0F, 1.0F, 1.0F, alpha);
    }

    public record Key(ResourceKey<Level> dimension, BlockPos machinePos) {

        public Key {
            machinePos = machinePos.immutable();
        }
    }

    private static final class CylinderMesh {

        private final int sides;
        private VertexBuffer buffer;

        private CylinderMesh(int sides) {
            this.sides = sides;
        }

        private VertexBuffer getBuffer() {
            if (buffer == null) {
                buffer = RenderHelper.buildUnitCylinderBuffer(sides);
            }
            return buffer;
        }
    }
}
