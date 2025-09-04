package com.gtocore.mixin.botania;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.botania.common.block.flower.functional.MarimorphosisBlockEntity;

@Mixin(MarimorphosisBlockEntity.class)
public final class MarimorphosisBlockEntityMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public int getCost() {
        return 25;
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
        return 4;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public int getRangeY() {
        return 2;
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
