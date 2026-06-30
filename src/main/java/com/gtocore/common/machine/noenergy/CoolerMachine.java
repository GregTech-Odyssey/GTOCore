package com.gtocore.common.machine.noenergy;

import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.machine.SimpleNoEnergyMachine;
import com.gtolib.api.machine.heat.HeatHandler;
import com.gtolib.api.machine.heat.feature.IHeatContainerMachine;
import com.gtolib.api.recipe.IdleReason;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.ActionResult;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluids;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.gto.datasynclib.annotations.SyncToClient;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public final class CoolerMachine extends SimpleNoEnergyMachine implements IHeatContainerMachine, IExplosionMachine, ICustomRecipeLogicHolder {

    public static final int MaxTemperature = 750;

    @Getter
    @SaveToDisk
    @SyncToClient
    private final HeatHandler heatContainer;

    public CoolerMachine(MetaMachineBlockEntity holder) {
        super(holder, 0, i -> 8000);
        heatContainer = new HeatHandler(holder, MaxTemperature, 2, 0.2, 0.01);
        heatContainer.setSideIOCondition(s -> s != Direction.DOWN && s != Direction.UP);
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
    @NotNull
    public GTRecipeType getRecipeType() {
        return GTORecipeTypes.F1A1B;
    }

    @Override
    public GTRecipe fullModifyRecipe(RecipeHandlerUnit unit, GTRecipeDefinition definition) {
        return definition.toRuntime();
    }

    @Override
    public void onWorking() {
        super.onWorking();
        if (getOffsetTimer() % 20 == 0) {
            if (heatContainer.getCurrentHeat() < 8) {
                getRecipeLogic().markLastRecipeDirty();
            } else {
                heatContainer.removeHeatUnrestricted(8, false);
            }
        }
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        if (heatContainer.getCurrentHeat() < 8) {
            setIdleReason(IdleReason.INSUFFICIENT_TEMPERATURE);
            return null;
        }
        if (unit.getFluidAmount(true, Fluids.WATER)[0] < 1000) {
            setIdleReason(ActionResult.failInsufficientIn(Fluids.WATER.getFluidType().getDescription()));
            return null;
        }
        return getRecipeBuilder().duration(20).inputFluids(Fluids.WATER, 1000).outputFluids(Fluids.WATER, 990).build();
    }
}
