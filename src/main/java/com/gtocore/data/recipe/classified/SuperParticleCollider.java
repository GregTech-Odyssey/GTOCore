package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES;

final class SuperParticleCollider {

    public static void init() {
        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("antimatter")
                .inputFluids(GTOMaterials.Antihydrogen, 2000)
                .inputFluids(GTOMaterials.Antineutron, 2000)
                .outputFluids(GTOMaterials.Antimatter, 100)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("antineutron")
                .inputFluids(GTOMaterials.PositiveElectron, 100)
                .inputFluids(GTOMaterials.Antiproton, 100)
                .outputFluids(GTOMaterials.Antineutron, 2)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("roentgeniuma")
                .inputFluids(GTMaterials.Meitnerium, 4096)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Roentgenium, 4000)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("positive_electron")
                .inputFluids(GTMaterials.Phosphorus, 200)
                .inputFluids(GTMaterials.Lithium, 200)
                .outputFluids(GTOMaterials.PositiveElectron, 100)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("antiproton")
                .inputFluids(GTOMaterials.LiquidHydrogen, 1000)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 200))
                .outputFluids(GTOMaterials.Antiproton, 100)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("nihoniuma")
                .inputFluids(GTMaterials.Roentgenium, 4096)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Nihonium, 4000)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("einsteiniuma")
                .inputFluids(GTMaterials.Curium, 4096)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Einsteinium, 4000)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("nobeliuma")
                .inputFluids(GTMaterials.Fermium, 4096)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Nobelium, 4000)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("californiuma")
                .inputFluids(GTMaterials.Berkelium, 4096)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Californium, 4000)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("plutoniuma")
                .inputFluids(GTMaterials.Uranium238, 4096)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Plutonium239, 4000)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("berkeliuma")
                .inputFluids(GTMaterials.Americium, 4096)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Berkelium, 4000)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("curiuma")
                .inputFluids(GTMaterials.Plutonium239, 4096)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Curium, 4000)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("neptuniuma")
                .inputFluids(GTMaterials.Protactinium, 4096)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Neptunium, 4000)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("uraniuma")
                .inputFluids(GTMaterials.Thorium, 4096)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Uranium238, 4000)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("astatinea")
                .inputFluids(GTMaterials.Bismuth, 4096)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Astatine, 4000)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("lawrenciuma")
                .inputFluids(GTMaterials.Mendelevium, 4096)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Lawrencium, 4000)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("mendeleviuma")
                .inputFluids(GTMaterials.Einsteinium, 4096)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Mendelevium, 4000)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("coperniciuma")
                .inputFluids(GTMaterials.Darmstadtium, 4096)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Copernicium, 4000)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("bohriuma")
                .inputFluids(GTMaterials.Dubnium, 4096)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Bohrium, 4000)
                .EUt(491520)
                .duration(200)
                .save();

        SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder("fermiuma")
                .inputFluids(GTMaterials.Californium, 4096)
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Fermium, 4000)
                .EUt(491520)
                .duration(200)
                .save();
    }
}
