package com.gtocore.common.recipe.condition;

import com.gtolib.api.machine.feature.IGravityPartMachine;

import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipeDefinition;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.api.recipe.handler.IRecipeHandlerHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.network.chat.Component;

import earth.terrarium.adastra.api.planets.PlanetApi;

public final class GravityCondition extends RecipeCondition {

    public final boolean zero;

    public GravityCondition(boolean zero) {
        this.zero = zero;
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("gtocore.condition." + (zero ? "zero_" : "") + "gravity");
    }

    @Override
    public boolean testCondition(IRecipeHandlerHolder holder, RecipeHandlerUnit unit, GTRecipeDefinition recipe) {
        if (holder instanceof MultiblockControllerMachine controllerMachine) {
            for (IMultiPart part : controllerMachine.getParts()) {
                if (part instanceof IGravityPartMachine gravityPart) {
                    return gravityPart.getCurrentGravity() == (zero ? 0 : 100);
                }
            }
        }
        var planet = PlanetApi.API.getPlanet(holder.self().getLevel());
        return planet != null && planet.isSpace() && zero;
    }
}
