package com.gto.gtocore.mixin.gtm;

import com.gregtechceu.gtceu.GTCEu;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GTCEu.Mods.class)
public final class GTCEuMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static boolean isFTBTeamsLoaded() {
        return false;
    }
}
