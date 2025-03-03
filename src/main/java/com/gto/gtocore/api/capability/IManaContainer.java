package com.gto.gtocore.api.capability;

import com.gto.gtocore.api.machine.INetMachineInteractor;
import com.gto.gtocore.common.machine.mana.multiblock.ManaDistributorMachine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Set;

public interface IManaContainer extends INetMachineInteractor<ManaDistributorMachine> {

    @Override
    default Set<ManaDistributorMachine> getMachineNet() {
        return ManaDistributorMachine.DISTRIBUTOR_NETWORK;
    }

    @Override
    default boolean firstTestMachine(ManaDistributorMachine machine) {
        if (!acceptDistributor()) return false;
        BlockPos pos = getMachine().getPos();
        Level level = machine.getLevel();
        if (level == null) return false;
        return machine.isFormed() && level.dimension().equals(level.dimension()) && machine.add(pos);
    }

    @Override
    default boolean testMachine(ManaDistributorMachine machine) {
        return machine.isFormed();
    }

    @Override
    default void removeNetMachineCache() {
        ManaDistributorMachine distributor = getNetMachineCache();
        if (distributor != null) {
            distributor.remove();
            setNetMachineCache(null);
        }
    }

    boolean acceptDistributor();

    MetaMachine getMachine();

    int getMaxMana();

    int getCurrentMana();

    void setCurrentMana(int mana);

    int getMaxConsumption();

    default int addMana(int amount, int limit) {
        int change = Math.min(getMaxMana() - getCurrentMana(), Math.min(limit * getMaxConsumption(), amount));
        if (change <= 0) return 0;
        setCurrentMana(getCurrentMana() + change);
        return change;
    }

    default int removeMana(int amount, int limit) {
        int change = Math.min(getCurrentMana(), Math.min(limit * getMaxConsumption(), amount));
        if (change <= 0) return 0;
        setCurrentMana(getCurrentMana() - change);
        return change;
    }
}
