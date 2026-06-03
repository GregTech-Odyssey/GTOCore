package com.gtocore.common.machine.noenergy;

import com.gtolib.api.machine.SimpleNoEnergyMachine;
import com.gtolib.api.machine.feature.IReceiveHeatMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluids;

import com.gto.datasynclib.annotations.SaveToDisk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BoilWaterMachine extends SimpleNoEnergyMachine implements IReceiveHeatMachine, ICustomRecipeLogicHolder {

    public static final int DrawWaterExplosionLine = 400;
    @SaveToDisk
    private int temperature = 293;
    private TickableSubscription tickSubs;

    public BoilWaterMachine(MetaMachineBlockEntity holder) {
        super(holder, 0, i -> 16000);
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
        return GTRecipeTypes.STEAM_TURBINE_FUELS;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            tickSubs = subscribeServerTick(tickSubs, this::tickUpdate, 20);
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
    public boolean handleTickRecipe(GTRecipe recipe) {
        if (super.handleTickRecipe(recipe)) {
            if (getOffsetTimer() % 15 == 0) return reduceTemperature(1) == 1;
            return true;
        }
        return false;
    }

    @Override
    public int getHeatCapacity() {
        return 12;
    }

    @Override
    public int getMaxTemperature() {
        return 600;
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
        if (temperature < 360) return null;
        return getRecipeBuilder().duration(20).inputFluids(Fluids.WATER, 6).outputFluids(GTMaterials.Steam, 960 * temperature / 600).build();
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
