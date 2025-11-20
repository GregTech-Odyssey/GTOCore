package com.gtocore.data.recipe.builder.botania;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.common.data.GTRecipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.crafting.BotanicalBreweryRecipe;

import java.util.ArrayList;
import java.util.List;

public final class BrewRecipeBuilder {

    public static BrewRecipeBuilder builder(String name) {
        return new BrewRecipeBuilder(GTOCore.id("brew/" + name));
    }

    private final ResourceLocation id;
    private Brew brew;
    private final List<Ingredient> ingredients = new ArrayList<>();

    private BrewRecipeBuilder(ResourceLocation id) {
        this.id = id;
    }

    public BrewRecipeBuilder brew(Brew brew) {
        this.brew = brew;
        return this;
    }

    private BrewRecipeBuilder addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        return this;
    }

    public BrewRecipeBuilder addIngredient(ItemLike item) {
        return addIngredient(Ingredient.of(item));
    }

    public BrewRecipeBuilder addIngredient(TagKey<Item> tag) {
        return addIngredient(Ingredient.of(tag));
    }

    public void save() {
        if (brew == null) {
            throw new IllegalStateException("No brew specified for brew recipe");
        }
        if (ingredients.isEmpty()) {
            throw new IllegalStateException("No ingredients added to brew recipe");
        }
        GTRecipes.RECIPE_MAP.put(id, new BotanicalBreweryRecipe(id, brew, ingredients.toArray(new Ingredient[0])));
    }
}
