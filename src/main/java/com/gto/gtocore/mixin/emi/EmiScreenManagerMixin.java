package com.gto.gtocore.mixin.emi;

import com.gto.gtocore.client.ClientUtil;

import dev.emi.emi.config.EmiConfig;
import dev.emi.emi.runtime.EmiReloadManager;
import dev.emi.emi.screen.EmiScreenManager;
import earth.terrarium.adastra.common.menus.PlanetsMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(EmiScreenManager.class)
public final class EmiScreenManagerMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static boolean isDisabled() {
        return !EmiReloadManager.isLoaded() || !EmiConfig.enabled || ClientUtil.getPlayer().containerMenu instanceof PlanetsMenu;
    }
}
