package com.gto.gtocore.common.data;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.machine.SimpleNoEnergyMachine;
import com.gto.gtocore.api.machine.feature.multiblock.ICoilMachine;
import com.gto.gtocore.api.machine.part.GTOPartAbility;
import com.gto.gtocore.api.machine.part.ItemHatchPartMachine;
import com.gto.gtocore.api.pattern.GTOPredicates;
import com.gto.gtocore.api.registries.MultiblockBuilder;
import com.gto.gtocore.client.renderer.machine.BallHatchRenderer;
import com.gto.gtocore.client.renderer.machine.WindMillTurbineRenderer;
import com.gto.gtocore.common.block.BlockMap;
import com.gto.gtocore.common.data.machines.*;
import com.gto.gtocore.common.machine.electric.VacuumPumpMachine;
import com.gto.gtocore.common.machine.generator.LightningRodMachine;
import com.gto.gtocore.common.machine.generator.MagicEnergyMachine;
import com.gto.gtocore.common.machine.generator.WindMillTurbineMachine;
import com.gto.gtocore.common.machine.multiblock.generator.CombustionEngineMachine;
import com.gto.gtocore.common.machine.multiblock.generator.GeneratorArrayMachine;
import com.gto.gtocore.common.machine.multiblock.generator.TurbineMachine;
import com.gto.gtocore.common.machine.multiblock.part.*;
import com.gto.gtocore.common.machine.multiblock.part.maintenance.*;
import com.gto.gtocore.common.machine.steam.SteamVacuumPumpMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.*;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.machine.steam.SimpleSteamMachine;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.api.registry.registrate.MachineBuilder;
import com.gregtechceu.gtceu.api.registry.registrate.MultiblockMachineBuilder;
import com.gregtechceu.gtceu.client.renderer.machine.*;
import com.gregtechceu.gtceu.client.util.TooltipHelper;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.common.item.TurbineRotorBehaviour;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DataAccessHatchMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.EnergyHatchPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.LaserHatchPartMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import com.hepdd.gtmthings.GTMThings;
import com.hepdd.gtmthings.data.CreativeMachines;
import com.hepdd.gtmthings.data.CreativeModeTabs;
import com.hepdd.gtmthings.data.CustomMachines;
import com.hepdd.gtmthings.data.WirelessMachines;
import com.lowdragmc.lowdraglib.side.fluid.FluidHelper;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gto.gtocore.api.registries.GTORegistration.REGISTRATE;

@SuppressWarnings("unused")
public final class GTOMachines {

    public static final BiConsumer<ItemStack, List<Component>> GTO_MODIFY = (stack, components) -> components.add(Component.translatable("gtocore.registry.modify").withStyle(style -> style.withColor(TooltipHelper.RAINBOW.getCurrent())));

    public static final BiConsumer<IMultiController, List<Component>> CHEMICAL_PLANT_DISPLAY = (controller, components) -> {
        double value = 1 - ((ICoilMachine) controller).getCoilTier() * 0.05;
        components.add(Component.translatable("gtocore.machine.eut_multiplier.tooltip", FormattingUtil.formatNumbers(value * 0.8)));
        components.add(Component.translatable("gtocore.machine.duration_multiplier.tooltip", FormattingUtil.formatNumbers(value * 0.6)));
    };

    static {
        REGISTRATE.creativeModeTab(() -> GTOCreativeModeTabs.GTO_MACHINE);
    }

    public static void init() {
        BlockMap.init();
        CreativeModeTabs.init();
        CreativeMachines.init();
        WirelessMachines.init();
        CustomMachines.init();
        ManaMachine.init();
        GeneratorMultiblockMachine.init();
        MultiBlockMachineA.init();
        MultiBlockMachineB.init();
        MultiBlockMachineC.init();
        MultiBlockMachineD.init();
        MultiBlockMachineE.init();
        MultiBlockMachineF.init();
        MultiBlockMachineG.init();
    }

    private static MachineBuilder<MachineDefinition> machine(String name, String cn, Function<IMachineBlockEntity, MetaMachine> metaMachine) {
        GTOBlocks.addLang(name, cn);
        return REGISTRATE.machine(name, metaMachine);
    }

    public static MultiblockBuilder multiblock(String name, String cn, Function<IMachineBlockEntity, ? extends MultiblockControllerMachine> metaMachine) {
        GTOBlocks.addLang(name, cn);
        return REGISTRATE.multiblock(name, metaMachine);
    }

    //////////////////////////////////////
    // *** Simple Machine ***//
    //////////////////////////////////////
    public static final Pair<MachineDefinition, MachineDefinition> STEAM_VACUUM_PUMP = registerSteamMachines("steam_vacuum_pump", "真空泵", SteamVacuumPumpMachine::new, (pressure, builder) -> builder
            .rotationState(RotationState.ALL)
            .recipeType(GTORecipeTypes.VACUUM_PUMP_RECIPES)
            .recipeModifier(SimpleSteamMachine::recipeModifier)
            .tooltips(Component.translatable("gtocore.recipe.vacuum.tier", pressure ? 2 : 1))
            .renderer(() -> new WorkableSteamMachineRenderer(pressure, GTOCore.id("block/machines/vacuum_pump")))
            .register());

    public static final MachineDefinition[] SEMI_FLUID_GENERATOR = registerSimpleGenerator("semi_fluid", "半流质发电机",
            GTORecipeTypes.SEMI_FLUID_GENERATOR_FUELS, t -> 6400, ULV, LV, MV, HV);

    public static final MachineDefinition[] THERMAL_GENERATOR = registerSimpleGenerator("thermal_generator", "热力发电机",
            GTORecipeTypes.THERMAL_GENERATOR_FUELS, tier -> (tier + 1) * 1000, ULV, LV, MV, HV);

    public static final MachineDefinition[] ROCKET_ENGINE_GENERATOR = registerSimpleGenerator("rocket_engine", "火箭引擎", GTORecipeTypes.ROCKET_ENGINE_FUELS,
            GTMachineUtils.genericGeneratorTankSizeFunction, EV, IV, LuV);

    public static final MachineDefinition[] NAQUADAH_REACTOR_GENERATOR = registerSimpleGenerator("naquadah_reactor", "硅岩反应堆", GTORecipeTypes.NAQUADAH_REACTOR,
            GTMachineUtils.genericGeneratorTankSizeFunction, IV, LuV, ZPM);

    public static final MachineDefinition[] ARC_GENERATOR = registerSimpleMachines("arc_generator", "电弧发生器", GTORecipeTypes.ARC_GENERATOR_RECIPES, GTMachineUtils.defaultTankSizeFunction);

    public static final MachineDefinition[] DEHYDRATOR = registerSimpleMachines("dehydrator", "脱水机", GTORecipeTypes.DEHYDRATOR_RECIPES, GTMachineUtils.defaultTankSizeFunction);

    public static final MachineDefinition[] UNPACKER = registerSimpleMachines("unpacker", "解包机", GTORecipeTypes.UNPACKER_RECIPES, GTMachineUtils.defaultTankSizeFunction);

    public static final MachineDefinition[] CLUSTER = registerSimpleMachines("cluster", "多辊式轧机", GTORecipeTypes.CLUSTER_RECIPES, GTMachineUtils.defaultTankSizeFunction);

    public static final MachineDefinition[] ROLLING = registerSimpleMachines("rolling", "辊压机", GTORecipeTypes.ROLLING_RECIPES, GTMachineUtils.defaultTankSizeFunction);

    public static final MachineDefinition[] LAMINATOR = registerSimpleMachines("laminator", "过胶机", GTORecipeTypes.LAMINATOR_RECIPES, GTMachineUtils.defaultTankSizeFunction);

    public static final MachineDefinition[] LOOM = registerSimpleMachines("loom", "织布机", GTORecipeTypes.LOOM_RECIPES, GTMachineUtils.defaultTankSizeFunction);

    public static final MachineDefinition[] LASER_WELDER = registerSimpleMachines("laser_welder", "激光焊接器", GTORecipeTypes.LASER_WELDER_RECIPES, GTMachineUtils.defaultTankSizeFunction);

    public static final MachineDefinition[] WORLD_DATA_SCANNER = registerSimpleMachines("world_data_scanner", "世界信息扫描仪",
            GTORecipeTypes.WORLD_DATA_SCANNER_RECIPES, tier -> 64000);

    public static final MachineDefinition[] NEUTRON_COMPRESSOR = registerSimpleMachines("neutron_compressor", "中子压缩机",
            GTORecipeTypes.NEUTRON_COMPRESSOR_RECIPES, GTMachineUtils.defaultTankSizeFunction, MAX);

    public static final MachineDefinition[] ULV_FLUID_SOLIDIFIER = registerSimpleMachines("fluid_solidifier", "流体固化器", GTRecipeTypes.FLUID_SOLIDFICATION_RECIPES, GTMachineUtils.defaultTankSizeFunction, GTCEu.id("block/machines/fluid_solidifier"), ULV);
    public static final MachineDefinition[] ULV_CHEMICAL_REACTOR = registerSimpleMachines("chemical_reactor", "化学反应釜", GTRecipeTypes.CHEMICAL_RECIPES, GTMachineUtils.defaultTankSizeFunction, GTCEu.id("block/machines/chemical_reactor"), ULV);
    public static final MachineDefinition[] ULV_ASSEMBLER = registerSimpleMachines("assembler", "组装机", GTRecipeTypes.ASSEMBLER_RECIPES, GTMachineUtils.defaultTankSizeFunction, GTCEu.id("block/machines/assembler"), ULV);
    public static final MachineDefinition[] ULV_PACKER = registerSimpleMachines("packer", "打包机", GTRecipeTypes.PACKER_RECIPES, GTMachineUtils.defaultTankSizeFunction, GTCEu.id("block/machines/packer"), ULV);
    public static final MachineDefinition[] ULV_UNPACKER = registerSimpleMachines("unpacker", "解包机", GTORecipeTypes.UNPACKER_RECIPES, GTMachineUtils.defaultTankSizeFunction, ULV);

    public static final MachineDefinition[] VACUUM_PUMP = registerTieredMachines("vacuum_pump", tier -> "%s真空泵 %s".formatted(GTOValues.VLVHCN[tier], VLVT[tier]), VacuumPumpMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Vacuum Pump %s".formatted(VLVH[tier], VLVT[tier]))
                    .rotationState(RotationState.NON_Y_AXIS)
                    .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id("vacuum_pump"), GTORecipeTypes.VACUUM_PUMP_RECIPES))
                    .alwaysTryModifyRecipe(true)
                    .recipeType(GTORecipeTypes.VACUUM_PUMP_RECIPES)
                    .workableTieredHullRenderer(GTOCore.id("block/machines/vacuum_pump"))
                    .tooltips(Component.translatable("gtocore.recipe.vacuum.tier", tier + 1))
                    .tooltips(GTMachineUtils.workableTiered(tier, V[tier], V[tier] << 6, GTORecipeTypes.VACUUM_PUMP_RECIPES, GTMachineUtils.defaultTankSizeFunction.apply(tier), true))
                    .register(),
            LV, MV, HV);

    public static final MachineDefinition[] LIGHTNING_ROD = registerTieredMachines(
            "lightning_rod", tier -> "%s避雷针 %s".formatted(GTOValues.VLVHCN[tier], VLVT[tier]),
            LightningRodMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Lightning Rod %s".formatted(VLVH[tier], VLVT[tier]))
                    .rotationState(RotationState.NON_Y_AXIS)
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

    public static final MachineDefinition[] PRIMITIVE_MAGIC_ENERGY = registerTieredMachines(
            "primitive_magic_energy", tier -> "%s原始魔法能源吸收器 %s".formatted(GTOValues.VLVHCN[tier], VLVT[tier]),
            MagicEnergyMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Primitive Magic Energy %s".formatted(VLVH[tier], VLVT[tier]))
                    .rotationState(RotationState.NON_Y_AXIS)
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

    public static final MachineDefinition[] WIND_MILL_TURBINE = registerTieredMachines(
            "wind_mill_turbine", tier -> "%s风力发电机 %s".formatted(GTOValues.VLVHCN[tier], VLVT[tier]),
            WindMillTurbineMachine::new,
            (tier, builder) -> builder
                    .langValue("%s Wind Mill Turbine %s".formatted(VLVH[tier], VLVT[tier]))
                    .rotationState(RotationState.NON_Y_AXIS)
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

    public static final MachineDefinition WIRELESS_DATA_HATCH_TRANSMITTER = machine("wireless_data_transmitter_hatch", "无线光学数据源仓", (holder) -> new WirelessOpticalDataHatchMachine(holder, true))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.OPTICAL_DATA_TRANSMISSION)
            .renderer(() -> new OverlayTieredMachineRenderer(UV, GTCEu.id("block/machine/part/optical_data_hatch")))
            .tooltips(Component.translatable("gtceu.machine.data_transmitter_hatch.tooltip"), Component.translatable("gtocore.machine.wireless_data_transmitter_hatch.tooltip.0"))
            .tier(UV)
            .register();

    public static final MachineDefinition WIRELESS_DATA_HATCH_RECEIVER = machine("wireless_data_receiver_hatch", "无线光学数据靶仓", (holder) -> new WirelessOpticalDataHatchMachine(holder, false))
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.OPTICAL_DATA_RECEPTION)
            .renderer(() -> new OverlayTieredMachineRenderer(UV, GTCEu.id("block/machine/part/optical_data_hatch")))
            .tooltips(Component.translatable("gtceu.machine.data_receiver_hatch.tooltip"), Component.translatable("gtocore.machine.wireless_data_receiver_hatch.tooltip.0"))
            .tier(UV)
            .register();

    private static MachineDefinition[] registerHugeFluidHatches(String name, String displayname, String cn, String model,
                                                                String tooltip, IO io, PartAbility... abilities) {
        return registerTieredMachines(name, tier -> GTOValues.VNFR[tier] + cn,
                (holder, tier) -> new HugeFluidHatchPartMachine(holder, tier, io),
                (tier, builder) -> {
                    builder.langValue(VNF[tier] + ' ' + displayname)
                            .rotationState(RotationState.ALL)
                            .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + model)))
                            .abilities(abilities)
                            .tooltips(Component.translatable("gtceu.machine." + tooltip + ".tooltip"));
                    builder.tooltips(Component.translatable("gtceu.universal.tooltip.fluid_storage_capacity_mult",
                            tier, FormattingUtil.formatNumbers(Integer.MAX_VALUE)));
                    return builder.register();
                },
                tiersBetween(LV, OpV));
    }

    private static MachineDefinition[] registerWirelessEnergyHatch(IO io, int amperage, PartAbility ability) {
        String id = io == IO.IN ? "input" : "output";
        String render = "wireless_energy_hatch";
        render = switch (amperage) {
            case 2 -> render;
            case 4 -> render + "_4a";
            case 16 -> render + "_16a";
            case 64 -> render + "_64a";
            default -> "wireless_laser_hatch.target";
        };
        String finalRender = render;
        int t = LV;
        if (amperage == 64) t = EV;
        else if (amperage > 64) t = IV;
        return registerTieredMachines(amperage + "a_wireless_" + id + "_hatch", tier -> amperage + (amperage > 64 ? "§e安§r" : "安") + GTOValues.VNFR[tier] + "无线" + (io == IO.IN ? "能源" : "动力") + "仓",
                (holder, tier) -> new WirelessEnergyHatchPartMachine(holder, tier, io, amperage), (tier, builder) -> builder
                        .langValue(VNF[tier] + " " + FormattingUtil.formatNumbers(amperage) + "A Wireless" + (io == IO.IN ? "Energy" : "Dynamo") + " Hatch")
                        .rotationState(RotationState.ALL)
                        .abilities(ability)
                        .tooltips(Component.translatable("gtmthings.machine.energy_hatch." + id + ".tooltip"), (Component.translatable("gtmthings.machine.wireless_energy_hatch." + id + ".tooltip")))
                        .renderer(() -> new OverlayTieredMachineRenderer(tier, GTMThings.id("block/machine/part/" + finalRender)))
                        .register(),
                tiersBetween(t, MAX));
    }

    private static MachineDefinition[] registerLaserHatch(IO io, int amperage, PartAbility ability) {
        String name = io == IO.IN ? "target" : "source";
        return registerTieredMachines(amperage + "a_laser_" + name + "_hatch", tier -> amperage + "§e安§r" + GTOValues.VNFR[tier] + "激光" + (io == IO.IN ? "靶" : "源") + "仓",
                (holder, tier) -> new LaserHatchPartMachine(holder, io, tier, amperage), (tier, builder) -> builder
                        .langValue(VNF[tier] + " " + FormattingUtil.formatNumbers(amperage) + "A Laser " + FormattingUtil.toEnglishName(name) + " Hatch")
                        .rotationState(RotationState.ALL)
                        .tooltips(Component.translatable("gtceu.machine.laser_hatch." + name + ".tooltip"),
                                Component.translatable("gtceu.machine.laser_hatch.both.tooltip"),
                                Component.translatable("gtceu.universal.disabled"))
                        .abilities(ability)
                        .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/laser_hatch." + name)))
                        .register(),
                tiersBetween(IV, MAX));
    }

    private static MachineDefinition[] registerSimpleGenerator(String name, String cn, GTRecipeType recipeType, Int2IntFunction tankScalingFunction, int... tiers) {
        return registerTieredMachines(name, tier -> "%s%s %s".formatted(GTOValues.VLVHCN[tier], cn, VLVT[tier]),
                (holder, tier) -> new SimpleGeneratorMachine(holder, tier, 0.1F * tier, tankScalingFunction),
                (tier, builder) -> builder
                        .langValue("%s %s %s".formatted(VLVH[tier], FormattingUtil.toEnglishName(name), VLVT[tier]))
                        .editableUI(SimpleGeneratorMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id(name), recipeType))
                        .rotationState(RotationState.ALL)
                        .recipeType(recipeType)
                        .recipeModifier(GTORecipeModifiers::simpleGeneratorMachineModifier)
                        .addOutputLimit(ItemRecipeCapability.CAP, 0)
                        .addOutputLimit(FluidRecipeCapability.CAP, 0)
                        .renderer(() -> new SimpleGeneratorMachineRenderer(tier, GTOCore.id("block/generators/" + name)))
                        .tooltips(Component.translatable("gtocore.machine.efficiency.tooltip", GeneratorArrayMachine.getEfficiency(recipeType, tier)).append("%"))
                        .tooltips(Component.translatable("gtocore.universal.tooltip.ampere_out", GeneratorArrayMachine.getAmperage(tier)))
                        .tooltips(GTMachineUtils.workableTiered(tier, V[tier], V[tier] * 64 * GeneratorArrayMachine.getAmperage(tier), recipeType, tankScalingFunction.apply(tier), false))
                        .register(),
                tiers);
    }

    private static MachineDefinition[] registerSimpleMachines(String name, String cn, GTRecipeType recipeType,
                                                              Int2IntFunction tankScalingFunction) {
        return registerSimpleMachines(name, cn, recipeType, tankScalingFunction, GTMachineUtils.ELECTRIC_TIERS);
    }

    private static MachineDefinition[] registerSimpleMachines(String name, String cn,
                                                              GTRecipeType recipeType,
                                                              Int2IntFunction tankScalingFunction,
                                                              int... tiers) {
        return registerSimpleMachines(name, cn, recipeType, tankScalingFunction, GTOCore.id("block/machines/" + name), tiers);
    }

    private static MachineDefinition[] registerSimpleMachines(String name, String cn,
                                                              GTRecipeType recipeType,
                                                              Int2IntFunction tankScalingFunction,
                                                              ResourceLocation workableModel, int... tiers) {
        return registerTieredMachines(name, tier -> "%s%s %s".formatted(GTOValues.VLVHCN[tier], cn, VLVT[tier]),
                (holder, tier) -> new SimpleTieredMachine(holder, tier, tankScalingFunction), (tier, builder) -> {
                    builder.recipeModifier(GTRecipeModifiers.ELECTRIC_OVERCLOCK.apply(OverclockingLogic.NON_PERFECT_OVERCLOCK));
                    return builder
                            .langValue("%s %s %s".formatted(VLVH[tier], FormattingUtil.toEnglishName(name), VLVT[tier]))
                            .editableUI(SimpleTieredMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id(name), recipeType))
                            .rotationState(RotationState.NON_Y_AXIS)
                            .recipeType(recipeType)
                            .workableTieredHullRenderer(workableModel)
                            .tooltips(GTMachineUtils.workableTiered(tier, V[tier], V[tier] << 6, recipeType,
                                    tankScalingFunction.apply(tier), true))
                            .register();
                },
                tiers);
    }

    private static MachineDefinition[] registerSimpleNoEnergyMachines(String name, String cn,
                                                                      GTRecipeType recipeType,
                                                                      Int2IntFunction tankScalingFunction,
                                                                      int... tiers) {
        return registerSimpleNoEnergyMachines(name, cn, recipeType, tankScalingFunction, GTOCore.id("block/machines/" + name), tiers);
    }

    private static MachineDefinition[] registerSimpleNoEnergyMachines(String name, String cn,
                                                                      GTRecipeType recipeType,
                                                                      Int2IntFunction tankScalingFunction,
                                                                      ResourceLocation workableModel, int... tiers) {
        return registerTieredMachines(name, tier -> "%s%s %s".formatted(GTOValues.VLVHCN[tier], cn, VLVT[tier]),
                (holder, tier) -> new SimpleNoEnergyMachine(holder, tier, tankScalingFunction), (tier, builder) -> {
                    builder.noRecipeModifier();
                    return builder
                            .langValue("%s %s %s".formatted(VLVH[tier], FormattingUtil.toEnglishName(name), VLVT[tier]))
                            .editableUI(SimpleNoEnergyMachine.EDITABLE_UI_CREATOR.apply(GTCEu.id(name), recipeType))
                            .rotationState(RotationState.NON_Y_AXIS)
                            .recipeType(recipeType)
                            .workableTieredHullRenderer(workableModel)
                            .tooltips(workableNoEnergy(recipeType, tankScalingFunction.apply(tier)))
                            .register();
                },
                tiers);
    }

    public static Component[] workableNoEnergy(GTRecipeType recipeType, long tankCapacity) {
        List<Component> tooltipComponents = new ArrayList<>();
        if (recipeType.getMaxInputs(FluidRecipeCapability.CAP) > 0 ||
                recipeType.getMaxOutputs(FluidRecipeCapability.CAP) > 0)
            tooltipComponents
                    .add(Component.translatable("gtceu.universal.tooltip.fluid_storage_capacity",
                            FormattingUtil.formatNumbers(tankCapacity)));
        return tooltipComponents.toArray(Component[]::new);
    }

    private static MachineDefinition[] registerTieredMachines(String name, Function<Integer, String> cn,
                                                              BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory,
                                                              BiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builder,
                                                              int... tiers) {
        return registerMachineDefinitions(name, cn, factory, builder, REGISTRATE, tiers);
    }

    public static MachineDefinition[] registerMachineDefinitions(String name, Function<Integer, String> cn, BiFunction<IMachineBlockEntity, Integer, MetaMachine> factory, BiFunction<Integer, MachineBuilder<MachineDefinition>, MachineDefinition> builder, GTRegistrate registrate, int[] tiers) {
        MachineDefinition[] definitions = new MachineDefinition[TIER_COUNT];
        for (int tier : tiers) {
            String n = VN[tier].toLowerCase(Locale.ROOT) + "_" + name;
            if (cn != null) GTOBlocks.addLang(n, cn.apply(tier));
            MachineBuilder<MachineDefinition> register = registrate.machine(n, holder -> factory.apply(holder, tier)).tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }

    private static Pair<MachineDefinition, MachineDefinition> registerSteamMachines(String name, String cn,
                                                                                    BiFunction<IMachineBlockEntity, Boolean, MetaMachine> factory,
                                                                                    BiFunction<Boolean, MachineBuilder<MachineDefinition>, MachineDefinition> builder) {
        MachineDefinition lowTier = builder.apply(false,
                machine("lp_%s".formatted(name), "低压蒸汽%s".formatted(cn), holder -> factory.apply(holder, false))
                        .langValue("Low Pressure " + FormattingUtil.toEnglishName(name))
                        .tier(0));
        MachineDefinition highTier = builder.apply(true,
                machine("hp_%s".formatted(name), "高压蒸汽%s".formatted(cn), holder -> factory.apply(holder, true))
                        .langValue("High Pressure " + FormattingUtil.toEnglishName(name))
                        .tier(1));
        return Pair.of(lowTier, highTier);
    }

    public static MultiblockMachineDefinition[] registerTieredMultis(String name, Function<Integer, String> cn,
                                                                     BiFunction<IMachineBlockEntity, Integer, MultiblockControllerMachine> factory,
                                                                     BiFunction<Integer, MultiblockBuilder, MultiblockMachineDefinition> builder,
                                                                     int... tiers) {
        MultiblockMachineDefinition[] definitions = new MultiblockMachineDefinition[TIER_COUNT];
        for (int tier : tiers) {
            MultiblockBuilder register = multiblock(VN[tier].toLowerCase(Locale.ROOT) + "_" + name, cn.apply(tier), holder -> factory.apply(holder, tier)).tier(tier);
            definitions[tier] = builder.apply(tier, register);
        }
        return definitions;
    }

    public static MultiblockMachineDefinition registerLargeCombustionEngine(GTRegistrate registrate, String name, String cn,
                                                                            int tier, GTRecipeType recipeType,
                                                                            Supplier<? extends Block> casing,
                                                                            Supplier<? extends Block> gear,
                                                                            Supplier<? extends Block> intake,
                                                                            ResourceLocation casingTexture,
                                                                            ResourceLocation overlayModel, boolean isGTM) {
        if (!isGTM) GTOBlocks.addLang(name, cn);
        MultiblockMachineBuilder builder = registrate.multiblock(name, holder -> new CombustionEngineMachine(holder, tier))
                .rotationState(RotationState.ALL)
                .recipeType(recipeType)
                .generator(true)
                .alwaysTryModifyRecipe(true)
                .appearanceBlock(casing)
                .pattern(definition -> FactoryBlockPattern.start()
                        .aisle("XXX", "XDX", "XXX")
                        .aisle("XCX", "CGC", "XCX")
                        .aisle("XCX", "CGC", "XCX")
                        .aisle("AAA", "AYA", "AAA")
                        .where('X', Predicates.blocks(casing.get()))
                        .where('G', Predicates.blocks(gear.get()))
                        .where('C', Predicates.blocks(casing.get()).setMinGlobalLimited(3).or(Predicates.autoAbilities(definition.getRecipeTypes(), false, false, true, true, true, true)).or(Predicates.autoAbilities(true, true, false)))
                        .where('D', Predicates.ability(PartAbility.OUTPUT_ENERGY, Stream.of(EV, IV, LuV, ZPM, UV, UHV).filter(t -> t >= tier).mapToInt(Integer::intValue).toArray()).addTooltips(Component.translatable("gtceu.machine.large_combustion_engine.tooltip.boost_regular", VN[tier])))
                        .where('A', Predicates.blocks(intake.get()).addTooltips(Component.translatable("gtceu.multiblock.pattern.clear_amount_1")))
                        .where('Y', Predicates.controller(Predicates.blocks(definition.getBlock())))
                        .build())
                .workableCasingRenderer(casingTexture, overlayModel)
                .tooltips(Component.translatable("gtceu.universal.tooltip.base_production_eut", V[tier] << 1), Component.translatable("gtceu.universal.tooltip.uses_per_hour_lubricant", FluidHelper.getBucket()), tier > EV ? Component.translatable("gtceu.machine.large_combustion_engine.tooltip.boost_extreme", V[tier] << 3) : Component.translatable("gtceu.machine.large_combustion_engine.tooltip.boost_regular", V[tier] * 6));
        if (isGTM) builder.tooltipBuilder(GTO_MODIFY);
        return builder.register();
    }

    public static MultiblockMachineDefinition registerLargeTurbine(GTRegistrate registrate, String name, String cn, int tier, boolean special, GTRecipeType recipeType, Supplier<? extends Block> casing, Supplier<? extends Block> gear, ResourceLocation casingTexture, ResourceLocation overlayModel, boolean isGTM) {
        if (Objects.equals(name, "plasma_large_turbine")) {
            DUMMY_MULTIBLOCK.setItemSupplier(MultiBlockMachineA.VOID_MINER::getItem);
            return DUMMY_MULTIBLOCK;
        }
        if (!isGTM) GTOBlocks.addLang(name, cn);
        MultiblockMachineBuilder builder = registrate.multiblock(name, holder -> new TurbineMachine(holder, tier, special, false))
                .rotationState(RotationState.ALL)
                .recipeType(recipeType)
                .generator(true)
                .alwaysTryModifyRecipe(true)
                .appearanceBlock(casing)
                .pattern(definition -> FactoryBlockPattern.start()
                        .aisle("CCCC", "CHHC", "CCCC")
                        .aisle("CHHC", "RGGR", "CHHC")
                        .aisle("CCCC", "CSHC", "CCCC")
                        .where('S', Predicates.controller(Predicates.blocks(definition.getBlock())))
                        .where('G', Predicates.blocks(gear.get()))
                        .where('C', Predicates.blocks(casing.get()))
                        .where('R', GTOPredicates.RotorBlock(tier).setExactLimit(1)
                                .or(Predicates.abilities(PartAbility.OUTPUT_ENERGY)).setExactLimit(1))
                        .where('H', Predicates.blocks(casing.get()).or(Predicates.autoAbilities(definition.getRecipeTypes(), false, false, true, true, true, true).or(Predicates.autoAbilities(true, true, false))))
                        .build())
                .workableCasingRenderer(casingTexture, overlayModel)
                .tooltips(Component.translatable("gtceu.universal.tooltip.base_production_eut", (int) (V[tier] * (special ? 2.5 : 2))), Component.translatable("gtceu.multiblock.turbine.efficiency_tooltip", VNF[tier]));
        if (isGTM) builder.tooltipBuilder(GTO_MODIFY);
        return builder.register();
    }

    private static final MultiblockMachineDefinition DUMMY_MULTIBLOCK = MultiblockMachineDefinition.createDefinition(GTOCore.id("dummy"));

    //////////////////////////////////////
    // ********** Part **********//
    //////////////////////////////////////
    public static final MachineDefinition[] THREAD_HATCH = registerTieredMachines("thread_hatch", tier -> GTOValues.VNFR[tier] + "线程仓",
            ThreadHatchPartMachine::new, (tier, builder) -> builder
                    .langValue(VNF[tier] + " Thread Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(GTOPartAbility.THREAD_HATCH)
                    .workableTieredHullRenderer(GTOCore.id("block/machines/thread_hatch"))
                    .tooltips(Component.translatable("gtocore.machine.thread_hatch.tooltip.0", 1 << (tier - ZPM)))
                    .register(),
            UHV, UEV, UIV, UXV, OpV, MAX);

    public static final MachineDefinition[] ACCELERATE_HATCH = registerTieredMachines("accelerate_hatch", tier -> GTOValues.VNFR[tier] + "加速仓",
            AccelerateHatchPartMachine::new, (tier, builder) -> builder
                    .langValue(VNF[tier] + " Accelerate Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(GTOPartAbility.ACCELERATE_HATCH)
                    .tooltips(Component.translatable("gtocore.machine.accelerate_hatch.tooltip.0"))
                    .tooltips(Component.translatable("gtocore.machine.accelerate_hatch.tooltip.1"))
                    .workableTieredHullRenderer(GTOCore.id("block/machines/accelerate_hatch"))
                    .register(),
            tiersBetween(LV, MAX));

    public static final MachineDefinition[] ENERGY_INPUT_HATCH_4A = registerTieredMachines("energy_input_hatch_4a", tier -> 4 + "安" + GTOValues.VNFR[tier] + "能源仓",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IO.IN, 4),
            (tier, builder) -> builder
                    .langValue(VNF[tier] + " 4A Energy Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.INPUT_ENERGY)
                    .tooltips(Component.translatable("gtceu.machine.energy_hatch.input_hi_amp.tooltip"))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.input_4a")))
                    .register(),
            tiersBetween(LV, HV));

    public static final MachineDefinition[] ENERGY_OUTPUT_HATCH_4A = registerTieredMachines("energy_output_hatch_4a", tier -> 4 + "安" + GTOValues.VNFR[tier] + "动力仓",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IO.OUT, 4),
            (tier, builder) -> builder
                    .langValue(VNF[tier] + " 4A Dynamo Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.OUTPUT_ENERGY)
                    .tooltips(Component.translatable("gtceu.machine.energy_hatch.output_hi_amp.tooltip"))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.output_4a")))
                    .register(),
            tiersBetween(LV, HV));

    public static final MachineDefinition[] ENERGY_INPUT_HATCH_16A = registerTieredMachines("energy_input_hatch_16a", tier -> 16 + "安" + GTOValues.VNFR[tier] + "能源仓",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IO.IN, 16),
            (tier, builder) -> builder
                    .langValue(VNF[tier] + " 16A Energy Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.INPUT_ENERGY)
                    .tooltips(Component.translatable("gtceu.machine.energy_hatch.input_hi_amp.tooltip"))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.input_16a")))
                    .register(),
            tiersBetween(LV, HV));

    public static final MachineDefinition[] ENERGY_OUTPUT_HATCH_16A = registerTieredMachines("energy_output_hatch_16a", tier -> 16 + "安" + GTOValues.VNFR[tier] + "动力仓",
            (holder, tier) -> new EnergyHatchPartMachine(holder, tier, IO.OUT, 16),
            (tier, builder) -> builder
                    .langValue(VNF[tier] + " 16A Dynamo Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(PartAbility.OUTPUT_ENERGY)
                    .tooltips(Component.translatable("gtceu.machine.energy_hatch.output_hi_amp.tooltip"))
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/" + "energy_hatch.output_16a")))
                    .register(),
            tiersBetween(LV, HV));

    public static final MachineDefinition[] DRONE_HATCH = registerTieredMachines("drone_hatch", tier -> GTOValues.VNFR[tier] + "无人机仓",
            DroneHatchPartMachine::new, (tier, builder) -> builder
                    .langValue(VNF[tier] + " Drone Hatch")
                    .rotationState(RotationState.ALL)
                    .abilities(GTOPartAbility.DRONE_HATCH)
                    .renderer(() -> new OverlayTieredMachineRenderer(tier, GTCEu.id("block/machine/part/item_bus.import")))
                    .register(),
            HV, EV, IV);

    public final static MachineDefinition[] HUGE_FLUID_IMPORT_HATCH = registerHugeFluidHatches("huge_input_hatch", "Huge Input Hatch", "巨型输入仓", "fluid_hatch.import", "fluid_hatch.import", IO.IN, PartAbility.IMPORT_FLUIDS);

    public final static MachineDefinition[] HUGE_FLUID_EXPORT_HATCH = registerHugeFluidHatches("huge_output_hatch", "Huge Output Hatch", "巨型输出仓", "fluid_hatch.export", "fluid_hatch.export", IO.OUT, PartAbility.EXPORT_FLUIDS);

    public static final MachineDefinition[] WIRELESS_INPUT_HATCH_2 = registerWirelessEnergyHatch(IO.IN, 2,
            PartAbility.INPUT_ENERGY);
    public static final MachineDefinition[] WIRELESS_OUTPUT_HATCH_2 = registerWirelessEnergyHatch(IO.OUT, 2,
            PartAbility.OUTPUT_ENERGY);
    public static final MachineDefinition[] WIRELESS_INPUT_HATCH_4 = registerWirelessEnergyHatch(IO.IN, 4,
            PartAbility.INPUT_ENERGY);
    public static final MachineDefinition[] WIRELESS_OUTPUT_HATCH_4 = registerWirelessEnergyHatch(IO.OUT, 4,
            PartAbility.OUTPUT_ENERGY);
    public static final MachineDefinition[] WIRELESS_INPUT_HATCH_16 = registerWirelessEnergyHatch(IO.IN, 16,
            PartAbility.INPUT_ENERGY);
    public static final MachineDefinition[] WIRELESS_OUTPUT_HATCH_16 = registerWirelessEnergyHatch(IO.OUT, 16,
            PartAbility.OUTPUT_ENERGY);
    public static final MachineDefinition[] WIRELESS_INPUT_HATCH_64 = registerWirelessEnergyHatch(IO.IN, 64,
            PartAbility.INPUT_ENERGY);
    public static final MachineDefinition[] WIRELESS_OUTPUT_HATCH_64 = registerWirelessEnergyHatch(IO.OUT, 64,
            PartAbility.OUTPUT_ENERGY);
    public static final MachineDefinition[] WIRELESS_INPUT_HATCH_256 = registerWirelessEnergyHatch(IO.IN, 256,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] WIRELESS_OUTPUT_HATCH_256 = registerWirelessEnergyHatch(IO.OUT, 256,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] WIRELESS_INPUT_HATCH_1024 = registerWirelessEnergyHatch(IO.IN, 1024,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] WIRELESS_OUTPUT_HATCH_1024 = registerWirelessEnergyHatch(IO.OUT, 1024,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] WIRELESS_INPUT_HATCH_4096 = registerWirelessEnergyHatch(IO.IN, 4096,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] WIRELESS_OUTPUT_HATCH_4096 = registerWirelessEnergyHatch(IO.OUT, 4096,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] WIRELESS_INPUT_HATCH_16384 = registerWirelessEnergyHatch(IO.IN, 16384,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] WIRELESS_OUTPUT_HATCH_16384 = registerWirelessEnergyHatch(IO.OUT, 16384,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] WIRELESS_INPUT_HATCH_65536 = registerWirelessEnergyHatch(IO.IN, 65536,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] WIRELESS_OUTPUT_HATCH_65536 = registerWirelessEnergyHatch(IO.OUT, 65536,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] WIRELESS_INPUT_HATCH_262144 = registerWirelessEnergyHatch(IO.IN, 262144,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] WIRELESS_OUTPUT_HATCH_262144 = registerWirelessEnergyHatch(IO.OUT, 262144,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] WIRELESS_INPUT_HATCH_1048576 = registerWirelessEnergyHatch(IO.IN, 1048576,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] WIRELESS_OUTPUT_HATCH_1048576 = registerWirelessEnergyHatch(IO.OUT, 1048576,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] WIRELESS_INPUT_HATCH_4194304 = registerWirelessEnergyHatch(IO.IN, 4194304,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] WIRELESS_OUTPUT_HATCH_4194304 = registerWirelessEnergyHatch(IO.OUT, 4194304,
            PartAbility.OUTPUT_LASER);

    public static final MachineDefinition[] LASER_INPUT_HATCH_16384 = registerLaserHatch(IO.IN, 16384,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] LASER_OUTPUT_HATCH_16384 = registerLaserHatch(IO.OUT, 16384,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] LASER_INPUT_HATCH_65536 = registerLaserHatch(IO.IN, 65536,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] LASER_OUTPUT_HATCH_65536 = registerLaserHatch(IO.OUT, 65536,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] LASER_INPUT_HATCH_262144 = registerLaserHatch(IO.IN, 262144,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] LASER_OUTPUT_HATCH_262144 = registerLaserHatch(IO.OUT, 262144,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] LASER_INPUT_HATCH_1048576 = registerLaserHatch(IO.IN, 1048576,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] LASER_OUTPUT_HATCH_1048576 = registerLaserHatch(IO.OUT, 1048576,
            PartAbility.OUTPUT_LASER);
    public static final MachineDefinition[] LASER_INPUT_HATCH_4194304 = registerLaserHatch(IO.IN, 4194304,
            PartAbility.INPUT_LASER);
    public static final MachineDefinition[] LASER_OUTPUT_HATCH_4194304 = registerLaserHatch(IO.OUT, 4194304,
            PartAbility.OUTPUT_LASER);

    public static final MachineDefinition[] NEUTRON_ACCELERATOR = registerTieredMachines("neutron_accelerator", tier -> VNF[tier] + "中子加速器",
            NeutronAcceleratorPartMachine::new,
            (tier, builder) -> builder
                    .langValue(VNF[tier] + "Neutron Accelerator")
                    .rotationState(RotationState.ALL)
                    .abilities(GTOPartAbility.NEUTRON_ACCELERATOR)
                    .tooltips(Component.translatable("gtceu.universal.tooltip.max_voltage_in", V[tier], VNF[tier]),
                            Component.translatable("gtocore.machine.neutron_accelerator.tooltip.0", (V[tier] << 3) / 10),
                            Component.translatable("gtocore.machine.neutron_accelerator.tooltip.1"),
                            Component.translatable("gtceu.universal.tooltip.energy_storage_capacity", 2 * V[tier]))
                    .overlayTieredHullRenderer("neutron_accelerator")
                    .register(),
            GTMachineUtils.ALL_TIERS);

    public static final MachineDefinition LARGE_STEAM_HATCH = machine("large_steam_input_hatch", "大型蒸汽输入仓", LargeSteamHatchPartMachine::new)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.STEAM)
            .renderer(() -> new OverlaySteamMachineRenderer(GTCEu.id("block/machine/part/" + "steam_hatch")))
            .tooltips(Component.translatable("gtocore.machine.large_steam_input_hatch.tooltip.0"),
                    Component.translatable("gtceu.universal.tooltip.fluid_storage_capacity", 4096000),
                    Component.translatable("gtceu.machine.steam.steam_hatch.tooltip"))
            .register();

    public static final MachineDefinition STERILE_CLEANING_MAINTENANCE_HATCH = machine("sterile_cleaning_maintenance_hatch", "无菌维护仓", holder -> new CMHatchPartMachine(holder, CMHatchPartMachine.STERILE_DUMMY_CLEANROOM))
            .rotationState(RotationState.ALL)
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

    public static final MachineDefinition LAW_CLEANING_MAINTENANCE_HATCH = machine("law_cleaning_maintenance_hatch", "绝对洁净维护仓", holder -> new CMHatchPartMachine(holder, CMHatchPartMachine.LAW_DUMMY_CLEANROOM))
            .rotationState(RotationState.ALL)
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

    public static final MachineDefinition AUTO_CONFIGURATION_MAINTENANCE_HATCH = machine("auto_configuration_maintenance_hatch", "可配置自动维护仓", ACMHatchPartMachine::new)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"))
            .renderer(() -> new MaintenanceHatchPartRenderer(5, GTCEu.id("block/machine/part/maintenance.full_auto")))
            .register();

    public static final MachineDefinition CLEANING_CONFIGURATION_MAINTENANCE_HATCH = machine("cleaning_configuration_maintenance_hatch", "超净可配置维护仓", holder -> new CCMHatchPartMachine(holder, CMHatchPartMachine.DUMMY_CLEANROOM))
            .rotationState(RotationState.ALL)
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

    public static final MachineDefinition STERILE_CONFIGURATION_CLEANING_MAINTENANCE_HATCH = machine("sterile_configuration_cleaning_maintenance_hatch", "无菌可配置维护仓", holder -> new CCMHatchPartMachine(holder, CMHatchPartMachine.STERILE_DUMMY_CLEANROOM))
            .rotationState(RotationState.ALL)
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

    public static final MachineDefinition LAW_CONFIGURATION_CLEANING_MAINTENANCE_HATCH = machine("law_configuration_cleaning_maintenance_hatch", "绝对洁净可配置维护仓", holder -> new CCMHatchPartMachine(holder, CMHatchPartMachine.LAW_DUMMY_CLEANROOM))
            .rotationState(RotationState.ALL)
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

    public static final MachineDefinition GRAVITY_HATCH = machine("gravity_hatch", "重力控制仓", GravityHatchPartMachine::new)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"))
            .renderer(() -> new MaintenanceHatchPartRenderer(8, GTCEu.id("block/machine/part/maintenance.full_auto")))
            .register();

    public static final MachineDefinition GRAVITY_CONFIGURATION_HATCH = machine("gravity_configuration_hatch", "可配置重力维护仓", CGCHatchPartMachine::new)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtceu.universal.disabled"))
            .renderer(() -> new MaintenanceHatchPartRenderer(10, GTCEu.id("block/machine/part/maintenance.full_auto")))
            .register();

    public static final MachineDefinition VACUUM_HATCH = machine("vacuum_hatch", "真空仓", VacuumHatchPartMachine::new)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtocore.recipe.vacuum.tier", 4))
            .tooltips(Component.translatable("gtceu.universal.disabled"))
            .renderer(() -> new MaintenanceHatchPartRenderer(4, GTCEu.id("block/machine/part/maintenance.full_auto")))
            .register();

    public static final MachineDefinition VACUUM_CONFIGURATION_HATCH = machine("vacuum_configuration_hatch", "可配置真空维护仓", CVCHatchPartMachine::new)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.MAINTENANCE)
            .tooltips(Component.translatable("gtocore.recipe.vacuum.tier", 4))
            .tooltips(Component.translatable("gtceu.universal.disabled"))
            .renderer(() -> new MaintenanceHatchPartRenderer(6,
                    GTCEu.id("block/machine/part/maintenance.full_auto")))
            .register();

    public final static MachineDefinition NEUTRON_SENSOR = machine("neutron_sensor", "中子传感器", SensorPartMachine::new)
            .tier(IV)
            .rotationState(RotationState.ALL)
            .tooltips(Component.translatable("gtocore.machine.neutron_sensor.tooltip.0"))
            .overlayTieredHullRenderer("neutron_sensor")
            .register();

    public final static MachineDefinition PH_SENSOR = machine("ph_sensor", "pH 传感器", SensorPartMachine::new)
            .langValue("pH Sensor")
            .tier(EV)
            .rotationState(RotationState.ALL)
            .overlayTieredHullRenderer("neutron_sensor")
            .register();

    public final static MachineDefinition HEAT_SENSOR = machine("heat_sensor", "热传感器", SensorPartMachine::new)
            .tier(LV)
            .rotationState(RotationState.ALL)
            .overlayTieredHullRenderer("neutron_sensor")
            .register();

    public static final MachineDefinition GRIND_BALL_HATCH = machine("grind_ball_hatch", "研磨球仓", BallHatchPartMachine::new)
            .tier(IV)
            .rotationState(RotationState.ALL)
            .renderer(BallHatchRenderer::new)
            .register();

    public static final MachineDefinition RADIATION_HATCH = machine("radiation_hatch", "放射仓", RadiationHatchPartMachine::new)
            .tier(ZPM)
            .recipeType(GTORecipeTypes.RADIATION_HATCH_RECIPES)
            .rotationState(RotationState.ALL)
            .overlayTieredHullRenderer("radiation_hatch")
            .register();

    public static final MachineDefinition ROTOR_HATCH = machine("rotor_hatch", "转子仓", h -> new ItemHatchPartMachine(h, 1, i -> TurbineRotorBehaviour.getBehaviour(i) != null))
            .tier(EV)
            .rotationState(RotationState.ALL)
            .overlayTieredHullRenderer("rotor_hatch")
            .register();

    public static final MachineDefinition BLOCK_BUS = machine("block_bus", "方块总线", BlockBusPartMachine::new)
            .tier(LuV)
            .rotationState(RotationState.ALL)
            .renderer(() -> new OverlayTieredMachineRenderer(LuV, GTCEu.id("block/machine/part/item_bus.import")))
            .register();

    public static final MachineDefinition LENS_HOUSING = machine("lens_housing", "透镜仓", h -> new ItemHatchPartMachine(h, 1, i -> ChemicalHelper.getPrefix(i.getItem()) == TagPrefix.lens))
            .tier(EV)
            .rotationState(RotationState.ALL)
            .renderer(() -> new OverlayTieredMachineRenderer(EV, GTCEu.id("block/machine/part/item_bus.import")))
            .register();

    public final static MachineDefinition LENS_INDICATOR_HATCH = machine("lens_indicator_hatch", "透镜指示仓", IndicatorHatchPartMachine::new)
            .tier(HV)
            .rotationState(RotationState.ALL)
            .overlayTieredHullRenderer("neutron_sensor")
            .register();

    public final static MachineDefinition DEGASSING_CONTROL_HATCH = machine("degassing_control_hatch", "脱气控制仓", IndicatorHatchPartMachine::new)
            .tier(LuV)
            .rotationState(RotationState.ALL)
            .overlayTieredHullRenderer("neutron_sensor")
            .register();

    public static final MachineDefinition CATALYST_HATCH = machine("catalyst_hatch", "催化剂仓", CatalystHatchPartMachine::new)
            .tier(IV)
            .rotationState(RotationState.ALL)
            .overlayTieredHullRenderer("catalyst_hatch")
            .register();

    public static final MachineDefinition QUANTUM_DATA_ACCESS_HATCH = machine("quantum_data_access_hatch", "量子数据访问仓", (holder) -> new DataAccessHatchMachine(holder, UV, false))
            .tier(UV)
            .rotationState(RotationState.ALL)
            .abilities(PartAbility.DATA_ACCESS)
            .tooltips(Component.translatable("gtceu.machine.data_access_hatch.tooltip.0"),
                    Component.translatable("gtceu.machine.data_access_hatch.tooltip.1", 36),
                    Component.translatable("gtceu.universal.disabled"))
            .renderer(() -> new OverlayTieredMachineRenderer(UV, GTCEu.id("block/machine/part/data_access_hatch")))
            .register();

    public static final MachineDefinition MACHINE_ACCESS_INTERFACE = machine("machine_access_interface", "机器访问接口", MachineAccessInterfacePartMachine::new)
            .tier(IV)
            .rotationState(RotationState.ALL)
            .renderer(() -> new OverlayTieredMachineRenderer(IV, GTCEu.id("block/machine/part/data_access_hatch")))
            .register();
}
