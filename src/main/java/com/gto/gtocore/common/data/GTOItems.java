package com.gto.gtocore.common.data;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.item.*;
import com.gto.gtocore.integration.ae2.InfinityCell;
import com.gto.gtocore.utils.StringUtil;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.tag.TagUtil;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.component.ElectricStats;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.item.CoverPlaceBehavior;
import com.gregtechceu.gtceu.common.item.TooltipBehavior;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import com.lowdragmc.lowdraglib.utils.LocalizationUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import appeng.api.stacks.AEKeyType;
import appeng.api.upgrades.Upgrades;
import appeng.core.definitions.AEItems;
import appeng.core.definitions.ItemDefinition;
import appeng.core.localization.GuiText;
import appeng.items.materials.MaterialItem;
import appeng.items.materials.StorageComponentItem;
import appeng.items.storage.BasicStorageCell;
import appeng.items.storage.StorageTier;
import appeng.items.tools.powered.PortableCellItem;
import appeng.menu.me.common.MEStorageMenu;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

import java.util.List;
import java.util.Locale;

import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;

@SuppressWarnings("unused")
public class GTOItems {

    static {
        REGISTRATE.creativeModeTab(() -> GTOCreativeModeTabs.GTO_CORE);
    }

    public static void init() {}

    private static ItemEntry<StorageComponentItem> registerStorageComponentItem(int tier) {
        return REGISTRATE
                .item("cell_component_" + tier + "m", p -> new StorageComponentItem(p, 1048576 * tier))
                .register();
    }

    private static ItemEntry<BasicStorageCell> registerStorageCell(int tier,
                                                                   ItemEntry<StorageComponentItem> StorageComponent,
                                                                   boolean isItem) {
        ItemDefinition<MaterialItem> CELL_HOUSING = isItem ? AEItems.ITEM_CELL_HOUSING : AEItems.FLUID_CELL_HOUSING;
        return REGISTRATE
                .item((isItem ? "item" : "fluid") + "_storage_cell_" + tier + "m", p -> new BasicStorageCell(
                        p.stacksTo(1),
                        StorageComponent,
                        CELL_HOUSING,
                        3 + 0.5 * Math.log(tier) / Math.log(4),
                        1024 * tier,
                        1,
                        isItem ? 128 : 36,
                        isItem ? AEKeyType.items() : AEKeyType.fluids()))
                .register();
    }

    public static final ItemEntry<StorageComponentItem> CELL_COMPONENT_1M = registerStorageComponentItem(1);
    public static final ItemEntry<StorageComponentItem> CELL_COMPONENT_4M = registerStorageComponentItem(4);
    public static final ItemEntry<StorageComponentItem> CELL_COMPONENT_16M = registerStorageComponentItem(16);
    public static final ItemEntry<StorageComponentItem> CELL_COMPONENT_64M = registerStorageComponentItem(64);
    public static final ItemEntry<StorageComponentItem> CELL_COMPONENT_256M = registerStorageComponentItem(256);

    public static final ItemEntry<BasicStorageCell> ITEM_CELL_1M = registerStorageCell(1, CELL_COMPONENT_1M, true);
    public static final ItemEntry<BasicStorageCell> ITEM_CELL_4M = registerStorageCell(4, CELL_COMPONENT_4M, true);
    public static final ItemEntry<BasicStorageCell> ITEM_CELL_16M = registerStorageCell(16, CELL_COMPONENT_16M, true);
    public static final ItemEntry<BasicStorageCell> ITEM_CELL_64M = registerStorageCell(64, CELL_COMPONENT_64M, true);
    public static final ItemEntry<BasicStorageCell> ITEM_CELL_256M = registerStorageCell(256, CELL_COMPONENT_256M,
            true);

    public static final ItemEntry<BasicStorageCell> FLUID_CELL_1M = registerStorageCell(1, CELL_COMPONENT_1M, false);
    public static final ItemEntry<BasicStorageCell> FLUID_CELL_4M = registerStorageCell(4, CELL_COMPONENT_4M, false);
    public static final ItemEntry<BasicStorageCell> FLUID_CELL_16M = registerStorageCell(16, CELL_COMPONENT_16M, false);
    public static final ItemEntry<BasicStorageCell> FLUID_CELL_64M = registerStorageCell(64, CELL_COMPONENT_64M, false);
    public static final ItemEntry<BasicStorageCell> FLUID_CELL_256M = registerStorageCell(256, CELL_COMPONENT_256M,
            false);

    public static final ItemEntry<PortableCellItem> SUPER_PORTABLE_ITEM_CELL = REGISTRATE
            .item("super_portable_item_storage_cell", p -> new PortableCellItem(AEKeyType.items(),
                    256,
                    MEStorageMenu.PORTABLE_ITEM_CELL_TYPE,
                    new StorageTier(100, "super", Integer.MAX_VALUE, 100, WETWARE_MAINFRAME_UHV),
                    p.stacksTo(1), 0xDDDDDD))
            .register();

    public static final ItemEntry<PortableCellItem> SUPER_PORTABLE_FLUID_CELL = REGISTRATE
            .item("super_portable_fluid_storage_cell", p -> new PortableCellItem(AEKeyType.fluids(),
                    256,
                    MEStorageMenu.PORTABLE_ITEM_CELL_TYPE,
                    new StorageTier(100, "super", Integer.MAX_VALUE, 100, WETWARE_MAINFRAME_UHV),
                    p.stacksTo(1), 0xFF6D36))
            .register();

    public static final ItemEntry<InfinityCell> ITEM_INFINITY_CELL = REGISTRATE.item("item_infinity_cell", p -> new InfinityCell(AEKeyType.items())).register();
    public static final ItemEntry<InfinityCell> FLUID_INFINITY_CELL = REGISTRATE.item("fluid_infinity_cell", p -> new InfinityCell(AEKeyType.fluids())).register();

    public static void InitUpgrades() {
        String storageCellGroup = GuiText.StorageCells.getTranslationKey();
        String portableCellGroup = GuiText.PortableCells.getTranslationKey();

        var itemCells = List.of(
                ITEM_CELL_1M, ITEM_CELL_4M, ITEM_CELL_16M, ITEM_CELL_64M,
                ITEM_CELL_256M);
        for (var itemCell : itemCells) {
            Upgrades.add(AEItems.FUZZY_CARD, itemCell, 1, storageCellGroup);
            Upgrades.add(AEItems.INVERTER_CARD, itemCell, 1, storageCellGroup);
            Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, itemCell, 1, storageCellGroup);
            Upgrades.add(AEItems.VOID_CARD, itemCell, 1, storageCellGroup);
        }

        var fluidCells = List.of(
                FLUID_CELL_1M, FLUID_CELL_4M, FLUID_CELL_16M, FLUID_CELL_64M,
                FLUID_CELL_256M);
        for (var fluidCell : fluidCells) {
            Upgrades.add(AEItems.INVERTER_CARD, fluidCell, 1, storageCellGroup);
            Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, fluidCell, 1, storageCellGroup);
            Upgrades.add(AEItems.VOID_CARD, fluidCell, 1, storageCellGroup);
        }

        Upgrades.add(AEItems.FUZZY_CARD, SUPER_PORTABLE_ITEM_CELL, 1, portableCellGroup);
        Upgrades.add(AEItems.INVERTER_CARD, SUPER_PORTABLE_ITEM_CELL, 1, portableCellGroup);
        Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, SUPER_PORTABLE_ITEM_CELL, 1, portableCellGroup);
        Upgrades.add(AEItems.VOID_CARD, SUPER_PORTABLE_ITEM_CELL, 1, portableCellGroup);
        Upgrades.add(AEItems.ENERGY_CARD, SUPER_PORTABLE_ITEM_CELL, 2, portableCellGroup);

        Upgrades.add(AEItems.INVERTER_CARD, SUPER_PORTABLE_FLUID_CELL, 1, portableCellGroup);
        Upgrades.add(AEItems.EQUAL_DISTRIBUTION_CARD, SUPER_PORTABLE_FLUID_CELL, 1, portableCellGroup);
        Upgrades.add(AEItems.VOID_CARD, SUPER_PORTABLE_FLUID_CELL, 1, portableCellGroup);
        Upgrades.add(AEItems.ENERGY_CARD, SUPER_PORTABLE_FLUID_CELL, 2, portableCellGroup);
    }

    public static final ItemEntry<ComponentItem> REALLY_ULTIMATE_BATTERY = REGISTRATE
            .item("really_max_battery", ComponentItem::create)
            .lang("Really MAX Battery")
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.translatable("gtocore.tooltip.item.really_max_battery").withStyle(ChatFormatting.GRAY)))))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, GTValues.UEV)))
            .register();
    public static final ItemEntry<ComponentItem> TRANSCENDENT_ULTIMATE_BATTERY = REGISTRATE
            .item("transcendent_max_battery", ComponentItem::create)
            .lang("Transcendent MAX Battery")
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.translatable("gtocore.tooltip.item.transcendent_max_battery").withStyle(ChatFormatting.GRAY)))))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, GTValues.UIV)))
            .register();
    public static final ItemEntry<ComponentItem> EXTREMELY_ULTIMATE_BATTERY = REGISTRATE
            .item("extremely_max_battery", ComponentItem::create)
            .lang("Extremely MAX Battery")
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.translatable("gtocore.tooltip.item.extremely_max_battery").withStyle(ChatFormatting.GRAY)))))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, GTValues.UXV)))
            .register();
    public static final ItemEntry<ComponentItem> INSANELY_ULTIMATE_BATTERY = REGISTRATE
            .item("insanely_max_battery", ComponentItem::create)
            .lang("Insanely MAX Battery")
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.literal(StringUtil.dark_purplish_red(LocalizationUtils.format("gtocore.tooltip.item.insanely_max_battery")))))))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, GTValues.OpV)))
            .register();
    public static final ItemEntry<ComponentItem> MEGA_ULTIMATE_BATTERY = REGISTRATE
            .item("mega_max_battery", ComponentItem::create)
            .lang("Mega MAX Battery")
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.literal(StringUtil.full_color(LocalizationUtils.format("gtocore.tooltip.item.mega_max_battery")))))))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, GTValues.MAX)))
            .register();

    public static final ItemEntry<Item> ELECTRIC_MOTOR_MAX = REGISTRATE.item("max_electric_motor", Item::new).register();

    public static final ItemEntry<ComponentItem> ELECTRIC_PUMP_MAX = REGISTRATE
            .item("max_electric_pump", ComponentItem::create)
            .lang("MAX Electric Pump")
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.ELECTRIC_PUMP_MAX)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.electric.pump.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.fluid_transfer_rate",
                        1280 * 64 * 64 * 4 / 20));
            })))
            .register();

    public static final ItemEntry<ComponentItem> CONVEYOR_MODULE_MAX = REGISTRATE
            .item("max_conveyor_module", ComponentItem::create)
            .lang("MAX Conveyor Module")
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.CONVEYOR_MODULE_MAX)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.conveyor.module.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.item_transfer_rate_stacks", 16));
            })))
            .register();

    public static final ItemEntry<ComponentItem> ROBOT_ARM_MAX = REGISTRATE.item("max_robot_arm", ComponentItem::create)
            .lang("MAX Robot Arm")
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.ROBOT_ARM_MAX)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.robot.arm.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.item_transfer_rate_stacks", 16));
            })))
            .register();

    public static final ItemEntry<Item> ELECTRIC_PISTON_MAX = registerLang("max_electric_piston", "MAX Electric Piston");
    public static final ItemEntry<Item> FIELD_GENERATOR_MAX = registerLang("max_field_generator", "MAX Field Generator");
    public static final ItemEntry<Item> EMITTER_MAX = registerLang("max_emitter", "MAX Emitte");
    public static final ItemEntry<Item> SENSOR_MAX = registerLang("max_sensor", "MAX Sensor");

    public static final ItemEntry<ComponentItem> PRIMITIVE_ROBOT_ARM = REGISTRATE
            .item("primitive_robot_arm", ComponentItem::create)
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.ROBOT_ARM_ULV)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.robot.arm.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.item_transfer_rate_stacks", 33554431));
            })))
            .register();

    public static final ItemEntry<ComponentItem> PRIMITIVE_FLUID_REGULATOR = REGISTRATE
            .item("primitive_fluid_regulator", ComponentItem::create)
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.FLUID_REGULATORS_ULV)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.fluid.regulator.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.fluid_transfer_rate", Integer.MAX_VALUE));
            })))
            .register();

    private static ItemEntry<ComponentItem> registerTieredCover(int amperage) {
        return REGISTRATE
                .item(GTValues.VN[GTValues.MAX].toLowerCase(Locale.ROOT) + "_" +
                        (amperage == 1 ? "" : amperage + "a_") + "wireless_energy_receive_cover", ComponentItem::create)
                .lang(GTValues.VNF[GTValues.MAX] + " " + (amperage == 1 ? "" : amperage + "A ") + "Wireless Energy Receive Cover")
                .onRegister(attach(new TooltipBehavior(lines -> {
                    lines.add(Component.translatable("item.gtmthings.wireless_energy_receive_cover.tooltip.1"));
                    lines.add(Component.translatable("item.gtmthings.wireless_energy_receive_cover.tooltip.2"));
                    lines.add(Component.translatable("item.gtmthings.wireless_energy_receive_cover.tooltip.3",
                            GTValues.V[GTValues.MAX] * amperage));
                }), new CoverPlaceBehavior(amperage == 1 ? GTOCovers.MAX_WIRELESS_ENERGY_RECEIVE :
                        GTOCovers.MAX_WIRELESS_ENERGY_RECEIVE_4A)))
                .register();
    }

    public static final ItemEntry<ComponentItem> WIRELESS_ENERGY_RECEIVE_COVER_MAX = registerTieredCover(1);

    public static final ItemEntry<ComponentItem> WIRELESS_ENERGY_RECEIVE_COVER_MAX_4A = registerTieredCover(4);

    public static final ItemEntry<ComponentItem> TIME_TWISTER = REGISTRATE
            .item("time_twister", ComponentItem::create)
            .onRegister(GTItems.attach(TimeTwister.INSTANCE))
            .register();

    public static final ItemEntry<ComponentItem> DEBUG_STRUCTURE_WRITER = REGISTRATE
            .item("debug_structure_writer", ComponentItem::create)
            .onRegister(GTItems.attach(StructureWriteBehavior.INSTANCE))
            .model(NonNullBiConsumer.noop())
            .register();

    public static final ItemEntry<ComponentItem> DEBUG_PATTERN_TEST = REGISTRATE
            .item("debug_pattern_test", ComponentItem::create)
            .onRegister(GTItems.attach(PatternTestBehavior.INSTANCE))
            .model(NonNullBiConsumer.noop())
            .register();

    public static final ItemEntry<ComponentItem> PATTERN_MODIFIER = REGISTRATE
            .item("pattern_modifier", ComponentItem::create)
            .onRegister(GTItems.attach(PatternModifier.INSTANCE))
            .model(NonNullBiConsumer.noop())
            .register();

    public static final ItemEntry<ComponentItem> CFG_COPY = REGISTRATE
            .item("cfg_copy", ComponentItem::create)
            .lang("Configuration Copy")
            .onRegister(attach(ConfigurationCopyBehavior.INSTANCE))
            .model(NonNullBiConsumer.noop())
            .register();

    private static ItemEntry<KineticRotorItem> registerRotor(String id, int durability, int min, int max, int material) {
        return REGISTRATE.item(id, p -> new KineticRotorItem(p, durability, min, max, material)).register();
    }

    public static ItemEntry<Item>[] registerCircuits(String name, int[] tiers) {
        ItemEntry<Item>[] entries = new ItemEntry[GTValues.TIER_COUNT];
        for (int tier : tiers) {
            String id = name + "_" + GTValues.VN[tier].toLowerCase();
            ItemEntry<Item> register = REGISTRATE.item(id, Item::new)
                    .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/circuit/" + id)))
                    .lang(FormattingUtil.toEnglishName(name) + " " + GTValues.VN[tier])
                    .tag(CustomTags.CIRCUITS_ARRAY[tier])
                    .register();
            entries[tier] = register;
        }
        return entries;
    }

    private static ItemEntry<Item> registerCircuit(String id, TagKey<Item> tagKey) {
        return REGISTRATE.item(id, Item::new)
                .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/circuit/" + id)))
                .tag(tagKey)
                .register();
    }

    private static ItemEntry<Item> registerEssence(String id) {
        return REGISTRATE.item(id + "_essence", Item::new)
                .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/essence/" + id)))
                .tag(TagUtil.optionalTag(BuiltInRegistries.ITEM, GTOCore.id("vein_essence")))
                .register();
    }

    private static ItemEntry<Item> registerCustomModel(String id) {
        return REGISTRATE.item(id, Item::new).model(NonNullBiConsumer.noop()).register();
    }

    private static ItemEntry<Item> register(String id) {
        return REGISTRATE.item(id, Item::new).register();
    }

    private static ItemEntry<Item> register(String id, String texture) {
        return REGISTRATE.item(id, Item::new)
                .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/" + texture)))
                .register();
    }

    private static ItemEntry<Item> registerLang(String id, String lang) {
        return REGISTRATE.item(id, Item::new)
                .lang(lang).register();
    }

    private static ItemEntry<Item> registerMagic(String id) {
        return REGISTRATE.item(id, Item::new)
                .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/magic/" + id)))
                .register();
    }

    public static final ItemEntry<KineticRotorItem> WOOD_ROTOR = registerRotor("wood_rotor", 2400, 4, 10, 0);
    public static final ItemEntry<KineticRotorItem> IRON_ROTOR = registerRotor("iron_rotor", 14000, 10, 20, 1);
    public static final ItemEntry<KineticRotorItem> STEEL_ROTOR = registerRotor("steel_rotor", 16000, 10, 30, 1);
    public static final ItemEntry<KineticRotorItem> CARBON_ROTOR = registerRotor("carbon_rotor", 24000, 2, 40, 2);

    public static final ItemEntry<Item> BIOWARE_PROCESSOR = registerCircuit("bioware_processor", CustomTags.ZPM_CIRCUITS);
    public static final ItemEntry<Item> BIOWARE_ASSEMBLY = registerCircuit("bioware_assembly", CustomTags.UV_CIRCUITS);
    public static final ItemEntry<Item> BIOWARE_COMPUTER = registerCircuit("bioware_computer", CustomTags.UHV_CIRCUITS);
    public static final ItemEntry<Item> BIOWARE_MAINFRAME = registerCircuit("bioware_mainframe", CustomTags.UEV_CIRCUITS);

    public static final ItemEntry<Item> OPTICAL_PROCESSOR = registerCircuit("optical_processor", CustomTags.UV_CIRCUITS);
    public static final ItemEntry<Item> OPTICAL_ASSEMBLY = registerCircuit("optical_assembly", CustomTags.UHV_CIRCUITS);
    public static final ItemEntry<Item> OPTICAL_COMPUTER = registerCircuit("optical_computer", CustomTags.UEV_CIRCUITS);
    public static final ItemEntry<Item> OPTICAL_MAINFRAME = registerCircuit("optical_mainframe", CustomTags.UIV_CIRCUITS);

    public static final ItemEntry<Item> EXOTIC_PROCESSOR = registerCircuit("exotic_processor", CustomTags.UHV_CIRCUITS);
    public static final ItemEntry<Item> EXOTIC_ASSEMBLY = registerCircuit("exotic_assembly", CustomTags.UEV_CIRCUITS);
    public static final ItemEntry<Item> EXOTIC_COMPUTER = registerCircuit("exotic_computer", CustomTags.UIV_CIRCUITS);
    public static final ItemEntry<Item> EXOTIC_MAINFRAME = registerCircuit("exotic_mainframe", CustomTags.UXV_CIRCUITS);

    public static final ItemEntry<Item> COSMIC_PROCESSOR = registerCircuit("cosmic_processor", CustomTags.UEV_CIRCUITS);
    public static final ItemEntry<Item> COSMIC_ASSEMBLY = registerCircuit("cosmic_assembly", CustomTags.UIV_CIRCUITS);
    public static final ItemEntry<Item> COSMIC_COMPUTER = registerCircuit("cosmic_computer", CustomTags.UXV_CIRCUITS);
    public static final ItemEntry<Item> COSMIC_MAINFRAME = registerCircuit("cosmic_mainframe", CustomTags.OpV_CIRCUITS);

    public static final ItemEntry<Item> SUPRACAUSAL_PROCESSOR = registerCircuit("supracausal_processor", CustomTags.UIV_CIRCUITS);
    public static final ItemEntry<Item> SUPRACAUSAL_ASSEMBLY = registerCircuit("supracausal_assembly", CustomTags.UXV_CIRCUITS);
    public static final ItemEntry<Item> SUPRACAUSAL_COMPUTER = registerCircuit("supracausal_computer", CustomTags.OpV_CIRCUITS);
    public static final ItemEntry<Item> SUPRACAUSAL_MAINFRAME = registerCircuit("supracausal_mainframe", CustomTags.MAX_CIRCUITS);

    public static final ItemEntry<Item>[] SUPRACHRONAL_CIRCUIT = registerCircuits("suprachronal_circuit", GTValues.tiersBetween(GTValues.ULV, GTValues.MAX));

    public static final ItemEntry<Item>[] MAGNETO_RESONATIC_CIRCUIT = registerCircuits("magneto_resonatic_circuit", GTValues.tiersBetween(GTValues.ULV, GTValues.UIV));

    public static final ItemEntry<Item>[] UNIVERSAL_CIRCUIT = registerCircuits("universal_circuit", GTValues.tiersBetween(GTValues.ULV, GTValues.MAX));

    public static final ItemEntry<Item> BIOWARE_CIRCUIT_BOARD = register("bioware_circuit_board");
    public static final ItemEntry<Item> BIOWARE_PRINTED_CIRCUIT_BOARD = register("bioware_printed_circuit_board");
    public static final ItemEntry<Item> SMD_CAPACITOR_BIOWARE = registerLang("smd_capacitor_bioware", "Bioware Capacitor");
    public static final ItemEntry<Item> SMD_DIODE_BIOWARE = registerLang("smd_diode_bioware", "Bioware Diode");
    public static final ItemEntry<Item> SMD_RESISTOR_BIOWARE = registerLang("smd_resistor_bioware", "Bioware Resistor");
    public static final ItemEntry<Item> SMD_TRANSISTOR_BIOWARE = registerLang("smd_transistor_bioware", "Bioware Transistor");
    public static final ItemEntry<Item> SMD_INDUCTOR_BIOWARE = registerLang("smd_inductor_bioware", "Bioware Inductor");

    public static final ItemEntry<Item> OPTICAL_CIRCUIT_BOARD = register("optical_circuit_board");
    public static final ItemEntry<Item> OPTICAL_PRINTED_CIRCUIT_BOARD = register("optical_printed_circuit_board");
    public static final ItemEntry<Item> OPTICAL_RAM_WAFER = register("optical_ram_wafer");
    public static final ItemEntry<Item> OPTICAL_RAM_CHIP = register("optical_ram_chip");
    public static final ItemEntry<Item> SMD_CAPACITOR_OPTICAL = registerLang("smd_capacitor_optical", "Optical Capacitor");
    public static final ItemEntry<Item> SMD_DIODE_OPTICAL = registerLang("smd_diode_optical", "Optical Diode");
    public static final ItemEntry<Item> SMD_RESISTOR_OPTICAL = registerLang("smd_resistor_optical", "Optical Resistor");
    public static final ItemEntry<Item> SMD_TRANSISTOR_OPTICAL = registerLang("smd_transistor_optical", "Optical Transistor");
    public static final ItemEntry<Item> SMD_INDUCTOR_OPTICAL = registerLang("smd_inductor_optical", "Optical Inductor");

    public static final ItemEntry<Item> EXOTIC_CIRCUIT_BOARD = register("exotic_circuit_board");
    public static final ItemEntry<Item> EXOTIC_PRINTED_CIRCUIT_BOARD = register("exotic_printed_circuit_board");
    public static final ItemEntry<Item> EXOTIC_RAM_WAFER = register("exotic_ram_wafer");
    public static final ItemEntry<Item> EXOTIC_RAM_CHIP = register("exotic_ram_chip");
    public static final ItemEntry<Item> SMD_CAPACITOR_EXOTIC = registerLang("smd_capacitor_exotic", "Exotic Capacitor");
    public static final ItemEntry<Item> SMD_DIODE_EXOTIC = registerLang("smd_diode_exotic", "Exotic Diode");
    public static final ItemEntry<Item> SMD_RESISTOR_EXOTIC = registerLang("smd_resistor_exotic", "Exotic Resistor");
    public static final ItemEntry<Item> SMD_TRANSISTOR_EXOTIC = registerLang("smd_transistor_exotic", "Exotic Transistor");
    public static final ItemEntry<Item> SMD_INDUCTOR_EXOTIC = registerLang("smd_inductor_exotic", "Exotic Inductor");

    public static final ItemEntry<Item> COSMIC_CIRCUIT_BOARD = register("cosmic_circuit_board");
    public static final ItemEntry<Item> COSMIC_PRINTED_CIRCUIT_BOARD = register("cosmic_printed_circuit_board");
    public static final ItemEntry<Item> COSMIC_RAM_WAFER = register("cosmic_ram_wafer");
    public static final ItemEntry<Item> COSMIC_RAM_CHIP = register("cosmic_ram_chip");
    public static final ItemEntry<Item> SMD_CAPACITOR_COSMIC = registerLang("smd_capacitor_cosmic", "Cosmic Capacitor");
    public static final ItemEntry<Item> SMD_DIODE_COSMIC = registerLang("smd_diode_cosmic", "Cosmic Diode");
    public static final ItemEntry<Item> SMD_RESISTOR_COSMIC = registerLang("smd_resistor_cosmic", "Cosmic Resistor");
    public static final ItemEntry<Item> SMD_TRANSISTOR_COSMIC = registerLang("smd_transistor_cosmic", "Cosmic Transistor");
    public static final ItemEntry<Item> SMD_INDUCTOR_COSMIC = registerLang("smd_inductor_cosmic", "Cosmic Inductor");

    public static final ItemEntry<Item> SUPRACAUSAL_CIRCUIT_BOARD = register("supracausal_circuit_board");
    public static final ItemEntry<Item> SUPRACAUSAL_PRINTED_CIRCUIT_BOARD = register("supracausal_printed_circuit_board");
    public static final ItemEntry<Item> SUPRACAUSAL_RAM_WAFER = register("supracausal_ram_wafer");
    public static final ItemEntry<Item> SUPRACAUSAL_RAM_CHIP = register("supracausal_ram_chip");
    public static final ItemEntry<Item> SMD_CAPACITOR_SUPRACAUSAL = registerLang("smd_capacitor_supracausal", "Supracausal Capacitor");
    public static final ItemEntry<Item> SMD_DIODE_SUPRACAUSAL = registerLang("smd_diode_supracausal", "Supracausal Diode");
    public static final ItemEntry<Item> SMD_RESISTOR_SUPRACAUSAL = registerLang("smd_resistor_supracausal", "Supracausal Resistor");
    public static final ItemEntry<Item> SMD_TRANSISTOR_SUPRACAUSAL = registerLang("smd_transistor_supracausal", "Supracausal Transistor");
    public static final ItemEntry<Item> SMD_INDUCTOR_SUPRACAUSAL = registerLang("smd_inductor_supracausal", "Supracausal Inductor");

    public static final ItemEntry<Item> UHV_VOLTAGE_COIL = registerLang("uhv_voltage_coil", "UHV Voltage Coil");
    public static final ItemEntry<Item> UEV_VOLTAGE_COIL = registerLang("uev_voltage_coil", "UEV Voltage Coil");
    public static final ItemEntry<Item> UIV_VOLTAGE_COIL = registerLang("uiv_voltage_coil", "UIV Voltage Coil");
    public static final ItemEntry<Item> UXV_VOLTAGE_COIL = registerLang("uxv_voltage_coil", "UXV Voltage Coil");
    public static final ItemEntry<Item> OPV_VOLTAGE_COIL = registerLang("opv_voltage_coil", "OpV Voltage Coil");
    public static final ItemEntry<Item> MAX_VOLTAGE_COIL = registerLang("max_voltage_coil", "MAX Voltage Coil");

    public static final ItemEntry<Item> SPACE_DRONE_MK1 = registerLang("space_drone_mk1", "Space Drone MK1");
    public static final ItemEntry<Item> SPACE_DRONE_MK2 = registerLang("space_drone_mk2", "Space Drone MK2");
    public static final ItemEntry<Item> SPACE_DRONE_MK3 = registerLang("space_drone_mk3", "Space Drone MK3");
    public static final ItemEntry<Item> SPACE_DRONE_MK4 = registerLang("space_drone_mk4", "Space Drone MK4");
    public static final ItemEntry<Item> SPACE_DRONE_MK5 = registerLang("space_drone_mk5", "Space Drone MK5");
    public static final ItemEntry<Item> SPACE_DRONE_MK6 = registerLang("space_drone_mk6", "Space Drone MK6");

    public static final ItemEntry<Item> ENTANGLED_SINGULARITY = registerCustomModel("entangled_singularity");
    public static final ItemEntry<Item> COSMIC_SINGULARITY = registerCustomModel("cosmic_singularity");
    public static final ItemEntry<Item> SPACETIME_CATALYST = registerCustomModel("spacetime_catalyst");
    public static final ItemEntry<Item> ETERNITY_CATALYST = registerCustomModel("eternity_catalyst");

    public static final ItemEntry<Item> COMBINED_SINGULARITY_0 = registerCustomModel("combined_singularity_0");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_1 = registerCustomModel("combined_singularity_1");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_2 = registerCustomModel("combined_singularity_2");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_3 = registerCustomModel("combined_singularity_3");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_4 = registerCustomModel("combined_singularity_4");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_5 = registerCustomModel("combined_singularity_5");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_6 = registerCustomModel("combined_singularity_6");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_7 = registerCustomModel("combined_singularity_7");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_8 = registerCustomModel("combined_singularity_8");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_9 = registerCustomModel("combined_singularity_9");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_10 = registerCustomModel("combined_singularity_10");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_11 = registerCustomModel("combined_singularity_11");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_12 = registerCustomModel("combined_singularity_12");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_13 = registerCustomModel("combined_singularity_13");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_14 = registerCustomModel("combined_singularity_14");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_15 = registerCustomModel("combined_singularity_15");

    public static final ItemEntry<Item> APATITE_VEIN = registerEssence("apatite_vein");
    public static final ItemEntry<Item> BANDED_IRON_VEIN = registerEssence("banded_iron_vein");
    public static final ItemEntry<Item> BAUXITE_VEIN_END = registerEssence("bauxite_vein_end");
    public static final ItemEntry<Item> BERYLLIUM_VEIN = registerEssence("beryllium_vein");
    public static final ItemEntry<Item> CASSITERITE_VEIN = registerEssence("cassiterite_vein");
    public static final ItemEntry<Item> CERTUS_QUARTZ = registerEssence("certus_quartz");
    public static final ItemEntry<Item> COAL_VEIN = registerEssence("coal_vein");
    public static final ItemEntry<Item> COPPER_TIN_VEIN = registerEssence("copper_tin_vein");
    public static final ItemEntry<Item> COPPER_VEIN = registerEssence("copper_vein");
    public static final ItemEntry<Item> DIAMOND_VEIN = registerEssence("diamond_vein");
    public static final ItemEntry<Item> GALENA_VEIN = registerEssence("galena_vein");
    public static final ItemEntry<Item> GARNET_TIN_VEIN = registerEssence("garnet_tin_vein");
    public static final ItemEntry<Item> GARNET_VEIN = registerEssence("garnet_vein");
    public static final ItemEntry<Item> IRON_VEIN = registerEssence("iron_vein");
    public static final ItemEntry<Item> LAPIS_VEIN = registerEssence("lapis_vein");
    public static final ItemEntry<Item> LUBRICANT_VEIN = registerEssence("lubricant_vein");
    public static final ItemEntry<Item> MAGNETITE_VEIN_END = registerEssence("magnetite_vein_end");
    public static final ItemEntry<Item> MAGNETITE_VEIN_OW = registerEssence("magnetite_vein_ow");
    public static final ItemEntry<Item> MANGANESE_VEIN = registerEssence("manganese_vein");
    public static final ItemEntry<Item> MANGANESE_VEIN_OW = registerEssence("manganese_vein_ow");
    public static final ItemEntry<Item> MICA_VEIN = registerEssence("mica_vein");
    public static final ItemEntry<Item> MINERAL_SAND_VEIN = registerEssence("mineral_sand_vein");
    public static final ItemEntry<Item> MOLYBDENUM_VEIN = registerEssence("molybdenum_vein");
    public static final ItemEntry<Item> MONAZITE_VEIN = registerEssence("monazite_vein");
    public static final ItemEntry<Item> NAQUADAH_VEIN = registerEssence("naquadah_vein");
    public static final ItemEntry<Item> NETHER_QUARTZ_VEIN = registerEssence("nether_quartz_vein");
    public static final ItemEntry<Item> NICKEL_VEIN = registerEssence("nickel_vein");
    public static final ItemEntry<Item> OILSANDS_VEIN = registerEssence("oilsands_vein");
    public static final ItemEntry<Item> OLIVINE_VEIN = registerEssence("olivine_vein");
    public static final ItemEntry<Item> PITCHBLEND_VEIN_END = registerEssence("pitchblende_vein_end");
    public static final ItemEntry<Item> REDSTONE_VEIN = registerEssence("redstone_vein");
    public static final ItemEntry<Item> REDSTONE_VEIN_OW = registerEssence("redstone_vein_ow");
    public static final ItemEntry<Item> SALTPETER_VEIN = registerEssence("saltpeter_vein");
    public static final ItemEntry<Item> SALTS_VEIN = registerEssence("salts_vein");
    public static final ItemEntry<Item> SAPPHIRE_VEIN = registerEssence("sapphire_vein");
    public static final ItemEntry<Item> SCHEELITE_VEIN = registerEssence("scheelite_vein");
    public static final ItemEntry<Item> SHELDONITE_VEIN = registerEssence("sheldonite_vein");
    public static final ItemEntry<Item> SULFUR_VEIN = registerEssence("sulfur_vein");
    public static final ItemEntry<Item> TETRAHEDRITE_VEIN = registerEssence("tetrahedrite_vein");
    public static final ItemEntry<Item> TOPAZ_VEIN = registerEssence("topaz_vein");

    public static final ItemEntry<Item> REACTOR_URANIUM_SIMPLE = register("reactor_uranium_simple");
    public static final ItemEntry<Item> REACTOR_URANIUM_DUAL = register("reactor_uranium_dual");
    public static final ItemEntry<Item> REACTOR_URANIUM_QUAD = register("reactor_uranium_quad");
    public static final ItemEntry<Item> DEPLETED_REACTOR_URANIUM_SIMPLE = register("depleted_reactor_uranium_simple");
    public static final ItemEntry<Item> DEPLETED_REACTOR_URANIUM_DUAL = register("depleted_reactor_uranium_dual");
    public static final ItemEntry<Item> DEPLETED_REACTOR_URANIUM_QUAD = register("depleted_reactor_uranium_quad");

    public static final ItemEntry<Item> REACTOR_THORIUM_SIMPLE = register("reactor_thorium_simple");
    public static final ItemEntry<Item> REACTOR_THORIUM_DUAL = register("reactor_thorium_dual");
    public static final ItemEntry<Item> REACTOR_THORIUM_QUAD = register("reactor_thorium_quad");
    public static final ItemEntry<Item> DEPLETED_REACTOR_THORIUM_SIMPLE = register("depleted_reactor_thorium_simple");
    public static final ItemEntry<Item> DEPLETED_REACTOR_THORIUM_DUAL = register("depleted_reactor_thorium_dual");
    public static final ItemEntry<Item> DEPLETED_REACTOR_THORIUM_QUAD = register("depleted_reactor_thorium_quad");

    public static final ItemEntry<Item> REACTOR_MOX_SIMPLE = register("reactor_mox_simple");
    public static final ItemEntry<Item> REACTOR_MOX_DUAL = register("reactor_mox_dual");
    public static final ItemEntry<Item> REACTOR_MOX_QUAD = register("reactor_mox_quad");
    public static final ItemEntry<Item> DEPLETED_REACTOR_MOX_SIMPLE = register("depleted_reactor_mox_simple");
    public static final ItemEntry<Item> DEPLETED_REACTOR_MOX_DUAL = register("depleted_reactor_mox_dual");
    public static final ItemEntry<Item> DEPLETED_REACTOR_MOX_QUAD = register("depleted_reactor_mox_quad");

    public static final ItemEntry<Item> REACTOR_NAQUADAH_SIMPLE = register("reactor_naquadah_simple");
    public static final ItemEntry<Item> REACTOR_NAQUADAH_DUAL = register("reactor_naquadah_dual");
    public static final ItemEntry<Item> REACTOR_NAQUADAH_QUAD = register("reactor_naquadah_quad");
    public static final ItemEntry<Item> DEPLETED_REACTOR_NAQUADAH_SIMPLE = register("depleted_reactor_naquadah_simple");
    public static final ItemEntry<Item> DEPLETED_REACTOR_NAQUADAH_DUAL = register("depleted_reactor_naquadah_dual");
    public static final ItemEntry<Item> DEPLETED_REACTOR_NAQUADAH_QUAD = register("depleted_reactor_naquadah_quad");

    public static final ItemEntry<Item> NEUTRONIUM_ANTIMATTER_FUEL_ROD = register("neutronium_antimatter_fuel_rod", "antimatter_fuel_rod");
    public static final ItemEntry<Item> DRACONIUM_ANTIMATTER_FUEL_ROD = register("draconium_antimatter_fuel_rod", "antimatter_fuel_rod");
    public static final ItemEntry<Item> COSMIC_NEUTRONIUM_ANTIMATTER_FUEL_ROD = register("cosmic_neutronium_antimatter_fuel_rod", "antimatter_fuel_rod");
    public static final ItemEntry<Item> INFINITY_ANTIMATTER_FUEL_ROD = register("infinity_antimatter_fuel_rod", "antimatter_fuel_rod");

    public static final ItemEntry<Item> SPACE_ESSENCE = register("space_essence");
    public static final ItemEntry<Item> BEDROCK_DESTROYER = register("bedrock_destroyer");
    public static final ItemEntry<Item> LEPTON_TRAP_CRYSTAL = register("lepton_trap_crystal");
    public static final ItemEntry<Item> CHARGED_LEPTON_TRAP_CRYSTAL = register("charged_lepton_trap_crystal");
    public static final ItemEntry<Item> QUANTUM_ANOMALY = register("quantum_anomaly");
    public static final ItemEntry<Item> HASSIUM_SEED_CRYSTAL = register("hassium_seed_crystal");
    public static final ItemEntry<Item> RAW_IMPRINTED_RESONATIC_CIRCUIT_BOARD = register("raw_imprinted_resonatic_circuit_board");
    public static final ItemEntry<Item> IMPRINTED_RESONATIC_CIRCUIT_BOARD = register("imprinted_resonatic_circuit_board");
    public static final ItemEntry<Item> ROTATING_TRANSPARENT_SURFACE = register("rotating_transparent_surface");
    public static final ItemEntry<Item> ELECTRON_SOURCE = register("electron_source");
    public static final ItemEntry<Item> ESSENCE = register("essence");
    public static final ItemEntry<Item> ESSENCE_SEED = register("essence_seed");
    public static final ItemEntry<Item> NUCLEAR_STAR = register("nuclear_star");
    public static final ItemEntry<Item> UNSTABLE_STAR = register("unstable_star");
    public static final ItemEntry<Item> PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK1 = register("precision_circuit_assembly_robot_mk1");
    public static final ItemEntry<Item> PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK2 = register("precision_circuit_assembly_robot_mk2");
    public static final ItemEntry<Item> PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK3 = register("precision_circuit_assembly_robot_mk3");
    public static final ItemEntry<Item> PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK4 = register("precision_circuit_assembly_robot_mk4");
    public static final ItemEntry<Item> PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK5 = register("precision_circuit_assembly_robot_mk5");
    public static final ItemEntry<Item> SCRAP = register("scrap");
    public static final ItemEntry<Item> SCRAP_BOX = register("scrap_box");
    public static final ItemEntry<Item> NUCLEAR_WASTE = register("nuclear_waste");
    public static final ItemEntry<Item> RESONATING_GEM = register("resonating_gem");
    public static final ItemEntry<Item> ECHOBONE = register("echobone");
    public static final ItemEntry<Item> NETHERITE_ROD = register("netherite_rod");
    public static final ItemEntry<Item> LONG_NETHERITE_ROD = register("long_netherite_rod");
    public static final ItemEntry<Item> MAGNETIC_NETHERITE_ROD = register("magnetic_netherite_rod");
    public static final ItemEntry<Item> MAGNETIC_LONG_NETHERITE_ROD = register("magnetic_long_netherite_rod");
    public static final ItemEntry<Item> PLASMA_CONTAINMENT_CELL = register("plasma_containment_cell");
    public static final ItemEntry<Item> RHENIUM_PLASMA_CONTAINMENT_CELL = register("rhenium_plasma_containment_cell");
    public static final ItemEntry<Item> ACTINIUM_SUPERHYDRIDE_PLASMA_CONTAINMENT_CELL = register("actinium_superhydride_plasma_containment_cell");
    public static final ItemEntry<Item> DRAGON_STEM_CELLS = register("dragon_stem_cells");
    public static final ItemEntry<Item> DRAGON_CELLS = register("dragon_cells");
    public static final ItemEntry<Item> BIOWARE_BOULE = register("bioware_boule");
    public static final ItemEntry<Item> BIOWARE_CHIP = register("bioware_chip");
    public static final ItemEntry<Item> BIOWARE_PROCESSING_CORE = register("bioware_processing_core");
    public static final ItemEntry<Item> BIOLOGICAL_CELLS = register("biological_cells");
    public static final ItemEntry<Item> OPTICAL_SOC = register("optical_soc");
    public static final ItemEntry<Item> OPTICAL_SOC_CONTAINMENT_HOUSING = register("optical_soc_containment_housing");
    public static final ItemEntry<Item> OPTICAL_SLICE = register("optical_slice");
    public static final ItemEntry<Item> OPTICAL_PROCESSING_CORE = register("optical_processing_core");
    public static final ItemEntry<Item> OPTICAL_WAFER = register("optical_wafer");
    public static final ItemEntry<Item> PHOTON_CARRYING_WAFER = register("photon_carrying_wafer");
    public static final ItemEntry<Item> RAW_PHOTON_CARRYING_WAFER = register("raw_photon_carrying_wafer");
    public static final ItemEntry<Item> EXOTIC_PROCESSING_CORE = register("exotic_processing_core");
    public static final ItemEntry<Item> COSMIC_PROCESSING_CORE = register("cosmic_processing_core");
    public static final ItemEntry<Item> SUPRACAUSAL_PROCESSING_CORE = register("supracausal_processing_core");
    public static final ItemEntry<Item> PERIODICALLY_POLED_LITHIUM_NIOBATE_BOULE = register("periodically_poled_lithium_niobate_boule");
    public static final ItemEntry<Item> NEUTRON_PLASMA_CONTAINMENT_CELL = register("neutron_plasma_containment_cell");
    public static final ItemEntry<Item> CRYSTALMATRIX_PLASMA_CONTAINMENT_CELL = register("crystalmatrix_plasma_containment_cell");
    public static final ItemEntry<Item> AWAKENED_DRACONIUM_PLASMA_CONTAINMENT_CELL = register("awakened_draconium_plasma_containment_cell");
    public static final ItemEntry<Item> HIGHLY_REFLECTIVE_MIRROR = register("highly_reflective_mirror");
    public static final ItemEntry<Item> LOW_FREQUENCY_LASER = register("low_frequency_laser");
    public static final ItemEntry<Item> MEDIUM_FREQUENCY_LASER = register("medium_frequency_laser");
    public static final ItemEntry<Item> HIGH_FREQUENCY_LASER = register("high_frequency_laser");
    public static final ItemEntry<Item> RED_HALIDE_LAMP = register("red_halide_lamp");
    public static final ItemEntry<Item> GREEN_HALIDE_LAMP = register("green_halide_lamp");
    public static final ItemEntry<Item> BLUE_HALIDE_LAMP = register("blue_halide_lamp");
    public static final ItemEntry<Item> BALLAST = register("ballast");
    public static final ItemEntry<Item> EMPTY_LASER_COOLING_CONTAINER = register("empty_laser_cooling_container");
    public static final ItemEntry<Item> HIGH_PRECISION_CRYSTAL_SOC = register("high_precision_crystal_soc");
    public static final ItemEntry<Item> BOSE_EINSTEIN_COOLING_CONTAINER = register("bose_einstein_cooling_container");
    public static final ItemEntry<Item> LASER_COOLING_UNIT = register("laser_cooling_unit");
    public static final ItemEntry<Item> LASER_DIODE = register("laser_diode");
    public static final ItemEntry<Item> MAGNETIC_TRAP = register("magnetic_trap");
    public static final ItemEntry<Item> RYDBERG_SPINORIAL_ASSEMBLY = register("rydberg_spinorial_assembly");
    public static final ItemEntry<Item> X_RAY_LASER = register("x_ray_laser");
    public static final ItemEntry<Item> CRYOGENIC_INTERFACE = register("cryogenic_interface");
    public static final ItemEntry<Item> EXOTIC_WAFER = register("exotic_wafer");
    public static final ItemEntry<Item> EXOTIC_CHIP = register("exotic_chip");
    public static final ItemEntry<Item> X_RAY_WAVEGUIDE = register("x_ray_waveguide");
    public static final ItemEntry<Item> X_RAY_MIRROR_PLATE = register("x_ray_mirror_plate");
    public static final ItemEntry<Item> COSMIC_PROCESSING_UNIT_CORE = register("cosmic_processing_unit_core");
    public static final ItemEntry<Item> ULTRASHORT_PULSE_LASER = register("ultrashort_pulse_laser");
    public static final ItemEntry<Item> DIFFRACTOR_GRATING_MIRROR = register("diffractor_grating_mirror");
    public static final ItemEntry<Item> GRATING_LITHOGRAPHY_MASK = register("grating_lithography_mask");
    public static final ItemEntry<Item> LITHOGRAPHY_MASK = register("lithography_mask");
    public static final ItemEntry<Item> PHOTOCOATED_HASSIUM_BOULE = register("photocoated_hassium_boule");
    public static final ItemEntry<Item> PHOTOCOATED_HASSIUM_WAFER = register("photocoated_hassium_wafer");
    public static final ItemEntry<Item> TIME_DILATION_CONTAINMENT_UNIT = register("time_dilation_containment_unit");
    public static final ItemEntry<Item> NEUTRONIUM_SPHERE = register("neutronium_sphere");
    public static final ItemEntry<Item> CHARGED_TRIPLET_NEUTRONIUM_SPHERE = register("charged_triplet_neutronium_sphere");
    public static final ItemEntry<Item> TRIPLET_NEUTRONIUM_SPHERE = register("triplet_neutronium_sphere");
    public static final ItemEntry<Item> CONTAINED_HIGH_DENSITY_PROTONIC_MATTER = register("contained_high_density_protonic_matter");
    public static final ItemEntry<Item> EXTREMELY_DURABLE_PLASMA_CELL = register("extremely_durable_plasma_cell");
    public static final ItemEntry<Item> DENSE_NEUTRON_PLASMA_CELL = register("dense_neutron_plasma_cell");
    public static final ItemEntry<Item> COSMIC_NEUTRON_PLASMA_CELL = register("cosmic_neutron_plasma_cell");
    public static final ItemEntry<Item> QUANTUMCHROMODYNAMIC_PROTECTIVE_PLATING = register("quantumchromodynamic_protective_plating");
    public static final ItemEntry<Item> CHAOS_CONTAINMENT_UNIT = register("chaos_containment_unit");
    public static final ItemEntry<Item> COSMIC_MESH_CONTAINMENT_UNIT = register("cosmic_mesh_containment_unit");
    public static final ItemEntry<Item> MICROWORMHOLE_GENERATOR = register("microwormhole_generator");
    public static final ItemEntry<Item> GRAVITON_TRANSDUCER = register("graviton_transducer");
    public static final ItemEntry<Item> CONTAINED_REISSNER_NORDSTROM_SINGULARITY = register("contained_reissner_nordstrom_singularity");
    public static final ItemEntry<Item> CONTAINED_KERR_NEWMANN_SINGULARITY = register("contained_kerr_newmann_singularity");
    public static final ItemEntry<Item> CONTAINED_KERR_SINGULARITY = register("contained_kerr_singularity");
    public static final ItemEntry<Item> CONTAINED_EXOTIC_MATTER = register("contained_exotic_matter");
    public static final ItemEntry<Item> MACROWORMHOLE_GENERATOR = register("macrowormhole_generator");
    public static final ItemEntry<Item> STABILIZED_WORMHOLE_GENERATOR = register("stabilized_wormhole_generator");
    public static final ItemEntry<Item> TOPOLOGICAL_MANIPULATOR_UNIT = register("topological_manipulator_unit");
    public static final ItemEntry<Item> RELATIVISTIC_SPINORIAL_MEMORY_SYSTEM = register("relativistic_spinorial_memory_system");
    public static final ItemEntry<Item> NUCLEAR_CLOCK = register("nuclear_clock");
    public static final ItemEntry<Item> SCINTILLATOR = register("scintillator");
    public static final ItemEntry<Item> SCINTILLATOR_CRYSTAL = register("scintillator_crystal");
    public static final ItemEntry<Item> MANIFOLD_OSCILLATORY_POWER_CELL = register("manifold_oscillatory_power_cell");
    public static final ItemEntry<Item> RECURSIVELY_FOLDED_NEGATIVE_SPACE = register("recursively_folded_negative_space");
    public static final ItemEntry<Item> EIGENFOLDED_KERR_MANIFOLD = register("eigenfolded_kerr_manifold");
    public static final ItemEntry<Item> CTC_COMPUTATIONAL_UNIT_CONTAINER = register("ctc_computational_unit_container");
    public static final ItemEntry<Item> CTC_COMPUTATIONAL_UNIT = register("ctc_computational_unit");
    public static final ItemEntry<Item> CTC_GUIDANCE_UNIT = register("ctc_guidance_unit");
    public static final ItemEntry<Item> HIGHLY_DENSE_POLYMER_PLATE = register("highly_dense_polymer_plate");
    public static final ItemEntry<Item> SPACE_PROBE_MK1 = register("space_probe_mk1");
    public static final ItemEntry<Item> SPACE_PROBE_MK2 = register("space_probe_mk2");
    public static final ItemEntry<Item> SPACE_PROBE_MK3 = register("space_probe_mk3");
    public static final ItemEntry<Item> HYPERCUBE = register("hypercube");
    public static final ItemEntry<Item> ANNIHILATION_CONSTRRAINER = register("annihilation_constrainer");
    public static final ItemEntry<Item> SOLAR_LIGHT_SPLITTER = register("solar_light_splitter");
    public static final ItemEntry<Item> CREATE_ULTIMATE_BATTERY = register("create_ultimate_battery");
    public static final ItemEntry<Item> SUPRACHRONAL_MAINFRAME_COMPLEX = register("suprachronal_mainframe_complex");
    public static final ItemEntry<Item> ZERO_POINT_MODULE_FRAGMENTS = register("zero_point_module_fragments");
    public static final ItemEntry<Item> TCETIESEAWEDEXTRACT = register("tcetieseaweedextract");
    public static final ItemEntry<Item> TCETIEDANDELIONS = register("tcetiedandelions");
    public static final ItemEntry<Item> WOVEN_KEVLAR = register("woven_kevlar");
    public static final ItemEntry<Item> KEVLAR_FIBER = register("kevlar_fiber");
    public static final ItemEntry<Item> INFUSED_BREATH = register("infused_breath");
    public static final ItemEntry<Item> WARPED_ENDER_PEAL = register("warped_ender_pearl");
    public static final ItemEntry<Item> CHAOS_SHARD = register("chaos_shard");
    public static final ItemEntry<Item> COSMIC_FABRIC = register("cosmic_fabric");
    public static final ItemEntry<Item> DRACONIC_CORE = register("draconic_core");
    public static final ItemEntry<Item> WYVERN_CORE = register("wyvern_core");
    public static final ItemEntry<Item> AWAKENED_CORE = register("awakened_core");
    public static final ItemEntry<Item> CHAOTIC_CORE = register("chaotic_core");
    public static final ItemEntry<Item> WYVERN_ENERGY_CORE = register("wyvern_energy_core");
    public static final ItemEntry<Item> DRACONIC_ENERGY_CORE = register("draconic_energy_core");
    public static final ItemEntry<Item> CHAOTIC_ENERGY_CORE = register("chaotic_energy_core");
    public static final ItemEntry<Item> DRACONIUM_DUST = register("draconium_dust");
    public static final ItemEntry<Item> DRAGON_HEART = register("dragon_heart");
    public static final ItemEntry<Item> STABILIZER_CORE = register("stabilizer_core");
    public static final ItemEntry<Item> DRAGON_STABILIZER_CORE = register("dragon_stabilizer_core");
    public static final ItemEntry<Item> RUTHERFORDIUM_NEUTRONIUM_BOULE = register("rutherfordium_neutronium_boule");
    public static final ItemEntry<Item> RUTHERFORDIUM_NEUTRONIUM_WAFER = register("rutherfordium_neutronium_wafer");
    public static final ItemEntry<Item> TARANIUM_BOULE = register("taranium_boule");
    public static final ItemEntry<Item> TARANIUM_WAFER = register("taranium_wafer");
    public static final ItemEntry<Item> PREPARED_COSMIC_SOC_WAFER = register("prepared_cosmic_soc_wafer");
    public static final ItemEntry<Item> COSMIC_SOC_WAFER = register("cosmic_soc_wafer");
    public static final ItemEntry<Item> COSMIC_SOC = register("cosmic_soc");
    public static final ItemEntry<Item> NM_WAFER = register("nm_wafer");
    public static final ItemEntry<Item> NM_CHIP = register("nm_chip");
    public static final ItemEntry<Item> PM_WAFER = register("pm_wafer");
    public static final ItemEntry<Item> PM_CHIP = register("pm_chip");
    public static final ItemEntry<Item> FM_WAFER = register("fm_wafer");
    public static final ItemEntry<Item> FM_CHIP = register("fm_chip");
    public static final ItemEntry<Item> FULLERENE_POLYMER_MATRIX_SOFT_TUBING = register("fullerene_polymer_matrix_soft_tubing");
    public static final ItemEntry<Item> FULLERENE_POLYMER_MATRIX_FINE_TUBING = register("fullerene_polymer_matrix_fine_tubing");
    public static final ItemEntry<Item> DUST_BLIZZ = register("dust_blizz");
    public static final ItemEntry<Item> DUST_CRYOTHEUM = register("dust_cryotheum");
    public static final ItemEntry<Item> REINFORCED_ECHO_SHARD = register("reinforced_echo_shard");
    public static final ItemEntry<Item> BEDROCK_DRILL = register("bedrock_drill");
    public static final ItemEntry<Item> MEMORY_FOAM_BLOCK = register("memory_foam_block");
    public static final ItemEntry<Item> GRAPHENE_IRON_PLATE = register("graphene_iron_plate");
    public static final ItemEntry<Item> INSULATION_WIRE_ASSEMBLY = register("insulation_wire_assembly");
    public static final ItemEntry<Item> AEROGRAPHENE = register("aerographene");
    public static final ItemEntry<Item> COMMAND_BLOCK_CORE = register("command_block_core");
    public static final ItemEntry<Item> CHAIN_COMMAND_BLOCK_CORE = register("chain_command_block_core");
    public static final ItemEntry<Item> REPEATING_COMMAND_BLOCK_CORE = register("repeating_command_block_core");
    public static final ItemEntry<Item> TWO_WAY_FOIL = register("two_way_foil");
    public static final ItemEntry<Item> HYPER_STABLE_SELF_HEALING_ADHESIVE = register("hyper_stable_self_healing_adhesive");
    public static final ItemEntry<Item> BLACK_BODY_NAQUADRIA_SUPERSOLID = register("black_body_naquadria_supersolid");
    public static final ItemEntry<Item> HEARTOFTHESMOGUS = register("heartofthesmogus");
    public static final ItemEntry<Item> VOID_MATTER = register("void_matter");
    public static final ItemEntry<Item> TEMPORAL_MATTER = register("temporal_matter");
    public static final ItemEntry<Item> PROTO_MATTER = register("proto_matter");
    public static final ItemEntry<Item> OMNI_MATTER = register("omni_matter");
    public static final ItemEntry<Item> KINETIC_MATTER = register("kinetic_matter");
    public static final ItemEntry<Item> ESSENTIA_MATTER = register("essentia_matter");
    public static final ItemEntry<Item> DARK_MATTER = register("dark_matter");
    public static final ItemEntry<Item> CORPOREAL_MATTER = register("corporeal_matter");
    public static final ItemEntry<Item> AMORPHOUS_MATTER = register("amorphous_matter");
    public static final ItemEntry<Item> PELLET_ANTIMATTER = register("pellet_antimatter");
    public static final ItemEntry<Item> DYSON_SWARM_MODULE = register("dyson_swarm_module");
    public static final ItemEntry<Item> GLACIO_SPIRIT = register("glacio_spirit");
    public static final ItemEntry<Item> TIMEPIECE = register("timepiece");
    public static final ItemEntry<Item> PRECISION_STEAM_MECHANISM = register("precision_steam_mechanism");
    public static final ItemEntry<Item> INVERTER = register("inverter");
    public static final ItemEntry<Item> GIGA_CHAD = register("giga_chad");
    public static final ItemEntry<Item> REACTOR_FUEL_ROD = register("reactor_fuel_rod");
    public static final ItemEntry<Item> TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD = register("tungsten_carbide_reactor_fuel_rod");
    public static final ItemEntry<Item> HUI_CIRCUIT_1 = register("hui_circuit_1");
    public static final ItemEntry<Item> HUI_CIRCUIT_2 = register("hui_circuit_2");
    public static final ItemEntry<Item> HUI_CIRCUIT_3 = register("hui_circuit_3");
    public static final ItemEntry<Item> HUI_CIRCUIT_4 = register("hui_circuit_4");
    public static final ItemEntry<Item> HUI_CIRCUIT_5 = register("hui_circuit_5");
    public static final ItemEntry<Item> SPECIAL_CERAMICS = register("special_ceramics");
    public static final ItemEntry<Item> HYPERDIMENSIONAL_DRONE = register("hyperdimensional_drone");
    public static final ItemEntry<Item> FISHBIG_BODY = register("fishbig_body");
    public static final ItemEntry<Item> FISHBIG_HADE = register("fishbig_hade");
    public static final ItemEntry<Item> FISHBIG_HAIR = register("fishbig_hair");
    public static final ItemEntry<Item> FISHBIG_LHAND = register("fishbig_lhand");
    public static final ItemEntry<Item> FISHBIG_LLEG = register("fishbig_lleg");
    public static final ItemEntry<Item> FISHBIG_RHAND = register("fishbig_rhand");
    public static final ItemEntry<Item> FISHBIG_RLEG = register("fishbig_rleg");
    public static final ItemEntry<Item> FISHBIG_FABRIC = register("fishbig_fabric");
    public static final ItemEntry<Item> FISHBIG_FRAME = register("fishbig_frame");

    public static final ItemEntry<Item> HOT_IRON_INGOT = registerCustomModel("hot_iron_ingot");
    public static final ItemEntry<Item> RAW_VACUUM_TUBE = registerCustomModel("raw_vacuum_tube");
    public static final ItemEntry<Item> INFINITE_CELL_COMPONENT = register("infinite_cell_component");
    public static final ItemEntry<Item> PROTONATED_FULLERENE_SIEVING_MATRIX = register("protonated_fullerene_sieving_matrix");
    public static final ItemEntry<Item> SATURATED_FULLERENE_SIEVING_MATRIX = register("saturated_fullerene_sieving_matrix");
    public static final ItemEntry<Item> MICROFOCUS_X_RAY_TUBE = register("microfocus_x_ray_tube");
    public static final ItemEntry<Item> SEPARATION_ELECTROMAGNET = register("separation_electromagnet");
    public static final ItemEntry<Item> HIGHLY_INSULATING_FOIL = registerCustomModel("highly_insulating_foil");
    public static final ItemEntry<Item> STERILIZED_PETRI_DISH = registerCustomModel("sterilized_petri_dish");
    public static final ItemEntry<Item> ELECTRICALY_WIRED_PETRI_DISH = registerCustomModel("electricaly_wired_petri_dish");
    public static final ItemEntry<Item> CONTAMINATED_PETRI_DISH = register("contaminated_petri_dish");
    public static final ItemEntry<Item> BREVIBACTERIUM_PETRI_DISH = register("brevibacterium_petri_dish", "germ");
    public static final ItemEntry<Item> BIFIDOBACTERIUMM_PETRI_DISH = register("bifidobacteriumm_petri_dish", "germ");
    public static final ItemEntry<Item> ESCHERICIA_PETRI_DISH = register("eschericia_petri_dish", "germ");
    public static final ItemEntry<Item> STREPTOCOCCUS_PETRI_DISH = register("streptococcus_petri_dish", "germ");
    public static final ItemEntry<Item> CUPRIAVIDUS_PETRI_DISH = register("cupriavidus_petri_dish", "germ");
    public static final ItemEntry<Item> SHEWANELLA_PETRI_DISH = register("shewanella_petri_dish", "germ");

    public static final ItemEntry<Item> CONVERSION_SIMULATE_CARD = register("conversion_simulate_card");
    public static final ItemEntry<Item> ACTIVATED_CARBON_FILTER_MESH = register("activated_carbon_filter_mesh");
    public static final ItemEntry<Item> USED_ACTIVATED_CARBON_FILTER_MESH = register("used_activated_carbon_filter_mesh", "activated_carbon_filter_mesh");
    public static final ItemEntry<Item> EMPTY_QUARK_RELEASE_CATALYST_HOUSING = register("empty_quark_release_catalyst_housing");
    public static final ItemEntry<Item> UNALIGNED_QUARK_RELEASING_CATALYST = register("unaligned_quark_releasing_catalyst");
    public static final ItemEntry<Item> UP_QUARK_RELEASING_CATALYST = register("up_quark_releasing_catalyst");
    public static final ItemEntry<Item> DOWN_QUARK_RELEASING_CATALYST = register("down_quark_releasing_catalyst");
    public static final ItemEntry<Item> BOTTOM_QUARK_RELEASING_CATALYST = register("bottom_quark_releasing_catalyst");
    public static final ItemEntry<Item> TOP_QUARK_RELEASING_CATALYST = register("top_quark_releasing_catalyst");
    public static final ItemEntry<Item> STRANGE_QUARK_RELEASING_CATALYST = register("strange_quark_releasing_catalyst");
    public static final ItemEntry<Item> CHARM_QUARK_RELEASING_CATALYST = register("charm_quark_releasing_catalyst");

    // 魔法物品
    public static final ItemEntry<Item> MAGIC_CORE_SHELL1 = registerMagic("magic_core_shell1");
    public static final ItemEntry<Item> MAGIC_CORE_SHELL2 = registerMagic("magic_core_shell2");
    public static final ItemEntry<Item> MAGIC_CORE_SHELL3 = registerMagic("magic_core_shell3");
    public static final ItemEntry<Item> MAGIC_CORE1 = registerMagic("magic_core1");
    public static final ItemEntry<Item> MAGIC_CORE2 = registerMagic("magic_core2");
    public static final ItemEntry<Item> MAGIC_CORE3 = registerMagic("magic_core3");
    public static final ItemEntry<Item> ARCANE_CORE1 = registerMagic("arcane_core1");
    public static final ItemEntry<Item> ARCANE_CORE2 = registerMagic("arcane_core2");

    public static final ItemEntry<Item> DIPPING_STICKS = registerMagic("dipping_sticks");
    public static final ItemEntry<Item> MAGIC_FILTER = registerMagic("magic_filter");

    public static final ItemEntry<Item> THE_FLAME_OF_ORIGIN = registerMagic("the_flame_of_origin");
    public static final ItemEntry<Item> THE_FIRE_OF_INFERNO = registerMagic("the_fire_of_inferno");
    public static final ItemEntry<Item> BLAZING_VEIN = registerMagic("blazing_vein");

    public static final ItemEntry<Item> BALANCE_MAGIC_CRYSTAL = registerMagic("balance_magic_crystal");
    public static final ItemEntry<Item> WORLD_SALT = registerMagic("world_salt");

    public static final ItemEntry<Item> PRIMAL_MAGIC = registerMagic("primal_magic");
    public static final ItemEntry<Item> PEARL_ORIGINAL = registerMagic("pearl_original");

    public static final ItemEntry<Item> SOUL_FRAGMENT = registerMagic("soul_fragment");
    public static final ItemEntry<Item> SOUL_CRYSTAL_SMALL = registerMagic("soul_crystal_small");
    public static final ItemEntry<Item> SOUL_CRYSTAL_MEDIUM = registerMagic("soul_crystal_medium");
    public static final ItemEntry<Item> SOUL_CRYSTAL_LARGE = registerMagic("soul_crystal_large");
    public static final ItemEntry<Item> SOUL_CRYSTAL_COMPLETE = registerMagic("soul_crystal_complete");
    public static final ItemEntry<Item> SOUL_COIN = registerMagic("soul_coin");

    public static final ItemEntry<Item> MAGICAL_RESIDUE = registerMagic("magical_residue");
    public static final ItemEntry<Item> ARCANE_RESIDUE = registerMagic("arcane_residue");

    public static final ItemEntry<Item> CONDENSED_MAGIC_SOURCE = registerMagic("condensed_magic_source");
    public static final ItemEntry<Item> CRUSHED_CONDENSED_MAGIC_SOURCE = registerMagic("crushed_condensed_magic_source");
    public static final ItemEntry<Item> CONCENTRATED_MAGIC_ESSENCE_CRYSTAL = registerMagic("concentrated_magic_essence_crystal");
    public static final ItemEntry<Item> IMPURE_ARCANE_ELEMENTIUM_CRYSTAL = registerMagic("impure_arcane_elementium_crystal");
    public static final ItemEntry<Item> CRYSTALS_CONTAINING_THE_BREATH_OF_THE_ABYSS = registerMagic("crystals_containing_the_breath_of_the_abyss");
    public static final ItemEntry<Item> CRYSTALS_CONTAINING_THE_BREATH_OF_THE_SUN = registerMagic("crystals_containing_the_breath_of_the_sun");
    public static final ItemEntry<Item> BRIGHT_LARGE_PIECE_OF_MAGIC_CRYSTAL = registerMagic("bright_large_piece_of_magic_crystal");
    public static final ItemEntry<Item> ARCANE_SIEVE = registerMagic("arcane_sieve");
    public static final ItemEntry<Item> SATURATED_ARCANE_SIEVE = registerMagic("saturated_arcane_sieve");
}
