package com.gto.gtocore.api.machine.feature.multiblock;

import com.gto.gtocore.config.GTOConfig;

import com.gregtechceu.gtceu.api.machine.feature.IMachineFeature;
import com.gregtechceu.gtceu.api.machine.feature.IRecipeLogicMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.trait.RecipeHandlerList;
import com.gregtechceu.gtceu.api.machine.trait.RecipeLogic;
import com.gregtechceu.gtceu.api.sound.SoundEntry;

/**
 * 该接口提供了一系列默认方法，用于处理多方块机器的常见操作，如配方完成、内容变化、声音获取、部件扫描以及阻尼处理等。
 */
public interface IEnhancedMultiblockMachine extends IMachineFeature {

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
        } else if (((IRecipeLogicMachine) self()).dampingWhenWaiting()) {
            if (recipeLogic.getProgress() > 1) recipeLogic.setProgress(1);
        }
    }
}
