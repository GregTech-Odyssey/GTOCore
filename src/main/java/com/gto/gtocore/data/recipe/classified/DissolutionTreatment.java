package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

interface DissolutionTreatment {

    static void init() {
        GTORecipeTypes.DISSOLUTION_TREATMENT_RECIPES.recipeBuilder(GTOCore.id("rare_earth_hydroxides"))
                .inputItems(TagPrefix.dust, GTMaterials.RareEarth, 10)
                .inputItems(TagPrefix.dust, GTMaterials.SodiumHydroxide, 30)
                .inputFluids(GTMaterials.PhosphoricAcid.getFluid(1000))
                .inputFluids(GTMaterials.Water.getFluid(9000))
                .outputFluids(GTOMaterials.RareEarthHydroxides.getFluid(10000))
                .EUt(480)
                .duration(800)
                .save();

        GTORecipeTypes.DISSOLUTION_TREATMENT_RECIPES.recipeBuilder(GTOCore.id("rhenium_sulfuric_solution"))
                .inputFluids(GTOMaterials.MolybdenumFlue.getFluid(30000))
                .inputFluids(GTMaterials.Water.getFluid(2500))
                .outputFluids(GTOMaterials.RheniumSulfuricSolution.getFluid(30000))
                .EUt(1920)
                .duration(3000)
                .save();

        GTORecipeTypes.DISSOLUTION_TREATMENT_RECIPES.recipeBuilder(GTOCore.id("bedrock_soot_solution"))
                .inputItems(TagPrefix.dust, GTMaterials.Naquadah, 10)
                .inputFluids(GTOMaterials.BedrockSmoke.getFluid(10000))
                .inputFluids(GTMaterials.DistilledWater.getFluid(10000))
                .outputFluids(GTOMaterials.BedrockSootSolution.getFluid(10000))
                .EUt(7680)
                .duration(4000)
                .save();

        GTORecipeTypes.DISSOLUTION_TREATMENT_RECIPES.recipeBuilder(GTOCore.id("actinium_radium_hydroxide_solution"))
                .inputFluids(GTOMaterials.ActiniumRadiumHydroxideSolution.getFluid(10000))
                .inputFluids(GTMaterials.NitricAcid.getFluid(120000))
                .outputFluids(GTOMaterials.ActiniumRadiumNitrateSolution.getFluid(130000))
                .EUt(3840)
                .duration(2900)
                .save();
    }
}
