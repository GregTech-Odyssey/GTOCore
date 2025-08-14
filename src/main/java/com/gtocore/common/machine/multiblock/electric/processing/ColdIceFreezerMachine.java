package com.gtocore.common.machine.multiblock.electric.processing;

import com.gtolib.api.machine.multiblock.CustomParallelMultiblockMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

public final class ColdIceFreezerMachine extends CustomParallelMultiblockMachine {

    private static final FluidStack ICE = GTMaterials.Ice.getFluid(1);

    public ColdIceFreezerMachine(MetaMachineBlockEntity holder) {
        super(holder, true, m -> 64);
    }

    private boolean inputFluid() {
        if (inputFluid(ICE.getRawFluid(), (1L << Math.max(0, getTier() - 2)) * 10L)) {
            return true;
        }
        getEnhancedRecipeLogic().gtolib$setIdleReason(Component.translatable("gtceu.recipe_logic.insufficient_in").append(": ").append(ICE.getDisplayName()));
        return false;
    }

    @Override
    public boolean onWorking() {
        if (getOffsetTimer() % 20 == 0 && !inputFluid()) getRecipeLogic().setProgress(0);
        return super.onWorking();
    }

    @Override
    protected boolean beforeWorking(@Nullable Recipe recipe) {
        return super.beforeWorking(recipe) && inputFluid();
    }
}
