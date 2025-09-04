package com.gtocore.mixin.gtm.machine;

import com.gtocore.data.IdleReason;

import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.machine.multiblock.electric.AssemblyLineMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AssemblyLineMachine.class)
public class AssemblyLineMachineMixin {

    @Inject(method = "checkItemInputs", at = @At("RETURN"), remap = false)
    public void checkItemInputs(GTRecipe recipe, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) IdleReason.setIdleReason((IRecipeLogicMachine) this, IdleReason.ORDERED);
    }

    @Inject(method = "checkFluidInputs", at = @At("RETURN"), remap = false)
    public void checkFluidInputs(GTRecipe recipe, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) IdleReason.setIdleReason((IRecipeLogicMachine) this, IdleReason.ORDERED);
    }
}
