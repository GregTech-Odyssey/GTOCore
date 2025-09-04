package com.gtocore.common.recipe.condition;

import com.gtolib.api.machine.feature.IHeaterMachine;
import com.gtolib.api.machine.feature.ITemperatureMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.ICoilMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;

public final class HeatCondition extends AbstractRecipeCondition {

    private final int temperature;

    public HeatCondition(int temperature) {
        this.temperature = temperature;
    }

    @Override
    public RecipeConditionType<?> getType() {
        return HEAT;
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("gtocore.recipe.heat.temperature", temperature);
    }

    @Override
    public boolean test(@NotNull Recipe recipe, @NotNull RecipeLogic recipeLogic) {
        MetaMachine machine = recipeLogic.getMachine();
        if (machine instanceof MultiblockControllerMachine controllerMachine) {
            if (machine instanceof ICoilMachine coilMachine && coilMachine.getTemperature() >= temperature) {
                return true;
            }
            for (var p : controllerMachine.getParts()) {
                if (p instanceof ITemperatureMachine t && t.getTemperature() >= temperature) return true;
            }
        }
        for (Direction side : GTUtil.DIRECTIONS) {
            if (checkNeighborHeat(machine, side)) {
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
