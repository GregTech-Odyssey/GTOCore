package com.gto.gtocore.mixin.gtm.capability;

import com.gto.gtocore.api.recipe.MapFluid;
import com.gto.gtocore.mixin.gtm.api.recipe.FluidIngredientAccessor;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.api.recipe.lookup.AbstractMapIngredient;
import com.gregtechceu.gtceu.api.recipe.lookup.MapFluidTagIngredient;

import net.minecraftforge.fluids.FluidStack;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(FluidRecipeCapability.class)
public class FluidRecipeCapabilityMixin {

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public List<AbstractMapIngredient> convertToMapIngredient(Object obj) {
        List<AbstractMapIngredient> ingredients = new ObjectArrayList<>(1);
        if (obj instanceof FluidIngredient ingredient) {
            for (FluidIngredient.Value value : ingredient.values) {
                if (value instanceof FluidIngredient.TagValue tagValue) {
                    ingredients.add(new MapFluidTagIngredient(tagValue.getTag()));
                } else {
                    ingredients.add(new MapFluid(((FluidIngredientAccessor) value).getFluid(), ingredient.getNbt()));
                }
            }
        } else if (obj instanceof FluidStack stack) {
            ingredients.add(new MapFluid(stack.getFluid(), stack.getTag()));
        }
        return ingredients;
    }
}
