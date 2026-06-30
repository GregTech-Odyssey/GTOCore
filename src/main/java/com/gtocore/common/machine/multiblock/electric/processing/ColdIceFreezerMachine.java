package com.gtocore.common.machine.multiblock.electric.processing;

import com.gtocore.common.data.GTORecipeTypes;

import com.gtolib.api.machine.multiblock.CustomParallelMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.recipe.handler.ActionResult;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

public final class ColdIceFreezerMachine extends CustomParallelMultiblockMachine {

    private static final FluidStack ICE = GTMaterials.Ice.getFluid(1);

    public ColdIceFreezerMachine(MetaMachineBlockEntity holder) {
        super(holder, m -> 64);
    }

    private boolean inputFluid() {
        if (inputFluid(ICE.getRawFluid(), (1L << Math.max(0, getTier() - 2)) * 10L)) {
            return true;
        }
        setIdleReason(() -> ActionResult.failInsufficientIn(ICE.getDisplayName()).reason());
        return false;
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

    @Override
    public boolean recipeTypeAvailable(GTRecipeType type) {
        if (type == GTORecipeTypes.ATOMIZATION_CONDENSATION_RECIPES) {
            return formedAmount > 0;
        }
        return true;
    }
}
