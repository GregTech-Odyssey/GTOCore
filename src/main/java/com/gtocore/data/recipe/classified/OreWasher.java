package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.ORE_WASHER_RECIPES;

final class OreWasher {

    public static void init() {
        ORE_WASHER_RECIPES.recipeBuilder("clean_inert_residues_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.InertResidues)
                .inputFluids(GTMaterials.AquaRegia, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.CleanInertResidues)
                .EUt(480)
                .duration(400)
                .save();

        ORE_WASHER_RECIPES.recipeBuilder("clean_raw_tengam_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.RawTengam)
                .inputFluids(GTMaterials.DistilledWater, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.CleanRawTengam)
                .EUt(480)
                .duration(800)
                .save();
    }
}
