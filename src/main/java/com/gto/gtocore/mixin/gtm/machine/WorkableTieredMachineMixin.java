package com.gto.gtocore.mixin.gtm.machine;

import com.gto.gtocore.common.machine.multiblock.generator.GeneratorArrayMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;
import com.gregtechceu.gtceu.common.item.IntCircuitBehaviour;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorkableTieredMachine.class)
public abstract class WorkableTieredMachineMixin extends TieredEnergyMachine implements IRecipeLogicMachine {

    public WorkableTieredMachineMixin(IMachineBlockEntity holder, int tier, Object... args) {
        super(holder, tier, args);
    }

    @Inject(method = "createEnergyContainer", at = @At(value = "HEAD"), remap = false, cancellable = true)
    protected void createEnergyContainer(Object[] args, CallbackInfoReturnable<NotifiableEnergyContainer> cir) {
        long tierVoltage = GTValues.V[getTier()];
        if (isEnergyEmitter()) {
            int amperage = GeneratorArrayMachine.getAmperage(this.getTier());
            cir.setReturnValue(NotifiableEnergyContainer.emitterContainer(this,
                    tierVoltage * amperage * 64,
                    tierVoltage, amperage));
        }
    }

    @Inject(method = "createImportItemHandler", at = @At(value = "HEAD"), remap = false, cancellable = true)
    protected void createImportItemHandler(Object[] args, CallbackInfoReturnable<NotifiableItemStackHandler> cir) {
        cir.setReturnValue(new NotifiableItemStackHandler(this, getRecipeType().getMaxInputs(ItemRecipeCapability.CAP), IO.IN)
                .setFilter((itemStack -> !IntCircuitBehaviour.isIntegratedCircuit(itemStack))));
    }
}
