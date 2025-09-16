package com.gtocore.data.recipe.builder.botania;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.data.pack.GTDynamicDataPack;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import com.google.gson.JsonObject;
import io.github.lounode.extrabotany.common.crafting.ExtraBotanyRecipeTypes;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public final class PedestalRecipeBuilder {

    public static PedestalRecipeBuilder builder(String name) {
        return new PedestalRecipeBuilder(GTOCore.id("pedestal/" + name));
    }

    private final ResourceLocation id;
    private ItemStack output;
    private Ingredient input;
    private Ingredient smashTools;
    private int strike = 1;
    private int exp = 0;

    private PedestalRecipeBuilder(ResourceLocation id) {
        this.id = id;
    }

    public PedestalRecipeBuilder output(ItemLike output) {
        return output(new ItemStack(output));
    }

    public PedestalRecipeBuilder output(ItemStack output) {
        this.output = output;
        return this;
    }

    public PedestalRecipeBuilder input(Ingredient input) {
        this.input = input;
        return this;
    }

    public PedestalRecipeBuilder input(ItemLike item) {
        return input(Ingredient.of(item));
    }

    public PedestalRecipeBuilder input(TagKey<Item> tag) {
        return input(Ingredient.of(tag));
    }

    public PedestalRecipeBuilder smashTools(Ingredient smashTools) {
        this.smashTools = smashTools;
        return this;
    }

    public PedestalRecipeBuilder smashTools(ItemLike item) {
        return smashTools(Ingredient.of(item));
    }

    public PedestalRecipeBuilder smashTools(TagKey<Item> tag) {
        return smashTools(Ingredient.of(tag));
    }

    public PedestalRecipeBuilder strike(int strike) {
        this.strike = strike;
        return this;
    }

    public PedestalRecipeBuilder exp(int exp) {
        this.exp = exp;
        return this;
    }

    public void save() {
        if (output == null) {
            throw new IllegalStateException("No output specified for pedestal recipe");
        }
        if (input == null) {
            throw new IllegalStateException("No input specified for pedestal recipe");
        }
        if (smashTools == null) {
            throw new IllegalStateException("No smash tools specified for pedestal recipe");
        }

        GTDynamicDataPack.addRecipe(new FinishedRecipe() {

            @Override
            public void serializeRecipeData(@NotNull JsonObject json) {
                // 序列化输出 - 使用 Minecraft 1.20.1 的方法
                JsonObject outputObj = new JsonObject();
                outputObj.addProperty("item", BuiltInRegistries.ITEM.getKey(output.getItem()).toString());
                if (output.getCount() > 1) {
                    outputObj.addProperty("count", output.getCount());
                }
                if (output.hasTag()) {
                    outputObj.addProperty("nbt", output.getTag().toString());
                }
                json.add("output", outputObj);

                // 序列化输入和工具
                json.add("input", input.toJson());
                json.add("smash_tools", smashTools.toJson());

                // 序列化打击次数和经验值
                json.addProperty("strike", strike);
                json.addProperty("exp", exp);
            }

            @Override
            public @NotNull ResourceLocation getId() {
                return id;
            }

            @Override
            public @NotNull RecipeSerializer<?> getType() {
                return ExtraBotanyRecipeTypes.PEDESTAL_SMASH_SERIALIZER;
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
