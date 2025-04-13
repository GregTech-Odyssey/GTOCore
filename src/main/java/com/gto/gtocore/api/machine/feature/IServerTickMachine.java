package com.gto.gtocore.api.machine.feature;

public interface IServerTickMachine {

    default void runTick() {}

    default boolean keepTick() {
        return false;
    }

    default boolean cancelTick() {
        return false;
    }
}
