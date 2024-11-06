package com.gto.gtocore.mixin.mc;

import net.minecraft.client.ClientRecipeBook;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.Recipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookMixin {

    @Inject(method = "setupCollections", at = @At("HEAD"), cancellable = true)
    public void onSetup(Iterable<Recipe<?>> iterable, RegistryAccess registryAccess, CallbackInfo ci) {
        ci.cancel();
    }
}
