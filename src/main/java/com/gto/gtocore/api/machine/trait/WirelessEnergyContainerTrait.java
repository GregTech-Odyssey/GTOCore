package com.gto.gtocore.api.machine.trait;

import com.gto.gtocore.api.machine.feature.IExtendWirelessEnergyContainerHolder;
import com.gto.gtocore.common.wireless.ExtendTransferData;
import com.gto.gtocore.common.wireless.ExtendWirelessEnergyContainer;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;

import net.minecraft.core.Direction;

import com.hepdd.gtmthings.api.misc.BasicTransferData;
import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class WirelessEnergyContainerTrait extends NotifiableEnergyContainer implements IExtendWirelessEnergyContainerHolder {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            WirelessEnergyContainerTrait.class, NotifiableEnergyContainer.MANAGED_FIELD_HOLDER);

    private WirelessEnergyContainer WirelessEnergyContainerCache;

    @Persisted
    private UUID UUID;

    private WirelessEnergyContainerTrait(MetaMachine machine, long maxCapacity, long maxInputVoltage, long maxInputAmperage, long maxOutputVoltage, long maxOutputAmperage) {
        super(machine, maxCapacity, maxInputVoltage, maxInputAmperage, maxOutputVoltage, maxOutputAmperage);
    }

    public static WirelessEnergyContainerTrait emitterContainer(MetaMachine machine, long maxCapacity, long maxOutputVoltage, long maxOutputAmperage) {
        return new WirelessEnergyContainerTrait(machine, maxCapacity, 0L, 0L, maxOutputVoltage, maxOutputAmperage);
    }

    public static WirelessEnergyContainerTrait receiverContainer(MetaMachine machine, long maxCapacity, long maxInputVoltage, long maxInputAmperage) {
        return new WirelessEnergyContainerTrait(machine, maxCapacity, maxInputVoltage, maxInputAmperage, 0L, 0L);
    }

    @Override
    public long changeEnergy(long energyToAdd) {
        if (energyToAdd == 0) return 0;
        ExtendWirelessEnergyContainer container = getWirelessEnergyContainer();
        if (container == null) return 0;
        long oldEnergyStored = getEnergyStored();
        long change;
        if (energyToAdd > 0) {
            change = Math.min(energyToAdd, Math.min(getEnergyCapacity() - oldEnergyStored, container.getRate()));
            if (change > 0 && WirelessEnergyContainer.observed && getMachine() != null) {
                long loss = (change / 1000) * container.getLoss();
                WirelessEnergyContainer.TRANSFER_DATA.put(getMachine(), new ExtendTransferData(UUID, change - loss, loss));
            }
        } else {
            change = Math.min(-energyToAdd, Math.min(oldEnergyStored, container.getRate()));
            change = -change;
            if (change < 0 && WirelessEnergyContainer.observed && getMachine() != null) {
                WirelessEnergyContainer.TRANSFER_DATA.put(getMachine(), new BasicTransferData(UUID, change));
            }
        }
        setEnergyStored(oldEnergyStored + change);
        return change;
    }

    @Override
    public void updateTick() {
        super.updateTick();
        if (!getMachine().isRemote() && getMachine().getOffsetTimer() % 10 == 0) {
            ExtendWirelessEnergyContainer container = getWirelessEnergyContainer();
            if (container != null) {
                long energyStored = getEnergyStored();
                if (handlerIO == IO.IN) {
                    long canInput = getEnergyCapacity() - energyStored;
                    setEnergyStored(energyStored + container.unrestrictedRemoveEnergy(canInput));
                } else {
                    setEnergyStored(energyStored - container.unrestrictedAddEnergy(energyStored));
                }
            }
        }
    }

    @Override
    public void checkOutputSubscription() {}

    @Override
    public void serverTick() {}

    @Override
    public long acceptEnergyFromNetwork(Direction side, long voltage, long amperage) {
        return 0;
    }

    @Override
    public boolean outputsEnergy(Direction side) {
        return false;
    }

    @Override
    public boolean inputsEnergy(Direction side) {
        return false;
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
