package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GCYMRecipeTypes;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface AlloyBlast {

    static void init(Consumer<FinishedRecipe> provider) {
        GCYMRecipeTypes.ALLOY_BLAST_RECIPES.recipeBuilder(GTOCore.id("carbon_disulfide"))
                .circuitMeta(8)
                .inputItems(TagPrefix.dust, GTMaterials.Carbon)
                .inputItems(TagPrefix.dust, GTMaterials.Sulfur, 2)
                .outputFluids(GTOMaterials.CarbonDisulfide.getFluid(1000))
                .EUt(120)
                .duration(350)
                .blastFurnaceTemp(1200)
                .save(provider);
    }
}
