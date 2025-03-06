package com.gto.gtocore.mixin.emi;

import com.gregtechceu.gtceu.integration.emi.recipe.GTRecipeEMICategory;

import dev.emi.emi.api.EmiRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(GTRecipeEMICategory.class)
public final class GTRecipeEMICategoryMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static void registerDisplays(EmiRegistry registry) {}
}
