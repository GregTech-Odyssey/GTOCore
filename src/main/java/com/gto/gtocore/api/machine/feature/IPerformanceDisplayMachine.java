package com.gto.gtocore.api.machine.feature;

public interface IPerformanceDisplayMachine extends IServerTickMachine {

    int gtocore$getTickTime();

    void gtocore$observe();
}
