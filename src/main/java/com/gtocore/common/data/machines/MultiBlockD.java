package com.gtocore.common.data.machines;

import com.gtocore.api.machine.part.GTOPartAbility;
import com.gtocore.api.pattern.GTOPredicates;
import com.gtocore.client.renderer.machine.*;
import com.gtocore.common.block.FusionCasings;
import com.gtocore.common.data.*;
import com.gtocore.common.data.translation.GTOMachineStories;
import com.gtocore.common.data.translation.GTOMachineTooltips;
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
import com.gtolib.api.annotation.component_builder.StyleBuilder;
import com.gtolib.api.lang.CNEN;
import com.gtolib.api.machine.MultiblockDefinition;
import com.gtolib.api.machine.multiblock.CoilCrossRecipeMultiblockMachine;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
import com.gtolib.utils.*;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import earth.terrarium.adastra.common.registry.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gtocore.common.block.BlockMap.SEPMMAP;
import static com.gtocore.utils.register.MachineRegisterUtils.multiblock;
import static com.gtocore.utils.register.MachineRegisterUtils.registerTieredMultis;
import static com.gtolib.api.GTOValues.POWER_MODULE_TIER;

public final class MultiBlockD {

    public static void init() {}

    public static final MultiblockMachineDefinition GREENHOUSE = multiblock("greenhouse", "温室", GreenhouseMachine::new)
            .nonYAxisRotation()
            .recipeTypes(GTORecipeTypes.GREENHOUSE_RECIPES)
            .tooltips(GTOMachineTooltips.INSTANCE.getGreenhouseTooltips().getSupplier())
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
            .tooltips(GTOMachineTooltips.INSTANCE.getEyeOfHarmonyTooltips().getSupplier())
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
            .tooltips(GTOMachineTooltips.INSTANCE.getSpaceProbeSurfaceReceptionTooltips().getSupplier())
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
            .multipleRecipesTooltips()
            .block(GTOBlocks.DIMENSIONALLY_TRANSCENDENT_CASING)
            .pattern(definition -> MultiBlockFileReader.start(definition)
                    .where('A', blocks(GTOBlocks.STRENGTHEN_THE_BASE_BLOCK.get()))
                    .where('B', blocks(GTOBlocks.DIMENSIONALLY_TRANSCENDENT_CASING.get())
                            .or(abilities(PARALLEL_HATCH).setMaxGlobalLimited(1))
                            .or(GTOPredicates.autoThreadLaserAbilities(definition.getRecipeTypes())))
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
            .tooltips(GTOMachineStories.INSTANCE.getCircuitAssemblyLineTooltips().getSupplier())
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
            .tooltips(GTOMachineTooltips.INSTANCE.getBlockConversionRoomTooltips().getSupplier())
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
            .tooltips(GTOMachineStories.INSTANCE.getLargeBlockConversionRoomTooltips().getSupplier())
            .tooltips(GTOMachineTooltips.INSTANCE.getLargeBlockConversionRoomTooltips().getSupplier())
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
            .tooltips(GTOMachineStories.INSTANCE.getPCBFactoryTooltips().getSupplier())
            .tooltips(GTOMachineTooltips.INSTANCE.getPCBFactoryTooltips().getSupplier())
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
            .tooltips(GTOMachineStories.INSTANCE.getBlazeBlastFurnaceTooltips().getSupplier())
            .tooltips(GTOMachineTooltips.INSTANCE.getBlazeBlastFurnaceTooltips().getSupplier())
            .specialParallelizableTooltips()
            .tooltips(NewDataAttributes.ALLOW_PARALLEL_NUMBER.create(64))
            .block(GTOBlocks.BLAZE_CASING)
            .upgradable()
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
            .tooltips(GTOMachineStories.INSTANCE.getColdIceFreezerTooltips().getSupplier())
            .tooltips(GTOMachineTooltips.INSTANCE.getColdIceFreezerTooltips().getSupplier())
            .tooltips(NewDataAttributes.ALLOW_PARALLEL_NUMBER.create(64))
            .recipeModifiers(RecipeModifierFunction.overclocking(0.5, 1, 0.5))
            .block(GTOBlocks.COLD_ICE_CASING)
            .pattern(definition -> FactoryBlockPattern.start(definition, RelativeDirection.RIGHT, RelativeDirection.UP, RelativeDirection.BACK)
                    .aisle("AAAAA", " BBB ", " BGB ", " BBB ", "AAAAA")
                    .aisle("AAAAA", "BE EB", "BE EB", "BE EB", "AAAAA")
                    .aisle("AAAAA", " F F ", " F F ", " F F ", "ACHCA")
                    .aisle("AAAAA", "BE EB", "BE EB", "BE EB", "ACCCA")
                    .aisle("AAAAA", "D   D", "D   D", "D   D", "ACCCA")
                    .aisle("AAAAA", "BE EB", "BE EB", "BE EB", "ACCCA")
                    .aisle("AAAAA", " F F ", " F F ", " F F ", "ACHCA")
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
            .recoveryStacks((machine, recipe) -> machine.getLevel() instanceof ServerLevel l && l.random.nextFloat() < 0.1f ?
                    ModItems.ICE_SHARD.get().getDefaultInstance() :
                    ChemicalHelper.get(TagPrefix.dust, GTMaterials.Ice))
            .register();

    public static final MultiblockMachineDefinition DOOR_OF_CREATE = multiblock("door_of_create", "创造之门", ElectricMultiblockMachine::new)
            .noneRotation()
            .recipeTypes(GTORecipeTypes.DOOR_OF_CREATE_RECIPES)
            .tooltips(GTOMachineStories.INSTANCE.getDoorOfCreateTooltips().getSupplier())
            .tooltips(GTOMachineTooltips.INSTANCE.getDoorOfCreateTooltips().getSupplier())
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
            .tooltips(GTOMachineStories.INSTANCE.getBedrockDrillingRigTooltips().getSupplier())
            .tooltips(GTOMachineTooltips.INSTANCE.getBedrockDrillingRigTooltips().getSupplier())
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
            .tooltips(GTOMachineStories.INSTANCE.getCreateAggregationTooltips().getSupplier())
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
                        if (MachineUtils.inputItem(machine, GTOItems.CHAIN_COMMAND_BLOCK_CORE.asItem(), 1) && block == GTOBlocks.COMMAND_BLOCK_BROKEN.get()) {
                            level.setBlockAndUpdate(pos, Blocks.CHAIN_COMMAND_BLOCK.defaultBlockState());
                        }
                        if (MachineUtils.inputItem(machine, GTOItems.REPEATING_COMMAND_BLOCK_CORE.asItem(), 1) && block == GTOBlocks.CHAIN_COMMAND_BLOCK_BROKEN.get()) {
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
            .tooltips(GTOMachineStories.INSTANCE.getNyarlathotepsTentacleTooltips().getSupplier())
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
            .tooltips(GTOMachineStories.INSTANCE.getProcessingPlantTooltips().getSupplier())
            .tooltips(GTOMachineTooltips.INSTANCE.getProcessingPlantTooltips().getSupplier())
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
                            .or(Predicates.blocks(GTMachines.CONTROL_HATCH.getBlock()).setMaxGlobalLimited(1).setPreviewCount(0))
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
            .tooltips(GTOMachineTooltips.INSTANCE.getNanoForgeTooltips().getSupplier())
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
            .tooltips(GTOMachineStories.INSTANCE.getIsaMillTooltips().getSupplier())
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
                            .or(Predicates.blocks(GTMachines.CONTROL_HATCH.getBlock()).setMaxGlobalLimited(1).setPreviewCount(0))
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
            .recoveryStacks((m, r) -> {
                if (r == null) return ItemStack.EMPTY;
                return ((Ingredient) r.outputs.get(ItemRecipeCapability.CAP).get(0).content).getItems()[0].copyWithCount(1);
            })
            .register();

    public static final MultiblockMachineDefinition NEUTRON_ACTIVATOR = multiblock("neutron_activator", "中子活化器", NeutronActivatorMachine::new)
            .nonYAxisRotation()
            .tooltips(GTOMachineStories.INSTANCE.getNeutronActivatorTooltips().getSupplier())
            .tooltips(GTOMachineTooltips.INSTANCE.getNeutronActivatorTooltips().getSupplier())
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
                            .or(Predicates.blocks(GTMachines.CONTROL_HATCH.getBlock()).setMaxGlobalLimited(1).setPreviewCount(0))
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
            .tooltips(GTOMachineStories.INSTANCE.getHeatExchangerTooltips().getSupplier())
            .tooltips(GTOMachineTooltips.INSTANCE.getHeatExchangerTooltips().getSupplier())
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
            .tooltips(GTOMachineStories.INSTANCE.getInfinityFluidDrillingRigTooltips().getSupplier())
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
            .tooltips(GTOMachineStories.INSTANCE.getAdvancedAssemblyLineTooltips().getSupplier())
            .tooltips(GTOMachineTooltips.INSTANCE.getAdvancedAssemblyLineTooltips().getSupplier())
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
            .tooltips(GTOMachineStories.INSTANCE.getFissionReactorTooltips().getSupplier())
            .tooltips(GTOMachineTooltips.INSTANCE.getFissionReactorTooltips().getSupplier())
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
                            .or(Predicates.blocks(GTMachines.CONTROL_HATCH.getBlock()).setMaxGlobalLimited(1).setPreviewCount(0))
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
            .tooltips(GTOMachineStories.INSTANCE.getSpaceElevatorTooltips().getSupplier())
            .tooltips(GTOMachineTooltips.INSTANCE.getSpaceElevatorTooltips().getSupplier())
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
            .tooltipsSupplier(GTOMachineStories.INSTANCE.getSlaughterhouseTooltips().getSupplier())
            .tooltipsSupplier(GTOMachineTooltips.INSTANCE.getSlaughterhouseTooltips().getSupplier())
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
                            .or(Predicates.blocks(GTMachines.CONTROL_HATCH.getBlock()).setMaxGlobalLimited(1).setPreviewCount(0))
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
                    .tooltips(GTOMachineStories.INSTANCE.getKuangbiaoGiantNuclearFusionReactorTooltips().getSupplier())
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
                                        .or(blocks(GTMachines.CONTROL_HATCH.getBlock()).setMaxGlobalLimited(1).setPreviewCount(0))
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
            .tooltips(GTOMachineStories.INSTANCE.getCulturingTankTooltips().getSupplier())
            .tooltips(GTOMachineTooltips.INSTANCE.getCulturingTankTooltips().getSupplier())
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
            .tooltips(GTOMachineTooltips.INSTANCE.getLargeCulturingTankTooltips().getSupplier())
            .tooltips(GTOMachineStories.INSTANCE.getLargeCulturingTankTooltips().getSupplier())
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
            .tooltips(GTOMachineStories.INSTANCE.getDissolvingTankTooltips().getSupplier())
            .tooltips(GTOMachineTooltips.INSTANCE.getDissolvingTankTooltips().getSupplier())
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
