package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.api.machine.trait.InaccessibleInfiniteHandler;
import com.gto.gtocore.integration.ae2.KeyMap;

import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.integration.ae2.machine.MEOutputBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.utils.KeyStorage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MEOutputBusPartMachine.class)
public class MEOutputBusPartMachineMixin {

    @Shadow(remap = false)
    private KeyStorage internalBuffer;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    protected boolean shouldSubscribe() {
        return false;
    }

    @Inject(method = "createInventory", at = @At("HEAD"), remap = false, cancellable = true)
    private void createInventory(Object[] args, CallbackInfoReturnable<NotifiableItemStackHandler> cir) {
        this.internalBuffer = new KeyMap();
        cir.setReturnValue(new InaccessibleInfiniteHandler((MEOutputBusPartMachine) (Object) this, (KeyMap) internalBuffer));
    }
}
