package com.gtocore.common.machine.mana.multiblock;

import com.gtolib.api.machine.mana.feature.IManaMultiblock;
import com.gtolib.api.machine.mana.trait.ManaTrait;
import com.gtolib.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gtolib.api.misc.ManaContainerList;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import org.jetbrains.annotations.NotNull;

public class ManaMultiblockMachine extends NoEnergyMultiblockMachine implements IManaMultiblock {

    private final ManaTrait manaTrait;

    public ManaMultiblockMachine(MetaMachineBlockEntity holder) {
        super(holder);
        this.manaTrait = new ManaTrait(this);
    }

    @Override
    @NotNull
    public ManaContainerList getManaContainer() {
        return manaTrait.getManaContainers();
    }

    @Override
    public boolean isGeneratorMana() {
        return false;
    }
}
