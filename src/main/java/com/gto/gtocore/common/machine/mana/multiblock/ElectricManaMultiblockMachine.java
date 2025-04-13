package com.gto.gtocore.common.machine.mana.multiblock;

import com.gto.gtocore.api.capability.ManaContainerList;
import com.gto.gtocore.api.machine.mana.feature.IManaMultiblock;
import com.gto.gtocore.api.machine.mana.trait.ManaTrait;
import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import org.jetbrains.annotations.NotNull;

public class ElectricManaMultiblockMachine extends ElectricMultiblockMachine implements IManaMultiblock {

    private final ManaTrait manaTrait;

    public ElectricManaMultiblockMachine(IMachineBlockEntity holder) {
        super(holder);
        this.manaTrait = new ManaTrait(this);
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
