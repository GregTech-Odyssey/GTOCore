package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gtolib.GTOCore;
import com.gtolib.utils.TagUtils;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;

import net.minecraft.world.level.block.Blocks;

import static com.gtocore.common.data.GTORecipeTypes.LOOM_RECIPES;

final class Loom {

    public static void init() {
        LOOM_RECIPES.recipeBuilder("string")
                .inputItems(GTOItems.PLANT_FIBER, 4)
                .outputItems(Blocks.TRIPWIRE.asItem())
                .EUt(120)
                .duration(200)
                .save();

        LOOM_RECIPES.recipeBuilder("plant_fiber")
                .inputItems(GTItems.PLANT_BALL)
                .outputItems(GTOItems.PLANT_FIBER, 2)
                .EUt(7)
                .duration(200)
                .save();

        LOOM_RECIPES.recipeBuilder("gold_algae_fiber")
                .inputItems(GTOItems.GOLD_ALGAE, 3)
                .outputItems(GTOItems.GOLD_ALGAE_FIBER)
                .EUt(30)
                .duration(240)
                .save();

        LOOM_RECIPES.recipeBuilder("green_algae_fiber")
                .inputItems(GTOItems.GREEN_ALGAE, 3)
                .outputItems(GTOItems.GREEN_ALGAE_FIBER)
                .EUt(30)
                .duration(240)
                .save();

        LOOM_RECIPES.recipeBuilder("red_algae_fiber")
                .inputItems(GTOItems.RED_ALGAE, 3)
                .outputItems(GTOItems.RED_ALGAE_FIBER)
                .EUt(30)
                .duration(240)
                .save();

        LOOM_RECIPES.recipeBuilder("algae_plant_fiber")
                .inputItems(TagUtils.createItemTag(GTOCore.id("algae_fiber")))
                .outputItems(GTOItems.PLANT_FIBER)
                .EUt(30)
                .duration(20)
                .save();

        // LOOM_RECIPES.recipeBuilder("woven_kevlar")
        // .inputItems(GTOItems.KEVLAR_FIBER, 8)
        // .outputItems(GTOItems.WOVEN_KEVLAR)
        // .EUt(120)
        // .duration(200)
        // .cleanroom(CleanroomType.CLEANROOM)
        // .save();

        LOOM_RECIPES.recipeBuilder("nanotube_spool")
                .inputItems(TagPrefix.wireFine, GTOMaterials.CarbonNanotubes, 16)
                .outputItems(GTOItems.NANOTUBE_SPOOL)
                .EUt(7680)
                .duration(200)
                .save();

        LOOM_RECIPES.builder("fluix_covered_dense_cable")
                .inputItems("ae2:fluix_covered_cable", 4)
                .outputItems("ae2:fluix_covered_dense_cable")
                .EUt(32)
                .duration(5)
                .save();

        LOOM_RECIPES.builder("fluix_smart_dense_cable")
                .inputItems("ae2:fluix_smart_cable", 4)
                .outputItems("ae2:fluix_smart_dense_cable")
                .EUt(32)
                .duration(5)
                .save();

        LOOM_RECIPES.builder("magebloom_fiber")
                .inputItems("ars_nouveau:magebloom")
                .outputItems("ars_nouveau:magebloom_fiber", 16)
                .EUt(30)
                .duration(200)
                .save();
    }
}
