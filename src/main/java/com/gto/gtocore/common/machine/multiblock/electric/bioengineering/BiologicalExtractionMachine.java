package com.gto.gtocore.common.machine.multiblock.electric.bioengineering;

import com.gto.gtocore.api.machine.multiblock.CrossRecipeMultiblockMachine;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.capability.recipe.FluidRecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.ItemRecipeCapability;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;

import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;

import com.enderio.base.common.init.EIOFluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public final class BiologicalExtractionMachine extends CrossRecipeMultiblockMachine {

    private static final FluidStack NUTRIENT_DISTILLATION = new FluidStack(EIOFluids.NUTRIENT_DISTILLATION.getSource(), 1000);
    private static final FluidStack CLOUD_SEED_CONCENTRATED = new FluidStack(EIOFluids.CLOUD_SEED_CONCENTRATED.getSource(), 1000);
    private static final FluidStack FIRE_WATER = new FluidStack(EIOFluids.FIRE_WATER.getSource(), 1000);
    private static final FluidStack VAPOR_OF_LEVITY = new FluidStack(EIOFluids.VAPOR_OF_LEVITY.getSource(), 1000);

    private int redstoneSignalOutput;

    public BiologicalExtractionMachine(IMachineBlockEntity holder) {
        super(holder, false, true, MachineUtils::getHatchParallel);
    }

    @Override
    protected GTRecipe modifyRecipe(@NotNull GTRecipe recipe) {
        if (getRecipeLogic().getTotalContinuousRunningTime() < 400) {
            recipe.outputs.remove(ItemRecipeCapability.CAP);
            recipe.outputs.remove(FluidRecipeCapability.CAP);
            return recipe;
        } else {
            return super.modifyRecipe(recipe);
        }
    }

    @Override
    public boolean onWorking() {
        if (super.onWorking()) {
            if (redstoneSignalOutput > 9) {
                redstoneSignalOutput--;
                if (redstoneSignalOutput == 9) {
                    redstoneSignalOutput = 0;
                    updateSignal();
                }
            }
            if (getRecipeLogic().getProgress() % 20 == 0) {
                if (MachineUtils.inputFluid(this, NUTRIENT_DISTILLATION)) {
                    redstoneSignalOutput = 15;
                    updateSignal();
                } else {
                    return false;
                }
            }
            return switch ((int) getRecipeLogic().getTotalContinuousRunningTime()) {
                case 100 -> input(CLOUD_SEED_CONCENTRATED);
                case 300 -> input(FIRE_WATER);
                case 400 -> input(VAPOR_OF_LEVITY);
                default -> true;
            };
        }
        return false;
    }

    private boolean input(FluidStack stack) {
        AtomicBoolean success = new AtomicBoolean(false);
        AtomicBoolean failed = new AtomicBoolean(false);
        MachineUtils.forEachInputFluids(this, fluidStack -> {
            if (fluidStack.getFluid() != NUTRIENT_DISTILLATION.getFluid()) {
                if (fluidStack.getFluid() == stack.getFluid()) {
                    if (fluidStack.getAmount() >= stack.getAmount()) {
                        MachineUtils.inputFluid(this, stack);
                        success.set(true);
                    }
                } else {
                    failed.set(true);
                    return true;
                }
            }
            return false;
        });
        return success.get() && !failed.get();
    }

    @Override
    public void customText(@NotNull List<Component> textList) {
        super.customText(textList);
        textList.add(Component.translatable("gtocore.machine.total_time", getRecipeLogic().getTotalContinuousRunningTime()));
        if (!isActive()) return;
        if (getRecipeLogic().getTotalContinuousRunningTime() < 100) {
            textList.add(Component.translatable("gtocore.machine.need", CLOUD_SEED_CONCENTRATED.getDisplayName()));
        } else if (getRecipeLogic().getTotalContinuousRunningTime() < 300) {
            textList.add(Component.translatable("gtocore.machine.need", FIRE_WATER.getDisplayName()));
        } else if (getRecipeLogic().getTotalContinuousRunningTime() < 400) {
            textList.add(Component.translatable("gtocore.machine.need", VAPOR_OF_LEVITY.getDisplayName()));
        }
    }

    @Override
    public int getOutputSignal(@Nullable Direction side) {
        if (side == getFrontFacing().getOpposite()) {
            return redstoneSignalOutput;
        }
        return 0;
    }

    @Override
    public boolean canConnectRedstone(@NotNull Direction side) {
        return side == getFrontFacing();
    }
}
