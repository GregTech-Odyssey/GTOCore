package com.gto.gtocore.api.machine.trait;

import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.MachineTrait;

import net.minecraft.core.Direction;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class EnergyContainerTrait extends MachineTrait implements IEnergyContainer {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(EnergyContainerTrait.class);

    @Setter
    @Persisted
    protected long energyStored;
    private long energyCapacity;

    public EnergyContainerTrait(MetaMachine machine, long maxCapacity) {
        super(machine);
        this.energyCapacity = maxCapacity;
    }

    public void resetBasicInfo(long maxCapacity) {
        this.energyCapacity = maxCapacity;
    }

    @Override
    public long acceptEnergyFromNetwork(Direction side, long voltage, long amperage) {
        return 0;
    }

    @Override
    public boolean inputsEnergy(Direction side) {
        return false;
    }

    @Override
    public long changeEnergy(long energyToAdd) {
        long oldEnergyStored = energyStored;
        long newEnergyStored = (energyCapacity - oldEnergyStored < energyToAdd) ? energyCapacity : (oldEnergyStored + energyToAdd);
        if (newEnergyStored < 0) newEnergyStored = 0;
        setEnergyStored(newEnergyStored);
        return newEnergyStored - oldEnergyStored;
    }

    @Override
    public long getInputAmperage() {
        return 0;
    }

    @Override
    public long getInputVoltage() {
        return 0;
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
