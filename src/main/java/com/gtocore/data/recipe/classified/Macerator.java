package com.gtocore.data.recipe.classified;

import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.data.tag.Tags;

import com.gtolib.GTOCore;
import com.gtolib.utils.TagUtils;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import com.enderio.base.common.init.EIOItems;

import static com.gtocore.common.data.GTORecipeTypes.MACERATOR_RECIPES;

final class Macerator {

    public static void init() {
        MACERATOR_RECIPES.recipeBuilder("prescient_powder")
                .inputItems(EIOItems.PRESCIENT_CRYSTAL.asItem())
                .outputItems(EIOItems.PRESCIENT_POWDER.asItem())
                .EUt(30)
                .duration(200)
                .save();

        MACERATOR_RECIPES.recipeBuilder("spacetime_dust")
                .inputItems(TagPrefix.ingot, GTOMaterials.SpaceTime)
                .outputItems(TagPrefix.dust, GTOMaterials.SpaceTime)
                .EUt(2013265920)
                .duration(400)
                .save();

        MACERATOR_RECIPES.recipeBuilder("essence")
                .inputItems(GTOBlocks.ESSENCE_BLOCK.asItem())
                .outputItems(GTOItems.ESSENCE.asItem())
                .chancedOutput(GTOItems.ESSENCE.asStack(), 5000, 400)
                .chancedOutput(GTOItems.ESSENCE.asStack(), 5000, 200)
                .chancedOutput(GTOItems.ESSENCE.asStack(), 5000, 100)
                .EUt(30)
                .duration(200)
                .save();

        MACERATOR_RECIPES.recipeBuilder("silicon_dust")
                .inputItems(new ItemStack(AEItems.SILICON.asItem()))
                .outputItems(TagPrefix.dust, GTMaterials.Silicon)
                .EUt(16)
                .duration(200)
                .save();

        MACERATOR_RECIPES.recipeBuilder("vibrant_powder")
                .inputItems(EIOItems.VIBRANT_CRYSTAL.asItem())
                .outputItems(EIOItems.VIBRANT_POWDER.asItem())
                .EUt(30)
                .duration(200)
                .save();

        MACERATOR_RECIPES.recipeBuilder("draconium_dust")
                .inputItems(GTOBlocks.DRACONIUM_BLOCK_CHARGED.asItem())
                .outputItems(GTOItems.DRACONIUM_DIRT.asStack(9))
                .outputItems(TagPrefix.dust, GTMaterials.Obsidian)
                .outputItems(TagPrefix.dust, GTMaterials.EnderEye)
                .outputItems(TagPrefix.dust, GTMaterials.Redstone)
                .EUt(1920)
                .duration(400)
                .save();

        MACERATOR_RECIPES.recipeBuilder("sculk_catalyst")
                .inputItems(new ItemStack(Blocks.SCULK_CATALYST.asItem()))
                .outputItems(TagPrefix.dust, GTMaterials.EchoShard)
                .EUt(7)
                .duration(100)
                .save();

        MACERATOR_RECIPES.recipeBuilder("sculk_shrieker")
                .inputItems(new ItemStack(Blocks.SCULK_SHRIEKER.asItem()))
                .outputItems(TagPrefix.dust, GTMaterials.EchoShard)
                .EUt(7)
                .duration(100)
                .save();

        MACERATOR_RECIPES.recipeBuilder("pulsating_powder")
                .inputItems(EIOItems.PULSATING_CRYSTAL.asItem())
                .outputItems(EIOItems.PULSATING_POWDER.asItem())
                .EUt(30)
                .duration(200)
                .save();

        MACERATOR_RECIPES.recipeBuilder("sculk_sensor")
                .inputItems(new ItemStack(Blocks.SCULK_SENSOR.asItem()))
                .outputItems(TagPrefix.dust, GTMaterials.EchoShard)
                .EUt(7)
                .duration(100)
                .save();

        MACERATOR_RECIPES.recipeBuilder("ender_crystal_powder")
                .inputItems(EIOItems.ENDER_CRYSTAL.asItem())
                .outputItems(EIOItems.ENDER_CRYSTAL_POWDER.asItem())
                .EUt(30)
                .duration(200)
                .save();

        MACERATOR_RECIPES.recipeBuilder("degenerate_rhenium_dust")
                .inputItems(TagPrefix.plate, GTOMaterials.DegenerateRhenium)
                .outputItems(TagPrefix.dust, GTOMaterials.DegenerateRhenium)
                .EUt(31457280)
                .duration(100)
                .save();

        MACERATOR_RECIPES.recipeBuilder("prismarine_shard")
                .inputItems(new ItemStack(Blocks.PRISMARINE.asItem()))
                .outputItems(new ItemStack(Items.PRISMARINE_SHARD.asItem()))
                .EUt(2)
                .duration(200)
                .save();

        MACERATOR_RECIPES.recipeBuilder("wheat_seeds")
                .inputItems(new ItemStack(Items.WHEAT.asItem()))
                .outputItems(new ItemStack(Blocks.WHEAT.asItem(), 8))
                .EUt(2)
                .duration(200)
                .save();

        MACERATOR_RECIPES.recipeBuilder("sky_dust")
                .inputItems(new ItemStack(AEBlocks.SKY_STONE_BLOCK.block().asItem()))
                .outputItems(new ItemStack(AEItems.SKY_DUST.asItem()))
                .EUt(2)
                .duration(200)
                .save();

        MACERATOR_RECIPES.recipeBuilder("treated_wood_dust")
                .inputItems(GTBlocks.TREATED_WOOD_PLANK.asItem())
                .outputItems(TagPrefix.dust, GTMaterials.TreatedWood)
                .EUt(2)
                .duration(98)
                .save();

        MACERATOR_RECIPES.recipeBuilder("algae_paper")
                .inputItems(TagUtils.createTag(GTOCore.id("algae_fiber")))
                .outputItems(TagPrefix.dust, GTMaterials.Paper)
                .duration(320)
                .EUt(2)
                .save();

        MACERATOR_RECIPES.recipeBuilder("yeast")
                .inputItems(Items.SWEET_BERRIES.asItem())
                .chancedOutput(TagPrefix.dust, GTOMaterials.Yeast, 500, 500)
                .EUt(30)
                .duration(50)
                .save();

        MACERATOR_RECIPES.builder("star_stone_dust")
                .inputItems(Tags.STAR_STONE)
                .outputItems(TagPrefix.dust, GTOMaterials.StarStone)
                .EUt(30)
                .duration(150)
                .save();
    }
}
