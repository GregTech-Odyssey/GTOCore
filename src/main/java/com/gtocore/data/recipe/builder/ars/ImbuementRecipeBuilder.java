package com.gtocore.data.recipe.builder.ars;

import com.gtolib.GTOCore;
import com.gtolib.utils.ItemUtils;

import com.gregtechceu.gtceu.data.pack.GTDynamicDataPack;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.hollingsworth.arsnouveau.setup.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

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

        GTDynamicDataPack.addRecipe(new FinishedRecipe() {

            @Override
            public void serializeRecipeData(@NotNull JsonObject json) {
                json.addProperty("type", "ars_nouveau:imbuement");

                json.add("input", input.toJson());

                json.addProperty("output", ItemUtils.getIdLocation(output.getItem()).toString());
                json.addProperty("count", output.getCount());

                json.addProperty("source", source);

                JsonArray pedestalArray = new JsonArray();
                for (Ingredient ingredient : pedestalItems) {
                    JsonObject itemObj = new JsonObject();
                    itemObj.add("item", ingredient.toJson());
                    pedestalArray.add(itemObj);
                }
                json.add("pedestalItems", pedestalArray);
            }

            @Override
            public @NotNull ResourceLocation getId() {
                return id;
            }

            @Override
            public @NotNull RecipeSerializer<?> getType() {
                return RecipeRegistry.IMBUEMENT_SERIALIZER.get();
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
