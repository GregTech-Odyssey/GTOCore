package com.gto.gtocore.api.machine.trait;

import com.gto.gtocore.api.machine.INetMachineInteractor;
import com.gto.gtocore.common.machine.multiblock.electric.voidseries.DrillingControlCenterMachine;
import com.gto.gtocore.utils.GTOUtils;

import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.world.level.Level;

import java.util.Set;

public interface IFluidDrillLogic extends INetMachineInteractor<DrillingControlCenterMachine> {

    MetaMachine getMachine();

    @Override
    default Set<DrillingControlCenterMachine> getMachineNet() {
        return DrillingControlCenterMachine.DRILLING_NETWORK;
    }

    @Override
    default boolean firstTestMachine(DrillingControlCenterMachine machine) {
        Level level = machine.getLevel();
        if (level == null) return false;
        return machine.isFormed() && machine.getRecipeLogic().isWorking() && level.dimension().equals(level.dimension()) && GTOUtils.calculateDistance(machine.getPos(), getMachine().getPos()) < 16;
    }

    @Override
    default boolean testMachine(DrillingControlCenterMachine machine) {
        return machine.isFormed() && machine.getRecipeLogic().isWorking();
    }
}
