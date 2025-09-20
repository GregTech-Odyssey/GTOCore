package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.ELECTROMAGNETIC_SEPARATOR_RECIPES;

final class ElectromagneticSeparator {

    public static void init() {
        ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder("graphene_oxide_dust")
                .inputItems(GTOItems.GRAPHENE_IRON_PLATE.asItem())
                .outputItems(TagPrefix.dust, GTOMaterials.GrapheneOxide, 9)
                .outputItems(TagPrefix.dust, GTMaterials.Iron)
                .EUt(30)
                .duration(120)
                .save();

        ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder("raw_tengam_dust")
                .inputItems(TagPrefix.dustPure, GTOMaterials.Jasper)
                .outputItems(TagPrefix.dust, GTOMaterials.Jasper)
                .chancedOutput(TagPrefix.dust, GTOMaterials.RawTengam, 1000, 0)
                .chancedOutput(TagPrefix.dust, GTOMaterials.RawTengam, 500, 0)
                .EUt(24)
                .duration(200)
                .save();

        ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder("purified_tengam_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.CleanRawTengam)
                .outputItems(TagPrefix.dust, GTOMaterials.PurifiedTengam)
                .chancedOutput(TagPrefix.dust, GTMaterials.NeodymiumMagnetic, 1000, 0)
                .chancedOutput(TagPrefix.dust, GTMaterials.SamariumMagnetic, 500, 0)
                .EUt(7864320)
                .duration(200)
                .save();
        ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder("enrich_etrium_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.LightEmittingChargedSuspiciousWaste)
                .chancedOutput(TagPrefix.dust, GTOMaterials.EnrichedLightEmittingChargedSuspiciousWaste, 9000, 0)
                .chancedOutput(TagPrefix.dust, GTOMaterials.EnrichedLightEmittingChargedSuspiciousWaste, 1000, 0)
                .chancedOutput(TagPrefix.dust, GTMaterials.Fermium, 4500, 0)
                .EUt(122800)
                .duration(200)
                .save();
    }
}
