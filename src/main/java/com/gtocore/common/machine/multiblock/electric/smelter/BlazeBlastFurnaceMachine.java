package com.gtocore.common.machine.multiblock.electric.smelter;

import com.gtolib.api.machine.multiblock.CoilCustomParallelMultiblockMachine;
import com.gtolib.api.recipe.Recipe;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

public final class BlazeBlastFurnaceMachine extends CoilCustomParallelMultiblockMachine {

    private static final FluidStack BLAZE = GTMaterials.Blaze.getFluid(1);

    public BlazeBlastFurnaceMachine(MetaMachineBlockEntity holder) {
        super(holder, true, true, true, m -> 64);
    }

    private boolean inputFluid() {
        if (inputFluid(BLAZE.getRawFluid(), (1L << Math.max(0, getTier() - 2)) * 10L)) {
            return true;
        }
        getEnhancedRecipeLogic().gtolib$setIdleReason(Component.translatable("gtceu.recipe_logic.insufficient_in").append(": ").append(BLAZE.getDisplayName()));
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
