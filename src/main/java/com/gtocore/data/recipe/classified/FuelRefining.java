package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.FluidStack;

import earth.terrarium.adastra.common.registry.ModFluids;

import static com.gtocore.common.data.GTORecipeTypes.FUEL_REFINING_RECIPES;

final class FuelRefining {

    public static void init() {
        FUEL_REFINING_RECIPES.recipeBuilder("stellar_energy_rocket_fuel")
                .inputItems(new ItemStack(Items.FIRE_CHARGE.asItem(), 64))
                .inputItems(TagPrefix.dust, GTOMaterials.HmxExplosive, 8)
                .inputItems(TagPrefix.dust, GTMaterials.NaquadahEnriched, 4)
                .inputFluids(GTMaterials.HydrogenPeroxide.getFluid(8000))
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(8000))
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(12000))
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(4000))
                .inputFluids(GTMaterials.NitrationMixture.getFluid(4000))
                .inputFluids(GTMaterials.Benzene.getFluid(4000))
                .outputFluids(GTOMaterials.StellarEnergyRocketFuel.getFluid(40000))
                .EUt(7680)
                .duration(7200)
                .blastFurnaceTemp(14400)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("cryo_fuel")
                .inputItems("ad_astra:ice_shard", 8)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 60)
                .inputFluids(GTMaterials.Hydrogen.getFluid(42000))
                .inputFluids(GTMaterials.Oxygen.getFluid(20000))
                .inputFluids(GTMaterials.Nitrogen.getFluid(8000))
                .inputFluids(GTMaterials.HeavyFuel.getFluid(1000))
                .inputFluids(GTMaterials.LightFuel.getFluid(4000))
                .outputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 8000))
                .EUt(30720)
                .duration(6400)
                .blastFurnaceTemp(11800)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("dense_hydrazine_fuel_mixture")
                .circuitMeta(3)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 32)
                .inputFluids(GTMaterials.Hydrogen.getFluid(12000))
                .inputFluids(GTMaterials.Oxygen.getFluid(8000))
                .inputFluids(GTMaterials.Nitrogen.getFluid(10000))
                .inputFluids(GTMaterials.HydrogenPeroxide.getFluid(4000))
                .outputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(8000))
                .EUt(1920)
                .duration(800)
                .blastFurnaceTemp(7400)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("high_octane_gasoline")
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 44)
                .inputFluids(GTMaterials.Oxygen.getFluid(12000))
                .inputFluids(GTMaterials.Nitrogen.getFluid(8000))
                .inputFluids(GTMaterials.Naphtha.getFluid(16000))
                .inputFluids(GTMaterials.RefineryGas.getFluid(2000))
                .inputFluids(GTMaterials.Toluene.getFluid(4000))
                .inputFluids(GTMaterials.Octane.getFluid(3000))
                .outputFluids(GTMaterials.HighOctaneGasoline.getFluid(50000))
                .EUt(7680)
                .duration(1200)
                .blastFurnaceTemp(6800)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("rocket_fuel")
                .circuitMeta(1)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 8)
                .inputFluids(GTMaterials.Hydrogen.getFluid(32000))
                .inputFluids(GTMaterials.Oxygen.getFluid(14000))
                .inputFluids(GTMaterials.Nitrogen.getFluid(12000))
                .inputFluids(GTMaterials.Chlorine.getFluid(10000))
                .outputFluids(GTMaterials.RocketFuel.getFluid(36000))
                .EUt(1920)
                .duration(1600)
                .blastFurnaceTemp(5200)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("cetane_boosted_diesel_b")
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 12)
                .inputFluids(GTMaterials.Hydrogen.getFluid(10000))
                .inputFluids(GTMaterials.Oxygen.getFluid(5000))
                .inputFluids(GTMaterials.BioDiesel.getFluid(16000))
                .inputFluids(GTMaterials.NitrationMixture.getFluid(4000))
                .inputFluids(GTOMaterials.Cetane.getFluid(400))
                .outputFluids(GTMaterials.CetaneBoostedDiesel.getFluid(14000))
                .EUt(1920)
                .duration(600)
                .blastFurnaceTemp(3600)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("rocket_fuel_h8n4c2o4")
                .circuitMeta(2)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 18)
                .inputFluids(GTMaterials.Hydrogen.getFluid(30000))
                .inputFluids(GTMaterials.Nitrogen.getFluid(18000))
                .inputFluids(GTMaterials.Oxygen.getFluid(24000))
                .inputFluids(GTMaterials.Chlorine.getFluid(10000))
                .outputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(12000))
                .EUt(7680)
                .duration(2000)
                .blastFurnaceTemp(9000)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("rocket_fuel_rp_1")
                .circuitMeta(2)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 64)
                .inputFluids(GTMaterials.CoalGas.getFluid(80000))
                .inputFluids(GTMaterials.Oxygen.getFluid(10000))
                .outputFluids(GTOMaterials.RocketFuelRp1.getFluid(4000))
                .EUt(1920)
                .duration(1200)
                .blastFurnaceTemp(6300)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("cetane_boosted_diesel")
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 16)
                .inputFluids(GTMaterials.Hydrogen.getFluid(10000))
                .inputFluids(GTMaterials.Oxygen.getFluid(5000))
                .inputFluids(GTMaterials.LightFuel.getFluid(10000))
                .inputFluids(GTMaterials.HeavyFuel.getFluid(2000))
                .inputFluids(GTMaterials.NitrationMixture.getFluid(4000))
                .inputFluids(GTOMaterials.Cetane.getFluid(400))
                .outputFluids(GTMaterials.CetaneBoostedDiesel.getFluid(18000))
                .EUt(1920)
                .duration(400)
                .blastFurnaceTemp(4800)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("concentration_mixing_hyper_fuel_2")
                .inputItems(TagPrefix.dust, GTOMaterials.Starmetal)
                .inputItems(TagPrefix.dust, GTOMaterials.Draconium)
                .inputFluids(GTOMaterials.ConcentrationMixingHyperFuel1.getFluid(1000))
                .inputFluids(GTOMaterials.DimensionallyTranscendentProsaicCatalyst.getFluid(1000))
                .outputFluids(GTOMaterials.ConcentrationMixingHyperFuel2.getFluid(1000))
                .EUt(536870912)
                .duration(800)
                .blastFurnaceTemp(18800)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("hyper_fuel_1")
                .inputFluids(GTMaterials.NaquadriaSolution.getFluid(1000))
                .inputFluids(GTOMaterials.NaquadahFuel.getFluid(1000))
                .inputFluids(GTOMaterials.EnrichedNaquadahFuel.getFluid(1000))
                .inputFluids(GTMaterials.Thorium.getFluid(2304))
                .outputFluids(GTOMaterials.HyperFuel1.getFluid(3000))
                .EUt(491520)
                .duration(400)
                .blastFurnaceTemp(12400)
                .save();

        FUEL_REFINING_RECIPES.recipeBuilder("rocket_fuel_cn3h7o3")
                .circuitMeta(4)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon, 12)
                .inputFluids(GTMaterials.Hydrogen.getFluid(14000))
                .inputFluids(GTMaterials.Nitrogen.getFluid(6000))
                .inputFluids(GTMaterials.NitricAcid.getFluid(3000))
                .inputFluids(GTMaterials.HydrogenPeroxide.getFluid(2000))
                .outputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(4000))
                .EUt(7680)
                .duration(1200)
                .blastFurnaceTemp(8200)
                .save();
    }
}
