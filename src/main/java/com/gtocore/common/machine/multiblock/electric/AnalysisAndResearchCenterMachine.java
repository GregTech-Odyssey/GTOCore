package com.gtocore.common.machine.multiblock.electric;

import com.gtocore.common.machine.multiblock.part.AnalyzeHolderMachine;
import com.gtocore.common.machine.multiblock.part.ResearchHolderMachine;

import com.gtolib.api.machine.multiblock.ElectricMultiblockMachine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.blockentity.MetaMachineBlockEntity;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.multiblock.MultiblockDisplayText;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
import com.gregtechceu.gtceu.api.recipe.content.Content;
import com.gregtechceu.gtceu.api.recipe.handler.IO;
import com.gregtechceu.gtceu.api.recipe.handler.RecipeHandlerUnit;

import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class AnalysisAndResearchCenterMachine extends ElectricMultiblockMachine {

    private AnalyzeHolderMachine AnalyzeHolder;
    private ResearchHolderMachine ResearchHolder;

    public AnalysisAndResearchCenterMachine(MetaMachineBlockEntity holder) {
        super(holder);
    }

    private int mode = 0;

    @Override
    public void onStructureFormed() {
        super.onStructureFormed();
        for (IMultiPart part : getParts()) {
            if (part instanceof AnalyzeHolderMachine analyzeHolder) {
                // 修改这里：检查是否面与机器方向相同
                if (analyzeHolder.getFrontFacing() != getFrontFacing()) {
                    onStructureInvalid();
                    return;
                }
                this.AnalyzeHolder = analyzeHolder;
                // 添加物品处理器（包含扫描槽、催化剂槽和数据槽）
                addHandlerList(RecipeHandlerUnit.of(IO.IN, analyzeHolder.getAsHandler()));
                mode = 1;
            }
            if (part instanceof ResearchHolderMachine researchHolder) {
                if (researchHolder.getFrontFacing() != getFrontFacing()) {
                    onStructureInvalid();
                    return;
                }
                this.ResearchHolder = researchHolder;
                addHandlerList(RecipeHandlerUnit.of(IO.IN, researchHolder.getAsHandler()));
                mode = 2;
            }
        }

        // 必须有扫描部件
        if (mode == 0) {
            onStructureInvalid();
        }
    }

    @Override
    public boolean checkPattern() {
        boolean isFormed = super.checkPattern();
        if (isFormed && mode != 0) {
            onStructureInvalid();
        }
        return isFormed;
    }

    @Override
    public void onStructureInvalid() {
        // 重置扫描部件状态
        if (AnalyzeHolder != null && mode == 1) {
            AnalyzeHolder.setLocked(false);
            AnalyzeHolder = null;
        }
        if (ResearchHolder != null && mode == 2) {
            ResearchHolder.setLocked(false);
            ResearchHolder = null;
        }
        super.onStructureInvalid();
    }

    @Override
    public boolean regressWhenWaiting() {
        return false;
    }

    @Override
    public void addDisplayText(@NotNull List<Component> textList) {
        MultiblockDisplayText.builder(textList, isFormed())
                .setWorkingStatus(recipeLogic.isWorkingEnabled(), recipeLogic.isActive())
                .setWorkingStatusKeys("gtceu.multiblock.idling", "gtceu.multiblock.work_paused",
                        "gtocore.machine.analysis")
                .addEnergyUsageLine(energyContainer)
                .addEnergyTierLine(tier)
                .addWorkingStatusLine()
                .addProgressLineOnlyPercent(recipeLogic.getProgressPercent());
    }

    @Override
    public GTRecipe getRealRecipe(@NotNull RecipeHandlerUnit unit, GTRecipe recipe) {
        // 1. 获取所有物品输出
        var itemOutputs = recipe.itemOutputs;

        // 3. 加权随机选择一个输出
        int random = GTValues.RNG.nextInt(10000);
        int cumulative = 0;
        var selectedContent = itemOutputs.getLast();
        for (var content : itemOutputs) {
            if (content.chance <= 0) continue;
            cumulative += content.chance;
            if (random <= cumulative) {
                selectedContent = content;
                break;
            }
        }

        // 3. 创建新的唯一输出列表
        if (selectedContent != null) {
            var selectedStack = selectedContent.inner;
            var newContent = new Content<>(selectedStack, 10000, 0);
            recipe.itemOutputs = Collections.singletonList(newContent);
        }

        return super.getRealRecipe(unit, recipe);
    }
}
