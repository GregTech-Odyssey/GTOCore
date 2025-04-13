package com.gto.gtocore.common.machine.multiblock.electric.processing;

import com.gto.gtocore.api.machine.multiblock.CustomParallelMultiblockMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.level.material.Fluid;

import org.jetbrains.annotations.Nullable;

public final class ColdIceFreezerMachine extends CustomParallelMultiblockMachine {

    private static final Fluid ICE = GTMaterials.Ice.getFluid();

    public ColdIceFreezerMachine(IMachineBlockEntity holder) {
        super(holder, true, m -> 64);
    }

    private boolean inputFluid() {
        return inputFluid(ICE, (1 << Math.max(0, getTier() - 2)) * 10);
    }

    @Override
    public boolean onWorking() {
        if (getOffsetTimer() % 20 == 0 && !inputFluid()) getRecipeLogic().setProgress(0);
        return super.onWorking();
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        return super.beforeWorking(recipe) && inputFluid();
    }
}
