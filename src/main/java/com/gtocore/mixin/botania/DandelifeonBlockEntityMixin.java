package com.gtocore.mixin.botania;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import vazkii.botania.common.block.flower.generating.DandelifeonBlockEntity;

@Mixin(DandelifeonBlockEntity.class)
public final class DandelifeonBlockEntityMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public int getMaxMana() {
        return 1000000;
    }

    @ModifyConstant(
                    method = "setBlockForGeneration(Lnet/minecraft/core/BlockPos;II)V",
                    remap = false,
                    constant = @Constant(intValue = 60) // 匹配原 MANA_PER_GEN 的值
    )
    private int modifyManaPerGeneration(int original) {
        return 600; // 新值
    }
}
