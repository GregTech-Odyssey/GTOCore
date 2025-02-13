package com.gto.gtocore.mixin.gtm.registry;

import com.gto.gtocore.common.data.GTOCreativeModeTabs;
import com.gto.gtocore.init.GTOMachines;

import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTMachines;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;

@Mixin(GTMachines.class)
public final class GTMachinesMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void init() {
        REGISTRATE.creativeModeTab(() -> GTOCreativeModeTabs.GTO_MACHINE);
        GTOMachines.init();
        GTRegistries.MACHINES.freeze();
    }
}
