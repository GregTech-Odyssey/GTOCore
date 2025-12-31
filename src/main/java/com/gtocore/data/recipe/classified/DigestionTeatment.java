package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.DIGESTION_TREATMENT_RECIPES;
import static com.gtocore.common.data.GTORecipeTypes.DISTILLATION_RECIPES;

final class DigestionTeatment {

    public static void init() {
        DIGESTION_TREATMENT_RECIPES.recipeBuilder("rare_earth_oxide_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.SodiumOxide, 3)
                .inputFluids(GTOMaterials.RareEarthChlorides, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.RareEarthOxide)
                .outputFluids(GTMaterials.SaltWater, 1000)
                .EUt(1920)
                .duration(800)
                .blastFurnaceTemp(2580)
                .save();

        DIGESTION_TREATMENT_RECIPES.recipeBuilder("naquadah_contain_rare_earth_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.NaquadahContainRareEarthFluoride)
                .inputFluids(GTMaterials.Hydrogen, 2000)
                .outputItems(TagPrefix.dust, GTOMaterials.NaquadahContainRareEarth)
                .outputFluids(GTMaterials.HydrofluoricAcid, 2000)
                .EUt(491520)
                .duration(800)
                .blastFurnaceTemp(6800)
                .save();

        DIGESTION_TREATMENT_RECIPES.recipeBuilder("rare_earth_metal_dust")
                .inputItems(TagPrefix.dust, GTOMaterials.RareEarthOxide)
                .inputFluids(GTMaterials.Hydrogen, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.RareEarthMetal)
                .outputFluids(GTMaterials.Water, 500)
                .EUt(7680)
                .duration(400)
                .blastFurnaceTemp(1760)
                .save();

        DISTILLATION_RECIPES.recipeBuilder("enriched_naquadah_fuel")
                .inputFluids(GTOMaterials.RadonCrackedEnrichedAquadah, 1000)
                .outputFluids(GTOMaterials.EnrichedNaquadahFuel, 800)
                .outputFluids(GTMaterials.NaquadriaWaste, 100)
                .outputFluids(GTMaterials.Radon, 200)
                .outputFluids(GTMaterials.Fluorine, 200)
                .EUt(30720)
                .duration(600)
                .save();

        DISTILLATION_RECIPES.recipeBuilder("naquadah_fuel")
                .inputFluids(GTOMaterials.FluorineCrackedAquadah, 1000)
                .outputFluids(GTOMaterials.NaquadahFuel, 800)
                .outputFluids(GTMaterials.NitricAcid, 200)
                .outputFluids(GTMaterials.EnrichedNaquadahWaste, 100)
                .outputFluids(GTMaterials.Ammonia, 200)
                .outputFluids(GTMaterials.Fluorine, 200)
                .EUt(122880)
                .duration(600)
                .save();

        DISTILLATION_RECIPES.recipeBuilder("diphenylmethane_diisocyanate_dust")
                .inputFluids(GTOMaterials.DiphenylmethanediisocyanateMixture, 1000)
                .outputItems(TagPrefix.dust, GTOMaterials.DiphenylmethaneDiisocyanate, 29)
                .outputFluids(GTMaterials.HydrochloricAcid, 5000)
                .EUt(1920)
                .duration(700)
                .save();

        DISTILLATION_RECIPES.recipeBuilder("turpentine")
                .inputFluids(GTOMaterials.SteamCrackedTurpentine, 1000)
                .outputFluids(GTOMaterials.Turpentine, 1000)
                .outputFluids(GTMaterials.Naphtha, 900)
                .EUt(1920)
                .duration(400)
                .save();

        DISTILLATION_RECIPES.recipeBuilder("rawradox")
                .inputFluids(GTOMaterials.RawRadox, 5000)
                .outputItems(TagPrefix.dust, GTMaterials.Ash, 5)
                .outputFluids(GTMaterials.OilHeavy, 600)
                .outputFluids(GTMaterials.Oil, 300)
                .outputFluids(GTMaterials.Creosote, 1000)
                .outputFluids(GTMaterials.Water, 1400)
                .outputFluids(GTMaterials.Bacteria, 50)
                .outputFluids(GTMaterials.FermentedBiomass, 50)
                .outputFluids(GTOMaterials.SuperHeavyRadox, 100)
                .outputFluids(GTOMaterials.HeavyRadox, 100)
                .outputFluids(GTOMaterials.DilutedXenoxene, 100)
                .outputFluids(GTOMaterials.LightRadox, 100)
                .outputFluids(GTOMaterials.SuperLightRadox, 100)
                .EUt(491520)
                .duration(800)
                .save();

        DISTILLATION_RECIPES.recipeBuilder("dilutedxenoxene")
                .inputFluids(GTOMaterials.DilutedXenoxene, 1000)
                .outputItems(TagPrefix.dust, GTMaterials.Ash)
                .outputFluids(GTOMaterials.Xenoxene, 250)
                .outputFluids(GTOMaterials.LightRadox, 300)
                .EUt(491520)
                .duration(200)
                .save();

        DISTILLATION_RECIPES.recipeBuilder("radox_gas")
                .inputFluids(GTOMaterials.CrackedRadox, 1000)
                .outputItems(TagPrefix.dust, GTMaterials.Ash)
                .outputFluids(GTOMaterials.RadoxGas, 100)
                .outputFluids(GTOMaterials.LightRadox, 200)
                .EUt(491520)
                .duration(600)
                .save();
    }
}
