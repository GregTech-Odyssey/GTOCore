package com.gtocore.common.data;

import com.gtocore.data.recipe.RecipeFilter;

import com.gtolib.utils.RLUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import dev.emi.emi.api.recipe.EmiRecipe;

import java.util.Map;

public final class GTORecipes {

    private GTORecipes() {}

    public static boolean cache;

    public static Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> RECIPES_CACHE;
    public static Map<ResourceLocation, Recipe<?>> BYNAME_CACHE;

    public static ImmutableSet<EmiRecipe> EMI_RECIPES;

    public static <C extends Container> Recipe<C> convert(Recipe<C> recipe) {
        if (RecipeFilter.JsonFilter != null) RecipeFilter.JsonFilter = null;
        return recipe;
    }

    public static Recipe<?> fromJson(ResourceLocation recipeId, JsonObject json, net.minecraftforge.common.crafting.conditions.ICondition.IContext context) {
        String s = GsonHelper.getAsString(json, "type");
        RecipeSerializer<?> recipeSerializer = BuiltInRegistries.RECIPE_SERIALIZER.get(RLUtils.parse(s));
        if (recipeSerializer == null) return null;
        return recipeSerializer.fromJson(recipeId, json, context);
    }
}
