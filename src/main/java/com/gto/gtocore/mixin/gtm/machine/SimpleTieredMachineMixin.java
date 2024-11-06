package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.api.machine.trait.NotifiableCircuitItemStackHandler;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.SimpleTieredMachine;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import it.unimi.dsi.fastutil.ints.Int2LongFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleTieredMachine.class)
public class SimpleTieredMachineMixin extends WorkableTieredMachine {

    public SimpleTieredMachineMixin(IMachineBlockEntity holder, int tier, Int2LongFunction tankScalingFunction, Object... args) {
        super(holder, tier, tankScalingFunction, args);
    }

    @Inject(method = "createCircuitItemHandler", at = @At(value = "HEAD"), remap = false, cancellable = true)
    protected void createCircuitItemHandler(CallbackInfoReturnable<NotifiableItemStackHandler> cir) {
        cir.setReturnValue(new NotifiableCircuitItemStackHandler(this));
    }
}
