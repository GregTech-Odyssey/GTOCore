package com.gtocore.data.recipe.builder.botania;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.common.data.GTRecipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import vazkii.botania.common.crafting.ElvenTradeRecipe;

import java.util.ArrayList;
import java.util.List;

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
        GTRecipes.RECIPE_MAP.put(id, new ElvenTradeRecipe(id, outputs.toArray(new ItemStack[0]), inputs.toArray(new Ingredient[0])));
    }
}
