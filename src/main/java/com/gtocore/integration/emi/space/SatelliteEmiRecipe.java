package com.gtocore.integration.emi.space;

import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.integration.emi.recipe.GTEmiRecipe;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

class SatelliteEmiRecipe extends GTEmiRecipe {

    private SatelliteEmiRecipe(Recipe recipe) {
        super(recipe, SatelliteEmiCategory.CATEGORY);
    }

    static SatelliteEmiRecipe fromInputOutput(ResourceLocation id, Map<RecipeCapability<?>, List<Content>> inputs, Map<RecipeCapability<?>, List<Content>> outputs) {
        var recipe = RecipeBuilder.ofRaw();
        recipe.recipeType = GTORecipeTypes.LAMINATOR_RECIPES;
        recipe.input = inputs;
        recipe.output = outputs;
        recipe.id = id;
        recipe.duration = 20 * 300;
        recipe.EUt(GTValues.V[GTValues.HV]);
        return new SatelliteEmiRecipe(recipe.buildRawRecipe());
    }
}
