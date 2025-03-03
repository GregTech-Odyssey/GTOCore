package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.machine.multiblock.primitive.PrimitiveBlastFurnaceMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.primitive.PrimitiveWorkableMachine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PrimitiveBlastFurnaceMachine.class)
public class PrimitiveBlastFurnaceMachineMixin extends PrimitiveWorkableMachine {

    public PrimitiveBlastFurnaceMachineMixin(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }


    /**
     * @author Kovrax
     * @reason  enable PBF IO behavior
     */
    @Inject(method = "createImportItemHandler", at = @At("HEAD"),remap = false, cancellable = true)
    protected void createImportItemHandlerMixin(Object[] args, CallbackInfoReturnable<NotifiableItemStackHandler> cir) {
       cir.setReturnValue(new NotifiableItemStackHandler(this, getRecipeType().getMaxInputs(ItemRecipeCapability.CAP), IO.IN));
       cir.cancel();
    }

    /**
     * @author Kovrax
     * @reason  enable PBF IO behavior
     */
    @Inject(method = "createExportItemHandler", at = @At("HEAD"), remap = false, cancellable = true)
    protected void createExportItemHandlerMixin(Object[] args, CallbackInfoReturnable<NotifiableItemStackHandler> cir) {
        cir.setReturnValue(new NotifiableItemStackHandler(this, getRecipeType().getMaxInputs(ItemRecipeCapability.CAP), IO.OUT));
        cir.cancel();
    }
}
