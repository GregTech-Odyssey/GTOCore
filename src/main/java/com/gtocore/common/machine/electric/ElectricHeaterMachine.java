package com.gtocore.common.machine.electric;

import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.annotation.Scanned;
import com.gtolib.api.machine.feature.IHeaterMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.machine.trait.IEnhancedRecipeLogic;
import com.gtolib.api.machine.trait.NotifiableSafeEnergyContainer;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.WorkableTieredMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableEnergyContainer;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.transfer.fluid.IFluidHandlerModifiable;

import net.minecraft.core.Direction;
import net.minecraftforge.items.IItemHandlerModifiable;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Scanned
public final class ElectricHeaterMachine extends WorkableTieredMachine implements IHeaterMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ElectricHeaterMachine.class, WorkableTieredMachine.MANAGED_FIELD_HOLDER);
    @Persisted
    @DescSynced
    @RequireRerender
    private int temperature = 273;
    private TickableSubscription tickSubs;

    public ElectricHeaterMachine(IMachineBlockEntity holder) {
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
    public IItemHandlerModifiable getItemHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        return null;
    }

    @Override
    @Nullable
    public IFluidHandlerModifiable getFluidHandlerCap(@Nullable Direction side, boolean useCoverCapability) {
        return null;
    }

    @Nullable
    private Recipe getRecipe() {
        if (temperature >= getMaxTemperature()) return null;
        Recipe recipe = IEnhancedRecipeLogic.of(getRecipeLogic()).gtolib$getRecipeBuilder().duration(20).EUt(30).buildRawRecipe();
        if (RecipeRunner.matchTickRecipe(this, recipe)) {
            return recipe;
        }
        return null;
    }

    @Override
    @NotNull
    protected RecipeLogic createRecipeLogic(Object @NotNull... args) {
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
