package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;

import static com.gto.gtocore.common.data.GTORecipeTypes.BIOCHEMICAL_EXTRACTION_RECIPES;

interface BiochemicalExtraction {

    static void init() {
        BIOCHEMICAL_EXTRACTION_RECIPES.recipeBuilder(GTOCore.id("cow_spawn_egg"))
                .inputItems(Items.COW_SPAWN_EGG.asItem())
                .outputFluids(GTMaterials.Milk.getFluid(1000))
                .EUt(120)
                .duration(200)
                .save();
    }
}
