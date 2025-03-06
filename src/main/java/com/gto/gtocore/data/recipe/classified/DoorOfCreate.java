package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.GTODimensions;

import static com.gto.gtocore.common.data.GTORecipeTypes.DOOR_OF_CREATE_RECIPES;

interface DoorOfCreate {

    static void init() {
        DOOR_OF_CREATE_RECIPES.recipeBuilder(GTOCore.id("1"))
                .circuitMeta(1)
                .EUt(8053063680L)
                .duration(20)
                .dimension(GTODimensions.OVERWORLD)
                .save();
    }
}
