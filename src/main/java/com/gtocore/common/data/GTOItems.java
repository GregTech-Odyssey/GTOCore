package com.gtocore.common.data;

import com.gtocore.api.misc.AutoInitializeImpl;
import com.gtocore.client.renderer.item.HaloItemRenderer;
import com.gtocore.client.renderer.item.OrderItemProviderRenderer;
import com.gtocore.common.cover.PowerAmplifierCover;
import com.gtocore.common.data.translation.GTOItemTooltips;
import com.gtocore.common.item.*;
import com.gtocore.common.item.armor.SpaceArmorComponentItem;
import com.gtocore.common.item.misc.GrassHarvesterBehaviour;

import com.gtolib.GTOCore;
import com.gtolib.ae2.me2in1.Wireless;
import com.gtolib.api.annotation.NewDataAttributes;
import com.gtolib.api.annotation.component_builder.ComponentBuilder;
import com.gtolib.api.annotation.component_builder.ComponentSupplier;
import com.gtolib.api.annotation.component_builder.StyleBuilder;
import com.gtolib.api.lang.CNEN;
import com.gtolib.utils.StringUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.material.properties.PropertyKey;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.ItemMaterialInfo;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialStack;
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
import net.minecraftforge.common.Tags;

import appeng.items.materials.StorageComponentItem;
import com.enderio.base.common.init.EIOFluids;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import earth.terrarium.adastra.common.registry.ModFluids;

import java.util.List;

import static com.gregtechceu.gtceu.common.data.GTItems.*;
import static com.gregtechceu.gtceu.common.data.GTItems.attach;
import static com.gtolib.api.registries.GTORegistration.GTM;
import static com.gtolib.utils.register.ItemRegisterUtils.*;

public final class GTOItems {

    public static void init() {
        GTOOrganItems.INSTANCE.init();
        GTOAEParts.INSTANCE.init();
        AutoInitializeImpl.INSTANCE.originInit();
        GTMaterials.Oxygen.getProperty(PropertyKey.FLUID).getStorage().store(FluidStorageKeys.GAS, ModFluids.OXYGEN, null);
        GTMaterials.Hydrogen.getProperty(PropertyKey.FLUID).getStorage().store(FluidStorageKeys.GAS, ModFluids.HYDROGEN, null);
        GTFluids.handleNonMaterialFluids(GTMaterials.Oil, ModFluids.OIL);
        GTFluids.handleNonMaterialFluids(GTMaterials.RocketFuel, () -> EIOFluids.ROCKET_FUEL.get().getSource());

        if (GTCEu.isDev()) {
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

            item("ui_tester", "UI测试器", ComponentItem::create)
                    .properties(p -> p.stacksTo(1))
                    .onRegister(attach(new TesterBehaviour()))
                    .model(NonNullBiConsumer.noop())
                    .register();
        }
    }

    public static final ItemEntry<Item> SHAPE_EXTRUDER_ROD_LONG = GTM.item("long_rod_extruder_mold", Item::new).onRegister(materialInfo(new ItemMaterialInfo(new MaterialStack(GTMaterials.Steel, GTValues.M << 2)))).register();

    public static final ItemEntry<StorageComponentItem> CELL_COMPONENT_1M = registerStorageComponentItem(1);
    public static final ItemEntry<StorageComponentItem> CELL_COMPONENT_4M = registerStorageComponentItem(4);
    public static final ItemEntry<StorageComponentItem> CELL_COMPONENT_16M = registerStorageComponentItem(16);
    public static final ItemEntry<StorageComponentItem> CELL_COMPONENT_64M = registerStorageComponentItem(64);
    public static final ItemEntry<StorageComponentItem> CELL_COMPONENT_256M = registerStorageComponentItem(256);

    private static final ComponentSupplier ORDER_TOOLTIPS = NewDataAttributes.MIRACULOUS_TOOLS.create(CNEN.create("AE2 Order", "AE2 订单"), b -> b.addCommentLines("""
            右键可以放入一个虚拟物品，例如多方块主机
            不需要再在铁砧使用告示牌命名
            可以作为AE自动合成的大型机器产物
            当此合成完成时，会自动取消，无需手动取消""",
            """
                    Right click to put a virtual item, such as a multi-block machine
                    No longer need to use a sign to name it in anvil
                    Can be used as a large machine product for AE2 automatic synthesis
                    When the synthesis is completed, it will automatically cancel, no need to cancel manually"""));
    private static final List<Component> ORDER_TOOLTIPS_DATA_GEN_INITIALIZATION = ORDER_TOOLTIPS.get();
    public static final ItemEntry<ComponentItem> ORDER = item("order", "%s 订单", ComponentItem::create)
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(OrderItem.INSTANCE))
            .onRegister(attach(new TooltipBehavior(a -> a.addAll(ORDER_TOOLTIPS.get()))))
            .onRegister(attachRenderer(() -> OrderItemProviderRenderer.INSTANCE))
            .register();
    public static final ItemEntry<ComponentItem> REALLY_MAX_BATTERY = item("really_max_battery", "真·终极电池", ComponentItem::create)
            .lang("Really MAX Battery")
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.translatable("gtocore.tooltip.item.really_max_battery").withStyle(ChatFormatting.GRAY)))))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, GTValues.UEV)))
            .register();
    public static final ItemEntry<ComponentItem> TRANSCENDENT_MAX_BATTERY = item("transcendent_max_battery", "超·终极电池", ComponentItem::create)
            .lang("Transcendent MAX Battery")
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.translatable("gtocore.tooltip.item.transcendent_max_battery").withStyle(ChatFormatting.GRAY)))))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, GTValues.UIV)))
            .register();
    public static final ItemEntry<ComponentItem> EXTREMELY_MAX_BATTERY = item("extremely_max_battery", "极·终极电池", ComponentItem::create)
            .lang("Extremely MAX Battery")
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.translatable("gtocore.tooltip.item.extremely_max_battery").withStyle(ChatFormatting.GRAY)))))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, GTValues.UXV)))
            .register();
    public static final ItemEntry<ComponentItem> INSANELY_MAX_BATTERY = item("insanely_max_battery", "狂·终极电池", ComponentItem::create)
            .lang("Insanely MAX Battery")
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.literal(StringUtils.dark_purplish_red(LocalizationUtils.format("gtocore.tooltip.item.insanely_max_battery")))))))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, GTValues.OpV)))
            .register();
    public static final ItemEntry<ComponentItem> MEGA_MAX_BATTERY = item("mega_max_battery", "兆·终极电池", ComponentItem::create)
            .lang("Mega MAX Battery")
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.literal(StringUtils.full_color(LocalizationUtils.format("gtocore.tooltip.item.mega_max_battery")))))))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(ElectricStats.createRechargeableBattery(Long.MAX_VALUE, GTValues.MAX)))
            .register();

    public static final ItemEntry<ComponentItem> SUPER_CAPACITOR = item("super_capacitor", "超级电容", ComponentItem::create)
            .onRegister(attach(ElectricStats.createRechargeableBattery(100000, GTValues.ULV)))
            .tag(CustomTags.ULV_BATTERIES).register();

    public static final ItemEntry<ComponentItem> HV_DRONE = item("hv_drone", "基础无人机", ComponentItem::create)
            .lang("Base Drone")
            .properties(p -> p.stacksTo(1))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(new DroneBehavior(GTValues.HV)))
            .register();

    public static final ItemEntry<ComponentItem> EV_DRONE = item("ev_drone", "高级无人机", ComponentItem::create)
            .lang("Advanced Drone")
            .properties(p -> p.stacksTo(1))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(new DroneBehavior(GTValues.EV)))
            .register();

    public static final ItemEntry<ComponentItem> IV_DRONE = item("iv_drone", "终极无人机", ComponentItem::create)
            .lang("Ultimate Drone")
            .properties(p -> p.stacksTo(1))
            .onRegister(modelPredicate(GTCEu.id("battery"), ElectricStats::getStoredPredicate))
            .onRegister(attach(new DroneBehavior(GTValues.IV)))
            .register();

    public static final ItemEntry<ComponentItem> MAX_ELECTRIC_PUMP = item("max_electric_pump", "§4§lMAX§r电动泵", ComponentItem::create)
            .lang("MAX Electric Pump")
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.ELECTRIC_PUMP_MAX)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.electric.pump.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.fluid_transfer_rate",
                        1280 * 64 * 64 * 4 / 20));
            })))
            .register();

    public static final ItemEntry<ComponentItem> MAX_CONVEYOR_MODULE = item("max_conveyor_module", "§4§lMAX§r传送带", ComponentItem::create)
            .lang("MAX Conveyor Module")
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.CONVEYOR_MODULE_MAX)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.conveyor.module.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.item_transfer_rate_stacks", 16));
            })))
            .register();

    public static final ItemEntry<ComponentItem> MAX_ROBOT_ARM = item("max_robot_arm", "§4§lMAX§r机械臂", ComponentItem::create)
            .lang("MAX Robot Arm")
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.ROBOT_ARM_MAX)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.robot.arm.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.item_transfer_rate_stacks", 16));
            })))
            .register();

    public static final ItemEntry<Item> MAX_ELECTRIC_MOTOR = registerLang("max_electric_motor", "MAX Electric Motor", "§4§lMAX§r电动马达");
    public static final ItemEntry<Item> MAX_ELECTRIC_PISTON = registerLang("max_electric_piston", "MAX Electric Piston", "§4§lMAX§r电力活塞");
    public static final ItemEntry<Item> MAX_FIELD_GENERATOR = registerLang("max_field_generator", "MAX Field Generator", "§4§lMAX§r力场发生器");
    public static final ItemEntry<Item> MAX_EMITTER = registerLang("max_emitter", "MAX Emitte", "§4§lMAX§r发射器");
    public static final ItemEntry<Item> MAX_SENSOR = registerLang("max_sensor", "MAX Sensor", "§4§lMAX§r传感器");

    public static final ItemEntry<ComponentItem> ULV_ELECTRIC_PUMP = item("ulv_electric_pump", "ULV电动泵", ComponentItem::create)
            .lang("ULV Electric Pump")
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.ELECTRIC_PUMP_ULV)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.electric.pump.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.fluid_transfer_rate", (1280 / 2) / 20));
            })))
            .register();

    public static final ItemEntry<ComponentItem> ULV_CONVEYOR_MODULE = item("ulv_conveyor_module", "ULV传送带", ComponentItem::create)
            .lang("ULV Conveyor Module")
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.CONVEYOR_MODULE_ULV)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.conveyor.module.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.item_transfer_rate", 2));
            })))
            .register();

    public static final ItemEntry<ComponentItem> ULV_FLUID_REGULATOR = item("ulv_fluid_regulator", "ULV流体校准器", ComponentItem::create)
            .lang("ULV Fluid Regulator")
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.FLUID_REGULATOR_ULV)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.fluid.regulator.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.fluid_transfer_rate", (1280 / 2) / 20));
            })))
            .register();

    public static final ItemEntry<ComponentItem> ULV_ROBOT_ARM = item("ulv_robot_arm", "ULV机械臂", ComponentItem::create)
            .lang("ULV Robot Arm")
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.ROBOT_ARM_ULV)))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.robot.arm.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.item_transfer_rate", 2));
            })))
            .register();

    public static final ItemEntry<Item> ULV_ELECTRIC_MOTOR = registerLang("ulv_electric_motor", "ULV Electric Motor", "ULV电动马达");
    public static final ItemEntry<Item> ULV_ELECTRIC_PISTON = registerLang("ulv_electric_piston", "ULV Electric Piston", "ULV电力活塞");

    public static final ItemEntry<ComponentItem> LV_POWER_AMPLIFIERS = item("lv_power_amplifiers", "LV功率增幅器", ComponentItem::create)
            .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/power_amplifiers/lv_power_amplifiers")))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(1D / PowerAmplifierCover.getMultiplier(1))));
                lines.add(Component.translatable("gtocore.machine.eut_multiplier.tooltip", FormattingUtil.formatNumbers(PowerAmplifierCover.getMultiplier(1))));
            }), new CoverPlaceBehavior(GTOCovers.POWER_AMPLIFIERS[0])))
            .register();

    public static final ItemEntry<ComponentItem> MV_POWER_AMPLIFIERS = item("mv_power_amplifiers", "MV功率增幅器", ComponentItem::create)
            .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/power_amplifiers/mv_power_amplifiers")))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(1D / PowerAmplifierCover.getMultiplier(2))));
                lines.add(Component.translatable("gtocore.machine.eut_multiplier.tooltip", FormattingUtil.formatNumbers(PowerAmplifierCover.getMultiplier(2))));
            }), new CoverPlaceBehavior(GTOCovers.POWER_AMPLIFIERS[1])))
            .register();

    public static final ItemEntry<ComponentItem> HV_POWER_AMPLIFIERS = item("hv_power_amplifiers", "HV功率增幅器", ComponentItem::create)
            .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/power_amplifiers/hv_power_amplifiers")))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(1D / PowerAmplifierCover.getMultiplier(3))));
                lines.add(Component.translatable("gtocore.machine.eut_multiplier.tooltip", FormattingUtil.formatNumbers(PowerAmplifierCover.getMultiplier(3))));
            }), new CoverPlaceBehavior(GTOCovers.POWER_AMPLIFIERS[2])))
            .register();

    public static final ItemEntry<ComponentItem> EV_POWER_AMPLIFIERS = item("ev_power_amplifiers", "EV功率增幅器", ComponentItem::create)
            .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/power_amplifiers/ev_power_amplifiers")))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(1D / PowerAmplifierCover.getMultiplier(4))));
                lines.add(Component.translatable("gtocore.machine.eut_multiplier.tooltip", FormattingUtil.formatNumbers(PowerAmplifierCover.getMultiplier(4))));
            }), new CoverPlaceBehavior(GTOCovers.POWER_AMPLIFIERS[3])))
            .register();

    public static final ItemEntry<ComponentItem> IV_POWER_AMPLIFIERS = item("iv_power_amplifiers", "IV功率增幅器", ComponentItem::create)
            .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/power_amplifiers/iv_power_amplifiers")))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(1D / PowerAmplifierCover.getMultiplier(5))));
                lines.add(Component.translatable("gtocore.machine.eut_multiplier.tooltip", FormattingUtil.formatNumbers(PowerAmplifierCover.getMultiplier(5))));
            }), new CoverPlaceBehavior(GTOCovers.POWER_AMPLIFIERS[4])))
            .register();

    public static final ItemEntry<ComponentItem> LUV_POWER_AMPLIFIERS = item("luv_power_amplifiers", "LuV功率增幅器", ComponentItem::create)
            .model((ctx, prov) -> prov.generated(ctx, GTOCore.id("item/power_amplifiers/luv_power_amplifiers")))
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(1D / PowerAmplifierCover.getMultiplier(6))));
                lines.add(Component.translatable("gtocore.machine.eut_multiplier.tooltip", FormattingUtil.formatNumbers(PowerAmplifierCover.getMultiplier(6))));
            }), new CoverPlaceBehavior(GTOCovers.POWER_AMPLIFIERS[5])))
            .register();

    public static final ItemEntry<ComponentItem> AIR_VENT = item("air_vent", "通风口", ComponentItem::create)
            .onRegister(attach(new TooltipBehavior(lines -> lines.add(Component.translatable("gtceu.universal.tooltip.produces_fluid", 10))), new CoverPlaceBehavior(GTOCovers.AIR_VENT)))
            .register();

    public static final ItemEntry<ComponentItem> STEAM_PUMP = item("steam_pump", "蒸汽泵", ComponentItem::create)
            .onRegister(attach(new TooltipBehavior(lines -> {
                lines.add(Component.translatable("item.gtceu.electric.pump.tooltip"));
                lines.add(Component.translatable("gtceu.universal.tooltip.fluid_transfer_rate", FormattingUtil.formatNumbers(1000000)));
            }), new CoverPlaceBehavior(GTOCovers.STEAM_PUMP)))
            .register();

    public static final ItemEntry<ComponentItem> WIRELESS_CHARGER_COVER = item("wireless_charger_cover", "无线充能覆盖板", ComponentItem::create)
            .toolTips(ComponentBuilder.create()
                    .addLines("贴在存储方块上可使内部的物品自动充能", "Attach to a storage block to automatically charge its contents")
                    .addLines("需要链接无线充能器使用", "Requires a wireless charger to use")
                    .build().getArray())
            .onRegister(attach(new CoverPlaceBehavior(GTOCovers.WIRELESS_CHARGER_COVER)))
            .register();

    public static final ItemEntry<ComponentItem> WIRELESS_ENERGY_RECEIVE_COVER_MAX = registerTieredCover(1);

    public static final ItemEntry<ComponentItem> WIRELESS_ENERGY_RECEIVE_COVER_MAX_4A = registerTieredCover(4);

    public static final ItemEntry<ComponentItem> TIME_TWISTER = item("time_twister", "时间扭曲者", ComponentItem::create)
            .toolTips(ComponentBuilder.create()
                    .addLines(NewDataAttributes.MIRACULOUS_TOOLS.create(CNEN.create("时间扭曲者", "Time Twister")), a -> a)
                    .addLines(NewDataAttributes.EMPTY_WITH_BAR.create(CNEN.create("对普通方块实体的加速：", "Acceleration for normal block entities")), StyleBuilder::setGold)
                    .addLines(NewDataAttributes.EMPTY_WITH_TAB.create(CNEN.create("普通点击：消耗8192 EU能量，使方块实体额外执行200个游戏刻(tick)", "Consume 8192 EU energy, make the block entity execute 200 ticks extra")), StyleBuilder::setAqua)
                    .addLines(NewDataAttributes.EMPTY_WITH_TAB.create(CNEN.create("Shift点击：消耗819200 EU能量，持续100刻内加速目标方块", "Consume 819200 EU energy, accelerate the target block for 100 ticks")), StyleBuilder::setAqua)
                    .addLines(NewDataAttributes.EMPTY_WITH_BAR.create(CNEN.create("对GT机器的加速：", "Acceleration for GT machines")), StyleBuilder::setGold)
                    .addLines(NewDataAttributes.EMPTY_WITH_TAB.create(CNEN.create("普通点击：消耗相应EU能量，使当前正在工作的机器进度立即增加50%", "Consume corresponding EU energy, make the current working machine progress increase by 50% immediately")), StyleBuilder::setAqua)
                    .addLines(NewDataAttributes.EMPTY_WITH_TAB.create(CNEN.create("Shift点击：消耗819200 EU能量，持续100刻内加速GT机器", "Consume 819200 EU energy, accelerate GT machines for 100 ticks")), StyleBuilder::setAqua)
                    .addLines(NewDataAttributes.EMPTY_WITH_BAR.create(CNEN.create("能量消耗：", "Energy consumption")), StyleBuilder::setGold)
                    .addLines(NewDataAttributes.EMPTY_WITH_TAB.create(CNEN.create("使用无线能量系统作为能量来源", "Use wireless energy system as energy source")), StyleBuilder::setAqua)
                    .addLines(NewDataAttributes.EMPTY_WITH_TAB.create(CNEN.create("不同操作消耗不同数量的EU", "Different operations consume different numbers of EU")), StyleBuilder::setAqua)
                    .build().getArray())
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(TimeTwisterBehavior.INSTANCE))
            .register();

    public static final ItemEntry<ComponentItem> STRUCTURE_DETECT = item("structure_detect", "结构检测工具", ComponentItem::create)
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(StructureDetectBehavior.INSTANCE))
            .model(NonNullBiConsumer.noop())
            .register();

    public static final ItemEntry<ComponentItem> PATTERN_MODIFIER_PRO = item("pattern_modifier_pro", "样板修改器pro", ComponentItem::create)
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(PatternModifierProBehavior.INSTANCE))
            .onRegister(attach(new TooltipBehavior(GTOItemTooltips.INSTANCE.getPatternModifierTooltips()::apply)))
            .model(NonNullBiConsumer.noop())
            .register();

    public static final ItemEntry<BucketItem> GELID_CRYOTHEUM_BUCKET = item("gelid_cryotheum_bucket", "极寒之凛冰桶", p -> new BucketItem(GTOFluids.GELID_CRYOTHEUM, p.craftRemainder(Items.BUCKET).stacksTo(1).rarity(Rarity.COMMON)))
            .model(NonNullBiConsumer.noop())
            .register();

    public static final ItemEntry<Item> COMMAND_WAND = item("command_wand", "命令权杖", Item::new)
            .properties(p -> p.stacksTo(1))
            .model(NonNullBiConsumer.noop())
            .register();

    public static final ItemEntry<Item> GRINDBALL_SOAPSTONE = item("grindball_soapstone", "皂石研磨球", Item::new)
            .properties(p -> p.stacksTo(1).defaultDurability(50))
            .register();

    public static final ItemEntry<Item> GRINDBALL_ALUMINIUM = item("grindball_aluminium", "铝研磨球", Item::new)
            .properties(p -> p.stacksTo(1).defaultDurability(100))
            .register();

    public static final ItemEntry<PlanetDataChipItem> PLANET_DATA_CHIP = item("planet_data_chip", "星球数据芯片", PlanetDataChipItem::new)
            .register();

    public static final ItemEntry<DimensionDataItem> DIMENSION_DATA = item("dimension_data", "维度数据", DimensionDataItem::new).register();

    public static final ItemEntry<ComponentItem> OPTICAL_DATA_STICK = item("optical_data_stick", "光学闪存", ComponentItem::create)
            .onRegister(attach(new DataItemBehavior(true)))
            .register();

    public static final ItemEntry<ComponentItem> CLOSED_TIMELIKE_CURVE_GUIDANCE_UNIT = item("closed_timelike_curve_guidance_unit", "封闭类时曲线引导单元", ComponentItem::create)
            .onRegister(attach(new DataItemBehavior(true)))
            .register();

    public static final ItemEntry<DiscItem> DATA_DISC = item("data_disc", "数据光盘", DiscItem::new)
            .register();

    public static final ItemEntry<ComponentItem> NEURAL_MATRIX = item("neural_matrix", "神经矩阵", ComponentItem::create)
            .onRegister(attach(new DataItemBehavior(true)))
            .register();// UV生物活性CPU解锁制造,UHV光学主机后开始使用

    public static final ItemEntry<ComponentItem> ATOMIC_ARCHIVES = item("atomic_archives", "原子档案", ComponentItem::create)
            .onRegister(attachRenderer(() -> HaloItemRenderer.QUANTUM_CHROMO_DYNAMICALLY_HALO))
            .onRegister(attach(new DataItemBehavior(true)))
            .register();// UEV奇异CPU后解锁,UIV中期开始使用

    public static final ItemEntry<ComponentItem> OBSIDIAN_MATRIX = item("obsidian_matrix", "黑曜石矩阵", ComponentItem::create)
            .onRegister(attach(new DataItemBehavior(true)))
            .register();// UXV暗物质后解锁,UXV后期开始使用

    public static final ItemEntry<ComponentItem> MICROCOSM = item("microcosm", "微缩宇宙", ComponentItem::create)
            .onRegister(attachRenderer(() -> HaloItemRenderer.COSMIC_HALO))
            .onRegister(attach(new DataItemBehavior(true)))
            .register();// OPV鸿蒙之眼后解锁,MAX初期开使用

    public static final ItemEntry<Item> DATA_CRYSTAL_COMPONENT_MK1 = register("data_crystal_component_mk1", "数据晶片组件 mk1");
    public static final ItemEntry<Item> DATA_CRYSTAL_COMPONENT_MK2 = register("data_crystal_component_mk2", "数据晶片组件 mk2");
    public static final ItemEntry<Item> DATA_CRYSTAL_COMPONENT_MK3 = register("data_crystal_component_mk3", "数据晶片组件 mk3");
    public static final ItemEntry<Item> DATA_CRYSTAL_COMPONENT_MK4 = register("data_crystal_component_mk4", "数据晶片组件 mk4");
    public static final ItemEntry<Item> DATA_CRYSTAL_COMPONENT_MK5 = register("data_crystal_component_mk5", "数据晶片组件 mk5");

    public static final ItemEntry<ComponentItem> DATA_CRYSTAL_MK1 = item("data_crystal_mk1", "数据晶片 mk1", ComponentItem::create)
            .onRegister(attach(new ExDataItemBehavior()))
            .register();
    public static final ItemEntry<ComponentItem> DATA_CRYSTAL_MK2 = item("data_crystal_mk2", "数据晶片 mk2", ComponentItem::create)
            .onRegister(attach(new ExDataItemBehavior()))
            .register();
    public static final ItemEntry<ComponentItem> DATA_CRYSTAL_MK3 = item("data_crystal_mk3", "数据晶片 mk3", ComponentItem::create)
            .onRegister(attach(new ExDataItemBehavior()))
            .register();
    public static final ItemEntry<ComponentItem> DATA_CRYSTAL_MK4 = item("data_crystal_mk4", "数据晶片 mk4", ComponentItem::create)
            .onRegister(attach(new ExDataItemBehavior()))
            .register();
    public static final ItemEntry<ComponentItem> DATA_CRYSTAL_MK5 = item("data_crystal_mk5", "数据晶片 mk5", ComponentItem::create)
            .onRegister(attach(new ExDataItemBehavior()))
            .register();

    public static final ItemEntry<KineticRotorItem> WOOD_ROTOR = registerRotor("wood_kinetic_rotor", "木", 2400, 4, 10, 0);
    public static final ItemEntry<KineticRotorItem> IRON_ROTOR = registerRotor("iron_kinetic_rotor", "铁", 14000, 10, 20, 1);
    public static final ItemEntry<KineticRotorItem> STEEL_ROTOR = registerRotor("steel_kinetic_rotor", "钢", 16000, 10, 30, 1);
    public static final ItemEntry<KineticRotorItem> CARBON_ROTOR = registerRotor("carbon_kinetic_rotor", "碳", 24000, 2, 40, 2);

    public static final ItemEntry<Item> BIOWARE_PROCESSOR = registerCircuit("bioware_processor", "生物活性处理器", CustomTags.ZPM_CIRCUITS, () -> StringUtils.dark_green(I18n.get("gtocore.tooltip.item.tier_circuit", "ZPM")));
    public static final ItemEntry<Item> BIOWARE_ASSEMBLY = registerCircuit("bioware_assembly", "生物活性处理器集群", CustomTags.UV_CIRCUITS, () -> StringUtils.dark_green(I18n.get("gtocore.tooltip.item.tier_circuit", "UV")));
    public static final ItemEntry<Item> BIOWARE_COMPUTER = registerCircuit("bioware_computer", "生物活性处理器超级计算机", CustomTags.UHV_CIRCUITS, () -> StringUtils.dark_green(I18n.get("gtocore.tooltip.item.tier_circuit", "UHV")));
    public static final ItemEntry<Item> BIOWARE_MAINFRAME = registerCircuit("bioware_mainframe", "生物活性处理器主机", CustomTags.UEV_CIRCUITS, () -> StringUtils.dark_green(I18n.get("gtocore.tooltip.item.tier_circuit", "UEV")));

    public static final ItemEntry<Item> OPTICAL_PROCESSOR = registerCircuit("optical_processor", "光学处理器", CustomTags.UV_CIRCUITS, () -> StringUtils.golden(I18n.get("gtocore.tooltip.item.tier_circuit", "UV")));
    public static final ItemEntry<Item> OPTICAL_ASSEMBLY = registerCircuit("optical_assembly", "光学处理器集群", CustomTags.UHV_CIRCUITS, () -> StringUtils.golden(I18n.get("gtocore.tooltip.item.tier_circuit", "UHV")));
    public static final ItemEntry<Item> OPTICAL_COMPUTER = registerCircuit("optical_computer", "光学处理器超级计算机", CustomTags.UEV_CIRCUITS, () -> StringUtils.golden(I18n.get("gtocore.tooltip.item.tier_circuit", "UEV")));
    public static final ItemEntry<Item> OPTICAL_MAINFRAME = registerCircuit("optical_mainframe", "光学处理器主机", CustomTags.UIV_CIRCUITS, () -> StringUtils.golden(I18n.get("gtocore.tooltip.item.tier_circuit", "UIV")));

    public static final ItemEntry<Item> EXOTIC_PROCESSOR = registerCircuit("exotic_processor", "奇异处理器", CustomTags.UHV_CIRCUITS, () -> StringUtils.purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UHV")));
    public static final ItemEntry<Item> EXOTIC_ASSEMBLY = registerCircuit("exotic_assembly", "奇异处理器集群", CustomTags.UEV_CIRCUITS, () -> StringUtils.purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UEV")));
    public static final ItemEntry<Item> EXOTIC_COMPUTER = registerCircuit("exotic_computer", "奇异处理器超级计算机", CustomTags.UIV_CIRCUITS, () -> StringUtils.purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UIV")));
    public static final ItemEntry<Item> EXOTIC_MAINFRAME = registerCircuit("exotic_mainframe", "奇异处理器主机", CustomTags.UXV_CIRCUITS, () -> StringUtils.purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UXV")));

    public static final ItemEntry<Item> COSMIC_PROCESSOR = registerCircuit("cosmic_processor", "寰宇处理器", CustomTags.UEV_CIRCUITS, () -> StringUtils.dark_purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UEV")));
    public static final ItemEntry<Item> COSMIC_ASSEMBLY = registerCircuit("cosmic_assembly", "寰宇处理器集群", CustomTags.UIV_CIRCUITS, () -> StringUtils.dark_purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UIV")));
    public static final ItemEntry<Item> COSMIC_COMPUTER = registerCircuit("cosmic_computer", "寰宇处理器超级计算机", CustomTags.UXV_CIRCUITS, () -> StringUtils.dark_purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "UXV")));
    public static final ItemEntry<Item> COSMIC_MAINFRAME = registerCircuit("cosmic_mainframe", "寰宇处理器主机", CustomTags.OpV_CIRCUITS, () -> StringUtils.dark_purplish_red(I18n.get("gtocore.tooltip.item.tier_circuit", "OpV")));

    public static final ItemEntry<Item> SUPRACAUSAL_PROCESSOR = registerCircuit("supracausal_processor", "超因果处理器", CustomTags.UIV_CIRCUITS, () -> StringUtils.full_color(I18n.get("gtocore.tooltip.item.tier_circuit", "UIV")));
    public static final ItemEntry<Item> SUPRACAUSAL_ASSEMBLY = registerCircuit("supracausal_assembly", "超因果处理器集群", CustomTags.UXV_CIRCUITS, () -> StringUtils.full_color(I18n.get("gtocore.tooltip.item.tier_circuit", "UXV")));
    public static final ItemEntry<Item> SUPRACAUSAL_COMPUTER = registerCircuit("supracausal_computer", "超因果处理器超级计算机", CustomTags.OpV_CIRCUITS, () -> StringUtils.full_color(I18n.get("gtocore.tooltip.item.tier_circuit", "OpV")));
    public static final ItemEntry<Item> SUPRACAUSAL_MAINFRAME = registerCircuit("supracausal_mainframe", "超因果处理器主机", CustomTags.MAX_CIRCUITS, () -> StringUtils.full_color(I18n.get("gtocore.tooltip.item.tier_circuit", "MAX")));

    public static final ItemEntry<Item>[] SUPRACHRONAL_CIRCUIT = registerCircuits("suprachronal_circuit", "超时空电路", GTValues.tiersBetween(GTValues.ULV, GTValues.MAX), tier -> Component.literal(StringUtils.white_blue(I18n.get("gtocore.tooltip.item.tier_circuit", GTValues.VN[tier]))));

    public static final ItemEntry<Item>[] MAGNETO_RESONATIC_CIRCUIT = registerCircuits("magneto_resonatic_circuit", "磁共振电路", GTValues.tiersBetween(GTValues.ULV, GTValues.UIV), tier -> Component.translatable("gtocore.tooltip.item.tier_circuit", GTValues.VN[tier]).withStyle(ChatFormatting.LIGHT_PURPLE));

    public static final ItemEntry<Item>[] UNIVERSAL_CIRCUIT = registerCircuits("universal_circuit", "通用电路", GTValues.tiersBetween(GTValues.ULV, GTValues.MAX), tier -> Component.translatable("gtocore.tooltip.item.tier_circuit", GTValues.VN[tier]).withStyle(ChatFormatting.AQUA));

    public static final ItemEntry<ComponentItem>[] MYSTERIOUS_BOOST_DRINK = registerMysteriousBoostDrink();

    public static final ItemEntry<Item> WETWARE_SOC = registerLang("wetware_soc", "Wetware SoC", "湿件SoC");

    public static final ItemEntry<Item> BIOWARE_CIRCUIT_BOARD = register("bioware_circuit_board", "生物电路基板");
    public static final ItemEntry<Item> BIOWARE_PRINTED_CIRCUIT_BOARD = register("bioware_printed_circuit_board", "生物印刷电路基板");
    public static final ItemEntry<Item> SMD_CAPACITOR_BIOWARE = registerLang("smd_capacitor_bioware", "Bioware Capacitor", "生物活性贴片电容");
    public static final ItemEntry<Item> SMD_DIODE_BIOWARE = registerLang("smd_diode_bioware", "Bioware Diode", "生物活性贴片二极管");
    public static final ItemEntry<Item> SMD_RESISTOR_BIOWARE = registerLang("smd_resistor_bioware", "Bioware Resistor", "生物活性贴片电阻");
    public static final ItemEntry<Item> SMD_TRANSISTOR_BIOWARE = registerLang("smd_transistor_bioware", "Bioware Transistor", "生物活性贴片晶体管");
    public static final ItemEntry<Item> SMD_INDUCTOR_BIOWARE = registerLang("smd_inductor_bioware", "Bioware Inductor", "生物活性贴片电感");

    public static final ItemEntry<Item> OPTICAL_CIRCUIT_BOARD = register("optical_circuit_board", "光学电路基板");
    public static final ItemEntry<Item> OPTICAL_PRINTED_CIRCUIT_BOARD = register("optical_printed_circuit_board", "光学印刷电路基板");
    public static final ItemEntry<Item> OPTICAL_RAM_WAFER = register("optical_ram_wafer", "光学RAM晶圆");
    public static final ItemEntry<Item> OPTICAL_RAM_CHIP = register("optical_ram_chip", "光学RAM晶片");
    public static final ItemEntry<Item> SMD_CAPACITOR_OPTICAL = registerLang("smd_capacitor_optical", "Optical Capacitor", "光学贴片电容");
    public static final ItemEntry<Item> SMD_DIODE_OPTICAL = registerLang("smd_diode_optical", "Optical Diode", "光学贴片二极管");
    public static final ItemEntry<Item> SMD_RESISTOR_OPTICAL = registerLang("smd_resistor_optical", "Optical Resistor", "光学贴片电阻");
    public static final ItemEntry<Item> SMD_TRANSISTOR_OPTICAL = registerLang("smd_transistor_optical", "Optical Transistor", "光学贴片晶体管");
    public static final ItemEntry<Item> SMD_INDUCTOR_OPTICAL = registerLang("smd_inductor_optical", "Optical Inductor", "光学贴片电感");

    public static final ItemEntry<Item> EXOTIC_CIRCUIT_BOARD = register("exotic_circuit_board", "奇异电路基板");
    public static final ItemEntry<Item> EXOTIC_PRINTED_CIRCUIT_BOARD = register("exotic_printed_circuit_board", "奇异印刷电路基板");
    public static final ItemEntry<Item> EXOTIC_RAM_WAFER = register("exotic_ram_wafer", "奇异RAM晶圆");
    public static final ItemEntry<Item> EXOTIC_RAM_CHIP = register("exotic_ram_chip", "奇异RAM晶片");
    public static final ItemEntry<Item> SMD_CAPACITOR_EXOTIC = registerLang("smd_capacitor_exotic", "Exotic Capacitor", "奇异贴片电容");
    public static final ItemEntry<Item> SMD_DIODE_EXOTIC = registerLang("smd_diode_exotic", "Exotic Diode", "奇异贴片二极管");
    public static final ItemEntry<Item> SMD_RESISTOR_EXOTIC = registerLang("smd_resistor_exotic", "Exotic Resistor", "奇异贴片电阻");
    public static final ItemEntry<Item> SMD_TRANSISTOR_EXOTIC = registerLang("smd_transistor_exotic", "Exotic Transistor", "奇异贴片晶体管");
    public static final ItemEntry<Item> SMD_INDUCTOR_EXOTIC = registerLang("smd_inductor_exotic", "Exotic Inductor", "奇异贴片电感");

    public static final ItemEntry<Item> COSMIC_CIRCUIT_BOARD = register("cosmic_circuit_board", "寰宇电路基板");
    public static final ItemEntry<Item> COSMIC_PRINTED_CIRCUIT_BOARD = register("cosmic_printed_circuit_board", "寰宇印刷电路基板");
    public static final ItemEntry<Item> COSMIC_RAM_WAFER = register("cosmic_ram_wafer", "寰宇RAM晶圆");
    public static final ItemEntry<Item> COSMIC_RAM_CHIP = register("cosmic_ram_chip", "寰宇RAM晶片");
    public static final ItemEntry<Item> SMD_CAPACITOR_COSMIC = registerLang("smd_capacitor_cosmic", "Cosmic Capacitor", "寰宇贴片电容");
    public static final ItemEntry<Item> SMD_DIODE_COSMIC = registerLang("smd_diode_cosmic", "Cosmic Diode", "寰宇贴片二极管");
    public static final ItemEntry<Item> SMD_RESISTOR_COSMIC = registerLang("smd_resistor_cosmic", "Cosmic Resistor", "寰宇贴片电阻");
    public static final ItemEntry<Item> SMD_TRANSISTOR_COSMIC = registerLang("smd_transistor_cosmic", "Cosmic Transistor", "寰宇贴片晶体管");
    public static final ItemEntry<Item> SMD_INDUCTOR_COSMIC = registerLang("smd_inductor_cosmic", "Cosmic Inductor", "寰宇贴片电感");

    public static final ItemEntry<Item> SUPRACAUSAL_CIRCUIT_BOARD = register("supracausal_circuit_board", "超因果电路基板");
    public static final ItemEntry<Item> SUPRACAUSAL_PRINTED_CIRCUIT_BOARD = register("supracausal_printed_circuit_board", "超因果印刷电路基板");
    public static final ItemEntry<Item> SUPRACAUSAL_RAM_WAFER = register("supracausal_ram_wafer", "超因果RAM晶圆");
    public static final ItemEntry<Item> SUPRACAUSAL_RAM_CHIP = register("supracausal_ram_chip", "超因果RAM晶片");
    public static final ItemEntry<Item> SMD_CAPACITOR_SUPRACAUSAL = registerLang("smd_capacitor_supracausal", "Supracausal Capacitor", "超因果贴片电容");
    public static final ItemEntry<Item> SMD_DIODE_SUPRACAUSAL = registerLang("smd_diode_supracausal", "Supracausal Diode", "超因果贴片二极管");
    public static final ItemEntry<Item> SMD_RESISTOR_SUPRACAUSAL = registerLang("smd_resistor_supracausal", "Supracausal Resistor", "超因果贴片电阻");
    public static final ItemEntry<Item> SMD_TRANSISTOR_SUPRACAUSAL = registerLang("smd_transistor_supracausal", "Supracausal Transistor", "超因果贴片晶体管");
    public static final ItemEntry<Item> SMD_INDUCTOR_SUPRACAUSAL = registerLang("smd_inductor_supracausal", "Supracausal Inductor", "超因果贴片电感");

    public static final ItemEntry<Item> UHV_VOLTAGE_COIL = registerLang("uhv_voltage_coil", "UHV Voltage Coil", "极高压线圈");
    public static final ItemEntry<Item> UEV_VOLTAGE_COIL = registerLang("uev_voltage_coil", "UEV Voltage Coil", "极超压线圈");
    public static final ItemEntry<Item> UIV_VOLTAGE_COIL = registerLang("uiv_voltage_coil", "UIV Voltage Coil", "极巨压线圈");
    public static final ItemEntry<Item> UXV_VOLTAGE_COIL = registerLang("uxv_voltage_coil", "UXV Voltage Coil", "极顶压线圈");
    public static final ItemEntry<Item> OPV_VOLTAGE_COIL = registerLang("opv_voltage_coil", "OpV Voltage Coil", "过载压线圈");
    public static final ItemEntry<Item> MAX_VOLTAGE_COIL = registerLang("max_voltage_coil", "MAX Voltage Coil", "上限压线圈");

    public static final ItemEntry<Item> SPACE_DRONE_MK1 = registerLang("space_drone_mk1", "Space Drone MKⅠ", "太空无人机MKⅠ");
    public static final ItemEntry<Item> SPACE_DRONE_MK2 = registerLang("space_drone_mk2", "Space Drone MKⅡ", "太空无人机MKⅡ");
    public static final ItemEntry<Item> SPACE_DRONE_MK3 = registerLang("space_drone_mk3", "Space Drone MKⅢ", "太空无人机MKⅢ");
    public static final ItemEntry<Item> SPACE_DRONE_MK4 = registerLang("space_drone_mk4", "Space Drone MKⅣ", "太空无人机MKⅣ");
    public static final ItemEntry<Item> SPACE_DRONE_MK5 = registerLang("space_drone_mk5", "Space Drone MKⅤ", "太空无人机MKⅤ");
    public static final ItemEntry<Item> SPACE_DRONE_MK6 = registerLang("space_drone_mk6", "Space Drone MKⅥ", "太空无人机MKⅥ");

    public static final ItemEntry<ComponentItem> COSMIC_SINGULARITY = item("cosmic_singularity", "宇宙奇点", ComponentItem::create)
            .onRegister(attachRenderer(() -> HaloItemRenderer.COSMIC_HALO))
            .model(NonNullBiConsumer.noop())
            .register();

    public static final ItemEntry<Item> ENTANGLED_SINGULARITY = registerCustomModel("entangled_singularity", "纠缠奇点");
    public static final ItemEntry<Item> SPACETIME_CATALYST = registerCustomModel("spacetime_catalyst", "时空催化剂");
    public static final ItemEntry<Item> ETERNITY_CATALYST = registerCustomModel("eternity_catalyst", "永恒催化剂");

    public static final ItemEntry<Item> COMBINED_SINGULARITY_0 = registerCustomModel("combined_singularity_0", "特化奇点");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_1 = registerCustomModel("combined_singularity_1", "超凡奇点");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_2 = registerCustomModel("combined_singularity_2", "度量奇点");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_3 = registerCustomModel("combined_singularity_3", "古远奇点");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_4 = registerCustomModel("combined_singularity_4", "电气奇点");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_5 = registerCustomModel("combined_singularity_5", "质子奇点");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_6 = registerCustomModel("combined_singularity_6", "奇异奇点");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_7 = registerCustomModel("combined_singularity_7", "本征奇点");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_8 = registerCustomModel("combined_singularity_8", "量子奇点");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_9 = registerCustomModel("combined_singularity_9", "炫光奇点");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_10 = registerCustomModel("combined_singularity_10", "磁力奇点");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_11 = registerCustomModel("combined_singularity_11", "银河奇点");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_12 = registerCustomModel("combined_singularity_12", "八角奇点");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_13 = registerCustomModel("combined_singularity_13", "密文奇点");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_14 = registerCustomModel("combined_singularity_14", "天使奇点");
    public static final ItemEntry<Item> COMBINED_SINGULARITY_15 = registerCustomModel("combined_singularity_15", "立方奇点");

    public static final ItemEntry<Item> APATITE_VEIN_ESSENCE = registerEssence("apatite_vein", "磷灰石");
    public static final ItemEntry<Item> BANDED_IRON_VEIN_ESSENCE = registerEssence("banded_iron_vein", "带状铁");
    public static final ItemEntry<Item> BAUXITE_VEIN_ESSENCE = registerEssence("bauxite_vein", "铝土");
    public static final ItemEntry<Item> BERYLLIUM_VEIN_ESSENCE = registerEssence("beryllium_vein", "铍");
    public static final ItemEntry<Item> CASSITERITE_VEIN_ESSENCE = registerEssence("cassiterite_vein", "锡石");
    public static final ItemEntry<Item> CERTUS_QUARTZ_ESSENCE = registerEssence("certus_quartz", "赛特斯石英");
    public static final ItemEntry<Item> COAL_VEIN_ESSENCE = registerEssence("coal_vein", "煤炭");
    public static final ItemEntry<Item> COPPER_TIN_VEIN_ESSENCE = registerEssence("copper_tin_vein", "铜锡");
    public static final ItemEntry<Item> COPPER_VEIN_ESSENCE = registerEssence("copper_vein", "铜");
    public static final ItemEntry<Item> DIAMOND_VEIN_ESSENCE = registerEssence("diamond_vein", "钻石");
    public static final ItemEntry<Item> GALENA_VEIN_ESSENCE = registerEssence("galena_vein", "方铅");
    public static final ItemEntry<Item> GARNET_TIN_VEIN_ESSENCE = registerEssence("garnet_tin_vein", "锡石榴石");
    public static final ItemEntry<Item> GARNET_VEIN_ESSENCE = registerEssence("garnet_vein", "石榴石");
    public static final ItemEntry<Item> IRON_VEIN_ESSENCE = registerEssence("iron_vein", "铁");
    public static final ItemEntry<Item> LAPIS_VEIN_ESSENCE = registerEssence("lapis_vein", "青金石");
    public static final ItemEntry<Item> LUBRICANT_VEIN_ESSENCE = registerEssence("lubricant_vein", "皂滑");
    public static final ItemEntry<Item> MAGNETITE_VEIN_END_ESSENCE = registerEssence("magnetite_vein", "磁铁");
    public static final ItemEntry<Item> MANGANESE_VEIN_ESSENCE = registerEssence("manganese_vein", "锰");
    public static final ItemEntry<Item> MICA_VEIN_ESSENCE = registerEssence("mica_vein", "云母");
    public static final ItemEntry<Item> MINERAL_SAND_VEIN_ESSENCE = registerEssence("mineral_sand_vein", "矿砂");
    public static final ItemEntry<Item> MOLYBDENUM_VEIN_ESSENCE = registerEssence("molybdenum_vein", "钼");
    public static final ItemEntry<Item> MONAZITE_VEIN_ESSENCE = registerEssence("monazite_vein", "独居石");
    public static final ItemEntry<Item> NAQUADAH_VEIN_ESSENCE = registerEssence("naquadah_vein", "硅岩");
    public static final ItemEntry<Item> NETHER_QUARTZ_VEIN_ESSENCE = registerEssence("nether_quartz_vein", "下界石英");
    public static final ItemEntry<Item> NICKEL_VEIN_ESSENCE = registerEssence("nickel_vein", "镍");
    public static final ItemEntry<Item> OILSANDS_VEIN_ESSENCE = registerEssence("oilsands_vein", "油砂");
    public static final ItemEntry<Item> OLIVINE_VEIN_ESSENCE = registerEssence("olivine_vein", "橄榄石");
    public static final ItemEntry<Item> PITCHBLENDE_VEIN_ESSENCE = registerEssence("pitchblende_vein", "沥青铀");
    public static final ItemEntry<Item> REDSTONE_VEIN_ESSENCE = registerEssence("redstone_vein", "红石");
    public static final ItemEntry<Item> SALTPETER_VEIN_ESSENCE = registerEssence("saltpeter_vein", "硝石");
    public static final ItemEntry<Item> SALTS_VEIN_ESSENCE = registerEssence("salts_vein", "盐");
    public static final ItemEntry<Item> SAPPHIRE_VEIN_ESSENCE = registerEssence("sapphire_vein", "蓝宝石");
    public static final ItemEntry<Item> SCHEELITE_VEIN_ESSENCE = registerEssence("scheelite_vein", "白钨");
    public static final ItemEntry<Item> SHELDONITE_VEIN_ESSENCE = registerEssence("sheldonite_vein", "谢尔顿");
    public static final ItemEntry<Item> SULFUR_VEIN_ESSENCE = registerEssence("sulfur_vein", "硫");
    public static final ItemEntry<Item> TETRAHEDRITE_VEIN_ESSENCE = registerEssence("tetrahedrite_vein", "黝铜");
    public static final ItemEntry<Item> TOPAZ_VEIN_ESSENCE = registerEssence("topaz_vein", "黄玉");

    public static final ItemEntry<Item> REACTOR_URANIUM_SIMPLE = register("reactor_uranium_simple", "铀燃料棒");
    public static final ItemEntry<Item> REACTOR_URANIUM_DUAL = register("reactor_uranium_dual", "二联铀燃料棒");
    public static final ItemEntry<Item> REACTOR_URANIUM_QUAD = register("reactor_uranium_quad", "四联铀燃料棒");
    public static final ItemEntry<Item> DEPLETED_REACTOR_URANIUM_SIMPLE = register("depleted_reactor_uranium_simple", "枯竭铀燃料棒");
    public static final ItemEntry<Item> DEPLETED_REACTOR_URANIUM_DUAL = register("depleted_reactor_uranium_dual", "枯竭二联铀燃料棒");
    public static final ItemEntry<Item> DEPLETED_REACTOR_URANIUM_QUAD = register("depleted_reactor_uranium_quad", "枯竭四联铀燃料棒");

    public static final ItemEntry<Item> REACTOR_THORIUM_SIMPLE = register("reactor_thorium_simple", "钍燃料棒");
    public static final ItemEntry<Item> REACTOR_THORIUM_DUAL = register("reactor_thorium_dual", "二联钍燃料棒");
    public static final ItemEntry<Item> REACTOR_THORIUM_QUAD = register("reactor_thorium_quad", "四联钍燃料棒");
    public static final ItemEntry<Item> DEPLETED_REACTOR_THORIUM_SIMPLE = register("depleted_reactor_thorium_simple", "枯竭钍燃料棒");
    public static final ItemEntry<Item> DEPLETED_REACTOR_THORIUM_DUAL = register("depleted_reactor_thorium_dual", "枯竭二联钍燃料棒");
    public static final ItemEntry<Item> DEPLETED_REACTOR_THORIUM_QUAD = register("depleted_reactor_thorium_quad", "枯竭四联钍燃料棒");

    public static final ItemEntry<Item> REACTOR_MOX_SIMPLE = register("reactor_mox_simple", "MOX燃料棒");
    public static final ItemEntry<Item> REACTOR_MOX_DUAL = register("reactor_mox_dual", "二联MOX燃料棒");
    public static final ItemEntry<Item> REACTOR_MOX_QUAD = register("reactor_mox_quad", "四联MOX燃料棒");
    public static final ItemEntry<Item> DEPLETED_REACTOR_MOX_SIMPLE = register("depleted_reactor_mox_simple", "枯竭MOX燃料棒");
    public static final ItemEntry<Item> DEPLETED_REACTOR_MOX_DUAL = register("depleted_reactor_mox_dual", "枯竭二联MOX燃料棒");
    public static final ItemEntry<Item> DEPLETED_REACTOR_MOX_QUAD = register("depleted_reactor_mox_quad", "枯竭四联MOX燃料棒");

    public static final ItemEntry<Item> REACTOR_NAQUADAH_SIMPLE = register("reactor_naquadah_simple", "硅岩燃料棒");
    public static final ItemEntry<Item> REACTOR_NAQUADAH_DUAL = register("reactor_naquadah_dual", "二联硅岩燃料棒");
    public static final ItemEntry<Item> REACTOR_NAQUADAH_QUAD = register("reactor_naquadah_quad", "四联硅岩燃料棒");
    public static final ItemEntry<Item> DEPLETED_REACTOR_NAQUADAH_SIMPLE = register("depleted_reactor_naquadah_simple", "枯竭硅岩燃料棒");
    public static final ItemEntry<Item> DEPLETED_REACTOR_NAQUADAH_DUAL = register("depleted_reactor_naquadah_dual", "枯竭二联硅岩燃料棒");
    public static final ItemEntry<Item> DEPLETED_REACTOR_NAQUADAH_QUAD = register("depleted_reactor_naquadah_quad", "枯竭四联硅岩燃料棒");

    public static final ItemEntry<Item> NEUTRONIUM_ANTIMATTER_FUEL_ROD = registerTexture("neutronium_antimatter_fuel_rod", "中子反物质燃料棒", "antimatter_fuel_rod");
    public static final ItemEntry<Item> DRACONIUM_ANTIMATTER_FUEL_ROD = registerTexture("draconium_antimatter_fuel_rod", "龙反物质燃料棒", "antimatter_fuel_rod");
    public static final ItemEntry<Item> COSMIC_NEUTRONIUM_ANTIMATTER_FUEL_ROD = registerTexture("cosmic_neutronium_antimatter_fuel_rod", "宇宙中子反物质燃料棒", "antimatter_fuel_rod");
    public static final ItemEntry<Item> INFINITY_ANTIMATTER_FUEL_ROD = registerTexture("infinity_antimatter_fuel_rod", "无尽反物质燃料棒", "antimatter_fuel_rod");

    public static final ItemEntry<Item> SPACE_ESSENCE = register("space_essence", "宇宙精华");
    public static final ItemEntry<Item> BEDROCK_DESTROYER = register("bedrock_destroyer", "基岩破坏者");
    public static final ItemEntry<Item> LEPTON_TRAP_CRYSTAL = register("lepton_trap_crystal", "轻子阱晶体");
    public static final ItemEntry<Item> CHARGED_LEPTON_TRAP_CRYSTAL = register("charged_lepton_trap_crystal", "带电轻子阱晶体");
    public static final ItemEntry<Item> QUANTUM_ANOMALY = register("quantum_anomaly", "量子反常");
    public static final ItemEntry<Item> HASSIUM_SEED_CRYSTAL = register("hassium_seed_crystal", "\ud872\udf76晶种");
    public static final ItemEntry<Item> RAW_IMPRINTED_RESONATIC_CIRCUIT_BOARD = register("raw_imprinted_resonatic_circuit_board", "压印磁共振电路板原料");
    public static final ItemEntry<Item> IMPRINTED_RESONATIC_CIRCUIT_BOARD = register("imprinted_resonatic_circuit_board", "压印磁共振电路板");
    public static final ItemEntry<Item> ROTATING_TRANSPARENT_SURFACE = register("rotating_transparent_surface", "旋转透明层");
    public static final ItemEntry<Item> ELECTRON_SOURCE = register("electron_source", "电子源");
    public static final ItemEntry<Item> ESSENCE = register("essence", "精华");
    public static final ItemEntry<Item> ESSENCE_SEED = register("essence_seed", "精华种子");
    public static final ItemEntry<Item> NUCLEAR_STAR = register("nuclear_star", "核能之星");
    public static final ItemEntry<Item> UNSTABLE_STAR = register("unstable_star", "易变之星");
    public static final ItemEntry<Item> PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK1 = register("precision_circuit_assembly_robot_mk1", "精密电路装配机器人MKⅠ");
    public static final ItemEntry<Item> PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK2 = register("precision_circuit_assembly_robot_mk2", "精密电路装配机器人MKⅡ");
    public static final ItemEntry<Item> PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK3 = register("precision_circuit_assembly_robot_mk3", "精密电路装配机器人MKⅢ");
    public static final ItemEntry<Item> PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK4 = register("precision_circuit_assembly_robot_mk4", "精密电路装配机器人MKⅣ");
    public static final ItemEntry<Item> PRECISION_CIRCUIT_ASSEMBLY_ROBOT_MK5 = register("precision_circuit_assembly_robot_mk5", "精密电路装配机器人MKⅤ");
    public static final ItemEntry<Item> SCRAP = register("scrap", "废料");
    public static final ItemEntry<Item> SCRAP_BOX = register("scrap_box", "废料盒");
    public static final ItemEntry<Item> NUCLEAR_WASTE = register("nuclear_waste", "核废料");
    public static final ItemEntry<Item> RESONATING_GEM = register("resonating_gem", "共振宝石");
    public static final ItemEntry<Item> PLASMA_CONTAINMENT_CELL = register("plasma_containment_cell", "等离子体密闭容器");
    public static final ItemEntry<Item> RHENIUM_PLASMA_CONTAINMENT_CELL = register("rhenium_plasma_containment_cell", "铼等离子体密闭容器");
    public static final ItemEntry<Item> ACTINIUM_SUPERHYDRIDE_PLASMA_CONTAINMENT_CELL = register("actinium_superhydride_plasma_containment_cell", "超氢化锕等离子体密闭容器");
    public static final ItemEntry<Item> DRAGON_STEM_CELLS = register("dragon_stem_cells", "龙干细胞");
    public static final ItemEntry<Item> DRAGON_CELLS = register("dragon_cells", "龙细胞");
    public static final ItemEntry<Item> BIOWARE_BOULE = register("bioware_boule", "生物活性晶圆");
    public static final ItemEntry<Item> BIOWARE_CHIP = register("bioware_chip", "生物活性芯片");
    public static final ItemEntry<Item> BIOWARE_PROCESSING_CORE = register("bioware_processing_core", "生物活性处理器核心");
    public static final ItemEntry<Item> BIOLOGICAL_CELLS = register("biological_cells", "生物细胞");
    public static final ItemEntry<Item> SIMPLE_OPTICAL_SOC = registerLang("simple_optical_soc", "Simple Optical SoC", "简易光学SoC");
    public static final ItemEntry<Item> OPTICAL_SOC_CONTAINMENT_HOUSING = registerLang("optical_soc_containment_housing", "Optical SoC Containment Housing", "光学SoC密封外壳");
    public static final ItemEntry<Item> OPTICAL_SLICE = register("optical_slice", "光学裸片");
    public static final ItemEntry<Item> OPTICAL_PROCESSING_CORE = register("optical_processing_core", "光学处理器核心");
    public static final ItemEntry<Item> OPTICAL_WAFER = register("optical_wafer", "光学晶圆");
    public static final ItemEntry<Item> PHOTON_CARRYING_WAFER = register("photon_carrying_wafer", "光电子承载晶圆");
    public static final ItemEntry<Item> RAW_PHOTON_CARRYING_WAFER = register("raw_photon_carrying_wafer", "未成型的光电子承载晶圆");
    public static final ItemEntry<Item> EXOTIC_PROCESSING_CORE = register("exotic_processing_core", "奇异处理器核心");
    public static final ItemEntry<Item> COSMIC_PROCESSING_CORE = register("cosmic_processing_core", "寰宇处理器核心");
    public static final ItemEntry<Item> SUPRACAUSAL_PROCESSING_CORE = register("supracausal_processing_core", "超因果处理器核心");
    public static final ItemEntry<Item> PERIODICALLY_POLED_LITHIUM_NIOBATE_BOULE = register("periodically_poled_lithium_niobate_boule", "周期性极化铌酸锂晶体");
    public static final ItemEntry<Item> NEUTRON_PLASMA_CONTAINMENT_CELL = register("neutron_plasma_containment_cell", "中子等离子体密闭容器");
    public static final ItemEntry<Item> CRYSTAL_MATRIX_PLASMA_CONTAINMENT_CELL = register("crystal_matrix_plasma_containment_cell", "水晶矩阵等离子体密闭容器");
    public static final ItemEntry<Item> AWAKENED_DRACONIUM_PLASMA_CONTAINMENT_CELL = register("awakened_draconium_plasma_containment_cell", "觉醒龙等离子体密闭容器");
    public static final ItemEntry<Item> HIGHLY_REFLECTIVE_MIRROR = register("highly_reflective_mirror", "高反射率镜");
    public static final ItemEntry<Item> LOW_FREQUENCY_LASER = register("low_frequency_laser", "低频激光器");
    public static final ItemEntry<Item> MEDIUM_FREQUENCY_LASER = register("medium_frequency_laser", "中频激光器");
    public static final ItemEntry<Item> HIGH_FREQUENCY_LASER = register("high_frequency_laser", "高频激光器");
    public static final ItemEntry<Item> RED_HALIDE_LAMP = register("red_halide_lamp", "红光卤素灯");
    public static final ItemEntry<Item> GREEN_HALIDE_LAMP = register("green_halide_lamp", "绿光卤素灯");
    public static final ItemEntry<Item> BLUE_HALIDE_LAMP = register("blue_halide_lamp", "蓝光卤素灯");
    public static final ItemEntry<Item> BALLAST = register("ballast", "镇流器");
    public static final ItemEntry<Item> EMPTY_LASER_COOLING_CONTAINER = register("empty_laser_cooling_container", "空的激光冷却容器");
    public static final ItemEntry<Item> HIGH_PRECISION_CRYSTAL_SOC = registerLang("high_precision_crystal_soc", "High Precision Crystal SoC", "高精度晶体SoC");
    public static final ItemEntry<Item> BOSE_EINSTEIN_COOLING_CONTAINER = register("bose_einstein_cooling_container", "玻色-爱因斯坦凝聚态物质遏制装置");
    public static final ItemEntry<Item> LASER_COOLING_UNIT = register("laser_cooling_unit", "激光冷却单元");
    public static final ItemEntry<Item> LASER_DIODE = register("laser_diode", "激光二极管");
    public static final ItemEntry<Item> MAGNETIC_TRAP = register("magnetic_trap", "磁阱");
    public static final ItemEntry<Item> RYDBERG_SPINORIAL_ASSEMBLY = register("rydberg_spinorial_assembly", "里德堡旋量集群");
    public static final ItemEntry<Item> X_RAY_LASER = register("x_ray_laser", "X射线激光器");
    public static final ItemEntry<Item> CRYOGENIC_INTERFACE = register("cryogenic_interface", "低温接口");
    public static final ItemEntry<Item> EXOTIC_WAFER = register("exotic_wafer", "奇异晶圆");
    public static final ItemEntry<Item> EXOTIC_CHIP = register("exotic_chip", "奇异芯片");
    public static final ItemEntry<Item> X_RAY_WAVEGUIDE = register("x_ray_waveguide", "X射线导波管");
    public static final ItemEntry<Item> X_RAY_MIRROR_PLATE = register("x_ray_mirror_plate", "X射线镜片");
    public static final ItemEntry<Item> COSMIC_PROCESSING_UNIT_CORE = register("cosmic_processing_unit_core", "寰宇处理器单元");
    public static final ItemEntry<Item> ULTRASHORT_PULSE_LASER = register("ultrashort_pulse_laser", "超短脉冲激光器");
    public static final ItemEntry<Item> DIFFRACTOR_GRATING_MIRROR = register("diffractor_grating_mirror", "衍射光栅镜");
    public static final ItemEntry<Item> GRATING_LITHOGRAPHY_MASK = register("grating_lithography_mask", "光栅光刻掩膜");
    public static final ItemEntry<Item> LITHOGRAPHY_MASK = register("lithography_mask", "光刻掩膜");
    public static final ItemEntry<Item> PHOTOCOATED_HASSIUM_BOULE = register("photocoated_hassium_boule", "光聚合物涂覆的掺\ud872\udf76单晶硅");
    public static final ItemEntry<Item> PHOTOCOATED_HASSIUM_WAFER = register("photocoated_hassium_wafer", "光聚合物涂覆的掺\ud872\udf76晶圆");
    public static final ItemEntry<Item> TIME_DILATION_CONTAINMENT_UNIT = register("time_dilation_containment_unit", "时间膨胀密闭单元");
    public static final ItemEntry<Item> NEUTRONIUM_SPHERE = register("neutronium_sphere", "中子素球体");
    public static final ItemEntry<Item> CHARGED_TRIPLET_NEUTRONIUM_SPHERE = registerCustomModel("charged_triplet_neutronium_sphere", "带电三连中子素球体");
    public static final ItemEntry<Item> TRIPLET_NEUTRONIUM_SPHERE = register("triplet_neutronium_sphere", "三连中子素球体");
    public static final ItemEntry<Item> CONTAINED_HIGH_DENSITY_PROTONIC_MATTER = register("contained_high_density_protonic_matter", "遏制高密度质子物质");
    public static final ItemEntry<Item> EXTREMELY_DURABLE_PLASMA_CELL = register("extremely_durable_plasma_cell", "高耐久等离子体容器");
    public static final ItemEntry<Item> DENSE_NEUTRON_PLASMA_CELL = register("dense_neutron_plasma_cell", "致密中子等离子体单元");
    public static final ItemEntry<Item> COSMIC_NEUTRON_PLASMA_CELL = register("cosmic_neutron_plasma_cell", "宇宙中子等离子体单元");
    public static final ItemEntry<Item> QUANTUMCHROMODYNAMIC_PROTECTIVE_PLATING = register("quantumchromodynamic_protective_plating", "量子色动力学防护层");
    public static final ItemEntry<Item> CHAOS_CONTAINMENT_UNIT = register("chaos_containment_unit", "混沌物质遏制容器");
    public static final ItemEntry<Item> COSMIC_MESH_CONTAINMENT_UNIT = register("cosmic_mesh_containment_unit", "寰宇织网遏制容器");
    public static final ItemEntry<Item> MICROWORMHOLE_GENERATOR = register("microwormhole_generator", "微型虫洞发生器");
    public static final ItemEntry<Item> GRAVITON_TRANSDUCER = register("graviton_transducer", "引力子换能器");
    public static final ItemEntry<Item> CONTAINED_REISSNER_NORDSTROM_SINGULARITY = register("contained_reissner_nordstrom_singularity", "遏制莱斯纳-诺斯特朗黑洞奇点");
    public static final ItemEntry<Item> CONTAINED_KERR_NEWMANN_SINGULARITY = register("contained_kerr_newmann_singularity", "遏制克尔-纽曼黑洞奇点");
    public static final ItemEntry<Item> CONTAINED_KERR_SINGULARITY = register("contained_kerr_singularity", "遏制克尔黑洞奇点");
    public static final ItemEntry<Item> CONTAINED_EXOTIC_MATTER = register("contained_exotic_matter", "遏制奇异物质");
    public static final ItemEntry<Item> MACROWORMHOLE_GENERATOR = register("macrowormhole_generator", "巨型虫洞发生器");
    public static final ItemEntry<Item> STABILIZED_WORMHOLE_GENERATOR = register("stabilized_wormhole_generator", "稳定虫洞发生器");
    public static final ItemEntry<Item> TOPOLOGICAL_MANIPULATOR_UNIT = register("topological_manipulator_unit", "拓扑控制操纵单元");
    public static final ItemEntry<Item> RELATIVISTIC_SPINORIAL_MEMORY_SYSTEM = register("relativistic_spinorial_memory_system", "相对论旋量存储器");
    public static final ItemEntry<Item> NUCLEAR_CLOCK = register("nuclear_clock", "核时钟");
    public static final ItemEntry<Item> SCINTILLATOR = register("scintillator", "闪烁体");
    public static final ItemEntry<Item> SCINTILLATOR_CRYSTAL = register("scintillator_crystal", "闪烁晶体");
    public static final ItemEntry<Item> MANIFOLD_OSCILLATORY_POWER_CELL = register("manifold_oscillatory_power_cell", "流形振荡电池");
    public static final ItemEntry<Item> RECURSIVELY_FOLDED_NEGATIVE_SPACE = register("recursively_folded_negative_space", "递归折叠负空间");
    public static final ItemEntry<Item> EIGENFOLDED_KERR_MANIFOLD = register("eigenfolded_kerr_manifold", "本征折叠克尔流形");
    public static final ItemEntry<Item> CLOSED_TIMELIKE_CURVE_COMPUTATIONAL_UNIT_CONTAINER = register("closed_timelike_curve_computational_unit_container", "封闭类时曲线计算单元容器");
    public static final ItemEntry<Item> CLOSED_TIMELIKE_CURVE_COMPUTATIONAL_UNIT = register("closed_timelike_curve_computational_unit", "封闭类时曲线计算单元");
    public static final ItemEntry<Item> HIGHLY_DENSE_POLYMER_PLATE = register("highly_dense_polymer_plate", "高密度聚合物板");
    public static final ItemEntry<Item> SPACE_PROBE_MK1 = register("space_probe_mk1", "宇宙探测器MKⅠ");
    public static final ItemEntry<Item> SPACE_PROBE_MK2 = register("space_probe_mk2", "宇宙探测器MKⅡ");
    public static final ItemEntry<Item> SPACE_PROBE_MK3 = register("space_probe_mk3", "宇宙探测器MKⅢ");
    public static final ItemEntry<Item> HYPERCUBE = register("hypercube", "超立方体");
    public static final ItemEntry<Item> ANNIHILATION_CONSTRAINER = register("annihilation_constrainer", "湮灭约束器");
    public static final ItemEntry<Item> SOLAR_LIGHT_SPLITTER = register("solar_light_splitter", "阳光分离器");
    public static final ItemEntry<Item> CREATE_ULTIMATE_BATTERY = registerTooltip("create_ultimate_battery", "创造电池", () -> Component.literal(I18n.get("tooltip.avaritia.tier", StringUtils.white_blue(I18n.get("gtocore.tooltip.unknown")))).withStyle(ChatFormatting.GREEN));
    public static final ItemEntry<Item> SUPRACHRONAL_MAINFRAME_COMPLEX = registerTooltip("suprachronal_mainframe_complex", "创造主机", () -> Component.literal(I18n.get("tooltip.avaritia.tier", StringUtils.white_blue(I18n.get("gtocore.tooltip.unknown")))).withStyle(ChatFormatting.GREEN));
    public static final ItemEntry<Item> ZERO_POINT_MODULE_FRAGMENTS = register("zero_point_module_fragments", "零点模块碎片");
    public static final ItemEntry<Item> TCETIESEAWEEDEXTRACT = register("tcetieseaweedextract", "鲸鱼座T星E藻类提取物");
    public static final ItemEntry<Item> TCETIEDANDELIONS = register("tcetiedandelions", "鲸鱼座T星E藻类");
    public static final ItemEntry<Item> WOVEN_KEVLAR = register("woven_kevlar", "编织凯芙拉");
    public static final ItemEntry<Item> KEVLAR_FIBER = register("kevlar_fiber", "凯芙拉纤维");
    public static final ItemEntry<Item> WARPED_ENDER_PEARL = register("warped_ender_pearl", "扭曲的末影珍珠");
    public static final ItemEntry<Item> CHAOS_SHARD = register("chaos_shard", "混沌碎片");
    public static final ItemEntry<Item> COSMIC_FABRIC = register("cosmic_fabric", "寰宇织料");
    public static final ItemEntry<Item> DRACONIC_CORE = register("draconic_core", "龙芯");
    public static final ItemEntry<Item> WYVERN_CORE = register("wyvern_core", "双足飞龙核心");
    public static final ItemEntry<Item> AWAKENED_CORE = register("awakened_core", "觉醒核心");
    public static final ItemEntry<Item> CHAOTIC_CORE = register("chaotic_core", "混沌核心");
    public static final ItemEntry<Item> WYVERN_ENERGY_CORE = register("wyvern_energy_core", "双足飞龙能量核心");
    public static final ItemEntry<Item> DRACONIC_ENERGY_CORE = register("draconic_energy_core", "神龙能量核心");
    public static final ItemEntry<Item> CHAOTIC_ENERGY_CORE = register("chaotic_energy_core", "混沌能量核心");
    public static final ItemEntry<Item> DRACONIUM_DIRT = register("draconium_dirt", "龙尘");
    public static final ItemEntry<Item> DRAGON_HEART = register("dragon_heart", "龙之心");
    public static final ItemEntry<Item> STABILIZER_CORE = register("stabilizer_core", "稳定核心");
    public static final ItemEntry<Item> DRAGON_STABILIZER_CORE = register("dragon_stabilizer_core", "龙之稳定核心");
    public static final ItemEntry<Item> RUTHERFORDIUM_AMPROSIUM_BOULE = register("rutherfordium_amprosium_boule", "\ud872\udf3b强化的安普洛掺杂的单晶硅");
    public static final ItemEntry<Item> RUTHERFORDIUM_AMPROSIUM_WAFER = register("rutherfordium_amprosium_wafer", "\ud872\udf3b强化的安普洛掺杂的晶圆");
    public static final ItemEntry<Item> TARANIUM_BOULE = register("taranium_boule", "塔兰掺杂的单晶硅");
    public static final ItemEntry<Item> TARANIUM_WAFER = register("taranium_wafer", "塔兰掺杂的晶圆");
    public static final ItemEntry<Item> PREPARED_COSMIC_SOC_WAFER = registerLang("prepared_cosmic_soc_wafer", "Prepared Cosmic SoC Wafer", "预备寰宇SoC晶圆");
    public static final ItemEntry<Item> SIMPLE_COSMIC_SOC_WAFER = registerLang("simple_cosmic_soc_wafer", "Simple Cosmic SoC Wafer", "简易寰宇SoC晶圆");
    public static final ItemEntry<Item> SIMPLE_COSMIC_SOC = registerLang("simple_cosmic_soc", "Simple Cosmic SoC", "简易寰宇SoC");
    public static final ItemEntry<Item> NM_WAFER = register("nm_wafer", "纳米功率集成电路晶圆");
    public static final ItemEntry<Item> NM_CHIP = register("nm_chip", "纳米功率集成电路");
    public static final ItemEntry<Item> PM_WAFER = register("pm_wafer", "皮米功率集成电路晶圆");
    public static final ItemEntry<Item> PM_CHIP = register("pm_chip", "皮米功率集成电路");
    public static final ItemEntry<Item> FM_WAFER = register("fm_wafer", "飞米功率集成电路晶圆");
    public static final ItemEntry<Item> FM_CHIP = register("fm_chip", "飞米功率集成电路");
    public static final ItemEntry<Item> FULLERENE_POLYMER_MATRIX_SOFT_TUBING = register("fullerene_polymer_matrix_soft_tubing", "富勒烯聚合物基体软管");
    public static final ItemEntry<Item> FULLERENE_POLYMER_MATRIX_FINE_TUBING = register("fullerene_polymer_matrix_fine_tubing", "富勒烯聚合物基质细管");
    public static final ItemEntry<Item> DUST_BLIZZ = register("dust_blizz", "暴雪粉");
    public static final ItemEntry<Item> DUST_CRYOTHEUM = register("dust_cryotheum", "凛冰粉");
    public static final ItemEntry<Item> BEDROCK_DRILL = register("bedrock_drill", "基岩钻头");
    public static final ItemEntry<Item> MEMORY_FOAM_BLOCK = register("memory_foam_block", "记忆海绵");
    public static final ItemEntry<Item> GRAPHENE_IRON_PLATE = register("graphene_iron_plate", "石墨烯铁板");
    public static final ItemEntry<Item> INSULATION_WIRE_ASSEMBLY = register("insulation_wire_assembly", "绝缘线团");
    public static final ItemEntry<Item> AEROGRAPHENE = register("aerographene", "石墨烯气凝胶");
    public static final ItemEntry<Item> COMMAND_BLOCK_CORE = register("command_block_core", "脉冲命令方块核心");
    public static final ItemEntry<Item> CHAIN_COMMAND_BLOCK_CORE = register("chain_command_block_core", "连锁命令方块核心");
    public static final ItemEntry<Item> REPEATING_COMMAND_BLOCK_CORE = register("repeating_command_block_core", "循环命令方块核心");
    public static final ItemEntry<Item> TWO_WAY_FOIL = register("two_way_foil", "二向箔");
    public static final ItemEntry<Item> HYPER_STABLE_SELF_HEALING_ADHESIVE = register("hyper_stable_self_healing_adhesive", "超稳态自修复粘合剂");
    public static final ItemEntry<Item> BLACK_BODY_NAQUADRIA_SUPERSOLID = register("black_body_naquadria_supersolid", "超固态超能硅岩黑体");
    public static final ItemEntry<Item> HEARTOFTHESMOGUS = register("heartofthesmogus", "内鬼饼干夹烤棉花糖之心");
    public static final ItemEntry<Item> VOID_MATTER = register("void_matter", "虚空物质");
    public static final ItemEntry<Item> TEMPORAL_MATTER = register("temporal_matter", "时间物质");
    public static final ItemEntry<Item> PROTO_MATTER = register("proto_matter", "元始物质");
    public static final ItemEntry<Item> OMNI_MATTER = register("omni_matter", "全物质");
    public static final ItemEntry<Item> KINETIC_MATTER = register("kinetic_matter", "富动力物质");
    public static final ItemEntry<Item> ESSENTIA_MATTER = register("essentia_matter", "根源物质");
    public static final ItemEntry<Item> DARK_MATTER = register("dark_matter", "暗物质");
    public static final ItemEntry<Item> CORPOREAL_MATTER = register("corporeal_matter", "凡尘物质");
    public static final ItemEntry<Item> AMORPHOUS_MATTER = register("amorphous_matter", "非晶态物质");
    public static final ItemEntry<Item> PELLET_ANTIMATTER = register("pellet_antimatter", "反物质");
    public static final ItemEntry<Item> DYSON_SWARM_MODULE = register("dyson_swarm_module", "戴森球模块");
    public static final ItemEntry<Item> GLACIO_SPIRIT = register("glacio_spirit", "霜原碎片");
    public static final ItemEntry<Item> TIMEPIECE = register("timepiece", "时间碎片");
    public static final ItemEntry<Item> PRECISION_STEAM_MECHANISM = register("precision_steam_mechanism", "精密蒸汽构件");
    public static final ItemEntry<Item> INVERTER = register("inverter", "逆变器");
    public static final ItemEntry<Item> GIGA_CHAD = register("giga_chad", "Giga Chad代币");
    public static final ItemEntry<Item> REACTOR_FUEL_ROD = register("reactor_fuel_rod", "空燃料棒");
    public static final ItemEntry<Item> TUNGSTEN_CARBIDE_REACTOR_FUEL_ROD = register("tungsten_carbide_reactor_fuel_rod", "空碳化钨燃料棒");
    public static final ItemEntry<Item> HUI_CIRCUIT_1 = registerLang("hui_circuit_1", "High Calculation Workstation MK-I", "高算力工作站 MK-I");
    public static final ItemEntry<Item> HUI_CIRCUIT_2 = registerLang("hui_circuit_2", "High Calculation Workstation MK-II", "高算力工作站 MK-II");
    public static final ItemEntry<Item> HUI_CIRCUIT_3 = registerLang("hui_circuit_3", "High Calculation Workstation MK-III", "高算力工作站 MK-III");
    public static final ItemEntry<Item> HUI_CIRCUIT_4 = registerLang("hui_circuit_4", "High Calculation Workstation MK-IV", "高算力工作站 MK-IV");
    public static final ItemEntry<Item> HUI_CIRCUIT_5 = registerLang("hui_circuit_5", "High Calculation Workstation MK-V", "高算力工作站 MK-V");
    public static final ItemEntry<Item> SPECIAL_CERAMICS = register("special_ceramics", "特种陶瓷");
    public static final ItemEntry<Item> PLANET_SCAN_SATELLITE = register("planet_scan_satellite", "行星扫描卫星");
    public static final ItemEntry<Item> HYPERDIMENSIONAL_DRONE = register("hyperdimensional_drone", "超维度无人机");

    public static final ItemEntry<Item> HOT_IRON_INGOT = registerCustomModel("hot_iron_ingot", "热铁锭");
    public static final ItemEntry<Item> RAW_VACUUM_TUBE = registerCustomModel("raw_vacuum_tube", "粗真空管");
    public static final ItemEntry<Item> INFINITE_CELL_COMPONENT = register("infinite_cell_component", "无限存储组件");
    public static final ItemEntry<Item> PROTONATED_FULLERENE_SIEVING_MATRIX = register("protonated_fullerene_sieving_matrix", "质子化富勒烯筛分基质");
    public static final ItemEntry<Item> SATURATED_FULLERENE_SIEVING_MATRIX = register("saturated_fullerene_sieving_matrix", "饱和富勒烯筛分基质");
    public static final ItemEntry<Item> MICROFOCUS_X_RAY_TUBE = register("microfocus_x_ray_tube", "微焦点X射线管");
    public static final ItemEntry<Item> SEPARATION_ELECTROMAGNET = register("separation_electromagnet", "分离用电磁铁");
    public static final ItemEntry<Item> HIGHLY_INSULATING_FOIL = registerCustomModel("highly_insulating_foil", "高绝缘性箔");

    public static final ItemEntry<Item> BLUE_ALGAE = registerAlgae("blue", "蓝");
    public static final ItemEntry<Item> BROWN_ALGAE = registerAlgae("brown", "褐");
    public static final ItemEntry<Item> GOLD_ALGAE = registerAlgae("gold", "金");
    public static final ItemEntry<Item> GREEN_ALGAE = registerAlgae("green", "绿");
    public static final ItemEntry<Item> RED_ALGAE = registerAlgae("red", "红");
    public static final ItemEntry<Item> GOLD_ALGAE_FIBER = registerAlgaeFiber("gold", "金");
    public static final ItemEntry<Item> GREEN_ALGAE_FIBER = registerAlgaeFiber("green", "绿");
    public static final ItemEntry<Item> RED_ALGAE_FIBER = registerAlgaeFiber("red", "红");

    public static final ItemEntry<Item> CEREBRUM = register("cerebrum", "大脑");
    public static final ItemEntry<Item> SUPER_CEREBRUM = register("super_cerebrum", "超级大脑");
    public static final ItemEntry<Item> PREPARATION_PETRI_DISH = register("preparation_petri_dish", "预备培养皿");
    public static final ItemEntry<Item> STERILIZED_PETRI_DISH = registerCustomModel("sterilized_petri_dish", "无菌培养皿");
    public static final ItemEntry<Item> ELECTRICALY_WIRED_PETRI_DISH = registerCustomModel("electricaly_wired_petri_dish", "电信号培养皿");
    public static final ItemEntry<Item> CONTAMINATED_PETRI_DISH = register("contaminated_petri_dish", "污染的培养皿");
    public static final ItemEntry<Item> BREVIBACTERIUM_PETRI_DISH = registerTexture("brevibacterium_petri_dish", "黄色短杆菌培养皿", "germ");
    public static final ItemEntry<Item> BIFIDOBACTERIUMM_PETRI_DISH = registerTexture("bifidobacteriumm_petri_dish", "短双歧杆菌培养皿", "germ");
    public static final ItemEntry<Item> ESCHERICIA_PETRI_DISH = registerTexture("eschericia_petri_dish", "大肠杆菌培养皿", "germ");
    public static final ItemEntry<Item> STREPTOCOCCUS_PETRI_DISH = registerTexture("streptococcus_petri_dish", "酿脓链球菌培养皿", "germ");
    public static final ItemEntry<Item> CUPRIAVIDUS_PETRI_DISH = registerTexture("cupriavidus_petri_dish", "贪铜钩虫菌培养皿", "germ");
    public static final ItemEntry<Item> SHEWANELLA_PETRI_DISH = registerTexture("shewanella_petri_dish", "希瓦氏菌培养皿", "germ");

    public static final ItemEntry<Item> CONVERSION_SIMULATE_CARD = register("conversion_simulate_card", "转换模拟卡");
    public static final ItemEntry<Item> ACTIVATED_CARBON_FILTER_MESH = register("activated_carbon_filter_mesh", "活性炭过滤网");
    public static final ItemEntry<Item> EMPTY_QUARK_RELEASE_CATALYST_HOUSING = register("empty_quark_release_catalyst_housing", "空夸克释放催化剂外壳");
    public static final ItemEntry<Item> UNALIGNED_QUARK_RELEASING_CATALYST = register("unaligned_quark_releasing_catalyst", "未对齐夸克释放催化剂");
    public static final ItemEntry<Item> UP_QUARK_RELEASING_CATALYST = register("up_quark_releasing_catalyst", "上夸克释放催化剂");
    public static final ItemEntry<Item> DOWN_QUARK_RELEASING_CATALYST = register("down_quark_releasing_catalyst", "下夸克释放催化剂");
    public static final ItemEntry<Item> BOTTOM_QUARK_RELEASING_CATALYST = register("bottom_quark_releasing_catalyst", "顶夸克释放催化剂");
    public static final ItemEntry<Item> TOP_QUARK_RELEASING_CATALYST = register("top_quark_releasing_catalyst", "底夸克释放催化剂");
    public static final ItemEntry<Item> STRANGE_QUARK_RELEASING_CATALYST = register("strange_quark_releasing_catalyst", "奇夸克释放催化剂");
    public static final ItemEntry<Item> CHARM_QUARK_RELEASING_CATALYST = register("charm_quark_releasing_catalyst", "粲夸克释放催化剂");

    public static final ItemEntry<Item> PLANT_FIBER = register("plant_fiber", "植物纤维");
    public static final ItemEntry<Item> HEAVY_DUTY_PLATE_1 = register("heavy_duty_plate_1", "一阶重装防护板");
    public static final ItemEntry<Item> HEAVY_DUTY_PLATE_2 = register("heavy_duty_plate_2", "二阶重装防护板");
    public static final ItemEntry<Item> HEAVY_DUTY_PLATE_3 = register("heavy_duty_plate_3", "三阶重装防护板");

    public static final ItemEntry<Item> INGOT_FIELD_SHAPE = register("ingot_field_shape", "锭状形态力场");
    public static final ItemEntry<Item> BALL_FIELD_SHAPE = register("ball_field_shape", "球状形态力场");
    public static final ItemEntry<Item> NON_LINEAR_OPTICAL_LENS = register("non_linear_optical_lens", "非线性光学透镜");
    public static final ItemEntry<Item> CATALYST_BASE = register("catalyst_base", "催化剂基底");

    public static final ItemEntry<Item> DIAMOND_CRYSTAL_CIRCUIT = register("diamond_crystal_circuit", "钻石晶体电路");
    public static final ItemEntry<Item> RUBY_CRYSTAL_CIRCUIT = register("ruby_crystal_circuit", "红宝石晶体电路");
    public static final ItemEntry<Item> EMERALD_CRYSTAL_CIRCUIT = register("emerald_crystal_circuit", "绿宝石晶体电路");
    public static final ItemEntry<Item> SAPPHIRE_CRYSTAL_CIRCUIT = register("sapphire_crystal_circuit", "蓝宝石晶体电路");

    public static final ItemEntry<Item> COOLANT_CELL_10K = registerLang("coolant_cell_10k", "10K Coolant Cell", "10K冷却单元");
    public static final ItemEntry<Item> COOLANT_CELL_30K = registerLang("coolant_cell_30k", "30K Coolant Cell", "30K冷却单元");
    public static final ItemEntry<Item> COOLANT_CELL_60K = registerLang("coolant_cell_60k", "60K Coolant Cell", "60K冷却单元");
    public static final ItemEntry<Item> SPACE_COOLANT_CELL_10K = registerLang("space_coolant_cell_10k", "10K Space Coolant Cell", "10K空间冷却单元");
    public static final ItemEntry<Item> SPACE_COOLANT_CELL_30K = registerLang("space_coolant_cell_30k", "30K Space Coolant Cell", "30K空间冷却单元");
    public static final ItemEntry<Item> SPACE_COOLANT_CELL_60K = registerLang("space_coolant_cell_60k", "60K Space Coolant Cell", "60K空间冷却单元");

    public static final ItemEntry<Item> NANOTUBE_SPOOL = register("nanotube_spool", "纳米管线轴");

    public static final ItemEntry<Item> MICA_BASED_PULP = register("mica_based_pulp", "云母浆");
    public static final ItemEntry<Item> MICA_BASED_SHEET = register("mica_based_sheet", "云母基板");
    public static final ItemEntry<Item> MICA_INSULATOR_SHEET = register("mica_insulator_sheet", "云母绝缘板");
    public static final ItemEntry<Item> MICA_INSULATOR_FOIL = register("mica_insulator_foil", "云母绝缘薄片");

    public static final ItemEntry<Item> RAW_ALUMINUM = register("raw_aluminum", "生铝");

    public static final ItemEntry<Item> HIGH_PURITY_SILICON_BOULE = register("high_purity_silicon_boule", "高纯多晶硅");
    public static final ItemEntry<Item> REGIONAL_SMELTING_SILICON_BOULE = register("regional_smelting_silicon_boule", "区域熔炼的多晶硅");
    public static final ItemEntry<Item> ETCHED_SILICON_BOULE = register("etched_silicon_boule", "电子束蚀刻的多晶硅");
    public static final ItemEntry<Item> FLOATING_ZONE_PURIFICATION_SILICON_BOULE = register("floating_zone_purification_silicon_boule", "浮区提纯的多晶硅");
    public static final ItemEntry<Item> HIGH_PURITY_SINGLE_CRYSTAL_SILICON = register("high_purity_single_crystal_silicon", "超高纯单晶硅");
    public static final ItemEntry<Item> HIGH_PURITY_SILICA_COLUMN = register("high_purity_silica_column", "高纯二氧化硅柱");
    public static final ItemEntry<Item> HIGH_PURITY_SILICA_TUBE = register("high_purity_silica_tube", "高纯二氧化硅管");
    public static final ItemEntry<Item> SIMPLE_OPTICAL_FIBER_PREFORM = item("simple_optical_fiber_preform", "简易光纤预制棒", Item::new)
            .properties(p -> p.stacksTo(1).defaultDurability(1024)).register();
    public static final ItemEntry<Item> SIMPLE_FIBER_OPTIC_ROUGH = register("simple_fiber_optic_rough", "简易光纤粗胚");
    public static final ItemEntry<Item> SIMPLE_FIBER_OPTIC = register("simple_fiber_optic", "简易光纤");

    public static final ItemEntry<Item> SPOOLS_MICRO = register("spools_micro", "微型线轴");
    public static final ItemEntry<Item> SPOOLS_SMALL = register("spools_small", "小型线轴");
    public static final ItemEntry<Item> SPOOLS_MEDIUM = register("spools_medium", "中型线轴");
    public static final ItemEntry<Item> SPOOLS_LARGE = register("spools_large", "大型线轴");
    public static final ItemEntry<Item> SPOOLS_JUMBO = register("spools_jumbo", "巨型线轴");

    public static final ItemEntry<Item> COLORFUL_MYSTICAL_FLOWER = register("colorful_mystical_flower", "多彩神秘花瓣");
    public static final ItemEntry<Wireless.Item> WIRELESS_ME2IN1 = item("wireless_me2in1_terminal", "无线ME2合1终端", Wireless.Item::new).register();
    public static final ItemEntry<Item> GAIA_CORE = register("gaia_core", "§e盖亚之核");
    public static final ItemEntry<Item> UNSTABLE_GAIA_SOUL = register("unstable_gaia_soul", "§e不稳定的盖亚之魂");
    public static final ItemEntry<Item> WILDEN_SLATE = register("wilden_slate", "§d荒野石板");

    public static final ItemEntry<SpaceArmorComponentItem> SPACE_NANOMUSCLE_CHESTPLATE = item("space_nanomuscle_chestplate", "纳米肌体™套装太空胸甲",
            (p) -> new SpaceArmorComponentItem(GTArmorMaterials.ARMOR,
                    ArmorItem.Type.CHESTPLATE, 8000, p)
                    .setArmorLogic(new NanoMuscleSuite(
                            ArmorItem.Type.CHESTPLATE,
                            512,
                            6_400_000L * (long) Math.max(1, Math.pow(4, ConfigHolder.INSTANCE.tools.voltageTierNanoSuit - 3)),
                            ConfigHolder.INSTANCE.tools.voltageTierNanoSuit)))
            .toolTips(ComponentBuilder.create().addLines("需要整套装备", "A complete set of armor is required").build().getArray())
            .lang("NanoMuscle™ Space Suite Chestplate")
            .properties(p -> p.rarity(Rarity.RARE))
            .tag(Tags.Items.ARMORS_CHESTPLATES)
            .tag(CustomTags.PPE_ARMOR)
            .register();

    public static final ItemEntry<SpaceArmorComponentItem> SPACE_ADVANCED_NANOMUSCLE_CHESTPLATE = item("space_advanced_nanomuscle_chestplate", "纳米肌体™进阶套装太空胸甲",
            (p) -> new SpaceArmorComponentItem(
                    GTArmorMaterials.ARMOR,
                    ArmorItem.Type.CHESTPLATE, 16000, p)
                    .setArmorLogic(new AdvancedNanoMuscleSuite(
                            512,
                            12_800_000L * (long) Math.max(1, Math.pow(4, ConfigHolder.INSTANCE.tools.voltageTierAdvNanoSuit - 3)),
                            ConfigHolder.INSTANCE.tools.voltageTierAdvNanoSuit)))
            .toolTips(ComponentBuilder.create().addLines("需要整套装备", "A complete set of armor is required").build().getArray())
            .lang("Advanced NanoMuscle™ Space Suite Chestplate")
            .properties(p -> p.rarity(Rarity.EPIC))
            .tag(Tags.Items.ARMORS_CHESTPLATES)
            .tag(CustomTags.PPE_ARMOR)
            .register();

    public static final ItemEntry<SpaceArmorComponentItem> SPACE_QUARKTECH_CHESTPLATE = item("space_quarktech_chestplate", "夸克高科™套装太空胸甲",
            (p) -> new SpaceArmorComponentItem(
                    GTArmorMaterials.ARMOR,
                    ArmorItem.Type.CHESTPLATE, 32000, p)
                    .setArmorLogic(new QuarkTechSuite(
                            ArmorItem.Type.CHESTPLATE,
                            8192,
                            100_000_000L * (long) Math.max(1, Math.pow(4, ConfigHolder.INSTANCE.tools.voltageTierQuarkTech - 5)),
                            ConfigHolder.INSTANCE.tools.voltageTierQuarkTech)))
            .toolTips(ComponentBuilder.create().addLines("需要整套装备", "A complete set of armor is required").build().getArray())
            .lang("QuarkTech™ Space Suite Chestplate")
            .properties(p -> p.rarity(Rarity.RARE))
            .tag(Tags.Items.ARMORS_CHESTPLATES)
            .tag(CustomTags.PPE_ARMOR)
            .register();

    public static final ItemEntry<SpaceArmorComponentItem> SPACE_ADVANCED_QUARKTECH_CHESTPLATE = item("space_advanced_quarktech_chestplate", "夸克高科™进阶套装太空胸甲",
            (p) -> new SpaceArmorComponentItem(GTArmorMaterials.ARMOR,
                    ArmorItem.Type.CHESTPLATE, 128000, p)
                    .setArmorLogic(new AdvancedQuarkTechSuite(
                            8192,
                            1_000_000_000L * (long) Math.max(1, Math.pow(4, ConfigHolder.INSTANCE.tools.voltageTierAdvQuarkTech - 6)),
                            ConfigHolder.INSTANCE.tools.voltageTierAdvQuarkTech)))
            .toolTips(ComponentBuilder.create().addLines("需要整套装备", "A complete set of armor is required").build().getArray())
            .lang("Advanced QuarkTech™ Space Suite Chestplate")
            .properties(p -> p.rarity(Rarity.EPIC))
            .tag(Tags.Items.ARMORS_CHESTPLATES)
            .tag(CustomTags.PPE_ARMOR)
            .register();

    public static final ItemEntry<UpgradeModuleItem> SPEED_UPGRADE_MODULE = item("speed_upgrade_module", "速度升级模块", UpgradeModuleItem::new).register();
    public static final ItemEntry<UpgradeModuleItem> ENERGY_UPGRADE_MODULE = item("energy_upgrade_module", "能量升级模块", UpgradeModuleItem::new).register();

    public static final ItemEntry<Item> DISPOSABLE_FILE = item("disposable_file", "一次性锉刀", Item::new)
            .tag(CustomTags.CRAFTING_FILES).register();

    public static final ItemEntry<Item> DISPOSABLE_WRENCH = item("disposable_wrench", "一次性扳手", Item::new)
            .tag(CustomTags.CRAFTING_WRENCHES).register();

    public static final ItemEntry<Item> DISPOSABLE_CROWBAR = item("disposable_crowbar", "一次性撬棍", Item::new)
            .tag(CustomTags.CRAFTING_CROWBARS).register();

    public static final ItemEntry<Item> DISPOSABLE_WIRE_CUTTER = item("disposable_wire_cutter", "一次性剪线钳", Item::new)
            .tag(CustomTags.CRAFTING_WIRE_CUTTERS).register();

    public static final ItemEntry<Item> DISPOSABLE_HAMMER = item("disposable_hammer", "一次性锤子", Item::new)
            .tag(CustomTags.CRAFTING_HAMMERS).register();

    public static final ItemEntry<Item> DISPOSABLE_MALLET = item("disposable_mallet", "一次性软锤", Item::new)
            .tag(CustomTags.CRAFTING_MALLETS).register();

    public static final ItemEntry<Item> DISPOSABLE_SCREWDRIVER = item("disposable_screwdriver", "一次性螺丝刀", Item::new)
            .tag(CustomTags.CRAFTING_SCREWDRIVERS).register();

    public static final ItemEntry<Item> DISPOSABLE_SAW = item("disposable_saw", "一次性锯子", Item::new)
            .tag(CustomTags.CRAFTING_SAWS).register();

    public static final ItemEntry<Item> DISPOSABLE_FILE_MOLD = register("disposable_file_mold", "一次性锉刀模具");
    public static final ItemEntry<Item> DISPOSABLE_WRENCH_MOLD = register("disposable_wrench_mold", "一次性扳手模具");
    public static final ItemEntry<Item> DISPOSABLE_CROWBAR_MOLD = register("disposable_crowbar_mold", "一次性撬棍模具");
    public static final ItemEntry<Item> DISPOSABLE_WIRE_CUTTER_MOLD = register("disposable_wire_cutter_mold", "一次性剪线钳模具");
    public static final ItemEntry<Item> DISPOSABLE_HAMMER_MOLD = register("disposable_hammer_mold", "一次性锤模具");
    public static final ItemEntry<Item> DISPOSABLE_MALLET_MOLD = register("disposable_mallet_mold", "一次性软锤模具");
    public static final ItemEntry<Item> DISPOSABLE_SCREWDRIVER_MOLD = register("disposable_screwdriver_mold", "一次性螺丝刀模具");
    public static final ItemEntry<Item> DISPOSABLE_SAW_MOLD = register("disposable_saw_mold", "一次性锯模具");
    public static final ItemEntry<ComponentItem> GRASS_HARVESTER = item("grass_harvester", "割草镰刀", ComponentItem::create).properties(p -> p.stacksTo(1).durability(128).setNoRepair()).onRegister(attach(GrassHarvesterBehaviour.INSTANCE)).register();

    // public static ItemEntry<SteamProspectorBehavior.ProspectorItem> PROSPECTOR_STEAM = item("prospector.steam",
    // "原始人探矿仪", SteamProspectorBehavior.ProspectorItem::new)
    // .properties(p -> p.stacksTo(1))
    // .onRegister(attach(new SteamProspectorBehavior()))
    // .register();
    public static ItemEntry<ComponentItem> PROSPECTOR_MANA_ULV = item("prospector.mana_ulv", "魔力钢探矿仪", ComponentItem::create)
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(ManaProspectorBehavior.create(ManaProspectorBehavior.ULV)))
            .register();
    public static ItemEntry<ComponentItem> PROSPECTOR_MANA_LV = item("prospector.mana_lv", "泰拉钢探矿仪", ComponentItem::create)
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(ManaProspectorBehavior.create(ManaProspectorBehavior.LV)))
            .register();
    public static ItemEntry<ComponentItem> PROSPECTOR_MANA_HV = item("prospector.mana_hv", "精灵钢探矿仪", ComponentItem::create)
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(ManaProspectorBehavior.create(ManaProspectorBehavior.HV)))
            .register();

    public static final ItemEntry<ComponentItem> COORDINATE_CARD = item("coordinate_card", "坐标信息卡", ComponentItem::create)
            .properties(p -> p.stacksTo(1))
            .onRegister(attach(new CoordinateCardBehavior()))
            .register();
}
