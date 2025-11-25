package com.gtocore.data.recipe.builder.ars;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.common.data.GTRecipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import com.hollingsworth.arsnouveau.common.crafting.recipes.ImbuementRecipe;

import java.util.ArrayList;
import java.util.List;

public final class ImbuementRecipeBuilder {

    public static ImbuementRecipeBuilder builder(String name) {
        return new ImbuementRecipeBuilder(GTOCore.id("imbuement/" + name));
    }

    private final ResourceLocation id;
    private Ingredient input;
    private ItemStack output;
    private int source;
    private final List<Ingredient> pedestalItems = new ArrayList<>();

    private ImbuementRecipeBuilder(ResourceLocation id) {
        this.id = id;
    }

    public ImbuementRecipeBuilder input(Ingredient input) {
        this.input = input;
        return this;
    }

    public ImbuementRecipeBuilder input(ItemLike item) {
        return input(Ingredient.of(item));
    }

    public ImbuementRecipeBuilder input(TagKey<Item> tag) {
        return input(Ingredient.of(tag));
    }

    public ImbuementRecipeBuilder output(ItemStack output) {
        this.output = output;
        return this;
    }

    public ImbuementRecipeBuilder output(ItemLike item) {
        return output(new ItemStack(item));
    }

    public ImbuementRecipeBuilder output(ItemLike item, int count) {
        return output(new ItemStack(item, count));
    }

    public ImbuementRecipeBuilder source(int source) {
        if (source != 0) {
            this.source = source;
        }
        return this;
    }

    public ImbuementRecipeBuilder addPedestalItem(Ingredient ingredient) {
        if (ingredient != null) {
            this.pedestalItems.add(ingredient);
        }
        return this;
    }

    public ImbuementRecipeBuilder addPedestalItem(ItemLike item) {
        return addPedestalItem(Ingredient.of(item));
    }

    public ImbuementRecipeBuilder addPedestalItem(TagKey<Item> tag) {
        return addPedestalItem(Ingredient.of(tag));
    }

    public void save() {
        if (input == null) {
            throw new IllegalStateException("No input specified for imbuement recipe");
        }
        if (output == null) {
            throw new IllegalStateException("No output specified for imbuement recipe");
        }
        GTRecipes.RECIPE_MAP.put(id, new ImbuementRecipe(id, input, output, source, pedestalItems));
    }
}
