package com.gto.gtocore.common.data;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.pipelike.cable.Insulation;

import net.minecraft.world.item.CreativeModeTab;

import com.tterrag.registrate.util.entry.RegistryEntry;

import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;

public interface GTOCreativeModeTabs {

    RegistryEntry<CreativeModeTab> GTO_ITEM = REGISTRATE
            .defaultCreativeTab("item", builder -> builder
                    .displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator("item", REGISTRATE))
                    .title(REGISTRATE.addLang("itemGroup", GTOCore.id("item"), GTOCore.NAME + " | Item"))
                    .icon(GTOItems.MEGA_MAX_BATTERY::asStack)
                    .build())
            .register();

    RegistryEntry<CreativeModeTab> GTO_BLOCK = REGISTRATE
            .defaultCreativeTab("block", builder -> builder
                    .displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator("block", REGISTRATE))
                    .title(REGISTRATE.addLang("itemGroup", GTOCore.id("block"), GTOCore.NAME + " | Block"))
                    .icon(GTOBlocks.IRIDIUM_CASING::asStack)
                    .withTabsBefore(GTO_ITEM.getKey())
                    .build())
            .register();

    RegistryEntry<CreativeModeTab> GTO_MACHINE = REGISTRATE
            .defaultCreativeTab("machine", builder -> builder
                    .displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator("machine", REGISTRATE))
                    .title(REGISTRATE.addLang("itemGroup", GTOCore.id("machine"), GTOCore.NAME + " | Machine"))
                    .icon(GTOMachines.ARC_GENERATOR[1]::asStack)
                    .withTabsBefore(GTO_BLOCK.getKey())
                    .build())
            .register();

    RegistryEntry<CreativeModeTab> GTO_MATERIAL_BLOCK = REGISTRATE
            .defaultCreativeTab("material_block", builder -> builder
                    .displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator("material_block", REGISTRATE))
                    .title(REGISTRATE.addLang("itemGroup", GTOCore.id("material_block"), GTOCore.NAME + " | Material Block"))
                    .icon(() -> ChemicalHelper.get(TagPrefix.block, GTOMaterials.Hypogen))
                    .withTabsBefore(GTO_MACHINE.getKey())
                    .build())
            .register();

    RegistryEntry<CreativeModeTab> GTO_MATERIAL_ITEM = REGISTRATE
            .defaultCreativeTab("material_item", builder -> builder
                    .displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator("material_item", REGISTRATE))
                    .title(REGISTRATE.addLang("itemGroup", GTOCore.id("material_item"), GTOCore.NAME + " | Material Item"))
                    .icon(() -> ChemicalHelper.get(TagPrefix.ingot, GTOMaterials.Hypogen))
                    .withTabsBefore(GTO_MATERIAL_BLOCK.getKey())
                    .build())
            .register();

    RegistryEntry<CreativeModeTab> GTO_MATERIAL_PIPE = REGISTRATE
            .defaultCreativeTab("material_pipe", builder -> builder
                    .displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator("material_pipe", REGISTRATE))
                    .title(REGISTRATE.addLang("itemGroup", GTOCore.id("material_pipe"), GTOCore.NAME + " | Material Pipe"))
                    .icon(() -> ChemicalHelper.get(Insulation.WIRE_QUADRUPLE.tagPrefix, GTOMaterials.Hypogen))
                    .withTabsBefore(GTO_MATERIAL_ITEM.getKey())
                    .build())
            .register();

    RegistryEntry<CreativeModeTab> GTO_MATERIAL_FLUID = REGISTRATE
            .defaultCreativeTab("material_fluid", builder -> builder
                    .displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator("material_fluid", REGISTRATE))
                    .title(REGISTRATE.addLang("itemGroup", GTOCore.id("material_fluid"), GTOCore.NAME + " | Material Fluid"))
                    .icon(() -> GTItems.FLUID_CELL_LARGE_TUNGSTEN_STEEL.asStack())
                    .withTabsBefore(GTO_MATERIAL_PIPE.getKey())
                    .build())
            .register();

    static void init() {}
}
