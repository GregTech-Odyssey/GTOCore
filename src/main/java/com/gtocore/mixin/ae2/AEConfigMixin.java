package com.gtocore.mixin.ae2;

import appeng.core.AEConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(AEConfig.class)
public class AEConfigMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean isPortableCellDisassemblyEnabled() {
        return false;
    }
}
