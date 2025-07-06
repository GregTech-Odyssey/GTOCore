package com.gtocore.common.machine.multiblock.water;

import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.recipe.RecipeRunner;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;

import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class FlocculationPurificationUnitMachine extends WaterPurificationUnitMachine {

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
            FlocculationPurificationUnitMachine.class, WaterPurificationUnitMachine.MANAGED_FIELD_HOLDER);

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    private static final Fluid PolyAluminiumChloride = GTOMaterials.PolyAluminiumChloride.getFluid();
    private static final Fluid FlocculationWasteSolution = GTOMaterials.FlocculationWasteSolution.getFluid();

    @Persisted
    private long chance;

    @Persisted
    private long inputCount;

    @Persisted
    private long outputCount;

    public FlocculationPurificationUnitMachine(IMachineBlockEntity holder) {
        super(holder, 4);
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        if (getRecipeLogic().isWorking()) {
            textList.add(Component.translatable("gui.enderio.sag_mill_chance", chance));
        }
    }

    @Override
    public boolean onWorking() {
        if (!super.onWorking()) return false;
        if (getOffsetTimer() % 20 == 0) {
            long amount = getFluidAmount(PolyAluminiumChloride)[0];
            if (inputFluid(PolyAluminiumChloride, amount)) {
                outputCount += amount;
                if (amount % 100000 == 0) {
                    if (chance < 100) chance += amount / 10000;
                } else {
                    chance = chance * (1L << (-10 * Math.abs((amount - 100000) / 100000)));
                }
            }
        }
        return true;
    }

    @Override
    public void onRecipeFinish() {
        super.onRecipeFinish();
        outputFluid(FlocculationWasteSolution, outputCount);
        if (Math.random() * 100 <= chance) outputFluid(WaterPurificationPlantMachine.GradePurifiedWater3, inputCount * 9 / 10);
    }

    @Override
    long before() {
        eut = 0;
        chance = 0;
        outputCount = 0;
        inputCount = Math.min(parallel(), getFluidAmount(WaterPurificationPlantMachine.GradePurifiedWater2)[0]);
        if (inputCount > 0) {
            recipe = getRecipeBuilder().duration(WaterPurificationPlantMachine.DURATION).inputFluids(WaterPurificationPlantMachine.GradePurifiedWater2, inputCount).buildRawRecipe();
            if (RecipeRunner.matchRecipe(this, recipe)) {
                calculateVoltage(inputCount);
            }
        }
        return eut;
    }
}
