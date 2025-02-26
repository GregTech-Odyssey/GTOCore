package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface LargeBoiler {

    static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.LARGE_BOILER_RECIPES.recipeBuilder(GTOCore.id("tin_bucket"))
                .inputItems(TagPrefix.ingot, GTMaterials.Tin)
                .inputFluids(GTMaterials.Lava.getFluid(100))
                .outputFluids(GTMaterials.Tin.getFluid(72))
                .duration(5)
                .save(provider);
    }
}
