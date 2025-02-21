package com.gto.gtocore.data.recipe.classified;

import com.gto.gtocore.GTOCore;
import com.gto.gtocore.common.data.GTORecipeTypes;

import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

interface Greenhouse {

    static void init(Consumer<FinishedRecipe> provider) {
        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("azure_bluet"))
                .notConsumable(new ItemStack(Blocks.AZURE_BLUET.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.AZURE_BLUET.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("blue_orchid_fertilizer"))
                .notConsumable(new ItemStack(Blocks.BLUE_ORCHID.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.BLUE_ORCHID.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("sweet_berries_fertilizer"))
                .notConsumable(new ItemStack(Blocks.SWEET_BERRY_BUSH.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.SWEET_BERRY_BUSH.asItem(), 32))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("red_tulip"))
                .notConsumable(new ItemStack(Blocks.RED_TULIP.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.RED_TULIP.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("cactus_fertilizer"))
                .notConsumable(new ItemStack(Blocks.CACTUS.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.CACTUS.asItem(), 24))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("wither_rose"))
                .notConsumable(new ItemStack(Blocks.WITHER_ROSE.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.WITHER_ROSE.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("beetroot_seeds_fertilizer"))
                .notConsumable(new ItemStack(Blocks.BEETROOTS.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Items.BEETROOT.asItem(), 32))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("spore_blossom_fertilizer"))
                .notConsumable(new ItemStack(Blocks.SPORE_BLOSSOM.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.SPORE_BLOSSOM.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("orange_tulip_fertilizer"))
                .notConsumable(new ItemStack(Blocks.ORANGE_TULIP.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.ORANGE_TULIP.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("lilac"))
                .notConsumable(new ItemStack(Blocks.LILAC.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.LILAC.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("pink_petals_fertilizer"))
                .notConsumable(new ItemStack(Blocks.PINK_PETALS.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.PINK_PETALS.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("sea_pickle"))
                .notConsumable(new ItemStack(Blocks.SEA_PICKLE.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.SEA_PICKLE.asItem(), 16))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("pink_petals"))
                .notConsumable(new ItemStack(Blocks.PINK_PETALS.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.PINK_PETALS.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("pitcher_plant"))
                .notConsumable(new ItemStack(Blocks.PITCHER_PLANT.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.PITCHER_PLANT.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("orange_tulip"))
                .notConsumable(new ItemStack(Blocks.ORANGE_TULIP.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.ORANGE_TULIP.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("carrot"))
                .notConsumable(new ItemStack(Blocks.CARROTS.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.CARROTS.asItem(), 12))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("tall_grass"))
                .notConsumable(new ItemStack(Blocks.TALL_GRASS.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.TALL_GRASS.asItem(), 12))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("dandelion"))
                .notConsumable(new ItemStack(Blocks.DANDELION.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.DANDELION.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("torchflower"))
                .notConsumable(new ItemStack(Blocks.TORCHFLOWER.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.TORCHFLOWER.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("nether_wart_fertilizer"))
                .notConsumable(new ItemStack(Blocks.NETHER_WART.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.NETHER_WART.asItem(), 24))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("poppy"))
                .notConsumable(new ItemStack(Blocks.POPPY.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.POPPY.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("oxeye_daisy_fertilizer"))
                .notConsumable(new ItemStack(Blocks.OXEYE_DAISY.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.OXEYE_DAISY.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("carrot_fertilizer"))
                .notConsumable(new ItemStack(Blocks.CARROTS.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.CARROTS.asItem(), 24))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("red_mushroom_fertilizer"))
                .notConsumable(new ItemStack(Blocks.RED_MUSHROOM.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.RED_MUSHROOM.asItem(), 24))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("sugar_cane_fertilizer"))
                .notConsumable(new ItemStack(Blocks.SUGAR_CANE.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.SUGAR_CANE.asItem(), 24))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("white_tulip"))
                .notConsumable(new ItemStack(Blocks.WHITE_TULIP.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.WHITE_TULIP.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("sweet_berries"))
                .notConsumable(new ItemStack(Blocks.SWEET_BERRY_BUSH.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.SWEET_BERRY_BUSH.asItem(), 16))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("lily_of_the_valley"))
                .notConsumable(new ItemStack(Blocks.LILY_OF_THE_VALLEY.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.LILY_OF_THE_VALLEY.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("blue_orchid"))
                .notConsumable(new ItemStack(Blocks.BLUE_ORCHID.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.BLUE_ORCHID.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("wheat_seeds_fertilizer"))
                .notConsumable(new ItemStack(Blocks.WHEAT.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Items.WHEAT.asItem(), 32))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("rose_bush_fertilizer"))
                .notConsumable(new ItemStack(Blocks.ROSE_BUSH.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.ROSE_BUSH.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("potato_fertilizer"))
                .notConsumable(new ItemStack(Blocks.POTATOES.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.POTATOES.asItem(), 24))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("kelp_fertilizer"))
                .notConsumable(new ItemStack(Blocks.KELP.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.KELP.asItem(), 24))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("pumpkin_seeds_fertilizer"))
                .notConsumable(new ItemStack(Blocks.PUMPKIN_STEM.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.PUMPKIN.asItem(), 12))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("wither_rose_fertilizer"))
                .notConsumable(new ItemStack(Blocks.WITHER_ROSE.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.WITHER_ROSE.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("wheat_seeds"))
                .notConsumable(new ItemStack(Blocks.WHEAT.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Items.WHEAT.asItem(), 16))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("fern"))
                .notConsumable(new ItemStack(Blocks.FERN.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.FERN.asItem(), 16))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("cocoa_beans_fertilizer"))
                .notConsumable(new ItemStack(Blocks.COCOA.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.COCOA.asItem(), 24))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("sugar_cane"))
                .notConsumable(new ItemStack(Blocks.SUGAR_CANE.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.SUGAR_CANE.asItem(), 12))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("allium_fertilizer"))
                .notConsumable(new ItemStack(Blocks.ALLIUM.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.ALLIUM.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("glow_berries"))
                .notConsumable(new ItemStack(Blocks.CAVE_VINES.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.CAVE_VINES.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("brown_mushroom_fertilizer"))
                .notConsumable(new ItemStack(Blocks.BROWN_MUSHROOM.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.BROWN_MUSHROOM.asItem(), 24))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("sunflower"))
                .notConsumable(new ItemStack(Blocks.SUNFLOWER.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.SUNFLOWER.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("brown_mushroom"))
                .notConsumable(new ItemStack(Blocks.BROWN_MUSHROOM.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.BROWN_MUSHROOM.asItem(), 12))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("azure_bluet_fertilizer"))
                .notConsumable(new ItemStack(Blocks.AZURE_BLUET.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.AZURE_BLUET.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("grass"))
                .notConsumable(new ItemStack(Blocks.GRASS.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.GRASS.asItem(), 24))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("peony_fertilizer"))
                .notConsumable(new ItemStack(Blocks.PEONY.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.PEONY.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("dandelion_fertilizer"))
                .notConsumable(new ItemStack(Blocks.DANDELION.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.DANDELION.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("sunflower_fertilizer"))
                .notConsumable(new ItemStack(Blocks.SUNFLOWER.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.SUNFLOWER.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("oxeye_daisy"))
                .notConsumable(new ItemStack(Blocks.OXEYE_DAISY.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.OXEYE_DAISY.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("red_mushroom"))
                .notConsumable(new ItemStack(Blocks.RED_MUSHROOM.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.RED_MUSHROOM.asItem(), 12))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("pumpkin_seeds"))
                .notConsumable(new ItemStack(Blocks.PUMPKIN_STEM.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.PUMPKIN.asItem(), 6))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("bamboo"))
                .notConsumable(new ItemStack(Blocks.BAMBOO.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.BAMBOO.asItem(), 16))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("glow_berries_fertilizer"))
                .notConsumable(new ItemStack(Blocks.CAVE_VINES.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.CAVE_VINES.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("tall_grass_fertilizer"))
                .notConsumable(new ItemStack(Blocks.TALL_GRASS.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.TALL_GRASS.asItem(), 24))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("lilac_fertilizer"))
                .notConsumable(new ItemStack(Blocks.LILAC.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.LILAC.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("grass_fertilizer"))
                .notConsumable(new ItemStack(Blocks.GRASS.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.GRASS.asItem(), 48))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("peony"))
                .notConsumable(new ItemStack(Blocks.PEONY.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.PEONY.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("kelp"))
                .notConsumable(new ItemStack(Blocks.KELP.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.KELP.asItem(), 12))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("melon_seeds"))
                .notConsumable(new ItemStack(Blocks.MELON_STEM.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.MELON.asItem(), 6))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("potato"))
                .notConsumable(new ItemStack(Blocks.POTATOES.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.POTATOES.asItem(), 12))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("allium"))
                .notConsumable(new ItemStack(Blocks.ALLIUM.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.ALLIUM.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("pink_tulip_fertilizer"))
                .notConsumable(new ItemStack(Blocks.PINK_TULIP.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.PINK_TULIP.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("rose_bush"))
                .notConsumable(new ItemStack(Blocks.ROSE_BUSH.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.ROSE_BUSH.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("vine"))
                .notConsumable(new ItemStack(Blocks.VINE.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.VINE.asItem(), 16))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("pink_tulip"))
                .notConsumable(new ItemStack(Blocks.PINK_TULIP.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.PINK_TULIP.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("torchflower_fertilizer"))
                .notConsumable(new ItemStack(Blocks.TORCHFLOWER.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.TORCHFLOWER.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("sea_pickle_fertilizer"))
                .notConsumable(new ItemStack(Blocks.SEA_PICKLE.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.SEA_PICKLE.asItem(), 32))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("beetroot_seeds"))
                .notConsumable(new ItemStack(Blocks.BEETROOTS.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Items.BEETROOT.asItem(), 16))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("poppy_fertilizer"))
                .notConsumable(new ItemStack(Blocks.POPPY.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.POPPY.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("pitcher_plant_fertilizer"))
                .notConsumable(new ItemStack(Blocks.PITCHER_PLANT.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.PITCHER_PLANT.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("large_fern"))
                .notConsumable(new ItemStack(Blocks.LARGE_FERN.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.LARGE_FERN.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("white_tulip_fertilizer"))
                .notConsumable(new ItemStack(Blocks.WHITE_TULIP.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.WHITE_TULIP.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("bamboo_fertilizer"))
                .notConsumable(new ItemStack(Blocks.BAMBOO.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.BAMBOO.asItem(), 32))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("spore_blossom"))
                .notConsumable(new ItemStack(Blocks.SPORE_BLOSSOM.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.SPORE_BLOSSOM.asItem(), 8))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("cocoa_beans"))
                .notConsumable(new ItemStack(Blocks.COCOA.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.COCOA.asItem(), 12))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("large_fern_fertilizer"))
                .notConsumable(new ItemStack(Blocks.LARGE_FERN.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.LARGE_FERN.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("vine_fertilizer"))
                .notConsumable(new ItemStack(Blocks.VINE.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.VINE.asItem(), 32))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("nether_wart"))
                .notConsumable(new ItemStack(Blocks.NETHER_WART.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.NETHER_WART.asItem(), 12))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("fern_fertilizer"))
                .notConsumable(new ItemStack(Blocks.FERN.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.FERN.asItem(), 32))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("cactus"))
                .notConsumable(new ItemStack(Blocks.CACTUS.asItem()))
                .circuitMeta(1)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.CACTUS.asItem(), 12))
                .EUt(30)
                .duration(600)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("lily_of_the_valley_fertilizer"))
                .notConsumable(new ItemStack(Blocks.LILY_OF_THE_VALLEY.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.LILY_OF_THE_VALLEY.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("melon_seeds_fertilizer"))
                .notConsumable(new ItemStack(Blocks.MELON_STEM.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.MELON.asItem(), 12))
                .EUt(60)
                .duration(200)
                .save(provider);

        GTORecipeTypes.GREENHOUSE_RECIPES.recipeBuilder(GTOCore.id("red_tulip_fertilizer"))
                .notConsumable(new ItemStack(Blocks.RED_TULIP.asItem()))
                .inputItems(GTItems.FERTILIZER.asStack(4))
                .circuitMeta(2)
                .inputFluids(GTMaterials.Water.getFluid(1000))
                .outputItems(new ItemStack(Blocks.RED_TULIP.asItem(), 16))
                .EUt(60)
                .duration(200)
                .save(provider);
    }
}
