package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.ELECTROLYZER_RECIPES;

final class Electrolyzer {

    public static void init() {
        ELECTROLYZER_RECIPES.recipeBuilder("radium_nitrate_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.RadiumNitrate, 9)
                .outputItems(TagPrefix.dust, GTMaterials.Radium)
                .outputFluids(GTMaterials.Nitrogen, 2000)
                .outputFluids(GTMaterials.Oxygen, 6000)
                .EUt(500)
                .duration(160)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder("trinium_tetrafluoride_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.TriniumTetrafluoride, 5)
                .inputFluids(GTOMaterials.MoltenCalciumSalts, 1000)
                .outputItems(TagPrefix.dust, GTMaterials.Trinium)
                .outputItems(TagPrefix.dust, GTMaterials.Calcium, 2)
                .outputFluids(GTMaterials.Fluorine, 6000)
                .EUt(1920)
                .duration(200)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder("sodium_chlorate_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumHypochlorite, 9)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumChlorate, 5)
                .outputItems(TagPrefix.dust, GTMaterials.Salt, 4)
                .EUt(120)
                .duration(210)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder("actinium_nitrate_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.ActiniumNitrate, 13)
                .outputItems(TagPrefix.dust, GTMaterials.Actinium)
                .outputFluids(GTMaterials.Nitrogen, 3000)
                .outputFluids(GTMaterials.Oxygen, 9000)
                .EUt(500)
                .duration(210)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder("purified_xenoxene")
                .inputItems(TagPrefix.dust, GTOMaterials.XenoxeneCrystal, 4)
                .inputFluids(GTMaterials.Oil, 1000)
                .outputFluids(GTOMaterials.PurifiedXenoxene, 1000)
                .outputFluids(GTMaterials.Water, 1000)
                .EUt(30720)
                .duration(900)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder("caesium_nitrate_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.CaesiumNitrate, 5)
                .outputItems(TagPrefix.dust, GTMaterials.Caesium)
                .outputFluids(GTMaterials.Nitrogen, 1000)
                .outputFluids(GTMaterials.Oxygen, 3000)
                .EUt(30)
                .duration(170)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder("lithium_aluminium_fluoride_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.LithiumAluminiumFluoride, 6)
                .outputItems(TagPrefix.dust, GTOMaterials.AluminiumTrifluoride, 4)
                .outputItems(TagPrefix.dust, GTOMaterials.LithiumFluoride, 2)
                .EUt(120)
                .duration(250)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder("trinium_compound_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.TriniumCompound, 14)
                .outputItems(TagPrefix.dust, GTMaterials.Trinium, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Actinium, 3)
                .outputItems(TagPrefix.dust, GTMaterials.Selenium, 4)
                .outputItems(TagPrefix.dust, GTMaterials.Astatine, 4)
                .EUt(31457280)
                .duration(560)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder("sodium_hexafluoroaluminate")
                .inputItems(TagPrefix.dust, GTOMaterials.Alumina, 10)
                .inputFluids(GTOMaterials.SodiumHexafluoroaluminate, 1000)
                .outputItems(GTOItems.RAW_ALUMINUM, 4)
                .outputItems(TagPrefix.dust, GTOMaterials.SodiumFluoride, 6)
                .outputItems(TagPrefix.dust, GTOMaterials.AluminiumTrifluoride, 4)
                .outputFluids(GTMaterials.Oxygen, 6000)
                .EUt(120)
                .duration(160)
                .save();

        ELECTROLYZER_RECIPES.recipeBuilder("trimethylamine")
                .inputFluids(GTOMaterials.Trimethylamine, 13000)
                .outputItems(TagPrefix.dust, GTMaterials.Carbon, 3)
                .outputFluids(GTMaterials.Hydrogen, 9000)
                .outputFluids(GTMaterials.Nitrogen, 1000)
                .EUt(125)
                .duration(140)
                .save();
    }
}
