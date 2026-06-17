package com.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.compat.FeCompat;
import com.gregtechceu.gtceu.api.capability.item.IElectricItem;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.common.machine.electric.BatteryBufferMachine;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.Direction;
import net.minecraftforge.energy.IEnergyStorage;

import org.jetbrains.annotations.Nullable;
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

    @Shadow
    protected abstract void changed();

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

    @Inject(method = "acceptEnergyFromNetwork", at = @At("HEAD"), cancellable = true)
    private void acceptEnergyFromNetwork(Object o, @Nullable Direction side, long voltage, long energyToAdd, CallbackInfoReturnable<Long> cir) {
        if (side != null && !inputsEnergy(side)) {
            cir.setReturnValue(0L);
            return;
        }
        if (voltage > getInputVoltage()) {
            this$0.doExplosion(GTUtil.getExplosionPower(voltage));
            cir.setReturnValue(0L);
            return;
        }

        var batteries = ((BatteryBufferMachineAccess) this$0).gtocore$getNonFullBatteries();
        var size = batteries.size();
        if (size == 0) {
            cir.setReturnValue(0L);
            return;
        }

        long canAccept = Math.min(size * voltage, Math.min(energyToAdd, gtocore$getEnergyCanBeInserted(batteries)));
        if (canAccept == 0) {
            cir.setReturnValue(0L);
            return;
        }

        long distributed = canAccept / size;
        if (distributed == 0) {
            distributed = voltage;
        }

        long energyAdded = 0;
        for (Object item : batteries) {
            long charged = 0;
            if (item instanceof IElectricItem electricItem) {
                charged = electricItem.charge(Math.min(distributed, Math.min(canAccept, GTValues.V[electricItem.getTier()])), this$0.getTier(), true, false);
            } else if (item instanceof IEnergyStorage energyStorage) {
                charged = FeCompat.insertEu(energyStorage, Math.min(distributed, Math.min(canAccept, GTValues.V[this$0.getTier()])), false);
            }
            if (charged > 0) {
                canAccept -= charged;
                energyAdded += charged;
                if (canAccept < 1) break;
            }
        }
        if (energyAdded > 0) {
            changed();
        }
        cir.setReturnValue(energyAdded);
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

    @Unique
    private long gtocore$getEnergyCanBeInserted(Iterable<Object> batteries) {
        long total = 0;
        for (Object battery : batteries) {
            if (battery instanceof IElectricItem electricItem) {
                total = BatteryBufferMachineAccess.gtocore$addClamped(total, electricItem.getMaxCharge() - electricItem.getCharge());
            } else if (battery instanceof IEnergyStorage energyStorage) {
                long remaining = energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored();
                total = BatteryBufferMachineAccess.gtocore$addClamped(total, FeCompat.toEu(remaining, FeCompat.ratio(false)));
            }
        }
        return total;
    }
}
