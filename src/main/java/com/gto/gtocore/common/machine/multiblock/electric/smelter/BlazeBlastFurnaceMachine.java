package com.gto.gtocore.common.machine.multiblock.electric.smelter;

import com.gto.gtocore.api.machine.multiblock.CoilCustomParallelMultiblockMachine;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.level.material.Fluid;

import org.jetbrains.annotations.Nullable;

public final class BlazeBlastFurnaceMachine extends CoilCustomParallelMultiblockMachine {

    private static final Fluid BLAZE = GTMaterials.Blaze.getFluid();

    public BlazeBlastFurnaceMachine(IMachineBlockEntity holder) {
        super(holder, true, true, true, m -> 64);
    }

    private boolean inputFluid() {
        return inputFluid(BLAZE, (1 << Math.max(0, getTier() - 2)) * 10);
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
