package com.gtocore.common.machine.mana;

import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.machine.feature.IHeaterMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import net.minecraft.core.Direction;
import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ManaHeaterMachine extends SimpleManaMachine implements IHeaterMachine {

    private static final FluidStack SALAMANDER = GTOMaterials.Salamander.getFluid(FluidStorageKeys.GAS, 10);
    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ManaHeaterMachine.class, SimpleManaMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    @DescSynced
    private int temperature = 293;

    /// an indicator used to determine if the salamander input is present
    /// **used by client renderer**
    @Persisted
    @DescSynced
    private boolean salamanderInput = false;
    private TickableSubscription tickSubs;

    public ManaHeaterMachine(IMachineBlockEntity holder) {
        super(holder, 2, t -> 8000);
    }

    @Nullable
    private Recipe getRecipe() {
        if (temperature >= getMaxTemperature()) return null;
        Recipe recipe = RecipeBuilder.ofRaw().duration(20).MANAt(16).buildRawRecipe();
        if (RecipeRunner.matchTickRecipe(this, recipe)) {
            return recipe;
        }
        return null;
    }

    @Override
    @NotNull
    protected RecipeLogic createRecipeLogic() {
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
    public void clientTick() {
        super.clientTick();
        if (self().getOffsetTimer() % 10 == 0) {
            this.scheduleRenderUpdate();
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
                var hasSalamander = inputFluid(SALAMANDER);
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
    @NotNull
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public void setTemperature(final int temperature) {
        this.temperature = temperature;
    }

    public int getTemperature() {
        return this.temperature;
    }

    public boolean hasSalamanderInput() {
        return salamanderInput;
    }
}
