package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.init.GTOMaterials;
import com.gto.gtocore.init.GTORecipeTypes;

import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface SuperParticleCollider {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("antimatter"))
                .inputFluids(GTOMaterials.Antihydrogen.getFluid(2000))
                .inputFluids(GTOMaterials.Antineutron.getFluid(2000))
                .outputFluids(GTOMaterials.Antimatter.getFluid(100))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("antineutron"))
                .inputFluids(GTOMaterials.PositiveElectron.getFluid(100))
                .inputFluids(GTOMaterials.Antiproton.getFluid(100))
                .outputFluids(GTOMaterials.Antineutron.getFluid(2))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("roentgeniuma"))
                .inputFluids(GTMaterials.Meitnerium.getFluid(4096))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Roentgenium.getFluid(4000))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("positive_electron"))
                .inputFluids(GTMaterials.Phosphorus.getFluid(200))
                .inputFluids(GTMaterials.Lithium.getFluid(200))
                .outputFluids(GTOMaterials.PositiveElectron.getFluid(100))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("antiproton"))
                .inputFluids(GTOMaterials.LiquidHydrogen.getFluid(1000))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 200))
                .outputFluids(GTOMaterials.Antiproton.getFluid(100))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("nihoniuma"))
                .inputFluids(GTMaterials.Roentgenium.getFluid(4096))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Nihonium.getFluid(4000))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("einsteiniuma"))
                .inputFluids(GTMaterials.Curium.getFluid(4096))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Einsteinium.getFluid(4000))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("nobeliuma"))
                .inputFluids(GTMaterials.Fermium.getFluid(4096))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Nobelium.getFluid(4000))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("californiuma"))
                .inputFluids(GTMaterials.Berkelium.getFluid(4096))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Californium.getFluid(4000))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("plutoniuma"))
                .inputFluids(GTMaterials.Uranium238.getFluid(4096))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Plutonium239.getFluid(4000))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("berkeliuma"))
                .inputFluids(GTMaterials.Americium.getFluid(4096))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Berkelium.getFluid(4000))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("curiuma"))
                .inputFluids(GTMaterials.Plutonium239.getFluid(4096))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Curium.getFluid(4000))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("neptuniuma"))
                .inputFluids(GTMaterials.Protactinium.getFluid(4096))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Neptunium.getFluid(4000))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("uraniuma"))
                .inputFluids(GTMaterials.Thorium.getFluid(4096))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Uranium238.getFluid(4000))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("astatinea"))
                .inputFluids(GTMaterials.Bismuth.getFluid(4096))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Astatine.getFluid(4000))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("lawrenciuma"))
                .inputFluids(GTMaterials.Mendelevium.getFluid(4096))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Lawrencium.getFluid(4000))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("mendeleviuma"))
                .inputFluids(GTMaterials.Einsteinium.getFluid(4096))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Mendelevium.getFluid(4000))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("coperniciuma"))
                .inputFluids(GTMaterials.Darmstadtium.getFluid(4096))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Copernicium.getFluid(4000))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("bohriuma"))
                .inputFluids(GTMaterials.Dubnium.getFluid(4096))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Bohrium.getFluid(4000))
                .EUt(491520)
                .duration(200)
                .save(provider);

        GTORecipeTypes.SUPER_PARTICLE_COLLIDER_RECIPES.recipeBuilder(GTOCore.id("fermiuma"))
                .inputFluids(GTMaterials.Californium.getFluid(4096))
                .inputFluids(GTMaterials.Helium.getFluid(FluidStorageKeys.PLASMA, 4096))
                .outputFluids(GTMaterials.Fermium.getFluid(4000))
                .EUt(491520)
                .duration(200)
                .save(provider);
    }
}
