package com.gto.gtocore.common.data;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.machine.SimpleNoEnergyMachine;
import com.gto.gtocore.api.machine.part.GTOPartAbility;
import com.gto.gtocore.api.machine.part.ItemHatchPartMachine;
import com.gto.gtocore.client.renderer.machine.BallHatchRenderer;
import com.gto.gtocore.client.renderer.machine.WindMillTurbineRenderer;
import com.gto.gtocore.common.machine.electric.ElectricHeaterMachine;
import com.gto.gtocore.common.machine.electric.VacuumPumpMachine;
import com.gto.gtocore.common.machine.generator.LightningRodMachine;
import com.gto.gtocore.common.machine.generator.MagicEnergyMachine;
import com.gto.gtocore.common.machine.generator.WindMillTurbineMachine;
import com.gto.gtocore.common.machine.multiblock.part.*;
import com.gto.gtocore.common.machine.multiblock.part.ae.MECatalystPatternBufferPartMachine;
import com.gto.gtocore.common.machine.multiblock.part.ae.MEDualHatchStockPartMachine;
import com.gto.gtocore.common.machine.multiblock.part.ae.MEProgrammablePatternBufferPartMachine;
import com.gto.gtocore.common.machine.multiblock.part.ae.METagFilterStockBusPartMachine;
import com.gto.gtocore.common.machine.multiblock.part.maintenance.*;
import com.gto.gtocore.common.machine.noenergy.BoilWaterMachine;
import com.gto.gtocore.common.machine.noenergy.HeaterMachine;
import com.gto.gtocore.common.machine.noenergy.PerformanceMonitorMachine;
import com.gto.gtocore.common.machine.steam.SteamVacuumPumpMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.steam.SimpleSteamMachine;
import com.gregtechceu.gtceu.client.renderer.machine.*;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.common.item.TurbineRotorBehaviour;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DualHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.EnergyHatchPartMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import com.hepdd.gtmthings.GTMThings;
import com.hepdd.gtmthings.common.block.machine.multiblock.part.ProgrammableHatchPartMachine;
import it.unimi.dsi.fastutil.Pair;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.capability.recipe.IO.IN;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.PARALLEL_HATCH;
import static com.gto.gtocore.utils.register.MachineRegisterUtils.*;

public interface GTOMachines {

    static void init() {}

    //////////////////////////////////////
    // *** Simple Machine ***//
    //////////////////////////////////////
    Pair<MachineDefinition, MachineDefinition> STEAM_VACUUM_PUMP = registerSteamMachines("steam_vacuum_pump", "真空泵", SteamVacuumPumpMachine::new, (pressure, builder) -> builder
            .allRotation()
            .recipeType(GTORecipeTypes.VACUUM_PUMP_RECIPES)
            .recipeModifier(SimpleSteamMachine::recipeModifier)
            .tooltips(Component.translatable("gtocore.recipe.vacuum.tier", pressure ? 2 : 1))
            .renderer(() -> new WorkableSteamMachineRenderer(pressure, GTOCore.id("block/machines/vacuum_pump")))
            .register());

    MachineDefinition[] SEMI_FLUID_GENERATOR = registerSimpleGenerator("semi_fluid", "半流质发电机",
            GTORecipeTypes.SEMI_FLUID_GENERATOR_FUELS, t -> 6400, ULV, LV, MV, HV);

    MachineDefinition[] THERMAL_GENERATOR = registerSimpleGenerator("thermal_generator", "热力发电机",
            GTORecipeTypes.THERMAL_GENERATOR_FUELS, tier -> (tier + 1) * 1000, ULV, LV, MV);

    MachineDefinition[] ROCKET_ENGINE_GENERATOR = registerSimpleGenerator("rocket_engine", "火箭引擎", GTORecipeTypes.ROCKET_ENGINE_FUELS,
            GTMachineUtils.genericGeneratorTankSizeFunction, EV, IV, LuV);

    MachineDefinition[] NAQUADAH_REACTOR_GENERATOR = registerSimpleGenerator("naquadah_reactor", "硅岩反应堆", GTORecipeTypes.NAQUADAH_REACTOR,
            GTMachineUtils.genericGeneratorTankSizeFunction, IV, LuV, ZPM);

    MachineDefinition[] ARC_GENERATOR = registerSimpleMachines("arc_generator", "电弧发生器", GTORecipeTypes.ARC_GENERATOR_RECIPES, GTMachineUtils.defaultTankSizeFunction);

    MachineDefinition[] DEHYDRATOR = registerSimpleMachines("dehydrator", "脱水机", GTORecipeTypes.DEHYDRATOR_RECIPES, GTMachineUtils.defaultTankSizeFunction);

    MachineDefinition[] UNPACKER = registerSimpleMachines("unpacker", "解包机", GTORecipeTypes.UNPACKER_RECIPES, GTMachineUtils.defaultTankSizeFunction);

    MachineDefinition[] CLUSTER = registerSimpleMachines("cluster", "多辊式轧机", GTORecipeTypes.CLUSTER_RECIPES, GTMachineUtils.defaultTankSizeFunction);

    MachineDefinition[] ROLLING = registerSimpleMachines("rolling", "辊轧机", GTORecipeTypes.ROLLING_RECIPES, GTMachineUtils.defaultTankSizeFunction);

    MachineDefinition[] LAMINATOR = registerSimpleMachines("laminator", "过胶机", GTORecipeTypes.LAMINATOR_RECIPES, GTMachineUtils.defaultTankSizeFunction);

    MachineDefinition[] LOOM = registerSimpleMachines("loom", "织布机", GTORecipeTypes.LOOM_RECIPES, GTMachineUtils.defaultTankSizeFunction);

    MachineDefinition[] LASER_WELDER = registerSimpleMachines("laser_welder", "激光焊接器", GTORecipeTypes.LASER_WELDER_RECIPES, GTMachineUtils.defaultTankSizeFunction);

    MachineDefinition[] WORLD_DATA_SCANNER = registerSimpleMachines("world_data_scanner", "世界信息扫描仪",
            GTORecipeTypes.WORLD_DATA_SCANNER_RECIPES, tier -> 64000);

    MachineDefinition[] NEUTRON_COMPRESSOR = registerSimpleMachines("neutron_compressor", "中子压缩机",
            GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES, GTMachineUtils.defaultTankSizeFunction, MAX);

    MachineDefinition[] ULV_WIREMILL = registerSimpleMachines("wiremill", "线材轧机", GTORecipeTypes.WIREMILL_RECIPES, GTMachineUtils.defaultTankSizeFunction, GTCEu.id("block/machines/wiremill"), ULV);
    MachineDefinition[] ULV_LATHE = registerSimpleMachines("lathe", "车床", GTORecipeTypes.LATHE_RECIPES, GTMachineUtils.defaultTankSizeFunction, GTCEu.id("block/machines/lathe"), ULV);
    MachineDefinition[] ULV_FLUID_SOLIDIFIER = registerSimpleMachines("fluid_solidifier", "流体固化器", GTORecipeTypes.FLUID_SOLIDFICATION_RECIPES, GTMachineUtils.defaultTankSizeFunction, GTCEu.id("block/machines/fluid_solidifier"), ULV);
    MachineDefinition[] ULV_CHEMICAL_REACTOR = registerSimpleMachines("chemical_reactor", "化学反应釜", GTORecipeTypes.CHEMICAL_RECIPES, GTMachineUtils.defaultTankSizeFunction, GTCEu.id("block/machines/chemical_reactor"), ULV);
    MachineDefinition[] ULV_ASSEMBLER = registerSimpleMachines("assembler", "组装机", GTORecipeTypes.ASSEMBLER_RECIPES, GTMachineUtils.defaultTankSizeFunction, GTCEu.id("block/machines/assembler"), ULV);
    MachineDefinition[] ULV_PACKER = registerSimpleMachines("packer", "打包机", GTORecipeTypes.PACKER_RECIPES, GTMachineUtils.defaultTankSizeFunction, GTCEu.id("block/machines/packer"), ULV);
    MachineDefinition[] ULV_UNPACKER = registerSimpleMachines("unpacker", "解包机", GTORecipeTypes.UNPACKER_RECIPES, GTMachineUtils.defaultTankSizeFunction, ULV);
    MachineDefinition[] ULV_LOOM = registerSimpleMachines("loom", "织布机", GTORecipeTypes.LOOM_RECIPES, GTMachineUtils.defaultTankSizeFunction, ULV);

    MachineDefinition[] VACUUM_PUMP = registerTieredMachines("vacuum_pump", tier -> "%s真空泵 %s".formatted(GTOValues.VLVHCN[tier], VLVT[tier]), VacuumPumpMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Vacuum Pump %s".formatted(VLVH[tier], VLVT[tier]))
                    .nonYAxisRotation()
                    .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("vacuum_pump"), GTORecipeTypes.VACUUM_PUMP_RECIPES))
                    .alwaysTryModifyRecipe(true)
                    .recipeType(GTORecipeTypes.VACUUM_PUMP_RECIPES)
                    .workableTieredHullRenderer(GTOCore.id("block/machines/vacuum_pump"))
                    .tooltips(Component.translatable("gtocore.recipe.vacuum.tier", tier + 1))
                    .tooltips(GTMachineUtils.workableTiered(tier, V[tier], V[tier] << 6, GTORecipeTypes.VACUUM_PUMP_RECIPES, GTMachineUtils.defaultTankSizeFunction.apply(tier), true))
                    .register(),
            LV, MV, HV);
    MachineDefinition[] LIGHTNING_ROD = registerTieredMachines(
            "lightning_rod", tier -> "%s避雷针 %s".formatted(GTOValues.VLVHCN[tier], VLVT[tier]),
            LightningRodMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Lightning Rod %s".formatted(VLVH[tier], VLVT[tier]))
                    .nonYAxisRotation()
                    .renderer(() -> new SimpleGeneratorMachineRenderer(tier, GTOCore.id("block/generators/lightning_rod")))
                    .tooltips(Component.translatable("gtocore.machine.lightning_rod.tooltip.0"))
                    .tooltips(Component.translatable("gtocore.machine.lightning_rod.tooltip.1"))
                    .tooltips(Component.translatable("gtocore.machine.lightning_rod.tooltip.2"))
                    .tooltips(Component.translatable("gtocore.universal.tooltip.ampere_out", 512))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.voltage_out",
                            FormattingUtil.formatNumbers(V[tier - 1]), VNF[tier - 1]))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.energy_storage_capacity",
                            FormattingUtil.formatNumbers(48828 * (1L << (2 * tier)))))
                    .register(),
            EV, IV, LuV);

    MachineDefinition[] PRIMITIVE_MAGIC_ENERGY = registerTieredMachines(
            "primitive_magic_energy", tier -> "%s原始魔法能源吸收器 %s".formatted(GTOValues.VLVHCN[tier], VLVT[tier]),
            MagicEnergyMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Primitive Magic Energy %s".formatted(VLVH[tier], VLVT[tier]))
                    .nonYAxisRotation()
                    .renderer(() -> new SimpleGeneratorMachineRenderer(tier,
                            GTOCore.id("block/generators/primitive_magic_energy")))
                    .tooltips(Component.translatable("gtocore.machine.primitive_magic_energy.tooltip.0"))
                    .tooltips(Component.translatable("gtocore.universal.tooltip.ampere_out", 16))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.voltage_out",
                            FormattingUtil.formatNumbers(V[tier]), VNF[tier]))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.energy_storage_capacity",
                            FormattingUtil.formatNumbers(V[tier] << 9)))
                    .register(),
            ULV, LV);

    MachineDefinition[] WIND_MILL_TURBINE = registerTieredMachines(
            "wind_mill_turbine", tier -> "%s风力发电机 %s".formatted(GTOValues.VLVHCN[tier], VLVT[tier]),
            WindMillTurbineMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Wind Mill Turbine %s".formatted(VLVH[tier], VLVT[tier]))
                    .nonYAxisRotation()
                    .renderer(() -> new WindMillTurbineRenderer(tier))
                    .hasTESR(true)
                    .tooltips(Component.translatable("gtocore.machine.wind_mill_turbine.tooltip.0"))
                    .tooltips(Component.translatable("gtocore.machine.wind_mill_turbine.tooltip.1"))
                    .tooltips(Component.translatable("gtocore.universal.tooltip.ampere_out", 2))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.voltage_out",
                            FormattingUtil.formatNumbers(V[tier]), VNF[tier]))
                    .tooltips(Component.translatable("gtceu.universal.tooltip.energy_storage_capacity",
                            FormattingUtil.formatNumbers(V[tier] << 6)))
                    .register(),
            ULV, LV, MV, HV);

    MachineDefinition HEATER = machine("heater", "加热器", HeaterMachine::new)
            .tier(ULV)
            .editableUI(SimpleNoEnergyMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("heater"), GTRecipeTypes.STEAM_BOILER_RECIPES))
            .recipeType(GTRecipeTypes.DUMMY_RECIPES)
            .noRecipeModifier()
            .nonYAxisRotation()
            .workableTieredHullRenderer(GTCEu.id("block/generators/boiler/coal"))
            .register();

    MachineDefinition BOILER = machine("boiler", "锅炉", BoilWaterMachine::new)
            .tier(ULV)
            .editableUI(SimpleNoEnergyMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("boiler"), GTRecipeTypes.STEAM_TURBINE_FUELS))
            .recipeType(GTRecipeTypes.DUMMY_RECIPES)
            .noRecipeModifier()
            .nonYAxisRotation()
            .workableTieredHullRenderer(GTCEu.id("block/generators/boiler/lava"))
            .register();

    MachineDefinition PERFORMANCE_MONITOR = machine("performance_monitor", "性能监控器", PerformanceMonitorMachine::new)
            .nonYAxisRotation()
            .tooltipsText("Can monitor the average latency of all machines within 2 seconds and support highlighting.", "能监测全部机器2秒内的平均延迟，并支持高亮显示。")
            .workableTieredHullRenderer(GTMThings.id("block/machines/wireless_energy_monitor"))
            .tier(LV)
            .register();

    MachineDefinition ELECTRIC_HEATER = machine("electric_heater", "电力加热器", ElectricHeaterMachine::new)
            .tier(LV)
            .editableUI(SimpleNoEnergyMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("electric_heater"), GTORecipeTypes.MANA_HEATER_RECIPES))
            .recipeType(GTORecipeTypes.MANA_HEATER_RECIPES)
            .noRecipeModifier()
            .nonYAxisRotation()
            .tooltipsText("Start heating after power on.", "通电后开始加热")
            .workableTieredHullRenderer(GTCEu.id("block/generators/boiler/coal"))
            .register();

    //////////////////////////////////////
    // ********** Part **********//
    //////////////////////////////////////
    MachineDefinition[] THREAD_HATCH = registerTieredMachines("thread_hatch", tier -> GTOValues.VNFR[tier] + "线程仓",
            ThreadHatchPartMachine::new, (tier, builder) -> builder
                    .langValue(VNF[tier] + " Thread Hatch")
                    .allRotation()
                    .abilities(GTOPartAbility.THREAD_HATCH)
                    .workableTieredHullRenderer(GTOCore.id("block/machines/thread_hatch/thread_hatch_mk" + (tier - 8)))
                    .tooltips(Component.translatable("gtocore.machine.thread_hatch.tooltip.0", 1 << (tier - ZPM)))
                    .register(),
            UHV, UEV, UIV, UXV, OpV, MAX);

    MachineDefinition[] ACCELERATE_HATCH = registerTieredMachines("accelerate_hatch", tier -> GTOValues.VNFR[tier] + "加速仓",
            AccelerateHatchPartMachine::new, (tier, builder) -> builder
                    .langValue(VNF[tier] + " Accelerate Hatch")
                    .allRotation()
                    .abilities(GTOPartAbility.ACCELERATE_HATCH)
                    .tooltips(Component.translatable("gtocore.machine.accelerate_hatch.tooltip.0"))
                    .tooltips(Component.translatable("gtocore.machine.accelerate_hatch.tooltip.1"))
                    .workableTieredHullRenderer(GTOCore.id("block/machines/accelerate_hatch/accelerate_hatch_mk" + tier))
                    .register(),
            tiersBetween(LV, MAX));

    MachineDefinition[] PROGRAMMABLEC_HATCH = registerTieredMachines("programmablec_hatch", tier -> GTOValues.VNFR[tier] + "可编程仓",
            (holder, tier) -> new ProgrammableHatchPartMachine(holder, tier, IN),
            (tier, builder) -> builder
                    .langValue("%s Programmablec Hatch".formatted(VNF[tier]))
                    .allRotation()
                    .abilities(PartAbility.IMPORT_ITEMS)
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/dual_hatch.import")))
                    .tooltips(Component.translatable("gtceu.machine.dual_hatch.import.tooltip"),
                            Component.translatable("gtceu.universal.tooltip.item_storage_capacity", tier * tier),
                            Component.translatable("gtceu.universal.tooltip.fluid_storage_capacity_mult", tier, DualHatchPartMachine.getTankCapacity(DualHatchPartMachine.INITIAL_TANK_CAPACITY, tier)),
                            Component.translatable("gtceu.universal.enabled"))
                    .register(),
            tiersBetween(LV, MAX));

    MachineDefinition[] ENERGY_INPUT_HATCH_4A = registerTieredMachines("energy_input_hatch_4a", tier -> 4 + "安" + GTOValues.VNFR[tier] + "能源仓",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IO.IN, 4),
            (tier, builder) -> builder
                    .langValue(VNF[tier] + " 4A Energy Hatch")
                    .allRotation()
                    .abilities(PartAbility.INPUT_ENERGY)
                    .tooltips(Component.translatable("gtceu.machine.energy_hatch.input_hi_amp.tooltip"))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.input_4a")))
                    .register(),
            tiersBetween(LV, HV));

    MachineDefinition[] ENERGY_OUTPUT_HATCH_4A = registerTieredMachines("energy_output_hatch_4a", tier -> 4 + "安" + GTOValues.VNFR[tier] + "动力仓",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IO.OUT, 4),
            (tier, builder) -> builder
                    .langValue(VNF[tier] + " 4A Dynamo Hatch")
                    .allRotation()
                    .abilities(PartAbility.OUTPUT_ENERGY)
                    .tooltips(Component.translatable("gtceu.machine.energy_hatch.output_hi_amp.tooltip"))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.output_4a")))
                    .register(),
            tiersBetween(LV, HV));

    MachineDefinition[] ENERGY_INPUT_HATCH_16A = registerTieredMachines("energy_input_hatch_16a", tier -> 16 + "安" + GTOValues.VNFR[tier] + "能源仓",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IO.IN, 16),
            (tier, builder) -> builder
                    .langValue(VNF[tier] + " 16A Energy Hatch")
                    .allRotation()
                    .abilities(PartAbility.INPUT_ENERGY)
                    .tooltips(Component.translatable("gtceu.machine.energy_hatch.input_hi_amp.tooltip"))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.input_16a")))
                    .register(),
            tiersBetween(LV, HV));

    MachineDefinition[] ENERGY_OUTPUT_HATCH_16A = registerTieredMachines("energy_output_hatch_16a", tier -> 16 + "安" + GTOValues.VNFR[tier] + "动力仓",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IO.OUT, 16),
            (tier, builder) -> builder
                    .langValue(VNF[tier] + " 16A Dynamo Hatch")
                    .allRotation()
                    .abilities(PartAbility.OUTPUT_ENERGY)
                    .tooltips(Component.translatable("gtceu.machine.energy_hatch.output_hi_amp.tooltip"))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.output_16a")))
                    .register(),
            tiersBetween(LV, HV));

    MachineDefinition[] DRONE_HATCH = registerTieredMachines("drone_hatch", tier -> GTOValues.VNFR[tier] + "无人机仓",
            DroneHatchPartMachine::new, (tier, builder) -> builder
                    .langValue(VNF[tier] + " Drone Hatch")
                    .allRotation()
                    .abilities(GTOPartAbility.DRONE_HATCH)
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/item_bus.import")))
                    .register(),
            HV, EV, IV);

    MachineDefinition[] WIRELESS_INPUT_HATCH_2 = registerWirelessEnergyHatch(IO.IN, 2,
            PartAbility.INPUT_ENERGY);
    MachineDefinition[] WIRELESS_OUTPUT_HATCH_2 = registerWirelessEnergyHatch(IO.OUT, 2,
            PartAbility.OUTPUT_ENERGY);
    MachineDefinition[] WIRELESS_INPUT_HATCH_4 = registerWirelessEnergyHatch(IO.IN, 4,
            PartAbility.INPUT_ENERGY);
    MachineDefinition[] WIRELESS_OUTPUT_HATCH_4 = registerWirelessEnergyHatch(IO.OUT, 4,
            PartAbility.OUTPUT_ENERGY);
    MachineDefinition[] WIRELESS_INPUT_HATCH_16 = registerWirelessEnergyHatch(IO.IN, 16,
            PartAbility.INPUT_ENERGY);
    MachineDefinition[] WIRELESS_OUTPUT_HATCH_16 = registerWirelessEnergyHatch(IO.OUT, 16,
            PartAbility.OUTPUT_ENERGY);
    MachineDefinition[] WIRELESS_INPUT_HATCH_64 = registerWirelessEnergyHatch(IO.IN, 64,
            PartAbility.INPUT_ENERGY);
    MachineDefinition[] WIRELESS_OUTPUT_HATCH_64 = registerWirelessEnergyHatch(IO.OUT, 64,
            PartAbility.OUTPUT_ENERGY);
    MachineDefinition[] WIRELESS_INPUT_HATCH_256 = registerWirelessEnergyHatch(IO.IN, 256,
            PartAbility.INPUT_LASER);
    MachineDefinition[] WIRELESS_OUTPUT_HATCH_256 = registerWirelessEnergyHatch(IO.OUT, 256,
            PartAbility.OUTPUT_LASER);
    MachineDefinition[] WIRELESS_INPUT_HATCH_1024 = registerWirelessEnergyHatch(IO.IN, 1024,
            PartAbility.INPUT_LASER);
    MachineDefinition[] WIRELESS_OUTPUT_HATCH_1024 = registerWirelessEnergyHatch(IO.OUT, 1024,
            PartAbility.OUTPUT_LASER);
    MachineDefinition[] WIRELESS_INPUT_HATCH_4096 = registerWirelessEnergyHatch(IO.IN, 4096,
            PartAbility.INPUT_LASER);
    MachineDefinition[] WIRELESS_OUTPUT_HATCH_4096 = registerWirelessEnergyHatch(IO.OUT, 4096,
            PartAbility.OUTPUT_LASER);
    MachineDefinition[] WIRELESS_INPUT_HATCH_16384 = registerWirelessEnergyHatch(IO.IN, 16384,
            PartAbility.INPUT_LASER);
    MachineDefinition[] WIRELESS_OUTPUT_HATCH_16384 = registerWirelessEnergyHatch(IO.OUT, 16384,
            PartAbility.OUTPUT_LASER);
    MachineDefinition[] WIRELESS_INPUT_HATCH_65536 = registerWirelessEnergyHatch(IO.IN, 65536,
            PartAbility.INPUT_LASER);
    MachineDefinition[] WIRELESS_OUTPUT_HATCH_65536 = registerWirelessEnergyHatch(IO.OUT, 65536,
            PartAbility.OUTPUT_LASER);
    MachineDefinition[] WIRELESS_INPUT_HATCH_262144 = registerWirelessEnergyHatch(IO.IN, 262144,
            PartAbility.INPUT_LASER);
    MachineDefinition[] WIRELESS_OUTPUT_HATCH_262144 = registerWirelessEnergyHatch(IO.OUT, 262144,
            PartAbility.OUTPUT_LASER);
    MachineDefinition[] WIRELESS_INPUT_HATCH_1048576 = registerWirelessEnergyHatch(IO.IN, 1048576,
            PartAbility.INPUT_LASER);
    MachineDefinition[] WIRELESS_OUTPUT_HATCH_1048576 = registerWirelessEnergyHatch(IO.OUT, 1048576,
            PartAbility.OUTPUT_LASER);
    MachineDefinition[] WIRELESS_INPUT_HATCH_4194304 = registerWirelessEnergyHatch(IO.IN, 4194304,
            PartAbility.INPUT_LASER);
    MachineDefinition[] WIRELESS_OUTPUT_HATCH_4194304 = registerWirelessEnergyHatch(IO.OUT, 4194304,
            PartAbility.OUTPUT_LASER);

    MachineDefinition[] LASER_INPUT_HATCH_16384 = registerLaserHatch(IO.IN, 16384,
            PartAbility.INPUT_LASER);
    MachineDefinition[] LASER_OUTPUT_HATCH_16384 = registerLaserHatch(IO.OUT, 16384,
            PartAbility.OUTPUT_LASER);
    MachineDefinition[] LASER_INPUT_HATCH_65536 = registerLaserHatch(IO.IN, 65536,
            PartAbility.INPUT_LASER);
    MachineDefinition[] LASER_OUTPUT_HATCH_65536 = registerLaserHatch(IO.OUT, 65536,
            PartAbility.OUTPUT_LASER);
    MachineDefinition[] LASER_INPUT_HATCH_262144 = registerLaserHatch(IO.IN, 262144,
            PartAbility.INPUT_LASER);
    MachineDefinition[] LASER_OUTPUT_HATCH_262144 = registerLaserHatch(IO.OUT, 262144,
            PartAbility.OUTPUT_LASER);
    MachineDefinition[] LASER_INPUT_HATCH_1048576 = registerLaserHatch(IO.IN, 1048576,
            PartAbility.INPUT_LASER);
    MachineDefinition[] LASER_OUTPUT_HATCH_1048576 = registerLaserHatch(IO.OUT, 1048576,
            PartAbility.OUTPUT_LASER);
    MachineDefinition[] LASER_INPUT_HATCH_4194304 = registerLaserHatch(IO.IN, 4194304,
            PartAbility.INPUT_LASER);
    MachineDefinition[] LASER_OUTPUT_HATCH_4194304 = registerLaserHatch(IO.OUT, 4194304,
            PartAbility.OUTPUT_LASER);

    MachineDefinition[] NEUTRON_ACCELERATOR = registerTieredMachines("neutron_accelerator", tier -> VNF[tier] + "中子加速器",
            NeutronAcceleratorPartMachine::new,
            (tier, builder) -> builder
                    .langValue(VNF[tier] + "Neutron Accelerator")
                    .allRotation()
                    .abilities(GTOPartAbility.NEUTRON_ACCELERATOR)
                    .tooltips(Component.translatable("gtceu.universal.tooltip.max_voltage_in", V[tier], VNF[tier]),
                            Component.translatable("gtocore.machine.neutron_accelerator.tooltip.0", (V[tier] << 3) / 10),
                            Component.translatable("gtocore.machine.neutron_accelerator.tooltip.1"),
                            Component.translatable("gtceu.universal.tooltip.energy_storage_capacity", 2 * V[tier]))
                    .overlayTieredHullRenderer("neutron_accelerator")
                    .register(),
            GTMachineUtils.ALL_TIERS);

    MachineDefinition WIRELESS_DATA_HATCH_TRANSMITTER = machine("wireless_data_transmitter_hatch", "无线光学数据源仓", (holder) -> new WirelessOpticalDataHatchMachine(holder, true))
            .allRotation()
            .abilities(PartAbility.OPTICAL_DATA_TRANSMISSION)
            .renderer(() -> new OverlayTieredMachineRenderer(UV, GTCEu.id("block/machine/part/optical_data_hatch")))
            .tooltips(Component.translatable("gtceu.machine.data_transmitter_hatch.tooltip"), Component.translatable("gtocore.machine.wireless_data_transmitter_hatch.tooltip.0"))
            .tier(UV)
            .register();

    MachineDefinition WIRELESS_DATA_HATCH_RECEIVER = machine("wireless_data_receiver_hatch", "无线光学数据靶仓", (holder) -> new WirelessOpticalDataHatchMachine(holder, false))
            .allRotation()
            .abilities(PartAbility.OPTICAL_DATA_RECEPTION)
            .renderer(() -> new OverlayTieredMachineRenderer(UV, GTCEu.id("block/machine/part/optical_data_hatch")))
            .tooltips(Component.translatable("gtceu.machine.data_receiver_hatch.tooltip"), Component.translatable("gtocore.machine.wireless_data_receiver_hatch.tooltip.0"))
            .tier(UV)
            .register();

    MachineDefinition LARGE_STEAM_HATCH = machine("large_steam_input_hatch", "大型蒸汽输入仓", LargeSteamHatchPartMachine::new)
            .allRotation()
            .abilities(PartAbility.STEAM)
            .renderer(() -> new OverlaySteamMachineRenderer(GTCEu.id("block/machine/part/" + "steam_hatch")))
            .tooltips(Component.translatable("gtceu.universal.tooltip.fluid_storage_capacity", 4096000),
                    Component.translatable("gtceu.machine.steam.steam_hatch.tooltip"))
            .register();

    MachineDefinition STERILE_CLEANING_MAINTENANCE_HATCH = machine("sterile_cleaning_maintenance_hatch", "无菌维护仓", holder -> new CMHatchPartMachine(holder, CMHatchPartMachine.STERILE_DUMMY_CLEANROOM))
            .allRotation()
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1"))
            .tooltipBuilder((stack, tooltips) -> {
                for (CleanroomType type : CMHatchPartMachine
                        .getCleanroomTypes(CMHatchPartMachine.STERILE_DUMMY_CLEANROOM)) {
                    tooltips.add(Component.literal(String.format("  %s%s", ChatFormatting.GREEN,
                            Component.translatable(type.getTranslationKey()).getString())));
                }
            })
            .renderer(() -> new MaintenanceHatchPartRenderer(7, GTOCore.id("block/machine/part/maintenance.sterile_cleaning")))
            .register();

    MachineDefinition LAW_CLEANING_MAINTENANCE_HATCH = machine("law_cleaning_maintenance_hatch", "绝对洁净维护仓", holder -> new CMHatchPartMachine(holder, CMHatchPartMachine.LAW_DUMMY_CLEANROOM))
            .allRotation()
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1"))
            .tooltipBuilder((stack, tooltips) -> {
                for (CleanroomType type : CMHatchPartMachine
                        .getCleanroomTypes(CMHatchPartMachine.LAW_DUMMY_CLEANROOM)) {
                    tooltips.add(Component.literal(String.format("  %s%s", ChatFormatting.GREEN,
                            Component.translatable(type.getTranslationKey()).getString())));
                }
            })
            .renderer(() -> new MaintenanceHatchPartRenderer(10, GTOCore.id("block/machine/part/maintenance.law_cleaning")))
            .register();

    MachineDefinition AUTO_CONFIGURATION_MAINTENANCE_HATCH = machine("auto_configuration_maintenance_hatch", "可配置自动维护仓", ACMHatchPartMachine::new)
            .allRotation()
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"))
            .renderer(() -> new MaintenanceHatchPartRenderer(5, GTCEu.id("block/machine/part/maintenance.full_auto")))
            .register();

    MachineDefinition CLEANING_CONFIGURATION_MAINTENANCE_HATCH = machine("cleaning_configuration_maintenance_hatch", "超净可配置维护仓", holder -> new CCMHatchPartMachine(holder, CMHatchPartMachine.DUMMY_CLEANROOM))
            .allRotation()
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1"))
            .tooltipBuilder((stack, tooltips) -> {
                for (CleanroomType type : CMHatchPartMachine
                        .getCleanroomTypes(CMHatchPartMachine.DUMMY_CLEANROOM)) {
                    tooltips.add(Component.literal(String.format("  %s%s", ChatFormatting.GREEN,
                            Component.translatable(type.getTranslationKey()).getString())));
                }
            })
            .renderer(() -> new MaintenanceHatchPartRenderer(5, GTCEu.id("block/machine/part/maintenance.cleaning")))
            .register();

    MachineDefinition STERILE_CONFIGURATION_CLEANING_MAINTENANCE_HATCH = machine("sterile_configuration_cleaning_maintenance_hatch", "无菌可配置维护仓", holder -> new CCMHatchPartMachine(holder, CMHatchPartMachine.STERILE_DUMMY_CLEANROOM))
            .allRotation()
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1"))
            .tooltipBuilder((stack, tooltips) -> {
                for (CleanroomType type : CMHatchPartMachine
                        .getCleanroomTypes(CMHatchPartMachine.STERILE_DUMMY_CLEANROOM)) {
                    tooltips.add(Component.literal(String.format("  %s%s", ChatFormatting.GREEN,
                            Component.translatable(type.getTranslationKey()).getString())));
                }
            })
            .renderer(() -> new MaintenanceHatchPartRenderer(9, GTOCore.id("block/machine/part/maintenance.sterile_cleaning")))
            .register();

    MachineDefinition LAW_CONFIGURATION_CLEANING_MAINTENANCE_HATCH = machine("law_configuration_cleaning_maintenance_hatch", "绝对洁净可配置维护仓", holder -> new CCMHatchPartMachine(holder, CMHatchPartMachine.LAW_DUMMY_CLEANROOM))
            .allRotation()
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.0"),
                    Component.translatable("gtceu.machine.maintenance_hatch_cleanroom_auto.tooltip.1"))
            .tooltipBuilder((stack, tooltips) -> {
                for (CleanroomType type : CMHatchPartMachine
                        .getCleanroomTypes(CMHatchPartMachine.LAW_DUMMY_CLEANROOM)) {
                    tooltips.add(Component.literal(String.format("  %s%s", ChatFormatting.GREEN,
                            Component.translatable(type.getTranslationKey()).getString())));
                }
            })
            .renderer(() -> new MaintenanceHatchPartRenderer(12, GTOCore.id("block/machine/part/maintenance.law_cleaning")))
            .register();

    MachineDefinition GRAVITY_HATCH = machine("gravity_hatch", "重力控制仓", GravityHatchPartMachine::new)
            .allRotation()
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"))
            .renderer(() -> new MaintenanceHatchPartRenderer(8, GTCEu.id("block/machine/part/maintenance.full_auto")))
            .register();

    MachineDefinition GRAVITY_CONFIGURATION_HATCH = machine("gravity_configuration_hatch", "可配置重力维护仓", CGCHatchPartMachine::new)
            .allRotation()
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"))
            .renderer(() -> new MaintenanceHatchPartRenderer(10, GTCEu.id("block/machine/part/maintenance.full_auto")))
            .register();

    MachineDefinition VACUUM_HATCH = machine("vacuum_hatch", "真空仓", VacuumHatchPartMachine::new)
            .allRotation()
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtocore.recipe.vacuum.tier", 4))
            .tooltips(Component.translatable("gtceu.universal.disabled"))
            .renderer(() -> new MaintenanceHatchPartRenderer(4, GTCEu.id("block/machine/part/maintenance.full_auto")))
            .register();

    MachineDefinition VACUUM_CONFIGURATION_HATCH = machine("vacuum_configuration_hatch", "可配置真空维护仓", CVCHatchPartMachine::new)
            .allRotation()
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtocore.recipe.vacuum.tier", 4))
            .tooltips(Component.translatable("gtceu.universal.disabled"))
            .renderer(() -> new MaintenanceHatchPartRenderer(6,
                    GTCEu.id("block/machine/part/maintenance.full_auto")))
            .register();

    MachineDefinition NEUTRON_SENSOR = machine("neutron_sensor", "中子传感器", SensorPartMachine::new)
            .tier(IV)
            .allRotation()
            .tooltips(Component.translatable("gtocore.machine.neutron_sensor.tooltip.0"))
            .overlayTieredHullRenderer("neutron_sensor")
            .register();

    MachineDefinition PH_SENSOR = machine("ph_sensor", "pH 传感器", SensorPartMachine::new)
            .langValue("pH Sensor")
            .tier(EV)
            .allRotation()
            .overlayTieredHullRenderer("neutron_sensor")
            .register();

    MachineDefinition HEAT_SENSOR = machine("heat_sensor", "热传感器", SensorPartMachine::new)
            .tier(LV)
            .allRotation()
            .overlayTieredHullRenderer("neutron_sensor")
            .register();

    MachineDefinition GRIND_BALL_HATCH = machine("grind_ball_hatch", "研磨球仓", BallHatchPartMachine::new)
            .tier(IV)
            .allRotation()
            .renderer(BallHatchRenderer::new)
            .register();

    MachineDefinition RADIATION_HATCH = machine("radiation_hatch", "放射仓", RadiationHatchPartMachine::new)
            .tier(ZPM)
            .recipeType(GTORecipeTypes.RADIATION_HATCH_RECIPES)
            .allRotation()
            .overlayTieredHullRenderer("radiation_hatch")
            .register();

    MachineDefinition SPOOL_HATCH = machine("spool_hatch", "线轴仓", SpoolHatchPartMachine::new)
            .tier(IV)
            .allRotation()
            .overlayTieredHullRenderer("radiation_hatch")
            .register();

    MachineDefinition ROTOR_HATCH = machine("rotor_hatch", "转子仓", h -> new ItemHatchPartMachine(h, 1, i -> TurbineRotorBehaviour.getBehaviour(i) != null))
            .tier(EV)
            .allRotation()
            .overlayTieredHullRenderer("rotor_hatch")
            .register();

    MachineDefinition PRIMITIVE_BLAST_FURNACE_HATCH = machine("primitive_blast_furnace_hatch", "土高炉仓", PrimitiveBlastFurnaceHatch::new)
            .allRotation()
            .modelRenderer(() -> GTCEu.id("block/machine/part/primitive_blast_furnace_hatch"))
            .register();

    MachineDefinition BLOCK_BUS = machine("block_bus", "方块总线", BlockBusPartMachine::new)
            .tier(LuV)
            .allRotation()
            .renderer(() -> new OverlayTieredMachineRenderer(LuV, GTCEu.id("block/machine/part/item_bus.import")))
            .register();

    MachineDefinition LENS_HOUSING = machine("lens_housing", "透镜仓", h -> new ItemHatchPartMachine(h, 1, i -> ChemicalHelper.getPrefix(i.getItem()) == TagPrefix.lens))
            .tier(EV)
            .allRotation()
            .renderer(() -> new OverlayTieredMachineRenderer(EV, GTCEu.id("block/machine/part/item_bus.import")))
            .register();

    MachineDefinition LENS_INDICATOR_HATCH = machine("lens_indicator_hatch", "透镜指示仓", IndicatorHatchPartMachine::new)
            .tier(HV)
            .allRotation()
            .overlayTieredHullRenderer("neutron_sensor")
            .register();

    MachineDefinition DEGASSING_CONTROL_HATCH = machine("degassing_control_hatch", "脱气控制仓", IndicatorHatchPartMachine::new)
            .tier(LuV)
            .allRotation()
            .overlayTieredHullRenderer("neutron_sensor")
            .register();

    MachineDefinition CATALYST_HATCH = machine("catalyst_hatch", "催化剂仓", CatalystHatchPartMachine::new)
            .tier(IV)
            .allRotation()
            .overlayTieredHullRenderer("catalyst_hatch")
            .register();

    MachineDefinition MACHINE_ACCESS_INTERFACE = machine("machine_access_interface", "机器访问接口", MachineAccessInterfacePartMachine::new)
            .tier(IV)
            .allRotation()
            .renderer(() -> new OverlayTieredMachineRenderer(IV, GTCEu.id("block/machine/part/data_access_hatch")))
            .register();

    MachineDefinition THERMAL_CONDUCTOR_HATCH = machine("thermal_conductor_hatch", "导热剂仓", ThermalConductorHatchPartMachine::new)
            .tier(LuV)
            .allRotation()
            .overlayTieredHullRenderer("neutron_sensor")
            .register();

    MachineDefinition TAG_FILTER_ME_STOCK_BUS_PART_MACHINE = machine("me_tag_filter_stock_bus_part_machine", "ME标签过滤库存输入总线", METagFilterStockBusPartMachine::new)
            .tier(LuV)
            .abilities(PartAbility.IMPORT_ITEMS)
            .allRotation()
            .renderer(() -> new OverlayTieredMachineRenderer(LuV, GTCEu.id("block/machine/part/me_item_bus.import")))
            .tooltips(Component.translatable("gtceu.machine.item_bus.import.tooltip"),
                    Component.translatable("gtceu.machine.me.item_import.tooltip"),
                    Component.translatable("gtceu.machine.me.copy_paste.tooltip"),
                    Component.translatable("gtceu.universal.enabled"))
            .register();

    MachineDefinition ME_DUAL_HATCH_STOCK_PART_MACHINE = machine("me_dual_hatch_stock_part_machine", "ME库存输入总成", MEDualHatchStockPartMachine::new)
            .tier(LuV)
            .allRotation()
            .renderer(() -> new OverlayTieredMachineRenderer(LuV, GTCEu.id("block/machine/part/me_item_bus.import")))
            .tooltips(Component.translatable("gtceu.machine.dual_hatch.import.tooltip"),
                    Component.translatable("gtocore.machine.me_dual_hatch_stock.tooltip.0"),
                    Component.translatable("gtocore.machine.me_dual_hatch_stock.tooltip.1"),
                    Component.translatable("gtceu.machine.me.copy_paste.tooltip"),
                    Component.translatable("gtceu.universal.enabled"))
            .register();

    MachineDefinition ME_PATTERN_BUFFER = machine("me_catalyst_pattern_buffer", "ME催化剂样板总成", MECatalystPatternBufferPartMachine::new)
            .langValue("ME Catalyst Pattern Buffer")
            .tier(UV)
            .allRotation()
            .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS)
            .renderer(() -> new OverlayTieredMachineRenderer(UV, GTCEu.id("block/machine/part/me_pattern_buffer")))
            .register();

    MachineDefinition ME_PROGRAMMABLE_PATTERN_BUFFER = machine("me_programmable_pattern_buffer", "ME可编程样板总成", MEProgrammablePatternBufferPartMachine::new)
            .langValue("ME Programmable Pattern Buffer")
            .tier(ZPM)
            .allRotation()
            .abilities(PartAbility.IMPORT_ITEMS, PartAbility.IMPORT_FLUIDS)
            .renderer(() -> new OverlayTieredMachineRenderer(ZPM, GTCEu.id("block/machine/part/me_pattern_buffer")))
            .register();

    MachineDefinition INFINITE_PARALLEL_HATCH = machine("infinite_parallel_hatch", "无限并行仓", h -> new ParallelHatchPartMachine(h, -1))
            .tier(MAX)
            .allRotation()
            .abilities(PARALLEL_HATCH)
            .tooltips(Component.translatable("gtceu.universal.tooltip.parallel", Integer.MAX_VALUE - 1))
            .workableTieredHullRenderer(GTCEu.id("block/machines/parallel_hatch"))
            .register();
}
