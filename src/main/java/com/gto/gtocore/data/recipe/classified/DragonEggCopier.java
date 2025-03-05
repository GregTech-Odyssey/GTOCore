package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.machine.GTOCleanroomType;
import com.gto.gtocore.common.data.GTOMaterials;
import com.gto.gtocore.common.data.GTORecipeTypes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

interface DragonEggCopier {

    static void init() {
        GTORecipeTypes.DRAGON_EGG_COPIER_RECIPES.recipeBuilder(GTOCore.id("dragon_egg_copier"))
                .inputItems(new ItemStack(Blocks.DRAGON_EGG.asItem()))
                .inputFluids(GTOMaterials.BiohmediumSterilized.getFluid(100))
                .outputItems(new ItemStack(Blocks.DRAGON_EGG.asItem()))
                .chancedOutput(new ItemStack(Blocks.DRAGON_EGG.asItem()), 2000, 1000)
                .EUt(122880)
                .duration(200)
                .cleanroom(GTOCleanroomType.LAW_CLEANROOM)
                .save();
    }
}
