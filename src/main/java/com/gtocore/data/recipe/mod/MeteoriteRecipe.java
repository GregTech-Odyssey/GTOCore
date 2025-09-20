package com.gtocore.data.recipe.mod;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.data.recipe.builder.ars.MeteoriteRegistryHelper;

import com.gtolib.api.data.chemical.GTOChemicalHelper;

import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import appeng.core.definitions.AEItems;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import com.kyanite.deeperdarker.content.DDBlocks;
import earth.terrarium.adastra.common.registry.ModBlocks;

public final class MeteoriteRecipe {

    public static void init() {
        MeteoriteRegistryHelper.registerMeteoriteType(
                Items.AIR, 1, 0, ItemsRegistry.SOURCE_GEM.get().asItem(),
                new Block[] {
                        Blocks.STONE, Blocks.DEEPSLATE, Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE,
                        Blocks.TUFF, Blocks.COBBLESTONE, Blocks.BLACKSTONE, Blocks.BASALT, Blocks.CALCITE,
                        Blocks.MAGMA_BLOCK, ChemicalHelper.getBlock(TagPrefix.ore, GTMaterials.Iron), ChemicalHelper.getBlock(TagPrefix.ore, GTMaterials.Gold) },
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
                        ChemicalHelper.getBlock(TagPrefix.oreNetherrack, GTMaterials.NetherQuartz), ChemicalHelper.getBlock(TagPrefix.oreNetherrack, GTMaterials.Gold), Blocks.GILDED_BLACKSTONE,
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
                        Blocks.CACTUS, Blocks.DEAD_BUSH, ChemicalHelper.getBlock(TagPrefix.ore, GTMaterials.Gold),
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
                        ChemicalHelper.getBlock(TagPrefix.ore, GTMaterials.Coal), ChemicalHelper.getBlock(TagPrefix.oreDeepslate, GTMaterials.Coal), Blocks.COAL_BLOCK,
                        Blocks.STONE, Blocks.DEEPSLATE, Blocks.TUFF,
                        Blocks.ANDESITE, Blocks.GRAVEL,
                        Blocks.MOSSY_COBBLESTONE, ChemicalHelper.getBlock(TagPrefix.ore, GTMaterials.Iron)
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
                        ChemicalHelper.getBlock(TagPrefix.ore, GTMaterials.Iron), ChemicalHelper.getBlock(TagPrefix.oreDeepslate, GTMaterials.Iron), Blocks.RAW_IRON_BLOCK,
                        Blocks.IRON_BLOCK,
                        Blocks.STONE, Blocks.DEEPSLATE, Blocks.TUFF,
                        Blocks.DIORITE, Blocks.GRANITE,
                        ChemicalHelper.getBlock(TagPrefix.ore, GTMaterials.Copper), ChemicalHelper.getBlock(TagPrefix.ore, GTMaterials.Lapis), ChemicalHelper.getBlock(TagPrefix.ore, GTMaterials.Emerald)
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
                        ChemicalHelper.getBlock(TagPrefix.ore, GTMaterials.Copper), ChemicalHelper.getBlock(TagPrefix.oreDeepslate, GTMaterials.Copper),
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
                        ChemicalHelper.getBlock(TagPrefix.ore, GTMaterials.Gold), ChemicalHelper.getBlock(TagPrefix.oreDeepslate, GTMaterials.Gold),
                        Blocks.RAW_GOLD_BLOCK, Blocks.GILDED_BLACKSTONE,
                        Blocks.NETHERRACK, Blocks.BLACKSTONE,
                        Blocks.BASALT, Blocks.MAGMA_BLOCK,
                        ChemicalHelper.getBlock(TagPrefix.oreNetherrack, GTMaterials.Gold), Blocks.ANCIENT_DEBRIS
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
                        ChemicalHelper.getBlock(TagPrefix.ore, GTMaterials.Diamond), ChemicalHelper.getBlock(TagPrefix.oreDeepslate, GTMaterials.Diamond),
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
                Items.NETHER_STAR, 200, 0, Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                new Block[] {
                        Blocks.ANCIENT_DEBRIS, Blocks.NETHERITE_BLOCK,
                        Blocks.BASALT, Blocks.BLACKSTONE,
                        Blocks.MAGMA_BLOCK, Blocks.SOUL_SAND,
                        Blocks.CRYING_OBSIDIAN, Blocks.LODESTONE
                },
                new int[] {
                        40, 2,
                        180, 150,
                        90, 60,
                        10, 5
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

        {
            // Material组
            Material[][] materials = new Material[][] {
                    // 维度: Overworld (主世界)--ID: minecraft:overworld
                    new Material[] { GTMaterials.Iron, GTMaterials.CassiteriteSand, GTMaterials.Galena, GTMaterials.GarnetRed, GTMaterials.Goethite, GTMaterials.Magnetite, GTMaterials.Gypsum, GTMaterials.Hematite, GTMaterials.Spessartine, GTMaterials.Silver, GTMaterials.Tin, GTMaterials.Gold, GTMaterials.Mica, GTMaterials.Lazurite, GTMaterials.Graphite, GTMaterials.TricalciumPhosphate, GTMaterials.Sodalite, GTMaterials.Kyanite, GTMaterials.Tantalite, GTMaterials.FullersEarth, GTMaterials.GarnetSand, GTMaterials.Cobaltite, GTMaterials.Diamond, GTMaterials.Ruby, GTMaterials.Apatite, GTMaterials.Garnierite, GTOMaterials.SalamanderCrystal, GTMaterials.Almandine, GTMaterials.Olivine, GTMaterials.Pyrochlore, GTMaterials.Realgar, GTMaterials.RockSalt, GTMaterials.Cassiterite, GTMaterials.Grossular, GTMaterials.Sapphire, GTMaterials.Coal, GTMaterials.Cinnabar, GTMaterials.Chalcopyrite, GTMaterials.YellowLimonite, GTMaterials.Lepidolite, GTOMaterials.PerditioCrystal, GTMaterials.Zeolite, GTMaterials.Redstone, GTMaterials.Pyrite, GTOMaterials.GnomeCrystal, GTMaterials.VanadiumMagnetite, GTMaterials.Pentlandite, GTMaterials.Amethyst, GTMaterials.Lapis, GTOMaterials.SylphCrystal, GTMaterials.GreenSapphire, GTMaterials.Soapstone, GTMaterials.Pyrope, GTMaterials.Bentonite, GTMaterials.Pollucite, GTMaterials.Talc, GTMaterials.Salt, GTMaterials.GarnetYellow, GTOMaterials.UndineCrystal, GTMaterials.Calcite, GTMaterials.Oilsands, GTMaterials.GraniticMineralSand, GTMaterials.Malachite, GTMaterials.Pyrolusite, GTMaterials.Opal, GTMaterials.Diatomite, GTMaterials.Asbestos, GTMaterials.BasalticMineralSand, GTMaterials.Nickel, GTMaterials.Spodumene, GTMaterials.GlauconiteSand, GTMaterials.Copper, GTMaterials.Lead },
                    new Material[] { GTMaterials.Lepidolite, GTMaterials.Gypsum, GTMaterials.Spessartine, GTMaterials.Silver, GTMaterials.Gold, GTMaterials.Mica, GTMaterials.TricalciumPhosphate, GTMaterials.Kyanite, GTMaterials.Tantalite, GTMaterials.FullersEarth, GTMaterials.VanadiumMagnetite, GTMaterials.Pentlandite, GTMaterials.GraniticMineralSand, GTMaterials.Salt, GTMaterials.Diamond, GTMaterials.Cobaltite, GTMaterials.GreenSapphire, GTMaterials.Bentonite, GTMaterials.Pollucite, GTMaterials.Talc, GTMaterials.Garnierite, GTMaterials.Ruby, GTMaterials.Pyrochlore, GTMaterials.Olivine, GTMaterials.Soapstone, GTMaterials.Calcite, GTMaterials.Opal, GTMaterials.Pyrolusite, GTMaterials.Diatomite, GTMaterials.Nickel, GTMaterials.Sapphire, GTMaterials.Spodumene, GTMaterials.Cinnabar, GTMaterials.GlauconiteSand, GTMaterials.Lead },
                    // 维度: Ancient World (远古世界)--ID: gtocore:ancient_world
                    new Material[] { GTMaterials.Barite, GTMaterials.Iron, GTMaterials.Lepidolite, GTMaterials.CertusQuartz, GTMaterials.Chalcocite, GTMaterials.Tetrahedrite, GTMaterials.Spessartine, GTMaterials.Bornite, GTMaterials.Sphalerite, GTMaterials.Alunite, GTMaterials.Topaz, GTMaterials.Powellite, GTMaterials.Lazurite, GTMaterials.NetherQuartz, GTMaterials.Stibnite, GTMaterials.Sodalite, GTMaterials.Tantalite, GTMaterials.Pyrite, GTMaterials.Saltpeter, GTMaterials.Pentlandite, GTMaterials.Lapis, GTMaterials.GreenSapphire, GTMaterials.Cobaltite, GTMaterials.Wulfenite, GTMaterials.Pyrope, GTMaterials.Garnierite, GTMaterials.Salt, GTMaterials.Quartzite, GTMaterials.Beryllium, GTMaterials.Almandine, GTMaterials.Calcite, GTMaterials.RockSalt, GTMaterials.Electrotine, GTMaterials.Pyrolusite, GTMaterials.Diatomite, GTMaterials.Molybdenum, GTMaterials.BlueTopaz, GTMaterials.Grossular, GTMaterials.Nickel, GTMaterials.Sapphire, GTMaterials.Molybdenite, GTMaterials.Spodumene, GTMaterials.Emerald, GTMaterials.Sulfur, GTMaterials.Copper, GTMaterials.Chalcopyrite },
                    new Material[] { GTMaterials.Barite, GTMaterials.Wulfenite, GTMaterials.Cobaltite, GTMaterials.GreenSapphire, GTMaterials.Lepidolite, GTMaterials.CertusQuartz, GTMaterials.Salt, GTMaterials.Bornite, GTMaterials.Alunite, GTMaterials.Powellite, GTMaterials.Sphalerite, GTMaterials.Diatomite, GTMaterials.Electrotine, GTMaterials.Spodumene, GTMaterials.Molybdenite, GTMaterials.Nickel, GTMaterials.Sapphire, GTMaterials.Molybdenum, GTMaterials.Tantalite, GTMaterials.Pentlandite },
                    // 维度: Nether (下界)--ID: minecraft:the_nether
                    new Material[] { GTMaterials.Barite, GTMaterials.Sulfur, GTMaterials.YellowLimonite, GTMaterials.CertusQuartz, GTMaterials.Chalcocite, GTMaterials.Tetrahedrite, GTMaterials.Spessartine, GTMaterials.Hematite, GTMaterials.Goethite, GTMaterials.Bornite, GTMaterials.Sphalerite, GTMaterials.Powellite, GTMaterials.Alunite, GTMaterials.Redstone, GTMaterials.Gold, GTMaterials.Topaz, GTMaterials.NetherQuartz, GTMaterials.Stibnite, GTMaterials.Tantalite, GTMaterials.Pyrite, GTMaterials.Saltpeter, GTMaterials.Wulfenite, GTMaterials.Ruby, GTMaterials.Quartzite, GTMaterials.Pyrolusite, GTMaterials.Diatomite, GTMaterials.Electrotine, GTMaterials.Grossular, GTMaterials.Beryllium, GTMaterials.BlueTopaz, GTMaterials.Molybdenum, GTMaterials.Molybdenite, GTMaterials.Emerald, GTMaterials.Cinnabar, GTMaterials.Copper },
                    new Material[] { GTMaterials.Barite, GTMaterials.Wulfenite, GTMaterials.CertusQuartz, GTMaterials.Bornite, GTMaterials.Spessartine, GTMaterials.Gold, GTMaterials.Alunite, GTMaterials.Powellite, GTMaterials.Sphalerite, GTMaterials.Pyrolusite, GTMaterials.Diatomite, GTMaterials.Electrotine, GTMaterials.Molybdenite, GTMaterials.Molybdenum, GTMaterials.Tantalite, GTMaterials.Cinnabar, GTMaterials.Saltpeter },
                    // 维度: Moon (月球)--ID: ad_astra:moon
                    new Material[] { GTMaterials.GarnetSand, GTMaterials.Uraninite, GTMaterials.Monazite, GTMaterials.Ilmenite, GTMaterials.CassiteriteSand, GTMaterials.Magnetite, GTMaterials.Aluminium, GTMaterials.Talc, GTMaterials.Tin, GTMaterials.Soapstone, GTMaterials.Gold, GTMaterials.Bauxite, GTMaterials.Diatomite, GTMaterials.Cassiterite, GTMaterials.Asbestos, GTMaterials.Neodymium, GTMaterials.Pitchblende, GTMaterials.Pentlandite, GTMaterials.VanadiumMagnetite, GTMaterials.Bastnasite, GTMaterials.GlauconiteSand },
                    new Material[] { GTMaterials.Uraninite, GTMaterials.Monazite, GTMaterials.Talc, GTMaterials.Aluminium, GTMaterials.Ilmenite, GTMaterials.Soapstone, GTMaterials.Gold, GTMaterials.Neodymium, GTMaterials.GlauconiteSand, GTMaterials.Pentlandite },
                    // 维度: Mars (火星)--ID: ad_astra:mars
                    new Material[] { GTMaterials.Iron, GTMaterials.YellowLimonite, GTMaterials.Tetrahedrite, GTMaterials.GarnetRed, GTMaterials.Goethite, GTMaterials.Magnetite, GTMaterials.Gypsum, GTMaterials.Hematite, GTMaterials.Bornite, GTMaterials.Palladium, GTMaterials.Alunite, GTMaterials.Gold, GTMaterials.Mica, GTMaterials.Stibnite, GTMaterials.TricalciumPhosphate, GTMaterials.Kyanite, GTMaterials.Pyrite, GTMaterials.Scheelite, GTMaterials.FullersEarth, GTMaterials.VanadiumMagnetite, GTMaterials.Saltpeter, GTMaterials.Amethyst, GTMaterials.Lithium, GTMaterials.Platinum, GTMaterials.Tungstate, GTMaterials.Pollucite, GTMaterials.GarnetYellow, GTMaterials.Cooperite, GTMaterials.Apatite, GTMaterials.Pyrochlore, GTMaterials.GraniticMineralSand, GTMaterials.Opal, GTMaterials.Malachite, GTMaterials.Diatomite, GTMaterials.Electrotine, GTMaterials.BasalticMineralSand, GTMaterials.Copper, GTMaterials.Chalcopyrite },
                    new Material[] { GTMaterials.Lithium, GTMaterials.Platinum, GTMaterials.Pollucite, GTMaterials.Gypsum, GTMaterials.Bornite, GTMaterials.Cooperite, GTMaterials.Palladium, GTMaterials.Pyrochlore, GTMaterials.Alunite, GTMaterials.Opal, GTMaterials.Gold, GTMaterials.Mica, GTMaterials.Diatomite, GTMaterials.Electrotine, GTMaterials.TricalciumPhosphate, GTMaterials.Kyanite, GTMaterials.Saltpeter },
                    // 维度: Venus (金星)--ID: ad_astra:venus
                    new Material[] { GTOMaterials.Desh, GTMaterials.Sulfur, GTMaterials.YellowLimonite, GTMaterials.Galena, GTMaterials.Magnetite, GTMaterials.Hematite, GTMaterials.Goethite, GTMaterials.Silver, GTMaterials.Powellite, GTMaterials.Sphalerite, GTMaterials.Gold, GTMaterials.Chromite, GTMaterials.Graphite, GTMaterials.Pyrite, GTMaterials.VanadiumMagnetite, GTMaterials.Wulfenite, GTMaterials.Diamond, GTMaterials.Bentonite, GTMaterials.Olivine, GTMaterials.Molybdenite, GTMaterials.Magnesite, GTMaterials.Molybdenum, GTMaterials.Coal, GTMaterials.GlauconiteSand, GTMaterials.Lead },
                    new Material[] { GTOMaterials.Desh, GTMaterials.Wulfenite, GTMaterials.Bentonite, GTMaterials.Olivine, GTMaterials.Powellite, GTMaterials.Molybdenum, GTMaterials.Magnesite, GTMaterials.Molybdenite, GTMaterials.Coal, GTMaterials.VanadiumMagnetite, GTMaterials.GlauconiteSand, GTMaterials.Lead },
                    // 维度: Mercury (水星)--ID: ad_astra:mercury
                    new Material[] { GTMaterials.Chalcocite, GTMaterials.Spessartine, GTOMaterials.Calorite, GTMaterials.Bornite, GTMaterials.Palladium, GTMaterials.Zeolite, GTMaterials.Alunite, GTMaterials.Topaz, GTMaterials.Tantalite, GTMaterials.Saltpeter, GTMaterials.Pentlandite, GTMaterials.Platinum, GTMaterials.Cobaltite, GTMaterials.Garnierite, GTMaterials.Cooperite, GTMaterials.Pyrochlore, GTMaterials.Realgar, GTMaterials.Pyrolusite, GTMaterials.Diatomite, GTMaterials.Electrotine, GTMaterials.Cassiterite, GTMaterials.Grossular, GTMaterials.Nickel, GTMaterials.BlueTopaz, GTMaterials.Cobalt, GTMaterials.Chalcopyrite },
                    new Material[] { GTMaterials.Platinum, GTMaterials.Cobaltite, GTMaterials.Bornite, GTOMaterials.Calorite, GTMaterials.Cooperite, GTMaterials.Palladium, GTMaterials.Pyrochlore, GTMaterials.Alunite, GTMaterials.Diatomite, GTMaterials.Electrotine, GTMaterials.Nickel, GTMaterials.Tantalite, GTMaterials.Pentlandite },
                    // 维度: Ceres (谷神星)--ID: gtocore:ceres
                    new Material[] { GTMaterials.Monazite, GTMaterials.YellowLimonite, GTMaterials.Magnetite, GTMaterials.Hematite, GTMaterials.Goethite, GTMaterials.Spessartine, GTMaterials.Zeolite, GTMaterials.Gold, GTMaterials.Neodymium, GTMaterials.Tantalite, GTMaterials.Scheelite, GTMaterials.Bastnasite, GTMaterials.Pentlandite, GTMaterials.Lithium, GTMaterials.Tungstate, GTMaterials.Bentonite, GTMaterials.Talc, GTMaterials.Olivine, GTMaterials.Soapstone, GTMaterials.Realgar, GTMaterials.Malachite, GTMaterials.Pyrolusite, GTOMaterials.Ostrum, GTMaterials.Cassiterite, GTMaterials.Grossular, GTMaterials.GlauconiteSand, GTMaterials.Chalcopyrite },
                    new Material[] { GTMaterials.Lithium, GTMaterials.Monazite, GTMaterials.Bentonite, GTMaterials.Magnetite, GTMaterials.Talc, GTMaterials.Olivine, GTMaterials.Gold, GTOMaterials.Ostrum, GTMaterials.Neodymium, GTMaterials.Tantalite, GTMaterials.GlauconiteSand, GTMaterials.Pentlandite },
                    // 维度: Io (木卫一)--ID: gtocore:io
                    new Material[] { GTMaterials.Sulfur, GTMaterials.YellowLimonite, GTMaterials.Naquadah, GTMaterials.Hematite, GTMaterials.Plutonium239, GTMaterials.Magnetite, GTMaterials.Gypsum, GTMaterials.Goethite, GTMaterials.Sphalerite, GTMaterials.Powellite, GTMaterials.Graphite, GTMaterials.Pyrite, GTMaterials.FullersEarth, GTMaterials.Diamond, GTOMaterials.Celestine, GTMaterials.Wulfenite, GTMaterials.Bentonite, GTMaterials.Cooperite, GTMaterials.Olivine, GTMaterials.GraniticMineralSand, GTMaterials.Malachite, GTMaterials.BasalticMineralSand, GTMaterials.Molybdenite, GTMaterials.Molybdenum, GTMaterials.Coal, GTMaterials.Trona, GTMaterials.GlauconiteSand },
                    new Material[] { GTMaterials.Bentonite, GTMaterials.Wulfenite, GTOMaterials.Celestine, GTMaterials.Plutonium239, GTMaterials.Magnetite, GTMaterials.Gypsum, GTMaterials.Cooperite, GTMaterials.Olivine, GTMaterials.Powellite, GTMaterials.Molybdenum, GTMaterials.Molybdenite, GTMaterials.Coal, GTMaterials.GlauconiteSand },
                    // 维度: Ganymede (木卫三)--ID: gtocore:ganymede
                    new Material[] { GTMaterials.Cobaltite, GTMaterials.Ilmenite, GTMaterials.Pollucite, GTMaterials.Aluminium, GTMaterials.Talc, GTMaterials.Garnierite, GTMaterials.Zeolite, GTMaterials.Tin, GTMaterials.Soapstone, GTMaterials.Realgar, GTMaterials.Bauxite, GTMaterials.Mica, GTMaterials.Cassiterite, GTMaterials.Kyanite, GTMaterials.Nickel, GTMaterials.Beryllium, GTMaterials.Emerald, GTMaterials.Pentlandite, GTMaterials.GlauconiteSand, GTMaterials.Chalcopyrite },
                    new Material[] { GTMaterials.Cobaltite, GTMaterials.Talc, GTMaterials.Pollucite, GTMaterials.Aluminium, GTMaterials.Ilmenite, GTMaterials.Mica, GTMaterials.Nickel, GTMaterials.Kyanite, GTMaterials.GlauconiteSand, GTMaterials.Pentlandite },
                    // 维度: Enceladus (土卫二)--ID: gtocore:enceladus
                    new Material[] { GTMaterials.Ruby, GTMaterials.Iron, GTMaterials.Platinum, GTMaterials.Galena, GTMaterials.Wulfenite, GTMaterials.Titanium, GTMaterials.Chalcocite, GTMaterials.Palladium, GTMaterials.Ilmenite, GTMaterials.Bornite, GTMaterials.Cooperite, GTMaterials.Silver, GTMaterials.Redstone, GTMaterials.Powellite, GTMaterials.Topaz, GTMaterials.BlueTopaz, GTMaterials.Molybdenum, GTMaterials.Molybdenite, GTMaterials.Pyrite, GTMaterials.Cinnabar, GTMaterials.Lead, GTMaterials.Copper, GTMaterials.Chalcopyrite },
                    new Material[] { GTMaterials.Platinum, GTMaterials.Wulfenite, GTMaterials.Titanium, GTMaterials.Ilmenite, GTMaterials.Cooperite, GTMaterials.Palladium, GTMaterials.Powellite, GTMaterials.Molybdenite, GTMaterials.Molybdenum, GTMaterials.Cinnabar, GTMaterials.Lead },
                    // 维度: Titan (土卫六)--ID: gtocore:titan
                    new Material[] { GTMaterials.Uraninite, GTOMaterials.Desh, GTMaterials.YellowLimonite, GTMaterials.Magnetite, GTMaterials.CassiteriteSand, GTMaterials.Hematite, GTMaterials.Goethite, GTMaterials.Tetrahedrite, GTMaterials.Chromite, GTMaterials.Stibnite, GTMaterials.TricalciumPhosphate, GTMaterials.VanadiumMagnetite, GTMaterials.GarnetSand, GTMaterials.GreenSapphire, GTMaterials.Pyrope, GTMaterials.Pyrochlore, GTMaterials.Apatite, GTMaterials.Almandine, GTMaterials.Malachite, GTMaterials.Diatomite, GTMaterials.Asbestos, GTMaterials.Sapphire, GTMaterials.Magnesite, GTMaterials.Pitchblende, GTMaterials.Copper },
                    new Material[] { GTOMaterials.Desh, GTMaterials.Uraninite, GTMaterials.GreenSapphire, GTMaterials.Pyrope, GTMaterials.Magnetite, GTMaterials.Pyrochlore, GTMaterials.Diatomite, GTMaterials.Chromite, GTMaterials.TricalciumPhosphate, GTMaterials.Sapphire, GTMaterials.Magnesite, GTMaterials.VanadiumMagnetite },
                    // 维度: Pluto (冥王星)--ID: gtocore:pluto
                    new Material[] { GTMaterials.Amethyst, GTMaterials.Uraninite, GTMaterials.Naquadah, GTMaterials.GarnetRed, GTMaterials.Galena, GTMaterials.Plutonium239, GTMaterials.Pollucite, GTMaterials.Apatite, GTMaterials.Quartzite, GTMaterials.GarnetYellow, GTMaterials.Tungsten, GTMaterials.Silver, GTMaterials.Pyrochlore, GTMaterials.Mica, GTMaterials.Opal, GTMaterials.Pyrolusite, GTMaterials.TricalciumPhosphate, GTMaterials.Kyanite, GTMaterials.Tantalite, GTMaterials.Pitchblende, GTMaterials.CertusQuartz, GTMaterials.Barite, GTOMaterials.Zircon, GTMaterials.Lead },
                    new Material[] { GTMaterials.Barite, GTMaterials.Uraninite, GTMaterials.Plutonium239, GTMaterials.Pollucite, GTMaterials.Tungsten, GTMaterials.Pyrochlore, GTMaterials.Pyrolusite, GTMaterials.Mica, GTMaterials.Kyanite, GTMaterials.Tantalite, GTOMaterials.Zircon, GTMaterials.Lead },
                    // 维度: Glacio (霜原星)--ID: ad_astra:glacio
                    new Material[] { GTMaterials.Monazite, GTMaterials.Lepidolite, GTMaterials.CassiteriteSand, GTMaterials.Bornite, GTMaterials.Tin, GTMaterials.Palladium, GTMaterials.Mica, GTMaterials.Gold, GTMaterials.Lazurite, GTMaterials.Kyanite, GTMaterials.Sodalite, GTMaterials.Neodymium, GTMaterials.Tantalite, GTMaterials.Scheelite, GTMaterials.Lithium, GTMaterials.Bastnasite, GTOMaterials.Zircon, GTMaterials.GarnetSand, GTMaterials.Lapis, GTMaterials.Platinum, GTOMaterials.Celestine, GTMaterials.Tungstate, GTMaterials.Pollucite, GTMaterials.Salt, GTMaterials.Cooperite, GTMaterials.Tungsten, GTMaterials.Oilsands, GTMaterials.Calcite, GTMaterials.RockSalt, GTOMaterials.Ostrum, GTMaterials.Pyrolusite, GTMaterials.Diatomite, GTMaterials.Cassiterite, GTMaterials.Asbestos, GTMaterials.Spodumene, GTMaterials.Coal, GTMaterials.Trona },
                    new Material[] { GTMaterials.Lithium, GTMaterials.Platinum, GTMaterials.Monazite, GTOMaterials.Celestine, GTMaterials.Lepidolite, GTMaterials.Pollucite, GTMaterials.Tungsten, GTMaterials.Bornite, GTMaterials.Cooperite, GTMaterials.Palladium, GTMaterials.Pyrolusite, GTMaterials.Mica, GTOMaterials.Ostrum, GTMaterials.Kyanite, GTMaterials.Spodumene, GTMaterials.Neodymium, GTMaterials.Tantalite, GTOMaterials.Zircon },
                    // 维度: Barnarda C (巴纳德C)--ID: gtocore:barnarda_c
                    new Material[] { GTMaterials.Ruby, GTOMaterials.Calorite, GTMaterials.Naquadah, GTMaterials.Galena, GTMaterials.Lepidolite, GTMaterials.Plutonium239, GTMaterials.RockSalt, GTMaterials.Gypsum, GTMaterials.Salt, GTMaterials.Apatite, GTMaterials.Pyrochlore, GTMaterials.Silver, GTMaterials.Oilsands, GTMaterials.GraniticMineralSand, GTMaterials.Redstone, GTMaterials.BasalticMineralSand, GTMaterials.TricalciumPhosphate, GTMaterials.Spodumene, GTMaterials.Cinnabar, GTMaterials.FullersEarth, GTMaterials.Coal, GTMaterials.Cobalt, GTMaterials.Lead },
                    new Material[] { GTMaterials.Lepidolite, GTMaterials.Plutonium239, GTMaterials.Gypsum, GTMaterials.Salt, GTOMaterials.Calorite, GTMaterials.Pyrochlore, GTMaterials.Spodumene, GTMaterials.Cinnabar, GTMaterials.Cobalt, GTMaterials.Lead },
                    // 维度: Otherside (幽冥)--ID: deeperdarker:otherside
                    new Material[] { GTMaterials.Iron, GTOMaterials.Desh, GTMaterials.Spessartine, GTMaterials.Hematite, GTMaterials.Goethite, GTMaterials.Bornite, GTMaterials.Tin, GTMaterials.Alunite, GTMaterials.Mica, GTMaterials.Graphite, GTMaterials.TricalciumPhosphate, GTMaterials.Kyanite, GTMaterials.Neodymium, GTMaterials.Tantalite, GTMaterials.FullersEarth, GTMaterials.Scheelite, GTMaterials.Lithium, GTMaterials.Platinum, GTOMaterials.Celestine, GTMaterials.Ruby, GTMaterials.Olivine, GTMaterials.Almandine, GTMaterials.Electrotine, GTMaterials.Grossular, GTMaterials.BlueTopaz, GTMaterials.Molybdenite, GTMaterials.Beryllium, GTMaterials.Emerald, GTMaterials.Coal, GTMaterials.Chalcopyrite, GTMaterials.Monazite, GTMaterials.Chalcocite, GTMaterials.Naquadah, GTMaterials.Ilmenite, GTMaterials.Zeolite, GTMaterials.Chromite, GTMaterials.Stibnite, GTOMaterials.GnomeCrystal, GTMaterials.Lapis, GTMaterials.Bentonite, GTMaterials.Pyrope, GTMaterials.Pollucite, GTMaterials.Salt, GTMaterials.Soapstone, GTOMaterials.UndineCrystal, GTMaterials.Calcite, GTMaterials.Pyrolusite, GTMaterials.Opal, GTMaterials.Asbestos, GTMaterials.Nickel, GTMaterials.Magnesite, GTMaterials.Trona, GTMaterials.Pitchblende, GTMaterials.Copper, GTMaterials.Lead, GTMaterials.CertusQuartz, GTMaterials.Magnetite, GTMaterials.GarnetRed, GTMaterials.Galena, GTMaterials.Plutonium239, GTMaterials.CassiteriteSand, GTMaterials.Gypsum, GTMaterials.Aluminium, GTMaterials.Palladium, GTMaterials.Silver, GTMaterials.Topaz, GTMaterials.Gold, GTMaterials.Lazurite, GTMaterials.Sodalite, GTMaterials.Bastnasite, GTOMaterials.Zircon, GTMaterials.GarnetSand, GTMaterials.Wulfenite, GTMaterials.Cobaltite, GTMaterials.Tungstate, GTMaterials.Diamond, GTMaterials.Garnierite, GTMaterials.Cooperite, GTOMaterials.SalamanderCrystal, GTMaterials.Tungsten, GTMaterials.Apatite, GTMaterials.Pyrochlore, GTMaterials.Realgar, GTMaterials.RockSalt, GTMaterials.Cassiterite, GTMaterials.Sapphire, GTMaterials.Cinnabar, GTMaterials.Cobalt, GTMaterials.Barite, GTMaterials.Uraninite, GTMaterials.Sulfur, GTMaterials.YellowLimonite, GTMaterials.Lepidolite, GTMaterials.Titanium, GTMaterials.Tetrahedrite, GTOMaterials.Calorite, GTOMaterials.PerditioCrystal, GTMaterials.Powellite, GTMaterials.Redstone, GTMaterials.Sphalerite, GTMaterials.Bauxite, GTMaterials.NetherQuartz, GTMaterials.Pyrite, GTMaterials.Saltpeter, GTMaterials.VanadiumMagnetite, GTMaterials.Pentlandite, GTMaterials.Amethyst, GTMaterials.GreenSapphire, GTMaterials.Quartzite, GTMaterials.Talc, GTMaterials.GarnetYellow, GTOMaterials.SylphCrystal, GTMaterials.Oilsands, GTMaterials.GraniticMineralSand, GTMaterials.Diatomite, GTMaterials.Malachite, GTOMaterials.Ostrum, GTMaterials.BasalticMineralSand, GTMaterials.Molybdenum, GTMaterials.Spodumene, GTMaterials.GlauconiteSand },
                    new Material[] { GTOMaterials.Desh, GTMaterials.CertusQuartz, GTMaterials.Plutonium239, GTMaterials.Bornite, GTMaterials.Gypsum, GTMaterials.Aluminium, GTMaterials.Spessartine, GTMaterials.Palladium, GTMaterials.Silver, GTMaterials.Alunite, GTMaterials.Mica, GTMaterials.TricalciumPhosphate, GTMaterials.Kyanite, GTMaterials.Neodymium, GTMaterials.Tantalite, GTOMaterials.Zircon, GTOMaterials.Celestine, GTMaterials.Lithium, GTMaterials.Wulfenite, GTMaterials.Platinum, GTMaterials.Cobaltite, GTMaterials.Diamond, GTMaterials.Garnierite, GTMaterials.Cooperite, GTMaterials.Tungsten, GTMaterials.Olivine, GTMaterials.Pyrochlore, GTMaterials.Electrotine, GTMaterials.Sapphire, GTMaterials.Molybdenite, GTMaterials.Cinnabar, GTMaterials.Cobalt, GTMaterials.Barite, GTMaterials.Uraninite, GTMaterials.Monazite, GTMaterials.Lepidolite, GTMaterials.Titanium, GTOMaterials.Calorite, GTMaterials.Sphalerite, GTMaterials.Powellite, GTMaterials.Chromite, GTMaterials.Pentlandite, GTMaterials.Saltpeter, GTMaterials.GreenSapphire, GTMaterials.Bentonite, GTMaterials.Pollucite, GTMaterials.Talc, GTMaterials.Salt, GTMaterials.Soapstone, GTMaterials.Calcite, GTMaterials.Opal, GTOMaterials.Ostrum, GTMaterials.Nickel, GTMaterials.Spodumene, GTMaterials.Magnesite, GTMaterials.Molybdenum, GTMaterials.Trona, GTMaterials.GlauconiteSand, GTMaterials.Lead },
            };

            // int组
            int[][] material_weights = new int[][] {
                    // 维度: Overworld (主世界)--ID: minecraft:overworld
                    new int[] { 1454, 1200, 500, 1125, 6000, 725, 200, 2400, 375, 333, 5333, 200, 166, 1125, 544, 333, 750, 250, 187, 400, 800, 250, 272, 400, 500, 375, 474, 642, 125, 166, 500, 428, 3666, 562, 214, 2090, 200, 6136, 2400, 142, 1896, 1000, 600, 1454, 474, 400, 250, 750, 750, 474, 214, 375, 428, 187, 83, 250, 285, 750, 474, 375, 600, 400, 1200, 375, 375, 400, 800, 600, 250, 142, 312, 1454, 166 },
                    new int[] { 142, 200, 375, 333, 200, 166, 333, 250, 187, 400, 400, 250, 400, 285, 272, 250, 214, 187, 83, 250, 375, 400, 166, 125, 375, 375, 375, 375, 400, 250, 214, 142, 200, 312, 166 },
                    // 维度: Ancient World (远古世界)--ID: gtocore:ancient_world
                    new int[] { 166, 1454, 142, 333, 437, 3999, 375, 218, 333, 125, 437, 17, 1125, 1200, 999, 750, 187, 2120, 375, 125, 750, 214, 250, 53, 428, 375, 285, 900, 964, 642, 375, 428, 250, 375, 250, 17, 656, 562, 250, 214, 35, 142, 1284, 1000, 3453, 3636 },
                    new int[] { 166, 53, 250, 214, 142, 333, 285, 218, 125, 17, 333, 250, 250, 142, 35, 250, 214, 17, 187, 125 },
                    // 维度: Nether (下界)--ID: minecraft:the_nether
                    new int[] { 166, 1000, 750, 333, 437, 3999, 375, 750, 1125, 218, 333, 17, 125, 600, 375, 437, 1200, 999, 187, 666, 375, 53, 400, 900, 375, 250, 250, 562, 964, 656, 17, 35, 1284, 200, 1999 },
                    new int[] { 166, 53, 333, 218, 375, 375, 125, 17, 333, 375, 250, 250, 35, 17, 187, 200, 375 },
                    // 维度: Moon (月球)--ID: ad_astra:moon
                    new int[] { 800, 93, 150, 300, 1200, 600, 300, 250, 5333, 375, 200, 600, 400, 2666, 800, 150, 561, 125, 400, 450, 250 },
                    new int[] { 93, 150, 250, 300, 300, 375, 200, 150, 250, 125 },
                    // 维度: Mars (火星)--ID: ad_astra:mars
                    new int[] { 1454, 2400, 3999, 1125, 6000, 600, 200, 2400, 75, 25, 125, 200, 166, 999, 333, 250, 1454, 699, 400, 400, 375, 750, 233, 50, 466, 83, 750, 50, 500, 166, 400, 375, 1200, 250, 250, 600, 3453, 3636 },
                    new int[] { 233, 50, 83, 200, 75, 50, 25, 166, 125, 375, 200, 166, 250, 250, 333, 250, 375 },
                    // 维度: Venus (金星)--ID: ad_astra:venus
                    new int[] { 100, 1000, 750, 500, 293, 750, 1125, 333, 17, 333, 375, 225, 544, 666, 56, 53, 272, 187, 125, 35, 200, 17, 90, 62, 166 },
                    new int[] { 100, 53, 187, 125, 17, 17, 200, 35, 90, 56, 62, 166 },
                    // 维度: Mercury (水星)--ID: ad_astra:mercury
                    new int[] { 437, 375, 150, 293, 25, 1000, 125, 437, 187, 375, 125, 50, 250, 375, 50, 150, 500, 375, 250, 250, 1000, 562, 250, 656, 300, 2500 },
                    new int[] { 50, 250, 293, 150, 50, 25, 150, 125, 250, 250, 250, 187, 125 },
                    // 维度: Ceres (谷神星)--ID: gtocore:ceres
                    new int[] { 150, 2400, 125, 2400, 6000, 375, 1000, 266, 150, 187, 699, 450, 125, 233, 466, 187, 250, 125, 375, 500, 1200, 375, 133, 1000, 562, 312, 2500 },
                    new int[] { 233, 150, 187, 125, 250, 125, 266, 133, 150, 187, 312, 125 },
                    // 维度: Io (木卫一)--ID: gtocore:io
                    new int[] { 1000, 2400, 561, 2400, 93, 125, 200, 6000, 333, 17, 544, 666, 400, 272, 150, 53, 187, 150, 125, 400, 1200, 600, 35, 17, 90, 300, 62 },
                    new int[] { 187, 53, 150, 93, 125, 200, 150, 125, 17, 17, 35, 90, 62 },
                    // 维度: Ganymede (木卫三)--ID: gtocore:ganymede
                    new int[] { 250, 300, 83, 300, 250, 375, 1000, 5333, 375, 500, 600, 166, 3666, 250, 250, 964, 1284, 250, 250, 2500 },
                    new int[] { 250, 250, 83, 300, 300, 166, 250, 250, 250, 250 },
                    // 维度: Enceladus (土卫二)--ID: gtocore:enceladus
                    new int[] { 400, 1454, 50, 500, 53, 133, 437, 25, 266, 293, 50, 333, 600, 17, 437, 656, 17, 35, 1454, 200, 166, 1454, 3636 },
                    new int[] { 50, 53, 133, 266, 50, 25, 17, 35, 17, 200, 166 },
                    // 维度: Titan (土卫六)--ID: gtocore:titan
                    new int[] { 93, 100, 2400, 168, 1200, 2400, 6000, 3999, 225, 999, 333, 56, 800, 214, 428, 166, 500, 642, 1200, 400, 800, 214, 200, 561, 1999 },
                    new int[] { 100, 93, 214, 428, 168, 166, 400, 225, 333, 214, 200, 56 },
                    // 维度: Pluto (冥王星)--ID: gtocore:pluto
                    new int[] { 750, 93, 561, 1125, 500, 93, 83, 500, 500, 750, 200, 333, 166, 166, 375, 200, 333, 250, 200, 561, 333, 166, 200, 166 },
                    new int[] { 166, 93, 93, 83, 200, 166, 200, 166, 250, 200, 200, 166 },
                    // 维度: Glacio (霜原星)--ID: ad_astra:glacio
                    new int[] { 150, 142, 1200, 75, 5333, 25, 166, 266, 1125, 250, 750, 150, 200, 699, 233, 450, 200, 800, 750, 50, 150, 466, 83, 285, 200, 200, 600, 375, 428, 133, 200, 400, 2666, 800, 142, 2000, 300 },
                    new int[] { 233, 50, 150, 150, 142, 83, 200, 75, 200, 25, 200, 166, 133, 250, 142, 150, 200, 200 },
                    // 维度: Barnarda C (巴纳德C)--ID: gtocore:barnarda_c
                    new int[] { 400, 150, 561, 500, 142, 93, 428, 200, 285, 500, 316, 333, 600, 400, 600, 600, 333, 142, 200, 400, 2000, 300, 166 },
                    new int[] { 142, 93, 200, 285, 150, 316, 142, 200, 300, 166 },
                    // 维度: Otherside (幽冥)--ID: deeperdarker:otherside
                    new int[] { 1454, 100, 375, 3150, 7125, 293, 5333, 125, 166, 544, 333, 250, 150, 387, 400, 699, 233, 50, 150, 400, 125, 642, 250, 562, 656, 35, 964, 1284, 2090, 6136, 150, 437, 561, 566, 1000, 225, 999, 474, 750, 187, 428, 83, 285, 375, 474, 375, 575, 375, 800, 250, 200, 300, 561, 3453, 166, 333, 893, 1125, 500, 93, 1200, 200, 300, 25, 333, 437, 841, 1125, 750, 450, 200, 800, 53, 250, 466, 272, 375, 200, 474, 200, 500, 316, 500, 428, 3666, 214, 200, 300, 166, 93, 1000, 3150, 142, 133, 3999, 150, 1896, 17, 600, 333, 600, 1200, 2120, 375, 456, 250, 750, 214, 900, 250, 750, 474, 600, 400, 650, 1200, 133, 600, 17, 142, 312 },
                    new int[] { 100, 333, 93, 293, 200, 300, 375, 25, 333, 125, 166, 333, 250, 150, 387, 200, 150, 233, 53, 50, 250, 272, 375, 200, 200, 125, 316, 250, 214, 35, 200, 300, 166, 93, 150, 142, 133, 150, 333, 17, 225, 250, 375, 214, 187, 83, 250, 285, 375, 375, 375, 133, 250, 142, 200, 17, 300, 312, 166 },
            };

            Item[] inputItem = new Item[] {
                    // 维度: Overworld (主世界)--ID: minecraft:overworld
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTMaterials.Steel), GTOChemicalHelper.getItem(TagPrefix.ingot, GTOMaterials.Manasteel),
                    // 维度: Ancient World (远古世界)--ID: gtocore:ancient_world
                    GTOItems.ULV_ROBOT_ARM.asItem(), GTOItems.ULV_FLUID_REGULATOR.asItem(),
                    // 维度: Nether (下界)--ID: minecraft:the_nether
                    Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, Items.NETHERITE_INGOT,
                    // 维度: Moon (月球)--ID: ad_astra:moon
                    GTItems.ROBOT_ARM_EV.asItem(), GTItems.FLUID_REGULATOR_EV.asItem(),
                    // 维度: Mars (火星)--ID: ad_astra:mars
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTMaterials.TungstenSteel), GTOChemicalHelper.getItem(TagPrefix.ingot, GTMaterials.RTMAlloy),
                    // 维度: Venus (金星)--ID: ad_astra:venus
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTOMaterials.Desh), GTOChemicalHelper.getItem(TagPrefix.plateDense, GTOMaterials.Desh),
                    // 维度: Mercury (水星)--ID: ad_astra:mercury
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTOMaterials.Calorite), GTOChemicalHelper.getItem(TagPrefix.plateDense, GTOMaterials.Calorite),
                    // 维度: Ceres (谷神星)--ID: gtocore:ceres
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTOMaterials.Ostrum), GTOChemicalHelper.getItem(TagPrefix.plateDense, GTOMaterials.Ostrum),
                    // 维度: Io (木卫一)--ID: gtocore:io
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTMaterials.NaquadahAlloy), GTOChemicalHelper.getItem(TagPrefix.ingot, GTMaterials.Naquadria),
                    // 维度: Ganymede (木卫三)--ID: gtocore:ganymede
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTMaterials.Naquadah), GTOChemicalHelper.getItem(TagPrefix.ingot, GTMaterials.Trinium),
                    // 维度: Enceladus (土卫二)--ID: gtocore:enceladus
                    GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Tellurium), GTOChemicalHelper.getItem(TagPrefix.dust, GTMaterials.Rubidium),
                    // 维度: Titan (土卫六)--ID: gtocore:titan
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTOMaterials.HydroxyapatiteCeramic), GTOChemicalHelper.getItem(TagPrefix.ingot, GTOMaterials.Sunnarium),
                    // 维度: Pluto (冥王星)--ID: gtocore:pluto
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTMaterials.Darmstadtium), GTOChemicalHelper.getItem(TagPrefix.ingot, GTMaterials.Tritanium),
                    // 维度: Glacio (霜原星)--ID: ad_astra:glacio
                    // 维度: Barnarda C (巴纳德C)--ID: gtocore:barnarda_c
                    // 维度: Otherside (幽冥)--ID: deeperdarker:otherside
            };
            int[] source = new int[] {
                    // 维度: Overworld (主世界)--ID: minecraft:overworld
                    1, 5,
                    // 维度: Ancient World (远古世界)--ID: gtocore:ancient_world
                    2, 10,
                    // 维度: Nether (下界)--ID: minecraft:the_nether
                    5, 50,
                    // 维度: Moon (月球)--ID: ad_astra:moon
                    20, 100,
                    // 维度: Mars (火星)--ID: ad_astra:mars
                    30, 150,
                    // 维度: Venus (金星)--ID: ad_astra:venus
                    30, 150,
                    // 维度: Mercury (水星)--ID: ad_astra:mercury
                    80, 400,
                    // 维度: Ceres (谷神星)--ID: gtocore:ceres
                    80, 400,
                    // 维度: Io (木卫一)--ID: gtocore:io
                    160, 800,
                    // 维度: Ganymede (木卫三)--ID: gtocore:ganymede
                    160, 800,
                    // 维度: Enceladus (土卫二)--ID: gtocore:enceladus
                    300, 1500,
                    // 维度: Titan (土卫六)--ID: gtocore:titan
                    300, 1500,
                    // 维度: Pluto (冥王星)--ID: gtocore:pluto
                    300, 1500,
                    // 维度: Glacio (霜原星)--ID: ad_astra:glacio
                    // 维度: Barnarda C (巴纳德C)--ID: gtocore:barnarda_c
                    // 维度: Otherside (幽冥)--ID: deeperdarker:otherside
            };
            Item[] consumeitem = new Item[] {
                    // 维度: Overworld (主世界)--ID: minecraft:overworld
                    GTOItems.COLORFUL_MYSTICAL_FLOWER.asItem(),
                    // 维度: Ancient World (远古世界)--ID: gtocore:ancient_world
                    GTOItems.PRECISION_STEAM_MECHANISM.asItem(),
                    // 维度: Nether (下界)--ID: minecraft:the_nether
                    Items.WITHER_SKELETON_SKULL,
                    // 维度: Moon (月球)--ID: ad_astra:moon
                    GTItems.FIELD_GENERATOR_EV.asItem(),
                    // 维度: Mars (火星)--ID: ad_astra:mars
                    GTItems.NANO_PROCESSOR_ASSEMBLY_EV.asItem(),
                    // 维度: Venus (金星)--ID: ad_astra:venus
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTMaterials.UraniumTriplatinum),
                    // 维度: Mercury (水星)--ID: ad_astra:mercury
                    GTOItems.GRAPHENE_IRON_PLATE.asItem(),
                    // 维度: Ceres (谷神星)--ID: gtocore:ceres
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTMaterials.Plutonium241),
                    // 维度: Io (木卫一)--ID: gtocore:io
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTOMaterials.SiliconNitrideCeramic),
                    // 维度: Ganymede (木卫三)--ID: gtocore:ganymede
                    GTItems.RAW_CRYSTAL_CHIP.asItem(),
                    // 维度: Enceladus (土卫二)--ID: gtocore:enceladus
                    GTOChemicalHelper.getItem(TagPrefix.ingot, GTMaterials.UraniumRhodiumDinaquadide),
                    // 维度: Titan (土卫六)--ID: gtocore:titan
                    GTOItems.SUPER_CEREBRUM.asItem(),
                    // 维度: Pluto (冥王星)--ID: gtocore:pluto
                    GTItems.STEM_CELLS.asItem(),
                    // 维度: Glacio (霜原星)--ID: ad_astra:glacio
                    // 维度: Barnarda C (巴纳德C)--ID: gtocore:barnarda_c
                    // 维度: Otherside (幽冥)--ID: deeperdarker:otherside
            };
            Block[][] stones = new Block[][] {
                    // 维度: Overworld (主世界)--ID: minecraft:overworld
                    new Block[] { Blocks.STONE, Blocks.DEEPSLATE },
                    // 维度: Ancient World (远古世界)--ID: gtocore:ancient_world
                    new Block[] { Blocks.DEEPSLATE, Blocks.CLAY },
                    // 维度: Nether (下界)--ID: minecraft:the_nether
                    new Block[] { Blocks.NETHERRACK, Blocks.BASALT },
                    // 维度: Moon (月球)--ID: ad_astra:moon
                    new Block[] { ModBlocks.MOON_STONE.get(), ModBlocks.MOON_SAND.get(), ModBlocks.MOON_CHEESE_ORE.get() },
                    // 维度: Mars (火星)--ID: ad_astra:mars
                    new Block[] { ModBlocks.MARS_STONE.get(), ModBlocks.MARS_SAND.get(), ModBlocks.MARS_ICE_SHARD_ORE.get() },
                    // 维度: Venus (金星)--ID: ad_astra:venus
                    new Block[] { ModBlocks.VENUS_STONE.get(), ModBlocks.VENUS_SAND.get(), ModBlocks.VENUS_SANDSTONE.get() },
                    // 维度: Mercury (水星)--ID: ad_astra:mercury
                    new Block[] { ModBlocks.MERCURY_STONE.get() },
                    // 维度: Ceres (谷神星)--ID: gtocore:ceres
                    new Block[] { GTOBlocks.CERES_STONE.get(), GTOBlocks.CERES_GRUNT.get() },
                    // 维度: Io (木卫一)--ID: gtocore:io
                    new Block[] { GTOBlocks.IO_STONE.get(), GTOBlocks.IO_ASH.get() },
                    // 维度: Ganymede (木卫三)--ID: gtocore:ganymede
                    new Block[] { GTOBlocks.GANYMEDE_STONE.get(), GTOBlocks.GANYMEDE_GRUNT.get() },
                    // 维度: Enceladus (土卫二)--ID: gtocore:enceladus
                    new Block[] { GTOBlocks.ENCELADUS_STONE.get() },
                    // 维度: Titan (土卫六)--ID: gtocore:titan
                    new Block[] { GTOBlocks.TITAN_STONE.get(), GTOBlocks.TITAN_GRUNT.get() },
                    // 维度: Pluto (冥王星)--ID: gtocore:pluto
                    new Block[] { GTOBlocks.PLUTO_STONE.get(), GTOBlocks.PLUTO_GRUNT.get() },
                    // 维度: Glacio (霜原星)--ID: ad_astra:glacio
                    new Block[] { ModBlocks.GLACIO_STONE.get(), ModBlocks.GLACIO_ICE_SHARD_ORE.get() },
                    // 维度: Barnarda C (巴纳德C)--ID: gtocore:barnarda_c
                    new Block[] { Blocks.STONE, Blocks.DEEPSLATE },
                    // 维度: Otherside (幽冥)--ID: deeperdarker:otherside
                    new Block[] { DDBlocks.SCULK_STONE.get(), DDBlocks.GLOOMSLATE.get() },
            };
            int[][] stonesWeights = new int[][] {
                    // 维度: Overworld (主世界)--ID: minecraft:overworld
                    new int[] { 200, 50 },
                    // 维度: Ancient World (远古世界)--ID: gtocore:ancient_world
                    new int[] { 200, 50 },
                    // 维度: Nether (下界)--ID: minecraft:the_nether
                    new int[] { 200, 50 },
                    // 维度: Moon (月球)--ID: ad_astra:moon
                    new int[] { 200, 50, 20 },
                    // 维度: Mars (火星)--ID: ad_astra:mars
                    new int[] { 200, 50, 20 },
                    // 维度: Venus (金星)--ID: ad_astra:venus
                    new int[] { 200, 50, 20 },
                    // 维度: Mercury (水星)--ID: ad_astra:mercury
                    new int[] { 200 },
                    // 维度: Ceres (谷神星)--ID: gtocore:ceres
                    new int[] { 200, 50 },
                    // 维度: Io (木卫一)--ID: gtocore:io
                    new int[] { 200, 50 },
                    // 维度: Ganymede (木卫三)--ID: gtocore:ganymede
                    new int[] { 200, 50 },
                    // 维度: Enceladus (土卫二)--ID: gtocore:enceladus
                    new int[] { 200 },
                    // 维度: Titan (土卫六)--ID: gtocore:titan
                    new int[] { 200, 50 },
                    // 维度: Pluto (冥王星)--ID: gtocore:pluto
                    new int[] { 200, 50 },
                    // 维度: Glacio (霜原星)--ID: ad_astra:glacio
                    new int[] { 200, 20 },
                    // 维度: Barnarda C (巴纳德C)--ID: gtocore:barnarda_c
                    new int[] { 200, 50 },
                    // 维度: Otherside (幽冥)--ID: deeperdarker:otherside
                    new int[] { 120, 120 },
            };
            TagPrefix[] tagPrefix = new TagPrefix[] {
                    // 维度: Overworld (主世界)--ID: minecraft:overworld
                    TagPrefix.ore,
                    // 维度: Ancient World (远古世界)--ID: gtocore:ancient_world
                    TagPrefix.oreDeepslate,
                    // 维度: Nether (下界)--ID: minecraft:the_nether
                    TagPrefix.oreNetherrack,
                    // 维度: Moon (月球)--ID: ad_astra:moon
                    GTOTagPrefix.MOON_STONE,
                    // 维度: Mars (火星)--ID: ad_astra:mars
                    GTOTagPrefix.MARS_STONE,
                    // 维度: Venus (金星)--ID: ad_astra:venus
                    GTOTagPrefix.VENUS_STONE,
                    // 维度: Mercury (水星)--ID: ad_astra:mercury
                    GTOTagPrefix.MERCURY_STONE,
                    // 维度: Ceres (谷神星)--ID: gtocore:ceres
                    GTOTagPrefix.CERES_STONE,
                    // 维度: Io (木卫一)--ID: gtocore:io
                    GTOTagPrefix.IO_STONE,
                    // 维度: Ganymede (木卫三)--ID: gtocore:ganymede
                    GTOTagPrefix.GANYMEDE_STONE,
                    // 维度: Enceladus (土卫二)--ID: gtocore:enceladus
                    GTOTagPrefix.ENCELADUS_STONE,
                    // 维度: Titan (土卫六)--ID: gtocore:titan
                    GTOTagPrefix.TITAN_STONE,
                    // 维度: Pluto (冥王星)--ID: gtocore:pluto
                    GTOTagPrefix.PLUTO_STONE,
                    // 维度: Glacio (霜原星)--ID: ad_astra:glacio
                    GTOTagPrefix.GLACIO_STONE,
                    // 维度: Barnarda C (巴纳德C)--ID: gtocore:barnarda_c
                    TagPrefix.ore,
                    // 维度: Otherside (幽冥)--ID: deeperdarker:otherside
                    GTOTagPrefix.SCULK_STONE
            };

            for (int i = 0; i < consumeitem.length; i++) {
                int m = i * 2;
                MeteoriteRegistryHelper.registerMeteoriteType(
                        inputItem[m],
                        source[m],
                        consumeitem[i],
                        stones[i],
                        stonesWeights[i],
                        tagPrefix[i],
                        materials[m],
                        material_weights[m]);

                m++;
                MeteoriteRegistryHelper.registerMeteoriteType(
                        inputItem[m],
                        source[m],
                        consumeitem[i],
                        stones[i],
                        stonesWeights[i],
                        tagPrefix[i],
                        materials[m],
                        material_weights[m]);
            }
        }
    }
}
