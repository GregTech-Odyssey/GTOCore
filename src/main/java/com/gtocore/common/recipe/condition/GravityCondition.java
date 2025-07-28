package com.gtocore.common.recipe.condition;

import com.gtolib.api.machine.feature.IGravityPartMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;

import net.minecraft.network.chat.Component;

import earth.terrarium.adastra.api.planets.PlanetApi;
import org.jetbrains.annotations.NotNull;

public final class GravityCondition extends AbstractRecipeCondition {

    private final boolean zero;

    public GravityCondition(boolean zero) {
        this.zero = zero;
    }

    @Override
    public RecipeConditionType<?> getType() {
        return GRAVITY;
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("gtocore.condition." + (zero ? "zero_" : "") + "gravity");
    }

    @Override
    public boolean test(@NotNull Recipe recipe, @NotNull RecipeLogic recipeLogic) {
        MetaMachine machine = recipeLogic.getMachine();
        if (machine instanceof MultiblockControllerMachine controllerMachine) {
            for (IMultiPart part : controllerMachine.getParts()) {
                if (part instanceof IGravityPartMachine gravityPart) {
                    return gravityPart.getCurrentGravity() == (zero ? 0 : 100);
                }
            }
        }
        var planet = PlanetApi.API.getPlanet(machine.getLevel());
        return planet != null && planet.isSpace() && zero;
    }
}
