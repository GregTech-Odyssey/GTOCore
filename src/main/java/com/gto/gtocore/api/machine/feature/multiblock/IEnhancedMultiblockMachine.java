package com.gto.gtocore.api.machine.feature.multiblock;

import com.gto.gtocore.config.GTOConfig;
import com.gto.gtocore.utils.MachineUtils;

import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.sound.SoundEntry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Predicate;

public interface IEnhancedMultiblockMachine extends IRecipeLogicMachine {

    /**
     * 当配方真正完成时调用，不同于afterWorking()。
     */
    default void onRecipeFinish() {}

    /**
     * 当内容发生变化时调用。
     */
    default void onContentChanges(RecipeHandlerList handlerList) {}

    /**
     * 覆盖默认声音条目。
     */
    default SoundEntry getSound() {
        return null;
    }

    /**
     * 在结构成型时，当一个多方块仓被扫描时调用。
     */
    default void onPartScan(IMultiPart part) {}

    /**
     * 多方块机器跳电时调用。
     */
    default void doDamping(RecipeLogic recipeLogic) {
        if (GTOConfig.getDifficulty() == 3) {
            recipeLogic.interruptRecipe();
        } else if (((IRecipeLogicMachine) self()).regressWhenWaiting()) {
            if (recipeLogic.getProgress() > 1) recipeLogic.setProgress(1);
        }
    }

    default int checkingCircuit(boolean sum) {
        return MachineUtils.checkingCircuit(this, sum);
    }

    default int[] getItemAmount(Item... items) {
        return MachineUtils.getItemAmount(this, items);
    }

    default int[] getFluidAmount(Fluid... fluids) {
        return MachineUtils.getFluidAmount(this, fluids);
    }

    default void forEachInputItems(Predicate<ItemStack> function) {
        MachineUtils.forEachInputItems(this, function);
    }

    default void forEachInputFluids(Predicate<FluidStack> function) {
        MachineUtils.forEachInputFluids(this, function);
    }

    default boolean inputItem(ItemStack... item) {
        return MachineUtils.inputItem(this, item);
    }

    default boolean outputItem(ItemStack... item) {
        return MachineUtils.outputItem(this, item);
    }

    default boolean notConsumableCircuit(int configuration) {
        return MachineUtils.notConsumableCircuit(this, configuration);
    }

    default boolean inputFluid(Fluid fluid, int amount) {
        return MachineUtils.inputFluid(this, fluid, amount);
    }

    default boolean inputFluid(FluidStack... fluid) {
        return MachineUtils.inputFluid(this, fluid);
    }

    default boolean outputFluid(Fluid fluid, int amount) {
        return MachineUtils.outputFluid(this, fluid, amount);
    }

    default boolean outputFluid(FluidStack... fluid) {
        return MachineUtils.outputFluid(this, fluid);
    }
}
