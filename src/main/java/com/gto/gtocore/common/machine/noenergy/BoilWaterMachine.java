package com.gto.gtocore.common.machine.noenergy;

import com.gto.gtocore.api.machine.SimpleNoEnergyMachine;
import com.gto.gtocore.api.machine.feature.IReceiveHeatMachine;
import com.gto.gtocore.api.machine.trait.CustomRecipeLogic;
import com.gto.gtocore.api.recipe.GTORecipeBuilder;
import com.gto.gtocore.api.recipe.RecipeRunner;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BoilWaterMachine extends SimpleNoEnergyMachine implements IReceiveHeatMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            BoilWaterMachine.class, SimpleNoEnergyMachine.MANAGED_FIELD_HOLDER);

    @Getter
    @Setter
    @Persisted
    private int temperature = 293;

    private TickableSubscription tickSubs;

    public BoilWaterMachine(IMachineBlockEntity holder) {
        super(holder, 0, i -> 8000);
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
    public @NotNull GTRecipeType getRecipeType() {
        return GTRecipeTypes.STEAM_TURBINE_FUELS;
    }

    @Nullable
    private GTRecipe getRecipe() {
        if (!hasProxies() || temperature < 360) return null;
        GTRecipe recipe = GTORecipeBuilder.ofRaw().duration(20).inputFluids(new FluidStack(Fluids.WATER, 6)).outputFluids(GTMaterials.Steam.getFluid(960 * temperature / 600)).buildRawRecipe();
        if (RecipeRunner.matchRecipe(this, recipe)) {
            return recipe;
        } else if (temperature > 400) {
            if (MachineUtils.inputFluid(this, Fluids.WATER, 1)) {
                doExplosion(6);
            }
        }
        return null;
    }

    @Override
    protected @NotNull RecipeLogic createRecipeLogic() {
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
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }
}
