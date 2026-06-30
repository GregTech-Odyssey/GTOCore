package com.gtocore.common.machine.mana;

import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.machine.heat.HeatHandler;
import com.gtolib.api.machine.heat.feature.IHeatContainerMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.ICustomRecipeLogicHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.gto.datasynclib.annotations.SyncToClient;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class ManaHeaterMachine extends SimpleManaMachine implements IHeatContainerMachine, ICustomRecipeLogicHolder {

    private static final Fluid SALAMANDER = GTOMaterials.Salamander.getFluid(FluidStorageKeys.GAS);

    /// an indicator used to determine if the salamander input is present
    /// **used by client renderer**
    @SaveToDisk
    @SyncToClient(notifyUpdate = true)
    private boolean salamanderInput = false;

    @Getter
    @SaveToDisk
    @SyncToClient
    private final HeatHandler heatContainer;

    public ManaHeaterMachine(MetaMachineBlockEntity holder) {
        super(holder, 2, t -> 8000);
        heatContainer = new HeatHandler(holder, 2400, 4, 0.4, 0.01);
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
            if (heatContainer.currentHeat + 80 < heatContainer.maxHeat) {
                var hasSalamander = inputFluid(SALAMANDER, 10);
                this.salamanderInput = hasSalamander;
                heatContainer.addHeatUnrestricted(hasSalamander ? 80 : 16, false);
            } else {
                getRecipeLogic().markLastRecipeDirty();
            }
        }
    }

    public boolean hasSalamanderInput() {
        return salamanderInput;
    }

    @Override
    public GTRecipeDefinition createCustomRecipe(RecipeHandlerUnit unit) {
        if (heatContainer.currentHeat + 80 >= heatContainer.maxHeat) return null;
        return getRecipeBuilder().duration(20).MANAt(16).build();
    }
}
