package com.gtocore.data.recipe.builder.botania;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.common.data.GTRecipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import vazkii.botania.common.crafting.RunicAltarRecipe;
import vazkii.botania.common.crafting.recipe.HeadRecipe;

import java.util.ArrayList;
import java.util.List;

public final class RunicAltarRecipeBuilder {

    public static RunicAltarRecipeBuilder builder(String name) {
        return new RunicAltarRecipeBuilder(GTOCore.id("runic_altar/" + name));
    }

    private final ResourceLocation id;
    private ItemStack output;
    private int mana;
    private final List<Ingredient> ingredients = new ArrayList<>();
    private boolean isHeadRecipe = false;

    private RunicAltarRecipeBuilder(ResourceLocation id) {
        this.id = id;
    }

    public RunicAltarRecipeBuilder output(ItemLike output) {
        return output(new ItemStack(output));
    }

    public RunicAltarRecipeBuilder output(ItemStack output) {
        this.output = output;
        return this;
    }

    public RunicAltarRecipeBuilder mana(int mana) {
        this.mana = mana;
        return this;
    }

    public RunicAltarRecipeBuilder addIngredient(Ingredient ingredient) {
        if (ingredient != null) {
            this.ingredients.add(ingredient);
        }
        return this;
    }

    public RunicAltarRecipeBuilder addIngredient(ItemLike item) {
        return addIngredient(Ingredient.of(item));
    }

    public RunicAltarRecipeBuilder addIngredient(TagKey<Item> tag) {
        return addIngredient(Ingredient.of(tag));
    }

    public RunicAltarRecipeBuilder setHeadRecipe(Boolean setHeadRecipe) {
        this.isHeadRecipe = setHeadRecipe;
        return this;
    }

    public void save() {
        if (ingredients.isEmpty()) {
            throw new IllegalStateException("No ingredients added to runic altar recipe");
        }
        if (output == null) {
            throw new IllegalStateException("No output specified for runic altar recipe");
        }
        GTRecipes.RECIPE_MAP.put(id, isHeadRecipe ? new HeadRecipe(id, output, mana, ingredients.toArray(new Ingredient[] {})) : new RunicAltarRecipe(id, output, mana, ingredients.toArray(new Ingredient[] {})));
    }
}
