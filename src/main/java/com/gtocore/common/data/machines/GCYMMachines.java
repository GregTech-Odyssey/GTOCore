package com.gtocore.common.data.machines;

import com.gtocore.api.machine.part.GTOPartAbility;
import com.gtocore.api.pattern.GTOPredicates;
import com.gtocore.client.renderer.machine.FluidRenderer;
import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.common.machine.multiblock.electric.gcym.*;
import com.gtocore.common.machine.multiblock.part.ParallelHatchPartMachine;

import com.gtolib.api.annotation.NewDataAttributes;
import com.gtolib.api.machine.multiblock.CoilCustomParallelMultiblockMachine;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
import com.gtolib.utils.register.MachineRegisterUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.api.pattern.TraceabilityPredicate;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.common.data.*;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

import static com.gregtechceu.gtceu.api.GTValues.IV;
import static com.gregtechceu.gtceu.api.GTValues.MAX;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.api.pattern.util.RelativeDirection.*;
import static com.gregtechceu.gtceu.common.data.GCYMBlocks.*;
import static com.gregtechceu.gtceu.common.data.GCYMRecipeTypes.ALLOY_BLAST_RECIPES;
import static com.gregtechceu.gtceu.common.data.GTBlocks.*;
import static com.gregtechceu.gtceu.common.data.GTMachines.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.gtocore.common.data.GTOBlocks.INTEGRAL_FRAMEWORK_MV;
import static com.gtolib.api.registries.GTORegistration.GTM;
import static com.gtolib.utils.register.MachineRegisterUtils.multiblock;

public final class GCYMMachines {

    public static void init() {}

    public static final MachineDefinition[] PARALLEL = MachineRegisterUtils.registerTieredGTMMachines("parallel_hatch",
            ParallelHatchPartMachine::new, (tier, builder) -> builder
                    .allRotation()
                    .abilities(PARALLEL_HATCH)
                    .workableTieredHullRenderer(GTCEu.id("block/machines/parallel_hatch_mk" + (tier - 4)))
                    .tooltips(NewDataAttributes.ALLOW_PARALLEL_NUMBER.create(ParallelHatchPartMachine.PARALLEL_FUNCTION.apply(tier)))
                    .notAllowSharedTooltips()
                    .register(),
            GTValues.tiersBetween(IV, MAX));

    public static final MultiblockMachineDefinition LARGE_MACERATION_TOWER = GTM
            .multiblock("large_maceration_tower", LargeMacerationTowerMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .nonYAxisRotation()
            .recipeTypes(MACERATOR_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_SECURE_MACERATION)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("XXXXX", "XXXXX", "XXXXX", "XXXXX")
                    .aisle("XXXXX", "XGGGX", "XGGGX", "XAAAX")
                    .aisle("XXaXX", "XGGGX", "XGGGX", "XAAAX")
                    .aisle("XXXXX", "XGGGX", "XGGGX", "XAAAX")
                    .aisle("XXXXX", "XXXXX", "XXSXX", "XXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_SECURE_MACERATION.get()).setMinGlobalLimited(55)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('G', blocks(CRUSHING_WHEELS.get()))
                    .where('A', air())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/secure_maceration_casing"),
                    GTCEu.id("block/multiblock/gcym/large_maceration_tower"))
            .register();

    public static final MultiblockMachineDefinition LARGE_CHEMICAL_BATH = GTM
            .multiblock("large_chemical_bath", LargeChemicalBathMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .nonYAxisRotation()
            .recipeTypes(CHEMICAL_BATH_RECIPES, ORE_WASHER_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_WATERTIGHT)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("XXXXX", "XXXXX", "XXXXX")
                    .aisle("XXXXX", "XTTTX", "X   X")
                    .aisle("XXXXX", "X   X", "X   X")
                    .aisle("XXaXX", "X   X", "X   X")
                    .aisle("XXXXX", "X   X", "X   X")
                    .aisle("XXXXX", "XTTTX", "X   X")
                    .aisle("XXXXX", "XXSXX", "XXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_WATERTIGHT.get()).setMinGlobalLimited(55)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where(' ', air())
                    .where('T', blocks(CASING_TITANIUM_PIPE.get()))
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .renderer(FluidRenderer.create(GTCEu.id("block/casings/gcym/watertight_casing"), GTCEu.id("block/multiblock/gcym/large_chemical_bath")))
            .hasTESR(true)
            .register();

    public static final MultiblockMachineDefinition LARGE_CENTRIFUGE = GTM
            .multiblock("large_centrifuge", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .allRotation()
            .recipeTypes(CENTRIFUGE_RECIPES, THERMAL_CENTRIFUGE_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_VIBRATION_SAFE)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("#XXX#", "XXXXX", "#XXX#")
                    .aisle("XXXXX", "XAPAX", "XXXXX")
                    .aisle("XXXXX", "XPaPX", "XXXXX")
                    .aisle("XXXXX", "XAPAX", "XXXXX")
                    .aisle("#XXX#", "XXSXX", "#XXX#")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_VIBRATION_SAFE.get()).setMinGlobalLimited(40)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('P', blocks(CASING_STEEL_PIPE.get()))
                    .where('A', air())
                    .where('#', any())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/vibration_safe_casing"),
                    GTCEu.id("block/multiblock/gcym/large_centrifuge"))
            .register();

    public static final MultiblockMachineDefinition LARGE_MIXER = GTM
            .multiblock("large_mixer", LargeMixerMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .nonYAxisRotation()
            .recipeTypes(MIXER_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_REACTION_SAFE)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("#XXX#", "#XXX#", "#XXX#", "#XXX#", "#XXX#", "##F##")
                    .aisle("XXXXX", "XAPAX", "XAAAX", "XAPAX", "XAAAX", "##F##")
                    .aisle("XXaXX", "XPPPX", "XAPAX", "XPPPX", "XAGAX", "FFGFF")
                    .aisle("XXXXX", "XAPAX", "XAAAX", "XAPAX", "XAAAX", "##F##")
                    .aisle("#XXX#", "#XSX#", "#XXX#", "#XXX#", "#XXX#", "##F##")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_REACTION_SAFE.get()).setMinGlobalLimited(50)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('F', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.HastelloyX)))
                    .where('G', blocks(CASING_STAINLESS_STEEL_GEARBOX.get()))
                    .where('P', blocks(CASING_TITANIUM_PIPE.get()))
                    .where('A', air())
                    .where('#', any())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .renderer(FluidRenderer.create(GTCEu.id("block/casings/gcym/reaction_safe_mixing_casing"), GTCEu.id("block/multiblock/gcym/large_mixer")))
            .hasTESR(true)
            .register();

    public static final MultiblockMachineDefinition LARGE_ELECTROLYZER = GTM
            .multiblock("large_electrolyzer", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .allRotation()
            .recipeTypes(ELECTROLYZER_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_NONCONDUCTING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("XXXXX", "XXXXX", "XXXXX")
                    .aisle("XXXXX", "XCCCX", "XCCCX")
                    .aisle("XXaXX", "XCCCX", "XCCCX")
                    .aisle("XXXXX", "XXSXX", "XXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_NONCONDUCTING.get()).setMinGlobalLimited(30)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('C', blocks(ELECTROLYTIC_CELL.get()))
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/nonconducting_casing"),
                    GTCEu.id("block/multiblock/gcym/large_electrolyzer"))
            .register();

    public static final MultiblockMachineDefinition LARGE_ELECTROMAGNET = GTM
            .multiblock("large_electromagnet", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .allRotation()
            .recipeTypes(ELECTROMAGNETIC_SEPARATOR_RECIPES, POLARIZER_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_NONCONDUCTING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("XXXXX", "XXXXX", "XXXXX")
                    .aisle("XCXCX", "XCXCX", "XCXCX")
                    .aisle("XCaCX", "XCXCX", "XCXCX")
                    .aisle("XXXXX", "XXSXX", "XXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_NONCONDUCTING.get()).setMinGlobalLimited(35)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('C', blocks(ELECTROLYTIC_CELL.get()))
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/nonconducting_casing"),
                    GTCEu.id("block/multiblock/gcym/large_electrolyzer"))
            .register();

    public static final MultiblockMachineDefinition LARGE_PACKER = GTM
            .multiblock("large_packer", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .allRotation()
            .recipeTypes(PACKER_RECIPES)
            .recipeTypes(GTORecipeTypes.UNPACKER_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_TUNGSTENSTEEL_ROBUST)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("XXX", "XXX", "XXX")
                    .aisle("XXX", "XAX", "XXX")
                    .aisle("XXX", "XAX", "XXX")
                    .aisle("XXX", "XAX", "XXX")
                    .aisle("XXX", "XaX", "XXX")
                    .aisle("XXX", "XSX", "XXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_TUNGSTENSTEEL_ROBUST.get()).setMinGlobalLimited(30)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('A', air())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_robust_tungstensteel"),
                    GTCEu.id("block/multiblock/gcym/large_packer"))
            .register();

    public static final MultiblockMachineDefinition LARGE_ASSEMBLER = GTM
            .multiblock("large_assembler", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .allRotation()
            .recipeTypes(ASSEMBLER_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_LARGE_SCALE_ASSEMBLING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("XXXXXXXXX", "XXXXXXXXX", "XXXXXXXXX")
                    .aisle("XXXXXXXXX", "XAAAXAaAX", "XGGGXXXXX")
                    .aisle("XXXXXXXXX", "XGGGXXSXX", "XGGGX###X")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_LARGE_SCALE_ASSEMBLING.get()).setMinGlobalLimited(40)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('G', blocks(CASING_TEMPERED_GLASS.get()))
                    .where('A', air())
                    .where('#', any())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/large_scale_assembling_casing"),
                    GTCEu.id("block/multiblock/gcym/large_assembler"))
            .register();

    public static final MultiblockMachineDefinition LARGE_CIRCUIT_ASSEMBLER = GTM
            .multiblock("large_circuit_assembler", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .allRotation()
            .recipeTypes(CIRCUIT_ASSEMBLER_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_LARGE_SCALE_ASSEMBLING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("XXXXXXX", "XXXXXXX", "XXXXXXX")
                    .aisle("XXXXXXX", "XPPPPPX", "XGGGGGX")
                    .aisle("XXXXXXX", "XAAAaPX", "XGGGGGX")
                    .aisle("XXXXXXX", "XTTTTXX", "XXXXXXX")
                    .aisle("#####XX", "#####SX", "#####XX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_LARGE_SCALE_ASSEMBLING.get()).setMinGlobalLimited(55)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('T', blocks(CASING_TEMPERED_GLASS.get()))
                    .where('G', blocks(CASING_GRATE.get()))
                    .where('P', blocks(CASING_TUNGSTENSTEEL_PIPE.get()))
                    .where('A', air())
                    .where('#', any())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/large_scale_assembling_casing"),
                    GTCEu.id("block/multiblock/gcym/large_circuit_assembler"))
            .register();

    public static final MultiblockMachineDefinition LARGE_ARC_SMELTER = GTM
            .multiblock("large_arc_smelter", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .allRotation()
            .recipeTypes(ARC_FURNACE_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_HIGH_TEMPERATURE_SMELTING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("#XXX#", "#XXX#", "#XXX#", "#XXX#")
                    .aisle("XXXXX", "XCaCX", "XCACX", "XXXXX")
                    .aisle("XXXXX", "XAAAX", "XAAAX", "XXMXX")
                    .aisle("XXXXX", "XACAX", "XACAX", "XXXXX")
                    .aisle("#XXX#", "#XSX#", "#XXX#", "#XXX#")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_HIGH_TEMPERATURE_SMELTING.get()).setMinGlobalLimited(45)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('C', blocks(MOLYBDENUM_DISILICIDE_COIL_BLOCK.get()))
                    .where('M', abilities(MUFFLER))
                    .where('A', air())
                    .where('#', any())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/high_temperature_smelting_casing"), GTCEu.id("block/multiblock/gcym/large_arc_smelter"))
            .register();

    public static final MultiblockMachineDefinition LARGE_ENGRAVING_LASER = GTM
            .multiblock("large_engraving_laser", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .allRotation()
            .recipeTypes(LASER_ENGRAVER_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_LASER_SAFE_ENGRAVING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("XXXXX", "XXGXX", "XXGXX", "XXXXX")
                    .aisle("XXXXX", "XAAAX", "XAAAX", "XKKKX")
                    .aisle("XXXXX", "GAaAG", "GACAG", "XKXKX")
                    .aisle("XXXXX", "XAAAX", "XAAAX", "XKKKX")
                    .aisle("XXSXX", "XXGXX", "XXGXX", "XXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('C', blocks(CASING_TUNGSTENSTEEL_PIPE.get()))
                    .where('X', blocks(CASING_LASER_SAFE_ENGRAVING.get()).setMinGlobalLimited(45)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('G', blocks(CASING_TEMPERED_GLASS.get()))
                    .where('K', blocks(CASING_GRATE.get()))
                    .where('A', air())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/laser_safe_engraving_casing"),
                    GTCEu.id("block/multiblock/gcym/large_engraving_laser"))
            .register();

    public static final MultiblockMachineDefinition LARGE_SIFTING_FUNNEL = GTM
            .multiblock("large_sifting_funnel", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .nonYAxisRotation()
            .recipeTypes(SIFTER_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_VIBRATION_SAFE)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("#X#X#", "#X#X#", "#XXX#", "XXXXX", "#XXX#")
                    .aisle("XXXXX", "XAXAX", "XKKKX", "XKKKX", "X###X")
                    .aisle("#XXX#", "#XaX#", "XKKKX", "XKKKX", "X###X")
                    .aisle("XXXXX", "XAXAX", "XKKKX", "XKKKX", "X###X")
                    .aisle("#X#X#", "#X#X#", "#XSX#", "XXXXX", "#XXX#")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_VIBRATION_SAFE.get()).setMinGlobalLimited(50)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('K', blocks(CASING_GRATE.get()))
                    .where('A', air())
                    .where('#', any())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/vibration_safe_casing"),
                    GTCEu.id("block/multiblock/gcym/large_sifting_funnel"))
            .register();

    public static final MultiblockMachineDefinition BLAST_ALLOY_SMELTER = GTM
            .multiblock("alloy_blast_smelter", CoilCustomParallelMultiblockMachine.createParallelCoil(m -> {
                if (m.getRecipeType() == ALLOY_SMELTER_RECIPES) {
                    return 1L << (long) (m.getTemperature() / 900.0D);
                }
                return 1;
            }, true, true, false))
            .tooltipsKey("gtocore.machine.recipe.run", Component.translatable("gtceu.alloy_blast_smelter"))
            .tooltipsKey("gtceu.machine.electric_blast_furnace.tooltip.0")
            .tooltipsKey("gtceu.machine.electric_blast_furnace.tooltip.1")
            .tooltipsKey("gtceu.machine.electric_blast_furnace.tooltip.2")
            .tooltipsKey("gtocore.machine.recipe.run", Component.translatable("gtceu.alloy_smelter"))
            .coilParallelTooltips()
            .allRotation()
            .recipeTypes(ALLOY_BLAST_RECIPES)
            .recipeTypes(ALLOY_SMELTER_RECIPES)
            .recipeModifier((m, r) -> {
                if (m instanceof CoilCustomParallelMultiblockMachine machine) {
                    if (machine.getRecipeType() == ALLOY_SMELTER_RECIPES) {
                        return RecipeModifierFunction.overclocking(m, r);
                    } else {
                        return RecipeModifierFunction.ebfOverclock(m, r);
                    }
                }
                return null;
            })
            .block(CASING_HIGH_TEMPERATURE_SMELTING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("#XXX#", "#CCC#", "#GGG#", "#CCC#", "#XXX#")
                    .aisle("XXXXX", "CAAAC", "GAAAG", "CAAAC", "XXXXX")
                    .aisle("XXXXX", "CAAAC", "GAAAG", "CAAAC", "XXMXX")
                    .aisle("XXXXX", "CAAAC", "GAAAG", "CAAAC", "XXXXX")
                    .aisle("#XSX#", "#CCC#", "#GGG#", "#CCC#", "#XXX#")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_HIGH_TEMPERATURE_SMELTING.get()).setMinGlobalLimited(30)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
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

    public static final MultiblockMachineDefinition LARGE_AUTOCLAVE = GTM
            .multiblock("large_autoclave", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .allRotation()
            .recipeTypes(AUTOCLAVE_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_WATERTIGHT)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("XXX", "XXX", "XXX")
                    .aisle("XXX", "XTX", "XXX")
                    .aisle("XaX", "XTX", "XXX")
                    .aisle("XXX", "XTX", "XXX")
                    .aisle("XXX", "XSX", "XXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_WATERTIGHT.get()).setMinGlobalLimited(30)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('T', blocks(CASING_STEEL_PIPE.get()))
                    .where('#', any())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/watertight_casing"),
                    GTCEu.id("block/multiblock/gcym/large_autoclave"))
            .register();

    public static final MultiblockMachineDefinition LARGE_MATERIAL_PRESS = GTM
            .multiblock("large_material_press", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .allRotation()
            .recipeTypes(COMPRESSOR_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_STRESS_PROOF)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("XXXXXXX", "XXXXXXX", "XXXXXXX")
                    .aisle("XXXXXXX", "XaXGGGX", "XXXXXXX")
                    .aisle("XXXXXXX", "XSXCCCX", "XXXXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_STRESS_PROOF.get()).setMinGlobalLimited(40)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('G', blocks(CASING_STEEL_GEARBOX.get()))
                    .where('C', blocks(CASING_TEMPERED_GLASS.get()))
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/stress_proof_casing"),
                    GTCEu.id("block/multiblock/gcym/large_material_press"))
            .register();

    public static final MultiblockMachineDefinition LARGE_BREWER = GTM
            .multiblock("large_brewer", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .allRotation()
            .recipeTypes(BREWING_RECIPES, FERMENTING_RECIPES, FLUID_HEATER_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_CORROSION_PROOF)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("#XXX#", "#XXX#", "#XXX#", "#XXX#", "#####")
                    .aisle("XXXXX", "XCCCX", "XAAAX", "XXAXX", "##X##")
                    .aisle("XXXXX", "XCPCX", "XAPAX", "XAPAX", "#XMX#")
                    .aisle("XXXXX", "XCCCX", "XAAAX", "XXaXX", "##X##")
                    .aisle("#XXX#", "#XSX#", "#XXX#", "#XXX#", "#####")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_CORROSION_PROOF.get()).setMinGlobalLimited(50)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('P', blocks(CASING_STEEL_PIPE.get()))
                    .where('C', blocks(MOLYBDENUM_DISILICIDE_COIL_BLOCK.get()))
                    .where('M', abilities(MUFFLER))
                    .where('A', air())
                    .where('#', any())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/corrosion_proof_casing"),
                    GTCEu.id("block/multiblock/gcym/large_brewer"))
            .register();

    public static final MultiblockMachineDefinition LARGE_CUTTER = GTM
            .multiblock("large_cutter", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .allRotation()
            .recipeTypes(CUTTER_RECIPES, LATHE_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_SHOCK_PROOF)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("XXXXXXX", "XXXXXXX", "XXXXXXX", "##XXXXX")
                    .aisle("XXXXXXX", "XAXCCCX", "XXXAAAX", "##XXXXX")
                    .aisle("XXXXXXX", "XaXCCCX", "XXXAAAX", "##XXXXX")
                    .aisle("XXXXXXX", "XSXGGGX", "XXXGGGX", "##XXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_SHOCK_PROOF.get()).setMinGlobalLimited(65)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('G', blocks(CASING_TEMPERED_GLASS.get()))
                    .where('C', blocks(SLICING_BLADES.get()))
                    .where('A', air())
                    .where('#', any())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/shock_proof_cutting_casing"),
                    GTCEu.id("block/multiblock/gcym/large_cutter"))
            .register();

    public static final MultiblockMachineDefinition LARGE_DISTILLERY = GTM
            .multiblock("large_distillery", largeLDistillationTowerMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .nonYAxisRotation()
            .recipeTypes(DISTILLATION_RECIPES, DISTILLERY_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_WATERTIGHT)
            .pattern(definition -> {
                TraceabilityPredicate casingPredicate = blocks(CASING_WATERTIGHT.get()).setMinGlobalLimited(40);
                return FactoryBlockPattern.start(definition, RIGHT, BACK, UP)
                        .aisle("#YYY#", "YYYYY", "YYYYY", "YYYYY", "#YYY#")
                        .aisle("#YSY#", "YAAAY", "YAaAY", "YAAAY", "#YYY#")
                        .aisle("##X##", "#XAX#", "XAPAX", "#XAX#", "##X##").setRepeatable(1, 12)
                        .aisle("#####", "#ZZZ#", "#ZCZ#", "#ZZZ#", "#####")
                        .where('S', controller(blocks(definition.get())))
                        .where('Y', casingPredicate.or(abilities(IMPORT_ITEMS))
                                .or(abilities(INPUT_ENERGY).setMinGlobalLimited(1).setMaxGlobalLimited(8))
                                .or(abilities(IMPORT_FLUIDS).setMinGlobalLimited(1))
                                .or(Predicates.abilities(GTOPartAbility.ACCELERATE_HATCH).setMaxGlobalLimited(1))
                                .or(Predicates.blocks(ManaMachine.MANA_AMPLIFIER_HATCH.getBlock()).setMaxGlobalLimited(1))
                                .or(abilities(GTOPartAbility.ITEMS_OUTPUT).or(blocks(GTAEMachines.ITEM_EXPORT_BUS_ME.get())).setMaxLayerLimited(1))
                                .or(autoAbilities(true, false, true)))
                        .where('X', casingPredicate.or(abilities(PartAbility.EXPORT_FLUIDS_1X).or(blocks(GTAEMachines.FLUID_EXPORT_HATCH_ME.get())).setMaxLayerLimited(1)))
                        .where('Z', casingPredicate)
                        .where('P', blocks(CASING_STEEL_PIPE.get()))
                        .where('C', abilities(MUFFLER))
                        .where('A', air())
                        .where('#', any())
                        .where('a', GTOPredicates.integralFramework())
                        .build();
            })
            .shapeInfos(definition -> {
                List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
                var builder = MultiblockShapeInfo.builder()
                        .where('S', definition, Direction.NORTH)
                        .where('C', CASING_WATERTIGHT.getDefaultState())
                        .where('M', MUFFLER_HATCH[IV], Direction.UP)
                        .where('X', PARALLEL[IV], Direction.NORTH)
                        .where('H', FLUID_IMPORT_HATCH[IV], Direction.NORTH)
                        .where('B', ITEM_EXPORT_BUS[IV], Direction.NORTH)
                        .where('N', MAINTENANCE_HATCH, Direction.NORTH)
                        .where('P', CASING_STEEL_PIPE.getDefaultState())
                        .where('F', FLUID_EXPORT_HATCH[IV], Direction.SOUTH)
                        .where('E', ENERGY_INPUT_HATCH[IV], Direction.SOUTH)
                        .where('a', INTEGRAL_FRAMEWORK_MV.get())
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
                aisle3.add("C#a#C");
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
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/watertight_casing"),
                    GTCEu.id("block/multiblock/gcym/large_distillery"))
            .register();

    public static final MultiblockMachineDefinition LARGE_EXTRACTOR = GTM
            .multiblock("large_extractor", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .allRotation()
            .recipeTypes(EXTRACTOR_RECIPES, CANNER_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_WATERTIGHT)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("XXXXX", "XXXXX", "XXXXX")
                    .aisle("XXXXX", "XCaCX", "XXXXX")
                    .aisle("XXXXX", "XXSXX", "XXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_WATERTIGHT.get()).setMinGlobalLimited(25)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('C', blocks(CASING_STEEL_PIPE.get()))
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/watertight_casing"),
                    GTCEu.id("block/multiblock/gcym/large_extractor"))
            .register();

    public static final MultiblockMachineDefinition LARGE_EXTRUDER = GTM
            .multiblock("large_extruder", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .allRotation()
            .recipeTypes(EXTRUDER_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_STRESS_PROOF)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("##XXX", "##XXX", "##XXX")
                    .aisle("##XXX", "##XPX", "##XGX").setRepeatable(2)
                    .aisle("XXXXX", "XXXPX", "XXXGX")
                    .aisle("XXXXX", "XaXPX", "XXXGX")
                    .aisle("XXXXX", "XSXXX", "XXXXX")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_STRESS_PROOF.get()).setMinGlobalLimited(40)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('P', blocks(CASING_TITANIUM_PIPE.get()))
                    .where('G', blocks(CASING_TEMPERED_GLASS.get()))
                    .where('a', GTOPredicates.integralFramework())
                    .where('#', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/stress_proof_casing"),
                    GTCEu.id("block/multiblock/gcym/large_extruder"))
            .register();

    public static final MultiblockMachineDefinition LARGE_SOLIDIFIER = GTM
            .multiblock("large_solidifier", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .allRotation()
            .recipeTypes(FLUID_SOLIDFICATION_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_WATERTIGHT)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("#XXX#", "#XXX#", "#XXX#", "#XXX#")
                    .aisle("XXXXX", "XCACX", "XCACX", "XXXXX")
                    .aisle("XXXXX", "XAAAX", "XAAAX", "XXXXX")
                    .aisle("XXXXX", "XCaCX", "XCACX", "XXXXX")
                    .aisle("#XXX#", "#XSX#", "#XXX#", "#XXX#")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_WATERTIGHT.get()).setMinGlobalLimited(45)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('C', blocks(CASING_STEEL_PIPE.get()))
                    .where('A', air())
                    .where('#', any())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/watertight_casing"),
                    GTCEu.id("block/multiblock/gcym/large_solidifier"))
            .register();

    public static final MultiblockMachineDefinition LARGE_WIREMILL = GTM
            .multiblock("large_wiremill", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .allRotation()
            .recipeTypes(WIREMILL_RECIPES)
            .recipeTypes(GTORecipeTypes.LOOM_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_STRESS_PROOF)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("XXXXX", "XXXXX", "XXX##")
                    .aisle("XXXXX", "XaCCX", "XXXXX")
                    .aisle("XXXXX", "XSXXX", "XXX##")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_STRESS_PROOF.get()).setMinGlobalLimited(25)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('C', blocks(CASING_TITANIUM_GEARBOX.get()))
                    .where('#', any())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/stress_proof_casing"),
                    GTCEu.id("block/multiblock/gcym/large_wiremill"))
            .register();

    public static final MultiblockMachineDefinition MEGA_BLAST_FURNACE = GTM
            .multiblock("mega_blast_furnace", MegaBlastFurnaceMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .nonYAxisRotation()
            .recipeTypes(BLAST_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_HIGH_TEMPERATURE_SMELTING)
            .pattern(definition -> {
                TraceabilityPredicate casing = blocks(CASING_HIGH_TEMPERATURE_SMELTING.get()).setMinGlobalLimited(360);
                return FactoryBlockPattern.start(definition)
                        .aisle("##XXXXXXXXX##", "##XXXXXXXXX##", "#############", "#############", "#############", "#############", "#############", "#############", "#############", "#############", "#############", "#############", "#############", "#############", "#############", "#############", "#############")
                        .aisle("#XXXXXXXXXXX#", "#XXXXXXXXXXX#", "###F#####F###", "###F#####F###", "###FFFFFFF###", "#############", "#############", "#############", "#############", "#############", "####FFFFF####", "#############", "#############", "#############", "#############", "#############", "#############")
                        .aisle("XXXXXXXXXXXXX", "XXXXVVVVVXXXX", "##F#######F##", "##F#######F##", "##FFFHHHFFF##", "##F#######F##", "##F#######F##", "##F#######F##", "##F#######F##", "##F#######F##", "##FFFHHHFFF##", "#############", "#############", "#############", "#############", "#############", "###TTTTTTT###")
                        .aisle("XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "#F####P####F#", "#F####P####F#", "#FFHHHPHHHFF#", "######P######", "######P######", "######P######", "######P######", "######P######", "##FHHHPHHHF##", "######P######", "######P######", "######P######", "######P######", "######P######", "##TTTTPTTTT##")
                        .aisle("XXXXXXXXXXXXX", "XXVXXXXXXXVXX", "####BBPBB####", "####TITIT####", "#FFHHHHHHHFF#", "####BITIB####", "####CCCCC####", "####CCCCC####", "####CCCCC####", "####BITIB####", "#FFHHHHHHHFF#", "####BITIB####", "####CCCCC####", "####CCCCC####", "####CCCCC####", "####BITIB####", "##TTTTPTTTT##")
                        .aisle("XXXXXXXXXXXXX", "XXVXXXXXXXVXX", "####BAAAB####", "####IAAAI####", "#FHHHAAAHHHF#", "####IAAAI####", "####CAAAC####", "####CAAAC####", "####CAAAC####", "####IAAAI####", "#FHHHAAAHHHF#", "####IAAAI####", "####CAAAC####", "####CAAAC####", "####CAAAC####", "####IAAAI####", "##TTTTPTTTT##")
                        .aisle("XXXXXXXXXXXXX", "XXVXXXXXXXVXX", "###PPAAAPP###", "###PTAAATP###", "#FHPHAAAHPHF#", "###PTAAATP###", "###PCAAACP###", "###PCAAACP###", "###PCAAACP###", "###PTAAATP###", "#FHPHAAAHPHF#", "###PTAAATP###", "###PCAAACP###", "###PCAAACP###", "###PCAAACP###", "###PTAAATP###", "##TPPPMPPPT##")
                        .aisle("XXXXXXXXXXXXX", "XXVXXXXXXXVXX", "####BAAAB####", "####IAAAI####", "#FHHHAAAHHHF#", "####IAAAI####", "####CAAAC####", "####CAAAC####", "####CAAAC####", "####IAAAI####", "#FHHHAAAHHHF#", "####IAAAI####", "####CAAAC####", "####CAAAC####", "####CAAAC####", "####IAAAI####", "##TTTTPTTTT##")
                        .aisle("XXXXXXXXXXXXX", "XXVXXXXXXXVXX", "####BBPBB####", "####TITIT####", "#FFHHHHHHHFF#", "####BITIB####", "####CCCCC####", "####CCCCC####", "####CCCCC####", "####BITIB####", "#FFHHHHHHHFF#", "####BITIB####", "####CCCCC####", "####CCCCC####", "####CCCCC####", "####BITIB####", "##TTTTPTTTT##")
                        .aisle("XXXXXXXXXXXXX", "XXXXXXXXXXXXX", "#F####P####F#", "#F####P####F#", "#FFHHHPHHHFF#", "######P######", "######P######", "######P######", "######P######", "######P######", "##FHHHPHHHF##", "######P######", "######P######", "######P######", "######P######", "######P######", "##TTTTPTTTT##")
                        .aisle("XXXXXXXXXXXXX", "XXXXVVVVVXXXX", "##F#######F##", "##F#######F##", "##FFFHHHFFF##", "##F#######F##", "##F#######F##", "##F#######F##", "##F#######F##", "##F#######F##", "##FFFHHHFFF##", "#############", "#############", "#############", "#############", "#############", "###TTTTTTT###")
                        .aisle("#XXXXXXXXXXX#", "#XXXXXaXXXXX#", "###F#####F###", "###F#####F###", "###FFFFFFF###", "#############", "#############", "#############", "#############", "#############", "####FFFFF####", "#############", "#############", "#############", "#############", "#############", "#############")
                        .aisle("##XXXXXXXXX##", "##XXXXSXXXX##", "#############", "#############", "#############", "#############", "#############", "#############", "#############", "#############", "#############", "#############", "#############", "#############", "#############", "#############", "#############")
                        .where('S', controller(blocks(definition.get())))
                        .where('X', casing.or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
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
                        .where('a', GTOPredicates.integralFramework())
                        .build();
            })
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/high_temperature_smelting_casing"),
                    GTCEu.id("block/multiblock/gcym/mega_blast_furnace"))
            .register();

    public static final MultiblockMachineDefinition MEGA_VACUUM_FREEZER = GTM
            .multiblock("mega_vacuum_freezer", GCYMMultiblockMachine::new)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .allRotation()
            .recipeTypes(VACUUM_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(CASING_ALUMINIUM_FROSTPROOF)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("XXXXXXX#KKK", "XXXXXXX#KVK", "XXXXXXX#KVK", "XXXXXXX#KVK", "XXXXXXX#KKK", "XXXXXXX####", "XXXXXXX####")
                    .aisle("XXXXXXX#KVK", "XPPPPPPPPPV", "XPAPAPX#VPV", "XPPPPPPPPPV", "XPAPAPX#KVK", "XPPPPPX####", "XXXXXXX####")
                    .aisle("XXXaXXX#KVK", "XPAPAPXAVPV", "XAAAAAX#VPV", "XPAAAPX#VPV", "XAAAAAX#KVK", "XPAPAPX####", "XXXXXXX####")
                    .aisle("XXXaXXX#KVK", "XPAPAPPPPPV", "XAAAAAX#VPV", "XPAAAPPPPPV", "XAAAAAX#KVK", "XPAPAPX####", "XXXXXXX####")
                    .aisle("XXXXXXX#KKK", "XPPPPPX#KVK", "XPA#APX#KVK", "XPAAAPX#KVK", "XPAAAPX#KKK", "XPPPPPX####", "XXXXXXX####")
                    .aisle("#XXXXX#####", "#XXSXX#####", "#XGGGX#####", "#XGGGX#####", "#XGGGX#####", "#XXXXX#####", "###########")
                    .where('S', controller(blocks(definition.get())))
                    .where('X', blocks(CASING_ALUMINIUM_FROSTPROOF.get()).setMinGlobalLimited(140)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('G', blocks(CASING_TEMPERED_GLASS.get()))
                    .where('K', blocks(CASING_STAINLESS_CLEAN.get()))
                    .where('P', blocks(CASING_TUNGSTENSTEEL_PIPE.get()))
                    .where('V', blocks(HEAT_VENT.get()))
                    .where('A', air())
                    .where('#', any())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_frost_proof"),
                    GTCEu.id("block/multiblock/gcym/mega_vacuum_freezer"))
            .register();

    public static final MultiblockMachineDefinition MEGA_ALLOY_BLAST_SMELTER = multiblock("mega_alloy_blast_smelter", "巨型合金冶炼炉", MegaBlastFurnaceMachine::new)
            .nonYAxisRotation()
            .recipeTypes(GCYMRecipeTypes.ALLOY_BLAST_RECIPES)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .tooltipsKey("gtceu.machine.electric_blast_furnace.tooltip.2")
            .parallelizableTooltips()
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(GCYMBlocks.CASING_HIGH_TEMPERATURE_SMELTING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("   eeeee   ", "   cbbbc   ", "   cbbbc   ", "   cbbbc   ", "   eeeee   ", "   bbbbb   ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ")
                    .aisle("  ebbbbbe  ", "  c     c  ", "  c     c  ", "  c     c  ", "  efffffe  ", "  bbbbbbb  ", "   bbbbb   ", "   ccccc   ", "   ccccc   ", "   ccccc   ", "   ccccc   ", "   ccccc   ", "   ccccc   ", "   ccccc   ", "   ccccc   ", "   ccccc   ", "   bbbbb   ", "           ")
                    .aisle(" ebbbbbbbe ", " cbeeeeebc ", " cbeeeeebc ", " cbeeeeebc ", " ebeeeeebe ", " bbbbbbbbb ", "  baaaaab  ", "  caaaaac  ", "  caaaaac  ", "  caaaaac  ", "  caaaaac  ", "  caaaaac  ", "  caaaaac  ", "  caaaaac  ", "  caaaaac  ", "  caaaaac  ", "  bbbbbbb  ", "   bbbbb   ")
                    .aisle("ebbbbbbbbbe", "c ehhhhhe c", "c eiiiiie c", "c ejjjjje c", "efeeeeeeefe", "bbbb   bbbb", " baa   aab ", " caa   aac ", " caa   aac ", " caa   aac ", " caa   aac ", " caa   aac ", " caa   aac ", " caa   aac ", " caa   aac ", " caa   aac ", " bbb   bbb ", "  bbbbbbb  ")
                    .aisle("ebbbbbbbbbe", "b ehhhhhe b", "b eiiiiie b", "b ejjjjje b", "efeeeeeeefe", "bbb     bbb", " ba     ab ", " ca     ac ", " ca     ac ", " ca     ac ", " ca     ac ", " ca     ac ", " ca     ac ", " ca     ac ", " ca     ac ", " ca     ac ", " bb     bb ", "  bbbbbbb  ")
                    .aisle("ebbbbbbbbbe", "b ehhkhhe b", "b eiikiie b", "b ejjkjje b", "efeeeAeeefe", "bbb  k  bbb", " ba  k  ab ", " ca  k  ac ", " ca  k  ac ", " ca  k  ac ", " ca  k  ac ", " ca  k  ac ", " ca  k  ac ", " ca  k  ac ", " ca  k  ac ", " ca  k  ac ", " bb  k  bb ", "  bbbgbbb  ")
                    .aisle("ebbbbbbbbbe", "b ehhhhhe b", "b eiiiiie b", "b ejjjjje b", "efeeeeeeefe", "bbb     bbb", " ba     ab ", " ca     ac ", " ca     ac ", " ca     ac ", " ca     ac ", " ca     ac ", " ca     ac ", " ca     ac ", " ca     ac ", " ca     ac ", " bb     bb ", "  bbbbbbb  ")
                    .aisle("ebbbbbbbbbe", "c ehhhhhe c", "c eiiiiie c", "c ejjjjje c", "efeeeeeeefe", "bbbb   bbbb", " baa   aab ", " caa   aac ", " caa   aac ", " caa   aac ", " caa   aac ", " caa   aac ", " caa   aac ", " caa   aac ", " caa   aac ", " caa   aac ", " bbb   bbb ", "  bbbbbbb  ")
                    .aisle(" ebbbbbbbe ", " cbeeeeebc ", " cbeeeeebc ", " cbeeeeebc ", " ebeeeeebe ", " bbbbbbbbb ", "  baaaaab  ", "  caaaaac  ", "  caaaaac  ", "  caaaaac  ", "  caaaaac  ", "  caaaaac  ", "  caaaaac  ", "  caaaaac  ", "  caaaaac  ", "  caaaaac  ", "  bbbbbbb  ", "   bbbbb   ")
                    .aisle("  ebbbbbe  ", "  c     c  ", "  c     c  ", "  c     c  ", "  efffffe  ", "  bbbbbbb  ", "   bbbbb   ", "   ccccc   ", "   ccccc   ", "   ccccc   ", "   ccccc   ", "   ccccc   ", "   ccccc   ", "   ccccc   ", "   ccccc   ", "   ccccc   ", "   bbbbb   ", "           ")
                    .aisle("   eeeee   ", "   cbbbc   ", "   cb~bc   ", "   cbbbc   ", "   eeeee   ", "   bbbbb   ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ", "           ")
                    .where('~', controller(blocks(definition.get())))
                    .where('b', blocks(GCYMBlocks.CASING_HIGH_TEMPERATURE_SMELTING.get()).setMinGlobalLimited(280)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(abilities(MAINTENANCE).setExactLimit(1))
                            .or(abilities(PARALLEL_HATCH).setMaxGlobalLimited(1)))
                    .where('a', heatingCoils())
                    .where('g', abilities(MUFFLER))
                    .where('e', blocks(GCYMBlocks.HEAT_VENT.get()))
                    .where('c', blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                    .where('f', blocks(GTBlocks.CASING_EXTREME_ENGINE_INTAKE.get()))
                    .where('h', blocks(GTBlocks.FIREBOX_STEEL.get()))
                    .where('i', blocks(GTBlocks.FIREBOX_TITANIUM.get()))
                    .where('j', blocks(GTBlocks.FIREBOX_TUNGSTENSTEEL.get()))
                    .where('k', blocks(GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()))
                    .where(' ', any())
                    .where('A', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/high_temperature_smelting_casing"), GTCEu.id("block/multiblock/gcym/blast_alloy_smelter"))
            .register();

    public static final MultiblockMachineDefinition LARGE_ROCK_CRUSHER = multiblock("large_rock_crusher", "大型碎岩机", GCYMMultiblockMachine::new)
            .allRotation()
            .recipeTypes(GTRecipeTypes.ROCK_BREAKER_RECIPES)
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .tooltipsText("需要在输入仓中放入对应流体", "Requires the corresponding fluid to be placed in the input chamber")
            .parallelizableTooltips()
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(GCYMBlocks.CASING_SECURE_MACERATION)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("AAAAA", "AAAAA", "AAAAA", "AAAAA")
                    .aisle("AAAAA", "ABBBA", "A   A", "A C A")
                    .aisle("AAAAA", "ABaBA", "A   A", "ACCCA")
                    .aisle("AAAAA", "ABBBA", "A   A", "A C A")
                    .aisle("AAAAA", "AA~AA", "AAAAA", "AAAAA")
                    .where('~', controller(blocks(definition.get())))
                    .where('A', blocks(GCYMBlocks.CASING_SECURE_MACERATION.get())
                            .or(abilities(GTOPartAbility.ACCELERATE_HATCH).setMaxGlobalLimited(1))
                            .or(abilities(IMPORT_ITEMS).setMaxGlobalLimited(1).setPreviewCount(1))
                            .or(abilities(EXPORT_ITEMS).setMaxGlobalLimited(1).setPreviewCount(1))
                            .or(abilities(IMPORT_FLUIDS_1X).setMaxGlobalLimited(2).setPreviewCount(1))
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(8).setPreviewCount(1))
                            .or(abilities(PARALLEL_HATCH).setMaxGlobalLimited(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('B', blocks(GCYMBlocks.CRUSHING_WHEELS.get()))
                    .where('C', frames(GTMaterials.MaragingSteel300))
                    .where(' ', air())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/secure_maceration_casing"), GTCEu.id("block/machines/rock_crusher"))
            .register();

    public static final MultiblockMachineDefinition LARGE_BENDER = multiblock("large_bender", "大型卷弯机", GCYMMultiblockMachine::new)
            .allRotation()
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .recipeTypes(GTRecipeTypes.BENDER_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(GCYMBlocks.CASING_STRESS_PROOF)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("AAAAA", "A   A", "AAAAA", " AAA ")
                    .aisle("AAAAA", "A   A", "ACCCA", "AAAAA")
                    .aisle("AAAAA", "ADDDA", "DDDDD", "AAAAA")
                    .aisle("AAAAA", "ABaBA", "ABBBA", "AAAAA")
                    .aisle("AAAAA", "ADDDA", "DDDDD", "AAAAA")
                    .aisle("AAAAA", "A   A", "ACCCA", "AAAAA")
                    .aisle("AA~AA", "A   A", "AAAAA", " AAA ")
                    .where('~', controller(blocks(definition.get())))
                    .where('A', blocks(GCYMBlocks.CASING_STRESS_PROOF.get())
                            .setMinGlobalLimited(80)
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('B', air())
                    .where('C', blocks(GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()))
                    .where('D', blocks(GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX.get()))
                    .where(' ', any())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/stress_proof_casing"), GTCEu.id("block/multiblock/gcym/large_material_press"))
            .register();

    public static final MultiblockMachineDefinition LARGE_ROLLING = multiblock("large_rolling", "大型辊轧机", GCYMMultiblockMachine::new)
            .allRotation()
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .recipeTypes(GTORecipeTypes.ROLLING_RECIPES)
            .recipeTypes(GTORecipeTypes.CLUSTER_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(GCYMBlocks.CASING_STRESS_PROOF)
            .pattern(definition -> FactoryBlockPattern.start(definition, RelativeDirection.FRONT, RelativeDirection.UP, RelativeDirection.RIGHT)
                    .aisle("AAAAA", "B   B", "CCCCC", "     ")
                    .aisle("ACCCA", "B   B", "CBBBC", "     ")
                    .aisle("ADDDA", "E   E", "CDDDC", "ACCCA")
                    .aisle("AFaFG", "E   E", "CFFFC", "ACCCA")
                    .aisle("ADDDA", "E   E", "CDDDC", "ACCCA")
                    .aisle("ACCCA", "B   B", "CBBBC", "     ")
                    .aisle("AAAAA", "B   B", "CCCCC", "     ")
                    .where('B', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Tungsten)))
                    .where('C', blocks(GCYMBlocks.CASING_STRESS_PROOF.get()))
                    .where('D', blocks(GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX.get()))
                    .where('E', blocks(GTBlocks.CASING_GRATE.get()))
                    .where('F', blocks(GTBlocks.STEEL_HULL.get()))
                    .where('G', controller(blocks(definition.get())))
                    .where('A', blocks(GCYMBlocks.CASING_STRESS_PROOF.get())
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where(' ', any())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/stress_proof_casing"), GTCEu.id("block/multiblock/gcym/large_material_press"))
            .register();

    public static final MultiblockMachineDefinition LARGE_FORMING = multiblock("large_forming", "大型冲压机", GCYMMultiblockMachine::new)
            .allRotation()
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .recipeTypes(GTRecipeTypes.FORGE_HAMMER_RECIPES)
            .recipeTypes(GTRecipeTypes.FORMING_PRESS_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(GCYMBlocks.CASING_STRESS_PROOF)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("EEEEE", "AAAAA", "DDDDD", "DHHHD", "DDDDD", "DHHHD", "DDDDD", "     ")
                    .aisle("EEEEE", "AFFFA", "D   D", "     ", "DFFFD", "CGGGC", "DD DD", " DDD ")
                    .aisle("EEEEE", "AFFFA", "     ", "     ", "DFFFD", "CGGGC", "D   D", " DDD ")
                    .aisle("EEaEE", "AFFFA", "D   D", "     ", "DFFFD", "CGGGC", "DD DD", " DDD ")
                    .aisle("AABAA", "A   A", "A   A", "ACCCA", "DDDDD", "DCCCD", "DDDDD", "     ")
                    .where('B', controller(blocks(definition.get())))
                    .where('C', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.BlueSteel)))
                    .where('D', blocks(GCYMBlocks.CASING_STRESS_PROOF.get()))
                    .where('E', blocks(GTBlocks.STEEL_HULL.get()))
                    .where('F', blocks(ChemicalHelper.getBlock(TagPrefix.block, GTMaterials.TungstenSteel)))
                    .where('G', blocks(GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX.get()))
                    .where('H', blocks(GTBlocks.CASING_GRATE.get()))
                    .where('A', blocks(GCYMBlocks.CASING_STRESS_PROOF.get())
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where(' ', any())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/stress_proof_casing"), GTCEu.id("block/multiblock/gcym/large_material_press"))
            .register();

    public static final MultiblockMachineDefinition LARGE_ARC_GENERATOR = multiblock("large_arc_generator", "大型电弧发生器", GCYMMultiblockMachine::new)
            .nonYAxisRotation()
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .recipeTypes(GTORecipeTypes.ARC_GENERATOR_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(GCYMBlocks.CASING_NONCONDUCTING)
            .pattern(definition -> FactoryBlockPattern.start(definition, RelativeDirection.FRONT, RelativeDirection.UP, RelativeDirection.RIGHT)
                    .aisle("AaaaaaA", "AaaaaaA", "ABCCCBA", "A A A A", "       ", "       ", "       ", "       ", "       ", "       ", "       ", "       ", "       ", "       ")
                    .aisle("aBBBBBa", "aD E Da", "BD C DB", "ADDDDDA", " DADAD ", "       ", "  DDD  ", "       ", "  DDD  ", "       ", "  DDD  ", "       ", "  DDD  ", "       ")
                    .aisle("aBBBBBa", "a  E  a", "B  C  B", "ADDDDDA", " DADAD ", "  A A  ", " DA AD ", "  A A  ", " DA AD ", "  A A  ", " DA AD ", "  A A  ", " DA AD ", "  A A  ")
                    .aisle("aBBBBBa", "BEEbEEF", "BCCCCCB", "ADDDDDA", " DADAD ", "       ", " D   D ", "       ", " D   D ", "       ", " D   D ", "       ", " D   D ", "       ")
                    .aisle("aBBBBBa", "a  E  a", "B  C  B", "ADDDDDA", " DADAD ", "  A A  ", " DA AD ", "  A A  ", " DA AD ", "  A A  ", " DA AD ", "  A A  ", " DA AD ", "  A A  ")
                    .aisle("aBBBBBa", "aD E Da", "BD C DB", "ADDDDDA", " DADAD ", "       ", "  DDD  ", "       ", "  DDD  ", "       ", "  DDD  ", "       ", "  DDD  ", "       ")
                    .aisle("AaaaaaA", "AaaaaaA", "ABCCCBA", "A A A A", "       ", "       ", "       ", "       ", "       ", "       ", "       ", "       ", "       ", "       ")
                    .where('A', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.StainlessSteel)))
                    .where('B', blocks(GCYMBlocks.CASING_NONCONDUCTING.get()))
                    .where('C', blocks(GTBlocks.SUPERCONDUCTING_COIL.get()))
                    .where('D', blocks(GTBlocks.CASING_PALLADIUM_SUBSTATION.get()))
                    .where('E', blocks(GCYMBlocks.ELECTROLYTIC_CELL.get()))
                    .where('F', controller(blocks(definition.get())))
                    .where('a', blocks(GCYMBlocks.CASING_NONCONDUCTING.get())
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where(' ', any())
                    .where('b', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/nonconducting_casing"), GTCEu.id("block/multiblock/gcym/large_assembler"))
            .register();

    public static final MultiblockMachineDefinition LARGE_LAMINATOR = multiblock("large_laminator", "大型过胶机", GCYMMultiblockMachine::new)
            .nonYAxisRotation()
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .recipeTypes(GTORecipeTypes.LAMINATOR_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(GTBlocks.CASING_PTFE_INERT)
            .pattern(definition -> FactoryBlockPattern.start(definition, RelativeDirection.BACK, RelativeDirection.UP, RelativeDirection.LEFT)
                    .aisle("ABBBB", "ABCBB", "ABBBB")
                    .aisle("ABBBB", "ADCDB", "AEEEB")
                    .aisle("ABBBB", "AFCFB", "AEEEB")
                    .aisle("ABBBB", "GaCFB", "AEEEB")
                    .aisle("ABBBB", "AFCFB", "AEEEB")
                    .aisle("ABBBB", "ADCDB", "AEEEB")
                    .aisle("ABBBB", "ABCBB", "ABBBB")
                    .where('A', blocks(GTBlocks.CASING_PTFE_INERT.get())
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('B', blocks(GTBlocks.CASING_PTFE_INERT.get()))
                    .where('C', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Polytetrafluoroethylene)))
                    .where('D', blocks(GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX.get()))
                    .where('E', blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                    .where('F', blocks(GTBlocks.CASING_POLYTETRAFLUOROETHYLENE_PIPE.get()))
                    .where('G', controller(blocks(definition.get())))
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_inert_ptfe"), GTCEu.id("block/multiblock/gcym/large_assembler"))
            .register();

    public static final MultiblockMachineDefinition LARGE_LASER_WELDER = multiblock("large_laser_welder", "大型激光焊接机", GCYMMultiblockMachine::new)
            .allRotation()
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .recipeTypes(GTORecipeTypes.LASER_WELDER_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(GCYMBlocks.CASING_LASER_SAFE_ENGRAVING)
            .pattern(definition -> FactoryBlockPattern.start(definition)
                    .aisle("AAA", "AAA", "AAA")
                    .aisle("ECE", "C C", "FFF")
                    .aisle("ECE", "CaC", "FFF")
                    .aisle("ECE", "CDC", "FFF")
                    .aisle("ACA", "CDC", "CCC")
                    .aisle("ACA", "CDC", "CCC")
                    .aisle("ACA", "CDC", "CCC")
                    .aisle("AAA", "ABA", "AAA")
                    .where('A', blocks(GCYMBlocks.CASING_LASER_SAFE_ENGRAVING.get())
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where('B', controller(blocks(definition.get())))
                    .where('C', blocks(GCYMBlocks.CASING_LASER_SAFE_ENGRAVING.get()))
                    .where('D', blocks(GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()))
                    .where('E', blocks(GTBlocks.CASING_GRATE.get()))
                    .where('F', blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                    .where(' ', any())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/laser_safe_engraving_casing"), GTCEu.id("block/multiblock/gcym/large_assembler"))
            .register();

    public static final MultiblockMachineDefinition LARGE_CRUSHER = multiblock("large_crusher", "大型破碎机", GCYMMultiblockMachine::new)
            .nonYAxisRotation()
            .eutMultiplierTooltips(0.8)
            .durationMultiplierTooltips(0.6)
            .parallelizableTooltips()
            .recipeTypes(GTORecipeTypes.CRUSHER_RECIPES)
            .recipeModifier(RecipeModifierFunction.GCYM_OVERCLOCKING)
            .block(GCYMBlocks.CASING_SECURE_MACERATION)
            .pattern(definition -> FactoryBlockPattern.start(definition, RelativeDirection.FRONT, RelativeDirection.UP, RelativeDirection.RIGHT)
                    .aisle("AAAAAAAAA", "XXXXXXXXX", "AAAAAAAAA", "AAAAAAAAA", "     AAA ")
                    .aisle("AAAAAAAAA", "  BCB CCX", "A BCBCBCA", "ADDAABCBA", "    A   A")
                    .aisle("AAAAAAAAA", "      Ca~", "A    CBCA", "ADDAABCBA", "    A   A")
                    .aisle("AAAAAAAAA", "  BCB CCX", "A BCBCBCA", "ADDAABCBA", "    A   A")
                    .aisle("AAAAAAAAA", "XXXXXXXXX", "AAAAAAAAA", "AAAAAAAAA", "     AAA ")
                    .where('~', controller(blocks(definition.get())))
                    .where('A', blocks(GCYMBlocks.CASING_SECURE_MACERATION.get()))
                    .where('B', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.Ultimet)))
                    .where('C', blocks(GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX.get()))
                    .where('D', blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                    .where('X', blocks(GCYMBlocks.CASING_SECURE_MACERATION.get())
                            .or(GTOPredicates.autoGCYMAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true)))
                    .where(' ', any())
                    .where('a', GTOPredicates.integralFramework())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/gcym/secure_maceration_casing"), GTCEu.id("block/multiblock/gcym/large_maceration_tower"))
            .register();
}
