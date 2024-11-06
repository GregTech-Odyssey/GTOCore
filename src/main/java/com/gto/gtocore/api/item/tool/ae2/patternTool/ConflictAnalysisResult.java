package com.gto.gtocore.api.item.tool.ae2.patternTool;

import lombok.Data;

import java.util.List;

@Data
public class ConflictAnalysisResult {

    boolean conflict;
    List<String> matchPool;
    List<String> storagePool;
    ConflictAnalysisObj conflictAnalysisObj;

    public ConflictAnalysisResult(boolean conflict, List<String> matchPool, List<String> storagePool, ConflictAnalysisObj conflictAnalysisObj) {
        this.conflict = conflict; // 程序不出错的情况下一定为true，仅供检验用
        this.matchPool = matchPool; // 冲突配方的材料池，包含物品和流体
        this.storagePool = storagePool; // 同电路同机器类型的所有配方的材料池，包含物品和流体
        this.conflictAnalysisObj = conflictAnalysisObj; // 冲突配方的对象，可以通过这个获取原recipe
    }

    public StringBuilder exportToPrint(StringBuilder sb) {
        sb.append("\n配方ID：").append(conflictAnalysisObj.recipe.id.toString()).append(" --------- 电路：").append(conflictAnalysisObj.CIRCUIT).append("\n>>材料：\n");
        RecipeStackHelper.getInputItemStacksFromRecipe(conflictAnalysisObj.recipe).forEach(itemStack -> sb.append("物品：").append(RecipeStackHelper.getItemTranslatedName(itemStack)));
        RecipeStackHelper.getInputFluidStacksFromRecipe(conflictAnalysisObj.recipe).forEach(fluidStack -> sb.append("流体：").append(RecipeStackHelper.getFluidTranslatedName(fluidStack)));
        sb.append("\n>>成品：\n");
        RecipeStackHelper.getOutputItemStacksFromRecipe(conflictAnalysisObj.recipe).forEach(itemStack -> sb.append("物品：").append(RecipeStackHelper.getItemTranslatedName(itemStack)));
        RecipeStackHelper.getOutputFluidStacksFromRecipe(conflictAnalysisObj.recipe).forEach(fluidStack -> sb.append("流体：").append(RecipeStackHelper.getFluidTranslatedName(fluidStack)));
        return sb;
    }
}
