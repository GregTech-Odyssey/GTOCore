package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface Extruder {

    static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.EXTRUDER_RECIPES.recipeBuilder(GTOCore.id("special_ceramics"))
                .inputItems(TagPrefix.dust, GTOMaterials.SpecialCeramics, 2)
                .notConsumable(GTItems.SHAPE_EXTRUDER_PLATE.asStack())
                .outputItems(GTOItems.SPECIAL_CERAMICS.asStack())
                .EUt(480)
                .duration(20)
                .save(provider);
    }
}
