package com.gto.gtocore.data.recipe.pack;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.chemical.material.MarkerMaterials;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

final class NanoForge {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("gold_nanites"))
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Yellow)
                .inputItems(TagPrefix.block, GTMaterials.Gold, 8)
                .inputItems(GTItems.SYSTEM_ON_CHIP.asStack(16))
                .inputFluids(GTMaterials.NaquadahEnriched.getFluid(2000))
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(20000))
                .inputFluids(GTMaterials.Bismuth.getFluid(20000))
                .outputItems(GTOTagPrefix.nanites, GTMaterials.Gold)
                .EUt(491520)
                .duration(2000)
                .addData("nano_forge_tier", 1)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("osmium_nanites"))
                .notConsumable(TagPrefix.lens, GTMaterials.Diamond)
                .inputItems(TagPrefix.block, GTMaterials.Osmium, 8)
                .inputItems(GTItems.SYSTEM_ON_CHIP.asStack(32))
                .inputFluids(GTMaterials.Naquadria.getFluid(2000))
                .inputFluids(GTMaterials.Hafnium.getFluid(8000))
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(20000))
                .outputItems(GTOTagPrefix.nanites, GTMaterials.Osmium)
                .EUt(491520)
                .duration(4000)
                .addData("nano_forge_tier", 1)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("infuscolium_nanites"))
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Pink)
                .inputItems(TagPrefix.block, GTOMaterials.Infuscolium, 8)
                .inputItems(GTItems.ADVANCED_SYSTEM_ON_CHIP.asStack(64))
                .inputItems(GTItems.HIGHLY_ADVANCED_SOC.asStack(32))
                .inputFluids(GTMaterials.Neutronium.getFluid(8000))
                .inputFluids(GTOMaterials.UuAmplifier.getFluid(4000))
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(40000))
                .outputItems(GTOTagPrefix.nanites, GTOMaterials.Infuscolium)
                .EUt(7864320)
                .duration(2000)
                .addData("nano_forge_tier", 2)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("draconium_nanites"))
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Purple)
                .inputItems(TagPrefix.block, GTOMaterials.Draconium, 8)
                .inputItems(GTItems.HIGHLY_ADVANCED_SOC_WAFER.asStack(32))
                .inputItems(GTOItems.OPTICAL_RAM_WAFER.asStack(32))
                .inputItems(GTOItems.OPTICAL_SOC.asStack(32))
                .inputItems(GTOItems.EXOTIC_PROCESSING_CORE.asStack(8))
                .inputFluids(GTMaterials.UUMatter.getFluid(40000))
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(80000))
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(80000))
                .outputItems(GTOTagPrefix.nanites, GTOMaterials.Draconium)
                .EUt(7864320)
                .duration(16000)
                .addData("nano_forge_tier", 2)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("spacetime_nanites"))
                .notConsumable(TagPrefix.lens, GTMaterials.Sapphire)
                .notConsumable(GTOItems.QUANTUM_ANOMALY.asStack())
                .inputItems(TagPrefix.block, GTOMaterials.SpaceTime, 8)
                .inputItems(GTOItems.EIGENFOLDED_KERR_MANIFOLD.asStack(4))
                .inputItems(GTOItems.SUPRACAUSAL_RAM_WAFER.asStack(16))
                .inputItems(GTOItems.SUPRACAUSAL_PROCESSING_CORE.asStack(8))
                .inputFluids(GTMaterials.UUMatter.getFluid(80000))
                .inputFluids(GTOMaterials.Infinity.getFluid(40000))
                .inputFluids(GTOMaterials.TemporalFluid.getFluid(40000))
                .outputItems(GTOTagPrefix.nanites, GTOMaterials.SpaceTime)
                .EUt(125829120)
                .duration(8000)
                .addData("nano_forge_tier", 3)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("neutronium_nanites"))
                .notConsumable(TagPrefix.lens, GTMaterials.NetherStar)
                .inputItems(TagPrefix.block, GTMaterials.Neutronium, 8)
                .inputItems(GTItems.SYSTEM_ON_CHIP.asStack(64))
                .inputItems(GTItems.ADVANCED_SYSTEM_ON_CHIP.asStack(32))
                .inputFluids(GTMaterials.Neutronium.getFluid(4000))
                .inputFluids(GTOMaterials.UuAmplifier.getFluid(2000))
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(20000))
                .outputItems(GTOTagPrefix.nanites, GTMaterials.Neutronium)
                .EUt(491520)
                .duration(32000)
                .addData("nano_forge_tier", 1)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("naquadah_nanites"))
                .notConsumable(TagPrefix.lens, GTMaterials.Emerald)
                .inputItems(TagPrefix.block, GTMaterials.Naquadah, 8)
                .inputItems(GTItems.ADVANCED_SYSTEM_ON_CHIP.asStack(16))
                .inputFluids(GTMaterials.Naquadria.getFluid(8000))
                .inputFluids(GTOMaterials.UuAmplifier.getFluid(2000))
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(20000))
                .outputItems(GTOTagPrefix.nanites, GTMaterials.Naquadah)
                .EUt(491520)
                .duration(16000)
                .addData("nano_forge_tier", 1)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("carbon_nanites"))
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Lime)
                .inputItems(TagPrefix.block, GTMaterials.Carbon, 64)
                .inputItems(GTItems.SYSTEM_ON_CHIP.asStack(64))
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(20000))
                .inputFluids(GTMaterials.Lubricant.getFluid(20000))
                .outputItems(GTOTagPrefix.nanites, GTMaterials.Carbon, 64)
                .EUt(491520)
                .duration(4000)
                .addData("nano_forge_tier", 1)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("starmetal_nanites"))
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Red)
                .inputItems(TagPrefix.block, GTOMaterials.Starmetal, 8)
                .inputItems(GTItems.HIGHLY_ADVANCED_SOC.asStack(64))
                .inputItems(GTItems.HIGHLY_ADVANCED_SOC.asStack(64))
                .inputItems(TagPrefix.gemExquisite, GTMaterials.Glass, 64)
                .inputItems(TagPrefix.gemExquisite, GTMaterials.Amethyst, 64)
                .inputFluids(GTMaterials.UUMatter.getFluid(40000))
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(80000))
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(80000))
                .outputItems(GTOTagPrefix.nanites, GTOMaterials.Starmetal)
                .EUt(7864320)
                .duration(8000)
                .addData("nano_forge_tier", 2)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("silver_nanites"))
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightGray)
                .inputItems(TagPrefix.block, GTMaterials.Silver, 8)
                .inputItems(GTItems.SYSTEM_ON_CHIP.asStack(16))
                .inputFluids(GTMaterials.NaquadahEnriched.getFluid(2000))
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(20000))
                .inputFluids(GTMaterials.Bismuth.getFluid(20000))
                .outputItems(GTOTagPrefix.nanites, GTMaterials.Silver)
                .EUt(491520)
                .duration(2000)
                .addData("nano_forge_tier", 1)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("orichalcum_nanites"))
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Magenta)
                .inputItems(TagPrefix.block, GTOMaterials.Orichalcum, 8)
                .inputItems(GTItems.ADVANCED_SYSTEM_ON_CHIP.asStack(64))
                .inputItems(GTItems.ADVANCED_SYSTEM_ON_CHIP.asStack(64))
                .inputFluids(GTMaterials.Neutronium.getFluid(8000))
                .inputFluids(GTOMaterials.UuAmplifier.getFluid(4000))
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(40000))
                .outputItems(GTOTagPrefix.nanites, GTOMaterials.Orichalcum)
                .EUt(491520)
                .duration(32000)
                .addData("nano_forge_tier", 1)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("iridium_nanites"))
                .notConsumable(TagPrefix.lens, GTMaterials.Glass)
                .inputItems(TagPrefix.block, GTMaterials.Iridium, 8)
                .inputItems(GTItems.SYSTEM_ON_CHIP.asStack(32))
                .inputFluids(GTMaterials.Naquadria.getFluid(2000))
                .inputFluids(GTMaterials.Hafnium.getFluid(8000))
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(20000))
                .outputItems(GTOTagPrefix.nanites, GTMaterials.Iridium)
                .EUt(491520)
                .duration(4000)
                .addData("nano_forge_tier", 1)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("black_dwarf_mtter_nanites"))
                .notConsumable(TagPrefix.lens, GTMaterials.Ruby)
                .inputItems(TagPrefix.block, GTOMaterials.BlackDwarfMatter, 8)
                .inputItems(GTOItems.COSMIC_PROCESSING_UNIT_CORE.asStack(8))
                .inputFluids(GTMaterials.UUMatter.getFluid(40000))
                .inputFluids(GTMaterials.Neutronium.getFluid(40000))
                .inputFluids(GTOMaterials.CosmicElement.getFluid(40000))
                .outputItems(GTOTagPrefix.nanites, GTOMaterials.BlackDwarfMatter)
                .EUt(125829120)
                .duration(4000)
                .addData("nano_forge_tier", 3)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("copper_nanites"))
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Brown)
                .inputItems(TagPrefix.block, GTMaterials.Copper, 8)
                .inputItems(GTItems.SYSTEM_ON_CHIP.asStack(8))
                .inputFluids(GTMaterials.Naquadah.getFluid(2000))
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(10000))
                .inputFluids(GTMaterials.Bismuth.getFluid(10000))
                .outputItems(GTOTagPrefix.nanites, GTMaterials.Copper)
                .EUt(491520)
                .duration(2000)
                .addData("nano_forge_tier", 1)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("rhenium_nanites"))
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Cyan)
                .inputItems(TagPrefix.block, GTMaterials.Rhenium, 8)
                .inputItems(GTItems.SYSTEM_ON_CHIP.asStack(64))
                .inputFluids(GTMaterials.Naquadria.getFluid(2000))
                .inputFluids(GTOMaterials.UuAmplifier.getFluid(2000))
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(20000))
                .outputItems(GTOTagPrefix.nanites, GTMaterials.Rhenium)
                .EUt(491520)
                .duration(8000)
                .addData("nano_forge_tier", 1)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("iron_nanites"))
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Gray)
                .inputItems(TagPrefix.block, GTMaterials.Iron, 8)
                .inputItems(GTItems.SYSTEM_ON_CHIP.asStack(8))
                .inputFluids(GTMaterials.Naquadah.getFluid(2000))
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(10000))
                .inputFluids(GTMaterials.Bismuth.getFluid(10000))
                .outputItems(GTOTagPrefix.nanites, GTMaterials.Iron)
                .EUt(491520)
                .duration(2000)
                .addData("nano_forge_tier", 1)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("enderium_nanites"))
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Green)
                .inputItems(TagPrefix.block, GTOMaterials.Enderium, 8)
                .inputItems(GTItems.ADVANCED_SYSTEM_ON_CHIP.asStack(64))
                .inputItems(GTItems.ADVANCED_SYSTEM_ON_CHIP.asStack(64))
                .inputFluids(GTMaterials.Neutronium.getFluid(8000))
                .inputFluids(GTOMaterials.UuAmplifier.getFluid(4000))
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(40000))
                .outputItems(GTOTagPrefix.nanites, GTOMaterials.Enderium)
                .EUt(7864320)
                .duration(2000)
                .addData("nano_forge_tier", 2)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("transcendent_metal_nanites"))
                .notConsumable(GTOItems.NON_LINEAR_OPTICAL_LENS.asStack())
                .notConsumable(GTOItems.HYPERCUBE.asStack())
                .inputItems(GTOTagPrefix.nanites, GTMaterials.Rhenium)
                .inputItems(TagPrefix.block, GTOMaterials.TranscendentMetal, 8)
                .inputItems(GTOItems.RECURSIVELY_FOLDED_NEGATIVE_SPACE.asStack(8))
                .inputItems(CustomTags.MAX_CIRCUITS)
                .inputFluids(GTMaterials.UUMatter.getFluid(80000))
                .inputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 40000))
                .inputFluids(GTOMaterials.SpatialFluid.getFluid(20000))
                .outputItems(GTOTagPrefix.nanites, GTOMaterials.TranscendentMetal)
                .EUt(125829120)
                .duration(16000)
                .addData("nano_forge_tier", 3)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("eternity_nanites"))
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Blue)
                .notConsumable(GTOItems.QUANTUM_ANOMALY.asStack())
                .notConsumable(GTOItems.ETERNITY_CATALYST.asStack())
                .inputItems(GTOTagPrefix.nanites, GTMaterials.Neutronium)
                .inputItems(TagPrefix.block, GTOMaterials.Eternity, 8)
                .inputItems(GTOItems.CLOSED_TIMELIKE_CURVE_COMPUTATIONAL_UNIT.asStack(8))
                .inputFluids(GTOMaterials.SpatialFluid.getFluid(80000))
                .inputFluids(GTOMaterials.ExcitedDtsc.getFluid(80000))
                .inputFluids(GTOMaterials.PrimordialMatter.getFluid(80000))
                .outputItems(GTOTagPrefix.nanites, GTOMaterials.Eternity)
                .EUt(125829120)
                .duration(64000)
                .addData("nano_forge_tier", 3)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("cosmic_neutronium_nanites"))
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Black)
                .inputItems(TagPrefix.block, GTOMaterials.CosmicNeutronium, 8)
                .inputItems(GTOItems.OPTICAL_SOC.asStack(32))
                .inputItems(GTOItems.EXOTIC_WAFER.asStack(32))
                .inputItems(GTOItems.COSMIC_RAM_WAFER.asStack(16))
                .inputItems(GTOItems.COSMIC_PROCESSING_UNIT_CORE.asStack(8))
                .inputFluids(GTMaterials.UUMatter.getFluid(40000))
                .inputFluids(GTOMaterials.CrystalMatrix.getFluid(40000))
                .inputFluids(GTOMaterials.CosmicMesh.getFluid(FluidStorageKeys.LIQUID, 40000))
                .outputItems(GTOTagPrefix.nanites, GTOMaterials.CosmicNeutronium)
                .EUt(125829120)
                .duration(8000)
                .addData("nano_forge_tier", 3)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("vibranium_nanites"))
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightBlue)
                .inputItems(TagPrefix.block, GTOMaterials.Vibranium, 8)
                .inputItems(GTItems.HIGHLY_ADVANCED_SOC.asStack(64))
                .inputItems(GTItems.HIGHLY_ADVANCED_SOC.asStack(64))
                .inputFluids(GTMaterials.UUMatter.getFluid(20000))
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(40000))
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(40000))
                .outputItems(GTOTagPrefix.nanites, GTOMaterials.Vibranium)
                .EUt(7864320)
                .duration(4000)
                .addData("nano_forge_tier", 2)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("white_dwarf_mtter_nanites"))
                .notConsumable(TagPrefix.lens, GTMaterials.Ruby)
                .inputItems(TagPrefix.block, GTOMaterials.WhiteDwarfMatter, 8)
                .inputItems(GTOItems.COSMIC_PROCESSING_UNIT_CORE.asStack(8))
                .inputFluids(GTMaterials.UUMatter.getFluid(40000))
                .inputFluids(GTMaterials.Neutronium.getFluid(40000))
                .inputFluids(GTOMaterials.CosmicElement.getFluid(40000))
                .outputItems(GTOTagPrefix.nanites, GTOMaterials.WhiteDwarfMatter)
                .EUt(125829120)
                .duration(4000)
                .addData("nano_forge_tier", 3)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("uruium_nanites"))
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.LightBlue)
                .inputItems(TagPrefix.block, GTOMaterials.Uruium, 8)
                .inputItems(GTItems.ADVANCED_SYSTEM_ON_CHIP.asStack(64))
                .inputItems(GTItems.HIGHLY_ADVANCED_SOC.asStack(64))
                .inputFluids(GTMaterials.UUMatter.getFluid(20000))
                .inputFluids(GTOMaterials.MutatedLivingSolder.getFluid(40000))
                .inputFluids(GTOMaterials.SuperMutatedLivingSolder.getFluid(40000))
                .outputItems(GTOTagPrefix.nanites, GTOMaterials.Uruium)
                .EUt(7864320)
                .duration(4000)
                .addData("nano_forge_tier", 2)
                .save(provider);

        GTORecipeTypes.NANO_FORGE_RECIPES.recipeBuilder(GTOCore.id("glowstone_nanites"))
                .notConsumable(TagPrefix.lens, MarkerMaterials.Color.Orange)
                .inputItems(TagPrefix.block, GTMaterials.Glowstone, 64)
                .inputItems(GTItems.ADVANCED_SYSTEM_ON_CHIP.asStack(64))
                .inputFluids(GTOMaterials.UuAmplifier.getFluid(10000))
                .inputFluids(GTMaterials.SolderingAlloy.getFluid(20000))
                .inputFluids(GTMaterials.Lubricant.getFluid(20000))
                .outputItems(GTOTagPrefix.nanites, GTMaterials.Glowstone, 64)
                .EUt(491520)
                .duration(16000)
                .addData("nano_forge_tier", 1)
                .save(provider);
    }
}
