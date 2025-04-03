package com.gto.gtocore.mixin.gtm.api.recipe;

import com.gto.gtocore.api.recipe.IGTRecipe;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GTRecipe.class)
public class GTRecipeMixin implements IGTRecipe {

    @Unique
    private boolean gtocore$overclocking;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public ChanceLogic getChanceLogicForCapability(RecipeCapability<?> cap, IO io, boolean isTick) {
        return ChanceLogic.OR;
    }

    @Override
    public boolean gtocore$overclocking() {
        return gtocore$overclocking;
    }

    @Override
    public void gtocore$setOverclocking() {
        gtocore$overclocking = true;
    }
}
