package com.gtocore.data.recipe.builder.ars;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.common.data.GTRecipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import com.hollingsworth.arsnouveau.api.enchanting_apparatus.EnchantingApparatusRecipe;

import java.util.ArrayList;
import java.util.List;

public final class EnchantingApparatusRecipeBuilder {

    public static EnchantingApparatusRecipeBuilder builder(String name) {
        return new EnchantingApparatusRecipeBuilder(GTOCore.id("enchanting_apparatus/" + name));
    }

    private final ResourceLocation id;
    private Ingredient reagent;
    private ItemStack result;
    private final List<Ingredient> pedestalItems = new ArrayList<>();
    private int sourceCost = 0;
    private boolean keepNbtOfReagent = false;

    private EnchantingApparatusRecipeBuilder(ResourceLocation id) {
        this.id = id;
    }

    public EnchantingApparatusRecipeBuilder input(Ingredient reagent) {
        this.reagent = reagent;
        return this;
    }

    public EnchantingApparatusRecipeBuilder input(ItemLike item) {
        return input(Ingredient.of(item));
    }

    public EnchantingApparatusRecipeBuilder input(TagKey<Item> tag) {
        return input(Ingredient.of(tag));
    }

    public EnchantingApparatusRecipeBuilder output(ItemStack result) {
        this.result = result;
        return this;
    }

    public EnchantingApparatusRecipeBuilder output(ItemLike item) {
        return output(new ItemStack(item));
    }

    public EnchantingApparatusRecipeBuilder output(ItemLike item, int count) {
        return output(new ItemStack(item, count));
    }

    public EnchantingApparatusRecipeBuilder addPedestalItem(Ingredient ingredient) {
        if (ingredient != null) {
            this.pedestalItems.add(ingredient);
        }
        return this;
    }

    public EnchantingApparatusRecipeBuilder addPedestalItem(ItemLike item) {
        return addPedestalItem(Ingredient.of(item));
    }

    public EnchantingApparatusRecipeBuilder addPedestalItem(TagKey<Item> tag) {
        return addPedestalItem(Ingredient.of(tag));
    }

    public EnchantingApparatusRecipeBuilder sourceCost(int cost) {
        this.sourceCost = cost;
        return this;
    }

    public EnchantingApparatusRecipeBuilder keepNbtOfReagent(boolean keepNbt) {
        this.keepNbtOfReagent = keepNbt;
        return this;
    }

    public void save() {
        if (reagent == null) {
            throw new IllegalStateException("No input specified for enchanting apparatus recipe");
        }
        if (result == null) {
            throw new IllegalStateException("No output specified for enchanting apparatus recipe");
        }
        if (pedestalItems.isEmpty()) {
            throw new IllegalStateException("No pedestal items added to enchanting apparatus recipe");
        }

        GTRecipes.RECIPE_MAP.put(id, new EnchantingApparatusRecipe(id, pedestalItems, reagent, result, sourceCost, keepNbtOfReagent));
    }
}
