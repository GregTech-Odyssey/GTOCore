package com.gto.gtocore.mixin.gtm;

import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.utils.FluidUtils;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler;
import com.gregtechceu.gtceu.api.machine.trait.NotifiableFluidTank;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.RecipeCondition;
import com.gregtechceu.gtceu.common.recipe.condition.RockBreakerCondition;
import com.gregtechceu.gtceu.utils.GTUtil;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.List;

@Mixin(RockBreakerCondition.class)
public abstract class RockBreakerConditionMixin extends RecipeCondition {

    /**
     * @author .
     * @reason .
     */
    @Override
    @Overwrite(remap = false)
    public boolean testCondition(@NotNull GTRecipe recipe, @NotNull RecipeLogic recipeLogic) {
        Fluid fluidA = FluidUtils.getFluid(recipe.data.getString("fluidA"));
        Fluid fluidB = FluidUtils.getFluid(recipe.data.getString("fluidB"));
        boolean hasFluidA = false, hasFluidB = false;
        if (recipeLogic.machine instanceof ElectricMultiblockMachine MMachine) {
            List<IRecipeHandler<?>> handlers = MMachine.getCapabilitiesFlat(IO.IN, FluidRecipeCapability.CAP);
            for (IRecipeHandler<?> handler : handlers) {
                if (handler instanceof NotifiableFluidTank tank) {
                    if (tank.getFluidInTank(0).getFluid() == fluidA) hasFluidA = true;
                    if (tank.getFluidInTank(0).getFluid() == fluidB) hasFluidB = true;
                    if (hasFluidA || hasFluidB) break;
                }
            }
        } else {
            Level level = recipeLogic.machine.self().getLevel();
            BlockPos pos = recipeLogic.machine.self().getPos();
            for (Direction side : GTUtil.DIRECTIONS) {
                if (side.getAxis() != Direction.Axis.Y) {
                    var fluid = level.getFluidState(pos.relative(side));
                    if (fluid.getType() == fluidA) hasFluidA = true;
                    if (fluid.getType() == fluidB) hasFluidB = true;
                }
            }
        }
        return hasFluidA && hasFluidB;
    }
}
