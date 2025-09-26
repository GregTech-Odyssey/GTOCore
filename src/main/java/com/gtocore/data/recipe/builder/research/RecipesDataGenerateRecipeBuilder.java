package com.gtocore.data.recipe.builder.research;

import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.utils.FormattingUtil;
import com.gregtechceu.gtceu.utils.GTStringUtils;
import com.gregtechceu.gtceu.utils.ResearchManager;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;

import static com.gregtechceu.gtceu.api.GTValues.LuV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gtocore.common.data.GTORecipeTypes.RECIPES_DATA_GENERATE_RECIPES;
import static com.gtocore.data.recipe.builder.research.ExResearchManager.DataItemMap;
import static com.gtocore.data.recipe.builder.research.ExResearchManager.getAnalyzeData;

public final class RecipesDataGenerateRecipeBuilder {

    // 配方数据生成
    public static RecipesDataGenerateRecipeBuilder buildRecipe(GTRecipeType recipeType) {
        return new RecipesDataGenerateRecipeBuilder(recipeType);
    }

    private final GTRecipeType recipeType;
    private Item DataItem;
    private int chance;
    private String researchId;

    private int[] inputAnalyzeData = {};

    private ItemStack catalyst1;
    private ItemStack catalyst2;
    private long eut = VA[LuV];
    private int cwut = 0;
    private int totalCWU = 0;

    private RecipesDataGenerateRecipeBuilder(GTRecipeType recipeType) {
        this.recipeType = recipeType;
    }

    public RecipesDataGenerateRecipeBuilder catalyst1(ItemStack catalyst) {
        this.catalyst1 = catalyst;
        return this;
    }

    public RecipesDataGenerateRecipeBuilder catalyst2(ItemStack catalyst) {
        this.catalyst2 = catalyst;
        return this;
    }

    public RecipesDataGenerateRecipeBuilder getDataItem(int tire) {
        this.DataItem = DataItemMap.get(tire);
        return this;
    }

    public RecipesDataGenerateRecipeBuilder getDataItem(Item dataItem) {
        this.DataItem = dataItem;
        return this;
    }

    public RecipesDataGenerateRecipeBuilder inputData(int data) {
        if (inputAnalyzeData.length > 8) return this;
        this.inputAnalyzeData = Arrays.copyOf(this.inputAnalyzeData, this.inputAnalyzeData.length + 1);
        this.inputAnalyzeData[this.inputAnalyzeData.length - 1] = data;
        return this;
    }

    public RecipesDataGenerateRecipeBuilder outputSynthetic(ItemStack itemStack, int chance) {
        this.researchId = GTStringUtils.itemStackToString(itemStack);
        this.chance = chance;
        return this;
    }

    public RecipesDataGenerateRecipeBuilder outputSynthetic(FluidStack fluidStack, int chance) {
        this.researchId = GTStringUtils.fluidStackToString(fluidStack);
        this.chance = chance;
        return this;
    }

    public RecipesDataGenerateRecipeBuilder EUt(long eut) {
        this.eut = eut;
        return this;
    }

    public RecipesDataGenerateRecipeBuilder CWUt(int cwut) {
        this.cwut = cwut;
        this.totalCWU = cwut * 4000;
        return this;
    }

    public RecipesDataGenerateRecipeBuilder CWUt(int cwut, int totalCWU) {
        this.cwut = cwut;
        this.totalCWU = totalCWU;
        return this;
    }

    public void save() {
        if (cwut > totalCWU) throw new IllegalStateException("Total CWU cannot be greater than CWU/t!");
        if (catalyst1 == null || catalyst2 == null) throw new IllegalStateException("Catalyst input required");
        if (DataItem == null) throw new IllegalStateException("DataItem input required");
        if (researchId == null) throw new IllegalStateException("Output Synthetic required");

        var dataStack = DataItem.getDefaultInstance();
        ResearchManager.writeResearchToNBT(dataStack.getOrCreateTag(), researchId, recipeType);

        var build = RECIPES_DATA_GENERATE_RECIPES.recipeBuilder(FormattingUtil.toLowerCaseUnderscore(researchId));
        build
                .notConsumable(catalyst1)
                .notConsumable(catalyst2)
                .inputItems(DataItem);
        for (int inputAnalyzeDatum : inputAnalyzeData) build.notConsumable(getAnalyzeData(inputAnalyzeDatum));
        build
                .chancedOutput(dataStack, chance, 0)
                .EUt(eut)
                .CWUt(cwut)
                .totalCWU(totalCWU)
                .save();
    }
}
