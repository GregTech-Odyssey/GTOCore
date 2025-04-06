package com.gto.gtocore.mixin.gtm.api.recipe;

import com.gto.gtocore.api.recipe.IGTRecipe;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GTRecipe.class)
public class GTRecipeMixin implements IGTRecipe {

    @Unique
    private boolean gtocore$perfect;

    @Override
    public boolean gtocore$perfect() {
        return gtocore$perfect;
    }

    @Override
    public void gtocore$setPerfect(boolean perfect) {
        gtocore$perfect = perfect;
    }
}
