package com.gtocore.data.recipe.builder.vanilla;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTRecipes;
import com.gregtechceu.gtceu.data.recipe.builder.ShapedRecipeBuilder;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.StrictNBTIngredient;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

public class SmithingRecipeBuilder {

    public static SmithingRecipeBuilder builder(String id) {
        return new SmithingRecipeBuilder(GTOCore.id(id));
    }

    public static SmithingRecipeBuilder builder() {
        return new SmithingRecipeBuilder(null);
    }

    protected Ingredient template;
    protected Ingredient base;
    protected Ingredient addition;
    protected ItemStack result;

    @Setter
    @Accessors(fluent = true, chain = true)
    protected ResourceLocation id;

    public SmithingRecipeBuilder(@Nullable ResourceLocation id) {
        this.id = id;
    }

    public SmithingRecipeBuilder template(TagKey<Item> tagKey) {
        return template(ShapedRecipeBuilder.INGREDIENT_TAG_FUNCTION.apply(tagKey));
    }

    public SmithingRecipeBuilder template(ItemStack itemStack) {
        return template(itemStack.hasTag() ? StrictNBTIngredient.of(itemStack) : ShapedRecipeBuilder.INGREDIENT_ITEM_FUNCTION.apply(itemStack.getItem()));
    }

    public SmithingRecipeBuilder template(ItemLike itemLike) {
        return template(ShapedRecipeBuilder.INGREDIENT_ITEM_FUNCTION.apply(itemLike.asItem()));
    }

    public SmithingRecipeBuilder template(Ingredient ingredient) {
        template = ingredient;
        return this;
    }

    public SmithingRecipeBuilder input(TagKey<Item> tagKey) {
        return input(ShapedRecipeBuilder.INGREDIENT_TAG_FUNCTION.apply(tagKey));
    }

    public SmithingRecipeBuilder input(ItemStack itemStack) {
        return input(itemStack.hasTag() ? StrictNBTIngredient.of(itemStack) : ShapedRecipeBuilder.INGREDIENT_ITEM_FUNCTION.apply(itemStack.getItem()));
    }

    public SmithingRecipeBuilder input(ItemLike itemLike) {
        return input(ShapedRecipeBuilder.INGREDIENT_ITEM_FUNCTION.apply(itemLike.asItem()));
    }

    public SmithingRecipeBuilder input(Ingredient ingredient) {
        base = ingredient;
        return this;
    }

    public SmithingRecipeBuilder addition(TagKey<Item> tagKey) {
        return addition(ShapedRecipeBuilder.INGREDIENT_TAG_FUNCTION.apply(tagKey));
    }

    public SmithingRecipeBuilder addition(ItemStack itemStack) {
        return addition(itemStack.hasTag() ? StrictNBTIngredient.of(itemStack) : ShapedRecipeBuilder.INGREDIENT_ITEM_FUNCTION.apply(itemStack.getItem()));
    }

    public SmithingRecipeBuilder addition(ItemLike itemLike) {
        return addition(ShapedRecipeBuilder.INGREDIENT_ITEM_FUNCTION.apply(itemLike.asItem()));
    }

    public SmithingRecipeBuilder addition(TagPrefix tagPrefix, Material material) {
        return addition(ChemicalHelper.getItem(tagPrefix, material));
    }

    public SmithingRecipeBuilder addition(Ingredient ingredient) {
        addition = ingredient;
        return this;
    }

    public SmithingRecipeBuilder output(ItemLike itemLike) {
        this.result = new ItemStack(itemLike.asItem());
        return this;
    }

    public SmithingRecipeBuilder output(ItemStack itemStack) {
        this.result = itemStack.copy();
        return this;
    }

    public SmithingRecipeBuilder output(ItemStack itemStack, int count) {
        this.result = itemStack.copy();
        this.result.setCount(count);
        return this;
    }

    public SmithingRecipeBuilder output(ItemStack itemStack, int count, CompoundTag nbt) {
        this.result = itemStack.copy();
        this.result.setCount(count);
        this.result.setTag(nbt);
        return this;
    }

    protected ResourceLocation defaultId() {
        return GTUtil.ITEM_ID.apply(result.getItem());
    }

    public ResourceLocation getId() {
        var ID = id == null ? defaultId() : id;
        return GTUtil.getResourceLocation(ID.getNamespace(), "smithing" + "/" + ID.getPath());
    }

    public void save() {
        var id = getId();
        if (GTRecipes.RECIPE_MAP.put(id, new SmithingTransformRecipe(id, template, base, addition, result)) != null) throw new IllegalStateException();
    }
}
