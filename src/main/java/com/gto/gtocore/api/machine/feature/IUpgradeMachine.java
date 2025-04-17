package com.gto.gtocore.api.machine.feature;

public interface IUpgradeMachine {

    default void gtocore$setSpeed(double speed) {}

    default void gtocore$setEnergy(double energy) {}

    default double gtocore$getSpeed() {
        return 1;
    }

    default double gtocore$getEnergy() {
        return 1;
    }

    default boolean gtocore$canUpgraded() {
        return true;
    }
}
