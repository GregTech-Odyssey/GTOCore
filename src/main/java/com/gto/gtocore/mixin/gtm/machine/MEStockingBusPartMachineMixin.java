package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.api.machine.trait.ExportOnlyAEStockingItemList;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.integration.ae2.machine.MEInputBusPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEStockingBusPartMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MEStockingBusPartMachine.class)
public class MEStockingBusPartMachineMixin extends MEInputBusPartMachine {

    public MEStockingBusPartMachineMixin(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Inject(method = "createInventory", at = @At("HEAD"), remap = false, cancellable = true)
    protected void createInventory(Object[] args, CallbackInfoReturnable<NotifiableItemStackHandler> cir) {
        this.aeItemHandler = new ExportOnlyAEStockingItemList(this, CONFIG_SIZE);
        cir.setReturnValue(aeItemHandler);
    }
}
