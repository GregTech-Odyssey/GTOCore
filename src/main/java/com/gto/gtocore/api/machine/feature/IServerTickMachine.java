package com.gto.gtocore.api.machine.feature;

public interface IServerTickMachine {

    default void gtocore$tick() {}

    default boolean gtocore$cancel() {
        return false;
    }
}
