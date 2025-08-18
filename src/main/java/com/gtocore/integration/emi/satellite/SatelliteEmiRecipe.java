package com.gtocore.integration.emi.satellite;

import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.integration.emi.recipe.GTEmiRecipe;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public class SatelliteEmiRecipe extends GTEmiRecipe {

    public SatelliteEmiRecipe(Recipe recipe) {
        super(recipe, SatelliteEmiCategory.CATEGORY);
    }

    public static SatelliteEmiRecipe fromInputOutput(ResourceLocation id, Map<RecipeCapability<?>, List<Content>> inputs, Map<RecipeCapability<?>, List<Content>> outputs) {
        var recipe = RecipeBuilder.ofRaw();
        recipe.recipeType = GTORecipeTypes.LAMINATOR_RECIPES;
        recipe.input = inputs;
        recipe.output = outputs;
        recipe.id = id;
        recipe.duration = 20 * 300;
        return new SatelliteEmiRecipe(recipe.buildRawRecipe());
    }
}
