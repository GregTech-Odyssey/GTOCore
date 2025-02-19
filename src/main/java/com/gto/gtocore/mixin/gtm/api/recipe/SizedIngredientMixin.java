package com.gto.gtocore.mixin.gtm.api.recipe;

import com.gregtechceu.gtceu.api.recipe.ingredient.SizedIngredient;

import net.minecraft.world.item.crafting.Ingredient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SizedIngredient.class)
public class SizedIngredientMixin {

    @Inject(method = "copy", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/recipe/ingredient/SizedIngredient;create(Lnet/minecraft/world/item/crafting/Ingredient;I)Lcom/gregtechceu/gtceu/api/recipe/ingredient/SizedIngredient;"), remap = false, cancellable = true)
    private static void copy(Ingredient ingredient, CallbackInfoReturnable<Ingredient> cir) {
        cir.setReturnValue(SizedIngredient.create(((SizedIngredient) ingredient).getInner(), ((SizedIngredient) ingredient).getAmount()));
    }
}
