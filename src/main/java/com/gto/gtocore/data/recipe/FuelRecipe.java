package com.gto.gtocore.data.recipe;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import earth.terrarium.adastra.common.registry.ModFluids;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Set;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.gto.gtocore.common.data.GTORecipeTypes.*;

public final class FuelRecipe {

    public static void init(Consumer<FinishedRecipe> provider) {
        Set<Item> addedItems = new ObjectOpenHashSet<>();
        for (var fuelEntry : FurnaceBlockEntity.getFuel().entrySet()) {
            addedItems.add(fuelEntry.getKey());
            var resLoc = BuiltInRegistries.ITEM.getKey(fuelEntry.getKey());
            STEAM_BOILER_RECIPES.recipeBuilder(GTCEu.id(resLoc.getNamespace() + "_" + resLoc.getPath()))
                    .inputItems(fuelEntry.getKey())
                    .duration((int) Math.min(Integer.MAX_VALUE, fuelEntry.getValue() * 12L))
                    .save(provider);

            LARGE_BOILER_RECIPES.recipeBuilder(GTCEu.id(resLoc.getNamespace() + "_" + resLoc.getPath()))
                    .inputItems(fuelEntry.getKey())
                    .duration(fuelEntry.getValue() / 80)
                    .save(provider);
        }

        for (Item item : BuiltInRegistries.ITEM) {
            int burnTime = GTUtil.getItemBurnTime(item);
            if (burnTime > 0 && !addedItems.contains(item)) {
                var resLoc = BuiltInRegistries.ITEM.getKey(item);
                STEAM_BOILER_RECIPES.recipeBuilder(GTCEu.id(resLoc.getNamespace() + "_" + resLoc.getPath()))
                        .inputItems(item)
                        .duration((int) Math.min(Integer.MAX_VALUE, burnTime * 12L))
                        .save(provider);

                LARGE_BOILER_RECIPES.recipeBuilder(GTCEu.id(resLoc.getNamespace() + "_" + resLoc.getPath()))
                        .inputItems(item)
                        .duration(burnTime / 80)
                        .save(provider);
            }
        }

        STEAM_BOILER_RECIPES.recipeBuilder("lava")
                .inputFluids(new FluidStack(Fluids.LAVA, 100))
                .duration(600 * 12)
                .save(provider);

        STEAM_BOILER_RECIPES.recipeBuilder("creosote")
                .inputFluids(Creosote.getFluid(250))
                .duration(600 * 12)
                .save(provider);

        // semi-fluid fuels, like creosote
        LARGE_BOILER_RECIPES.recipeBuilder("creosote")
                .inputFluids(Creosote.getFluid(160))
                .duration(10)
                .save(provider);

        LARGE_BOILER_RECIPES.recipeBuilder("biomass")
                .inputFluids(Biomass.getFluid(40))
                .duration(10)
                .save(provider);

        LARGE_BOILER_RECIPES.recipeBuilder("oil")
                .inputFluids(Oil.getFluid(200))
                .duration(10)
                .save(provider);

        LARGE_BOILER_RECIPES.recipeBuilder("oil_heavy")
                .inputFluids(OilHeavy.getFluid(32))
                .duration(10)
                .save(provider);

        LARGE_BOILER_RECIPES.recipeBuilder("sulfuric_heavy_fuel")
                .inputFluids(SulfuricHeavyFuel.getFluid(32))
                .duration(10)
                .save(provider);

        LARGE_BOILER_RECIPES.recipeBuilder("heavy_fuel")
                .inputFluids(HeavyFuel.getFluid(16))
                .duration(30)
                .save(provider);

        LARGE_BOILER_RECIPES.recipeBuilder("fish_oil")
                .inputFluids(FishOil.getFluid(160))
                .duration(10)
                .save(provider);

        // diesel generator fuels
        COMBUSTION_GENERATOR_FUELS.recipeBuilder("naphtha")
                .inputFluids(Naphtha.getFluid(1))
                .duration(10)
                .EUt(-V[LV])
                .save(provider);

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("sulfuric_light_fuel")
                .inputFluids(SulfuricLightFuel.getFluid(4))
                .duration(5)
                .EUt(-V[LV])
                .save(provider);

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("methanol")
                .inputFluids(Methanol.getFluid(4))
                .duration(8)
                .EUt(-V[LV])
                .save(provider);

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("ethanol")
                .inputFluids(Ethanol.getFluid(1))
                .duration(6)
                .EUt(-V[LV])
                .save(provider);

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("octane")
                .inputFluids(Octane.getFluid(2))
                .duration(5)
                .EUt(-V[LV])
                .save(provider);

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("biodiesel")
                .inputFluids(BioDiesel.getFluid(1))
                .duration(8)
                .EUt(-V[LV])
                .save(provider);

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("light_fuel")
                .inputFluids(LightFuel.getFluid(1))
                .duration(10)
                .EUt(-V[LV])
                .save(provider);

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("diesel")
                .inputFluids(Diesel.getFluid(1))
                .duration(20)
                .EUt(-V[LV])
                .save(provider);

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("cetane_diesel")
                .inputFluids(CetaneBoostedDiesel.getFluid(2))
                .duration(90)
                .EUt(-V[LV])
                .save(provider);

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("gasoline")
                .inputFluids(Gasoline.getFluid(1))
                .duration(80)
                .EUt(-V[LV])
                .save(provider);

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("high_octane_gasoline")
                .inputFluids(HighOctaneGasoline.getFluid(1))
                .duration(150)
                .EUt(-V[LV])
                .save(provider);

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("toluene")
                .inputFluids(Toluene.getFluid(1))
                .duration(10)
                .EUt(-V[LV])
                .save(provider);

        // steam generator fuels
        STEAM_TURBINE_FUELS.recipeBuilder("steam")
                .inputFluids(Steam.getFluid(640))
                .outputFluids(DistilledWater.getFluid(4))
                .duration(10)
                .EUt(-V[LV])
                .save(provider);

        // gas turbine fuels
        GAS_TURBINE_FUELS.recipeBuilder("natural_gas")
                .inputFluids(NaturalGas.getFluid(8))
                .duration(5)
                .EUt(-V[LV])
                .save(provider);

        GAS_TURBINE_FUELS.recipeBuilder("wood_gas")
                .inputFluids(WoodGas.getFluid(8))
                .duration(6)
                .EUt(-V[LV])
                .save(provider);

        GAS_TURBINE_FUELS.recipeBuilder("sulfuric_gas")
                .inputFluids(SulfuricGas.getFluid(32))
                .duration(25)
                .EUt(-V[LV])
                .save(provider);

        GAS_TURBINE_FUELS.recipeBuilder("sulfuric_naphtha")
                .inputFluids(SulfuricNaphtha.getFluid(4))
                .duration(5)
                .EUt(-V[LV])
                .save(provider);

        GAS_TURBINE_FUELS.recipeBuilder("coal_gas")
                .inputFluids(CoalGas.getFluid(1))
                .duration(3)
                .EUt(-V[LV])
                .save(provider);

        GAS_TURBINE_FUELS.recipeBuilder("methane")
                .inputFluids(Methane.getFluid(2))
                .duration(7)
                .EUt(-V[LV])
                .save(provider);

        GAS_TURBINE_FUELS.recipeBuilder("ethylene")
                .inputFluids(Ethylene.getFluid(1))
                .duration(4)
                .EUt(-V[LV])
                .save(provider);

        GAS_TURBINE_FUELS.recipeBuilder("refinery_gas")
                .inputFluids(RefineryGas.getFluid(1))
                .duration(5)
                .EUt(-V[LV])
                .save(provider);

        GAS_TURBINE_FUELS.recipeBuilder("ethane")
                .inputFluids(Ethane.getFluid(4))
                .duration(21)
                .EUt(-V[LV])
                .save(provider);

        GAS_TURBINE_FUELS.recipeBuilder("propene")
                .inputFluids(Propene.getFluid(1))
                .duration(6)
                .EUt(-V[LV])
                .save(provider);

        GAS_TURBINE_FUELS.recipeBuilder("butadiene")
                .inputFluids(Butadiene.getFluid(16))
                .duration(102)
                .EUt(-V[LV])
                .save(provider);

        GAS_TURBINE_FUELS.recipeBuilder("propane")
                .inputFluids(Propane.getFluid(4))
                .duration(29)
                .EUt(-V[LV])
                .save(provider);

        GAS_TURBINE_FUELS.recipeBuilder("butene")
                .inputFluids(Butene.getFluid(1))
                .duration(8)
                .EUt(-V[LV])
                .save(provider);

        GAS_TURBINE_FUELS.recipeBuilder("phenol")
                .inputFluids(Phenol.getFluid(1))
                .duration(9)
                .EUt(-V[LV])
                .save(provider);

        GAS_TURBINE_FUELS.recipeBuilder("benzene")
                .inputFluids(Benzene.getFluid(1))
                .duration(11)
                .EUt(-V[LV])
                .save(provider);

        GAS_TURBINE_FUELS.recipeBuilder("butane")
                .inputFluids(Butane.getFluid(4))
                .duration(37)
                .EUt(-V[LV])
                .save(provider);

        GAS_TURBINE_FUELS.recipeBuilder("lpg")
                .inputFluids(LPG.getFluid(1))
                .duration(10)
                .EUt(-V[LV])
                .save(provider);

        GAS_TURBINE_FUELS.recipeBuilder("nitrobenzene")
                .inputFluids(Nitrobenzene.getFluid(1))
                .duration(40)
                .EUt(-V[LV])
                .save(provider);

        // plasma turbine
        PLASMA_GENERATOR_FUELS.recipeBuilder("helium")
                .inputFluids(Helium.getFluid(FluidStorageKeys.PLASMA, 1))
                .outputFluids(Helium.getFluid(1))
                .duration(40)
                .EUt(-V[EV])
                .save(provider);

        PLASMA_GENERATOR_FUELS.recipeBuilder("oxygen")
                .inputFluids(Oxygen.getFluid(FluidStorageKeys.PLASMA, 1))
                .outputFluids(Oxygen.getFluid(1))
                .duration(48)
                .EUt(-V[EV])
                .save(provider);

        PLASMA_GENERATOR_FUELS.recipeBuilder("nitrogen")
                .inputFluids(Nitrogen.getFluid(FluidStorageKeys.PLASMA, 1))
                .outputFluids(Nitrogen.getFluid(1))
                .duration(64)
                .EUt(-V[EV])
                .save(provider);

        PLASMA_GENERATOR_FUELS.recipeBuilder("iron")
                .inputFluids(Iron.getFluid(FluidStorageKeys.PLASMA, 1))
                .outputFluids(Iron.getFluid(1))
                .duration(96)
                .EUt(-V[EV])
                .save(provider);

        PLASMA_GENERATOR_FUELS.recipeBuilder("nickel")
                .inputFluids(Nickel.getFluid(FluidStorageKeys.PLASMA, 1))
                .outputFluids(Nickel.getFluid(1))
                .duration(192)
                .EUt(-V[EV])
                .save(provider);

        PLASMA_GENERATOR_FUELS.recipeBuilder(GTOCore.id("orichalcum"))
                .inputFluids(GTOMaterials.Orichalcum.getFluid(FluidStorageKeys.PLASMA, 1))
                .outputFluids(GTOMaterials.Orichalcum.getFluid(1))
                .duration(32)
                .EUt(-V[EV])
                .save(provider);

        PLASMA_GENERATOR_FUELS.recipeBuilder(GTOCore.id("mithril"))
                .inputFluids(GTOMaterials.Mithril.getFluid(FluidStorageKeys.PLASMA, 1))
                .outputFluids(GTOMaterials.Mithril.getFluid(1))
                .duration(36)
                .EUt(-V[EV])
                .save(provider);

        PLASMA_GENERATOR_FUELS.recipeBuilder(GTOCore.id("silver"))
                .inputFluids(Silver.getFluid(FluidStorageKeys.PLASMA, 1))
                .outputFluids(Silver.getFluid(1))
                .duration(204)
                .EUt(-V[EV])
                .save(provider);

        PLASMA_GENERATOR_FUELS.recipeBuilder(GTOCore.id("sulfur"))
                .inputFluids(Sulfur.getFluid(FluidStorageKeys.PLASMA, 1))
                .outputFluids(Sulfur.getFluid(1))
                .duration(72)
                .EUt(-V[EV])
                .save(provider);

        PLASMA_GENERATOR_FUELS.recipeBuilder(GTOCore.id("boron"))
                .inputFluids(Boron.getFluid(FluidStorageKeys.PLASMA, 1))
                .outputFluids(Boron.getFluid(1))
                .duration(54)
                .EUt(-V[EV])
                .save(provider);

        PLASMA_GENERATOR_FUELS.recipeBuilder(GTOCore.id("calcium"))
                .inputFluids(Calcium.getFluid(FluidStorageKeys.PLASMA, 1))
                .outputFluids(Calcium.getFluid(1))
                .duration(86)
                .EUt(-V[EV])
                .save(provider);

        PLASMA_GENERATOR_FUELS.recipeBuilder(GTOCore.id("zinc"))
                .inputFluids(Zinc.getFluid(FluidStorageKeys.PLASMA, 1))
                .outputFluids(Zinc.getFluid(1))
                .duration(144)
                .EUt(-V[EV])
                .save(provider);

        PLASMA_GENERATOR_FUELS.recipeBuilder(GTOCore.id("niobium"))
                .inputFluids(Niobium.getFluid(FluidStorageKeys.PLASMA, 1))
                .outputFluids(Niobium.getFluid(1))
                .duration(168)
                .EUt(-V[EV])
                .save(provider);

        PLASMA_GENERATOR_FUELS.recipeBuilder(GTOCore.id("tin"))
                .inputFluids(Tin.getFluid(FluidStorageKeys.PLASMA, 1))
                .outputFluids(Tin.getFluid(1))
                .duration(138)
                .EUt(-V[EV])
                .save(provider);

        PLASMA_GENERATOR_FUELS.recipeBuilder(GTOCore.id("neon"))
                .inputFluids(Neon.getFluid(FluidStorageKeys.PLASMA, 1))
                .outputFluids(Neon.getFluid(1))
                .duration(172)
                .EUt(-V[EV])
                .save(provider);

        PLASMA_GENERATOR_FUELS.recipeBuilder(GTOCore.id("xenon"))
                .inputFluids(Xenon.getFluid(FluidStorageKeys.PLASMA, 1))
                .outputFluids(Xenon.getFluid(1))
                .duration(148)
                .EUt(-V[EV])
                .save(provider);

        SUPERCRITICAL_STEAM_TURBINE_FUELS.recipeBuilder(GTOCore.id("supercritical_steam"))
                .inputFluids(GTOMaterials.SupercriticalSteam.getFluid(80))
                .outputFluids(DistilledWater.getFluid(8))
                .duration(30)
                .EUt(-V[MV])
                .save(provider);

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder(GTOCore.id("seed_oil"))
                .inputFluids(SeedOil.getFluid(8))
                .duration(8)
                .EUt(-V[ULV])
                .save(provider);

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder(GTOCore.id("fish_oil"))
                .inputFluids(FishOil.getFluid(8))
                .duration(8)
                .EUt(-V[ULV])
                .save(provider);

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder(GTOCore.id("biomass"))
                .inputFluids(Biomass.getFluid(2))
                .duration(8)
                .EUt(-V[ULV])
                .save(provider);

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder(GTOCore.id("oil"))
                .inputFluids(Oil.getFluid(1))
                .duration(10)
                .EUt(-V[ULV])
                .save(provider);

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder(GTOCore.id("oil_light"))
                .inputFluids(OilLight.getFluid(1))
                .duration(10)
                .EUt(-V[ULV])
                .save(provider);

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder(GTOCore.id("creosote"))
                .inputFluids(Creosote.getFluid(1))
                .duration(12)
                .EUt(-V[ULV])
                .save(provider);

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder(GTOCore.id("oil_heavy"))
                .inputFluids(OilHeavy.getFluid(1))
                .duration(15)
                .EUt(-V[ULV])
                .save(provider);

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder(GTOCore.id("oil_medium"))
                .inputFluids(RawOil.getFluid(1))
                .duration(15)
                .EUt(-V[ULV])
                .save(provider);

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder(GTOCore.id("coal_tar"))
                .inputFluids(CoalTar.getFluid(2))
                .duration(8)
                .EUt(-V[ULV])
                .save(provider);

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder(GTOCore.id("sulfuric_heavy_fuel"))
                .inputFluids(SulfuricHeavyFuel.getFluid(1))
                .duration(20)
                .EUt(-V[ULV])
                .save(provider);

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder(GTOCore.id("glycerol"))
                .inputFluids(Glycerol.getFluid(1))
                .duration(82)
                .EUt(-V[ULV])
                .save(provider);

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder(GTOCore.id("heavy_fuel"))
                .inputFluids(HeavyFuel.getFluid(1))
                .duration(90)
                .EUt(-V[ULV])
                .save(provider);

        ROCKET_ENGINE_FUELS.recipeBuilder(GTOCore.id("rocket_engine_fuel_1"))
                .inputFluids(RocketFuel.getFluid(10))
                .duration(20)
                .EUt(-512)
                .save(provider);

        ROCKET_ENGINE_FUELS.recipeBuilder(GTOCore.id("rocket_engine_fuel_2"))
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(10))
                .duration(40)
                .EUt(-512)
                .save(provider);

        ROCKET_ENGINE_FUELS.recipeBuilder(GTOCore.id("rocket_engine_fuel_3"))
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10))
                .duration(64)
                .EUt(-512)
                .save(provider);

        ROCKET_ENGINE_FUELS.recipeBuilder(GTOCore.id("rocket_engine_fuel_4"))
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(10))
                .duration(96)
                .EUt(-512)
                .save(provider);

        ROCKET_ENGINE_FUELS.recipeBuilder(GTOCore.id("rocket_engine_fuel_5"))
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10))
                .duration(160)
                .EUt(-512)
                .save(provider);

        ROCKET_ENGINE_FUELS.recipeBuilder(GTOCore.id("rocket_engine_fuel_6"))
                .inputFluids(GTOMaterials.ExplosiveHydrazine.getFluid(10))
                .duration(320)
                .EUt(-512)
                .save(provider);

        ROCKET_ENGINE_FUELS.recipeBuilder(GTOCore.id("rocket_engine_fuel_7"))
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 10))
                .EUt(-512)
                .duration(480)
                .save(provider);

        ROCKET_ENGINE_FUELS.recipeBuilder(GTOCore.id("rocket_engine_fuel_8"))
                .inputFluids(GTOMaterials.StellarEnergyRocketFuel.getFluid(10))
                .duration(960)
                .EUt(-512)
                .save(provider);

        NAQUADAH_REACTOR.recipeBuilder(GTOCore.id("naquadah"))
                .inputFluids(Naquadah.getFluid(1))
                .duration(60)
                .EUt(-32768)
                .save(provider);

        NAQUADAH_REACTOR.recipeBuilder(GTOCore.id("enriched_naquadah"))
                .inputFluids(NaquadahEnriched.getFluid(1))
                .duration(90)
                .EUt(-32768)
                .save(provider);

        NAQUADAH_REACTOR.recipeBuilder(GTOCore.id("naquadah_fuel"))
                .inputFluids(GTOMaterials.NaquadahFuel.getFluid(1))
                .duration(546)
                .EUt(-32768)
                .save(provider);

        NAQUADAH_REACTOR.recipeBuilder(GTOCore.id("enriched_naquadah_fuel"))
                .inputFluids(GTOMaterials.EnrichedNaquadahFuel.getFluid(1))
                .duration(819)
                .EUt(-32768)
                .save(provider);

        LARGE_NAQUADAH_REACTOR_RECIPES.recipeBuilder(GTOCore.id("naquadah_fuel"))
                .inputFluids(GTOMaterials.NaquadahFuel.getFluid(16))
                .inputFluids(Hydrogen.getFluid(80))
                .duration(875)
                .EUt(-524288)
                .save(provider);

        LARGE_NAQUADAH_REACTOR_RECIPES.recipeBuilder(GTOCore.id("enriched_naquadah_fuel"))
                .inputFluids(GTOMaterials.EnrichedNaquadahFuel.getFluid(16))
                .inputFluids(Hydrogen.getFluid(80))
                .duration(1250)
                .EUt(-524288)
                .save(provider);

        LARGE_NAQUADAH_REACTOR_RECIPES.recipeBuilder(GTOCore.id("naquadah_fuel1"))
                .inputFluids(GTOMaterials.NaquadahFuel.getFluid(160))
                .inputFluids(Oxygen.getFluid(FluidStorageKeys.PLASMA, 40))
                .duration(16 * 875)
                .EUt(-524288)
                .save(provider);

        LARGE_NAQUADAH_REACTOR_RECIPES.recipeBuilder(GTOCore.id("enriched_naquadah_fuel1"))
                .inputFluids(GTOMaterials.EnrichedNaquadahFuel.getFluid(160))
                .inputFluids(Nitrogen.getFluid(FluidStorageKeys.PLASMA, 40))
                .duration(16 * 1250)
                .EUt(-524288)
                .save(provider);

        HYPER_REACTOR_RECIPES.recipeBuilder(GTOCore.id("hyper_fuel_1"))
                .inputFluids(GTOMaterials.HyperFuel1.getFluid(8))
                .inputFluids(Argon.getFluid(FluidStorageKeys.PLASMA, 4))
                .duration(200)
                .EUt(-V[UEV])
                .save(provider);

        HYPER_REACTOR_RECIPES.recipeBuilder(GTOCore.id("hyper_fuel_2"))
                .inputFluids(GTOMaterials.HyperFuel2.getFluid(8))
                .inputFluids(Iron.getFluid(FluidStorageKeys.PLASMA, 4))
                .duration(200)
                .EUt(-V[UXV])
                .save(provider);

        HYPER_REACTOR_RECIPES.recipeBuilder(GTOCore.id("hyper_fuel_3"))
                .inputFluids(GTOMaterials.HyperFuel3.getFluid(8))
                .inputFluids(Nickel.getFluid(FluidStorageKeys.PLASMA, 4))
                .duration(200)
                .EUt(-V[OpV])
                .save(provider);

        HYPER_REACTOR_RECIPES.recipeBuilder(GTOCore.id("hyper_fuel_4"))
                .inputFluids(GTOMaterials.HyperFuel4.getFluid(8))
                .inputFluids(GTOMaterials.DegenerateRhenium.getFluid(FluidStorageKeys.PLASMA, 1))
                .duration(200)
                .EUt(-V[MAX])
                .save(provider);

        ADVANCED_HYPER_REACTOR_RECIPES.recipeBuilder(GTOCore.id("concentration_mixing_hyper_fuel_1"))
                .inputFluids(GTOMaterials.ConcentrationMixingHyperFuel1.getFluid(16))
                .duration(200)
                .EUt(-16 * V[MAX])
                .save(provider);

        ADVANCED_HYPER_REACTOR_RECIPES.recipeBuilder(GTOCore.id("concentration_mixing_hyper_fuel_2"))
                .inputFluids(GTOMaterials.ConcentrationMixingHyperFuel2.getFluid(16))
                .duration(200)
                .EUt(-64 * V[MAX])
                .save(provider);

        DYSON_SPHERE_RECIPES.recipeBuilder("a")
                .inputItems(GTOItems.DYSON_SWARM_MODULE.asStack(64))
                .CWUt(512)
                .duration(200)
                .EUt(VA[UIV])
                .save(provider);
    }
}
