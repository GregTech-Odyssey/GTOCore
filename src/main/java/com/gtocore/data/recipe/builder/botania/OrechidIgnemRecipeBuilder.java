package com.gtocore.data.recipe.builder.botania;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.data.pack.GTDynamicDataPack;

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

import javax.annotation.Nullable;

public final class OrechidIgnemRecipeBuilder {

    public static OrechidIgnemRecipeBuilder builder(String name) {
        return new OrechidIgnemRecipeBuilder(GTOCore.id("orechid_ignem/" + name));
    }

    private final ResourceLocation id;
    private StateIngredient input;
    private Block output;
    private int weight = 1;

    private OrechidIgnemRecipeBuilder(ResourceLocation id) {
        this.id = id;
    }

    private OrechidIgnemRecipeBuilder input(StateIngredient input) {
        this.input = input;
        return this;
    }

    public OrechidIgnemRecipeBuilder input(Block input) {
        return input(StateIngredientHelper.of(input));
    }

    public OrechidIgnemRecipeBuilder input(TagKey<Block> inputTag) {
        return input(StateIngredientHelper.of(inputTag));
    }

    public OrechidIgnemRecipeBuilder output(Block output) {
        this.output = output;
        return this;
    }

    public OrechidIgnemRecipeBuilder weight(int weight) {
        this.weight = weight;
        return this;
    }

    public void save() {
        GTDynamicDataPack.addRecipe(new FinishedRecipe() {

            @Override
            public void serializeRecipeData(@NotNull JsonObject json) {
                json.add("input", input.serialize());
                json.add("output", StateIngredientHelper.of(output.defaultBlockState()).serialize());
                json.addProperty("weight", weight);
            }

            @Override
            public @NotNull ResourceLocation getId() {
                return id;
            }

            @Override
            public @NotNull RecipeSerializer<?> getType() {
                return BotaniaRecipeTypes.ORECHID_IGNEM_SERIALIZER;
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
