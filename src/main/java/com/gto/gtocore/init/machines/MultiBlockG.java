package com.gto.gtocore.init.machines;

import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.api.machine.multiblock.CoilMultiblockMachine;
import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.api.machine.part.GTOPartAbility;
import com.gto.gtocore.api.pattern.GTOPredicates;
import com.gto.gtocore.client.renderer.machine.ArrayMachineRenderer;
import com.gto.gtocore.common.machine.multiblock.electric.ProcessingArrayMachine;
import com.gto.gtocore.common.machine.multiblock.noenergy.DroneControlCenterMachine;
import com.gto.gtocore.common.machine.multiblock.storage.WirelessEnergySubstationMachine;
import com.gto.gtocore.init.GTOBlocks;
import com.gto.gtocore.init.GTOMachines;
import com.gto.gtocore.init.GTORecipeTypes;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.phys.shapes.Shapes;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.DUMMY_RECIPES;
import static com.gto.gtocore.utils.register.MachineRegisterUtils.multiblock;
import static com.gto.gtocore.utils.register.MachineRegisterUtils.registerTieredMultis;

public interface MultiBlockG {

    static void init() {}

    MultiblockMachineDefinition DRONE_CONTROL_CENTER = multiblock("drone_control_center", "无人机控制中心", DroneControlCenterMachine::new)
            .nonYAxisRotation()
            .recipe(DUMMY_RECIPES)
            .block(GTBlocks.CASING_STAINLESS_CLEAN)
            .pattern(definition -> FactoryBlockPattern.start(RelativeDirection.RIGHT, RelativeDirection.UP, RelativeDirection.BACK)
                    .aisle("ABAAA  ", "ADA A  ", "AAA A  ", "AABAA  ", "A A A  ", "A A A  ", "AAAAA  ")
                    .aisle("AAAAAAA", "A ACAAA", "A ACAAA", "AAAAAAA", "ACACAAA", "ACACA  ", "AAAAA  ")
                    .aisle("AAAAAAB", "A A A A", "A A A A", "AAAAA A", "A A A A", "A A A  ", "AAAAA  ")
                    .aisle("AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAA  ", "AAAAA  ")
                    .where('A', blocks(GTBlocks.CASING_STAINLESS_CLEAN.get()))
                    .where('B', blocks(GTBlocks.CASING_STAINLESS_CLEAN.get())
                            .or(abilities(EXPORT_ITEMS).setMaxGlobalLimited(1))
                            .or(abilities(GTOPartAbility.DRONE_HATCH).setExactLimit(1)))
                    .where('C', blocks(GTBlocks.CASING_STAINLESS_STEEL_GEARBOX.get()))
                    .where('D', controller(blocks(definition.get())))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_clean_stainless_steel"), GTCEu.id("block/multiblock/fusion_reactor"))
            .register();

    MultiblockMachineDefinition WIRELESS_ENERGY_SUBSTATION = multiblock("wireless_energy_substation", "无线能源塔", WirelessEnergySubstationMachine::new)
            .nonYAxisRotation()
            .recipe(DUMMY_RECIPES)
            .tooltipsText("Provides capacity support to the wireless grid", "为无线电网提供容量支持")
            .tooltipsText("You can install any wireless energy unit inside to increase the capacity limit", "可在内部安装任意无线能量单元来提高容量上限")
            .tooltipsText("The actual working units are limited by the glass tier", "实际起作用的单元受玻璃等级限制")
            .tooltipsText("Total capacity is the sum of unit capacities x number of units / 2, total loss is the average loss of units", "总容量为单元容量之和x单元数的一半，总损耗为单元损耗平均值")
            .block(GTBlocks.CASING_STEEL_SOLID)
            .pattern(definition -> FactoryBlockPattern.start(RelativeDirection.RIGHT, RelativeDirection.FRONT, RelativeDirection.UP)
                    .aisle("AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAABAAA")
                    .aisle("ACCCCCA", "CDDDDDC", "CDDDDDC", "CDDDDDC", "CDDDDDC", "CDDDDDC", "ACCCCCA").setRepeatable(2, 30)
                    .aisle("AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA")
                    .where('A', blocks(GTBlocks.CASING_STEEL_SOLID.get()))
                    .where('B', controller(blocks(definition.get())))
                    .where('C', GTOPredicates.glass())
                    .where('D', air().or(GTOPredicates.wirelessEnergyUnit()))
                    .build())
            .workableCasingRenderer(GTCEu.id("block/casings/solid/machine_casing_solid_steel"), GTCEu.id("block/multiblock/power_substation"))
            .register();

    MultiblockMachineDefinition[] PROCESSING_ARRAY = registerTieredMultis("processing_array", t -> GTOValues.VNFR[t] + "处理阵列",
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

    MultiblockMachineDefinition SINTERING_FURNACE = multiblock("sintering_furnace", "烧结炉", CoilMultiblockMachine.createCoilMachine(false, true))
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

    MultiblockMachineDefinition ISOSTATIC_PRESS = multiblock("isostatic_press", "等静压成型", ElectricMultiblockMachine::new)
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
