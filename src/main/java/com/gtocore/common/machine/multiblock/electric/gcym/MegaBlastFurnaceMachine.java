package com.gtocore.common.machine.multiblock.electric.gcym;

import com.gtolib.api.machine.trait.CoilTrait;

import com.gregtechceu.gtceu.api.block.ICoilType;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.ICoilMachine;

public final class MegaBlastFurnaceMachine extends GCYMMultiblockMachine implements ICoilMachine {

    private final CoilTrait coilTrait;

    public MegaBlastFurnaceMachine(MetaMachineBlockEntity holder) {
        super(holder);
        coilTrait = new CoilTrait(this, true, true);
    }

    @Override
    public int getTemperature() {
        return coilTrait.getTemperature();
    }

    @Override
    public ICoilType getCoilType() {
        return coilTrait.getCoilType();
    }
}
