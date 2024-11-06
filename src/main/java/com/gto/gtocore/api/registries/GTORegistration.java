package com.gto.gtocore.api.registries;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

public class GTORegistration {

    public static final GTRegistrate REGISTRATE = GTRegistrate.create(GTOCore.MOD_ID);

    static {
        GTORegistration.REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
    }

    private GTORegistration() {/**/}
}
