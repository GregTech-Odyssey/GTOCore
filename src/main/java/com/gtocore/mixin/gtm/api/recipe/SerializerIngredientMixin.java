package com.gtocore.mixin.gtm.api.recipe;

import com.gtolib.api.recipe.ingredient.FastSizedIngredient;

import com.gregtechceu.gtceu.api.recipe.content.SerializerIngredient;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SerializerIngredient.class)
public class SerializerIngredientMixin {

    /**
     * @author .
     * @reason 换成FastSizedIngredient
     */
    @Overwrite(remap = false)
    public Ingredient of(Object o) {
        if (o instanceof Ingredient ingredient) {
            return ingredient;
        } else if (o instanceof ItemStack itemStack) {
            return FastSizedIngredient.create(itemStack);
        } else if (o instanceof ItemLike itemLike) {
            return FastSizedIngredient.create(itemLike);
        } else if (o instanceof TagKey tag) {
            return Ingredient.of(tag);
        }
        return Ingredient.EMPTY;
    }
}
