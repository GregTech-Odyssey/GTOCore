package com.gtocore.mixin.gtm.machine;

import com.gtocore.common.data.GTORecipeDataKeys;
import com.gtocore.data.IdleReason;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.IRecipeHandlerHolder;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.common.machine.multiblock.steam.LargeBoilerMachine;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LargeBoilerMachine.class)
public abstract class LargeBoilerMachineMixin extends WorkableMultiblockMachine {

    public LargeBoilerMachineMixin(MetaMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public boolean usePrioritySearch() {
        return true;
    }

    @Override
    public boolean alwaysSearchRecipe() {
        return true;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static @Nullable GTRecipe recipeModifier(IRecipeHandlerHolder machine, RecipeHandlerUnit unit, GTRecipe recipe) {
        if (machine instanceof LargeBoilerMachine largeBoilerMachine) {
            if (recipe.data.getInt(GTORecipeDataKeys.TEMPERATURE) > largeBoilerMachine.getCurrentTemperature() + 274) {
                largeBoilerMachine.setIdleReason(IdleReason.INSUFFICIENT_TEMPERATURE::reason);
                return null;
            }
            double duration = recipe.duration * 1600.0D / largeBoilerMachine.maxTemperature;
            if (duration < 1) {
                recipe = ParallelLogic.accurateParallel(machine, unit, recipe, (long) (1 / duration));
                if (recipe == null) return null;
            }
            if (largeBoilerMachine.getThrottle() < 100) {
                duration = duration * 100 / largeBoilerMachine.getThrottle();
            }
            recipe.duration = (int) duration;
            return recipe;
        }
        return null;
    }
}
