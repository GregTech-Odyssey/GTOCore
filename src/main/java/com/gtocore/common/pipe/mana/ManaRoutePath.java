package com.gtocore.common.pipe.mana;

import com.gtocore.common.blockentity.ManaPipeBlockEntity;

import com.gregtechceu.gtceu.api.pipenet.IRoutePath;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.mana.ManaReceiver;

public final class ManaRoutePath implements IRoutePath<ManaReceiver> {

    @Getter
    private final ManaPipeBlockEntity targetPipe;
    private final Direction targetFacing;
    @Getter
    private final int distance;

    public ManaRoutePath(ManaPipeBlockEntity targetPipe, Direction targetFacing, int distance) {
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
    public ManaReceiver getHandler(Level world) {
        var blockEntity = targetPipe.getNeighborBlockEntity(targetFacing);
        if (blockEntity == null) return null;
        return blockEntity.getCapability(BotaniaForgeCapabilities.MANA_RECEIVER, targetFacing.getOpposite()).orElse(null);
    }

    public @NotNull Direction getTargetFacing() {
        return this.targetFacing;
    }
}
