package com.gto.gtocore.api.machine.mana.feature;

import com.gto.gtocore.api.capability.IManaContainer;

import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.google.common.collect.Table;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IManaEnergyMachine {

    static void addProxy(Table<IO, RecipeCapability<?>, List<IRecipeHandler<?>>> capabilitiesProxy, long eut, IManaContainer manaContainer) {
        capabilitiesProxy.put(IO.IN, EURecipeCapability.CAP, List.of(new ManaEnergyRecipeHandler(eut, manaContainer)));
    }

    class ManaEnergyRecipeHandler implements IRecipeHandler<Long> {

        private final long eut;

        private final IManaContainer container;

        private ManaEnergyRecipeHandler(long eut, IManaContainer container) {
            this.eut = eut;
            this.container = container;
        }

        @Override
        public List<Long> handleRecipeInner(IO io, GTRecipe recipe, List<Long> left, @Nullable String slotName, boolean simulate) {
            long total = left.stream().reduce(0L, Long::sum);
            if (total > eut || total > container.getCurrentMana()) return left;
            container.setCurrentMana((int) (container.getCurrentMana() - total));
            return null;
        }

        @Override
        public List<Object> getContents() {
            return List.of(container.getCurrentMana());
        }

        @Override
        public double getTotalContentAmount() {
            return container.getCurrentMana();
        }

        @Override
        public RecipeCapability<Long> getCapability() {
            return EURecipeCapability.CAP;
        }
    }
}
