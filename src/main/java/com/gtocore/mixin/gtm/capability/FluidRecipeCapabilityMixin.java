package com.gtocore.mixin.gtm.capability;

import com.gtolib.api.recipe.ingredient.FastFluidIngredient;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.content.ContentModifier;
import com.gregtechceu.gtceu.api.recipe.content.IContentSerializer;
import com.gregtechceu.gtceu.api.recipe.ingredient.FluidIngredient;
import com.gregtechceu.gtceu.integration.xei.entry.fluid.FluidEntryList;
import com.gregtechceu.gtceu.integration.xei.entry.fluid.FluidStackList;

import net.minecraftforge.fluids.FluidStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(FluidRecipeCapability.class)
public abstract class FluidRecipeCapabilityMixin extends RecipeCapability<FluidIngredient> {

    protected FluidRecipeCapabilityMixin(String name, int color, boolean doRenderSlot, int sortIndex, IContentSerializer<FluidIngredient> serializer) {
        super(name, color, doRenderSlot, sortIndex, serializer);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public FluidIngredient copyWithModifier(FluidIngredient content, ContentModifier modifier) {
        if (content instanceof FastFluidIngredient fastFluidIngredient) {
            FastFluidIngredient copy = fastFluidIngredient.copy();
            copy.setAmount(modifier.apply(copy.amount()));
            return copy;
        }
        return content;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public static FluidEntryList mapFluid(FluidIngredient ingredient) {
        FluidStackList fluids = new FluidStackList();
        var f = FastFluidIngredient.getFluid(ingredient);
        if (f == null) return fluids;

        fluids.add(new FluidStack(f, ingredient.getAmount(), ingredient.getNbt()));
        return fluids;
    }
}
