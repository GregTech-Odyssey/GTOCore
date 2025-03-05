package com.gto.gtocore.mixin.gtmt;

import com.gto.gtocore.utils.register.RecipeTypeRegisterUtils;

import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import net.minecraft.world.item.crafting.RecipeType;

import com.hepdd.gtmthings.data.GTMTRecipeTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GTMTRecipeTypes.class)
public class GTMTRecipeTypesMixin {

    @Inject(method = "register", at = @At("HEAD"), remap = false, cancellable = true)
    private static void register(String name, String group, RecipeType<?>[] proxyRecipes, CallbackInfoReturnable<GTRecipeType> cir) {
        cir.setReturnValue(RecipeTypeRegisterUtils.register(name, group, proxyRecipes));
    }
}
