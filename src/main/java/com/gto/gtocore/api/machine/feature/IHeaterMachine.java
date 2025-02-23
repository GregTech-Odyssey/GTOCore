package com.gto.gtocore.api.machine.feature;

import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;

public interface IHeaterMachine extends IExplosionMachine {

    int getHeatCapacity();

    int getMaxTemperature();

    int getTemperature();

    void setTemperature(int temperature);

    default boolean reduceTemperature(int value) {
        if (getTemperature() - 293 < value) {
            setTemperature(293);
            return false;
        } else {
            setTemperature(getTemperature() - value);
        }
        return true;
    }

    default boolean raiseTemperature(int value) {
        int newTemperature = getTemperature() + value;
        if (newTemperature < getMaxTemperature()) {
            setTemperature(newTemperature);
            return true;
        } else {
            doExplosion(5);
            return false;
        }
    }
}
