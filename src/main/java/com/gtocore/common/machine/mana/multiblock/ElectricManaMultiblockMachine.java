package com.gtocore.common.machine.mana.multiblock;

import com.gtolib.api.machine.mana.feature.IManaMultiblock;
import com.gtolib.api.machine.mana.trait.ManaTrait;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.misc.ManaContainerList;
import com.gtolib.api.recipe.IdleReason;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElectricManaMultiblockMachine extends ElectricMultiblockMachine implements IManaMultiblock {

    private final ManaTrait manaTrait;

    public ElectricManaMultiblockMachine(MetaMachineBlockEntity holder) {
        super(holder);
        this.manaTrait = new ManaTrait(this);
    }

    @Override
    public boolean handleTickRecipe(@Nullable Recipe recipe) {
        if (recipe != null) {
            long eu = recipe.eut;
            if (eu != 0) {
                if (!useEnergy(eu, false)) {
                    IdleReason.setIdleReason(this, recipe.eut < 0 ? IdleReason.INSUFFICIENT_OUT : IdleReason.NO_EU);
                    return false;
                }
            }
            long mana = recipe.manat;
            if (mana != 0) {
                if (!useMana(mana, false)) {
                    IdleReason.setIdleReason(this, recipe.manat < 0 ? IdleReason.INSUFFICIENT_OUT : IdleReason.NO_MANA);
                    return false;
                }
            }
            long cwu = recipe.cwut;
            if (cwu != 0) {
                if (requestCWU(cwu, false) < cwu) {
                    IdleReason.setIdleReason(this, IdleReason.NO_CWU);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean regressWhenWaiting() {
        return false;
    }

    @Override
    public @NotNull ManaContainerList getManaContainer() {
        return manaTrait.getManaContainers();
    }

    @Override
    public boolean isGeneratorMana() {
        return true;
    }
}
