package com.gtocore.data.recipe.builder.botania;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.common.data.GTRecipes;

import net.minecraft.commands.CommandFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.OrechidIgnemRecipe;
import vazkii.botania.common.crafting.StateIngredientHelper;

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
        GTRecipes.RECIPE_MAP.put(id, new OrechidIgnemRecipe(id, input, StateIngredientHelper.of(output.defaultBlockState()), weight, CommandFunction.CacheableFunction.NONE));
    }
}
