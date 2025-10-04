package com.gtocore.data.recipe.classified;

import com.gtolib.api.recipe.RecipeBuilder;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import org.jetbrains.annotations.Nullable;

import static com.gtocore.common.data.GTORecipeTypes.GREENHOUSE_RECIPES;

final class Greenhouse {

    public static void init() {
        GREENHOUSE_RECIPES.recipeBuilder("azure_bluet")
                .notConsumable(Blocks.AZURE_BLUET.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.AZURE_BLUET.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("blue_orchid_fertilizer")
                .notConsumable(Blocks.BLUE_ORCHID.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.BLUE_ORCHID.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("sweet_berries_fertilizer")
                .notConsumable(Blocks.SWEET_BERRY_BUSH.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.SWEET_BERRY_BUSH.asItem(), 32)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("red_tulip")
                .notConsumable(Blocks.RED_TULIP.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.RED_TULIP.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("cactus_fertilizer")
                .notConsumable(Blocks.CACTUS.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.CACTUS.asItem(), 24)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("wither_rose")
                .notConsumable(Blocks.WITHER_ROSE.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.WITHER_ROSE.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("beetroot_seeds_fertilizer")
                .notConsumable(Blocks.BEETROOTS.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Items.BEETROOT.asItem(), 32)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("spore_blossom_fertilizer")
                .notConsumable(Blocks.SPORE_BLOSSOM.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.SPORE_BLOSSOM.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("orange_tulip_fertilizer")
                .notConsumable(Blocks.ORANGE_TULIP.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.ORANGE_TULIP.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("lilac")
                .notConsumable(Blocks.LILAC.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.LILAC.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("pink_petals_fertilizer")
                .notConsumable(Blocks.PINK_PETALS.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.PINK_PETALS.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("sea_pickle")
                .notConsumable(Blocks.SEA_PICKLE.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.SEA_PICKLE.asItem(), 16)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("pink_petals")
                .notConsumable(Blocks.PINK_PETALS.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.PINK_PETALS.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("pitcher_plant")
                .notConsumable(Blocks.PITCHER_PLANT.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.PITCHER_PLANT.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("orange_tulip")
                .notConsumable(Blocks.ORANGE_TULIP.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.ORANGE_TULIP.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("carrot")
                .notConsumable(Blocks.CARROTS.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.CARROTS.asItem(), 12)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("tall_grass")
                .notConsumable(Blocks.TALL_GRASS.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.TALL_GRASS.asItem(), 12)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("dandelion")
                .notConsumable(Blocks.DANDELION.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.DANDELION.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("torchflower")
                .notConsumable(Items.TORCHFLOWER_SEEDS.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.TORCHFLOWER.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("nether_wart_fertilizer")
                .notConsumable(Blocks.NETHER_WART.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.NETHER_WART.asItem(), 24)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("poppy")
                .notConsumable(Blocks.POPPY.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.POPPY.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("oxeye_daisy_fertilizer")
                .notConsumable(Blocks.OXEYE_DAISY.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.OXEYE_DAISY.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("carrot_fertilizer")
                .notConsumable(Blocks.CARROTS.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.CARROTS.asItem(), 24)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("red_mushroom_fertilizer")
                .notConsumable(Blocks.RED_MUSHROOM.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.RED_MUSHROOM.asItem(), 24)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("sugar_cane_fertilizer")
                .notConsumable(Blocks.SUGAR_CANE.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.SUGAR_CANE.asItem(), 24)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("white_tulip")
                .notConsumable(Blocks.WHITE_TULIP.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.WHITE_TULIP.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("sweet_berries")
                .notConsumable(Blocks.SWEET_BERRY_BUSH.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.SWEET_BERRY_BUSH.asItem(), 16)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("lily_of_the_valley")
                .notConsumable(Blocks.LILY_OF_THE_VALLEY.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.LILY_OF_THE_VALLEY.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("blue_orchid")
                .notConsumable(Blocks.BLUE_ORCHID.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.BLUE_ORCHID.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("wheat_seeds_fertilizer")
                .notConsumable(Blocks.WHEAT.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Items.WHEAT.asItem(), 32)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("rose_bush_fertilizer")
                .notConsumable(Blocks.ROSE_BUSH.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.ROSE_BUSH.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("potato_fertilizer")
                .notConsumable(Blocks.POTATOES.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.POTATOES.asItem(), 24)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("kelp_fertilizer")
                .notConsumable(Blocks.KELP.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.KELP.asItem(), 24)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("pumpkin_seeds_fertilizer")
                .notConsumable(Blocks.PUMPKIN_STEM.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.PUMPKIN.asItem(), 12)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("wither_rose_fertilizer")
                .notConsumable(Blocks.WITHER_ROSE.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.WITHER_ROSE.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("wheat_seeds")
                .notConsumable(Blocks.WHEAT.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Items.WHEAT.asItem(), 16)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("fern")
                .notConsumable(Blocks.FERN.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.FERN.asItem(), 16)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("cocoa_beans_fertilizer")
                .notConsumable(Blocks.COCOA.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.COCOA.asItem(), 24)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("sugar_cane")
                .notConsumable(Blocks.SUGAR_CANE.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.SUGAR_CANE.asItem(), 12)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("allium_fertilizer")
                .notConsumable(Blocks.ALLIUM.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.ALLIUM.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("glow_berries")
                .notConsumable(Blocks.CAVE_VINES.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.CAVE_VINES.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("brown_mushroom_fertilizer")
                .notConsumable(Blocks.BROWN_MUSHROOM.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.BROWN_MUSHROOM.asItem(), 24)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("sunflower")
                .notConsumable(Blocks.SUNFLOWER.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.SUNFLOWER.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("brown_mushroom")
                .notConsumable(Blocks.BROWN_MUSHROOM.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.BROWN_MUSHROOM.asItem(), 12)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("azure_bluet_fertilizer")
                .notConsumable(Blocks.AZURE_BLUET.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.AZURE_BLUET.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("grass")
                .notConsumable(Blocks.GRASS.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.GRASS.asItem(), 24)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("peony_fertilizer")
                .notConsumable(Blocks.PEONY.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.PEONY.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("dandelion_fertilizer")
                .notConsumable(Blocks.DANDELION.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.DANDELION.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("sunflower_fertilizer")
                .notConsumable(Blocks.SUNFLOWER.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.SUNFLOWER.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("oxeye_daisy")
                .notConsumable(Blocks.OXEYE_DAISY.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.OXEYE_DAISY.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("red_mushroom")
                .notConsumable(Blocks.RED_MUSHROOM.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.RED_MUSHROOM.asItem(), 12)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("pumpkin_seeds")
                .notConsumable(Blocks.PUMPKIN_STEM.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.PUMPKIN.asItem(), 6)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("bamboo")
                .notConsumable(Blocks.BAMBOO.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.BAMBOO.asItem(), 16)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("glow_berries_fertilizer")
                .notConsumable(Blocks.CAVE_VINES.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.CAVE_VINES.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("tall_grass_fertilizer")
                .notConsumable(Blocks.TALL_GRASS.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.TALL_GRASS.asItem(), 24)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("lilac_fertilizer")
                .notConsumable(Blocks.LILAC.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.LILAC.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("grass_fertilizer")
                .notConsumable(Blocks.GRASS.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.GRASS.asItem(), 48)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("peony")
                .notConsumable(Blocks.PEONY.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.PEONY.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("kelp")
                .notConsumable(Blocks.KELP.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.KELP.asItem(), 12)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("melon_seeds")
                .notConsumable(Blocks.MELON_STEM.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.MELON.asItem(), 6)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("potato")
                .notConsumable(Blocks.POTATOES.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.POTATOES.asItem(), 12)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("allium")
                .notConsumable(Blocks.ALLIUM.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.ALLIUM.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("pink_tulip_fertilizer")
                .notConsumable(Blocks.PINK_TULIP.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.PINK_TULIP.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("rose_bush")
                .notConsumable(Blocks.ROSE_BUSH.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.ROSE_BUSH.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("vine")
                .notConsumable(Blocks.VINE.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.VINE.asItem(), 16)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("pink_tulip")
                .notConsumable(Blocks.PINK_TULIP.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.PINK_TULIP.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("torchflower_fertilizer")
                .notConsumable(Items.TORCHFLOWER_SEEDS.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.TORCHFLOWER.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("sea_pickle_fertilizer")
                .notConsumable(Blocks.SEA_PICKLE.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.SEA_PICKLE.asItem(), 32)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("beetroot_seeds")
                .notConsumable(Blocks.BEETROOTS.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Items.BEETROOT.asItem(), 16)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("poppy_fertilizer")
                .notConsumable(Blocks.POPPY.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.POPPY.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("pitcher_plant_fertilizer")
                .notConsumable(Blocks.PITCHER_PLANT.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.PITCHER_PLANT.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("large_fern")
                .notConsumable(Blocks.LARGE_FERN.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.LARGE_FERN.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("white_tulip_fertilizer")
                .notConsumable(Blocks.WHITE_TULIP.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.WHITE_TULIP.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("bamboo_fertilizer")
                .notConsumable(Blocks.BAMBOO.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.BAMBOO.asItem(), 32)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("spore_blossom")
                .notConsumable(Blocks.SPORE_BLOSSOM.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.SPORE_BLOSSOM.asItem(), 8)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("cocoa_beans")
                .notConsumable(Blocks.COCOA.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.COCOA.asItem(), 12)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("large_fern_fertilizer")
                .notConsumable(Blocks.LARGE_FERN.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.LARGE_FERN.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("vine_fertilizer")
                .notConsumable(Blocks.VINE.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.VINE.asItem(), 32)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("nether_wart")
                .notConsumable(Blocks.NETHER_WART.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.NETHER_WART.asItem(), 12)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("fern_fertilizer")
                .notConsumable(Blocks.FERN.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.FERN.asItem(), 32)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("cactus")
                .notConsumable(Blocks.CACTUS.asItem())
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.CACTUS.asItem(), 12)
                .EUt(30)
                .duration(1200)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("lily_of_the_valley_fertilizer")
                .notConsumable(Blocks.LILY_OF_THE_VALLEY.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.LILY_OF_THE_VALLEY.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("melon_seeds_fertilizer")
                .notConsumable(Blocks.MELON_STEM.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.MELON.asItem(), 12)
                .EUt(60)
                .duration(400)
                .save();

        GREENHOUSE_RECIPES.recipeBuilder("red_tulip_fertilizer")
                .notConsumable(Blocks.RED_TULIP.asItem())
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(Blocks.RED_TULIP.asItem(), 16)
                .EUt(60)
                .duration(400)
                .save();

        add("minecraft", "crimson_fungus", null, 8);
        add("minecraft", "warped_fungus", null, 8);

        add("farmersdelight", "cabbage", "cabbage_seeds", 8);
        add("farmersdelight", "tomato", "tomato_seeds", 8);
        add("farmersdelight", "onion", null, 8);
        add("farmersdelight", "rice_panicle", "rice", 8);

        add("farmersrespite", "coffee_berries", "coffee_beans", 8);
        add("farmersrespite", "green_tea_leaves", null, 8);
        add("farmersrespite", "yellow_tea_leaves", null, 8);
        add("farmersrespite", "black_tea_leaves", null, 8);

        add("ars_nouveau", "sourceberry_bush", null, 8);
        add("ars_nouveau", "magebloom", "magebloom_crop", 4);
    }

    private static void add(String mod, String output, @Nullable String input, int count) {
        ItemStack stack1 = new ItemStack(RegistriesUtils.getItem(mod, output), count);
        ItemStack stack2 = input == null ? stack1.copyWithCount(1) : new ItemStack(RegistriesUtils.getItem(mod, input));
        RecipeBuilder builder = GREENHOUSE_RECIPES.recipeBuilder(output)
                .notConsumable(stack2)
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(stack1)
                .duration(1200);

        if (mod.equals("botania")) {
            builder.MANAt(4);
        } else {
            builder.EUt(30);
        }
        builder.save();

        builder = GREENHOUSE_RECIPES.recipeBuilder(output + "_fertilizer")
                .notConsumable(stack2)
                .inputItems(GTItems.FERTILIZER.asItem(), 4)
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(stack1.copyWithCount(count << 1))
                .duration(400);

        if (mod.equals("botania")) {
            builder.MANAt(8);
        } else {
            builder.EUt(60);
        }
        builder.save();
    }
}
