package com.gtocore.common.machine.multiblock.part;

import com.gtolib.api.capability.IExtendWirelessEnergyContainerHolder;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;

import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class WirelessEnergyInterfacePartMachine extends MultiblockPartMachine implements IExtendWirelessEnergyContainerHolder {

    private WirelessEnergyContainer WirelessEnergyContainerCache;

    public WirelessEnergyInterfacePartMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    @Nullable
    public UUID getUUID() {
        return getOwnerUUID();
    }

    @Override
    public void setWirelessEnergyContainerCache(final WirelessEnergyContainer WirelessEnergyContainerCache) {
        this.WirelessEnergyContainerCache = WirelessEnergyContainerCache;
    }

    @Override
    public WirelessEnergyContainer getWirelessEnergyContainerCache() {
        return this.WirelessEnergyContainerCache;
    }
}
