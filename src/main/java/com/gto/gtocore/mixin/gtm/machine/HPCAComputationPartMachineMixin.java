package com.gto.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.common.machine.multiblock.part.hpca.HPCAComponentPartMachine;
import com.gregtechceu.gtceu.common.machine.multiblock.part.hpca.HPCAComputationPartMachine;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HPCAComputationPartMachine.class)
public abstract class HPCAComputationPartMachineMixin extends HPCAComponentPartMachine {

    public HPCAComputationPartMachineMixin(IMachineBlockEntity holder) {
        super(holder);
    }

    @Shadow(remap = false)
    @Final
    private boolean advanced;

    @Inject(method = "getCWUPerTick", at = @At("HEAD"), remap = false, cancellable = true)
    public void getCWUPerTick(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(isDamaged() ? 0 : advanced ? 32 : 8);
    }
}
