package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.SteamParallelMultiblockMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.gregtechceu.gtceu.common.data.GTMachines.STEAM_OVEN;

@Mixin(SteamParallelMultiblockMachine.class)
public class SteamParallelMultiblockMachineMixin extends WorkableMultiblockMachine {

    public SteamParallelMultiblockMachineMixin(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Inject(method = "recipeModifier", at = @At("HEAD"), remap = false, cancellable = true)
    private static void recipeModifier(MetaMachine machine, GTRecipe recipe, OCParams params, OCResult result, CallbackInfoReturnable<GTRecipe> cir) {
        if (machine.getDefinition() == STEAM_OVEN) {
            var recipe1 = GTRecipeModifiers.accurateParallel(machine, recipe, 256, false)
                    .getFirst();
            recipe1.duration = (int) Math.max(1, recipe1.duration * 0.5);
            cir.setReturnValue(recipe1);
        }
    }
}
