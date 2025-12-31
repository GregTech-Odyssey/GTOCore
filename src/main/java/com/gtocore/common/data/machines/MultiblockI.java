package com.gtocore.common.data.machines;

import com.gtocore.client.renderer.machine.MultiFluidRenderer;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOFluids;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.common.data.translation.GTOMachineStories;
import com.gtocore.common.data.translation.GTOMachineTooltipsA;
import com.gtocore.common.machine.multiblock.electric.LargeAlgaeFarm;
import com.gtocore.common.machine.multiblock.electric.PigmentMixer;

import com.gtolib.GTOCore;
import com.gtolib.api.recipe.modifier.RecipeModifierFunction;
import com.gtolib.utils.MultiBlockFileReader;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.common.data.GCYMBlocks;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.level.material.Fluids;

import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gtocore.api.pattern.GTOPredicates.glass;
import static com.gtocore.api.pattern.GTOPredicates.recordPosition;
import static com.gtocore.utils.register.MachineRegisterUtils.multiblock;

public class MultiblockI {

    public static void init() {}

    // 颜料混合机
    public static final MultiblockMachineDefinition PIGMENT_MIXER = multiblock("pigment_mixer", "染料混合机", PigmentMixer::new)
            .nonYAxisRotation()
            .parallelizableTooltips()
            .perfectOCTooltips()
            .tooltips(GTOMachineStories.INSTANCE.getPigmentMixerTooltips().getSupplier())
            .recipeTypes(GTORecipeTypes.PIGMENT_MIXING_RECIPES)
            .recipeModifier(RecipeModifierFunction.PERFECT_OVERCLOCKING)
            .block(GTOBlocks.PRESSURE_RESISTANT_HOUSING_MECHANICAL_BLOCK)
            .pattern(definition -> MultiBlockFileReader.start(definition)
                    .where('A', blocks(GTBlocks.CASING_PTFE_INERT.get()))
                    .where('B', blocks(GTOBlocks.STABLE_BASE_CASING.get()))
                    .where('C', blocks(GTOBlocks.CHEMICAL_CORROSION_RESISTANT_PIPE_CASING.get()))
                    .where('D', blocks(GTBlocks.CASING_TUNGSTENSTEEL_GEARBOX.get()))
                    .where('E', blocks(GCYMBlocks.CASING_CORROSION_PROOF.get()))
                    .where('F', blocks(GTOBlocks.PRESSURE_RESISTANT_HOUSING_MECHANICAL_BLOCK.get()))
                    .where('G', blocks(GTOBlocks.BOROSILICATE_GLASS.get()))
                    .where('H', blocks(GTOBlocks.COMPRESSOR_CONTROLLER_CASING.get()))
                    .where('I', blocks(GTBlocks.HIGH_POWER_CASING.get()))
                    .where('J', blocks(GTOBlocks.STAINLESS_STEEL_CORROSION_RESISTANT_CASING.get()))
                    .where('K', blocks(GTBlocks.CASING_POLYTETRAFLUOROETHYLENE_PIPE.get()))
                    .where('L', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTOMaterials.StructuralSteel45)))
                    .where('M', blocks(GTOBlocks.PRESSURE_RESISTANT_HOUSING_MECHANICAL_BLOCK.get())
                            .or(autoAbilities(definition.getRecipeTypes()))
                            .or(autoAbilities(true, false, true))
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(2)))
                    .where('N', blocks(GTOBlocks.IRIDIUM_CASING.get()))
                    .where('O', blocks(GTOBlocks.HASTELLOY_N_75_CASING.get()))
                    .where('P', blocks(GTOBlocks.HASTELLOY_N_75_GEARBOX.get()))
                    .where('Q', blocks(GTOBlocks.NAQUADAH_REINFORCED_PLANT_CASING.get()))
                    .where('R', controller(definition))
                    .where(' ', any())
                    .where('c', recordPosition("cyan", air()))
                    .where('m', recordPosition("magenta", air()))
                    .where('y', recordPosition("yellow", air()))
                    .where('k', recordPosition("black", air()))
                    .where('w', recordPosition("white", air()))
                    .build())
            .renderer(MultiFluidRenderer.create(GTOCore.id("block/casings/pressure_resistant_housing_mechanical_block"),
                    GTCEu.id("block/multiblock/gcym/large_chemical_bath")))
            .hasTESR(true)
            .register();

    // 大型藻类养殖中心
    public static final MultiblockMachineDefinition LARGE_ALGAE_FARM = multiblock("large_algae_farm", "大型藻类养殖中心", LargeAlgaeFarm::new)
            .nonYAxisRotation()
            .recipeTypes(GTORecipeTypes.DUMMY_RECIPES)
            .recipeModifier(RecipeModifierFunction.PERFECT_OVERCLOCKING)
            .tooltipsSupplier(GTOMachineStories.INSTANCE.getLargeAlgaeFarmTooltips().getSupplier())
            .tooltipsSupplier(GTOMachineTooltipsA.INSTANCE.getLargeAlgaeFarmTooltips().getSupplier())
            .block(GTOBlocks.STAINLESS_STEEL_CORROSION_RESISTANT_CASING)
            .pattern(definition -> MultiBlockFileReader.start(definition)
                    .where('A', blocks(GTOBlocks.IRIDIUM_CASING.get()))
                    .where('a', fluids(GTOFluids.VAPOR_OF_LEVITY.getSource()))
                    .where('B', blocks(GTOBlocks.STAINLESS_STEEL_CORROSION_RESISTANT_CASING.get())
                            .or(autoAbilities(definition.getRecipeTypes()))
                            .or(blocks(GTAEMachines.ALGAE_ACCESS_HATCH.get()).setExactLimit(1))
                            .or(abilities(INPUT_ENERGY).setMaxGlobalLimited(2))
                            .or(abilities(IMPORT_FLUIDS))
                            .or(abilities(MAINTENANCE).setExactLimit(1)))
                    .where('C', blocks(GTOBlocks.STAINLESS_STEEL_CORROSION_RESISTANT_CASING.get()))
                    .where('D', blocks(GTBlocks.CLEANROOM_GLASS.get()))
                    .where('E', controller(definition))
                    .where('F', blocks(GTBlocks.FILTER_CASING_STERILE.get()))
                    .where('G', blocks(GTOBlocks.PRESSURE_CONTAINMENT_CASING.get()))
                    .where('g', glass())
                    .where('H', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTOMaterials.Inconel242)))
                    .where('I', blocks(GTOBlocks.BIOLOGICAL_MECHANICAL_CASING.get()))
                    .where('J', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTOMaterials.StainlessSteel316)))
                    .where('K', blocks(GTOBlocks.IRIDIUM_PIPE_CASING.get()))
                    .where('L', blocks(GTOBlocks.CHEMICAL_CORROSION_RESISTANT_PIPE_CASING.get()))
                    .where('l', fluids(GTOFluids.NUTRIENT_DISTILLATION.getSource()))
                    .where('M', blocks(GTOBlocks.HIGH_PRESSURE_PIPE_CASING.get()))
                    .where('N', blocks(ChemicalHelper.getBlock(TagPrefix.frameGt, GTMaterials.BlueSteel)))
                    .where('w', fluids(Fluids.WATER.getSource()))
                    .where(' ', any())
                    .build())
            .renderer(MultiFluidRenderer.create(GTOCore.id("block/casings/stainless_steel_corrosion_resistant_casing"),
                    GTCEu.id("block/multiblock/gcym/large_chemical_bath")))
            .register();
}
