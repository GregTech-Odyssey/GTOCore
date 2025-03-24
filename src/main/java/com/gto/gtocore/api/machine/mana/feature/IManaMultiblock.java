package com.gto.gtocore.api.machine.mana.feature;

import com.gto.gtocore.api.capability.IManaContainer;

import java.util.Set;

public interface IManaMultiblock {

    Set<IManaContainer> getManaContainer();

    boolean isGeneratorMana();

    default long addMana(long amount, int limit) {
        long change = 0;
        for (IManaContainer container : getManaContainer()) {
            if (amount <= 0) return change;
            long mana = container.addMana(amount, limit);
            change += mana;
            amount -= mana;
        }
        return change;
    }

    default long removeMana(long amount, int limit) {
        long change = 0;
        for (IManaContainer container : getManaContainer()) {
            if (amount <= 0) return change;
            long mana = container.removeMana(amount, limit);
            change += mana;
            amount -= mana;
        }
        return change;
    }
}
