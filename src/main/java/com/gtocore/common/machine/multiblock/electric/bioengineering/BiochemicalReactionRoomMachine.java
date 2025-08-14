package com.gtocore.common.machine.multiblock.electric.bioengineering;

import com.gtocore.common.machine.trait.RadioactivityTrait;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;
import org.jetbrains.annotations.NotNull;

public final class BiochemicalReactionRoomMachine extends ElectricMultiblockMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            BiochemicalReactionRoomMachine.class, ElectricMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Persisted
    private final RadioactivityTrait radioactivityTrait;

    public BiochemicalReactionRoomMachine(MetaMachineBlockEntity holder) {
        super(holder);
        radioactivityTrait = new RadioactivityTrait(this);
    }
}
