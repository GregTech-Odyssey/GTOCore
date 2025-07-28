package com.gtocore.data.recipe.misc;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.data.recipe.builder.ars.MeteoriteRegistryHelper;

import com.gtolib.api.data.chemical.GTOChemicalHelper;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEItems;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import earth.terrarium.adastra.common.registry.ModBlocks;

public final class MeteoriteRecipe {

    public static void init() {
        {
            // 主世界
            MeteoriteRegistryHelper.registerMeteoriteType(
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTMaterials.Steel),
                    1,
                    GTOItems.COLORFUL_MYSTICAL_FLOWER.asItem(),
                    new Block[] { Blocks.STONE, Blocks.DEEPSLATE },
                    new int[] { 200, 50 },
                    TagPrefix.ore,
                    new Material[] { GTMaterials.TricalciumPhosphate, GTMaterials.GarnetYellow, GTMaterials.Coal, GTOMaterials.GnomeCrystal, GTMaterials.Mica, GTMaterials.Cobaltite, GTMaterials.GreenSapphire, GTMaterials.YellowLimonite, GTMaterials.Bentonite, GTMaterials.GarnetRed, GTMaterials.RockSalt, GTMaterials.Pyrochlore, GTMaterials.Talc, GTMaterials.Realgar, GTMaterials.Lepidolite, GTMaterials.Ruby, GTMaterials.BasalticMineralSand, GTMaterials.Malachite, GTMaterials.Salt, GTMaterials.Chalcopyrite, GTMaterials.Goethite, GTMaterials.Gold, GTMaterials.Redstone, GTMaterials.Pyrolusite, GTMaterials.GlauconiteSand, GTMaterials.Nickel, GTMaterials.Iron, GTMaterials.Silver, GTMaterials.Spodumene, GTMaterials.Graphite, GTMaterials.Kyanite, GTMaterials.Opal, GTMaterials.Lapis, GTMaterials.Magnetite, GTMaterials.Spessartine, GTMaterials.Amethyst, GTMaterials.Cinnabar, GTOMaterials.PerditioCrystal, GTMaterials.Galena, GTMaterials.Soapstone, GTMaterials.Pentlandite, GTMaterials.Sapphire, GTMaterials.Grossular, GTMaterials.Asbestos, GTMaterials.GraniticMineralSand, GTOMaterials.UndineCrystal, GTMaterials.Pollucite, GTMaterials.FullersEarth, GTMaterials.Almandine, GTMaterials.Oilsands, GTOMaterials.SylphCrystal, GTMaterials.CassiteriteSand, GTMaterials.Cassiterite, GTMaterials.VanadiumMagnetite, GTMaterials.Copper, GTMaterials.Pyrite, GTMaterials.Calcite, GTMaterials.Hematite, GTMaterials.Pyrope, GTMaterials.Tin, GTMaterials.Zeolite, GTMaterials.Diatomite, GTMaterials.Gypsum, GTMaterials.Olivine, GTMaterials.Apatite, GTMaterials.Diamond, GTMaterials.GarnetSand, GTMaterials.Sodalite, GTMaterials.Tantalite, GTOMaterials.SalamanderCrystal, GTMaterials.Garnierite, GTMaterials.Lead, GTMaterials.Lazurite },
                    new int[] { 333, 750, 2090, 474, 166, 250, 214, 2400, 187, 1125, 428, 166, 250, 500, 142, 400, 600, 1200, 285, 6136, 6000, 200, 600, 375, 312, 250, 1454, 333, 142, 544, 250, 375, 750, 725, 375, 750, 200, 1896, 500, 375, 250, 214, 562, 800, 400, 474, 83, 400, 642, 600, 474, 1200, 3666, 400, 1454, 1454, 375, 2400, 428, 5333, 1000, 400, 200, 125, 500, 272, 800, 750, 187, 474, 375, 166, 1125 });

            MeteoriteRegistryHelper.registerMeteoriteType(
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTOMaterials.Manasteel),
                    5,
                    GTOItems.COLORFUL_MYSTICAL_FLOWER.asItem(),
                    new Block[] { Blocks.STONE, Blocks.DEEPSLATE },
                    new int[] { 200, 50 },
                    TagPrefix.ore,
                    new Material[] { GTOMaterials.PerditioCrystal, GTMaterials.GarnetYellow, GTMaterials.Coal, GTOMaterials.GnomeCrystal, GTMaterials.Mica, GTMaterials.Pentlandite, GTMaterials.Cobaltite, GTMaterials.Grossular, GTOMaterials.SylphCrystal, GTMaterials.Bentonite, GTMaterials.Pollucite, GTMaterials.GarnetRed, GTOMaterials.UndineCrystal, GTMaterials.Oilsands, GTMaterials.RockSalt, GTMaterials.Pyrochlore, GTMaterials.Realgar, GTMaterials.Lepidolite, GTMaterials.Talc, GTMaterials.Chalcopyrite, GTMaterials.Salt, GTMaterials.Cassiterite, GTMaterials.Calcite, GTMaterials.Spodumene, GTMaterials.Sodalite, GTMaterials.Zeolite, GTMaterials.Pyrolusite, GTMaterials.Nickel, GTMaterials.Lazurite, GTMaterials.Diamond, GTMaterials.GlauconiteSand, GTMaterials.Silver, GTMaterials.Soapstone, GTMaterials.TricalciumPhosphate, GTMaterials.Olivine, GTMaterials.Graphite, GTMaterials.Lapis, GTMaterials.Opal, GTMaterials.Kyanite, GTMaterials.Apatite, GTMaterials.Tantalite, GTMaterials.Garnierite, GTMaterials.Magnetite, GTOMaterials.SalamanderCrystal, GTMaterials.Spessartine, GTMaterials.Amethyst, GTMaterials.Lead, GTMaterials.Galena },
                    new int[] { 1896, 750, 90, 474, 166, 250, 250, 562, 474, 187, 83, 1125, 474, 600, 428, 166, 500, 142, 250, 2500, 285, 1000, 375, 142, 750, 1000, 375, 250, 1125, 272, 312, 333, 375, 333, 125, 544, 750, 375, 250, 500, 187, 375, 125, 474, 375, 750, 166, 500 });

            // 远古世界
            MeteoriteRegistryHelper.registerMeteoriteType(
                    GTOItems.ULV_ROBOT_ARM.asItem(),
                    2,
                    GTOItems.PRECISION_STEAM_MECHANISM.asItem(),
                    new Block[] { Blocks.DEEPSLATE, Blocks.CLAY },
                    new int[] { 200, 50 },
                    TagPrefix.ore,
                    new Material[] { GTMaterials.Tantalite, GTMaterials.Garnierite, GTMaterials.Alunite, GTMaterials.Sapphire, GTMaterials.Pentlandite, GTMaterials.Cobaltite, GTMaterials.Electrotine, GTMaterials.Barite, GTMaterials.Molybdenite, GTMaterials.GreenSapphire, GTMaterials.Grossular, GTMaterials.NetherQuartz, GTMaterials.Almandine, GTMaterials.CertusQuartz, GTMaterials.Beryllium, GTMaterials.RockSalt, GTMaterials.Lepidolite, GTMaterials.Saltpeter, GTMaterials.Pyrite, GTMaterials.Bornite, GTMaterials.Sulfur, GTMaterials.Wulfenite, GTMaterials.Salt, GTMaterials.Chalcopyrite, GTMaterials.Sphalerite, GTMaterials.Copper, GTMaterials.Emerald, GTMaterials.Quartzite, GTMaterials.Calcite, GTMaterials.Tetrahedrite, GTMaterials.BlueTopaz, GTMaterials.Pyrope, GTMaterials.Molybdenum, GTMaterials.Pyrolusite, GTMaterials.Diatomite, GTMaterials.Nickel, GTMaterials.Iron, GTMaterials.Spodumene, GTMaterials.Chalcocite, GTMaterials.Stibnite, GTMaterials.Lapis, GTMaterials.Powellite, GTMaterials.Sodalite, GTMaterials.Spessartine, GTMaterials.Lazurite, GTMaterials.Topaz },
                    new int[] { 187, 375, 125, 214, 125, 250, 250, 166, 35, 214, 562, 1200, 642, 333, 964, 428, 142, 375, 2120, 218, 1000, 53, 285, 3636, 333, 3453, 1284, 900, 375, 3999, 656, 428, 17, 375, 250, 250, 1454, 142, 437, 999, 750, 17, 750, 375, 1125, 437 });

            MeteoriteRegistryHelper.registerMeteoriteType(
                    GTOItems.ULV_FLUID_REGULATOR.asItem(),
                    10,
                    GTOItems.PRECISION_STEAM_MECHANISM.asItem(),
                    new Block[] { Blocks.DEEPSLATE, Blocks.CLAY },
                    new int[] { 200, 50 },
                    TagPrefix.oreDeepslate,
                    new Material[] { GTMaterials.Tantalite, GTMaterials.Garnierite, GTMaterials.Alunite, GTMaterials.Pentlandite, GTMaterials.Barite, GTMaterials.Molybdenite, GTMaterials.Cobaltite, GTMaterials.Electrotine, GTMaterials.Grossular, GTMaterials.Beryllium, GTMaterials.CertusQuartz, GTMaterials.RockSalt, GTMaterials.Lepidolite, GTMaterials.Saltpeter, GTMaterials.Wulfenite, GTMaterials.Salt, GTMaterials.Quartzite, GTMaterials.Calcite, GTMaterials.Emerald, GTMaterials.Molybdenum, GTMaterials.Pyrolusite, GTMaterials.Diatomite, GTMaterials.Nickel, GTMaterials.Spodumene, GTMaterials.Lapis, GTMaterials.Powellite, GTMaterials.Sodalite, GTMaterials.Spessartine, GTMaterials.Lazurite },
                    new int[] { 187, 375, 125, 125, 166, 35, 250, 250, 562, 964, 333, 428, 142, 375, 53, 285, 500, 375, 1284, 17, 375, 250, 250, 142, 750, 17, 750, 375, 1125 });

            // 地狱
            MeteoriteRegistryHelper.registerMeteoriteType(
                    Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                    5,
                    Items.WITHER_SKELETON_SKULL,
                    new Block[] { Blocks.NETHERRACK, Blocks.BASALT },
                    new int[] { 200, 50 },
                    TagPrefix.oreNetherrack,
                    new Material[] { GTMaterials.Tantalite, GTMaterials.Alunite, GTMaterials.Barite, GTMaterials.Molybdenite, GTMaterials.YellowLimonite, GTMaterials.Electrotine, GTMaterials.NetherQuartz, GTMaterials.Grossular, GTMaterials.CertusQuartz, GTMaterials.Beryllium, GTMaterials.Pyrite, GTMaterials.Saltpeter, GTMaterials.Ruby, GTMaterials.Sphalerite, GTMaterials.Sulfur, GTMaterials.Wulfenite, GTMaterials.Bornite, GTMaterials.Hematite, GTMaterials.Copper, GTMaterials.Goethite, GTMaterials.Quartzite, GTMaterials.Tetrahedrite, GTMaterials.Emerald, GTMaterials.Gold, GTMaterials.BlueTopaz, GTMaterials.Molybdenum, GTMaterials.Redstone, GTMaterials.Pyrolusite, GTMaterials.Diatomite, GTMaterials.Chalcocite, GTMaterials.Stibnite, GTMaterials.Powellite, GTMaterials.Spessartine, GTMaterials.Cinnabar, GTMaterials.Topaz },
                    new int[] { 187, 125, 166, 35, 750, 250, 1200, 562, 333, 964, 666, 375, 400, 333, 1000, 53, 218, 750, 1999, 1125, 900, 3999, 1284, 375, 656, 17, 600, 375, 250, 437, 999, 17, 375, 200, 437 });

            MeteoriteRegistryHelper.registerMeteoriteType(
                    Items.NETHERITE_INGOT,
                    50,
                    Items.WITHER_SKELETON_SKULL,
                    new Block[] { Blocks.NETHERRACK, Blocks.BASALT },
                    new int[] { 200, 50 },
                    TagPrefix.oreNetherrack,
                    new Material[] { GTMaterials.Tantalite, GTMaterials.Molybdenum, GTMaterials.Alunite, GTMaterials.Barite, GTMaterials.Molybdenite, GTMaterials.Diatomite, GTMaterials.Pyrolusite, GTMaterials.YellowLimonite, GTMaterials.Electrotine, GTMaterials.Grossular, GTMaterials.CertusQuartz, GTMaterials.Beryllium, GTMaterials.Saltpeter, GTMaterials.Wulfenite, GTMaterials.Powellite, GTMaterials.Gold, GTMaterials.Spessartine, GTMaterials.Goethite, GTMaterials.Emerald, GTMaterials.Quartzite, GTMaterials.Hematite },
                    new int[] { 187, 17, 125, 166, 35, 250, 375, 750, 250, 562, 333, 964, 375, 53, 17, 375, 375, 1125, 1284, 500, 750 });

            // 月球
            MeteoriteRegistryHelper.registerMeteoriteType(
                    GTItems.ROBOT_ARM_EV.asItem(),
                    20,
                    GTItems.FIELD_GENERATOR_EV.asItem(),
                    new Block[] { ModBlocks.MOON_STONE.get(), ModBlocks.MOON_SAND.get(), ModBlocks.MOON_CHEESE_ORE.get() },
                    new int[] { 200, 50, 20 },
                    GTOTagPrefix.MOON_STONE,
                    new Material[] { GTMaterials.Gold, GTMaterials.Ilmenite, GTMaterials.Soapstone, GTMaterials.Tin, GTMaterials.Pentlandite, GTMaterials.Pitchblende, GTMaterials.Uraninite, GTMaterials.Bauxite, GTMaterials.Neodymium, GTMaterials.Diatomite, GTMaterials.Asbestos, GTMaterials.GlauconiteSand, GTMaterials.Aluminium, GTMaterials.CassiteriteSand, GTMaterials.Talc, GTMaterials.Bastnasite, GTMaterials.Cassiterite, GTMaterials.Monazite, GTMaterials.GarnetSand, GTMaterials.Magnetite, GTMaterials.VanadiumMagnetite },
                    new int[] { 200, 300, 375, 5333, 125, 561, 93, 600, 150, 400, 800, 250, 300, 1200, 250, 450, 2666, 150, 800, 600, 400 });

            MeteoriteRegistryHelper.registerMeteoriteType(
                    GTItems.FLUID_REGULATOR_EV.asItem(),
                    100,
                    GTItems.FIELD_GENERATOR_EV.asItem(),
                    new Block[] { ModBlocks.MOON_STONE.get(), ModBlocks.MOON_SAND.get(), ModBlocks.MOON_CHEESE_ORE.get() },
                    new int[] { 200, 50, 20 },
                    GTOTagPrefix.MOON_STONE,
                    new Material[] { GTMaterials.Ilmenite, GTMaterials.Soapstone, GTMaterials.Pentlandite, GTMaterials.Pitchblende, GTMaterials.Uraninite, GTMaterials.Bauxite, GTMaterials.Neodymium, GTMaterials.GlauconiteSand, GTMaterials.Aluminium, GTMaterials.Bastnasite, GTMaterials.Talc, GTMaterials.Monazite },
                    new int[] { 300, 375, 125, 561, 93, 600, 150, 250, 300, 450, 250, 150 });

            // 火星
            MeteoriteRegistryHelper.registerMeteoriteType(
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTMaterials.TungstenSteel),
                    30,
                    GTItems.NANO_PROCESSOR_ASSEMBLY_EV.asItem(),
                    new Block[] { ModBlocks.MARS_STONE.get(), ModBlocks.MARS_SAND.get(), ModBlocks.MARS_ICE_SHARD_ORE.get() },
                    new int[] { 200, 50, 20 },
                    GTOTagPrefix.MARS_STONE,
                    new Material[] { GTMaterials.TricalciumPhosphate, GTMaterials.GarnetYellow, GTMaterials.Alunite, GTMaterials.Mica, GTMaterials.Electrotine, GTMaterials.Palladium, GTMaterials.FullersEarth, GTMaterials.GraniticMineralSand, GTMaterials.YellowLimonite, GTMaterials.Pollucite, GTMaterials.GarnetRed, GTMaterials.Pyrite, GTMaterials.Pyrochlore, GTMaterials.Saltpeter, GTMaterials.Platinum, GTMaterials.BasalticMineralSand, GTMaterials.Malachite, GTMaterials.Bornite, GTMaterials.Goethite, GTMaterials.Chalcopyrite, GTMaterials.Copper, GTMaterials.VanadiumMagnetite, GTMaterials.Hematite, GTMaterials.Gold, GTMaterials.Tetrahedrite, GTMaterials.Tungstate, GTMaterials.Diatomite, GTMaterials.Cooperite, GTMaterials.Iron, GTMaterials.Lithium, GTMaterials.Stibnite, GTMaterials.Gypsum, GTMaterials.Opal, GTMaterials.Kyanite, GTMaterials.Apatite, GTMaterials.Scheelite, GTMaterials.Magnetite, GTMaterials.Amethyst },
                    new int[] { 333, 750, 125, 166, 250, 25, 400, 400, 2400, 83, 1125, 1454, 166, 375, 50, 600, 1200, 75, 6000, 3636, 3453, 400, 2400, 200, 3999, 466, 250, 50, 1454, 233, 999, 200, 375, 250, 500, 699, 600, 750 });

            MeteoriteRegistryHelper.registerMeteoriteType(
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTMaterials.RTMAlloy),
                    150,
                    GTItems.NANO_PROCESSOR_ASSEMBLY_EV.asItem(),
                    new Block[] { ModBlocks.MARS_STONE.get(), ModBlocks.MARS_SAND.get(), ModBlocks.MARS_ICE_SHARD_ORE.get() },
                    new int[] { 200, 50, 20 },
                    GTOTagPrefix.MARS_STONE,
                    new Material[] { GTMaterials.TricalciumPhosphate, GTMaterials.GarnetYellow, GTMaterials.Tungstate, GTMaterials.Alunite, GTMaterials.Mica, GTMaterials.Lithium, GTMaterials.Palladium, GTMaterials.Electrotine, GTMaterials.Diatomite, GTMaterials.Pollucite, GTMaterials.GarnetRed, GTMaterials.Cooperite, GTMaterials.Pyrochlore, GTMaterials.Platinum, GTMaterials.Saltpeter, GTMaterials.Opal, GTMaterials.Kyanite, GTMaterials.Apatite, GTMaterials.Bornite, GTMaterials.Scheelite, GTMaterials.Amethyst },
                    new int[] { 333, 750, 466, 125, 166, 233, 25, 250, 250, 83, 1125, 50, 166, 50, 375, 375, 250, 500, 75, 699, 750 });

            // 金星
            MeteoriteRegistryHelper.registerMeteoriteType(
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTOMaterials.Desh),
                    30,
                    GTOChemicalHelper.getItem(TagPrefix.plateDense, GTOMaterials.Desh),
                    new Block[] { ModBlocks.MARS_STONE.get(), ModBlocks.MARS_SAND.get(), ModBlocks.MARS_ICE_SHARD_ORE.get() },
                    new int[] { 200, 50, 20 },
                    GTOTagPrefix.MARS_STONE,
                    new Material[] { GTMaterials.TricalciumPhosphate, GTMaterials.GarnetYellow, GTMaterials.Alunite, GTMaterials.Mica, GTMaterials.Electrotine, GTMaterials.Palladium, GTMaterials.FullersEarth, GTMaterials.GraniticMineralSand, GTMaterials.YellowLimonite, GTMaterials.Pollucite, GTMaterials.GarnetRed, GTMaterials.Pyrite, GTMaterials.Pyrochlore, GTMaterials.Saltpeter, GTMaterials.Platinum, GTMaterials.BasalticMineralSand, GTMaterials.Malachite, GTMaterials.Bornite, GTMaterials.Goethite, GTMaterials.Chalcopyrite, GTMaterials.Copper, GTMaterials.VanadiumMagnetite, GTMaterials.Hematite, GTMaterials.Gold, GTMaterials.Tetrahedrite, GTMaterials.Tungstate, GTMaterials.Diatomite, GTMaterials.Cooperite, GTMaterials.Iron, GTMaterials.Lithium, GTMaterials.Stibnite, GTMaterials.Gypsum, GTMaterials.Opal, GTMaterials.Kyanite, GTMaterials.Apatite, GTMaterials.Scheelite, GTMaterials.Magnetite, GTMaterials.Amethyst },
                    new int[] { 333, 750, 125, 166, 250, 25, 400, 400, 2400, 83, 1125, 1454, 166, 375, 50, 600, 1200, 75, 6000, 3636, 3453, 400, 2400, 200, 3999, 466, 250, 50, 1454, 233, 999, 200, 375, 250, 500, 699, 600, 750 });

            MeteoriteRegistryHelper.registerMeteoriteType(
                    RegistriesUtils.getItem("ad_astra:tier_1_rover"),
                    150,
                    GTOChemicalHelper.getItem(TagPrefix.plateDense, GTOMaterials.Desh),
                    new Block[] { ModBlocks.MARS_STONE.get(), ModBlocks.MARS_SAND.get(), ModBlocks.MARS_ICE_SHARD_ORE.get() },
                    new int[] { 200, 50, 20 },
                    GTOTagPrefix.MARS_STONE,
                    new Material[] { GTMaterials.TricalciumPhosphate, GTMaterials.GarnetYellow, GTMaterials.Tungstate, GTMaterials.Alunite, GTMaterials.Mica, GTMaterials.Lithium, GTMaterials.Palladium, GTMaterials.Electrotine, GTMaterials.Diatomite, GTMaterials.Pollucite, GTMaterials.GarnetRed, GTMaterials.Cooperite, GTMaterials.Pyrochlore, GTMaterials.Platinum, GTMaterials.Saltpeter, GTMaterials.Opal, GTMaterials.Kyanite, GTMaterials.Apatite, GTMaterials.Bornite, GTMaterials.Scheelite, GTMaterials.Amethyst },
                    new int[] { 333, 750, 466, 125, 166, 233, 25, 250, 250, 83, 1125, 50, 166, 50, 375, 375, 250, 500, 75, 699, 750 });

        }

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.AIR, 1, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new Block[] {
                        Blocks.STONE, Blocks.DEEPSLATE, Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE,
                        Blocks.TUFF, Blocks.COBBLESTONE, Blocks.BLACKSTONE, Blocks.BASALT, Blocks.CALCITE,
                        Blocks.MAGMA_BLOCK, Blocks.IRON_ORE, Blocks.GOLD_ORE },
                new int[] {
                        200, 180, 150, 150, 150,
                        120, 100, 80, 60, 40,
                        20, 5, 1 });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.PRISMARINE_SHARD, 5, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new Block[] {
                        Blocks.PRISMARINE, Blocks.PRISMARINE_BRICKS, Blocks.DARK_PRISMARINE,
                        Blocks.SEA_LANTERN, Blocks.SEA_PICKLE, Blocks.BUBBLE_CORAL_BLOCK,
                        Blocks.TUBE_CORAL_BLOCK, Blocks.BRAIN_CORAL_BLOCK, Blocks.FIRE_CORAL_BLOCK,
                        Blocks.BARREL, Blocks.CHISELED_STONE_BRICKS, Blocks.SPONGE
                },
                new int[] {
                        100, 50, 40,
                        30, 25, 25,
                        15, 15, 10,
                        3, 2, 1
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.SAND, 5, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new Block[] {
                        Blocks.SANDSTONE, Blocks.CUT_SANDSTONE, Blocks.SMOOTH_SANDSTONE, Blocks.CHISELED_SANDSTONE,
                        Blocks.TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.WHITE_TERRACOTTA,
                        Blocks.CACTUS, Blocks.DEAD_BUSH, Blocks.SAND, Blocks.RED_SAND,
                        Blocks.SMOOTH_QUARTZ, Blocks.QUARTZ_PILLAR
                },
                new int[] {
                        120, 100, 110, 100,
                        80, 80, 60, 50,
                        20, 15, 15, 10,
                        2, 1
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.ICE, 5, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new Block[] {
                        Blocks.ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE,
                        Blocks.SNOW_BLOCK, Blocks.POWDER_SNOW, Blocks.SNOW,
                        Blocks.CALCITE, Blocks.POINTED_DRIPSTONE,
                        Blocks.POLISHED_DIORITE, Blocks.QUARTZ_BLOCK
                },
                new int[] {
                        200, 150, 120,
                        100, 80, 50,
                        30, 20,
                        1, 1
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.NETHER_BRICK, 10, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new Block[] {
                        Blocks.NETHERRACK, Blocks.BLACKSTONE, Blocks.BASALT, Blocks.MAGMA_BLOCK,
                        Blocks.SOUL_SAND, Blocks.SOUL_SOIL, Blocks.GLOWSTONE, Blocks.SHROOMLIGHT,
                        Blocks.NETHER_QUARTZ_ORE, Blocks.NETHER_GOLD_ORE, Blocks.GILDED_BLACKSTONE,
                        Blocks.CRACKED_NETHER_BRICKS, Blocks.CHISELED_NETHER_BRICKS, Blocks.RED_NETHER_BRICKS
                },
                new int[] {
                        200, 180, 150, 120,
                        50, 40, 30, 25,
                        20, 15, 15,
                        2, 1, 1
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.ENDER_PEARL, 25, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new Block[] {
                        Blocks.END_STONE, Blocks.END_STONE_BRICKS, Blocks.PURPUR_BLOCK, Blocks.PURPUR_PILLAR,
                        Blocks.CHORUS_PLANT, Blocks.CHORUS_FLOWER,
                        Blocks.OBSIDIAN, Blocks.IRON_BARS, Blocks.END_ROD,
                        Blocks.DRAGON_HEAD, Blocks.PURPUR_SLAB
                },
                new int[] {
                        250, 200, 150, 120,
                        30, 80,
                        50, 40, 30,
                        1, 1,
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.GLOW_BERRIES, 5, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new Block[] {
                        Blocks.MOSS_BLOCK, Blocks.MOSS_CARPET, Blocks.AZALEA, Blocks.FLOWERING_AZALEA,
                        Blocks.GLOW_LICHEN, Blocks.SPORE_BLOSSOM, Blocks.HANGING_ROOTS,
                        Blocks.CLAY, Blocks.DRIPSTONE_BLOCK, Blocks.POINTED_DRIPSTONE,
                        Blocks.COBBLED_DEEPSLATE, Blocks.ROOTED_DIRT, Blocks.SCULK
                },
                new int[] {
                        200, 10, 20, 20,
                        5, 5, 10,
                        150, 80, 60,
                        30, 25, 25
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.RED_DYE, 2, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new Block[] {
                        Blocks.RED_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA,
                        Blocks.WHITE_TERRACOTTA, Blocks.BROWN_TERRACOTTA,
                        Blocks.TERRACOTTA, Blocks.RED_SAND, Blocks.RED_SANDSTONE,
                        Blocks.CACTUS, Blocks.DEAD_BUSH, Blocks.GOLD_ORE,
                        Blocks.RAIL, Blocks.CHISELED_SANDSTONE
                },
                new int[] {
                        180, 160, 140, 120, 100,
                        80, 60, 50,
                        40, 30, 20,
                        5, 5
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.COAL, 20, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new Block[] {
                        Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE, Blocks.COAL_BLOCK,
                        Blocks.STONE, Blocks.DEEPSLATE, Blocks.TUFF,
                        Blocks.ANDESITE, Blocks.GRAVEL,
                        Blocks.MOSSY_COBBLESTONE, Blocks.IRON_ORE
                },
                new int[] {
                        30, 25, 5,
                        20, 15, 10,
                        8, 5,
                        5, 3
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.IRON_INGOT, 25, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new Block[] {
                        Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.RAW_IRON_BLOCK,
                        Blocks.IRON_BLOCK,
                        Blocks.STONE, Blocks.DEEPSLATE, Blocks.TUFF,
                        Blocks.DIORITE, Blocks.GRANITE,
                        Blocks.COPPER_ORE, Blocks.LAPIS_ORE, Blocks.EMERALD_ORE
                },
                new int[] {
                        30, 25, 8, 5,
                        18, 15, 10,
                        8, 6,
                        4, 3, 2
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.COPPER_INGOT, 20, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new Block[] {
                        Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE,
                        Blocks.RAW_COPPER_BLOCK, Blocks.OXIDIZED_COPPER,
                        Blocks.DRIPSTONE_BLOCK, Blocks.POINTED_DRIPSTONE,
                        Blocks.CALCITE, Blocks.TUFF,
                        Blocks.WEATHERED_COPPER, Blocks.MOSS_BLOCK
                },
                new int[] {
                        35, 25, 10, 5,
                        15, 10,
                        8, 7,
                        5, 2
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.GOLD_INGOT, 30, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new Block[] {
                        Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE,
                        Blocks.RAW_GOLD_BLOCK, Blocks.GILDED_BLACKSTONE,
                        Blocks.NETHERRACK, Blocks.BLACKSTONE,
                        Blocks.BASALT, Blocks.MAGMA_BLOCK,
                        Blocks.NETHER_GOLD_ORE, Blocks.ANCIENT_DEBRIS
                },
                new int[] {
                        30, 20, 8, 5,
                        25, 15,
                        10, 5,
                        4, 2
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.DIAMOND, 50, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new Block[] {
                        Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
                        Blocks.DIAMOND_BLOCK,
                        Blocks.DEEPSLATE, Blocks.TUFF, Blocks.SCULK,
                        Blocks.OBSIDIAN, Blocks.BUDDING_AMETHYST
                },
                new int[] {
                        35, 30, 5,
                        25, 20, 10,
                        3, 1
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.NETHERITE_SCRAP, 200, 0, GTOItems.COLORFUL_MYSTICAL_FLOWER.asItem(),
                new Block[] {
                        Blocks.ANCIENT_DEBRIS, Blocks.NETHERITE_BLOCK,
                        Blocks.BASALT, Blocks.BLACKSTONE,
                        Blocks.MAGMA_BLOCK, Blocks.SOUL_SAND,
                        Blocks.CRYING_OBSIDIAN, Blocks.LODESTONE
                },
                new int[] {
                        35, 5,
                        30, 25,
                        15, 10,
                        3, 2
                });

        MeteoriteRegistryHelper.registerMeteoriteType(
                "ars_nouveau:archwood_planks",
                50, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new String[] {
                        "ars_nouveau:archwood_planks",

                        "ars_nouveau:blue_archwood_leaves",
                        "ars_nouveau:blue_archwood_log",
                        "ars_nouveau:blue_archwood_wood",
                        "ars_nouveau:stripped_blue_archwood_log",
                        "ars_nouveau:stripped_blue_archwood_wood",

                        "ars_nouveau:green_archwood_leaves",
                        "ars_nouveau:green_archwood_log",
                        "ars_nouveau:green_archwood_wood",
                        "ars_nouveau:stripped_green_archwood_log",
                        "ars_nouveau:stripped_green_archwood_wood",

                        "ars_nouveau:purple_archwood_leaves",
                        "ars_nouveau:purple_archwood_log",
                        "ars_nouveau:purple_archwood_wood",
                        "ars_nouveau:stripped_purple_archwood_log",
                        "ars_nouveau:stripped_purple_archwood_wood",

                        "ars_nouveau:red_archwood_leaves",
                        "ars_nouveau:red_archwood_log",
                        "ars_nouveau:red_archwood_wood",
                        "ars_nouveau:stripped_red_archwood_log",
                        "ars_nouveau:stripped_red_archwood_wood",
                },
                new int[] { 120, 20, 80, 40, 5, 5, 20, 80, 40, 5, 5, 20, 80, 40, 5, 5, 20, 80, 40, 5, 5, });

        MeteoriteRegistryHelper.registerMeteoriteType(
                "ars_nouveau:sourcestone",
                200, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new String[] {
                        "ars_nouveau:sourcestone",
                        "ars_nouveau:sourcestone_alternating",
                        "ars_nouveau:sourcestone_basketweave",
                        "ars_nouveau:sourcestone_large_bricks",
                        "ars_nouveau:sourcestone_mosaic",
                        "ars_nouveau:sourcestone_small_bricks",

                        "ars_nouveau:smooth_sourcestone",
                        "ars_nouveau:smooth_sourcestone_alternating",
                        "ars_nouveau:smooth_sourcestone_basketweave",
                        "ars_nouveau:smooth_sourcestone_large_bricks",
                        "ars_nouveau:smooth_sourcestone_mosaic",
                        "ars_nouveau:smooth_sourcestone_small_bricks",

                        "ars_nouveau:gilded_sourcestone_alternating",
                        "ars_nouveau:gilded_sourcestone_basketweave",
                        "ars_nouveau:gilded_sourcestone_large_bricks",
                        "ars_nouveau:gilded_sourcestone_mosaic",
                        "ars_nouveau:gilded_sourcestone_small_bricks",

                        "ars_nouveau:smooth_gilded_sourcestone_alternating",
                        "ars_nouveau:smooth_gilded_sourcestone_basketweave",
                        "ars_nouveau:smooth_gilded_sourcestone_large_bricks",
                        "ars_nouveau:smooth_gilded_sourcestone_mosaic",
                        "ars_nouveau:smooth_gilded_sourcestone_small_bricks",
                },
                new int[] { 50, 50, 50, 50, 50, 50, 40, 40, 40, 40, 40, 40, 30, 30, 30, 30, 30, 20, 20, 20, 20, 20 });

        MeteoriteRegistryHelper.registerMeteoriteType(
                "botania:fertilizer",
                50, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new String[] {
                        "botania:white_petal_block",
                        "botania:orange_petal_block",
                        "botania:magenta_petal_block",
                        "botania:light_blue_petal_block",

                        "botania:yellow_petal_block",
                        "botania:lime_petal_block",
                        "botania:pink_petal_block",
                        "botania:gray_petal_block",

                        "botania:light_gray_petal_block",
                        "botania:cyan_petal_block",
                        "botania:purple_petal_block",
                        "botania:blue_petal_block",

                        "botania:brown_petal_block",
                        "botania:green_petal_block",
                        "botania:red_petal_block",
                        "botania:black_petal_block",
                },
                new int[] { 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, });

        MeteoriteRegistryHelper.registerMeteoriteType(
                "botania:life_essence",
                500, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new String[] {
                        "botania:livingrock",
                        "botania:livingwood",
                        "botania:dreamwood",

                        "botania:shimmerrock",
                        "botania:glimmering_livingwood",
                        "botania:glimmering_dreamwood",

                        "botania:dark_quartz",
                        "botania:mana_quartz",
                        "botania:blaze_quartz",
                        "botania:lavender_quartz",
                        "botania:red_quartz",
                        "botania:elf_quartz",
                        "botania:sunny_quartz",

                        "botania:cell_block",
                        "botania:enchanted_soil",

                        "botania:manasteel_block",
                        "botania:terrasteel_block",
                        "botania:elementium_block",
                        "botania:mana_diamond_block",
                        "botania:dragonstone_block",

                        "botania:mana_glass",
                        "botania:elf_glass",
                        "botania:bifrost_perm",

                        "botania:mana_pylon",
                        "botania:natura_pylon",
                        "botania:gaia_pylon",
                },
                new int[] { 200, 200, 100, 150, 150, 60, 80, 80, 80, 80, 80, 80, 80, 20, 20, 30, 30, 20, 30, 20, 60, 40, 20, 15, 10, 1 });

        MeteoriteRegistryHelper.registerMeteoriteType(
                "ae2:singularity",
                10, 3, AEItems.FLUIX_PEARL.asItem(),
                new String[] {
                        "ae2:mysterious_cube",

                        "ae2:flawless_budding_quartz",
                        "ae2:flawed_budding_quartz",
                        "ae2:chipped_budding_quartz",
                        "ae2:damaged_budding_quartz",
                        "ae2:quartz_block",
                        "ae2:fluix_block",

                        "ae2:quartz_block",
                        "ae2:fluix_block",
                        "ae2:sky_stone_block",
                        "ae2:smooth_sky_stone_block"
                },
                new int[] { 1, 50, 60, 70, 80, 90, 20, 10, 5, 150, 50 },
                new int[] { 1, 6, 4, 10, 50, 100 });

        MeteoriteRegistryHelper.registerMeteoriteType(
                GTItems.TOOL_MATCHES.asItem(),
                50, 0, GTItems.TOOL_MATCHBOX.asItem(),
                new Block[] {
                        Blocks.TNT,
                        GTBlocks.INDUSTRIAL_TNT.get(),
                        GTBlocks.POWDERBARREL.get(),
                        Blocks.REDSTONE_BLOCK,
                },
                new int[] { 50, 30, 30, 5 });

        MeteoriteRegistryHelper.registerMeteoriteType(
                GTItems.TOOL_LIGHTER_INVAR.asItem(),
                250, 0, GTItems.TOOL_LIGHTER_PLATINUM.asItem(),
                new Block[] {
                        Blocks.TNT,
                        GTBlocks.INDUSTRIAL_TNT.get(),
                        GTBlocks.POWDERBARREL.get(),
                        GTOBlocks.NUKE_BOMB.get(),
                        Blocks.REDSTONE_BLOCK,
                },
                new int[] { 100, 80, 80, 1, 5 });
    }
}
