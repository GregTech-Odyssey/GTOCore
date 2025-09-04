package com.gtocore.api.machine.part;

import com.gtolib.api.machine.feature.IReceiveHeatMachine;

public interface ITempPartMachine extends IReceiveHeatMachine {

    @Override
    default int getHeatCapacity() {
        return 24;
    }

    @Override
    default int getMaxTemperature() {
        return Integer.MAX_VALUE;
    }
}
