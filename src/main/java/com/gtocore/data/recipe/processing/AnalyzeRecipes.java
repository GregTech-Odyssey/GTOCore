package com.gtocore.data.recipe.processing;

import com.gtolib.api.recipe.builder.DataAnalysisRecipeBuilder;
import com.gtolib.api.recipe.builder.DataIntegrationRecipeBuilder;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gregtechceu.gtceu.api.GTValues.LuV;
import static com.gregtechceu.gtceu.api.GTValues.VA;

public final class AnalyzeRecipes {

    public static void init() {
        DataAnalysisRecipeBuilder.buildRecipe()
                .catalyst(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .inputScanningData(0x32E5585F)
                .outputAnalyzeData(0x041823FF, 2500)
                .outputAnalyzeData(0x01181C20, 2500)
                .outputAnalyzeData(0x03181F46, 2500)
                .outputAnalyzeData(0x0518226C, 2500)
                .EUt(VA[LuV])
                .CWUt(16)
                .save();

        DataIntegrationRecipeBuilder.buildRecipe()
                .catalyst1(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .catalyst2(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .inputAnalyzeData(0x041823FF)
                .inputAnalyzeData(0x01181C20)
                .inputAnalyzeData(0x03181F46)
                .inputAnalyzeData(0x0518226C)
                .outputAnalyzeData(0x021820D9, 5000)
                .EUt(VA[LuV])
                .CWUt(16)
                .save();
    }
}
