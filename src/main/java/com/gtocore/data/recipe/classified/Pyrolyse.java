package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.PYROLYSE_RECIPES;

final class Pyrolyse {

    public static void init() {
        PYROLYSE_RECIPES.recipeBuilder("rawradox1")
                .inputItems(GTOBlocks.VARIATION_WOOD.asItem(), 16)
                .circuitMeta(1)
                .inputFluids(GTOMaterials.EnrichedXenoxene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Ash)
                .outputFluids(GTOMaterials.RawRadox.getFluid(10000))
                .EUt(7864320)
                .duration(600)
                .save();

        PYROLYSE_RECIPES.recipeBuilder("rawradox")
                .inputItems(GTOBlocks.VARIATION_WOOD.asItem(), 16)
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Xenoxene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Ash)
                .outputFluids(GTOMaterials.RawRadox.getFluid(1000))
                .EUt(491520)
                .duration(900)
                .save();
    }
}
