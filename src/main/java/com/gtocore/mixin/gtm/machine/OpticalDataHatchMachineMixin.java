package com.gtocore.mixin.gtm.machine;

import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.capability.IOpticalDataAccessHatch;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.machine.multiblock.part.OpticalDataHatchMachine;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(OpticalDataHatchMachine.class)
public abstract class OpticalDataHatchMachineMixin implements IOpticalDataAccessHatch {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public @Nullable GTRecipe modifyRecipe(GTRecipe recipe) {
        if (recipe instanceof Recipe gtoRecipe) {
            var root = gtoRecipe.rootRecipe;
            if (root == null || root.researchData == null) return recipe;
            if (isRecipeAvailable(root)) return recipe;
        }
        return null;
    }
}
