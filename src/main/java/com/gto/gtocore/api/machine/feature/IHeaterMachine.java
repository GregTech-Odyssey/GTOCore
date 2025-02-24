package com.gto.gtocore.api.machine.feature;

import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.IRedstoneSignalMachine;

public interface IHeaterMachine extends ITemperatureMachine {

    default void tickUpdate() {
        if (self().getOffsetTimer() % 20 != 0) return;
        if (this instanceof IRedstoneSignalMachine redstoneSignalMachine) redstoneSignalMachine.updateSignal();
        if (this instanceof IRecipeLogicMachine machine && machine.getRecipeLogic().isWorking()) return;
        reduceTemperature(1);
    }

    @Override
    default int reduceTemperature(int value) {
        int change = Math.min(getTemperature() - 293, value);
        if (change <= 0) {
            setTemperature(293);
            return 0;
        }
        setTemperature(getTemperature() - change);
        return change;
    }

    @Override
    default void raiseTemperature(int value) {
        int newTemperature = getTemperature() + value;
        if (newTemperature < getMaxTemperature()) {
            setTemperature(newTemperature);
        } else {
            doExplosion(5);
        }
    }
}
