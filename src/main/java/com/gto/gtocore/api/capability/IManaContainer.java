package com.gto.gtocore.api.capability;

public interface IManaContainer {

    long getManaStored();

    long getMaxCapacity();

    boolean addMana(long amount);

    boolean removeMana(long amount);

    default boolean isOneProbeHidden() {
        return false;
    }
}
