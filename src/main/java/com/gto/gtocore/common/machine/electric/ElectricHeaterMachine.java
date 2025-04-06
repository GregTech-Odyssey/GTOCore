package com.gto.gtocore.common.machine.electric;

import com.gto.gtocore.api.machine.feature.IHeaterMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunnerHelper;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import net.minecraft.core.Direction;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ElectricHeaterMachine extends WorkableTieredMachine implements IHeaterMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ElectricHeaterMachine.class, WorkableTieredMachine.MANAGED_FIELD_HOLDER);

    @Getter
    @Setter
    @Persisted
    private int temperature = 293;

    private TickableSubscription tickSubs;

    public ElectricHeaterMachine(IMachineBlockEntity holder) {
        super(holder, 1, t -> 8000);
    }

    @Nullable
    private GTRecipe getRecipe() {
        if (temperature >= getMaxTemperature()) return null;
        GTRecipe recipe = GTORecipeBuilder.ofRaw().duration(20).EUt(30).buildRawRecipe();
        if (RecipeRunnerHelper.matchTickRecipe(this, recipe)) {
            return recipe;
        }
        return null;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic(Object @NotNull... args) {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    @Override
    public @NotNull GTRecipeType getRecipeType() {
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
            if (getOffsetTimer() % 10 == 0 && getMaxTemperature() > temperature + 4) {
                raiseTemperature(4);
            }
            return true;
        }
        return false;
    }

    @Override
    public int getHeatCapacity() {
        return 6;
    }

    @Override
    public int getMaxTemperature() {
        return 1200;
    }

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
