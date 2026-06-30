package com.gtocore.common.machine.noenergy;

import com.gtolib.api.machine.SimpleNoEnergyMachine;
import com.gtolib.api.machine.heat.HeatHandler;
import com.gtolib.api.machine.heat.feature.IHeatContainerMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.gto.datasynclib.annotations.SyncToClient;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public final class HeaterMachine extends SimpleNoEnergyMachine implements IHeatContainerMachine, IExplosionMachine {

    public static final int MaxTemperature = 800;

    @Getter
    @SaveToDisk
    @SyncToClient
    private final HeatHandler heatContainer;

    public HeaterMachine(MetaMachineBlockEntity holder) {
        super(holder, 0, i -> 8000);
        heatContainer = new HeatHandler(holder, MaxTemperature, 1, 0.4, 0.01);
        heatContainer.setSideIOCondition(s -> s != getFrontFacing() && s != Direction.DOWN);
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
        return GTRecipeTypes.STEAM_BOILER_RECIPES;
    }

    @Override
    public GTRecipe fullModifyRecipe(RecipeHandlerUnit unit, GTRecipeDefinition definition) {
        return definition.toRuntime();
    }

    @Override
    public void setWorkingEnabled(boolean isWorkingAllowed) {}

    private void setEnabled(boolean isWorkingAllowed) {
        if (!isWorkingAllowed) getRecipeLogic().interruptRecipe();
        super.setWorkingEnabled(isWorkingAllowed);
    }

    @Override
    public void onNeighborChanged(@NotNull Block block, @NotNull BlockPos fromPos, boolean isMoving) {
        super.onNeighborChanged(block, fromPos, isMoving);
        updateEnabled();
    }

    private void updateEnabled() {
        Level level = getLevel();
        if (level == null) return;
        setEnabled(level.getBlockState(getPos().relative(getFrontFacing())).isAir());
    }

    @Override
    public void onWorking() {
        super.onWorking();
        if (getOffsetTimer() % 10 == 0) {
            heatContainer.addHeatUnrestricted(8, false);
        }
    }
}
