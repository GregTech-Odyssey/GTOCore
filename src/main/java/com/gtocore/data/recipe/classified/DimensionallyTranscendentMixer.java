package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES;

final class DimensionallyTranscendentMixer {

    public static void init() {
        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("transition_1_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Titanium)
                .inputItems(TagPrefix.dust, GTMaterials.Vanadium)
                .inputItems(TagPrefix.dust, GTMaterials.Chromium)
                .inputItems(TagPrefix.dust, GTMaterials.Manganese)
                .inputItems(TagPrefix.dust, GTMaterials.Iron)
                .inputItems(TagPrefix.dust, GTMaterials.Cobalt)
                .inputItems(TagPrefix.dust, GTMaterials.Nickel)
                .inputItems(TagPrefix.dust, GTMaterials.Copper)
                .inputItems(TagPrefix.dust, GTMaterials.Zinc)
                .outputItems(TagPrefix.dust, GTOMaterials.TransitionMix1, 9)
                .EUt(31457280)
                .duration(1800)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("alkaline_earth_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Beryllium)
                .inputItems(TagPrefix.dust, GTMaterials.Magnesium)
                .inputItems(TagPrefix.dust, GTMaterials.Calcium)
                .inputItems(TagPrefix.dust, GTMaterials.Strontium)
                .inputItems(TagPrefix.dust, GTMaterials.Barium)
                .inputItems(TagPrefix.dust, GTMaterials.Radium)
                .outputItems(TagPrefix.dust, GTOMaterials.AlkalineEarthMix, 6)
                .EUt(31457280)
                .duration(1200)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("alkaline_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Lithium)
                .inputItems(TagPrefix.dust, GTMaterials.Sodium)
                .inputItems(TagPrefix.dust, GTMaterials.Potassium)
                .inputItems(TagPrefix.dust, GTMaterials.Rubidium)
                .inputItems(TagPrefix.dust, GTMaterials.Caesium)
                .inputItems(TagPrefix.dust, GTMaterials.Francium)
                .outputItems(TagPrefix.dust, GTOMaterials.AlkalineMix, 6)
                .EUt(31457280)
                .duration(1200)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("dimensionallytranscendentprosaiccatalyst")
                .inputFluids(GTOMaterials.DimensionallyTranscendentCrudeCatalyst.getFluid(1000))
                .inputFluids(GTMaterials.Sulfur.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Oxygen.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Argon.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Iron.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Nickel.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentProsaicCatalyst.getFluid(1000))
                .EUt(503316480)
                .duration(400)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("dimensionallytranscendentstellarcatalyst")
                .inputFluids(GTOMaterials.DimensionallyTranscendentExoticCatalyst.getFluid(1000))
                .inputFluids(GTMaterials.Tin.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Thorium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTOMaterials.CrystalMatrix.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTOMaterials.AwakenedDraconium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentStellarCatalyst.getFluid(1000))
                .EUt(503316480)
                .duration(3200)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("actinoids_1_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Actinium)
                .inputItems(TagPrefix.dust, GTMaterials.Thorium)
                .inputItems(TagPrefix.dust, GTMaterials.Protactinium)
                .inputItems(TagPrefix.dust, GTMaterials.Uranium238)
                .inputItems(TagPrefix.dust, GTMaterials.Neptunium)
                .inputItems(TagPrefix.dust, GTMaterials.Plutonium239)
                .inputItems(TagPrefix.dust, GTMaterials.Americium)
                .inputItems(TagPrefix.dust, GTMaterials.Curium)
                .outputItems(TagPrefix.dust, GTOMaterials.ActinoidsMix1, 8)
                .EUt(31457280)
                .duration(1600)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("metalloid_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Boron)
                .inputItems(TagPrefix.dust, GTMaterials.Silicon)
                .inputItems(TagPrefix.dust, GTMaterials.Germanium)
                .inputItems(TagPrefix.dust, GTMaterials.Arsenic)
                .inputItems(TagPrefix.dust, GTMaterials.Antimony)
                .inputItems(TagPrefix.dust, GTMaterials.Tellurium)
                .inputItems(TagPrefix.dust, GTMaterials.Astatine)
                .outputItems(TagPrefix.dust, GTOMaterials.MetalloidMix, 7)
                .EUt(31457280)
                .duration(1400)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("chaos_plasma")
                .inputItems(GTOItems.CHAOS_SHARD.asItem())
                .inputFluids(GTMaterials.UUMatter.getFluid(4000))
                .inputFluids(GTOMaterials.CosmicNeutronium.getFluid(2000))
                .inputFluids(GTOMaterials.CosmicMesh.getFluid(FluidStorageKeys.PLASMA, 2000))
                .inputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTOMaterials.DimensionallyTranscendentExoticCatalyst.getFluid(1000))
                .outputFluids(GTOMaterials.Chaos.getFluid(FluidStorageKeys.PLASMA, 10000))
                .EUt(2013265920)
                .duration(1200)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("actinoids_2_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Berkelium)
                .inputItems(TagPrefix.dust, GTMaterials.Californium)
                .inputItems(TagPrefix.dust, GTMaterials.Einsteinium)
                .inputItems(TagPrefix.dust, GTMaterials.Fermium)
                .inputItems(TagPrefix.dust, GTMaterials.Mendelevium)
                .inputItems(TagPrefix.dust, GTMaterials.Nobelium)
                .inputItems(TagPrefix.dust, GTMaterials.Lawrencium)
                .outputItems(TagPrefix.dust, GTOMaterials.ActinoidsMix2, 7)
                .EUt(31457280)
                .duration(1400)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("dimensionallytranscendentexoticcatalyst")
                .inputFluids(GTOMaterials.DimensionallyTranscendentResplendentCatalyst.getFluid(1000))
                .inputFluids(GTMaterials.Niobium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTOMaterials.Echoite.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Lead.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTOMaterials.Vibranium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTOMaterials.Starmetal.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentExoticCatalyst.getFluid(1000))
                .EUt(503316480)
                .duration(1600)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("lanthanoids_2_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Terbium)
                .inputItems(TagPrefix.dust, GTMaterials.Dysprosium)
                .inputItems(TagPrefix.dust, GTMaterials.Holmium)
                .inputItems(TagPrefix.dust, GTMaterials.Erbium)
                .inputItems(TagPrefix.dust, GTMaterials.Thulium)
                .inputItems(TagPrefix.dust, GTMaterials.Ytterbium)
                .inputItems(TagPrefix.dust, GTMaterials.Lutetium)
                .outputItems(TagPrefix.dust, GTOMaterials.LanthanoidsMix2, 7)
                .EUt(31457280)
                .duration(1400)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("not_found")
                .inputItems(TagPrefix.dust, GTMaterials.Carbon)
                .inputItems(TagPrefix.dust, GTMaterials.Phosphorus)
                .inputItems(TagPrefix.dust, GTMaterials.Sulfur)
                .inputItems(TagPrefix.dust, GTMaterials.Selenium)
                .inputItems(TagPrefix.dust, GTMaterials.Iodine)
                .inputFluids(GTMaterials.Hydrogen.getFluid(1000))
                .inputFluids(GTMaterials.Nitrogen.getFluid(1000))
                .inputFluids(GTMaterials.Oxygen.getFluid(1000))
                .inputFluids(GTMaterials.Fluorine.getFluid(1000))
                .inputFluids(GTMaterials.Chlorine.getFluid(1000))
                .inputFluids(GTMaterials.Bromine.getFluid(1000))
                .outputFluids(GTOMaterials.NotFoundMix.getFluid(11000))
                .EUt(31457280)
                .duration(2200)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("poor_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Aluminium)
                .inputItems(TagPrefix.dust, GTMaterials.Gallium)
                .inputItems(TagPrefix.dust, GTMaterials.Indium)
                .inputItems(TagPrefix.dust, GTMaterials.Tin)
                .inputItems(TagPrefix.dust, GTMaterials.Thallium)
                .inputItems(TagPrefix.dust, GTMaterials.Lead)
                .inputItems(TagPrefix.dust, GTMaterials.Bismuth)
                .inputItems(TagPrefix.dust, GTMaterials.Polonium)
                .outputItems(TagPrefix.dust, GTOMaterials.PoorMix, 8)
                .EUt(31457280)
                .duration(1600)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("dimensionallytranscendentresplendentcatalyst")
                .inputFluids(GTOMaterials.DimensionallyTranscendentProsaicCatalyst.getFluid(1000))
                .inputFluids(GTMaterials.Zinc.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTOMaterials.Mithril.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTOMaterials.Orichalcum.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTOMaterials.Enderium.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTMaterials.Neon.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputFluids(GTOMaterials.DimensionallyTranscendentResplendentCatalyst.getFluid(1000))
                .EUt(503316480)
                .duration(800)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("transition_3_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Hafnium)
                .inputItems(TagPrefix.dust, GTMaterials.Tantalum)
                .inputItems(TagPrefix.dust, GTMaterials.Tungsten)
                .inputItems(TagPrefix.dust, GTMaterials.Rhenium)
                .inputItems(TagPrefix.dust, GTMaterials.Osmium)
                .inputItems(TagPrefix.dust, GTMaterials.Iridium)
                .inputItems(TagPrefix.dust, GTMaterials.Platinum)
                .inputItems(TagPrefix.dust, GTMaterials.Gold)
                .inputFluids(GTMaterials.Mercury.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.TransitionMix3, 9)
                .EUt(31457280)
                .duration(1800)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("exciteddtsc")
                .inputFluids(GTOMaterials.DimensionallyTranscendentStellarCatalyst.getFluid(10000))
                .inputFluids(GTOMaterials.ConcentrationMixingHyperFuel2.getFluid(1000))
                .inputFluids(GTOMaterials.HighEnergyQuarkGluon.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputFluids(GTOMaterials.ExcitedDtsc.getFluid(10000))
                .EUt(503316480)
                .duration(12000)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("transition_2_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Zirconium)
                .inputItems(TagPrefix.dust, GTMaterials.Niobium)
                .inputItems(TagPrefix.dust, GTMaterials.Molybdenum)
                .inputItems(TagPrefix.dust, GTMaterials.Technetium)
                .inputItems(TagPrefix.dust, GTMaterials.Ruthenium)
                .inputItems(TagPrefix.dust, GTMaterials.Rhodium)
                .inputItems(TagPrefix.dust, GTMaterials.Palladium)
                .inputItems(TagPrefix.dust, GTMaterials.Silver)
                .inputItems(TagPrefix.dust, GTMaterials.Cadmium)
                .outputItems(TagPrefix.dust, GTOMaterials.TransitionMix2, 9)
                .EUt(31457280)
                .duration(1800)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("lanthanoids_1_dust")
                .inputItems(TagPrefix.dust, GTMaterials.Lanthanum)
                .inputItems(TagPrefix.dust, GTMaterials.Cerium)
                .inputItems(TagPrefix.dust, GTMaterials.Praseodymium)
                .inputItems(TagPrefix.dust, GTMaterials.Neodymium)
                .inputItems(TagPrefix.dust, GTMaterials.Promethium)
                .inputItems(TagPrefix.dust, GTMaterials.Samarium)
                .inputItems(TagPrefix.dust, GTMaterials.Europium)
                .inputItems(TagPrefix.dust, GTMaterials.Gadolinium)
                .outputItems(TagPrefix.dust, GTOMaterials.LanthanoidsMix1, 8)
                .EUt(31457280)
                .duration(1600)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("primordialmatter")
                .inputFluids(GTOMaterials.RawStarMatter.getFluid(FluidStorageKeys.PLASMA, 1000))
                .inputFluids(GTOMaterials.SpaceTime.getFluid(1000))
                .inputFluids(GTOMaterials.SpatialFluid.getFluid(1000))
                .inputFluids(GTOMaterials.DimensionallyTranscendentResidue.getFluid(1000))
                .outputFluids(GTOMaterials.PrimordialMatter.getFluid(1000))
                .EUt(2013265920)
                .duration(1200)
                .save();

        DIMENSIONALLY_TRANSCENDENT_SHOCK_RECIPES.recipeBuilder("noble_gas")
                .inputFluids(GTMaterials.Helium.getFluid(1000))
                .inputFluids(GTMaterials.Neon.getFluid(1000))
                .inputFluids(GTMaterials.Argon.getFluid(1000))
                .inputFluids(GTMaterials.Krypton.getFluid(1000))
                .inputFluids(GTMaterials.Xenon.getFluid(1000))
                .inputFluids(GTMaterials.Radon.getFluid(1000))
                .outputFluids(GTOMaterials.NobleGasMix.getFluid(6000))
                .EUt(31457280)
                .duration(1200)
                .save();
    }
}
