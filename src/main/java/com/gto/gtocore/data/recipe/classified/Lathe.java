package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.machine.multiblock.CleanroomType;

import static com.gto.gtocore.common.data.GTORecipeTypes.LATHE_RECIPES;

interface Lathe {

    static void init() {
        LATHE_RECIPES.recipeBuilder(GTOCore.id("non_linear_optical_lens"))
                .inputItems(GTOItems.PERIODICALLY_POLED_LITHIUM_NIOBATE_BOULE.asStack())
                .outputItems(GTOItems.NON_LINEAR_OPTICAL_LENS.asStack())
                .EUt(1966080)
                .duration(360)
                .cleanroom(CleanroomType.CLEANROOM)
                .save();

        LATHE_RECIPES.recipeBuilder(GTOCore.id("magmatter_rod"))
                .inputItems(TagPrefix.ingot, GTOMaterials.Magmatter)
                .outputItems(TagPrefix.rod, GTOMaterials.Magmatter)
                .outputItems(TagPrefix.dustSmall, GTOMaterials.Magmatter)
                .EUt(2013265920)
                .duration(200)
                .save();
    }
}
