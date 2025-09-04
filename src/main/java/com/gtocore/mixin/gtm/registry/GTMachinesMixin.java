package com.gtocore.mixin.gtm.registry;

import com.gtocore.common.block.BlockMap;
import com.gtocore.common.data.GTOCreativeModeTabs;
import com.gtocore.common.data.GTOMachines;
import com.gtocore.common.data.machines.GCYMMachines;
import com.gtocore.common.data.machines.GTAEMachines;
import com.gtocore.common.data.machines.GTMachineModify;

import com.gregtechceu.gtceu.api.registry.GTRegistries;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.machines.GTMultiMachines;
import com.gregtechceu.gtceu.common.data.machines.GTResearchMachines;

import com.hepdd.gtmthings.data.CreativeMachines;
import com.hepdd.gtmthings.data.CreativeModeTabs;
import com.hepdd.gtmthings.data.CustomMachines;
import com.hepdd.gtmthings.data.WirelessMachines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static com.gtolib.api.registries.GTORegistration.GTO;

@Mixin(GTMachines.class)
public final class GTMachinesMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void init() {
        GTO.creativeModeTab(() -> GTOCreativeModeTabs.GTO_MACHINE);
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
