package com.gto.gtocore.common.machine.multiblock.electric.processing;

import com.gto.gtocore.api.machine.multiblock.CustomParallelMultiblockMachine;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import org.jetbrains.annotations.Nullable;

public final class ColdIceFreezerMachine extends CustomParallelMultiblockMachine {

    public ColdIceFreezerMachine(IMachineBlockEntity holder) {
        super(holder, true, m -> 64);
    }

    private boolean inputFluid() {
        return MachineUtils.inputFluid(this, GTMaterials.Ice.getFluid((1 << Math.max(0, getTier() - 2)) * 10));
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
