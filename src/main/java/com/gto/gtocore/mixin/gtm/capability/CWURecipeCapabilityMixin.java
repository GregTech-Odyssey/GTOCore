package com.gto.gtocore.mixin.gtm.capability;

import com.gregtechceu.gtceu.api.capability.IOpticalComputationHatch;
import com.gregtechceu.gtceu.api.capability.recipe.*;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.IContentSerializer;

import com.google.common.primitives.Ints;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Mixin(CWURecipeCapability.class)
public class CWURecipeCapabilityMixin extends RecipeCapability<Integer> {

    protected CWURecipeCapabilityMixin(String name, int color, boolean doRenderSlot, int sortIndex, IContentSerializer<Integer> serializer) {
        super(name, color, doRenderSlot, sortIndex, serializer);
    }

    @Override
    public int getMaxParallelRatio(IRecipeCapabilityHolder holder, GTRecipe recipe, int parallelAmount) {
        long maxCWU = 0;
        List<IRecipeHandler<?>> recipeHandlerList = Objects
                .requireNonNullElseGet(holder.getCapabilitiesProxy().get(IO.IN, CWURecipeCapability.CAP), Collections::<IRecipeHandler<?>>emptyList)
                .stream()
                .filter(handler -> !handler.isProxy()).toList();
        for (IRecipeHandler<?> container : recipeHandlerList) {
            if (container.getContents() instanceof IOpticalComputationHatch ncc) {
                maxCWU += ncc.requestCWUt(Integer.MAX_VALUE, true);
            }
        }
        int recipeCWU = CWURecipeCapability.CAP.of(recipe.tickInputs.get(CWURecipeCapability.CAP).get(0).getContent());
        if (recipeCWU == 0) {
            return Integer.MAX_VALUE;
        }
        return Math.abs(Ints.saturatedCast(maxCWU / recipeCWU));
    }
}
