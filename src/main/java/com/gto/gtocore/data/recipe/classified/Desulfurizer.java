package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface Desulfurizer {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.DESULFURIZER_RECIPES.recipeBuilder(GTOCore.id("naphtha"))
                .inputFluids(GTMaterials.SulfuricNaphtha.getFluid(12000))
                .outputItems(TagPrefix.dust, GTMaterials.Sulfur)
                .outputFluids(GTMaterials.Naphtha.getFluid(12000))
                .EUt(30)
                .duration(120)
                .save(provider);

        GTORecipeTypes.DESULFURIZER_RECIPES.recipeBuilder(GTOCore.id("light_fuel"))
                .inputFluids(GTMaterials.SulfuricLightFuel.getFluid(12000))
                .outputItems(TagPrefix.dust, GTMaterials.Sulfur)
                .outputFluids(GTMaterials.LightFuel.getFluid(12000))
                .EUt(30)
                .duration(120)
                .save(provider);

        GTORecipeTypes.DESULFURIZER_RECIPES.recipeBuilder(GTOCore.id("heavy_fuel"))
                .inputFluids(GTMaterials.SulfuricHeavyFuel.getFluid(12000))
                .outputItems(TagPrefix.dust, GTMaterials.Sulfur)
                .outputFluids(GTMaterials.HeavyFuel.getFluid(12000))
                .EUt(30)
                .duration(120)
                .save(provider);

        GTORecipeTypes.DESULFURIZER_RECIPES.recipeBuilder(GTOCore.id("gas"))
                .inputFluids(GTMaterials.SulfuricGas.getFluid(12000))
                .outputItems(TagPrefix.dust, GTMaterials.Sulfur)
                .outputFluids(GTMaterials.RefineryGas.getFluid(12000))
                .EUt(30)
                .duration(120)
                .save(provider);
    }
}
