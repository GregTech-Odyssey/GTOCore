package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraftforge.fluids.FluidStack;

import earth.terrarium.adastra.common.registry.ModFluids;

interface DrillingModule {

    static void init() {
        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("62_space_fluid_11"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(11)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.CarbonMonoxide.getFluid(1000000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("62_space_fluid_12"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(12)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Oxygen.getFluid(1000000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("62_space_fluid_10"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(10)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Fluorine.getFluid(1000000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("61_space_fluid_12"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(12)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Oxygen.getFluid(1000000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("61_space_fluid_10"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(10)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Fluorine.getFluid(1000000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("61_space_fluid_11"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(11)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.CarbonMonoxide.getFluid(1000000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("63_space_fluid_18"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(18)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Radon.getFluid(100000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("63_space_fluid_19"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(19)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Helium3.getFluid(100000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("63_space_fluid_16"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(16)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Krypton.getFluid(100000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("63_space_fluid_17"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(17)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Xenon.getFluid(100000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("63_space_fluid_14"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(14)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Neon.getFluid(100000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("63_space_fluid_15"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(15)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Argon.getFluid(100000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("63_space_fluid_13"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(13)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTOMaterials.UnknowWater.getFluid(10000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("68_space_fluid_30"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(64))
                .circuitMeta(30)
                .inputFluids(GTOMaterials.StellarEnergyRocketFuel.getFluid(10000))
                .outputFluids(GTOMaterials.BlackDwarfMatter.getFluid(100000))
                .EUt(503316480)
                .duration(750)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("68_space_fluid_29"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(64))
                .circuitMeta(29)
                .inputFluids(GTOMaterials.StellarEnergyRocketFuel.getFluid(10000))
                .outputFluids(GTOMaterials.WhiteDwarfMatter.getFluid(100000))
                .EUt(503316480)
                .duration(750)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("66_space_fluid_21"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(21)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.Tritium.getFluid(100000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("66_space_fluid_20"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(20)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.Deuterium.getFluid(100000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("66_space_fluid_25"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(25)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.RefineryGas.getFluid(100000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("66_space_fluid_24"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(24)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.Naphtha.getFluid(100000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("66_space_fluid_23"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(23)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.LightFuel.getFluid(100000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("66_space_fluid_22"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(22)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.HeavyFuel.getFluid(100000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("66_space_fluid_28"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(28)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTOMaterials.BarnardaAir.getFluid(100000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("66_space_fluid_27"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(27)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.Bromine.getFluid(100000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("66_space_fluid_26"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(26)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.CoalGas.getFluid(100000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("31_space_fluid_2"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(2)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Helium.getFluid(1000000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("31_space_fluid_3"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(3)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Nitrogen.getFluid(1000000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("31_space_fluid_4"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(4)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Methane.getFluid(1000000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("31_space_fluid_5"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(5)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.SulfurDioxide.getFluid(1000000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("31_space_fluid_6"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(6)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(1000000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("31_space_fluid_7"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(7)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.NitrogenDioxide.getFluid(1000000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("31_space_fluid_8"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(8)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Ammonia.getFluid(1000000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("31_space_fluid_9"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(9)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Chlorine.getFluid(1000000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("31_space_fluid_1"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(1)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("42_space_fluid_10"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(10)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Fluorine.getFluid(1000000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("42_space_fluid_11"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(11)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.CarbonMonoxide.getFluid(1000000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("42_space_fluid_12"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(12)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Oxygen.getFluid(1000000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("51_space_fluid_8"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(8)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Ammonia.getFluid(1000000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("51_space_fluid_9"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(9)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Chlorine.getFluid(1000000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("51_space_fluid_4"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(4)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Methane.getFluid(1000000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("51_space_fluid_5"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(5)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.SulfurDioxide.getFluid(1000000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("51_space_fluid_6"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(6)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(1000000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("51_space_fluid_7"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(7)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.NitrogenDioxide.getFluid(1000000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("51_space_fluid_1"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(1)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("51_space_fluid_2"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(2)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Helium.getFluid(1000000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("51_space_fluid_3"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(3)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Nitrogen.getFluid(1000000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("12_space_fluid_10"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(10)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Fluorine.getFluid(1000000))
                .EUt(491520)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("12_space_fluid_11"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(11)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.CarbonMonoxide.getFluid(1000000))
                .EUt(491520)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("12_space_fluid_12"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(12)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Oxygen.getFluid(1000000))
                .EUt(491520)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("22_space_fluid_2"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(2)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Helium.getFluid(1000000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("22_space_fluid_3"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(3)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Nitrogen.getFluid(1000000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("22_space_fluid_1"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("22_space_fluid_6"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(6)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(1000000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("22_space_fluid_7"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(7)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.NitrogenDioxide.getFluid(1000000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("22_space_fluid_4"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(4)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Methane.getFluid(1000000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("22_space_fluid_5"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(5)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.SulfurDioxide.getFluid(1000000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("22_space_fluid_8"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(8)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Ammonia.getFluid(1000000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("22_space_fluid_9"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(9)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Chlorine.getFluid(1000000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("23_space_fluid_18"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(18)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Radon.getFluid(100000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("23_space_fluid_19"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(19)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Helium3.getFluid(100000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("23_space_fluid_16"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(16)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Krypton.getFluid(100000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("23_space_fluid_17"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(17)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Xenon.getFluid(100000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("23_space_fluid_14"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(14)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Neon.getFluid(100000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("23_space_fluid_15"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(15)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Argon.getFluid(100000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("23_space_fluid_13"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(13)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTOMaterials.UnknowWater.getFluid(10000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("12_space_fluid_7"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(7)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.NitrogenDioxide.getFluid(1000000))
                .EUt(491520)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("12_space_fluid_8"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(8)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Ammonia.getFluid(1000000))
                .EUt(491520)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("12_space_fluid_9"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(9)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Chlorine.getFluid(1000000))
                .EUt(491520)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("12_space_fluid_1"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000000))
                .EUt(491520)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("12_space_fluid_2"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(2)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Helium.getFluid(1000000))
                .EUt(491520)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("12_space_fluid_3"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(3)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Nitrogen.getFluid(1000000))
                .EUt(491520)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("12_space_fluid_4"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(4)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Methane.getFluid(1000000))
                .EUt(491520)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("12_space_fluid_5"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(5)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.SulfurDioxide.getFluid(1000000))
                .EUt(491520)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("12_space_fluid_6"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(6)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(1000000))
                .EUt(491520)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("46_space_fluid_28"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(28)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTOMaterials.BarnardaAir.getFluid(100000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("46_space_fluid_20"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(20)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.Deuterium.getFluid(100000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("46_space_fluid_21"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(21)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.Tritium.getFluid(100000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("46_space_fluid_22"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(22)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.HeavyFuel.getFluid(100000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("46_space_fluid_23"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(23)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.LightFuel.getFluid(100000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("46_space_fluid_24"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(24)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.Naphtha.getFluid(100000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("46_space_fluid_25"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(25)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.RefineryGas.getFluid(100000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("46_space_fluid_26"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(26)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.CoalGas.getFluid(100000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("46_space_fluid_27"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(27)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.Bromine.getFluid(100000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("45_space_fluid_14"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(14)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Neon.getFluid(100000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("45_space_fluid_15"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(15)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Argon.getFluid(100000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("45_space_fluid_13"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(13)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTOMaterials.UnknowWater.getFluid(10000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("45_space_fluid_18"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(18)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Radon.getFluid(100000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("45_space_fluid_19"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(19)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Helium3.getFluid(100000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("45_space_fluid_16"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(16)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Krypton.getFluid(100000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("45_space_fluid_17"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(17)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Xenon.getFluid(100000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("62_space_fluid_5"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(5)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.SulfurDioxide.getFluid(1000000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("62_space_fluid_4"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(4)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Methane.getFluid(1000000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("62_space_fluid_7"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(7)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.NitrogenDioxide.getFluid(1000000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("62_space_fluid_6"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(6)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(1000000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("62_space_fluid_9"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(9)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Chlorine.getFluid(1000000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("62_space_fluid_8"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(8)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Ammonia.getFluid(1000000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("62_space_fluid_1"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("62_space_fluid_3"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(3)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Nitrogen.getFluid(1000000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("62_space_fluid_2"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(2)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Helium.getFluid(1000000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("31_space_fluid_10"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(10)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Fluorine.getFluid(1000000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("31_space_fluid_11"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(11)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.CarbonMonoxide.getFluid(1000000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("31_space_fluid_12"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(12)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Oxygen.getFluid(1000000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("21_space_fluid_7"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(7)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.NitrogenDioxide.getFluid(1000000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("21_space_fluid_8"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(8)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Ammonia.getFluid(1000000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("21_space_fluid_5"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(5)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.SulfurDioxide.getFluid(1000000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("21_space_fluid_6"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(6)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(1000000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("21_space_fluid_3"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(3)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Nitrogen.getFluid(1000000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("21_space_fluid_4"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(4)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Methane.getFluid(1000000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("21_space_fluid_1"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(1)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("21_space_fluid_2"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(2)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Helium.getFluid(1000000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("21_space_fluid_9"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(9)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Chlorine.getFluid(1000000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("52_space_fluid_2"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(2)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Helium.getFluid(1000000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("52_space_fluid_1"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("52_space_fluid_9"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(9)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Chlorine.getFluid(1000000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("52_space_fluid_8"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(8)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Ammonia.getFluid(1000000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("52_space_fluid_7"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(7)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.NitrogenDioxide.getFluid(1000000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("52_space_fluid_6"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(6)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(1000000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("52_space_fluid_5"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(5)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.SulfurDioxide.getFluid(1000000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("52_space_fluid_4"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(4)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Methane.getFluid(1000000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("52_space_fluid_3"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(3)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Nitrogen.getFluid(1000000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("35_space_fluid_17"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(17)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Xenon.getFluid(100000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("35_space_fluid_18"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(18)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Radon.getFluid(100000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("35_space_fluid_19"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(19)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Helium3.getFluid(100000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("35_space_fluid_13"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(13)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTOMaterials.UnknowWater.getFluid(10000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("35_space_fluid_14"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(14)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Neon.getFluid(100000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("35_space_fluid_15"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(15)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Argon.getFluid(100000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("35_space_fluid_16"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(16)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Krypton.getFluid(100000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("61_space_fluid_9"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(9)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Chlorine.getFluid(1000000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("61_space_fluid_7"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(7)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.NitrogenDioxide.getFluid(1000000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("61_space_fluid_8"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(8)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Ammonia.getFluid(1000000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("61_space_fluid_5"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(5)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.SulfurDioxide.getFluid(1000000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("61_space_fluid_6"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(6)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(1000000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("61_space_fluid_3"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(3)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Nitrogen.getFluid(1000000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("61_space_fluid_4"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(4)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Methane.getFluid(1000000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("61_space_fluid_1"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(1)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("61_space_fluid_2"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(2)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Helium.getFluid(1000000))
                .EUt(503316480)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("41_space_fluid_10"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(10)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Fluorine.getFluid(1000000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("41_space_fluid_11"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(11)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.CarbonMonoxide.getFluid(1000000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("41_space_fluid_12"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(12)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Oxygen.getFluid(1000000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("67_space_fluid_24"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(24)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.Naphtha.getFluid(100000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("67_space_fluid_23"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(23)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.LightFuel.getFluid(100000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("67_space_fluid_26"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(26)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.CoalGas.getFluid(100000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("67_space_fluid_25"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(25)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.RefineryGas.getFluid(100000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("67_space_fluid_20"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(20)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.Deuterium.getFluid(100000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("67_space_fluid_22"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(22)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.HeavyFuel.getFluid(100000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("67_space_fluid_21"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(21)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.Tritium.getFluid(100000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("67_space_fluid_28"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(28)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTOMaterials.BarnardaAir.getFluid(100000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("67_space_fluid_27"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(27)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.Bromine.getFluid(100000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("52_space_fluid_10"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(10)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Fluorine.getFluid(1000000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("52_space_fluid_11"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(11)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.CarbonMonoxide.getFluid(1000000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("52_space_fluid_12"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(12)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Oxygen.getFluid(1000000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("56_space_fluid_28"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(28)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTOMaterials.BarnardaAir.getFluid(100000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("56_space_fluid_27"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(27)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.Bromine.getFluid(100000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("56_space_fluid_20"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(20)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.Deuterium.getFluid(100000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("56_space_fluid_22"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(22)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.HeavyFuel.getFluid(100000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("56_space_fluid_21"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(21)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.Tritium.getFluid(100000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("56_space_fluid_24"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(24)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.Naphtha.getFluid(100000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("56_space_fluid_23"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(23)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.LightFuel.getFluid(100000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("56_space_fluid_26"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(26)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.CoalGas.getFluid(100000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("56_space_fluid_25"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(25)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.RefineryGas.getFluid(100000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("51_space_fluid_10"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(10)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Fluorine.getFluid(1000000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("51_space_fluid_11"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(11)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.CarbonMonoxide.getFluid(1000000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("51_space_fluid_12"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(12)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Oxygen.getFluid(1000000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("57_space_fluid_23"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(23)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.LightFuel.getFluid(100000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("57_space_fluid_22"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(22)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.HeavyFuel.getFluid(100000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("57_space_fluid_21"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(21)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.Tritium.getFluid(100000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("57_space_fluid_20"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(20)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.Deuterium.getFluid(100000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("57_space_fluid_27"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(27)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.Bromine.getFluid(100000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("57_space_fluid_26"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(26)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.CoalGas.getFluid(100000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("57_space_fluid_25"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(25)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.RefineryGas.getFluid(100000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("57_space_fluid_24"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(24)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.Naphtha.getFluid(100000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("55_space_fluid_15"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(15)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Argon.getFluid(100000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("55_space_fluid_16"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(16)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Krypton.getFluid(100000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("55_space_fluid_17"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(17)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Xenon.getFluid(100000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("57_space_fluid_28"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(28)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTOMaterials.BarnardaAir.getFluid(100000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("55_space_fluid_18"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(18)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Radon.getFluid(100000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("55_space_fluid_19"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(19)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Helium3.getFluid(100000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("55_space_fluid_13"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(13)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTOMaterials.UnknowWater.getFluid(10000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("55_space_fluid_14"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(14)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Neon.getFluid(100000))
                .EUt(125829120)
                .duration(18)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("53_space_fluid_17"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(17)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Xenon.getFluid(100000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("53_space_fluid_18"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(18)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Radon.getFluid(100000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("53_space_fluid_19"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(19)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Helium3.getFluid(100000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("53_space_fluid_13"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(13)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTOMaterials.UnknowWater.getFluid(10000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("53_space_fluid_14"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(14)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Neon.getFluid(100000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("53_space_fluid_15"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(15)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Argon.getFluid(100000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("53_space_fluid_16"))
                .notConsumable(GTOItems.SPACE_DRONE_MK5.asStack(16))
                .circuitMeta(16)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Krypton.getFluid(100000))
                .EUt(125829120)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("22_space_fluid_11"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(11)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.CarbonMonoxide.getFluid(1000000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("22_space_fluid_12"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(12)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Oxygen.getFluid(1000000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("22_space_fluid_10"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(10)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Fluorine.getFluid(1000000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("36_space_fluid_20"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(20)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.Deuterium.getFluid(100000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("36_space_fluid_27"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(27)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.Bromine.getFluid(100000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("36_space_fluid_28"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(28)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTOMaterials.BarnardaAir.getFluid(100000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("36_space_fluid_25"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(25)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.RefineryGas.getFluid(100000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("36_space_fluid_26"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(26)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.CoalGas.getFluid(100000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("36_space_fluid_23"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(23)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.LightFuel.getFluid(100000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("36_space_fluid_24"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(24)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.Naphtha.getFluid(100000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("36_space_fluid_21"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(21)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.Tritium.getFluid(100000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("36_space_fluid_22"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(22)
                .inputFluids(GTOMaterials.RocketFuelH8n4c2o4.getFluid(10000))
                .outputFluids(GTMaterials.HeavyFuel.getFluid(100000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("21_space_fluid_12"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(12)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Oxygen.getFluid(1000000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("21_space_fluid_10"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(10)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Fluorine.getFluid(1000000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("21_space_fluid_11"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(11)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.CarbonMonoxide.getFluid(1000000))
                .EUt(1966080)
                .duration(300)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("32_space_fluid_9"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(9)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Chlorine.getFluid(1000000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("32_space_fluid_5"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(5)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.SulfurDioxide.getFluid(1000000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("32_space_fluid_6"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(6)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(1000000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("32_space_fluid_7"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(7)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.NitrogenDioxide.getFluid(1000000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("32_space_fluid_8"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(8)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Ammonia.getFluid(1000000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("32_space_fluid_1"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("32_space_fluid_2"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(2)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Helium.getFluid(1000000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("32_space_fluid_3"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(3)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Nitrogen.getFluid(1000000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("32_space_fluid_4"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(4)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Methane.getFluid(1000000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("42_space_fluid_1"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("42_space_fluid_8"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(8)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Ammonia.getFluid(1000000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("42_space_fluid_9"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(9)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Chlorine.getFluid(1000000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("42_space_fluid_6"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(6)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(1000000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("42_space_fluid_7"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(7)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.NitrogenDioxide.getFluid(1000000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("42_space_fluid_4"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(4)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Methane.getFluid(1000000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("42_space_fluid_5"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(5)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.SulfurDioxide.getFluid(1000000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("42_space_fluid_2"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(2)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Helium.getFluid(1000000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("42_space_fluid_3"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(3)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Nitrogen.getFluid(1000000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("25_space_fluid_18"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(18)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Radon.getFluid(100000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("25_space_fluid_19"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(19)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Helium3.getFluid(100000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("25_space_fluid_13"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(13)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTOMaterials.UnknowWater.getFluid(10000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("25_space_fluid_16"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(16)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Krypton.getFluid(100000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("25_space_fluid_17"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(17)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Xenon.getFluid(100000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("25_space_fluid_14"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(14)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Neon.getFluid(100000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("25_space_fluid_15"))
                .notConsumable(GTOItems.SPACE_DRONE_MK2.asStack(16))
                .circuitMeta(15)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Argon.getFluid(100000))
                .EUt(1966080)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("47_space_fluid_20"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(20)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.Deuterium.getFluid(100000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("47_space_fluid_22"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(22)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.HeavyFuel.getFluid(100000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("47_space_fluid_21"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(21)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.Tritium.getFluid(100000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("47_space_fluid_24"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(24)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.Naphtha.getFluid(100000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("47_space_fluid_23"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(23)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.LightFuel.getFluid(100000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("47_space_fluid_26"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(26)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.CoalGas.getFluid(100000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("47_space_fluid_25"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(25)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.RefineryGas.getFluid(100000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("47_space_fluid_28"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(28)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTOMaterials.BarnardaAir.getFluid(100000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("47_space_fluid_27"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(27)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.Bromine.getFluid(100000))
                .EUt(31457280)
                .duration(37)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("32_space_fluid_12"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(12)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Oxygen.getFluid(1000000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("32_space_fluid_10"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(10)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.Fluorine.getFluid(1000000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("32_space_fluid_11"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(11)
                .inputFluids(GTOMaterials.RocketFuelRp1.getFluid(6000))
                .outputFluids(GTMaterials.CarbonMonoxide.getFluid(1000000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("11_space_fluid_9"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(9)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Chlorine.getFluid(1000000))
                .EUt(491520)
                .duration(600)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("11_space_fluid_8"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(8)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Ammonia.getFluid(1000000))
                .EUt(491520)
                .duration(600)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("11_space_fluid_5"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(5)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.SulfurDioxide.getFluid(1000000))
                .EUt(491520)
                .duration(600)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("11_space_fluid_4"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(4)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Methane.getFluid(1000000))
                .EUt(491520)
                .duration(600)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("11_space_fluid_7"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(7)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.NitrogenDioxide.getFluid(1000000))
                .EUt(491520)
                .duration(600)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("11_space_fluid_6"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(6)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(1000000))
                .EUt(491520)
                .duration(600)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("11_space_fluid_1"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(1)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000000))
                .EUt(491520)
                .duration(600)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("11_space_fluid_3"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(3)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Nitrogen.getFluid(1000000))
                .EUt(491520)
                .duration(600)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("11_space_fluid_2"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(2)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Helium.getFluid(1000000))
                .EUt(491520)
                .duration(600)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("33_space_fluid_13"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(13)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTOMaterials.UnknowWater.getFluid(10000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("33_space_fluid_14"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(14)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Neon.getFluid(100000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("33_space_fluid_15"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(15)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Argon.getFluid(100000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("33_space_fluid_16"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(16)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Krypton.getFluid(100000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("33_space_fluid_17"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(17)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Xenon.getFluid(100000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("33_space_fluid_18"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(18)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Radon.getFluid(100000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("33_space_fluid_19"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(19)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Helium3.getFluid(100000))
                .EUt(7864320)
                .duration(150)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("37_space_fluid_22"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(22)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.HeavyFuel.getFluid(100000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("37_space_fluid_23"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(23)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.LightFuel.getFluid(100000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("37_space_fluid_24"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(24)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.Naphtha.getFluid(100000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("37_space_fluid_25"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(25)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.RefineryGas.getFluid(100000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("37_space_fluid_26"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(26)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.CoalGas.getFluid(100000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("37_space_fluid_27"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(27)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.Bromine.getFluid(100000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("37_space_fluid_28"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(28)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTOMaterials.BarnardaAir.getFluid(100000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("37_space_fluid_20"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(20)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.Deuterium.getFluid(100000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("37_space_fluid_21"))
                .notConsumable(GTOItems.SPACE_DRONE_MK3.asStack(16))
                .circuitMeta(21)
                .inputFluids(new FluidStack(ModFluids.CRYO_FUEL.get(), 6000))
                .outputFluids(GTMaterials.Tritium.getFluid(100000))
                .EUt(7864320)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("41_space_fluid_5"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(5)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.SulfurDioxide.getFluid(1000000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("41_space_fluid_6"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(6)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.CarbonDioxide.getFluid(1000000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("41_space_fluid_3"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(3)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Nitrogen.getFluid(1000000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("41_space_fluid_4"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(4)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Methane.getFluid(1000000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("41_space_fluid_9"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(9)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Chlorine.getFluid(1000000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("41_space_fluid_7"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(7)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.NitrogenDioxide.getFluid(1000000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("41_space_fluid_8"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(8)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Ammonia.getFluid(1000000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("41_space_fluid_1"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(1)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Hydrogen.getFluid(1000000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("41_space_fluid_2"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(2)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Helium.getFluid(1000000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("43_space_fluid_13"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(13)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTOMaterials.UnknowWater.getFluid(10000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("43_space_fluid_16"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(16)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Krypton.getFluid(100000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("43_space_fluid_17"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(17)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Xenon.getFluid(100000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("43_space_fluid_14"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(14)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Neon.getFluid(100000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("43_space_fluid_15"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(15)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Argon.getFluid(100000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("43_space_fluid_18"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(18)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Radon.getFluid(100000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("43_space_fluid_19"))
                .notConsumable(GTOItems.SPACE_DRONE_MK4.asStack(16))
                .circuitMeta(19)
                .inputFluids(GTOMaterials.DenseHydrazineFuelMixture.getFluid(10000))
                .outputFluids(GTMaterials.Helium3.getFluid(100000))
                .EUt(31457280)
                .duration(75)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("65_space_fluid_15"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(15)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Argon.getFluid(100000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("65_space_fluid_14"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(14)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Neon.getFluid(100000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("65_space_fluid_17"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(17)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Xenon.getFluid(100000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("65_space_fluid_16"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(16)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Krypton.getFluid(100000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("65_space_fluid_19"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(19)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Helium3.getFluid(100000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("65_space_fluid_18"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(18)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTMaterials.Radon.getFluid(100000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("65_space_fluid_13"))
                .notConsumable(GTOItems.SPACE_DRONE_MK6.asStack(16))
                .circuitMeta(13)
                .inputFluids(GTOMaterials.RocketFuelCn3h7o3.getFluid(6000))
                .outputFluids(GTOMaterials.UnknowWater.getFluid(10000))
                .EUt(503316480)
                .duration(9)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("11_space_fluid_10"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(10)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Fluorine.getFluid(1000000))
                .EUt(491520)
                .duration(600)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("11_space_fluid_12"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(12)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.Oxygen.getFluid(1000000))
                .EUt(491520)
                .duration(600)
                .save();

        GTORecipeTypes.DRILLING_MODULE_RECIPES.recipeBuilder(GTOCore.id("11_space_fluid_11"))
                .notConsumable(GTOItems.SPACE_DRONE_MK1.asStack(16))
                .circuitMeta(11)
                .inputFluids(GTMaterials.RocketFuel.getFluid(10000))
                .outputFluids(GTMaterials.CarbonMonoxide.getFluid(1000000))
                .EUt(491520)
                .duration(600)
                .save();
    }
}
