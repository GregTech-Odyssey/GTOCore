package com.gtocore.data.recipe.misc;

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

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import java.util.List;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.INFUSER_CORE_RECIPES;

public final class ArsNouveauRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        // 灌注室
        record ImbuementRecipe(
                               String id,
                               Ingredient input,
                               ItemStack output,
                               int source,
                               Ingredient[] pedestal) {}

        List<ImbuementRecipe> Imbuement = List.of(
                new ImbuementRecipe("opal_gem", Ingredient.of(GTOChemicalHelper.getItem(gem, Opal)), new ItemStack(ItemsRegistry.SOURCE_GEM), 500, new Ingredient[0]),
                new ImbuementRecipe("olivine_gem", Ingredient.of(GTOChemicalHelper.getItem(gem, Olivine)), new ItemStack(ItemsRegistry.SOURCE_GEM), 500, new Ingredient[0]),
                new ImbuementRecipe("opal_gem_block", Ingredient.of(GTOChemicalHelper.getItem(block, Opal)), new ItemStack(BlockRegistry.SOURCE_GEM_BLOCK.asItem()), 4000, new Ingredient[0]),
                new ImbuementRecipe("olivine_gem_block", Ingredient.of(GTOChemicalHelper.getItem(block, Olivine)), new ItemStack(BlockRegistry.SOURCE_GEM_BLOCK.asItem()), 4000, new Ingredient[0]),

                new ImbuementRecipe("fertilizer_dye", Ingredient.of(Items.BONE_MEAL), new ItemStack(BotaniaItems.fertilizer), 500, new Ingredient[0]),

                new ImbuementRecipe("honey_bottle", Ingredient.of(Items.GLASS_BOTTLE), new ItemStack(Items.HONEY_BOTTLE), 5000,
                        new Ingredient[] { Ingredient.of(Items.BEE_SPAWN_EGG) }),
                new ImbuementRecipe("honey_comb", Ingredient.of(ItemTags.SMALL_FLOWERS), new ItemStack(Items.HONEYCOMB), 5000,
                        new Ingredient[] { Ingredient.of(Items.BEE_SPAWN_EGG) }),

                new ImbuementRecipe("gaia_core", Ingredient.of(RegistriesUtils.getItem("ars_nouveau:mirrorweave")), new ItemStack(GTOItems.GAIA_CORE), 10000,
                        new Ingredient[] { Ingredient.of(BotaniaItems.lifeEssence), Ingredient.of(BotaniaItems.lifeEssence), Ingredient.of(BotaniaItems.lifeEssence), Ingredient.of(BotaniaItems.lifeEssence) }),
                new ImbuementRecipe("bifrost_perm", Ingredient.of(BotaniaBlocks.elfGlass), new ItemStack(BotaniaBlocks.bifrostPerm), 1000,
                        new Ingredient[] { Ingredient.of(RegistriesUtils.getItem("botania:rainbow_rod")) })

        );

        for (ImbuementRecipe recipe : Imbuement) {
            var build = ImbuementRecipeBuilder.builder(recipe.id);
            build
                    .input(recipe.input)
                    .output(recipe.output)
                    .source(recipe.source);
            if (recipe.pedestal.length != 0) for (int i = 0; i < recipe.pedestal.length; i++) build.addPedestalItem(recipe.pedestal[i]);
            build.save(provider);
        }

        // 附魔装置
        record EnchantingApparatusRecipe(
                                         String id,
                                         Ingredient input,
                                         ItemStack output,
                                         int source,
                                         boolean keepNbt,
                                         Ingredient[] pedestal) {}

        List<EnchantingApparatusRecipe> EnchantingApparatus = List.of(
                new EnchantingApparatusRecipe("frozen_pearl", Ingredient.of(Items.ENDER_PEARL), new ItemStack(RegistriesUtils.getItem("torchmaster:frozen_pearl")), 10000, false,
                        new Ingredient[] { Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE),
                                Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE) }),
                new EnchantingApparatusRecipe("runic_altar", Ingredient.of(BotaniaItems.manaPearl), new ItemStack(BotaniaBlocks.runeAltar), 10000, false,
                        new Ingredient[] { Ingredient.of(BotaniaBlocks.livingrock), Ingredient.of(BotaniaBlocks.livingrock), Ingredient.of(BotaniaBlocks.livingrock), Ingredient.of(BotaniaBlocks.livingrock),
                                Ingredient.of(BotaniaBlocks.livingrock), Ingredient.of(BotaniaBlocks.livingrock) }),
                new EnchantingApparatusRecipe("terra_plate", Ingredient.of(BotaniaBlocks.manasteelBlock), new ItemStack(BotaniaBlocks.terraPlate), 10000, false,
                        new Ingredient[] { Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)),
                                Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)) }),
                new EnchantingApparatusRecipe("alfheim_portal", Ingredient.of(BotaniaBlocks.livingwood), new ItemStack(BotaniaBlocks.alfPortal), 10000, false,
                        new Ingredient[] { Ingredient.of(BotaniaBlocks.manasteelBlock), Ingredient.of(BotaniaBlocks.terrasteelBlock), Ingredient.of(GTOChemicalHelper.getItem(block, GTOMaterials.InfusedGold)), Ingredient.of(GTOChemicalHelper.getItem(block, GTOMaterials.Thaumium)),
                                Ingredient.of(BotaniaBlocks.manasteelBlock), Ingredient.of(BotaniaBlocks.terrasteelBlock), Ingredient.of(GTOChemicalHelper.getItem(block, GTOMaterials.InfusedGold)), Ingredient.of(GTOChemicalHelper.getItem(block, GTOMaterials.Thaumium)) }),

                new EnchantingApparatusRecipe("mana_pylon", Ingredient.of(BotaniaItems.manaDiamond), new ItemStack(BotaniaBlocks.manaPylon), 10000, false,
                        new Ingredient[] { Ingredient.of(BotaniaItems.manaSteel), Ingredient.of(BotaniaItems.manaSteel), Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.GOLD_INGOT),
                                Ingredient.of(BotaniaItems.manaSteel), Ingredient.of(BotaniaItems.manaSteel), Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.GOLD_INGOT) }),
                new EnchantingApparatusRecipe("natura_pylon", Ingredient.of(BotaniaBlocks.manaPylon), new ItemStack(BotaniaBlocks.naturaPylon), 10000, false,
                        new Ingredient[] { Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaBlocks.forestEye),
                                Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaBlocks.forestEye) }),
                new EnchantingApparatusRecipe("alfsteel_pylon", Ingredient.of(BotaniaBlocks.naturaPylon), RegistriesUtils.getItemStack("mythicbotany:alfsteel_pylon"), 10000, false,
                        new Ingredient[] { Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(BotaniaBlocks.lightRelayDefault), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Herbs)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_ingot")),
                                Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(BotaniaBlocks.lightRelayDefault), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Herbs)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_ingot")),
                                Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(BotaniaBlocks.lightRelayDefault), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Herbs)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_ingot")),
                                Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(BotaniaBlocks.lightRelayDefault), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Herbs)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_ingot")) }),
                new EnchantingApparatusRecipe("gaia_pylon", Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_pylon")), new ItemStack(BotaniaBlocks.gaiaPylon.asItem()), 10000, false,
                        new Ingredient[] { Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Laureril)), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Gaiasteel)), Ingredient.of(BotaniaBlocks.shimmerrock), Ingredient.of(BotaniaBlocks.dreamwoodGlimmering),
                                Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Laureril)), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Gaiasteel)), Ingredient.of(BotaniaBlocks.shimmerrock), Ingredient.of(BotaniaBlocks.dreamwoodGlimmering),
                                Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Laureril)), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Gaiasteel)), Ingredient.of(BotaniaBlocks.shimmerrock), Ingredient.of(BotaniaBlocks.dreamwoodGlimmering),
                                Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Laureril)), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Gaiasteel)), Ingredient.of(BotaniaBlocks.shimmerrock), Ingredient.of(BotaniaBlocks.dreamwoodGlimmering) }),

                new EnchantingApparatusRecipe("enchanting_laureril_ingot", Ingredient.of(GTOChemicalHelper.getItem(plate, Runerock)), new ItemStack(GTOChemicalHelper.getItem(ingot, Laureril)), 10000, false,
                        new Ingredient[] { Ingredient.of(GTOChemicalHelper.getItem(ingot, Thaumium)), Ingredient.of(GTOChemicalHelper.getItem(ingot, WhiteWax)), Ingredient.of(GTOChemicalHelper.getItem(ingot, InfusedGold)), Ingredient.of(GTOChemicalHelper.getItem(ingot, Herbs)) }),

                new EnchantingApparatusRecipe("enchanting_starbuncle_shards", Ingredient.of(ItemsRegistry.STARBUNCLE_SHARD), new ItemStack(ItemsRegistry.STARBUNCLE_SHARD, 4), 10000, false,
                        new Ingredient[] { Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.EARTH_ESSENCE), Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.FIRE_ESSENCE) }),
                new EnchantingApparatusRecipe("enchanting_whirlisprig_shards", Ingredient.of(ItemsRegistry.WHIRLISPRIG_SHARDS), new ItemStack(ItemsRegistry.WHIRLISPRIG_SHARDS, 4), 10000, false,
                        new Ingredient[] { Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.AIR_ESSENCE), Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.WATER_ESSENCE) }),
                new EnchantingApparatusRecipe("enchanting_drygmy_shard", Ingredient.of(ItemsRegistry.DRYGMY_SHARD), new ItemStack(ItemsRegistry.DRYGMY_SHARD, 4), 10000, false,
                        new Ingredient[] { Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.EARTH_ESSENCE), Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE) }),
                new EnchantingApparatusRecipe("enchanting_magic_shards", Ingredient.of(ItemsRegistry.WIXIE_SHARD), new ItemStack(ItemsRegistry.WIXIE_SHARD, 4), 10000, false,
                        new Ingredient[] { Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.AIR_ESSENCE), Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE) }),

                new EnchantingApparatusRecipe("enchanting_earth_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.EARTH_ESSENCE), 5000, false,
                        new Ingredient[] { Ingredient.of(Items.IRON_INGOT), Ingredient.of(ItemTags.DIRT), Ingredient.of(ForgeTags.SEEDS), Ingredient.of(RegistriesUtils.getItemStack("gtocore:gnome_bucket")) }),
                new EnchantingApparatusRecipe("enchanting_air_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.AIR_ESSENCE), 5000, false,
                        new Ingredient[] { Ingredient.of(Items.FEATHER), Ingredient.of(ItemTags.ARROWS), Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(RegistriesUtils.getItemStack("gtocore:sylph_bucket")) }),
                new EnchantingApparatusRecipe("enchanting_water_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.WATER_ESSENCE), 5000, false,
                        new Ingredient[] { Ingredient.of(Items.KELP), Ingredient.of(Items.SNOW_BLOCK), Ingredient.of(Items.WATER_BUCKET), Ingredient.of(RegistriesUtils.getItemStack("gtocore:undine_bucket")) }),
                new EnchantingApparatusRecipe("enchanting_fire_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.FIRE_ESSENCE), 5000, false,
                        new Ingredient[] { Ingredient.of(Items.FLINT_AND_STEEL), Ingredient.of(Items.GUNPOWDER), Ingredient.of(Items.TORCH), Ingredient.of(RegistriesUtils.getItemStack("gtocore:salamander_bucket")) }),
                new EnchantingApparatusRecipe("enchanting_manipulation_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.MANIPULATION_ESSENCE), 5000, false,
                        new Ingredient[] { Ingredient.of(Items.STONE_BUTTON), Ingredient.of(Items.REDSTONE), Ingredient.of(Items.CLOCK), Ingredient.of(RegistriesUtils.getItemStack("gtocore:aether_bucket")) }),
                new EnchantingApparatusRecipe("enchanting_abjuration_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.ABJURATION_ESSENCE), 5000, false,
                        new Ingredient[] { Ingredient.of(Items.SUGAR), Ingredient.of(Items.FERMENTED_SPIDER_EYE), Ingredient.of(Items.MILK_BUCKET), Ingredient.of(RegistriesUtils.getItemStack("gtocore:aether_bucket")) }),
                new EnchantingApparatusRecipe("enchanting_conjuration_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.CONJURATION_ESSENCE), 5000, false,
                        new Ingredient[] { Ingredient.of(Items.BOOK), Ingredient.of(ItemsRegistry.WILDEN_HORN), Ingredient.of(ItemsRegistry.STARBUNCLE_SHARD), Ingredient.of(RegistriesUtils.getItemStack("gtocore:aether_bucket")) }),

                new EnchantingApparatusRecipe("wilden_slate", Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), new ItemStack(GTOItems.WILDEN_SLATE, 9), 10000, false,
                        new Ingredient[] { Ingredient.of(ItemsRegistry.WILDEN_HORN), Ingredient.of(ItemsRegistry.WILDEN_SPIKE), Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(ItemsRegistry.WILDEN_HORN), Ingredient.of(ItemsRegistry.WILDEN_SPIKE),
                                Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(ItemsRegistry.WILDEN_HORN), Ingredient.of(ItemsRegistry.WILDEN_SPIKE), Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(ItemsRegistry.WILDEN_TRIBUTE) })

        );

        for (EnchantingApparatusRecipe recipe : EnchantingApparatus) {
            var build = EnchantingApparatusRecipeBuilder.builder(recipe.id);
            build
                    .input(recipe.input)
                    .output(recipe.output)
                    .sourceCost(recipe.source)
                    .keepNbtOfReagent(recipe.keepNbt);
            for (int i = 0; i < recipe.pedestal.length; i++) build.addPedestalItem(recipe.pedestal[i]);
            build.save(provider);
        }

        // 多方块灌注室 + 附魔核心
        record MultiblockImbuementRecipe(
                                         int circuitMeta,
                                         boolean model,
                                         String id,
                                         Ingredient input,
                                         ItemStack output,
                                         int source,
                                         Ingredient[] pedestal,
                                         FluidStack[] inputFluid) {}

        List<MultiblockImbuementRecipe> MultiblockImbuement = List.of(
                // GTO灌注室
                new MultiblockImbuementRecipe(1, false, "opal_gem", Ingredient.of(GTOChemicalHelper.getItem(gem, Opal)), new ItemStack(ItemsRegistry.SOURCE_GEM), 500, new Ingredient[0], new FluidStack[0]),
                new MultiblockImbuementRecipe(1, false, "olivine_gem", Ingredient.of(GTOChemicalHelper.getItem(gem, Olivine)), new ItemStack(ItemsRegistry.SOURCE_GEM), 500, new Ingredient[0], new FluidStack[0]),
                new MultiblockImbuementRecipe(1, false, "opal_gem_block", Ingredient.of(GTOChemicalHelper.getItem(block, Opal)), new ItemStack(BlockRegistry.SOURCE_GEM_BLOCK.asItem()), 4000, new Ingredient[0], new FluidStack[0]),
                new MultiblockImbuementRecipe(1, false, "olivine_gem_block", Ingredient.of(GTOChemicalHelper.getItem(block, Olivine)), new ItemStack(BlockRegistry.SOURCE_GEM_BLOCK.asItem()), 4000, new Ingredient[0], new FluidStack[0]),

                new MultiblockImbuementRecipe(1, false, "fertilizer_dye", Ingredient.of(Items.BONE_MEAL), new ItemStack(BotaniaItems.fertilizer), 500, new Ingredient[0], new FluidStack[0]),

                new MultiblockImbuementRecipe(1, false, "honey_bottle", Ingredient.of(Items.GLASS_BOTTLE), new ItemStack(Items.HONEY_BOTTLE), 5000,
                        new Ingredient[] { Ingredient.of(Items.BEE_SPAWN_EGG) }, new FluidStack[0]),
                new MultiblockImbuementRecipe(1, false, "honey_comb", Ingredient.of(ItemTags.SMALL_FLOWERS), new ItemStack(Items.HONEYCOMB), 5000,
                        new Ingredient[] { Ingredient.of(Items.BEE_SPAWN_EGG) }, new FluidStack[0]),

                new MultiblockImbuementRecipe(1, false, "gaia_core", Ingredient.of(RegistriesUtils.getItem("ars_nouveau:mirrorweave")), new ItemStack(GTOItems.GAIA_CORE), 10000,
                        new Ingredient[] { Ingredient.of(BotaniaItems.lifeEssence), Ingredient.of(BotaniaItems.lifeEssence), Ingredient.of(BotaniaItems.lifeEssence), Ingredient.of(BotaniaItems.lifeEssence) }, new FluidStack[0]),
                new MultiblockImbuementRecipe(1, false, "bifrost_perm", Ingredient.of(BotaniaBlocks.elfGlass), new ItemStack(BotaniaBlocks.bifrostPerm), 1000,
                        new Ingredient[] { Ingredient.of(RegistriesUtils.getItem("botania:rainbow_rod")) }, new FluidStack[0]),

                // GTO附魔核心
                new MultiblockImbuementRecipe(5, true, "frozen_pearl", Ingredient.of(Items.ENDER_PEARL), new ItemStack(RegistriesUtils.getItem("torchmaster:frozen_pearl")), 10000,
                        new Ingredient[] { Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE),
                                Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE), Ingredient.of(Items.BLUE_ICE) },
                        new FluidStack[0]),
                new MultiblockImbuementRecipe(5, true, "runic_altar", Ingredient.of(BotaniaItems.manaPearl), new ItemStack(BotaniaBlocks.runeAltar), 10000,
                        new Ingredient[] { Ingredient.of(BotaniaBlocks.livingrock), Ingredient.of(BotaniaBlocks.livingrock), Ingredient.of(BotaniaBlocks.livingrock), Ingredient.of(BotaniaBlocks.livingrock),
                                Ingredient.of(BotaniaBlocks.livingrock), Ingredient.of(BotaniaBlocks.livingrock) },
                        new FluidStack[0]),
                new MultiblockImbuementRecipe(5, true, "terra_plate", Ingredient.of(BotaniaBlocks.manasteelBlock), new ItemStack(BotaniaBlocks.terraPlate), 10000,
                        new Ingredient[] { Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)),
                                Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)) },
                        new FluidStack[0]),
                new MultiblockImbuementRecipe(5, true, "alfheim_portal", Ingredient.of(BotaniaBlocks.livingwood), new ItemStack(BotaniaBlocks.alfPortal), 10000,
                        new Ingredient[] { Ingredient.of(BotaniaBlocks.manasteelBlock), Ingredient.of(BotaniaBlocks.terrasteelBlock), Ingredient.of(GTOChemicalHelper.getItem(block, GTOMaterials.InfusedGold)), Ingredient.of(GTOChemicalHelper.getItem(block, GTOMaterials.Thaumium)),
                                Ingredient.of(BotaniaBlocks.manasteelBlock), Ingredient.of(BotaniaBlocks.terrasteelBlock), Ingredient.of(GTOChemicalHelper.getItem(block, GTOMaterials.InfusedGold)), Ingredient.of(GTOChemicalHelper.getItem(block, GTOMaterials.Thaumium)) },
                        new FluidStack[0]),

                new MultiblockImbuementRecipe(5, true, "mana_pylon", Ingredient.of(BotaniaItems.manaDiamond), new ItemStack(BotaniaBlocks.manaPylon), 10000,
                        new Ingredient[] { Ingredient.of(BotaniaItems.manaSteel), Ingredient.of(BotaniaItems.manaSteel), Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.GOLD_INGOT),
                                Ingredient.of(BotaniaItems.manaSteel), Ingredient.of(BotaniaItems.manaSteel), Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.GOLD_INGOT) },
                        new FluidStack[0]),
                new MultiblockImbuementRecipe(5, true, "natura_pylon", Ingredient.of(BotaniaBlocks.manaPylon), new ItemStack(BotaniaBlocks.naturaPylon), 10000,
                        new Ingredient[] { Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaBlocks.forestEye),
                                Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaItems.terrasteel), Ingredient.of(BotaniaBlocks.forestEye) },
                        new FluidStack[0]),
                new MultiblockImbuementRecipe(5, true, "alfsteel_pylon", Ingredient.of(BotaniaBlocks.naturaPylon), RegistriesUtils.getItemStack("mythicbotany:alfsteel_pylon"), 10000,
                        new Ingredient[] { Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(BotaniaBlocks.lightRelayDefault), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Herbs)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_ingot")),
                                Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(BotaniaBlocks.lightRelayDefault), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Herbs)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_ingot")),
                                Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(BotaniaBlocks.lightRelayDefault), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Herbs)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_ingot")),
                                Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE), Ingredient.of(BotaniaBlocks.lightRelayDefault), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Herbs)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_ingot")) },
                        new FluidStack[0]),
                new MultiblockImbuementRecipe(5, true, "gaia_pylon", Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfsteel_pylon")), new ItemStack(BotaniaBlocks.gaiaPylon.asItem()), 10000,
                        new Ingredient[] { Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Laureril)), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Gaiasteel)), Ingredient.of(BotaniaBlocks.shimmerrock), Ingredient.of(BotaniaBlocks.dreamwoodGlimmering),
                                Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Laureril)), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Gaiasteel)), Ingredient.of(BotaniaBlocks.shimmerrock), Ingredient.of(BotaniaBlocks.dreamwoodGlimmering),
                                Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Laureril)), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Gaiasteel)), Ingredient.of(BotaniaBlocks.shimmerrock), Ingredient.of(BotaniaBlocks.dreamwoodGlimmering),
                                Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Laureril)), Ingredient.of(GTOChemicalHelper.getItem(ingot, GTOMaterials.Gaiasteel)), Ingredient.of(BotaniaBlocks.shimmerrock), Ingredient.of(BotaniaBlocks.dreamwoodGlimmering) },
                        new FluidStack[0]),

                new MultiblockImbuementRecipe(5, true, "enchanting_laureril_ingot", Ingredient.of(GTOChemicalHelper.getItem(plate, Runerock)), new ItemStack(GTOChemicalHelper.getItem(ingot, Laureril)), 10000,
                        new Ingredient[] { Ingredient.of(GTOChemicalHelper.getItem(ingot, Thaumium)), Ingredient.of(GTOChemicalHelper.getItem(ingot, WhiteWax)), Ingredient.of(GTOChemicalHelper.getItem(ingot, InfusedGold)), Ingredient.of(GTOChemicalHelper.getItem(ingot, Herbs)) }, new FluidStack[0]),

                new MultiblockImbuementRecipe(5, true, "enchanting_starbuncle_shards", Ingredient.of(ItemsRegistry.STARBUNCLE_SHARD), new ItemStack(ItemsRegistry.STARBUNCLE_SHARD, 4), 10000,
                        new Ingredient[] { Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.EARTH_ESSENCE), Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.FIRE_ESSENCE) }, new FluidStack[0]),
                new MultiblockImbuementRecipe(5, true, "enchanting_whirlisprig_shards", Ingredient.of(ItemsRegistry.WHIRLISPRIG_SHARDS), new ItemStack(ItemsRegistry.WHIRLISPRIG_SHARDS, 4), 10000,
                        new Ingredient[] { Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.AIR_ESSENCE), Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.WATER_ESSENCE) }, new FluidStack[0]),
                new MultiblockImbuementRecipe(5, true, "enchanting_drygmy_shard", Ingredient.of(ItemsRegistry.DRYGMY_SHARD), new ItemStack(ItemsRegistry.DRYGMY_SHARD, 4), 10000,
                        new Ingredient[] { Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.EARTH_ESSENCE), Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE) }, new FluidStack[0]),
                new MultiblockImbuementRecipe(5, true, "enchanting_magic_shards", Ingredient.of(ItemsRegistry.WIXIE_SHARD), new ItemStack(ItemsRegistry.WIXIE_SHARD, 4), 10000,
                        new Ingredient[] { Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.AIR_ESSENCE), Ingredient.of(ItemsRegistry.CONJURATION_ESSENCE), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE) }, new FluidStack[0]),

                new MultiblockImbuementRecipe(5, true, "enchanting_earth_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.EARTH_ESSENCE), 5000,
                        new Ingredient[] { Ingredient.of(Items.IRON_INGOT), Ingredient.of(ItemTags.DIRT), Ingredient.of(ForgeTags.SEEDS) }, new FluidStack[] { Gnome.getFluid(1000) }),
                new MultiblockImbuementRecipe(5, true, "enchanting_air_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.AIR_ESSENCE), 5000,
                        new Ingredient[] { Ingredient.of(Items.FEATHER), Ingredient.of(ItemTags.ARROWS), Ingredient.of(ItemsRegistry.WILDEN_WING) }, new FluidStack[] { Sylph.getFluid(1000) }),
                new MultiblockImbuementRecipe(5, true, "enchanting_water_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.WATER_ESSENCE), 5000,
                        new Ingredient[] { Ingredient.of(Items.KELP), Ingredient.of(Items.SNOW_BLOCK) }, new FluidStack[] { Undine.getFluid(1000), Water.getFluid(1000) }),
                new MultiblockImbuementRecipe(5, true, "enchanting_fire_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.FIRE_ESSENCE), 5000,
                        new Ingredient[] { Ingredient.of(Items.FLINT_AND_STEEL), Ingredient.of(Items.GUNPOWDER), Ingredient.of(Items.TORCH) }, new FluidStack[] { Salamander.getFluid(1000) }),
                new MultiblockImbuementRecipe(5, true, "enchanting_manipulation_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.MANIPULATION_ESSENCE), 5000,
                        new Ingredient[] { Ingredient.of(Items.STONE_BUTTON), Ingredient.of(Items.REDSTONE), Ingredient.of(Items.CLOCK) }, new FluidStack[] { Aether.getFluid(1000) }),
                new MultiblockImbuementRecipe(5, true, "enchanting_abjuration_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.ABJURATION_ESSENCE), 5000,
                        new Ingredient[] { Ingredient.of(Items.SUGAR), Ingredient.of(Items.FERMENTED_SPIDER_EYE) }, new FluidStack[] { Aether.getFluid(1000), Milk.getFluid(1000) }),
                new MultiblockImbuementRecipe(5, true, "enchanting_conjuration_essence", Ingredient.of(ItemsRegistry.SOURCE_GEM), new ItemStack(ItemsRegistry.CONJURATION_ESSENCE), 5000,
                        new Ingredient[] { Ingredient.of(Items.BOOK), Ingredient.of(ItemsRegistry.WILDEN_HORN), Ingredient.of(ItemsRegistry.STARBUNCLE_SHARD) }, new FluidStack[] { Aether.getFluid(1000) }),

                new MultiblockImbuementRecipe(5, true, "wilden_slate", Ingredient.of(GTOChemicalHelper.getItem(block, Runerock)), new ItemStack(GTOItems.WILDEN_SLATE, 9), 10000,
                        new Ingredient[] { Ingredient.of(ItemsRegistry.WILDEN_HORN), Ingredient.of(ItemsRegistry.WILDEN_SPIKE), Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(ItemsRegistry.WILDEN_HORN), Ingredient.of(ItemsRegistry.WILDEN_SPIKE),
                                Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(ItemsRegistry.WILDEN_HORN), Ingredient.of(ItemsRegistry.WILDEN_SPIKE), Ingredient.of(ItemsRegistry.WILDEN_WING), Ingredient.of(ItemsRegistry.WILDEN_TRIBUTE) },
                        new FluidStack[0])

        );

        for (MultiblockImbuementRecipe recipe : MultiblockImbuement) {
            var build = INFUSER_CORE_RECIPES.builder(recipe.id);
            if (!recipe.model) {
                build
                        .inputItems(recipe.input, 8)
                        .outputItems(recipe.output.copyWithCount(recipe.output.getCount() << 3))
                        .circuitMeta(recipe.circuitMeta)
                        .duration(400)
                        .MANAt(recipe.source / 50);
                for (int i = 0; i < recipe.pedestal.length; i++) build.notConsumable(recipe.pedestal[i]);
                for (int i = 0; i < recipe.inputFluid.length; i++) build.inputFluids(recipe.inputFluid[i]);
            } else {
                build
                        .inputItems(recipe.input)
                        .outputItems(recipe.output)
                        .circuitMeta(recipe.circuitMeta)
                        .duration(50)
                        .MANAt(recipe.source / 50);
                for (int i = 0; i < recipe.pedestal.length; i++) build.inputItems(recipe.pedestal[i]);
                for (int i = 0; i < recipe.inputFluid.length; i++) build.inputFluids(recipe.inputFluid[i]);
            }
            build.save(provider);
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
                    .save(provider);
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
                    .save(provider);
            EnchantingApparatusRecipeBuilder.builder("botania_" + string + "_mystical_flower")
                    .input(RegistriesUtils.getItemStack("botania:" + string + "_mystical_flower").getItem())
                    .output(RegistriesUtils.getItemStack("botania:" + string + "_mystical_flower", 32))
                    .sourceCost(1000)
                    .addPedestalItem(BotaniaItems.fertilizer)
                    .addPedestalItem(BotaniaItems.fertilizer)
                    .addPedestalItem(BotaniaItems.fertilizer)
                    .addPedestalItem(BotaniaItems.fertilizer)
                    .save(provider);
            INFUSER_CORE_RECIPES.builder("botania_" + string + "_mystical_flower")
                    .inputItems(RegistriesUtils.getItemStack("botania:" + string + "_mystical_flower").getItem(), 8)
                    .circuitMeta(5)
                    .inputItems(BotaniaItems.fertilizer, 32)
                    .outputItems(RegistriesUtils.getItemStack("botania:" + string + "_mystical_flower", 32 * 8))
                    .duration(40)
                    .MANAt(150)
                    .save(provider);
        }

        // 工作台
        {

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("imbuement_chamber"), RegistriesUtils.getItemStack("ars_nouveau:imbuement_chamber"),
                    "ABA",
                    "A A",
                    "ABA",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:archwood_planks"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("enchanting_apparatus"), RegistriesUtils.getItemStack("ars_nouveau:enchanting_apparatus"),
                    "ABA",
                    "CDC",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.nugget, GTOMaterials.InfusedGold), 'B', RegistriesUtils.getItemStack("ars_nouveau:sourcestone"), 'C', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'D', new MaterialEntry(TagPrefix.gem, GTOMaterials.ManaDiamond));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("arcane_core"), RegistriesUtils.getItemStack("ars_nouveau:arcane_core"),
                    "AAA",
                    "BCB",
                    "AAA",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:sourcestone"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'C', RegistriesUtils.getItemStack("ars_nouveau:source_gem"));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("blank_thread"), RegistriesUtils.getItemStack("ars_nouveau:blank_thread"),
                    "AAA",
                    "BBB",
                    "AAA",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:magebloom_fiber"), 'B', new MaterialEntry(TagPrefix.nugget, GTOMaterials.InfusedGold));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("apprentice_spell_book"), RegistriesUtils.getItemStack("ars_nouveau:apprentice_spell_book"),
                    "ABC",
                    "BDB",
                    "EBF",
                    'A', new MaterialEntry(TagPrefix.block, GTOMaterials.Livingrock), 'B', new MaterialEntry(TagPrefix.gem, GTOMaterials.ManaDiamond), 'C', RegistriesUtils.getItemStack("botania:livingwood_log"), 'D', RegistriesUtils.getItemStack("ars_nouveau:novice_spell_book"), 'E', new MaterialEntry(TagPrefix.block, GTOMaterials.Livingsteel), 'F', new MaterialEntry(TagPrefix.block, GTOMaterials.Livingclay));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("archmage_spell_book"), RegistriesUtils.getItemStack("ars_nouveau:archmage_spell_book"),
                    "ABA",
                    "CDC",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.ingot, GTOMaterials.Terrasteel), 'B', RegistriesUtils.getItemStack("ars_nouveau:wilden_tribute"), 'C', RegistriesUtils.getItemStack("botania:mana_pearl"), 'D', RegistriesUtils.getItemStack("ars_nouveau:apprentice_spell_book"));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("worn_notebook"), RegistriesUtils.getItemStack("ars_nouveau:worn_notebook"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.gem, GTMaterials.Olivine), 'B', new MaterialEntry(TagPrefix.gem, GTMaterials.Opal), 'C', new ItemStack(Items.BOOK.asItem()));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("dowsing_rod"), RegistriesUtils.getItemStack("ars_nouveau:dowsing_rod"),
                    " A ",
                    "B B",
                    "   ",
                    'A', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'B', RegistriesUtils.getItemStack("ars_nouveau:archwood_planks"));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("wixie_hat"), RegistriesUtils.getItemStack("ars_nouveau:wixie_hat"),
                    "AAA",
                    "ABA",
                    "AAA",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:magebloom_fiber"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("agronomic_sourcelink"), RegistriesUtils.getItemStack("ars_nouveau:agronomic_sourcelink"),
                    " A ",
                    "BCB",
                    " A ",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:source_gem"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'C', new ItemStack(Items.WHEAT.asItem()));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("source_jar"), RegistriesUtils.getItemStack("ars_nouveau:source_jar"),
                    "AAA",
                    "B B",
                    "AAA",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:archwood_slab"), 'B', new MaterialEntry(TagPrefix.block, GTOMaterials.ManaGlass));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("relay"), RegistriesUtils.getItemStack("ars_nouveau:relay"),
                    "A A",
                    "ABA",
                    "A A",
                    'A', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'B', RegistriesUtils.getItemStack("ars_nouveau:source_gem_block"));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("scribes_table"), RegistriesUtils.getItemStack("ars_nouveau:scribes_table"),
                    "AAA",
                    "B B",
                    "C C",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:archwood_slab"), 'B', new MaterialEntry(TagPrefix.nugget, GTOMaterials.InfusedGold), 'C', RegistriesUtils.getItemStack("botania:livingwood_log"));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("volcanic_sourcelink"), RegistriesUtils.getItemStack("ars_nouveau:volcanic_sourcelink"),
                    " A ",
                    "BCB",
                    " A ",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:source_gem"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'C', new ItemStack(Items.LAVA_BUCKET.asItem()));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("alchemical_sourcelink"), RegistriesUtils.getItemStack("ars_nouveau:alchemical_sourcelink"),
                    " A ",
                    "BCB",
                    " A ",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:source_gem"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'C', new ItemStack(Items.BREWING_STAND.asItem()));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("vitalic_sourcelink"), RegistriesUtils.getItemStack("ars_nouveau:vitalic_sourcelink"),
                    " A ",
                    "BCB",
                    " A ",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:source_gem"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'C', new ItemStack(Items.GLISTERING_MELON_SLICE.asItem()));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("mycelial_sourcelink"), RegistriesUtils.getItemStack("ars_nouveau:mycelial_sourcelink"),
                    " A ",
                    "BCB",
                    " A ",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:source_gem"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'C', new ItemStack(Items.MUSHROOM_STEW.asItem()));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("basic_spell_turret"), RegistriesUtils.getItemStack("ars_nouveau:basic_spell_turret"),
                    "AAA",
                    "ACB",
                    "BBB",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:source_gem"), 'B', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'C', new ItemStack(Items.REDSTONE_BLOCK.asItem()));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("archwood_chest"), RegistriesUtils.getItemStack("ars_nouveau:archwood_chest"),
                    "AAA",
                    "ABA",
                    "AAA",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:archwood_planks"), 'B', new MaterialEntry(TagPrefix.nugget, GTOMaterials.InfusedGold));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("spell_prism"), RegistriesUtils.getItemStack("ars_nouveau:spell_prism"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'B', RegistriesUtils.getItemStack("ars_nouveau:archwood_planks"), 'C', new MaterialEntry(TagPrefix.block, GTMaterials.NetherQuartz));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("mob_jar"), RegistriesUtils.getItemStack("ars_nouveau:mob_jar"),
                    "AAA",
                    "B B",
                    "BBB",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:archwood_slab"), 'B', new MaterialEntry(TagPrefix.block, GTOMaterials.ManaGlass));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("repository"), RegistriesUtils.getItemStack("ars_nouveau:repository"),
                    "ABA",
                    "B B",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.nugget, GTOMaterials.InfusedGold), 'B', RegistriesUtils.getItemStack("botania:livingwood_log"));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("magelight_torch"), RegistriesUtils.getItemStack("ars_nouveau:magelight_torch"),
                    "ABA",
                    " A ",
                    "   ",
                    'A', new MaterialEntry(TagPrefix.nugget, GTOMaterials.InfusedGold), 'B', RegistriesUtils.getItemStack("ars_nouveau:source_gem"));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("arcane_pedestal"), RegistriesUtils.getItemStack("ars_nouveau:arcane_pedestal"),
                    "ABA",
                    "CAC",
                    "CAC",
                    'A', RegistriesUtils.getItemStack("ars_nouveau:sourcestone"), 'B', RegistriesUtils.getItemStack("ars_nouveau:source_gem"), 'C', new MaterialEntry(TagPrefix.nugget, GTOMaterials.InfusedGold));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("ritual_brazier"), RegistriesUtils.getItemStack("ars_nouveau:ritual_brazier"),
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.block, GTOMaterials.InfusedGold), 'B', RegistriesUtils.getItemStack("ars_nouveau:source_gem_block"), 'C', RegistriesUtils.getItemStack("ars_nouveau:arcane_pedestal"));

            VanillaRecipeHelper.addShapedRecipe(provider, GTOCore.id("redstone_relay"), RegistriesUtils.getItemStack("ars_nouveau:redstone_relay"),
                    "ABA",
                    "ACA",
                    "ABA",
                    'A', new MaterialEntry(TagPrefix.ingot, GTOMaterials.InfusedGold), 'B', new MaterialEntry(TagPrefix.dust, GTMaterials.Redstone), 'C', RegistriesUtils.getItemStack("ars_nouveau:source_gem_block"));

        }
    }
}
