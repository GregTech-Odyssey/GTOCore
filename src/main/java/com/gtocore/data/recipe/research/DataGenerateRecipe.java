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
        /// 配方数据生成
        /// .buildRecipe 输入输出数据球存储的配方类型
        /// 需要输入两个催化剂 .catalyst1 catalyst2
        /// 输入一个输出数据球等级
        /// 输入一个输出数据球存储的物品
        /// 需要至少一个至多八个数据输入 .inputData
        /// 耗电 .EUt
        /// 算力 .CWUt( ) 或 .CWUt( , )
        RecipesDataGenerateRecipeBuilder.buildRecipe(ASSEMBLY_LINE_RECIPES)
                .catalyst1(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .catalyst2(ChemicalHelper.get(TagPrefix.lens, GTMaterials.Amethyst))
                .getDataItem(2)
                .outputSynthetic(new ItemStack(GTBlocks.OPTICAL_PIPES[0]), 10000)
                .inputData(0xC9D13190)
                .inputData(0xD7DCCFE3)
                .inputData(0x38181C20)
                .inputData(0x3C18226C)
                .inputData(0x6853DEEA)
                .inputData(0x03B31AA9)
                .EUt(VA[IV])
                .CWUt(16, 1600)
                .save();
    }
}
