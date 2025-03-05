package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTORecipeTypes;

interface WeatherControl {

    static void init() {
        GTORecipeTypes.WEATHER_CONTROL_RECIPES.recipeBuilder(GTOCore.id("3"))
                .circuitMeta(3)
                .EUt(30)
                .duration(200)
                .save();

        GTORecipeTypes.WEATHER_CONTROL_RECIPES.recipeBuilder(GTOCore.id("1"))
                .circuitMeta(1)
                .EUt(30)
                .duration(200)
                .save();

        GTORecipeTypes.WEATHER_CONTROL_RECIPES.recipeBuilder(GTOCore.id("2"))
                .circuitMeta(2)
                .EUt(30)
                .duration(200)
                .save();
    }
}
