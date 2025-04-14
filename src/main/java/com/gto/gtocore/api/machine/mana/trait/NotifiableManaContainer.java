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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class NotifiableManaContainer extends NotifiableRecipeHandlerTrait<Long> implements IManaContainer {

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

    @Getter
    @Setter
    @Persisted
    @DescSynced
    private long currentMana;

    @Setter
    private boolean acceptDistributor;

    @Getter
    private final IO handlerIO;

    @Getter
    private final long maxMana;

    @Getter
    private final long maxConsumptionRate;

    @Getter
    private final long maxProductionRate;

    public NotifiableManaContainer(MetaMachine machine, IO io, long maxMana) {
        super(machine);
        handlerIO = io;
        this.maxMana = maxMana;
        this.maxConsumptionRate = io != IO.NONE ? maxMana : 0;
        this.maxProductionRate = io != IO.NONE ? maxMana : 0;
    }

    public NotifiableManaContainer(MetaMachine machine, IO io, long maxMana, long maxIORate) {
        super(machine);
        handlerIO = io;
        this.maxMana = maxMana;
        long tmpMaxRate = GTMath.clamp(maxIORate, 0, maxMana);
        if (io == IO.IN) {
            this.maxConsumptionRate = maxMana;
            this.maxProductionRate = tmpMaxRate;
        } else if (io == IO.OUT) {
            this.maxConsumptionRate = tmpMaxRate;
            this.maxProductionRate = maxMana;
        } else if (io == IO.BOTH) {
            this.maxConsumptionRate = tmpMaxRate;
            this.maxProductionRate = tmpMaxRate;
        } else {
            this.maxConsumptionRate = 0;
            this.maxProductionRate = 0;
        }
    }

    public NotifiableManaContainer(MetaMachine machine, IO io, long maxMana, int maxConsumptionRate, int maxProductionRate) {
        super(machine);
        handlerIO = io;
        this.maxMana = maxMana;
        this.maxConsumptionRate = GTMath.clamp(maxConsumptionRate, 0, maxMana);
        this.maxProductionRate = GTMath.clamp(maxProductionRate, 0, maxMana);
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
            currentMana = currentMana + distributor.removeMana(mana, 20, false);
        }
    }

    protected long extractionRate() {
        return maxMana - currentMana;
    }

    @Override
    public List<Long> handleRecipeInner(IO io, GTRecipe recipe, List<Long> left, boolean simulate) {
        long sum = Math.abs(left.stream().reduce(0L, Long::sum));
        if (io == IO.IN) {
            long change = removeMana(sum, 1, simulate);
            sum = sum - change;
        } else if (io == IO.OUT) {
            long change = addMana(sum, 1, simulate);
            sum = sum - change;
        }
        return sum <= 0 ? null : Collections.singletonList(sum);
    }

    @Override
    public @NotNull List<Object> getContents() {
        return List.of(currentMana);
    }

    @Override
    public double getTotalContentAmount() {
        return currentMana;
    }

    @Override
    public RecipeCapability<Long> getCapability() {
        return ManaRecipeCapability.CAP;
    }

    @Override
    public boolean acceptDistributor() {
        return acceptDistributor;
    }
}
