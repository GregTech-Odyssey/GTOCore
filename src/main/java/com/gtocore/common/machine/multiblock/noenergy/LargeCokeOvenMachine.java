package com.gtocore.common.machine.multiblock.noenergy;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.common.machine.multiblock.primitive.CokeOvenMachine;

import org.jetbrains.annotations.NotNull;

public final class LargeCokeOvenMachine extends CokeOvenMachine {

    public LargeCokeOvenMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    protected @NotNull NotifiableFluidTank createExportFluidHandler(Object @NotNull... args) {
        return new NotifiableFluidTank(this, getRecipeType().getMaxOutputs(FluidRecipeCapability.CAP), 512000, IO.OUT);
    }
}
