package com.gto.gtocore.mixin.gtm.api.recipe;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.lookup.GTRecipeLookup;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GTRecipeLookup.class)
public class GTRecipeLookupMixin {

    @Inject(method = "addRecipe", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/recipe/category/GTRecipeCategory;addRecipe(Lcom/gregtechceu/gtceu/api/recipe/GTRecipe;)V"), remap = false, cancellable = true)
    private void addRecipe(GTRecipe recipe, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "removeAllRecipes", at = @At(value = "INVOKE", target = "Lcom/gregtechceu/gtceu/api/recipe/GTRecipeType;getCategoryMap()Ljava/util/Map;"), remap = false, cancellable = true)
    private void removeAllRecipes(CallbackInfo ci) {
        ci.cancel();
    }
}
