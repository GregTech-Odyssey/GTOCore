package com.gto.gtocore.api.machine.trait;

import com.gto.gtocore.api.machine.feature.IExtendWirelessEnergyContainerHolder;
import com.gto.gtocore.common.wireless.ExtendTransferData;
import com.gto.gtocore.common.wireless.ExtendWirelessEnergyContainer;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.core.Direction;

import com.hepdd.gtmthings.api.misc.BasicTransferData;
import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public final class WirelessEnergyContainerTrait extends NotifiableEnergyContainer implements IExtendWirelessEnergyContainerHolder {

    private WirelessEnergyContainer WirelessEnergyContainerCache;

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
    public List<Long> handleRecipeInner(IO io, GTRecipe recipe, List<Long> left, boolean simulate) {
        ExtendWirelessEnergyContainer container = getWirelessEnergyContainer();
        if (container == null) return left;
        long sum = left.stream().reduce(0L, Long::sum);
        if (sum <= 0) return null;
        if (io == IO.IN) {
            var canOutput = Math.min(sum, Math.min(container.getRate(), getEnergyStored()));
            if (!simulate) {
                if (canOutput < 0 && WirelessEnergyContainer.observed && getMachine() != null) {
                    WirelessEnergyContainer.TRANSFER_DATA.put(getMachine(), new BasicTransferData(getUUID(), canOutput, getMachine()));
                }
                setEnergyStored(getEnergyStored() - canOutput);
            }
            sum = sum - canOutput;
        } else if (io == IO.OUT) {
            long canInput = Math.min(sum, Math.min(container.getRate(), getEnergyCapacity() - getEnergyStored()));
            if (!simulate) {
                if (canInput > 0 && WirelessEnergyContainer.observed && getMachine() != null) {
                    long loss = (canInput / 1000) * container.getLoss();
                    WirelessEnergyContainer.TRANSFER_DATA.put(getMachine(), new ExtendTransferData(getUUID(), canInput - loss, loss, getMachine()));
                }
                setEnergyStored(getEnergyStored() + canInput);
            }
            sum = sum - canInput;
        }
        return sum <= 0 ? null : Collections.singletonList(sum);
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
                WirelessEnergyContainer.TRANSFER_DATA.put(getMachine(), new ExtendTransferData(getUUID(), change - loss, loss, getMachine()));
            }
        } else {
            change = Math.min(-energyToAdd, Math.min(oldEnergyStored, container.getRate()));
            change = -change;
            if (change < 0 && WirelessEnergyContainer.observed && getMachine() != null) {
                WirelessEnergyContainer.TRANSFER_DATA.put(getMachine(), new BasicTransferData(getUUID(), change, getMachine()));
            }
        }
        setEnergyStored(oldEnergyStored + change);
        return change;
    }

    @Override
    public void updateTick() {
        super.updateTick();
        if (!getMachine().isRemote() && getMachine().getOffsetTimer() % 20 == 0) {
            ExtendWirelessEnergyContainer container = getWirelessEnergyContainer();
            if (container != null) {
                long energyStored = getEnergyStored();
                if (handlerIO == IO.IN) {
                    long canInput = getEnergyCapacity() - energyStored;
                    if (canInput > 0) {
                        long change = container.unrestrictedRemoveEnergy(canInput);
                        if (change > 0) {
                            setEnergyStored(energyStored + change);
                        }
                    }
                } else {
                    if (energyStored > 0) {
                        long change = container.unrestrictedAddEnergy(energyStored);
                        if (change > 0) {
                            setEnergyStored(energyStored - change);
                        }
                    }
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
    public @Nullable UUID getUUID() {
        return getMachine().getOwnerUUID();
    }
}
