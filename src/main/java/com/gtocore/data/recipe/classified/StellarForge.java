package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.GTOValues;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import static com.gtocore.common.data.GTORecipeTypes.STELLAR_FORGE_RECIPES;

final class StellarForge {

    public static void init() {
        STELLAR_FORGE_RECIPES.recipeBuilder("compressed_stone")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(TagPrefix.dust, GTMaterials.Stone, 1024)
                .outputItems(TagPrefix.dust, GTOMaterials.CompressedStone)
                .chancedOutput(TagPrefix.nugget, GTOMaterials.Bedrockium, 1, 100, 1)
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("neutron")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(TagPrefix.block, GTOMaterials.SuperheavyMix)
                .outputFluids(GTOMaterials.Neutron, 100)
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("contained_reissner_nordstrom_singularity")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT, 64)
                .inputItems(GTOItems.CHARGED_TRIPLET_NEUTRONIUM_SPHERE, 64)
                .outputItems(GTOItems.CONTAINED_REISSNER_NORDSTROM_SINGULARITY, 64)
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("closed_timelike_curvecomputational_unit")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(GTOItems.EIGENFOLDED_KERR_MANIFOLD)
                .inputItems(GTOItems.CLOSED_TIMELIKE_CURVE_COMPUTATIONAL_UNIT_CONTAINER)
                .outputItems(GTOItems.CLOSED_TIMELIKE_CURVE_COMPUTATIONAL_UNIT)
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("cosmic_mesh_plasma")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(GTOItems.HIGHLY_DENSE_POLYMER_PLATE)
                .outputFluids(GTOMaterials.CosmicMesh.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("legendarium_plasma")
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asItem())
                .inputItems(GTOItems.NEUTRON_PLASMA_CONTAINMENT_CELL)
                .inputFluids(GTOMaterials.Lemurite, 576)
                .inputFluids(GTOMaterials.Alduorite, 576)
                .inputFluids(GTOMaterials.Kalendrite, 576)
                .inputFluids(GTOMaterials.Haderoth, 576)
                .inputFluids(GTOMaterials.Ignatius, 576)
                .inputFluids(GTOMaterials.Ceruclase, 576)
                .inputFluids(GTOMaterials.Sanguinite, 576)
                .inputFluids(GTOMaterials.Quicksilver, 576)
                .inputFluids(GTOMaterials.Celenegil, 576)
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL)
                .outputFluids(GTOMaterials.Legendarium.getFluid(FluidStorageKeys.PLASMA, 2304))
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("degenerate_rhenium_plasma")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(TagPrefix.plateDouble, GTMaterials.Rhenium, 5)
                .outputFluids(GTOMaterials.DegenerateRhenium.getFluid(FluidStorageKeys.PLASMA, 10000))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("free_proton_gas")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(GTOItems.CONTAINED_HIGH_DENSITY_PROTONIC_MATTER)
                .outputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT)
                .outputFluids(GTOMaterials.FreeProtonGas, 10000)
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("recursively_folded_negative_space")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(GTOItems.MACROWORMHOLE_GENERATOR, 2)
                .inputItems(GTOItems.TEMPORAL_MATTER, 2)
                .outputItems(GTOItems.RECURSIVELY_FOLDED_NEGATIVE_SPACE)
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("dense_neutron_plasma")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(TagPrefix.block, GTOMaterials.Neutron, 5)
                .inputItems(TagPrefix.block, GTOMaterials.HeavyQuarkDegenerateMatter, 5)
                .inputFluids(GTOMaterials.Periodicium, 2736)
                .inputFluids(GTOMaterials.Gluons, 6000)
                .inputFluids(GTOMaterials.HeavyLeptonMixture, 6000)
                .outputFluids(GTOMaterials.DenseNeutron.getFluid(FluidStorageKeys.PLASMA, 6000))
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("quantum_chromo_dynamically_confined_matter_plasma")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(GTOItems.QUANTUMCHROMODYNAMIC_PROTECTIVE_PLATING, 20)
                .outputFluids(GTOMaterials.QuantumChromoDynamicallyConfinedMatter.getFluid(FluidStorageKeys.PLASMA, 2000))
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("chaos_shard")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(GTOBlocks.INFUSED_OBSIDIAN.asItem())
                .inputItems(Blocks.BEDROCK.asItem())
                .inputFluids(GTOMaterials.Radox, 1000)
                .outputItems(GTOItems.CHAOS_SHARD)
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("contained_kerr_newmann_singularity")
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asItem())
                .inputItems(GTOItems.CONTAINED_REISSNER_NORDSTROM_SINGULARITY, 64)
                .outputItems(GTOItems.CONTAINED_KERR_NEWMANN_SINGULARITY)
                .outputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT, 63)
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("adamantium_plasma")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.Bloodstone, 24)
                .inputFluids(GTOMaterials.Orichalcum, 576)
                .inputFluids(GTMaterials.Tin, 1024)
                .inputFluids(GTMaterials.Antimony, 864)
                .inputFluids(GTMaterials.Iron, 1152)
                .inputFluids(GTMaterials.Mercury, 1000)
                .outputFluids(GTOMaterials.Adamantium.getFluid(FluidStorageKeys.PLASMA, 2304))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("contained_exotic_matter")
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asItem())
                .inputItems(GTOItems.CONTAINED_HIGH_DENSITY_PROTONIC_MATTER)
                .inputItems(TagPrefix.dustTiny, GTOMaterials.DegenerateRhenium, 9)
                .outputItems(GTOItems.CONTAINED_EXOTIC_MATTER)
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("celestial_tungsten_plasma")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputFluids(GTOMaterials.Tartarite, 576)
                .inputFluids(GTMaterials.Tungsten, 576)
                .inputFluids(GTMaterials.Americium, 288)
                .inputFluids(GTOMaterials.TitanPrecisionSteel, 144)
                .inputFluids(GTOMaterials.AstralTitanium, 144)
                .inputFluids(GTMaterials.Xenon, 1000)
                .outputFluids(GTOMaterials.CelestialTungsten.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("neutron_plasma_containment_cell")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(GTOItems.PLASMA_CONTAINMENT_CELL)
                .inputFluids(GTOMaterials.Neutron, 1000)
                .inputFluids(GTOMaterials.HeavyLeptonMixture, 1000)
                .outputItems(GTOItems.NEUTRON_PLASMA_CONTAINMENT_CELL)
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("starmetal_plasma")
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asItem())
                .inputItems(GTOItems.RESONATING_GEM, 10)
                .inputFluids(GTOMaterials.FreeProtonGas, 1000)
                .inputFluids(GTOMaterials.FreeElectronGas, 1000)
                .outputFluids(GTOMaterials.Starmetal.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("exciteddtec")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(GTOItems.PRESCIENT_CRYSTAL)
                .inputFluids(GTOMaterials.DimensionallyTranscendentExoticCatalyst, 10000)
                .outputFluids(GTOMaterials.ExcitedDtec, 10000)
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("eternity_dust")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem(), 4)
                .inputItems(GTOItems.INFINITY_SINGULARITY.get())
                .inputFluids(GTOMaterials.PrimordialMatter, 1000)
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.Eternity)
                .outputFluids(GTOMaterials.TemporalFluid, 1000)
                .EUt(2013265920)
                .duration(800)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("extremely_durable_plasma_cell")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(GTOItems.DENSE_NEUTRON_PLASMA_CELL, 2)
                .outputItems(GTOItems.COSMIC_NEUTRON_PLASMA_CELL)
                .outputItems(GTOItems.EXTREMELY_DURABLE_PLASMA_CELL)
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("infinity_ingot")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(GTOItems.VIBRANT_CRYSTAL)
                .inputFluids(GTOMaterials.CrystalMatrix, 2000)
                .inputFluids(GTOMaterials.CosmicNeutronium, 1000)
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Infinity)
                .outputFluids(GTOMaterials.Infinity, 10)
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("heavy_quark_degenerate_matter_plasma")
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asItem())
                .inputFluids(GTOMaterials.HeavyQuarkEnrichedMixture, 1152)
                .inputFluids(GTMaterials.Flerovium, 144)
                .inputFluids(GTMaterials.Oganesson, 144)
                .inputFluids(GTMaterials.Hassium, 144)
                .inputFluids(GTMaterials.Deuterium, 1000)
                .outputFluids(GTOMaterials.HeavyQuarkDegenerateMatter.getFluid(FluidStorageKeys.PLASMA, 1152))
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("astral_titanium_plasma")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputFluids(GTOMaterials.Force, 576)
                .inputFluids(GTMaterials.Titanium, 576)
                .inputFluids(GTMaterials.Cobalt, 288)
                .inputFluids(GTMaterials.Copper, 288)
                .inputFluids(GTMaterials.Tritium, 1000)
                .outputFluids(GTOMaterials.AstralTitanium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("dragon_heart")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(Blocks.DRAGON_EGG.asItem(), 64)
                .inputItems(TagPrefix.plateDouble, GTOMaterials.AwakenedDraconium)
                .outputItems(GTOItems.DRAGON_HEART)
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("quark_gluon_plasma")
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.DegenerateRhenium, 10)
                .outputFluids(GTOMaterials.QuarkGluon.getFluid(FluidStorageKeys.PLASMA, 10000))
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("contained_high_density_protonic_matter")
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asItem())
                .inputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT)
                .inputItems(GTOItems.CHARGED_TRIPLET_NEUTRONIUM_SPHERE)
                .outputItems(GTOItems.CONTAINED_HIGH_DENSITY_PROTONIC_MATTER)
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("temporalfluid")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem(), 4)
                .inputItems(GTOItems.HYPERCUBE)
                .inputFluids(GTOMaterials.SpaceTime, 1000)
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue, 100)
                .outputFluids(GTOMaterials.TemporalFluid, 500)
                .outputFluids(GTOMaterials.SpatialFluid, 500)
                .EUt(2013265920)
                .duration(800)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("enderium_plasma")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(GTOItems.ENDER_CRYSTAL)
                .inputFluids(GTMaterials.EnderEye, 2304)
                .inputFluids(GTMaterials.Lead, 2304)
                .inputFluids(GTMaterials.Bismuth, 2304)
                .inputFluids(GTMaterials.Platinum, 1152)
                .inputFluids(GTMaterials.LiquidEnderAir, 100000)
                .outputFluids(GTOMaterials.Enderium.getFluid(FluidStorageKeys.PLASMA, 2304))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("crystal_matrix_plasma")
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asItem())
                .inputItems(GTOItems.CORPOREAL_MATTER, 16)
                .inputFluids(GTOMaterials.FreeProtonGas, 20000)
                .outputFluids(GTOMaterials.CrystalMatrix.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("high_energy_quark_gluon_plasma")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(TagPrefix.plateDouble, GTOMaterials.HeavyQuarkDegenerateMatter, 10)
                .inputItems(GTOItems.PULSATING_CRYSTAL)
                .outputFluids(GTOMaterials.HighEnergyQuarkGluon.getFluid(FluidStorageKeys.PLASMA, 2000))
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("infuscolium_plasma")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(Items.END_CRYSTAL.asItem(), 16)
                .inputItems(Items.POPPED_CHORUS_FRUIT.asItem(), 16)
                .inputFluids(GTOMaterials.Adamantine, 2304)
                .inputFluids(GTOMaterials.TranscendingMatter, 10000)
                .outputFluids(GTOMaterials.Infuscolium.getFluid(FluidStorageKeys.PLASMA, 2304))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("actinium_superhydride_plasma")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.ActiniumHydride, 36)
                .inputFluids(GTMaterials.Hydrogen, 81000)
                .outputFluids(GTOMaterials.ActiniumSuperhydride.getFluid(FluidStorageKeys.PLASMA, 36000))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("eigenfolded_kerr_manifold")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(GTOItems.STABILIZED_WORMHOLE_GENERATOR)
                .inputItems(GTOItems.RECURSIVELY_FOLDED_NEGATIVE_SPACE)
                .outputItems(GTOItems.EIGENFOLDED_KERR_MANIFOLD)
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.builder("resonarium_plasma")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(GTOTagPrefix.gemExquisite, GTOMaterials.Resonarium, 2)
                .outputFluids(GTOMaterials.Resonarium, FluidStorageKeys.PLASMA, 1152)
                .EUt(20971520)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.builder("blazecube_plasma")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems("apotheosis:gem_dust", 4)
                .inputItems("deeperdarker:sculk_bone", 4)
                .inputFluids(GTOMaterials.Sanguinite, 576)
                .inputFluids(GTOMaterials.Tartarite, 576)
                .inputFluids(GTMaterials.Blaze, 576)
                .outputFluids(GTOMaterials.BlazeCube, FluidStorageKeys.PLASMA, 1000)
                .EUt(31457280)
                .duration(128)
                .save();
    }
}
