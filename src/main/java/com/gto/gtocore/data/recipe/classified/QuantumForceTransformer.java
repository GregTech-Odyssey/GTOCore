package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.init.GTOBlocks;
import com.gto.gtocore.init.GTOItems;
import com.gto.gtocore.init.GTOMaterials;
import com.gto.gtocore.init.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;

import committee.nova.mods.avaritia.init.registry.ModItems;

import java.util.function.Consumer;

interface QuantumForceTransformer {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("hyper_stable_self_healing_adhesive"))
                .chancedInput(ChemicalHelper.get(GTOTagPrefix.nanites, GTOMaterials.Uruium), 500, 0)
                .inputItems(TagPrefix.dust, GTMaterials.ActivatedCarbon, 64)
                .inputItems(TagPrefix.dust, GTMaterials.Bismuth, 64)
                .inputFluids(GTMaterials.Oxygen.getFluid(20000))
                .inputFluids(GTMaterials.Hydrogen.getFluid(20000))
                .chancedOutput(GTOItems.HYPER_STABLE_SELF_HEALING_ADHESIVE.asStack(), 2000, 0)
                .EUt(8053063680L)
                .duration(20)
                .save(provider);

        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("spacetime_hex_wire"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.SpaceTime, 32)
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.TranscendentMetal, 32)
                .inputItems(TagPrefix.wireGtOctal, GTOMaterials.SpaceTime, 2)
                .inputFluids(GTOMaterials.Rhugnor.getFluid(1600))
                .outputItems(TagPrefix.wireGtHex, GTOMaterials.SpaceTime)
                .EUt(32212254720L)
                .duration(6400)
                .save(provider);

        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("magmatter"))
                .notConsumable(GTOItems.SPACETIME_CATALYST.asStack())
                .inputItems(TagPrefix.block, GTOMaterials.AttunedTengam)
                .inputFluids(GTOMaterials.Chaos.getFluid(1000))
                .inputFluids(GTOMaterials.SpatialFluid.getFluid(1000))
                .inputFluids(GTOMaterials.ExcitedDtsc.getFluid(1000))
                .outputFluids(GTOMaterials.Magmatter.getFluid(1000))
                .EUt(32212254720L)
                .duration(800)
                .save(provider);

        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("spatialfluid"))
                .notConsumable(GTOItems.HYPERCUBE.asStack())
                .notConsumable(GTOItems.QUANTUM_ANOMALY.asStack())
                .inputItems(TagPrefix.plate, GTOMaterials.CosmicNeutronium, 16)
                .inputFluids(GTOMaterials.TemporalFluid.getFluid(10000))
                .inputFluids(GTOMaterials.ExcitedDtsc.getFluid(10000))
                .outputFluids(GTOMaterials.SpatialFluid.getFluid(10000))
                .EUt(8053063680L)
                .duration(600)
                .save(provider);

        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("radox"))
                .inputItems(TagPrefix.dust, GTOMaterials.MolybdenumTrioxide, 16)
                .inputItems(TagPrefix.dust, GTMaterials.ChromiumTrioxide, 16)
                .inputItems(TagPrefix.dust, GTMaterials.PhosphorusPentoxide, 14)
                .inputItems(TagPrefix.dust, GTOMaterials.CubicZirconia, 12)
                .inputItems(TagPrefix.dust, GTOMaterials.GermaniumDioxide, 12)
                .inputItems(TagPrefix.dust, GTMaterials.SiliconDioxide, 12)
                .inputItems(TagPrefix.dust, GTMaterials.ArsenicTrioxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.AntimonyTrioxide, 10)
                .inputItems(TagPrefix.dust, GTOMaterials.BoronTrioxide, 10)
                .inputItems(TagPrefix.dust, GTMaterials.Zincite, 8)
                .inputItems(TagPrefix.dust, GTMaterials.Magnesia, 8)
                .inputItems(TagPrefix.dust, GTMaterials.CobaltOxide, 8)
                .inputItems(TagPrefix.dust, GTMaterials.Massicot, 8)
                .inputItems(TagPrefix.dust, GTMaterials.CupricOxide, 8)
                .inputItems(TagPrefix.dust, GTMaterials.Potash, 6)
                .inputItems(TagPrefix.dust, GTOMaterials.SilverOxide, 6)
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumOxide, 6)
                .inputItems(TagPrefix.dust, GTOMaterials.RareEarthOxide, 4)
                .inputFluids(GTOMaterials.RadoxGas.getFluid(21600))
                .inputFluids(GTMaterials.Oxygen.getFluid(FluidStorageKeys.PLASMA, 75000))
                .inputFluids(GTOMaterials.Titanium50Tetrachloride.getFluid(1000))
                .outputFluids(GTOMaterials.Radox.getFluid(10800))
                .EUt(503316480)
                .duration(8000)
                .save(provider);

        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("spacetime_double_wire"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.SpaceTime, 4)
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.TranscendentMetal, 4)
                .inputItems(TagPrefix.wireGtSingle, GTOMaterials.SpaceTime, 2)
                .inputFluids(GTOMaterials.Rhugnor.getFluid(200))
                .outputItems(TagPrefix.wireGtDouble, GTOMaterials.SpaceTime)
                .EUt(32212254720L)
                .duration(800)
                .save(provider);

        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("spacetime_octal_wire"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.SpaceTime, 16)
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.TranscendentMetal, 16)
                .inputItems(TagPrefix.wireGtQuadruple, GTOMaterials.SpaceTime, 2)
                .inputFluids(GTOMaterials.Rhugnor.getFluid(800))
                .outputItems(TagPrefix.wireGtOctal, GTOMaterials.SpaceTime)
                .EUt(32212254720L)
                .duration(3200)
                .save(provider);

        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("cosmic_ingot"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.CosmicNeutronium)
                .chancedInput(GTOItems.COSMIC_SINGULARITY.asStack(), 1000, 0)
                .inputItems(GTOItems.HYPERCUBE.asStack())
                .inputItems(TagPrefix.ingot, GTOMaterials.Infinity)
                .inputFluids(GTOMaterials.WhiteDwarfMatter.getFluid(576))
                .inputFluids(GTOMaterials.BlackDwarfMatter.getFluid(576))
                .inputFluids(GTOMaterials.PrimordialMatter.getFluid(500))
                .outputItems(TagPrefix.ingot, GTOMaterials.Cosmic)
                .EUt(128849018880L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("quantum_anomaly"))
                .chancedInput(ChemicalHelper.get(GTOTagPrefix.nanites, GTOMaterials.Draconium), 100, 0)
                .inputItems(GTOItems.ENTANGLED_SINGULARITY.asStack())
                .inputFluids(GTMaterials.Duranium.getFluid(144))
                .inputFluids(GTOMaterials.ExcitedDtec.getFluid(100))
                .chancedOutput(GTOItems.QUANTUM_ANOMALY.asStack(), 1000, 0)
                .EUt(2013265920)
                .duration(400)
                .save(provider);

        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("black_body_naquadria_supersolid"))
                .chancedInput(ChemicalHelper.get(GTOTagPrefix.nanites, GTOMaterials.Uruium), 500, 0)
                .inputItems(TagPrefix.dust, GTMaterials.Naquadria, 64)
                .inputItems(TagPrefix.dust, GTMaterials.Magnesium, 64)
                .inputFluids(GTMaterials.PhosphoricAcid.getFluid(20000))
                .inputFluids(GTMaterials.SulfuricAcid.getFluid(20000))
                .chancedOutput(GTOItems.BLACK_BODY_NAQUADRIA_SUPERSOLID.asStack(), 2000, 0)
                .EUt(8053063680L)
                .duration(20)
                .save(provider);

        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("spacetime_single_wire"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.SpaceTime)
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.TranscendentMetal)
                .inputItems(TagPrefix.wireGtSingle, GTOMaterials.Infinity)
                .inputFluids(GTOMaterials.SpaceTime.getFluid(100))
                .inputFluids(GTOMaterials.Rhugnor.getFluid(100))
                .outputItems(TagPrefix.wireGtSingle, GTOMaterials.SpaceTime)
                .EUt(32212254720L)
                .duration(400)
                .save(provider);

        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("fullerene_polymer_matrix_pulp_dust"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.Starmetal)
                .inputItems(TagPrefix.dust, GTOMaterials.Fullerene, 16)
                .inputItems(TagPrefix.dust, GTMaterials.Palladium, 8)
                .inputFluids(GTMaterials.Nitrogen.getFluid(15000))
                .inputFluids(GTMaterials.Hydrogen.getFluid(73000))
                .inputFluids(GTMaterials.Oxygen.getFluid(13000))
                .outputItems(TagPrefix.dust, GTOMaterials.FullerenePolymerMatrixPulp, 16)
                .EUt(2013265920)
                .duration(400)
                .save(provider);

        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("spacetime_quadruple_wire"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.SpaceTime, 8)
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.TranscendentMetal, 8)
                .inputItems(TagPrefix.wireGtDouble, GTOMaterials.SpaceTime, 2)
                .inputFluids(GTOMaterials.Rhugnor.getFluid(400))
                .outputItems(TagPrefix.wireGtQuadruple, GTOMaterials.SpaceTime)
                .EUt(32212254720L)
                .duration(1600)
                .save(provider);

        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("eternal_singularity_1"))
                .notConsumable(GTOItems.ETERNITY_CATALYST.asStack())
                .inputItems(TagPrefix.block, GTMaterials.Neutronium, 64)
                .inputItems(GTOItems.COMBINED_SINGULARITY_0.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_1.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_2.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_3.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_4.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_5.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_6.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_7.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_8.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_9.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_10.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_11.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_12.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_13.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_14.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_15.asStack())
                .inputFluids(GTOMaterials.CosmicNeutronium.getFluid(1000))
                .inputFluids(GTOMaterials.ExcitedDtec.getFluid(1000))
                .inputFluids(GTOMaterials.SpatialFluid.getFluid(1000))
                .outputItems(ModItems.eternal_singularity.get(), 16)
                .EUt(32212254720L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("radox_gas"))
                .notConsumable(GTOItems.QUANTUM_ANOMALY.asStack())
                .inputItems(GTOBlocks.VARIATION_WOOD.asStack(64))
                .inputFluids(GTOMaterials.Xenoxene.getFluid(10000))
                .inputFluids(GTOMaterials.UnknowWater.getFluid(90000))
                .inputFluids(GTOMaterials.TemporalFluid.getFluid(100))
                .outputFluids(GTOMaterials.RadoxGas.getFluid(100000))
                .EUt(2013265920)
                .duration(400)
                .save(provider);

        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("taranium_dust"))
                .notConsumable(ModItems.infinity_catalyst.get())
                .inputItems(TagPrefix.dust, GTOMaterials.Bedrockium, 176)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 64)
                .inputItems(TagPrefix.dust, GTMaterials.Deepslate, 640)
                .inputFluids(GTMaterials.Helium.getFluid(37000))
                .inputFluids(GTMaterials.Hydrogen.getFluid(73000))
                .inputFluids(GTMaterials.Xenon.getFluid(3000))
                .outputItems(TagPrefix.dust, GTOMaterials.Taranium, 64)
                .EUt(2013265920)
                .duration(1600)
                .save(provider);

        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("eternal_singularity"))
                .notConsumable(ModItems.infinity_catalyst.get())
                .inputItems(TagPrefix.block, GTMaterials.Neutronium, 64)
                .inputItems(GTOItems.COMBINED_SINGULARITY_0.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_1.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_2.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_3.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_4.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_5.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_6.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_7.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_8.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_9.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_10.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_11.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_12.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_13.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_14.asStack())
                .inputItems(GTOItems.COMBINED_SINGULARITY_15.asStack())
                .inputFluids(GTOMaterials.AwakenedDraconium.getFluid(1000))
                .inputFluids(GTOMaterials.CosmicNeutronium.getFluid(1000))
                .inputFluids(GTOMaterials.DimensionallyTranscendentStellarCatalyst.getFluid(1000))
                .outputItems(ModItems.eternal_singularity.get())
                .EUt(32212254720L)
                .duration(200)
                .save(provider);

        GTORecipeTypes.QUANTUM_FORCE_TRANSFORMER_RECIPES.recipeBuilder(GTOCore.id("timepiece"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.WhiteDwarfMatter)
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.BlackDwarfMatter)
                .chancedInput(ChemicalHelper.get(TagPrefix.wireGtHex, GTOMaterials.SpaceTime), 1, 0)
                .inputFluids(GTOMaterials.CosmicElement.getFluid(100))
                .chancedOutput(GTOItems.TIMEPIECE.asStack(), 2500, 0)
                .EUt(2013265920)
                .duration(200)
                .save(provider);
    }
}
