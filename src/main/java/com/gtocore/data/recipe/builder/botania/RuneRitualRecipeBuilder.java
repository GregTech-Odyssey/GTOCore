package com.gtocore.data.recipe.builder.botania;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.common.data.GTRecipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import mythicbotany.rune.RuneRitualRecipe;

import java.util.ArrayList;
import java.util.List;

public final class RuneRitualRecipeBuilder {

    public static RuneRitualRecipeBuilder builder(String name) {
        return new RuneRitualRecipeBuilder(GTOCore.id("rune_ritual/" + name));
    }

    private final ResourceLocation id; // 配方唯一ID
    private Ingredient centerRune; // 中心符文（必填）
    private final List<RuneRitualRecipe.RunePosition> runes = new ArrayList<>(); // 外围符文临时数据
    private int mana = 0; // 魔法值消耗
    private int ticks = 200; // 持续时间（tick）
    private final List<Ingredient> inputs = new ArrayList<>(); // 物品输入
    private final List<ItemStack> outputs = new ArrayList<>(); // 物品输出

    // 私有构造器：初始化ID和默认序列化器（可根据需求修改）
    private RuneRitualRecipeBuilder(ResourceLocation id) {
        this.id = id;
    }

    // -------------- 配置核心参数 --------------
    public RuneRitualRecipeBuilder centerRune(Ingredient centerRune) {
        this.centerRune = centerRune;
        return this;
    }

    public RuneRitualRecipeBuilder centerRune(ItemLike centerRune) {
        return centerRune(Ingredient.of(centerRune));
    }

    public RuneRitualRecipeBuilder centerRune(TagKey<Item> centerRuneTag) {
        return centerRune(Ingredient.of(centerRuneTag));
    }

    // -------------- 配置外围符文 --------------
    public RuneRitualRecipeBuilder addOuterRune(Ingredient rune, int x, int z, boolean consume) {
        this.runes.add(new RuneRitualRecipe.RunePosition(rune, x, z, consume));
        return this;
    }

    public RuneRitualRecipeBuilder addOuterRune(ItemLike rune, int x, int z, boolean consume) {
        return addOuterRune(Ingredient.of(rune), x, z, consume);
    }

    public RuneRitualRecipeBuilder addOuterRune(TagKey<Item> runeTag, int x, int z, boolean consume) {
        return addOuterRune(Ingredient.of(runeTag), x, z, consume);
    }

    // -------------- 配置消耗与时间 --------------
    public RuneRitualRecipeBuilder mana(int mana) {
        this.mana = mana;
        return this;
    }

    public RuneRitualRecipeBuilder ticks(int ticks) {
        this.ticks = ticks;
        return this;
    }

    // -------------- 配置物品输入输出 --------------
    public RuneRitualRecipeBuilder addInput(Ingredient input) {
        this.inputs.add(input);
        return this;
    }

    public RuneRitualRecipeBuilder addInput(ItemLike input) {
        return addInput(Ingredient.of(input));
    }

    public RuneRitualRecipeBuilder addInput(TagKey<Item> inputTag) {
        return addInput(Ingredient.of(inputTag));
    }

    public RuneRitualRecipeBuilder addOutput(ItemStack output) {
        this.outputs.add(output);
        return this;
    }

    public RuneRitualRecipeBuilder addOutput(ItemLike output) {
        return addOutput(new ItemStack(output));
    }

    // -------------- 保存并注册配方 --------------
    public void save() {
        // 校验必填项（与RunicAltarRecipeBuilder保持一致的校验风格）
        if (centerRune == null) {
            throw new IllegalStateException("符文仪式配方 " + id + " 未设置中心符文！");
        }
        if (outputs.isEmpty()) {
            throw new IllegalStateException("符文仪式配方 " + id + " 未设置输出物品！");
        }
        GTRecipes.RECIPE_MAP.put(id, new RuneRitualRecipe(id, centerRune, runes, mana, ticks, inputs, outputs, null, null));
    }
}
