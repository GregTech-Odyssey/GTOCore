package com.gto.gtocore.api.machine.mana.feature;

import com.gto.gtocore.api.capability.ManaContainerList;

import org.jetbrains.annotations.NotNull;

public interface IManaMultiblock {

    @NotNull
    ManaContainerList getManaContainer();

    boolean isGeneratorMana();

    default long removeMana(long amount, int limit, boolean simulate) {
        return getManaContainer().removeMana(amount, limit, simulate);
    }
}
