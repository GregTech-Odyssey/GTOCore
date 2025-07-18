package com.gtocore.data.recipe.builder.botania;

import com.gtolib.GTOCore;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.helper.ItemNBTHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

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

    public void save(Consumer<FinishedRecipe> consumer) {
        if (ingredients.isEmpty()) {
            throw new IllegalStateException("No ingredients added to runic altar recipe");
        }
        if (output == null) {
            throw new IllegalStateException("No output specified for runic altar recipe");
        }

        consumer.accept(new FinishedRecipe() {

            @Override
            public void serializeRecipeData(@NotNull JsonObject json) {
                json.add("output", ItemNBTHelper.serializeStack(output));

                JsonArray ingredientsJson = new JsonArray();
                for (Ingredient ingredient : ingredients) {
                    ingredientsJson.add(ingredient.toJson());
                }
                json.add("ingredients", ingredientsJson);

                json.addProperty("mana", mana);
            }

            @Override
            public @NotNull ResourceLocation getId() {
                return id;
            }

            @Override
            public @NotNull RecipeSerializer<?> getType() {
                return isHeadRecipe ? BotaniaRecipeTypes.RUNE_HEAD_SERIALIZER : BotaniaRecipeTypes.RUNE_SERIALIZER;
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
