package com.gtocore.data.recipe.builder.botania;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.common.data.GTRecipes;

import net.minecraft.commands.CommandFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.OrechidRecipe;
import vazkii.botania.common.crafting.StateIngredientHelper;

public final class OrechidRecipeBuilder {

    public static OrechidRecipeBuilder builder(String name) {
        return new OrechidRecipeBuilder(GTOCore.id("orechid/" + name));
    }

    private final ResourceLocation id;
    private StateIngredient input;
    private Block output;
    private int weight = 1;

    private OrechidRecipeBuilder(ResourceLocation id) {
        this.id = id;
    }

    private OrechidRecipeBuilder input(StateIngredient input) {
        this.input = input;
        return this;
    }

    public OrechidRecipeBuilder input(Block input) {
        return input(StateIngredientHelper.of(input));
    }

    public OrechidRecipeBuilder input(TagKey<Block> inputTag) {
        return input(StateIngredientHelper.of(inputTag));
    }

    public OrechidRecipeBuilder output(Block output) {
        this.output = output;
        return this;
    }

    public OrechidRecipeBuilder weight(int weight) {
        this.weight = weight;
        return this;
    }

    public void save() {
        GTRecipes.RECIPE_MAP.put(id, new OrechidRecipe(id, input, StateIngredientHelper.of(output.defaultBlockState()), weight, CommandFunction.CacheableFunction.NONE));
    }
}
