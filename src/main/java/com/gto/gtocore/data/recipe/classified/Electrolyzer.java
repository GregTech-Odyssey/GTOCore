package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.ELECTROLYZER_RECIPES;

interface Electrolyzer {

    static void init() {
        ELECTROLYZER_RECIPES.recipeBuilder(GTOCore.id("radium_nitrate_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.RadiumNitrate, 9)
                .outputItems(TagPrefix.dust, GTMaterials.Radium)
                .outputFluids(GTMaterials.Nitrogen.getFluid(2000))
                .outputFluids(GTMaterials.Oxygen.getFluid(6000))
                .EUt(500)
                .duration(160)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder(GTOCore.id("trinium_tetrafluoride_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.TriniumTetrafluoride, 5)
                .inputFluids(GTOMaterials.MoltenCalciumSalts.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Trinium)
                .outputItems(TagPrefix.dust, GTMaterials.Calcium, 2)
                .outputFluids(GTMaterials.Fluorine.getFluid(6000))
                .EUt(1920)
                .duration(200)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder(GTOCore.id("sodium_chlorate_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumHypochlorite, 9)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumChlorate, 5)
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 4)
                .EUt(120)
                .duration(210)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder(GTOCore.id("actinium_nitrate_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.ActiniumNitrate, 13)
                .outputItems(TagPrefix.dust, GTMaterials.Actinium)
                .outputFluids(GTMaterials.Nitrogen.getFluid(3000))
                .outputFluids(GTMaterials.Oxygen.getFluid(9000))
                .EUt(500)
                .duration(210)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder(GTOCore.id("purified_xenoxene"))
                .inputItems(TagPrefix.dust, GTOMaterials.XenoxeneCrystal, 4)
                .inputFluids(GTMaterials.Oil.getFluid(1000))
                .outputFluids(GTOMaterials.PurifiedXenoxene.getFluid(1000))
                .outputFluids(GTMaterials.Water.getFluid(1000))
                .EUt(30720)
                .duration(900)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder(GTOCore.id("caesium_nitrate_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.CaesiumNitrate, 5)
                .outputItems(TagPrefix.dust, GTMaterials.Caesium)
                .outputFluids(GTMaterials.Nitrogen.getFluid(1000))
                .outputFluids(GTMaterials.Oxygen.getFluid(3000))
                .EUt(30)
                .duration(170)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder(GTOCore.id("lithium_aluminium_fluoride_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.LithiumAluminiumFluoride, 6)
                .outputItems(TagPrefix.dust, GTOMaterials.AluminiumTrifluoride, 4)
                .outputItems(TagPrefix.dust, GTOMaterials.LithiumFluoride, 2)
                .EUt(120)
                .duration(250)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder(GTOCore.id("trinium_compound_dust"))
                .inputItems(TagPrefix.dust, GTOMaterials.TriniumCompound, 14)
                .outputItems(TagPrefix.dust, GTMaterials.Trinium, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Actinium, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Selenium, 4)
                .outputItems(TagPrefix.dust, GTMaterials.Astatine, 4)
                .EUt(31457280)
                .duration(560)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder(GTOCore.id("sodium_hexafluoroaluminate"))
                .inputItems(TagPrefix.dust, GTOMaterials.Alumina, 10)
                .inputFluids(GTOMaterials.SodiumHexafluoroaluminate.getFluid(1000))
                .outputItems(GTOItems.RAW_ALUMINUM, 4)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumFluoride, 6)
                .outputItems(TagPrefix.dust, GTOMaterials.AluminiumTrifluoride, 4)
                .outputFluids(GTMaterials.Oxygen.getFluid(6000))
                .EUt(120)
                .duration(160)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder(GTOCore.id("trimethylamine"))
                .inputFluids(GTOMaterials.Trimethylamine.getFluid(13000))
                .outputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputFluids(GTMaterials.Hydrogen.getFluid(9000))
                .outputFluids(GTMaterials.Nitrogen.getFluid(1000))
                .EUt(125)
                .duration(140)
                .save();
    }
}
