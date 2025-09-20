package com.gtocore.common.machine.multiblock.noenergy;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gtolib.api.capability.IExtendWirelessEnergyContainerHolder;
import com.gtolib.api.machine.multiblock.NoRecipeLogicMultiblockMachine;
import com.hepdd.gtmthings.api.misc.WirelessEnergyContainer;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MEEnergySubstationMachine extends NoRecipeLogicMultiblockMachine implements  IExtendWirelessEnergyContainerHolder {
    private WirelessEnergyContainer wirelessEnergyContainerCache;

    public MEEnergySubstationMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void setWirelessEnergyContainerCache(WirelessEnergyContainer wirelessEnergyContainer) {
        this.wirelessEnergyContainerCache=wirelessEnergyContainer;
    }

    @Override
    public WirelessEnergyContainer getWirelessEnergyContainerCache() {
        return this.wirelessEnergyContainerCache;
    }

    @Override
    @Nullable
    public UUID getUUID() {
        return getOwnerUUID();
    }
}
