package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.ORE_WASHER_RECIPES;

interface OreWasher {

    static void init() {
        ORE_WASHER_RECIPES.recipeBuilder(GTOCore.id("clean_inert_residues_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.InertResidues)
                .inputFluids(GTMaterials.AquaRegia.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.CleanInertResidues)
                .EUt(480)
                .duration(400)
                .save();

        ORE_WASHER_RECIPES.recipeBuilder(GTOCore.id("clean_raw_tengam_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.RawTengam)
                .inputFluids(GTMaterials.DistilledWater.getFluid(1000))
                .outputItems(TagPrefix.dust, GTOMaterials.CleanRawTengam)
                .EUt(480)
                .duration(800)
                .save();
    }
}
