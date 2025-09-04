package com.gtocore.mixin.ae2.blockentity;

import com.gtolib.api.blockentity.IDirectionCacheBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import appeng.block.AEBaseBlock;
import appeng.block.AEBaseEntityBlock;
import appeng.blockentity.AEBaseBlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AEBaseEntityBlock.class)
public abstract class AEBaseEntityBlockMixin<T extends AEBaseBlockEntity> extends AEBaseBlock {

    @Shadow(remap = false)
    public abstract @Nullable T getBlockEntity(BlockGetter level, BlockPos pos);

    protected AEBaseEntityBlockMixin(Properties props) {
        super(props);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide()) {
            var te = getBlockEntity(level, pos);
            if (te != null) {
                IDirectionCacheBlockEntity.getBlockEntityDirectionCache(te).clearCache();
            }
        }
    }
}
