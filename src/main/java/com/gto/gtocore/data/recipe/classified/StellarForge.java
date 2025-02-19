package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.GTOValues;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import com.enderio.base.common.init.EIOItems;
import committee.nova.mods.avaritia.init.registry.ModBlocks;
import committee.nova.mods.avaritia.init.registry.ModItems;

import java.util.function.Consumer;

interface StellarForge {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("contained_reissner_nordstrom_singularity"))
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asStack())
                .inputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asStack(64))
                .inputItems(GTOItems.CHARGED_TRIPLET_NEUTRONIUM_SPHERE.asStack(64))
                .outputItems(GTOItems.CONTAINED_REISSNER_NORDSTROM_SINGULARITY.asStack(64))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("closed_timelike_curvecomputational_unit"))
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asStack())
                .inputItems(GTOItems.EIGENFOLDED_KERR_MANIFOLD.asStack())
                .inputItems(GTOItems.CLOSED_TIMELIKE_CURVE_COMPUTATIONAL_UNIT_CONTAINER.asStack())
                .outputItems(GTOItems.CLOSED_TIMELIKE_CURVE_COMPUTATIONAL_UNIT.asStack())
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("cosmic_mesh_plasma"))
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asStack())
                .inputItems(GTOItems.HIGHLY_DENSE_POLYMER_PLATE.asStack())
                .outputFluids(GTOMaterials.CosmicMesh.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("legendarium_plasma"))
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asStack())
                .inputItems(GTOItems.NEUTRON_PLASMA_CONTAINMENT_CELL.asStack())
                .inputFluids(GTOMaterials.NaquadriaticTaranium.getFluid(576))
                .inputFluids(GTMaterials.Trinium.getFluid(288))
                .inputFluids(GTMaterials.Duranium.getFluid(288))
                .inputFluids(GTMaterials.Tritanium.getFluid(288))
                .inputFluids(GTOMaterials.Orichalcum.getFluid(288))
                .inputFluids(GTOMaterials.Mithril.getFluid(288))
                .inputFluids(GTOMaterials.Adamantium.getFluid(288))
                .inputFluids(GTOMaterials.Adamantine.getFluid(288))
                .inputFluids(GTOMaterials.Vibranium.getFluid(288))
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asStack())
                .outputFluids(GTOMaterials.Legendarium.getFluid(FluidStorageKeys.PLASMA, 2304))
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("degenerate_rhenium_plasma"))
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asStack())
                .inputItems(TagPrefix.plateDouble, GTMaterials.Rhenium, 5)
                .outputFluids(GTOMaterials.DegenerateRhenium.getFluid(FluidStorageKeys.PLASMA, 10000))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("free_proton_gas"))
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asStack())
                .inputItems(GTOItems.CONTAINED_HIGH_DENSITY_PROTONIC_MATTER.asStack())
                .outputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asStack())
                .outputFluids(GTOMaterials.FreeProtonGas.getFluid(10000))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("recursively_folded_negative_space"))
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asStack())
                .inputItems(GTOItems.MACROWORMHOLE_GENERATOR.asStack(2))
                .inputItems(GTOItems.TEMPORAL_MATTER.asStack(2))
                .outputItems(GTOItems.RECURSIVELY_FOLDED_NEGATIVE_SPACE.asStack())
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("dense_neutron_plasma"))
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asStack())
                .inputItems(TagPrefix.block, GTMaterials.Neutronium, 5)
                .inputItems(TagPrefix.block, GTOMaterials.HeavyQuarkDegenerateMatter, 5)
                .inputFluids(GTOMaterials.Periodicium.getFluid(2736))
                .inputFluids(GTOMaterials.Gluons.getFluid(6000))
                .inputFluids(GTOMaterials.HeavyLeptonMixture.getFluid(6000))
                .outputFluids(GTOMaterials.DenseNeutron.getFluid(FluidStorageKeys.PLASMA, 6000))
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("quantum_chromo_dynamically_confined_matter_plasma"))
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asStack())
                .inputItems(GTOItems.QUANTUMCHROMODYNAMIC_PROTECTIVE_PLATING.asStack(20))
                .outputFluids(GTOMaterials.QuantumChromoDynamicallyConfinedMatter.getFluid(FluidStorageKeys.PLASMA, 2000))
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("chaos_shard"))
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asStack())
                .inputItems(GTOBlocks.INFUSED_OBSIDIAN.asStack())
                .inputItems(new ItemStack(Blocks.BEDROCK.asItem()))
                .inputFluids(GTOMaterials.Radox.getFluid(1000))
                .outputItems(GTOItems.CHAOS_SHARD.asStack())
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("contained_kerr_newmann_singularity"))
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asStack())
                .inputItems(GTOItems.CONTAINED_REISSNER_NORDSTROM_SINGULARITY.asStack(64))
                .outputItems(GTOItems.CONTAINED_KERR_NEWMANN_SINGULARITY.asStack())
                .outputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asStack(63))
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("adamantium_plasma"))
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asStack())
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
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("contained_exotic_matter"))
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asStack())
                .inputItems(GTOItems.CONTAINED_HIGH_DENSITY_PROTONIC_MATTER.asStack())
                .inputItems(TagPrefix.dustTiny, GTOMaterials.DegenerateRhenium, 9)
                .outputItems(GTOItems.CONTAINED_EXOTIC_MATTER.asStack())
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("celestial_tungsten_plasma"))
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asStack())
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
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("neutron_plasma_containment_cell"))
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asStack())
                .inputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asStack())
                .inputFluids(GTMaterials.Neutronium.getFluid(1000))
                .inputFluids(GTOMaterials.HeavyLeptonMixture.getFluid(1000))
                .outputItems(GTOItems.NEUTRON_PLASMA_CONTAINMENT_CELL.asStack())
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("starmetal_plasma"))
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asStack())
                .inputItems(GTOItems.RESONATING_GEM.asStack(10))
                .inputFluids(GTOMaterials.FreeProtonGas.getFluid(1000))
                .inputFluids(GTOMaterials.FreeElectronGas.getFluid(1000))
                .outputFluids(GTOMaterials.Starmetal.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("exciteddtec"))
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asStack())
                .inputItems(EIOItems.PRESCIENT_POWDER.asStack())
                .inputFluids(GTOMaterials.DimensionallyTranscendentExoticCatalyst.getFluid(10000))
                .outputFluids(GTOMaterials.ExcitedDtec.getFluid(10000))
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("eternity_dust"))
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asStack(4))
                .inputItems(ModItems.eternal_singularity.get())
                .inputFluids(GTOMaterials.PrimordialMatter.getFluid(1000))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.Eternity)
                .outputFluids(GTOMaterials.TemporalFluid.getFluid(1000))
                .EUt(2013265920)
                .duration(800)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("extremely_durable_plasma_cell"))
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asStack())
                .inputItems(GTOItems.DENSE_NEUTRON_PLASMA_CELL.asStack(2))
                .outputItems(GTOItems.COSMIC_NEUTRON_PLASMA_CELL.asStack())
                .outputItems(GTOItems.EXTREMELY_DURABLE_PLASMA_CELL.asStack())
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("infinity_ingot"))
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asStack())
                .inputItems(EIOItems.VIBRANT_POWDER.asStack())
                .inputFluids(GTOMaterials.CrystalMatrix.getFluid(2000))
                .inputFluids(GTOMaterials.CosmicNeutronium.getFluid(1000))
                .outputItems(TagPrefix.ingotHot, GTOMaterials.Infinity)
                .outputFluids(GTOMaterials.Infinity.getFluid(10))
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("heavy_quark_degenerate_matter_plasma"))
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asStack())
                .inputFluids(GTOMaterials.HeavyQuarkEnrichedMixture.getFluid(1152))
                .inputFluids(GTMaterials.Flerovium.getFluid(144))
                .inputFluids(GTMaterials.Oganesson.getFluid(144))
                .inputFluids(GTMaterials.Hassium.getFluid(144))
                .inputFluids(GTMaterials.Deuterium.getFluid(1000))
                .outputFluids(GTOMaterials.HeavyQuarkDegenerateMatter.getFluid(FluidStorageKeys.PLASMA, 1152))
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("astral_titanium_plasma"))
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asStack())
                .inputFluids(GTOMaterials.Force.getFluid(576))
                .inputFluids(GTMaterials.Titanium.getFluid(576))
                .inputFluids(GTMaterials.Cobalt.getFluid(288))
                .inputFluids(GTMaterials.Copper.getFluid(288))
                .inputFluids(GTMaterials.Tritium.getFluid(1000))
                .outputFluids(GTOMaterials.AstralTitanium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("dragon_heart"))
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asStack())
                .inputItems(new ItemStack(Blocks.DRAGON_EGG.asItem(), 64))
                .inputItems(TagPrefix.plateDouble, GTOMaterials.AwakenedDraconium)
                .outputItems(GTOItems.DRAGON_HEART.asStack())
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("quark_gluon_plasma"))
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asStack())
                .inputItems(TagPrefix.dust, GTOMaterials.DegenerateRhenium, 10)
                .outputFluids(GTOMaterials.QuarkGluon.getFluid(FluidStorageKeys.PLASMA, 10000))
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("contained_high_density_protonic_matter"))
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asStack())
                .inputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asStack())
                .inputItems(GTOItems.CHARGED_TRIPLET_NEUTRONIUM_SPHERE.asStack())
                .outputItems(GTOItems.CONTAINED_HIGH_DENSITY_PROTONIC_MATTER.asStack())
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("temporalfluid"))
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asStack(4))
                .inputItems(GTOItems.HYPERCUBE.asStack())
                .inputFluids(GTOMaterials.SpaceTime.getFluid(1000))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .outputFluids(GTOMaterials.TemporalFluid.getFluid(500))
                .outputFluids(GTOMaterials.SpatialFluid.getFluid(500))
                .EUt(2013265920)
                .duration(800)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("enderium_plasma"))
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asStack())
                .inputItems(EIOItems.ENDER_CRYSTAL_POWDER.asStack())
                .inputFluids(GTMaterials.EnderEye.getFluid(2304))
                .inputFluids(GTMaterials.Lead.getFluid(2304))
                .inputFluids(GTMaterials.Bismuth.getFluid(2304))
                .inputFluids(GTMaterials.Platinum.getFluid(1152))
                .inputFluids(GTMaterials.LiquidEnderAir.getFluid(100000))
                .outputFluids(GTOMaterials.Enderium.getFluid(FluidStorageKeys.PLASMA, 2304))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("crystal_matrix_plasma"))
                .inputItems(GTOBlocks.LEPTONIC_CHARGE.asStack())
                .inputItems(ModBlocks.crystal_matrix.get().asItem())
                .inputItems(GTOItems.CORPOREAL_MATTER.asStack(16))
                .inputFluids(GTOMaterials.FreeProtonGas.getFluid(20000))
                .outputFluids(GTOMaterials.CrystalMatrix.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("high_energy_quark_gluon_plasma"))
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asStack())
                .inputItems(TagPrefix.plateDouble, GTOMaterials.HeavyQuarkDegenerateMatter, 10)
                .inputItems(EIOItems.PULSATING_POWDER.asStack())
                .outputFluids(GTOMaterials.HighEnergyQuarkGluon.getFluid(FluidStorageKeys.PLASMA, 2000))
                .EUt(125829120)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 2)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("infuscolium_plasma"))
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asStack())
                .inputItems(new ItemStack(Items.END_CRYSTAL.asItem(), 16))
                .inputItems(new ItemStack(Items.POPPED_CHORUS_FRUIT.asItem(), 16))
                .inputFluids(GTOMaterials.Adamantine.getFluid(2304))
                .inputFluids(GTOMaterials.TranscendingMatter.getFluid(10000))
                .outputFluids(GTOMaterials.Infuscolium.getFluid(FluidStorageKeys.PLASMA, 2304))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("actinium_superhydride_plasma"))
                .inputItems(GTOBlocks.NAQUADRIA_CHARGE.asStack())
                .inputItems(TagPrefix.dust, GTOMaterials.ActiniumHydride, 36)
                .inputFluids(GTMaterials.Hydrogen.getFluid(81000))
                .outputFluids(GTOMaterials.ActiniumSuperhydride.getFluid(FluidStorageKeys.PLASMA, 36000))
                .EUt(31457280)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 1)
                .save(provider);

        GTORecipeTypes.STELLAR_FORGE_RECIPES.recipeBuilder(GTOCore.id("eigenfolded_kerr_manifold"))
                .inputItems(GTOBlocks.QUANTUM_CHROMODYNAMIC_CHARGE.asStack())
                .inputItems(GTOItems.STABILIZED_WORMHOLE_GENERATOR.asStack())
                .inputItems(GTOItems.RECURSIVELY_FOLDED_NEGATIVE_SPACE.asStack())
                .outputItems(GTOItems.EIGENFOLDED_KERR_MANIFOLD.asStack())
                .EUt(503316480)
                .duration(200)
                .addData(GTOValues.STELLAR_CONTAINMENT_TIER, 3)
                .save(provider);
    }
}
