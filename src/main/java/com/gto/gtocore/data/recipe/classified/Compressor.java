package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.api.data.tag.GTOTagPrefix;
import com.gto.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import committee.nova.mods.avaritia.init.registry.ModBlocks;

import static com.gto.gtocore.common.data.GTORecipeTypes.COMPRESSOR_RECIPES;

interface Compressor {

    static void init() {
        COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("compressed_crafting_table"))
                .inputItems(new ItemStack(Blocks.CRAFTING_TABLE.asItem(), 64))
                .outputItems(ModBlocks.compressed_crafting_table.get().asItem())
                .EUt(480)
                .duration(200)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("spacetime_block"))
                .inputItems(TagPrefix.ingot, GTOMaterials.SpaceTime, 9)
                .outputItems(TagPrefix.block, GTOMaterials.SpaceTime)
                .EUt(2013265920)
                .duration(3000)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("spongebob"))
                .inputItems(TagPrefix.foil, GTMaterials.Polycaprolactam, 2)
                .outputItems(new ItemStack(Blocks.SPONGE.asItem()))
                .EUt(2)
                .duration(200)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("graphite_ingot"))
                .inputItems(TagPrefix.dust, GTMaterials.Graphite)
                .outputItems(TagPrefix.ingot, GTMaterials.Graphite)
                .EUt(30)
                .duration(300)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("bedrock"))
                .inputItems(TagPrefix.block, GTOMaterials.Bedrockium)
                .outputItems(new ItemStack(Blocks.BEDROCK.asItem()))
                .EUt(31457280)
                .duration(4000)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("netherite_block"))
                .inputItems(TagPrefix.ingot, GTMaterials.Netherite, 9)
                .outputItems(new ItemStack(Blocks.NETHERITE_BLOCK.asItem()))
                .EUt(2)
                .duration(300)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("alumina_ceramic"))
                .inputItems(TagPrefix.dust, GTOMaterials.AluminaCeramic, 10)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.AluminaCeramic)
                .EUt(120)
                .duration(800)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("superheavy_mix"))
                .inputItems(TagPrefix.dust, GTOMaterials.SuperheavyMix, 9)
                .outputItems(TagPrefix.block, GTOMaterials.SuperheavyMix)
                .EUt(524288)
                .duration(200)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder(GTOCore.id("diamond_lattice_block"))
                .inputItems("avaritia:diamond_lattice", 9)
                .outputItems("avaritia:diamond_lattice_block")
                .EUt(1920)
                .duration(400)
                .save();
    }
}
