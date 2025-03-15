package com.gto.gtocore.api.machine.feature;

import com.gto.gtocore.api.machine.INetMachineInteractor;
import com.gto.gtocore.api.misc.Drone;
import com.gto.gtocore.common.machine.multiblock.noenergy.DroneControlCenterMachine;
import com.gto.gtocore.utils.GTOUtils;

import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public interface IDroneInteractionMachine extends INetMachineInteractor<DroneControlCenterMachine> {

    default MetaMachine getMachine() {
        return (MetaMachine) this;
    }

    @Override
    default Map<ResourceLocation, Set<DroneControlCenterMachine>> getMachineNet() {
        return DroneControlCenterMachine.NETWORK;
    }

    @Override
    default boolean firstTestMachine(DroneControlCenterMachine machine) {
        Level level = machine.getLevel();
        if (level == null) return false;
        return machine.isFormed() && machine.getRecipeLogic().isWorking() && GTOUtils.calculateDistance(machine.getPos(), getMachine().getPos()) < 256;
    }

    @Override
    default boolean testMachine(DroneControlCenterMachine machine) {
        return machine.isFormed() && machine.getRecipeLogic().isWorking();
    }

    @Nullable
    default Drone getFirstUsableDrone() {
        DroneControlCenterMachine centerMachine = getNetMachine();
        if (centerMachine != null) {
            return centerMachine.getFirstUsableDrone(getMachine().getPos());
        }
        return null;
    }
}
