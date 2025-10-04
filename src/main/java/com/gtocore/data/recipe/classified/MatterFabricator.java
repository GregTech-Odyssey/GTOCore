package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import appeng.core.definitions.AEItems;

import static com.gtocore.common.data.GTORecipeTypes.MATTER_FABRICATOR_RECIPES;

final class MatterFabricator {

    public static void init() {
        MATTER_FABRICATOR_RECIPES.recipeBuilder("uu_amplifier")
                .inputItems(GTOItems.SCRAP.asItem())
                .circuitMeta(1)
                .outputFluids(GTOMaterials.UuAmplifier.getFluid(1))
                .EUt(491520)
                .duration(200)
                .save();

        MATTER_FABRICATOR_RECIPES.recipeBuilder("uu_amplifier_1")
                .inputItems(GTOItems.SCRAP.asItem())
                .circuitMeta(2)
                .outputItems(AEItems.MATTER_BALL.asItem(), 64)
                .EUt(491520)
                .duration(1)
                .save();

        MATTER_FABRICATOR_RECIPES.recipeBuilder("uu_amplifier_2")
                .inputItems(GTOItems.SCRAP_BOX.asItem())
                .circuitMeta(2)
                .outputItems(AEItems.MATTER_BALL.asItem(), 576)
                .EUt(1966080)
                .duration(1)
                .save();

        MATTER_FABRICATOR_RECIPES.recipeBuilder("uu_amplifier_a")
                .inputItems(GTOItems.SCRAP_BOX.asItem())
                .circuitMeta(1)
                .outputFluids(GTOMaterials.UuAmplifier.getFluid(9))
                .EUt(1966080)
                .duration(200)
                .save();
    }
}
