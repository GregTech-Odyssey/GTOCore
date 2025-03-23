package com.gto.gtocore.utils;

import com.gto.gtocore.api.item.IItem;

import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public final class ItemUtils {

    public static ItemStack getFirstSized(Ingredient ingredient) {
        if (ingredient instanceof SizedIngredient sizedIngredient) {
            return getFirstSized(sizedIngredient);
        }
        return getFirst(ingredient);
    }

    public static ItemStack getFirstSized(SizedIngredient sizedIngredient) {
        return getFirst(getSizedInner(sizedIngredient));
    }

    public static Ingredient getSizedInner(SizedIngredient sizedIngredient) {
        Ingredient inner = sizedIngredient.getInner();
        if (inner instanceof SizedIngredient ingredient) {
            return getSizedInner(ingredient);
        }
        return inner;
    }

    public static Ingredient getInnerIngredient(Ingredient ingredient) {
        if (ingredient instanceof SizedIngredient sizedIngredient) {
            return getSizedInner(sizedIngredient);
        }
        return ingredient;
    }

    public static ItemStack getFirst(Ingredient ingredient) {
        for (ItemStack stack : ingredient.getItems()) {
            if (!stack.isEmpty()) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static String getId(Block block) {
        return getIdLocation(block.asItem()).toString();
    }

    public static String getId(ItemStack item) {
        return getIdLocation(item.getItem()).toString();
    }

    public static String getId(Item item) {
        return getIdLocation(item).toString();
    }

    public static ResourceLocation getIdLocation(Block block) {
        return ((IItem) block.asItem()).gtocore$getIdLocation();
    }

    public static ResourceLocation getIdLocation(Item item) {
        return ((IItem) item).gtocore$getIdLocation();
    }
}
