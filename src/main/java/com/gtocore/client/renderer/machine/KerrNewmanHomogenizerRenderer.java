package com.gtocore.client.renderer.machine;

import com.gtocore.client.renderer.GTORenderTypes;

import com.gtolib.GTOCore;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;
import com.gregtechceu.gtceu.client.util.BloomUtils;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import com.lowdragmc.shimmer.client.shader.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

// 从GTNL特效修改而来，协议: LGPLv3
public final class KerrNewmanHomogenizerRenderer extends WorkableCasingMachineRenderer {

    private static final int RING_SEGMENTS = 64;
    private static final int RING_SIDES = 16;

    public KerrNewmanHomogenizerRenderer() {
        super(GTOCore.id("block/casings/dimension_injection_casing"), GTCEu.id("block/multiblock/fusion_reactor"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer,
                       int combinedLight, int combinedOverlay) {
        if (blockEntity instanceof MetaMachineBlockEntity machineBlockEntity &&
                machineBlockEntity.getMetaMachine() instanceof ElectricMultiblockMachine machine &&
                machine.isFormed() &&
                (machine.isActive() || blockEntity.getLevel() instanceof TrackedDummyWorld)) {
            if (GTCEu.Mods.isShimmerLoaded() && !(blockEntity.getLevel() instanceof TrackedDummyWorld)) {
                PoseStack finalStack = RenderUtils.copyPoseStack(poseStack);
                BloomUtils.entityBloom(source -> renderBlackHole(machine, partialTicks, finalStack, source));
            } else {
                renderBlackHole(machine, partialTicks, poseStack, buffer);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void renderBlackHole(ElectricMultiblockMachine machine, float partialTicks, PoseStack poseStack,
                                        MultiBufferSource buffer) {
        float tick = machine.getOffsetTimer() + partialTicks;
        var back = machine.getFrontFacing().getOpposite();
        var up = RelativeDirection.UP.getRelative(machine.getFrontFacing(), machine.getUpwardsFacing(),
                machine.isFlipped());

        poseStack.pushPose();
        poseStack.translate(
                0.5 + 34 * back.getStepX(),
                0.5 + 34 * back.getStepY(),
                0.5 + 34 * back.getStepZ());

        alignToDirection(poseStack, back, up);

        VertexConsumer light = buffer.getBuffer(GTORenderTypes.LIGHT_TRIANGLES);

        float rotation = tick * 1.2F;
        poseStack.mulPose(new Quaternionf().fromAxisAngleDeg(1.0F, 1.0F, 1.0F, rotation));
        renderRainbowRing(poseStack, light, 20.0F, 0.9F, RING_SEGMENTS, RING_SIDES, 0.8F);

        poseStack.mulPose(Axis.ZP.rotationDegrees(rotation));
        renderRainbowRing(poseStack, light, 24.0F, 0.9F, RING_SEGMENTS, RING_SIDES, 0.8F);

        poseStack.mulPose(Axis.ZN.rotationDegrees(rotation));
        poseStack.mulPose(Axis.XP.rotationDegrees(rotation));
        renderRainbowRing(poseStack, light, 28.0F, 0.9F, RING_SEGMENTS, RING_SIDES, 0.8F);

        poseStack.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    private static void alignToDirection(PoseStack poseStack, net.minecraft.core.Direction back,
                                         net.minecraft.core.Direction up) {
        switch (back) {
            case NORTH -> poseStack.mulPose(Axis.YP.rotationDegrees(180));
            case EAST -> poseStack.mulPose(Axis.YP.rotationDegrees(90));
            case WEST -> poseStack.mulPose(Axis.YP.rotationDegrees(-90));
            case UP -> poseStack.mulPose(Axis.XP.rotationDegrees(-90));
            case DOWN -> poseStack.mulPose(Axis.XP.rotationDegrees(90));
            default -> {}
        }

        if (back.getAxis() != net.minecraft.core.Direction.Axis.Y) {
            if (up == net.minecraft.core.Direction.DOWN) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(180));
            } else if (up == back.getClockWise()) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(90));
            } else if (up == back.getCounterClockWise()) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(-90));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void renderRainbowRing(PoseStack poseStack, VertexConsumer buffer, float radius, float tubeRadius,
                                          int segments, int sides, float alpha) {
        Matrix4f matrix = poseStack.last().pose();
        for (int segment = 0; segment < segments; segment++) {
            float theta0 = Mth.TWO_PI * segment / segments;
            float theta1 = Mth.TWO_PI * (segment + 1) / segments;
            float hue = segment / (float) segments;
            float red = Mth.sin(hue * Mth.TWO_PI) * 0.4F + 0.6F;
            float green = Mth.sin((hue + 0.33F) * Mth.TWO_PI) * 0.4F + 0.6F;
            float blue = Mth.sin((hue + 0.66F) * Mth.TWO_PI) * 0.4F + 0.6F;

            for (int side = 0; side < sides; side++) {
                float phi0 = Mth.TWO_PI * side / sides;
                float phi1 = Mth.TWO_PI * (side + 1) / sides;

                ringVertex(buffer, matrix, radius, tubeRadius, theta0, phi0, red, green, blue, alpha);
                ringVertex(buffer, matrix, radius, tubeRadius, theta1, phi0, red, green, blue, alpha);
                ringVertex(buffer, matrix, radius, tubeRadius, theta1, phi1, red, green, blue, alpha);

                ringVertex(buffer, matrix, radius, tubeRadius, theta0, phi0, red, green, blue, alpha);
                ringVertex(buffer, matrix, radius, tubeRadius, theta1, phi1, red, green, blue, alpha);
                ringVertex(buffer, matrix, radius, tubeRadius, theta0, phi1, red, green, blue, alpha);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void ringVertex(VertexConsumer buffer, Matrix4f matrix, float radius, float tubeRadius,
                                   float theta, float phi, float red, float green, float blue, float alpha) {
        float ringRadius = radius + tubeRadius * Mth.cos(phi);
        buffer.vertex(matrix,
                ringRadius * Mth.cos(theta),
                tubeRadius * Mth.sin(phi),
                ringRadius * Mth.sin(theta))
                .color(red, green, blue, alpha)
                .endVertex();
    }

    @OnlyIn(Dist.CLIENT)
    private static void renderSphere(PoseStack poseStack, VertexConsumer buffer, float radius, int segments, int rings,
                                     float red, float green, float blue, float alpha) {
        Matrix4f matrix = poseStack.last().pose();
        for (int ring = 0; ring < rings; ring++) {
            float phi0 = (float) Math.PI * ring / rings;
            float phi1 = (float) Math.PI * (ring + 1) / rings;
            for (int segment = 0; segment < segments; segment++) {
                float theta0 = Mth.TWO_PI * segment / segments;
                float theta1 = Mth.TWO_PI * (segment + 1) / segments;

                vertex(buffer, matrix, radius, phi0, theta0, red, green, blue, alpha);
                vertex(buffer, matrix, radius, phi1, theta0, red, green, blue, alpha);
                vertex(buffer, matrix, radius, phi1, theta1, red, green, blue, alpha);

                vertex(buffer, matrix, radius, phi0, theta0, red, green, blue, alpha);
                vertex(buffer, matrix, radius, phi1, theta1, red, green, blue, alpha);
                vertex(buffer, matrix, radius, phi0, theta1, red, green, blue, alpha);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void vertex(VertexConsumer buffer, Matrix4f matrix, float radius, float phi, float theta,
                               float red, float green, float blue, float alpha) {
        float sinPhi = Mth.sin(phi);
        buffer.vertex(matrix,
                radius * sinPhi * Mth.cos(theta),
                radius * Mth.cos(phi),
                radius * sinPhi * Mth.sin(theta))
                .color(red, green, blue, alpha)
                .endVertex();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasTESR(BlockEntity blockEntity) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isGlobalRenderer(BlockEntity blockEntity) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getViewDistance() {
        return 128;
    }
}
