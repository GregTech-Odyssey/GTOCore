package com.gto.gtocore.api.machine.mana.trait;

import com.gto.gtocore.api.capability.IManaContainer;
import com.gto.gtocore.api.capability.recipe.ManaRecipeCapability;
import com.gto.gtocore.common.machine.mana.multiblock.ManaDistributorMachine;

import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.RecipeCapability;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableRecipeHandlerTrait;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.utils.GTMath;

import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@Slf4j
public class NotifiableManaContainer extends NotifiableRecipeHandlerTrait<Integer> implements IManaContainer {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            NotifiableManaContainer.class, NotifiableRecipeHandlerTrait.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Getter
    @Setter
    private ManaDistributorMachine NetMachineCache;

    @Nullable
    private TickableSubscription updateSubs;

    @Persisted
    @DescSynced
    private long manaStored;

    @Setter
    private boolean acceptDistributor;

    @Getter
    private final IO handlerIO;

    private final long max;
    @Getter
    private final int maxIORate;

    public NotifiableManaContainer(MetaMachine machine, IO io, long max, int maxIORate) {
        super(machine);
        handlerIO = io;
        this.max = max;
        this.maxIORate = maxIORate;
    }

    @Override
    public void onMachineLoad() {
        super.onMachineLoad();
        updateSubs = getMachine().subscribeServerTick(updateSubs, this::updateTick);
    }

    @Override
    public void onMachineUnLoad() {
        super.onMachineUnLoad();
        removeNetMachineCache();
        if (updateSubs != null) {
            updateSubs.unsubscribe();
            updateSubs = null;
        }
    }

    private void updateTick() {
        if (getMachine().getOffsetTimer() % 20 == 0) {
            ManaDistributorMachine distributor = getNetMachine();
            if (distributor == null) return;
            long mana = extractionRate();
            if (mana <= 0) return;
            manaStored = manaStored + distributor.removeMana(mana, 20, false);
        }
    }

    protected long extractionRate() {
        return max - manaStored;
    }

    @Override
    public List<Integer> handleRecipeInner(IO io, GTRecipe recipe, List<Integer> left, boolean simulate) {
        int sum = left.stream().reduce(0, Integer::sum);
        if (io == IO.IN) {
            int canOutput = Math.min(maxConsumption, getSaturatedCurrentMana());
            if (!simulate) {
                manaStored -= Math.min(canOutput, sum);
            }
            sum = sum - canOutput;
        } else if (io == IO.OUT) {
            int canInput = Math.min(maxConsumption, GTMath.saturatedCast(max - manaStored));
            if (!simulate) {
                manaStored += Math.min(canInput, sum);
            }
            sum = sum - canInput;
        }
        return sum <= 0 ? null : Collections.singletonList(sum);
    }

    @Override
    public @NotNull List<Object> getContents() {
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
    public boolean acceptDistributor() {
        return acceptDistributor;
    }

    @Override
    public long getMaxMana() {
        return max;
    }

    @Override
    public void setCurrentMana(long mana) {
        manaStored = mana;
    }

    @Override
    public long getCurrentMana() {
        return manaStored;
    }
}
