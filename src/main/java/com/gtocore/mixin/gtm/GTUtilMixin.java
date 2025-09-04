package com.gtocore.mixin.gtm;

import com.gtolib.utils.GTOUtils;

import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GTUtil.class)
public final class GTUtilMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static boolean canSeeSunClearly(Level world, BlockPos blockPos) {
        return GTOUtils.canSeeSunClearly(world, blockPos);
    }
}
