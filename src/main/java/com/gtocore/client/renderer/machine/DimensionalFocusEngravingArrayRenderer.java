package com.gtocore.client.renderer.machine;

import com.gtocore.client.renderer.RenderHelper;

import com.gtolib.api.machine.multiblock.CoilCrossRecipeMultiblockMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.client.renderer.GTRenderTypes;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;
import com.gregtechceu.gtceu.client.util.BloomUtils;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.utils.ColorUtils;
import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import com.lowdragmc.lowdraglib.utils.interpolate.Eases;
import com.lowdragmc.shimmer.client.shader.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import java.util.Objects;

public final class DimensionalFocusEngravingArrayRenderer extends WorkableCasingMachineRenderer {

    public DimensionalFocusEngravingArrayRenderer() {
        super(GTCEu.id("block/casings/gcym/laser_safe_engraving_casing"), GTCEu.id("block/multiblock/fusion_reactor"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (blockEntity instanceof MetaMachineBlockEntity machineBlockEntity && machineBlockEntity.getMetaMachine() instanceof CoilCrossRecipeMultiblockMachine machine && machine.isFormed() && (machine.getRecipeLogic().isWorking() || blockEntity.getLevel() instanceof TrackedDummyWorld)) {
            if (GTCEu.Mods.isShimmerLoaded() && !(blockEntity.getLevel() instanceof TrackedDummyWorld)) {
                PoseStack finalStack = RenderUtils.copyPoseStack(stack);
                BloomUtils.entityBloom(source -> renderLightRing(machine, partialTicks, finalStack, source));
            } else {
                renderLightRing(machine, partialTicks, stack, buffer);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void renderLightRing(CoilCrossRecipeMultiblockMachine machine, float partialTicks, PoseStack stack, MultiBufferSource buffer) {
        float x = 0.5F, y = -17.5F, z = 0.5F;
        switch (machine.getFrontFacing()) {
            case NORTH -> z = 22.5F;
            case SOUTH -> z = -21.5F;
            case WEST -> x = 22.5F;
            case EAST -> x = -21.5F;
        }
        stack.translate(x, y, z);
        int color = ColorUtils.blendColor(Objects.requireNonNull(machine.getCoilType().getMaterial()).getMaterialRGB(), -1, 0.1F + Eases.EaseQuadIn.getInterpolation(Math.abs((Math.abs(machine.getOffsetTimer() % 100) + partialTicks) - 50) / 50));
        float a = ColorUtils.alpha(color);
        float r = ColorUtils.red(color);
        float g = ColorUtils.green(color);
        float b = ColorUtils.blue(color);
        a(stack, buffer, a, r, g, b);
        stack.mulPose(Axis.XN.rotationDegrees(90));
        a(stack, buffer, a, r, g, b);
        stack.mulPose(Axis.ZN.rotationDegrees(90));
        RenderHelper.renderCylinder(stack, buffer.getBuffer(GTRenderTypes.getLightRing()),
                0, 0, 0, 0.5F, 8, 10, r, g, b, a);
        stack.mulPose(Axis.ZN.rotationDegrees(180));
        RenderHelper.renderCylinder(stack, buffer.getBuffer(GTRenderTypes.getLightRing()),
                0, 0, 0, 0.5F, 8, 10, r, g, b, a);
    }

    @OnlyIn(Dist.CLIENT)
    private static void a(PoseStack stack, MultiBufferSource buffer, float a, float r, float g, float b) {
        RenderHelper.renderCylinder(stack, buffer.getBuffer(GTRenderTypes.getLightRing()), 0, 0, 0, 0.5F, 8, 10, r, g, b, a);
        stack.mulPose(Axis.XN.rotationDegrees(90));
        RenderHelper.renderCylinder(stack, buffer.getBuffer(GTRenderTypes.getLightRing()), 0, 0, 0, 0.5F, 8, 10, r, g, b, a);
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
}
