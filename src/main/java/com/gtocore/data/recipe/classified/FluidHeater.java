package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOFluids;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraftforge.fluids.FluidStack;

import static com.gtocore.common.data.GTORecipeTypes.FLUID_HEATER_RECIPES;

final class FluidHeater {

    public static void init() {
        FLUID_HEATER_RECIPES.recipeBuilder("supercritical_carbon_dioxide")
                .inputFluids(GTMaterials.CarbonDioxide, 1000)
                .outputFluids(GTOMaterials.SupercriticalCarbonDioxide, 1000)
                .EUt(480)
                .duration(40000)
                .save();

        FLUID_HEATER_RECIPES.recipeBuilder("azafullerene")
                .notConsumable(TagPrefix.dustTiny, GTMaterials.Rhenium, 36)
                .inputFluids(GTOMaterials.AminatedFullerene, 100)
                .outputFluids(GTOMaterials.Azafullerene, 100)
                .EUt(480)
                .duration(120)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        FLUID_HEATER_RECIPES.recipeBuilder("biohmediumsterilized")
                .inputFluids(GTOMaterials.BiomediumRaw, 100)
                .outputFluids(GTOMaterials.BiohmediumSterilized, 100)
                .EUt(480)
                .duration(400)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        FLUID_HEATER_RECIPES.recipeBuilder("bedrock_gas")
                .inputFluids(GTOMaterials.CleanBedrockSolution, 1000)
                .outputFluids(GTOMaterials.BedrockGas, 1000)
                .EUt(7864320)
                .duration(200)
                .save();

        FLUID_HEATER_RECIPES.recipeBuilder("heater_germanium_tetrachloride_solution")
                .inputFluids(GTOMaterials.GermaniumTetrachlorideSolution, 1000)
                .outputFluids(GTOMaterials.GermaniumTetrachlorideSolution.getFluid(FluidStorageKeys.GAS, 1000))
                .EUt(30)
                .duration(20)
                .save();

        FLUID_HEATER_RECIPES.recipeBuilder("fire_water")
                .inputItems(TagPrefix.dustTiny, GTMaterials.Blaze)
                .inputFluids(new FluidStack(GTOFluids.HOOTCH.getSource(), 1000))
                .outputFluids(new FluidStack(GTOFluids.FIRE_WATER.getSource(), 1000))
                .EUt(120)
                .duration(40)
                .save();

        FLUID_HEATER_RECIPES.recipeBuilder("cloud_seed")
                .inputItems(GTOItems.GOLD_ALGAE, 4)
                .inputFluids(GTOMaterials.CoolantLiquid, 1000)
                .outputFluids(new FluidStack(GTOFluids.CLOUD_SEED.getSource(), 1000))
                .EUt(30)
                .duration(300)
                .save();
    }
}
