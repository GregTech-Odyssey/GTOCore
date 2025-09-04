package com.gtocore.mixin.botania;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vazkii.botania.common.handler.ContributorList;

@Mixin(ContributorList.class)
public final class ContributorListMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void firstStart() {}
}
