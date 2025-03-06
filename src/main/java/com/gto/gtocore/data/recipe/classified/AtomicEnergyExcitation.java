package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.ATOMIC_ENERGY_EXCITATION_RECIPES;

interface AtomicEnergyExcitation {

    static void init() {
        ATOMIC_ENERGY_EXCITATION_RECIPES.recipeBuilder(GTOCore.id("concentration_mixing_hyper_fuel_1"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.WhiteDwarfMatter)
                .inputItems(GTOItems.RESONATING_GEM.asStack(4))
                .inputItems(TagPrefix.dust, GTMaterials.Hassium, 16)
                .inputFluids(GTOMaterials.HyperFuel4.getFluid(8000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 6000))
                .inputFluids(GTMaterials.Nickel.getFluid(FluidStorageKeys.PLASMA, 6000))
                .inputFluids(GTMaterials.Oganesson.getFluid(1152))
                .inputFluids(GTOMaterials.NaquadriaticTaranium.getFluid(2304))
                .inputFluids(GTMaterials.Plutonium241.getFluid(864))
                .outputFluids(GTOMaterials.ConcentrationMixingHyperFuel1.getFluid(12000))
                .outputFluids(GTOMaterials.HyperFuel4.getFluid(1500))
                .EUt(125829120)
                .duration(1200)
                .blastFurnaceTemp(18800)
                .save();

        ATOMIC_ENERGY_EXCITATION_RECIPES.recipeBuilder(GTOCore.id("concentration_mixing_hyper_fuel_2"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.BlackDwarfMatter)
                .inputItems(TagPrefix.dust, GTOMaterials.Draconium, 16)
                .inputItems(TagPrefix.dust, GTOMaterials.Starmetal, 18)
                .inputFluids(GTOMaterials.ConcentrationMixingHyperFuel1.getFluid(6000))
                .inputFluids(GTOMaterials.CosmicElement.getFluid(60000))
                .inputFluids(GTMaterials.Oxygen.getFluid(FluidStorageKeys.PLASMA, 6000))
                .inputFluids(GTMaterials.Argon.getFluid(FluidStorageKeys.PLASMA, 6000))
                .inputFluids(GTMaterials.Iron.getFluid(FluidStorageKeys.PLASMA, 6000))
                .inputFluids(GTMaterials.Nitrogen.getFluid(FluidStorageKeys.PLASMA, 6000))
                .outputFluids(GTOMaterials.ConcentrationMixingHyperFuel2.getFluid(12000))
                .outputFluids(GTOMaterials.ConcentrationMixingHyperFuel1.getFluid(2000))
                .EUt(503316480)
                .duration(2400)
                .blastFurnaceTemp(21000)
                .save();

        ATOMIC_ENERGY_EXCITATION_RECIPES.recipeBuilder(GTOCore.id("hyper_fuel_1"))
                .inputItems(TagPrefix.dust, GTMaterials.Naquadria, 14)
                .inputItems(TagPrefix.dust, GTMaterials.NaquadahEnriched, 20)
                .inputItems(TagPrefix.dust, GTMaterials.Naquadah, 40)
                .inputFluids(GTMaterials.Hydrogen.getFluid(100000))
                .inputFluids(GTMaterials.Nitrogen.getFluid(76000))
                .inputFluids(GTMaterials.Fluorine.getFluid(10000))
                .inputFluids(GTMaterials.Radon.getFluid(2000))
                .inputFluids(GTMaterials.Xenon.getFluid(4000))
                .inputFluids(GTMaterials.Thorium.getFluid(4608))
                .outputFluids(GTOMaterials.HyperFuel1.getFluid(24000))
                .EUt(1966080)
                .duration(3800)
                .blastFurnaceTemp(13200)
                .save();

        ATOMIC_ENERGY_EXCITATION_RECIPES.recipeBuilder(GTOCore.id("hyper_fuel_2"))
                .inputItems(TagPrefix.dust, GTMaterials.Dubnium, 4)
                .inputItems(TagPrefix.dust, GTMaterials.Fermium, 6)
                .inputFluids(GTOMaterials.HyperFuel1.getFluid(6000))
                .inputFluids(GTMaterials.Radon.getFluid(40000))
                .inputFluids(GTMaterials.Xenon.getFluid(32000))
                .inputFluids(GTMaterials.Thorium.getFluid(3456))
                .inputFluids(GTMaterials.Naquadria.getFluid(864))
                .inputFluids(GTMaterials.Uranium235.getFluid(2304))
                .outputFluids(GTOMaterials.HyperFuel2.getFluid(9600))
                .outputFluids(GTOMaterials.HyperFuel1.getFluid(500))
                .EUt(7864320)
                .duration(4000)
                .blastFurnaceTemp(14000)
                .save();

        ATOMIC_ENERGY_EXCITATION_RECIPES.recipeBuilder(GTOCore.id("hyper_fuel_3"))
                .inputItems(TagPrefix.dust, GTMaterials.Lawrencium, 6)
                .inputItems(TagPrefix.dust, GTOMaterials.Adamantine, 8)
                .inputFluids(GTOMaterials.HyperFuel2.getFluid(6000))
                .inputFluids(GTMaterials.Naquadria.getFluid(864))
                .inputFluids(GTMaterials.Thorium.getFluid(1728))
                .inputFluids(GTMaterials.Fermium.getFluid(5184))
                .inputFluids(GTMaterials.Uranium235.getFluid(2304))
                .inputFluids(GTMaterials.Plutonium241.getFluid(4608))
                .outputFluids(GTOMaterials.HyperFuel3.getFluid(12000))
                .outputFluids(GTOMaterials.HyperFuel2.getFluid(750))
                .EUt(31457280)
                .duration(4000)
                .blastFurnaceTemp(15200)
                .save();

        ATOMIC_ENERGY_EXCITATION_RECIPES.recipeBuilder(GTOCore.id("hyper_fuel_4"))
                .inputItems(TagPrefix.dust, GTMaterials.Neutronium, 8)
                .inputItems(TagPrefix.dust, GTOMaterials.Taranium, 12)
                .inputFluids(GTOMaterials.HyperFuel3.getFluid(6000))
                .inputFluids(GTMaterials.Nobelium.getFluid(8000))
                .inputFluids(GTMaterials.Thorium.getFluid(1728))
                .inputFluids(GTMaterials.Fermium.getFluid(2340))
                .inputFluids(GTMaterials.Uranium235.getFluid(2304))
                .inputFluids(GTMaterials.Plutonium241.getFluid(5184))
                .outputFluids(GTOMaterials.HyperFuel4.getFluid(16000))
                .outputFluids(GTOMaterials.HyperFuel3.getFluid(1000))
                .EUt(31457280)
                .duration(4200)
                .blastFurnaceTemp(18000)
                .save();

        ATOMIC_ENERGY_EXCITATION_RECIPES.recipeBuilder(GTOCore.id("enriched_naquadah_fuel"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.Vibranium)
                .inputItems(TagPrefix.dust, GTMaterials.NaquadahEnriched, 16)
                .inputItems(TagPrefix.dust, GTMaterials.Antimony, 4)
                .notConsumableFluid(GTOMaterials.CaesiumFluoride.getFluid(1000))
                .inputFluids(GTMaterials.Hydrogen.getFluid(48000))
                .inputFluids(GTMaterials.Nitrogen.getFluid(30000))
                .inputFluids(GTMaterials.Fluorine.getFluid(12000))
                .inputFluids(GTMaterials.Xenon.getFluid(8000))
                .inputFluids(GTMaterials.Radon.getFluid(6000))
                .outputFluids(GTOMaterials.EnrichedNaquadahFuel.getFluid(20000))
                .EUt(491520)
                .duration(4000)
                .blastFurnaceTemp(12500)
                .save();

        ATOMIC_ENERGY_EXCITATION_RECIPES.recipeBuilder(GTOCore.id("naquadah_fuel"))
                .notConsumable(GTOTagPrefix.nanites, GTOMaterials.Orichalcum)
                .inputItems(TagPrefix.dust, GTMaterials.Naquadah, 16)
                .inputFluids(GTMaterials.Hydrogen.getFluid(30000))
                .inputFluids(GTMaterials.Nitrogen.getFluid(15000))
                .inputFluids(GTMaterials.Fluorine.getFluid(8000))
                .inputFluids(GTMaterials.NitricAcid.getFluid(8000))
                .outputFluids(GTOMaterials.NaquadahFuel.getFluid(20000))
                .EUt(122880)
                .duration(4000)
                .blastFurnaceTemp(12000)
                .save();
    }
}
