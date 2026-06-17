package com.gtocore.common.recipe.condition;

import com.gtocore.api.machine.part.IHeatContainerPart;

import com.gtolib.api.capability.IHeatContainer;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.ICoilMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiController;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.handler.IRecipeHandlerHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

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
        if (holder instanceof IMultiController controller) {
            if (holder instanceof ICoilMachine coilMachine && coilMachine.getTemperature() >= temperature) {
                return true;
            }
            for (var p : controller.getParts()) {
                if (p instanceof IHeatContainerPart t && t.getHeatContainer().getTemperature() >= temperature) return true;
            }
        } else {
            var container = IHeatContainer.getCapability(holder.self().holder);
            if (container != null) {
                return container.getTemperature() >= temperature;
            }
        }
        return false;
    }
}
