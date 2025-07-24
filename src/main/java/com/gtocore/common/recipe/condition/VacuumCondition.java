package com.gtocore.common.recipe.condition;

import com.gtolib.api.machine.feature.IVacuumMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockControllerMachine;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.condition.RecipeConditionType;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import earth.terrarium.adastra.api.planets.PlanetApi;
import earth.terrarium.adastra.api.systems.OxygenApi;
import org.jetbrains.annotations.NotNull;

public final class VacuumCondition extends AbstractRecipeCondition {

    private final int tier;

    public VacuumCondition(int tier) {
        this.tier = tier;
    }

    @Override
    public RecipeConditionType<?> getType() {
        return VACUUM;
    }

    @Override
    public Component getTooltips() {
        return Component.translatable("gtocore.recipe.vacuum.tier", tier);
    }

    @Override
    public boolean test(@NotNull Recipe recipe, @NotNull RecipeLogic recipeLogic) {
        MetaMachine machine = recipeLogic.getMachine();

        if (machine instanceof MultiblockControllerMachine controllerMachine) {
            if (checkVacuumTier(controllerMachine.self().getParts())) {
                return true;
            }
        }

        for (Direction side : GTUtil.DIRECTIONS) {
            if (side.getAxis() != Direction.Axis.Y && checkNeighborVacuumTier(machine, side)) {
                return true;
            }
        }
        Level level = machine.getLevel();
        return !OxygenApi.API.hasOxygen(level, machine.getPos()) && PlanetApi.API.isSpace(level);
    }

    private boolean checkVacuumTier(Iterable<IMultiPart> parts) {
        for (IMultiPart part : parts) {
            if (part instanceof IVacuumMachine vacuumMachine && vacuumMachine.getVacuumTier() >= tier) {
                return true;
            }
        }
        return false;
    }

    private boolean checkNeighborVacuumTier(MetaMachine machine, Direction side) {
        if (machine.getNeighborMachine(side) instanceof IVacuumMachine vacuumMachine) {
            return vacuumMachine.getVacuumTier() >= tier;
        }
        return false;
    }
}
