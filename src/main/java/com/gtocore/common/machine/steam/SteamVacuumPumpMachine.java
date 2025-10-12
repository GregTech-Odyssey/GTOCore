package com.gtocore.common.machine.steam;

import com.gtolib.api.machine.feature.IVacuumMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.steam.SimpleSteamMachine;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

public final class SteamVacuumPumpMachine extends SimpleSteamMachine implements IVacuumMachine {

    @Persisted
    private int vacuumTier;
    private TickableSubscription tickSubs;

    public SteamVacuumPumpMachine(MetaMachineBlockEntity holder, boolean isHighPressure, Object... args) {
        super(holder, isHighPressure, args);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            tickSubs = subscribeServerTick(tickSubs, this::tick);
        }
    }

    @Override
    public void onUnload() {
        super.onUnload();
        if (tickSubs != null) {
            tickSubs.unsubscribe();
            tickSubs = null;
        }
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        update();
    }

    private void tick() {
        if (getOffsetTimer() % 20 != 0) return;
        if (isHighPressure() && getRecipeLogic().getTotalContinuousRunningTime() > 1200) {
            vacuumTier = 2;
        } else if (getRecipeLogic().getTotalContinuousRunningTime() > (isHighPressure() ? 600 : 1200)) {
            vacuumTier = 1;
        } else {
            vacuumTier = 0;
        }
    }

    @Override
    public int getVacuumTier() {
        return this.vacuumTier;
    }

    public TickableSubscription getTickSubs() {
        return this.tickSubs;
    }
}
