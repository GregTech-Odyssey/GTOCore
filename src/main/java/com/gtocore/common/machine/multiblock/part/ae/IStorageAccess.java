package com.gtocore.common.machine.multiblock.part.ae;

import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;
import com.gregtechceu.gtceu.integration.ae2.machine.feature.IGridConnectedMachine;

import appeng.api.storage.IStorageProvider;
import appeng.api.storage.MEStorage;

import java.util.UUID;

public interface IStorageAccess extends IMachineLife, MEStorage, IGridConnectedMachine, IStorageProvider {

    void setCapacity(double capacity);

    void setInfinite(boolean isInfinite);

    void setUUID(UUID uuid);

    void setCheck(boolean check);

    void setObserve(boolean observe);

    boolean isInfinite();

    double getCapacity();

    int getTypes();

    double getBytes();
}
