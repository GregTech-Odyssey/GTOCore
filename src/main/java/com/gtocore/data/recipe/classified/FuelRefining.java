package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.FluidStack;

import earth.terrarium.adastra.common.registry.ModFluids;

import static com.gtocore.common.data.GTORecipeTypes.FUEL_REFINING_RECIPES;

final class FuelRefining {

    public static void init() {
        FUEL_REFINING_RECIPES.recipeBuilder("stellar_energy_rocket_fuel")
                .inputItems(Items.FIRE_CHARGE.asItem(), 64)
                .inputItems(TagPrefix.dust, GTOMaterials.HmxExplosive, 8)
                .inputItems(TagPrefix.dust, GTMaterials.NaquadahEnriched, 4)
                .inputFluids(GTMaterials.HydrogenPeroxide, 8000)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3, 8000)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture, 12000)
                .inputFluids(GTOMaterials.RocketFuelRp1, 4000)
                .inputFluids(GTMaterials.NitrationMixture, 4000)
                .inputFluids(GTMaterials.Benzene, 4000)
                .outputFluids(GTOMaterials.StellarEnergyRocketFuel, 40000)
                .EUt(7680)
                .duration(7200)
                .blastFurnaceTemp(14400)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("cryo_fuel")
                .inputItems("ad_astra:ice_shard", 8)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 60)
                .inputFluids(GTMaterials.Hydrogen, 42000)
                .inputFluids(GTMaterials.Oxygen, 20000)
                .inputFluids(GTMaterials.Nitrogen, 8000)
                .inputFluids(GTMaterials.HeavyFuel, 1000)
                .inputFluids(GTMaterials.LightFuel, 4000)
                .outputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 8000))
                .EUt(30720)
                .duration(6400)
                .blastFurnaceTemp(11800)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("dense_hydrazine_fuel_mixture")
                .circuitMeta(3)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 32)
                .inputFluids(GTMaterials.Hydrogen, 12000)
                .inputFluids(GTMaterials.Oxygen, 8000)
                .inputFluids(GTMaterials.Nitrogen, 10000)
                .inputFluids(GTMaterials.HydrogenPeroxide, 4000)
                .outputFluids(GTOMaterials.DenseHydrazineFuelMixture, 8000)
                .EUt(1920)
                .duration(800)
                .blastFurnaceTemp(7400)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("high_octane_gasoline")
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 44)
                .inputFluids(GTMaterials.Oxygen, 12000)
                .inputFluids(GTMaterials.Nitrogen, 8000)
                .inputFluids(GTMaterials.Naphtha, 16000)
                .inputFluids(GTMaterials.RefineryGas, 2000)
                .inputFluids(GTMaterials.Toluene, 4000)
                .inputFluids(GTMaterials.Octane, 3000)
                .outputFluids(GTMaterials.HighOctaneGasoline, 50000)
                .EUt(7680)
                .duration(1200)
                .blastFurnaceTemp(6800)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("rocket_fuel")
                .circuitMeta(1)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 8)
                .inputFluids(GTMaterials.Hydrogen, 32000)
                .inputFluids(GTMaterials.Oxygen, 14000)
                .inputFluids(GTMaterials.Nitrogen, 12000)
                .inputFluids(GTMaterials.Chlorine, 10000)
                .outputFluids(GTMaterials.RocketFuel, 36000)
                .EUt(1920)
                .duration(1600)
                .blastFurnaceTemp(5200)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("cetane_boosted_diesel_b")
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 12)
                .inputFluids(GTMaterials.Hydrogen, 10000)
                .inputFluids(GTMaterials.Oxygen, 5000)
                .inputFluids(GTMaterials.BioDiesel, 16000)
                .inputFluids(GTMaterials.NitrationMixture, 4000)
                .inputFluids(GTOMaterials.Cetane, 400)
                .outputFluids(GTMaterials.CetaneBoostedDiesel, 14000)
                .EUt(1920)
                .duration(600)
                .blastFurnaceTemp(3600)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("rocket_fuel_h8n4c2o4")
                .circuitMeta(2)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 18)
                .inputFluids(GTMaterials.Hydrogen, 30000)
                .inputFluids(GTMaterials.Nitrogen, 18000)
                .inputFluids(GTMaterials.Oxygen, 24000)
                .inputFluids(GTMaterials.Chlorine, 10000)
                .outputFluids(GTOMaterials.RocketFuelH8n4c2o4, 12000)
                .EUt(7680)
                .duration(2000)
                .blastFurnaceTemp(9000)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("rocket_fuel_rp_1")
                .circuitMeta(2)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 64)
                .inputFluids(GTMaterials.CoalGas, 80000)
                .inputFluids(GTMaterials.Oxygen, 10000)
                .outputFluids(GTOMaterials.RocketFuelRp1, 4000)
                .EUt(1920)
                .duration(1200)
                .blastFurnaceTemp(6300)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("cetane_boosted_diesel")
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 16)
                .inputFluids(GTMaterials.Hydrogen, 10000)
                .inputFluids(GTMaterials.Oxygen, 5000)
                .inputFluids(GTMaterials.LightFuel, 10000)
                .inputFluids(GTMaterials.HeavyFuel, 2000)
                .inputFluids(GTMaterials.NitrationMixture, 4000)
                .inputFluids(GTOMaterials.Cetane, 400)
                .outputFluids(GTMaterials.CetaneBoostedDiesel, 18000)
                .EUt(1920)
                .duration(400)
                .blastFurnaceTemp(4800)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("concentration_mixing_hyper_fuel_2")
                .inputItems(TagPrefix.dust, GTOMaterials.Starmetal)
                .inputItems(TagPrefix.dust, GTOMaterials.Draconium)
                .inputFluids(GTOMaterials.ConcentrationMixingHyperFuel1, 1000)
                .inputFluids(GTOMaterials.DimensionallyTranscendentProsaicCatalyst, 1000)
                .outputFluids(GTOMaterials.ConcentrationMixingHyperFuel2, 1000)
                .EUt(536870912)
                .duration(800)
                .blastFurnaceTemp(18800)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("hyper_fuel_1")
                .inputFluids(GTMaterials.NaquadriaSolution, 1000)
                .inputFluids(GTOMaterials.NaquadahFuel, 1000)
                .inputFluids(GTOMaterials.EnrichedNaquadahFuel, 1000)
                .inputFluids(GTMaterials.Thorium, 2304)
                .outputFluids(GTOMaterials.HyperFuel1, 3000)
                .EUt(491520)
                .duration(400)
                .blastFurnaceTemp(12400)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("rocket_fuel_cn3h7o3")
                .circuitMeta(4)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 12)
                .inputFluids(GTMaterials.Hydrogen, 14000)
                .inputFluids(GTMaterials.Nitrogen, 6000)
                .inputFluids(GTMaterials.NitricAcid, 3000)
                .inputFluids(GTMaterials.HydrogenPeroxide, 2000)
                .outputFluids(GTOMaterials.RocketFuelCn3h7o3, 4000)
                .EUt(7680)
                .duration(1200)
                .blastFurnaceTemp(8200)
                .save();
    }
}
