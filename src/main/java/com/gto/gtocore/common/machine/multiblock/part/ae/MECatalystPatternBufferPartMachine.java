package com.gto.gtocore.common.machine.multiblock.part.ae;

import com.gto.gtocore.api.machine.trait.NotifiableCatalystHandler;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableItemStackHandler;

import org.jetbrains.annotations.NotNull;

public final class MECatalystPatternBufferPartMachine extends MEPatternBufferPartMachine {

    public MECatalystPatternBufferPartMachine(IMachineBlockEntity holder) {
        super(holder);
    }

    @Override
    @NotNull
    NotifiableItemStackHandler createShareInventory() {
        return new NotifiableCatalystHandler(this, 9);
    }
}
