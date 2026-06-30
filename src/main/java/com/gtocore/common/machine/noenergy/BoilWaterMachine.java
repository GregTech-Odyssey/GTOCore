package com.gtocore.common.machine.noenergy;

import com.gtocore.common.data.GTORecipeTypes;
import com.gtocore.data.IdleReason;

import com.gtolib.api.machine.SimpleNoEnergyMachine;
import com.gtolib.api.machine.heat.HeatHandler;
import com.gtolib.api.machine.heat.feature.IHeatContainerMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluids;

import com.gto.datasynclib.annotations.SaveToDisk;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public final class BoilWaterMachine extends SimpleNoEnergyMachine implements IHeatContainerMachine, IExplosionMachine, ICustomRecipeLogicHolder {

    @Getter
    @SaveToDisk
    private final HeatHandler heatContainer;

    public BoilWaterMachine(MetaMachineBlockEntity holder) {
        super(holder, 0, i -> 16000);
        heatContainer = new HeatHandler(holder, 600, 2, 0.6, 0.01);
        heatContainer.setSideIOCondition(s -> s == Direction.DOWN);
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
    public boolean handleTickRecipe(GTRecipe recipe) {
        if (super.handleTickRecipe(recipe)) {
            if (getOffsetTimer() % 10 == 0) return heatContainer.removeHeatUnrestricted(1, false) == 1;
            return true;
        }
        return false;
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        if (heatContainer.getTemperature() < 360) {
            setIdleReason(IdleReason.INSUFFICIENT_TEMPERATURE);
            return null;
        }
        return getRecipeBuilder().duration(20).inputFluids(Fluids.WATER, 6).outputFluids(GTMaterials.Steam, (int) (960 * heatContainer.getTemperature() / 600)).build();
    }

    @Override
    public boolean matchRecipeOutput(GTRecipe recipe) {
        if (super.matchRecipeOutput(recipe)) return true;
        if (inputFluid(Fluids.WATER, 1)) doExplosion(4);
        return false;
    }

    @Override
    public boolean alwaysSearchRecipe() {
        return true;
    }
}
