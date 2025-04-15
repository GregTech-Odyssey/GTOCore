package com.gto.gtocore.api.capability;

import com.gto.gtocore.common.machine.mana.multiblock.ManaDistributorMachine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;

public final class ManaContainerList implements IManaContainer {

    public static final ManaContainerList EMPTY = new ManaContainerList();

    private final IManaContainer[] containers;

    public ManaContainerList(IManaContainer... containers) {
        this.containers = containers;
    }

    @Override
    public boolean acceptDistributor() {
        return false;
    }

    @Override
    public MetaMachine getMachine() {
        return null;
    }

    @Override
    public long getMaxMana() {
        long maxMana = 0;
        for (IManaContainer container : containers) {
            maxMana += container.getMaxMana();
        }
        return maxMana;
    }

    @Override
    public long getCurrentMana() {
        long currentMana = 0;
        for (IManaContainer container : containers) {
            currentMana += container.getCurrentMana();
        }
        return currentMana;
    }

    @Override
    public long getMaxProductionRate() {
        long getMaxProductionRate = 0;
        for (IManaContainer container : containers) {
            getMaxProductionRate += container.getMaxProductionRate();
        }
        return getMaxProductionRate;
    }

    @Override
    public long getMaxConsumptionRate() {
        long getMaxProductionRate = 0;
        for (IManaContainer container : containers) {
            getMaxProductionRate += container.getMaxProductionRate();
        }
        return getMaxProductionRate;
    }

    @Override
    public void setCurrentMana(long mana) {}

    @Override
    public ManaDistributorMachine getNetMachineCache() {
        return null;
    }

    @Override
    public void setNetMachineCache(ManaDistributorMachine cache) {}

    @Override
    public long addMana(long amount, int rateMultiplier, boolean simulate) {
        long change = 0;
        for (IManaContainer container : containers) {
            if (amount <= 0) return change;
            long mana = container.addMana(amount, rateMultiplier, simulate);
            change += mana;
            amount -= mana;
        }
        return change;
    }

    @Override
    public long removeMana(long amount, int rateMultiplier, boolean simulate) {
        long change = 0;
        for (IManaContainer container : containers) {
            if (amount <= 0) return change;
            long mana = container.removeMana(amount, rateMultiplier, simulate);
            change += mana;
            amount -= mana;
        }
        return change;
    }
}
