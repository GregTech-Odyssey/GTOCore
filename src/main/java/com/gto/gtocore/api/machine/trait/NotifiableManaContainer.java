package com.gto.gtocore.api.machine.trait;

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

@Getter
public class NotifiableManaContainer extends NotifiableRecipeHandlerTrait<Long> implements IManaContainer {

    public static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            NotifiableManaContainer.class, NotifiableRecipeHandlerTrait.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private final IO handlerIO;

    @Persisted
    @DescSynced
    private long manaStored;

    @DescSynced
    private final long maxCapacity;

    @DescSynced
    private final long maxConsumption;

    public NotifiableManaContainer(MetaMachine machine, IO io, long maxCapacity, long maxConsumption) {
        super(machine);
        this.handlerIO = io;
        this.maxCapacity = maxCapacity;
        this.maxConsumption = maxConsumption;
    }

    @Override
    public List<Long> handleRecipeInner(IO io, GTRecipe recipe, List<Long> left, @Nullable String slotName, boolean simulate) {
        long sum = left.stream().reduce(0L, Long::sum);
        if (sum > maxConsumption) return Collections.singletonList(sum);
        if (io == IO.IN) {
            long canOutput = manaStored;
            if (!simulate) {
                manaStored -= Math.min(canOutput, sum);
            }
            sum = sum - canOutput;
        } else if (io == IO.OUT) {
            long canInput = maxCapacity - manaStored;
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
    public RecipeCapability<Long> getCapability() {
        return ManaRecipeCapability.CAP;
    }

    @Override
    public boolean addMana(long amount) {
        if (amount > maxConsumption || manaStored + amount > maxCapacity) {
            return false;
        }
        manaStored += amount;
        return true;
    }

    @Override
    public boolean removeMana(long amount) {
        if (amount > maxConsumption || manaStored - amount < 0) {
            return false;
        }
        manaStored -= amount;
        return true;
    }
}
