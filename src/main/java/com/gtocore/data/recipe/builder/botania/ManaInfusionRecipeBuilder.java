package com.gtocore.data.recipe.builder.botania;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.common.data.GTRecipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.ManaInfusionRecipe;
import vazkii.botania.common.crafting.StateIngredientHelper;

import javax.annotation.Nullable;

public final class ManaInfusionRecipeBuilder {

    public static ManaInfusionRecipeBuilder builder(String name) {
        return new ManaInfusionRecipeBuilder(GTOCore.id("mana_infusion/" + name));
    }

    private final ResourceLocation id;
    private Ingredient input;
    private ItemStack output;
    private int mana;
    private String group = "";
    @Nullable
    private StateIngredient catalyst;

    private ManaInfusionRecipeBuilder(ResourceLocation id) {
        this.id = id;
    }

    public ManaInfusionRecipeBuilder input(Ingredient input) {
        this.input = input;
        return this;
    }

    public ManaInfusionRecipeBuilder input(ItemLike item) {
        return input(Ingredient.of(item));
    }

    public ManaInfusionRecipeBuilder input(TagKey<Item> tag) {
        return input(Ingredient.of(tag));
    }

    public ManaInfusionRecipeBuilder output(ItemLike output) {
        return output(new ItemStack(output));
    }

    public ManaInfusionRecipeBuilder output(ItemStack output) {
        this.output = output;
        return this;
    }

    public ManaInfusionRecipeBuilder mana(int mana) {
        this.mana = mana;
        return this;
    }

    public ManaInfusionRecipeBuilder group(String group) {
        if (group != null)
            this.group = group;
        return this;
    }

    public ManaInfusionRecipeBuilder alchemyCatalyst() {
        this.catalyst = StateIngredientHelper.of(BotaniaBlocks.alchemyCatalyst);
        return this;
    }

    public ManaInfusionRecipeBuilder conjurationCatalyst() {
        this.catalyst = StateIngredientHelper.of(BotaniaBlocks.conjurationCatalyst);
        return this;
    }

    public ManaInfusionRecipeBuilder customCatalyst(Block catalyst) {
        if (catalyst != null)
            this.catalyst = StateIngredientHelper.of(catalyst);
        return this;
    }

    public void save() {
        GTRecipes.RECIPE_MAP.put(id, new ManaInfusionRecipe(id, output, input, mana, group, catalyst));
    }
}
