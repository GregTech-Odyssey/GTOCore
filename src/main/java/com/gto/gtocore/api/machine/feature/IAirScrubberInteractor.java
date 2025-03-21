package com.gto.gtocore.api.machine.feature;

import com.gto.gtocore.utils.GTOUtils;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.common.machine.electric.AirScrubberMachine;

import net.minecraft.resources.ResourceLocation;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

import java.util.Map;
import java.util.Set;

public interface IAirScrubberInteractor {

    Map<ResourceLocation, Set<AirScrubberMachine>> NETWORK = new Object2ObjectOpenHashMap<>();

    AirScrubberMachine getAirScrubberMachineCache();

    void setAirScrubberMachineCache(AirScrubberMachine cache);

    default AirScrubberMachine getAirScrubberMachine() {
        if (getAirScrubberMachineCache() == null && this instanceof MetaMachine metaMachine && metaMachine.getLevel() != null) {
            for (AirScrubberMachine machine : NETWORK.getOrDefault(metaMachine.getLevel().dimension().location(), Set.of())) {
                if (machine.getRecipeLogic().isWorking() && machine.getLevel() != null && GTOUtils.calculateDistance(machine.getPos(), metaMachine.getPos()) < (1 << machine.getTier()) << 3) {
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
