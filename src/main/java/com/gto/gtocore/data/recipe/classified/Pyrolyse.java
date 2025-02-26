package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOBlocks;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface Pyrolyse {

    static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.PYROLYSE_RECIPES.recipeBuilder(GTOCore.id("rawradox1"))
                .inputItems(GTOBlocks.VARIATION_WOOD.asStack(16))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.EnrichedXenoxene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Ash)
                .outputFluids(GTOMaterials.RawRadox.getFluid(10000))
                .EUt(7864320)
                .duration(600)
                .save(provider);

        GTRecipeTypes.PYROLYSE_RECIPES.recipeBuilder(GTOCore.id("rawradox"))
                .inputItems(GTOBlocks.VARIATION_WOOD.asStack(16))
                .circuitMeta(1)
                .inputFluids(GTOMaterials.Xenoxene.getFluid(1000))
                .outputItems(TagPrefix.dust, GTMaterials.Ash)
                .outputFluids(GTOMaterials.RawRadox.getFluid(1000))
                .EUt(491520)
                .duration(900)
                .save(provider);
    }
}
