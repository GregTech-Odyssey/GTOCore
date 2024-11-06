package com.gto.gtocore.common.machine.multiblock.water;

import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.utils.MachineUtil;
import com.gto.gtocore.utils.NumberUtils;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ClarifierPurificationUnitMachine extends WaterPurificationUnitMachine {

    public ClarifierPurificationUnitMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    long before() {
        eut = 0;
        int inputCount = (int) Math.min(Integer.MAX_VALUE, Math.min(MachineUtil.getFluidAmount(this, Fluids.WATER)[0], MachineUtil.getItemAmount(this, GTOItems.ACTIVATED_CARBON_FILTER_MESH.get())[0] * 1000));
        int outputCount = inputCount * 9 / 10;
        int filterCount = NumberUtils.chanceOccurrences(inputCount / 1000, 5);
        GTRecipeBuilder builder = GTRecipeBuilder.ofRaw();
        builder.duration(WaterPurificationPlantMachine.DURATION).inputFluids(FluidStack.create(Fluids.WATER, inputCount));
        if (Math.random() * 100 <= getChance(outputCount / 10)) {
            builder.outputFluids(FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater1, outputCount));
        } else {
            builder.outputFluids(FluidStack.create(Fluids.WATER, outputCount));
        }
        if (filterCount > 0) {
            builder.inputItems(GTOItems.ACTIVATED_CARBON_FILTER_MESH.asStack(filterCount));
            builder.outputItems(GTOItems.USED_ACTIVATED_CARBON_FILTER_MESH.asStack(filterCount));
        }
        recipe = builder.buildRawRecipe();
        if (recipe.matchRecipe(this).isSuccess()) {
            eut = inputCount;
        }
        return eut;
    }

    private int getChance(int count) {
        if (MachineUtil.inputFluid(this, FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater4, count / 16))) {
            return 100;
        } else if (MachineUtil.inputFluid(this, FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater3, count / 4))) {
            return 95;
        } else if (MachineUtil.inputFluid(this, FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater2, count / 2))) {
            return 90;
        } else if (MachineUtil.inputFluid(this, FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater1, count))) {
            return 85;
        }
        return 70;
    }
}
