package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.GTOWorldGenLayers;
import com.gto.gtocore.common.data.GTORecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface DoorOfCreate {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.DOOR_OF_CREATE_RECIPES.recipeBuilder(GTOCore.id("1"))
                .circuitMeta(1)
                .EUt(8053063680L)
                .duration(20)
                .dimension(GTOWorldGenLayers.OVERWORLD)
                .save(provider);
    }
}
