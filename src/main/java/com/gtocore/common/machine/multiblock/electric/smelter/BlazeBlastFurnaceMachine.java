package com.gtocore.common.machine.multiblock.electric.smelter;

import com.gtolib.api.machine.multiblock.CoilCustomParallelMultiblockMachine;
import com.gtolib.api.recipe.GTORecipeModifiers;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.ActionResult;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

public final class BlazeBlastFurnaceMachine extends CoilCustomParallelMultiblockMachine {

    private static final FluidStack BLAZE = GTMaterials.Blaze.getFluid(1);

    public BlazeBlastFurnaceMachine(MetaMachineBlockEntity holder) {
        super(holder, true, true, m -> 64);
    }

    private boolean inputFluid() {
        if (inputFluid(BLAZE.getRawFluid(), (1L << Math.max(0, getTier() - 2)) * 10L)) {
            return true;
        }
        setIdleReason(() -> ActionResult.failInsufficientIn(BLAZE.getDisplayName()).reason());
        return false;
    }

    @Override
    public GTRecipe getRealRecipe(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        recipe.duration = recipe.duration / 2;
        recipe = ParallelLogic.accurateParallel(this, unit, recipe, getParallel());
        if (recipe == null) return null;
        return GTORecipeModifiers.UPGRADE_EBF_OVERCLOCK.applyModifier(this, unit, recipe);
    }

    @Override
    public boolean handleTickRecipe(GTRecipe recipe) {
        if (getOffsetTimer() % 20 == 0 && !inputFluid()) return false;
        return super.handleTickRecipe(recipe);
    }

    @Override
    public boolean handleRecipeInput(RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        return inputFluid() && super.handleRecipeInput(unit, recipe);
    }
}
