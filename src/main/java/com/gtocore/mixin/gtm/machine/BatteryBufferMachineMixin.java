package com.gtocore.mixin.gtm.machine;

import com.gregtechceu.gtceu.common.machine.electric.BatteryBufferMachine;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(BatteryBufferMachine.class)
public abstract class BatteryBufferMachineMixin implements BatteryBufferMachineAccess {

    @Shadow(remap = false)
    private List<Object> getAllBatteries() {
        throw new AssertionError();
    }

    @Shadow(remap = false)
    protected boolean checkEnergyStored;

    @Override
    public List<Object> gtocore$getAllBatteries() {
        return getAllBatteries();
    }

    @Override
    public boolean gtocore$checkEnergyStored() {
        return checkEnergyStored;
    }

    @Override
    public void gtocore$setCheckEnergyStored(boolean checkEnergyStored) {
        this.checkEnergyStored = checkEnergyStored;
    }
}

interface BatteryBufferMachineAccess {

    List<Object> gtocore$getAllBatteries();

    boolean gtocore$checkEnergyStored();

    void gtocore$setCheckEnergyStored(boolean checkEnergyStored);

    static long gtocore$addClamped(long stored, long added) {
        // ponytail: IEnergyContainer is long-sized; clamp aggregate battery totals here.
        return added > Long.MAX_VALUE - stored ? Long.MAX_VALUE : stored + added;
    }
}
