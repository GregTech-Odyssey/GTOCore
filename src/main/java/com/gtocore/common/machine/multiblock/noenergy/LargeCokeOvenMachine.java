package com.gtocore.common.machine.multiblock.noenergy;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.info.FluidRecipeInfo;
import com.gregtechceu.gtceu.common.machine.multiblock.primitive.CokeOvenMachine;

import org.jetbrains.annotations.NotNull;

public final class LargeCokeOvenMachine extends CokeOvenMachine {

    public LargeCokeOvenMachine(MetaMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    protected @NotNull NotifiableFluidTank createExportFluidHandler(Object @NotNull... args) {
        return new NotifiableFluidTank(this, getRecipeType().getMaxOutputs(FluidRecipeInfo.INSTANCE), 512000, IO.OUT);
    }
}
