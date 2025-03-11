package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.utils.RLUtils;
import com.gto.gtocore.utils.TagUtils;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.LAVA_FURNACE_RECIPES;

interface LavaFurnace {

    static void init() {
        LAVA_FURNACE_RECIPES.recipeBuilder(GTOCore.id("lava_furnace"))
                .inputItems(TagUtils.createTag(RLUtils.forge("cobblestone")))
                .outputFluids(GTMaterials.Lava.getFluid(1000))
                .EUt(16)
                .duration(200)
                .save();

        LAVA_FURNACE_RECIPES.recipeBuilder(GTOCore.id("lava_furnace1"))
                .inputItems(TagUtils.createTag(RLUtils.forge("stone")))
                .outputFluids(GTMaterials.Lava.getFluid(1000))
                .EUt(16)
                .duration(200)
                .save();
    }
}
