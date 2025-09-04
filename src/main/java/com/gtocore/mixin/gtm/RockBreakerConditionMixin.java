package com.gtocore.mixin.gtm;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;
import com.gtolib.utils.FluidUtils;

import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.common.recipe.condition.RockBreakerCondition;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RockBreakerCondition.class)
public abstract class RockBreakerConditionMixin extends RecipeCondition {

    @Shadow(remap = false)
    private Fluid A;

    @Shadow(remap = false)
    private Fluid B;

    /**
     * @author .
     * @reason .
     */
    @Override
    @Overwrite(remap = false)
    public boolean testCondition(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        if (A == null || B == null) {
            A = FluidUtils.getFluid(recipe.data.getString("fluidA"));
            B = FluidUtils.getFluid(recipe.data.getString("fluidB"));
        }
        boolean hasFluidA = false, hasFluidB = false;
        if (recipeLogic.machine instanceof ElectricMultiblockMachine MMachine) {
            var as = MMachine.getFluidAmount(A, B);
            if (as[0] > 0) hasFluidA = true;
            if (as[1] > 0) hasFluidB = true;
        } else {
            for (Direction side : GTUtil.DIRECTIONS) {
                if (side.getAxis() != Direction.Axis.Y) {
                    var fluid = recipeLogic.machine.self().getNeighborFluidState(side).getType();
                    if (fluid == A) {
                        hasFluidA = true;
                    } else if (fluid == B) {
                        hasFluidB = true;
                    }
                    if (hasFluidA && hasFluidB) return true;
                }
            }
        }
        return hasFluidA && hasFluidB;
    }
}
