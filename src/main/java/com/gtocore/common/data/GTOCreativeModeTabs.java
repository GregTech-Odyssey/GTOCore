package com.gtocore.common.data;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.pipelike.cable.Insulation;

import net.minecraft.world.item.CreativeModeTab;

import com.tterrag.registrate.util.entry.RegistryEntry;

import static com.gtolib.api.registries.GTORegistration.GTO;

public final class GTOCreativeModeTabs {

    public static final RegistryEntry<CreativeModeTab> GTO_ITEM = GTO
            .defaultCreativeTab("item", builder -> builder
                    .title(GTO.addLang("itemGroup", GTOCore.id("item"), GTOCore.NAME + " | Item"))
                    .icon(GTOItems.MEGA_MAX_BATTERY::asStack)
                    .build())
            .register();

    public static final RegistryEntry<CreativeModeTab> GTO_BLOCK = GTO
            .defaultCreativeTab("block", builder -> builder
                    .title(GTO.addLang("itemGroup", GTOCore.id("block"), GTOCore.NAME + " | Block"))
                    .icon(GTOBlocks.IRIDIUM_CASING::asStack)
                    .withTabsBefore(GTO_ITEM.getKey())
                    .build())
            .register();

    public static final RegistryEntry<CreativeModeTab> GTO_MACHINE = GTO
            .defaultCreativeTab("machine", builder -> builder
                    .title(GTO.addLang("itemGroup", GTOCore.id("machine"), GTOCore.NAME + " | Machine"))
                    .icon(GTOMachines.ARC_GENERATOR[1]::asStack)
                    .withTabsBefore(GTO_BLOCK.getKey())
                    .build())
            .register();

    public static final RegistryEntry<CreativeModeTab> GTO_MATERIAL_BLOCK = GTO
            .defaultCreativeTab("material_block", builder -> builder
                    .title(GTO.addLang("itemGroup", GTOCore.id("material_block"), GTOCore.NAME + " | Material Block"))
                    .icon(() -> ChemicalHelper.get(TagPrefix.block, GTOMaterials.Hypogen))
                    .withTabsBefore(GTO_MACHINE.getKey())
                    .build())
            .register();

    public static final RegistryEntry<CreativeModeTab> GTO_MATERIAL_ITEM = GTO
            .defaultCreativeTab("material_item", builder -> builder
                    .title(GTO.addLang("itemGroup", GTOCore.id("material_item"), GTOCore.NAME + " | Material Item"))
                    .icon(() -> ChemicalHelper.get(TagPrefix.ingot, GTOMaterials.Hypogen))
                    .withTabsBefore(GTO_MATERIAL_BLOCK.getKey())
                    .build())
            .register();

    public static final RegistryEntry<CreativeModeTab> GTO_MATERIAL_PIPE = GTO
            .defaultCreativeTab("material_pipe", builder -> builder
                    .title(GTO.addLang("itemGroup", GTOCore.id("material_pipe"), GTOCore.NAME + " | Material Pipe"))
                    .icon(() -> ChemicalHelper.get(Insulation.WIRE_QUADRUPLE.tagPrefix, GTOMaterials.Hypogen))
                    .withTabsBefore(GTO_MATERIAL_ITEM.getKey())
                    .build())
            .register();

    public static final RegistryEntry<CreativeModeTab> GTO_MATERIAL_FLUID = GTO
            .defaultCreativeTab("material_fluid", builder -> builder
                    .title(GTO.addLang("itemGroup", GTOCore.id("material_fluid"), GTOCore.NAME + " | Material Fluid"))
                    .icon(() -> GTItems.FLUID_CELL_LARGE_TUNGSTEN_STEEL.asStack())
                    .withTabsBefore(GTO_MATERIAL_PIPE.getKey())
                    .build())
            .register();

    public static void init() {}
}
