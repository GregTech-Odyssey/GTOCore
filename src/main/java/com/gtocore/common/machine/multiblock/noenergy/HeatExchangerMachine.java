package com.gtocore.common.machine.multiblock.noenergy;

import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.GTORecipeDataKeys;

import com.gtolib.api.machine.multiblock.NoEnergyMultiblockMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.api.recipe.modifier.ParallelLogic;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class HeatExchangerMachine extends NoEnergyMultiblockMachine implements IExplosionMachine {

    private static final Fluid Steam = GTMaterials.Steam.getFluid();
    private static final Fluid HighPressureSteam = GTOMaterials.HighPressureSteam.getFluid();
    private static final Fluid SupercriticalSteam = GTOMaterials.SupercriticalSteam.getFluid();
    private static final Fluid DistilledWater = GTMaterials.DistilledWater.getFluid();

    public HeatExchangerMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    @Persisted
    private long hs;

    @Persisted
    private boolean water;

    @Nullable
    @Override
    public GTRecipe getRealRecipe(@NotNull RecipeHandlerUnit unit, @NotNull GTRecipe recipe) {
        water = recipe.fluidInputs.getFirst().inner.getFluid() == Fluids.WATER;
        var result = ParallelLogic.accurateParallel(this, unit, getRecipeBuilder()
                .inputFluids(recipe.fluidInputs.getFirst())
                .outputFluids(recipe.fluidOutputs.getFirst())
                .duration(200)
                .buildRawRecipe(), Integer.MAX_VALUE);
        if (result == null) return null;
        hs = result.parallels * recipe.data.getLong(GTORecipeDataKeys.EU) / 2;
        if (unit.inputFluid(water ? Fluids.WATER : DistilledWater, hs / 40)) {
            return result;
        } else {
            doExplosion(Math.min(10, hs / 10000));
        }
        return null;
    }

    @Override
    public void onRecipeFinish() {
        super.onRecipeFinish();
        if (hs != 0) {
            if (getRecipeLogic().getTotalContinuousRunningTime() > 800) {
                if (water) {
                    outputFluid(HighPressureSteam, hs);
                } else {
                    outputFluid(SupercriticalSteam, hs >> 2);
                }
            } else {
                if (water) {
                    outputFluid(Steam, hs << 2);
                } else {
                    outputFluid(HighPressureSteam, hs);
                }
            }
        }
        hs = 0;
    }
}
