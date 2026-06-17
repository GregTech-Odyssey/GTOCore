package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES;

final class DimensionallytranscendentPlasmaForge {

    public static void init() {
        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("spacetime_ingot")
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asItem(), 64)
                .notConsumable(GTOBlocks.SPACETIME_BENDING_CORE.asItem(), 64)
                .inputFluids(GTOMaterials.SpaceTime, 1000)
                .inputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(TagPrefix.ingot, GTOMaterials.SpaceTime, 8)
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue, 100)
                .EUt(2013265920)
                .duration(6400)
                .blastFurnaceTemp(62000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("hypercube_1")
                .inputItems(TagPrefix.rod, GTOMaterials.TranscendentMetal, 16)
                .inputItems(GTOItems.QUANTUM_ANOMALY)
                .inputFluids(GTOMaterials.ExcitedDtec, 1000)
                .inputFluids(GTOMaterials.SpatialFluid, 1000)
                .outputItems(GTOItems.HYPERCUBE, 64)
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue, 100)
                .EUt(32212254720L)
                .duration(6400)
                .blastFurnaceTemp(62000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("hypercube")
                .inputItems(TagPrefix.rod, GTOMaterials.CosmicNeutronium, 12)
                .inputItems(TagPrefix.rod, GTOMaterials.CelestialTungsten, 24)
                .inputFluids(GTOMaterials.ExcitedDtec, 1000)
                .outputItems(GTOItems.HYPERCUBE)
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue, 100)
                .EUt(2013265920)
                .duration(2400)
                .blastFurnaceTemp(30000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("rhugnor")
                .notConsumable(GTOItems.INFINITY_CATALYST.get())
                .inputItems(GTItems.ENERGIUM_CRYSTAL, 64)
                .inputFluids(GTOMaterials.Infinity, 10000)
                .inputFluids(GTOMaterials.QuantumMetal, 10000)
                .outputFluids(GTOMaterials.Rhugnor, 10000)
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue, 100)
                .EUt(8053063680L)
                .duration(3600)
                .blastFurnaceTemp(36000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("crystal_matrix_plasma")
                .notConsumable(GTOItems.INFINITY_CATALYST.get())
                .inputFluids(GTMaterials.UUMatter, 1000000)
                .inputFluids(GTOMaterials.FreeProtonGas, 20000)
                .outputFluids(GTOMaterials.CrystalMatrix.getFluid(FluidStorageKeys.PLASMA, 10000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue, 100)
                .EUt(2013265920)
                .duration(800)
                .blastFurnaceTemp(28000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("dense_neutron_plasma")
                .notConsumable(GTOItems.INFINITY_CATALYST.get())
                .inputItems(GTOItems.NEUTRON_PLASMA_CONTAINMENT_CELL)
                .inputFluids(GTOMaterials.HeavyQuarkDegenerateMatter.getFluid(FluidStorageKeys.PLASMA, 10000))
                .inputFluids(GTOMaterials.Periodicium, 1000)
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL)
                .outputFluids(GTOMaterials.DenseNeutron.getFluid(FluidStorageKeys.PLASMA, 10000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue, 100)
                .EUt(2013265920)
                .duration(800)
                .blastFurnaceTemp(26000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("create_casing")
                .inputItems(TagPrefix.frameGt, GTOMaterials.Eternity)
                .inputItems(GTOItems.COMMAND_BLOCK_CORE)
                .inputFluids(GTOMaterials.ExcitedDtsc, 1000)
                .inputFluids(GTOMaterials.PrimordialMatter, 1000)
                .outputItems(GTOBlocks.CREATE_CASING.asItem())
                .EUt(32985348833280L)
                .duration(3200)
                .blastFurnaceTemp(96000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("infinity_catalyst")
                .inputItems(GTOItems.CONTAINED_EXOTIC_MATTER, 64)
                .inputItems(GTOItems.ESSENTIA_MATTER, 64)
                .inputFluids(GTOMaterials.Infinity, 1000)
                .inputFluids(GTOMaterials.HighEnergyQuarkGluon.getFluid(FluidStorageKeys.PLASMA, 100000))
                .outputItems(GTOItems.INFINITY_CATALYST.get())
                .outputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT, 64)
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue, 100)
                .EUt(2013265920)
                .duration(4800)
                .blastFurnaceTemp(32000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("chaos_plasma")
                .inputItems(GTOItems.CHAOS_SHARD)
                .inputFluids(GTOMaterials.DimensionallyTranscendentResplendentCatalyst, 1000)
                .inputFluids(GTOMaterials.CosmicMesh.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputFluids(GTOMaterials.Chaos.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue, 100)
                .EUt(2013265920)
                .duration(1600)
                .blastFurnaceTemp(32000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("spacetime")
                .notConsumable(GTOItems.SPACETIME_CATALYST.asItem())
                .inputFluids(GTOMaterials.Infinity, 100)
                .inputFluids(GTOMaterials.Hypogen, 100)
                .outputFluids(GTOMaterials.SpaceTime, 200)
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue, 100)
                .EUt(8053063680L)
                .duration(1600)
                .blastFurnaceTemp(36000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("cosmic_neutron_plasma_cell")
                .notConsumable(GTOItems.INFINITY_CATALYST.get())
                .inputItems(GTOItems.EXTREMELY_DURABLE_PLASMA_CELL, 5)
                .inputFluids(GTMaterials.UUMatter, 1000000)
                .inputFluids(GTOMaterials.DenseNeutron.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.COSMIC_NEUTRON_PLASMA_CELL, 5)
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue, 100)
                .EUt(2013265920)
                .duration(800)
                .blastFurnaceTemp(28000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("magnetohydrodynamically_constrained_star_matter")
                .notConsumable(GTOItems.ETERNITY_CATALYST.asItem())
                .inputItems(GTOTagPrefix.NANITES, GTOMaterials.Eternity)
                .inputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 100000))
                .inputFluids(GTOMaterials.ExcitedDtsc, 100000)
                .chancedOutput(GTOTagPrefix.CONTAMINABLE_NANITES, GTOMaterials.Eternity, 5000, 0)
                .outputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter, 100000)
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue, 100)
                .EUt(2061584302080L)
                .duration(6400)
                .blastFurnaceTemp(81000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("infinity")
                .notConsumable(GTOItems.SPACETIME_CATALYST.asItem())
                .inputFluids(GTOMaterials.CrystalMatrix.getFluid(FluidStorageKeys.PLASMA, 10000))
                .inputFluids(GTOMaterials.CosmicNeutronium, 5000)
                .outputFluids(GTOMaterials.Infinity, 1000)
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue, 100)
                .EUt(8053063680L)
                .duration(1600)
                .blastFurnaceTemp(32000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("dimensionallytranscendentresidue")
                .inputFluids(GTOMaterials.DimensionallyTranscendentCrudeCatalyst, 100)
                .inputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 100))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue, 100)
                .EUt(2013265920)
                .duration(400)
                .blastFurnaceTemp(36000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("hypogen")
                .notConsumable(GTOItems.INFINITY_CATALYST.get())
                .inputItems(TagPrefix.block, GTOMaterials.QuantumChromoDynamicallyConfinedMatter)
                .inputFluids(GTOMaterials.Rhugnor, 10000)
                .inputFluids(GTOMaterials.DragonBlood, 10000)
                .outputFluids(GTOMaterials.Hypogen, 10000)
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue, 100)
                .EUt(2013265920)
                .duration(1200)
                .blastFurnaceTemp(26000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("transcendent_metal")
                .notConsumable(GTOItems.SPACETIME_CATALYST.asItem())
                .inputItems(GTOItems.HYPERCUBE)
                .inputFluids(GTOMaterials.SpaceTime, 100)
                .inputFluids(GTMaterials.Tennessine, 144)
                .outputFluids(GTOMaterials.TranscendentMetal, 144)
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue, 100)
                .EUt(2013265920)
                .duration(3200)
                .blastFurnaceTemp(36000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("infinity_glass")
                .inputItems(GTOItems.BLACK_BODY_NAQUADRIA_SUPERSOLID)
                .inputItems(TagPrefix.dust, GTOMaterials.Infinity, 2)
                .inputFluids(GTOMaterials.DimensionallyTranscendentExoticCatalyst, 1000)
                .inputFluids(GTOMaterials.WoodsGlass, 9216)
                .outputItems(GTOBlocks.INFINITY_GLASS.asItem())
                .EUt(8246337208320L)
                .duration(1600)
                .blastFurnaceTemp(88000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("chaos_infinity_glass")
                .inputItems(GTOBlocks.INFINITY_GLASS.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.ChaosInfinityAlloy, 4)
                .inputFluids(GTOMaterials.DimensionallyTranscendentStellarCatalyst, 1000)
                .inputFluids(GTOMaterials.ElfGlass, 9216)
                .outputItems(GTOBlocks.CHAOS_INFINITY_GLASS.asItem())
                .EUt(32985348833280L)
                .duration(1600)
                .blastFurnaceTemp(92000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("eternity_glass")
                .inputItems(GTOBlocks.CHAOS_INFINITY_GLASS.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.Eternity, 8)
                .inputFluids(GTOMaterials.ExcitedDtsc, 1000)
                .inputFluids(GTOMaterials.ChromaticGlass, 9216)
                .outputItems(GTOBlocks.ETERNITY_GLASS.asItem())
                .EUt(131941395333120L)
                .duration(1600)
                .blastFurnaceTemp(96000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder("eternity")
                .notConsumable(GTOItems.ETERNITY_CATALYST.asItem())
                .inputItems(GTOItems.INFINITY_SINGULARITY.get())
                .inputFluids(GTOMaterials.PrimordialMatter, 1000)
                .inputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 9000))
                .outputFluids(GTOMaterials.Eternity, 10000)
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue, 100)
                .EUt(32212254720L)
                .duration(4800)
                .blastFurnaceTemp(56000)
                .save();
    }
}
