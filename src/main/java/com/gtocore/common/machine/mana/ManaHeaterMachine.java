package com.gtocore.common.machine.mana;

import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.machine.feature.IHeaterMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.gto.datasynclib.annotations.SyncToClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManaHeaterMachine extends SimpleManaMachine implements IHeaterMachine, ICustomRecipeLogicHolder {

    private static final Fluid SALAMANDER = GTOMaterials.Salamander.getFluid(FluidStorageKeys.GAS);

    @SaveToDisk
    @SyncToClient(notifyUpdate = true)
    private int temperature = 293;

    /// an indicator used to determine if the salamander input is present
    /// **used by client renderer**
    @SaveToDisk
    @SyncToClient(notifyUpdate = true)
    private boolean salamanderInput = false;
    private TickableSubscription tickSubs;

    public ManaHeaterMachine(MetaMachineBlockEntity holder) {
        super(holder, 2, t -> 8000);
    }

    @Override
    @NotNull
    public GTRecipeType getRecipeType() {
        return GTORecipeTypes.MANA_HEATER_RECIPES;
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
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            tickSubs = subscribeServerTick(tickSubs, () -> {
                tickUpdate();
                if (temperature > getMaxTemperature()) getRecipeLogic().markLastRecipeDirty();
                getRecipeLogic().updateTickSubscription();
            }, 20);
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
    public void onWorking() {
        super.onWorking();
        if (getOffsetTimer() % 10 == 0 && getMaxTemperature() > temperature + 10) {
            var hasSalamander = inputFluid(SALAMANDER, 10);
            this.salamanderInput = hasSalamander;
            raiseTemperature(hasSalamander ? 10 : 2);
        }
    }

    @Override
    public int getHeatCapacity() {
        return 8;
    }

    @Override
    public int getMaxTemperature() {
        return 2400;
    }

    @Override
    public void setTemperature(final int temperature) {
        this.temperature = temperature;
    }

    @Override
    public int getTemperature() {
        return this.temperature;
    }

    public boolean hasSalamanderInput() {
        return salamanderInput;
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        if (temperature >= getMaxTemperature()) return null;
        return getRecipeBuilder().duration(20).MANAt(16).build();
    }
}
