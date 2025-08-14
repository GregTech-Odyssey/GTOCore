package com.gtocore.client.renderer.machine;

import com.gtocore.common.machine.multiblock.electric.AdvancedFusionReactorMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.client.renderer.GTRenderTypes;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;
import com.gregtechceu.gtceu.client.util.BloomUtils;
import com.gregtechceu.gtceu.client.util.RenderBufferHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.shimmer.client.shader.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;

import static net.minecraft.util.FastColor.ARGB32.*;

public final class AdvancedFusionReactorRenderer extends WorkableCasingMachineRenderer {

    private float delta = 0;
    private int lastColor = -1;

    public AdvancedFusionReactorRenderer(ResourceLocation baseCasing, ResourceLocation workableModel) {
        super(baseCasing, workableModel);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack stack, MultiBufferSource buffer,
                       int combinedLight, int combinedOverlay) {
        if (blockEntity instanceof MetaMachineBlockEntity machineBlockEntity && machineBlockEntity.getMetaMachine() instanceof AdvancedFusionReactorMachine machine) {
            if (!machine.recipeLogic.isWorking() && delta <= 0) {
                return;
            }
            if (GTCEu.Mods.isShimmerLoaded()) {
                PoseStack finalStack = RenderUtils.copyPoseStack(stack);
                BloomUtils.entityBloom(source -> renderLightRing(machine, partialTicks, finalStack, source));
            } else {
                renderLightRing(machine, partialTicks, stack, buffer);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void renderLightRing(AdvancedFusionReactorMachine machine, float partialTicks, PoseStack poseStack, MultiBufferSource buffer) {
        double x = 0.5, y = 6.75, z = 0.5;
        switch (machine.getFrontFacing()) {
            case NORTH -> z = 19.5;
            case SOUTH -> z = -18.5;
            case WEST -> x = 19.5;
            case EAST -> x = -18.5;
        }
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        var color = machine.getColor();
        var alpha = 1f;
        if (machine.recipeLogic.isWorking()) {
            lastColor = color;
            delta = 60;
        } else {
            alpha = delta / 60;
            lastColor = color(Mth.floor(alpha * 255), red(lastColor), green(lastColor), blue(lastColor));
            delta -= Minecraft.getInstance().getDeltaFrameTime();
        }

        var lerpFactor = Math.abs((Math.abs(machine.getOffsetTimer() % 50) + partialTicks) - 25) / 25;
        var front = machine.getFrontFacing();
        var upwards = machine.getUpwardsFacing();
        var flipped = machine.isFlipped();
        var axis = RelativeDirection.UP.getRelative(front, upwards, flipped).getAxis();
        var r = Mth.lerp(lerpFactor, red(lastColor), 255) / 255f;
        var g = Mth.lerp(lerpFactor, green(lastColor), 255) / 255f;
        var b = Mth.lerp(lerpFactor, blue(lastColor), 255) / 255f;
        RenderBufferHelper.renderRing(poseStack, buffer.getBuffer(GTRenderTypes.getLightRing()), 0, 0, 0, 8, 1.2F, 10, 20, r, g, b, alpha, axis);
        poseStack.popPose();
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
        return 32;
    }
}
