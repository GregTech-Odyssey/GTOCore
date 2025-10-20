package com.gtocore.mixin.eae;

import com.gtolib.api.blockentity.IDirectionCacheBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import com.glodblock.github.extendedae.common.blocks.BlockBaseGui;
import com.glodblock.github.extendedae.common.blocks.BlockExIOPort;
import com.glodblock.github.extendedae.common.tileentities.TileExIOPort;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockExIOPort.class)
public abstract class BlockExIOPortMixin extends BlockBaseGui<TileExIOPort> {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos, boolean isMoving) {
        TileExIOPort te = this.getBlockEntity(level, pos);
        if (te != null) {
            IDirectionCacheBlockEntity.getBlockEntityDirectionCache(te).clearCache();
            te.updateRedstoneState();
        }
    }
}
