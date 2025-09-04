package com.gtocore.mixin.gtm.machine;

import com.gtolib.api.machine.feature.IElectricMachine;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.machine.TieredEnergyMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TieredEnergyMachine.class)
public abstract class TieredEnergyMachineMixin implements IElectricMachine {

    @Shadow(remap = false)
    @Final
    public NotifiableEnergyContainer energyContainer;

    @Override
    public @NotNull IEnergyContainer gtolib$getEnergyContainer() {
        return energyContainer;
    }
}
