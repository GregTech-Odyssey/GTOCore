package com.gto.gtocore.common.data;

import com.gto.gtocore.api.item.ToolTipsItem;
import com.gto.gtocore.client.renderer.item.HaloItemRenderer;
import com.gto.gtocore.client.renderer.item.OrderItemProviderRenderer;
import com.gto.gtocore.common.cover.PowerAmplifierCover;
import com.gto.gtocore.common.item.*;
import com.gto.gtocore.common.item.armor.SpaceArmorComponentItem;
import com.gto.gtocore.common.item.tools.GrassHarvesterBehaviour;
import com.gto.gtocore.config.GTOConfig;
import com.gto.gtocore.integration.ae2.InfinityCellItem;
import com.gto.gtocore.utils.StringUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.item.ComponentItem;
import com.gregtechceu.gtceu.api.item.component.ElectricStats;
import com.gregtechceu.gtceu.common.data.GTFluids;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.item.CoverPlaceBehavior;
import com.gregtechceu.gtceu.common.item.DataItemBehavior;
import com.gregtechceu.gtceu.common.item.TooltipBehavior;
import com.gregtechceu.gtceu.common.item.armor.*;
import com.gregtechceu.gtceu.config.ConfigHolder;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

import appeng.api.stacks.AEKeyType;
import appeng.items.materials.StorageComponentItem;
import appeng.items.storage.BasicStorageCell;
import appeng.items.storage.StorageTier;
import appeng.items.tools.powered.PortableCellItem;
import appeng.menu.me.common.MEStorageMenu;
import com.enderio.base.common.init.EIOFluids;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import earth.terrarium.adastra.common.registry.ModFluids;

import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gto.gtocore.utils.register.ItemRegisterUtils.*;

public interface GTOItems {

    static void init() {
        GTMaterials.Oxygen.getProperty(PropertyKey.FLUID).getStorage().store(FluidStorageKeys.GAS, ModFluids.OXYGEN, null);
        GTMaterials.Hydrogen.getProperty(PropertyKey.FLUID).getStorage().store(FluidStorageKeys.GAS, ModFluids.HYDROGEN, null);
        GTFluids.handleNonMaterialFluids(GTMaterials.Oil, ModFluids.OIL);
        GTFluids.handleNonMaterialFluids(GTMaterials.RocketFuel, () -> EIOFluids.ROCKET_FUEL.get().getSource());

        if (GTOConfig.INSTANCE.dev) {
            item("recipe_editor", "配方编辑器", ComponentItem::create)
                    .properties(p -> p.stacksTo(1))
                    .onRegister(attach(RecipeEditorBehavior.INSTANCE))
                    .model(NonNullBiConsumer.noop())
                    .register();

            item("debug_structure_writer", "多方块结构导出工具", ComponentItem::create)
                    .properties(p -> p.stacksTo(1))
                    .onRegister(attach(StructureWriteBehavior.INSTANCE))
                    .model(NonNullBiConsumer.noop())
                    .register();
        }
    }

    ItemEntry<StorageComponentItem> CELL_COMPONENT_1M = registerStorageComponentItem(1);
    ItemEntry<StorageComponentItem> CELL_COMPONENT_4M = registerStorageComponentItem(4);
    ItemEntry<StorageComponentItem> CELL_COMPONENT_16M = registerStorageComponentItem(16);
    ItemEntry<StorageComponentItem> CELL_COMPONENT_64M = registerStorageComponentItem(64);
    ItemEntry<StorageComponentItem> CELL_COMPONENT_256M = registerStorageComponentItem(256);

    ItemEntry<BasicStorageCell> ITEM_STORAGE_CELL_1M = registerStorageCell(1, CELL_COMPONENT_1M, true);
    ItemEntry<BasicStorageCell> ITEM_STORAGE_CELL_4M = registerStorageCell(4, CELL_COMPONENT_4M, true);
    ItemEntry<BasicStorageCell> ITEM_STORAGE_CELL_16M = registerStorageCell(16, CELL_COMPONENT_16M, true);
    ItemEntry<BasicStorageCell> ITEM_STORAGE_CELL_64M = registerStorageCell(64, CELL_COMPONENT_64M, true);
    ItemEntry<BasicStorageCell> ITEM_STORAGE_CELL_256M = registerStorageCell(256, CELL_COMPONENT_256M,
            true);

    ItemEntry<BasicStorageCell> FLUID_STORAGE_CELL_1M = registerStorageCell(1, CELL_COMPONENT_1M, false);
    ItemEntry<BasicStorageCell> FLUID_STORAGE_CELL_4M = registerStorageCell(4, CELL_COMPONENT_4M, false);
    ItemEntry<BasicStorageCell> FLUID_STORAGE_CELL_16M = registerStorageCell(16, CELL_COMPONENT_16M, false);
    ItemEntry<BasicStorageCell> FLUID_STORAGE_CELL_64M = registerStorageCell(64, CELL_COMPONENT_64M, false);
    ItemEntry<BasicStorageCell> FLUID_STORAGE_CELL_256M = registerStorageCell(256, CELL_COMPONENT_256M,
            false);

    ItemEntry<PortableCellItem> SUPER_PORTABLE_ITEM_CELL = item("super_portable_item_storage_cell", "超级便携物品存储元件", p -> new PortableCellItem(AEKeyType.items(),
            256,
            MEStorageMenu.PORTABLE_ITEM_CELL_TYPE,
            new StorageTier(100, "super", Integer.MAX_VALUE, 100, WETWARE_MAINFRAME_UHV),
            p.stacksTo(1), 0xDDDDDD))
            .register();

    ItemEntry<PortableCellItem> SUPER_PORTABLE_FLUID_CELL = item("super_portable_fluid_storage_cell", "超级便携流体存储元件", p -> new PortableCellItem(AEKeyType.fluids(),
            256,
            MEStorageMenu.PORTABLE_ITEM_CELL_TYPE,
            new StorageTier(100, "super", Integer.MAX_VALUE, 100, WETWARE_MAINFRAME_UHV),
            p.stacksTo(1), 0xFF6D36))
            .register();

    ItemEntry<InfinityCellItem> ITEM_INFINITY_CELL = item("item_infinity_cell", "无限物品存储元件", p -> new InfinityCellItem(AEKeyType.items())).register();
    ItemEntry<InfinityCellItem> FLUID_INFINITY_CELL = item("fluid_infinity_cell", "无限流体存储元件", p -> new InfinityCellItem(AEKeyType.fluids())).register();
    ItemEntry<ComponentItem> ORDER = item("order", "%s 订单", ComponentItem::create)
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(OrderItem.INSTANCE))
            .onRegister(attachRenderer(() -> OrderItemProviderRenderer.INSTANCE))
            .register();
    ItemEntry<ComponentItem> REALLY_MAX_BATTERY = item("really_max_battery", "真·终极电池", ComponentItem::create)
            .lang("Really MAX Battery")
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.translatable("gtocore.tooltip.item.really_max_battery").withStyle(ChatFormatting.GRAY)))))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, GTValues.UEV)))
            .register();
    ItemEntry<ComponentItem> TRANSCENDENT_MAX_BATTERY = item("transcendent_max_battery", "超·终极电池", ComponentItem::create)
            .lang("Transcendent MAX Battery")
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.translatable("gtocore.tooltip.item.transcendent_max_battery").withStyle(ChatFormatting.GRAY)))))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, GTValues.UIV)))
            .register();
    ItemEntry<ComponentItem> EXTREMELY_MAX_BATTERY = item("extremely_max_battery", "极·终极电池", ComponentItem::create)
            .lang("Extremely MAX Battery")
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.translatable("gtocore.tooltip.item.extremely_max_battery").withStyle(ChatFormatting.GRAY)))))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, GTValues.UXV)))
            .register();
    ItemEntry<ComponentItem> INSANELY_MAX_BATTERY = item("insanely_max_battery", "狂·终极电池", ComponentItem::create)
            .lang("Insanely MAX Battery")
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.literal(StringUtils.dark_purplish_red(LocalizationUtils.format("gtocore.tooltip.item.insanely_max_battery")))))))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, GTValues.OpV)))
            .register();
    ItemEntry<ComponentItem> MEGA_MAX_BATTERY = item("mega_max_battery", "兆·终极电池", ComponentItem::create)
            .lang("Mega MAX Battery")
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.literal(StringUtils.full_color(LocalizationUtils.format("gtocore.tooltip.item.mega_max_battery")))))))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, GTValues.MAX)))
            .register();

    ItemEntry<ComponentItem> SUPER_CAPACITOR = item("super_capacitor", "超级电容", ComponentItem::create)
            .onRegister(attach(ElectricStats.createRechargeableBattery(100000, GTValues.ULV)))
            .tag(CustomTags.ULV_BATTERIES).register();

    ItemEntry<ComponentItem> HV_DRONE = item("hv_drone", "基础无人机", ComponentItem::create)
            .lang("Base Drone")
            .properties(p -> p.stacksTo(1))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(new DroneBehavior(GTValues.HV)))
            .register();

    ItemEntry<ComponentItem> EV_DRONE = item("ev_drone", "高级无人机", ComponentItem::create)
            .lang("Advanced Drone")
            .properties(p -> p.stacksTo(1))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(new DroneBehavior(GTValues.EV)))
            .register();

    ItemEntry<ComponentItem> IV_DRONE = item("iv_drone", "终极无人机", ComponentItem::create)
            .lang("Ultimate Drone")
            .properties(p -> p.stacksTo(1))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(new DroneBehavior(GTValues.IV)))
            .register();

    ItemEntry<ComponentItem> MAX_ELECTRIC_PUMP = item("max_electric_pump", "§4§lMAX§r电动泵", ComponentItem::create)
            .lang("MAX Electric Pump")
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.ELECTRIC_PUMP_MAX)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.electric.pump.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.fluid_transfer_rate",
                        1280 * 64 * 64 * 4 / 20));
            })))
            .register();

    ItemEntry<ComponentItem> MAX_CONVEYOR_MODULE = item("max_conveyor_module", "§4§lMAX§r传送带", ComponentItem::create)
            .lang("MAX Conveyor Module")
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.CONVEYOR_MODULE_MAX)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.conveyor.module.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.item_transfer_rate_stacks", 16));
            })))
            .register();

    ItemEntry<ComponentItem> MAX_ROBOT_ARM = item("max_robot_arm", "§4§lMAX§r机械臂", ComponentItem::create)
            .lang("MAX Robot Arm")
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.ROBOT_ARM_MAX)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.robot.arm.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.item_transfer_rate_stacks", 16));
            })))
            .register();

    ItemEntry<Item> MAX_ELECTRIC_MOTOR = registerLang("max_electric_motor", "MAX Electric Motor", "§4§lMAX§r电动马达");
    ItemEntry<Item> MAX_ELECTRIC_PISTON = registerLang("max_electric_piston", "MAX Electric Piston", "§4§lMAX§r电力活塞");
    ItemEntry<Item> MAX_FIELD_GENERATOR = registerLang("max_field_generator", "MAX Field Generator", "§4§lMAX§r力场发生器");
    ItemEntry<Item> MAX_EMITTER = registerLang("max_emitter", "MAX Emitte", "§4§lMAX§r发射器");
    ItemEntry<Item> MAX_SENSOR = registerLang("max_sensor", "MAX Sensor", "§4§lMAX§r传感器");

    ItemEntry<ComponentItem> ULV_ELECTRIC_PUMP = item("ulv_electric_pump", "ULV电动泵", ComponentItem::create)
            .lang("ULV Electric Pump")
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.ELECTRIC_PUMP_ULV)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.electric.pump.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.fluid_transfer_rate", (1280 / 2) / 20));
            })))
            .register();

    ItemEntry<ComponentItem> ULV_CONVEYOR_MODULE = item("ulv_conveyor_module", "ULV传送带", ComponentItem::create)
            .lang("ULV Conveyor Module")
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.CONVEYOR_MODULE_ULV)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.conveyor.module.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.item_transfer_rate", 2));
            })))
            .register();

    ItemEntry<ComponentItem> ULV_FLUID_REGULATOR = item("ulv_fluid_regulator", "ULV流体校准器", ComponentItem::create)
            .lang("ULV Fluid Regulator")
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.FLUID_REGULATOR_ULV)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.fluid.regulator.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.fluid_transfer_rate", (1280 / 2) / 20));
            })))
            .register();

    ItemEntry<ComponentItem> ULV_ROBOT_ARM = item("ulv_robot_arm", "ULV机械臂", ComponentItem::create)
            .lang("ULV Robot Arm")
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.ROBOT_ARM_ULV)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.robot.arm.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.item_transfer_rate", 2));
            })))
            .register();

    ItemEntry<Item> ULV_ELECTRIC_MOTOR = registerLang("ulv_electric_motor", "ULV Electric Motor", "ULV电动马达");
    ItemEntry<Item> ULV_ELECTRIC_PISTON = registerLang("ulv_electric_piston", "ULV Electric Piston", "ULV电力活塞");

    ItemEntry<ComponentItem> LV_POWER_AMPLIFIERS = item("lv_power_amplifiers", "LV功率增幅器", ComponentItem::create)
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(PowerAmplifierCover.getReduction(1))))), new CoverPlaceBehavior(GTOCovers.POWER_AMPLIFIERS[0])))
            .register();

    ItemEntry<ComponentItem> MV_POWER_AMPLIFIERS = item("mv_power_amplifiers", "MV功率增幅器", ComponentItem::create)
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(PowerAmplifierCover.getReduction(2))))), new CoverPlaceBehavior(GTOCovers.POWER_AMPLIFIERS[1])))
            .register();

    ItemEntry<ComponentItem> HV_POWER_AMPLIFIERS = item("hv_power_amplifiers", "HV功率增幅器", ComponentItem::create)
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(PowerAmplifierCover.getReduction(3))))), new CoverPlaceBehavior(GTOCovers.POWER_AMPLIFIERS[2])))
            .register();

    ItemEntry<ComponentItem> EV_POWER_AMPLIFIERS = item("ev_power_amplifiers", "EV功率增幅器", ComponentItem::create)
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(PowerAmplifierCover.getReduction(4))))), new CoverPlaceBehavior(GTOCovers.POWER_AMPLIFIERS[3])))
            .register();

    ItemEntry<ComponentItem> AIR_VENT = item("air_vent", "通风口", ComponentItem::create)
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.translatable("gtceu.universal.tooltip.produces_fluid", 10))), new CoverPlaceBehavior(GTOCovers.AIR_VENT)))
            .register();

    ItemEntry<ComponentItem> STEAM_PUMP = item("steam_pump", "蒸汽泵", ComponentItem::create)
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.electric.pump.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.fluid_transfer_rate", FormattingUtil.formatNumbers(1000000)));
            }), new CoverPlaceBehavior(GTOCovers.STEAM_PUMP)))
            .register();

    ItemEntry<ComponentItem> WIRELESS_ENERGY_RECEIVE_COVER_MAX = registerTieredCover(1);

    ItemEntry<ComponentItem> WIRELESS_ENERGY_RECEIVE_COVER_MAX_4A = registerTieredCover(4);

    ItemEntry<ComponentItem> TIME_TWISTER = item("time_twister", "时间扭曲者", ComponentItem::create)
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(TimeTwisterBehavior.INSTANCE))
            .register();

    ItemEntry<ComponentItem> STRUCTURE_DETECT = item("structure_detect", "结构检测工具", ComponentItem::create)
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(StructureDetectBehavior.INSTANCE))
            .model(NonNullBiConsumer.noop())
            .register();

    ItemEntry<ComponentItem> PATTERN_MODIFIER_PRO = item("pattern_modifier_pro", "样板修改器pro", ComponentItem::create)
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(PatternModifierProBehavior.INSTANCE))
            .model(NonNullBiConsumer.noop())
            .register();

    ItemEntry<BucketItem> GELID_CRYOTHEUM_BUCKET = item("gelid_cryotheum_bucket", "极寒之凛冰桶", p -> new BucketItem(GTOFluids.GELID_CRYOTHEUM, p.craftRemainder(Items.BUCKET).stacksTo(1).rarity(Rarity.COMMON)))
            .model(NonNullBiConsumer.noop())
            .register();

    ItemEntry<Item> COMMAND_WAND = item("command_wand", "命令权杖", Item::new)
            .properties(p -> p.stacksTo(1))
            .model(NonNullBiConsumer.noop())
            .register();

    ItemEntry<Item> GRINDBALL_SOAPSTONE = item("grindball_soapstone", "皂石研磨球", Item::new)
            .properties(p -> p.stacksTo(1).defaultDurability(50))
            .register();

    ItemEntry<Item> GRINDBALL_ALUMINIUM = item("grindball_aluminium", "铝研磨球", Item::new)
            .properties(p -> p.stacksTo(1).defaultDurability(100))
            .register();

    ItemEntry<PlanetDataChipItem> PLANET_DATA_CHIP = item("planet_data_chip", "星球数据芯片", PlanetDataChipItem::new)
            .register();

    ItemEntry<DimensionDataItem> DIMENSION_DATA = item("dimension_data", "维度数据", DimensionDataItem::new).register();

    ItemEntry<ComponentItem> OPTICAL_DATA_STICK = item("optical_data_stick", "光学闪存", ComponentItem::create)
            .onRegister(attach(new DataItemBehavior(true)))
            .register();

    ItemEntry<ComponentItem> CLOSED_TIMELIKE_CURVE_GUIDANCE_UNIT = item("closed_timelike_curve_guidance_unit", "封闭类时曲线引导单元", ComponentItem::create)
            .onRegister(attach(new DataItemBehavior(true)))
            .register();

    ItemEntry<DiscItem> DATA_DISC = item("data_disc", "数据光盘", DiscItem::new)
            .register();

    ItemEntry<ComponentItem> NEURAL_MATRIX = item("neural_matrix", "神经矩阵", ComponentItem::create)
            .onRegister(attach(new DataItemBehavior(true)))
            .register();// UV生物活性CPU解锁制造,UHV光学主机后开始使用

    ItemEntry<ComponentItem> ATOMIC_ARCHIVES = item("atomic_archives", "原子档案", ComponentItem::create)
            .onRegister(attachRenderer(() -> HaloItemRenderer.QUANTUM_CHROMO_DYNAMICALLY_HALO))
            .onRegister(attach(new DataItemBehavior(true)))
            .register();// UEV奇异CPU后解锁,UIV中期开始使用

    ItemEntry<ComponentItem> OBSIDIAN_MATRIX = item("obsidian_matrix", "黑曜石矩阵", ComponentItem::create)
            .onRegister(attach(new DataItemBehavior(true)))
            .register();// UXV暗物质后解锁,UXV后期开始使用

    ItemEntry<ComponentItem> MICROCOSM = item("microcosm", "微缩宇宙", ComponentItem::create)
            .onRegister(attachRenderer(() -> HaloItemRenderer.COSMIC_HALO))
            .onRegister(attach(new DataItemBehavior(true)))
            .register();// OPV鸿蒙之眼后解锁,MAX初期开使用

    ItemEntry<KineticRotorItem> WOOD_ROTOR = registerRotor("wood_kinetic_rotor", "木", 2400, 4, 10, 0);
    ItemEntry<KineticRotorItem> IRON_ROTOR = registerRotor("iron_kinetic_rotor", "铁", 14000, 10, 20, 1);
    ItemEntry<KineticRotorItem> STEEL_ROTOR = registerRotor("steel_kinetic_rotor", "钢", 16000, 10, 30, 1);
    ItemEntry<KineticRotorItem> CARBON_ROTOR = registerRotor("carbon_kinetic_rotor", "碳", 24000, 2, 40, 2);

    ItemEntry<ToolTipsItem> BIOWARE_PROCESSOR = registerCircuit("bioware_processor", "生物活性处理器", CustomTags.ZPM_CIRCUITS, () -> StringUtils.dark_green(I18n.get("gtocore.tooltip.item.tier_circuit", "ZPM")));
    ItemEntry<ToolTipsItem> BIOWARE_ASSEMBLY = registerCircuit("bioware_assembly", "生物活性处理器集群", CustomTags.UV_CIRCUITS, () -> StringUtils.dark_green(I18n.get("gtocore.tooltip.item.tier_circuit", "UV")));
    ItemEntry<ToolTipsItem> BIOWARE_COMPUTER = registerCircuit("bioware_computer", "生物活性处理器超级计算机", CustomTags.UHV_CIRCUITS, () -> StringUtils.dark_green(I18n.get("gtocore.tooltip.item.tier_circuit", "UHV")));
    ItemEntry<ToolTipsItem> BIOWARE_MAINFRAME = registerCircuit("bioware_mainframe", "生物活性处理器主机", CustomTags.UEV_CIRCUITS, () -> StringUtils.dark_green(I18n.get("gtocore.tooltip.item.tier_circuit", "UEV")));

    ItemEntry<ToolTipsItem> OPTICAL_PROCESSOR = registerCircuit("optical_processor", "光学处理器", CustomTags.UV_CIRCUITS, () -> StringUtils.golden(I18n.get("gtocore.tooltip.item.tier_circuit", "UV")));
    ItemEntry<ToolTipsItem> OPTICAL_ASSEMBLY = registerCircuit("optical_assembly", "光学处理器集群", CustomTags.UHV_CIRCUITS, () -> StringUtils.golden(I18n.get("gtocore.tooltip.item.tier_circuit", "UHV")));
    ItemEntry<ToolTipsItem> OPTICAL_COMPUTER = registerCircuit("optical_computer", "光学处理器超级计算机", CustomTags.UEV_CIRCUITS, () -> StringUtils.golden(I18n.get("gtocore.tooltip.item.tier_circuit", "UEV")));
    ItemEntry<ToolTipsItem> OPTICAL_MAINFRAME = registerCircuit("optical_mainframe", "光学处理器主机", CustomTags.UIV_CIRCUITS, () -> StringUtils.golden(I18n.get("gtocore.tooltip.item.tier_circuit", "UIV")));

    ItemEntry<ToolTipsItem> EXOTIC_PROCESSOR = registerCircuit("exotic_processor", "奇异处理器", CustomTags.UHV_CIRCUITS, () -> StringUtils.purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UHV")));
    ItemEntry<ToolTipsItem> EXOTIC_ASSEMBLY = registerCircuit("exotic_assembly", "奇异处理器集群", CustomTags.UEV_CIRCUITS, () -> StringUtils.purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UEV")));
    ItemEntry<ToolTipsItem> EXOTIC_COMPUTER = registerCircuit("exotic_computer", "奇异处理器超级计算机", CustomTags.UIV_CIRCUITS, () -> StringUtils.purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UIV")));
    ItemEntry<ToolTipsItem> EXOTIC_MAINFRAME = registerCircuit("exotic_mainframe", "奇异处理器主机", CustomTags.UXV_CIRCUITS, () -> StringUtils.purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UXV")));

    ItemEntry<ToolTipsItem> COSMIC_PROCESSOR = registerCircuit("cosmic_processor", "寰宇处理器", CustomTags.UEV_CIRCUITS, () -> StringUtils.dark_purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UEV")));
    ItemEntry<ToolTipsItem> COSMIC_ASSEMBLY = registerCircuit("cosmic_assembly", "寰宇处理器集群", CustomTags.UIV_CIRCUITS, () -> StringUtils.dark_purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UIV")));
    ItemEntry<ToolTipsItem> COSMIC_COMPUTER = registerCircuit("cosmic_computer", "寰宇处理器超级计算机", CustomTags.UXV_CIRCUITS, () -> StringUtils.dark_purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UXV")));
    ItemEntry<ToolTipsItem> COSMIC_MAINFRAME = registerCircuit("cosmic_mainframe", "寰宇处理器主机", CustomTags.OpV_CIRCUITS, () -> StringUtils.dark_purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "OpV")));

    ItemEntry<ToolTipsItem> SUPRACAUSAL_PROCESSOR = registerCircuit("supracausal_processor", "超因果处理器", CustomTags.UIV_CIRCUITS, () -> StringUtils.full_color(I18n.get("gtocore.tooltip.item.tier_circuit", "UIV")));
    ItemEntry<ToolTipsItem> SUPRACAUSAL_ASSEMBLY = registerCircuit("supracausal_assembly", "超因果处理器集群", CustomTags.UXV_CIRCUITS, () -> StringUtils.full_color(I18n.get("gtocore.tooltip.item.tier_circuit", "UXV")));
    ItemEntry<ToolTipsItem> SUPRACAUSAL_COMPUTER = registerCircuit("supracausal_computer", "超因果处理器超级计算机", CustomTags.OpV_CIRCUITS, () -> StringUtils.full_color(I18n.get("gtocore.tooltip.item.tier_circuit", "OpV")));
    ItemEntry<ToolTipsItem> SUPRACAUSAL_MAINFRAME = registerCircuit("supracausal_mainframe", "超因果处理器主机", CustomTags.MAX_CIRCUITS, () -> StringUtils.full_color(I18n.get("gtocore.tooltip.item.tier_circuit", "MAX")));

    ItemEntry<ToolTipsItem>[] SUPRACHRONAL_CIRCUIT = registerCircuits("suprachronal_circuit", "超时空电路", GTValues.tiersBetween(GTValues.ULV, GTValues.MAX), tier -> Component.literal(StringUtils.white_blue(I18n.get("gtocore.tooltip.item.tier_circuit", GTValues.VN[tier]))));

    ItemEntry<ToolTipsItem>[] MAGNETO_RESONATIC_CIRCUIT = registerCircuits("magneto_resonatic_circuit", "磁共振电路", GTValues.tiersBetween(GTValues.ULV, GTValues.UIV), tier -> Component.translatable("gtocore.tooltip.item.tier_circuit", GTValues.VN[tier]).withStyle(ChatFormatting.LIGHT_PURPLE));

    ItemEntry<ToolTipsItem>[] UNIVERSAL_CIRCUIT = registerCircuits("universal_circuit", "通用电路", GTValues.tiersBetween(GTValues.ULV, GTValues.MAX), tier -> Component.translatable("gtocore.tooltip.item.tier_circuit", GTValues.VN[tier]).withStyle(ChatFormatting.AQUA));

    ItemEntry<Item> WETWARE_SOC = registerLang("wetware_soc", "Wetware SoC", "湿件SoC");

    ItemEntry<Item> BIOWARE_CIRCUIT_BOARD = register("bioware_circuit_board", "生物电路基板");
    ItemEntry<Item> BIOWARE_PRINTED_CIRCUIT_BOARD = register("bioware_printed_circuit_board", "生物印刷电路基板");
    ItemEntry<Item> SMD_CAPACITOR_BIOWARE = registerLang("smd_capacitor_bioware", "Bioware Capacitor", "生物活性贴片电容");
    ItemEntry<Item> SMD_DIODE_BIOWARE = registerLang("smd_diode_bioware", "Bioware Diode", "生物活性贴片二极管");
    ItemEntry<Item> SMD_RESISTOR_BIOWARE = registerLang("smd_resistor_bioware", "Bioware Resistor", "生物活性贴片电阻");
    ItemEntry<Item> SMD_TRANSISTOR_BIOWARE = registerLang("smd_transistor_bioware", "Bioware Transistor", "生物活性贴片晶体管");
    ItemEntry<Item> SMD_INDUCTOR_BIOWARE = registerLang("smd_inductor_bioware", "Bioware Inductor", "生物活性贴片电感");

    ItemEntry<Item> OPTICAL_CIRCUIT_BOARD = register("optical_circuit_board", "光学电路基板");
    ItemEntry<Item> OPTICAL_PRINTED_CIRCUIT_BOARD = register("optical_printed_circuit_board", "光学印刷电路基板");
    ItemEntry<Item> OPTICAL_RAM_WAFER = register("optical_ram_wafer", "光学RAM晶圆");
    ItemEntry<Item> OPTICAL_RAM_CHIP = register("optical_ram_chip", "光学RAM晶片");
    ItemEntry<Item> SMD_CAPACITOR_OPTICAL = registerLang("smd_capacitor_optical", "Optical Capacitor", "光学贴片电容");
    ItemEntry<Item> SMD_DIODE_OPTICAL = registerLang("smd_diode_optical", "Optical Diode", "光学贴片二极管");
    ItemEntry<Item> SMD_RESISTOR_OPTICAL = registerLang("smd_resistor_optical", "Optical Resistor", "光学贴片电阻");
    ItemEntry<Item> SMD_TRANSISTOR_OPTICAL = registerLang("smd_transistor_optical", "Optical Transistor", "光学贴片晶体管");
    ItemEntry<Item> SMD_INDUCTOR_OPTICAL = registerLang("smd_inductor_optical", "Optical Inductor", "光学贴片电感");

    ItemEntry<Item> EXOTIC_CIRCUIT_BOARD = register("exotic_circuit_board", "奇异电路基板");
    ItemEntry<Item> EXOTIC_PRINTED_CIRCUIT_BOARD = register("exotic_printed_circuit_board", "奇异印刷电路基板");
    ItemEntry<Item> EXOTIC_RAM_WAFER = register("exotic_ram_wafer", "奇异RAM晶圆");
    ItemEntry<Item> EXOTIC_RAM_CHIP = register("exotic_ram_chip", "奇异RAM晶片");
    ItemEntry<Item> SMD_CAPACITOR_EXOTIC = registerLang("smd_capacitor_exotic", "Exotic Capacitor", "奇异贴片电容");
    ItemEntry<Item> SMD_DIODE_EXOTIC = registerLang("smd_diode_exotic", "Exotic Diode", "奇异贴片二极管");
    ItemEntry<Item> SMD_RESISTOR_EXOTIC = registerLang("smd_resistor_exotic", "Exotic Resistor", "奇异贴片电阻");
    ItemEntry<Item> SMD_TRANSISTOR_EXOTIC = registerLang("smd_transistor_exotic", "Exotic Transistor", "奇异贴片晶体管");
    ItemEntry<Item> SMD_INDUCTOR_EXOTIC = registerLang("smd_inductor_exotic", "Exotic Inductor", "奇异贴片电感");

    ItemEntry<Item> COSMIC_CIRCUIT_BOARD = register("cosmic_circuit_board", "寰宇电路基板");
    ItemEntry<Item> COSMIC_PRINTED_CIRCUIT_BOARD = register("cosmic_printed_circuit_board", "寰宇印刷电路基板");
    ItemEntry<Item> COSMIC_RAM_WAFER = register("cosmic_ram_wafer", "寰宇RAM晶圆");
    ItemEntry<Item> COSMIC_RAM_CHIP = register("cosmic_ram_chip", "寰宇RAM晶片");
    ItemEntry<Item> SMD_CAPACITOR_COSMIC = registerLang("smd_capacitor_cosmic", "Cosmic Capacitor", "寰宇贴片电容");
    ItemEntry<Item> SMD_DIODE_COSMIC = registerLang("smd_diode_cosmic", "Cosmic Diode", "寰宇贴片二极管");
    ItemEntry<Item> SMD_RESISTOR_COSMIC = registerLang("smd_resistor_cosmic", "Cosmic Resistor", "寰宇贴片电阻");
    ItemEntry<Item> SMD_TRANSISTOR_COSMIC = registerLang("smd_transistor_cosmic", "Cosmic Transistor", "寰宇贴片晶体管");
    ItemEntry<Item> SMD_INDUCTOR_COSMIC = registerLang("smd_inductor_cosmic", "Cosmic Inductor", "寰宇贴片电感");

    ItemEntry<Item> SUPRACAUSAL_CIRCUIT_BOARD = register("supracausal_circuit_board", "超因果电路基板");
    ItemEntry<Item> SUPRACAUSAL_PRINTED_CIRCUIT_BOARD = register("supracausal_printed_circuit_board", "超因果印刷电路基板");
    ItemEntry<Item> SUPRACAUSAL_RAM_WAFER = register("supracausal_ram_wafer", "超因果RAM晶圆");
    ItemEntry<Item> SUPRACAUSAL_RAM_CHIP = register("supracausal_ram_chip", "超因果RAM晶片");
    ItemEntry<Item> SMD_CAPACITOR_SUPRACAUSAL = registerLang("smd_capacitor_supracausal", "Supracausal Capacitor", "超因果贴片电容");
    ItemEntry<Item> SMD_DIODE_SUPRACAUSAL = registerLang("smd_diode_supracausal", "Supracausal Diode", "超因果贴片二极管");
    ItemEntry<Item> SMD_RESISTOR_SUPRACAUSAL = registerLang("smd_resistor_supracausal", "Supracausal Resistor", "超因果贴片电阻");
    ItemEntry<Item> SMD_TRANSISTOR_SUPRACAUSAL = registerLang("smd_transistor_supracausal", "Supracausal Transistor", "超因果贴片晶体管");
    ItemEntry<Item> SMD_INDUCTOR_SUPRACAUSAL = registerLang("smd_inductor_supracausal", "Supracausal Inductor", "超因果贴片电感");

    ItemEntry<Item> UHV_VOLTAGE_COIL = registerLang("uhv_voltage_coil", "UHV Voltage Coil", "极高压线圈");
    ItemEntry<Item> UEV_VOLTAGE_COIL = registerLang("uev_voltage_coil", "UEV Voltage Coil", "极超压线圈");
    ItemEntry<Item> UIV_VOLTAGE_COIL = registerLang("uiv_voltage_coil", "UIV Voltage Coil", "极巨压线圈");
    ItemEntry<Item> UXV_VOLTAGE_COIL = registerLang("uxv_voltage_coil", "UXV Voltage Coil", "极顶压线圈");
    ItemEntry<Item> OPV_VOLTAGE_COIL = registerLang("opv_voltage_coil", "OpV Voltage Coil", "过载压线圈");
    ItemEntry<Item> MAX_VOLTAGE_COIL = registerLang("max_voltage_coil", "MAX Voltage Coil", "上限压线圈");

    ItemEntry<Item> SPACE_DRONE_MK1 = registerLang("space_drone_mk1", "Space Drone MKⅠ", "太空无人机MKⅠ");
    ItemEntry<Item> SPACE_DRONE_MK2 = registerLang("space_drone_mk2", "Space Drone MKⅡ", "太空无人机MKⅡ");
    ItemEntry<Item> SPACE_DRONE_MK3 = registerLang("space_drone_mk3", "Space Drone MKⅢ", "太空无人机MKⅢ");
    ItemEntry<Item> SPACE_DRONE_MK4 = registerLang("space_drone_mk4", "Space Drone MKⅣ", "太空无人机MKⅣ");
    ItemEntry<Item> SPACE_DRONE_MK5 = registerLang("space_drone_mk5", "Space Drone MKⅤ", "太空无人机MKⅤ");
    ItemEntry<Item> SPACE_DRONE_MK6 = registerLang("space_drone_mk6", "Space Drone MKⅥ", "太空无人机MKⅥ");

    ItemEntry<ComponentItem> COSMIC_SINGULARITY = item("cosmic_singularity", "宇宙奇点", ComponentItem::create)
            .onRegister(attachRenderer(() -> HaloItemRenderer.COSMIC_HALO))
            .model(NonNullBiConsumer.noop())
            .register();

    ItemEntry<Item> ENTANGLED_SINGULARITY = registerCustomModel("entangled_singularity", "纠缠奇点");
    ItemEntry<Item> SPACETIME_CATALYST = registerCustomModel("spacetime_catalyst", "时空催化剂");
    ItemEntry<Item> ETERNITY_CATALYST = registerCustomModel("eternity_catalyst", "永恒催化剂");

    ItemEntry<Item> COMBINED_SINGULARITY_0 = registerCustomModel("combined_singularity_0", "特化奇点");
    ItemEntry<Item> COMBINED_SINGULARITY_1 = registerCustomModel("combined_singularity_1", "超凡奇点");
    ItemEntry<Item> COMBINED_SINGULARITY_2 = registerCustomModel("combined_singularity_2", "度量奇点");
    ItemEntry<Item> COMBINED_SINGULARITY_3 = registerCustomModel("combined_singularity_3", "古远奇点");
    ItemEntry<Item> COMBINED_SINGULARITY_4 = registerCustomModel("combined_singularity_4", "电气奇点");
    ItemEntry<Item> COMBINED_SINGULARITY_5 = registerCustomModel("combined_singularity_5", "质子奇点");
    ItemEntry<Item> COMBINED_SINGULARITY_6 = registerCustomModel("combined_singularity_6", "奇异奇点");
    ItemEntry<Item> COMBINED_SINGULARITY_7 = registerCustomModel("combined_singularity_7", "本征奇点");
    ItemEntry<Item> COMBINED_SINGULARITY_8 = registerCustomModel("combined_singularity_8", "量子奇点");
    ItemEntry<Item> COMBINED_SINGULARITY_9 = registerCustomModel("combined_singularity_9", "炫光奇点");
    ItemEntry<Item> COMBINED_SINGULARITY_10 = registerCustomModel("combined_singularity_10", "磁力奇点");
    ItemEntry<Item> COMBINED_SINGULARITY_11 = registerCustomModel("combined_singularity_11", "银河奇点");
    ItemEntry<Item> COMBINED_SINGULARITY_12 = registerCustomModel("combined_singularity_12", "八角奇点");
    ItemEntry<Item> COMBINED_SINGULARITY_13 = registerCustomModel("combined_singularity_13", "密文奇点");
    ItemEntry<Item> COMBINED_SINGULARITY_14 = registerCustomModel("combined_singularity_14", "天使奇点");
    ItemEntry<Item> COMBINED_SINGULARITY_15 = registerCustomModel("combined_singularity_15", "立方奇点");

    ItemEntry<Item> APATITE_VEIN_ESSENCE = registerEssence("apatite_vein", "磷灰石");
    ItemEntry<Item> BANDED_IRON_VEIN_ESSENCE = registerEssence("banded_iron_vein", "带状铁");
    ItemEntry<Item> BAUXITE_VEIN_ESSENCE = registerEssence("bauxite_vein", "铝土");
    ItemEntry<Item> BERYLLIUM_VEIN_ESSENCE = registerEssence("beryllium_vein", "铍");
    ItemEntry<Item> CASSITERITE_VEIN_ESSENCE = registerEssence("cassiterite_vein", "锡石");
    ItemEntry<Item> CERTUS_QUARTZ_ESSENCE = registerEssence("certus_quartz", "赛特斯石英");
    ItemEntry<Item> COAL_VEIN_ESSENCE = registerEssence("coal_vein", "煤炭");
    ItemEntry<Item> COPPER_TIN_VEIN_ESSENCE = registerEssence("copper_tin_vein", "铜锡");
    ItemEntry<Item> COPPER_VEIN_ESSENCE = registerEssence("copper_vein", "铜");
    ItemEntry<Item> DIAMOND_VEIN_ESSENCE = registerEssence("diamond_vein", "钻石");
    ItemEntry<Item> GALENA_VEIN_ESSENCE = registerEssence("galena_vein", "方铅");
    ItemEntry<Item> GARNET_TIN_VEIN_ESSENCE = registerEssence("garnet_tin_vein", "锡石榴石");
    ItemEntry<Item> GARNET_VEIN_ESSENCE = registerEssence("garnet_vein", "石榴石");
    ItemEntry<Item> IRON_VEIN_ESSENCE = registerEssence("iron_vein", "铁");
    ItemEntry<Item> LAPIS_VEIN_ESSENCE = registerEssence("lapis_vein", "青金石");
    ItemEntry<Item> LUBRICANT_VEIN_ESSENCE = registerEssence("lubricant_vein", "皂滑");
    ItemEntry<Item> MAGNETITE_VEIN_END_ESSENCE = registerEssence("magnetite_vein", "磁铁");
    ItemEntry<Item> MANGANESE_VEIN_ESSENCE = registerEssence("manganese_vein", "锰");
    ItemEntry<Item> MICA_VEIN_ESSENCE = registerEssence("mica_vein", "云母");
    ItemEntry<Item> MINERAL_SAND_VEIN_ESSENCE = registerEssence("mineral_sand_vein", "矿砂");
    ItemEntry<Item> MOLYBDENUM_VEIN_ESSENCE = registerEssence("molybdenum_vein", "钼");
    ItemEntry<Item> MONAZITE_VEIN_ESSENCE = registerEssence("monazite_vein", "独居石");
    ItemEntry<Item> NAQUADAH_VEIN_ESSENCE = registerEssence("naquadah_vein", "硅岩");
    ItemEntry<Item> NETHER_QUARTZ_VEIN_ESSENCE = registerEssence("nether_quartz_vein", "下界石英");
    ItemEntry<Item> NICKEL_VEIN_ESSENCE = registerEssence("nickel_vein", "镍");
    ItemEntry<Item> OILSANDS_VEIN_ESSENCE = registerEssence("oilsands_vein", "油砂");
    ItemEntry<Item> OLIVINE_VEIN_ESSENCE = registerEssence("olivine_vein", "橄榄石");
    ItemEntry<Item> PITCHBLENDE_VEIN_ESSENCE = registerEssence("pitchblende_vein", "沥青铀");
    ItemEntry<Item> REDSTONE_VEIN_ESSENCE = registerEssence("redstone_vein", "红石");
    ItemEntry<Item> SALTPETER_VEIN_ESSENCE = registerEssence("saltpeter_vein", "硝石");
    ItemEntry<Item> SALTS_VEIN_ESSENCE = registerEssence("salts_vein", "盐");
    ItemEntry<Item> SAPPHIRE_VEIN_ESSENCE = registerEssence("sapphire_vein", "蓝宝石");
    ItemEntry<Item> SCHEELITE_VEIN_ESSENCE = registerEssence("scheelite_vein", "白钨");
    ItemEntry<Item> SHELDONITE_VEIN_ESSENCE = registerEssence("sheldonite_vein", "谢尔顿");
    ItemEntry<Item> SULFUR_VEIN_ESSENCE = registerEssence("sulfur_vein", "硫");
    ItemEntry<Item> TETRAHEDRITE_VEIN_ESSENCE = registerEssence("tetrahedrite_vein", "黝铜");
    ItemEntry<Item> TOPAZ_VEIN_ESSENCE = registerEssence("topaz_vein", "黄玉");

    ItemEntry<Item> REACTOR_URANIUM_SIMPLE = register("reactor_uranium_simple", "铀燃料棒");
    ItemEntry<Item> REACTOR_URANIUM_DUAL = register("reactor_uranium_dual", "二联铀燃料棒");
    ItemEntry<Item> REACTOR_URANIUM_QUAD = register("reactor_uranium_quad", "四联铀燃料棒");
    ItemEntry<Item> DEPLETED_REACTOR_URANIUM_SIMPLE = register("depleted_reactor_uranium_simple", "枯竭铀燃料棒");
    ItemEntry<Item> DEPLETED_REACTOR_URANIUM_DUAL = register("depleted_reactor_uranium_dual", "枯竭二联铀燃料棒");
    ItemEntry<Item> DEPLETED_REACTOR_URANIUM_QUAD = register("depleted_reactor_uranium_quad", "枯竭四联铀燃料棒");

    ItemEntry<Item> REACTOR_THORIUM_SIMPLE = register("reactor_thorium_simple", "钍燃料棒");
    ItemEntry<Item> REACTOR_THORIUM_DUAL = register("reactor_thorium_dual", "二联钍燃料棒");
    ItemEntry<Item> REACTOR_THORIUM_QUAD = register("reactor_thorium_quad", "四联钍燃料棒");
    ItemEntry<Item> DEPLETED_REACTOR_THORIUM_SIMPLE = register("depleted_reactor_thorium_simple", "枯竭钍燃料棒");
    ItemEntry<Item> DEPLETED_REACTOR_THORIUM_DUAL = register("depleted_reactor_thorium_dual", "枯竭二联钍燃料棒");
    ItemEntry<Item> DEPLETED_REACTOR_THORIUM_QUAD = register("depleted_reactor_thorium_quad", "枯竭四联钍燃料棒");

    ItemEntry<Item> REACTOR_MOX_SIMPLE = register("reactor_mox_simple", "MOX燃料棒");
    ItemEntry<Item> REACTOR_MOX_DUAL = register("reactor_mox_dual", "二联MOX燃料棒");
    ItemEntry<Item> REACTOR_MOX_QUAD = register("reactor_mox_quad", "四联MOX燃料棒");
    ItemEntry<Item> DEPLETED_REACTOR_MOX_SIMPLE = register("depleted_reactor_mox_simple", "枯竭MOX燃料棒");
    ItemEntry<Item> DEPLETED_REACTOR_MOX_DUAL = register("depleted_reactor_mox_dual", "枯竭二联MOX燃料棒");
    ItemEntry<Item> DEPLETED_REACTOR_MOX_QUAD = register("depleted_reactor_mox_quad", "枯竭四联MOX燃料棒");

    ItemEntry<Item> REACTOR_NAQUADAH_SIMPLE = register("reactor_naquadah_simple", "硅岩燃料棒");
    ItemEntry<Item> REACTOR_NAQUADAH_DUAL = register("reactor_naquadah_dual", "二联硅岩燃料棒");
    ItemEntry<Item> REACTOR_NAQUADAH_QUAD = register("reactor_naquadah_quad", "四联硅岩燃料棒");
    ItemEntry<Item> DEPLETED_REACTOR_NAQUADAH_SIMPLE = register("depleted_reactor_naquadah_simple", "枯竭硅岩燃料棒");
    ItemEntry<Item> DEPLETED_REACTOR_NAQUADAH_DUAL = register("depleted_reactor_naquadah_dual", "枯竭二联硅岩燃料棒");
    ItemEntry<Item> DEPLETED_REACTOR_NAQUADAH_QUAD = register("depleted_reactor_naquadah_quad", "枯竭四联硅岩燃料棒");

    ItemEntry<Item> NEUTRONIUM_ANTIMATTER_FUEL_ROD = registerTexture("neutronium_antimatter_fuel_rod", "中子反物质燃料棒", "antimatter_fuel_rod");
    ItemEntry<Item> DRACONIUM_ANTIMATTER_FUEL_ROD = registerTexture("draconium_antimatter_fuel_rod", "龙反物质燃料棒", "antimatter_fuel_rod");
    ItemEntry<Item> COSMIC_NEUTRONIUM_ANTIMATTER_FUEL_ROD = registerTexture("cosmic_neutronium_antimatter_fuel_rod", "宇宙中子反物质燃料棒", "antimatter_fuel_rod");
    ItemEntry<Item> INFINITY_ANTIMATTER_FUEL_ROD = registerTexture("infinity_antimatter_fuel_rod", "无尽反物质燃料棒", "antimatter_fuel_rod");

    ItemEntry<Item> SPACE_ESSENCE = register("space_essence", "宇宙精华");
    ItemEntry<Item> BEDROCK_DESTROYER = register("bedrock_destroyer", "基岩破坏者");
    ItemEntry<Item> LEPTON_TRAP_CRYSTAL = register("lepton_trap_crystal", "轻子阱晶体");
    ItemEntry<Item> CHARGED_LEPTON_TRAP_CRYSTAL = register("charged_lepton_trap_crystal", "带电轻子阱晶体");
    ItemEntry<Item> QUANTUM_ANOMALY = register("quantum_anomaly", "量子反常");
    ItemEntry<Item> HASSIUM_SEED_CRYSTAL = register("hassium_seed_crystal", "𬭶晶种");
    ItemEntry<Item> RAW_IMPRINTED_RESONATIC_CIRCUIT_BOARD = register("raw_imprinted_resonatic_circuit_board", "压印磁共振电路板原料");
    ItemEntry<Item> IMPRINTED_RESONATIC_CIRCUIT_BOARD = register("imprinted_resonatic_circuit_board", "压印磁共振电路板");
    ItemEntry<Item> ROTATING_TRANSPARENT_SURFACE = register("rotating_transparent_surface", "旋转透明层");
    ItemEntry<Item> ELECTRON_SOURCE = register("electron_source", "电子源");
    ItemEntry<Item> ESSENCE = register("essence", "精华");
    ItemEntry<Item> ESSENCE_SEED = register("essence_seed", "精华种子");
    ItemEntry<Item> NUCLEAR_STAR = register("nuclear_star", "核能之星");
    ItemEntry<Item> UNSTABLE_STAR = register("unstable_star", "易变之星");
    ItemEntry<Item> PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK1 = register("precision_circuit_assembly_robot_mk1", "精密电路装配机器人MKⅠ");
    ItemEntry<Item> PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK2 = register("precision_circuit_assembly_robot_mk2", "精密电路装配机器人MKⅡ");
    ItemEntry<Item> PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK3 = register("precision_circuit_assembly_robot_mk3", "精密电路装配机器人MKⅢ");
    ItemEntry<Item> PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK4 = register("precision_circuit_assembly_robot_mk4", "精密电路装配机器人MKⅣ");
    ItemEntry<Item> PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK5 = register("precision_circuit_assembly_robot_mk5", "精密电路装配机器人MKⅤ");
    ItemEntry<Item> SCRAP = register("scrap", "废料");
    ItemEntry<Item> SCRAP_BOX = register("scrap_box", "废料盒");
    ItemEntry<Item> NUCLEAR_WASTE = register("nuclear_waste", "核废料");
    ItemEntry<Item> RESONATING_GEM = register("resonating_gem", "共振宝石");
    ItemEntry<Item> PLASMA_CONTAINMENT_CELL = register("plasma_containment_cell", "等离子体密闭容器");
    ItemEntry<Item> RHENIUM_PLASMA_CONTAINMENT_CELL = register("rhenium_plasma_containment_cell", "铼等离子体密闭容器");
    ItemEntry<Item> ACTINIUM_SUPERHYDRIDE_PLASMA_CONTAINMENT_CELL = register("actinium_superhydride_plasma_containment_cell", "超氢化锕等离子体密闭容器");
    ItemEntry<Item> DRAGON_STEM_CELLS = register("dragon_stem_cells", "龙干细胞");
    ItemEntry<Item> DRAGON_CELLS = register("dragon_cells", "龙细胞");
    ItemEntry<Item> BIOWARE_BOULE = register("bioware_boule", "生物活性晶圆");
    ItemEntry<Item> BIOWARE_CHIP = register("bioware_chip", "生物活性芯片");
    ItemEntry<Item> BIOWARE_PROCESSING_CORE = register("bioware_processing_core", "生物活性处理器核心");
    ItemEntry<Item> BIOLOGICAL_CELLS = register("biological_cells", "生物细胞");
    ItemEntry<Item> SIMPLE_OPTICAL_SOC = registerLang("simple_optical_soc", "Simple Optical SoC", "简易光学SoC");
    ItemEntry<Item> OPTICAL_SOC_CONTAINMENT_HOUSING = registerLang("optical_soc_containment_housing", "Optical SoC Containment Housing", "光学SoC密封外壳");
    ItemEntry<Item> OPTICAL_SLICE = register("optical_slice", "光学裸片");
    ItemEntry<Item> OPTICAL_PROCESSING_CORE = register("optical_processing_core", "光学处理器核心");
    ItemEntry<Item> OPTICAL_WAFER = register("optical_wafer", "光学晶圆");
    ItemEntry<Item> PHOTON_CARRYING_WAFER = register("photon_carrying_wafer", "光电子承载晶圆");
    ItemEntry<Item> RAW_PHOTON_CARRYING_WAFER = register("raw_photon_carrying_wafer", "未成型的光电子承载晶圆");
    ItemEntry<Item> EXOTIC_PROCESSING_CORE = register("exotic_processing_core", "奇异处理器核心");
    ItemEntry<Item> COSMIC_PROCESSING_CORE = register("cosmic_processing_core", "寰宇处理器核心");
    ItemEntry<Item> SUPRACAUSAL_PROCESSING_CORE = register("supracausal_processing_core", "超因果处理器核心");
    ItemEntry<Item> PERIODICALLY_POLED_LITHIUM_NIOBATE_BOULE = register("periodically_poled_lithium_niobate_boule", "周期性极化铌酸锂晶体");
    ItemEntry<Item> NEUTRON_PLASMA_CONTAINMENT_CELL = register("neutron_plasma_containment_cell", "中子等离子体密闭容器");
    ItemEntry<Item> CRYSTAL_MATRIX_PLASMA_CONTAINMENT_CELL = register("crystal_matrix_plasma_containment_cell", "水晶矩阵等离子体密闭容器");
    ItemEntry<Item> AWAKENED_DRACONIUM_PLASMA_CONTAINMENT_CELL = register("awakened_draconium_plasma_containment_cell", "觉醒龙等离子体密闭容器");
    ItemEntry<Item> HIGHLY_REFLECTIVE_MIRROR = register("highly_reflective_mirror", "高反射率镜");
    ItemEntry<Item> LOW_FREQUENCY_LASER = register("low_frequency_laser", "低频激光器");
    ItemEntry<Item> MEDIUM_FREQUENCY_LASER = register("medium_frequency_laser", "中频激光器");
    ItemEntry<Item> HIGH_FREQUENCY_LASER = register("high_frequency_laser", "高频激光器");
    ItemEntry<Item> RED_HALIDE_LAMP = register("red_halide_lamp", "红光卤素灯");
    ItemEntry<Item> GREEN_HALIDE_LAMP = register("green_halide_lamp", "绿光卤素灯");
    ItemEntry<Item> BLUE_HALIDE_LAMP = register("blue_halide_lamp", "蓝光卤素灯");
    ItemEntry<Item> BALLAST = register("ballast", "镇流器");
    ItemEntry<Item> EMPTY_LASER_COOLING_CONTAINER = register("empty_laser_cooling_container", "空的激光冷却容器");
    ItemEntry<Item> HIGH_PRECISION_CRYSTAL_SOC = registerLang("high_precision_crystal_soc", "High Precision Crystal SoC", "高精度晶体SoC");
    ItemEntry<Item> BOSE_EINSTEIN_COOLING_CONTAINER = register("bose_einstein_cooling_container", "玻色-爱因斯坦凝聚态物质遏制装置");
    ItemEntry<Item> LASER_COOLING_UNIT = register("laser_cooling_unit", "激光冷却单元");
    ItemEntry<Item> LASER_DIODE = register("laser_diode", "激光二极管");
    ItemEntry<Item> MAGNETIC_TRAP = register("magnetic_trap", "磁阱");
    ItemEntry<Item> RYDBERG_SPINORIAL_ASSEMBLY = register("rydberg_spinorial_assembly", "里德堡旋量集群");
    ItemEntry<Item> X_RAY_LASER = register("x_ray_laser", "X射线激光器");
    ItemEntry<Item> CRYOGENIC_INTERFACE = register("cryogenic_interface", "低温接口");
    ItemEntry<Item> EXOTIC_WAFER = register("exotic_wafer", "奇异晶圆");
    ItemEntry<Item> EXOTIC_CHIP = register("exotic_chip", "奇异芯片");
    ItemEntry<Item> X_RAY_WAVEGUIDE = register("x_ray_waveguide", "X射线导波管");
    ItemEntry<Item> X_RAY_MIRROR_PLATE = register("x_ray_mirror_plate", "X射线镜片");
    ItemEntry<Item> COSMIC_PROCESSING_UNIT_CORE = register("cosmic_processing_unit_core", "寰宇处理器单元");
    ItemEntry<Item> ULTRASHORT_PULSE_LASER = register("ultrashort_pulse_laser", "超短脉冲激光器");
    ItemEntry<Item> DIFFRACTOR_GRATING_MIRROR = register("diffractor_grating_mirror", "衍射光栅镜");
    ItemEntry<Item> GRATING_LITHOGRAPHY_MASK = register("grating_lithography_mask", "光栅光刻掩膜");
    ItemEntry<Item> LITHOGRAPHY_MASK = register("lithography_mask", "光刻掩膜");
    ItemEntry<Item> PHOTOCOATED_HASSIUM_BOULE = register("photocoated_hassium_boule", "光聚合物涂覆的掺𬭶单晶硅");
    ItemEntry<Item> PHOTOCOATED_HASSIUM_WAFER = register("photocoated_hassium_wafer", "光聚合物涂覆的掺𬭶晶圆");
    ItemEntry<Item> TIME_DILATION_CONTAINMENT_UNIT = register("time_dilation_containment_unit", "时间膨胀密闭单元");
    ItemEntry<Item> NEUTRONIUM_SPHERE = register("neutronium_sphere", "中子素球体");
    ItemEntry<Item> CHARGED_TRIPLET_NEUTRONIUM_SPHERE = register("charged_triplet_neutronium_sphere", "带电三连中子素球体");
    ItemEntry<Item> TRIPLET_NEUTRONIUM_SPHERE = register("triplet_neutronium_sphere", "三连中子素球体");
    ItemEntry<Item> CONTAINED_HIGH_DENSITY_PROTONIC_MATTER = register("contained_high_density_protonic_matter", "遏制高密度质子物质");
    ItemEntry<Item> EXTREMELY_DURABLE_PLASMA_CELL = register("extremely_durable_plasma_cell", "高耐久等离子体容器");
    ItemEntry<Item> DENSE_NEUTRON_PLASMA_CELL = register("dense_neutron_plasma_cell", "致密中子等离子体单元");
    ItemEntry<Item> COSMIC_NEUTRON_PLASMA_CELL = register("cosmic_neutron_plasma_cell", "宇宙中子等离子体单元");
    ItemEntry<Item> QUANTUMCHROMODYNAMIC_PROTECTIVE_PLATING = register("quantumchromodynamic_protective_plating", "量子色动力学防护层");
    ItemEntry<Item> CHAOS_CONTAINMENT_UNIT = register("chaos_containment_unit", "混沌物质遏制容器");
    ItemEntry<Item> COSMIC_MESH_CONTAINMENT_UNIT = register("cosmic_mesh_containment_unit", "寰宇织网遏制容器");
    ItemEntry<Item> MICROWORMHOLE_GENERATOR = register("microwormhole_generator", "微型虫洞发生器");
    ItemEntry<Item> GRAVITON_TRANSDUCER = register("graviton_transducer", "引力子换能器");
    ItemEntry<Item> CONTAINED_REISSNER_NORDSTROM_SINGULARITY = register("contained_reissner_nordstrom_singularity", "遏制莱斯纳-诺斯特朗黑洞奇点");
    ItemEntry<Item> CONTAINED_KERR_NEWMANN_SINGULARITY = register("contained_kerr_newmann_singularity", "遏制克尔-纽曼黑洞奇点");
    ItemEntry<Item> CONTAINED_KERR_SINGULARITY = register("contained_kerr_singularity", "遏制克尔黑洞奇点");
    ItemEntry<Item> CONTAINED_EXOTIC_MATTER = register("contained_exotic_matter", "遏制奇异物质");
    ItemEntry<Item> MACROWORMHOLE_GENERATOR = register("macrowormhole_generator", "巨型虫洞发生器");
    ItemEntry<Item> STABILIZED_WORMHOLE_GENERATOR = register("stabilized_wormhole_generator", "稳定虫洞发生器");
    ItemEntry<Item> TOPOLOGICAL_MANIPULATOR_UNIT = register("topological_manipulator_unit", "拓扑控制操纵单元");
    ItemEntry<Item> RELATIVISTIC_SPINORIAL_MEMORY_SYSTEM = register("relativistic_spinorial_memory_system", "相对论旋量存储器");
    ItemEntry<Item> NUCLEAR_CLOCK = register("nuclear_clock", "核时钟");
    ItemEntry<Item> SCINTILLATOR = register("scintillator", "闪烁体");
    ItemEntry<Item> SCINTILLATOR_CRYSTAL = register("scintillator_crystal", "闪烁晶体");
    ItemEntry<Item> MANIFOLD_OSCILLATORY_POWER_CELL = register("manifold_oscillatory_power_cell", "流形振荡电池");
    ItemEntry<Item> RECURSIVELY_FOLDED_NEGATIVE_SPACE = register("recursively_folded_negative_space", "递归折叠负空间");
    ItemEntry<Item> EIGENFOLDED_KERR_MANIFOLD = register("eigenfolded_kerr_manifold", "本征折叠克尔流形");
    ItemEntry<Item> CLOSED_TIMELIKE_CURVE_COMPUTATIONAL_UNIT_CONTAINER = register("closed_timelike_curve_computational_unit_container", "封闭类时曲线计算单元容器");
    ItemEntry<Item> CLOSED_TIMELIKE_CURVE_COMPUTATIONAL_UNIT = register("closed_timelike_curve_computational_unit", "封闭类时曲线计算单元");
    ItemEntry<Item> HIGHLY_DENSE_POLYMER_PLATE = register("highly_dense_polymer_plate", "高密度聚合物板");
    ItemEntry<Item> SPACE_PROBE_MK1 = register("space_probe_mk1", "宇宙探测器MKⅠ");
    ItemEntry<Item> SPACE_PROBE_MK2 = register("space_probe_mk2", "宇宙探测器MKⅡ");
    ItemEntry<Item> SPACE_PROBE_MK3 = register("space_probe_mk3", "宇宙探测器MKⅢ");
    ItemEntry<Item> HYPERCUBE = register("hypercube", "超立方体");
    ItemEntry<Item> ANNIHILATION_CONSTRAINER = register("annihilation_constrainer", "湮灭约束器");
    ItemEntry<Item> SOLAR_LIGHT_SPLITTER = register("solar_light_splitter", "阳光分离器");
    ItemEntry<ToolTipsItem> CREATE_ULTIMATE_BATTERY = registerTooltip("create_ultimate_battery", "创造电池", () -> Component.literal(I18n.get("tooltip.avaritia.tier", StringUtils.white_blue(I18n.get("gtocore.tooltip.unknown")))).withStyle(ChatFormatting.GREEN));
    ItemEntry<ToolTipsItem> SUPRACHRONAL_MAINFRAME_COMPLEX = registerTooltip("suprachronal_mainframe_complex", "创造主机", () -> Component.literal(I18n.get("tooltip.avaritia.tier", StringUtils.white_blue(I18n.get("gtocore.tooltip.unknown")))).withStyle(ChatFormatting.GREEN));
    ItemEntry<Item> ZERO_POINT_MODULE_FRAGMENTS = register("zero_point_module_fragments", "零点模块碎片");
    ItemEntry<Item> TCETIESEAWEEDEXTRACT = register("tcetieseaweedextract", "鲸鱼座T星E藻类提取物");
    ItemEntry<Item> TCETIEDANDELIONS = register("tcetiedandelions", "鲸鱼座T星E藻类");
    ItemEntry<Item> WOVEN_KEVLAR = register("woven_kevlar", "编织凯芙拉");
    ItemEntry<Item> KEVLAR_FIBER = register("kevlar_fiber", "凯芙拉纤维");
    ItemEntry<Item> WARPED_ENDER_PEARL = register("warped_ender_pearl", "扭曲的末影珍珠");
    ItemEntry<Item> CHAOS_SHARD = register("chaos_shard", "混沌碎片");
    ItemEntry<Item> COSMIC_FABRIC = register("cosmic_fabric", "寰宇织料");
    ItemEntry<Item> DRACONIC_CORE = register("draconic_core", "龙芯");
    ItemEntry<Item> WYVERN_CORE = register("wyvern_core", "双足飞龙核心");
    ItemEntry<Item> AWAKENED_CORE = register("awakened_core", "觉醒核心");
    ItemEntry<Item> CHAOTIC_CORE = register("chaotic_core", "混沌核心");
    ItemEntry<Item> WYVERN_ENERGY_CORE = register("wyvern_energy_core", "双足飞龙能量核心");
    ItemEntry<Item> DRACONIC_ENERGY_CORE = register("draconic_energy_core", "神龙能量核心");
    ItemEntry<Item> CHAOTIC_ENERGY_CORE = register("chaotic_energy_core", "混沌能量核心");
    ItemEntry<Item> DRACONIUM_DIRT = register("draconium_dirt", "龙尘");
    ItemEntry<Item> DRAGON_HEART = register("dragon_heart", "龙之心");
    ItemEntry<Item> STABILIZER_CORE = register("stabilizer_core", "稳定核心");
    ItemEntry<Item> DRAGON_STABILIZER_CORE = register("dragon_stabilizer_core", "龙之稳定核心");
    ItemEntry<Item> RUTHERFORDIUM_AMPROSIUM_BOULE = register("rutherfordium_amprosium_boule", "𬬻强化的安普洛掺杂的单晶硅");
    ItemEntry<Item> RUTHERFORDIUM_AMPROSIUM_WAFER = register("rutherfordium_amprosium_wafer", "𬬻强化的安普洛掺杂的晶圆");
    ItemEntry<Item> TARANIUM_BOULE = register("taranium_boule", "塔兰掺杂的单晶硅");
    ItemEntry<Item> TARANIUM_WAFER = register("taranium_wafer", "塔兰掺杂的晶圆");
    ItemEntry<Item> PREPARED_COSMIC_SOC_WAFER = registerLang("prepared_cosmic_soc_wafer", "Prepared Cosmic SoC Wafer", "预备寰宇SoC晶圆");
    ItemEntry<Item> SIMPLE_COSMIC_SOC_WAFER = registerLang("simple_cosmic_soc_wafer", "Simple Cosmic SoC Wafer", "简易寰宇SoC晶圆");
    ItemEntry<Item> SIMPLE_COSMIC_SOC = registerLang("simple_cosmic_soc", "Simple Cosmic SoC", "简易寰宇SoC");
    ItemEntry<Item> NM_WAFER = register("nm_wafer", "纳米功率集成电路晶圆");
    ItemEntry<Item> NM_CHIP = register("nm_chip", "纳米功率集成电路");
    ItemEntry<Item> PM_WAFER = register("pm_wafer", "皮米功率集成电路晶圆");
    ItemEntry<Item> PM_CHIP = register("pm_chip", "皮米功率集成电路");
    ItemEntry<Item> FM_WAFER = register("fm_wafer", "飞米功率集成电路晶圆");
    ItemEntry<Item> FM_CHIP = register("fm_chip", "飞米功率集成电路");
    ItemEntry<Item> FULLERENE_POLYMER_MATRIX_SOFT_TUBING = register("fullerene_polymer_matrix_soft_tubing", "富勒烯聚合物基体软管");
    ItemEntry<Item> FULLERENE_POLYMER_MATRIX_FINE_TUBING = register("fullerene_polymer_matrix_fine_tubing", "富勒烯聚合物基质细管");
    ItemEntry<Item> DUST_BLIZZ = register("dust_blizz", "暴雪粉");
    ItemEntry<Item> DUST_CRYOTHEUM = register("dust_cryotheum", "凛冰粉");
    ItemEntry<Item> BEDROCK_DRILL = register("bedrock_drill", "基岩钻头");
    ItemEntry<Item> MEMORY_FOAM_BLOCK = register("memory_foam_block", "记忆海绵");
    ItemEntry<Item> GRAPHENE_IRON_PLATE = register("graphene_iron_plate", "石墨烯铁板");
    ItemEntry<Item> INSULATION_WIRE_ASSEMBLY = register("insulation_wire_assembly", "绝缘线团");
    ItemEntry<Item> AEROGRAPHENE = register("aerographene", "石墨烯气凝胶");
    ItemEntry<Item> COMMAND_BLOCK_CORE = register("command_block_core", "脉冲命令方块核心");
    ItemEntry<Item> CHAIN_COMMAND_BLOCK_CORE = register("chain_command_block_core", "连锁命令方块核心");
    ItemEntry<Item> REPEATING_COMMAND_BLOCK_CORE = register("repeating_command_block_core", "循环命令方块核心");
    ItemEntry<Item> TWO_WAY_FOIL = register("two_way_foil", "二向箔");
    ItemEntry<Item> HYPER_STABLE_SELF_HEALING_ADHESIVE = register("hyper_stable_self_healing_adhesive", "超稳态自修复粘合剂");
    ItemEntry<Item> BLACK_BODY_NAQUADRIA_SUPERSOLID = register("black_body_naquadria_supersolid", "超固态超能硅岩黑体");
    ItemEntry<Item> HEARTOFTHESMOGUS = register("heartofthesmogus", "内鬼饼干夹烤棉花糖之心");
    ItemEntry<Item> VOID_MATTER = register("void_matter", "虚空物质");
    ItemEntry<Item> TEMPORAL_MATTER = register("temporal_matter", "时间物质");
    ItemEntry<Item> PROTO_MATTER = register("proto_matter", "元始物质");
    ItemEntry<Item> OMNI_MATTER = register("omni_matter", "全物质");
    ItemEntry<Item> KINETIC_MATTER = register("kinetic_matter", "富动力物质");
    ItemEntry<Item> ESSENTIA_MATTER = register("essentia_matter", "根源物质");
    ItemEntry<Item> DARK_MATTER = register("dark_matter", "暗物质");
    ItemEntry<Item> CORPOREAL_MATTER = register("corporeal_matter", "凡尘物质");
    ItemEntry<Item> AMORPHOUS_MATTER = register("amorphous_matter", "非晶态物质");
    ItemEntry<Item> PELLET_ANTIMATTER = register("pellet_antimatter", "反物质");
    ItemEntry<Item> DYSON_SWARM_MODULE = register("dyson_swarm_module", "戴森球模块");
    ItemEntry<Item> GLACIO_SPIRIT = register("glacio_spirit", "霜原碎片");
    ItemEntry<Item> TIMEPIECE = register("timepiece", "时间碎片");
    ItemEntry<Item> PRECISION_STEAM_MECHANISM = register("precision_steam_mechanism", "精密蒸汽构件");
    ItemEntry<Item> INVERTER = register("inverter", "逆变器");
    ItemEntry<Item> GIGA_CHAD = register("giga_chad", "Giga Chad代币");
    ItemEntry<Item> REACTOR_FUEL_ROD = register("reactor_fuel_rod", "空燃料棒");
    ItemEntry<Item> TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD = register("tungsten_carbide_reactor_fuel_rod", "空碳化钨燃料棒");
    ItemEntry<Item> HUI_CIRCUIT_1 = registerLang("hui_circuit_1", "High Calculation Workstation MK-I", "高算力工作站 MK-I");
    ItemEntry<Item> HUI_CIRCUIT_2 = registerLang("hui_circuit_2", "High Calculation Workstation MK-II", "高算力工作站 MK-II");
    ItemEntry<Item> HUI_CIRCUIT_3 = registerLang("hui_circuit_3", "High Calculation Workstation MK-III", "高算力工作站 MK-III");
    ItemEntry<Item> HUI_CIRCUIT_4 = registerLang("hui_circuit_4", "High Calculation Workstation MK-IV", "高算力工作站 MK-IV");
    ItemEntry<Item> HUI_CIRCUIT_5 = registerLang("hui_circuit_5", "High Calculation Workstation MK-V", "高算力工作站 MK-V");
    ItemEntry<Item> SPECIAL_CERAMICS = register("special_ceramics", "特种陶瓷");
    ItemEntry<Item> PLANET_SCAN_SATELLITE = register("planet_scan_satellite", "行星扫描卫星");
    ItemEntry<Item> HYPERDIMENSIONAL_DRONE = register("hyperdimensional_drone", "超维度无人机");

    ItemEntry<Item> HOT_IRON_INGOT = registerCustomModel("hot_iron_ingot", "热铁锭");
    ItemEntry<Item> RAW_VACUUM_TUBE = registerCustomModel("raw_vacuum_tube", "粗真空管");
    ItemEntry<Item> INFINITE_CELL_COMPONENT = register("infinite_cell_component", "无限存储组件");
    ItemEntry<Item> PROTONATED_FULLERENE_SIEVING_MATRIX = register("protonated_fullerene_sieving_matrix", "质子化富勒烯筛分基质");
    ItemEntry<Item> SATURATED_FULLERENE_SIEVING_MATRIX = register("saturated_fullerene_sieving_matrix", "饱和富勒烯筛分基质");
    ItemEntry<Item> MICROFOCUS_X_RAY_TUBE = register("microfocus_x_ray_tube", "微焦点X射线管");
    ItemEntry<Item> SEPARATION_ELECTROMAGNET = register("separation_electromagnet", "分离用电磁铁");
    ItemEntry<Item> HIGHLY_INSULATING_FOIL = registerCustomModel("highly_insulating_foil", "高绝缘性箔");

    ItemEntry<Item> BLUE_ALGAE = registerAlgae("blue", "蓝");
    ItemEntry<Item> BROWN_ALGAE = registerAlgae("brown", "褐");
    ItemEntry<Item> GOLD_ALGAE = registerAlgae("gold", "金");
    ItemEntry<Item> GREEN_ALGAE = registerAlgae("green", "绿");
    ItemEntry<Item> RED_ALGAE = registerAlgae("red", "红");
    ItemEntry<Item> GOLD_ALGAE_FIBER = registerAlgaeFiber("gold", "金");
    ItemEntry<Item> GREEN_ALGAE_FIBER = registerAlgaeFiber("green", "绿");
    ItemEntry<Item> RED_ALGAE_FIBER = registerAlgaeFiber("red", "红");

    ItemEntry<Item> CEREBRUM = register("cerebrum", "大脑");
    ItemEntry<Item> SUPER_CEREBRUM = register("super_cerebrum", "超级大脑");
    ItemEntry<Item> PREPARATION_PETRI_DISH = register("preparation_petri_dish", "预备培养皿");
    ItemEntry<Item> STERILIZED_PETRI_DISH = registerCustomModel("sterilized_petri_dish", "无菌培养皿");
    ItemEntry<Item> ELECTRICALY_WIRED_PETRI_DISH = registerCustomModel("electricaly_wired_petri_dish", "电信号培养皿");
    ItemEntry<Item> CONTAMINATED_PETRI_DISH = register("contaminated_petri_dish", "污染的培养皿");
    ItemEntry<Item> BREVIBACTERIUM_PETRI_DISH = registerTexture("brevibacterium_petri_dish", "黄色短杆菌培养皿", "germ");
    ItemEntry<Item> BIFIDOBACTERIUMM_PETRI_DISH = registerTexture("bifidobacteriumm_petri_dish", "短双歧杆菌培养皿", "germ");
    ItemEntry<Item> ESCHERICIA_PETRI_DISH = registerTexture("eschericia_petri_dish", "大肠杆菌培养皿", "germ");
    ItemEntry<Item> STREPTOCOCCUS_PETRI_DISH = registerTexture("streptococcus_petri_dish", "酿脓链球菌培养皿", "germ");
    ItemEntry<Item> CUPRIAVIDUS_PETRI_DISH = registerTexture("cupriavidus_petri_dish", "贪铜钩虫菌培养皿", "germ");
    ItemEntry<Item> SHEWANELLA_PETRI_DISH = registerTexture("shewanella_petri_dish", "希瓦氏菌培养皿", "germ");

    ItemEntry<Item> CONVERSION_SIMULATE_CARD = register("conversion_simulate_card", "转换模拟卡");
    ItemEntry<Item> ACTIVATED_CARBON_FILTER_MESH = register("activated_carbon_filter_mesh", "活性炭过滤网");
    ItemEntry<Item> EMPTY_QUARK_RELEASE_CATALYST_HOUSING = register("empty_quark_release_catalyst_housing", "空夸克释放催化剂外壳");
    ItemEntry<Item> UNALIGNED_QUARK_RELEASING_CATALYST = register("unaligned_quark_releasing_catalyst", "未对齐夸克释放催化剂");
    ItemEntry<Item> UP_QUARK_RELEASING_CATALYST = register("up_quark_releasing_catalyst", "上夸克释放催化剂");
    ItemEntry<Item> DOWN_QUARK_RELEASING_CATALYST = register("down_quark_releasing_catalyst", "下夸克释放催化剂");
    ItemEntry<Item> BOTTOM_QUARK_RELEASING_CATALYST = register("bottom_quark_releasing_catalyst", "顶夸克释放催化剂");
    ItemEntry<Item> TOP_QUARK_RELEASING_CATALYST = register("top_quark_releasing_catalyst", "底夸克释放催化剂");
    ItemEntry<Item> STRANGE_QUARK_RELEASING_CATALYST = register("strange_quark_releasing_catalyst", "奇夸克释放催化剂");
    ItemEntry<Item> CHARM_QUARK_RELEASING_CATALYST = register("charm_quark_releasing_catalyst", "粲夸克释放催化剂");

    ItemEntry<Item> PLANT_FIBER = register("plant_fiber", "植物纤维");
    ItemEntry<Item> HEAVY_DUTY_PLATE_1 = register("heavy_duty_plate_1", "一阶重装防护板");
    ItemEntry<Item> HEAVY_DUTY_PLATE_2 = register("heavy_duty_plate_2", "二阶重装防护板");
    ItemEntry<Item> HEAVY_DUTY_PLATE_3 = register("heavy_duty_plate_3", "三阶重装防护板");

    ItemEntry<Item> INGOT_FIELD_SHAPE = register("ingot_field_shape", "锭状形态力场");
    ItemEntry<Item> BALL_FIELD_SHAPE = register("ball_field_shape", "球状形态力场");
    ItemEntry<Item> NON_LINEAR_OPTICAL_LENS = register("non_linear_optical_lens", "非线性光学透镜");
    ItemEntry<Item> CATALYST_BASE = register("catalyst_base", "催化剂基底");

    ItemEntry<Item> DIAMOND_CRYSTAL_CIRCUIT = register("diamond_crystal_circuit", "钻石晶体电路");
    ItemEntry<Item> RUBY_CRYSTAL_CIRCUIT = register("ruby_crystal_circuit", "红宝石晶体电路");
    ItemEntry<Item> EMERALD_CRYSTAL_CIRCUIT = register("emerald_crystal_circuit", "绿宝石晶体电路");
    ItemEntry<Item> SAPPHIRE_CRYSTAL_CIRCUIT = register("sapphire_crystal_circuit", "蓝宝石晶体电路");

    ItemEntry<Item> COOLANT_CELL_10K = registerLang("coolant_cell_10k", "10K Coolant Cell", "10K冷却单元");
    ItemEntry<Item> COOLANT_CELL_30K = registerLang("coolant_cell_30k", "30K Coolant Cell", "30K冷却单元");
    ItemEntry<Item> COOLANT_CELL_60K = registerLang("coolant_cell_60k", "60K Coolant Cell", "60K冷却单元");
    ItemEntry<Item> SPACE_COOLANT_CELL_10K = registerLang("space_coolant_cell_10k", "10K Space Coolant Cell", "10K空间冷却单元");
    ItemEntry<Item> SPACE_COOLANT_CELL_30K = registerLang("space_coolant_cell_30k", "30K Space Coolant Cell", "30K空间冷却单元");
    ItemEntry<Item> SPACE_COOLANT_CELL_60K = registerLang("space_coolant_cell_60k", "60K Space Coolant Cell", "60K空间冷却单元");

    ItemEntry<Item> NANOTUBE_SPOOL = register("nanotube_spool", "纳米管线轴");

    ItemEntry<Item> MICA_BASED_PULP = register("mica_based_pulp", "云母浆");
    ItemEntry<Item> MICA_BASED_SHEET = register("mica_based_sheet", "云母基板");
    ItemEntry<Item> MICA_INSULATOR_SHEET = register("mica_insulator_sheet", "云母绝缘板");
    ItemEntry<Item> MICA_INSULATOR_FOIL = register("mica_insulator_foil", "云母绝缘薄片");

    ItemEntry<Item> RAW_ALUMINUM = register("raw_aluminum", "生铝");

    ItemEntry<Item> HIGH_PURITY_SILICON_BOULE = register("high_purity_silicon_boule", "高纯多晶硅");
    ItemEntry<Item> REGIONAL_SMELTING_SILICON_BOULE = register("regional_smelting_silicon_boule", "区域熔炼的多晶硅");
    ItemEntry<Item> ETCHED_SILICON_BOULE = register("etched_silicon_boule", "电子束蚀刻的多晶硅");
    ItemEntry<Item> FLOATING_ZONE_PURIFICATION_SILICON_BOULE = register("floating_zone_purification_silicon_boule", "浮区提纯的多晶硅");
    ItemEntry<Item> HIGH_PURITY_SINGLE_CRYSTAL_SILICON = register("high_purity_single_crystal_silicon", "超高纯单晶硅");
    ItemEntry<Item> HIGH_PURITY_SILICA_COLUMN = register("high_purity_silica_column", "高纯二氧化硅柱");
    ItemEntry<Item> HIGH_PURITY_SILICA_TUBE = register("high_purity_silica_tube", "高纯二氧化硅管");
    ItemEntry<Item> SIMPLE_OPTICAL_FIBER_PREFORM = item("simple_optical_fiber_preform", "简易光纤预制棒", Item::new)
            .properties(p -> p.stacksTo(1).defaultDurability(1024)).register();
    ItemEntry<Item> SIMPLE_FIBER_OPTIC_ROUGH = register("simple_fiber_optic_rough", "简易光纤粗胚");
    ItemEntry<Item> SIMPLE_FIBER_OPTIC = register("simple_fiber_optic", "简易光纤");

    ItemEntry<Item> SPOOLS_MICRO = register("spools_micro", "微型线轴");
    ItemEntry<Item> SPOOLS_SMALL = register("spools_small", "小型线轴");
    ItemEntry<Item> SPOOLS_MEDIUM = register("spools_medium", "中型线轴");
    ItemEntry<Item> SPOOLS_LARGE = register("spools_large", "大型线轴");
    ItemEntry<Item> SPOOLS_JUMBO = register("spools_jumbo", "巨型线轴");

    ItemEntry<SpaceArmorComponentItem> SPACE_NANOMUSCLE_CHESTPLATE = item("space_nanomuscle_chestplate", "太空纳米胸甲",
            (p) -> new SpaceArmorComponentItem(GTArmorMaterials.ARMOR, ArmorItem.Type.CHESTPLATE, 5000, p).setArmorLogic(new NanoMuscleSuite(ArmorItem.Type.CHESTPLATE, 512, 6_400_000L * (long) Math.max(1, Math.pow(4, ConfigHolder.INSTANCE.tools.voltageTierNanoSuit - 3)), ConfigHolder.INSTANCE.tools.voltageTierNanoSuit)))
            .lang("NanoMuscle™ Space Suite Chestplate")
            .properties(p -> p.rarity(Rarity.RARE))
            .register();

    ItemEntry<SpaceArmorComponentItem> SPACE_ADVANCED_NANOMUSCLE_CHESTPLATE = item("space_advanced_nanomuscle_chestplate", "太空进阶纳米胸甲",
            (p) -> new SpaceArmorComponentItem(GTArmorMaterials.ARMOR, ArmorItem.Type.CHESTPLATE, 10000, p).setArmorLogic(new AdvancedNanoMuscleSuite(512, 12_800_000L * (long) Math.max(1, Math.pow(4, ConfigHolder.INSTANCE.tools.voltageTierAdvNanoSuit - 3)), ConfigHolder.INSTANCE.tools.voltageTierAdvNanoSuit)))
            .lang("Advanced NanoMuscle™ Space Suite Chestplate")
            .properties(p -> p.rarity(Rarity.EPIC))
            .register();

    ItemEntry<SpaceArmorComponentItem> SPACE_QUARKTECH_CHESTPLATE = item("space_quarktech_chestplate", "太空夸克胸甲",
            (p) -> new SpaceArmorComponentItem(GTArmorMaterials.ARMOR, ArmorItem.Type.CHESTPLATE, 20000, p).setArmorLogic(new QuarkTechSuite(ArmorItem.Type.CHESTPLATE, 8192, 100_000_000L * (long) Math.max(1, Math.pow(4, ConfigHolder.INSTANCE.tools.voltageTierQuarkTech - 5)), ConfigHolder.INSTANCE.tools.voltageTierQuarkTech)))
            .lang("QuarkTech™ Space Suite Chestplate")
            .properties(p -> p.rarity(Rarity.RARE))
            .register();

    ItemEntry<SpaceArmorComponentItem> SPACE_ADVANCED_QUARKTECH_CHESTPLATE = item("space_advanced_quarktech_chestplate", "太空进阶夸克胸甲",
            (p) -> new SpaceArmorComponentItem(GTArmorMaterials.ARMOR, ArmorItem.Type.CHESTPLATE, 50000, p).setArmorLogic(new AdvancedQuarkTechSuite(8192, 1_000_000_000L * (long) Math.max(1, Math.pow(4, ConfigHolder.INSTANCE.tools.voltageTierAdvQuarkTech - 6)), ConfigHolder.INSTANCE.tools.voltageTierAdvQuarkTech)))
            .lang("Advanced QuarkTech™ Space Suite Chestplate")
            .properties(p -> p.rarity(Rarity.EPIC))
            .register();

    ItemEntry<UpgradeModuleItem> SPEED_UPGRADE_MODULE = item("speed_upgrade_module", "速度升级模块", UpgradeModuleItem::new).register();
    ItemEntry<UpgradeModuleItem> ENERGY_UPGRADE_MODULE = item("energy_upgrade_module", "能量升级模块", UpgradeModuleItem::new).register();

    ItemEntry<Item> DISPOSABLE_FILE = item("disposable_file", "一次性锉刀", Item::new)
            .tag(CustomTags.CRAFTING_FILES).register();

    ItemEntry<Item> DISPOSABLE_WRENCH = item("disposable_wrench", "一次性扳手", Item::new)
            .tag(CustomTags.CRAFTING_WRENCHES).register();

    ItemEntry<Item> DISPOSABLE_CROWBAR = item("disposable_crowbar", "一次性撬棍", Item::new)
            .tag(CustomTags.CRAFTING_CROWBARS).register();

    ItemEntry<Item> DISPOSABLE_WIRE_CUTTER = item("disposable_wire_cutter", "一次性剪线钳", Item::new)
            .tag(CustomTags.CRAFTING_WIRE_CUTTERS).register();

    ItemEntry<Item> DISPOSABLE_HAMMER = item("disposable_hammer", "一次性锤子", Item::new)
            .tag(CustomTags.CRAFTING_HAMMERS).register();

    ItemEntry<Item> DISPOSABLE_MALLET = item("disposable_mallet", "一次性软锤", Item::new)
            .tag(CustomTags.CRAFTING_MALLETS).register();

    ItemEntry<Item> DISPOSABLE_SCREWDRIVER = item("disposable_screwdriver", "一次性螺丝刀", Item::new)
            .tag(CustomTags.CRAFTING_SCREWDRIVERS).register();

    ItemEntry<Item> DISPOSABLE_SAW = item("disposable_saw", "一次性锯子", Item::new)
            .tag(CustomTags.CRAFTING_SAWS).register();

    ItemEntry<Item> DISPOSABLE_FILE_MOLD = register("disposable_file_mold", "一次性锉刀模具");
    ItemEntry<Item> DISPOSABLE_WRENCH_MOLD = register("disposable_wrench_mold", "一次性扳手模具");
    ItemEntry<Item> DISPOSABLE_CROWBAR_MOLD = register("disposable_crowbar_mold", "一次性撬棍模具");
    ItemEntry<Item> DISPOSABLE_WIRE_CUTTER_MOLD = register("disposable_wire_cutter_mold", "一次性剪线钳模具");
    ItemEntry<Item> DISPOSABLE_HAMMER_MOLD = register("disposable_hammer_mold", "一次性锤模具");
    ItemEntry<Item> DISPOSABLE_MALLET_MOLD = register("disposable_mallet_mold", "一次性软锤模具");
    ItemEntry<Item> DISPOSABLE_SCREWDRIVER_MOLD = register("disposable_screwdriver_mold", "一次性螺丝刀模具");
    ItemEntry<Item> DISPOSABLE_SAW_MOLD = register("disposable_saw_mold", "一次性锯模具");
    ItemEntry<ComponentItem> GRASS_HARVESTER = item("grass_harvester", "割草镰刀", ComponentItem::create).properties(p -> p.stacksTo(1).durability(128).setNoRepair()).onRegister(attach(GrassHarvesterBehaviour.INSTANCE)).register();
}
