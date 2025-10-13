package com.gtocore.common.machine.mana;

import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.machine.feature.IHeaterMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManaHeaterMachine extends SimpleManaMachine implements IHeaterMachine {

    private static final Fluid SALAMANDER = GTOMaterials.Salamander.getFluid(FluidStorageKeys.GAS);

    @Persisted
    @DescSynced
    @RequireRerender
    private int temperature = 293;

    /// an indicator used to determine if the salamander input is present
    /// **used by client renderer**
    @Persisted
    @DescSynced
    @RequireRerender
    private boolean salamanderInput = false;
    private TickableSubscription tickSubs;

    public ManaHeaterMachine(MetaMachineBlockEntity holder) {
        super(holder, 2, t -> 8000);
    }

    @Nullable
    private Recipe getRecipe() {
        if (temperature >= getMaxTemperature()) return null;
        Recipe recipe = getRecipeBuilder().duration(20).MANAt(16).buildRawRecipe();
        if (RecipeRunner.matchTickRecipe(this, recipe)) {
            return recipe;
        }
        return null;
    }

    @Override
    @NotNull
    public RecipeLogic createRecipeLogic() {
        return new CustomRecipeLogic(this, this::getRecipe);
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
                if (self().getOffsetTimer() % 20 != 0) return;
                tickUpdate();
                getRecipeLogic().updateTickSubscription();
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
            if (getOffsetTimer() % 10 == 0 && getMaxTemperature() > temperature + 10) {
                var hasSalamander = inputFluid(SALAMANDER, 10);
                this.salamanderInput = hasSalamander;
                raiseTemperature(hasSalamander ? 10 : 2);
            }
            return true;
        }
        this.salamanderInput = false;
        return false;
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
}
