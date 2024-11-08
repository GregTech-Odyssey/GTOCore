package com.gto.gtocore.api.capability;

public interface IManaContainer {

    long getManaStored();

    long getMaxCapacity();

    long getMaxConsumption();

    boolean addMana(long amount);

    boolean removeMana(long amount);

    default boolean isOneProbeHidden() {
        return false;
    }
}
