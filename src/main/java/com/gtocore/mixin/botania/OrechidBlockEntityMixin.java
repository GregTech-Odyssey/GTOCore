package com.gtocore.mixin.botania;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.botania.common.block.flower.functional.OrechidBlockEntity;

@Mixin(OrechidBlockEntity.class)
public final class OrechidBlockEntityMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public int getCost() {
        return 100;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public int getDelay() {
        return 2;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public int getRange() {
        return 3;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public int getRangeY() {
        return 1;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public int getMaxMana() {
        return 500;
    }
}
