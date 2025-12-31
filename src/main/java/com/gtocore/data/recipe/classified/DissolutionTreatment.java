package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.DISSOLUTION_TREATMENT_RECIPES;

final class DissolutionTreatment {

    public static void init() {
        DISSOLUTION_TREATMENT_RECIPES.recipeBuilder("rare_earth_hydroxides")
                .inputItems(TagPrefix.dust, GTMaterials.RareEarth, 10)
                .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 30)
                .inputFluids(GTMaterials.PhosphoricAcid, 1000)
                .inputFluids(GTMaterials.Water, 9000)
                .outputFluids(GTOMaterials.RareEarthHydroxides, 10000)
                .EUt(480)
                .duration(800)
                .save();

        DISSOLUTION_TREATMENT_RECIPES.recipeBuilder("rhenium_sulfuric_solution")
                .inputFluids(GTOMaterials.MolybdenumFlue, 30000)
                .inputFluids(GTMaterials.Water, 2500)
                .outputFluids(GTOMaterials.RheniumSulfuricSolution, 30000)
                .EUt(1920)
                .duration(3000)
                .save();

        DISSOLUTION_TREATMENT_RECIPES.recipeBuilder("bedrock_soot_solution")
                .inputItems(TagPrefix.dust, GTMaterials.Naquadah, 10)
                .inputFluids(GTOMaterials.BedrockSmoke, 10000)
                .inputFluids(GTMaterials.DistilledWater, 10000)
                .outputFluids(GTOMaterials.BedrockSootSolution, 10000)
                .EUt(7680)
                .duration(4000)
                .save();

        DISSOLUTION_TREATMENT_RECIPES.recipeBuilder("actinium_radium_hydroxide_solution")
                .inputFluids(GTOMaterials.ActiniumRadiumHydroxideSolution, 10000)
                .inputFluids(GTMaterials.NitricAcid, 120000)
                .outputFluids(GTOMaterials.ActiniumRadiumNitrateSolution, 130000)
                .EUt(3840)
                .duration(2900)
                .save();
    }
}
