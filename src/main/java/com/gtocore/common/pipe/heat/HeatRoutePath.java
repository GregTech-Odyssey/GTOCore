package com.gtocore.common.pipe.heat;

import com.gtocore.common.blockentity.HeatPipeBlockEntity;

import com.gtolib.api.capability.IHeatContainer;

import com.gregtechceu.gtceu.api.capability.*;
import com.gregtechceu.gtceu.api.pipenet.IRoutePath;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class HeatRoutePath implements IRoutePath<IHeatContainer> {

    private final HeatPipeBlockEntity targetPipe;
    private final Direction targetFacing;
    @Getter
    private final int distance;

    public HeatRoutePath(HeatPipeBlockEntity targetPipe, Direction targetFacing, int distance) {
        this.targetPipe = targetPipe;
        this.targetFacing = targetFacing;
        this.distance = distance;
    }

    @Override
    @NotNull
    public BlockPos getTargetPipePos() {
        return targetPipe.getPipePos();
    }

    @Nullable
    @Override
    public IHeatContainer getHandler(Level world) {
        return GTCapabilityHelper.getBlockEntityGTCapability(IHeatContainer.class, targetPipe.getNeighborBlockEntity(targetFacing), targetFacing.getOpposite());
    }

    public @NotNull Direction getTargetFacing() {
        return this.targetFacing;
    }
}
