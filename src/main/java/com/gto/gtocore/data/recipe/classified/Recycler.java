package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTORecipeTypes;
import com.gto.gtocore.utils.TagUtils;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface Recycler {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.RECYCLER_RECIPES.recipeBuilder(GTOCore.id("recycler_a"))
                .inputItems(TagUtils.createTGTag("ingots"))
                .outputItems(GTOItems.SCRAP.asStack())
                .EUt(30)
                .duration(200)
                .save(provider);

        GTORecipeTypes.RECYCLER_RECIPES.recipeBuilder(GTOCore.id("recycler_b"))
                .inputItems(TagUtils.createTGTag("storage_blocks"))
                .outputItems(GTOItems.SCRAP.asStack(9))
                .EUt(120)
                .duration(200)
                .save(provider);

        GTORecipeTypes.RECYCLER_RECIPES.recipeBuilder(GTOCore.id("recycler_c"))
                .inputItems(TagUtils.createTGTag("gems"))
                .outputItems(GTOItems.SCRAP.asStack())
                .EUt(30)
                .duration(200)
                .save(provider);
    }
}
