package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.LargeBoilerMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LargeBoilerMachine.class)
public class LargeBoilerMachineMixin {

    @Inject(method = "recipeModifier", at = @At("HEAD"), remap = false, cancellable = true)
    private static void recipeModifier(MetaMachine machine, GTRecipe recipe, OCParams params, OCResult result, CallbackInfoReturnable<GTRecipe> cir) {
        if (machine instanceof LargeBoilerMachine largeBoilerMachine) {
            GTRecipe recipe1 = recipe.copy();
            double duration = recipe.duration * 1600D / largeBoilerMachine.maxTemperature;
            if (duration < 1) {
                recipe1 = recipe.copy(ContentModifier.multiplier(1 / duration), false);
                recipe1.duration = 1;
            } else {
                recipe1.duration = (int) duration;
            }
            if (largeBoilerMachine.getThrottle() < 100) {
                recipe1.duration = recipe1.duration * 100 / largeBoilerMachine.getThrottle();
                cir.setReturnValue(recipe1);
            }
            cir.setReturnValue(recipe1);
        }
    }
}
