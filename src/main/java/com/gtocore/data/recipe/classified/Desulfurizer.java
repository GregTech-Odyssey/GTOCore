package com.gtocore.data.recipe.classified;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gregtechceu.gtceu.common.data.GTMaterials.NaturalGas;
import static com.gregtechceu.gtceu.common.data.GTMaterials.RefineryGas;
import static com.gtocore.common.data.GTORecipeTypes.DESULFURIZER_RECIPES;

final class Desulfurizer {

    public static void init() {
        DESULFURIZER_RECIPES.recipeBuilder("naphtha")
                .inputFluids(GTMaterials.SulfuricNaphtha, 12000)
                .outputItems(TagPrefix.dust, GTMaterials.Sulfur)
                .outputFluids(GTMaterials.Naphtha, 12000)
                .EUt(30)
                .duration(120)
                .save();

        DESULFURIZER_RECIPES.recipeBuilder("light_fuel")
                .inputFluids(GTMaterials.SulfuricLightFuel, 12000)
                .outputItems(TagPrefix.dust, GTMaterials.Sulfur)
                .outputFluids(GTMaterials.LightFuel, 12000)
                .EUt(30)
                .duration(120)
                .save();

        DESULFURIZER_RECIPES.recipeBuilder("heavy_fuel")
                .inputFluids(GTMaterials.SulfuricHeavyFuel, 12000)
                .outputItems(TagPrefix.dust, GTMaterials.Sulfur)
                .outputFluids(GTMaterials.HeavyFuel, 12000)
                .EUt(30)
                .duration(120)
                .save();

        DESULFURIZER_RECIPES.recipeBuilder("gas")
                .inputFluids(GTMaterials.SulfuricGas, 16000)
                .outputItems(TagPrefix.dust, GTMaterials.Sulfur)
                .outputFluids(GTMaterials.RefineryGas, 16000)
                .EUt(30)
                .duration(120)
                .save();

        DESULFURIZER_RECIPES.recipeBuilder("natural_gas")
                .inputFluids(NaturalGas, 16000)
                .outputItems(TagPrefix.dust, GTMaterials.Sulfur)
                .outputFluids(RefineryGas.getFluid(16000))
                .EUt(30)
                .duration(120)
                .save();
    }
}
