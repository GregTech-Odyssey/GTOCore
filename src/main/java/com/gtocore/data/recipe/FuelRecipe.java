package com.gtocore.data.recipe;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import earth.terrarium.adastra.common.registry.ModFluids;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;

import java.util.Set;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;

public final class FuelRecipe {

    public static void init() {
        Set<Item> addedItems = new ReferenceOpenHashSet<>();
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
                .inputFluids(Creosote, 250)
                .duration(3000)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("lava")
                .inputFluids(new FluidStack(Fluids.LAVA, 100))
                .duration(10)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("creosote")
                .inputFluids(Creosote, 250)
                .duration(25)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("biomass")
                .inputFluids(Biomass, 40)
                .duration(40)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("oil")
                .inputFluids(Oil, 200)
                .duration(40)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("oil_heavy")
                .inputFluids(OilHeavy, 32)
                .duration(40)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("sulfuric_heavy_fuel")
                .inputFluids(SulfuricHeavyFuel, 32)
                .duration(40)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("heavy_fuel")
                .inputFluids(HeavyFuel, 16)
                .duration(120)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("fish_oil")
                .inputFluids(FishOil, 160)
                .duration(40)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder("water_gas")
                .inputFluids(GTOMaterials.CoalSlurry, 1000)
                .outputFluids(GTOMaterials.WaterGas, 1000)
                .duration(80)
                .temperature(1000)
                .priority(1)
                .save();

        // diesel generator fuels
        COMBUSTION_GENERATOR_FUELS.recipeBuilder("naphtha")
                .inputFluids(Naphtha, 2)
                .inputFluids(Air, 20)
                .duration(20)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("sulfuric_light_fuel")
                .inputFluids(SulfuricLightFuel, 16)
                .inputFluids(Air, 20)
                .duration(20)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("methanol")
                .inputFluids(Methanol, 12)
                .inputFluids(Air, 24)
                .duration(24)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("ethanol")
                .inputFluids(Ethanol, 3)
                .inputFluids(Air, 18)
                .duration(18)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("octane")
                .inputFluids(Octane, 8)
                .inputFluids(Air, 20)
                .duration(20)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("biodiesel")
                .inputFluids(BioDiesel, 3)
                .inputFluids(Air, 24)
                .duration(24)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("light_fuel")
                .inputFluids(LightFuel, 2)
                .inputFluids(Air, 20)
                .duration(20)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("diesel")
                .inputFluids(Diesel, 1)
                .inputFluids(Air, 20)
                .duration(20)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("cetane_diesel")
                .inputFluids(CetaneBoostedDiesel, 2)
                .inputFluids(Air, 120)
                .duration(120)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("gasoline")
                .inputFluids(Gasoline, 1)
                .inputFluids(Air, 80)
                .duration(80)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("high_octane_gasoline")
                .inputFluids(HighOctaneGasoline, 1)
                .inputFluids(Air, 150)
                .duration(150)
                .EUt(-V[LV])
                .save();

        COMBUSTION_GENERATOR_FUELS.recipeBuilder("toluene")
                .inputFluids(Toluene, 2)
                .inputFluids(Air, 20)
                .duration(20)
                .EUt(-V[LV])
                .save();

        // steam generator fuels
        STEAM_TURBINE_FUELS.recipeBuilder("steam")
                .inputFluids(Steam, 1280)
                .outputFluids(DistilledWater.getFluid(8))
                .duration(20)
                .EUt(-V[LV])
                .save();

        STEAM_TURBINE_FUELS.recipeBuilder("high_pressure_steam")
                .inputFluids(GTOMaterials.HighPressureSteam, 320)
                .outputFluids(DistilledWater.getFluid(8))
                .duration(20)
                .EUt(-V[MV])
                .save();

        SUPERCRITICAL_STEAM_TURBINE_FUELS.recipeBuilder("supercritical_steam")
                .inputFluids(GTOMaterials.SupercriticalSteam, 80)
                .outputFluids(DistilledWater.getFluid(8))
                .duration(30)
                .EUt(-V[MV])
                .save();

        // gas turbine fuels
        GAS_TURBINE_FUELS.recipeBuilder("natural_gas")
                .inputFluids(NaturalGas, 32)
                .inputFluids(Air, 20)
                .duration(20)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("wood_gas")
                .inputFluids(WoodGas, 24)
                .inputFluids(Air, 18)
                .duration(18)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("sulfuric_gas")
                .inputFluids(SulfuricGas, 32)
                .inputFluids(Air, 25)
                .duration(25)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("sulfuric_naphtha")
                .inputFluids(SulfuricNaphtha, 16)
                .inputFluids(Air, 20)
                .duration(20)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("coal_gas")
                .inputFluids(CoalGas, 7)
                .inputFluids(Air, 21)
                .duration(21)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("methane")
                .inputFluids(Methane, 6)
                .inputFluids(Air, 21)
                .duration(21)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("ethylene")
                .inputFluids(Ethylene, 5)
                .inputFluids(Air, 20)
                .duration(20)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("refinery_gas")
                .inputFluids(RefineryGas, 4)
                .inputFluids(Air, 20)
                .duration(20)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("ethane")
                .inputFluids(Ethane, 4)
                .inputFluids(Air, 21)
                .duration(21)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("propene")
                .inputFluids(Propene, 3)
                .inputFluids(Air, 18)
                .duration(18)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("butadiene")
                .inputFluids(Butadiene, 16)
                .inputFluids(Air, 102)
                .duration(102)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("propane")
                .inputFluids(Propane, 4)
                .inputFluids(Air, 29)
                .duration(29)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("butene")
                .inputFluids(Butene, 3)
                .inputFluids(Air, 24)
                .duration(24)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("phenol")
                .inputFluids(Phenol, 2)
                .inputFluids(Air, 18)
                .duration(18)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("benzene")
                .inputFluids(Benzene, 2)
                .inputFluids(Air, 22)
                .duration(22)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("butane")
                .inputFluids(Butane, 4)
                .inputFluids(Air, 37)
                .duration(37)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("lpg")
                .inputFluids(LPG, 2)
                .inputFluids(Air, 20)
                .duration(20)
                .EUt(-V[LV])
                .save();

        GAS_TURBINE_FUELS.recipeBuilder("nitrobenzene")
                .inputFluids(Nitrobenzene, 1)
                .inputFluids(Air, 40)
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
                .outputFluids(GTOMaterials.Orichalcum, 4)
                .duration(64)
                .EUt(-V[EV])
                .save();

        PLASMA_GENERATOR_FUELS.recipeBuilder("mithril")
                .inputFluids(GTOMaterials.Mithril.getFluid(FluidStorageKeys.PLASMA, 5))
                .outputFluids(GTOMaterials.Mithril, 4)
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

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("seed_oil")
                .inputFluids(SeedOil, 24)
                .inputFluids(Air, 6)
                .duration(24)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("fish_oil")
                .inputFluids(FishOil, 24)
                .inputFluids(Air, 6)
                .duration(24)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("biomass")
                .inputFluids(Biomass, 24)
                .inputFluids(Air, 6)
                .duration(24)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("oil")
                .inputFluids(Oil, 2)
                .inputFluids(Air, 5)
                .duration(20)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("oil_light")
                .inputFluids(OilLight, 2)
                .inputFluids(Air, 5)
                .duration(20)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("creosote")
                .inputFluids(Creosote, 2)
                .inputFluids(Air, 6)
                .duration(24)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("oil_heavy")
                .inputFluids(OilHeavy, 2)
                .inputFluids(Air, 8)
                .duration(30)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("oil_medium")
                .inputFluids(RawOil, 2)
                .inputFluids(Air, 8)
                .duration(30)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("coal_tar")
                .inputFluids(CoalTar, 6)
                .inputFluids(Air, 6)
                .duration(24)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("sulfuric_heavy_fuel")
                .inputFluids(SulfuricHeavyFuel, 1)
                .inputFluids(Air, 5)
                .duration(20)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("glycerol")
                .inputFluids(Glycerol, 1)
                .inputFluids(Air, 20)
                .duration(82)
                .EUt(-V[ULV])
                .save();

        SEMI_FLUID_GENERATOR_FUELS.recipeBuilder("heavy_fuel")
                .inputFluids(HeavyFuel, 1)
                .inputFluids(Air, 22)
                .duration(90)
                .EUt(-V[ULV])
                .save();

        ROCKET_ENGINE_FUELS.recipeBuilder("rocket_engine_fuel_1")
                .inputFluids(RocketFuel, 10)
                .duration(20)
                .EUt(-512)
                .save();

        ROCKET_ENGINE_FUELS.recipeBuilder("rocket_engine_fuel_2")
                .inputFluids(GTOMaterials.RocketFuelRp1, 10)
                .duration(40)
                .EUt(-512)
                .save();

        ROCKET_ENGINE_FUELS.recipeBuilder("rocket_engine_fuel_3")
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture, 10)
                .duration(64)
                .EUt(-512)
                .save();

        ROCKET_ENGINE_FUELS.recipeBuilder("rocket_engine_fuel_4")
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3, 10)
                .duration(96)
                .EUt(-512)
                .save();

        ROCKET_ENGINE_FUELS.recipeBuilder("rocket_engine_fuel_5")
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4, 10)
                .duration(160)
                .EUt(-512)
                .save();

        ROCKET_ENGINE_FUELS.recipeBuilder("rocket_engine_fuel_6")
                .inputFluids(GTOMaterials.ExplosiveHydrazine, 10)
                .duration(320)
                .EUt(-512)
                .save();

        ROCKET_ENGINE_FUELS.recipeBuilder("rocket_engine_fuel_7")
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 10))
                .EUt(-512)
                .duration(480)
                .save();

        ROCKET_ENGINE_FUELS.recipeBuilder("rocket_engine_fuel_8")
                .inputFluids(GTOMaterials.StellarEnergyRocketFuel, 10)
                .duration(960)
                .EUt(-512)
                .save();

        NAQUADAH_REACTOR.recipeBuilder("naquadah")
                .inputFluids(Naquadah, 1)
                .duration(240)
                .EUt(-8192)
                .save();

        NAQUADAH_REACTOR.recipeBuilder("enriched_naquadah")
                .inputFluids(NaquadahEnriched, 1)
                .duration(360)
                .EUt(-8192)
                .save();

        NAQUADAH_REACTOR.recipeBuilder("naquadah_fuel")
                .inputFluids(GTOMaterials.NaquadahFuel, 1)
                .duration(2184)
                .EUt(-8192)
                .save();

        NAQUADAH_REACTOR.recipeBuilder("enriched_naquadah_fuel")
                .inputFluids(GTOMaterials.EnrichedNaquadahFuel, 1)
                .duration(3276)
                .EUt(-8192)
                .save();

        LARGE_NAQUADAH_REACTOR_RECIPES.recipeBuilder("naquadah_fuel")
                .inputFluids(GTOMaterials.NaquadahFuel, 16)
                .inputFluids(Hydrogen, 80)
                .duration(875)
                .EUt(-524288)
                .save();

        LARGE_NAQUADAH_REACTOR_RECIPES.recipeBuilder("enriched_naquadah_fuel")
                .inputFluids(GTOMaterials.EnrichedNaquadahFuel, 16)
                .inputFluids(Hydrogen, 80)
                .duration(1250)
                .EUt(-524288)
                .save();

        LARGE_NAQUADAH_REACTOR_RECIPES.recipeBuilder("naquadah_fuel1")
                .inputFluids(GTOMaterials.NaquadahFuel, 160)
                .inputFluids(Oxygen.getFluid(FluidStorageKeys.PLASMA, 40))
                .duration(16 * 875)
                .EUt(-524288)
                .save();

        LARGE_NAQUADAH_REACTOR_RECIPES.recipeBuilder("enriched_naquadah_fuel1")
                .inputFluids(GTOMaterials.EnrichedNaquadahFuel, 160)
                .inputFluids(Nitrogen.getFluid(FluidStorageKeys.PLASMA, 40))
                .duration(16 * 1250)
                .EUt(-524288)
                .save();

        HYPER_REACTOR_RECIPES.recipeBuilder("hyper_fuel_1")
                .inputFluids(GTOMaterials.HyperFuel1, 8)
                .inputFluids(Argon.getFluid(FluidStorageKeys.PLASMA, 4))
                .duration(200)
                .EUt(-V[UEV])
                .save();

        HYPER_REACTOR_RECIPES.recipeBuilder("hyper_fuel_2")
                .inputFluids(GTOMaterials.HyperFuel2, 8)
                .inputFluids(Iron.getFluid(FluidStorageKeys.PLASMA, 4))
                .duration(200)
                .EUt(-V[UXV])
                .save();

        HYPER_REACTOR_RECIPES.recipeBuilder("hyper_fuel_3")
                .inputFluids(GTOMaterials.HyperFuel3, 8)
                .inputFluids(Nickel.getFluid(FluidStorageKeys.PLASMA, 4))
                .duration(200)
                .EUt(-V[OpV])
                .save();

        HYPER_REACTOR_RECIPES.recipeBuilder("hyper_fuel_4")
                .inputFluids(GTOMaterials.HyperFuel4, 8)
                .inputFluids(GTOMaterials.DegenerateRhenium.getFluid(FluidStorageKeys.PLASMA, 1))
                .duration(200)
                .EUt(-V[MAX])
                .save();

        ADVANCED_HYPER_REACTOR_RECIPES.recipeBuilder("concentration_mixing_hyper_fuel_1")
                .inputFluids(GTOMaterials.ConcentrationMixingHyperFuel1, 16)
                .duration(200)
                .EUt(-16 * V[MAX])
                .save();

        ADVANCED_HYPER_REACTOR_RECIPES.recipeBuilder("concentration_mixing_hyper_fuel_2")
                .inputFluids(GTOMaterials.ConcentrationMixingHyperFuel2, 16)
                .duration(200)
                .EUt(-64 * V[MAX])
                .save();

        DYSON_SPHERE_RECIPES.recipeBuilder("a")
                .inputItems(GTOItems.DYSON_SWARM_MODULE, 64)
                .CWUt(512)
                .duration(200)
                .EUt(VA[UIV])
                .save();
    }
}
