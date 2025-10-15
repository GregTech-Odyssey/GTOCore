package com.gtocore.api.misc;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import java.util.Collections;
import java.util.List;

public class CreativeEnergyRecipeHandler implements IRecipeHandler<Long> {
    private final long maxHandler;
    public CreativeEnergyRecipeHandler(long maxHandler){
        this.maxHandler=maxHandler;
    }

    @Override
    public List<Long> handleRecipeInner(IO io, GTRecipe recipe, List<Long> left, boolean simulate) {
        if(io!=IO.IN)return left;
        long eut = left.stream().reduce(0L, Long::sum);
        if(eut<maxHandler)return null;
        return Collections.singletonList(eut-maxHandler);
    }


    @Override
    public RecipeCapability<Long> getCapability() {
        return EURecipeCapability.CAP;
    }
}
