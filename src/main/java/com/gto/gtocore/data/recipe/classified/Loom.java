package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTOItems;
import com.gto.gtocore.utils.TagUtils;

import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import static com.gto.gtocore.common.data.GTORecipeTypes.LOOM_RECIPES;

interface Loom {

    static void init() {
        LOOM_RECIPES.recipeBuilder(GTOCore.id("string"))
                .inputItems(GTOItems.PLANT_FIBER.asStack(4))
                .outputItems(new ItemStack(Blocks.TRIPWIRE.asItem()))
                .EUt(120)
                .duration(200)
                .save();

        LOOM_RECIPES.recipeBuilder(GTOCore.id("plant_fiber"))
                .inputItems(GTItems.PLANT_BALL.asItem())
                .outputItems(GTOItems.PLANT_FIBER.asStack(2))
                .EUt(7)
                .duration(200)
                .save();

        LOOM_RECIPES.recipeBuilder(GTOCore.id("gold_algae_fiber"))
                .inputItems(GTOItems.GOLD_ALGAE.asStack(3))
                .outputItems(GTOItems.GOLD_ALGAE_FIBER.asItem())
                .EUt(30)
                .duration(240)
                .save();

        LOOM_RECIPES.recipeBuilder(GTOCore.id("green_algae_fiber"))
                .inputItems(GTOItems.GREEN_ALGAE.asStack(3))
                .outputItems(GTOItems.GREEN_ALGAE_FIBER.asItem())
                .EUt(30)
                .duration(240)
                .save();

        LOOM_RECIPES.recipeBuilder(GTOCore.id("red_algae_fiber"))
                .inputItems(GTOItems.RED_ALGAE.asStack(3))
                .outputItems(GTOItems.RED_ALGAE_FIBER.asItem())
                .EUt(30)
                .duration(240)
                .save();

        LOOM_RECIPES.recipeBuilder(GTOCore.id("algae_plant_fiber"))
                .inputItems(TagUtils.createTag(GTOCore.id("algae_fiber")))
                .outputItems(GTOItems.PLANT_FIBER.asItem())
                .EUt(30)
                .duration(20)
                .save();
    }
}
