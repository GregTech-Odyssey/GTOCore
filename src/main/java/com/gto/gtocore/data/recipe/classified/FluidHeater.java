package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;
import com.gregtechceu.gtceu.common.data.GTMaterials;

interface FluidHeater {

    static void init() {
        GTORecipeTypes.FLUID_HEATER_RECIPES.recipeBuilder(GTOCore.id("supercritical_carbon_dioxide"))
                .inputFluids(GTMaterials.CarbonDioxide.getFluid(1000))
                .outputFluids(GTOMaterials.SupercriticalCarbonDioxide.getFluid(1000))
                .EUt(480)
                .duration(200)
                .save();

        GTORecipeTypes.FLUID_HEATER_RECIPES.recipeBuilder(GTOCore.id("azafullerene"))
                .notConsumable(TagPrefix.dustTiny, GTMaterials.Rhenium, 36)
                .inputFluids(GTOMaterials.AminatedFullerene.getFluid(100))
                .outputFluids(GTOMaterials.Azafullerene.getFluid(100))
                .EUt(480)
                .duration(120)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        GTORecipeTypes.FLUID_HEATER_RECIPES.recipeBuilder(GTOCore.id("biohmediumsterilized"))
                .inputFluids(GTOMaterials.BiomediumRaw.getFluid(100))
                .outputFluids(GTOMaterials.BiohmediumSterilized.getFluid(100))
                .EUt(480)
                .duration(400)
                .cleanroom(CleanroomType.STERILE_CLEANROOM)
                .save();

        GTORecipeTypes.FLUID_HEATER_RECIPES.recipeBuilder(GTOCore.id("bedrock_gas"))
                .inputFluids(GTOMaterials.CleanBedrockSolution.getFluid(1000))
                .outputFluids(GTOMaterials.BedrockGas.getFluid(1000))
                .EUt(31457280)
                .duration(100)
                .save();

        GTORecipeTypes.FLUID_HEATER_RECIPES.recipeBuilder(GTOCore.id("heater_germanium_tetrachloride_solution"))
                .inputFluids(GTOMaterials.GermaniumTetrachlorideSolution.getFluid(1000))
                .outputFluids(GTOMaterials.GermaniumTetrachlorideSolution.getFluid(FluidStorageKeys.GAS, 1000))
                .EUt(30)
                .duration(20)
                .save();
    }
}
