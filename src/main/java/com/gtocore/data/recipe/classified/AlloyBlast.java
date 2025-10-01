package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.ALLOY_BLAST_RECIPES;

final class AlloyBlast {

    public static void init() {
        ALLOY_BLAST_RECIPES.recipeBuilder("carbon_disulfide")
                .circuitMeta(8)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon)
                .inputItems(TagPrefix.dust, GTMaterials.Sulfur, 2)
                .outputFluids(GTOMaterials.CarbonDisulfide.getFluid(1000))
                .EUt(120)
                .duration(350)
                .blastFurnaceTemp(1200)
                .save();

        ALLOY_BLAST_RECIPES.recipeBuilder("superheavy_mix")
                .circuitMeta(2)
                .inputItems(TagPrefix.dust, GTOMaterials.SuperheavyLAlloy)
                .inputItems(TagPrefix.dust, GTOMaterials.SuperheavyHAlloy)
                .outputFluids(GTOMaterials.SuperheavyMix.getFluid(288))
                .EUt(100000000)
                .duration(80)
                .blastFurnaceTemp(12880)
                .save();

        ALLOY_BLAST_RECIPES.recipeBuilder("yttrium_barium_cuprate")
                .inputItems(TagPrefix.dust, GTMaterials.Yttrium)
                .inputItems(TagPrefix.dust, GTMaterials.Barium, 2)
                .inputItems(TagPrefix.dust, GTMaterials.Copper, 3)
                .inputFluids(GTMaterials.Oxygen.getFluid(7000))
                .outputFluids(GTMaterials.YttriumBariumCuprate.getFluid(1872))
                .EUt(524288)
                .blastFurnaceTemp(12288)
                .duration(3450)
                .save();
    }
}
