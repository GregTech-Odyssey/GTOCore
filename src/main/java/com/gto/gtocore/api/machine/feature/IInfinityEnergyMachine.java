package com.gto.gtocore.api.machine.feature;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.google.common.collect.Table;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IInfinityEnergyMachine {

    static void addProxy(Table<IO, RecipeCapability<?>, List<IRecipeHandler<?>>> capabilitiesProxy, long eut) {
        capabilitiesProxy.put(IO.IN, EURecipeCapability.CAP, List.of(new InfinityEnergyRecipeHandler(eut)));
    }

    final class InfinityEnergyRecipeHandler implements IRecipeHandler<Long> {

        private final long eut;

        private InfinityEnergyRecipeHandler(long eut) {
            this.eut = eut;
        }

        @Override
        public List<Long> handleRecipeInner(IO io, GTRecipe recipe, List<Long> left, @Nullable String slotName, boolean simulate) {
            return left.stream().reduce(0L, Long::sum) > eut ? left : null;
        }

        @Override
        public List<Object> getContents() {
            return List.of(Long.MAX_VALUE);
        }

        @Override
        public double getTotalContentAmount() {
            return Long.MAX_VALUE;
        }

        @Override
        public RecipeCapability<Long> getCapability() {
            return EURecipeCapability.CAP;
        }
    }
}
