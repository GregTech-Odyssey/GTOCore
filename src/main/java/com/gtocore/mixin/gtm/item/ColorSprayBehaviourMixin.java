package com.gtocore.mixin.gtm.item;

import com.gtocore.common.block.ColorBlockMap;
import com.gtocore.common.data.GTOBlocks;

import com.gregtechceu.gtceu.common.item.ColorSprayBehaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ColorSprayBehaviour.class)
public class ColorSprayBehaviourMixin {

    @Shadow(remap = false)
    @Final
    private DyeColor color;

    @Shadow(remap = false)
    private static boolean recolorBlockNoState(Map<DyeColor, Block> map, DyeColor color, Level world, BlockPos pos, Block _default) {
        return false;
    }

    @Inject(method = "tryPaintSpecialBlock", at = @At("HEAD"), remap = false, cancellable = true)
    private void tryPaintSpecialBlock(Level world, BlockPos pos, Block block, CallbackInfoReturnable<Boolean> cir) {
        if (ColorBlockMap.ABS_MAP.containsValue(block)) {
            if (recolorBlockNoState(ColorBlockMap.ABS_MAP, color, world, pos, GTOBlocks.ABS_WHITE_CASING.get())) {
                cir.setReturnValue(true);
            }
        }
    }
}
