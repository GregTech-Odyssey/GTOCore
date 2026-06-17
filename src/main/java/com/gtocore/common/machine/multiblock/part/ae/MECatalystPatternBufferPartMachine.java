package com.gtocore.common.machine.multiblock.part.ae;

import com.gtolib.api.machine.trait.NotifiableCatalystHandler;
import com.gtolib.api.machine.trait.NotifiableNotConsumableItemHandler;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;

import org.jetbrains.annotations.NotNull;

public final class MECatalystPatternBufferPartMachine extends MEPatternBufferPartMachineKt {

    public MECatalystPatternBufferPartMachine(MetaMachineBlockEntity holder) {
        super(holder, 36);
    }

    @Override
    @NotNull
    NotifiableNotConsumableItemHandler createShareInventory() {
        return new NotifiableCatalystHandler(this, 9, false);
    }
}
