package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTORecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface WeatherControl {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.WEATHER_CONTROL_RECIPES.recipeBuilder(GTOCore.id("3"))
                .circuitMeta(3)
                .EUt(30)
                .duration(200)
                .save(provider);

        GTORecipeTypes.WEATHER_CONTROL_RECIPES.recipeBuilder(GTOCore.id("1"))
                .circuitMeta(1)
                .EUt(30)
                .duration(200)
                .save(provider);

        GTORecipeTypes.WEATHER_CONTROL_RECIPES.recipeBuilder(GTOCore.id("2"))
                .circuitMeta(2)
                .EUt(30)
                .duration(200)
                .save(provider);
    }
}
