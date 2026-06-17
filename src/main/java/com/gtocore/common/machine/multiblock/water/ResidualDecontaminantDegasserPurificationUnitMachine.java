package com.gtocore.common.machine.multiblock.water;

import com.gtocore.common.machine.multiblock.part.IndicatorHatchPartMachine;

import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;

import com.gto.datasynclib.annotations.SaveToDisk;
import com.gto.datasynclib.util.holder.IntHolder;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class ResidualDecontaminantDegasserPurificationUnitMachine extends WaterPurificationUnitMachine {

    private static final List<FluidStack> FLUIDS = List.of(
            GTMaterials.Helium.getFluid(FluidStorageKeys.LIQUID, 10000),
            GTMaterials.Helium.getFluid(10000),
            GTMaterials.SamariumIronArsenicOxide.getFluid(1000),
            GTMaterials.Neon.getFluid(8000),
            GTMaterials.IndiumTinBariumTitaniumCuprate.getFluid(1000),
            GTMaterials.Argon.getFluid(6000),
            GTMaterials.UraniumRhodiumDinaquadide.getFluid(1000),
            GTMaterials.Krypton.getFluid(4000),
            GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide.getFluid(1000),
            GTMaterials.Xenon.getFluid(2000),
            GTMaterials.RutheniumTriniumAmericiumNeutronate.getFluid(1000),
            GTMaterials.Neutronium.getFluid(2000));

    @SaveToDisk
    private long inputCount;

    @SaveToDisk
    private boolean successful;

    @SaveToDisk
    private boolean failed;

    @SaveToDisk
    private FluidStack fluidStack;

    private IndicatorHatchPartMachine indicatorHatchPartMachine;

    public ResidualDecontaminantDegasserPurificationUnitMachine(MetaMachineBlockEntity holder) {
        super(holder, 64);
    }

    @Override
    public void customText(List<Component> textList) {
        super.customText(textList);
        if (getRecipeLogic().isWorking()) {
            textList.add(Component.translatable("gtocore.machine.residual_decontaminant_degasser_purification_unit.fluids", fluidStack.getDisplayName()));
            textList.add(Component.translatable("gtceu.jei.ore_vein_diagram.chance", (successful && !failed) ? 100 : 0));
        }
    }

    @Override
    public void onPartScan(IMultiPart part) {
        super.onPartScan(part);
        if (indicatorHatchPartMachine == null && part instanceof IndicatorHatchPartMachine indicatorHatch) {
            indicatorHatchPartMachine = indicatorHatch;
        }
    }

    @Override
    public void onStructureInvalid() {
        super.onStructureInvalid();
        indicatorHatchPartMachine = null;
    }

    @Override
    public void onWorking() {
        super.onWorking();
        if (!failed && getOffsetTimer() % 20 == 0) {
            IntHolder nonEmpty = new IntHolder();
            fastForEachFluids(true, (stack, amount) -> {
                if (stack.getFluid() == WaterPurificationPlantMachine.GradePurifiedWater6) return;
                nonEmpty.value++;
                if (!fluidStack.isEmpty() && fluidStack.getFluid() == stack.getFluid() && fluidStack.getAmount() <= amount) {
                    successful = true;
                } else {
                    failed = true;
                }
                inputFluid(stack.getFluid(), amount);
            });
            if (fluidStack.isEmpty() && nonEmpty.value == 0) successful = true;
        }
    }

    @Override
    public void afterWorking() {
        super.afterWorking();
        indicatorHatchPartMachine.setRedstoneSignalOutput(0);
        if (successful && !failed) outputFluid(WaterPurificationPlantMachine.GradePurifiedWater7, inputCount * 9 / 10);
    }

    @Override
    long prepareRecipe(RecipeHandlerUnit unit) {
        eut = 0;
        successful = false;
        failed = false;
        inputCount = Math.min(parallel(), unit.getFluidAmount(true, WaterPurificationPlantMachine.GradePurifiedWater6)[0]);
        if (inputCount > 0) {
            recipe = getRecipeBuilder().duration(WaterPurificationPlantMachine.DURATION).inputFluids(WaterPurificationPlantMachine.GradePurifiedWater6, inputCount).buildRawRecipe();
            if (matchRecipe(unit, recipe)) {
                indicatorHatchPartMachine.setRedstoneSignalOutput((int) (Math.random() * 15));
                if (indicatorHatchPartMachine.getRedstoneSignalOutput() == 13 || indicatorHatchPartMachine.getRedstoneSignalOutput() == 15) {
                    fluidStack = FLUIDS.get(11);
                } else if (indicatorHatchPartMachine.getRedstoneSignalOutput() == 12 || indicatorHatchPartMachine.getRedstoneSignalOutput() == 14) {
                    fluidStack = FluidStack.EMPTY;
                } else {
                    fluidStack = FLUIDS.get(indicatorHatchPartMachine.getRedstoneSignalOutput());
                }
                calculateVoltage(inputCount);
            }
        }
        return eut;
    }
}
