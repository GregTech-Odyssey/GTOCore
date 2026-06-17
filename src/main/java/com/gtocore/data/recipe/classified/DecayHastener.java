package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.DECAY_HASTENER_RECIPES;

final class DecayHastener {

    public static void init() {
        DECAY_HASTENER_RECIPES.recipeBuilder("polonium_dust")
                .inputFluids(GTMaterials.Bismuth, 144)
                .outputItems(TagPrefix.dust, GTMaterials.Polonium)
                .EUt(480)
                .duration(8000)
                .save();

        DECAY_HASTENER_RECIPES.recipeBuilder("rutherfordium_dust")
                .inputFluids(GTMaterials.Seaborgium, 144)
                .outputItems(TagPrefix.dust, GTMaterials.Rutherfordium)
                .EUt(480)
                .duration(4000)
                .save();

        DECAY_HASTENER_RECIPES.recipeBuilder("flyb_plasma")
                .inputFluids(GTOMaterials.Quasifissioning.getFluid(FluidStorageKeys.PLASMA, 1000))
                .outputFluids(GTOMaterials.Flyb.getFluid(FluidStorageKeys.PLASMA, 1000))
                .EUt(122880)
                .duration(160)
                .save();

        DECAY_HASTENER_RECIPES.recipeBuilder("rutherfordium_dust1")
                .inputFluids(GTMaterials.Actinium, 144)
                .outputItems(TagPrefix.dust, GTMaterials.Rutherfordium)
                .EUt(480)
                .duration(8000)
                .save();

        DECAY_HASTENER_RECIPES.recipeBuilder("hafnium")
                .inputFluids(GTOMaterials.Ytterbium178, 144)
                .outputFluids(GTMaterials.Hafnium, 144)
                .EUt(30720)
                .duration(120)
                .save();

        DECAY_HASTENER_RECIPES.recipeBuilder("copper76_dust")
                .inputFluids(GTMaterials.Copper, 144)
                .outputItems(TagPrefix.dust, GTOMaterials.Copper76)
                .EUt(1920)
                .duration(4000)
                .save();

        DECAY_HASTENER_RECIPES.recipeBuilder("actinium_dust")
                .inputFluids(GTMaterials.Radium, 144)
                .outputItems(TagPrefix.dust, GTMaterials.Actinium)
                .EUt(480)
                .duration(2000)
                .save();

        DECAY_HASTENER_RECIPES.recipeBuilder("meitnerium_dust")
                .inputFluids(GTMaterials.Hassium, 144)
                .outputItems(TagPrefix.dust, GTMaterials.Meitnerium)
                .EUt(480)
                .duration(8000)
                .save();
    }
}
