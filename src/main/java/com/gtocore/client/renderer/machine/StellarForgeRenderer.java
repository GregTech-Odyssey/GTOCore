package com.gtocore.client.renderer.machine;

import com.gtocore.client.renderer.fx.FXManager;
import com.gtocore.client.renderer.fx.StellarForgeVortexFX;
import com.gtocore.common.machine.multiblock.electric.StellarForgeMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.client.renderer.machine.WorkableCasingMachineRenderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import com.lowdragmc.lowdraglib.utils.TrackedDummyWorld;
import com.mojang.blaze3d.vertex.PoseStack;

public class StellarForgeRenderer extends WorkableCasingMachineRenderer {

    private static final int offD = -2;
    private static final int offU = 31;
    private static final double angle = Math.PI / 8D;
    private static final double decayRadius = 32D;
    private static final double disappearRadius = 48D;

    public StellarForgeRenderer() {
        super(GTCEu.id("block/casings/gcym/atomic_casing"), GTCEu.id("block/multiblock/fusion_reactor"));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void render(BlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (blockEntity instanceof MetaMachineBlockEntity machineBlockEntity) {
            MetaMachine metaMachine = machineBlockEntity.getMetaMachine();
            if (metaMachine instanceof StellarForgeMachine machine && (machine.isActive() && machine.isFormed() || blockEntity.getLevel() instanceof TrackedDummyWorld)) {
                var center = RelativeDirection.offsetPos(BlockPos.ZERO, machine.getFrontFacing(), machine.getUpwardsFacing(), machine.isFlipped(), offU, 0, offD);
                var level = blockEntity.getLevel();
                if (level != null && !(level instanceof TrackedDummyWorld)) {
                    Vec3 vortexCenter = Vec3.atCenterOf(blockEntity.getBlockPos().offset(center));
                    StellarForgeVortexFX.Key key = new StellarForgeVortexFX.Key(level.dimension(), blockEntity.getBlockPos());
                    FXManager.upsertFX(key,
                            () -> new StellarForgeVortexFX(key.dimension(), key.machinePos(), vortexCenter, machine.getFrontFacing(),
                                    (float) angle, (float) decayRadius, (float) disappearRadius),
                            fx -> fx.refresh(vortexCenter, machine.getFrontFacing(), (float) angle, (float) decayRadius, (float) disappearRadius));
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
            return (metaMachine instanceof StellarForgeMachine machine && machine.isFormed() ||
                    blockEntity.getLevel() instanceof TrackedDummyWorld) ||
                    super.shouldRender(blockEntity, cameraPos);
        }
        return super.shouldRender(blockEntity, cameraPos);
    }
}
