package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

interface Sifter {

    static void init() {
        GTORecipeTypes.SIFTER_RECIPES.recipeBuilder(GTOCore.id("tricalcium_phosphate_ceramic_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.TricalciumPhosphate, 2)
                .outputItems(TagPrefix.dust, GTOMaterials.TricalciumPhosphateCeramic)
                .EUt(120)
                .duration(280)
                .save();

        GTORecipeTypes.SIFTER_RECIPES.recipeBuilder(GTOCore.id("cobalt_oxide_ceramic_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.CobaltOxide, 2)
                .outputItems(TagPrefix.dust, GTOMaterials.CobaltOxideCeramic)
                .EUt(240)
                .duration(100)
                .save();

        GTORecipeTypes.SIFTER_RECIPES.recipeBuilder(GTOCore.id("alumina_ceramic_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.Alumina, 2)
                .outputItems(TagPrefix.dust, GTOMaterials.AluminaCeramic)
                .EUt(240)
                .duration(100)
                .save();

        GTORecipeTypes.SIFTER_RECIPES.recipeBuilder(GTOCore.id("magnesium_oxide_ceramic_dust"))
                .inputItems(TagPrefix.dust, GTMaterials.Magnesia, 2)
                .outputItems(TagPrefix.dust, GTOMaterials.MagnesiumOxideCeramic)
                .EUt(240)
                .duration(100)
                .save();

        GTORecipeTypes.SIFTER_RECIPES.recipeBuilder(GTOCore.id("boron_carbide_ceramics_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.BoronCarbide, 2)
                .outputItems(TagPrefix.dust, GTOMaterials.BoronCarbideCeramics)
                .EUt(240)
                .duration(100)
                .save();

        GTORecipeTypes.SIFTER_RECIPES.recipeBuilder(GTOCore.id("zirconia_ceramic_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.ZirconiumOxide, 2)
                .outputItems(TagPrefix.dust, GTOMaterials.ZirconiaCeramic)
                .EUt(240)
                .duration(100)
                .save();
    }
}
