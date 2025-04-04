package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.GTOCleanroomType;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.common.data.GTMaterials;

import static com.gto.gtocore.common.data.GTORecipeTypes.BREWING_RECIPES;

interface Brewing {

    static void init() {
        BREWING_RECIPES.recipeBuilder(GTOCore.id("dragon_blood"))
                .inputItems(GTOItems.DRAGON_CELLS.asItem())
                .inputFluids(GTMaterials.SterileGrowthMedium.getFluid(1000))
                .outputFluids(GTOMaterials.DragonBlood.getFluid(1000))
                .EUt(480)
                .duration(6000)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save();
    }
}
