package com.gto.gtocore.mixin.gtm;

import com.gto.gtocore.api.recipe.FastSizedIngredient;

import com.gregtechceu.gtceu.api.recipe.ingredient.IntProviderIngredient;
import com.gregtechceu.gtceu.utils.IngredientEquality;

import net.minecraft.world.item.crafting.Ingredient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(IngredientEquality.class)
public class IngredientEqualityMixin {

    @Shadow(remap = false)
    private static boolean cmp(Ingredient first, Ingredient second) {
        return false;
    }

    /**
     * @author .
     * @reason 换成FastSizedIngredient
     */
    @Overwrite(remap = false)
    public static boolean ingredientEquals(Ingredient first, Ingredient second) {
        if (first == second) return true;
        if ((first == null) != (second == null)) {
            return false;
        }

        if (first instanceof FastSizedIngredient sized1) {
            if (second instanceof FastSizedIngredient sized2) {
                return cmp(sized1.getInner(), sized2.getInner());
            } else if (second instanceof IntProviderIngredient intProvider2) {
                return cmp(sized1.getInner(), intProvider2.getInner());
            } else {
                return cmp(sized1.getInner(), second);
            }
        } else if (first instanceof IntProviderIngredient intProvider1) {
            if (second instanceof IntProviderIngredient intProvider2) {
                return cmp(intProvider1.getInner(), intProvider2.getInner());
            } else if (second instanceof FastSizedIngredient sized) {
                return cmp(intProvider1.getInner(), sized.getInner());
            } else {
                return cmp(intProvider1.getInner(), second);
            }
        } else if (second instanceof FastSizedIngredient sized2) {
            return cmp(first, sized2.getInner());
        } else if (second instanceof IntProviderIngredient intProvider2) {
            return cmp(first, intProvider2.getInner());
        }
        return cmp(first, second);
    }
}
