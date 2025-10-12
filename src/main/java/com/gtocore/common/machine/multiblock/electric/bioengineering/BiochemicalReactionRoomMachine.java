package com.gtocore.common.machine.multiblock.electric.bioengineering;

import com.gtocore.common.machine.trait.RadioactivityTrait;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;

public final class BiochemicalReactionRoomMachine extends ElectricMultiblockMachine {

    @Persisted
    private final RadioactivityTrait radioactivityTrait;

    public BiochemicalReactionRoomMachine(MetaMachineBlockEntity holder) {
        super(holder);
        radioactivityTrait = new RadioactivityTrait(this);
    }
}
