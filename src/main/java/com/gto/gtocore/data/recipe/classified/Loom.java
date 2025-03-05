package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

interface Loom {

    static void init() {
        GTORecipeTypes.LOOM_RECIPES.recipeBuilder(GTOCore.id("string"))
                .inputItems(GTOItems.PLANT_FIBER.asStack(4))
                .outputItems(new ItemStack(Blocks.TRIPWIRE.asItem()))
                .EUt(1920)
                .duration(20)
                .save();

        GTORecipeTypes.LOOM_RECIPES.recipeBuilder(GTOCore.id("plant_fiber"))
                .inputItems(GTItems.PLANT_BALL.asStack())
                .outputItems(GTOItems.PLANT_FIBER.asStack(2))
                .EUt(7)
                .duration(200)
                .save();
    }
}
