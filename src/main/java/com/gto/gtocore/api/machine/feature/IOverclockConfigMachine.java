package com.gto.gtocore.api.machine.feature;

public interface IOverclockConfigMachine {

    default int gTOCore$getOCLimit() {
        return 20;
    }

    default void gTOCore$setOCLimit(int number) {}
}
