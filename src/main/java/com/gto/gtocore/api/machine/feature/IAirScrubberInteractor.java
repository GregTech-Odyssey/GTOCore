package com.gto.gtocore.api.machine.feature;

import com.gto.gtocore.utils.GTOUtils;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.common.machine.electric.AirScrubberMachine;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Set;

public interface IAirScrubberInteractor {

    Set<AirScrubberMachine> NETWORK = new ObjectOpenHashSet<>();

    AirScrubberMachine getAirScrubberMachineCache();

    void setAirScrubberMachineCache(AirScrubberMachine cache);

    default AirScrubberMachine getAirScrubberMachine() {
        if (getAirScrubberMachineCache() == null && this instanceof MetaMachine metaMachine && metaMachine.getLevel() != null) {
            for (AirScrubberMachine machine : NETWORK) {
                if (machine.getRecipeLogic().isWorking() && machine.getLevel() != null && metaMachine.getLevel().dimension().location().equals(machine.getLevel().dimension().location()) && GTOUtils.calculateDistance(machine.getPos(), metaMachine.getPos()) < 64) {
                    setAirScrubberMachineCache(machine);
                    return machine;
                }
            }
        }
        AirScrubberMachine machine = getAirScrubberMachineCache();
        if (machine != null && machine.getRecipeLogic().isWorking()) {
            return machine;
        } else {
            setAirScrubberMachineCache(null);
        }
        return null;
    }
}
