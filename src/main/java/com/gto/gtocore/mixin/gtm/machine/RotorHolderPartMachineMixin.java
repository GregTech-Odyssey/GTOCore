package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.item.TurbineRotorBehaviour;
import com.gregtechceu.gtceu.common.machine.multiblock.part.RotorHolderPartMachine;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RotorHolderPartMachine.class)
public class RotorHolderPartMachineMixin {

    @Shadow(remap = false)
    @Final
    public NotifiableItemStackHandler inventory;

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void onInit(CallbackInfo ci) {
        this.inventory.setFilter(i -> TurbineRotorBehaviour.getBehaviour(i) != null);
    }
}
