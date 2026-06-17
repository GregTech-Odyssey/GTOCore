package com.gtocore.common.machine.multiblock.electric.bioengineering;

import com.gtocore.common.machine.trait.RadioactivityTrait;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import com.gto.datasynclib.annotations.SaveToDisk;

public final class BiochemicalReactionRoomMachine extends ElectricMultiblockMachine {

    @SaveToDisk
    private final RadioactivityTrait radioactivityTrait;

    public BiochemicalReactionRoomMachine(MetaMachineBlockEntity holder) {
        super(holder);
        radioactivityTrait = new RadioactivityTrait(this);
    }
}
