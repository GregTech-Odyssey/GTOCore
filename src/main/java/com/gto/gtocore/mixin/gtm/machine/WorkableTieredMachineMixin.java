package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.common.machine.multiblock.generator.GeneratorArrayMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorkableTieredMachine.class)
public abstract class WorkableTieredMachineMixin extends TieredEnergyMachine implements IRecipeLogicMachine {

    protected WorkableTieredMachineMixin(IMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, args);
    }

    @Inject(method = "createEnergyContainer", at = @At(value = "HEAD"), remap = false, cancellable = true)
    private void createEnergyContainer(Object[] args, CallbackInfoReturnable<NotifiableEnergyContainer> cir) {
        long tierVoltage = GTValues.V[getTier()];
        if (isEnergyEmitter()) {
            int amperage = GeneratorArrayMachine.getAmperage(getTier());
            cir.setReturnValue(NotifiableEnergyContainer.emitterContainer(this,
                    tierVoltage * amperage * 64,
                    tierVoltage, amperage));
        }
    }
}
