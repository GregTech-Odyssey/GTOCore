package com.gtocore.mixin.gtm.registry;

import com.gtolib.utils.register.RecipeTypeRegisterUtils;

import com.gregtechceu.gtceu.api.recipe.*;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.world.item.crafting.RecipeType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GTRecipeTypes.class)
public final class GTRecipeTypesMixin {

    @Inject(method = "register", at = @At("HEAD"), remap = false, cancellable = true)
    private static void register(String name, String group, RecipeType<?>[] proxyRecipes, CallbackInfoReturnable<GTRecipeType> cir) {
        cir.setReturnValue(RecipeTypeRegisterUtils.register(name, group, proxyRecipes));
    }
}
