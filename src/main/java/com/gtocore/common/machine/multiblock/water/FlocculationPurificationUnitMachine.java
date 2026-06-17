package com.gtocore.common.machine.multiblock.water;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;

import com.gto.datasynclib.annotations.SaveToDisk;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class FlocculationPurificationUnitMachine extends WaterPurificationUnitMachine {

    private static final Fluid PolyAluminiumChloride = GTOMaterials.PolyAluminiumChloride.getFluid();
    private static final Fluid FlocculationWasteSolution = GTOMaterials.FlocculationWasteSolution.getFluid();

    @SaveToDisk
    private long chance;

    @SaveToDisk
    private long inputCount;

    @SaveToDisk
    private long outputCount;

    public FlocculationPurificationUnitMachine(MetaMachineBlockEntity holder) {
        super(holder, 4);
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        if (getRecipeLogic().isWorking()) {
            textList.add(Component.translatable("gtceu.jei.ore_vein_diagram.chance", Math.min(chance, 100)));
        }
    }

    @Override
    public void onWorking() {
        super.onWorking();
        if (getOffsetTimer() % 20 == 0) {
            long amount = getFluidAmount(true, PolyAluminiumChloride)[0];
            if (inputFluid(PolyAluminiumChloride, amount)) {
                outputCount += amount;
                if (amount % 100000 == 0) {
                    if (chance < 100) chance += amount / 10000;
                } else {
                    chance = chance * (1L << (-10 * Math.abs((amount - 100000) / 100000)));
                }
            }
        }
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        outputFluid(FlocculationWasteSolution, outputCount);
        long outputCount = inputCount * 9 / 10;
        if (Math.random() * 100 <= chance) outputFluid(WaterPurificationPlantMachine.GradePurifiedWater3, outputCount);
        else outputFluid(WaterPurificationPlantMachine.GradePurifiedWater2, outputCount);
    }

    @Override
    long prepareRecipe(RecipeHandlerUnit unit) {
        eut = 0;
        chance = 0;
        outputCount = 0;
        inputCount = Math.min(parallel(), unit.getFluidAmount(true, WaterPurificationPlantMachine.GradePurifiedWater2)[0]);
        if (inputCount > 0) {
            recipe = getRecipeBuilder().duration(WaterPurificationPlantMachine.DURATION).inputFluids(WaterPurificationPlantMachine.GradePurifiedWater2, inputCount).buildRawRecipe();
            if (matchRecipe(unit, recipe)) {
                calculateVoltage(inputCount);
            }
        }
        return eut;
    }
}
