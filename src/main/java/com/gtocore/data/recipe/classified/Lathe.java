package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;

import static com.gtocore.common.data.GTORecipeTypes.LATHE_RECIPES;

final class Lathe {

    public static void init() {
        LATHE_RECIPES.recipeBuilder("non_linear_optical_lens")
                .inputItems(GTOItems.PERIODICALLY_POLED_LITHIUM_NIOBATE_BOULE)
                .outputItems(GTOItems.NON_LINEAR_OPTICAL_LENS)
                .EUt(1966080)
                .duration(360)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LATHE_RECIPES.recipeBuilder("magmatter_rod")
                .inputItems(TagPrefix.ingot, GTOMaterials.Magmatter)
                .outputItems(TagPrefix.rod, GTOMaterials.Magmatter)
                .outputItems(TagPrefix.dustSmall, GTOMaterials.Magmatter)
                .EUt(2013265920)
                .duration(200)
                .save();
    }
}
