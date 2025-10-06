package com.gtocore.mixin.ae2;

import appeng.api.features.P2PTunnelAttunementInternal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(P2PTunnelAttunementInternal.class)
public class P2PTunnelAttunementInternalMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite(remap = false)
    public static List<P2PTunnelAttunementInternal.Resultant> getApiTunnels() {
        return List.of();
    }
}
