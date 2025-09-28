package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import earth.terrarium.adastra.common.registry.ModFluids;

import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOMaterials.BarnardaAir;
import static com.gtocore.common.data.GTOMaterials.UnknowWater;
import static com.gtocore.common.data.GTORecipeTypes.DRILLING_MODULE_RECIPES;

final class DrillingModule {

    private static final ItemStack[] drones = {
            GTOItems.SPACE_DRONE_MK1.asStack(16), GTOItems.SPACE_DRONE_MK2.asStack(16), GTOItems.SPACE_DRONE_MK3.asStack(16),
            GTOItems.SPACE_DRONE_MK4.asStack(16), GTOItems.SPACE_DRONE_MK5.asStack(16), GTOItems.SPACE_DRONE_MK6.asStack(16)
    };
    private static final FluidStack[][] fuels = {
            { GTMaterials.RocketFuel.getFluid(10000), GTOMaterials.RocketFuelRp1.getFluid(6000) },
            { GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000), GTOMaterials.RocketFuelCn3h7o3.getFluid(6000) },
            { GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000), new FluidStack(ModFluids.CRYO_FUEL.get(), 6000) },
            {},
            {},
            {}
    };
    private static final int[] baseTime = { 600, 400 };

    private static void buildDrillingModule(int circuit, int minDroneTire, FluidStack fluidStack) {
        for (int i = minDroneTire - 1; i < 6; i++) {
            for (int j = 0; j < fuels[minDroneTire - 1].length; j++) {
                DRILLING_MODULE_RECIPES.recipeBuilder("space_fluid_" + circuit + "_" + i + "_" + j)
                        .notConsumable(drones[i])
                        .circuitMeta(circuit)
                        .inputFluids(fuels[minDroneTire - 1][j])
                        .outputFluids(fluidStack)
                        .EUt(VA[8 + i])
                        .duration(baseTime[j] << i)
                        .save();
            }
        }
    }

    public static void init() {
        buildDrillingModule(1, 1, Hydrogen.getFluid(1000000));
        buildDrillingModule(2, 1, Helium.getFluid(1000000));
        buildDrillingModule(3, 1, Nitrogen.getFluid(1000000));
        buildDrillingModule(4, 1, Methane.getFluid(1000000));
        buildDrillingModule(5, 1, SulfurDioxide.getFluid(1000000));
        buildDrillingModule(6, 1, CarbonDioxide.getFluid(1000000));
        buildDrillingModule(7, 1, NitrogenDioxide.getFluid(1000000));
        buildDrillingModule(8, 1, Ammonia.getFluid(1000000));
        buildDrillingModule(9, 1, Chlorine.getFluid(1000000));
        buildDrillingModule(10, 1, Fluorine.getFluid(1000000));
        buildDrillingModule(11, 1, CarbonMonoxide.getFluid(1000000));
        buildDrillingModule(12, 1, Oxygen.getFluid(1000000));
        buildDrillingModule(13, 2, UnknowWater.getFluid(1000000));
        buildDrillingModule(14, 2, Neon.getFluid(1000000));
        buildDrillingModule(15, 2, Argon.getFluid(1000000));
        buildDrillingModule(16, 2, Krypton.getFluid(1000000));
        buildDrillingModule(17, 2, Xenon.getFluid(1000000));
        buildDrillingModule(18, 2, Radon.getFluid(1000000));
        buildDrillingModule(19, 2, Helium3.getFluid(1000000));
        buildDrillingModule(20, 3, Deuterium.getFluid(1000000));
        buildDrillingModule(21, 3, Tritium.getFluid(1000000));
        buildDrillingModule(22, 3, HeavyFuel.getFluid(1000000));
        buildDrillingModule(23, 3, LightFuel.getFluid(1000000));
        buildDrillingModule(24, 3, Naphtha.getFluid(1000000));
        buildDrillingModule(25, 3, RefineryGas.getFluid(1000000));
        buildDrillingModule(26, 3, CoalGas.getFluid(1000000));
        buildDrillingModule(27, 3, Bromine.getFluid(1000000));
        buildDrillingModule(28, 3, BarnardaAir.getFluid(1000000));

        DRILLING_MODULE_RECIPES.recipeBuilder("space_fluid_29")
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(64))
                .circuitMeta(29)
                .inputFluids(GTOMaterials.StellarEnergyRocketFuel.getFluid(10000))
                .outputFluids(GTOMaterials.WhiteDwarfMatter.getFluid(100000))
                .EUt(503316480)
                .duration(750)
                .save();

        DRILLING_MODULE_RECIPES.recipeBuilder("space_fluid_30")
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(64))
                .circuitMeta(30)
                .inputFluids(GTOMaterials.StellarEnergyRocketFuel.getFluid(10000))
                .outputFluids(GTOMaterials.BlackDwarfMatter.getFluid(100000))
                .EUt(503316480)
                .duration(750)
                .save();
    }
}
