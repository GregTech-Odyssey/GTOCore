package com.gtocore.common.data.machines;

import com.gtocore.api.machine.part.GTOPartAbility;
import com.gtocore.api.pattern.GTOPredicates;
import com.gtocore.client.renderer.machine.*;
import com.gtocore.common.block.FusionCasings;
import com.gtocore.common.data.*;
import com.gtocore.common.data.translation.GTOMachineTranslation;
import com.gtocore.common.machine.multiblock.electric.*;
import com.gtocore.common.machine.multiblock.electric.adventure.SlaughterhouseMachine;
import com.gtocore.common.machine.multiblock.electric.assembly.AdvancedAssemblyLineMachine;
import com.gtocore.common.machine.multiblock.electric.assembly.CircuitAssemblyLineMachine;
import com.gtocore.common.machine.multiblock.electric.bioengineering.IncubatorMachine;
import com.gtocore.common.machine.multiblock.electric.nano.NanoForgeMachine;
import com.gtocore.common.machine.multiblock.electric.processing.ColdIceFreezerMachine;
import com.gtocore.common.machine.multiblock.electric.processing.ProcessingPlantMachine;
import com.gtocore.common.machine.multiblock.electric.smelter.BlazeBlastFurnaceMachine;
import com.gtocore.common.machine.multiblock.electric.smelter.DimensionallyTranscendentPlasmaForgeMachine;
import com.gtocore.common.machine.multiblock.electric.space.SpaceElevatorMachine;
import com.gtocore.common.machine.multiblock.electric.space.SpaceElevatorModuleMachine;
import com.gtocore.common.machine.multiblock.electric.space.SpaceProbeSurfaceReceptionMachine;
import com.gtocore.common.machine.multiblock.electric.voidseries.INFFluidDrillMachine;
import com.gtocore.common.machine.multiblock.noenergy.GodForgeMachine;
import com.gtocore.common.machine.multiblock.noenergy.HarmonyMachine;
import com.gtocore.common.machine.multiblock.noenergy.HeatExchangerMachine;
import com.gtocore.common.machine.multiblock.noenergy.NeutronActivatorMachine;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.NewDataAttributes;
import com.gtolib.api.annotation.component_builder.ComponentBuilder;
import com.gtolib.api.annotation.component_builder.StyleBuilder;
import com.gtolib.api.lang.CNEN;
import com.gtolib.api.machine.MultiblockDefinition;
import com.gtolib.api.machine.multiblock.CoilCrossRecipeMultiblockMachine;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
import com.gtolib.utils.*;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.client.renderer.machine.FusionReactorRenderer;
import com.gregtechceu.gtceu.common.data.*;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.FusionReactorMachine;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gtocore.common.block.BlockMap.SEPMMAP;
import static com.gtolib.api.GTOValues.POWER_MODULE_TIER;
import static com.gtolib.utils.register.MachineRegisterUtils.multiblock;
import static com.gtolib.utils.register.MachineRegisterUtils.registerTieredMultis;

public final class MultiBlockD {

    public static void init() {}

    public static final MultiblockMachineDefinition GREENHOUSE = multiblock("greenhouse", "温室", GreenhouseMachine::new)
            .nonYAxisRotation()
            .recipeTypes(GTORecipeTypes.GREENHOUSE_RECIPES)
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("运行条件", "Operating Conditions", StyleBuilder::setGold),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("需要阳光才能运行", "Requires sunlight to operate", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("太阳光照不足时速度减缓", "Speed slows down when sunlight is insufficient", StyleBuilder::setRed), p -> p, StyleBuilder::setOneTab))))
            .overclock()
            .block(GTBlocks.MACHINE_CASING_ULV)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("0BBB0", "0BBB0", "0BBB0", "0BBB0", "0BBB0")
                    .aisle("BBBBB", "BdddB", "B###B", "B###B", "BGGGB")
                    .aisle("BBBBB", "BdddB", "B###B", "B###B", "BGGGB")
                    .aisle("BBBBB", "BdddB", "B###B", "B###B", "BGGGB")
                    .aisle("0BBB0", "0BEB0", "0BBB0", "0BBB0", "0BBB0")
                    .where('E', controller(blocks(definition.get())))
                    .where('G', blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                    .where('B', blocks(GTBlocks.MACHINE_CASING_ULV.get())
                            .setMinGlobalLimited(40)
                            .or(autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('d', blocks(RegistriesUtils.getBlock("farmersdelight:rich_soil")))
                    .where('#', air())
                    .where('0', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/voltage/ulv/side"), GTCEu.id("block/multiblock/fusion_reactor"))
            .register();

    public static final MultiblockMachineDefinition EYE_OF_HARMONY = multiblock("eye_of_harmony", "鸿蒙之眼", HarmonyMachine::new)
            .nonYAxisRotation()
            .recipeTypes(GTORecipeTypes.COSMOS_SIMULATION_RECIPES)
            .tooltips(NewDataAttributes.MAIN_FUNCTION.create(v -> v.addLines("创造微缩宇宙并获取资源", "Creates a mini-universe and gathers resources inside")))
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("供电系统", "Power System", StyleBuilder::setGold),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("需要太多EU，无法用常规手段供能", "Requires too much EU, unable to power with conventional means", StyleBuilder::setRed), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("由无线EU网络直接供给", "Directly supplied by wireless EU network", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("具体数值可在GUI内查看", "Specific values can be viewed in the GUI", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab))))
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("特殊超频", "Special Overclocking", StyleBuilder::setGold),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("每提升16倍功率提升2倍速度", "Increasing speed by 2x for every 16x power increase", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("超频由编程电路调节", "Overclocking is adjusted by programmed circuits", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("电路1: 不执行超频", "Circuit 1: No overclocking", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("电路2-4: 分别执行1-3次超频", "Circuits 2-4: Execute 1-3 times overclocking", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab))))
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("启动需求", "Startup Requirements", StyleBuilder::setGold),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("1024B宇宙素", "1024B Cosmic Element", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("1024KB氢", "1024KB Hydrogen", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("1024KB氦", "1024KB Helium", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("氢氦存储在机器内部并持续消耗", "Hydrogen and helium stored inside and continuously consumed", StyleBuilder::setRed), p -> p, StyleBuilder::setOneTab))))
            .fromSourceTooltips("GTNH")
            .block(GTBlocks.HIGH_POWER_CASING)
            .pattern(definition -> MultiBlockFileReader.start(definition)
                    .where('~', controller(blocks(definition.get())))
                    .where('A', blocks(GTOBlocks.DIMENSIONALLY_TRANSCENDENT_CASING.get()))
                    .where('B', blocks(GTBlocks.HIGH_POWER_CASING.get())
                            .or(abilities(EXPORT_ITEMS).setPreviewCount(1))
                            .or(abilities(IMPORT_ITEMS).setPreviewCount(1))
                            .or(abilities(EXPORT_FLUIDS).setPreviewCount(1))
                            .or(abilities(IMPORT_FLUIDS).setPreviewCount(1)))
                    .where('D', blocks(GTOBlocks.DIMENSION_INJECTION_CASING.get()))
                    .where('E', blocks(GTOBlocks.DIMENSIONAL_BRIDGE_CASING.get()))
                    .where('F', blocks(GTOBlocks.SPACETIME_COMPRESSION_FIELD_GENERATOR.get()))
                    .where('G', blocks(GTOBlocks.DIMENSIONAL_STABILITY_CASING.get()))
                    .where(' ', any())
                    .build())
            .renderer(EyeOfHarmonyRenderer::new)
            .hasTESR(true)
            .register();

    public static final MultiblockMachineDefinition SPACE_PROBE_SURFACE_RECEPTION = multiblock("space_probe_surface_reception", "宇宙探测器地面接收单元", SpaceProbeSurfaceReceptionMachine::new)
            .nonYAxisRotation()
            .recipeTypes(GTORecipeTypes.SPACE_PROBE_SURFACE_RECEPTION_RECIPES)
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("运行限制", "Operating Restrictions", StyleBuilder::setGold),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("只能运行在空间站", "Can only operate on space station", StyleBuilder::setRed), p -> p, StyleBuilder::setOneTab))))
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("戴森球连接", "Dyson Sphere Connection", StyleBuilder::setGold),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("自动连接星系内未使用的戴森球", "Automatically connects to unused Dyson spheres in the galaxy", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("根据戴森球模块数量提升产出", "Increases production based on Dyson sphere module count", StyleBuilder::setGreen), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("该操作不会损坏戴森球", "This operation will not damage the Dyson sphere", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab))))
            .perfectOCTooltips()
            .block(GCYMBlocks.CASING_ATOMIC)
            .pattern(definition -> MultiBlockFileReader.start(definition)
                    .where('~', controller(blocks(definition.get())))
                    .where('A', blocks(GTOBlocks.COSMIC_DETECTION_RECEIVER_MATERIAL_RAY_ABSORBING_ARRAY.get()))
                    .where('B', blocks(GTOBlocks.NAQUADAH_REINFORCED_PLANT_CASING.get()))
                    .where('C', blocks(GTOBlocks.HIGH_PRESSURE_RESISTANT_CASING.get()))
                    .where('D', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Neutronium)))
                    .where('E', blocks(GCYMBlocks.CASING_ATOMIC.get()))
                    .where('F', blocks(GTOBlocks.HYPER_MECHANICAL_CASING.get()))
                    .where('G', blocks(GTOBlocks.NEUTRONIUM_STABLE_CASING.get()))
                    .where('H', blocks(GTOBlocks.HYPER_CORE.get()))
                    .where('I', blocks(GCYMBlocks.HEAT_VENT.get()))
                    .where('J', blocks(GTOBlocks.ANTIFREEZE_HEATPROOF_MACHINE_CASING.get()))
                    .where('K', blocks(GTBlocks.HIGH_POWER_CASING.get()))
                    .where('L', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTOMaterials.BlackTitanium)))
                    .where('M', blocks(GTOBlocks.AMPROSIUM_ACTIVE_CASING.get()))
                    .where('b', blocks(GCYMBlocks.CASING_ATOMIC.get())
                            .or(autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(MAINTENANCE).setExactLimit(1))
                            .or(abilities(COMPUTATION_DATA_RECEPTION).setExactLimit(1)))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/atomic_casing"), GTCEu.id("block/multiblock/data_bank"))
            .register();

    public static final MultiblockMachineDefinition HYPERDIMENSIONAL_PLASMA_FUSION_CORE = multiblock("hyperdimensional_plasma_fusion_core", "高维等离子聚变核心", DimensionallyTranscendentPlasmaForgeMachine::new)
            .nonYAxisRotation()
            .recipeTypes(GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES)
            .recipeTypes(GTORecipeTypes.STELLAR_FORGE_RECIPES)
            .parallelizableTooltips()
            .laserTooltips()
            .parallelizableOverclock()
            .block(GTOBlocks.DIMENSIONALLY_TRANSCENDENT_CASING)
            .pattern(definition -> MultiBlockFileReader.start(definition)
                    .where('A', blocks(GTOBlocks.STRENGTHEN_THE_BASE_BLOCK.get()))
                    .where('B', blocks(GTOBlocks.DIMENSIONALLY_TRANSCENDENT_CASING.get())
                            .or(abilities(PARALLEL_HATCH).setMaxGlobalLimited(1))
                            .or(GTOPredicates.autoLaserAbilities(definition.getRecipeTypes())))
                    .where('C', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTOMaterials.Amprosium)))
                    .where('D', blocks(GTOBlocks.SPS_CASING.get()))
                    .where('E', GTOPredicates.absBlocks())
                    .where('F', blocks(GTOBlocks.STRONTIUM_CARBONATE_CERAMIC_RAY_ABSORBING_MECHANICAL_CUBE.get()))
                    .where('G', blocks(GTBlocks.FUSION_GLASS.get()))
                    .where('H', blocks(GTOBlocks.DIMENSIONALLY_TRANSCENDENT_CASING.get()))
                    .where('I', blocks(GTBlocks.HIGH_POWER_CASING.get()))
                    .where('J', blocks(GTOBlocks.MOLECULAR_COIL.get()))
                    .where('K', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Naquadria)))
                    .where('L', blocks(GTOBlocks.HYPER_CORE.get()))
                    .where('M', blocks(GTOBlocks.DIMENSIONAL_BRIDGE_CASING.get()))
                    .where('N', blocks(GTOBlocks.ABS_ORANGE_CASING.get()))
                    .where('O', blocks(GTOBlocks.HIGH_STRENGTH_CONCRETE.get()))
                    .where('P', heatingCoils())
                    .where('Q', blocks(GTOBlocks.COBALT_OXIDE_CERAMIC_STRONG_THERMALLY_CONDUCTIVE_MECHANICAL_BLOCK.get()))
                    .where('R', blocks(GTOBlocks.RHENIUM_REINFORCED_ENERGY_GLASS.get()))
                    .where('S', blocks(GTOBlocks.HOLLOW_CASING.get()))
                    .where('T', blocks(GTOBlocks.DIMENSION_INJECTION_CASING.get()))
                    .where('U', controller(blocks(definition.get())))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/dimensionally_transcendent_casing"), GTOCore.id("block/multiblock/dimensionally_transcendent_plasma_forge"))
            .register();

    public static final MultiblockMachineDefinition CIRCUIT_ASSEMBLY_LINE = multiblock("circuit_assembly_line", "电路装配线", CircuitAssemblyLineMachine::new)
            .allRotation()
            .recipeTypes(GTORecipeTypes.CIRCUIT_ASSEMBLY_LINE_RECIPES)
            .tooltips(ComponentBuilder.create().addStoryLine("""
                    GTO寰宇格雷科技有限公司的顶级工程师们设计了这条电路装配线。
                    董事长视察时赞叹道："这是我们最优雅的基础设施之一。"
                    透明的层压玻璃展示着内部精密的装配过程，机器人们有条不紊
                    地制造着各种复杂电路。当相同配方的机器人协作时，效率翻倍，
                    这条生产线成为了公司电子工业的核心支柱。
                    """,
                    """
                            Top engineers of GTO designed this elegant circuit assembly line.
                            The CEO praised it as "one of our most graceful infrastructures."
                            Through laminated glass, precise assembly processes are displayed,
                            with robots methodically crafting complex circuits in perfect harmony.
                            This production line became the cornerstone of our electronics industry.
                            """).build())
            .specialParallelizableTooltips()
            .tooltips(NewDataAttributes.ALLOW_PARALLEL_NUMBER.create(h -> h.addLines("同配方机器人数量x2的并行", "Parallelism of x2 for the same recipe robots")))
            .block(GTOBlocks.PIKYONIUM_MACHINE_CASING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("bbb", "bbb", "bfb")
                    .aisle("bbb", "cec", "bdb").setRepeatable(16)
                    .aisle("bbb", "bab", "bgb")
                    .where('a', controller(blocks(definition.get())))
                    .where('b', blocks(GTOBlocks.PIKYONIUM_MACHINE_CASING.get())
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(2))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('c', blocks(GTBlocks.CASING_LAMINATED_GLASS.get()))
                    .where('d', blocks(GTOMachines.HUGE_ITEM_IMPORT_BUS.getBlock()).or(blocks(GTMachines.ITEM_IMPORT_BUS[0].getBlock())))
                    .where('e', blocks(GTOBlocks.MACHINE_CASING_CIRCUIT_ASSEMBLY_LINE.get()))
                    .where('f', abilities(EXPORT_ITEMS))
                    .where('g', abilities(IMPORT_FLUIDS_4X))
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/pikyonium_machine_casing"), GTCEu.id("block/multiblock/assembly_line"))
            .register();

    public static final MultiblockMachineDefinition ASSEMBLER_MODULE = multiblock("assembler_module", "太空电梯组装模块", (holder) -> new SpaceElevatorModuleMachine(holder, true))
            .nonYAxisRotation()
            .recipeTypes(GTORecipeTypes.ASSEMBLER_MODULE_RECIPES)
            .specialParallelizableTooltips()
            .tooltips(NewDataAttributes.ALLOW_PARALLEL_NUMBER.create(h -> h.addLines("4^(动力模块等级-1)", "4^(Power Module tier - 1)")))
            .block(GTOBlocks.SPACE_ELEVATOR_MECHANICAL_CASING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("a", "b", "b", "b", "b")
                    .aisle("a", "b", "b", "b", "b")
                    .aisle("c", "b", "b", "~", "b")
                    .where('~', controller(blocks(definition.get())))
                    .where('b', blocks(GTOBlocks.SPACE_ELEVATOR_MECHANICAL_CASING.get())
                            .or(autoAbilities(definition.getRecipeTypes())))
                    .where('a', blocks(GTOBlocks.SPACE_ELEVATOR_MODULE_BASE.get()))
                    .where('c', blocks(GTOBlocks.MODULE_CONNECTOR.get()))
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/space_elevator_mechanical_casing"), GTCEu.id("block/multiblock/gcym/large_assembler"))
            .register();

    public static final MultiblockMachineDefinition RESOURCE_COLLECTION_MODULE = multiblock("resource_collection_module", "太空电梯资源采集模块", (holder) -> new SpaceElevatorModuleMachine(holder, false))
            .nonYAxisRotation()
            .recipeTypes(GTORecipeTypes.MINER_MODULE_RECIPES)
            .recipeTypes(GTORecipeTypes.DRILLING_MODULE_RECIPES)
            .specialParallelizableTooltips()
            .tooltips(NewDataAttributes.ALLOW_PARALLEL_NUMBER.create(h -> h.addLines("4^(动力模块等级-1)", "4^(Power Module tier - 1)")))
            .block(GTOBlocks.SPACE_ELEVATOR_MECHANICAL_CASING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("a", "b", "b", "b", "b")
                    .aisle("a", "b", "b", "b", "b")
                    .aisle("c", "b", "b", "~", "b")
                    .where('~', controller(blocks(definition.get())))
                    .where('b', blocks(GTOBlocks.SPACE_ELEVATOR_MECHANICAL_CASING.get())
                            .or(autoAbilities(definition.getRecipeTypes())))
                    .where('a', blocks(GTOBlocks.SPACE_ELEVATOR_MODULE_BASE.get()))
                    .where('c', blocks(GTOBlocks.MODULE_CONNECTOR.get()))
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/space_elevator_mechanical_casing"), GTCEu.id("block/multiblock/gcym/large_assembler"))
            .register();

    public static final MultiblockMachineDefinition BLOCK_CONVERSION_ROOM = multiblock("block_conversion_room", "方块转换室", holder -> new BlockConversionRoomMachine(holder, false))
            .noneRotation()
            .recipeTypes(GTORecipeTypes.BLOCK_CONVERSIONRECIPES)
            .tooltips(NewDataAttributes.MAIN_FUNCTION.create(v -> v.addLines("每秒随机转化机器内部方块", "Randomly converts blocks inside the machine every second")))
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("电压等级加成", "Voltage Tier Bonus", StyleBuilder::setGold),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("每高出MV1级，转换方块数量+4", "For each tier above MV1, block conversion +4", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("不会重复转换同一方块", "Will not repeatedly convert the same block", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab))))
            .block(GTOBlocks.ALUMINIUM_BRONZE_CASING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("bbbbbbb", "bbbbbbb", "bdddddb", "bdddddb", "bdddddb", "bdddddb", "bdddddb", "bbbbbbb")
                    .aisle("bbbbbbb", "bcccccb", "d     d", "d     d", "d     d", "d     d", "d     d", "bbbbbbb")
                    .aisle("bbbbbbb", "bcccccb", "d     d", "d     d", "d     d", "d     d", "d     d", "bbbbbbb")
                    .aisle("bbbbbbb", "bcccccb", "d     d", "d     d", "d     d", "d     d", "d     d", "bbbabbb")
                    .aisle("bbbbbbb", "bcccccb", "d     d", "d     d", "d     d", "d     d", "d     d", "bbbbbbb")
                    .aisle("bbbbbbb", "bcccccb", "d     d", "d     d", "d     d", "d     d", "d     d", "bbbbbbb")
                    .aisle("bbbbbbb", "bbbbbbb", "bdddddb", "bdddddb", "bdddddb", "bdddddb", "bdddddb", "bbbbbbb")
                    .where('a', controller(blocks(definition.get())))
                    .where('b', blocks(GTOBlocks.ALUMINIUM_BRONZE_CASING.get())
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(2))
                            .or(blocks(GTOMachines.BLOCK_BUS.getBlock()).setMaxGlobalLimited(1)))
                    .where('c', blocks(GTOBlocks.SHINING_OBSIDIAN.get()))
                    .where('d', blocks(GTBlocks.CASING_TEMPERED_GLASS.get())
                            .or(blocks(Blocks.IRON_DOOR).setMaxGlobalLimited(4).setPreviewCount(1)))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/aluminium_bronze_casing"), GTCEu.id("block/multiblock/cleanroom"))
            .register();

    public static final MultiblockMachineDefinition LARGE_BLOCK_CONVERSION_ROOM = multiblock("large_block_conversion_room", "大型方块转换室", holder -> new BlockConversionRoomMachine(holder, true))
            .noneRotation()
            .recipeTypes(GTORecipeTypes.BLOCK_CONVERSIONRECIPES)
            .tooltips(ComponentBuilder.create().addStoryLine(
                    """
                            一名格雷科技有限公司员工在实验室里焦急地踱步，大型方块转换室是他的最新发明。
                            这台庞然大物由铝青铜外壳构成，能够每秒随机转换内部的一个方块。
                            他记得董事长曾说过，提高电压等级可以增加转换效率，每高出MV1级，转换数量增加64个。
                            第一次测试时，他紧张地启动了机器，看着内部的石头逐渐变成了各种稀有材料。
                            值得注意的是，机器似乎有自己的"记忆"，从不会重复转换同一个方块。
                            GTO寰宇格雷科技有限公司的同事们都惊叹于这项发明，它将彻底改变资源获取的方式。
                            """,
                    """
                            A GregTech Ltd. employee paced anxiously in the laboratory, the Large Block Conversion Room was his latest invention.
                            This behemoth, encased in aluminium bronze, could randomly convert one block inside it every second.
                            He remembered the CEO saying that increasing voltage tiers would improve efficiency, each tier above MV1 adds 64 to conversion count.
                            During the first test, he nervously activated the machine, watching as stones inside gradually transformed into various rare materials.
                            Notably, the machine seemed to have its own "memory," never converting the same block twice.
                            Colleagues at GTO Universal GregTech Company marveled at this invention that would revolutionize resource acquisition.
                            """).build())
            .tooltips(NewDataAttributes.MAIN_FUNCTION.create(v -> v.addLines("每秒随机转化机器内部一个方块", "Randomly converts blocks inside the machine every second")))
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("电压等级加成", "Voltage Tier Bonus", StyleBuilder::setGold),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("每高出MV1级，转换方块数量+64", "For each tier above MV1, block conversion +64", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("不会重复转换同一方块", "Will not repeatedly convert the same block", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab))))
            .block(GTOBlocks.ALUMINIUM_BRONZE_CASING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("bbbbbbbbbbb", "bbbbbbbbbbb", "bdddddddddb", "bdddddddddb", "bdddddddddb", "bdddddddddb", "bdddddddddb", "bdddddddddb", "bdddddddddb", "bbbbbbbbbbb")
                    .aisle("bbbbbbbbbbb", "bcccccccccb", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "bbbbbbbbbbb")
                    .aisle("bbbbbbbbbbb", "bcccccccccb", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "bbbbbbbbbbb")
                    .aisle("bbbbbbbbbbb", "bcccccccccb", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "bbbbbbbbbbb")
                    .aisle("bbbbbbbbbbb", "bcccccccccb", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "bbbbbbbbbbb")
                    .aisle("bbbbbbbbbbb", "bcccccccccb", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "bbbbbabbbbb")
                    .aisle("bbbbbbbbbbb", "bcccccccccb", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "bbbbbbbbbbb")
                    .aisle("bbbbbbbbbbb", "bcccccccccb", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "bbbbbbbbbbb")
                    .aisle("bbbbbbbbbbb", "bcccccccccb", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "bbbbbbbbbbb")
                    .aisle("bbbbbbbbbbb", "bcccccccccb", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "d         d", "bbbbbbbbbbb")
                    .aisle("bbbbbbbbbbb", "bbbbbbbbbbb", "bdddddddddb", "bdddddddddb", "bdddddddddb", "bdddddddddb", "bdddddddddb", "bdddddddddb", "bdddddddddb", "bbbbbbbbbbb")
                    .where('a', controller(blocks(definition.get())))
                    .where('b', blocks(GTOBlocks.ALUMINIUM_BRONZE_CASING.get())
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(2))
                            .or(blocks(GTOMachines.BLOCK_BUS.getBlock()).setMaxGlobalLimited(1)))
                    .where('c', blocks(GTOBlocks.SHINING_OBSIDIAN.get()))
                    .where('d', blocks(GTBlocks.CASING_TEMPERED_GLASS.get())
                            .or(blocks(Blocks.IRON_DOOR).setMaxGlobalLimited(4).setPreviewCount(1)))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/aluminium_bronze_casing"), GTCEu.id("block/multiblock/cleanroom"))
            .register();

    public static final MultiblockMachineDefinition PCB_FACTORY = multiblock("pcb_factory", "PCB工厂", PCBFactoryMachine::new)
            .nonYAxisRotation()
            .recipeTypes(GTORecipeTypes.PCB_FACTORY_RECIPES)
            .parallelizableTooltips()
            .tooltips(ComponentBuilder.create().addStoryLine("""
                    GTO寰宇格雷科技有限公司的研发部门取得了伟大的突破。
                    他们利用纳米蜂群技术，建造了革命性的PCB工厂。
                    这座工厂能够精确制造各种电路板，标志着公司进入了
                    纳米制造的新时代，为未来的科技发展铺平了道路。
                    """,
                    """
                            GTO's R&D department achieved a great breakthrough.
                            They built a revolutionary PCB Factory using nanite swarm technology.
                            This facility can precisely manufacture various circuit boards, marking
                            the company's entry into the nanomanufacturing era.
                            """).build())
            .tooltipsText("使用纳米蜂群引导结构等级，金：1，山铜：2，末影素：3", "Use the nanites guidance structure level, gold: 1, orichalcum: 2, enderium: 3")
            .laserTooltips()
            .block(GTOBlocks.IRIDIUM_CASING)
            .pattern(definition -> PCBFactoryMachine.getBlockPattern(1, definition))
            .shapeInfos(definition -> {
                List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
                for (int i = 1; i < 4; i++) {
                    shapeInfos.addAll(MultiblockDefinition.getMatchingShapes(false, PCBFactoryMachine.getBlockPattern(i, definition)));
                }
                return shapeInfos;
            })
            .workableCasingRenderer(GTOCore.id("block/casings/iridium_casing"), GTCEu.id("block/multiblock/gcym/large_maceration_tower"))
            .register();

    public static final MultiblockMachineDefinition BLAZE_BLAST_FURNACE = multiblock("blaze_blast_furnace", "烈焰高炉", BlazeBlastFurnaceMachine::new)
            .allRotation()
            .recipeTypes(GTRecipeTypes.BLAST_RECIPES)
            .durationMultiplierTooltips(0.5)
            .tooltips(ComponentBuilder.create().addStoryLine(
                    """
                            格雷科技有限公司的工程师们发现了利用烈焰的力量的新方法。
                            在一个寒冷的冬日，他们建造了第一座烈焰高炉，将液态烈焰
                            注入特制的钨钢管道中。董事长亲自验收时，惊叹于其效率，
                            它能以普通高炉一半的时间冶炼金属，甚至可同时处理64个配方。
                            自此，公司的冶金产能翻了数倍，员工们不再担心材料短缺。""",
                    """
                            GregTech engineers discovered a new way to harness blaze power.
                            On a cold winter day, they built the first Blaze Blast Furnace,
                            injecting liquid blaze into specially designed tungstensteel pipes.
                            The CEO was amazed by its efficiency, smelting metals in half the time
                            and processing up to 64 recipes simultaneously, solving material shortages.""").build())
            .tooltipsText("需每秒提供§b10x配方等级^2§r的§e液态烈焰§r", "Requires to provide %b10x(Recipe tier)²%r of %eLiquid Blaze%r per second")
            .tooltipsKey("gtceu.machine.electric_blast_furnace.tooltip.2")
            .specialParallelizableTooltips()
            .tooltips(NewDataAttributes.ALLOW_PARALLEL_NUMBER.create(64))
            .recipeModifier(RecipeModifierFunction.overclocking(0.5, 1, 0.5))
            .block(GTOBlocks.BLAZE_CASING)
            .pattern(definition -> FactoryBlockPattern.start(definition, RelativeDirection.RIGHT, RelativeDirection.UP, RelativeDirection.BACK)
                    .aisle(" AAAAA ", " AAAAA ", "       ", "       ", "       ", "       ")
                    .aisle("AAACAAA", "AADDDAA", " AEGEA ", "  FFF  ", "  FFF  ", "  EEE  ")
                    .aisle("AAACAAA", "BD   DB", " E   E ", " F   F ", " F   F ", " EDDDE ")
                    .aisle("ACCCCCA", "BD   DB", "AD   DA", "AF   FA", "AF   FA", "AEDHDEA")
                    .aisle("AAACAAA", "BD   DB", " E   E ", " F   F ", " F   F ", " EDDDE ")
                    .aisle("AAACAAA", "AADDDAA", " AEEEA ", "  FFF  ", "  FFF  ", "  EEE  ")
                    .aisle(" AAAAA ", " AAAAA ", "       ", "       ", "       ", "       ")
                    .where('A', blocks(GCYMBlocks.CASING_HIGH_TEMPERATURE_SMELTING.get()))
                    .where('B', blocks(GCYMBlocks.HEAT_VENT.get()))
                    .where('C', blocks(GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()))
                    .where('D', blocks(GTOBlocks.BLAZE_CASING.get()))
                    .where('E', blocks(GTOBlocks.BLAZE_CASING.get())
                            .or(autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('F', heatingCoils())
                    .where('G', controller(blocks(definition.get())))
                    .where('H', abilities(MUFFLER))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/blaze_casing"), GTCEu.id("block/multiblock/blast_furnace"))
            .register();

    public static final MultiblockMachineDefinition COLD_ICE_FREEZER = multiblock("cold_ice_freezer", "寒冰冷冻机", ColdIceFreezerMachine::new)
            .allRotation()
            .recipeTypes(GTRecipeTypes.VACUUM_RECIPES)
            .durationMultiplierTooltips(0.5)
            .tooltips(ComponentBuilder.create().addStoryLine("""
                    寒冰冷冻机的诞生源于一次偶然的实验事故。GTO寰宇格雷科技的研究员
                    在测试极低温材料时，意外发现液态冰与铝合金框架的奇妙组合效果。
                    经过数月改进，这台庞然大物终于在公司总部亮相，其内部的钨钢管道
                    和强化玻璃窗让参观者叹为观止。董事长亲自按下启动按钮，机器瞬间
                    将实验样本冻结，速度是普通真空冷冻机的两倍，且能同时处理64份材料。
                    """,
                    """
                            The Cold Ice Freezer was born from an accidental lab mishap. GTO Universal
                            GregTech researchers discovered a remarkable combination of liquid ice and
                            aluminum frames while testing cryogenic materials. After months of refinement,
                            the massive machine debuted at headquarters, its tungstensteel pipes and
                            tempered glass windows amazing visitors as it froze samples twice as fast.
                            """).build())
            .tooltipsText("需每秒提供§b10x配方等级^2§r的§b液态冰§r", "Requires to provide %b10x(Recipe tier)²%r of %bLiquid Ice%r per second")
            .specialParallelizableTooltips()
            .tooltips(NewDataAttributes.ALLOW_PARALLEL_NUMBER.create(64))
            .recipeModifiers(RecipeModifierFunction.overclocking(0.5, 1, 0.5))
            .block(GTOBlocks.COLD_ICE_CASING)
            .pattern(definition -> FactoryBlockPattern.start(definition, RelativeDirection.RIGHT, RelativeDirection.UP, RelativeDirection.BACK)
                    .aisle("AAAAA", " BBB ", " BGB ", " BBB ", "AAAAA")
                    .aisle("AAAAA", "BE EB", "BE EB", "BE EB", "AAAAA")
                    .aisle("AAAAA", " F F ", " F F ", " F F ", "ABHBA")
                    .aisle("AAAAA", "BE EB", "BE EB", "BE EB", "ACCCA")
                    .aisle("AAAAA", "D   D", "D   D", "D   D", "ACCCA")
                    .aisle("AAAAA", "CE EC", "CE EC", "CE EC", "ACCCA")
                    .aisle("AAAAA", " F F ", " F F ", " F F ", "ABHBA")
                    .aisle("AAAAA", "BE EB", "BE EB", "BE EB", "AAAAA")
                    .aisle("AAAAA", " BBB ", " BBB ", " BBB ", "AAAAA")
                    .where('A', blocks(GTBlocks.CASING_ALUMINIUM_FROSTPROOF.get()))
                    .where('B', blocks(GTOBlocks.COLD_ICE_CASING.get())
                            .or(autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('C', blocks(GTOBlocks.COLD_ICE_CASING.get()))
                    .where('D', blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                    .where('E', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Aluminium)))
                    .where('F', blocks(GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()))
                    .where('G', controller(blocks(definition.get())))
                    .where('H', abilities(MUFFLER))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/cold_ice_casing"), GTCEu.id("block/multiblock/vacuum_freezer"))
            .register();

    public static final MultiblockMachineDefinition DOOR_OF_CREATE = multiblock("door_of_create", "创造之门", ElectricMultiblockMachine::new)
            .noneRotation()
            .recipeTypes(GTORecipeTypes.DOOR_OF_CREATE_RECIPES)
            .tooltips(ComponentBuilder.create().addStoryLine("""
                    格雷科技寰宇公司的秘密实验室深处，矗立着一座巨大的环形结构。
                    "创造之门"是公司最高机密项目，董事长亲自监督其建造过程。
                    传说只有身着星物质装甲的员工才能通过这扇门，抵达创造维度。
                    每当MAX级电压注入，环形门户便开始旋转，龙息粒子在空间中舞动，
                    连接着两个世界。有幸穿越的员工回来后，都对所见三缄其口。
                    """,
                    """
                            Deep in GTO's secret lab stands a massive ring-like structure.
                            The "Door of Create" is the company's highest classified project.
                            Only employees wearing star matter armor can pass through to the Creation Dimension.
                            When MAX voltage is applied, the ring spins as dragon breath particles dance,
                            connecting two worlds. Those who return never speak of what they've seen.
                            """).build())
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("启动条件", "Startup Conditions", StyleBuilder::setGold),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("在主世界提供MAX级电压", "Provides MAX tier voltage in the main world", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("设置电路为1开始运行", "Set circuit to 1 to start running", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab))))
            .noRecipeModifier()
            .block(GTOBlocks.DIMENSION_CONNECTION_CASING)
            .pattern(definition -> MultiBlockFileReader.start(definition)
                    .where('b', controller(blocks(definition.get())))
                    .where('a', blocks(GTOBlocks.DIMENSION_CONNECTION_CASING.get()))
                    .where('d', blocks(GTOBlocks.DIMENSION_CONNECTION_CASING.get())
                            .or(abilities(IMPORT_ITEMS).setPreviewCount(1))
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(1)))
                    .where('c', blocks(GTOBlocks.DIMENSION_CREATION_CASING.get()))
                    .where(' ', any())
                    .build())
            .onWorking(machine -> {
                if (machine.getRecipeLogic().getProgress() == 5) {
                    BlockPos pos = machine.self().getPos().offset(0, -13, 0);
                    Level level = machine.self().getLevel();
                    if (level != null) {
                        MinecraftServer server = level.getServer();
                        if (server != null) {
                            ServerUtils.runCommandSilent(server, "particle minecraft:dragon_breath " + pos.getX() + " " + pos.getY() + " " + pos.getZ() + " 4 4 4 0.01 1000 force");
                            List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(
                                    pos.getX() - 10,
                                    pos.getY() - 10,
                                    pos.getZ() - 10,
                                    pos.getX() + 10,
                                    pos.getY() + 10,
                                    pos.getZ() + 10));
                            for (Entity entity : entities) {
                                if (entity instanceof Player player) {
                                    if (Objects.equals(player.getArmorSlots().toString(), "[1 magnetohydrodynamically_constrained_star_matter_boots, 1 magnetohydrodynamically_constrained_star_matter_leggings, 1 magnetohydrodynamically_constrained_star_matter_chestplate, 1 magnetohydrodynamically_constrained_star_matter_helmet]")) {
                                        ServerUtils.runCommandSilent(server, "execute in gtocore:create as " + entity.getName().getString() + " run tp 0 1 0");
                                    } else {
                                        player.displayClientMessage(Component.literal("你的装备无法适应目标维度的环境"), true);
                                    }
                                }
                                if (entity instanceof ItemEntity item && Objects.equals(ItemUtils.getId(item.getItem()), "gtocore:magnetohydrodynamically_constrained_star_matter_block")) {
                                    level.addFreshEntity(new ItemEntity(level, item.getX(), item.getY(), item.getZ(), new ItemStack(Blocks.COMMAND_BLOCK.asItem(), item.getItem().getCount())));
                                    item.discard();
                                }
                                if (entity instanceof ItemEntity item && Objects.equals(ItemUtils.getId(item.getItem()), "gtocore:magmatter_ingot") && item.getItem().getCount() >= 64) {
                                    level.addFreshEntity(new ItemEntity(level, item.getX(), item.getY(), item.getZ(), ChemicalHelper.get(TagPrefix.block, GTOMaterials.Magmatter, item.getItem().getCount() / 64)));
                                    item.discard();
                                }
                            }
                        }
                    }
                }
                return true;
            })
            .workableCasingRenderer(GTOCore.id("block/casings/dimension_connection_casing"), GTOCore.id("block/multiblock/door_of_create"))
            .register();

    public static final MultiblockMachineDefinition BEDROCK_DRILLING_RIG = multiblock("bedrock_drilling_rig", "基岩钻机", BedrockDrillingRigMachine::new)
            .noneRotation()
            .recipeTypes(GTORecipeTypes.BEDROCK_DRILLING_RIG_RECIPES)
            .tooltips(ComponentBuilder.create().addStoryLine("""
                    GTO寰宇格雷科技有限公司的员工们建造了史上最强大的钻机。
                    它能穿透坚不可摧的基岩，获取珍贵的深层资源。
                    但董事长警告：每次使用都有风险，基岩可能永远消失。
                    """,
                    """
                            GTO Universal GregTech employees built the most powerful drilling rig ever.
                            It can penetrate indestructible bedrock to obtain precious deep resources.
                            But the CEO warned: each use carries risk, bedrock might vanish forever.
                            """).build())
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("运行条件", "Operating Conditions", StyleBuilder::setGold),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("需要基岩在钻头下方", "Requires bedrock below the drill head", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("每次运行有10%概率破坏基岩", "Each run has 10% chance to destroy bedrock", StyleBuilder::setRed), p -> p, StyleBuilder::setOneTab))))
            .overclock()
            .block(GTOBlocks.ECHO_CASING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("aaaaaaaaaaa", "a         a", "a         a", "ab       ba", "a         a", "a         a", "a         a", "a    b    a", "aaaaaaaaaaa")
                    .aisle("a         a", "           ", "           ", "bbb     bbb", "           ", "           ", "           ", "     b     ", "a    b    a")
                    .aisle("a         a", "           ", "           ", " bbbbcbbb  ", "    ccc    ", "    ccc    ", "    ccc    ", "    ccc    ", "a   bbb   a")
                    .aisle("a         a", "     c     ", "     c     ", "  bbcccbb  ", "   ccdcc   ", "   ccdcc   ", "   ccdcc   ", "   ccdcc   ", "a  bcccb  a")
                    .aisle("a    c    a", "    cdc    ", "    cdc    ", "  bccdccb  ", "  ccedecc  ", "  ccefecc  ", "  cceeecc  ", "  cceeecc  ", "a bcccccb a")
                    .aisle("a   cgc   a", "   cdfdc   ", "   cdfdc   ", "  ccdfdcc  ", "  cddfddc  ", "  cdfffdc  ", "  cdefedc  ", "bbcdefedcbb", "abbcc~ccbba")
                    .aisle("a    c    a", "    cdc    ", "    cdc    ", "  bccdccb  ", "  ccedecc  ", "  ccefecc  ", "  cceeecc  ", "  cceeecc  ", "a bcccccb a")
                    .aisle("a         a", "     c     ", "     c     ", "  bbcccbb  ", "   ccdcc   ", "   ccdcc   ", "   ccdcc   ", "   ccdcc   ", "a  bcccb  a")
                    .aisle("a         a", "           ", "           ", " bbbbcbbbb ", "    ccc    ", "    ccc    ", "    ccc    ", "    ccc    ", "a   bbb   a")
                    .aisle("a         a", "           ", "           ", "bbb     bbb", "           ", "           ", "           ", "     b     ", "a    b    a")
                    .aisle("aaaaaaaaaaa", "a         a", "a         a", "ab       ba", "a         a", "a         a", "a         a", "a    b    a", "aaaaaaaaaaa")
                    .where('~', controller(blocks(definition.get())))
                    .where('c', blocks(GTOBlocks.ECHO_CASING.get())
                            .or(abilities(EXPORT_ITEMS).setMaxGlobalLimited(1).setPreviewCount(1))
                            .or(abilities(IMPORT_ITEMS).setMaxGlobalLimited(1).setPreviewCount(1))
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(2).setPreviewCount(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('a', blocks(GTOBlocks.OXIDATION_RESISTANT_HASTELLOY_N_MECHANICAL_CASING.get()))
                    .where('b', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.HastelloyX)))
                    .where('d', blocks(GTBlocks.CASING_TITANIUM_PIPE.get()))
                    .where('e', blocks(GCYMBlocks.MOLYBDENUM_DISILICIDE_COIL_BLOCK.get()))
                    .where('f', blocks(GTOBlocks.AMPROSIUM_GEARBOX.get()))
                    .where('g', blocks(GTOBlocks.MACHINE_CASING_GRINDING_HEAD.get()))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/echo_casing"), GTCEu.id("block/multiblock/cleanroom"))
            .register();

    public static final MultiblockMachineDefinition CREATE_AGGREGATION = multiblock("create_aggregation", "创造聚合仪", ElectricMultiblockMachine::new)
            .noneRotation()
            .recipeTypes(GTORecipeTypes.CREATE_AGGREGATION_RECIPES)
            .tooltips(ComponentBuilder.create().addStoryLine("""
                    GTO寰宇格雷科技有限公司的终极项目，创造聚合仪在创造维度启动。
                    董事长凝视着这台跨越维度的机器，它将重塑现实本身的规则。
                    """,
                    """
                            The ultimate project of GTO Universal GregTech Corporation, Create Aggregation activated in creation dimension.
                            The CEO gazed at this trans-dimensional machine that would reshape the very rules of reality itself.
                            """).build())
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("启动条件", "Startup Conditions", StyleBuilder::setGold),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("在创造维度提供MAX级电压", "Provides MAX tier voltage in creation dimension", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("提供MAX级算力", "Provides MAX tier computing power", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("设置电路为1开始运行", "Set circuit to 1 to start running", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab))))
            .noRecipeModifier()
            .block(GTOBlocks.DIMENSION_CONNECTION_CASING)
            .pattern(definition -> MultiBlockFileReader.start(definition)
                    .where('a', blocks(GTOBlocks.DIMENSION_CONNECTION_CASING.get())
                            .or(abilities(IMPORT_ITEMS).setMaxGlobalLimited(1).setPreviewCount(1))
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(2).setPreviewCount(1))
                            .or(abilities(COMPUTATION_DATA_RECEPTION).setExactLimit(1)))
                    .where('b', blocks(GTOBlocks.DIMENSIONAL_BRIDGE_CASING.get()))
                    .where('c', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTOMaterials.Infinity)))
                    .where('d', blocks(GTOBlocks.CREATE_CASING.get()))
                    .where('e', blocks(GTOBlocks.SPACETIME_COMPRESSION_FIELD_GENERATOR.get()))
                    .where('f', blocks(GTOBlocks.CREATE_AGGREGATIONE_CORE.get()))
                    .where('~', controller(blocks(definition.get())))
                    .where(' ', any())
                    .build())
            .onWorking(machine -> {
                if (machine.getRecipeLogic().getProgress() == 19) {
                    Level level = machine.self().getLevel();
                    if (level != null) {
                        BlockPos pos = machine.self().getPos().offset(0, -16, 0);
                        Block block = level.getBlockState(pos).getBlock();
                        if (MachineUtils.inputItem(machine, GTOItems.CHAIN_COMMAND_BLOCK_CORE.asStack()) && block == GTOBlocks.COMMAND_BLOCK_BROKEN.get()) {
                            level.setBlockAndUpdate(pos, Blocks.CHAIN_COMMAND_BLOCK.defaultBlockState());
                        }
                        if (MachineUtils.inputItem(machine, GTOItems.REPEATING_COMMAND_BLOCK_CORE.asStack()) && block == GTOBlocks.CHAIN_COMMAND_BLOCK_BROKEN.get()) {
                            level.setBlockAndUpdate(pos, Blocks.REPEATING_COMMAND_BLOCK.defaultBlockState());
                        }
                    }
                }
                return true;
            })
            .workableCasingRenderer(GTOCore.id("block/casings/dimension_connection_casing"), GTOCore.id("block/multiblock/create_aggregation"))
            .register();

    public static final MultiblockMachineDefinition NYARLATHOTEPS_TENTACLE = multiblock("nyarlathoteps_tentacle", "奈亚拉托提普之触", CoilCrossRecipeMultiblockMachine::createCoilParallel)
            .allRotation()
            .recipeTypes(GTORecipeTypes.SUPRACHRONAL_ASSEMBLY_LINE)
            .tooltips(GTOMachineTranslation.INSTANCE.getNyarlathotepsTentacleTooltips().getSupplier())
            .combinedRecipeTooltips()
            .coilParallelTooltips()
            .laserTooltips()
            .multipleRecipesTooltips()
            .block(GTOBlocks.MOLECULAR_CASING)
            .pattern(definition -> MultiBlockFileReader.start(definition, RelativeDirection.LEFT, RelativeDirection.UP, RelativeDirection.FRONT)
                    .where('A', blocks(GTOBlocks.MOLECULAR_CASING.get())
                            .or(GTOPredicates.autoThreadLaserAbilities(definition.getRecipeTypes()))
                            .or(abilities(OPTICAL_DATA_RECEPTION).setExactLimit(1)))
                    .where('B', blocks(GTOBlocks.ADVANCED_FUSION_COIL.get()))
                    .where('C', blocks(GTOBlocks.LASER_CASING.get()))
                    .where('D', blocks(GTOBlocks.URUIUM_COIL_BLOCK.get()))
                    .where('E', blocks(GTOBlocks.HYPER_CORE.get()))
                    .where('F', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTOMaterials.Amprosium)))
                    .where('G', blocks(GTOBlocks.SPACETIME_ASSEMBLY_LINE_CASING.get()))
                    .where('H', blocks(GTOBlocks.EXTREME_DENSITY_CASING.get()))
                    .where('I', blocks(GTBlocks.FUSION_GLASS.get()))
                    .where('J', blocks(GTOBlocks.LASER_COOLING_CASING.get()))
                    .where('K', blocks(GTOBlocks.QUANTUM_GLASS.get()))
                    .where('L', blocks(GTOBlocks.NAQUADAH_ALLOY_CASING.get()))
                    .where('M', blocks(GTOBlocks.SPS_CASING.get()))
                    .where('N', blocks(GTOBlocks.SPACETIME_ASSEMBLY_LINE_UNIT.get()))
                    .where('O', blocks(GTOBlocks.MOLECULAR_CASING.get()))
                    .where('P', blocks(GTOBlocks.CONTAINMENT_FIELD_GENERATOR.get()))
                    .where('Q', heatingCoils())
                    .where('R', blocks(GTBlocks.FILTER_CASING_STERILE.get()))
                    .where('S', blocks(GTOBlocks.ELECTRON_PERMEABLE_AMPROSIUM_COATED_GLASS.get()))
                    .where('T', blocks(GTOBlocks.HOLLOW_CASING.get()))
                    .where('U', blocks(GTOBlocks.AMPROSIUM_ACTIVE_CASING.get()))
                    .where('V', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTOMaterials.Mithril)))
                    .where('W', blocks(RegistriesUtils.getBlock("gtceu:ruby_block")))
                    .where('X', blocks(GCYMBlocks.ELECTROLYTIC_CELL.get()))
                    .where('Y', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Trinium)))
                    .where('Z', blocks(GTBlocks.HIGH_POWER_CASING.get()))
                    .where('[', blocks(GTOBlocks.DIMENSIONAL_BRIDGE_CASING.get()))
                    .where('+', blocks(GTOBlocks.RESTRAINT_DEVICE.get()))
                    .where(']', blocks(GTOBlocks.QUARK_PIPE.get()))
                    .where('^', blocks(GTOBlocks.MOLECULAR_COIL.get()))
                    .where('_', blocks(GTOBlocks.DEGENERATE_RHENIUM_CONSTRAINED_CASING.get()))
                    .where('`', blocks(GTOBlocks.DIMENSIONALLY_TRANSCENDENT_CASING.get()))
                    .where('a', controller(blocks(definition.get())))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/molecular_casing"), GTCEu.id("block/multiblock/fusion_reactor"))
            .register();

    public static final MultiblockMachineDefinition PROCESSING_PLANT = multiblock("processing_plant", "通用工厂", ProcessingPlantMachine::new)
            .allRotation()
            .recipeTypes(GTRecipeTypes.DUMMY_RECIPES)
            .eutMultiplierTooltips(0.9)
            .durationMultiplierTooltips(0.8)
            .tooltips(ComponentBuilder.create().addStoryLine("""
                    格雷科技董事长视察工厂时，偶然发现员工们在手动切换机器。
                    他灵机一动，下令建造通用工厂，将所有小机器集成在一起。
                    从此，一座工厂就能完成三十多种加工，效率提升了数倍。
                    """,
                    """
                            The GregTech CEO discovered workers manually switching machines during inspection.
                            He had a brilliant idea to build a processing plant integrating all machines.
                            Since then, one factory could handle over thirty processes with tripled efficiency.
                            """).build())
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("运行要求", "Operating Requirements", StyleBuilder::setGold),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("需要放入对应配方等级的小机器", "Requires corresponding tier small machine", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("无法通过超净维护仓获得洁净环境", "Cannot obtain clean environment through clean maintenance", StyleBuilder::setRed), p -> p, StyleBuilder::setOneTab))))
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("并行加成", "Parallelism Bonus", StyleBuilder::setGold),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("配方等级每高出ULV一级，并行数+2，安装附属模块后+4", "For each tier above ULV, parallelism +2, After installing the auxiliary module +4", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("最终配方等级受限于整体框架等级", "Final recipe tier is constrained by framework tier", StyleBuilder::setRed), p -> p, StyleBuilder::setOneTab))))
            .specialParallelizableTooltips()
            .tooltips(NewDataAttributes.ALLOW_PARALLEL_NUMBER.create(
                    h -> h.addLines("自ULV起，电压每高出1级，获得的并行数+2", "From ULV, each voltage tier increases the obtained parallelism by 2"),
                    c -> c.addCommentLines("公式 : 2 * (tier - 0), 算去吧", "Formula: 2 * (tier - 0), go calculate it yourself")))
            .tooltips(NewDataAttributes.RECIPES_TYPE.create(ProcessingPlantMachine.getComponent()))
            .moduleTooltips()
            .block(GTOBlocks.MULTI_FUNCTIONAL_CASING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("bbb", "bbb", "bbb")
                    .aisle("bbb", "bcb", "bbb")
                    .aisle("bbb", "bab", "bbb")
                    .where('a', controller(blocks(definition.get())))
                    .where('b', blocks(GTOBlocks.MULTI_FUNCTIONAL_CASING.get())
                            .setMinGlobalLimited(14)
                            .or(abilities(GTOPartAbility.ACCELERATE_HATCH).setMaxGlobalLimited(1))
                            .or(abilities(IMPORT_ITEMS))
                            .or(abilities(EXPORT_ITEMS))
                            .or(abilities(IMPORT_FLUIDS))
                            .or(abilities(EXPORT_FLUIDS))
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(4).setPreviewCount(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('c', GTOPredicates.integralFramework())
                    .build())
            .addSubPattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("  BBBA", "  CCCA", "  CCCA")
                    .aisle("  DDDA", "     A", "  EEEA")
                    .aisle("  BBBA", "F CCCA", "  CCCA")
                    .where('A', blocks(GTOBlocks.MULTI_FUNCTIONAL_CASING.get())
                            .or(GTOPredicates.autoIOAbilities(definition.getRecipeTypes()))
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(4))
                            .or(blocks(GTOMachines.CATALYST_HATCH.getBlock()).setMaxGlobalLimited(1))
                            .or(Predicates.blocks(ManaMachine.MANA_AMPLIFIER_HATCH.getBlock()).setMaxGlobalLimited(1)))
                    .where('B', blocks(GTOBlocks.MULTI_FUNCTIONAL_CASING.get()))
                    .where('C', blocks(GTBlocks.CASING_LAMINATED_GLASS.get()))
                    .where('D', blocks(GTBlocks.CASING_STAINLESS_STEEL_GEARBOX.get()))
                    .where('E', blocks(GTBlocks.CASING_GRATE.get()))
                    .where('F', controller(blocks(definition.get())))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/multi_functional_casing"), GTCEu.id("block/multiblock/gcym/large_assembler"))
            .register();

    public static final MultiblockMachineDefinition NANO_FORGE = multiblock("nano_forge", "纳米锻炉", NanoForgeMachine::new)
            .nonYAxisRotation()
            .recipeTypes(GTORecipeTypes.NANO_FORGE_RECIPES)
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("运行条件", "Operating Conditions", StyleBuilder::setGold),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("需要放入对应的纳米蜂群", "Requires corresponding nano swarm", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("三种等级: 碳, 安普洛, 龙", "Three tiers: Carbon, Amprosium, Draconium", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab))))
            .specialParallelizableTooltips()
            .tooltips(NewDataAttributes.ALLOW_PARALLEL_NUMBER.create(
                    h -> h.addLines("对应配方纳米蜂群的数量", "Number of corresponding recipe nano swarms")))
            .laserTooltips()
            .fromSourceTooltips("GTNH")
            .block(GTOBlocks.NAQUADAH_ALLOY_CASING)
            .pattern(definition -> NanoForgeMachine.getBlockPattern(1, definition))
            .shapeInfos(definition -> {
                List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
                for (int i = 1; i < 4; i++) {
                    shapeInfos.addAll(MultiblockDefinition.getMatchingShapes(false, NanoForgeMachine.getBlockPattern(i, definition)));
                }
                return shapeInfos;
            })
            .workableCasingRenderer(GTOCore.id("block/casings/hyper_mechanical_casing"), GTCEu.id("block/multiblock/gcym/large_assembler"))
            .register();

    public static final MultiblockMachineDefinition ISA_MILL = multiblock("isa_mill", "艾萨研磨机", IsaMillMachine::new)
            .allRotation()
            .recipeTypes(GTORecipeTypes.ISA_MILL_RECIPES)
            .tooltips(ComponentBuilder.create().addStoryLine(
                    """
                            在格雷科技的实验室里，一位科学家偶然发现了湿法研磨的奥秘。
                            他在实验室里观察着高速旋转的研磨球，突然意识到
                            通过精确控制研磨介质的湿度，可以大幅提升研磨效率。""",
                    """
                            In the lab of GTO, a scientist accidentally discovered the secret of wet maceration.
                            He observed the rapid rotation of the mill balls in the lab, and he realized
                            that by controlling the humidity of the mill medium, it can significantly increase the efficiency of the mill.
                            """).build())
            .tooltipsText("工业级湿法碾磨", "Industrial Wet macerator")
            .perfectOCTooltips()
            .perfectOverclock()
            .fromSourceTooltips("GTNH")
            .block(GTOBlocks.INCONEL_625_CASING)
            .pattern(definition -> FactoryBlockPattern.start(definition, RelativeDirection.BACK, RelativeDirection.UP, RelativeDirection.RIGHT)
                    .aisle("BBB", "BBB", "BBB")
                    .aisle("BBB", "BCB", "BBB")
                    .aisle("BBB", "BCB", "BBB")
                    .aisle("BBB", "~CB", "BBB")
                    .aisle("BBB", "BCB", "BBB")
                    .aisle("BBB", "BCB", "BBB")
                    .aisle("AAA", "ADA", "AAA")
                    .where('~', controller(blocks(definition.get())))
                    .where('B', blocks(GTOBlocks.INCONEL_625_CASING.get())
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(2).setPreviewCount(1))
                            .or(abilities(IMPORT_ITEMS).setMaxGlobalLimited(4).setPreviewCount(1))
                            .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(1).setPreviewCount(1))
                            .or(abilities(EXPORT_ITEMS).setMaxGlobalLimited(1).setPreviewCount(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1))
                            .or(abilities(MUFFLER).setExactLimit(1)))
                    .where('C', blocks(GTOBlocks.INCONEL_625_GEARBOX.get()))
                    .where('A', blocks(GTOBlocks.INCONEL_625_PIPE.get()))
                    .where('D', blocks(GTOMachines.GRIND_BALL_HATCH.getBlock()))
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/inconel_625_casing"), GTCEu.id("block/multiblock/gcym/large_maceration_tower"))
            .register();

    public static final MultiblockMachineDefinition NEUTRON_ACTIVATOR = multiblock("neutron_activator", "中子活化器", NeutronActivatorMachine::new)
            .nonYAxisRotation()
            .tooltips(ComponentBuilder.create().addStoryLine("""
                    一位格雷科技员工偶然发现了中子的奥秘。
                    他在实验室里观察着高速管道中飞驰的中子流，突然意识到
                    通过精确控制中子动能，可以激活普通材料的原子核。
                    董事长听闻后立即批准了中子活化器项目的研发。
                    如今这台设备能以超光速处理各种核反应配方。
                    """,
                    """
                            A GregTech employee accidentally discovered neutron secrets.
                            Watching neutron streams racing through high-speed pipes in the lab,
                            he realized atomic nuclei could be activated by controlling neutron kinetics.
                            The CEO immediately approved the Neutron Activator project upon hearing this.
                            Now this device processes nuclear reactions at superluminal speeds.
                            """).build())
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("§7超光速运动!", "§7Superluminal Movement!", StyleBuilder::setGold),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("额外高速管道方块提供时间减免", "Additional high-speed pipeline blocks provide time reduction", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("同时降低中子加速器效率", "While lowering neutron accelerator efficiency", StyleBuilder::setRed), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("效率公式: 0.95^额外方块数量", "Efficiency formula: 0.95^Number of Additional Blocks", StyleBuilder::setGray), p -> p, StyleBuilder::setOneTab))))
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(
                    h -> h.addLines("中子动能系统", "Neutron Kinetic Energy System", StyleBuilder::setGold),
                    c -> c.addLines(
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("无中子加速器运行时每秒降低§e72KeV§r", "When no neutron accelerator is running, decreases per second", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab),
                            NewDataAttributes.EMPTY_WITH_POINT.createBuilder(x -> x.addLines("输入石墨/铍粉可立即吸收§e10MeV§r", "Input graphite/beryllium powder can immediately absorb", StyleBuilder::setWhite), p -> p, StyleBuilder::setOneTab))))
            .parallelizableTooltips()
            .fromSourceTooltips("GTNH")
            .recipeTypes(GTORecipeTypes.NEUTRON_ACTIVATOR_RECIPES)
            .block(GTBlocks.CASING_STAINLESS_CLEAN)
            .pattern(definition -> FactoryBlockPattern.start(definition, RelativeDirection.RIGHT, RelativeDirection.BACK, RelativeDirection.UP)
                    .aisle("AAGAA", "ADDDA", "ADDDA", "ADDDA", "AAAAA")
                    .aisle("B   B", " EEE ", " EFE ", " EEE ", "B   B").setRepeatable(4, 100)
                    .aisle("CCCCC", "CDDDC", "CDDDC", "CDDDC", "CCCCC")
                    .where('G', controller(blocks(definition.getBlock())))
                    .where('A', blocks(GTBlocks.CASING_STAINLESS_CLEAN.get())
                            .or(blocks(GTOMachines.NEUTRON_SENSOR.get()).setMaxGlobalLimited(1).setPreviewCount(1))
                            .or(abilities(EXPORT_FLUIDS).setMaxGlobalLimited(1).setPreviewCount(1))
                            .or(abilities(EXPORT_ITEMS).setMaxGlobalLimited(2).setPreviewCount(1))
                            .or(abilities(GTOPartAbility.NEUTRON_ACCELERATOR).setMaxGlobalLimited(2).setPreviewCount(1))
                            .or(abilities(PARALLEL_HATCH).setMaxGlobalLimited(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('B', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Tungsten)))
                    .where('C', blocks(GTBlocks.CASING_STAINLESS_CLEAN.get())
                            .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(1).setPreviewCount(1))
                            .or(abilities(IMPORT_ITEMS).setMaxGlobalLimited(2).setPreviewCount(1)))
                    .where('D', blocks(GTOBlocks.PROCESS_MACHINE_CASING.get()))
                    .where('E', blocks(GTBlocks.CASING_LAMINATED_GLASS.get()))
                    .where('F', GTOPredicates.countBlock("SpeedPipe",
                            GTOBlocks.SPEEDING_PIPE.get()))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"), GTCEu.id("block/multiblock/fusion_reactor"))
            .register();

    public static final MultiblockMachineDefinition HEAT_EXCHANGER = multiblock("heat_exchanger", "热交换机", HeatExchangerMachine::new)
            .allRotation()
            .tooltips(ComponentBuilder.create().addStoryLine("""
                    寰宇格雷科技的工程师们面临着热能浪费的难题。
                    一位资深员工提出了热交换的概念，通过钨钢管道
                    让热流体与冷却液充分接触，实现完美的热量传递。
                    经过无数次试验，他们终于造出了这台神奇的机器。
                    连续运行后还能产出珍贵的高级蒸汽，一举两得。
                    """,
                    """
                            GTO engineers faced the problem of thermal energy waste.
                            A senior employee proposed heat exchange through tungstensteel pipes,
                            allowing hot fluids and coolants to contact for perfect heat transfer.
                            After countless experiments, they finally built this amazing machine.
                            Continuous operation even produces precious high-level steam.
                            """).build())
            .tooltipsText("每次处理全部输入的热流体", "Processes all input hot fluids every time")
            .tooltipsText("需要保证输入的冷却液能将流体全部冷却", "Must ensure the cooling liquid input can cool all fluids")
            .tooltipsText("连续运行4次后将输出高级蒸汽", "it will output high level steam after running continuously 4 times")
            .recipeTypes(GTORecipeTypes.HEAT_EXCHANGER_RECIPES)
            .block(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle(" AAA ", " AAA ", " AAA ", " AAA ", " AAA ", " AAA ")
                    .aisle("AAAAA", "BCCCB", "BCCCB", "BCCCB", "BCCCB", "AAAAA")
                    .aisle("AAAAA", "BDDDB", "BDCDB", "BDCDB", "BDDDB", "AAAAA")
                    .aisle("AAAAA", "BCCCB", "BCCCB", "BCCCB", "BCCCB", "AAAAA")
                    .aisle("AAAAA", "BDDDB", "BDCDB", "BDCDB", "BDDDB", "AAAAA")
                    .aisle("AAAAA", "BCCCB", "BCCCB", "BCCCB", "BCCCB", "AAAAA")
                    .aisle("AAAAA", "BDDDB", "BDCDB", "BDCDB", "BDDDB", "AAAAA")
                    .aisle("AAAAA", "BCCCB", "BCCCB", "BCCCB", "BCCCB", "AAAAA")
                    .aisle(" ASA ", " AAA ", " AAA ", " AAA ", " AAA ", " AAA ")
                    .where('S', controller(blocks(definition.get())))
                    .where('A',
                            blocks(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get()).setMinGlobalLimited(98)
                                    .or(autoAbilities(definition.getRecipeTypes()))
                                    .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('C', blocks(GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()))
                    .where('B', blocks(GTBlocks.CASING_LAMINATED_GLASS.get()))
                    .where('D', frames(GTMaterials.HSSG))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_robust_tungstensteel"),
                    GTCEu.id("block/multiblock/implosion_compressor"))
            .register();

    public static final MultiblockMachineDefinition INFINITY_FLUID_DRILLING_RIG = multiblock("infinity_fluid_drilling_rig", "无尽流体钻机", holder -> new INFFluidDrillMachine(holder, GTValues.ZPM, 256))
            .allRotation()
            .recipeTypes(GTRecipeTypes.DUMMY_RECIPES)
            .tooltips(ComponentBuilder.create().addStoryLine("""
                    GTO寰宇格雷科技有限公司的探索部门又有新发现了！
                    员工们在深层地底发现了永不枯竭的流体矿脉，
                    于是工程师们设计了这台无尽流体钻机。
                    董事长激动地说："这下再也不用担心资源短缺了！
                    从此，公司的流体供应变得源源不断。
                    """,
                    """
                            GTO Universal GregTech's exploration department made a new discovery!
                            Employees found inexhaustible fluid veins deep underground,
                            so engineers designed this Infinity Fluid Drilling Rig.
                            The CEO excitedly said: "No more worrying about resource shortages!
                            Since then, the company's fluid supply became endless.
                            """).build())
            .tooltipsKey("gtceu.machine.fluid_drilling_rig.description")
            .tooltips(NewDataAttributes.EMPTY_WITH_BAR.create(c -> c.addLines("损耗率 : 0", "Deplition Rate : 0", s -> s.setColor(0xEED8AE))))
            .tooltips(NewDataAttributes.VOLTAGE.create(c -> c.addLines(GTValues.VNF[GTValues.ZPM] + " -> " + GTValues.VNF[GTValues.UV])))
            .tooltips(NewDataAttributes.MULTIPLY.create(c -> c.addLines("256x -> 384x")))
            .block(GTOBlocks.IRIDIUM_CASING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("XXX", "#F#", "#F#", "#F#", "###", "###", "###")
                    .aisle("XXX", "FCF", "FCF", "FCF", "#F#", "#F#", "#F#")
                    .aisle("XSX", "#F#", "#F#", "#F#", "###", "###", "###")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(GTOBlocks.IRIDIUM_CASING.get()).setMinGlobalLimited(3)
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(2).setPreviewCount(1))
                            .or(abilities(EXPORT_FLUIDS).setMaxGlobalLimited(1).setPreviewCount(1)))
                    .where('C', blocks(GTOBlocks.IRIDIUM_CASING.get()))
                    .where('F', frames(GTMaterials.Ruridit))
                    .where('#', any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/iridium_casing"), GTCEu.id("block/multiblock/fluid_drilling_rig"))
            .register();

    public static final MultiblockMachineDefinition ADVANCED_ASSEMBLY_LINE = multiblock("advanced_assembly_line", "进阶装配线", AdvancedAssemblyLineMachine::new)
            .allRotation()
            .recipeTypes(GTRecipeTypes.ASSEMBLY_LINE_RECIPES)
            .tooltips(ComponentBuilder.create().addStoryLine("""
                    格雷科技有限公司的全体员工都参与了进阶装配线的设计。
                    机械工程师负责结构，电子工程师处理数据传输，
                    物流专家优化了输入总线的配置。
                    这台机器能够并行处理多个复杂配方，
                    大大提升了公司高端产品的生产效率。
                    """,
                    """
                            All employees of GregTech participated in the Advanced Assembly Line design.
                            Mechanical engineers handled structure, electronic engineers managed data transfer,
                            logistics experts optimized input bus configurations.
                            This machine can process multiple complex recipes in parallel,
                            greatly improving the company's high-end product efficiency.
                            """).build())

            .tooltipsText("可以使用更大的输入总线", "Can use larger input buses")
            .tooltipsText("需要保证每片的物品与配方对应，只能使用数据靶仓", "Must ensure each item corresponds to the recipe, only data target chambers can be used")
            .parallelizableTooltips()
            .laserTooltips()
            .block(GTBlocks.CASING_STEEL_SOLID)
            .pattern(definition -> FactoryBlockPattern.start(definition, RelativeDirection.BACK, RelativeDirection.UP, RelativeDirection.RIGHT)
                    .aisle("FIF", "RTR", "SAG", "#Y#")
                    .aisle("FIF", "RTR", "DAG", "#Y#").setRepeatable(3, 15)
                    .aisle("FOF", "RTR", "DAG", "#Y#")
                    .where('S', controller(blocks(definition.get())))
                    .where('F', blocks(GTBlocks.CASING_STEEL_SOLID.get())
                            .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(4).setPreviewCount(1))
                            .or(abilities(PARALLEL_HATCH).setMaxGlobalLimited(1)))
                    .where('O', abilities(EXPORT_ITEMS).addTooltips(Component.translatable("gtceu.multiblock.pattern.location_end")))
                    .where('Y', blocks(GTBlocks.CASING_STEEL_SOLID.get())
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(2).setPreviewCount(1))
                            .or(abilities(INPUT_LASER).setMaxGlobalLimited(2)))
                    .where('I', abilities(GTOPartAbility.ITEMS_INPUT))
                    .where('G', blocks(GTBlocks.CASING_GRATE.get()))
                    .where('D', blocks(GTBlocks.CASING_GRATE.get())
                            .or(abilities(OPTICAL_DATA_RECEPTION).setExactLimit(1)))
                    .where('A', blocks(GTBlocks.CASING_ASSEMBLY_CONTROL.get()))
                    .where('R', blocks(GTBlocks.CASING_LAMINATED_GLASS.get()))
                    .where('T', blocks(GTOBlocks.ADVANCED_ASSEMBLY_LINE_UNIT.get()))
                    .where('#', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_solid_steel"), GTCEu.id("block/multiblock/assembly_line"))
            .register();

    public static final MultiblockMachineDefinition FISSION_REACTOR = multiblock("fission_reactor", "裂变反应堆", FissionReactorMachine::new)
            .nonYAxisRotation()
            .recipeTypes(GTORecipeTypes.FISSION_REACTOR_RECIPES)
            .tooltips(GTOMachineTranslation.INSTANCE.getFissionReactorTooltips().getSupplier())
            // .tooltipsText("冷却组件必须贴着燃料组件才算有效", "The cooling component must be adjacent to the fuel component to be
            // effective")
            // .tooltipsText("反应堆在运行过程中会根据条件升温，每秒升温值(K)=配方产热x(1+相邻燃料棒数量)", "The reactor will heat up based on conditions
            // during operation, heating value (K) per second = heat generated by recipe x (1 + number of adjacent fuel
            // rods)")
            // .tooltipsText("如果温度高于1500K，反应堆将会损坏；如果损坏达到100%，反应堆爆炸", "If the temperature exceeds 1500K, the reactor will
            // be damaged; if damage reaches 100%, the reactor will explode")
            // .tooltipsText("在运行过程中提供冷却液并根据不同冷却液的冷却液系数来控制温度", "Provide cooling liquid during operation and control the
            // temperature based on different cooling liquid coefficients")
            // .tooltipsText("冷却液系数: 蒸馏水: 800，钠钾合金: 20", "Cooling liquid coefficients: Distilled Water: 800,
            // Sodium-Potassium Alloy: 20")
            // .tooltipsText("反应堆冷却有如下参数:", "Reactor cooling has the following parameters:")
            // .tooltipsText("最低冷却液需求量和最高冷却液供给量", "Minimum cooling liquid demand and maximum cooling liquid supply")
            // .tooltipsText("最低需求量=配方产热x实际并行数量x当前温度/1500", "Minimum Demand = Recipe Generated Heat x Actual Parallelism
            // x Current Temperature / 1500")
            // .tooltipsText("最高供给量=(冷却组件-(相邻数/3))x8", "Maximum Supply = (Cooling Components - (Adjacent Count / 3)) x
            // 8")
            // .tooltipsText("当供给量>=需求量时达到消耗冷却液条件，消耗提供的冷却液，消耗量为需求量x冷却液系数，并阻止反应堆升温", "When supply >= demand, cooling
            // liquid consumption conditions are met, consume provided cooling liquid, consumption amount = demand x
            // cooling liquid coefficient, and prevent the reactor from heating up")
            // .tooltipsText("如果供给量是需求量的n倍，则执行特殊超频，超频次数为n", "If the supply amount is n times the demand amount, a
            // special overclocking will be executed, overclocking times = n")
            // .tooltipsText("再次消耗需求量的冷却液，并减少1秒时间，如果无法供给冷却液则中断超频，如果进度到达100%则中断超频并消耗一次需求量的冷却液使温度降低1K", "Consumes one
            // demand amount of cooling liquid again, reducing the time by 1 second. If cooling liquid cannot be
            // supplied, overclocking is interrupted. If progress reaches 100%, overclocking is interrupted and one
            // demand amount of cooling liquid is consumed to reduce temperature by 1K")
            // .tooltipsText("由蒸馏水作为冷却液时将产生蒸汽，产生量: 消耗量xmin(160,160/(1.4^(373-温度)))", "Using distilled water as cooling
            // liquid will generate steam, amount generated: consumption x min(160, 160/(1.4^(373-temperature)))")
            // .tooltipsText("由钠钾合金作为冷却液时产生热钠钾合金，产生量=消耗量，如果温度高于825K则产生同等量的超临界钠钾合金", "Using sodium-potassium alloy as
            // cooling liquid will produce hot sodium-potassium alloy, amount generated = consumption. If the
            // temperature exceeds 825K, an equal amount of supercritical sodium-potassium alloy is produced")
            // .tooltipsText("无论反应堆是否具有消耗冷却液的条件都能执行配方", "Recipes can be executed regardless of whether the reactor
            // consumes cooling liquid")
            // .tooltipsText("反应堆停止工作后温度将每秒降低1K", "The temperature of the reactor will decrease by 1K every second after
            // it stops working")
            .specialParallelizableTooltips()
            .tooltips(NewDataAttributes.ALLOW_PARALLEL_NUMBER.create(CNEN.create("等于燃料组件数量", "Number of Fuel Components")).get().toArray(new Component[0]))
            .block(GTOBlocks.FISSION_REACTOR_CASING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("AAAAAAAAA", "ABBBBBBBA", "ABBBBBBBA", "ABBBBBBBA", "ABBBBBBBA", "ABBBBBBBA", "ABBBBBBBA", "ABBBBBBBA", "AAAAAAAAA")
                    .aisle("AAAAAAAAA", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "AAAAAAAAA")
                    .aisle("AAAAAAAAA", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "AAAAAAAAA")
                    .aisle("AAAAAAAAA", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "AAAAAAAAA")
                    .aisle("AAAAAAAAA", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "AAAAAAAAA")
                    .aisle("AAAAAAAAA", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "AAAAAAAAA")
                    .aisle("AAAAAAAAA", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "AAAAAAAAA")
                    .aisle("AAAAAAAAA", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "BCCCCCCCB", "AAAAAAAAA")
                    .aisle("AAAA~AAAA", "ABBBBBBBA", "ABBBBBBBA", "ABBBBBBBA", "ABBBBBBBA", "ABBBBBBBA", "ABBBBBBBA", "ABBBBBBBA", "AAAAAAAAA")
                    .where('~', controller(blocks(definition.get())))
                    .where('A', blocks(GTOBlocks.FISSION_REACTOR_CASING.get())
                            .or(blocks(GTOMachines.HEAT_SENSOR.getBlock()).setMaxGlobalLimited(1).setPreviewCount(1))
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(2).setPreviewCount(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1))
                            .or(abilities(IMPORT_ITEMS).setMaxGlobalLimited(1).setPreviewCount(1))
                            .or(abilities(EXPORT_ITEMS).setMaxGlobalLimited(1).setPreviewCount(1))
                            .or(abilities(EXPORT_FLUIDS).setMaxGlobalLimited(1).setPreviewCount(1))
                            .or(abilities(IMPORT_FLUIDS).setMaxGlobalLimited(1).setPreviewCount(1)))
                    .where('B', blocks(GTBlocks.CASING_LAMINATED_GLASS.get()).or(blocks(GTOBlocks.FISSION_REACTOR_CASING.get())))
                    .where('C', air().or(GTOPredicates.fissionComponent()))
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/fission_reactor_casing"), GTCEu.id("block/multiblock/fusion_reactor"))
            .register();

    public static final MultiblockMachineDefinition SPACE_ELEVATOR = multiblock("space_elevator", "太空电梯", SpaceElevatorMachine::new)
            .nonYAxisRotation()
            .recipeTypes(GTRecipeTypes.DUMMY_RECIPES)
            .tooltips(ComponentBuilder.create().addStoryLine("""
                    GTO寰宇格雷科技有限公司的工程师们仰望星空，梦想着触及宇宙的边界。
                    经过数十年的研发，他们终于建成了人类历史上第一座太空电梯。
                    这座高耸入云的巨塔承载着无数人的梦想，连接着地球与深空。
                    董事长在落成典礼上激动地说道："今天，我们不再被重力束缚。
                    从此，员工们可以轻松地将物资运送到太空站，开启了星际时代。
                    人类终于迈出了征服宇宙的第一步，未来的无限可能在此展开。
                    """,
                    """
                            GTO engineers gazed at the stars, dreaming of touching the universe's edge.
                            After decades of development, they built humanity's first space elevator.
                            This towering structure carries countless dreams, connecting Earth to deep space.
                            The CEO excitedly declared at the ceremony: "Today, we're no longer bound by gravity.
                            Employees can now easily transport materials to space stations, beginning the stellar age.
                            Humanity finally took its first step toward conquering the universe, opening infinite possibilities.
                            """).build())
            .tooltipsText("可安装最多12个拓展模块", "Can install up to 12 expansion modules")
            .tooltipsText("提升电压等级可为模块提供耗时减免", "Increasing voltage tier can provide Duration reductions for modules")
            .tooltipsText("运行前需提供128*(机器等级-7)的算力", "Before starting, it is necessary to provide 128 * (tier - 7) computation power.")
            .fromSourceTooltips("GTNH")
            .block(GTOBlocks.SPACE_ELEVATOR_MECHANICAL_CASING)
            .pattern(definition -> MultiBlockFileReader.start(definition, RelativeDirection.RIGHT, RelativeDirection.UP, RelativeDirection.BACK)
                    .where('~', controller(blocks(definition.get())))
                    .where('A', blocks(GTOBlocks.HIGH_STRENGTH_CONCRETE.get()))
                    .where('B', blocks(GTOBlocks.SPACE_ELEVATOR_INTERNAL_SUPPORT.get()))
                    .where('C', blocks(GTOBlocks.SPACE_ELEVATOR_MECHANICAL_CASING.get()))
                    .where('D', blocks(GTOBlocks.SPACE_ELEVATOR_SUPPORT.get()))
                    .where('E', frames(GTMaterials.Neutronium))
                    .where('F', blocks(GTOBlocks.SPACE_ELEVATOR_MODULE_BASE.get()))
                    .where('G', blocks(GTOBlocks.HIGH_STRENGTH_CONCRETE.get()).or(blocks(GTOBlocks.MODULE_CONNECTOR.get()).setPreviewCount(1)))
                    .where('H', GTOPredicates.tierBlock(SEPMMAP, POWER_MODULE_TIER))
                    .where('I', air())
                    .where('J', blocks(GTOBlocks.SPACE_ELEVATOR_POWER_CORE.get()))
                    .where('X', blocks(GTOBlocks.SPACE_ELEVATOR_MECHANICAL_CASING.get())
                            .or(abilities(GTOPartAbility.ITEMS_INPUT).setExactLimit(1))
                            .or(abilities(INPUT_ENERGY).setExactLimit(1))
                            .or(abilities(COMPUTATION_DATA_RECEPTION).setExactLimit(1)))
                    .where(' ', any())
                    .build())
            .renderer(SpaceElevatorRenderer::new)
            .hasTESR(true)
            .register();

    public static final MultiblockMachineDefinition SLAUGHTERHOUSE = multiblock("slaughterhouse", "工业屠宰场", SlaughterhouseMachine::new)
            .nonYAxisRotation()
            .tooltipsText("电动刷怪塔，自动杀怪", "Electric Spawner, automatically kills mobs")
            .tooltipsText("电压等级每高出MV1级，每次处理次数+8", "Voltage tier above MV1 increases the number of processes by 8 each time")
            .tooltipsText("运行前需设置电路，1号电路为非敌对生物，2号为敌对生物", "Circuit must be set up before running; Circuit 1 is for non-hostile mobs, 2 is for hostile mobs")
            .tooltipsText("如果在机器GUI内放置了电动刷怪笼则只会刷出刷怪笼里的内容", "If an electric spawner is placed in the machine GUI, only the contents of the spawner will spawn")
            .tooltipsText("实体生成模式为玩家击杀的实际掉落，性能较差，可获取经验", "Entity generation mode is based on actual drops from player kills, performance is lower, can gain experience")
            .tooltipsText("非实体生成模式为虚拟掉落，如果存在刷怪笼则使用并行处理", "Non-entity generation mode is virtual drops; if a spawner exists, it uses parallel processing")
            .recipeTypes(GTRecipeTypes.DUMMY_RECIPES)
            .block(GTBlocks.CASING_STEEL_SOLID)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("AAAAAAA", "AAAAAAA", "ABBBBBA", "ABBBBBA", "ABBBBBA", "ABBBBBA", "ABBBBBA", "ABBBBBA", "ABBBBBA", "AAAAAAA")
                    .aisle("AAAAAAA", "ACCCCCA", "BDDDDDB", "BDDDDDB", "BDDDDDB", "BDDDDDB", "BDDDDDB", "BDDDDDB", "BEEEEEB", "AAAAAAA")
                    .aisle("AAAAAAA", "ACCCCCA", "BD   DB", "BD   DB", "BD   DB", "BD   DB", "BD   DB", "BD   DB", "BEEEEEB", "AAAAAAA")
                    .aisle("AAAAAAA", "ACCCCCA", "BD   DB", "BD   DB", "BD   DB", "BD   DB", "BD   DB", "BD   DB", "BEEEEEB", "AAAAAAA")
                    .aisle("AAAAAAA", "ACCCCCA", "BD   DB", "BD   DB", "BD   DB", "BD   DB", "BD   DB", "BD   DB", "BEEEEEB", "AAAAAAA")
                    .aisle("AAAAAAA", "ACCCCCA", "BDDDDDB", "BDDDDDB", "BDDDDDB", "BDDDDDB", "BDDDDDB", "BDDDDDB", "BEEEEEB", "AAAAAAA")
                    .aisle("AAAAAAA", "AAA~AAA", "ABBBBBA", "ABBBBBA", "ABBBBBA", "ABBBBBA", "ABBBBBA", "ABBBBBA", "ABBBBBA", "AAAAAAA")
                    .where('~', controller(blocks(definition.get())))
                    .where('A', blocks(GTBlocks.CASING_STEEL_SOLID.get())
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(2).setPreviewCount(1))
                            .or(abilities(EXPORT_ITEMS).setMaxGlobalLimited(4).setPreviewCount(1))
                            .or(abilities(EXPORT_FLUIDS).setMaxGlobalLimited(1).setPreviewCount(1))
                            .or(abilities(IMPORT_ITEMS).setMaxGlobalLimited(1).setPreviewCount(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('B', blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                    .where('C', blocks(GTBlocks.CASING_STEEL_GEARBOX.get()))
                    .where('D', blocks(Blocks.IRON_BARS))
                    .where('E', blocks(GTBlocks.FIREBOX_STEEL.get()))
                    .where(' ', air())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_solid_steel"), GTCEu.id("block/multiblock/gcym/large_cutter"))
            .register();

    public static final MultiblockMachineDefinition[] FUSION_REACTOR = registerTieredMultis("fusion_reactor", tier -> "核聚变反应堆控制电脑 MK-" + FormattingUtil.toRomanNumeral(tier - 5),
            FusionReactorMachine::new, (tier, builder) -> builder
                    .allRotation()
                    .langValue("Fusion Reactor Computer MK %s".formatted(FormattingUtil.toRomanNumeral(tier - 5)))
                    .recipeTypes(GTRecipeTypes.FUSION_RECIPES)
                    .tooltipsKey("gtceu.machine.fusion_reactor.capacity", FusionReactorMachine.calculateEnergyStorageFactor(tier, 16) / 1000000L)
                    .tooltipsKey("gtceu.machine.fusion_reactor.overclocking")
                    .tooltipsKey("gtocore.machine.%s_fusion_reactor.description".formatted(GTValues.VN[tier].toLowerCase(Locale.ROOT)))
                    .block(() -> FusionCasings.getCasingState(tier))
                    .pattern(definition -> {
                        TraceabilityPredicate casing = blocks(FusionCasings.getCasingState(tier));
                        return FactoryBlockPattern.start(definition)
                                .aisle("###############", "######OGO######", "###############")
                                .aisle("######ICI######", "####GGAAAGG####", "######ICI######")
                                .aisle("####CC###CC####", "###EAAOGOAAE###", "####CC###CC####")
                                .aisle("###C#######C###", "##EKEG###GEKE##", "###C#######C###")
                                .aisle("##C#########C##", "#GAE#######EAG#", "##C#########C##")
                                .aisle("##C#########C##", "#GAG#######GAG#", "##C#########C##")
                                .aisle("#I###########I#", "OAO#########OAO", "#I###########I#")
                                .aisle("#C###########C#", "GAG#########GAG", "#C###########C#")
                                .aisle("#I###########I#", "OAO#########OAO", "#I###########I#")
                                .aisle("##C#########C##", "#GAG#######GAG#", "##C#########C##")
                                .aisle("##C#########C##", "#GAE#######EAG#", "##C#########C##")
                                .aisle("###C#######C###", "##EKEG###GEKE##", "###C#######C###")
                                .aisle("####CC###CC####", "###EAAOGOAAE###", "####CC###CC####")
                                .aisle("######ICI######", "####GGAAAGG####", "######ICI######")
                                .aisle("###############", "######OSO######", "###############")
                                .where('S', controller(blocks(definition.get())))
                                .where('G', blocks(GTBlocks.FUSION_GLASS.get()).or(casing))
                                .where('E', casing.or(
                                        blocks(INPUT_ENERGY.getBlockRange(tier, GTValues.UEV).toArray(Block[]::new))
                                                .setMinGlobalLimited(1).setPreviewCount(16)))
                                .where('C', casing)
                                .where('K', blocks(FusionCasings.getCoilState(tier)))
                                .where('O', casing.or(abilities(EXPORT_FLUIDS)))
                                .where('A', air())
                                .where('I', casing.or(abilities(IMPORT_FLUIDS).setMinGlobalLimited(2)))
                                .where('#', any())
                                .build();
                    })
                    .shapeInfos((controller) -> {
                        List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();

                        MultiblockShapeInfo.ShapeInfoBuilder baseBuilder = MultiblockShapeInfo.builder()
                                .aisle("###############", "######NMN######", "###############")
                                .aisle("######DCD######", "####GG###GG####", "######UCU######")
                                .aisle("####CC###CC####", "###w##SGS##e###", "####CC###CC####")
                                .aisle("###C#######C###", "##nKsG###GsKn##", "###C#######C###")
                                .aisle("##C#########C##", "#G#e#######w#G#", "##C#########C##")
                                .aisle("##C#########C##", "#G#G#######G#G#", "##C#########C##")
                                .aisle("#D###########D#", "W#E#########W#E", "#U###########U#")
                                .aisle("#C###########C#", "G#G#########G#G", "#C###########C#")
                                .aisle("#D###########D#", "W#E#########W#E", "#U###########U#")
                                .aisle("##C#########C##", "#G#G#######G#G#", "##C#########C##")
                                .aisle("##C#########C##", "#G#e#######w#G#", "##C#########C##")
                                .aisle("###C#######C###", "##sKnG###GnKs##", "###C#######C###")
                                .aisle("####CC###CC####", "###w##NGN##e###", "####CC###CC####")
                                .aisle("######DCD######", "####GG###GG####", "######UCU######")
                                .aisle("###############", "######SGS######", "###############")
                                .where('M', controller, Direction.NORTH)
                                .where('C', FusionCasings.getCasingState(tier))
                                .where('G', GTBlocks.FUSION_GLASS.get())
                                .where('K', FusionCasings.getCoilState(tier))
                                .where('W', GTMachines.FLUID_EXPORT_HATCH[tier], Direction.WEST)
                                .where('E', GTMachines.FLUID_EXPORT_HATCH[tier], Direction.EAST)
                                .where('S', GTMachines.FLUID_EXPORT_HATCH[tier], Direction.SOUTH)
                                .where('N', GTMachines.FLUID_EXPORT_HATCH[tier], Direction.NORTH)
                                .where('w', GTMachines.ENERGY_INPUT_HATCH[tier], Direction.WEST)
                                .where('e', GTMachines.ENERGY_INPUT_HATCH[tier], Direction.EAST)
                                .where('s', GTMachines.ENERGY_INPUT_HATCH[tier], Direction.SOUTH)
                                .where('n', GTMachines.ENERGY_INPUT_HATCH[tier], Direction.NORTH)
                                .where('U', GTMachines.FLUID_IMPORT_HATCH[tier], Direction.UP)
                                .where('D', GTMachines.FLUID_IMPORT_HATCH[tier], Direction.DOWN)
                                .where('#', Blocks.AIR.defaultBlockState());

                        shapeInfos.add(baseBuilder.shallowCopy()
                                .where('G', FusionCasings.getCasingState(tier))
                                .build());
                        shapeInfos.add(baseBuilder.build());
                        return shapeInfos;
                    })
                    .renderer(() -> new FusionReactorRenderer(FusionCasings.getCasingType(tier).getTexture(),
                            GTCEu.id("block/multiblock/fusion_reactor")))
                    .hasTESR(true)
                    .register(),
            GTValues.UHV, GTValues.UEV);

    public static final MultiblockMachineDefinition[] KUANGBIAO_ONE_GIANT_NUCLEAR_FUSION_REACTOR = registerTieredMultis("kuangbiao_one_giant_nuclear_fusion_reactor", tier -> "狂飙" + StringUtils.numberToChinese(tier - 5) + "号巨型聚变反应堆控制电脑",
            AdvancedFusionReactorMachine::new, (tier, builder) -> builder
                    .nonYAxisRotation()
                    .langValue("Advanced Fusion Reactor MK %s".formatted(FormattingUtil.toRomanNumeral(tier - 5)))
                    .recipeTypes(GTRecipeTypes.FUSION_RECIPES)
                    .tooltipsKey("gtocore.machine.kuangbiao_one_giant_nuclear_fusion_reactor.tooltip.0")
                    .tooltipsKey("gtceu.machine.fusion_reactor.capacity", FusionReactorMachine.calculateEnergyStorageFactor(tier, 16) / 1000000L)
                    .tooltipsKey("gtceu.machine.fusion_reactor.overclocking")
                    .parallelizableTooltips()
                    .laserTooltips()
                    .multipleRecipesTooltips()
                    .block(() -> FusionCasings.getCasingState(tier))
                    .pattern(definition -> {
                        TraceabilityPredicate casing = blocks(FusionCasings.getCasingState(tier));
                        return FactoryBlockPattern.start(definition, RelativeDirection.FRONT, RelativeDirection.UP, RelativeDirection.RIGHT)
                                .aisle("            AAAABBBBBBBAAAA            ", "                CDDDDDC                ", "                CEEEEEC                ", "                CEEEEEC                ", "                CEEEEEC                ", "                CCCCCCC                ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ")
                                .aisle("          AAABBBBBBBBBBBBBAAA          ", "                D     D                ", "                E F F E                ", "                E     E                ", "                E     E                ", "                CEEEEEC                ", "                  AAA                  ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ")
                                .aisle("         AABBBBBBBBBBBBBBBBBAA         ", "                D     D                ", "                E F F E                ", "                E     E                ", "                E     E                ", "                CEEEEEC                ", "                  AAA                  ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ")
                                .aisle("       AAABBBBBBBBBBBBBBBBBBBAAA       ", "                D     D                ", "                E F F E                ", "                E     E                ", "                E     E                ", "                CEEEEEC                ", "                  AAA                  ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ")
                                .aisle("      AABBBBBBBBGGHHHGGBBBBBBBBAA      ", "                D     D                ", "                E F F E                ", "                E     E                ", "                E     E                ", "                CEEEEEC                ", "                 AAAAA                 ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ")
                                .aisle("     AABBBBBBGGGGGHHHGGGGGBBBBBBAA     ", "       CI    GGGE     EGGG    IC       ", "       CI    GGGE F F EGGG    IC       ", "       CI       E     E       IC       ", "       CI       E     E       IC       ", "       CI       CEEEEEC       IC       ", "       CI        AAAAA        IC       ", "       CI                     IC       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ")
                                .aisle("    AABBBBBGGGGGGGHHHGGGGGGGBBBBBAA    ", "       I I GGGGGE     EGGGGG I I       ", "       I I GGGGGE F F EGGGGG I I       ", "       I I      E     E      I I       ", "       I I      E     E      I I       ", "       I I      CEEEEEC      I I       ", "       I I      AAAAAAA      I I       ", "       IJI                   IJI       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ")
                                .aisle("   AABBBBBGGGGGGGGHHHGGGGGGGGBBBBBAA   ", "     CI   GGGGGGAAAAAAAGGGGGG   IC     ", "     CI   GGGGGGAAAAAAAGGGGGG   IC     ", "     CI   I     AAAAAAA     I   IC     ", "     CI   I     AAAAAAA     I   IC     ", "     CI   I     AAAAAAA     I   IC     ", "     CI   I     AAAAAAA     I   IC     ", "     CIJJJI     AAAAAAA     IJJJIC     ", "                AKKKKKA                ", "                LKKKKKL                ", "                LLLLLLL                ", "                LLLLLLL                ", "                LLLLLLL                ", "                FFFFFFF                ", "                                       ", "                                       ", "                                       ")
                                .aisle("   ABBBBBGGGGGGGGGHHHGGGGGGGGGBBBBBA   ", "     I   GGGGGAAIIIIIIIAAGGGGG   I     ", "     I   GGGGGAAIIIIIIIAAGGGGG   I     ", "     I     I  AA       AA  I     I     ", "     I     I  AA       AA  I     I     ", "     I     HHHAA       AAHHH     I     ", "     I     I HAA       AAH I     I     ", "     IJJJJJI HAAEEEEEEEAAH IJJJJJI     ", "             HAAEKKKKKEAAH             ", "             HLLEKKKKKELLH             ", "             HLLEEEEEEELLH             ", "             HLL       LLH             ", "             HLL       LLH             ", "             HFFLLLLLLLFFH             ", "             HHHH     HHHH             ", "                H     H                ", "                                       ")
                                .aisle("  AABBBBGGGGGGGGGGHHHGGGGGGGGGGBBBBAA  ", "      I GGGGGAII   I   IIAGGGGG I      ", "      I GGGGGAII   I   IIAGGGGG I      ", "      I     IA           AI     I      ", "      I     IA           AI     I      ", "      I     IA  EEEEEEE  AI     I      ", "      I     IA  EEEEEEE  AI     I      ", "      IJJJJIIAEE       EEAIIJJJJI      ", "             AEE       EEA             ", "             LEE       EEL             ", "             LEE       EEL             ", "             LLLEEEEEEELLL             ", "             LLL       LLL             ", "             FLLLLLLLLLLLF             ", "                                       ", "                H     H                ", "                                       ")
                                .aisle(" AABBBBGGGGGGGGGGGHHHGGGGGGGGGGGBBBBAA ", "       GGGGAAI I   I   I IAAGGGG       ", "       GGGGAAI I   I   I IAAGGGG       ", "       I   AA             AA   I       ", "       I   AA   EEEEEEE   AA   I       ", "       I   AA EEHHHHHHHEE AA   I       ", "       I   AA EE       EE AA   I       ", "       IJJIAAE           EAAIJJI       ", "           AAE           EAA           ", "           LLE           ELL           ", "           LLE           ELL           ", "           LL EEHHHHHHHEE LL           ", "           LL   EEEEEEE   LL           ", "           FFLLL       LLLFF           ", "               LLLLLLLLL               ", "                H     H                ", "                                       ")
                                .aisle(" ABBBBGGGGGGGGGGGGHHHGGGGGGGGGGGGBBBBA ", "      GGGGAII  I   I   I  IIAGGGG      ", "      GGGGAII  I   I   I  IIAGGGG      ", "        I A     EEEEEEE     A I        ", "        I A   EE       EE   A I        ", "        I A  EHH       HHE  A I        ", "        I A  E           E  A I        ", "        IIAEE             EEAII        ", "          AEE             EEA          ", "          LEE             EEL          ", "          LEE             EEL          ", "          L  EHH       HHE  L          ", "          L   EE       EE   L          ", "          FLLLLLEEEEEEE LLLLF          ", "              LLLLLLLLLLL              ", "                H     H                ", "                                       ")
                                .aisle("AABBBBGGGGGGGGGGGGHHHGGGGGGGGGGGGBBBBAA", "      GGGGAI   I   I   I   IAGGGG      ", "      GGGGAI   I   I   I   IAGGGG      ", "         IA   EEEEEEEEEEE   AI         ", "         IA  E           E  AI         ", "         IA EH           HE AI         ", "         IA E             E AI         ", "         IAE               EAI         ", "          AE               EA          ", "          LE               EL          ", "          LE               EL          ", "          L EH           HE L          ", "          L  E           E  L          ", "          FLLLEEEEEEEEEEELLLF          ", "             LLLLLEEELLLLL             ", "                HFFFFFH                ", "                FFFFFFF                ")
                                .aisle("ABBBBGGGGGGGGGGGGGHHHGGGGGGGGGGGGGBBBBA", "     GGGGAI    I   I   I    IAGGGG     ", "     GGGGAI    I   I   I    IAGGGG     ", "         A   EEEEEEEEEEEEE   A         ", "         A  E             E  A         ", "         A EH             HE A         ", "         A E               E A         ", "         AE                 EA         ", "         AE                 EA         ", "         LE                 EL         ", "         LE                 EL         ", "         L EH             HE L         ", "         L  E             E  L         ", "         FLLLEEEEEEEEEEEEELLLF         ", "            LLLLLEEEEELLLLL            ", "               FHIIIIIHF               ", "               FAAAAAAAF               ")
                                .aisle("ABBBBGGGGGGGGGGGGGHHHGGGGGGGGGGGGGBBBBA", "     GGGAI     I   I   I     IAGGG     ", "     GGGAI     I   I   I     IAGGG     ", "        A   EEEEE     EEEEE   A        ", "        A  E     EEEEE     E  A        ", "        A EH     EEEEE     HE A        ", "        A E      EEEEE      E A        ", "        AE       EEEEE       EA        ", "        AE       EEEEE       EA        ", "        LE       EEEEE       EL        ", "        LE       EEEEE       EL        ", "        LLEH     EEEEE     HELL        ", "        LL E     EEEEE     E LL        ", "        FLLLEEEEE     EEEEELLLF        ", "           LLLLLEEEEEEELLLLL           ", "              FIIIIIIIIIF              ", "              FAAFHFHFAAF              ")
                                .aisle("ABBBBGGGGGGGGGGGGGHHHGGGGGGGGGGGGGBBBBA", "     GGGAIIIIIIIIIIIIIIIIIIIIIAGGG     ", "     GGGAIIIIIIIIIIIIIIIIIIIIIAGGG     ", "        A   EEEE IIIII EEEE   A        ", "        A  E    EIIIIIE    E  A        ", "        A EH    EIIIIIE    HE A        ", "        A E     EIIIIIE     E A        ", "        AE      EIIIIIE      EA        ", "        AE      EIIIIIE      EA        ", "        LE      EIIIIIE      EL        ", "        LE      EIIIIIE      EL        ", "        LLEH    EIIIIIE    HELL        ", "        LL E    EIIIIIE    E LL        ", "        FLL EEEE IIIII EEEELLLF        ", "          LLLLLEEEEEEEEELLLLL          ", "             FIIIIIIIIIIIF             ", "             FAA FHFHF AAF             ")
                                .aisle("BBBBGGGGGGGGGGGGGGHHHGGGGGGGGGGGGGGBBBB", "CDDDDEEAI      II  I  II      IAEEDDDDC", "CEEEEEEAI      II  I  II      IAEEEEEEC", "CEEEEEEA   EEEE I     I EEEE   AEEEEEEC", "CEEEEEEA  E    EI     IE    E  AEEEEEEC", "CCCCCCCA EH    EI     IE    HE ACCCCCCC", "      AA E     EI     IE     E AA      ", "       AE      EI     IE      EA       ", "       AE      EI     IE      EA       ", "       LE      EI     IE      EL       ", "       LE      EI     IE      EL       ", "       L EH    EI     IE    HE L       ", "       L  E    EI     IE    E  L       ", "       FLL EEEE I     I EEEE LLF       ", "          LLLLEEEEEEEEEEELLLL          ", "            FIIIIIIIIIIIIIF            ", "            FAA  FHFHF  AAF            ")
                                .aisle("BBBBGGGGGGGGGGGGGGEEEGGGGGGGGGGGGGGBBBB", "D      AI      I  HHH  I      IA      D", "E      AI      I  HHH  I      IA      D", "E      A   EEE I  HHH  I EEE   A      D", "E      A  E   EI  HHH  IE   E  A      D", "CEEEEEEA EH   EI  HHH  IE   HE AEEEEEEC", "    AAAA E    EI  HHH  IE    E AAAA    ", "       AE     EI  HHH  IE     EA       ", "       KK     EI  HHH  IE     KK       ", "       KK     EI  HHH  IE     KK       ", "       LE     EI  HHH  IE     EL       ", "       L EH   EI  HHH  IE   HE L       ", "       L  E   EI  HHH  IE   E  L       ", "       FLL EEE I  HHH  I EEE LLF       ", "          LLLEEEEEEEEEEEEELLL          ", "            FIIIIFFFFFIIIIF            ", "            FA   FHFHF   AF            ")
                                .aisle("BBBBHHHHHHHHHHHHHEAAAEHHHHHHHHHHHHHBBBB", "D      AI      I H H H I      IA      D", "EFFFFFFAI      I H H H I      IAFFFFFFD", "E      A   EEE I H H H I EEE   A      D", "E      A  E   EI H H H IE   E  A      D", "CEEEEEEA EH   EI H H H IE   HE AEEEEEEC", " AAAAAAA E    EI H H H IE    E AAAAAAA ", "       AE     EI H H H IE     EA       ", "       KK     EI H H H IE     KK       ", "       KK     EI H H H IE     KK       ", "       LE     EI H H H IE     EL       ", "       L EH   EI H H H IE   HE L       ", "       L  E   EI H H H IE   E  L       ", "       FLL EEE I HHHHH I EEE LLF       ", "          LLEEEEEEHEHEEEEEELL          ", "            FIIIIFHFHFIIIIF            ", "            FA    H H    AF            ")
                                .aisle("BBBBHHHHHHHHHHHHHEAEAEHHHHHHHHHHHHHBBBB", "D      AIIIIIIIIIHHEHHIIIIIIIIIA      D", "E      AIIIIIIIIIHHEHHIIIIIIIIIA      M", "E      A   EEE I HHEHH I EEE   A      D", "E      A  E   EI HHEHH IE   E  A      D", "CEEEEEEA EH   EI HHEHH IE   HE AEEEEEEC", " AAAAAAA E    EI HHEHH IE    E AAAAAAA ", "       AE     EI HHEHH IE     EA       ", "       KK     EI HHEHH IE     KK       ", "       KK     EI HHEHH IE     KK       ", "       LE     EI HHEHH IE     EL       ", "       L EH   EI HHEHH IE   HE L       ", "       L  E   EI HHEHH IE   E  L       ", "       FLL EEE I HHEHH I EEE LLF       ", "          LLEEEEEEEEEEEEEEELL          ", "            FIIIIFFFFFIIIIF            ", "            FAAAAAAAAAAAAAF            ")
                                .aisle("BBBBHHHHHHHHHHHHHEAAAEHHHHHHHHHHHHHBBBB", "D      AI      I H H H I      IA      D", "EFFFFFFAI      I H H H I      IAFFFFFFD", "E      A   EEE I H H H I EEE   A      D", "E      A  E   EI H H H IE   E  A      D", "CEEEEEEA EH   EI H H H IE   HE AEEEEEEC", " AAAAAAA E    EI H H H IE    E AAAAAAA ", "       AE     EI H H H IE     EA       ", "       KK     EI H H H IE     KK       ", "       KK     EI H H H IE     KK       ", "       LE     EI H H H IE     EL       ", "       L EH   EI H H H IE   HE L       ", "       L  E   EI H H H IE   E  L       ", "       FLL EEE I HHHHH I EEE LLF       ", "          LLEEEEEEHEHEEEEEELL          ", "            FIIIIFHFHFIIIIF            ", "            FA    H H    AF            ")
                                .aisle("BBBBGGGGGGGGGGGGGGEEEGGGGGGGGGGGGGGBBBB", "D      AI      I  HHH  I      IA      D", "E      AI      I  HHH  I      IA      D", "E      A   EEE I  HHH  I EEE   A      D", "E      A  E   EI  HHH  IE   E  A      D", "CEEEEEEA EH   EI  HHH  IE   HE AEEEEEEC", "    AAAA E    EI  HHH  IE    E AAAA    ", "       AE     EI  HHH  IE     EA       ", "       KK     EI  HHH  IE     KK       ", "       KK     EI  HHH  IE     KK       ", "       LE     EI  HHH  IE     EL       ", "       L EH   EI  HHH  IE   HE L       ", "       L  E   EI  HHH  IE   E  L       ", "       FLL EEE I  HHH  I EEE LLF       ", "          LLLEEEEEEEEEEEEELLL          ", "            FIIIIFFFFFIIIIF            ", "            FA   FHFHF   AF            ")
                                .aisle("BBBBGGGGGGGGGGGGGGHHHGGGGGGGGGGGGGGBBBB", "CDDDDEEAI      II  I  II      IAEEDDDDC", "CEEEEEEAI      II  I  II      IAEEEEEEC", "CEEEEEEA   EEEE I     I EEEE   AEEEEEEC", "CEEEEEEA  E    EI     IE    E  AEEEEEEC", "CCCCCCCA EH    EI     IE    HE ACCCCCCC", "      AA E     EI     IE     E AA      ", "       AE      EI     IE      EA       ", "       AE      EI     IE      EA       ", "       LE      EI     IE      EL       ", "       LE      EI     IE      EL       ", "       L EH    EI     IE    HE L       ", "       L  E    EI     IE    E  L       ", "       FLL EEEE I     I EEEE LLF       ", "          LLLLEEEEEEEEEEELLLL          ", "            FIIIIIIIIIIIIIF            ", "            FAA  FHFHF  AAF            ")
                                .aisle("ABBBBGGGGGGGGGGGGGHHHGGGGGGGGGGGGGBBBBA", "     GGGAIIIIIIIIIIIIIIIIIIIIIAGGG     ", "     GGGAIIIIIIIIIIIIIIIIIIIIIAGGG     ", "        A   EEEE IIIII EEEE   A        ", "        A  E    EIIIIIE    E  A        ", "        A EH    EIIIIIE    HE A        ", "        A E     EIIIIIE     E A        ", "        AE      EIIIIIE      EA        ", "        AE      EIIIIIE      EA        ", "        LE      EIIIIIE      EL        ", "        LE      EIIIIIE      EL        ", "        LLEH    EIIIIIE    HELL        ", "        LL E    EIIIIIE    E LL        ", "        FLLLEEEE IIIII EEEE LLF        ", "          LLLLLEEEEEEEEELLLLL          ", "             FIIIIIIIIIIIF             ", "             FAA FHFHF AAF             ")
                                .aisle("ABBBBGGGGGGGGGGGGGHHHGGGGGGGGGGGGGBBBBA", "     GGGAI     I   I   I     IAGGG     ", "     GGGAI     I   I   I     IAGGG     ", "        A   EEEEE     EEEEE   A        ", "        A  E     EEEEE     E  A        ", "        A EH     EEEEE     HE A        ", "        A E      EEEEE      E A        ", "        AE       EEEEE       EA        ", "        AE       EEEEE       EA        ", "        LE       EEEEE       EL        ", "        LE       EEEEE       EL        ", "        LLEH     EEEEE     HELL        ", "        LL E     EEEEE     E LL        ", "        FLLLEEEEE     EEEEELLLF        ", "           LLLLLEEEEEEELLLLL           ", "              FIIIIIIIIIF              ", "              FAAFHFHFAAF              ")
                                .aisle("ABBBBGGGGGGGGGGGGGHHHGGGGGGGGGGGGGBBBBA", "     GGGGAI    I   I   I    IAGGGG     ", "     GGGGAI    I   I   I    IAGGGG     ", "         A   EEEEEEEEEEEEE   A         ", "         A  E             E  A         ", "         A EH             HE A         ", "         A E               E A         ", "         AE                 EA         ", "         AE                 EA         ", "         LE                 EL         ", "         LE                 EL         ", "         L EH             HE L         ", "         L  E             E  L         ", "         FLLLEEEEEEEEEEEEELLLF         ", "            LLLLLEEEEELLLLL            ", "               FHIIIIIHF               ", "               FAAAAAAAF               ")
                                .aisle("AABBBBGGGGGGGGGGGGHHHGGGGGGGGGGGGBBBBAA", "      GGGGAI   I   I   I   IAGGGG      ", "      GGGGAI   I   I   I   IAGGGG      ", "         IA   EEEEEEEEEEE   AI         ", "         IA  E           E  AI         ", "         IA EH           HE AI         ", "         IA E             E AI         ", "         IAE               EAI         ", "          AE               EA          ", "          LE               EL          ", "          LE               EL          ", "          L EH           HE L          ", "          L  E           E  L          ", "          FLLLEEEEEEEEEEELLLF          ", "             LLLLLEEELLLLL             ", "                HFFFFFH                ", "                FFFFFFF                ")
                                .aisle(" ABBBBGGGGGGGGGGGGHHHGGGGGGGGGGGGBBBBA ", "      GGGGAII  I   I   I  IIAGGGG      ", "      GGGGAII  I   I   I  IIAGGGG      ", "        I A     EEEEEEE     A I        ", "        I A   EE       EE   A I        ", "        I A  EHH       HHE  A I        ", "        I A  E           E  A I        ", "        IIAEE             EEAII        ", "          AEE             EEA          ", "          LEE             EEL          ", "          LEE             EEL          ", "          L  EHH       HHE  L          ", "          L   EE       EE   L          ", "          FLLLL EEEEEEELLLLLF          ", "              LLLLLLLLLLL              ", "                H     H                ", "                                       ")
                                .aisle(" AABBBBGGGGGGGGGGGHHHGGGGGGGGGGGBBBBAA ", "       GGGGAAI I   I   I IAAGGGG       ", "       GGGGAAI I   I   I IAAGGGG       ", "       I   AA             AA   I       ", "       I   AA   EEEEEEE   AA   I       ", "       I   AA EEHHHHHHHEE AA   I       ", "       I   AA EE       EE AA   I       ", "       IJJIAAE           EAAIJJI       ", "           AAE           EAA           ", "           LLE           ELL           ", "           LLE           ELL           ", "           LL EEHHHHHHHEE LL           ", "           LL   EEEEEEE   LL           ", "           FFLLL       LLLFF           ", "               LLLLLLLLL               ", "                H     H                ", "                                       ")
                                .aisle("  AABBBBGGGGGGGGGGHHHGGGGGGGGGGBBBBAA  ", "      I GGGGGAII   I   IIAGGGGG I      ", "      I GGGGGAII   I   IIAGGGGG I      ", "      I     IA           AI     I      ", "      I     IA           AI     I      ", "      I     IA  EEEEEEE  AI     I      ", "      I     IA  EEEEEEE  AI     I      ", "      IJJJJIIAEE       EEAIIJJJJI      ", "             AEE       EEA             ", "             LEE       EEL             ", "             LEE       EEL             ", "             LLLEEEEEEELLL             ", "             LLL       LLL             ", "             FLLLLLLLLLLLF             ", "                                       ", "                H     H                ", "                                       ")
                                .aisle("   ABBBBBGGGGGGGGGHHHGGGGGGGGGBBBBBA   ", "     I   GGGGGAAIIIIIIIAAGGGGG   I     ", "     I   GGGGGAAIIIIIIIAAGGGGG   I     ", "     I     I  AA       AA  I     I     ", "     I     I  AA       AA  I     I     ", "     I     HHHAA       AAHHH     I     ", "     I     I HAA       AAH I     I     ", "     IJJJJJI HAAEEEEEEEAAH IJJJJJI     ", "             HAAEKKKKKEAAH             ", "             HLLEKKKKKELLH             ", "             HLLEEEEEEELLH             ", "             HLL       LLH             ", "             HLL       LLH             ", "             HFFLLLLLLLFFH             ", "             HHHH     HHHH             ", "                H     H                ", "                                       ")
                                .aisle("   AABBBBBGGGGGGGGHHHGGGGGGGGBBBBBAA   ", "     CI   GGGGGGAAAAAAAGGGGGG   IC     ", "     CI   GGGGGGAAAAAAAGGGGGG   IC     ", "     CI   I     AAAAAAA     I   IC     ", "     CI   I     AAAAAAA     I   IC     ", "     CI   I     AAAAAAA     I   IC     ", "     CI   I     AAAAAAA     I   IC     ", "     CIJJJI     AAAAAAA     IJJJIC     ", "                AKKKKKA                ", "                LKKKKKL                ", "                LLLLLLL                ", "                LLLLLLL                ", "                LLLLLLL                ", "                FFFFFFF                ", "                                       ", "                                       ", "                                       ")
                                .aisle("    AABBBBBGGGGGGGHHHGGGGGGGBBBBBAA    ", "       I I GGGGGE     EGGGGG I I       ", "       I I GGGGGE F F EGGGGG I I       ", "       I I      E     E      I I       ", "       I I      E     E      I I       ", "       I I      CEEEEEC      I I       ", "       I I      AAAAAAA      I I       ", "       IJI                   IJI       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ")
                                .aisle("     AABBBBBBGGGGGHHHGGGGGBBBBBBAA     ", "       CI    GGGE     EGGG    IC       ", "       CI    GGGE F F EGGG    IC       ", "       CI       E     E       IC       ", "       CI       E     E       IC       ", "       CI       CEEEEEC       IC       ", "       CI        AAAAA        IC       ", "       CI                     IC       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ")
                                .aisle("      AABBBBBBBBGGHHHGGBBBBBBBBAA      ", "                D     D                ", "                E F F E                ", "                E     E                ", "                E     E                ", "                CEEEEEC                ", "                 AAAAA                 ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ")
                                .aisle("       AAABBBBBBBBBBBBBBBBBBBAAA       ", "                D     D                ", "                E F F E                ", "                E     E                ", "                E     E                ", "                CEEEEEC                ", "                  AAA                  ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ")
                                .aisle("         AABBBBBBBBBBBBBBBBBAA         ", "                D     D                ", "                E F F E                ", "                E     E                ", "                E     E                ", "                CEEEEEC                ", "                  AAA                  ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ")
                                .aisle("          AAABBBBBBBBBBBBBAAA          ", "                D     D                ", "                E F F E                ", "                E     E                ", "                E     E                ", "                CEEEEEC                ", "                  AAA                  ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ")
                                .aisle("            AAAABBBBBBBAAAA            ", "                CDDDDDC                ", "                CEEEEEC                ", "                CEEEEEC                ", "                CEEEEEC                ", "                CCCCCCC                ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ", "                                       ")
                                .where('A', blocks(GCYMBlocks.CASING_NONCONDUCTING.get()))
                                .where('B', blocks(GTOBlocks.HIGH_STRENGTH_CONCRETE.get()))
                                .where('C', frames(GTMaterials.Tungsten))
                                .where('D', casing.or(abilities(IMPORT_FLUIDS).setPreviewCount(16))
                                        .or(abilities(EXPORT_FLUIDS).setPreviewCount(16))
                                        .or(abilities(PARALLEL_HATCH).setMaxGlobalLimited(1))
                                        .or(abilities(GTOPartAbility.THREAD_HATCH).setMaxGlobalLimited(1))
                                        .or(Predicates.blocks(GTOMachines.WIRELESS_ENERGY_INTERFACE_HATCH.getBlock()).setMaxGlobalLimited(1))
                                        .or(abilities(GTOPartAbility.OVERCLOCK_HATCH).setMaxGlobalLimited(1))
                                        .or(abilities(INPUT_LASER).setMaxGlobalLimited(16, 16)))
                                .where('E', casing)
                                .where('F', blocks(FusionCasings.getFrameState(tier)))
                                .where('G', blocks(GTOBlocks.STRENGTHEN_THE_BASE_BLOCK.get()))
                                .where('H', blocks(FusionCasings.getCompressedCoilState(tier)))
                                .where('I', blocks(GTOBlocks.PBI_RADIATION_RESISTANT_MECHANICAL_ENCLOSURE.get()))
                                .where('J', blocks(GCYMBlocks.ELECTROLYTIC_CELL.get()))
                                .where('K', blocks(GTBlocks.FUSION_GLASS.get()))
                                .where('L', blocks(GTOBlocks.FISSION_REACTOR_CASING.get()))
                                .where('M', controller(blocks(definition.get())))
                                .where(' ', any())
                                .build();
                    })
                    .renderer(() -> new AdvancedFusionReactorRenderer(FusionCasings.getCasingType(tier).getTexture(), GTCEu.id("block/multiblock/fusion_reactor")))
                    .hasTESR(true)
                    .register(),
            GTValues.LuV, GTValues.ZPM, GTValues.UV, GTValues.UHV, GTValues.UEV);

    public static final MultiblockMachineDefinition INCUBATOR = multiblock("incubator", "培养缸", IncubatorMachine::new)
            .nonYAxisRotation()
            .tooltips(NewDataAttributes.RUNTIME_REQUIREMENT.create(
                    c -> c.addLines("玻璃等级决定配方等级上限",
                            "The glass casing tier determines the upper limit of recipe tier")))
            .tooltips(GTOMachineTranslation.INSTANCE.getCulturingTankTooltips().getSupplier())
            .recipeTypes(GTORecipeTypes.INCUBATOR_RECIPES)
            .overclock()
            .block(GTBlocks.PLASTCRETE)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("bbbbb", "bbbbb", "ddddd", "ddddd", "bbbbb")
                    .aisle("bbbbb", "bcccb", "d   d", "d   d", "beeeb")
                    .aisle("bbbbb", "bcccb", "d   d", "d   d", "beeeb")
                    .aisle("bbbbb", "bcccb", "d   d", "d   d", "beeeb")
                    .aisle("bbabb", "bbbbb", "ddddd", "ddddd", "bbbbb")
                    .where('a', controller(blocks(definition.get())))
                    .where('b', blocks(GTBlocks.PLASTCRETE.get()).setMinGlobalLimited(40)
                            .or(autoAbilities(definition.getRecipeTypes()))
                            .or(blocks(GTOMachines.RADIATION_HATCH.getBlock()).setMaxGlobalLimited(2))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('c', blocks(Blocks.SPONGE))
                    .where('d', GTOPredicates.glass())
                    .where('e', cleanroomFilters())
                    .where(' ', air())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/cleanroom/plascrete"), GTCEu.id("block/multiblock/gcym/large_maceration_tower"))
            .register();

    public static final MultiblockMachineDefinition LARGE_INCUBATOR = multiblock("large_incubator", "大型培养缸", IncubatorMachine::new)
            .nonYAxisRotation()
            .tooltips(NewDataAttributes.RUNTIME_REQUIREMENT.create(
                    c -> c.addLines("玻璃等级决定配方等级上限",
                            "The glass casing tier determines the upper limit of recipe tier")))
            .tooltips(GTOMachineTranslation.INSTANCE.getLargeCulturingTankTooltips().getSupplier())
            .recipeTypes(GTORecipeTypes.INCUBATOR_RECIPES)
            .parallelizableTooltips()
            .parallelizableOverclock()
            .block(GTBlocks.PLASTCRETE)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("     AAA     ", "     BBB     ", "     DDD     ", "     EEE     ", "     DDD     ", "     EEE     ", "     DDD     ", "     EEE     ", "     AAA     ")
                    .aisle("   AAAAAAA   ", "   BBFFFBB   ", "   DD   DD   ", "   GGFFFGG   ", "   DD   DD   ", "   GGFFFGG   ", "   DD   DD   ", "   GGFFFGG   ", "   AAAAAAA   ")
                    .aisle("  AAAAAAAAA  ", "  BFFFFFFFB  ", "  D       D  ", "  GFFFFFFFG  ", "  D       D  ", "  GFFFFFFFG  ", "  D       D  ", "  GFFFFFFFG  ", "  AAAAAAAAA  ")
                    .aisle(" AAAAAAAAAAA ", " BFFFFFFFFFB ", " D         D ", " GFFFFFFFFFG ", " D         D ", " GFFFFFFFFFG ", " D         D ", " GFFFFFFFFFG ", " AAAAAAAAAAA ")
                    .aisle(" AAAAAAAAAAA ", " BFFFFFFFFFB ", " D         D ", " GFFFFFFFFFG ", " D         D ", " GFFFFFFFFFG ", " D         D ", " GFFFFFFFFFG ", " AAAAAAAAAAA ")
                    .aisle("AAAAAAAAAAAAA", "BFFFFFFFFFFFB", "D           D", "EFFFFFFFFFFFE", "D           D", "EFFFFFFFFFFFE", "D           D", "EFFFFFFFFFFFE", "AAAAAAAAAAAAA")
                    .aisle("AAAAAAAAAAAAA", "BFFFFFHFFFFFB", "D     H     D", "EFFFFFHFFFFFE", "D     H     D", "EFFFFFHFFFFFE", "D     H     D", "EFFFFFHFFFFFE", "AAAAAAAAAAAAA")
                    .aisle("AAAAAAAAAAAAA", "BFFFFFFFFFFFB", "D           D", "EFFFFFFFFFFFE", "D           D", "EFFFFFFFFFFFE", "D           D", "EFFFFFFFFFFFE", "AAAAAAAAAAAAA")
                    .aisle(" AAAAAAAAAAA ", " BFFFFFFFFFB ", " D         D ", " GFFFFFFFFFG ", " D         D ", " GFFFFFFFFFG ", " D         D ", " GFFFFFFFFFG ", " AAAAAAAAAAA ")
                    .aisle(" AAAAAAAAAAA ", " BFFFFFFFFFB ", " D         D ", " GFFFFFFFFFG ", " D         D ", " GFFFFFFFFFG ", " D         D ", " GFFFFFFFFFG ", " AAAAAAAAAAA ")
                    .aisle("  AAAAAAAAA  ", "  BFFFFFFFB  ", "  D       D  ", "  GFFFFFFFG  ", "  D       D  ", "  GFFFFFFFG  ", "  D       D  ", "  GFFFFFFFG  ", "  AAAAAAAAA  ")
                    .aisle("   AAAAAAA   ", "   BBFFFBB   ", "   DD   DD   ", "   GGFFFGG   ", "   DD   DD   ", "   GGFFFGG   ", "   DD   DD   ", "   GGFFFGG   ", "   AAAAAAA   ")
                    .aisle("     AAA     ", "     BCB     ", "     DDD     ", "     EEE     ", "     DDD     ", "     EEE     ", "     DDD     ", "     EEE     ", "     AAA     ")
                    .where('A', blocks(GTOBlocks.OXIDATION_RESISTANT_HASTELLOY_N_MECHANICAL_CASING.get()))
                    .where('B', blocks(GTBlocks.PLASTCRETE.get())
                            .or(autoAbilities(definition.getRecipeTypes()))
                            .or(blocks(GTOMachines.RADIATION_HATCH.getBlock()).setMaxGlobalLimited(8))
                            .or(abilities(PARALLEL_HATCH).setMaxGlobalLimited(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('C', controller(blocks(definition.get())))
                    .where('D', GTOPredicates.glass())
                    .where('E', cleanroomFilters())
                    .where('F', blocks(Blocks.SPONGE))
                    .where('G', blocks(GTBlocks.PLASTCRETE.get()))
                    .where('H', blocks(GTBlocks.CASING_POLYTETRAFLUOROETHYLENE_PIPE.get()))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/cleanroom/plascrete"), GTCEu.id("block/multiblock/gcym/large_maceration_tower"))
            .register();

    public static final MultiblockMachineDefinition DISSOLVING_TANK = multiblock("dissolving_tank", "溶解罐", DissolvingTankMachine::new)
            .nonYAxisRotation()
            .tooltipsText("必须保证输入的流体与配方流体比例相同，否则无产物输出", "Must ensure the ratio of input fluid to recipe fluid is the same, otherwise no product output")
            .parallelizableTooltips()
            .recipeTypes(GTORecipeTypes.DISSOLUTION_TREATMENT_RECIPES)
            .block(GTBlocks.CASING_STAINLESS_CLEAN)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("X###X", "OOOOO", "XGGGX", "XGGGX", "#XXX#")
                    .aisle("#####", "OKKKO", "G###G", "G###G", "XXXXX")
                    .aisle("#####", "OKKKO", "G###G", "G###G", "XXXXX")
                    .aisle("#####", "OKKKO", "G###G", "G###G", "XXXXX")
                    .aisle("X###X", "OOSOO", "XGGGX", "XGGGX", "#XXX#")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(GTBlocks.CASING_STAINLESS_CLEAN.get()))
                    .where('K', blocks(GTBlocks.CASING_INVAR_HEATPROOF.get()))
                    .where('O', blocks(GTBlocks.CASING_STAINLESS_CLEAN.get())
                            .or(autoAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('G', blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                    .where('#', any())
                    .build())
            .renderer(FluidRenderer.create(GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"), GTCEu.id("block/multiblock/generator/large_gas_turbine")))
            .hasTESR(true)
            .register();

    public static final MultiblockMachineDefinition GOD_FORGE = multiblock("god_forge", "诸神之锻炉", GodForgeMachine::new)
            .nonYAxisRotation()
            .recipeTypes(GTRecipeTypes.DUMMY_RECIPES)
            .fromSourceTooltips("GTNH")
            .block(GTOBlocks.TRANSCENDENTALLY_AMPLIFIED_MAGNETIC_CONFINEMENT_CASING)
            .pattern(GodForgeMachine::getBlockPattern)
            .renderer(GodforgeRenderer::new)
            .hasTESR(true)
            .register();
}
