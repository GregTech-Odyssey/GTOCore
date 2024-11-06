package com.gto.gtocore.common.machine.steam;

import com.gto.gtocore.api.machine.IVacuumMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.steam.SimpleSteamMachine;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class SteamVacuumPumpMachine extends SimpleSteamMachine implements IVacuumMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            SteamVacuumPumpMachine.class, SimpleSteamMachine.MANAGED_FIELD_HOLDER);

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private int vacuumTier;

    protected TickableSubscription tickSubs;

    public SteamVacuumPumpMachine(IMachineBlockEntity holder, boolean isHighPressure, Object... args) {
        super(holder, isHighPressure, args);
        this.tickSubs = subscribeServerTick(tickSubs, this::tick);
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
}
