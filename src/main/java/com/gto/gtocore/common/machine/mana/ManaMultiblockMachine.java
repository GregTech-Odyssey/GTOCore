package com.gto.gtocore.common.machine.mana;

import com.gto.gtocore.api.capability.IManaContainer;
import com.gto.gtocore.api.machine.mana.IManaMultiblock;
import com.gto.gtocore.api.machine.mana.ManaTrait;
import com.gto.gtocore.api.machine.multiblock.NoEnergyMultiblockMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import java.util.Set;

public class ManaMultiblockMachine extends NoEnergyMultiblockMachine implements IManaMultiblock {

    private final ManaTrait manaTrait;

    public ManaMultiblockMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
        this.manaTrait = new ManaTrait(this);
        addTraits(manaTrait);
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
