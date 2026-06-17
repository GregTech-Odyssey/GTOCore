package com.gtocore.mixin.gtolib;

import com.gtolib.api.machine.feature.multiblock.ICrossRecipeMachine;
import com.gtolib.api.machine.trait.CrossRecipeTrait;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ICrossRecipeMachine.class, remap = false)
public interface ICrossRecipeMachineMixin {

    @Shadow
    CrossRecipeTrait getCrossRecipeTrait();

    @Shadow
    boolean isRepeatedRecipes();

    /**
     * @author Codex
     * @reason Keep non-repeated mode on the cross-recipe scheduler; gtolib's independent-thread path parallelizes the first recipe.
     */
    @Overwrite
    default boolean isIndependentThread() {
        var threadHatch = getCrossRecipeTrait().threadHatchPartMachine;
        return (threadHatch == null || threadHatch.isIThread()) && isRepeatedRecipes();
    }
}
