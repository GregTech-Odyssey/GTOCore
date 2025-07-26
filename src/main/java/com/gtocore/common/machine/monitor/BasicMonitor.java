package com.gtocore.common.machine.monitor;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.IMachineLife;

import com.hepdd.gtmthings.api.capability.IBindable;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class BasicMonitor extends TieredMachine implements IBindable, IMachineLife, IMonitor {

    public BasicMonitor(IMachineBlockEntity holder) {
        super(holder, GTValues.IV);
    }

    public BasicMonitor(Object o) {
        this((IMachineBlockEntity) o);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (getLevel() != null && !getLevel().isClientSide) {
            Manager.addBlock(this);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (getLevel() != null && !getLevel().isClientSide) {
            Manager.removeBlock(this);
        }
    }

    @Override
    public void clientTick() {
        super.clientTick();
        if (getOffsetTimer() % 10 == 0) {
            Manager.updateAllNetworkDisplayMachines(getLevel());
        }
    }

    @Override
    public @Nullable UUID getUUID() {
        return getOwnerUUID();
    }

    @Override
    public void onPaintingColorChanged(int color) {
        super.onPaintingColorChanged(color);
        if (getLevel() != null && !getLevel().isClientSide) {
            Manager.removeBlock(getBlockState(), getPos(), getLevel());
            Manager.addBlock(getBlockState(), getPos(), getLevel(), color);
        }
    }
}
