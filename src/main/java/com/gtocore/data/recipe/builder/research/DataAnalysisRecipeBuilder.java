package com.gtocore.data.recipe.builder.research;

import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

import static com.gregtechceu.gtceu.api.GTValues.LuV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gtocore.common.data.GTORecipeTypes.DATA_ANALYSIS_RECIPES;
import static com.gtocore.data.recipe.builder.research.ExResearchManager.*;

public final class DataAnalysisRecipeBuilder {

    // 数据分析
    public static DataAnalysisRecipeBuilder buildRecipe() {
        return new DataAnalysisRecipeBuilder();
    }

    private int inputData = 0;
    private int[] outputData = {};
    private int[] outputWeights = {};
    private int errorWeight = 2000;

    private ItemStack catalyst;
    private long eut = VA[LuV];
    private int cwut = 0;
    private int totalCWU = 0;

    public DataAnalysisRecipeBuilder catalyst(ItemStack catalyst) {
        this.catalyst = catalyst;
        return this;
    }

    public DataAnalysisRecipeBuilder inputData(int data) {
        this.inputData = data;
        return this;
    }

    public DataAnalysisRecipeBuilder outputData(int data, int weight) {
        if (outputData.length > 5) return this;
        if (weight < 0) throw new IllegalArgumentException("Output weights cannot be negative");
        this.outputData = Arrays.copyOf(this.outputData, this.outputData.length + 1);
        this.outputData[this.outputData.length - 1] = data;
        this.outputWeights = Arrays.copyOf(this.outputWeights, this.outputWeights.length + 1);
        this.outputWeights[this.outputWeights.length - 1] = weight;
        return this;
    }

    public DataAnalysisRecipeBuilder errorWeight(int weight) {
        if (weight < 0) throw new IllegalArgumentException("错误数据权重不能为负数");
        this.errorWeight = weight;
        return this;
    }

    public DataAnalysisRecipeBuilder EUt(long eut) {
        this.eut = eut;
        return this;
    }

    public DataAnalysisRecipeBuilder CWUt(int cwut) {
        this.cwut = cwut;
        this.totalCWU = cwut * 800;
        return this;
    }

    public DataAnalysisRecipeBuilder CWUt(int cwut, int totalCWU) {
        this.cwut = cwut;
        this.totalCWU = totalCWU;
        return this;
    }

    public void save() {
        if (cwut > totalCWU) throw new IllegalStateException("Total CWU cannot be greater than CWU/t!");
        if (catalyst == null) throw new IllegalStateException("Catalyst input required");
        DataCrystal dataCrystal = scanningMap.get(inputData);
        if (dataCrystal == null) throw new IllegalStateException("Unknown input items");
        int crystalTire = dataCrystal.tier();
        if (crystalTire < 0 || crystalTire > 5) throw new IllegalStateException("DataCrystal Out of index");

        int totalWeight = errorWeight;
        for (int w : outputWeights) totalWeight += w;
        int[] outputChances = new int[outputWeights.length];
        int sumNormalized = 0;
        for (int i = 0; i < outputWeights.length; i++) {
            outputChances[i] = (int) (((double) outputWeights[i] / totalWeight) * 10000);
            sumNormalized += outputChances[i];
        }
        int errorChance = (int) (((double) errorWeight / totalWeight) * 10000);
        int difference = 10000 - sumNormalized - errorChance;
        if (difference != 0) errorChance += difference;

        var build = DATA_ANALYSIS_RECIPES.recipeBuilder(dataCrystal.data());
        build
                .notConsumable(catalyst)
                .inputItems(EmptyDataCrystalList.get(crystalTire))
                .notConsumable(getDataCrystal(inputData));
        for (int i = 0; i < outputData.length; i++) build.chancedOutput(getDataCrystal(outputData[i]), outputChances[i], 0);
        build
                .chancedOutput(ErrorDataCrystalList.get(crystalTire), errorChance, 0)
                .EUt(eut)
                .CWUt(cwut)
                .totalCWU(totalCWU)
                .save();
    }
}
