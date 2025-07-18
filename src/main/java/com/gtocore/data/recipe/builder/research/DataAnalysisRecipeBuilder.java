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
    private int[] chance = {};

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

    public DataAnalysisRecipeBuilder outputData(int data, int chance) {
        if (outputData.length > 5) return this;
        this.outputData = Arrays.copyOf(this.outputData, this.outputData.length + 1);
        this.outputData[this.outputData.length - 1] = data;
        this.chance = Arrays.copyOf(this.chance, this.chance.length + 1);
        this.chance[this.chance.length - 1] = chance;
        return this;
    }

    public DataAnalysisRecipeBuilder EUt(long eut) {
        this.eut = eut;
        return this;
    }

    public DataAnalysisRecipeBuilder CWUt(int cwut) {
        this.cwut = cwut;
        this.totalCWU = cwut * 4000;
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
        int crystalTire = ExtractDataCrystal(inputData);
        if (crystalTire < 1 || crystalTire > 5) throw new IllegalStateException("DataCrystal Out of index");

        var build = DATA_ANALYSIS_RECIPES.recipeBuilder(ScanningMap.get(inputData));
        build
                .notConsumable(catalyst)
                .inputItems(getEmptyCrystal(crystalTire))
                .chancedInput(getScanningData(inputData), 50, 0);
        for (int i = 0; i < outputData.length; i++)
            build.chancedOutput(getAnalyzeData(outputData[i]), chance[i], 0);
        build
                .chancedOutput(getAnalyzeData(ErrorDataMap.get(crystalTire)), 2000, 0)
                .EUt(eut)
                .CWUt(cwut)
                .totalCWU(totalCWU)
                .save();
    }
}
