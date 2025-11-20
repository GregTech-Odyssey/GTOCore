package com.gtocore.data.recipe.builder.botania;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.common.data.GTRecipes;

import net.minecraft.commands.CommandFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.PureDaisyRecipe;
import vazkii.botania.common.crafting.StateIngredientHelper;

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

    public void save() {
        GTRecipes.RECIPE_MAP.put(id, new PureDaisyRecipe(id, input, output.defaultBlockState(), time, CommandFunction.CacheableFunction.NONE));
    }
}
