package com.gto.gtocore.utils;

import com.gto.gtocore.api.fluid.IFluid;
import com.gto.gtocore.mixin.gtm.api.recipe.FluidIngredientFluidValueAccessor;

import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

public final class FluidUtils {

    private FluidUtils() {}

    public static String getId(Fluid fluid) {
        return getIdLocation(fluid).toString();
    }

    public static ResourceLocation getIdLocation(Fluid fluid) {
        return ((IFluid) fluid).gtocore$getIdLocation();
    }

    public static Fluid getFirst(FluidIngredient fluidIngredient) {
        for (FluidIngredient.Value value : fluidIngredient.values) {
            if (value instanceof FluidIngredient.FluidValue fluidValue) {
                return ((FluidIngredientFluidValueAccessor) fluidValue).getFluid();
            }
            for (Fluid fluid : value.getFluids()) {
                return fluid;
            }
        }
        return null;
    }

    public static Fluid getFluid(String fluidName) {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidName));
        return fluid == null ? Fluids.EMPTY : fluid;
    }
}
