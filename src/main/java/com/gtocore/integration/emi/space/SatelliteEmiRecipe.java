package com.gtocore.integration.emi.space;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.integration.emi.recipe.GTEmiRecipe;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

class SatelliteEmiRecipe extends GTEmiRecipe {

    private final ResourceLocation id;

    private SatelliteEmiRecipe(ResourceLocation id, GTRecipeDefinition recipe) {
        super(recipe, SatelliteEmiCategory.CATEGORY);
        this.id = id;
    }

    static SatelliteEmiRecipe fromInputOutput(ResourceLocation id, Consumer<RecipeBuilder> builder) {
        var recipe = GTORecipeTypes.LAMINATOR_RECIPES.recipeBuilder(id);
        recipe.EUt(GTValues.V[GTValues.HV])
                .duration(20 * 300)
                .inputItems(GTOItems.PLANET_DATA_CHIP)
                .inputItems(GTOItems.PLANET_SCAN_SATELLITE);
        builder.accept(recipe);
        return new SatelliteEmiRecipe(id, recipe.build());
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }
}
