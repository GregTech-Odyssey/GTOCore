package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.common.machine.multiblock.generator.GeneratorArrayMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.SimpleGeneratorMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleGeneratorMachine.class)
public class SimpleGeneratorMachineMixin {

    @Inject(method = "recipeModifier", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private static void recipeModifier(MetaMachine machine, GTRecipe recipe, OCParams params, OCResult result, CallbackInfoReturnable<GTRecipe> cir) {
        if (machine instanceof SimpleGeneratorMachine generator) {
            var EUt = RecipeHelper.getOutputEUt(recipe);
            if (EUt > 0) {
                int Parallel = (int) ((GTValues.V[generator.getTier()] * GeneratorArrayMachine.getAmperage(generator.getTier())) / EUt);
                GTRecipe recipe1 = GTRecipeModifiers.fastParallel(generator, recipe, Parallel, false).getFirst();
                recipe1.duration = recipe1.duration * GeneratorArrayMachine.getEfficiency(generator.getRecipeType(), generator.getTier()) / 100;
                cir.setReturnValue(recipe1);
            }
        }
    }
}
