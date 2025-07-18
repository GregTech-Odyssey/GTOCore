package com.gtocore.data.recipe.builder.botania;

import com.gtolib.GTOCore;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.annotation.Nullable;

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

    public void save(Consumer<FinishedRecipe> consumer) {
        if (brew == null) {
            throw new IllegalStateException("No brew specified for brew recipe");
        }
        if (ingredients.isEmpty()) {
            throw new IllegalStateException("No ingredients added to brew recipe");
        }

        consumer.accept(new FinishedRecipe() {

            @Override
            public void serializeRecipeData(@NotNull JsonObject json) {
                json.addProperty("brew", Objects.requireNonNull(Objects.requireNonNull(BotaniaAPI.instance().getBrewRegistry()).getKey(brew)).toString());

                JsonArray ingredientsJson = new JsonArray();
                for (Ingredient ingredient : ingredients) {
                    ingredientsJson.add(ingredient.toJson());
                }
                json.add("ingredients", ingredientsJson);
            }

            @Override
            public @NotNull ResourceLocation getId() {
                return id;
            }

            @Override
            public @NotNull RecipeSerializer<?> getType() {
                return BotaniaRecipeTypes.BREW_SERIALIZER;
            }

            @Nullable
            @Override
            public JsonObject serializeAdvancement() {
                return null;
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        });
    }
}
