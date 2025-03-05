package com.gto.gtocore.mixin.gtm.api.recipe;

import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GTRecipeType.class)
public interface GTRecipeTypeAccessor {

    @Accessor(value = "recipeBuilder", remap = false)
    GTRecipeBuilder getRecipeBuilder();
}
