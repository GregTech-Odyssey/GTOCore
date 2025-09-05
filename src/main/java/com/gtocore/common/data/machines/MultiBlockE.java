package com.gtocore.common.data.machines;

import com.gtocore.api.pattern.GTOPredicates;
import com.gtocore.client.renderer.machine.DimensionalFocusEngravingArrayRenderer;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.common.data.translation.GTOMachineStories;

import com.gtolib.GTOCore;
import com.gtolib.api.machine.multiblock.CoilCrossRecipeMultiblockMachine;
import com.gtolib.utils.MultiBlockFileReader;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.pattern.util.RelativeDirection;
import com.gregtechceu.gtceu.common.data.GCYMBlocks;
import com.gregtechceu.gtceu.common.data.GTBlocks;

import static com.gregtechceu.gtceu.api.machine.multiblock.PartAbility.*;
import static com.gregtechceu.gtceu.api.pattern.Predicates.*;
import static com.gtocore.utils.register.MachineRegisterUtils.multiblock;

public final class MultiBlockE {

    public static void init() {}

    public static final MultiblockMachineDefinition DIMENSIONAL_FOCUS_ENGRAVING_ARRAY = multiblock("dimensional_focus_engraving_array", "维度聚焦激光蚀刻阵列", CoilCrossRecipeMultiblockMachine::createCoilParallel)
            .nonYAxisRotation()
            .recipeTypes(GTORecipeTypes.DIMENSIONAL_FOCUS_ENGRAVING_ARRAY_RECIPES)
            .tooltips(GTOMachineStories.INSTANCE.getDimensionalFocusEngravingArrayTooltips().getSupplier())
            .coilParallelTooltips()
            .laserTooltips()
            .multipleRecipesTooltips()
            .block(GCYMBlocks.CASING_LASER_SAFE_ENGRAVING)
            .pattern(definition -> MultiBlockFileReader.start(definition, RelativeDirection.BACK, RelativeDirection.UP, RelativeDirection.RIGHT)
                    .where('I', controller(blocks(definition.get())))
                    .where('A', blocks(GTOBlocks.DIMENSIONALLY_TRANSCENDENT_CASING.get()))
                    .where('B', blocks(GTBlocks.FUSION_GLASS.get()))
                    .where('C', blocks(GTOBlocks.DIMENSION_INJECTION_CASING.get()))
                    .where('D', blocks(GTOBlocks.MOLECULAR_COIL.get()))
                    .where('E', blocks(GTOBlocks.CONTAINMENT_FIELD_GENERATOR.get()))
                    .where('F', blocks(GTOBlocks.IMPROVED_SUPERCONDUCTOR_COIL.get()))
                    .where('G', blocks(GCYMBlocks.CASING_LASER_SAFE_ENGRAVING.get()))
                    .where('a', blocks(GCYMBlocks.CASING_LASER_SAFE_ENGRAVING.get())
                            .or(GTOPredicates.autoThreadLaserAbilities(definition.getRecipeTypes()))
                            .or(abilities(MAINTENANCE).setExactLimit(1))
                            .or(abilities(OPTICAL_DATA_RECEPTION).setExactLimit(1))
                            .or(abilities(COMPUTATION_DATA_RECEPTION).setExactLimit(1)))
                    .where('H', heatingCoils())
                    .where(' ', any())
                    .build())
            .renderer(DimensionalFocusEngravingArrayRenderer::new)
            .hasTESR(true)
            .register();

    public static final MultiblockMachineDefinition STAR_ULTIMATE_MATERIAL_FORGE_FACTORY = multiblock("star_ultimate_material_forge_factory", "恒星终极物质锻造工厂", CoilCrossRecipeMultiblockMachine.createParallel(false, false, m -> Integer.MAX_VALUE))
            .allRotation()
            .recipeTypes(GTORecipeTypes.ULTIMATE_MATERIAL_FORGE_RECIPES)
            .tooltips(GTOMachineStories.INSTANCE.getStarUltimateMaterialForgeFactoryTooltips().getSupplier())
            .laserTooltips()
            .multipleRecipesTooltips()
            .block(GTOBlocks.MOLECULAR_CASING)
            .pattern(definition -> MultiBlockFileReader.start(definition)
                    .where('~', controller(blocks(definition.get())))
                    .where('A', blocks(GTOBlocks.MOLECULAR_CASING.get()))
                    .where('I', blocks(GTOBlocks.MOLECULAR_CASING.get())
                            .or(GTOPredicates.autoThreadLaserAbilities(definition.getRecipeTypes())))
                    .where('B', blocks(GTOBlocks.MOLECULAR_COIL.get()))
                    .where('C', blocks(GTOBlocks.CONTAINMENT_FIELD_GENERATOR.get()))
                    .where('D', blocks(GTBlocks.HIGH_POWER_CASING.get()))
                    .where('E', blocks(GTOBlocks.HOLLOW_CASING.get()))
                    .where('F', blocks(GTOBlocks.FORCE_FIELD_GLASS.get()))
                    .where('G', blocks(GTOBlocks.ULTIMATE_STELLAR_CONTAINMENT_CASING.get()))
                    .where(' ', any())
                    .build())
            .workableCasingRenderer(GTOCore.id("block/casings/molecular_casing"), GTCEu.id("block/multiblock/fusion_reactor"))
            .register();
}
