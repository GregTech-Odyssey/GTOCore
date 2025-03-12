package com.gto.gtocore.common.machine.mana.multiblock;

import com.gto.gtocore.api.capability.IManaContainer;
import com.gto.gtocore.api.machine.mana.feature.IManaMultiblock;
import com.gto.gtocore.api.machine.mana.trait.ManaTrait;
import com.gto.gtocore.api.machine.multiblock.NoEnergyMultiblockMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import java.util.Set;

public class ManaMultiblockMachine extends NoEnergyMultiblockMachine implements IManaMultiblock {

    private final ManaTrait manaTrait;

    public ManaMultiblockMachine(IMachineBlockEntity holder) {
        super(holder);
        this.manaTrait = new ManaTrait(this);
    }

    @Override
    public Set<IManaContainer> getManaContainer() {
        return manaTrait.getManaContainers();
    }

    @Override
    public boolean isGeneratorMana() {
        return false;
    }
}
