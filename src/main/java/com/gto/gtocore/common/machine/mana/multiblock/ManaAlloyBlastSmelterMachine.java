package com.gto.gtocore.common.machine.mana.multiblock;

import com.gto.gtocore.api.capability.ManaContainerList;
import com.gto.gtocore.api.machine.mana.feature.IManaMultiblock;
import com.gto.gtocore.api.machine.mana.trait.ManaTrait;
import com.gto.gtocore.api.machine.multiblock.CoilCustomParallelMultiblockMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ManaAlloyBlastSmelterMachine extends CoilCustomParallelMultiblockMachine implements IManaMultiblock {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            ManaAlloyBlastSmelterMachine.class, CoilCustomParallelMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Persisted
    private int time;

    private int mana;

    private final ManaTrait manaTrait;

    public ManaAlloyBlastSmelterMachine(IMachineBlockEntity holder) {
        super(holder, true, true, true, m -> 8);
        this.manaTrait = new ManaTrait(this);
    }

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        mana = 1 << getTier();
    }

    @Override
    public boolean onWorking() {
        if (super.onWorking()) {
            time++;
            if (time > 60) {
                time = 0;
                if (false) {
                    mana = 1 << getTier();
                } else {
                    mana <<= 2;
                }
            }
            if (getOffsetTimer() % 20 == 0 && removeMana(mana, 1, true) != mana) return false;
            removeMana(mana, 1, false);
            return true;
        }
        return false;
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        return super.beforeWorking(recipe) && removeMana(mana, 1, false) == mana;
    }

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public @NotNull ManaContainerList getManaContainer() {
        return manaTrait.getManaContainers();
    }

    @Override
    public boolean isGeneratorMana() {
        return false;
    }
}
