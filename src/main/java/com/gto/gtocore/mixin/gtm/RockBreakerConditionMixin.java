package com.gto.gtocore.mixin.gtm;

import com.gto.gtocore.api.machine.multiblock.ElectricMultiblockMachine;
import com.gto.gtocore.utils.RegistriesUtils;

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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(RockBreakerCondition.class)
public abstract class RockBreakerConditionMixin extends RecipeCondition {

    @Inject(method = "test", at = @At("HEAD"), remap = false, cancellable = true)
    private void test(GTRecipe recipe, RecipeLogic recipeLogic, CallbackInfoReturnable<Boolean> cir) {
        Fluid fluidA = RegistriesUtils.getFluid(recipe.data.getString("fluidA"));
        Fluid fluidB = RegistriesUtils.getFluid(recipe.data.getString("fluidB"));
        boolean hasFluidA = false, hasFluidB = false;
        if (recipeLogic.machine instanceof ElectricMultiblockMachine MMachine) {
            List<IRecipeHandler<?>> handlers = MMachine.getCapabilitiesProxy().get(IO.IN, FluidRecipeCapability.CAP);
            if (handlers != null) {
                for (com.gregtechceu.gtceu.api.capability.recipe.IRecipeHandler<?> handler : handlers) {
                    if (handler instanceof NotifiableFluidTank tank) {
                        if (tank.getFluidInTank(0).getFluid() == fluidA) hasFluidA = true;
                        if (tank.getFluidInTank(0).getFluid() == fluidB) hasFluidB = true;
                    }
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
        cir.setReturnValue((hasFluidA && hasFluidB));
    }
}
