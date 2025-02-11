package com.gto.gtocore.mixin.gtm.capability;

import com.gto.gtocore.api.machine.feature.multiblock.IMEOutputMachine;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidRecipeCapability.class)
public class FluidRecipeCapabilityMixin {

    @Inject(method = "limitParallel", at = @At("HEAD"), remap = false, cancellable = true)
    private void limitParallel(GTRecipe recipe, IRecipeCapabilityHolder holder, int multiplier, CallbackInfoReturnable<Integer> cir) {
        if (holder instanceof IMEOutputMachine machine && machine.gTOCore$isFluidOutput()) cir.setReturnValue(multiplier);
    }
}
