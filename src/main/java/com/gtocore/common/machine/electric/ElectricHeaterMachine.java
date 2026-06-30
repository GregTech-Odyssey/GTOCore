package com.gtocore.common.machine.electric;

import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.capability.IHeatContainer;
import com.gtolib.api.machine.heat.HeatHandler;
import com.gtolib.api.machine.heat.feature.IHeatContainerMachine;
import com.gtolib.api.machine.trait.NotifiableSafeEnergyContainer;
import com.gtolib.api.recipe.RecipeBuilder;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.GTCapability;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
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
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ElectricHeaterMachine extends WorkableTieredMachine implements IHeatContainerMachine, ICustomRecipeLogicHolder {

    public static final int MaxTemperature = 1200;

    @Getter
    @SaveToDisk
    @SyncToClient
    private final HeatHandler heatContainer;

    public ElectricHeaterMachine(MetaMachineBlockEntity holder) {
        super(holder, 1, t -> 8000);
        heatContainer = new HeatHandler(holder, MaxTemperature, 2, 0.4, 0.01);
        heatContainer.setSideIOCondition(s -> s == Direction.UP);
        heatContainer.addChangedListener(getRecipeLogic()::updateTickSubscription);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        heatContainer.onLoad();
    }

    @Override
    public void onUnload() {
        super.onUnload();
        heatContainer.onUnLoad();
    }

    @Override
    public @Nullable <T> Object getGTCapability(@NotNull Class<T> cap, @Nullable Direction side) {
        if (cap == IHeatContainer.class) {
            if (testHeatCapability(side)) return heatContainer;
            return GTCapability.EMPTY;
        }
        return super.getGTCapability(cap, side);
    }

    @Override
    protected @NotNull NotifiableEnergyContainer createEnergyContainer(Object @NotNull... args) {
        long tierVoltage = GTValues.V[tier];
        return new NotifiableSafeEnergyContainer(this, tierVoltage << 6, tierVoltage, getMaxInputOutputAmperage());
    }

    @Override
    protected @NotNull NotifiableFluidTank createImportFluidHandler(Object @NotNull... args) {
        return new NotifiableFluidTank(this, 0, 0, IO.NONE).setAvailable(false);
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
    public GTRecipe fullModifyRecipe(RecipeHandlerUnit unit, GTRecipeDefinition definition) {
        return definition.toRuntime();
    }

    @Override
    public void onWorking() {
        super.onWorking();
        if (getOffsetTimer() % 10 == 0) {
            if (heatContainer.currentHeat + 16 < heatContainer.maxHeat) {
                heatContainer.addHeatUnrestricted(16, false);
            } else {
                getRecipeLogic().markLastRecipeDirty();
            }
        }
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        if (heatContainer.currentHeat + 16 >= heatContainer.maxHeat) return null;
        return RecipeBuilder.ofRaw().duration(20).EUt(30).build();
    }
}
