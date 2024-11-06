package com.gto.gtocore.mixin.gtmt;

import net.minecraft.data.recipes.FinishedRecipe;

import com.hepdd.gtmthings.data.GTMTRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(GTMTRecipe.class)
public class GTMTRecipeMixin {

    @Inject(method = "init", at = @At("HEAD"), remap = false, cancellable = true)
    private static void init(Consumer<FinishedRecipe> provider, CallbackInfo ci) {
        ci.cancel();
    }
}
