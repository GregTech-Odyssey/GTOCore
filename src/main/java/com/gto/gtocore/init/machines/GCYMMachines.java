package com.gto.gtocore.init.machines;

import com.gto.gtocore.api.machine.multiblock.CoilMultiblockMachine;
import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.api.machine.part.GTOPartAbility;
import com.gto.gtocore.api.pattern.GTOPredicates;
import com.gto.gtocore.common.data.GTORecipeModifiers;
import com.gto.gtocore.common.machine.multiblock.part.ParallelHatchPartMachine;
import com.gto.gtocore.init.GTORecipeTypes;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.client.renderer.machine.gcym.LargeChemicalBathRenderer;
import com.gregtechceu.gtceu.client.renderer.machine.gcym.LargeMixerRenderer;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.machines.GTAEMachines;
import com.gregtechceu.gtceu.common.data.machines.GTMachineUtils;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.DistillationTowerMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.gcym.LargeChemicalBathMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.gcym.LargeMixerMachine;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.api.pattern.util.RelativeDirection.*;
import static com.gregtechceu.gtceu.common.data.GCYMBlocks.*;
import static com.gregtechceu.gtceu.common.data.GCYMRecipeTypes.ALLOY_BLAST_RECIPES;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.gregtechceu.gtceu.common.registry.GTRegistration.REGISTRATE;
import static com.gto.gtocore.utils.register.MachineRegisterUtils.GTO_MODIFY;

/**
 * @author Rundas
 * @implNote Gregicality Multiblocks
 */
public interface GCYMMachines {

    static void init() {}

    MachineDefinition[] PARALLEL = GTMachineUtils.registerTieredMachines("parallel_hatch",
            ParallelHatchPartMachine::new,
            (tier, builder) -> builder
                    .rotationState(RotationState.ALL)
                    .abilities(PARALLEL_HATCH)
                    .workableTieredHullRenderer(GTCEu.id("block/machines/parallel_hatch_mk" + (tier - 4)))
                    .tooltips(Component.translatable("gtceu.machine.parallel_hatch_mk" + (tier + 1) + ".tooltip"))
                    .tooltipBuilder(GTO_MODIFY)
                    .register(),
            IV, LuV, ZPM, UV, UHV, UEV, UIV, UXV, OpV, MAX);

    MultiblockMachineDefinition LARGE_MACERATION_TOWER = REGISTRATE
            .multiblock("large_maceration_tower", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.macerator")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeType(MACERATOR_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_SECURE_MACERATION)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXXXX", "XXXXX", "XXXXX", "XXXXX")
                    .aisle("XXXXX", "XGGGX", "XGGGX", "XAAAX")
                    .aisle("XXXXX", "XGGGX", "XGGGX", "XAAAX")
                    .aisle("XXXXX", "XGGGX", "XGGGX", "XAAAX")
                    .aisle("XXXXX", "XXXXX", "XXSXX", "XXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_SECURE_MACERATION.get()).setMinGlobalLimited(55)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('G', blocks(CRUSHING_WHEELS.get()))
                    .where('A', air())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/secure_maceration_casing"),
                    GTCEu.id("block/multiblock/gcym/large_maceration_tower"))
            .register();

    MultiblockMachineDefinition LARGE_CHEMICAL_BATH = REGISTRATE
            .multiblock("large_chemical_bath", LargeChemicalBathMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_2.tooltip",
                    Component.translatable("gtceu.ore_washer"), Component.translatable("gtceu.chemical_bath")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeTypes(CHEMICAL_BATH_RECIPES, ORE_WASHER_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_WATERTIGHT)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXXXX", "XXXXX", "XXXXX")
                    .aisle("XXXXX", "XTTTX", "X   X")
                    .aisle("XXXXX", "X   X", "X   X")
                    .aisle("XXXXX", "X   X", "X   X")
                    .aisle("XXXXX", "X   X", "X   X")
                    .aisle("XXXXX", "XTTTX", "X   X")
                    .aisle("XXXXX", "XXSXX", "XXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_WATERTIGHT.get()).setMinGlobalLimited(55)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where(' ', air())
                    .where('T', blocks(CASING_TITANIUM_PIPE.get()))
                    .build())
            .renderer(() -> new LargeChemicalBathRenderer(GTCEu.id("block/casings/gcym/watertight_casing"),
                    GTCEu.id("block/multiblock/gcym/large_chemical_bath")))
            .hasTESR(true)
            .register();

    MultiblockMachineDefinition LARGE_CENTRIFUGE = REGISTRATE
            .multiblock("large_centrifuge", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_2.tooltip",
                    Component.translatable("gtceu.centrifuge"), Component.translatable("gtceu.thermal_centrifuge")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeTypes(CENTRIFUGE_RECIPES, THERMAL_CENTRIFUGE_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_VIBRATION_SAFE)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("#XXX#", "XXXXX", "#XXX#")
                    .aisle("XXXXX", "XAPAX", "XXXXX")
                    .aisle("XXXXX", "XPAPX", "XXXXX")
                    .aisle("XXXXX", "XAPAX", "XXXXX")
                    .aisle("#XXX#", "XXSXX", "#XXX#")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_VIBRATION_SAFE.get()).setMinGlobalLimited(40)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('P', blocks(CASING_STEEL_PIPE.get()))
                    .where('A', air())
                    .where('#', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/vibration_safe_casing"),
                    GTCEu.id("block/multiblock/gcym/large_centrifuge"))
            .register();

    MultiblockMachineDefinition LARGE_MIXER = REGISTRATE
            .multiblock("large_mixer", LargeMixerMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.mixer")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeType(MIXER_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_REACTION_SAFE)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("#XXX#", "#XXX#", "#XXX#", "#XXX#", "#XXX#", "##F##")
                    .aisle("XXXXX", "XAPAX", "XAAAX", "XAPAX", "XAAAX", "##F##")
                    .aisle("XXXXX", "XPPPX", "XAPAX", "XPPPX", "XAGAX", "FFGFF")
                    .aisle("XXXXX", "XAPAX", "XAAAX", "XAPAX", "XAAAX", "##F##")
                    .aisle("#XXX#", "#XSX#", "#XXX#", "#XXX#", "#XXX#", "##F##")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_REACTION_SAFE.get()).setMinGlobalLimited(50)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('F', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.HastelloyX)))
                    .where('G', blocks(CASING_STAINLESS_STEEL_GEARBOX.get()))
                    .where('P', blocks(CASING_TITANIUM_PIPE.get()))
                    .where('A', air())
                    .where('#', any())
                    .build())
            .renderer(() -> new LargeMixerRenderer(GTCEu.id("block/casings/gcym/reaction_safe_mixing_casing"),
                    GTCEu.id("block/multiblock/gcym/large_mixer")))
            .hasTESR(true)
            .register();

    MultiblockMachineDefinition LARGE_ELECTROLYZER = REGISTRATE
            .multiblock("large_electrolyzer", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.electrolyzer")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeType(ELECTROLYZER_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_NONCONDUCTING)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXXXX", "XXXXX", "XXXXX")
                    .aisle("XXXXX", "XCCCX", "XCCCX")
                    .aisle("XXXXX", "XCCCX", "XCCCX")
                    .aisle("XXXXX", "XXSXX", "XXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_NONCONDUCTING.get()).setMinGlobalLimited(30)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('C', blocks(ELECTROLYTIC_CELL.get()))
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/nonconducting_casing"),
                    GTCEu.id("block/multiblock/gcym/large_electrolyzer"))
            .register();

    MultiblockMachineDefinition LARGE_ELECTROMAGNET = REGISTRATE
            .multiblock("large_electromagnet", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_2.tooltip",
                    Component.translatable("gtceu.electromagnetic_separator"),
                    Component.translatable("gtceu.polarizer")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeTypes(ELECTROMAGNETIC_SEPARATOR_RECIPES, POLARIZER_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_NONCONDUCTING)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXXXX", "XXXXX", "XXXXX")
                    .aisle("XCXCX", "XCXCX", "XCXCX")
                    .aisle("XCXCX", "XCXCX", "XCXCX")
                    .aisle("XXXXX", "XXSXX", "XXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_NONCONDUCTING.get()).setMinGlobalLimited(35)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('C', blocks(ELECTROLYTIC_CELL.get()))
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/nonconducting_casing"),
                    GTCEu.id("block/multiblock/gcym/large_electrolyzer"))
            .register();

    MultiblockMachineDefinition LARGE_PACKER = REGISTRATE
            .multiblock("large_packer", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_2.tooltip",
                    Component.translatable("gtceu.packer"), Component.translatable("gtceu.unpacker")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeType(PACKER_RECIPES)
            .recipeType(GTORecipeTypes.UNPACKER_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_TUNGSTENSTEEL_ROBUST)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "XXX", "XXX")
                    .aisle("XXX", "XAX", "XXX")
                    .aisle("XXX", "XAX", "XXX")
                    .aisle("XXX", "XAX", "XXX")
                    .aisle("XXX", "XAX", "XXX")
                    .aisle("XXX", "XSX", "XXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_TUNGSTENSTEEL_ROBUST.get()).setMinGlobalLimited(30)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('A', air())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_robust_tungstensteel"),
                    GTCEu.id("block/multiblock/gcym/large_packer"))
            .register();

    MultiblockMachineDefinition LARGE_ASSEMBLER = REGISTRATE
            .multiblock("large_assembler", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip", Component.translatable("gtceu.assembler")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeType(ASSEMBLER_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_LARGE_SCALE_ASSEMBLING)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX")
                    .aisle("XXXXXXXXX", "XAAAXAAAX", "XGGGXXXXX")
                    .aisle("XXXXXXXXX", "XGGGXXSXX", "XGGGX###X")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_LARGE_SCALE_ASSEMBLING.get()).setMinGlobalLimited(40)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('G', blocks(CASING_TEMPERED_GLASS.get()))
                    .where('A', air())
                    .where('#', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/large_scale_assembling_casing"),
                    GTCEu.id("block/multiblock/gcym/large_assembler"))
            .register();

    MultiblockMachineDefinition LARGE_CIRCUIT_ASSEMBLER = REGISTRATE
            .multiblock("large_circuit_assembler", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.circuit_assembler")))
            .tooltips(Component.translatable("gtceu.multiblock.exact_hatch_1.tooltip"))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeType(CIRCUIT_ASSEMBLER_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_LARGE_SCALE_ASSEMBLING)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXXXXXX", "XXXXXXX", "XXXXXXX")
                    .aisle("XXXXXXX", "XPPPPPX", "XGGGGGX")
                    .aisle("XXXXXXX", "XAAAAPX", "XGGGGGX")
                    .aisle("XXXXXXX", "XTTTTXX", "XXXXXXX")
                    .aisle("#####XX", "#####SX", "#####XX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_LARGE_SCALE_ASSEMBLING.get()).setMinGlobalLimited(55)
                            .or(autoAbilities(definition.getRecipeTypes(), false, false, true, true, true,
                                    true))
                            .or(abilities(GTOPartAbility.ACCELERATE_HATCH).setMaxGlobalLimited(1))
                            .or(abilities(INPUT_ENERGY).setExactLimit(1))
                            .or(autoAbilities(true, false, true)))
                    .where('T', blocks(CASING_TEMPERED_GLASS.get()))
                    .where('G', blocks(CASING_GRATE.get()))
                    .where('P', blocks(CASING_TUNGSTENSTEEL_PIPE.get()))
                    .where('A', air())
                    .where('#', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/large_scale_assembling_casing"),
                    GTCEu.id("block/multiblock/gcym/large_circuit_assembler"))
            .register();

    MultiblockMachineDefinition LARGE_ARC_SMELTER = REGISTRATE
            .multiblock("large_arc_smelter", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip", Component.translatable("gtceu.arc_furnace")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeType(ARC_FURNACE_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_HIGH_TEMPERATURE_SMELTING)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("#XXX#", "#XXX#", "#XXX#", "#XXX#")
                    .aisle("XXXXX", "XCACX", "XCACX", "XXXXX")
                    .aisle("XXXXX", "XAAAX", "XAAAX", "XXMXX")
                    .aisle("XXXXX", "XACAX", "XACAX", "XXXXX")
                    .aisle("#XXX#", "#XSX#", "#XXX#", "#XXX#")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_HIGH_TEMPERATURE_SMELTING.get()).setMinGlobalLimited(45)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('C', blocks(MOLYBDENUM_DISILICIDE_COIL_BLOCK.get()))
                    .where('M', abilities(MUFFLER))
                    .where('A', air())
                    .where('#', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/high_temperature_smelting_casing"), GTCEu.id("block/multiblock/gcym/large_arc_smelter"))
            .register();

    MultiblockMachineDefinition LARGE_ENGRAVING_LASER = REGISTRATE
            .multiblock("large_engraving_laser", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip", Component.translatable("gtceu.laser_engraver")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeType(LASER_ENGRAVER_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_LASER_SAFE_ENGRAVING)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXXXX", "XXGXX", "XXGXX", "XXXXX")
                    .aisle("XXXXX", "XAAAX", "XAAAX", "XKKKX")
                    .aisle("XXXXX", "GAAAG", "GACAG", "XKXKX")
                    .aisle("XXXXX", "XAAAX", "XAAAX", "XKKKX")
                    .aisle("XXSXX", "XXGXX", "XXGXX", "XXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('C', blocks(CASING_TUNGSTENSTEEL_PIPE.get()))
                    .where('X', blocks(CASING_LASER_SAFE_ENGRAVING.get()).setMinGlobalLimited(45)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('G', blocks(CASING_TEMPERED_GLASS.get()))
                    .where('K', blocks(CASING_GRATE.get()))
                    .where('A', air())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/laser_safe_engraving_casing"),
                    GTCEu.id("block/multiblock/gcym/large_engraving_laser"))
            .register();

    MultiblockMachineDefinition LARGE_SIFTING_FUNNEL = REGISTRATE
            .multiblock("large_sifting_funnel", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.sifter")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeType(SIFTER_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_VIBRATION_SAFE)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("#X#X#", "#X#X#", "#XXX#", "XXXXX", "#XXX#")
                    .aisle("XXXXX", "XAXAX", "XKKKX", "XKKKX", "X###X")
                    .aisle("#XXX#", "#XAX#", "XKKKX", "XKKKX", "X###X")
                    .aisle("XXXXX", "XAXAX", "XKKKX", "XKKKX", "X###X")
                    .aisle("#X#X#", "#X#X#", "#XSX#", "XXXXX", "#XXX#")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_VIBRATION_SAFE.get()).setMinGlobalLimited(50)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('K', blocks(CASING_GRATE.get()))
                    .where('A', air())
                    .where('#', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/vibration_safe_casing"),
                    GTCEu.id("block/multiblock/gcym/large_sifting_funnel"))
            .register();

    MultiblockMachineDefinition BLAST_ALLOY_SMELTER = REGISTRATE
            .multiblock("alloy_blast_smelter", CoilMultiblockMachine.createCoilMachine(true, false))
            .tooltips(Component.translatable("gtceu.machine.electric_blast_furnace.tooltip.0"),
                    Component.translatable("gtceu.machine.electric_blast_furnace.tooltip.1"),
                    Component.translatable("gtceu.machine.electric_blast_furnace.tooltip.2"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.alloy_blast_smelter")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeType(ALLOY_BLAST_RECIPES)
            .recipeModifier(GTORecipeModifiers::ebfOverclock)
            .appearanceBlock(CASING_HIGH_TEMPERATURE_SMELTING)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("#XXX#", "#CCC#", "#GGG#", "#CCC#", "#XXX#")
                    .aisle("XXXXX", "CAAAC", "GAAAG", "CAAAC", "XXXXX")
                    .aisle("XXXXX", "CAAAC", "GAAAG", "CAAAC", "XXMXX")
                    .aisle("XXXXX", "CAAAC", "GAAAG", "CAAAC", "XXXXX")
                    .aisle("#XSX#", "#CCC#", "#GGG#", "#CCC#", "#XXX#")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_HIGH_TEMPERATURE_SMELTING.get()).setMinGlobalLimited(30)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, false)))
                    .where('C', heatingCoils())
                    .where('M', abilities(MUFFLER))
                    .where('G', blocks(HEAT_VENT.get()))
                    .where('A', air())
                    .where('#', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/high_temperature_smelting_casing"),
                    GTCEu.id("block/multiblock/gcym/blast_alloy_smelter"))
            .register();

    MultiblockMachineDefinition LARGE_AUTOCLAVE = REGISTRATE
            .multiblock("large_autoclave", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.autoclave")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeType(AUTOCLAVE_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_WATERTIGHT)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXX", "XXX", "XXX")
                    .aisle("XXX", "XTX", "XXX")
                    .aisle("XXX", "XTX", "XXX")
                    .aisle("XXX", "XTX", "XXX")
                    .aisle("XXX", "XSX", "XXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_WATERTIGHT.get()).setMinGlobalLimited(30)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('T', blocks(CASING_STEEL_PIPE.get()))
                    .where('#', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/watertight_casing"),
                    GTCEu.id("block/multiblock/gcym/large_autoclave"))
            .register();

    MultiblockMachineDefinition LARGE_MATERIAL_PRESS = REGISTRATE
            .multiblock("large_material_press", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.compressor")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeTypes(COMPRESSOR_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_STRESS_PROOF)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXXXXXX", "XXXXXXX", "XXXXXXX")
                    .aisle("XXXXXXX", "XAXGGGX", "XXXXXXX")
                    .aisle("XXXXXXX", "XSXCCCX", "XXXXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_STRESS_PROOF.get()).setMinGlobalLimited(40)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('G', blocks(CASING_STEEL_GEARBOX.get()))
                    .where('C', blocks(CASING_TEMPERED_GLASS.get()))
                    .where('A', air())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/stress_proof_casing"),
                    GTCEu.id("block/multiblock/gcym/large_material_press"))
            .register();

    MultiblockMachineDefinition LARGE_BREWER = REGISTRATE
            .multiblock("large_brewer", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_3.tooltip",
                    Component.translatable("gtceu.brewery"), Component.translatable("gtceu.fermenter"),
                    Component.translatable("gtceu.fluid_heater")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeTypes(BREWING_RECIPES, FERMENTING_RECIPES, FLUID_HEATER_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_CORROSION_PROOF)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("#XXX#", "#XXX#", "#XXX#", "#XXX#", "#####")
                    .aisle("XXXXX", "XCCCX", "XAAAX", "XXAXX", "##X##")
                    .aisle("XXXXX", "XCPCX", "XAPAX", "XAPAX", "#XMX#")
                    .aisle("XXXXX", "XCCCX", "XAAAX", "XXAXX", "##X##")
                    .aisle("#XXX#", "#XSX#", "#XXX#", "#XXX#", "#####")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_CORROSION_PROOF.get()).setMinGlobalLimited(50)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('P', blocks(CASING_STEEL_PIPE.get()))
                    .where('C', blocks(MOLYBDENUM_DISILICIDE_COIL_BLOCK.get()))
                    .where('M', abilities(MUFFLER))
                    .where('A', air())
                    .where('#', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/corrosion_proof_casing"),
                    GTCEu.id("block/multiblock/gcym/large_brewer"))
            .register();

    MultiblockMachineDefinition LARGE_CUTTER = REGISTRATE
            .multiblock("large_cutter", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_2.tooltip",
                    Component.translatable("gtceu.cutter"), Component.translatable("gtceu.lathe")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeTypes(CUTTER_RECIPES, LATHE_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_SHOCK_PROOF)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXXXXXX", "XXXXXXX", "XXXXXXX", "##XXXXX")
                    .aisle("XXXXXXX", "XAXCCCX", "XXXAAAX", "##XXXXX")
                    .aisle("XXXXXXX", "XAXCCCX", "XXXAAAX", "##XXXXX")
                    .aisle("XXXXXXX", "XSXGGGX", "XXXGGGX", "##XXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_SHOCK_PROOF.get()).setMinGlobalLimited(65)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('G', blocks(CASING_TEMPERED_GLASS.get()))
                    .where('C', blocks(SLICING_BLADES.get()))
                    .where('A', air())
                    .where('#', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/shock_proof_cutting_casing"),
                    GTCEu.id("block/multiblock/gcym/large_cutter"))
            .register();

    MultiblockMachineDefinition LARGE_DISTILLERY = REGISTRATE
            .multiblock("large_distillery", DistillationTowerMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_2.tooltip",
                    Component.translatable("gtceu.distillation_tower"), Component.translatable("gtceu.distillery")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.NON_Y_AXIS)
            .recipeTypes(DISTILLATION_RECIPES, DISTILLERY_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_WATERTIGHT)
            .pattern(definition -> {
                TraceabilityPredicate casingPredicate = blocks(CASING_WATERTIGHT.get()).setMinGlobalLimited(40);
                return FactoryBlockPattern.start(RIGHT, BACK, UP)
                        .aisle("#YYY#", "YYYYY", "YYYYY", "YYYYY", "#YYY#")
                        .aisle("#YSY#", "YAAAY", "YAAAY", "YAAAY", "#YYY#")
                        .aisle("##X##", "#XAX#", "XAPAX", "#XAX#", "##X##").setRepeatable(1, 12)
                        .aisle("#####", "#ZZZ#", "#ZCZ#", "#ZZZ#", "#####")
                        .where('S', controller(blocks(definition.get())))
                        .where('Y', casingPredicate.or(abilities(IMPORT_ITEMS))
                                .or(abilities(INPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(2))
                                .or(abilities(IMPORT_FLUIDS).setMinGlobalLimited(1))
                                .or(Predicates.abilities(GTOPartAbility.ACCELERATE_HATCH).setMaxGlobalLimited(1))
                                .or(abilities(GTOPartAbility.ITEMS_OUTPUT).or(blocks(GTAEMachines.ITEM_EXPORT_BUS_ME.get())).setMaxLayerLimited(1))
                                .or(autoAbilities(true, false, true)))
                        .where('X', casingPredicate.or(abilities(PartAbility.EXPORT_FLUIDS_1X).or(blocks(GTAEMachines.FLUID_EXPORT_HATCH_ME.get())).setMaxLayerLimited(1)))
                        .where('Z', casingPredicate)
                        .where('P', blocks(CASING_STEEL_PIPE.get()))
                        .where('C', abilities(MUFFLER))
                        .where('A', air())
                        .where('#', any())
                        .build();
            })
            .shapeInfos(definition -> {
                List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
                var builder = MultiblockShapeInfo.builder()
                        .where('S', definition, Direction.NORTH)
                        .where('C', CASING_WATERTIGHT.getDefaultState())
                        .where('M', GTMachines.MUFFLER_HATCH[IV], Direction.UP)
                        .where('X', PARALLEL[IV], Direction.NORTH)
                        .where('H', GTMachines.FLUID_IMPORT_HATCH[IV], Direction.NORTH)
                        .where('B', GTMachines.ITEM_EXPORT_BUS[IV], Direction.NORTH)
                        .where('N', GTMachines.MAINTENANCE_HATCH, Direction.NORTH)
                        .where('P', CASING_STEEL_PIPE.getDefaultState())
                        .where('F', GTMachines.FLUID_EXPORT_HATCH[IV], Direction.SOUTH)
                        .where('E', GTMachines.ENERGY_INPUT_HATCH[IV], Direction.SOUTH)
                        .where('#', Blocks.AIR.defaultBlockState());
                List<String> aisle1 = new ArrayList<>(16);
                aisle1.add("#HCB#");
                aisle1.add("#NSX#");
                aisle1.add("#####");
                List<String> aisle2 = new ArrayList<>(16);
                aisle2.add("CCCCC");
                aisle2.add("C###C");
                aisle2.add("#CCC#");
                List<String> aisle3 = new ArrayList<>(16);
                aisle3.add("CCCCC");
                aisle3.add("C###C");
                aisle3.add("#CMC#");
                List<String> aisle4 = new ArrayList<>(16);
                aisle4.add("CCCCC");
                aisle4.add("C###C");
                aisle4.add("#CCC#");
                List<String> aisle5 = new ArrayList<>(16);
                aisle5.add("#CEC#");
                aisle5.add("#CCC#");
                aisle5.add("#####");
                for (int i = 1; i <= 12; ++i) {
                    aisle1.add(2, "##C##");
                    aisle2.add(2, "#C#C#");
                    aisle3.add(2, "C#P#C");
                    aisle4.add(2, "#C#C#");
                    aisle5.add(2, "##F##");
                    var copy = builder.shallowCopy()
                            .aisle(aisle1.toArray(String[]::new))
                            .aisle(aisle2.toArray(String[]::new))
                            .aisle(aisle3.toArray(String[]::new))
                            .aisle(aisle4.toArray(String[]::new))
                            .aisle(aisle5.toArray(String[]::new));
                    shapeInfos.add(copy.build());
                }
                return shapeInfos;
            })
            .partSorter(Comparator.comparingInt(a -> a.self().getPos().getY()))
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/watertight_casing"),
                    GTCEu.id("block/multiblock/gcym/large_distillery"))
            .register();

    MultiblockMachineDefinition LARGE_EXTRACTOR = REGISTRATE
            .multiblock("large_extractor", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_2.tooltip",
                    Component.translatable("gtceu.extractor"), Component.translatable("gtceu.canner")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeTypes(EXTRACTOR_RECIPES, CANNER_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_WATERTIGHT)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXXXX", "XXXXX", "XXXXX")
                    .aisle("XXXXX", "XCACX", "XXXXX")
                    .aisle("XXXXX", "XXSXX", "XXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_WATERTIGHT.get()).setMinGlobalLimited(25)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('C', blocks(CASING_STEEL_PIPE.get()))
                    .where('A', air())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/watertight_casing"),
                    GTCEu.id("block/multiblock/gcym/large_extractor"))
            .register();

    MultiblockMachineDefinition LARGE_EXTRUDER = REGISTRATE
            .multiblock("large_extruder", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.extruder")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeType(EXTRUDER_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_STRESS_PROOF)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("##XXX", "##XXX", "##XXX")
                    .aisle("##XXX", "##XPX", "##XGX").setRepeatable(2)
                    .aisle("XXXXX", "XXXPX", "XXXGX")
                    .aisle("XXXXX", "XAXPX", "XXXGX")
                    .aisle("XXXXX", "XSXXX", "XXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_STRESS_PROOF.get()).setMinGlobalLimited(40)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('P', blocks(CASING_TITANIUM_PIPE.get()))
                    .where('G', blocks(CASING_TEMPERED_GLASS.get()))
                    .where('A', air())
                    .where('#', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/stress_proof_casing"),
                    GTCEu.id("block/multiblock/gcym/large_extruder"))
            .register();

    MultiblockMachineDefinition LARGE_SOLIDIFIER = REGISTRATE
            .multiblock("large_solidifier", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.fluid_solidifier")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeType(FLUID_SOLIDFICATION_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_WATERTIGHT)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("#XXX#", "#XXX#", "#XXX#", "#XXX#")
                    .aisle("XXXXX", "XCACX", "XCACX", "XXXXX")
                    .aisle("XXXXX", "XAAAX", "XAAAX", "XXXXX")
                    .aisle("XXXXX", "XCACX", "XCACX", "XXXXX")
                    .aisle("#XXX#", "#XSX#", "#XXX#", "#XXX#")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_WATERTIGHT.get()).setMinGlobalLimited(45)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('C', blocks(CASING_STEEL_PIPE.get()))
                    .where('A', air())
                    .where('#', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/watertight_casing"),
                    GTCEu.id("block/multiblock/gcym/large_solidifier"))
            .register();

    MultiblockMachineDefinition LARGE_WIREMILL = REGISTRATE
            .multiblock("large_wiremill", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_2.tooltip",
                    Component.translatable("gtceu.wiremill"), Component.translatable("gtceu.loom")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeType(WIREMILL_RECIPES)
            .recipeType(GTORecipeTypes.LOOM_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_STRESS_PROOF)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXXXX", "XXXXX", "XXX##")
                    .aisle("XXXXX", "X#CCX", "XXXXX")
                    .aisle("XXXXX", "XSXXX", "XXX##")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_STRESS_PROOF.get()).setMinGlobalLimited(25)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('C', blocks(CASING_TITANIUM_GEARBOX.get()))
                    .where('#', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/stress_proof_casing"),
                    GTCEu.id("block/multiblock/gcym/large_wiremill"))
            .register();

    MultiblockMachineDefinition MEGA_BLAST_FURNACE = REGISTRATE
            .multiblock("mega_blast_furnace", CoilMultiblockMachine.createCoilMachine(true, true))
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.machine.electric_blast_furnace.tooltip.2"))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.electric_blast_furnace")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeType(BLAST_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_HIGH_TEMPERATURE_SMELTING)
            .pattern(definition -> {
                TraceabilityPredicate casing = blocks(CASING_HIGH_TEMPERATURE_SMELTING.get()).setMinGlobalLimited(360);
                return FactoryBlockPattern.start()
                        .aisle("##XXXXXXXXX##", "##XXXXXXXXX##", "#############", "#############", "#############",
                                "#############", "#############", "#############", "#############", "#############",
                                "#############", "#############", "#############", "#############", "#############",
                                "#############", "#############")
                        .aisle("#XXXXXXXXXXX#", "#XXXXXXXXXXX#", "###F#####F###", "###F#####F###", "###FFFFFFF###",
                                "#############", "#############", "#############", "#############", "#############",
                                "####FFFFF####", "#############", "#############", "#############", "#############",
                                "#############", "#############")
                        .aisle("XXXXXXXXXXXXX", "XXXXVVVVVXXXX", "##F#######F##", "##F#######F##", "##FFFHHHFFF##",
                                "##F#######F##", "##F#######F##", "##F#######F##", "##F#######F##", "##F#######F##",
                                "##FFFHHHFFF##", "#############", "#############", "#############", "#############",
                                "#############", "###TTTTTTT###")
                        .aisle("XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "#F####P####F#", "#F####P####F#", "#FFHHHPHHHFF#",
                                "######P######", "######P######", "######P######", "######P######", "######P######",
                                "##FHHHPHHHF##", "######P######", "######P######", "######P######", "######P######",
                                "######P######", "##TTTTPTTTT##")
                        .aisle("XXXXXXXXXXXXX", "XXVXXXXXXXVXX", "####BBPBB####", "####TITIT####", "#FFHHHHHHHFF#",
                                "####BITIB####", "####CCCCC####", "####CCCCC####", "####CCCCC####", "####BITIB####",
                                "#FFHHHHHHHFF#", "####BITIB####", "####CCCCC####", "####CCCCC####", "####CCCCC####",
                                "####BITIB####", "##TTTTPTTTT##")
                        .aisle("XXXXXXXXXXXXX", "XXVXXXXXXXVXX", "####BAAAB####", "####IAAAI####", "#FHHHAAAHHHF#",
                                "####IAAAI####", "####CAAAC####", "####CAAAC####", "####CAAAC####", "####IAAAI####",
                                "#FHHHAAAHHHF#", "####IAAAI####", "####CAAAC####", "####CAAAC####", "####CAAAC####",
                                "####IAAAI####", "##TTTTPTTTT##")
                        .aisle("XXXXXXXXXXXXX", "XXVXXXXXXXVXX", "###PPAAAPP###", "###PTAAATP###", "#FHPHAAAHPHF#",
                                "###PTAAATP###", "###PCAAACP###", "###PCAAACP###", "###PCAAACP###", "###PTAAATP###",
                                "#FHPHAAAHPHF#", "###PTAAATP###", "###PCAAACP###", "###PCAAACP###", "###PCAAACP###",
                                "###PTAAATP###", "##TPPPMPPPT##")
                        .aisle("XXXXXXXXXXXXX", "XXVXXXXXXXVXX", "####BAAAB####", "####IAAAI####", "#FHHHAAAHHHF#",
                                "####IAAAI####", "####CAAAC####", "####CAAAC####", "####CAAAC####", "####IAAAI####",
                                "#FHHHAAAHHHF#", "####IAAAI####", "####CAAAC####", "####CAAAC####", "####CAAAC####",
                                "####IAAAI####", "##TTTTPTTTT##")
                        .aisle("XXXXXXXXXXXXX", "XXVXXXXXXXVXX", "####BBPBB####", "####TITIT####", "#FFHHHHHHHFF#",
                                "####BITIB####", "####CCCCC####", "####CCCCC####", "####CCCCC####", "####BITIB####",
                                "#FFHHHHHHHFF#", "####BITIB####", "####CCCCC####", "####CCCCC####", "####CCCCC####",
                                "####BITIB####", "##TTTTPTTTT##")
                        .aisle("XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "#F####P####F#", "#F####P####F#", "#FFHHHPHHHFF#",
                                "######P######", "######P######", "######P######", "######P######", "######P######",
                                "##FHHHPHHHF##", "######P######", "######P######", "######P######", "######P######",
                                "######P######", "##TTTTPTTTT##")
                        .aisle("XXXXXXXXXXXXX", "XXXXVVVVVXXXX", "##F#######F##", "##F#######F##", "##FFFHHHFFF##",
                                "##F#######F##", "##F#######F##", "##F#######F##", "##F#######F##", "##F#######F##",
                                "##FFFHHHFFF##", "#############", "#############", "#############", "#############",
                                "#############", "###TTTTTTT###")
                        .aisle("#XXXXXXXXXXX#", "#XXXXXXXXXXX#", "###F#####F###", "###F#####F###", "###FFFFFFF###",
                                "#############", "#############", "#############", "#############", "#############",
                                "####FFFFF####", "#############", "#############", "#############", "#############",
                                "#############", "#############")
                        .aisle("##XXXXXXXXX##", "##XXXXSXXXX##", "#############", "#############", "#############",
                                "#############", "#############", "#############", "#############", "#############",
                                "#############", "#############", "#############", "#############", "#############",
                                "#############", "#############")
                        .where('S', controller(blocks(definition.get())))
                        .where('X', casing.or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                                .or(autoAbilities(true, false, true)))
                        .where('C', heatingCoils())
                        .where('M', abilities(MUFFLER))
                        .where('F', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.NaquadahAlloy)))
                        .where('H', casing)
                        .where('T', blocks(CASING_TUNGSTENSTEEL_ROBUST.get()))
                        .where('B', blocks(FIREBOX_TUNGSTENSTEEL.get()))
                        .where('P', blocks(CASING_TUNGSTENSTEEL_PIPE.get()))
                        .where('I', blocks(CASING_EXTREME_ENGINE_INTAKE.get()))
                        .where('V', blocks(HEAT_VENT.get()))
                        .where('A', air())
                        .where('#', any())
                        .build();
            })
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/high_temperature_smelting_casing"),
                    GTCEu.id("block/multiblock/gcym/mega_blast_furnace"))
            .register();

    MultiblockMachineDefinition MEGA_VACUUM_FREEZER = REGISTRATE
            .multiblock("mega_vacuum_freezer", ElectricMultiblockMachine::new)
            .tooltips(Component.translatable("gtocore.machine.eut_multiplier.tooltip", 0.8))
            .tooltips(Component.translatable("gtocore.machine.duration_multiplier.tooltip", 0.6))
            .tooltips(Component.translatable("gtceu.multiblock.parallelizable.tooltip"))
            .tooltips(Component.translatable("gtceu.machine.available_recipe_map_1.tooltip",
                    Component.translatable("gtceu.vacuum_freezer")))
            .tooltipBuilder(GTO_MODIFY)
            .rotationState(RotationState.ALL)
            .recipeType(VACUUM_RECIPES)
            .recipeModifiers(true, GTORecipeModifiers.GCYM_OVERCLOCKING)
            .appearanceBlock(CASING_ALUMINIUM_FROSTPROOF)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("XXXXXXX#KKK", "XXXXXXX#KVK", "XXXXXXX#KVK", "XXXXXXX#KVK", "XXXXXXX#KKK", "XXXXXXX####",
                            "XXXXXXX####")
                    .aisle("XXXXXXX#KVK", "XPPPPPPPPPV", "XPAPAPX#VPV", "XPPPPPPPPPV", "XPAPAPX#KVK", "XPPPPPX####",
                            "XXXXXXX####")
                    .aisle("XXXXXXX#KVK", "XPAPAPXAVPV", "XAAAAAX#VPV", "XPAAAPX#VPV", "XAAAAAX#KVK", "XPAPAPX####",
                            "XXXXXXX####")
                    .aisle("XXXXXXX#KVK", "XPAPAPPPPPV", "XAAAAAX#VPV", "XPAAAPPPPPV", "XAAAAAX#KVK", "XPAPAPX####",
                            "XXXXXXX####")
                    .aisle("XXXXXXX#KKK", "XPPPPPX#KVK", "XPA#APX#KVK", "XPAAAPX#KVK", "XPAAAPX#KKK", "XPPPPPX####",
                            "XXXXXXX####")
                    .aisle("#XXXXX#####", "#XXSXX#####", "#XGGGX#####", "#XGGGX#####", "#XGGGX#####", "#XXXXX#####",
                            "###########")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_ALUMINIUM_FROSTPROOF.get()).setMinGlobalLimited(140)
                            .or(GTOPredicates.autoAccelerateAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('G', blocks(CASING_TEMPERED_GLASS.get()))
                    .where('K', blocks(CASING_STAINLESS_CLEAN.get()))
                    .where('P', blocks(CASING_TUNGSTENSTEEL_PIPE.get()))
                    .where('V', blocks(HEAT_VENT.get()))
                    .where('A', air())
                    .where('#', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_frost_proof"),
                    GTCEu.id("block/multiblock/gcym/mega_vacuum_freezer"))
            .register();
}
