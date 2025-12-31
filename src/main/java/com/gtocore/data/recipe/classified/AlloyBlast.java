package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.ALLOY_BLAST_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.SPACE_SMELTING_RECIPES;

final class AlloyBlast {

    public static void init() {
        ALLOY_BLAST_RECIPES.recipeBuilder("carbon_disulfide")
                .inputItems(TagPrefix.dust, GTMaterials.Carbon)
                .inputItems(TagPrefix.dust, GTMaterials.Sulfur, 2)
                .circuitMeta(8)
                .outputFluids(GTOMaterials.CarbonDisulfide, 1000)
                .EUt(120)
                .duration(350)
                .blastFurnaceTemp(1200)
                .save();

        ALLOY_BLAST_RECIPES.recipeBuilder("yttrium_barium_cuprate")
                .inputItems(TagPrefix.dust, GTMaterials.Yttrium)
                .inputItems(TagPrefix.dust, GTMaterials.Barium, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Copper, 3)
                .circuitMeta(3)
                .inputFluids(GTMaterials.Oxygen, 7000)
                .outputFluids(GTMaterials.YttriumBariumCuprate, 1872)
                .EUt(524288)
                .blastFurnaceTemp(12288)
                .duration(3450)
                .save();

        SPACE_SMELTING_RECIPES.recipeBuilder("superheavy_mix")
                .inputItems(TagPrefix.dust, GTOMaterials.SuperheavyLAlloy)
                .inputItems(TagPrefix.dust, GTOMaterials.SuperheavyHAlloy)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Xenon, 1000)
                .outputFluids(GTOMaterials.SuperheavyMix, 288)
                .EUt(8388608)
                .blastFurnaceTemp(12888)
                .duration(2530)
                .save();
    }
}
