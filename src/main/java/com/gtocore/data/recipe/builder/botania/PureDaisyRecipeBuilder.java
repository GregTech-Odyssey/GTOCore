package com.gtocore.data.recipe.builder.botania;

import com.gtolib.GTOCore;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.crafting.StateIngredientHelper;

import java.util.function.Consumer;

import javax.annotation.Nullable;

public final class PureDaisyRecipeBuilder {

    public static PureDaisyRecipeBuilder builder(String name) {
        return new PureDaisyRecipeBuilder(GTOCore.id("pure_daisy/" + name));
    }

    private static final int DEFAULT_TIME = 150;

    private final ResourceLocation id;
    private StateIngredient input;
    private Block output;
    private int time = DEFAULT_TIME;

    private PureDaisyRecipeBuilder(ResourceLocation id) {
        this.id = id;
    }

    public PureDaisyRecipeBuilder input(StateIngredient input) {
        this.input = input;
        return this;
    }

    public PureDaisyRecipeBuilder input(Block input) {
        return input(StateIngredientHelper.of(input));
    }

    public PureDaisyRecipeBuilder input(TagKey<Block> inputTag) {
        return input(StateIngredientHelper.of(inputTag));
    }

    public PureDaisyRecipeBuilder output(Block output) {
        this.output = output;
        return this;
    }

    public PureDaisyRecipeBuilder time(int time) {
        this.time = time;
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new FinishedRecipe() {

            @Override
            public void serializeRecipeData(@NotNull JsonObject json) {
                json.add("input", input.serialize());
                json.add("output", StateIngredientHelper.serializeBlockState(output.defaultBlockState()));
                if (time != DEFAULT_TIME) {
                    json.addProperty("time", time);
                }
            }

            @Override
            public @NotNull ResourceLocation getId() {
                return id;
            }

            @Override
            public @NotNull RecipeSerializer<?> getType() {
                return BotaniaRecipeTypes.PURE_DAISY_SERIALIZER;
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
