package com.gtocore.mixin.eae;

import com.gtolib.api.blockentity.IDirectionCacheBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import com.glodblock.github.extendedae.common.blocks.BlockBaseGui;
import com.glodblock.github.extendedae.common.blocks.BlockExPatternProvider;
import com.glodblock.github.extendedae.common.tileentities.TileExPatternProvider;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockExPatternProvider.class)
public abstract class BlockExPatternProviderMixin extends BlockBaseGui<TileExPatternProvider> {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos, boolean isMoving) {
        TileExPatternProvider be = this.getBlockEntity(level, pos);
        if (be != null) {
            IDirectionCacheBlockEntity.getBlockEntityDirectionCache(be).clearCache();
            be.getLogic().updateRedstoneState();
        }
    }
}
