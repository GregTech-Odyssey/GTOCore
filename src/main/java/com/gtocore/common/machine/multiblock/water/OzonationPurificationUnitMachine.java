package com.gtocore.common.machine.multiblock.water;

import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class OzonationPurificationUnitMachine extends WaterPurificationUnitMachine implements IExplosionMachine {

    private static final Fluid Ozone = GTOMaterials.Ozone.getFluid();

    public OzonationPurificationUnitMachine(MetaMachineBlockEntity holder) {
        super(holder, 2);
    }

    @Override
    long before() {
        eut = 0;
        long[] a = getFluidAmount(WaterPurificationPlantMachine.GradePurifiedWater1, Ozone);
        long ozoneCount = a[1];
        if (ozoneCount > 1024000) {
            inputFluid(Ozone, ozoneCount);
            doExplosion(10);
        } else {
            long inputCount = Math.min(parallel(), Math.min(a[0], ozoneCount * 10000));
            if (inputCount > 0) {
                long outputCount = inputCount * 9 / 10;
                RecipeBuilder builder = getRecipeBuilder();
                builder.duration(WaterPurificationPlantMachine.DURATION).inputFluids(Ozone, inputCount / 10000).inputFluids(WaterPurificationPlantMachine.GradePurifiedWater1, inputCount);
                if (Math.random() * 100 <= getChance(outputCount / 10, ozoneCount)) {
                    builder.outputFluids(WaterPurificationPlantMachine.GradePurifiedWater2, outputCount);
                } else {
                    builder.outputFluids(WaterPurificationPlantMachine.GradePurifiedWater1, outputCount);
                }
                recipe = builder.buildRawRecipe();
                if (RecipeRunner.matchRecipe(this, recipe)) {
                    calculateVoltage(inputCount);
                }
            }
        }
        return eut;
    }

    private int getChance(long count, long ozoneCount) {
        int a = (int) (80 * Math.log(1 + ozoneCount / 10000.0) / Math.log(103.0));
        if (inputFluid(WaterPurificationPlantMachine.GradePurifiedWater3, count / 4)) {
            return a + 20;
        } else if (inputFluid(WaterPurificationPlantMachine.GradePurifiedWater2, count)) {
            return a + 15;
        }
        return a;
    }
}
