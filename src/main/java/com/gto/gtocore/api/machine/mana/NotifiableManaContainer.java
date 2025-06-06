package com.gto.gtocore.api.machine.mana;

import com.gto.gtocore.api.capability.IManaContainer;
import com.gto.gtocore.api.capability.recipe.ManaRecipeCapability;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class NotifiableManaContainer extends NotifiableRecipeHandlerTrait<Integer> implements IManaContainer {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            NotifiableManaContainer.class, NotifiableRecipeHandlerTrait.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    @DescSynced
    private int manaStored;

    @Getter
    private final IO handlerIO;

    private final int max;
    private final int maxConsumption;

    public NotifiableManaContainer(MetaMachine machine, IO io, int max, int maxConsumption) {
        super(machine);
        handlerIO = io;
        this.max = max;
        this.maxConsumption = maxConsumption;
    }

    @Override
    public List<Integer> handleRecipeInner(IO io, GTRecipe recipe, List<Integer> left, @Nullable String slotName, boolean simulate) {
        int sum = left.stream().reduce(0, Integer::sum);
        if (sum > maxConsumption) return Collections.singletonList(sum);
        if (io == IO.IN) {
            int canOutput = manaStored;
            if (!simulate) {
                manaStored -= Math.min(canOutput, sum);
            }
            sum = sum - canOutput;
        } else if (io == IO.OUT) {
            int canInput = max - manaStored;
            if (!simulate) {
                manaStored += Math.min(canInput, sum);
            }
            sum = sum - canInput;
        }
        return sum <= 0 ? null : Collections.singletonList(sum);
    }

    @Override
    public List<Object> getContents() {
        return List.of(manaStored);
    }

    @Override
    public double getTotalContentAmount() {
        return manaStored;
    }

    @Override
    public RecipeCapability<Integer> getCapability() {
        return ManaRecipeCapability.CAP;
    }

    @Override
    public int getMaxMana() {
        return max;
    }

    @Override
    public void setCurrentMana(int mana) {
        manaStored = mana;
    }

    @Override
    public int getCurrentMana() {
        return manaStored;
    }
}
