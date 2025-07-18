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
    private int outputData = 0;
    private int chanced = 10000;

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

    public DataIntegrationRecipeBuilder EUt(long eut) {
        this.eut = eut;
        return this;
    }

    public DataIntegrationRecipeBuilder CWUt(int cwut) {
        this.cwut = cwut;
        this.totalCWU = cwut * 4000;
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
        if (outputData == 0) throw new IllegalStateException("Missing output items");
        int crystalTire = ExtractDataCrystal(outputData);

        var build = DATA_INTEGRATION_RECIPES.recipeBuilder(AnalyzeMap.get(outputData));
        build
                .notConsumable(catalyst1)
                .notConsumable(catalyst2)
                .inputItems(getEmptyCrystal(crystalTire));
        for (int inputAnalyzeDatum : inputData) build.chancedInput(getAnalyzeData(inputAnalyzeDatum), 50, 0);
        build
                .chancedOutput(getAnalyzeData(outputData), chanced, 0)
                .chancedOutput(getAnalyzeData(ErrorDataMap.get(crystalTire)), 2000, 0)
                .EUt(eut)
                .CWUt(cwut)
                .totalCWU(totalCWU)
                .save();
    }
}
