package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.integration.ae2.machine.MEOutputBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.utils.KeyStorage;

import com.hepdd.gtmthings.api.machine.trait.InaccessibleInfiniteHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MEOutputBusPartMachine.class)
public class MEOutputBusPartMachineMixin {

    @Shadow(remap = false)
    private KeyStorage internalBuffer;

    @Inject(method = "createInventory", at = @At("HEAD"), remap = false, cancellable = true)
    private void createInventory(Object[] args, CallbackInfoReturnable<NotifiableItemStackHandler> cir) {
        this.internalBuffer = new KeyStorage();
        cir.setReturnValue(new InaccessibleInfiniteHandler((MEOutputBusPartMachine) (Object) this, internalBuffer));
    }
}
