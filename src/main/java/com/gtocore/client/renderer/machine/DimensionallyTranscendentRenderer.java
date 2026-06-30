package com.gtocore.client.renderer.machine;

import com.gtocore.client.renderer.fx.DimensionallyTranscendentFieldFX;
import com.gtocore.client.renderer.fx.FXManager;
import com.gtocore.common.machine.multiblock.electric.smelter.DimensionallyTranscendentPlasmaForgeMachine;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import com.mojang.blaze3d.vertex.PoseStack;

public class DimensionallyTranscendentRenderer extends WorkableCasingMachineRenderer {

    private static final int offD = -7;
    private static final int offU = 7;
    private static final float height = 71;
    private static final float radius = (float) (1.5D * Math.sqrt(2));

    public DimensionallyTranscendentRenderer() {
        super(GTOCore.id("block/casings/dimensionally_transcendent_casing"), GTOCore.id("block/multiblock/dimensionally_transcendent_plasma_forge"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (blockEntity instanceof MetaMachineBlockEntity machineBlockEntity) {
            MetaMachine metaMachine = machineBlockEntity.getMetaMachine();
            if (metaMachine instanceof DimensionallyTranscendentPlasmaForgeMachine machine && (machine.isActive() && machine.isFormed() || blockEntity.getLevel() instanceof TrackedDummyWorld)) {
                var pos = BlockPos.ZERO.relative(machine.getFrontFacing(), offD).above(offU);
                var level = blockEntity.getLevel();
                if (level != null && !(level instanceof TrackedDummyWorld)) {
                    Vec3 baseCenter = Vec3.atCenterOf(blockEntity.getBlockPos().offset(pos));
                    DimensionallyTranscendentFieldFX.Key key = new DimensionallyTranscendentFieldFX.Key(level.dimension(), blockEntity.getBlockPos());
                    FXManager.upsertFX(key,
                            () -> new DimensionallyTranscendentFieldFX(key.dimension(), key.machinePos(), baseCenter, radius, height),
                            fx -> fx.refresh(baseCenter, radius, height));
                }
            }
        }
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
        return 256;
    }

    @Override
    public boolean shouldRender(BlockEntity blockEntity, Vec3 cameraPos) {
        if (blockEntity instanceof MetaMachineBlockEntity machineBlockEntity) {
            MetaMachine metaMachine = machineBlockEntity.getMetaMachine();
            return (metaMachine instanceof DimensionallyTranscendentPlasmaForgeMachine machine && machine.isFormed() ||
                    blockEntity.getLevel() instanceof TrackedDummyWorld) ||
                    super.shouldRender(blockEntity, cameraPos);
        }
        return super.shouldRender(blockEntity, cameraPos);
    }
}
