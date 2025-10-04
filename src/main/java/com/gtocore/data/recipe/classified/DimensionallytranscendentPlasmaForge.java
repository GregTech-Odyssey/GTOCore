package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import committee.nova.mods.avaritia.init.registry.ModBlocks;
import committee.nova.mods.avaritia.init.registry.ModItems;

import static com.gtocore.common.data.GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES;

final class DimensionallytranscendentPlasmaForge {

    public static void init() {
        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("spacetime_ingot")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem(), 64)
                .notConsumable(GTOBlocks.SPACETIMEBENDINGCORE.asItem(), 64)
                .inputFluids(GTOMaterials.SpaceTime.getFluid(1000))
                .inputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(TagPrefix.ingot, GTOMaterials.SpaceTime, 8)
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(6400)
                .blastFurnaceTemp(62000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("hypercube_1")
                .inputItems(TagPrefix.rod, GTOMaterials.TranscendentMetal, 16)
                .inputItems(GTOItems.QUANTUM_ANOMALY.asItem())
                .inputFluids(GTOMaterials.ExcitedDtec.getFluid(1000))
                .inputFluids(GTOMaterials.SpatialFluid.getFluid(1000))
                .outputItems(GTOItems.HYPERCUBE.asItem(), 64)
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(32212254720L)
                .duration(6400)
                .blastFurnaceTemp(62000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("hypercube")
                .inputItems(TagPrefix.rod, GTOMaterials.CosmicNeutronium, 12)
                .inputItems(TagPrefix.rod, GTOMaterials.CelestialTungsten, 24)
                .inputFluids(GTOMaterials.ExcitedDtec.getFluid(1000))
                .outputItems(GTOItems.HYPERCUBE.asItem())
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(2400)
                .blastFurnaceTemp(30000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("rhugnor")
                .notConsumable(ModItems.infinity_catalyst.get())
                .inputItems(GTItems.ENERGIUM_CRYSTAL.asItem(), 64)
                .inputFluids(GTOMaterials.Infinity.getFluid(10000))
                .inputFluids(GTOMaterials.QuantumMetal.getFluid(10000))
                .outputFluids(GTOMaterials.Rhugnor.getFluid(10000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(8053063680L)
                .duration(3600)
                .blastFurnaceTemp(36000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("crystal_matrix_plasma")
                .notConsumable(ModItems.infinity_catalyst.get())
                .inputItems(ModBlocks.crystal_matrix.get().asItem())
                .inputFluids(GTMaterials.UUMatter.getFluid(1000000))
                .inputFluids(GTOMaterials.FreeProtonGas.getFluid(20000))
                .outputFluids(GTOMaterials.CrystalMatrix.getFluid(FluidStorageKeys.PLASMA, 10000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(800)
                .blastFurnaceTemp(28000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("dense_neutron_plasma")
                .notConsumable(ModItems.infinity_catalyst.get())
                .inputItems(GTOItems.NEUTRON_PLASMA_CONTAINMENT_CELL.asItem())
                .inputFluids(GTOMaterials.HeavyQuarkDegenerateMatter.getFluid(FluidStorageKeys.PLASMA, 10000))
                .inputFluids(GTOMaterials.Periodicium.getFluid(1000))
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asItem())
                .outputFluids(GTOMaterials.DenseNeutron.getFluid(FluidStorageKeys.PLASMA, 10000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(800)
                .blastFurnaceTemp(26000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("create_casing")
                .inputItems(TagPrefix.frameGt, GTOMaterials.Eternity)
                .inputItems(GTOItems.COMMAND_BLOCK_CORE.asItem())
                .inputFluids(GTOMaterials.ExcitedDtsc.getFluid(1000))
                .inputFluids(GTOMaterials.PrimordialMatter.getFluid(1000))
                .outputItems(GTOBlocks.CREATE_CASING.asItem())
                .EUt(32985348833280L)
                .duration(3200)
                .blastFurnaceTemp(96000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("infinity_catalyst")
                .inputItems(GTOItems.CONTAINED_EXOTIC_MATTER.asItem(), 64)
                .inputItems(GTOItems.ESSENTIA_MATTER.asItem(), 64)
                .inputFluids(GTOMaterials.Infinity.getFluid(1000))
                .inputFluids(GTOMaterials.HighEnergyQuarkGluon.getFluid(FluidStorageKeys.PLASMA, 100000))
                .outputItems(ModItems.infinity_catalyst.get())
                .outputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asItem(), 64)
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(4800)
                .blastFurnaceTemp(32000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("chaos_plasma")
                .inputItems(GTOItems.CHAOS_SHARD.asItem())
                .inputFluids(GTOMaterials.DimensionallyTranscendentResplendentCatalyst.getFluid(1000))
                .inputFluids(GTOMaterials.CosmicMesh.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputFluids(GTOMaterials.Chaos.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(1600)
                .blastFurnaceTemp(32000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("spacetime")
                .notConsumable(GTOItems.SPACETIME_CATALYST.asItem())
                .inputFluids(GTOMaterials.Infinity.getFluid(100))
                .inputFluids(GTOMaterials.Hypogen.getFluid(100))
                .outputFluids(GTOMaterials.SpaceTime.getFluid(200))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(8053063680L)
                .duration(1600)
                .blastFurnaceTemp(36000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("cosmic_neutron_plasma_cell")
                .notConsumable(ModItems.infinity_catalyst.get())
                .inputItems(GTOItems.EXTREMELY_DURABLE_PLASMA_CELL.asItem(), 5)
                .inputFluids(GTMaterials.UUMatter.getFluid(1000000))
                .inputFluids(GTOMaterials.DenseNeutron.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.COSMIC_NEUTRON_PLASMA_CELL.asItem(), 5)
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(800)
                .blastFurnaceTemp(28000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("magnetohydrodynamically_constrained_star_matter")
                .notConsumable(GTOItems.ETERNITY_CATALYST.asItem())
                .inputItems(GTOTagPrefix.NANITES, GTOMaterials.Eternity)
                .inputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 100000))
                .inputFluids(GTOMaterials.ExcitedDtsc.getFluid(100000))
                .chancedOutput(GTOTagPrefix.CONTAMINABLE_NANITES, GTOMaterials.Eternity, 5000, 0)
                .outputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(100000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2061584302080L)
                .duration(6400)
                .blastFurnaceTemp(81000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("infinity")
                .notConsumable(GTOItems.SPACETIME_CATALYST.asItem())
                .inputFluids(GTOMaterials.CrystalMatrix.getFluid(FluidStorageKeys.PLASMA, 10000))
                .inputFluids(GTOMaterials.CosmicNeutronium.getFluid(5000))
                .outputFluids(GTOMaterials.Infinity.getFluid(1000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(8053063680L)
                .duration(1600)
                .blastFurnaceTemp(32000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("dimensionallytranscendentresidue")
                .inputFluids(GTOMaterials.DimensionallyTranscendentCrudeCatalyst.getFluid(100))
                .inputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 100))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(400)
                .blastFurnaceTemp(36000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("hypogen")
                .notConsumable(ModItems.infinity_catalyst.get())
                .inputItems(TagPrefix.block, GTOMaterials.QuantumChromoDynamicallyConfinedMatter)
                .inputFluids(GTOMaterials.Rhugnor.getFluid(10000))
                .inputFluids(GTOMaterials.DragonBlood.getFluid(10000))
                .outputFluids(GTOMaterials.Hypogen.getFluid(10000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(1200)
                .blastFurnaceTemp(26000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("transcendent_metal")
                .notConsumable(GTOItems.SPACETIME_CATALYST.asItem())
                .inputItems(GTOItems.HYPERCUBE.asItem())
                .inputFluids(GTOMaterials.SpaceTime.getFluid(100))
                .inputFluids(GTMaterials.Tennessine.getFluid(144))
                .outputFluids(GTOMaterials.TranscendentMetal.getFluid(144))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(3200)
                .blastFurnaceTemp(36000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("infinity_glass")
                .inputItems(GTOItems.BLACK_BODY_NAQUADRIA_SUPERSOLID.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.Infinity, 2)
                .inputFluids(GTOMaterials.DimensionallyTranscendentExoticCatalyst.getFluid(1000))
                .inputFluids(GTOMaterials.WoodsGlass.getFluid(9216))
                .outputItems(GTOBlocks.INFINITY_GLASS.asItem())
                .EUt(8246337208320L)
                .duration(1600)
                .blastFurnaceTemp(88000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("eternity")
                .notConsumable(GTOItems.ETERNITY_CATALYST.asItem())
                .inputItems(ModItems.eternal_singularity.get())
                .inputFluids(GTOMaterials.PrimordialMatter.getFluid(1000))
                .inputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 9000))
                .outputFluids(GTOMaterials.Eternity.getFluid(10000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(32212254720L)
                .duration(4800)
                .blastFurnaceTemp(56000)
                .save();
    }
}
