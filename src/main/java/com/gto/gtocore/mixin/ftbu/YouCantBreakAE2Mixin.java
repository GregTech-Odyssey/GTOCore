package com.gto.gtocore.mixin.ftbu;

import appeng.block.AEBaseBlock;
import dev.ftb.mods.ftbultimine.FTBUltiminePlayerData;
import dev.ftb.mods.ftbultimine.shape.BlockMatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * @author EasterFG on 2024/10/10
 */
@Mixin(FTBUltiminePlayerData.class)
public class YouCantBreakAE2Mixin {

    @ModifyVariable(method = "updateBlocks", at = @At(value = "STORE", ordinal = 2), remap = false)
    public BlockMatcher modifyBlockMatcher(BlockMatcher matcher) {
        return (original, state) -> !(state.getBlock() instanceof AEBaseBlock) && original.getBlock() == state.getBlock();
    }
}
