package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.common.data.GTOItems;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.TieredPartMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.machine.multiblock.part.DataAccessHatchMachine;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DataAccessHatchMachine.class)
public class DataAccessHatchMachineMixin extends TieredPartMachine {

    @Shadow(remap = false)
    @Final
    public NotifiableItemStackHandler importItems;

    public DataAccessHatchMachineMixin(IMachineBlockEntity holder, int tier) {
        super(holder, tier);
    }

    @Inject(method = "<init>", at = @At("TAIL"), remap = false)
    private void init(IMachineBlockEntity holder, int tier, boolean isCreative, CallbackInfo ci) {
        importItems.setFilter(i -> !(i.is(GTOItems.QUANTUM_DISK.get()) && getTier() != GTValues.UV));
    }

    @Inject(method = "getInventorySize", at = @At("TAIL"), remap = false, cancellable = true)
    private void getInventorySize(CallbackInfoReturnable<Integer> cir) {
        if (getTier() == GTValues.UV) cir.setReturnValue(36);
    }
}
