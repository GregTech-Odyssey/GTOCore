package com.gtocore.data.recipe.research;

import com.gtocore.data.recipe.builder.research.DataAnalysisRecipeBuilder;
import com.gtocore.data.recipe.builder.research.DataIntegrationRecipeBuilder;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gregtechceu.gtceu.api.GTValues.LuV;
import static com.gregtechceu.gtceu.api.GTValues.VA;

public final class AnalyzeRecipes {

    public static void init() {
        /// 数据分析
        /// 需要输入一个催化剂 .catalyst
        /// 需要一个扫描数据输入 .inputData
        /// 需要至少一个至多五个的数据输出 .outputData
        /// 耗电 .EUt
        /// 算力 .CWUt( ) 或 .CWUt( , )
        DataAnalysisRecipeBuilder.buildRecipe()
                .catalyst(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .inputData(0xAEC0E629)
                .outputData(0x3D1823FF, 2500)
                .outputData(0x38181C20, 2500)
                .outputData(0x3A181F46, 2500)
                .outputData(0x3C18226C, 2500)
                .outputData(0x3B1820D9, 2500)
                .EUt(VA[LuV])
                .CWUt(16)
                .save();

        /// 数据统合
        /// 需要输入两个催化剂 .catalyst1 catalyst2
        /// 需要至少一个至多十个数据输入 .inputData
        /// 需要一个研究数据输出 .outputData
        /// 耗电 .EUt
        /// 算力 .CWUt( ) 或 .CWUt( , )
        DataIntegrationRecipeBuilder.buildRecipe()
                .catalyst1(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .catalyst2(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .inputData(0x3D1823FF)
                .inputData(0x38181C20)
                .inputData(0x3A181F46)
                .inputData(0x3C18226C)
                .outputData(0x03B31AA9, 5000)
                .EUt(VA[LuV])
                .CWUt(16)
                .save();
    }
}
