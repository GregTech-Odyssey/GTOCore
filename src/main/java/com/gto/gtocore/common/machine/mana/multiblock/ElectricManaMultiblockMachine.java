package com.gto.gtocore.common.machine.mana.multiblock;

import com.gto.gtocore.api.capability.IManaContainer;
import com.gto.gtocore.api.machine.mana.feature.IManaMultiblock;
import com.gto.gtocore.api.machine.mana.trait.ManaTrait;
import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import java.util.Set;

public class ElectricManaMultiblockMachine extends ElectricMultiblockMachine implements IManaMultiblock {

    private final ManaTrait manaTrait;

    public ElectricManaMultiblockMachine(IMachineBlockEntity holder, Object... args) {
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
        return true;
    }
}
