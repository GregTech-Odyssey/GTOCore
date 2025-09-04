package com.gtocore.mixin.gtm.recipe;

import com.gtocore.data.CraftingComponents;

import com.gregtechceu.gtceu.data.recipe.CraftingComponent;
import com.gregtechceu.gtceu.data.recipe.GTCraftingComponents;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GTCraftingComponents.class)
public class CraftingComponentMixin {

    @Inject(method = "init", at = @At(value = "TAIL"), remap = false)
    private static void initializeAddition(CallbackInfo ci) {
        CraftingComponents.init();
        CraftingComponent.ALL_COMPONENTS.clear();
    }
}
