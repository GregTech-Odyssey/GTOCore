package com.gtocore.common.recipe.condition;

import com.gtolib.api.machine.feature.IHeaterMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
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
