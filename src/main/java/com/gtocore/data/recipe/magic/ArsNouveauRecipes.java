package com.gtocore.data.recipe.magic;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.data.recipe.builder.ars.EnchantingApparatusRecipeBuilder;
import com.gtocore.data.recipe.builder.ars.ImbuementRecipeBuilder;

import com.gtolib.GTOCore;
import com.gtolib.api.data.chemical.GTOChemicalHelper;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;

import com.hollingsworth.arsnouveau.common.datagen.RecipeDatagen;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.INFUSER_CORE_RECIPES;

public final class ArsNouveauRecipes {

    public static void init() {
        // 灌注室
        {
            ImbuementRecipe("opal_gem", Ingredient.of(GTOChemicalHelper.getItem(gem, Opal)), new ItemStack(ItemsRegistry.SOURCE_GEM), 500, new Ingredient[0]);
            ImbuementRecipe("olivine_gem", Ingredient.of(GTOChemicalHelper.getItem(gem, Olivine)), new ItemStack(ItemsRegistry.SOURCE_GEM), 500, new Ingredient[0]);
            ImbuementRecipe("opal_gem_block", Ingredient.of(GTOChemicalHelper.getItem(block, Opal)), new ItemStack(BlockRegistry.SOURCE_GEM_BLOCK.asItem()), 4000, new Ingredient[0]);
            ImbuementRecipe("olivine_gem_block", Ingredient.of(GTOChemicalHelper.getItem(block, Olivine)), new ItemStack(BlockRegistry.SOURCE_GEM_BLOCK.asItem()), 4000, new Ingredient[0]);

            ImbuementRecipe("fertilizer_dye", Ingredient.of(Items.BONE_MEAL), new ItemStack(BotaniaItems.fertilizer), 500, new Ingredient[0]);

            ImbuementRecipe("honey_bottle", Ingredient.of(Items.GLASS_BOTTLE), new ItemStack(Items.HONEY_BOTTLE), 5000,
                    new Ingredient[] { Ingredient.of(Items.BEE_SPAWN_EGG) });
            ImbuementRecipe("honey_comb", Ingredient.of(ItemTags.SMALL_FLOWERS), new ItemStack(Items.HONEYCOMB), 5000,
                    new Ingredient[] { Ingredient.of(Items.BEE_SPAWN_EGG) });

            ImbuementRecipe("gaia_core", Ingredient.of(RegistriesUtils.getItem("ars_nouveau:mirrorweave")), new ItemStack(GTOItems.GAIA_CORE), 10000,
                    new Ingredient[] { Ingredient.of(BotaniaItems.lifeEssence), Ingredient.of(BotaniaItems.lifeEssence), Ingredient.of(BotaniaItems.lifeEssence), Ingredient.of(BotaniaItems.lifeEssence) });
            ImbuementRecipe("bifrost_perm", Ingredient.of(BotaniaBlocks.elfGlass), new ItemStack(BotaniaBlocks.bifrostPerm), 1000,
                    new Ingredient[] { Ingredient.of(RegistriesUtils.getItem("botania:rainbow_rod")) });

            ImbuementRecipe("spirit_fuel", Ingredient.of(ExtraBotanyItems.nightmareFuel), new ItemStack(ExtraBotanyItems.spiritFuel), 5000,
                    new Ingredient[] { Ingredient.of(Blocks.PLAYER_HEAD) });
        }

        // 附魔装置
        {
            EnchantingApparatusRecipe("frozen_pearl", Ingredient.of(Items.ENDER_PEARL), new ItemStack(RegistriesUtils.getItem("torchmaster:frozen_pearl")), 10000, false,
                    new Ingredient[] { Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE),
                            Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE) });
            EnchantingApparatusRecipe("runic_altar", Ingredient.of(BotaniaItems.manaPearl), new ItemStack(BotaniaBlocks.runeAltar), 10000, false,
                    new Ingredient[] { Ingredient.of(BotaniaBlocks.livingrock), Ingredient.of(BotaniaBlocks.livingrock), Ingredient.of(BotaniaBlocks.livingrock), Ingredient.of(BotaniaBlocks.livingrock),
                            Ingredient.of(BotaniaBlocks.livingrock), Ingredient.of(BotaniaBlocks.livingrock) });
            EnchantingApparatusRecipe("terra_plate", Ingredient.of(BotaniaBlocks.manasteelBlock), new ItemStack(BotaniaBlocks.terraPlate), 10000, false,
                    new Ingredient[] { Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)),
                            Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)) });
            EnchantingApparatusRecipe("alfheim_portal", Ingredient.of(BotaniaBlocks.livingwood), new ItemStack(BotaniaBlocks.alfPortal), 10000, false,
                    new Ingredient[] { Ingredient.of(BotaniaBlocks.manasteelBlock), Ingredient.of(BotaniaBlocks.terrasteelBlock), Ingredient.of(GTOChemicalHelper.getItem(block, GTOMaterials.InfusedGold)), Ingredient.of(GTOChemicalHelper.getItem(block, GTOMaterials.Thaumium)),
                            Ingredient.of(BotaniaBlocks.manasteelBlock), Ingredient.of(BotaniaBlocks.terrasteelBlock), Ingredient.of(GTOChemicalHelper.getItem(block, GTOMaterials.InfusedGold)), Ingredient.of(GTOChemicalHelper.getItem(block, GTOMaterials.Thaumium)) });

            EnchantingApparatusRecipe("mana_pylon", Ingredient.of(BotaniaItems.manaDiamond), new ItemStack(BotaniaBlocks.manaPylon), 10000, false,
                    new Ingredient[] { Ingredient.of(BotaniaItems.manaSteel), Ingredient.of(BotaniaItems.manaSteel), Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.GOLD_INGOT),
                            Ingredient.of(BotaniaItems.manaSteel), Ingredient.of(BotaniaItems.manaSteel), Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.GOLD_INGOT) });
            EnchantingApparatusRecipe("natura_pylon", Ingredient.of(BotaniaBlocks.manaPylon), new ItemStack(BotaniaBlocks.naturaPylon), 10000, false,
                    new Ingredient[] { Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaBlocks.forestEye),
                            Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaBlocks.forestEye) });
            EnchantingApparatusRecipe("alfsteel_pylon", Ingredient.of(BotaniaBlocks.naturaPylon), RegistriesUtils.getItemStack("mythicbotany:alfsteel_pylon"), 10000, false,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(BotaniaBlocks.lightRelayDefault), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Herbs)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_ingot")),
                            Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(BotaniaBlocks.lightRelayDefault), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Herbs)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_ingot")),
                            Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(BotaniaBlocks.lightRelayDefault), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Herbs)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_ingot")),
                            Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(BotaniaBlocks.lightRelayDefault), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Herbs)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_ingot")) });
            EnchantingApparatusRecipe("gaia_pylon", Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_pylon")), new ItemStack(BotaniaBlocks.gaiaPylon.asItem()), 10000, false,
                    new Ingredient[] { Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Laureril)), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Gaiasteel)), Ingredient.of(BotaniaBlocks.shimmerrock), Ingredient.of(BotaniaBlocks.dreamwoodGlimmering),
                            Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Laureril)), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Gaiasteel)), Ingredient.of(BotaniaBlocks.shimmerrock), Ingredient.of(BotaniaBlocks.dreamwoodGlimmering),
                            Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Laureril)), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Gaiasteel)), Ingredient.of(BotaniaBlocks.shimmerrock), Ingredient.of(BotaniaBlocks.dreamwoodGlimmering),
                            Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Laureril)), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Gaiasteel)), Ingredient.of(BotaniaBlocks.shimmerrock), Ingredient.of(BotaniaBlocks.dreamwoodGlimmering) });

            EnchantingApparatusRecipe("enchanting_laureril_ingot", Ingredient.of(GTOChemicalHelper.getItem(plate, Runerock)), new ItemStack(GTOChemicalHelper.getItem(ingot, Laureril)), 10000, false,
                    new Ingredient[] { Ingredient.of(GTOChemicalHelper.getItem(ingot, Thaumium)), Ingredient.of(GTOChemicalHelper.getItem(ingot, WhiteWax)), Ingredient.of(GTOChemicalHelper.getItem(ingot, InfusedGold)), Ingredient.of(GTOChemicalHelper.getItem(ingot, Herbs)) });

            EnchantingApparatusRecipe("enchanting_starbuncle_shards", Ingredient.of(ItemsRegistry.STARBUNCLE_SHARD), new ItemStack(ItemsRegistry.STARBUNCLE_SHARD, 4), 10000, false,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.EARTH_ESSENCE), Ingredient.of(ItemsRegistry.FIRE_ESSENCE) });
            EnchantingApparatusRecipe("enchanting_whirlisprig_shards", Ingredient.of(ItemsRegistry.WHIRLISPRIG_SHARDS), new ItemStack(ItemsRegistry.WHIRLISPRIG_SHARDS, 4), 10000, false,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.AIR_ESSENCE), Ingredient.of(ItemsRegistry.WATER_ESSENCE) });
            EnchantingApparatusRecipe("enchanting_drygmy_shard", Ingredient.of(ItemsRegistry.DRYGMY_SHARD), new ItemStack(ItemsRegistry.DRYGMY_SHARD, 4), 10000, false,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.EARTH_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE) });
            EnchantingApparatusRecipe("enchanting_magic_shards", Ingredient.of(ItemsRegistry.WIXIE_SHARD), new ItemStack(ItemsRegistry.WIXIE_SHARD, 4), 10000, false,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.AIR_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE) });

            EnchantingApparatusRecipe("enchanting_earth_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.EARTH_ESSENCE), 5000, false,
                    new Ingredient[] { Ingredient.of(Items.IRON_INGOT), Ingredient.of(ItemTags.DIRT), Ingredient.of(ForgeTags.SEEDS), Ingredient.of(RegistriesUtils.getItemStack("gtocore:gnome_bucket")) });
            EnchantingApparatusRecipe("enchanting_air_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.AIR_ESSENCE), 5000, false,
                    new Ingredient[] { Ingredient.of(Items.FEATHER), Ingredient.of(ItemTags.ARROWS), Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(RegistriesUtils.getItemStack("gtocore:sylph_bucket")) });
            EnchantingApparatusRecipe("enchanting_water_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.WATER_ESSENCE), 5000, false,
                    new Ingredient[] { Ingredient.of(Items.KELP), Ingredient.of(Items.SNOW_BLOCK), Ingredient.of(Items.WATER_BUCKET), Ingredient.of(RegistriesUtils.getItemStack("gtocore:undine_bucket")) });
            EnchantingApparatusRecipe("enchanting_fire_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.FIRE_ESSENCE), 5000, false,
                    new Ingredient[] { Ingredient.of(Items.FLINT_AND_STEEL), Ingredient.of(Items.GUNPOWDER), Ingredient.of(Items.TORCH), Ingredient.of(RegistriesUtils.getItemStack("gtocore:salamander_bucket")) });
            EnchantingApparatusRecipe("enchanting_manipulation_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.MANIPULATION_ESSENCE), 5000, false,
                    new Ingredient[] { Ingredient.of(Items.STONE_BUTTON), Ingredient.of(Items.REDSTONE), Ingredient.of(Items.CLOCK), Ingredient.of(RegistriesUtils.getItemStack("gtocore:aether_bucket")) });
            EnchantingApparatusRecipe("enchanting_abjuration_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.ABJURATION_ESSENCE), 5000, false,
                    new Ingredient[] { Ingredient.of(Items.SUGAR), Ingredient.of(Items.FERMENTED_SPIDER_EYE), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(RegistriesUtils.getItemStack("gtocore:aether_bucket")) });
            EnchantingApparatusRecipe("enchanting_conjuration_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.CONJURATION_ESSENCE), 5000, false,
                    new Ingredient[] { Ingredient.of(Items.BOOK), Ingredient.of(ItemsRegistry.WILDEN_HORN), Ingredient.of(ItemsRegistry.STARBUNCLE_SHARD), Ingredient.of(RegistriesUtils.getItemStack("gtocore:aether_bucket")) });

            EnchantingApparatusRecipe("wilden_slate", Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), new ItemStack(GTOItems.WILDEN_SLATE, 9), 10000, false,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.WILDEN_HORN), Ingredient.of(ItemsRegistry.WILDEN_SPIKE), Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(ItemsRegistry.WILDEN_HORN), Ingredient.of(ItemsRegistry.WILDEN_SPIKE),
                            Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(ItemsRegistry.WILDEN_HORN), Ingredient.of(ItemsRegistry.WILDEN_SPIKE), Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(ItemsRegistry.WILDEN_TRIBUTE) });

        }

        // 多方块灌注室 + 附魔核心
        {
            // 灌注室
            MultiblockImbuementRecipe(1, false, "opal_gem", Ingredient.of(GTOChemicalHelper.getItem(gem, Opal)), new ItemStack(ItemsRegistry.SOURCE_GEM), 500, new Ingredient[0], new FluidStack[0]);
            MultiblockImbuementRecipe(1, false, "olivine_gem", Ingredient.of(GTOChemicalHelper.getItem(gem, Olivine)), new ItemStack(ItemsRegistry.SOURCE_GEM), 500, new Ingredient[0], new FluidStack[0]);
            MultiblockImbuementRecipe(1, false, "opal_gem_block", Ingredient.of(GTOChemicalHelper.getItem(block, Opal)), new ItemStack(BlockRegistry.SOURCE_GEM_BLOCK.asItem()), 4000, new Ingredient[0], new FluidStack[0]);
            MultiblockImbuementRecipe(1, false, "olivine_gem_block", Ingredient.of(GTOChemicalHelper.getItem(block, Olivine)), new ItemStack(BlockRegistry.SOURCE_GEM_BLOCK.asItem()), 4000, new Ingredient[0], new FluidStack[0]);

            // 特殊箭头配方
            MultiblockImbuementRecipe(1, false, "pierce_arrow", Ingredient.of(ItemTags.ARROWS), new ItemStack(ItemsRegistry.PIERCE_ARROW.get()), 100,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.SOURCE_GEM.get()), Ingredient.of(ItemsRegistry.AIR_ESSENCE.get()), Ingredient.of(ItemsRegistry.WILDEN_SPIKE.get()) }, new FluidStack[0]);
            MultiblockImbuementRecipe(1, false, "amplify_arrow", Ingredient.of(ItemTags.ARROWS), new ItemStack(ItemsRegistry.AMPLIFY_ARROW.get()), 100,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.SOURCE_GEM.get()), Ingredient.of(ItemsRegistry.AIR_ESSENCE.get()), Ingredient.of(Items.DIAMOND) }, new FluidStack[0]);
            MultiblockImbuementRecipe(1, false, "split_arrow", Ingredient.of(ItemTags.ARROWS), new ItemStack(ItemsRegistry.SPLIT_ARROW.get()), 100,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.SOURCE_GEM.get()), Ingredient.of(ItemsRegistry.AIR_ESSENCE.get()), Ingredient.of(ItemsRegistry.WILDEN_HORN.get()) }, new FluidStack[0]);

            // GTO灌注室
            MultiblockImbuementRecipe(1, false, "fertilizer_dye", Ingredient.of(Items.BONE_MEAL), new ItemStack(BotaniaItems.fertilizer), 500, new Ingredient[0], new FluidStack[0]);

            MultiblockImbuementRecipe(1, false, "honey_bottle", Ingredient.of(Items.GLASS_BOTTLE), new ItemStack(Items.HONEY_BOTTLE), 5000,
                    new Ingredient[] { Ingredient.of(Items.BEE_SPAWN_EGG) }, new FluidStack[0]);
            MultiblockImbuementRecipe(1, false, "honey_comb", Ingredient.of(ItemTags.SMALL_FLOWERS), new ItemStack(Items.HONEYCOMB), 5000,
                    new Ingredient[] { Ingredient.of(Items.BEE_SPAWN_EGG) }, new FluidStack[0]);

            MultiblockImbuementRecipe(1, false, "gaia_core", Ingredient.of(RegistriesUtils.getItem("ars_nouveau:mirrorweave")), new ItemStack(GTOItems.GAIA_CORE), 10000,
                    new Ingredient[] { Ingredient.of(BotaniaItems.lifeEssence), Ingredient.of(BotaniaItems.lifeEssence), Ingredient.of(BotaniaItems.lifeEssence), Ingredient.of(BotaniaItems.lifeEssence) }, new FluidStack[0]);
            MultiblockImbuementRecipe(1, false, "bifrost_perm", Ingredient.of(BotaniaBlocks.elfGlass), new ItemStack(BotaniaBlocks.bifrostPerm), 1000,
                    new Ingredient[] { Ingredient.of(RegistriesUtils.getItem("botania:rainbow_rod")) }, new FluidStack[0]);
            MultiblockImbuementRecipe(1, false, "spirit_fuel", Ingredient.of(ExtraBotanyItems.nightmareFuel), new ItemStack(ExtraBotanyItems.spiritFuel), 5000,
                    new Ingredient[] { Ingredient.of(Blocks.PLAYER_HEAD) }, new FluidStack[0]);

            // 附魔核心
            MultiblockImbuementRecipe(5, true, "belt_of_levitation", Ingredient.of(ItemsRegistry.MUNDANE_BELT), new ItemStack(ItemsRegistry.BELT_OF_LEVITATION), 10000,
                    new Ingredient[] { Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Items.FEATHER), Ingredient.of(Items.FEATHER), Ingredient.of(Items.FEATHER), Ingredient.of(ItemsRegistry.AIR_ESSENCE) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "jar_of_light", Ingredient.of(Items.GLASS_BOTTLE), new ItemStack(ItemsRegistry.JAR_OF_LIGHT), 10000,
                    new Ingredient[] { Ingredient.of(Items.GLOWSTONE), Ingredient.of(Items.GLOWSTONE), Ingredient.of(Items.GLOWSTONE), Ingredient.of(Items.GLOWSTONE), Ingredient.of(Items.REDSTONE_LAMP), Ingredient.of(Items.REDSTONE_LAMP), Ingredient.of(Tags.Items.GLASS), Ingredient.of(Tags.Items.GLASS) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(6, true, "mage_bloom_crop", Ingredient.of(Tags.Items.SEEDS), new ItemStack(BlockRegistry.MAGE_BLOOM_CROP), 10000,
                    new Ingredient[] { RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "ring_of_lesser_discount", Ingredient.of(ItemsRegistry.RING_OF_POTENTIAL), new ItemStack(ItemsRegistry.RING_OF_LESSER_DISCOUNT), 10000,
                    new Ingredient[] { Ingredient.of(Tags.Items.GEMS_DIAMOND), Ingredient.of(Tags.Items.GEMS_DIAMOND), Ingredient.of(Tags.Items.GEMS_DIAMOND), Ingredient.of(Tags.Items.GEMS_DIAMOND), Ingredient.of(Tags.Items.ENDER_PEARLS), Ingredient.of(Tags.Items.ENDER_PEARLS), RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "ring_of_greater_discount", Ingredient.of(ItemsRegistry.RING_OF_LESSER_DISCOUNT), new ItemStack(ItemsRegistry.RING_OF_GREATER_DISCOUNT), 10000,
                    new Ingredient[] { Ingredient.of(Tags.Items.GEMS_DIAMOND), Ingredient.of(Tags.Items.GEMS_DIAMOND), Ingredient.of(Tags.Items.GEMS_DIAMOND), Ingredient.of(Tags.Items.GEMS_DIAMOND), Ingredient.of(Tags.Items.RODS_BLAZE), Ingredient.of(Tags.Items.RODS_BLAZE), RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "starbuncle_charm", Ingredient.of(ItemsRegistry.STARBUNCLE_SHARD), new ItemStack(ItemsRegistry.STARBUNCLE_CHARM), 10000,
                    new Ingredient[] { Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Tags.Items.INGOTS_GOLD) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "amulet_of_mana_boost", Ingredient.of(ItemsRegistry.DULL_TRINKET), new ItemStack(ItemsRegistry.AMULET_OF_MANA_BOOST), 10000,
                    new Ingredient[] { Ingredient.of(Tags.Items.GEMS_DIAMOND), Ingredient.of(Tags.Items.GEMS_DIAMOND), Ingredient.of(Tags.Items.GEMS_DIAMOND), RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(6, true, "amulet_of_mana_regen", Ingredient.of(ItemsRegistry.DULL_TRINKET), new ItemStack(ItemsRegistry.AMULET_OF_MANA_REGEN), 10000,
                    new Ingredient[] { Ingredient.of(Tags.Items.GEMS_DIAMOND), Ingredient.of(Tags.Items.GEMS_DIAMOND), Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Tags.Items.INGOTS_GOLD), RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "whirlisprig_charm", Ingredient.of(ItemsRegistry.WHIRLISPRIG_SHARDS), new ItemStack(ItemsRegistry.WHIRLISPRIG_CHARM), 10000,
                    new Ingredient[] { RecipeDatagen.SOURCE_GEM, Ingredient.of(BlockRegistry.MAGE_BLOOM_CROP), Ingredient.of(ItemsRegistry.MAGE_BLOOM), Ingredient.of(Tags.Items.GEMS_DIAMOND), Ingredient.of(Items.OAK_SAPLING), Ingredient.of(Items.SPRUCE_SAPLING), Ingredient.of(Items.BIRCH_SAPLING), Ingredient.of(Tags.Items.SEEDS_WHEAT) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "void_jar", Ingredient.of(Items.GLASS_BOTTLE), new ItemStack(ItemsRegistry.VOID_JAR), 10000,
                    new Ingredient[] { Ingredient.of(Items.LAVA_BUCKET), Ingredient.of(Items.BUCKET), Ingredient.of(ItemsRegistry.ALLOW_ITEM_SCROLL), Ingredient.of(Tags.Items.ENDER_PEARLS) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "dominion_rod", Ingredient.of(Items.STICK), new ItemStack(ItemsRegistry.DOMINION_ROD), 10000,
                    new Ingredient[] { RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM, Ingredient.of(Tags.Items.INGOTS_GOLD) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "wixie_charm", Ingredient.of(ItemsRegistry.WIXIE_SHARD), new ItemStack(ItemsRegistry.WIXIE_CHARM), 10000,
                    new Ingredient[] { Ingredient.of(ItemTags.SAPLINGS), Ingredient.of(Tags.Items.GEMS_EMERALD), Ingredient.of(Items.CRAFTING_TABLE), Ingredient.of(Items.BREWING_STAND) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "wand", RecipeDatagen.ARCHWOOD_LOG, new ItemStack(ItemsRegistry.WAND), 10000,
                    new Ingredient[] { RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM, Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(ItemsRegistry.AIR_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "potion_flask", Ingredient.of(Items.GLASS_BOTTLE), new ItemStack(ItemsRegistry.POTION_FLASK), 10000,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.ABJURATION_ESSENCE), Ingredient.of(ItemsRegistry.ABJURATION_ESSENCE), RecipeDatagen.SOURCE_GEM_BLOCK, Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "potion_flask_extend_time", Ingredient.of(ItemsRegistry.POTION_FLASK), new ItemStack(ItemsRegistry.POTION_FLASK_EXTEND_TIME.get()), 10000,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "potion_flask_amplify", Ingredient.of(ItemsRegistry.POTION_FLASK), new ItemStack(ItemsRegistry.POTION_FLASK_AMPLIFY.get()), 10000,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.ABJURATION_ESSENCE), Ingredient.of(ItemsRegistry.ABJURATION_ESSENCE), Ingredient.of(ItemsRegistry.ABJURATION_ESSENCE), Ingredient.of(ItemsRegistry.ABJURATION_ESSENCE), Ingredient.of(ItemsRegistry.ABJURATION_ESSENCE), Ingredient.of(ItemsRegistry.ABJURATION_ESSENCE), Ingredient.of(ItemsRegistry.ABJURATION_ESSENCE), Ingredient.of(ItemsRegistry.ABJURATION_ESSENCE) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "potion_melder", Ingredient.of(BlockRegistry.POTION_JAR), new ItemStack(BlockRegistry.POTION_MELDER), 10000,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.ABJURATION_ESSENCE), Ingredient.of(ItemsRegistry.ABJURATION_ESSENCE), Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD), Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD), Ingredient.of(Tags.Items.RODS_BLAZE), Ingredient.of(Tags.Items.RODS_BLAZE), Ingredient.of(Tags.Items.RODS_BLAZE), Ingredient.of(Tags.Items.RODS_BLAZE) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "drygmy_charm", Ingredient.of(ItemsRegistry.DRYGMY_SHARD), new ItemStack(ItemsRegistry.DRYGMY_CHARM), 10000,
                    new Ingredient[] { Ingredient.of(ItemTags.FISHES), Ingredient.of(Items.WHEAT), Ingredient.of(Items.APPLE), Ingredient.of(Items.CARROT), Ingredient.of(Tags.Items.SEEDS), RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM, RecipeDatagen.SOURCE_GEM },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "summoning_focus", Ingredient.of(BlockRegistry.SOURCE_GEM_BLOCK), new ItemStack(ItemsRegistry.SUMMONING_FOCUS), 10000,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.WILDEN_HORN), Ingredient.of(ItemsRegistry.WILDEN_SPIKE), Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(ItemsRegistry.WILDEN_TRIBUTE), Ingredient.of(Tags.Items.INGOTS_GOLD) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "relay_splitter", Ingredient.of(BlockRegistry.RELAY), new ItemStack(BlockRegistry.RELAY_SPLITTER), 10000,
                    new Ingredient[] { Ingredient.of(Tags.Items.GEMS_QUARTZ), Ingredient.of(Tags.Items.GEMS_QUARTZ), Ingredient.of(Tags.Items.GEMS_QUARTZ), Ingredient.of(Tags.Items.GEMS_QUARTZ), Ingredient.of(Tags.Items.GEMS_LAPIS), Ingredient.of(Tags.Items.GEMS_LAPIS), Ingredient.of(Tags.Items.GEMS_LAPIS), Ingredient.of(Tags.Items.GEMS_LAPIS) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "relay_warp", Ingredient.of(BlockRegistry.RELAY), new ItemStack(BlockRegistry.RELAY_WARP), 10000,
                    new Ingredient[] { Ingredient.of(Tags.Items.ENDER_PEARLS), Ingredient.of(Tags.Items.ENDER_PEARLS), Ingredient.of(Tags.Items.ENDER_PEARLS), Ingredient.of(Tags.Items.ENDER_PEARLS), Ingredient.of(Items.POPPED_CHORUS_FRUIT), Ingredient.of(Items.POPPED_CHORUS_FRUIT), Ingredient.of(Items.POPPED_CHORUS_FRUIT), Ingredient.of(Items.POPPED_CHORUS_FRUIT) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "relay_deposit", Ingredient.of(BlockRegistry.RELAY), new ItemStack(BlockRegistry.RELAY_DEPOSIT), 10000,
                    new Ingredient[] { Ingredient.of(Items.HOPPER), Ingredient.of(Items.HOPPER), Ingredient.of(Items.HOPPER), Ingredient.of(Items.HOPPER) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "enchanters_mirror", Ingredient.of(BlockRegistry.SOURCE_GEM_BLOCK), new ItemStack(ItemsRegistry.ENCHANTERS_MIRROR), 10000,
                    new Ingredient[] { Ingredient.of(Tags.Items.GLASS), Ingredient.of(Tags.Items.GLASS), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE.get()), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE.get()), RecipeDatagen.ARCHWOOD_LOG, RecipeDatagen.ARCHWOOD_LOG, Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Tags.Items.INGOTS_GOLD) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "timer_spell_turret", Ingredient.of(BlockRegistry.BASIC_SPELL_TURRET), new ItemStack(BlockRegistry.TIMER_SPELL_TURRET), 10000,
                    new Ingredient[] { Ingredient.of(Items.CLOCK) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "enchanted_spell_turret", Ingredient.of(BlockRegistry.BASIC_SPELL_TURRET), new ItemStack(BlockRegistry.ENCHANTED_SPELL_TURRET), 10000,
                    new Ingredient[] { Ingredient.of(BlockRegistry.SOURCE_GEM_BLOCK), Ingredient.of(Tags.Items.RODS_BLAZE), Ingredient.of(Tags.Items.RODS_BLAZE) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "relay_collector", Ingredient.of(BlockRegistry.RELAY), new ItemStack(BlockRegistry.RELAY_COLLECTOR), 10000,
                    new Ingredient[] { Ingredient.of(Tags.Items.CHESTS), Ingredient.of(Tags.Items.CHESTS), Ingredient.of(Tags.Items.CHESTS), Ingredient.of(Tags.Items.CHESTS) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "scryers_oculus", Ingredient.of(Items.ENDER_EYE), new ItemStack(BlockRegistry.SCRYERS_OCULUS), 10000,
                    new Ingredient[] { Ingredient.of(Blocks.OBSERVER), Ingredient.of(Items.SPYGLASS), Ingredient.of(BlockRegistry.SOURCE_GEM_BLOCK), Ingredient.of(Tags.Items.GEMS_DIAMOND) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "shapers_focus", Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), new ItemStack(ItemsRegistry.SHAPERS_FOCUS), 10000,
                    new Ingredient[] { Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Items.PISTON), Ingredient.of(Items.SLIME_BLOCK), Ingredient.of(Tags.Items.GEMS_DIAMOND) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "alchemists_crown", Ingredient.of(Items.GOLDEN_HELMET), new ItemStack(ItemsRegistry.ALCHEMISTS_CROWN), 10000,
                    new Ingredient[] { Ingredient.of(Items.GLASS_BOTTLE), Ingredient.of(Items.GLASS_BOTTLE), Ingredient.of(Items.GLASS_BOTTLE) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "potion_diffuser", Ingredient.of(Blocks.CAMPFIRE), new ItemStack(BlockRegistry.POTION_DIFFUSER), 10000,
                    new Ingredient[] { Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(BlockRegistry.ARCHWOOD_PLANK), Ingredient.of(BlockRegistry.ARCHWOOD_PLANK), Ingredient.of(BlockRegistry.ARCHWOOD_PLANK) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "splash_launcher", Ingredient.of(Items.DISPENSER), new ItemStack(ItemsRegistry.SPLASH_LAUNCHER), 10000,
                    new Ingredient[] { Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Tags.Items.INGOTS_GOLD), Ingredient.of(Tags.Items.RODS_BLAZE), Ingredient.of(Tags.Items.RODS_BLAZE), Ingredient.of(Tags.Items.GUNPOWDER), Ingredient.of(Tags.Items.GUNPOWDER), Ingredient.of(Tags.Items.GUNPOWDER), Ingredient.of(Tags.Items.GUNPOWDER) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "lingering_launcher", Ingredient.of(ItemsRegistry.SPLASH_LAUNCHER), new ItemStack(ItemsRegistry.LINGERING_LAUNCHER), 10000,
                    new Ingredient[] { Ingredient.of(Items.DRAGON_BREATH), Ingredient.of(ItemsRegistry.AIR_ESSENCE), Ingredient.of(ItemsRegistry.AIR_ESSENCE) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "spell_crossbow", Ingredient.of(Items.CROSSBOW), new ItemStack(ItemsRegistry.SPELL_CROSSBOW), 10000,
                    new Ingredient[] { Ingredient.of(Tags.Items.STORAGE_BLOCKS_GOLD), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), RecipeDatagen.SOURCE_GEM_BLOCK },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "brazier_relay", Ingredient.of(BlockRegistry.RITUAL_BLOCK), new ItemStack(BlockRegistry.BRAZIER_RELAY), 10000,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "stable_warp_scroll", Ingredient.of(ItemsRegistry.WARP_SCROLL), new ItemStack(ItemsRegistry.STABLE_WARP_SCROLL), 10000,
                    new Ingredient[] { Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Tags.Items.ENDER_PEARLS), Ingredient.of(Tags.Items.ENDER_PEARLS) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "scry_caster", Ingredient.of(BlockRegistry.SCRYERS_CRYSTAL), new ItemStack(ItemsRegistry.SCRY_CASTER), 10000,
                    new Ingredient[] { Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Tags.Items.ENDER_PEARLS), Ingredient.of(Tags.Items.ENDER_PEARLS), Ingredient.of(Tags.Items.ENDER_PEARLS), Ingredient.of(Tags.Items.ENDER_PEARLS) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "crafting_lectern", Ingredient.of(Blocks.LECTERN), new ItemStack(BlockRegistry.CRAFTING_LECTERN), 10000,
                    new Ingredient[] { Ingredient.of(Tags.Items.CHESTS), Ingredient.of(Tags.Items.CHESTS), Ingredient.of(Tags.Items.CHESTS), Ingredient.of(Tags.Items.CHESTS) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(6, true, "warp_scroll_copy", Ingredient.of(ItemsRegistry.WARP_SCROLL), new ItemStack(ItemsRegistry.WARP_SCROLL, 2), 1000,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.WARP_SCROLL) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "spell_sensor", Ingredient.of(Blocks.SCULK_SENSOR), new ItemStack(BlockRegistry.SPELL_SENSOR), 10000,
                    new Ingredient[0],
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "jump_ring", Ingredient.of(ItemsRegistry.RING_OF_POTENTIAL), new ItemStack(ItemsRegistry.JUMP_RING), 10000,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(ItemsRegistry.AIR_ESSENCE) },
                    new FluidStack[0]);

            // GTO附魔核心
            MultiblockImbuementRecipe(5, true, "frozen_pearl", Ingredient.of(Items.ENDER_PEARL), new ItemStack(RegistriesUtils.getItem("torchmaster:frozen_pearl")), 10000,
                    new Ingredient[] { Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE),
                            Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "runic_altar", Ingredient.of(BotaniaItems.manaPearl), new ItemStack(BotaniaBlocks.runeAltar), 10000,
                    new Ingredient[] { Ingredient.of(BotaniaBlocks.livingrock), Ingredient.of(BotaniaBlocks.livingrock), Ingredient.of(BotaniaBlocks.livingrock), Ingredient.of(BotaniaBlocks.livingrock),
                            Ingredient.of(BotaniaBlocks.livingrock), Ingredient.of(BotaniaBlocks.livingrock) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "terra_plate", Ingredient.of(BotaniaBlocks.manasteelBlock), new ItemStack(BotaniaBlocks.terraPlate), 10000,
                    new Ingredient[] { Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)),
                            Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "alfheim_portal", Ingredient.of(BotaniaBlocks.livingwood), new ItemStack(BotaniaBlocks.alfPortal), 10000,
                    new Ingredient[] { Ingredient.of(BotaniaBlocks.manasteelBlock), Ingredient.of(BotaniaBlocks.terrasteelBlock), Ingredient.of(GTOChemicalHelper.getItem(block, GTOMaterials.InfusedGold)), Ingredient.of(GTOChemicalHelper.getItem(block, GTOMaterials.Thaumium)),
                            Ingredient.of(BotaniaBlocks.manasteelBlock), Ingredient.of(BotaniaBlocks.terrasteelBlock), Ingredient.of(GTOChemicalHelper.getItem(block, GTOMaterials.InfusedGold)), Ingredient.of(GTOChemicalHelper.getItem(block, GTOMaterials.Thaumium)) },
                    new FluidStack[0]);

            MultiblockImbuementRecipe(5, true, "mana_pylon", Ingredient.of(BotaniaItems.manaDiamond), new ItemStack(BotaniaBlocks.manaPylon), 10000,
                    new Ingredient[] { Ingredient.of(BotaniaItems.manaSteel), Ingredient.of(BotaniaItems.manaSteel), Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.GOLD_INGOT),
                            Ingredient.of(BotaniaItems.manaSteel), Ingredient.of(BotaniaItems.manaSteel), Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.GOLD_INGOT) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "natura_pylon", Ingredient.of(BotaniaBlocks.manaPylon), new ItemStack(BotaniaBlocks.naturaPylon), 10000,
                    new Ingredient[] { Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaBlocks.forestEye),
                            Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaBlocks.forestEye) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "alfsteel_pylon", Ingredient.of(BotaniaBlocks.naturaPylon), RegistriesUtils.getItemStack("mythicbotany:alfsteel_pylon"), 10000,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(BotaniaBlocks.lightRelayDefault), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Herbs)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_ingot")),
                            Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(BotaniaBlocks.lightRelayDefault), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Herbs)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_ingot")),
                            Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(BotaniaBlocks.lightRelayDefault), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Herbs)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_ingot")),
                            Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(BotaniaBlocks.lightRelayDefault), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Herbs)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_ingot")) },
                    new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "gaia_pylon", Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_pylon")), new ItemStack(BotaniaBlocks.gaiaPylon.asItem()), 10000,
                    new Ingredient[] { Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Laureril)), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Gaiasteel)), Ingredient.of(BotaniaBlocks.shimmerrock), Ingredient.of(BotaniaBlocks.dreamwoodGlimmering),
                            Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Laureril)), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Gaiasteel)), Ingredient.of(BotaniaBlocks.shimmerrock), Ingredient.of(BotaniaBlocks.dreamwoodGlimmering),
                            Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Laureril)), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Gaiasteel)), Ingredient.of(BotaniaBlocks.shimmerrock), Ingredient.of(BotaniaBlocks.dreamwoodGlimmering),
                            Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Laureril)), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Gaiasteel)), Ingredient.of(BotaniaBlocks.shimmerrock), Ingredient.of(BotaniaBlocks.dreamwoodGlimmering) },
                    new FluidStack[0]);

            MultiblockImbuementRecipe(5, true, "enchanting_laureril_ingot", Ingredient.of(GTOChemicalHelper.getItem(plate, Runerock)), new ItemStack(GTOChemicalHelper.getItem(ingot, Laureril)), 10000,
                    new Ingredient[] { Ingredient.of(GTOChemicalHelper.getItem(ingot, Thaumium)), Ingredient.of(GTOChemicalHelper.getItem(ingot, WhiteWax)), Ingredient.of(GTOChemicalHelper.getItem(ingot, InfusedGold)), Ingredient.of(GTOChemicalHelper.getItem(ingot, Herbs)) }, new FluidStack[0]);

            MultiblockImbuementRecipe(5, true, "enchanting_starbuncle_shards", Ingredient.of(ItemsRegistry.STARBUNCLE_SHARD), new ItemStack(ItemsRegistry.STARBUNCLE_SHARD, 4), 10000,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.EARTH_ESSENCE), Ingredient.of(ItemsRegistry.FIRE_ESSENCE) }, new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "enchanting_whirlisprig_shards", Ingredient.of(ItemsRegistry.WHIRLISPRIG_SHARDS), new ItemStack(ItemsRegistry.WHIRLISPRIG_SHARDS, 4), 10000,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.AIR_ESSENCE), Ingredient.of(ItemsRegistry.WATER_ESSENCE) }, new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "enchanting_drygmy_shard", Ingredient.of(ItemsRegistry.DRYGMY_SHARD), new ItemStack(ItemsRegistry.DRYGMY_SHARD, 4), 10000,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.EARTH_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE) }, new FluidStack[0]);
            MultiblockImbuementRecipe(5, true, "enchanting_magic_shards", Ingredient.of(ItemsRegistry.WIXIE_SHARD), new ItemStack(ItemsRegistry.WIXIE_SHARD, 4), 10000,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.AIR_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE) }, new FluidStack[0]);

            MultiblockImbuementRecipe(5, true, "enchanting_earth_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.EARTH_ESSENCE), 5000,
                    new Ingredient[] { Ingredient.of(Items.IRON_INGOT), Ingredient.of(ItemTags.DIRT), Ingredient.of(ForgeTags.SEEDS) }, new FluidStack[] { Gnome.getFluid(1000) });
            MultiblockImbuementRecipe(5, true, "enchanting_air_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.AIR_ESSENCE), 5000,
                    new Ingredient[] { Ingredient.of(Items.FEATHER), Ingredient.of(ItemTags.ARROWS), Ingredient.of(ItemsRegistry.WILDEN_WING) }, new FluidStack[] { Sylph.getFluid(1000) });
            MultiblockImbuementRecipe(5, true, "enchanting_water_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.WATER_ESSENCE), 5000,
                    new Ingredient[] { Ingredient.of(Items.KELP), Ingredient.of(Items.SNOW_BLOCK) }, new FluidStack[] { Undine.getFluid(1000), Water.getFluid(1000) });
            MultiblockImbuementRecipe(5, true, "enchanting_fire_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.FIRE_ESSENCE), 5000,
                    new Ingredient[] { Ingredient.of(Items.FLINT_AND_STEEL), Ingredient.of(Items.GUNPOWDER), Ingredient.of(Items.TORCH) }, new FluidStack[] { Salamander.getFluid(1000) });
            MultiblockImbuementRecipe(5, true, "enchanting_manipulation_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.MANIPULATION_ESSENCE), 5000,
                    new Ingredient[] { Ingredient.of(Items.STONE_BUTTON), Ingredient.of(Items.REDSTONE), Ingredient.of(Items.CLOCK) }, new FluidStack[] { Aether.getFluid(1000) });
            MultiblockImbuementRecipe(5, true, "enchanting_abjuration_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.ABJURATION_ESSENCE), 5000,
                    new Ingredient[] { Ingredient.of(Items.SUGAR), Ingredient.of(Items.FERMENTED_SPIDER_EYE) }, new FluidStack[] { Aether.getFluid(1000), Milk.getFluid(1000) });
            MultiblockImbuementRecipe(5, true, "enchanting_conjuration_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.CONJURATION_ESSENCE), 5000,
                    new Ingredient[] { Ingredient.of(Items.BOOK), Ingredient.of(ItemsRegistry.WILDEN_HORN), Ingredient.of(ItemsRegistry.STARBUNCLE_SHARD) }, new FluidStack[] { Aether.getFluid(1000) });

            MultiblockImbuementRecipe(5, true, "wilden_slate", Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), new ItemStack(GTOItems.WILDEN_SLATE, 9), 10000,
                    new Ingredient[] { Ingredient.of(ItemsRegistry.WILDEN_HORN), Ingredient.of(ItemsRegistry.WILDEN_SPIKE), Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(ItemsRegistry.WILDEN_HORN), Ingredient.of(ItemsRegistry.WILDEN_SPIKE),
                            Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(ItemsRegistry.WILDEN_HORN), Ingredient.of(ItemsRegistry.WILDEN_SPIKE), Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(ItemsRegistry.WILDEN_TRIBUTE) },
                    new FluidStack[0]);
        }

        String[] Color = { "white", "orange", "magenta", "light_blue",
                "yellow", "lime", "pink", "gray",
                "light_gray", "cyan", "purple", "blue",
                "brown", "green", "red", "black" };
        for (String string : Color) {
            ImbuementRecipeBuilder.builder("botania_" + string + "_petal")
                    .input(BotaniaItems.fertilizer)
                    .output(RegistriesUtils.getItemStack("botania:" + string + "_petal", 24))
                    .source(500)
                    .addPedestalItem(RegistriesUtils.getItemStack("botania:" + string + "_petal").getItem())
                    .addPedestalItem(RegistriesUtils.getItemStack("botania:" + string + "_mystical_flower").getItem())
                    .addPedestalItem(RegistriesUtils.getItemStack("botania:" + string + "_double_flower").getItem())
                    .addPedestalItem(RegistriesUtils.getItemStack("botania:" + string + "_mushroom").getItem())
                    .save();
            INFUSER_CORE_RECIPES.builder("botania_" + string + "_petal")
                    .inputItems(BotaniaItems.fertilizer, 8)
                    .circuitMeta(1)
                    .notConsumable(RegistriesUtils.getItemStack("botania:" + string + "_petal").getItem())
                    .notConsumable(RegistriesUtils.getItemStack("botania:" + string + "_mystical_flower").getItem())
                    .notConsumable(RegistriesUtils.getItemStack("botania:" + string + "_double_flower").getItem())
                    .notConsumable(RegistriesUtils.getItemStack("botania:" + string + "_mushroom").getItem())
                    .outputItems(RegistriesUtils.getItemStack("botania:" + string + "_petal", 24 * 8))
                    .duration(40)
                    .MANAt(75)
                    .save();
            EnchantingApparatusRecipeBuilder.builder("botania_" + string + "_mystical_flower")
                    .input(RegistriesUtils.getItemStack("botania:" + string + "_mystical_flower").getItem())
                    .output(RegistriesUtils.getItemStack("botania:" + string + "_mystical_flower", 32))
                    .sourceCost(1000)
                    .addPedestalItem(BotaniaItems.fertilizer)
                    .addPedestalItem(BotaniaItems.fertilizer)
                    .addPedestalItem(BotaniaItems.fertilizer)
                    .addPedestalItem(BotaniaItems.fertilizer)
                    .save();
            INFUSER_CORE_RECIPES.builder("botania_" + string + "_mystical_flower")
                    .inputItems(RegistriesUtils.getItemStack("botania:" + string + "_mystical_flower").getItem(), 8)
                    .circuitMeta(5)
                    .inputItems(BotaniaItems.fertilizer, 32)
                    .outputItems(RegistriesUtils.getItemStack("botania:" + string + "_mystical_flower", 32 * 8))
                    .duration(40)
                    .MANAt(150)
                    .save();
        }

        // 工作台
        {

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("imbuement_chamber"), RegistriesUtils.getItemStack("ars_nouveau:imbuement_chamber"),
                    "ABA",
                    "A A",
                    "ABA",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:archwood_planks"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("enchanting_apparatus"), RegistriesUtils.getItemStack("ars_nouveau:enchanting_apparatus"),
                    "ABA",
                    "CDC",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.nugget, GTOMaterials.InfusedGold), 'B', RegistriesUtils.getItemStack("ars_nouveau:sourcestone"), 'C', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'D', new MaterialEntry(TagPrefix.gem, GTOMaterials.ManaDiamond));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("arcane_core"), RegistriesUtils.getItemStack("ars_nouveau:arcane_core"),
                    "AAA",
                    "BCB",
                    "AAA",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:sourcestone"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'C', RegistriesUtils.getItemStack("ars_nouveau:source_gem"));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("blank_thread"), RegistriesUtils.getItemStack("ars_nouveau:blank_thread"),
                    "AAA",
                    "BBB",
                    "AAA",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:magebloom_fiber"), 'B', new MaterialEntry(TagPrefix.nugget, GTOMaterials.InfusedGold));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("apprentice_spell_book"), RegistriesUtils.getItemStack("ars_nouveau:apprentice_spell_book"),
                    "ABC",
                    "BDB",
                    "EBF",
                    'A', new MaterialEntry(TagPrefix.block, GTOMaterials.Livingrock), 'B', new MaterialEntry(TagPrefix.gem, GTOMaterials.ManaDiamond), 'C', RegistriesUtils.getItemStack("botania:livingwood_log"), 'D', RegistriesUtils.getItemStack("ars_nouveau:novice_spell_book"), 'E', new MaterialEntry(TagPrefix.block, GTOMaterials.Livingsteel), 'F', new MaterialEntry(TagPrefix.block, GTOMaterials.Livingclay));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("archmage_spell_book"), RegistriesUtils.getItemStack("ars_nouveau:archmage_spell_book"),
                    "ABA",
                    "CDC",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.ingot, GTOMaterials.Terrasteel), 'B', RegistriesUtils.getItemStack("ars_nouveau:wilden_tribute"), 'C', RegistriesUtils.getItemStack("botania:mana_pearl"), 'D', RegistriesUtils.getItemStack("ars_nouveau:apprentice_spell_book"));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("worn_notebook"), RegistriesUtils.getItemStack("ars_nouveau:worn_notebook"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.gem, GTMaterials.Olivine), 'B', new MaterialEntry(TagPrefix.gem, GTMaterials.Opal), 'C', new ItemStack(Items.BOOK.asItem()));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("dowsing_rod"), RegistriesUtils.getItemStack("ars_nouveau:dowsing_rod"),
                    " A ",
                    "B B",
                    "   ",
                    'A', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'B', RegistriesUtils.getItemStack("ars_nouveau:archwood_planks"));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("wixie_hat"), RegistriesUtils.getItemStack("ars_nouveau:wixie_hat"),
                    "AAA",
                    "ABA",
                    "AAA",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:magebloom_fiber"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("agronomic_sourcelink"), RegistriesUtils.getItemStack("ars_nouveau:agronomic_sourcelink"),
                    " A ",
                    "BCB",
                    " A ",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:source_gem"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'C', new ItemStack(Items.WHEAT.asItem()));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("source_jar"), RegistriesUtils.getItemStack("ars_nouveau:source_jar"),
                    "AAA",
                    "B B",
                    "AAA",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:archwood_slab"), 'B', new MaterialEntry(TagPrefix.block, GTOMaterials.ManaGlass));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("relay"), RegistriesUtils.getItemStack("ars_nouveau:relay"),
                    "A A",
                    "ABA",
                    "A A",
                    'A', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'B', RegistriesUtils.getItemStack("ars_nouveau:source_gem_block"));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("scribes_table"), RegistriesUtils.getItemStack("ars_nouveau:scribes_table"),
                    "AAA",
                    "B B",
                    "C C",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:archwood_slab"), 'B', new MaterialEntry(TagPrefix.nugget, GTOMaterials.InfusedGold), 'C', RegistriesUtils.getItemStack("botania:livingwood_log"));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("volcanic_sourcelink"), RegistriesUtils.getItemStack("ars_nouveau:volcanic_sourcelink"),
                    " A ",
                    "BCB",
                    " A ",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:source_gem"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'C', new ItemStack(Items.LAVA_BUCKET.asItem()));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("alchemical_sourcelink"), RegistriesUtils.getItemStack("ars_nouveau:alchemical_sourcelink"),
                    " A ",
                    "BCB",
                    " A ",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:source_gem"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'C', new ItemStack(Items.BREWING_STAND.asItem()));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("vitalic_sourcelink"), RegistriesUtils.getItemStack("ars_nouveau:vitalic_sourcelink"),
                    " A ",
                    "BCB",
                    " A ",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:source_gem"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'C', new ItemStack(Items.GLISTERING_MELON_SLICE.asItem()));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("mycelial_sourcelink"), RegistriesUtils.getItemStack("ars_nouveau:mycelial_sourcelink"),
                    " A ",
                    "BCB",
                    " A ",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:source_gem"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'C', new ItemStack(Items.MUSHROOM_STEW.asItem()));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("basic_spell_turret"), RegistriesUtils.getItemStack("ars_nouveau:basic_spell_turret"),
                    "AAA",
                    "ACB",
                    "BBB",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:source_gem"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'C', new ItemStack(Items.REDSTONE_BLOCK.asItem()));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("archwood_chest"), RegistriesUtils.getItemStack("ars_nouveau:archwood_chest"),
                    "AAA",
                    "ABA",
                    "AAA",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:archwood_planks"), 'B', new MaterialEntry(TagPrefix.nugget, GTOMaterials.InfusedGold));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("spell_prism"), RegistriesUtils.getItemStack("ars_nouveau:spell_prism"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'B', RegistriesUtils.getItemStack("ars_nouveau:archwood_planks"), 'C', new MaterialEntry(TagPrefix.block, GTMaterials.NetherQuartz));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("mob_jar"), RegistriesUtils.getItemStack("ars_nouveau:mob_jar"),
                    "AAA",
                    "B B",
                    "BBB",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:archwood_slab"), 'B', new MaterialEntry(TagPrefix.block, GTOMaterials.ManaGlass));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("repository"), RegistriesUtils.getItemStack("ars_nouveau:repository"),
                    "ABA",
                    "B B",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.nugget, GTOMaterials.InfusedGold), 'B', RegistriesUtils.getItemStack("botania:livingwood_log"));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("magelight_torch"), RegistriesUtils.getItemStack("ars_nouveau:magelight_torch"),
                    "ABA",
                    " A ",
                    "   ",
                    'A', new MaterialEntry(TagPrefix.nugget, GTOMaterials.InfusedGold), 'B', RegistriesUtils.getItemStack("ars_nouveau:source_gem"));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("arcane_pedestal"), RegistriesUtils.getItemStack("ars_nouveau:arcane_pedestal"),
                    "ABA",
                    "CAC",
                    "CAC",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:sourcestone"), 'B', RegistriesUtils.getItemStack("ars_nouveau:source_gem"), 'C', new MaterialEntry(TagPrefix.nugget, GTOMaterials.InfusedGold));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("ritual_brazier"), RegistriesUtils.getItemStack("ars_nouveau:ritual_brazier"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.block, GTOMaterials.InfusedGold), 'B', RegistriesUtils.getItemStack("ars_nouveau:source_gem_block"), 'C', RegistriesUtils.getItemStack("ars_nouveau:arcane_pedestal"));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("redstone_relay"), RegistriesUtils.getItemStack("ars_nouveau:redstone_relay"),
                    "ABA",
                    "ACA",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'B', new MaterialEntry(TagPrefix.dust, GTMaterials.Redstone), 'C', RegistriesUtils.getItemStack("ars_nouveau:source_gem_block"));

        }
    }

    private static void ImbuementRecipe(
                                        String id,
                                        Ingredient input,
                                        ItemStack output,
                                        int source,
                                        Ingredient[] pedestal) {
        var build = ImbuementRecipeBuilder.builder(id);
        build
                .input(input)
                .output(output)
                .source(source);
        for (Ingredient ingredient : pedestal) build.addPedestalItem(ingredient);
        build.save();
    }

    private static void EnchantingApparatusRecipe(
                                                  String id,
                                                  Ingredient input,
                                                  ItemStack output,
                                                  int source,
                                                  boolean keepNbt,
                                                  Ingredient[] pedestal) {
        var build = EnchantingApparatusRecipeBuilder.builder(id);
        build
                .input(input)
                .output(output)
                .sourceCost(source)
                .keepNbtOfReagent(keepNbt);
        for (Ingredient ingredient : pedestal) build.addPedestalItem(ingredient);
        build.save();
    }

    private static void MultiblockImbuementRecipe(int circuitMeta,
                                                  boolean model,
                                                  String id,
                                                  Ingredient input,
                                                  ItemStack output,
                                                  int source,
                                                  Ingredient[] pedestal,
                                                  FluidStack[] inputFluid) {
        var build = INFUSER_CORE_RECIPES.builder(id);
        if (!model) {
            build
                    .inputItems(input, 4)
                    .outputItems(output.copyWithCount(output.getCount() << 2))
                    .circuitMeta(circuitMeta)
                    .duration(400)
                    .MANAt(source / 25);
            for (Ingredient ingredient : pedestal) build.notConsumable(ingredient);
            for (FluidStack fluidStack : inputFluid) build.inputFluids(fluidStack);
        } else {
            build
                    .inputItems(input)
                    .outputItems(output)
                    .circuitMeta(circuitMeta)
                    .duration(100)
                    .MANAt(source / 25);
            for (Ingredient ingredient : pedestal) build.inputIngredient(ingredient);
            for (FluidStack fluidStack : inputFluid) build.inputFluids(fluidStack);
        }
        build.save();
    }
}
