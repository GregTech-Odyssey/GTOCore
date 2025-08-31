package com.gtocore.client.renderer.machine;

import com.gtocore.common.machine.multiblock.electric.miner.DigitalMiner;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

import com.lowdragmc.lowdraglib.client.utils.RenderBufferUtils;
import com.mojang.blaze3d.vertex.PoseStack;

public class DigitalMinerRenderer extends WorkableCasingMachineRenderer {

    public DigitalMinerRenderer() {
        super(GTCEu.id("block/casings/solid/machine_casing_solid_steel"), GTOCore.id("block/multiblock/general0"));
    }

    @Override
    public boolean hasTESR(BlockEntity blockEntity) {
        return true;
    }

    private static final float OFFSET = 0.02F;

    @Override
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack stack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        super.render(blockEntity, partialTicks, stack, buffer, combinedLight, combinedOverlay);
        if (blockEntity instanceof MetaMachineBlockEntity mbe && mbe.getMetaMachine() instanceof DigitalMiner miner) {
            stack.pushPose();
            stack.translate(-miner.getPos().getX(), -miner.getPos().getY(), -miner.getPos().getZ());
            var aabb = miner.getMinerArea().inflate(-OFFSET);
            RenderBufferUtils.renderCubeFace(stack, buffer.getBuffer(RenderType.debugQuads()),
                    (float) aabb.minX, (float) aabb.minY, (float) aabb.minZ,
                    (float) aabb.maxX, (float) aabb.maxY + 1, (float) aabb.maxZ,
                    1, 1, 1, 0.32F, false);
            RenderBufferUtils.drawCubeFrame(stack, buffer.getBuffer(RenderType.lines()),
                    (float) aabb.minX, (float) aabb.minY, (float) aabb.minZ,
                    (float) aabb.maxX, (float) aabb.maxY + 1, (float) aabb.maxZ,
                    0, 1, 0, 0.8F);
            stack.popPose();
        }
    }

    @Override
    public boolean shouldRender(BlockEntity blockEntity, Vec3 cameraPos) {
        return blockEntity instanceof MetaMachineBlockEntity mbe &&
                mbe.getMetaMachine() instanceof DigitalMiner miner &&
                miner.isFormed() &&
                miner.isShowRange();
    }

    @Override
    public boolean isGlobalRenderer(BlockEntity blockEntity) {
        return true;
    }
}
