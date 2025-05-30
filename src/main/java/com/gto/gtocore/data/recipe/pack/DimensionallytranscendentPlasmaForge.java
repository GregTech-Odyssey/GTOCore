package com.gto.gtocore.data.recipe.pack;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;

import committee.nova.mods.avaritia.init.registry.ModBlocks;
import committee.nova.mods.avaritia.init.registry.ModItems;

import java.util.function.Consumer;

final class DimensionallytranscendentPlasmaForge {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("spacetime_ingot"))
                .notConsumable(GTOItems.INGOT_FIELD_SHAPE.asStack(64))
                .notConsumable(GTOBlocks.SPACETIMEBENDINGCORE.asStack(64))
                .inputFluids(GTOMaterials.SpaceTime.getFluid(1000))
                .inputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(TagPrefix.ingot, GTOMaterials.SpaceTime, 8)
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(6400)
                .blastFurnaceTemp(62000)
                .save(provider);

        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("hypercube_1"))
                .inputItems(TagPrefix.rod, GTOMaterials.TranscendentMetal, 16)
                .inputItems(GTOItems.QUANTUM_ANOMALY.asStack())
                .inputFluids(GTOMaterials.ExcitedDtec.getFluid(1000))
                .inputFluids(GTOMaterials.SpatialFluid.getFluid(1000))
                .outputItems(GTOItems.HYPERCUBE.asStack(64))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(32212254720L)
                .duration(6400)
                .blastFurnaceTemp(62000)
                .save(provider);

        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("hypercube"))
                .inputItems(TagPrefix.rod, GTOMaterials.CosmicNeutronium, 12)
                .inputItems(TagPrefix.rod, GTOMaterials.CelestialTungsten, 24)
                .inputFluids(GTOMaterials.ExcitedDtec.getFluid(1000))
                .outputItems(GTOItems.HYPERCUBE.asStack())
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(2400)
                .blastFurnaceTemp(30000)
                .save(provider);

        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("rhugnor"))
                .notConsumable(ModItems.infinity_catalyst.get())
                .inputItems(GTItems.ENERGIUM_CRYSTAL.asStack(64))
                .inputFluids(GTOMaterials.Infinity.getFluid(10000))
                .inputFluids(GTOMaterials.Quantum.getFluid(10000))
                .outputFluids(GTOMaterials.Rhugnor.getFluid(10000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(8053063680L)
                .duration(3600)
                .blastFurnaceTemp(36000)
                .save(provider);

        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("crystal_matrix_plasma"))
                .notConsumable(ModItems.infinity_catalyst.get())
                .inputItems(ModBlocks.crystal_matrix.get().asItem())
                .inputFluids(GTMaterials.UUMatter.getFluid(1000000))
                .inputFluids(GTOMaterials.FreeProtonGas.getFluid(20000))
                .outputFluids(GTOMaterials.CrystalMatrix.getFluid(FluidStorageKeys.PLASMA, 10000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(800)
                .blastFurnaceTemp(28000)
                .save(provider);

        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("dense_neutron_plasma"))
                .notConsumable(ModItems.infinity_catalyst.get())
                .inputItems(GTOItems.NEUTRON_PLASMA_CONTAINMENT_CELL.asStack())
                .inputFluids(GTOMaterials.HeavyQuarkDegenerateMatter.getFluid(FluidStorageKeys.PLASMA, 10000))
                .inputFluids(GTOMaterials.Periodicium.getFluid(1000))
                .outputItems(GTOItems.PLASMA_CONTAINMENT_CELL.asStack())
                .outputFluids(GTOMaterials.DenseNeutron.getFluid(FluidStorageKeys.PLASMA, 10000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(800)
                .blastFurnaceTemp(26000)
                .save(provider);

        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("create_casing"))
                .inputItems(TagPrefix.frameGt, GTOMaterials.Eternity)
                .inputItems(GTOItems.COMMAND_BLOCK_CORE.asStack())
                .inputFluids(GTOMaterials.ExcitedDtsc.getFluid(1000))
                .inputFluids(GTOMaterials.PrimordialMatter.getFluid(1000))
                .outputItems(GTOBlocks.CREATE_CASING.asStack())
                .EUt(32985348833280L)
                .duration(3200)
                .blastFurnaceTemp(96000)
                .save(provider);

        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("infinity_catalyst"))
                .inputItems(GTOItems.CONTAINED_EXOTIC_MATTER.asStack(64))
                .inputItems(GTOItems.ESSENTIA_MATTER.asStack(64))
                .inputFluids(GTOMaterials.Infinity.getFluid(1000))
                .inputFluids(GTOMaterials.HighEnergyQuarkGluon.getFluid(FluidStorageKeys.PLASMA, 100000))
                .outputItems(ModItems.infinity_catalyst.get())
                .outputItems(GTOItems.TIME_DILATION_CONTAINMENT_UNIT.asStack(64))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(4800)
                .blastFurnaceTemp(32000)
                .save(provider);

        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("chaos_plasma"))
                .inputItems(GTOItems.CHAOS_SHARD.asStack())
                .inputFluids(GTOMaterials.DimensionallyTranscendentResplendentCatalyst.getFluid(1000))
                .inputFluids(GTOMaterials.CosmicMesh.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputFluids(GTOMaterials.Chaos.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(1600)
                .blastFurnaceTemp(32000)
                .save(provider);

        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("spacetime"))
                .notConsumable(GTOItems.SPACETIME_CATALYST.asStack())
                .inputFluids(GTOMaterials.Infinity.getFluid(100))
                .inputFluids(GTOMaterials.Hypogen.getFluid(100))
                .outputFluids(GTOMaterials.SpaceTime.getFluid(200))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(8053063680L)
                .duration(1600)
                .blastFurnaceTemp(36000)
                .save(provider);

        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("cosmic_neutron_plasma_cell"))
                .notConsumable(ModItems.infinity_catalyst.get())
                .inputItems(GTOItems.EXTREMELY_DURABLE_PLASMA_CELL.asStack(5))
                .inputFluids(GTMaterials.UUMatter.getFluid(1000000))
                .inputFluids(GTOMaterials.DenseNeutron.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputItems(GTOItems.COSMIC_NEUTRON_PLASMA_CELL.asStack(5))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(800)
                .blastFurnaceTemp(28000)
                .save(provider);

        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("magnetohydrodynamically_constrained_star_matter"))
                .notConsumable(GTOItems.ETERNITY_CATALYST.asStack())
                .inputItems(GTOTagPrefix.nanites, GTOMaterials.Eternity)
                .inputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 100000))
                .inputFluids(GTOMaterials.ExcitedDtsc.getFluid(100000))
                .chancedOutput(GTOTagPrefix.contaminableNanites, GTOMaterials.Eternity, 5000, 0)
                .outputFluids(GTOMaterials.MagnetohydrodynamicallyConstrainedStarMatter.getFluid(100000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2061584302080L)
                .duration(6400)
                .blastFurnaceTemp(81000)
                .save(provider);

        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("infinity"))
                .notConsumable(GTOItems.SPACETIME_CATALYST.asStack())
                .inputFluids(GTOMaterials.CrystalMatrix.getFluid(FluidStorageKeys.PLASMA, 10000))
                .inputFluids(GTOMaterials.CosmicNeutronium.getFluid(5000))
                .outputFluids(GTOMaterials.Infinity.getFluid(1000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(8053063680L)
                .duration(1600)
                .blastFurnaceTemp(32000)
                .save(provider);

        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("dimensionallytranscendentresidue"))
                .inputFluids(GTOMaterials.DimensionallyTranscendentCrudeCatalyst.getFluid(100))
                .inputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 100))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(400)
                .blastFurnaceTemp(36000)
                .save(provider);

        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("hypogen"))
                .notConsumable(ModItems.infinity_catalyst.get())
                .inputItems(TagPrefix.block, GTOMaterials.QuantumChromoDynamicallyConfinedMatter)
                .inputFluids(GTOMaterials.Rhugnor.getFluid(10000))
                .inputFluids(GTOMaterials.DragonBlood.getFluid(10000))
                .outputFluids(GTOMaterials.Hypogen.getFluid(10000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(1200)
                .blastFurnaceTemp(26000)
                .save(provider);

        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("transcendent_metal"))
                .notConsumable(GTOItems.SPACETIME_CATALYST.asStack())
                .inputItems(GTOItems.HYPERCUBE.asStack())
                .inputFluids(GTOMaterials.SpaceTime.getFluid(100))
                .inputFluids(GTMaterials.Tennessine.getFluid(144))
                .outputFluids(GTOMaterials.TranscendentMetal.getFluid(144))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(2013265920)
                .duration(3200)
                .blastFurnaceTemp(36000)
                .save(provider);

        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("infinity_glass"))
                .inputItems(GTOItems.BLACK_BODY_NAQUADRIA_SUPERSOLID.asStack())
                .inputItems(TagPrefix.dust, GTOMaterials.Infinity, 2)
                .inputFluids(GTOMaterials.DimensionallyTranscendentExoticCatalyst.getFluid(1000))
                .inputFluids(GTOMaterials.WoodsGlass.getFluid(9216))
                .outputItems(GTOBlocks.INFINITY_GLASS.asStack())
                .EUt(8246337208320L)
                .duration(1600)
                .blastFurnaceTemp(88000)
                .save(provider);

        GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_PLASMA_FORGE_RECIPES.recipeBuilder(GTOCore.id("eternity"))
                .notConsumable(GTOItems.ETERNITY_CATALYST.asStack())
                .inputItems(ModItems.eternal_singularity.get())
                .inputFluids(GTOMaterials.PrimordialMatter.getFluid(1000))
                .inputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 9000))
                .outputFluids(GTOMaterials.Eternity.getFluid(10000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(100))
                .EUt(32212254720L)
                .duration(4800)
                .blastFurnaceTemp(56000)
                .save(provider);
    }
}
