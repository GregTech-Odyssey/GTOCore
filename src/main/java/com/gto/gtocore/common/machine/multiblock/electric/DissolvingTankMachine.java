package com.gto.gtocore.common.machine.multiblock.electric;

import com.gto.gtocore.utils.MachineUtil;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableElectricMultiblockMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.OverclockingLogic;
import com.gregtechceu.gtceu.api.recipe.RecipeHelper;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.logic.OCParams;
import com.gregtechceu.gtceu.api.recipe.logic.OCResult;
import com.gregtechceu.gtceu.common.data.GTRecipeModifiers;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
public class DissolvingTankMachine extends WorkableElectricMultiblockMachine {

    public DissolvingTankMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public boolean beforeWorking(@Nullable GTRecipe recipe) {
        boolean value = super.beforeWorking(recipe);
        if (value) {
            Level level = getLevel();
            if (level == null) return true;
            BlockPos pos = MachineUtil.getOffsetPos(2, 1, getFrontFacing(), getPos());
            if (level.getFluidState(pos) == Fluids.WATER.defaultFluidState()) return true;
            for (int i = -1; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    for (int k = -1; k < 2; k++) {
                        level.setBlockAndUpdate(pos.offset(i, j, k), Fluids.WATER.defaultFluidState().createLegacyBlock());
                    }
                }
            }
        }
        return value;
    }

    @Override
    public void afterWorking() {
        removeWater();
    }

    @Override
    public void onStructureInvalid() {
        removeWater();
        super.onStructureInvalid();
    }

    private void removeWater() {
        Level level = getLevel();
        if (level == null) return;
        BlockPos pos = MachineUtil.getOffsetPos(2, 1, getFrontFacing(), getPos());
        for (int i = -1; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = -1; k < 2; k++) {
                    level.setBlockAndUpdate(pos.offset(i, j, k), Blocks.AIR.defaultBlockState());
                }
            }
        }
    }

    public static GTRecipe dissolvingTankOverclock(MetaMachine machine, @NotNull GTRecipe recipe, @NotNull OCParams params,
                                                   @NotNull OCResult result) {
        if (machine instanceof DissolvingTankMachine dissolvingTankMachine) {
            List<Content> fluidList = recipe.inputs.getOrDefault(FluidRecipeCapability.CAP, null);
            FluidStack fluidStack1 = FluidRecipeCapability.CAP.of(fluidList.get(0).getContent()).getStacks()[0];
            FluidStack fluidStack2 = FluidRecipeCapability.CAP.of(fluidList.get(1).getContent()).getStacks()[0];
            long[] a = MachineUtil.getFluidAmount(dissolvingTankMachine, fluidStack1.getFluid(), fluidStack2.getFluid());
            if (a[1] > 0) {
                GTRecipe recipe1 = GTRecipeModifiers.hatchParallel(machine, recipe, false, params, result);
                if (recipe1 != null) {
                    GTRecipe recipe2 = RecipeHelper.applyOverclock(OverclockingLogic.NON_PERFECT_OVERCLOCK_SUBTICK, recipe1, dissolvingTankMachine.getOverclockVoltage(), params, result);
                    if ((double) a[0] / a[1] != ((double) fluidStack1.getAmount()) / fluidStack2.getAmount()) {
                        recipe2.outputs.clear();
                    }
                    return recipe2;
                }
            }
        }
        return null;
    }
}
