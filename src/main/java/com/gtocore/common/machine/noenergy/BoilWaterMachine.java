package com.gtocore.common.machine.noenergy;

import com.gtolib.api.machine.SimpleNoEnergyMachine;
import com.gtolib.api.machine.feature.IReceiveHeatMachine;
import com.gtolib.api.machine.trait.CustomRecipeLogic;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BoilWaterMachine extends SimpleNoEnergyMachine implements IReceiveHeatMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(BoilWaterMachine.class, SimpleNoEnergyMachine.MANAGED_FIELD_HOLDER);
    public static final int DrawWaterExplosionLine = 400;
    @Persisted
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

    @Nullable
    private Recipe getRecipe() {
        if (temperature < 360) return null;
        Recipe recipe = getRecipeBuilder().duration(20).inputFluids(new FluidStack(Fluids.WATER, 6)).outputFluids(GTMaterials.Steam.getFluid(960 * temperature / 600)).buildRawRecipe();
        if (RecipeRunner.matchRecipe(this, recipe)) {
            return recipe;
        } else if (temperature > DrawWaterExplosionLine) {
            if (inputFluid(Fluids.WATER, 1)) {
                doExplosion(6);
            }
        }
        return null;
    }

    @Override
    @NotNull
    public RecipeLogic createRecipeLogic() {
        return new CustomRecipeLogic(this, this::getRecipe);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!isRemote()) {
            tickSubs = subscribeServerTick(tickSubs, this::tickUpdate);
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
