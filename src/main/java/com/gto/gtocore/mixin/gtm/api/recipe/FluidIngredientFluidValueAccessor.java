package com.gto.gtocore.mixin.gtm.api.recipe;

import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import net.minecraft.world.level.material.Fluid;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FluidIngredient.FluidValue.class)
public interface FluidIngredientFluidValueAccessor {

    @Accessor(value = "fluid", remap = false)
    Fluid getFluid();
}
