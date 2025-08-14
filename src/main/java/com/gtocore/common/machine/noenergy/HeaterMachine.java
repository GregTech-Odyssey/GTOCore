package com.gtocore.common.machine.noenergy;

import com.gtolib.api.machine.SimpleNoEnergyMachine;
import com.gtolib.api.machine.feature.IHeaterMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class HeaterMachine extends SimpleNoEnergyMachine implements IHeaterMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(HeaterMachine.class, SimpleNoEnergyMachine.MANAGED_FIELD_HOLDER);
    public static final int MaxTemperature = 800;

    @Persisted
    @DescSynced
    @RequireRerender
    private int temperature = 293;
    private TickableSubscription tickSubs;

    public HeaterMachine(MetaMachineBlockEntity holder) {
        super(holder, 0, i -> 8000);
    }

    @Override
    public int getOutputSignal(@Nullable Direction side) {
        return getSignal(side);
    }

    @Override
    public boolean canConnectRedstone(@NotNull Direction side) {
        return true;
    }

    @Override
    @NotNull
    public GTRecipeType getRecipeType() {
        return GTRecipeTypes.STEAM_BOILER_RECIPES;
    }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {}

    private void setEnabled(boolean isWorkingAllowed) {
        if (!isWorkingAllowed && getRecipeLogic().isWorking()) getRecipeLogic().interruptRecipe();
        super.setWorkingEnabled(isWorkingAllowed);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            tickSubs = subscribeServerTick(tickSubs, () -> {
                if (self().getOffsetTimer() % 20 != 0) return;
                Level level = getLevel();
                if (level == null) return;
                tickUpdate();
                this.requestSync();
                setEnabled(level.getBlockState(getPos().relative(getFrontFacing())).isAir());
            });
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
    public boolean onWorking() {
        if (super.onWorking()) {
            if (getOffsetTimer() % 10 == 0) raiseTemperature(1);
            return true;
        }
        return false;
    }

    @Override
    public int getHeatCapacity() {
        return 4;
    }

    @Override
    public int getMaxTemperature() {
        return MaxTemperature;
    }

    @Override
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public void setTemperature(final int temperature) {
        this.temperature = temperature;
    }

    @Override
    public int getTemperature() {
        return this.temperature;
    }
}
