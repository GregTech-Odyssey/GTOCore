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

public final class ElvenTradeRecipeBuilder {

    public static ElvenTradeRecipeBuilder builder(String name) {
        return new ElvenTradeRecipeBuilder(GTOCore.id("elven_trade/" + name));
    }

    private final ResourceLocation id;
    private final List<Ingredient> inputs = new ArrayList<>();
    private final List<ItemStack> outputs = new ArrayList<>();

    private ElvenTradeRecipeBuilder(ResourceLocation id) {
        this.id = id;
    }

    private ElvenTradeRecipeBuilder addInput(Ingredient input) {
        if (input != null) {
            this.inputs.add(input);
        }
        return this;
    }

    public ElvenTradeRecipeBuilder addInput(ItemLike item) {
        return addInput(Ingredient.of(item));
    }

    public ElvenTradeRecipeBuilder addInput(TagKey<Item> tag) {
        return addInput(Ingredient.of(tag));
    }

    private ElvenTradeRecipeBuilder addOutput(ItemStack output) {
        if (output != null) {
            this.outputs.add(output);
        }
        return this;
    }

    public ElvenTradeRecipeBuilder addOutput(ItemLike output) {
        return addOutput(new ItemStack(output));
    }

    public ElvenTradeRecipeBuilder addOutput(ItemLike output, int count) {
        return addOutput(new ItemStack(output, count));
    }

    public void save() {
        if (inputs.isEmpty()) {
            throw new IllegalStateException("No inputs added to elven trade recipe");
        }
        if (outputs.isEmpty()) {
            throw new IllegalStateException("No outputs specified for elven trade recipe");
        }

        GTDynamicDataPack.addRecipe(new FinishedRecipe() {

            @Override
            public void serializeRecipeData(@NotNull JsonObject json) {
                JsonArray ingredientsJson = new JsonArray();
                for (Ingredient input : inputs) {
                    ingredientsJson.add(input.toJson());
                }
                json.add("ingredients", ingredientsJson);

                JsonArray outputsJson = new JsonArray();
                for (ItemStack output : outputs) {
                    outputsJson.add(ItemNBTHelper.serializeStack(output));
                }
                json.add("output", outputsJson);
            }

            @Override
            public @NotNull ResourceLocation getId() {
                return id;
            }

            @Override
            public @NotNull RecipeSerializer<?> getType() {
                return BotaniaRecipeTypes.ELVEN_TRADE_SERIALIZER;
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
