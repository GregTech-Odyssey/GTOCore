package com.gto.gtocore.api.machine.feature.multiblock;

import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import org.jetbrains.annotations.NotNull;

public interface IMEOutputMachine {

    default boolean gTOCore$isItemOutput() {
        return false;
    }

    default boolean gTOCore$isFluidOutput() {
        return false;
    }

    default boolean gTOCore$DualMEOutput(@NotNull GTRecipe recipe) {
        return false;
    }

    default boolean gTOCore$DualMEOutput(boolean hasItem, boolean hasFluid) {
        return false;
    }
}
