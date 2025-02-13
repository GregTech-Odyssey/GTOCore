package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.init.GTORecipeTypes;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface PetrochemicalPlant {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.PETROCHEMICAL_PLANT_RECIPES.recipeBuilder(GTOCore.id("petrochemical_plant_3"))
                .inputFluids(GTMaterials.OilHeavy.getFluid(1000))
                .inputFluids(GTMaterials.Steam.getFluid(1000))
                .outputFluids(GTMaterials.Toluene.getFluid(240))
                .outputFluids(GTMaterials.Benzene.getFluid(1200))
                .outputFluids(GTMaterials.Octane.getFluid(20))
                .outputFluids(GTMaterials.Butane.getFluid(60))
                .outputFluids(GTMaterials.Butene.getFluid(240))
                .outputFluids(GTMaterials.Butadiene.getFluid(150))
                .outputFluids(GTMaterials.Propane.getFluid(30))
                .outputFluids(GTMaterials.Propene.getFluid(300))
                .outputFluids(GTMaterials.Ethane.getFluid(45))
                .outputFluids(GTMaterials.Ethylene.getFluid(450))
                .outputFluids(GTMaterials.Methane.getFluid(450))
                .outputFluids(GTMaterials.Helium.getFluid(10))
                .EUt(1920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.PETROCHEMICAL_PLANT_RECIPES.recipeBuilder(GTOCore.id("petrochemical_plant_4"))
                .inputFluids(GTMaterials.OilLight.getFluid(1000))
                .inputFluids(GTMaterials.Steam.getFluid(1000))
                .outputFluids(GTMaterials.Toluene.getFluid(20))
                .outputFluids(GTMaterials.Benzene.getFluid(100))
                .outputFluids(GTMaterials.Octane.getFluid(20))
                .outputFluids(GTMaterials.Butane.getFluid(120))
                .outputFluids(GTMaterials.Butene.getFluid(80))
                .outputFluids(GTMaterials.Butadiene.getFluid(80))
                .outputFluids(GTMaterials.Propane.getFluid(140))
                .outputFluids(GTMaterials.Propene.getFluid(90))
                .outputFluids(GTMaterials.Ethane.getFluid(200))
                .outputFluids(GTMaterials.Ethylene.getFluid(250))
                .outputFluids(GTMaterials.Methane.getFluid(2000))
                .outputFluids(GTMaterials.Helium.getFluid(40))
                .EUt(1920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.PETROCHEMICAL_PLANT_RECIPES.recipeBuilder(GTOCore.id("petrochemical_plant_1"))
                .inputFluids(GTMaterials.Oil.getFluid(1000))
                .inputFluids(GTMaterials.Steam.getFluid(1000))
                .outputFluids(GTMaterials.Toluene.getFluid(60))
                .outputFluids(GTMaterials.Benzene.getFluid(180))
                .outputFluids(GTMaterials.Octane.getFluid(60))
                .outputFluids(GTMaterials.Butane.getFluid(80))
                .outputFluids(GTMaterials.Butene.getFluid(100))
                .outputFluids(GTMaterials.Butadiene.getFluid(90))
                .outputFluids(GTMaterials.Propane.getFluid(80))
                .outputFluids(GTMaterials.Propene.getFluid(400))
                .outputFluids(GTMaterials.Ethane.getFluid(80))
                .outputFluids(GTMaterials.Ethylene.getFluid(400))
                .outputFluids(GTMaterials.Methane.getFluid(400))
                .outputFluids(GTMaterials.Helium.getFluid(20))
                .EUt(1920)
                .duration(200)
                .save(provider);

        GTORecipeTypes.PETROCHEMICAL_PLANT_RECIPES.recipeBuilder(GTOCore.id("petrochemical_plant_2"))
                .inputFluids(GTMaterials.RawOil.getFluid(1000))
                .inputFluids(GTMaterials.Steam.getFluid(1000))
                .outputFluids(GTMaterials.Toluene.getFluid(40))
                .outputFluids(GTMaterials.Benzene.getFluid(200))
                .outputFluids(GTMaterials.Octane.getFluid(30))
                .outputFluids(GTMaterials.Butane.getFluid(70))
                .outputFluids(GTMaterials.Butene.getFluid(100))
                .outputFluids(GTMaterials.Butadiene.getFluid(100))
                .outputFluids(GTMaterials.Propane.getFluid(30))
                .outputFluids(GTMaterials.Propene.getFluid(600))
                .outputFluids(GTMaterials.Ethane.getFluid(130))
                .outputFluids(GTMaterials.Ethylene.getFluid(1000))
                .outputFluids(GTMaterials.Methane.getFluid(1000))
                .outputFluids(GTMaterials.Helium.getFluid(10))
                .EUt(1920)
                .duration(200)
                .save(provider);
    }
}
