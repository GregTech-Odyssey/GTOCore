package com.gto.gtocore.api.machine.mana.feature;

import com.gto.gtocore.api.capability.IManaContainer;

import java.util.Set;

public interface IManaMultiblock {

    Set<IManaContainer> getManaContainer();

    boolean isGeneratorMana();

    default int addMana(int amount, int limit) {
        int change = 0;
        for (IManaContainer container : getManaContainer()) {
            if (amount <= 0) return change;
            int mana = container.addMana(amount, limit);
            change += mana;
            amount -= mana;
        }
        return change;
    }

    default int removeMana(int amount, int limit) {
        int change = 0;
        for (IManaContainer container : getManaContainer()) {
            if (amount <= 0) return change;
            int mana = container.removeMana(amount, limit);
            change += mana;
            amount -= mana;
        }
        return change;
    }
}
