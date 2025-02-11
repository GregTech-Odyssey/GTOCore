package com.gto.gtocore.api.machine.feature;

import com.gto.gtocore.api.misc.Drone;
import com.gto.gtocore.common.machine.multiblock.noenergy.DroneControlCenterMachine;
import com.gto.gtocore.utils.GTOUtils;

import com.gregtechceu.gtceu.api.machine.MetaMachine;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface IDroneInteractionMachine {

    default MetaMachine getMachine() {
        return (MetaMachine) this;
    }

    DroneControlCenterMachine getDroneControlCenterMachineCache();

    void setDroneControlCenterMachineCache(DroneControlCenterMachine cache);

    @Nullable
    default DroneControlCenterMachine getDroneControlCenterMachine() {
        if (getDroneControlCenterMachineCache() == null) {
            for (DroneControlCenterMachine centerMachine : DroneControlCenterMachine.DRONE_NETWORK) {
                if (centerMachine.isFormed() && centerMachine.getRecipeLogic().isWorking() && Objects.requireNonNull(centerMachine.getLevel()).dimension().equals(Objects.requireNonNull(getMachine().getLevel()).dimension()) && GTOUtils.calculateDistance(centerMachine.getPos(), getMachine().getPos()) < 256) {
                    setDroneControlCenterMachineCache(centerMachine);
                    return centerMachine;
                }
            }
        }
        DroneControlCenterMachine centerMachine = getDroneControlCenterMachineCache();
        if (centerMachine != null) {
            if (centerMachine.isFormed() && centerMachine.getRecipeLogic().isWorking()) {
                return centerMachine;
            } else {
                removeDroneControlCenterMachineCache();
            }
        }
        return null;
    }

    default void removeDroneControlCenterMachineCache() {
        DroneControlCenterMachine centerMachine = getDroneControlCenterMachineCache();
        if (centerMachine != null) {
            setDroneControlCenterMachineCache(null);
        }
    }

    @Nullable
    default Drone getFirstUsableDrone() {
        DroneControlCenterMachine centerMachine = getDroneControlCenterMachine();
        if (centerMachine != null) {
            return centerMachine.getFirstUsableDrone(getMachine().getPos());
        }
        return null;
    }
}
