package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.ATOMIC_ENERGY_EXCITATION_RECIPES;

final class AtomicEnergyExcitation {

    public static void init() {
        ATOMIC_ENERGY_EXCITATION_RECIPES.recipeBuilder("concentration_mixing_hyper_fuel_1")
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.WhiteDwarfMatter)
                .inputItems(GTOItems.RESONATING_GEM, 4)
                .inputItems(TagPrefix.dust, GTMaterials.Hassium, 16)
                .inputFluids(GTOMaterials.HyperFuel4, 8000)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 6000))
                .inputFluids(GTMaterials.Nickel.getFluid(FluidStorageKeys.PLASMA, 6000))
                .inputFluids(GTMaterials.Oganesson, 1152)
                .inputFluids(GTOMaterials.NaquadriaticTaranium, 2304)
                .inputFluids(GTMaterials.Plutonium241, 864)
                .outputFluids(GTOMaterials.ConcentrationMixingHyperFuel1, 12000)
                .outputFluids(GTOMaterials.HyperFuel4, 1500)
                .EUt(125829120)
                .duration(1200)
                .blastFurnaceTemp(18800)
                .save();

        ATOMIC_ENERGY_EXCITATION_RECIPES.recipeBuilder("concentration_mixing_hyper_fuel_2")
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.BlackDwarfMatter)
                .inputItems(TagPrefix.dust, GTOMaterials.Draconium, 16)
                .inputItems(TagPrefix.dust, GTOMaterials.Starmetal, 18)
                .inputFluids(GTOMaterials.ConcentrationMixingHyperFuel1, 6000)
                .inputFluids(GTOMaterials.CosmicElement, 60000)
                .inputFluids(GTMaterials.Oxygen.getFluid(FluidStorageKeys.PLASMA, 6000))
                .inputFluids(GTMaterials.Argon.getFluid(FluidStorageKeys.PLASMA, 6000))
                .inputFluids(GTMaterials.Iron.getFluid(FluidStorageKeys.PLASMA, 6000))
                .inputFluids(GTMaterials.Nitrogen.getFluid(FluidStorageKeys.PLASMA, 6000))
                .outputFluids(GTOMaterials.ConcentrationMixingHyperFuel2, 12000)
                .outputFluids(GTOMaterials.ConcentrationMixingHyperFuel1, 2000)
                .EUt(503316480)
                .duration(2400)
                .blastFurnaceTemp(21000)
                .save();

        ATOMIC_ENERGY_EXCITATION_RECIPES.recipeBuilder("hyper_fuel_1")
                .inputItems(TagPrefix.dust, GTMaterials.Naquadria, 14)
                .inputItems(TagPrefix.dust, GTMaterials.NaquadahEnriched, 20)
                .inputItems(TagPrefix.dust, GTMaterials.Naquadah, 40)
                .inputFluids(GTMaterials.Hydrogen, 100000)
                .inputFluids(GTMaterials.Nitrogen, 76000)
                .inputFluids(GTMaterials.Fluorine, 10000)
                .inputFluids(GTMaterials.Radon, 2000)
                .inputFluids(GTMaterials.Xenon, 4000)
                .inputFluids(GTMaterials.Thorium, 4608)
                .outputFluids(GTOMaterials.HyperFuel1, 24000)
                .EUt(1966080)
                .duration(3800)
                .blastFurnaceTemp(13200)
                .save();

        ATOMIC_ENERGY_EXCITATION_RECIPES.recipeBuilder("hyper_fuel_2")
                .inputItems(TagPrefix.dust, GTMaterials.Dubnium, 4)
                .inputItems(TagPrefix.dust, GTMaterials.Fermium, 6)
                .inputFluids(GTOMaterials.HyperFuel1, 6000)
                .inputFluids(GTMaterials.Radon, 40000)
                .inputFluids(GTMaterials.Xenon, 32000)
                .inputFluids(GTMaterials.Thorium, 3456)
                .inputFluids(GTMaterials.Naquadria, 864)
                .inputFluids(GTMaterials.Uranium235, 2304)
                .outputFluids(GTOMaterials.HyperFuel2, 9600)
                .outputFluids(GTOMaterials.HyperFuel1, 500)
                .EUt(7864320)
                .duration(4000)
                .blastFurnaceTemp(14000)
                .save();

        ATOMIC_ENERGY_EXCITATION_RECIPES.recipeBuilder("hyper_fuel_3")
                .inputItems(TagPrefix.dust, GTMaterials.Lawrencium, 6)
                .inputItems(TagPrefix.dust, GTOMaterials.Adamantine, 8)
                .inputFluids(GTOMaterials.HyperFuel2, 6000)
                .inputFluids(GTMaterials.Naquadria, 864)
                .inputFluids(GTMaterials.Thorium, 1728)
                .inputFluids(GTMaterials.Fermium, 5184)
                .inputFluids(GTMaterials.Uranium235, 2304)
                .inputFluids(GTMaterials.Plutonium241, 4608)
                .outputFluids(GTOMaterials.HyperFuel3, 12000)
                .outputFluids(GTOMaterials.HyperFuel2, 750)
                .EUt(31457280)
                .duration(4000)
                .blastFurnaceTemp(15200)
                .save();

        ATOMIC_ENERGY_EXCITATION_RECIPES.recipeBuilder("hyper_fuel_4")
                .inputItems(TagPrefix.dust, GTMaterials.Neutronium, 8)
                .inputItems(TagPrefix.dust, GTOMaterials.Taranium, 12)
                .inputFluids(GTOMaterials.HyperFuel3, 6000)
                .inputFluids(GTMaterials.Nobelium, 8000)
                .inputFluids(GTMaterials.Thorium, 1728)
                .inputFluids(GTMaterials.Fermium, 2340)
                .inputFluids(GTMaterials.Uranium235, 2304)
                .inputFluids(GTMaterials.Plutonium241, 5184)
                .outputFluids(GTOMaterials.HyperFuel4, 16000)
                .outputFluids(GTOMaterials.HyperFuel3, 1000)
                .EUt(31457280)
                .duration(4200)
                .blastFurnaceTemp(18000)
                .save();

        ATOMIC_ENERGY_EXCITATION_RECIPES.recipeBuilder("enriched_naquadah_fuel")
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.Vibranium)
                .inputItems(TagPrefix.dust, GTMaterials.NaquadahEnriched, 16)
                .inputItems(TagPrefix.dust, GTMaterials.Antimony, 4)
                .notConsumableFluid(GTOMaterials.CaesiumFluoride.getFluid(1000))
                .inputFluids(GTMaterials.Hydrogen, 48000)
                .inputFluids(GTMaterials.Nitrogen, 30000)
                .inputFluids(GTMaterials.Fluorine, 12000)
                .inputFluids(GTMaterials.Xenon, 8000)
                .inputFluids(GTMaterials.Radon, 6000)
                .outputFluids(GTOMaterials.EnrichedNaquadahFuel, 20000)
                .EUt(491520)
                .duration(4000)
                .blastFurnaceTemp(12500)
                .save();

        ATOMIC_ENERGY_EXCITATION_RECIPES.recipeBuilder("naquadah_fuel")
                .notConsumable(GTOTagPrefix.NANITES, GTOMaterials.Orichalcum)
                .inputItems(TagPrefix.dust, GTMaterials.Naquadah, 16)
                .inputFluids(GTMaterials.Hydrogen, 30000)
                .inputFluids(GTMaterials.Nitrogen, 15000)
                .inputFluids(GTMaterials.Fluorine, 8000)
                .inputFluids(GTMaterials.NitricAcid, 8000)
                .outputFluids(GTOMaterials.NaquadahFuel, 20000)
                .EUt(122880)
                .duration(4000)
                .blastFurnaceTemp(12000)
                .save();
    }
}
