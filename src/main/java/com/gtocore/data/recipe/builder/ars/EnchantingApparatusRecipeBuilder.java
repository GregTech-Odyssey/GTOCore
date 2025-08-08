package com.gtocore.data.recipe.builder.ars;

import com.gtolib.GTOCore;
import com.gtolib.utils.ItemUtils;

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
import java.util.function.Consumer;

import javax.annotation.Nullable;

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

    public void save(Consumer<FinishedRecipe> consumer) {
        if (reagent == null) {
            throw new IllegalStateException("No input specified for enchanting apparatus recipe");
        }
        if (result == null) {
            throw new IllegalStateException("No output specified for enchanting apparatus recipe");
        }
        if (pedestalItems.isEmpty()) {
            throw new IllegalStateException("No pedestal items added to enchanting apparatus recipe");
        }

        consumer.accept(new FinishedRecipe() {

            @Override
            public void serializeRecipeData(@NotNull JsonObject json) {
                json.addProperty("type", "ars_nouveau:enchanting_apparatus");

                JsonArray reagentArray = new JsonArray();
                reagentArray.add(reagent.toJson());
                json.add("reagent", reagentArray);

                JsonArray pedestalArray = new JsonArray();
                for (Ingredient ingredient : pedestalItems) {
                    pedestalArray.add(ingredient.toJson());
                }
                json.add("pedestalItems", pedestalArray);

                JsonObject resultObj = new JsonObject();
                resultObj.addProperty("item", ItemUtils.getIdLocation(result.getItem()).toString());
                if (result.getCount() > 1) {
                    resultObj.addProperty("count", result.getCount());
                }
                json.add("output", resultObj);

                if (sourceCost > 0) {
                    json.addProperty("sourceCost", sourceCost);
                }
                if (keepNbtOfReagent) {
                    json.addProperty("keepNbtOfReagent", true);
                }
            }

            @Override
            public @NotNull ResourceLocation getId() {
                return id;
            }

            @Override
            public @NotNull RecipeSerializer<?> getType() {
                return RecipeRegistry.APPARATUS_SERIALIZER.get();
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
