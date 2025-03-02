package com.gto.gtocore.utils;

import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import dev.latvian.mods.kubejs.core.IngredientKJS;
import dev.latvian.mods.kubejs.core.ItemKJS;

public final class ItemUtils {

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

    public static ItemStack getFirst(Ingredient ingredient) {
        return ((IngredientKJS) ingredient).kjs$getFirst();
    }

    public static String getId(Block block) {
        return ((ItemKJS) block.asItem()).kjs$getId();
    }

    public static String getId(ItemStack item) {
        return ((ItemKJS) item.getItem()).kjs$getId();
    }

    public static String getId(Item item) {
        return ((ItemKJS) item).kjs$getId();
    }

    public static ResourceLocation getIdLocation(Block block) {
        return ((ItemKJS) block.asItem()).kjs$getIdLocation();
    }

    public static ResourceLocation getIdLocation(Item item) {
        return ((ItemKJS) item).kjs$getIdLocation();
    }
}
