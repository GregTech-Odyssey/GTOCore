package com.gtocore.common.machine.multiblock;

import com.gtolib.api.GTOValues;
import com.gtolib.api.capability.IExtendWirelessEnergyContainerHolder;
import com.gtolib.api.machine.feature.multiblock.ITierCasingMachine;

import net.minecraft.world.level.Level;

public interface IWirelessDimensionProvider extends IExtendWirelessEnergyContainerHolder, ITierCasingMachine {

    default void loadContainer() {
        if (isRemote()) return;
        Level level = getLevel();
        if (level == null) return;
        var container = getWirelessEnergyContainer();
        if (container == null) return;
        container.getDimension().put(level.dimension().location(), getCasingTier(GTOValues.INTEGRAL_FRAMEWORK_TIER));
    }

    boolean isRemote();

    Level getLevel();

    default void unloadContainer() {
        if (isRemote()) return;
        Level level = getLevel();
        if (level == null) return;
        var container = getWirelessEnergyContainer();
        if (container == null) return;
        container.getDimension().put(level.dimension().location(), 0);
    }
}
