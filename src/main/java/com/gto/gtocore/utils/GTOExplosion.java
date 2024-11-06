package com.gto.gtocore.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import lombok.Setter;

public class GTOExplosion extends Explosion {

    private final BlockPos center;
    private final Level level;
    private final int radius;
    @Setter
    private boolean isBreakBedrock;

    public GTOExplosion(BlockPos center, Level level, int radius) {
        super(level, null, null, null, center.getX(), center.getY(), center.getZ(), radius, false, BlockInteraction.DESTROY);
        this.center = center;
        this.level = level;
        this.radius = radius;
    }

    @Override
    public void finalizeExplosion(boolean spawnParticles) {
        int radiusSquared = (radius - 2) * (radius - 2);
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z <= radiusSquared) {
                        BlockPos pos = center.offset(x, y, z);
                        BlockState state = level.getBlockState(pos);
                        if (state.isAir() || (!isBreakBedrock && state.getBlock() == Blocks.BEDROCK)) continue;
                        state.onBlockExploded(level, pos, this);
                    }
                }
            }
        }
        super.explode();
        super.finalizeExplosion(spawnParticles);
    }
}
