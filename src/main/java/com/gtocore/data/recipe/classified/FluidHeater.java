package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraftforge.fluids.FluidStack;

import com.enderio.base.common.init.EIOFluids;

import static com.gtocore.common.data.GTORecipeTypes.FLUID_HEATER_RECIPES;

final class FluidHeater {

    public static void init() {
        FLUID_HEATER_RECIPES.recipeBuilder("supercritical_carbon_dioxide")
                .inputFluids(GTMaterials.CarbonDioxide.getFluid(1000))
                .outputFluids(GTOMaterials.SupercriticalCarbonDioxide.getFluid(1000))
                .EUt(480)
                .duration(40000)
                .save();

        FLUID_HEATER_RECIPES.recipeBuilder("azafullerene")
                .notConsumable(TagPrefix.dustTiny, GTMaterials.Rhenium, 36)
                .inputFluids(GTOMaterials.AminatedFullerene.getFluid(100))
                .outputFluids(GTOMaterials.Azafullerene.getFluid(100))
                .EUt(480)
                .duration(120)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        FLUID_HEATER_RECIPES.recipeBuilder("biohmediumsterilized")
                .inputFluids(GTOMaterials.BiomediumRaw.getFluid(100))
                .outputFluids(GTOMaterials.BiohmediumSterilized.getFluid(100))
                .EUt(480)
                .duration(400)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        FLUID_HEATER_RECIPES.recipeBuilder("bedrock_gas")
                .inputFluids(GTOMaterials.CleanBedrockSolution.getFluid(1000))
                .outputFluids(GTOMaterials.BedrockGas.getFluid(1000))
                .EUt(7864320)
                .duration(200)
                .save();

        FLUID_HEATER_RECIPES.recipeBuilder("heater_germanium_tetrachloride_solution")
                .inputFluids(GTOMaterials.GermaniumTetrachlorideSolution.getFluid(1000))
                .outputFluids(GTOMaterials.GermaniumTetrachlorideSolution.getFluid(FluidStorageKeys.GAS, 1000))
                .EUt(30)
                .duration(20)
                .save();

        FLUID_HEATER_RECIPES.recipeBuilder("fire_water")
                .inputItems(TagPrefix.dustTiny, GTMaterials.Blaze)
                .inputFluids(new FluidStack(EIOFluids.HOOTCH.getSource(), 1000))
                .outputFluids(new FluidStack(EIOFluids.FIRE_WATER.getSource(), 1000))
                .EUt(120)
                .duration(40)
                .save();

        FLUID_HEATER_RECIPES.recipeBuilder("cloud_seed")
                .inputItems(GTOItems.GOLD_ALGAE.asItem(), 4)
                .inputFluids(GTOMaterials.CoolantLiquid.getFluid(1000))
                .outputFluids(new FluidStack(EIOFluids.CLOUD_SEED.getSource(), 1000))
                .EUt(30)
                .duration(300)
                .save();
    }
}
