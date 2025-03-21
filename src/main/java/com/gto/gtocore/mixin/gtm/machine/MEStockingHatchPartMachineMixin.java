package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.api.machine.IMEHatchPart;
import com.gto.gtocore.api.machine.trait.ExportOnlyAEStockingFluidList;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.integration.ae2.machine.MEInputHatchPartMachine;
import com.gregtechceu.gtceu.integration.ae2.machine.MEStockingHatchPartMachine;

import appeng.api.networking.security.IActionSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MEStockingHatchPartMachine.class)
public class MEStockingHatchPartMachineMixin extends MEInputHatchPartMachine implements IMEHatchPart {

    public MEStockingHatchPartMachineMixin(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Inject(method = "createTank", at = @At("HEAD"), remap = false, cancellable = true)
    protected void createInventory(int initialCapacity, int slots, Object[] args, CallbackInfoReturnable<NotifiableFluidTank> cir) {
        this.aeFluidHandler = new ExportOnlyAEStockingFluidList(this, CONFIG_SIZE);
        cir.setReturnValue(aeFluidHandler);
    }

    @Override
    public IActionSource gtocore$getActionSource() {
        return actionSource;
    }
}
