package com.gto.gtocore.common.data.machines;

import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.machine.multiblock.CoilMultiblockMachine;
import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.api.pattern.GTOPredicates;
import com.gto.gtocore.client.renderer.machine.ArrayMachineRenderer;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOMachines;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.common.machine.multiblock.electric.ProcessingArrayMachine;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.phys.shapes.Shapes;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.DUMMY_RECIPES;
import static com.gto.gtocore.common.data.GTOMachines.multiblock;

public final class MultiBlockMachineG {

    public static void init() {}

    public static final MultiblockMachineDefinition[] PROCESSING_ARRAY = GTOMachines.registerTieredMultis("processing_array", t -> GTOValues.VNFR[t] + "处理阵列",
            ProcessingArrayMachine::new, (tier, builder) -> builder
                    .langValue(VNF[tier] + " Processing Array")
                    .nonYAxisRotation()
                    .recipe(DUMMY_RECIPES)
                    .tooltipsKey("gtocore.machine.processing_array.tooltip.0")
                    .tooltipsKey("gtocore.machine.processing_array.tooltip.1")
                    .tooltipsKey("gtceu.universal.tooltip.parallel", ProcessingArrayMachine.getMachineLimit(tier))
                    .alwaysTryModifyRecipe(true)
                    .customTooltipsBuilder(false, true, false)
                    .block(() -> ProcessingArrayMachine.getCasingState(tier))
                    .blockProp(p -> p.noOcclusion().isViewBlocking((state, level, pos) -> false))
                    .shape(Shapes.box(0.001, 0.001, 0.001, 0.999, 0.999, 0.999))
                    .pattern(definition -> FactoryBlockPattern.start()
                            .aisle("XXX", "CCC", "XXX")
                            .aisle("XXX", "C#C", "XXX")
                            .aisle("XSX", "CCC", "XXX")
                            .where('S', controller(blocks(definition.getBlock())))
                            .where('X', blocks(ProcessingArrayMachine.getCasingState(tier))
                                    .setMinGlobalLimited(6)
                                    .or(abilities(IMPORT_ITEMS))
                                    .or(abilities(EXPORT_ITEMS))
                                    .or(abilities(IMPORT_FLUIDS))
                                    .or(abilities(EXPORT_FLUIDS))
                                    .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(1))
                                    .or(abilities(INPUT_LASER).setMaxGlobalLimited(1))
                                    .or(blocks(GTOMachines.MACHINE_ACCESS_INTERFACE.getBlock()).setExactLimit(1))
                                    .or(abilities(MAINTENANCE).setExactLimit(1)))
                            .where('C', GTOPredicates.glass())
                            .where('#', air())
                            .build())
                    .renderer(() -> new ArrayMachineRenderer(tier == IV ? GTCEu.id("block/casings/solid/machine_casing_robust_tungstensteel") : GTCEu.id("block/casings/solid/machine_casing_sturdy_hsse"), GTCEu.id("block/multiblock/gcym/large_assembler")))
                    .register(),
            IV, LuV);

    public final static MultiblockMachineDefinition SINTERING_FURNACE = multiblock("sintering_furnace", "烧结炉", CoilMultiblockMachine.createCoilMachine(false, true))
            .nonYAxisRotation()
            .parallelizableTooltips()
            .recipe(GTORecipeTypes.SINTERING_FURNACE_RECIPES)
            .parallelizableOverclock()
            .block(GTBlocks.CASING_STAINLESS_CLEAN)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("     ", " AAA ", " AHA ", " AAA ", "     ")
                    .aisle(" CCC ", "CDDDC", "CD DC", "CDDDC", " CCC ")
                    .aisle(" CEC ", "CDFDC", "GF FG", "CDFDC", " CEC ")
                    .aisle(" CEC ", "CDFDC", "GF FG", "CDFDC", " CEC ")
                    .aisle(" CEC ", "CDFDC", "GF FG", "CDFDC", " CEC ")
                    .aisle(" CCC ", "CDDDC", "CD DC", "CDDDC", " CCC ")
                    .aisle("     ", " AAA ", " ABA ", " AAA ", "     ")
                    .where('A', blocks(GTBlocks.CASING_STAINLESS_CLEAN.get())
                            .or(autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(PARALLEL_HATCH).setMaxGlobalLimited(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('B', controller(blocks(definition.get())))
                    .where('C', blocks(GTBlocks.CASING_TUNGSTENSTEEL_ROBUST.get()))
                    .where('D', blocks(GTBlocks.CASING_STAINLESS_CLEAN.get()))
                    .where('E', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.StainlessSteel)))
                    .where('F', heatingCoils())
                    .where('G', blocks(GTBlocks.CASING_TEMPERED_GLASS.get()))
                    .where('H', abilities(MUFFLER))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"), GTCEu.id("block/multiblock/electric_blast_furnace"))
            .register();

    public final static MultiblockMachineDefinition ISOSTATIC_PRESS = multiblock("isostatic_press", "等静压成型", ElectricMultiblockMachine::new)
            .nonYAxisRotation()
            .parallelizableTooltips()
            .recipe(GTORecipeTypes.ISOSTATIC_PRESSING_RECIPES)
            .parallelizableOverclock()
            .block(GTBlocks.CASING_TITANIUM_STABLE)
            .pattern(definition -> FactoryBlockPattern.start()
                    .aisle("AAAAA", "A   A", "A   A", "A   A", "BBBBB", "CCCCC", "     ", "     ")
                    .aisle("ABBBA", " DDD ", " DDD ", " DDD ", "BDDDB", "C   C", "     ", " BBB ")
                    .aisle("ABBBA", " DDD ", " D D ", " D D ", "BDFDB", "B F B", "B F B", "BBFBB")
                    .aisle("ABBBA", " DDD ", " DDD ", " DDD ", "BDDDB", "C   C", "     ", " BBB ")
                    .aisle("AAEAA", "A   A", "A   A", "A   A", "BBBBB", "CCCCC", "     ", "     ")
                    .where('A', blocks(GTBlocks.CASING_TITANIUM_STABLE.get())
                            .or(autoAbilities(definition.getRecipeTypes()))
                            .or(abilities(PARALLEL_HATCH).setMaxGlobalLimited(1))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('B', blocks(GTBlocks.CASING_TITANIUM_STABLE.get()))
                    .where('C', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.BlueSteel)))
                    .where('D', blocks(GTOBlocks.COMPRESSOR_CONTROLLER_CASING.get()))
                    .where('E', controller(blocks(definition.get())))
                    .where('F', blocks(GTOBlocks.COMPRESSOR_PIPE_CASING.get()))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_stable_titanium"), GTCEu.id("block/multiblock/gcym/large_material_press"))
            .register();
}
