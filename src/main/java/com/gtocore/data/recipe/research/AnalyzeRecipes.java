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
        DataAnalysisRecipeBuilder.buildRecipe()
                .catalyst(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .inputData(0x320DFC89)
                .outputData(0x041823FF, 2500)
                .outputData(0x01181C20, 2500)
                .outputData(0x03181F46, 2500)
                .outputData(0x0518226C, 2500)
                .EUt(VA[LuV])
                .CWUt(16)
                .save();

        DataIntegrationRecipeBuilder.buildRecipe()
                .catalyst1(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .catalyst2(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .inputData(0x041823FF)
                .inputData(0x01181C20)
                .inputData(0x03181F46)
                .inputData(0x0518226C)
                .outputData(0x021820D9, 5000)
                .EUt(VA[LuV])
                .CWUt(16)
                .save();
    }
}
