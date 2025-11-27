package com.gtocore.data.recipe.builder.research;

import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

import static com.gregtechceu.gtceu.api.GTValues.LuV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gtocore.common.data.GTORecipeTypes.DATA_INTEGRATION_RECIPES;
import static com.gtocore.data.recipe.builder.research.ExResearchManager.*;

public final class DataIntegrationRecipeBuilder {

    // 数据统合
    public static DataIntegrationRecipeBuilder buildRecipe() {
        return new DataIntegrationRecipeBuilder();
    }

    private int[] inputData = {};
    private int outputData;
    private int chanced;
    private int errorWeight = 2000;

    private ItemStack catalyst1;
    private ItemStack catalyst2;
    private long eut = VA[LuV];
    private int cwut = 0;
    private int totalCWU = 0;

    public DataIntegrationRecipeBuilder catalyst1(ItemStack catalyst) {
        this.catalyst1 = catalyst;
        return this;
    }

    public DataIntegrationRecipeBuilder catalyst2(ItemStack catalyst) {
        this.catalyst2 = catalyst;
        return this;
    }

    public DataIntegrationRecipeBuilder inputData(int data) {
        if (inputData.length > 10) return this;
        inputData = Arrays.copyOf(inputData, inputData.length + 1);
        inputData[inputData.length - 1] = data;
        return this;
    }

    public DataIntegrationRecipeBuilder outputData(int data, int chanced) {
        this.outputData = data;
        this.chanced = chanced;
        return this;
    }

    public DataIntegrationRecipeBuilder errorWeight(int weight) {
        if (weight < 0) throw new IllegalArgumentException("错误数据权重不能为负数");
        this.errorWeight = weight;
        return this;
    }

    public DataIntegrationRecipeBuilder EUt(long eut) {
        this.eut = eut;
        return this;
    }

    public DataIntegrationRecipeBuilder CWUt(int cwut) {
        this.cwut = cwut;
        this.totalCWU = cwut * 800;
        return this;
    }

    public DataIntegrationRecipeBuilder CWUt(int cwut, int totalCWU) {
        this.cwut = cwut;
        this.totalCWU = totalCWU;
        return this;
    }

    public void save() {
        if (cwut > totalCWU) throw new IllegalStateException("Total CWU cannot be greater than CWU/t!");
        if (catalyst1 == null || catalyst2 == null) throw new IllegalStateException("Catalyst input required");
        if (inputData == null) throw new IllegalStateException("Missing input items");
        DataCrystal dataCrystal = analyzeMap.get(outputData);
        if (dataCrystal == null) throw new IllegalStateException("Unknown output items");
        int crystalTire = dataCrystal.tier();

        int originalTotalWeight = chanced + errorWeight;
        int normalizedOutput = (int) (((double) chanced / originalTotalWeight) * 10000);
        int normalizedError = (int) (((double) errorWeight / originalTotalWeight) * 10000);
        int difference = 10000 - normalizedOutput - normalizedError;
        if (difference != 0) normalizedError += difference;

        var build = DATA_INTEGRATION_RECIPES.recipeBuilder(dataCrystal.data());
        build
                .notConsumable(catalyst1)
                .notConsumable(catalyst2)
                .inputItems(EmptyDataCrystalList.get(crystalTire));
        for (int inputAnalyzeDatum : inputData) build.notConsumable(getDataCrystal(inputAnalyzeDatum));
        build
                .chancedOutput(getDataCrystal(outputData), normalizedOutput, 0)
                .chancedOutput(ErrorDataCrystalList.get(crystalTire), normalizedError, 0)
                .EUt(eut)
                .CWUt(cwut)
                .totalCWU(totalCWU)
                .save();
    }
}
