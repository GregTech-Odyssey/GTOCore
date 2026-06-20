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
        poseStack.mulPose(new Quaternionf().fromAxisAngleDeg(1.0F, 0.35F, 0.15F, rotation * 0.35F));
        poseStack.mulPose(Axis.XP.rotationDegrees(68.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotation * 0.35F));
        renderAccretionDisk(poseStack, light, 5.8F, 12.0F, 96, 0.42F);
        renderAccretionDisk(poseStack, light, 12.0F, 15.0F, 96, 0.14F);

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
    private static void renderAccretionDisk(PoseStack poseStack, VertexConsumer buffer, float innerRadius,
                                            float outerRadius, int segments, float alpha) {
        Matrix4f matrix = poseStack.last().pose();
        for (int segment = 0; segment < segments; segment++) {
            float theta0 = Mth.TWO_PI * segment / segments;
            float theta1 = Mth.TWO_PI * (segment + 1) / segments;
            float thetaMid = (theta0 + theta1) * 0.5F;
            float doppler = Mth.sin(thetaMid + 0.7F) * 0.5F + 0.5F;
            float brightness = 0.45F + 0.55F * doppler;
            float red = 1.0F;
            float green = 0.38F + 0.42F * brightness;
            float blue = 0.10F + 0.18F * brightness;
            float segmentAlpha = alpha * (0.55F + 0.45F * brightness);

            diskVertex(buffer, matrix, innerRadius, theta0, red, green, blue, segmentAlpha * 0.68F);
            diskVertex(buffer, matrix, outerRadius, theta0, red, green, blue, segmentAlpha * 0.24F);
            diskVertex(buffer, matrix, outerRadius, theta1, red, green, blue, segmentAlpha * 0.24F);

            diskVertex(buffer, matrix, innerRadius, theta0, red, green, blue, segmentAlpha * 0.68F);
            diskVertex(buffer, matrix, outerRadius, theta1, red, green, blue, segmentAlpha * 0.24F);
            diskVertex(buffer, matrix, innerRadius, theta1, red, green, blue, segmentAlpha * 0.68F);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void diskVertex(VertexConsumer buffer, Matrix4f matrix, float radius, float theta,
                                   float red, float green, float blue, float alpha) {
        buffer.vertex(matrix,
                radius * Mth.cos(theta),
                0.0F,
                radius * Mth.sin(theta))
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
