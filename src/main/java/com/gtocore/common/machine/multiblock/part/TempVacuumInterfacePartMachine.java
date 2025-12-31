package com.gtocore.common.machine.multiblock.part;

import com.gtocore.api.machine.ITempPartMachine;
import com.gtocore.api.machine.IVacuumPartMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.multiblock.part.MultiblockPartMachine;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

public class TempVacuumInterfacePartMachine extends MultiblockPartMachine implements ITempPartMachine, IVacuumPartMachine {

    @Persisted
    private int temperature = 293;

    private TickableSubscription tickableSubscription;

    public TempVacuumInterfacePartMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        tickableSubscription = subscribeServerTick(tickableSubscription, this::tickUpdate, 20);
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (tickableSubscription != null) {
            tickableSubscription.unsubscribe();
            tickableSubscription = null;
        }
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    @Override
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
