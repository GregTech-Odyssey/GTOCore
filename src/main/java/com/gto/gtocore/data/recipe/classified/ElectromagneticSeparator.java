package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface ElectromagneticSeparator {

    static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder(GTOCore.id("graphene_oxide_dust"))
                .inputItems(GTOItems.GRAPHENE_IRON_PLATE.asStack())
                .outputItems(TagPrefix.dust, GTOMaterials.GrapheneOxide, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Iron)
                .EUt(30)
                .duration(120)
                .save(provider);

        GTRecipeTypes.ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder(GTOCore.id("raw_tengam_dust"))
                .inputItems(TagPrefix.dustPure, GTOMaterials.Jasper)
                .outputItems(TagPrefix.dust, GTOMaterials.Jasper)
                .chancedOutput(TagPrefix.dust, GTOMaterials.RawTengam, 1000, 0)
                .chancedOutput(TagPrefix.dust, GTOMaterials.RawTengam, 500, 0)
                .EUt(24)
                .duration(200)
                .save(provider);

        GTRecipeTypes.ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder(GTOCore.id("purified_tengam_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.CleanRawTengam)
                .outputItems(TagPrefix.dust, GTOMaterials.PurifiedTengam)
                .chancedOutput(TagPrefix.dust, GTMaterials.NeodymiumMagnetic, 1000, 0)
                .chancedOutput(TagPrefix.dust, GTMaterials.SamariumMagnetic, 500, 0)
                .EUt(7864320)
                .duration(200)
                .save(provider);
    }
}
