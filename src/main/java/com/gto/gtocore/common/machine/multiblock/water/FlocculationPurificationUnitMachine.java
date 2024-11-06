package com.gto.gtocore.common.machine.multiblock.water;

import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.utils.MachineUtil;

import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.data.recipe.builder.GTRecipeBuilder;

import com.lowdragmc.lowdraglib.side.fluid.FluidStack;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FlocculationPurificationUnitMachine extends WaterPurificationUnitMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(
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
    private int inputCount;

    @Persisted
    private long outputCount;

    public FlocculationPurificationUnitMachine(IMachineBlockEntity holder, Object... args) {
        super(holder, args);
    }

    @Override
    public void addDisplayText(List<Component> textList) {
        super.addDisplayText(textList);
        if (getRecipeLogic().isWorking()) {
            textList.add(Component.translatable("gtceu.gui.content.chance_1", chance));
        }
    }

    @Override
    public boolean onWorking() {
        boolean result = super.onWorking();
        if (getOffsetTimer() % 20 == 0) {
            long amount = MachineUtil.getFluidAmount(this, PolyAluminiumChloride)[0];
            if (MachineUtil.inputFluid(this, FluidStack.create(PolyAluminiumChloride, amount))) {
                outputCount += amount;
                if (amount % 100000 == 0) {
                    if (chance < 100) chance += amount / 10000;
                } else {
                    chance = (long) (chance * Math.pow(2, -10 * Math.abs((amount - 100000) / 100000)));
                }
            }
        }
        return result;
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        MachineUtil.outputFluid(this, FluidStack.create(FlocculationWasteSolution, outputCount));
        if (Math.random() * 100 <= chance) MachineUtil.outputFluid(this, FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater3, inputCount * 9L / 10));
    }

    @Override
    long before() {
        eut = 0;
        chance = 0;
        outputCount = 0;
        inputCount = (int) Math.min(Integer.MAX_VALUE, MachineUtil.getFluidAmount(this, WaterPurificationPlantMachine.GradePurifiedWater2)[0]);
        recipe = GTRecipeBuilder.ofRaw().duration(WaterPurificationPlantMachine.DURATION).inputFluids(FluidStack.create(WaterPurificationPlantMachine.GradePurifiedWater2, inputCount)).buildRawRecipe();
        if (recipe.matchRecipe(this).isSuccess()) {
            eut = inputCount * 3L;
        }
        return eut;
    }
}
