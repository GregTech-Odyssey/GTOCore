package com.gtocore.common.machine.multiblock.part;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.common.machine.multiblock.part.SteamHatchPartMachine;

import org.jetbrains.annotations.NotNull;

public final class LargeSteamHatchPartMachine extends SteamHatchPartMachine {

    public LargeSteamHatchPartMachine(MetaMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    protected @NotNull NotifiableFluidTank createTank(int initialCapacity, int slots, Object @NotNull... args) {
        return super.createTank(initialCapacity << 6, slots);
    }
}
