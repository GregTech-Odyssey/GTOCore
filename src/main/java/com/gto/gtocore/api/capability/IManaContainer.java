package com.gto.gtocore.api.capability;

import com.gto.gtocore.common.machine.mana.multiblock.ManaDistributorMachine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.core.BlockPos;

import javax.annotation.Nullable;

public interface IManaContainer {

    void setDistributorCache(ManaDistributorMachine container);

    ManaDistributorMachine getDistributorCache();

    @Nullable
    default ManaDistributorMachine getDistributor() {
        if (acceptDistributor() && getDistributorCache() == null && !ManaDistributorMachine.DISTRIBUTOR_NETWORK.isEmpty()) {
            BlockPos pos = getMachine().getPos();
            for (ManaDistributorMachine distributor : ManaDistributorMachine.DISTRIBUTOR_NETWORK) {
                if (distributor.add(pos)) {
                    setDistributorCache(distributor);
                    break;
                }
            }
        }
        ManaDistributorMachine distributor = getDistributorCache();
        if (distributor != null) {
            if (distributor.isFormed()) {
                return distributor;
            } else {
                distributor.remove();
                setDistributorCache(null);
            }
        }
        return null;
    }

    boolean acceptDistributor();

    MetaMachine getMachine();

    int getMaxMana();

    int getCurrentMana();

    void setCurrentMana(int mana);

    default int getMaxConsumption() {
        return getMaxMana();
    }

    default int addMana(int amount) {
        int change = Math.min(getMaxMana() - getCurrentMana(), Math.min(getMaxConsumption(), amount));
        if (change <= 0) return 0;
        setCurrentMana(getCurrentMana() + change);
        return change;
    }

    default int removeMana(int amount) {
        int change = Math.min(getCurrentMana(), Math.min(getMaxConsumption(), amount));
        if (change <= 0) return 0;
        setCurrentMana(getCurrentMana() - change);
        return change;
    }
}
