package com.gtocore.common.machine.electric;

import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.machine.feature.IHeaterMachine;
import com.gtolib.api.machine.trait.NotifiableSafeEnergyContainer;
import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.transfer.fluid.ICustomFluidStackHandler;
import com.gregtechceu.gtceu.api.transfer.item.ICustomItemStackHandler;

import net.minecraft.core.Direction;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.gto.datasynclib.annotations.SyncToClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ElectricHeaterMachine extends WorkableTieredMachine implements IHeaterMachine, ICustomRecipeLogicHolder {

    public static final int MaxTemperature = 1200;
    @SaveToDisk
    @SyncToClient(notifyUpdate = true)
    private int temperature = 273;
    private TickableSubscription tickSubs;

    public ElectricHeaterMachine(MetaMachineBlockEntity holder) {
        super(holder, 1, t -> 8000);
    }

    @Override
    protected @NotNull NotifiableEnergyContainer createEnergyContainer(Object @NotNull... args) {
        long tierVoltage = GTValues.V[tier];
        return new NotifiableSafeEnergyContainer(this, tierVoltage << 6, tierVoltage, getMaxInputOutputAmperage());
    }

    @Override
    protected @NotNull NotifiableFluidTank createImportFluidHandler(Object @NotNull... args) {
        return new NotifiableFluidTank(this, 0, 0, IO.IN);
    }

    @Override
    @Nullable
    public ICustomItemStackHandler getItemHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        return null;
    }

    @Override
    @Nullable
    public ICustomFluidStackHandler getFluidHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        return null;
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
                if (temperature > MaxTemperature) getRecipeLogic().markLastRecipeDirty();
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
        if (getOffsetTimer() % 10 == 0 && MaxTemperature > temperature + 4) {
            raiseTemperature(4);
        }
    }

    @Override
    public int getHeatCapacity() {
        return 6;
    }

    @Override
    public int getMaxTemperature() {
        return MaxTemperature;
    }

    @Override
    public void setTemperature(final int temperature) {
        this.temperature = temperature;
    }

    @Override
    public int getTemperature() {
        return this.temperature;
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        if (temperature >= MaxTemperature) return null;
        return RecipeBuilder.ofRaw().duration(20).EUt(30).build();
    }
}
