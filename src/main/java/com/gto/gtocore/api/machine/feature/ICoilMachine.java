package com.gto.gtocore.api.machine.feature;

import com.gregtechceu.gtceu.api.block.ICoilType;

public interface ICoilMachine {

    int gto$getTemperature();

    int getCoilTier();

    ICoilType getCoilType();
}
