package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.LARGE_BOILER_RECIPES;

interface LargeBoiler {

    static void init() {
        LARGE_BOILER_RECIPES.recipeBuilder(GTOCore.id("tin_bucket"))
                .inputItems(TagPrefix.ingot, GTMaterials.Tin)
                .inputFluids(GTMaterials.Lava.getFluid(100))
                .outputFluids(GTMaterials.Tin.getFluid(72))
                .duration(20)
                .temperature(600)
                .save();

        LARGE_BOILER_RECIPES.recipeBuilder(GTOCore.id("water_gas"))
                .inputFluids(GTOMaterials.CoalSlurry.getFluid(1000))
                .outputFluids(GTOMaterials.WaterGas.getFluid(1000))
                .duration(80)
                .temperature(1300)
                .save();
    }
}
