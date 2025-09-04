package com.gtocore.mixin.botania;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.botania.common.block.flower.functional.OrechidIgnemBlockEntity;

@Mixin(OrechidIgnemBlockEntity.class)
public final class OrechidIgnemBlockEntityMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public int getCost() {
        return 120;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public boolean canOperate() {
        return true;
    }
}
