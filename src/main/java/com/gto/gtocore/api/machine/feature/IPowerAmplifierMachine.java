package com.gto.gtocore.api.machine.feature;

import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;

public interface IPowerAmplifierMachine extends IRecipeLogicMachine {

    double gtocore$getPowerAmplifier();

    void gtocore$setPowerAmplifier(double powerAmplifier);

    boolean gtocore$noPowerAmplifier();

    void gtocore$setHasPowerAmplifier(boolean hasPowerAmplifier);
}
