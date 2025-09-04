package com.gtocore.mixin.ae2.blockentity;

import com.gtolib.api.blockentity.IDirectionCacheBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import appeng.block.AEBaseEntityBlock;
import appeng.block.networking.CableBusBlock;
import appeng.blockentity.networking.CableBusBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(CableBusBlock.class)
public abstract class CableBusBlockMixin extends AEBaseEntityBlock<CableBusBlockEntity> {

    protected CableBusBlockMixin(Properties props) {
        super(props);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!level.isClientSide()) {
            final BlockEntity te = level.getBlockEntity(pos);
            if (te instanceof CableBusBlockEntity cableBusBlockEntity) {
                IDirectionCacheBlockEntity.getBlockEntityDirectionCache(te).clearCache();
                cableBusBlockEntity.getCableBus().onNeighborChanged(level, pos, fromPos);
            }
        }
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        final BlockEntity te = level.getBlockEntity(pos);
        if (te instanceof CableBusBlockEntity cableBusBlockEntity) {
            IDirectionCacheBlockEntity.getBlockEntityDirectionCache(te).clearCache();
            cableBusBlockEntity.getCableBus().onNeighborChanged(level, pos, neighbor);
        }
    }
}
