package com.gto.gtocore.mixin.gtm.recipe;

import com.gregtechceu.gtceu.data.recipe.misc.GCyMRecipes;

import net.minecraft.data.recipes.FinishedRecipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(GCyMRecipes.class)
public class GCyMRecipesMixin {

    @Inject(method = "registerMultiblockControllerRecipes", at = @At("HEAD"), remap = false, cancellable = true)
    private static void registerMultiblockControllerRecipes(Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "registerPartsRecipes", at = @At("HEAD"), remap = false, cancellable = true)
    private static void registerPartsRecipes(Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        ci.cancel();
    }
}
