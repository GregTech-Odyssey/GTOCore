package com.gto.gtocore.api.machine.feature.multiblock;

public interface IMEOutputMachine {

    default boolean gTOCore$isItemOutput() {
        return false;
    }

    default boolean gTOCore$isFluidOutput() {
        return false;
    }

    default boolean gTOCore$DualMEOutput() {
        return false;
    }
}
