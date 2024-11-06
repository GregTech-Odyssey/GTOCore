package com.gto.gtocore.mixin.gtm.recipe;

import com.gto.gtocore.data.CraftingComponentAddition;

import com.gregtechceu.gtceu.data.recipe.CraftingComponent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = CraftingComponent.class, remap = false)
public class CraftingComponentMixin {

    @Inject(method = "initializeComponents()V", at = @At(value = "TAIL"))
    private static void initializeAddition(CallbackInfo ci) {
        CraftingComponentAddition.init();
    }
}
