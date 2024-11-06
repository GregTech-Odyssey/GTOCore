package com.gto.gtocore.common.data;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;

import net.minecraft.world.item.CreativeModeTab;

import com.tterrag.registrate.util.entry.RegistryEntry;

import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;

public class GTOCreativeModeTabs {

    public static final RegistryEntry<CreativeModeTab> GTO_CORE = REGISTRATE.defaultCreativeTab(GTOCore.MOD_ID,
            builder -> builder.displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator(GTOCore.MOD_ID, REGISTRATE))
                    .title(REGISTRATE.addLang("itemGroup", GTOCore.id("creative_tab"), "GTO Core"))
                    .icon(GTOItems.MEGA_ULTIMATE_BATTERY::asStack)
                    .build())
            .register();

    public static void init() {}
}
