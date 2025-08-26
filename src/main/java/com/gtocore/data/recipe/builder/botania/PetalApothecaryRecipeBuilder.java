package com.gtocore.data.recipe.builder.botania;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.data.pack.GTDynamicDataPack;

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

import javax.annotation.Nullable;

public final class PetalApothecaryRecipeBuilder {

    public static PetalApothecaryRecipeBuilder builder(String name) {
        return new PetalApothecaryRecipeBuilder(GTOCore.id("petal_apothecary/" + name));
    }

    private final ResourceLocation id;
    private ItemStack output;
    private Ingredient reagent;
    private final List<Ingredient> ingredients = new ArrayList<>();

    private PetalApothecaryRecipeBuilder(ResourceLocation id) {
        this.id = id;
    }

    public PetalApothecaryRecipeBuilder output(ItemLike output) {
        return output(new ItemStack(output));
    }

    public PetalApothecaryRecipeBuilder output(ItemStack output) {
        this.output = output;
        return this;
    }

    public PetalApothecaryRecipeBuilder reagent(Ingredient reagent) {
        this.reagent = reagent;
        return this;
    }

    public PetalApothecaryRecipeBuilder reagent(ItemLike reagent) {
        return reagent(Ingredient.of(reagent));
    }

    public PetalApothecaryRecipeBuilder reagent(TagKey<Item> reagentTag) {
        return reagent(Ingredient.of(reagentTag));
    }

    public PetalApothecaryRecipeBuilder addIngredient(Ingredient ingredient) {
        if (ingredient != null) {
            this.ingredients.add(ingredient);
        }
        return this;
    }

    public PetalApothecaryRecipeBuilder addIngredient(ItemLike item) {
        return addIngredient(Ingredient.of(item));
    }

    public PetalApothecaryRecipeBuilder addIngredient(TagKey<Item> tag) {
        return addIngredient(Ingredient.of(tag));
    }

    public void save() {
        GTDynamicDataPack.addRecipe(new FinishedRecipe() {

            @Override
            public void serializeRecipeData(@NotNull JsonObject json) {
                json.add("output", ItemNBTHelper.serializeStack(output));

                JsonArray ingredientsJson = new JsonArray();
                for (Ingredient ingredient : ingredients) {
                    ingredientsJson.add(ingredient.toJson());
                }
                json.add("ingredients", ingredientsJson);

                if (reagent != null) {
                    json.add("reagent", reagent.toJson());
                }
            }

            @Override
            public @NotNull ResourceLocation getId() {
                return id;
            }

            @Override
            public @NotNull RecipeSerializer<?> getType() {
                return BotaniaRecipeTypes.PETAL_SERIALIZER;
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
