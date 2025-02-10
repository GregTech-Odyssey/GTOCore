package com.gto.gtocore.mixin.emi;

import com.gto.gtocore.common.data.GTORecipes;
import com.gto.gtocore.integration.kjs.GTKubeJSPlugin;

import com.gregtechceu.gtceu.integration.emi.recipe.GTRecipeEMICategory;

import dev.emi.emi.api.EmiRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GTRecipeEMICategory.class)
public class GTRecipeEMICategoryMixin {

    @Inject(method = "registerDisplays", at = @At("HEAD"), remap = false, cancellable = true)
    private static void registerDisplays(EmiRegistry registry, CallbackInfo ci) {
        if (GTKubeJSPlugin.hasKJSGTRecipe) {
            GTORecipes.initKJSCategoryMap();
        } else {
            ci.cancel();
        }
    }
}
