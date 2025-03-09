package com.gto.gtocore.api.machine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;

import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface INetMachineInteractor<T extends MetaMachine> {

    T getNetMachineCache();

    void setNetMachineCache(T cache);

    Set<T> getMachineNet();

    boolean testMachine(T machine);

    boolean firstTestMachine(T machine);

    default void removeNetMachineCache() {
        setNetMachineCache(null);
    }

    @Nullable
    default T getNetMachine() {
        if (getNetMachineCache() == null) {
            for (T machine : getMachineNet()) {
                if (firstTestMachine(machine)) {
                    setNetMachineCache(machine);
                    return machine;
                }
            }
        }
        T machine = getNetMachineCache();
        if (machine != null && testMachine(machine)) {
            return machine;
        } else {
            removeNetMachineCache();
        }
        return null;
    }
}
