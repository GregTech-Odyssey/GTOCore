package com.gto.gtocore.mixin.gtm.api.recipe;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeCapabilityHolder;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.chance.logic.ChanceLogic;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GTRecipe.class)
public class GTRecipeMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public ChanceLogic getChanceLogicForCapability(RecipeCapability<?> cap, IO io, boolean isTick) {
        return ChanceLogic.OR;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void postWorking(IRecipeCapabilityHolder holder) {}
}
