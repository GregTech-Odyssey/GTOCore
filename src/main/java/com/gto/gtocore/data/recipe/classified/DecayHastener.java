package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface DecayHastener {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.DECAY_HASTENER_RECIPES.recipeBuilder(GTOCore.id("polonium_dust"))
                .inputFluids(GTMaterials.Bismuth.getFluid(144))
                .outputItems(TagPrefix.dust, GTMaterials.Polonium)
                .EUt(480)
                .duration(8000)
                .save(provider);

        GTORecipeTypes.DECAY_HASTENER_RECIPES.recipeBuilder(GTOCore.id("rutherfordium_dust"))
                .inputFluids(GTMaterials.Seaborgium.getFluid(144))
                .outputItems(TagPrefix.dust, GTMaterials.Rutherfordium)
                .EUt(480)
                .duration(4000)
                .save(provider);

        GTORecipeTypes.DECAY_HASTENER_RECIPES.recipeBuilder(GTOCore.id("flyb_plasma"))
                .inputFluids(GTOMaterials.Quasifissioning.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputFluids(GTOMaterials.Flyb.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(122880)
                .duration(160)
                .save(provider);

        GTORecipeTypes.DECAY_HASTENER_RECIPES.recipeBuilder(GTOCore.id("rutherfordium_dust1"))
                .inputFluids(GTMaterials.Actinium.getFluid(144))
                .outputItems(TagPrefix.dust, GTMaterials.Rutherfordium)
                .EUt(480)
                .duration(8000)
                .save(provider);

        GTORecipeTypes.DECAY_HASTENER_RECIPES.recipeBuilder(GTOCore.id("hafnium"))
                .inputFluids(GTOMaterials.Ytterbium178.getFluid(144))
                .outputFluids(GTMaterials.Hafnium.getFluid(144))
                .EUt(30720)
                .duration(120)
                .save(provider);

        GTORecipeTypes.DECAY_HASTENER_RECIPES.recipeBuilder(GTOCore.id("copper76_dust"))
                .inputFluids(GTMaterials.Copper.getFluid(144))
                .outputItems(TagPrefix.dust, GTOMaterials.Copper76)
                .EUt(1920)
                .duration(4000)
                .save(provider);

        GTORecipeTypes.DECAY_HASTENER_RECIPES.recipeBuilder(GTOCore.id("actinium_dust"))
                .inputFluids(GTMaterials.Radium.getFluid(144))
                .outputItems(TagPrefix.dust, GTMaterials.Actinium)
                .EUt(480)
                .duration(2000)
                .save(provider);

        GTORecipeTypes.DECAY_HASTENER_RECIPES.recipeBuilder(GTOCore.id("meitnerium_dust"))
                .inputFluids(GTMaterials.Hassium.getFluid(144))
                .outputItems(TagPrefix.dust, GTMaterials.Meitnerium)
                .EUt(480)
                .duration(8000)
                .save(provider);
    }
}
