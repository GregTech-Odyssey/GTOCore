package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.GTOCleanroomType;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeTypes;

import net.minecraft.data.recipes.FinishedRecipe;

import java.util.function.Consumer;

interface Brewing {

    static void init(Consumer<FinishedRecipe> provider) {
        GTRecipeTypes.BREWING_RECIPES.recipeBuilder(GTOCore.id("dragon_blood"))
                .inputItems(GTOItems.DRAGON_CELLS.asStack())
                .inputFluids(GTMaterials.SterileGrowthMedium.getFluid(1000))
                .outputFluids(GTOMaterials.DragonBlood.getFluid(1000))
                .EUt(480)
                .duration(6000)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save(provider);
    }
}
