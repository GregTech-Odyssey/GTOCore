package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.ISOSTATIC_PRESSING_RECIPES;

final class IsostaticPressing {

    public static void init() {
        ISOSTATIC_PRESSING_RECIPES.recipeBuilder("barium_titanate_ceramic_rough_blank")
                .inputItems(TagPrefix.dust, GTOMaterials.BariumTitanateCeramic, 9)
                .inputFluids(GTMaterials.Epoxy, 1000)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.BariumTitanateCeramic)
                .EUt(500)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.recipeBuilder("tungsten_tetraboride_ceramics_rough_blank")
                .inputItems(TagPrefix.dust, GTOMaterials.TungstenTetraborideCeramics, 9)
                .inputFluids(GTMaterials.Nickel, 1000)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.TungstenTetraborideCeramics)
                .EUt(500)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.recipeBuilder("silica_ceramic_rough_blank")
                .inputItems(TagPrefix.dust, GTOMaterials.SilicaCeramic, 9)
                .inputFluids(GTMaterials.Epoxy, 1000)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.SilicaCeramic)
                .EUt(500)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.recipeBuilder("hydroxyapatite_ceramic_rough_blank")
                .inputItems(TagPrefix.dust, GTOMaterials.HydroxyapatiteCeramic, 9)
                .inputFluids(GTMaterials.Epoxy, 1000)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.HydroxyapatiteCeramic)
                .EUt(500)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.recipeBuilder("tellurate_ceramics_rough_blank")
                .inputItems(TagPrefix.dust, GTOMaterials.TellurateCeramics, 9)
                .inputFluids(GTMaterials.Epoxy, 1000)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.TellurateCeramics)
                .EUt(500)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.recipeBuilder("thulium_hexaboride_ceramics_rough_blank")
                .inputItems(TagPrefix.dust, GTOMaterials.ThuliumHexaborideCeramics, 9)
                .inputFluids(GTMaterials.Aluminium, 1000)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.ThuliumHexaborideCeramics)
                .EUt(500)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.recipeBuilder("silicon_nitride_ceramic_rough_blank")
                .inputItems(TagPrefix.dust, GTOMaterials.SiliconNitrideCeramic, 9)
                .inputFluids(GTOMaterials.PolyurethaneResin, 1000)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.SiliconNitrideCeramic)
                .EUt(500)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.recipeBuilder("cobalt_oxide_ceramic_rough_blank")
                .inputItems(TagPrefix.dust, GTOMaterials.CobaltOxideCeramic, 9)
                .inputFluids(GTMaterials.Glue, 1000)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CobaltOxideCeramic)
                .EUt(500)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.recipeBuilder("calcium_oxide_ceramic_rough_blank")
                .inputItems(TagPrefix.dust, GTOMaterials.CalciumOxideCeramic, 9)
                .inputFluids(GTMaterials.Glue, 1000)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.CalciumOxideCeramic)
                .EUt(500)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.recipeBuilder("lithium_oxide_ceramics_rough_blank")
                .inputItems(TagPrefix.dust, GTOMaterials.LithiumOxideCeramics, 9)
                .inputFluids(GTOMaterials.PhenolicResin, 1000)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.LithiumOxideCeramics)
                .EUt(500)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.recipeBuilder("magnesium_oxide_ceramic_rough_blank")
                .inputItems(TagPrefix.dust, GTOMaterials.MagnesiumOxideCeramic, 9)
                .inputFluids(GTMaterials.Glue, 1000)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.MagnesiumOxideCeramic)
                .EUt(500)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.recipeBuilder("tricalcium_phosphate_ceramic_rough_blank")
                .inputItems(TagPrefix.dust, GTOMaterials.TricalciumPhosphateCeramic, 9)
                .inputFluids(GTOMaterials.PhenolicResin, 1000)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.TricalciumPhosphateCeramic)
                .EUt(500)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.recipeBuilder("titanium_nitride_ceramic_rough_blank")
                .inputItems(TagPrefix.dust, GTOMaterials.TitaniumNitrideCeramic, 9)
                .inputFluids(GTMaterials.Epoxy, 1000)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.TitaniumNitrideCeramic)
                .EUt(500)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.recipeBuilder("zirconia_ceramic_rough_blank")
                .inputItems(TagPrefix.dust, GTOMaterials.ZirconiaCeramic, 9)
                .inputFluids(GTMaterials.Epoxy, 1000)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.ZirconiaCeramic)
                .EUt(500)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.recipeBuilder("strontium_carbonate_ceramic_rough_blank")
                .inputItems(TagPrefix.dust, GTOMaterials.StrontiumCarbonateCeramic, 9)
                .inputFluids(GTMaterials.Epoxy, 1000)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.StrontiumCarbonateCeramic)
                .EUt(500)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.recipeBuilder("aluminaceramic_ceramic_rough_blank")
                .inputItems(TagPrefix.dust, GTOMaterials.AluminaCeramic, 9)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.AluminaCeramic)
                .inputFluids(GTMaterials.Glue, 1000)
                .EUt(500)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.recipeBuilder("boroncarbideceramics_ceramic_rough_blank")
                .inputItems(TagPrefix.dust, GTOMaterials.BoronCarbideCeramics, 9)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.BoronCarbideCeramics)
                .inputFluids(GTMaterials.Epoxy, 1000)
                .EUt(500)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.builder("zirconium_diboride_rough_blank")
                .inputItems(GTOTagPrefix.dust, GTMaterials.Zirconium, 3)
                .inputItems(GTOTagPrefix.dust, GTMaterials.Boron, 6)
                .inputItems(GTOTagPrefix.dustTiny, GTOMaterials.CubicZirconia)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.ZirconiumDiboride)
                .inputFluids(GTMaterials.Epoxy, 1000)
                .EUt(504)
                .duration(200)
                .save();

        ISOSTATIC_PRESSING_RECIPES.builder("titanium3_carbide_ceramic_rough_blank")
                .inputItems(GTOTagPrefix.dust, GTOMaterials.Titanium3Carbide, 9)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.Titanium3Carbide)
                .inputFluids(GTMaterials.Epoxy, 1000)
                .EUt(504)
                .duration(200)
                .save();
    }
}
