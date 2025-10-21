package com.gtocore.data.recipe.research;

import com.gtocore.data.recipe.builder.research.RecipesDataGenerateRecipeBuilder;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;

import static com.gregtechceu.gtceu.api.GTValues.IV;
import static com.gregtechceu.gtceu.api.GTValues.VA;
import static com.gtocore.common.data.GTORecipeTypes.ASSEMBLY_LINE_RECIPES;

public final class DataGenerateRecipe {

    public static void init() {
        RecipesDataGenerateRecipeBuilder.buildRecipe(ASSEMBLY_LINE_RECIPES)
                .catalyst1(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .catalyst2(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .getDataItem(2)
                .inputData(0x53D13190)
                .inputData(0x041823FF)
                .inputData(0x01181C20)
                .inputData(0x021820D9)
                .outputSynthetic(new ItemStack(GTBlocks.OPTICAL_PIPES[0]), 10000)
                .EUt(VA[IV])
                .CWUt(16, 1600)
                .save();
    }
}
