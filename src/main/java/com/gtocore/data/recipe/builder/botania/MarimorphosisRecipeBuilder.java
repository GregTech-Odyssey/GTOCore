package com.gtocore.data.recipe.builder.botania;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.common.data.GTRecipes;

import net.minecraft.commands.CommandFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.MarimorphosisRecipe;
import vazkii.botania.common.crafting.StateIngredientHelper;

public final class MarimorphosisRecipeBuilder {

    public static MarimorphosisRecipeBuilder builder(String name) {
        return new MarimorphosisRecipeBuilder(GTOCore.id("marimorphosis/" + name));
    }

    private final ResourceLocation id;
    private StateIngredient input;
    private Block output;
    private int weight = 1;
    private int biomeBonus = 11;
    private TagKey<Biome> biomeTag;

    private MarimorphosisRecipeBuilder(ResourceLocation id) {
        this.id = id;
    }

    private MarimorphosisRecipeBuilder input(StateIngredient input) {
        this.input = input;
        return this;
    }

    public MarimorphosisRecipeBuilder input(Block input) {
        return input(StateIngredientHelper.of(input));
    }

    public MarimorphosisRecipeBuilder input(TagKey<Block> inputTag) {
        return input(StateIngredientHelper.of(inputTag));
    }

    public MarimorphosisRecipeBuilder output(Block output) {
        this.output = output;
        return this;
    }

    public MarimorphosisRecipeBuilder weight(int weight) {
        this.weight = weight;
        return this;
    }

    public MarimorphosisRecipeBuilder biomeBonus(int biomeBonus) {
        this.biomeBonus = biomeBonus;
        return this;
    }

    public MarimorphosisRecipeBuilder biomeTag(TagKey<Biome> biomeTag) {
        this.biomeTag = biomeTag;
        return this;
    }

    public void save() {
        GTRecipes.RECIPE_MAP.put(id, new MarimorphosisRecipe(id, input, StateIngredientHelper.of(output.defaultBlockState()), weight, CommandFunction.CacheableFunction.NONE, biomeBonus, biomeTag));
    }
}
