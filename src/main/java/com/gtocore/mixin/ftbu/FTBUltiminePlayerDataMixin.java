package com.gtocore.mixin.ftbu;

import com.gtocore.config.GTOConfig;

import com.gtolib.utils.ItemUtils;
import com.gtolib.utils.StringUtils;

import dev.ftb.mods.ftbultimine.FTBUltiminePlayerData;
import dev.ftb.mods.ftbultimine.shape.BlockMatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FTBUltiminePlayerData.class)
public class FTBUltiminePlayerDataMixin {

    @ModifyVariable(method = "updateBlocks", at = @At(value = "STORE", ordinal = 2), remap = false)
    private BlockMatcher modifyBlockMatcher(BlockMatcher matcher) {
        if (GTOConfig.INSTANCE.breakBlocksBlackList == null || GTOConfig.INSTANCE.breakBlocksBlackList.length < 1) {
            return matcher;
        }
        return (original, state) -> {
            boolean flag = !StringUtils.containsWithWildcard(GTOConfig.INSTANCE.breakBlocksBlackList,
                    ItemUtils.getId(state.getBlock()));
            return flag && state.getBlock() == original.getBlock();
        };
    }
}
