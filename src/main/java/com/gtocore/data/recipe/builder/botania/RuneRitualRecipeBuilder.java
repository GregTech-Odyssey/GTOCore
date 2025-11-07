package com.gtocore.data.recipe.builder.botania;

import com.gtolib.GTOCore;

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
import mythicbotany.rune.RuneRitualRecipe;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.helper.ItemNBTHelper;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

// 符文仪式配方建造者（适配GTDynamicDataPack.addRecipe注册方式）
public final class RuneRitualRecipeBuilder {

    // 静态入口：创建建造者，配方ID格式为 "gtocore:rune_ritual/名称"
    public static RuneRitualRecipeBuilder builder(String name) {
        return new RuneRitualRecipeBuilder(GTOCore.id("rune_ritual/" + name));
    }

    private final ResourceLocation id; // 配方唯一ID
    private Ingredient centerRune; // 中心符文（必填）
    private final List<RunePositionData> runes = new ArrayList<>(); // 外围符文临时数据
    private int mana = 0; // 魔法值消耗
    private int ticks = 200; // 持续时间（tick）
    private final List<Ingredient> inputs = new ArrayList<>(); // 物品输入
    private final List<ItemStack> outputs = new ArrayList<>(); // 物品输出
    @Nullable
    private ResourceLocation specialInputId; // 特殊输入ID
    @Nullable
    private ResourceLocation specialOutputId; // 特殊输出ID

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
        this.runes.add(new RunePositionData(rune, x, z, consume));
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

    // -------------- 配置特殊逻辑与序列化器 --------------
    public RuneRitualRecipeBuilder specialInput(ResourceLocation specialInputId) {
        this.specialInputId = specialInputId;
        return this;
    }

    public RuneRitualRecipeBuilder specialOutput(ResourceLocation specialOutputId) {
        this.specialOutputId = specialOutputId;
        return this;
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

        // 创建FinishedRecipe实例，通过GTDynamicDataPack.addRecipe注册
        GTDynamicDataPack.addRecipe(new FinishedRecipe() {

            @Override
            public void serializeRecipeData(@NotNull JsonObject json) {
                // 序列化中心符文
                json.add("center", centerRune.toJson());

                // 序列化外围符文列表
                JsonArray runesJson = new JsonArray();
                for (RunePositionData rune : runes) {
                    JsonObject runeObj = new JsonObject();
                    runeObj.add("rune", rune.ingredient.toJson());
                    runeObj.addProperty("x", rune.x);
                    runeObj.addProperty("z", rune.z);
                    runeObj.addProperty("consume", rune.consume);
                    runesJson.add(runeObj);
                }
                json.add("runes", runesJson);

                // 序列化魔法值和持续时间
                json.addProperty("mana", mana);
                json.addProperty("ticks", ticks);

                // 序列化物品输入
                JsonArray inputsJson = new JsonArray();
                for (Ingredient input : inputs) {
                    inputsJson.add(input.toJson());
                }
                json.add("inputs", inputsJson);

                // 序列化物品输出（使用Botania的ItemNBTHelper保持风格一致）
                JsonArray outputsJson = new JsonArray();
                for (ItemStack output : outputs) {
                    outputsJson.add(ItemNBTHelper.serializeStack(output));
                }
                json.add("outputs", outputsJson);

                // 序列化特殊输入/输出ID
                if (specialInputId != null) {
                    json.addProperty("special_input", specialInputId.toString());
                }
                if (specialOutputId != null) {
                    json.addProperty("special_output", specialOutputId.toString());
                }
            }

            @NotNull
            @Override
            public ResourceLocation getId() {
                return id;
            }

            @NotNull
            @Override
            public RecipeSerializer<?> getType() {
                return RuneRitualRecipe.Serializer.INSTANCE;
            }

            @Nullable
            @Override
            public JsonObject serializeAdvancement() {
                return null; // 不支持进阶
            }

            @Nullable
            @Override
            public ResourceLocation getAdvancementId() {
                return null;
            }
        });
    }

    // 内部辅助类：存储外围符文临时数据（序列化前）
    private record RunePositionData(Ingredient ingredient, int x, int z, boolean consume) {}
}
