package com.gto.gtocore.mixin.adastra;

import earth.terrarium.adastra.common.utils.radio.StationLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(StationLoader.class)
public class StationLoaderMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void init() {}
}
