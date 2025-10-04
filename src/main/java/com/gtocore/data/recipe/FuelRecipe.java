package com.gtocore.data.recipe;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.gregtechceu.gtceu.utils.collection.OpenCacheHashSet;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import earth.terrarium.adastra.common.registry.ModFluids;

import java.util.Set;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;

public final class FuelRecipe {

    public static void init() {
        Set<Item> addedItems = new OpenCacheHashSet<>();
        for (var fuelEntry : FurnaceBlockEntity.getFuel().entrySet()) {
            if (fuelEntry.getKey() instanceof BucketItem) continue;
            addedItems.add(fuelEntry.getKey());
            var resLoc = ItemUtils.getIdLocation(fuelEntry.getKey());
            long burnTime = fuelEntry.getValue();
            STEAM_BOILER_RECIPES.recipeBuilder(resLoc.getNamespace() + "_" + resLoc.getPath())
                    .inputItems(fuelEntry.getKey())
                    .duration((int) Math.min(Integer.MAX_VALUE, burnTime << 3))
                    .save();

            int time = (int) (burnTime / 20);
            if (time > 0) LARGE_BOILER_RECIPES.recipeBuilder(resLoc.getNamespace() + "_" + resLoc.getPath())
                    .inputItems(fuelEntry.getKey())
                    .duration(time)
                    .save();
        }

        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof BucketItem) continue;
            long burnTime = GTUtil.getItemBurnTime(item);
            if (burnTime > 0 && !addedItems.contains(item)) {
                var resLoc = ItemUtils.getIdLocation(item);
                STEAM_BOILER_RECIPES.recipeBuilder(resLoc.getNamespace() + "_" + resLoc.getPath())
                        .inputItems(item)
                        .duration((int) Math.min(Integer.MAX_VALUE, burnTime << 3))
                        .save();

                int time = (int) (burnTime / 20);
                if (time > 0) LARGE_BOILER_RECIPES.recipeBuilder(resLoc.getNamespace() + "_" + resLoc.getPath())
                        .inputItems(item)
                        .duration(time)
                        .save();
            }
        }

        STEAM_BOILER_RECIPES.recipeBuilder("lava")
                .inputFluids(new FluidStack(Fluids.LAVA, 100))
                .duration(1200)
                .save();

        STEAM_BOILER_RECIPES.recipeBuilder("creosote")
                .inputFluids(Creosote.getFluid(250))
                .duration(3000)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("lava")
                .inputFluids(new FluidStack(Fluids.LAVA, 100))
                .duration(10)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("creosote")
                .inputFluids(Creosote.getFluid(250))
                .duration(25)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("biomass")
                .inputFluids(Biomass.getFluid(40))
                .duration(40)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("oil")
                .inputFluids(Oil.getFluid(200))
                .duration(40)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("oil_heavy")
                .inputFluids(OilHeavy.getFluid(32))
                .duration(40)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("sulfuric_heavy_fuel")
                .inputFluids(SulfuricHeavyFuel.getFluid(32))
                .duration(40)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("heavy_fuel")
                .inputFluids(HeavyFuel.getFluid(16))
                .duration(120)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("fish_oil")
                .inputFluids(FishOil.getFluid(160))
                .duration(40)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("water_gas")
                .inputFluids(GTOMaterials.CoalSlurry.getFluid(1000))
                .outputFluids(GTOMaterials.WaterGas.getFluid(1000))
                .duration(80)
                .temperature(1000)
                .priority(1)
                .save();

        // diesel generator fuels
        COMBUSTION_GENERATOR_FUELS.recipeBuilder("naphtha")
                .inputFluids(Naphtha.getFluid(2))
                .inputFluids(Air.getFluid(20))
                .duration(20)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("sulfuric_light_fuel")
                .inputFluids(SulfuricLightFuel.getFluid(16))
                .inputFluids(Air.getFluid(20))
                .duration(20)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("methanol")
                .inputFluids(Methanol.getFluid(12))
                .inputFluids(Air.getFluid(24))
                .duration(24)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("ethanol")
                .inputFluids(Ethanol.getFluid(3))
                .inputFluids(Air.getFluid(18))
                .duration(18)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("octane")
                .inputFluids(Octane.getFluid(8))
                .inputFluids(Air.getFluid(20))
                .duration(20)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("biodiesel")
                .inputFluids(BioDiesel.getFluid(3))
                .inputFluids(Air.getFluid(24))
                .duration(24)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("light_fuel")
                .inputFluids(LightFuel.getFluid(2))
                .inputFluids(Air.getFluid(20))
                .duration(20)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("diesel")
                .inputFluids(Diesel.getFluid(1))
                .inputFluids(Air.getFluid(20))
                .duration(20)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("cetane_diesel")
                .inputFluids(CetaneBoostedDiesel.getFluid(2))
                .inputFluids(Air.getFluid(90))
                .duration(90)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("gasoline")
                .inputFluids(Gasoline.getFluid(1))
                .inputFluids(Air.getFluid(80))
                .duration(80)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("high_octane_gasoline")
                .inputFluids(HighOctaneGasoline.getFluid(1))
                .inputFluids(Air.getFluid(150))
                .duration(150)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("toluene")
                .inputFluids(Toluene.getFluid(2))
                .inputFluids(Air.getFluid(20))
                .duration(20)
                .EUt(-V[LV])
                .save();

        // steam generator fuels
        STEAM_TURBINE_FUELS.recipeBuilder("steam")
                .inputFluids(Steam.getFluid(1280))
                .outputFluids(DistilledWater.getFluid(8))
                .duration(20)
                .EUt(-V[LV])
                .save();

        STEAM_TURBINE_FUELS.recipeBuilder("high_pressure_steam")
                .inputFluids(GTOMaterials.HighPressureSteam.getFluid(320))
                .outputFluids(DistilledWater.getFluid(8))
                .duration(20)
                .EUt(-V[MV])
                .save();

        // gas turbine fuels
        GAS_TURBINE_FUELS.recipeBuilder("natural_gas")
                .inputFluids(NaturalGas.getFluid(32))
                .inputFluids(Air.getFluid(20))
                .duration(20)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("wood_gas")
                .inputFluids(WoodGas.getFluid(24))
                .inputFluids(Air.getFluid(18))
                .duration(18)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("sulfuric_gas")
                .inputFluids(SulfuricGas.getFluid(32))
                .inputFluids(Air.getFluid(25))
                .duration(25)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("sulfuric_naphtha")
                .inputFluids(SulfuricNaphtha.getFluid(16))
                .inputFluids(Air.getFluid(20))
                .duration(20)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("coal_gas")
                .inputFluids(CoalGas.getFluid(7))
                .inputFluids(Air.getFluid(21))
                .duration(21)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("methane")
                .inputFluids(Methane.getFluid(6))
                .inputFluids(Air.getFluid(21))
                .duration(21)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("ethylene")
                .inputFluids(Ethylene.getFluid(5))
                .inputFluids(Air.getFluid(20))
                .duration(20)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("refinery_gas")
                .inputFluids(RefineryGas.getFluid(4))
                .inputFluids(Air.getFluid(20))
                .duration(20)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("ethane")
                .inputFluids(Ethane.getFluid(4))
                .inputFluids(Air.getFluid(21))
                .duration(21)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("propene")
                .inputFluids(Propene.getFluid(3))
                .inputFluids(Air.getFluid(18))
                .duration(18)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("butadiene")
                .inputFluids(Butadiene.getFluid(16))
                .inputFluids(Air.getFluid(102))
                .duration(102)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("propane")
                .inputFluids(Propane.getFluid(4))
                .inputFluids(Air.getFluid(29))
                .duration(29)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("butene")
                .inputFluids(Butene.getFluid(3))
                .inputFluids(Air.getFluid(24))
                .duration(24)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("phenol")
                .inputFluids(Phenol.getFluid(2))
                .inputFluids(Air.getFluid(18))
                .duration(18)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("benzene")
                .inputFluids(Benzene.getFluid(2))
                .inputFluids(Air.getFluid(22))
                .duration(22)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("butane")
                .inputFluids(Butane.getFluid(4))
                .inputFluids(Air.getFluid(37))
                .duration(37)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("lpg")
                .inputFluids(LPG.getFluid(2))
                .inputFluids(Air.getFluid(20))
                .duration(20)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("nitrobenzene")
                .inputFluids(Nitrobenzene.getFluid(1))
                .inputFluids(Air.getFluid(40))
                .duration(40)
                .EUt(-V[LV])
                .save();

        // plasma turbine
        PLASMA_GENERATOR_FUELS.recipeBuilder("helium")
                .inputFluids(Helium.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Helium.getFluid(4))
                .duration(150)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("oxygen")
                .inputFluids(Oxygen.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Oxygen.getFluid(4))
                .duration(212)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("nitrogen")
                .inputFluids(Nitrogen.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Nitrogen.getFluid(4))
                .duration(256)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("iron")
                .inputFluids(Iron.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Iron.getFluid(4))
                .duration(564)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("nickel")
                .inputFluids(Nickel.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Nickel.getFluid(4))
                .duration(668)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("orichalcum")
                .inputFluids(GTOMaterials.Orichalcum.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(GTOMaterials.Orichalcum.getFluid(4))
                .duration(64)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("mithril")
                .inputFluids(GTOMaterials.Mithril.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(GTOMaterials.Mithril.getFluid(4))
                .duration(72)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("silver")
                .inputFluids(Silver.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Silver.getFluid(4))
                .duration(698)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("sulfur")
                .inputFluids(Sulfur.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Sulfur.getFluid(4))
                .duration(248)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("boron")
                .inputFluids(Boron.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Boron.getFluid(4))
                .duration(186)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("calcium")
                .inputFluids(Calcium.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Calcium.getFluid(4))
                .duration(244)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("zinc")
                .inputFluids(Zinc.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Zinc.getFluid(4))
                .duration(544)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("niobium")
                .inputFluids(Niobium.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Niobium.getFluid(4))
                .duration(462)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("tin")
                .inputFluids(Tin.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Tin.getFluid(4))
                .duration(416)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("neon")
                .inputFluids(Neon.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Neon.getFluid(4))
                .duration(542)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("xenon")
                .inputFluids(Xenon.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Xenon.getFluid(4))
                .duration(492)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("argon")
                .inputFluids(Argon.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Argon.getFluid(4))
                .duration(234)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("titanium")
                .inputFluids(Titanium.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Titanium.getFluid(4))
                .duration(532)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("americium")
                .inputFluids(Americium.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Americium.getFluid(4))
                .duration(716)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("thorium")
                .inputFluids(Thorium.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(Thorium.getFluid(4))
                .duration(986)
                .EUt(-V[EV])
                .save();

        SUPERCRITICAL_STEAM_TURBINE_FUELS.recipeBuilder("supercritical_steam")
                .inputFluids(GTOMaterials.SupercriticalSteam.getFluid(80))
                .outputFluids(DistilledWater.getFluid(8))
                .duration(30)
                .EUt(-V[MV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("seed_oil")
                .inputFluids(SeedOil.getFluid(24))
                .inputFluids(Air.getFluid(6))
                .duration(24)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("fish_oil")
                .inputFluids(FishOil.getFluid(24))
                .inputFluids(Air.getFluid(6))
                .duration(24)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("biomass")
                .inputFluids(Biomass.getFluid(24))
                .inputFluids(Air.getFluid(6))
                .duration(24)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("oil")
                .inputFluids(Oil.getFluid(2))
                .inputFluids(Air.getFluid(5))
                .duration(20)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("oil_light")
                .inputFluids(OilLight.getFluid(2))
                .inputFluids(Air.getFluid(5))
                .duration(20)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("creosote")
                .inputFluids(Creosote.getFluid(2))
                .inputFluids(Air.getFluid(6))
                .duration(24)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("oil_heavy")
                .inputFluids(OilHeavy.getFluid(2))
                .inputFluids(Air.getFluid(8))
                .duration(30)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("oil_medium")
                .inputFluids(RawOil.getFluid(2))
                .inputFluids(Air.getFluid(8))
                .duration(30)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("coal_tar")
                .inputFluids(CoalTar.getFluid(6))
                .inputFluids(Air.getFluid(6))
                .duration(24)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("sulfuric_heavy_fuel")
                .inputFluids(SulfuricHeavyFuel.getFluid(1))
                .inputFluids(Air.getFluid(5))
                .duration(20)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("glycerol")
                .inputFluids(Glycerol.getFluid(1))
                .inputFluids(Air.getFluid(20))
                .duration(82)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("heavy_fuel")
                .inputFluids(HeavyFuel.getFluid(1))
                .inputFluids(Air.getFluid(22))
                .duration(90)
                .EUt(-V[ULV])
                .save();

        ROCKET_ENGINE_FUELS.recipeBuilder("rocket_engine_fuel_1")
                .inputFluids(RocketFuel.getFluid(10))
                .duration(20)
                .EUt(-512)
                .save();

        ROCKET_ENGINE_FUELS.recipeBuilder("rocket_engine_fuel_2")
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(10))
                .duration(40)
                .EUt(-512)
                .save();

        ROCKET_ENGINE_FUELS.recipeBuilder("rocket_engine_fuel_3")
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10))
                .duration(64)
                .EUt(-512)
                .save();

        ROCKET_ENGINE_FUELS.recipeBuilder("rocket_engine_fuel_4")
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(10))
                .duration(96)
                .EUt(-512)
                .save();

        ROCKET_ENGINE_FUELS.recipeBuilder("rocket_engine_fuel_5")
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10))
                .duration(160)
                .EUt(-512)
                .save();

        ROCKET_ENGINE_FUELS.recipeBuilder("rocket_engine_fuel_6")
                .inputFluids(GTOMaterials.ExplosiveHydrazine.getFluid(10))
                .duration(320)
                .EUt(-512)
                .save();

        ROCKET_ENGINE_FUELS.recipeBuilder("rocket_engine_fuel_7")
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 10))
                .EUt(-512)
                .duration(480)
                .save();

        ROCKET_ENGINE_FUELS.recipeBuilder("rocket_engine_fuel_8")
                .inputFluids(GTOMaterials.StellarEnergyRocketFuel.getFluid(10))
                .duration(960)
                .EUt(-512)
                .save();

        NAQUADAH_REACTOR.recipeBuilder("naquadah")
                .inputFluids(Naquadah.getFluid(1))
                .duration(240)
                .EUt(-8192)
                .save();

        NAQUADAH_REACTOR.recipeBuilder("enriched_naquadah")
                .inputFluids(NaquadahEnriched.getFluid(1))
                .duration(360)
                .EUt(-8192)
                .save();

        NAQUADAH_REACTOR.recipeBuilder("naquadah_fuel")
                .inputFluids(GTOMaterials.NaquadahFuel.getFluid(1))
                .duration(2184)
                .EUt(-8192)
                .save();

        NAQUADAH_REACTOR.recipeBuilder("enriched_naquadah_fuel")
                .inputFluids(GTOMaterials.EnrichedNaquadahFuel.getFluid(1))
                .duration(3276)
                .EUt(-8192)
                .save();

        LARGE_NAQUADAH_REACTOR_RECIPES.recipeBuilder("naquadah_fuel")
                .inputFluids(GTOMaterials.NaquadahFuel.getFluid(16))
                .inputFluids(Hydrogen.getFluid(80))
                .duration(875)
                .EUt(-524288)
                .save();

        LARGE_NAQUADAH_REACTOR_RECIPES.recipeBuilder("enriched_naquadah_fuel")
                .inputFluids(GTOMaterials.EnrichedNaquadahFuel.getFluid(16))
                .inputFluids(Hydrogen.getFluid(80))
                .duration(1250)
                .EUt(-524288)
                .save();

        LARGE_NAQUADAH_REACTOR_RECIPES.recipeBuilder("naquadah_fuel1")
                .inputFluids(GTOMaterials.NaquadahFuel.getFluid(160))
                .inputFluids(Oxygen.getFluid(FluidStorageKeys.PLASMA, 40))
                .duration(16 * 875)
                .EUt(-524288)
                .save();

        LARGE_NAQUADAH_REACTOR_RECIPES.recipeBuilder("enriched_naquadah_fuel1")
                .inputFluids(GTOMaterials.EnrichedNaquadahFuel.getFluid(160))
                .inputFluids(Nitrogen.getFluid(FluidStorageKeys.PLASMA, 40))
                .duration(16 * 1250)
                .EUt(-524288)
                .save();

        HYPER_REACTOR_RECIPES.recipeBuilder("hyper_fuel_1")
                .inputFluids(GTOMaterials.HyperFuel1.getFluid(8))
                .inputFluids(Argon.getFluid(FluidStorageKeys.PLASMA, 4))
                .duration(200)
                .EUt(-V[UEV])
                .save();

        HYPER_REACTOR_RECIPES.recipeBuilder("hyper_fuel_2")
                .inputFluids(GTOMaterials.HyperFuel2.getFluid(8))
                .inputFluids(Iron.getFluid(FluidStorageKeys.PLASMA, 4))
                .duration(200)
                .EUt(-V[UXV])
                .save();

        HYPER_REACTOR_RECIPES.recipeBuilder("hyper_fuel_3")
                .inputFluids(GTOMaterials.HyperFuel3.getFluid(8))
                .inputFluids(Nickel.getFluid(FluidStorageKeys.PLASMA, 4))
                .duration(200)
                .EUt(-V[OpV])
                .save();

        HYPER_REACTOR_RECIPES.recipeBuilder("hyper_fuel_4")
                .inputFluids(GTOMaterials.HyperFuel4.getFluid(8))
                .inputFluids(GTOMaterials.DegenerateRhenium.getFluid(FluidStorageKeys.PLASMA, 1))
                .duration(200)
                .EUt(-V[MAX])
                .save();

        ADVANCED_HYPER_REACTOR_RECIPES.recipeBuilder("concentration_mixing_hyper_fuel_1")
                .inputFluids(GTOMaterials.ConcentrationMixingHyperFuel1.getFluid(16))
                .duration(200)
                .EUt(-16 * V[MAX])
                .save();

        ADVANCED_HYPER_REACTOR_RECIPES.recipeBuilder("concentration_mixing_hyper_fuel_2")
                .inputFluids(GTOMaterials.ConcentrationMixingHyperFuel2.getFluid(16))
                .duration(200)
                .EUt(-64 * V[MAX])
                .save();

        DYSON_SPHERE_RECIPES.recipeBuilder("a")
                .inputItems(GTOItems.DYSON_SWARM_MODULE.asItem(), 64)
                .CWUt(512)
                .duration(200)
                .EUt(VA[UIV])
                .save();
    }
}
