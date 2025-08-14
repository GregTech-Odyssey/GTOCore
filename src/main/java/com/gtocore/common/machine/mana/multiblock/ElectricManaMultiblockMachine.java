package com.gtocore.common.machine.mana.multiblock;

import com.gtolib.api.machine.mana.feature.IManaMultiblock;
import com.gtolib.api.machine.mana.trait.ManaTrait;
import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.api.misc.ManaContainerList;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import org.jetbrains.annotations.NotNull;

public class ElectricManaMultiblockMachine extends ElectricMultiblockMachine implements IManaMultiblock {

    private final ManaTrait manaTrait;

    public ElectricManaMultiblockMachine(MetaMachineBlockEntity holder) {
        super(holder);
        this.manaTrait = new ManaTrait(this);
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
