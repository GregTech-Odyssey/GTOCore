package com.gto.gtocore.api.machine.feature;

import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;

import net.minecraft.core.Direction;

import org.jetbrains.annotations.Nullable;

public interface ITemperatureMachine extends IExplosionMachine {

    int getHeatCapacity();

    int getMaxTemperature();

    int getTemperature();

    void setTemperature(int temperature);

    void raiseTemperature(int value);

    int reduceTemperature(int value);

    default int getSignal(@Nullable Direction side) {
        if (side != null) {
            return (int) (15D * (getTemperature() - 293) / (getMaxTemperature() - 293));
        }
        return 0;
    }
}
