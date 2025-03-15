package com.gto.gtocore.api.machine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public interface INetMachineInteractor<T extends MetaMachine> {

    @Nullable
    Level getLevel();

    T getNetMachineCache();

    void setNetMachineCache(T cache);

    Map<ResourceLocation, Set<T>> getMachineNet();

    boolean testMachine(T machine);

    boolean firstTestMachine(T machine);

    default void removeNetMachineCache() {
        setNetMachineCache(null);
    }

    @Nullable
    default T getNetMachine() {
        if (getNetMachineCache() == null) {
            Level level = getLevel();
            if (level != null) {
                Set<T> set = getMachineNet().get(level.dimension().location());
                if (set != null) {
                    for (T machine : set) {
                        if (firstTestMachine(machine)) {
                            setNetMachineCache(machine);
                            return machine;
                        }
                    }
                }
            }
        }
        T machine = getNetMachineCache();
        if (machine != null) {
            if (testMachine(machine)) {
                return machine;
            } else {
                removeNetMachineCache();
            }
        }
        return null;
    }

    static <T extends MetaMachine> void addToNet(Map<ResourceLocation, Set<T>> map, T machine) {
        if (machine.isRemote()) return;
        Level level = machine.getLevel();
        if (level == null) return;
        map.computeIfAbsent(level.dimension().location(), k -> new ObjectOpenHashSet<>()).add(machine);
    }

    static <T extends MetaMachine> void removeFromNet(Map<ResourceLocation, Set<T>> map, T machine) {
        if (machine.isRemote()) return;
        Level level = machine.getLevel();
        if (level == null) return;
        map.computeIfAbsent(level.dimension().location(), k -> new ObjectOpenHashSet<>()).remove(machine);
    }
}
