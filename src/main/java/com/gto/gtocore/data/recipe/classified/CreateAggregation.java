package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.GTODimensions;

import static com.gto.gtocore.common.data.GTORecipeTypes.CREATE_AGGREGATION_RECIPES;

interface CreateAggregation {

    static void init() {
        CREATE_AGGREGATION_RECIPES.recipeBuilder(GTOCore.id("1"))
                .circuitMeta(1)
                .EUt(32212254720L)
                .duration(20)
                .dimension(GTODimensions.CREATE)
                .save();
    }
}
