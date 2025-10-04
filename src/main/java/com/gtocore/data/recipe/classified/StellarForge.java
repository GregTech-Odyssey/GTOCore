package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.GTOValues;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import com.enderio.base.common.init.EIOItems;
import committee.nova.mods.avaritia.init.registry.ModBlocks;
import committee.nova.mods.avaritia.init.registry.ModItems;

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
                .outputFluids(GTOMaterials.Neutron.getFluid(100))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("contained_reissner_nordstrom_singularity")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asItem(), 64)
                .inputItems(GTOItems.CHARGED_TRIPLET_NEUTRONIUM_SPHERE.asItem(), 64)
                .outputItems(GTOItems.CONTAINED_REISSNER_NORDSTROM_SINGULARITY.asItem(), 64)
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("closed_timelike_curvecomputational_unit")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(GTOItems.EIGENFOLDED_KERR_MANIFOLD.asItem())
                .inputItems(GTOItems.CLOSED_TIMELIKE_CURVE_COMPUTATIONAL_UNIT_CONTAINER.asItem())
                .outputItems(GTOItems.CLOSED_TIMELIKE_CURVE_COMPUTATIONAL_UNIT.asItem())
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("cosmic_mesh_plasma")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(GTOItems.HIGHLY_DENSE_POLYMER_PLATE.asItem())
                .outputFluids(GTOMaterials.CosmicMesh.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("legendarium_plasma")
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asItem())
                .inputItems(GTOItems.NEUTRON_PLASMA_CONTAINMENT_CELL.asItem())
                .inputFluids(GTOMaterials.Lemurite.getFluid(576))
                .inputFluids(GTOMaterials.Alduorite.getFluid(576))
                .inputFluids(GTOMaterials.Kalendrite.getFluid(576))
                .inputFluids(GTOMaterials.Haderoth.getFluid(576))
                .inputFluids(GTOMaterials.Ignatius.getFluid(576))
                .inputFluids(GTOMaterials.Ceruclase.getFluid(576))
                .inputFluids(GTOMaterials.Sanguinite.getFluid(576))
                .inputFluids(GTOMaterials.Quicksilver.getFluid(576))
                .inputFluids(GTOMaterials.Celenegil.getFluid(576))
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asItem())
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
                .inputItems(GTOItems.CONTAINED_HIGH_DENSITY_PROTONIC_MATTER.asItem())
                .outputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asItem())
                .outputFluids(GTOMaterials.FreeProtonGas.getFluid(10000))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("recursively_folded_negative_space")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(GTOItems.MACROWORMHOLE_GENERATOR.asItem(), 2)
                .inputItems(GTOItems.TEMPORAL_MATTER.asItem(), 2)
                .outputItems(GTOItems.RECURSIVELY_FOLDED_NEGATIVE_SPACE.asItem())
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("dense_neutron_plasma")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(TagPrefix.block, GTOMaterials.Neutron, 5)
                .inputItems(TagPrefix.block, GTOMaterials.HeavyQuarkDegenerateMatter, 5)
                .inputFluids(GTOMaterials.Periodicium.getFluid(2736))
                .inputFluids(GTOMaterials.Gluons.getFluid(6000))
                .inputFluids(GTOMaterials.HeavyLeptonMixture.getFluid(6000))
                .outputFluids(GTOMaterials.DenseNeutron.getFluid(FluidStorageKeys.PLASMA, 6000))
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("quantum_chromo_dynamically_confined_matter_plasma")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(GTOItems.QUANTUMCHROMODYNAMIC_PROTECTIVE_PLATING.asItem(), 20)
                .outputFluids(GTOMaterials.QuantumChromoDynamicallyConfinedMatter.getFluid(FluidStorageKeys.PLASMA, 2000))
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("chaos_shard")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(GTOBlocks.INFUSED_OBSIDIAN.asItem())
                .inputItems(Blocks.BEDROCK.asItem())
                .inputFluids(GTOMaterials.Radox.getFluid(1000))
                .outputItems(GTOItems.CHAOS_SHARD.asItem())
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("contained_kerr_newmann_singularity")
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asItem())
                .inputItems(GTOItems.CONTAINED_REISSNER_NORDSTROM_SINGULARITY.asItem(), 64)
                .outputItems(GTOItems.CONTAINED_KERR_NEWMANN_SINGULARITY.asItem())
                .outputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asItem(), 63)
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("adamantium_plasma")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.Bloodstone, 24)
                .inputFluids(GTOMaterials.Orichalcum.getFluid(576))
                .inputFluids(GTMaterials.Tin.getFluid(1024))
                .inputFluids(GTMaterials.Antimony.getFluid(864))
                .inputFluids(GTMaterials.Iron.getFluid(1152))
                .inputFluids(GTMaterials.Mercury.getFluid(1000))
                .outputFluids(GTOMaterials.Adamantium.getFluid(FluidStorageKeys.PLASMA, 2304))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("contained_exotic_matter")
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asItem())
                .inputItems(GTOItems.CONTAINED_HIGH_DENSITY_PROTONIC_MATTER.asItem())
                .inputItems(TagPrefix.dustTiny, GTOMaterials.DegenerateRhenium, 9)
                .outputItems(GTOItems.CONTAINED_EXOTIC_MATTER.asItem())
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("celestial_tungsten_plasma")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputFluids(GTOMaterials.Tartarite.getFluid(576))
                .inputFluids(GTMaterials.Tungsten.getFluid(576))
                .inputFluids(GTMaterials.Americium.getFluid(288))
                .inputFluids(GTOMaterials.TitanPrecisionSteel.getFluid(144))
                .inputFluids(GTOMaterials.AstralTitanium.getFluid(144))
                .inputFluids(GTMaterials.Xenon.getFluid(1000))
                .outputFluids(GTOMaterials.CelestialTungsten.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("neutron_plasma_containment_cell")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asItem())
                .inputFluids(GTOMaterials.Neutron.getFluid(1000))
                .inputFluids(GTOMaterials.HeavyLeptonMixture.getFluid(1000))
                .outputItems(GTOItems.NEUTRON_PLASMA_CONTAINMENT_CELL.asItem())
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("starmetal_plasma")
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asItem())
                .inputItems(GTOItems.RESONATING_GEM.asItem(), 10)
                .inputFluids(GTOMaterials.FreeProtonGas.getFluid(1000))
                .inputFluids(GTOMaterials.FreeElectronGas.getFluid(1000))
                .outputFluids(GTOMaterials.Starmetal.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("exciteddtec")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(EIOItems.PRESCIENT_POWDER.asItem())
                .inputFluids(GTOMaterials.DimensionallyTranscendentExoticCatalyst.getFluid(10000))
                .outputFluids(GTOMaterials.ExcitedDtec.getFluid(10000))
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("eternity_dust")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem(), 4)
                .inputItems(ModItems.eternal_singularity.get())
                .inputFluids(GTOMaterials.PrimordialMatter.getFluid(1000))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Eternity)
                .outputFluids(GTOMaterials.TemporalFluid.getFluid(1000))
                .EUt(2013265920)
                .duration(800)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("extremely_durable_plasma_cell")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(GTOItems.DENSE_NEUTRON_PLASMA_CELL.asItem(), 2)
                .outputItems(GTOItems.COSMIC_NEUTRON_PLASMA_CELL.asItem())
                .outputItems(GTOItems.EXTREMELY_DURABLE_PLASMA_CELL.asItem())
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("infinity_ingot")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(EIOItems.VIBRANT_POWDER.asItem())
                .inputFluids(GTOMaterials.CrystalMatrix.getFluid(2000))
                .inputFluids(GTOMaterials.CosmicNeutronium.getFluid(1000))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Infinity)
                .outputFluids(GTOMaterials.Infinity.getFluid(10))
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("heavy_quark_degenerate_matter_plasma")
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asItem())
                .inputFluids(GTOMaterials.HeavyQuarkEnrichedMixture.getFluid(1152))
                .inputFluids(GTMaterials.Flerovium.getFluid(144))
                .inputFluids(GTMaterials.Oganesson.getFluid(144))
                .inputFluids(GTMaterials.Hassium.getFluid(144))
                .inputFluids(GTMaterials.Deuterium.getFluid(1000))
                .outputFluids(GTOMaterials.HeavyQuarkDegenerateMatter.getFluid(FluidStorageKeys.PLASMA, 1152))
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("astral_titanium_plasma")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputFluids(GTOMaterials.Force.getFluid(576))
                .inputFluids(GTMaterials.Titanium.getFluid(576))
                .inputFluids(GTMaterials.Cobalt.getFluid(288))
                .inputFluids(GTMaterials.Copper.getFluid(288))
                .inputFluids(GTMaterials.Tritium.getFluid(1000))
                .outputFluids(GTOMaterials.AstralTitanium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("dragon_heart")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(Blocks.DRAGON_EGG.asItem(), 64)
                .inputItems(TagPrefix.plateDouble, GTOMaterials.AwakenedDraconium)
                .outputItems(GTOItems.DRAGON_HEART.asItem())
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
                .inputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asItem())
                .inputItems(GTOItems.CHARGED_TRIPLET_NEUTRONIUM_SPHERE.asItem())
                .outputItems(GTOItems.CONTAINED_HIGH_DENSITY_PROTONIC_MATTER.asItem())
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("temporalfluid")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem(), 4)
                .inputItems(GTOItems.HYPERCUBE.asItem())
                .inputFluids(GTOMaterials.SpaceTime.getFluid(1000))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .outputFluids(GTOMaterials.TemporalFluid.getFluid(500))
                .outputFluids(GTOMaterials.SpatialFluid.getFluid(500))
                .EUt(2013265920)
                .duration(800)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("enderium_plasma")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(EIOItems.ENDER_CRYSTAL_POWDER.asItem())
                .inputFluids(GTMaterials.EnderEye.getFluid(2304))
                .inputFluids(GTMaterials.Lead.getFluid(2304))
                .inputFluids(GTMaterials.Bismuth.getFluid(2304))
                .inputFluids(GTMaterials.Platinum.getFluid(1152))
                .inputFluids(GTMaterials.LiquidEnderAir.getFluid(100000))
                .outputFluids(GTOMaterials.Enderium.getFluid(FluidStorageKeys.PLASMA, 2304))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("crystal_matrix_plasma")
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asItem())
                .inputItems(ModBlocks.crystal_matrix.get().asItem())
                .inputItems(GTOItems.CORPOREAL_MATTER.asItem(), 16)
                .inputFluids(GTOMaterials.FreeProtonGas.getFluid(20000))
                .outputFluids(GTOMaterials.CrystalMatrix.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("high_energy_quark_gluon_plasma")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(TagPrefix.plateDouble, GTOMaterials.HeavyQuarkDegenerateMatter, 10)
                .inputItems(EIOItems.PULSATING_POWDER.asItem())
                .outputFluids(GTOMaterials.HighEnergyQuarkGluon.getFluid(FluidStorageKeys.PLASMA, 2000))
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("infuscolium_plasma")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(Items.END_CRYSTAL.asItem(), 16)
                .inputItems(Items.POPPED_CHORUS_FRUIT.asItem(), 16)
                .inputFluids(GTOMaterials.Adamantine.getFluid(2304))
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .outputFluids(GTOMaterials.Infuscolium.getFluid(FluidStorageKeys.PLASMA, 2304))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("actinium_superhydride_plasma")
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asItem())
                .inputItems(TagPrefix.dust, GTOMaterials.ActiniumHydride, 36)
                .inputFluids(GTMaterials.Hydrogen.getFluid(81000))
                .outputFluids(GTOMaterials.ActiniumSuperhydride.getFluid(FluidStorageKeys.PLASMA, 36000))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save();

        STELLAR_FORGE_RECIPES.recipeBuilder("eigenfolded_kerr_manifold")
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asItem())
                .inputItems(GTOItems.STABILIZED_WORMHOLE_GENERATOR.asItem())
                .inputItems(GTOItems.RECURSIVELY_FOLDED_NEGATIVE_SPACE.asItem())
                .outputItems(GTOItems.EIGENFOLDED_KERR_MANIFOLD.asItem())
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save();
    }
}
