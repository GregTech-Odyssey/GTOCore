package com.gtocore.common.data;

import com.gtolib.utils.RLUtils;

import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import dev.emi.emi.api.recipe.EmiRecipe;

import java.util.Map;

public final class GTORecipes {

    private GTORecipes() {}

    public static boolean cache;

    public static Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> RECIPES_CACHE;
    public static Map<ResourceLocation, Recipe<?>> BYNAME_CACHE;

    public static Map<GTRecipeType, Widget> EMI_RECIPE_WIDGETS;

    public static ImmutableSet<EmiRecipe> EMI_RECIPES;

    public static Recipe<?> fromJson(ResourceLocation recipeId, JsonObject json, net.minecraftforge.common.crafting.conditions.ICondition.IContext context) {
        String s = GsonHelper.getAsString(json, "type");
        RecipeSerializer<?> recipeSerializer = BuiltInRegistries.RECIPE_SERIALIZER.get(RLUtils.parse(s));
        if (recipeSerializer == null) return null;
        return recipeSerializer.fromJson(recipeId, json, context);
    }
}
