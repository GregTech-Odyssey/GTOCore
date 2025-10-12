package com.gtocore.common.machine.multiblock.noenergy;

import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.machine.multiblock.NoEnergyMultiblockMachine;
import com.gtolib.api.recipe.Recipe;
import com.gtolib.api.recipe.modifier.ParallelLogic;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
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
    protected Recipe getRealRecipe(@NotNull Recipe recipe) {
        water = FluidRecipeCapability.CAP.of(recipe.inputs.get(FluidRecipeCapability.CAP).get(1).getContent()).getStacks()[0].getFluid() == Fluids.WATER;
        var result = ParallelLogic.accurateParallel(this, getRecipeBuilder()
                .inputFluids(FluidRecipeCapability.CAP.of(recipe.inputs
                        .get(FluidRecipeCapability.CAP).get(0).getContent()))
                .outputFluids(FluidRecipeCapability.CAP.of(recipe.outputs
                        .get(FluidRecipeCapability.CAP).get(0).getContent()))
                .duration(200)
                .buildRawRecipe(), Integer.MAX_VALUE);
        if (result == null) return null;
        hs = result.parallels * recipe.data.getLong("eu") / 2;
        if (inputFluid(water ? Fluids.WATER : DistilledWater, hs / 40)) {
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
