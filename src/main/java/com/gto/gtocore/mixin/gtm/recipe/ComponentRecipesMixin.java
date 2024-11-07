package com.gto.gtocore.mixin.gtm.recipe;

import com.gregtechceu.gtceu.data.recipe.misc.ComponentRecipes;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ComponentRecipes.class)
public class ComponentRecipesMixin {

    @Inject(method = "init", at = @At("HEAD"), remap = false, cancellable = true)
    private static void init(CallbackInfo ci) {
        ci.cancel();
    }
}
