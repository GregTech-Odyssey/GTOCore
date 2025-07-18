package com.gtocore.data.recipe.builder.botania;

import com.gtolib.GTOCore;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.crafting.StateIngredientHelper;
import vazkii.botania.common.helper.ItemNBTHelper;

import java.util.function.Consumer;

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

    public void save(Consumer<FinishedRecipe> consumer) {
        consumer.accept(new FinishedRecipe() {

            @Override
            public void serializeRecipeData(@NotNull JsonObject json) {
                json.add("input", input.toJson());
                json.add("output", ItemNBTHelper.serializeStack(output));
                json.addProperty("mana", mana);

                if (!group.isEmpty()) {
                    json.addProperty("group", group);
                }

                if (catalyst != null) {
                    json.add("catalyst", catalyst.serialize());
                }
            }

            @Override
            public @NotNull ResourceLocation getId() {
                return id;
            }

            @Override
            public @NotNull RecipeSerializer<?> getType() {
                return BotaniaRecipeTypes.MANA_INFUSION_SERIALIZER;
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
