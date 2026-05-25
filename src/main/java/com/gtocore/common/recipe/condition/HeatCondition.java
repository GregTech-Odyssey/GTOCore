package com.gtocore.common.recipe.condition;

import com.gtolib.api.machine.feature.IHeaterMachine;
import com.gtolib.api.machine.feature.ITemperatureMachine;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.ICoilMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.handler.IRecipeHandlerHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

public final class HeatCondition extends RecipeCondition {

    private final int temperature;

    public HeatCondition(int temperature) {
        this.temperature = temperature;
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("gtocore.recipe.heat.temperature", temperature);
    }

    @Override
    public boolean testCondition(IRecipeHandlerHolder holder, RecipeHandlerUnit unit, GTRecipeDefinition recipe) {
        if (holder instanceof MultiblockControllerMachine controllerMachine) {
            if (holder instanceof ICoilMachine coilMachine && coilMachine.getTemperature() >= temperature) {
                return true;
            }
            for (var p : controllerMachine.getParts()) {
                if (p instanceof ITemperatureMachine t && t.getTemperature() >= temperature) return true;
            }
        }
        for (Direction side : GTUtil.DIRECTIONS) {
            if (checkNeighborHeat(holder.self(), side)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkNeighborHeat(MetaMachine machine, Direction side) {
        if (machine.getNeighborMachine(side) instanceof IHeaterMachine heaterMachine) {
            return heaterMachine.getTemperature() >= temperature && heaterMachine.reduceTemperature(4) == 4;
        }
        return false;
    }
}
