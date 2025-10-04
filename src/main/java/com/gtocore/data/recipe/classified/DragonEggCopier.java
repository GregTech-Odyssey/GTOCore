package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOMaterials;

import com.gtolib.api.machine.GTOCleanroomType;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import static com.gtocore.common.data.GTORecipeTypes.DRAGON_EGG_COPIER_RECIPES;

final class DragonEggCopier {

    public static void init() {
        DRAGON_EGG_COPIER_RECIPES.recipeBuilder("dragon_egg_copier")
                .inputItems(Blocks.DRAGON_EGG.asItem())
                .inputFluids(GTOMaterials.BiohmediumSterilized.getFluid(100))
                .outputItems(Blocks.DRAGON_EGG.asItem())
                .chancedOutput(new ItemStack(Blocks.DRAGON_EGG.asItem()), 2000, 1000)
                .EUt(122880)
                .duration(200)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save();
    }
}
