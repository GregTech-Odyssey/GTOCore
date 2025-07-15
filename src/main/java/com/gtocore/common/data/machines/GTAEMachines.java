package com.gtocore.common.data.machines;

import com.gtocore.common.machine.multiblock.part.ae.*;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.client.renderer.machine.OverlayTieredMachineRenderer;

import net.minecraft.network.chat.Component;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gtolib.api.registries.GTORegistration.GTM;
import static com.gtolib.utils.register.MachineRegisterUtils.machine;

public final class GTAEMachines {

    public static void init() {}

    public static final MachineDefinition ME_BIG_STORAGE_ACCESS_HATCH = machine("me_big_storage_access_hatch", "ME大存储访问仓", MEBigStorageAccessPartMachine::new)
            .langValue("ME Big Storage Access Hatch")
            .tier(IV)
            .allRotation()
            .tooltipsText("Use BigInteger Storage", "使用BigInteger存储")
            .notAllowSharedTooltips()
            .renderer(() -> new OverlayTieredMachineRenderer(IV, GTCEu.id("block/machine/part/me_pattern_buffer")))
            .register();

    public static final MachineDefinition ME_STORAGE_ACCESS_HATCH = machine("me_storage_access_hatch", "ME存储访问仓", MEStorageAccessPartMachine::new)
            .langValue("ME Storage Access Hatch")
            .tier(EV)
            .allRotation()
            .notAllowSharedTooltips()
            .renderer(() -> new OverlayTieredMachineRenderer(EV, GTCEu.id("block/machine/part/me_pattern_buffer")))
            .register();

    public static final MachineDefinition ME_STORAGE_ACCESS_PROXY = machine("me_storage_access_proxy", "ME存储访问代理", MEStorageAccessProxyartMachine::new)
            .langValue("ME Storage Access Proxy")
            .tier(EV)
            .allRotation()
            .renderer(() -> new OverlayTieredMachineRenderer(EV, GTCEu.id("block/machine/part/me_pattern_buffer_proxy")))
            .register();

    public static final MachineDefinition ME_TAG_FILTER_STOCK_BUS = machine("me_tag_filter_stock_bus", "ME标签过滤库存输入总线", METagFilterStockBusPartMachine::new)
            .tier(LuV)
            .abilities(PartAbility.IMPORT_ITEMS)
            .allRotation()
            .renderer(() -> new OverlayTieredMachineRenderer(LuV, GTCEu.id("block/machine/part/me_item_bus.import")))
            .tooltips(Component.translatable("gtceu.machine.item_bus.import.tooltip"),
                    Component.translatable("gtceu.machine.me.item_import.tooltip"),
                    Component.translatable("gtceu.machine.me.copy_paste.tooltip"),
                    Component.translatable("gtceu.part_sharing.enabled"))
            .register();

    public static final MachineDefinition ME_TAG_FILTER_STOCK_HATCH = machine("me_tag_filter_stock_hatch", "ME标签过滤库存输入仓", METagFilterStockHatchPartMachine::new)
            .tier(LuV)
            .abilities(PartAbility.IMPORT_FLUIDS)
            .allRotation()
            .renderer(() -> new OverlayTieredMachineRenderer(LuV, GTCEu.id("block/machine/part/me_fluid_hatch.import")))
            .tooltips(Component.translatable("gtceu.machine.fluid_hatch.import.tooltip"),
                    Component.translatable("gtceu.machine.me.item_import.tooltip"),
                    Component.translatable("gtceu.machine.me.copy_paste.tooltip"),
                    Component.translatable("gtceu.part_sharing.enabled"))
            .register();

    public static final MachineDefinition ME_CRAFT_PATTERN_PART_MACHINE = machine("me_craft_pattern_part_machine", "合成样板仓", MECraftPatternPartMachine::new)
            .langValue("ME Craft Pattern Hatch")
            .tier(ZPM)
            .allRotation()
            .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS)
            .notAllowSharedTooltips()
            .renderer(() -> new OverlayTieredMachineRenderer(ZPM, GTCEu.id("block/machine/part/me_pattern_buffer_proxy")))
            .register();

    public static final MachineDefinition ME_CATALYST_ME_PATTERN_BUFFER = machine("me_catalyst_pattern_buffer", "ME催化剂样板总成", MECatalystPatternBufferPartMachine::new)
            .langValue("ME Catalyst Pattern Buffer")
            .tier(UV)
            .allRotation()
            .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS)
            .tooltipsKey("gtceu.part_sharing.enabled")
            .renderer(() -> new OverlayTieredMachineRenderer(UV, GTCEu.id("block/machine/part/me_pattern_buffer")))
            .register();

    public static final MachineDefinition ME_PROGRAMMABLE_PATTERN_BUFFER = machine("me_programmable_pattern_buffer", "ME可编程样板总成", MEProgrammablePatternBufferPartMachine::new)
            .langValue("ME Programmable Pattern Buffer")
            .tier(ZPM)
            .allRotation()
            .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS)
            .tooltipsKey("gtceu.part_sharing.enabled")
            .renderer(() -> new OverlayTieredMachineRenderer(ZPM, GTCEu.id("block/machine/part/me_pattern_buffer")))
            .register();

    public static final MachineDefinition ME_EXTEND_PATTERN_BUFFER = machine("me_extend_pattern_buffer", "ME扩展样板总成", h -> new MEPatternBufferPartMachineKt(h, 81))
            .langValue("ME Extend Pattern Buffer")
            .tier(UV)
            .allRotation()
            .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS)
            .tooltipsKey("gtceu.part_sharing.enabled")
            .renderer(() -> new OverlayTieredMachineRenderer(UV, GTCEu.id("block/machine/part/me_pattern_buffer")))
            .register();

    public static final MachineDefinition ME_EXTEND_PATTERN_BUFFER_ULTRA = machine("me_extend_pattern_buffer_ultra", "ME扩展样板总成Ultra", h -> new MEPatternBufferPartMachineKt(h, 324))
            .langValue("ME Extend Pattern Buffer Ultra")
            .tier(UHV)
            .allRotation()
            .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS)
            .tooltipsKey("gtceu.part_sharing.enabled")
            .renderer(() -> new OverlayTieredMachineRenderer(UHV, GTCEu.id("block/machine/part/me_pattern_buffer")))
            .register();

    public static final MachineDefinition ITEM_IMPORT_BUS_ME = GTM
            .machine("me_input_bus", MEInputBusPartMachine::new)
            .langValue("ME Input Bus")
            .tier(EV)
            .allRotation()
            .abilities(PartAbility.IMPORT_ITEMS)
            .overlayTieredHullRenderer("me_item_bus.import")
            .tooltips(Component.translatable("gtceu.machine.item_bus.import.tooltip"),
                    Component.translatable("gtceu.machine.me.item_import.tooltip"),
                    Component.translatable("gtceu.machine.me.copy_paste.tooltip"),
                    Component.translatable("gtceu.part_sharing.enabled"))
            .register();

    public static final MachineDefinition STOCKING_IMPORT_BUS_ME = GTM
            .machine("me_stocking_input_bus", MEStockingBusPartMachine::new)
            .langValue("ME Stocking Input Bus")
            .tier(LuV)
            .allRotation()
            .abilities(PartAbility.IMPORT_ITEMS)
            .overlayTieredHullRenderer("me_item_bus.import")
            .tooltips(Component.translatable("gtceu.machine.item_bus.import.tooltip"),
                    Component.translatable("gtceu.machine.me.stocking_item.tooltip.0"),
                    Component.translatable("gtceu.machine.me_import_item_hatch.configs.tooltip"),
                    Component.translatable("gtceu.machine.me.copy_paste.tooltip"),
                    Component.translatable("gtceu.machine.me.stocking_item.tooltip.1"),
                    Component.translatable("gtceu.part_sharing.enabled"))
            .register();

    public static final MachineDefinition ITEM_EXPORT_BUS_ME = GTM
            .machine("me_output_bus", MEOutputBusPartMachine::new)
            .langValue("ME Output Bus")
            .tier(EV)
            .allRotation()
            .abilities(PartAbility.EXPORT_ITEMS)
            .overlayTieredHullRenderer("me_item_bus.export")
            .tooltips(Component.translatable("gtceu.machine.item_bus.export.tooltip"),
                    Component.translatable("gtceu.machine.me.item_export.tooltip"),
                    Component.translatable("gtceu.machine.me.export.tooltip"),
                    Component.translatable("gtceu.part_sharing.enabled"))
            .register();

    public static final MachineDefinition FLUID_IMPORT_HATCH_ME = GTM
            .machine("me_input_hatch", MEInputHatchPartMachine::new)
            .langValue("ME Input Hatch")
            .tier(EV)
            .allRotation()
            .abilities(PartAbility.IMPORT_FLUIDS)
            .overlayTieredHullRenderer("me_fluid_hatch.import")
            .tooltips(Component.translatable("gtceu.machine.fluid_hatch.import.tooltip"),
                    Component.translatable("gtceu.machine.me.fluid_import.tooltip"),
                    Component.translatable("gtceu.machine.me.copy_paste.tooltip"),
                    Component.translatable("gtceu.part_sharing.enabled"))
            .register();

    public static final MachineDefinition STOCKING_IMPORT_HATCH_ME = GTM
            .machine("me_stocking_input_hatch", MEStockingHatchPartMachine::new)
            .langValue("ME Stocking Input Hatch")
            .tier(LuV)
            .allRotation()
            .abilities(PartAbility.IMPORT_FLUIDS)
            .overlayTieredHullRenderer("me_fluid_hatch.import")
            .tooltips(Component.translatable("gtceu.machine.fluid_hatch.import.tooltip"),
                    Component.translatable("gtceu.machine.me.stocking_fluid.tooltip.0"),
                    Component.translatable("gtceu.machine.me_import_fluid_hatch.configs.tooltip"),
                    Component.translatable("gtceu.machine.me.copy_paste.tooltip"),
                    Component.translatable("gtceu.machine.me.stocking_fluid.tooltip.1"),
                    Component.translatable("gtceu.part_sharing.enabled"))
            .register();

    public static final MachineDefinition FLUID_EXPORT_HATCH_ME = GTM
            .machine("me_output_hatch", MEOutputHatchPartMachine::new)
            .langValue("ME Output Hatch")
            .tier(EV)
            .allRotation()
            .abilities(PartAbility.EXPORT_FLUIDS)
            .overlayTieredHullRenderer("me_fluid_hatch.export")
            .tooltips(Component.translatable("gtceu.machine.fluid_hatch.export.tooltip"),
                    Component.translatable("gtceu.machine.me.fluid_export.tooltip"),
                    Component.translatable("gtceu.machine.me.export.tooltip"),
                    Component.translatable("gtceu.part_sharing.enabled"))
            .register();

    public static final MachineDefinition ME_PATTERN_BUFFER = GTM
            .machine("me_pattern_buffer", h -> new MEPatternBufferPartMachineKt(h, 27))
            .tier(LuV)
            .allRotation()
            .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS)
            .allRotation()
            .overlayTieredHullRenderer("me_pattern_buffer")
            .langValue("ME Pattern Buffer")
            .tooltips(Component.translatable("block.gtceu.pattern_buffer.desc.0"),
                    Component.translatable("block.gtceu.pattern_buffer.desc.1"),
                    Component.translatable("block.gtceu.pattern_buffer.desc.2"),
                    Component.translatable("gtceu.part_sharing.enabled"))
            .register();

    public static final MachineDefinition ME_PATTERN_BUFFER_PROXY = GTM
            .machine("me_pattern_buffer_proxy", MEPatternBufferProxyPartMachine::new)
            .tier(LuV)
            .allRotation()
            .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS)
            .allRotation()
            .overlayTieredHullRenderer("me_pattern_buffer_proxy")
            .langValue("ME Pattern Buffer Proxy")
            .tooltips(
                    Component.translatable("block.gtceu.pattern_buffer_proxy.desc.0"),
                    Component.translatable("block.gtceu.pattern_buffer_proxy.desc.1"),
                    Component.translatable("block.gtceu.pattern_buffer_proxy.desc.2"),
                    Component.translatable("gtceu.part_sharing.enabled"))
            .register();
}
