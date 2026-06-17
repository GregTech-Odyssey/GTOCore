package com.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.capability.compat.FeCompat;
import com.gregtechceu.gtceu.api.capability.item.IElectricItem;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.common.machine.electric.BatteryBufferMachine;

import net.minecraftforge.energy.IEnergyStorage;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.gregtechceu.gtceu.common.machine.electric.BatteryBufferMachine$EnergyBatteryTrait", remap = false)
public abstract class BatteryBufferEnergyTraitMixin extends NotifiableEnergyContainer {

    @Shadow
    @Final
    private BatteryBufferMachine this$0;

    protected BatteryBufferEnergyTraitMixin(MetaMachine machine, long maxCapacity, long maxInputVoltage, long maxInputAmperage, long maxOutputVoltage, long maxOutputAmperage) {
        super(machine, maxCapacity, maxInputVoltage, maxInputAmperage, maxOutputVoltage, maxOutputAmperage);
    }

    @Override
    public long getEnergyCapacity() {
        return gtocore$sumBatteryEnergy(true);
    }

    @Inject(method = "getEnergyStored", at = @At("HEAD"), cancellable = true)
    private void getEnergyStored(CallbackInfoReturnable<Long> cir) {
        var buffer = (BatteryBufferMachineAccess) this$0;
        if (buffer.gtocore$checkEnergyStored()) {
            energyStored = gtocore$sumBatteryEnergy(false);
            buffer.gtocore$setCheckEnergyStored(false);
        }
        cir.setReturnValue(energyStored);
    }

    @Unique
    private long gtocore$sumBatteryEnergy(boolean capacity) {
        long total = 0;
        for (Object battery : ((BatteryBufferMachineAccess) this$0).gtocore$getAllBatteries()) {
            if (battery instanceof IElectricItem electricItem) {
                total = BatteryBufferMachineAccess.gtocore$addClamped(total, capacity ? electricItem.getMaxCharge() : electricItem.getCharge());
            } else if (battery instanceof IEnergyStorage energyStorage) {
                long amount = capacity ? energyStorage.getMaxEnergyStored() : energyStorage.getEnergyStored();
                total = BatteryBufferMachineAccess.gtocore$addClamped(total, FeCompat.toEu(amount, FeCompat.ratio(false)));
            }
        }
        return total;
    }
}
