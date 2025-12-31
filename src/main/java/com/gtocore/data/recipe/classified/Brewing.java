package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.machine.GTOCleanroomType;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gtocore.common.data.GTORecipeTypes.BREWING_RECIPES;

final class Brewing {

    public static void init() {
        BREWING_RECIPES.recipeBuilder("dragon_blood")
                .inputItems(GTOItems.DRAGON_CELLS)
                .inputFluids(GTMaterials.SterileGrowthMedium, 1000)
                .outputFluids(GTOMaterials.DragonBlood, 1000)
                .EUt(480)
                .duration(6000)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save();
    }
}
