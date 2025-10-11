package com.gtocore.data.recipe.classified;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;

import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEItems;
import committee.nova.mods.avaritia.init.registry.ModBlocks;

import static com.gtocore.common.data.GTORecipeTypes.COMPRESSOR_RECIPES;

final class Compressor {

    public static void init() {
        COMPRESSOR_RECIPES.recipeBuilder("compressed_crafting_table")
                .inputItems(Blocks.CRAFTING_TABLE.asItem(), 64)
                .outputItems(ModBlocks.compressed_crafting_table.get().asItem())
                .EUt(480)
                .duration(200)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("spacetime_block")
                .inputItems(TagPrefix.ingot, GTOMaterials.SpaceTime, 9)
                .outputItems(TagPrefix.block, GTOMaterials.SpaceTime)
                .EUt(2013265920)
                .duration(3000)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("spongebob")
                .inputItems(TagPrefix.foil, GTMaterials.Polycaprolactam, 2)
                .outputItems(Blocks.SPONGE.asItem())
                .EUt(2)
                .duration(200)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("graphite_ingot")
                .inputItems(TagPrefix.dust, GTMaterials.Graphite)
                .outputItems(TagPrefix.ingot, GTMaterials.Graphite)
                .EUt(30)
                .duration(300)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("bedrock")
                .inputItems(TagPrefix.block, GTOMaterials.Bedrockium)
                .outputItems(Blocks.BEDROCK.asItem())
                .EUt(31457280)
                .duration(4000)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("netherite_block")
                .inputItems(TagPrefix.ingot, GTMaterials.Netherite, 9)
                .outputItems(Blocks.NETHERITE_BLOCK.asItem())
                .EUt(2)
                .duration(300)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("alumina_ceramic")
                .inputItems(TagPrefix.dust, GTOMaterials.AluminaCeramic, 10)
                .outputItems(GTOTagPrefix.ROUGH_BLANK, GTOMaterials.AluminaCeramic)
                .EUt(120)
                .duration(800)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("superheavy_mix")
                .inputItems(TagPrefix.dust, GTOMaterials.SuperheavyMix, 9)
                .outputItems(TagPrefix.block, GTOMaterials.SuperheavyMix)
                .EUt(524288)
                .duration(200)
                .save();

        COMPRESSOR_RECIPES.recipeBuilder("diamond_lattice_block")
                .inputItems("avaritia:diamond_lattice", 9)
                .outputItems("avaritia:diamond_lattice_block")
                .EUt(1920)
                .duration(400)
                .save();

        COMPRESSOR_RECIPES.builder("dense_steel_plate")
                .inputItems(TagPrefix.plate, GTMaterials.Steel, 9)
                .outputItems(TagPrefix.plateDense, GTMaterials.Steel)
                .EUt(30)
                .duration(800)
                .save();

        COMPRESSOR_RECIPES.builder("honey_block")
                .inputItems(Items.HONEY_BOTTLE, 4)
                .outputItems(Items.HONEY_BLOCK)
                .EUt(30)
                .duration(100)
                .save();

        COMPRESSOR_RECIPES.builder("blasting_industrial_components_0")
                .inputItems(AEItems.MATTER_BALL.asItem(), 64)
                .outputItems(GTOItems.INDUSTRIAL_COMPONENTS[0][0])
                .EUt(30)
                .duration(20)
                .save();

        COMPRESSOR_RECIPES.builder("blasting_industrial_components_1")
                .inputItems(GTOItems.INDUSTRIAL_COMPONENTS[0][0], 5)
                .outputItems(GTOItems.INDUSTRIAL_COMPONENTS[0][1])
                .EUt(30)
                .duration(20)
                .save();

        COMPRESSOR_RECIPES.builder("blasting_industrial_components_2")
                .inputItems(GTOItems.INDUSTRIAL_COMPONENTS[0][1], 5)
                .outputItems(GTOItems.INDUSTRIAL_COMPONENTS[0][2])
                .EUt(30)
                .duration(20)
                .save();

        COMPRESSOR_RECIPES.builder("extended_industrial_components_0")
                .inputItems(AEItems.SINGULARITY.asItem(), 64)
                .outputItems(GTOItems.INDUSTRIAL_COMPONENTS[1][0])
                .EUt(480)
                .duration(20)
                .save();

        COMPRESSOR_RECIPES.builder("extended_industrial_components_1")
                .inputItems(GTOItems.INDUSTRIAL_COMPONENTS[1][0], 5)
                .outputItems(GTOItems.INDUSTRIAL_COMPONENTS[1][1])
                .EUt(480)
                .duration(20)
                .save();

        COMPRESSOR_RECIPES.builder("extended_industrial_components_2")
                .inputItems(GTOItems.INDUSTRIAL_COMPONENTS[1][1], 5)
                .outputItems(GTOItems.INDUSTRIAL_COMPONENTS[1][2])
                .EUt(480)
                .duration(20)
                .save();

        COMPRESSOR_RECIPES.builder("special_industrial_components_0")
                .inputItems("avaritia:neutron_pile", 64)
                .outputItems(GTOItems.INDUSTRIAL_COMPONENTS[2][0])
                .EUt(7680)
                .duration(20)
                .save();

        COMPRESSOR_RECIPES.builder("special_industrial_components_1")
                .inputItems(GTOItems.INDUSTRIAL_COMPONENTS[2][0], 5)
                .outputItems(GTOItems.INDUSTRIAL_COMPONENTS[2][1])
                .EUt(7680)
                .duration(20)
                .save();

        COMPRESSOR_RECIPES.builder("special_industrial_components_2")
                .inputItems(GTOItems.INDUSTRIAL_COMPONENTS[2][1], 5)
                .outputItems(GTOItems.INDUSTRIAL_COMPONENTS[2][2])
                .EUt(7680)
                .duration(20)
                .save();
    }
}
