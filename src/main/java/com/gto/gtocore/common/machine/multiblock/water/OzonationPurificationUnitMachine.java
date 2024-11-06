package com.gto.gtocore.common.machine.multiblock.water;

import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.utils.MachineUtil;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.material.Fluid;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OzonationPurificationUnitMachine extends WaterPurificationUnitMachine implements IExplosionMachine {

    private static final Fluid Ozone = GTOMaterials.Ozone.getFluid();

    public OzonationPurificationUnitMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    long before() {
        eut = 0;
        long[] a = MachineUtil.getFluidAmount(this, WaterPurificationPlantMachine.GradePurifiedWater1, Ozone);
        long ozoneCount = a[1];
        if (ozoneCount > 1024000) {
            MachineUtil.inputFluid(this, FluidStack.create(Ozone, ozoneCount));
            doExplosion(10);
        }
        int inputCount = (int) Math.min(Integer.MAX_VALUE, Math.min(a[0], ozoneCount * 10000));
        int outputCount = inputCount * 9 / 10;
        GTRecipeBuilder builder = GTRecipeBuilder.ofRaw();
        builder.duration(WaterPurificationPlantMachine.DURATION).inputFluids(FluidStack.create(Ozone, inputCount / 10000)).inputFluids(FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater1, inputCount));
        if (Math.random() * 100 <= getChance(outputCount / 10, ozoneCount)) {
            builder.outputFluids(FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater2, outputCount));
        } else {
            builder.outputFluids(FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater1, outputCount));
        }
        recipe = builder.buildRawRecipe();
        if (recipe.matchRecipe(this).isSuccess()) {
            eut = inputCount * 2L;
        }
        return eut;
    }

    private int getChance(int count, long ozoneCount) {
        int a = Math.min(80, (int) (ozoneCount / 102400 * 8));
        if (MachineUtil.inputFluid(this, FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater2, count / 4))) {
            return a + 20;
        } else if (MachineUtil.inputFluid(this, FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater2, count))) {
            return a + 15;
        }
        return a;
    }
}
