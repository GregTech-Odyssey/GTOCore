package com.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.machine.multiblock.primitive.PrimitiveBlastFurnaceMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PrimitiveBlastFurnaceMachine.class)
public class PrimitiveBlastFurnaceMachineMixin {

    /**
     * @author Kovrax
     * @reason enable PBF IO behavior
     */
    @Inject(method = "createImportItemHandler", at = @At("HEAD"), remap = false, cancellable = true)
    private void createImportItemHandlerMixin(Object[] args, CallbackInfoReturnable<NotifiableItemStackHandler> cir) {
        cir.setReturnValue(new NotifiableItemStackHandler((MetaMachine) (Object) this, 3, IO.IN));
    }

    /**
     * @author Kovrax
     * @reason enable PBF IO behavior
     */
    @Inject(method = "createExportItemHandler", at = @At("HEAD"), remap = false, cancellable = true)
    private void createExportItemHandlerMixin(Object[] args, CallbackInfoReturnable<NotifiableItemStackHandler> cir) {
        cir.setReturnValue(new NotifiableItemStackHandler((MetaMachine) (Object) this, 3, IO.OUT));
    }
}
