package com.gto.gtocore.mixin.gtm.registry;

import com.gto.gtocore.common.block.BlockMap;
import com.gto.gtocore.common.data.GTOCreativeModeTabs;
import com.gto.gtocore.common.data.GTOMachines;
import com.gto.gtocore.common.data.machines.*;

import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.machines.GTAEMachines;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.common.data.machines.GTResearchMachines;

import com.hepdd.gtmthings.data.CreativeMachines;
import com.hepdd.gtmthings.data.CreativeModeTabs;
import com.hepdd.gtmthings.data.CustomMachines;
import com.hepdd.gtmthings.data.WirelessMachines;
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
        BlockMap.init();
        GTMultiMachines.init();
        GTResearchMachines.init();
        GTAEMachines.init();
        GTMachineModify.init();
        GCYMMachines.init();
        CreativeModeTabs.init();
        CreativeMachines.init();
        WirelessMachines.init();
        CustomMachines.init();
        GTOMachines.init();
        GTRegistries.MACHINES.freeze();
    }
}
