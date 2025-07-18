package com.gtocore.data.recipe.misc;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.data.recipe.builder.botania.*;
import com.gtocore.data.tag.Tags;

import com.gtolib.api.data.chemical.GTOChemicalHelper;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;

import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import com.kyanite.deeperdarker.content.DDBlocks;
import earth.terrarium.adastra.common.registry.ModBlocks;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.brew.BotaniaBrews;
import vazkii.botania.common.crafting.StateIngredientHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import java.util.List;
import java.util.function.Consumer;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.api.data.tag.GTOTagPrefix.SUPERCONDUCTOR_BASE;
import static com.gtocore.common.data.GTOItems.COLORFUL_MYSTICAL_FLOWER;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;

public final class BotaniaRecipes {

    public static void init(Consumer<FinishedRecipe> provider) {
        // 植物魔法系列测试配方
        if (GTCEu.isDev()) {
            // 植物酿造 - 好像只能酿造植物魔法的那些
            BrewRecipeBuilder.builder("iron100")
                    .brew(BotaniaBrews.clear)
                    .addIngredient(ItemTags.SMALL_FLOWERS)
                    .addIngredient(Items.DIAMOND_BLOCK)
                    .addIngredient(BotaniaTags.Items.RUNES)
                    .save(provider);
        }

        // 白雏菊
        record PureDaisyRecipe(
                               String id,
                               StateIngredient input,
                               Block output) {}
        List<PureDaisyRecipe> PureDaisy = List.of(
                new PureDaisyRecipe("livingclay", StateIngredientHelper.of(BlockTags.DIRT), ChemicalHelper.getBlock(block, Livingclay)),
                new PureDaisyRecipe("livingwood", StateIngredientHelper.of(Tags.ARCHWOOD_LOG), BotaniaBlocks.livingwoodLog));
        for (PureDaisyRecipe recipe : PureDaisy) {
            PureDaisyRecipeBuilder.builder(recipe.id)
                    .input(recipe.input)
                    .output(recipe.output)
                    .save(provider);
        }

        // 魔力池
        record ManaInfusionRecipe(
                                  String id,
                                  Ingredient input,
                                  ItemStack output,
                                  int mana,
                                  Block customCatalyst,
                                  String group) {}
        List<ManaInfusionRecipe> Infusion = List.of(
                new ManaInfusionRecipe("pulsating", Ingredient.of(GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, PulsatingAlloy)), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, PulsatingAlloy)), 400, null, null),
                new ManaInfusionRecipe("conductivee", Ingredient.of(GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, ConductiveAlloy)), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, ConductiveAlloy)), 1600, null, null),
                new ManaInfusionRecipe("energeticalloy", Ingredient.of(GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, EnergeticAlloy)), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, EnergeticAlloy)), 6000, null, null),
                new ManaInfusionRecipe("vibrantalloy", Ingredient.of(GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, VibrantAlloy)), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, VibrantAlloy)), 25600, null, null),
                new ManaInfusionRecipe("endsteel", Ingredient.of(GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, EndSteel)), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, EndSteel)), 102400, null, null),

                new ManaInfusionRecipe("manasteel", Ingredient.of(GTOChemicalHelper.getItem(ingot, Steel)), new ItemStack(GTOChemicalHelper.getItem(ingot, Manasteel)), 3000, null, null),
                new ManaInfusionRecipe("manasteel_block", Ingredient.of(GTOChemicalHelper.getItem(block, Steel)), new ItemStack(GTOChemicalHelper.getItem(block, Manasteel)), 27000, null, null),
                new ManaInfusionRecipe("mana_pearl", Ingredient.of(RegistriesUtils.getItem("torchmaster:frozen_pearl")), new ItemStack(BotaniaItems.manaPearl), 6000, null, null),
                new ManaInfusionRecipe("mana_string", Ingredient.of(ItemsRegistry.MAGE_FIBER.asItem()), new ItemStack(BotaniaItems.manaString), 1250, null, null),

                new ManaInfusionRecipe("life_essence", Ingredient.of(GTOItems.UNSTABLE_GAIA_SOUL.asItem()), new ItemStack(BotaniaItems.lifeEssence), 500000, ChemicalHelper.getBlock(block, Gaia), null),

                new ManaInfusionRecipe("infused_gold", Ingredient.of(Items.GOLD_INGOT), new ItemStack(GTOChemicalHelper.getItem(ingot, InfusedGold)), 8000, null, null),
                new ManaInfusionRecipe("original_bronze_dust", Ingredient.of(GTOChemicalHelper.getItem(ingot, Bronze)), new ItemStack(GTOChemicalHelper.getItem(ingot, OriginalBronze)), 6000, null, null));
        for (ManaInfusionRecipe recipe : Infusion) {
            ManaInfusionRecipeBuilder.builder(recipe.id)
                    .input(recipe.input)
                    .output(recipe.output)
                    .mana(recipe.mana)
                    .group(recipe.group)
                    .customCatalyst(recipe.customCatalyst)
                    .save(provider);
        }

        // 花药台
        record ApothecaryRecipe(
                                String id,
                                Ingredient catalyst,
                                ItemStack output,
                                Ingredient input1, Ingredient input2, Ingredient input3, Ingredient input4,
                                Ingredient input5, Ingredient input6, Ingredient input7, Ingredient input8,
                                Ingredient input9, Ingredient input10, Ingredient input11, Ingredient input12,
                                Ingredient input13, Ingredient input14, Ingredient input15, Ingredient input16) {}
        List<ApothecaryRecipe> Apothecary = List.of(
                new ApothecaryRecipe("colorful_mystical_flower", Ingredient.of(ForgeTags.SEEDS), new ItemStack(COLORFUL_MYSTICAL_FLOWER),
                        Ingredient.of(BotaniaItems.whitePetal), Ingredient.of(BotaniaItems.lightGrayPetal), Ingredient.of(BotaniaItems.grayPetal), Ingredient.of(BotaniaItems.blackPetal),
                        Ingredient.of(BotaniaItems.brownPetal), Ingredient.of(BotaniaItems.redPetal), Ingredient.of(BotaniaItems.orangePetal), Ingredient.of(BotaniaItems.yellowPetal),
                        Ingredient.of(BotaniaItems.limePetal), Ingredient.of(BotaniaItems.greenPetal), Ingredient.of(BotaniaItems.cyanPetal), Ingredient.of(BotaniaItems.lightBluePetal),
                        Ingredient.of(BotaniaItems.bluePetal), Ingredient.of(BotaniaItems.purplePetal), Ingredient.of(BotaniaItems.magentaPetal), Ingredient.of(BotaniaItems.pinkPetal)));
        for (ApothecaryRecipe recipe : Apothecary) {
            PetalApothecaryRecipeBuilder.builder(recipe.id)
                    .reagent(recipe.catalyst)
                    .output(recipe.output)
                    .addIngredient(recipe.input1).addIngredient(recipe.input2).addIngredient(recipe.input3).addIngredient(recipe.input4)
                    .addIngredient(recipe.input5).addIngredient(recipe.input6).addIngredient(recipe.input7).addIngredient(recipe.input8)
                    .addIngredient(recipe.input9).addIngredient(recipe.input10).addIngredient(recipe.input11).addIngredient(recipe.input12)
                    .addIngredient(recipe.input13).addIngredient(recipe.input14).addIngredient(recipe.input15).addIngredient(recipe.input16)
                    .save(provider);
        }

        // 符文祭坛
        record RunicAltarRecipe(
                                String id,
                                int mana,
                                ItemStack output,
                                Boolean setHeadRecipe,
                                Ingredient input1, Ingredient input2, Ingredient input3, Ingredient input4,
                                Ingredient input5, Ingredient input6, Ingredient input7, Ingredient input8,
                                Ingredient input9, Ingredient input10, Ingredient input11, Ingredient input12,
                                Ingredient input13, Ingredient input14, Ingredient input15, Ingredient input16) {}
        List<RunicAltarRecipe> RunicAltar = List.of(
                new RunicAltarRecipe("runerock_block", 1000000, new ItemStack(GTOChemicalHelper.getItem(block, Runerock), 4), false,
                        Ingredient.of(BotaniaItems.runeEarth), Ingredient.of(BotaniaItems.runeAir), Ingredient.of(BotaniaItems.runeFire), Ingredient.of(BotaniaItems.runeWater),
                        Ingredient.of(BotaniaItems.runeSpring), Ingredient.of(BotaniaItems.runeSummer), Ingredient.of(BotaniaItems.runeAutumn), Ingredient.of(BotaniaItems.runeWinter),
                        Ingredient.of(BotaniaItems.runeMana), Ingredient.of(BotaniaItems.runeLust), Ingredient.of(BotaniaItems.runeGluttony), Ingredient.of(BotaniaItems.runeGreed),
                        Ingredient.of(BotaniaItems.runeSloth), Ingredient.of(BotaniaItems.runeWrath), Ingredient.of(BotaniaItems.runeEnvy), Ingredient.of(BotaniaItems.runePride)),
                new RunicAltarRecipe("runerock_block_plas", 1000000, new ItemStack(GTOChemicalHelper.getItem(block, Runerock), 8), false,
                        Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:asgard_rune")), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:vanaheim_rune")),
                        Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfheim_rune")), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:midgard_rune")),
                        Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:joetunheim_rune")), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:muspelheim_rune")),
                        Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:niflheim_rune")), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:nidavellir_rune")),
                        Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:helheim_rune")), null, null, null, null, null, null, null));

        for (RunicAltarRecipe recipe : RunicAltar) {
            RunicAltarRecipeBuilder.builder(recipe.id)
                    .output(recipe.output)
                    .mana(recipe.mana)
                    .setHeadRecipe(recipe.setHeadRecipe)
                    .addIngredient(recipe.input1).addIngredient(recipe.input2).addIngredient(recipe.input3).addIngredient(recipe.input4)
                    .addIngredient(recipe.input5).addIngredient(recipe.input6).addIngredient(recipe.input7).addIngredient(recipe.input8)
                    .addIngredient(recipe.input9).addIngredient(recipe.input10).addIngredient(recipe.input11).addIngredient(recipe.input12)
                    .addIngredient(recipe.input13).addIngredient(recipe.input14).addIngredient(recipe.input15).addIngredient(recipe.input16)
                    .save(provider);
        }

        // 泰拉凝聚板
        record TAgglomerationRecipe(
                                    String id,
                                    int mana,
                                    ItemStack output,
                                    Ingredient input1, Ingredient input2, Ingredient input3, Ingredient input4,
                                    Ingredient input5, Ingredient input6, Ingredient input7, Ingredient input8,
                                    Ingredient input9, Ingredient input10, Ingredient input11, Ingredient input12,
                                    Ingredient input13, Ingredient input14, Ingredient input15, Ingredient input16) {}

        List<TAgglomerationRecipe> TAgglomeration = List.of(
                new TAgglomerationRecipe("thaumium_ingot", 500000, new ItemStack(GTOChemicalHelper.getItem(ingot, Thaumium)),
                        Ingredient.of(GTOChemicalHelper.getItem(ingot, Livingsteel)), Ingredient.of(ItemsRegistry.SOURCE_GEM), Ingredient.of(GTOChemicalHelper.getItem(ingot, OriginalBronze)), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE),
                        null, null, null, null, null, null, null, null, null, null, null, null),
                new TAgglomerationRecipe("gaiasteel_ingot", 2500000, new ItemStack(GTOChemicalHelper.getItem(ingot, Gaiasteel), 3),
                        Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:asgard_rune")), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:vanaheim_rune")),
                        Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfheim_rune")), Ingredient.of(GTOChemicalHelper.getItem(ingot, Alfsteel)),
                        Ingredient.of(GTOChemicalHelper.getItem(ingot, Runerock)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:midgard_rune")),
                        Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:joetunheim_rune")), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:muspelheim_rune")),
                        Ingredient.of(GTOChemicalHelper.getItem(ingot, Alfsteel)), Ingredient.of(GTOChemicalHelper.getItem(ingot, Runerock)),
                        Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:niflheim_rune")), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:nidavellir_rune")),
                        Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:helheim_rune")), Ingredient.of(GTOChemicalHelper.getItem(ingot, Alfsteel)),
                        Ingredient.of(GTOChemicalHelper.getItem(ingot, Runerock)), null),
                new TAgglomerationRecipe("gaia_ingot", 5000000, new ItemStack(GTOChemicalHelper.getItem(ingot, Gaia), 2),
                        Ingredient.of(GTOChemicalHelper.getItem(ingot, Gaiasteel)), Ingredient.of(BotaniaItems.lifeEssence), Ingredient.of(GTOChemicalHelper.getItem(ingot, Gaiasteel)), Ingredient.of(BotaniaItems.lifeEssence),
                        null, null, null, null, null, null, null, null, null, null, null, null));

        for (TAgglomerationRecipe recipe : TAgglomeration) {
            TerrestrialAgglomerationRecipeBuilder.builder(recipe.id)
                    .output(recipe.output)
                    .mana(recipe.mana)
                    .addIngredient(recipe.input1).addIngredient(recipe.input2).addIngredient(recipe.input3).addIngredient(recipe.input4)
                    .addIngredient(recipe.input5).addIngredient(recipe.input6).addIngredient(recipe.input7).addIngredient(recipe.input8)
                    .addIngredient(recipe.input9).addIngredient(recipe.input10).addIngredient(recipe.input11).addIngredient(recipe.input12)
                    .addIngredient(recipe.input13).addIngredient(recipe.input14).addIngredient(recipe.input15).addIngredient(recipe.input16)
                    .save(provider);
        }

        // 精灵门
        ElvenTradeRecipeBuilder.builder("dragonstone")
                .addInput(ItemsRegistry.SOURCE_GEM)
                .addOutput(BotaniaItems.dragonstone)
                .save(provider);

        ElvenTradeRecipeBuilder.builder("dragonstone_block")
                .addInput(BlockRegistry.SOURCE_GEM_BLOCK)
                .addOutput(BotaniaBlocks.dragonstoneBlock)
                .save(provider);

        ElvenTradeRecipeBuilder.builder("colorful_mystical_flower")
                .addInput(BotaniaItems.fertilizer)
                .addOutput(COLORFUL_MYSTICAL_FLOWER)
                .save(provider);

        // 凝矿兰
        record OrechidRecipe(
                             int id,
                             String name,
                             Block input,
                             TagPrefix output,
                             Material[] material,
                             int[] weight) {}
        List<OrechidRecipe> Orchid = List.of(
                new OrechidRecipe(1, "overworld_", Blocks.STONE, TagPrefix.ore,
                        new Material[] { GTMaterials.TricalciumPhosphate, GTMaterials.GarnetYellow, GTMaterials.Coal, GTOMaterials.GnomeCrystal, GTMaterials.Mica, GTMaterials.Cobaltite, GTMaterials.GreenSapphire, GTMaterials.YellowLimonite, GTMaterials.Bentonite, GTMaterials.GarnetRed, GTMaterials.RockSalt, GTMaterials.Pyrochlore, GTMaterials.Talc, GTMaterials.Realgar, GTMaterials.Lepidolite, GTMaterials.Ruby, GTMaterials.BasalticMineralSand, GTMaterials.Malachite, GTMaterials.Salt, GTMaterials.Chalcopyrite, GTMaterials.Goethite, GTMaterials.Gold, GTMaterials.Redstone, GTMaterials.Pyrolusite, GTMaterials.GlauconiteSand, GTMaterials.Nickel, GTMaterials.Iron, GTMaterials.Silver, GTMaterials.Spodumene, GTMaterials.Graphite, GTMaterials.Kyanite, GTMaterials.Opal, GTMaterials.Lapis, GTMaterials.Magnetite, GTMaterials.Spessartine, GTMaterials.Amethyst, GTMaterials.Cinnabar, GTOMaterials.PerditioCrystal, GTMaterials.Galena, GTMaterials.Soapstone, GTMaterials.Pentlandite, GTMaterials.Sapphire, GTMaterials.Grossular, GTMaterials.Asbestos, GTMaterials.GraniticMineralSand, GTOMaterials.UndineCrystal, GTMaterials.Pollucite, GTMaterials.FullersEarth, GTMaterials.Almandine, GTMaterials.Oilsands, GTOMaterials.SylphCrystal, GTMaterials.CassiteriteSand, GTMaterials.Cassiterite, GTMaterials.VanadiumMagnetite, GTMaterials.Copper, GTMaterials.Pyrite, GTMaterials.Calcite, GTMaterials.Hematite, GTMaterials.Pyrope, GTMaterials.Tin, GTMaterials.Zeolite, GTMaterials.Diatomite, GTMaterials.Gypsum, GTMaterials.Olivine, GTMaterials.Apatite, GTMaterials.Diamond, GTMaterials.GarnetSand, GTMaterials.Sodalite, GTMaterials.Tantalite, GTOMaterials.SalamanderCrystal, GTMaterials.Garnierite, GTMaterials.Lead, GTMaterials.Lazurite },
                        new int[] { 80, 80, 280, 20, 40, 80, 60, 240, 60, 120, 150, 40, 80, 50, 50, 120, 240, 120, 100, 650, 600, 80, 180, 40, 100, 80, 160, 80, 50, 280, 60, 40, 80, 280, 40, 80, 60, 80, 120, 120, 80, 60, 60, 160, 160, 20, 20, 160, 180, 240, 20, 240, 260, 160, 160, 160, 40, 240, 120, 320, 100, 80, 80, 40, 120, 120, 160, 80, 20, 20, 120, 40, 120 }),
                new OrechidRecipe(2, "the_nether_", Blocks.NETHERRACK, TagPrefix.oreNetherrack,
                        new Material[] { GTMaterials.Tantalite, GTMaterials.Alunite, GTMaterials.Barite, GTMaterials.Molybdenite, GTMaterials.YellowLimonite, GTMaterials.Electrotine, GTMaterials.NetherQuartz, GTMaterials.Grossular, GTMaterials.CertusQuartz, GTMaterials.Beryllium, GTMaterials.Pyrite, GTMaterials.Saltpeter, GTMaterials.Ruby, GTMaterials.Sphalerite, GTMaterials.Sulfur, GTMaterials.Wulfenite, GTMaterials.Bornite, GTMaterials.Hematite, GTMaterials.Copper, GTMaterials.Goethite, GTMaterials.Quartzite, GTMaterials.Tetrahedrite, GTMaterials.Emerald, GTMaterials.Gold, GTMaterials.BlueTopaz, GTMaterials.Molybdenum, GTMaterials.Redstone, GTMaterials.Pyrolusite, GTMaterials.Diatomite, GTMaterials.Chalcocite, GTMaterials.Stibnite, GTMaterials.Powellite, GTMaterials.Spessartine, GTMaterials.Cinnabar, GTMaterials.Topaz },
                        new int[] { 20, 40, 40, 10, 60, 80, 240, 60, 80, 90, 200, 120, 120, 100, 300, 15, 70, 60, 140, 90, 200, 280, 120, 30, 210, 5, 180, 40, 80, 140, 70, 5, 40, 60, 140 }),
                new OrechidRecipe(1, "moon_", ModBlocks.MOON_STONE.get(), GTOTagPrefix.MOON_STONE,
                        new Material[] { GTMaterials.Gold, GTMaterials.Ilmenite, GTMaterials.Soapstone, GTMaterials.Tin, GTMaterials.Pentlandite, GTMaterials.Pitchblende, GTMaterials.Uraninite, GTMaterials.Bauxite, GTMaterials.Neodymium, GTMaterials.Diatomite, GTMaterials.Asbestos, GTMaterials.GlauconiteSand, GTMaterials.Aluminium, GTMaterials.CassiteriteSand, GTMaterials.Talc, GTMaterials.Bastnasite, GTMaterials.Cassiterite, GTMaterials.Monazite, GTMaterials.GarnetSand, GTMaterials.Magnetite, GTMaterials.VanadiumMagnetite },
                        new int[] { 80, 40, 120, 320, 40, 210, 30, 80, 30, 80, 160, 80, 40, 240, 80, 90, 160, 30, 160, 240, 160 }),
                new OrechidRecipe(2, "mars_", ModBlocks.MARS_STONE.get(), GTOTagPrefix.MARS_STONE,
                        new Material[] { GTMaterials.TricalciumPhosphate, GTMaterials.GarnetYellow, GTMaterials.Alunite, GTMaterials.Mica, GTMaterials.Electrotine, GTMaterials.Palladium, GTMaterials.FullersEarth, GTMaterials.GraniticMineralSand, GTMaterials.YellowLimonite, GTMaterials.Pollucite, GTMaterials.GarnetRed, GTMaterials.Pyrite, GTMaterials.Pyrochlore, GTMaterials.Saltpeter, GTMaterials.Platinum, GTMaterials.BasalticMineralSand, GTMaterials.Malachite, GTMaterials.Bornite, GTMaterials.Goethite, GTMaterials.Chalcopyrite, GTMaterials.Copper, GTMaterials.VanadiumMagnetite, GTMaterials.Hematite, GTMaterials.Gold, GTMaterials.Tetrahedrite, GTMaterials.Tungstate, GTMaterials.Diatomite, GTMaterials.Cooperite, GTMaterials.Iron, GTMaterials.Lithium, GTMaterials.Stibnite, GTMaterials.Gypsum, GTMaterials.Opal, GTMaterials.Kyanite, GTMaterials.Apatite, GTMaterials.Scheelite, GTMaterials.Magnetite, GTMaterials.Amethyst },
                        new int[] { 80, 80, 40, 40, 80, 10, 160, 160, 240, 20, 120, 160, 40, 120, 20, 240, 120, 30, 600, 400, 300, 160, 240, 80, 280, 40, 80, 20, 160, 20, 70, 80, 40, 60, 120, 60, 240, 80 }),
                new OrechidRecipe(2, "venus_", ModBlocks.VENUS_STONE.get(), GTOTagPrefix.VENUS_STONE,
                        new Material[] { GTMaterials.Galena, GTMaterials.Coal, GTMaterials.Molybdenite, GTMaterials.YellowLimonite, GTMaterials.Bentonite, GTMaterials.Sulfur, GTMaterials.Wulfenite, GTMaterials.VanadiumMagnetite, GTMaterials.Goethite, GTMaterials.Pyrite, GTMaterials.Hematite, GTMaterials.Gold, GTMaterials.Sphalerite, GTMaterials.Molybdenum, GTMaterials.Chromite, GTOMaterials.Desh, GTMaterials.Magnesite, GTMaterials.GlauconiteSand, GTMaterials.Silver, GTMaterials.Graphite, GTMaterials.Olivine, GTMaterials.Diamond, GTMaterials.Powellite, GTMaterials.Magnetite, GTMaterials.Lead },
                        new int[] { 120, 40, 10, 60, 60, 300, 15, 30, 90, 200, 60, 30, 100, 5, 120, 20, 40, 20, 80, 280, 40, 120, 5, 130, 40 }),
                new OrechidRecipe(2, "mercury_", ModBlocks.MERCURY_STONE.get(), GTOTagPrefix.MERCURY_STONE,
                        new Material[] { GTMaterials.Tantalite, GTMaterials.Garnierite, GTMaterials.Alunite, GTMaterials.Pentlandite, GTMaterials.Palladium, GTMaterials.Cobaltite, GTMaterials.Electrotine, GTMaterials.Grossular, GTMaterials.Cobalt, GTMaterials.Realgar, GTMaterials.Pyrochlore, GTMaterials.Saltpeter, GTMaterials.Cassiterite, GTMaterials.Bornite, GTMaterials.Platinum, GTMaterials.Chalcopyrite, GTMaterials.BlueTopaz, GTMaterials.Pyrolusite, GTMaterials.Zeolite, GTMaterials.Diatomite, GTMaterials.Nickel, GTMaterials.Cooperite, GTOMaterials.Calorite, GTMaterials.Chalcocite, GTMaterials.Spessartine, GTMaterials.Topaz },
                        new int[] { 20, 120, 40, 40, 10, 80, 80, 60, 80, 50, 40, 120, 100, 100, 20, 250, 210, 40, 100, 80, 80, 20, 40, 140, 40, 140 }),
                new OrechidRecipe(3, "glacio_", ModBlocks.GLACIO_STONE.get(), GTOTagPrefix.GLACIO_STONE,
                        new Material[] { GTMaterials.Tantalite, GTOMaterials.Ostrum, GTMaterials.Coal, GTMaterials.Mica, GTMaterials.Palladium, GTMaterials.Tungsten, GTMaterials.Neodymium, GTMaterials.Pollucite, GTMaterials.Asbestos, GTMaterials.Oilsands, GTMaterials.Bastnasite, GTMaterials.RockSalt, GTMaterials.Lepidolite, GTMaterials.CassiteriteSand, GTMaterials.Platinum, GTMaterials.Bornite, GTMaterials.Salt, GTMaterials.Cassiterite, GTMaterials.Calcite, GTMaterials.Gold, GTMaterials.Tungstate, GTMaterials.Tin, GTMaterials.Pyrolusite, GTMaterials.Diatomite, GTMaterials.Cooperite, GTMaterials.Lithium, GTMaterials.Spodumene, GTMaterials.Sodalite, GTOMaterials.Celestine, GTOMaterials.Zircon, GTMaterials.Lapis, GTMaterials.Kyanite, GTMaterials.Monazite, GTMaterials.GarnetSand, GTMaterials.Scheelite, GTMaterials.Trona, GTMaterials.Lazurite },
                        new int[] { 40, 40, 240, 40, 10, 40, 30, 20, 160, 240, 90, 150, 50, 240, 20, 30, 100, 160, 40, 80, 40, 320, 40, 80, 60, 20, 50, 80, 40, 40, 80, 60, 30, 160, 60, 80, 120 }),
                new OrechidRecipe(1, "titan_", GTOBlocks.TITAN_STONE.get(), GTOTagPrefix.TITAN_STONE,
                        new Material[] { GTMaterials.TricalciumPhosphate, GTMaterials.Sapphire, GTMaterials.YellowLimonite, GTMaterials.GreenSapphire, GTMaterials.Asbestos, GTMaterials.Almandine, GTMaterials.Pyrochlore, GTMaterials.CassiteriteSand, GTMaterials.Malachite, GTMaterials.VanadiumMagnetite, GTMaterials.Copper, GTMaterials.Goethite, GTMaterials.Hematite, GTMaterials.Tetrahedrite, GTMaterials.Pyrope, GTMaterials.Pitchblende, GTMaterials.Uraninite, GTMaterials.Chromite, GTMaterials.Diatomite, GTOMaterials.Desh, GTMaterials.Magnesite, GTMaterials.Stibnite, GTMaterials.Apatite, GTMaterials.GarnetSand, GTMaterials.Magnetite },
                        new int[] { 80, 60, 240, 60, 160, 180, 40, 240, 120, 30, 140, 600, 240, 280, 120, 210, 30, 120, 80, 20, 40, 70, 120, 160, 90 }),
                new OrechidRecipe(1, "pluto_", GTOBlocks.PLUTO_STONE.get(), GTOTagPrefix.PLUTO_STONE,
                        new Material[] { GTMaterials.Tantalite, GTMaterials.Galena, GTMaterials.Mica, GTMaterials.Pitchblende, GTMaterials.Uraninite, GTMaterials.Naquadah, GTMaterials.Barite, GTMaterials.Tungsten, GTMaterials.Pyrolusite, GTMaterials.Pollucite, GTMaterials.GarnetRed, GTMaterials.Silver, GTMaterials.CertusQuartz, GTMaterials.Plutonium239, GTMaterials.Pyrochlore, GTOMaterials.Zircon, GTMaterials.Opal, GTMaterials.Kyanite, GTMaterials.Apatite, GTMaterials.Amethyst, GTMaterials.Lead, GTMaterials.GarnetYellow, GTMaterials.Quartzite, GTMaterials.TricalciumPhosphate },
                        new int[] { 40, 120, 40, 210, 30, 210, 40, 40, 40, 20, 120, 80, 80, 30, 40, 40, 40, 60, 120, 80, 40, 80, 120, 80 }),
                new OrechidRecipe(1, "io_", GTOBlocks.IO_STONE.get(), GTOTagPrefix.IO_STONE,
                        new Material[] { GTMaterials.Coal, GTMaterials.GraniticMineralSand, GTMaterials.Naquadah, GTMaterials.Molybdenite, GTMaterials.YellowLimonite, GTMaterials.Bentonite, GTMaterials.FullersEarth, GTMaterials.Sulfur, GTMaterials.BasalticMineralSand, GTMaterials.Malachite, GTMaterials.Wulfenite, GTMaterials.Goethite, GTMaterials.Pyrite, GTMaterials.Hematite, GTMaterials.Sphalerite, GTMaterials.Molybdenum, GTMaterials.Cooperite, GTMaterials.GlauconiteSand, GTMaterials.Plutonium239, GTMaterials.Graphite, GTOMaterials.Celestine, GTMaterials.Gypsum, GTMaterials.Olivine, GTMaterials.Diamond, GTMaterials.Magnetite, GTMaterials.Powellite, GTMaterials.Trona },
                        new int[] { 40, 160, 210, 10, 240, 60, 160, 300, 240, 120, 15, 600, 200, 240, 100, 5, 40, 20, 30, 280, 40, 80, 40, 120, 40, 5, 80 }),
                new OrechidRecipe(1, "ganymede_", GTOBlocks.GANYMEDE_STONE.get(), GTOTagPrefix.GANYMEDE_STONE,
                        new Material[] { GTMaterials.Emerald, GTMaterials.Soapstone, GTMaterials.Garnierite, GTMaterials.Tin, GTMaterials.Mica, GTMaterials.Pentlandite, GTMaterials.Bauxite, GTMaterials.Zeolite, GTMaterials.Cobaltite, GTMaterials.Pollucite, GTMaterials.Nickel, GTMaterials.GlauconiteSand, GTMaterials.Beryllium, GTMaterials.Aluminium, GTMaterials.Talc, GTMaterials.Realgar, GTMaterials.Kyanite, GTMaterials.Cassiterite, GTMaterials.Chalcopyrite, GTMaterials.Ilmenite },
                        new int[] { 120, 120, 120, 320, 40, 80, 80, 100, 80, 20, 80, 80, 90, 40, 80, 50, 60, 260, 250, 40 }),
                new OrechidRecipe(1, "enceladus_", GTOBlocks.ENCELADUS_STONE.get(), GTOTagPrefix.ENCELADUS_STONE,
                        new Material[] { GTMaterials.BlueTopaz, GTMaterials.Galena, GTMaterials.Molybdenum, GTMaterials.Redstone, GTMaterials.Palladium, GTMaterials.Molybdenite, GTMaterials.Cooperite, GTMaterials.Iron, GTMaterials.Silver, GTMaterials.Ruby, GTMaterials.Pyrite, GTMaterials.Chalcocite, GTMaterials.Platinum, GTMaterials.Cinnabar, GTMaterials.Bornite, GTMaterials.Wulfenite, GTMaterials.Powellite, GTMaterials.Chalcopyrite, GTMaterials.Ilmenite, GTMaterials.Copper, GTMaterials.Titanium, GTMaterials.Lead, GTMaterials.Topaz },
                        new int[] { 210, 120, 5, 180, 10, 10, 20, 160, 80, 120, 160, 140, 20, 60, 100, 15, 5, 400, 80, 160, 40, 40, 140 }),
                new OrechidRecipe(1, "ceres_", GTOBlocks.CERES_STONE.get(), GTOTagPrefix.CERES_STONE,
                        new Material[] { GTMaterials.Tantalite, GTOMaterials.Ostrum, GTMaterials.Soapstone, GTMaterials.Pentlandite, GTMaterials.Neodymium, GTMaterials.YellowLimonite, GTMaterials.Bentonite, GTMaterials.Grossular, GTMaterials.Realgar, GTMaterials.Bastnasite, GTMaterials.Talc, GTMaterials.Malachite, GTMaterials.Cassiterite, GTMaterials.Chalcopyrite, GTMaterials.Goethite, GTMaterials.Hematite, GTMaterials.Gold, GTMaterials.Tungstate, GTMaterials.Pyrolusite, GTMaterials.Zeolite, GTMaterials.Lithium, GTMaterials.GlauconiteSand, GTMaterials.Olivine, GTMaterials.Monazite, GTMaterials.Scheelite, GTMaterials.Magnetite, GTMaterials.Spessartine },
                        new int[] { 20, 40, 120, 40, 30, 240, 60, 60, 50, 90, 80, 120, 100, 250, 600, 240, 80, 40, 40, 100, 20, 100, 40, 30, 60, 40, 40 }),
                new OrechidRecipe(3, "otherside_", DDBlocks.SCULK_STONE.get(), GTOTagPrefix.SCULK_STONE,
                        new Material[] { GTMaterials.Ilmenite, GTOMaterials.Ostrum, GTMaterials.Garnierite, GTOMaterials.GnomeCrystal, GTMaterials.Cobaltite, GTMaterials.Mica, GTMaterials.GreenSapphire, GTMaterials.GarnetRed, GTMaterials.YellowLimonite, GTMaterials.Tungsten, GTMaterials.Molybdenite, GTMaterials.Neodymium, GTMaterials.Bentonite, GTMaterials.NetherQuartz, GTMaterials.Lepidolite, GTMaterials.Realgar, GTMaterials.CertusQuartz, GTMaterials.Pyrochlore, GTMaterials.Saltpeter, GTMaterials.Talc, GTMaterials.Bastnasite, GTMaterials.Ruby, GTMaterials.BasalticMineralSand, GTMaterials.Malachite, GTMaterials.Wulfenite, GTMaterials.Platinum, GTMaterials.Chalcopyrite, GTMaterials.Goethite, GTMaterials.Gold, GTMaterials.Emerald, GTMaterials.BlueTopaz, GTMaterials.Uraninite, GTMaterials.Pyrolusite, GTMaterials.Magnesite, GTMaterials.Cooperite, GTMaterials.GlauconiteSand, GTMaterials.Spodumene, GTOMaterials.Celestine, GTMaterials.Lapis, GTMaterials.Opal, GTMaterials.Kyanite, GTMaterials.Amethyst, GTMaterials.Cinnabar, GTMaterials.Topaz, GTOMaterials.PerditioCrystal, GTMaterials.Soapstone, GTMaterials.Palladium, GTMaterials.GraniticMineralSand, GTMaterials.Asbestos, GTMaterials.Pollucite, GTMaterials.Almandine, GTMaterials.Cobalt, GTMaterials.Beryllium, GTOMaterials.SylphCrystal, GTMaterials.Copper, GTMaterials.Cassiterite, GTMaterials.VanadiumMagnetite, GTMaterials.Pyrite, GTMaterials.Calcite, GTMaterials.Tin, GTMaterials.Molybdenum, GTMaterials.Chromite, GTMaterials.Zeolite, GTMaterials.Lithium, GTMaterials.Gypsum, GTMaterials.Apatite, GTMaterials.Sodalite, GTOMaterials.SalamanderCrystal, GTMaterials.Tantalite, GTMaterials.TricalciumPhosphate, GTMaterials.GarnetYellow, GTMaterials.Coal, GTMaterials.Alunite, GTMaterials.Naquadah, GTMaterials.Electrotine, GTMaterials.RockSalt, GTMaterials.Sulfur, GTMaterials.Salt, GTMaterials.Bornite, GTMaterials.Redstone, GTOMaterials.Desh, GTMaterials.Nickel, GTMaterials.Iron, GTMaterials.Silver, GTOMaterials.Calorite, GTMaterials.Aluminium, GTMaterials.Graphite, GTMaterials.Chalcocite, GTMaterials.Scheelite, GTMaterials.Magnetite, GTMaterials.Spessartine, GTMaterials.Titanium, GTMaterials.Galena, GTMaterials.Pentlandite, GTMaterials.Sapphire, GTMaterials.Bauxite, GTMaterials.Barite, GTMaterials.FullersEarth, GTOMaterials.UndineCrystal, GTMaterials.Grossular, GTMaterials.Oilsands, GTMaterials.CassiteriteSand, GTMaterials.Sphalerite, GTMaterials.Quartzite, GTMaterials.Hematite, GTMaterials.Tungstate, GTMaterials.Tetrahedrite, GTMaterials.Pyrope, GTMaterials.Pitchblende, GTMaterials.Diatomite, GTMaterials.Plutonium239, GTOMaterials.Zircon, GTMaterials.Monazite, GTMaterials.Olivine, GTMaterials.Stibnite, GTMaterials.Diamond, GTMaterials.GarnetSand, GTMaterials.Powellite, GTMaterials.Trona, GTMaterials.Lead, GTMaterials.Lazurite },
                        new int[] { 120, 40, 120, 80, 80, 40, 60, 120, 300, 40, 10, 30, 60, 240, 50, 50, 80, 80, 120, 80, 90, 120, 240, 120, 15, 20, 650, 690, 190, 120, 210, 30, 80, 40, 60, 100, 50, 40, 80, 40, 60, 80, 60, 140, 320, 120, 10, 160, 160, 20, 180, 80, 90, 80, 300, 260, 190, 360, 40, 320, 5, 120, 100, 20, 80, 120, 80, 80, 60, 80, 80, 280, 40, 210, 80, 150, 300, 100, 100, 180, 20, 80, 160, 80, 40, 40, 280, 140, 60, 370, 40, 40, 120, 80, 60, 80, 40, 160, 80, 60, 240, 240, 100, 200, 300, 40, 280, 120, 210, 160, 30, 40, 30, 40, 70, 120, 160, 5, 80, 40, 120 }));
        for (OrechidRecipe recipe : Orchid) {
            if (recipe.id == 1) {
                for (int k = 0; k < recipe.material.length; k++) {
                    OrechidRecipeBuilder.builder(recipe.name + recipe.material[k].getName().toLowerCase())
                            .input(recipe.input)
                            .output(ChemicalHelper.getBlock(recipe.output, recipe.material[k]))
                            .weight(recipe.weight[k])
                            .save(provider);
                }
            } else if (recipe.id == 2) {
                for (int k = 0; k < recipe.material.length; k++) {
                    OrechidIgnemRecipeBuilder.builder(recipe.name + recipe.material[k].getName().toLowerCase())
                            .input(recipe.input)
                            .output(ChemicalHelper.getBlock(recipe.output, recipe.material[k]))
                            .weight(recipe.weight[k])
                            .save(provider);
                }
            } else if (recipe.id == 3) {
                for (int k = 0; k < recipe.material.length; k++) {
                    MarimorphosisRecipeBuilder.builder(recipe.name + recipe.material[k].getName().toLowerCase())
                            .input(recipe.input)
                            .output(ChemicalHelper.getBlock(recipe.output, recipe.material[k]))
                            .weight(recipe.weight[k])
                            .biomeTag(BiomeTags.IS_FOREST)
                            .save(provider);
                }
            }
        }

        OrechidIgnemRecipeBuilder.builder("the_nether_ancient_debris")
                .input(Blocks.NETHERRACK)
                .output(Blocks.ANCIENT_DEBRIS)
                .weight(10)
                .save(provider);

        Material[] ores = { PerditioCrystal, GnomeCrystal, SylphCrystal, UndineCrystal, SalamanderCrystal };
        for (Material material : ores) {
            MarimorphosisRecipeBuilder.builder("living_rock_" + material.getName().toLowerCase())
                    .input(BotaniaBlocks.livingrock)
                    .output(ChemicalHelper.getBlock(GTOTagPrefix.LIVING_STONE, material))
                    .weight(10)
                    .biomeTag(BiomeTags.IS_FOREST)
                    .save(provider);
        }

        // 精灵交易
        record ElfExchangeRecipe(
                                 String id,
                                 ItemStack input,
                                 ItemStack output) {}
        List<ElfExchangeRecipe> ElfExchange = List.of(
                new ElfExchangeRecipe("elf_quartz", new ItemStack(Items.QUARTZ, 4), new ItemStack(BotaniaItems.elfQuartz, 4)),
                new ElfExchangeRecipe("elf_glass", new ItemStack(BotaniaBlocks.manaGlass, 4), new ItemStack(BotaniaBlocks.elfGlass, 4)),
                new ElfExchangeRecipe("dreamwood_log", new ItemStack(BotaniaBlocks.livingwoodLog, 4), new ItemStack(BotaniaBlocks.dreamwoodLog, 4)),
                new ElfExchangeRecipe("dreamwood", new ItemStack(BotaniaBlocks.livingwood, 4), new ItemStack(BotaniaBlocks.dreamwood, 4)),
                new ElfExchangeRecipe("elementium", new ItemStack(BotaniaItems.manaSteel, 8), new ItemStack(BotaniaItems.elementium, 4)),
                new ElfExchangeRecipe("elementium_block", new ItemStack(BotaniaBlocks.manasteelBlock, 8), new ItemStack(BotaniaBlocks.elementiumBlock, 4)),
                new ElfExchangeRecipe("pixie_dust", new ItemStack(BotaniaItems.manaPearl, 4), new ItemStack(BotaniaItems.pixieDust, 4)),
                new ElfExchangeRecipe("dragonstone", new ItemStack(ItemsRegistry.SOURCE_GEM, 4), new ItemStack(BotaniaItems.dragonstone, 4)),
                new ElfExchangeRecipe("dragonstone_block", new ItemStack(BlockRegistry.SOURCE_GEM_BLOCK, 4), new ItemStack(BotaniaBlocks.dragonstoneBlock, 4)),
                new ElfExchangeRecipe("colorful_mystical_flower", new ItemStack(BotaniaItems.fertilizer, 4), new ItemStack(COLORFUL_MYSTICAL_FLOWER, 4)));
        for (ElfExchangeRecipe recipe : ElfExchange) {
            ELF_EXCHANGE_RECIPES.builder(recipe.id)
                    .inputItems(recipe.input)
                    .outputItems(recipe.output)
                    .duration(10)
                    .MANAt(8)
                    .save();
        }

        // 魔力灌注 - 魔力池
        record InfusionManaPoolRecipe(
                                      String id,
                                      Item input,
                                      ItemStack output,
                                      int mana,
                                      Block customCatalyst,
                                      String group) {}
        List<InfusionManaPoolRecipe> InfusionManaPool = List.of(
                // === 基础配方 ===//
                new InfusionManaPoolRecipe("manasteel", GTOChemicalHelper.getItem(ingot, Steel), new ItemStack(BotaniaItems.manaSteel), 3000, null, null),
                new InfusionManaPoolRecipe("manasteel_block", GTOChemicalHelper.getItem(block, Steel), new ItemStack(BotaniaBlocks.manasteelBlock), 27000, null, null),
                new InfusionManaPoolRecipe("mana_pearl", RegistriesUtils.getItem("torchmaster:frozen_pearl"), new ItemStack(BotaniaItems.manaPearl), 6000, null, null),
                new InfusionManaPoolRecipe("mana_diamond", Items.DIAMOND, new ItemStack(BotaniaItems.manaDiamond), 10000, null, null),
                new InfusionManaPoolRecipe("mana_diamond_block", Items.DIAMOND_BLOCK, new ItemStack(BotaniaBlocks.manaDiamondBlock), 90000, null, null),
                new InfusionManaPoolRecipe("piston_relay", Items.PISTON, new ItemStack(BotaniaBlocks.pistonRelay), 15000, null, null),
                new InfusionManaPoolRecipe("mana_cookie", Items.COOKIE, new ItemStack(BotaniaItems.manaCookie), 20000, null, null),
                new InfusionManaPoolRecipe("grass_seeds", Items.GRASS, new ItemStack(BotaniaItems.grassSeeds), 2500, null, null),
                new InfusionManaPoolRecipe("podzol_seeds", Items.DEAD_BUSH, new ItemStack(BotaniaItems.podzolSeeds), 2500, null, null),
                new InfusionManaPoolRecipe("mycel_seeds", Items.RED_MUSHROOM, new ItemStack(BotaniaItems.mycelSeeds), 6500, null, null),
                new InfusionManaPoolRecipe("mana_quartz", Items.QUARTZ, new ItemStack(BotaniaItems.manaQuartz), 250, null, null),
                new InfusionManaPoolRecipe("tiny_potato", Items.POTATO, new ItemStack(BotaniaBlocks.tinyPotato), 1337, null, null),
                new InfusionManaPoolRecipe("mana_glass", Items.GLASS, new ItemStack(BotaniaBlocks.manaGlass), 150, null, null),
                new InfusionManaPoolRecipe("mana_string", ItemsRegistry.MAGE_FIBER.asItem(), new ItemStack(BotaniaItems.manaString), 1250, null, null),
                new InfusionManaPoolRecipe("mana_bottle", Items.GLASS_BOTTLE, new ItemStack(BotaniaItems.manaBottle), 5000, null, null),
                new InfusionManaPoolRecipe("hydroangeas_motif", BotaniaFlowerBlocks.hydroangeas.asItem(), new ItemStack(BotaniaBlocks.motifHydroangeas), 2500, null, null),

                // === 炼药配方 ===//
                new InfusionManaPoolRecipe("rotten_flesh_to_leather", Items.ROTTEN_FLESH, new ItemStack(Items.LEATHER), 600, BotaniaBlocks.alchemyCatalyst, null),
                new InfusionManaPoolRecipe("chiseled_stone_bricks", Items.STONE_BRICKS, new ItemStack(Blocks.CHISELED_STONE_BRICKS), 150, BotaniaBlocks.alchemyCatalyst, null),
                new InfusionManaPoolRecipe("ice", Items.SNOW_BLOCK, new ItemStack(Blocks.ICE), 2250, BotaniaBlocks.alchemyCatalyst, null),
                new InfusionManaPoolRecipe("vine_to_lily_pad", Items.VINE, new ItemStack(Blocks.LILY_PAD), 320, BotaniaBlocks.alchemyCatalyst, "botania:vine_and_lily_pad_cycle"),
                new InfusionManaPoolRecipe("lily_pad_to_vine", Items.LILY_PAD, new ItemStack(Blocks.VINE), 320, BotaniaBlocks.alchemyCatalyst, "botania:vine_and_lily_pad_cycle"),
                new InfusionManaPoolRecipe("potato_unpoison", Items.POISONOUS_POTATO, new ItemStack(Items.POTATO), 1200, BotaniaBlocks.alchemyCatalyst, null),
                new InfusionManaPoolRecipe("blaze_rod_to_nether_wart", Items.BLAZE_ROD, new ItemStack(Items.NETHER_WART), 4000, BotaniaBlocks.alchemyCatalyst, null),
                new InfusionManaPoolRecipe("book_to_name_tag", Items.WRITABLE_BOOK, new ItemStack(Items.NAME_TAG), 6000, BotaniaBlocks.alchemyCatalyst, null),
                new InfusionManaPoolRecipe("wool_deconstruct", Items.WHITE_WOOL, new ItemStack(Items.STRING, 3), 100, BotaniaBlocks.alchemyCatalyst, null),
                new InfusionManaPoolRecipe("cactus_to_slime", Items.CACTUS, new ItemStack(Items.SLIME_BALL), 1200, BotaniaBlocks.alchemyCatalyst, "botania:cactus_and_slime_cycle"),
                new InfusionManaPoolRecipe("slime_to_cactus", Items.SLIME_BALL, new ItemStack(Blocks.CACTUS), 1200, BotaniaBlocks.alchemyCatalyst, "botania:cactus_and_slime_cycle"),
                new InfusionManaPoolRecipe("ender_pearl_from_ghast_tear", Items.GHAST_TEAR, new ItemStack(Items.ENDER_PEARL), 28000, BotaniaBlocks.alchemyCatalyst, null),
                new InfusionManaPoolRecipe("cobble_to_sand", Items.COBBLESTONE, new ItemStack(Blocks.SAND), 50, BotaniaBlocks.alchemyCatalyst, null),
                new InfusionManaPoolRecipe("terracotta_to_red_sand", Items.TERRACOTTA, new ItemStack(Blocks.RED_SAND), 50, BotaniaBlocks.alchemyCatalyst, null),
                new InfusionManaPoolRecipe("coarse_dirt", Items.DIRT, new ItemStack(Blocks.COARSE_DIRT), 120, BotaniaBlocks.alchemyCatalyst, null),
                new InfusionManaPoolRecipe("soul_soil", Items.SOUL_SAND, new ItemStack(Blocks.SOUL_SOIL), 120, BotaniaBlocks.alchemyCatalyst, null),
                new InfusionManaPoolRecipe("stone_to_andesite", Items.STONE, new ItemStack(Blocks.ANDESITE), 200, BotaniaBlocks.alchemyCatalyst, null),
                new InfusionManaPoolRecipe("dripleaf_shrinking", Items.BIG_DRIPLEAF, new ItemStack(Blocks.SMALL_DRIPLEAF), 500, BotaniaBlocks.alchemyCatalyst, null),
                new InfusionManaPoolRecipe("chorus_fruit_to_flower", Items.POPPED_CHORUS_FRUIT, new ItemStack(Blocks.CHORUS_FLOWER), 10000, BotaniaBlocks.alchemyCatalyst, null),

                // === 方块分解 ===//
                new InfusionManaPoolRecipe("glowstone_deconstruct", Items.GLOWSTONE, new ItemStack(Items.GLOWSTONE_DUST, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction"),
                new InfusionManaPoolRecipe("quartz_deconstruct", Items.QUARTZ_BLOCK, new ItemStack(Items.QUARTZ, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction"),
                new InfusionManaPoolRecipe("dark_quartz_deconstruct", BotaniaBlocks.darkQuartz.asItem(), new ItemStack(BotaniaItems.darkQuartz, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction"),
                new InfusionManaPoolRecipe("mana_quartz_deconstruct", BotaniaBlocks.manaQuartz.asItem(), new ItemStack(BotaniaItems.manaQuartz, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction"),
                new InfusionManaPoolRecipe("blaze_quartz_deconstruct", BotaniaBlocks.blazeQuartz.asItem(), new ItemStack(BotaniaItems.blazeQuartz, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction"),
                new InfusionManaPoolRecipe("lavender_quartz_deconstruct", BotaniaBlocks.lavenderQuartz.asItem(), new ItemStack(BotaniaItems.lavenderQuartz, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction"),
                new InfusionManaPoolRecipe("red_quartz_deconstruct", BotaniaBlocks.redQuartz.asItem(), new ItemStack(BotaniaItems.redQuartz, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction"),
                new InfusionManaPoolRecipe("elf_quartz_deconstruct", BotaniaBlocks.elfQuartz.asItem(), new ItemStack(BotaniaItems.elfQuartz, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction"),
                new InfusionManaPoolRecipe("sunny_quartz_deconstruct", BotaniaBlocks.sunnyQuartz.asItem(), new ItemStack(BotaniaItems.sunnyQuartz, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction"),
                new InfusionManaPoolRecipe("clay_deconstruct", Items.CLAY, new ItemStack(Items.CLAY_BALL, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction"),
                new InfusionManaPoolRecipe("brick_deconstruct", Items.BRICKS, new ItemStack(Items.BRICK, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction"),

                // === 循环配方 ===//
                // 原木循环 (log_cycle)
                new InfusionManaPoolRecipe("oak_log_to_spruce_log", Items.OAK_LOG, new ItemStack(Blocks.SPRUCE_LOG), 40, BotaniaBlocks.alchemyCatalyst, "botania:log_cycle"),
                new InfusionManaPoolRecipe("spruce_log_to_birch_log", Items.SPRUCE_LOG, new ItemStack(Blocks.BIRCH_LOG), 40, BotaniaBlocks.alchemyCatalyst, "botania:log_cycle"),
                new InfusionManaPoolRecipe("birch_log_to_jungle_log", Items.BIRCH_LOG, new ItemStack(Blocks.JUNGLE_LOG), 40, BotaniaBlocks.alchemyCatalyst, "botania:log_cycle"),
                new InfusionManaPoolRecipe("jungle_log_to_acacia_log", Items.JUNGLE_LOG, new ItemStack(Blocks.ACACIA_LOG), 40, BotaniaBlocks.alchemyCatalyst, "botania:log_cycle"),
                new InfusionManaPoolRecipe("acacia_log_to_dark_oak_log", Items.ACACIA_LOG, new ItemStack(Blocks.DARK_OAK_LOG), 40, BotaniaBlocks.alchemyCatalyst, "botania:log_cycle"),
                new InfusionManaPoolRecipe("dark_oak_log_to_mangrove_log", Items.DARK_OAK_LOG, new ItemStack(Blocks.MANGROVE_LOG), 40, BotaniaBlocks.alchemyCatalyst, "botania:log_cycle"),
                new InfusionManaPoolRecipe("mangrove_log_to_cherry_log", Items.MANGROVE_LOG, new ItemStack(Blocks.CHERRY_LOG), 40, BotaniaBlocks.alchemyCatalyst, "botania:log_cycle"),
                new InfusionManaPoolRecipe("cherry_log_to_oak_log", Items.CHERRY_LOG, new ItemStack(Blocks.OAK_LOG), 40, BotaniaBlocks.alchemyCatalyst, "botania:log_cycle"),

                // 青蛙灯循环 (froglight_cycle)
                new InfusionManaPoolRecipe("ochre_froglight_to_verdant_froglight", Items.OCHRE_FROGLIGHT, new ItemStack(Blocks.VERDANT_FROGLIGHT), 120, BotaniaBlocks.alchemyCatalyst, "botania:froglight_cycle"),
                new InfusionManaPoolRecipe("verdant_froglight_to_pearlescent_froglight", Items.VERDANT_FROGLIGHT, new ItemStack(Blocks.PEARLESCENT_FROGLIGHT), 120, BotaniaBlocks.alchemyCatalyst, "botania:froglight_cycle"),
                new InfusionManaPoolRecipe("pearlescent_froglight_to_ochre_froglight", Items.PEARLESCENT_FROGLIGHT, new ItemStack(Blocks.OCHRE_FROGLIGHT), 120, BotaniaBlocks.alchemyCatalyst, "botania:froglight_cycle"),

                // 树苗循环（sapling_cycle，共7种+红树和樱花）
                new InfusionManaPoolRecipe("oak_sapling_to_spruce_sapling", Items.OAK_SAPLING, new ItemStack(Blocks.SPRUCE_SAPLING), 120, BotaniaBlocks.alchemyCatalyst, "botania:sapling_cycle"),
                new InfusionManaPoolRecipe("spruce_sapling_to_birch_sapling", Items.SPRUCE_SAPLING, new ItemStack(Blocks.BIRCH_SAPLING), 120, BotaniaBlocks.alchemyCatalyst, "botania:sapling_cycle"),
                new InfusionManaPoolRecipe("birch_sapling_to_jungle_sapling", Items.BIRCH_SAPLING, new ItemStack(Blocks.JUNGLE_SAPLING), 120, BotaniaBlocks.alchemyCatalyst, "botania:sapling_cycle"),
                new InfusionManaPoolRecipe("jungle_sapling_to_acacia_sapling", Items.JUNGLE_SAPLING, new ItemStack(Blocks.ACACIA_SAPLING), 120, BotaniaBlocks.alchemyCatalyst, "botania:sapling_cycle"),
                new InfusionManaPoolRecipe("acacia_sapling_to_dark_oak_sapling", Items.ACACIA_SAPLING, new ItemStack(Blocks.DARK_OAK_SAPLING), 120, BotaniaBlocks.alchemyCatalyst, "botania:sapling_cycle"),
                new InfusionManaPoolRecipe("dark_oak_sapling_to_mangrove_propagule", Items.DARK_OAK_SAPLING, new ItemStack(Blocks.MANGROVE_PROPAGULE), 120, BotaniaBlocks.alchemyCatalyst, "botania:sapling_cycle"),
                new InfusionManaPoolRecipe("mangrove_propagule_to_cherry_sapling", Items.MANGROVE_PROPAGULE, new ItemStack(Blocks.CHERRY_SAPLING), 120, BotaniaBlocks.alchemyCatalyst, "botania:sapling_cycle"),
                new InfusionManaPoolRecipe("cherry_sapling_to_oak_sapling", Items.CHERRY_SAPLING, new ItemStack(Blocks.OAK_SAPLING), 120, BotaniaBlocks.alchemyCatalyst, "botania:sapling_cycle"),

                // 鱼类循环
                new InfusionManaPoolRecipe("cod_to_salmon", Items.COD, new ItemStack(Items.SALMON), 200, BotaniaBlocks.alchemyCatalyst, "botania:fish_cycle"),
                new InfusionManaPoolRecipe("salmon_to_tropical_fish", Items.SALMON, new ItemStack(Items.TROPICAL_FISH), 200, BotaniaBlocks.alchemyCatalyst, "botania:fish_cycle"),
                new InfusionManaPoolRecipe("tropical_fish_to_pufferfish", Items.TROPICAL_FISH, new ItemStack(Items.PUFFERFISH), 200, BotaniaBlocks.alchemyCatalyst, "botania:fish_cycle"),
                new InfusionManaPoolRecipe("pufferfish_to_cod", Items.PUFFERFISH, new ItemStack(Items.COD), 200, BotaniaBlocks.alchemyCatalyst, "botania:fish_cycle"),

                // 石头循环（stone_cycle）
                new InfusionManaPoolRecipe("diorite_to_granite", Items.DIORITE, new ItemStack(Blocks.GRANITE), 200, BotaniaBlocks.alchemyCatalyst, "botania:stone_cycle"),
                new InfusionManaPoolRecipe("granite_to_andesite", Items.GRANITE, new ItemStack(Blocks.ANDESITE), 200, BotaniaBlocks.alchemyCatalyst, "botania:stone_cycle"),
                new InfusionManaPoolRecipe("andesite_to_diorite", Items.ANDESITE, new ItemStack(Blocks.DIORITE), 200, BotaniaBlocks.alchemyCatalyst, "botania:stone_cycle"),

                // 1.17石头循环（117_stone_cycle）
                new InfusionManaPoolRecipe("tuff_to_calcite", Items.TUFF, new ItemStack(Blocks.CALCITE), 200, BotaniaBlocks.alchemyCatalyst, "botania:117_stone_cycle"),
                new InfusionManaPoolRecipe("calcite_to_deepslate", Items.CALCITE, new ItemStack(Blocks.DEEPSLATE), 200, BotaniaBlocks.alchemyCatalyst, "botania:117_stone_cycle"),
                new InfusionManaPoolRecipe("deepslate_to_tuff", Items.DEEPSLATE, new ItemStack(Blocks.TUFF), 200, BotaniaBlocks.alchemyCatalyst, "botania:117_stone_cycle"),

                // 花循环（flower_cycle）
                new InfusionManaPoolRecipe("dandelion_to_poppy", Items.DANDELION, new ItemStack(Blocks.POPPY), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle"),
                new InfusionManaPoolRecipe("poppy_to_blue_orchid", Items.POPPY, new ItemStack(Blocks.BLUE_ORCHID), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle"),
                new InfusionManaPoolRecipe("blue_orchid_to_allium", Items.BLUE_ORCHID, new ItemStack(Blocks.ALLIUM), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle"),
                new InfusionManaPoolRecipe("allium_to_azure_bluet", Items.ALLIUM, new ItemStack(Blocks.AZURE_BLUET), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle"),
                new InfusionManaPoolRecipe("azure_bluet_to_red_tulip", Items.AZURE_BLUET, new ItemStack(Blocks.RED_TULIP), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle"),
                new InfusionManaPoolRecipe("red_tulip_to_orange_tulip", Items.RED_TULIP, new ItemStack(Blocks.ORANGE_TULIP), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle"),
                new InfusionManaPoolRecipe("orange_tulip_to_white_tulip", Items.ORANGE_TULIP, new ItemStack(Blocks.WHITE_TULIP), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle"),
                new InfusionManaPoolRecipe("white_tulip_to_pink_tulip", Items.WHITE_TULIP, new ItemStack(Blocks.PINK_TULIP), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle"),
                new InfusionManaPoolRecipe("pink_tulip_to_oxeye_daisy", Items.PINK_TULIP, new ItemStack(Blocks.OXEYE_DAISY), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle"),
                new InfusionManaPoolRecipe("oxeye_daisy_to_cornflower", Items.OXEYE_DAISY, new ItemStack(Blocks.CORNFLOWER), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle"),
                new InfusionManaPoolRecipe("cornflower_to_lily_of_the_valley", Items.CORNFLOWER, new ItemStack(Blocks.LILY_OF_THE_VALLEY), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle"),
                new InfusionManaPoolRecipe("lily_of_the_valley_to_sunflower", Items.LILY_OF_THE_VALLEY, new ItemStack(Blocks.SUNFLOWER), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle"),
                new InfusionManaPoolRecipe("sunflower_to_lilac", Items.SUNFLOWER, new ItemStack(Blocks.LILAC), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle"),
                new InfusionManaPoolRecipe("lilac_to_rose_bush", Items.LILAC, new ItemStack(Blocks.ROSE_BUSH), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle"),
                new InfusionManaPoolRecipe("rose_bush_to_peony", Items.ROSE_BUSH, new ItemStack(Blocks.PEONY), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle"),
                new InfusionManaPoolRecipe("peony_to_dandelion", Items.PEONY, new ItemStack(Blocks.DANDELION), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle"),

                // 种子循环（crop_cycle）
                new InfusionManaPoolRecipe("cocoa_beans_to_wheat_seeds", Items.COCOA_BEANS, new ItemStack(Items.WHEAT_SEEDS), 6000, BotaniaBlocks.alchemyCatalyst, "botania:crop_cycle"),
                new InfusionManaPoolRecipe("wheat_seeds_to_potato", Items.WHEAT_SEEDS, new ItemStack(Items.POTATO), 6000, BotaniaBlocks.alchemyCatalyst, "botania:crop_cycle"),
                new InfusionManaPoolRecipe("potato_to_carrot", Items.POTATO, new ItemStack(Items.CARROT), 6000, BotaniaBlocks.alchemyCatalyst, "botania:crop_cycle"),
                new InfusionManaPoolRecipe("carrot_to_beetroot_seeds", Items.CARROT, new ItemStack(Items.BEETROOT_SEEDS), 6000, BotaniaBlocks.alchemyCatalyst, "botania:crop_cycle"),
                new InfusionManaPoolRecipe("beetroot_seeds_to_melon_seeds", Items.BEETROOT_SEEDS, new ItemStack(Items.MELON_SEEDS), 6000, BotaniaBlocks.alchemyCatalyst, "botania:crop_cycle"),
                new InfusionManaPoolRecipe("melon_seeds_to_pumpkin_seeds", Items.MELON_SEEDS, new ItemStack(Items.PUMPKIN_SEEDS), 6000, BotaniaBlocks.alchemyCatalyst, "botania:crop_cycle"),
                new InfusionManaPoolRecipe("pumpkin_seeds_to_cocoa_beans", Items.PUMPKIN_SEEDS, new ItemStack(Items.COCOA_BEANS), 6000, BotaniaBlocks.alchemyCatalyst, "botania:crop_cycle"),

                // 萤石红石循环（glowstone_and_redstone_cycle）
                new InfusionManaPoolRecipe("redstone_to_glowstone_dust", Items.REDSTONE, new ItemStack(Items.GLOWSTONE_DUST), 300, BotaniaBlocks.alchemyCatalyst, "botania:glowstone_and_redstone_cycle"),
                new InfusionManaPoolRecipe("glowstone_dust_to_redstone", Items.GLOWSTONE_DUST, new ItemStack(Items.REDSTONE), 300, BotaniaBlocks.alchemyCatalyst, "botania:glowstone_and_redstone_cycle"),

                // 灌木循环（shrub_cycle）
                new InfusionManaPoolRecipe("fern_to_dead_bush", Items.FERN, new ItemStack(Blocks.DEAD_BUSH), 500, BotaniaBlocks.alchemyCatalyst, "botania:shrub_cycle"),
                new InfusionManaPoolRecipe("dead_bush_to_grass", Items.DEAD_BUSH, new ItemStack(Blocks.GRASS), 500, BotaniaBlocks.alchemyCatalyst, "botania:shrub_cycle"),
                new InfusionManaPoolRecipe("grass_to_fern", Items.GRASS, new ItemStack(Blocks.FERN), 500, BotaniaBlocks.alchemyCatalyst, "botania:shrub_cycle"),

                // 浆果循环（berry_cycle）
                new InfusionManaPoolRecipe("apple_to_sweet_berries", Items.APPLE, new ItemStack(Items.SWEET_BERRIES), 240, BotaniaBlocks.alchemyCatalyst, "botania:berry_cycle"),
                new InfusionManaPoolRecipe("sweet_berries_to_glow_berries", Items.SWEET_BERRIES, new ItemStack(Items.GLOW_BERRIES), 240, BotaniaBlocks.alchemyCatalyst, "botania:berry_cycle"),
                new InfusionManaPoolRecipe("glow_berries_to_apple", Items.GLOW_BERRIES, new ItemStack(Items.APPLE), 240, BotaniaBlocks.alchemyCatalyst, "botania:berry_cycle"),

                // 火药和燧石循环（gunpowder_and_flint_cycle）
                new InfusionManaPoolRecipe("gunpowder_to_flint", Items.GUNPOWDER, new ItemStack(Items.FLINT), 200, BotaniaBlocks.alchemyCatalyst, "gunpowder_and_flint_cycle"),
                new InfusionManaPoolRecipe("flint_to_gunpowder", Items.FLINT, new ItemStack(Items.GUNPOWDER), 200, BotaniaBlocks.alchemyCatalyst, "gunpowder_and_flint_cycle"),

                // === 迷你花配方 ===//
                new InfusionManaPoolRecipe("agricarnation_chibi", BotaniaFlowerBlocks.agricarnation.asItem(), new ItemStack(BotaniaFlowerBlocks.agricarnationChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking"),
                new InfusionManaPoolRecipe("clayconia_chibi", BotaniaFlowerBlocks.clayconia.asItem(), new ItemStack(BotaniaFlowerBlocks.clayconiaChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking"),
                new InfusionManaPoolRecipe("bellethorn_chibi", BotaniaFlowerBlocks.bellethorn.asItem(), new ItemStack(BotaniaFlowerBlocks.bellethornChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking"),
                new InfusionManaPoolRecipe("bubbell_chibi", BotaniaFlowerBlocks.bubbell.asItem(), new ItemStack(BotaniaFlowerBlocks.bubbellChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking"),
                new InfusionManaPoolRecipe("hopperhock_chibi", BotaniaFlowerBlocks.hopperhock.asItem(), new ItemStack(BotaniaFlowerBlocks.hopperhockChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking"),
                new InfusionManaPoolRecipe("jiyuulia_chibi", BotaniaFlowerBlocks.jiyuulia.asItem(), new ItemStack(BotaniaFlowerBlocks.jiyuuliaChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking"),
                new InfusionManaPoolRecipe("tangleberrie_chibi", BotaniaFlowerBlocks.tangleberrie.asItem(), new ItemStack(BotaniaFlowerBlocks.tangleberrieChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking"),
                new InfusionManaPoolRecipe("marimorphosis_chibi", BotaniaFlowerBlocks.marimorphosis.asItem(), new ItemStack(BotaniaFlowerBlocks.marimorphosisChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking"),
                new InfusionManaPoolRecipe("rannuncarpus_chibi", BotaniaFlowerBlocks.rannuncarpus.asItem(), new ItemStack(BotaniaFlowerBlocks.rannuncarpusChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking"),
                new InfusionManaPoolRecipe("solegnolia_chibi", BotaniaFlowerBlocks.solegnolia.asItem(), new ItemStack(BotaniaFlowerBlocks.solegnoliaChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking"),

                // === 复制配方 ===//
                new InfusionManaPoolRecipe("redstone_dupe", Items.REDSTONE, new ItemStack(Items.REDSTONE, 2), 5000, BotaniaBlocks.conjurationCatalyst, null),
                new InfusionManaPoolRecipe("glowstone_dupe", Items.GLOWSTONE_DUST, new ItemStack(Items.GLOWSTONE_DUST, 2), 5000, BotaniaBlocks.conjurationCatalyst, null),
                new InfusionManaPoolRecipe("quartz_dupe", Items.QUARTZ, new ItemStack(Items.QUARTZ, 2), 2500, BotaniaBlocks.conjurationCatalyst, null),
                new InfusionManaPoolRecipe("coal_dupe", Items.COAL, new ItemStack(Items.COAL, 2), 2100, BotaniaBlocks.conjurationCatalyst, null),
                new InfusionManaPoolRecipe("snowball_dupe", Items.SNOWBALL, new ItemStack(Items.SNOWBALL, 2), 200, BotaniaBlocks.conjurationCatalyst, null),
                new InfusionManaPoolRecipe("netherrack_dupe", Items.NETHERRACK, new ItemStack(Blocks.NETHERRACK, 2), 200, BotaniaBlocks.conjurationCatalyst, null),
                new InfusionManaPoolRecipe("soul_sand_dupe", Items.SOUL_SAND, new ItemStack(Blocks.SOUL_SAND, 2), 1500, BotaniaBlocks.conjurationCatalyst, null),
                new InfusionManaPoolRecipe("gravel_dupe", Items.GRAVEL, new ItemStack(Blocks.GRAVEL, 2), 720, BotaniaBlocks.conjurationCatalyst, null),
                new InfusionManaPoolRecipe("grass_dupe", Items.GRASS, new ItemStack(Blocks.GRASS, 2), 800, BotaniaBlocks.conjurationCatalyst, null),

                // 树叶复制
                new InfusionManaPoolRecipe("oak_leaves_dupe", Items.OAK_LEAVES, new ItemStack(Blocks.OAK_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null),
                new InfusionManaPoolRecipe("birch_leaves_dupe", Items.BIRCH_LEAVES, new ItemStack(Blocks.BIRCH_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null),
                new InfusionManaPoolRecipe("spruce_leaves_dupe", Items.SPRUCE_LEAVES, new ItemStack(Blocks.SPRUCE_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null),
                new InfusionManaPoolRecipe("jungle_leaves_dupe", Items.JUNGLE_LEAVES, new ItemStack(Blocks.JUNGLE_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null),
                new InfusionManaPoolRecipe("acacia_leaves_dupe", Items.ACACIA_LEAVES, new ItemStack(Blocks.ACACIA_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null),
                new InfusionManaPoolRecipe("dark_oak_leaves_dupe", Items.DARK_OAK_LEAVES, new ItemStack(Blocks.DARK_OAK_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null),
                new InfusionManaPoolRecipe("azalea_leaves_dupe", Items.AZALEA_LEAVES, new ItemStack(Blocks.AZALEA_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null),
                new InfusionManaPoolRecipe("flowering_azalea_leaves_dupe", Items.FLOWERING_AZALEA_LEAVES, new ItemStack(Blocks.FLOWERING_AZALEA_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null),
                new InfusionManaPoolRecipe("mangrove_leaves_dupe", Items.MANGROVE_LEAVES, new ItemStack(Blocks.MANGROVE_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null),
                new InfusionManaPoolRecipe("cherry_leaves_dupe", Items.CHERRY_LEAVES, new ItemStack(Blocks.CHERRY_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null),

                // GTO配方
                new InfusionManaPoolRecipe("pulsating", GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, PulsatingAlloy), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, PulsatingAlloy)), 400, null, null),
                new InfusionManaPoolRecipe("conductivee", GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, ConductiveAlloy), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, ConductiveAlloy)), 1600, null, null),
                new InfusionManaPoolRecipe("energeticalloy", GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, EnergeticAlloy), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, EnergeticAlloy)), 6400, null, null),
                new InfusionManaPoolRecipe("vibrantalloy", GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, VibrantAlloy), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, VibrantAlloy)), 25600, null, null),
                new InfusionManaPoolRecipe("endsteel", GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, EndSteel), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, EndSteel)), 102400, null, null),

                new InfusionManaPoolRecipe("infused_gold", Items.GOLD_INGOT, new ItemStack(GTOChemicalHelper.getItem(ingot, InfusedGold)), 8000, null, null),
                new InfusionManaPoolRecipe("original_bronze_dust", GTOChemicalHelper.getItem(ingot, Bronze), new ItemStack(GTOChemicalHelper.getItem(ingot, OriginalBronze)), 6000, null, null),

                new InfusionManaPoolRecipe("life_essence", GTOItems.UNSTABLE_GAIA_SOUL.asItem(), new ItemStack(BotaniaItems.lifeEssence), 500000, ChemicalHelper.getBlock(block, Gaia), null));
        for (InfusionManaPoolRecipe recipe : InfusionManaPool) {
            Item customCatalyst = (recipe.customCatalyst == null) ? BotaniaBlocks.livingrock.asItem() : recipe.customCatalyst.asItem();
            int manat = Math.max(recipe.mana / 500, 1);
            int count = 1;
            if (manat == 1) count = Math.max(500 / recipe.mana, 1);
            MANA_INFUSER_RECIPES.builder(recipe.id)
                    .notConsumable(customCatalyst)
                    .inputItems(recipe.input, count)
                    .outputItems(recipe.output.copyWithCount(recipe.output.getCount() * count))
                    .duration(20)
                    .circuitMeta(1)
                    .MANAt(manat)
                    .save();
        }
        MANA_INFUSER_RECIPES.builder("mana_powder_dust")
                .notConsumable(BotaniaBlocks.livingrock.asItem())
                .inputItems(Ingredient.of(Items.GUNPOWDER, Items.REDSTONE, Items.GLOWSTONE_DUST, Items.SUGAR))
                .outputItems(BotaniaItems.manaPowder)
                .duration(20)
                .circuitMeta(1)
                .MANAt(1)
                .save();
        MANA_INFUSER_RECIPES.builder("mana_powder_dye")
                .notConsumable(BotaniaBlocks.livingrock.asItem())
                .inputItems(net.minecraftforge.common.Tags.Items.DYES)
                .outputItems(BotaniaItems.manaPowder)
                .duration(20)
                .circuitMeta(1)
                .MANAt(1)
                .save();

        // 魔力灌注 - 白雏菊
        record InfusionPureDaisyRecipe(
                                       String id, Item input, Item output) {}
        List<InfusionPureDaisyRecipe> InfusionPureDaisy = List.of(
                new InfusionPureDaisyRecipe("livingclay", Items.DIRT, GTOChemicalHelper.getItem(block, Livingclay)),
                new InfusionPureDaisyRecipe("livingrock", Items.STONE, BotaniaBlocks.livingrock.asItem()),
                new InfusionPureDaisyRecipe("livingwood1", BlockRegistry.BLAZING_LOG.asItem(), BotaniaBlocks.livingwoodLog.asItem()),
                new InfusionPureDaisyRecipe("livingwood2", BlockRegistry.CASCADING_LOG.asItem(), BotaniaBlocks.livingwoodLog.asItem()),
                new InfusionPureDaisyRecipe("livingwood3", BlockRegistry.VEXING_LOG.asItem(), BotaniaBlocks.livingwoodLog.asItem()),
                new InfusionPureDaisyRecipe("livingwood4", BlockRegistry.FLOURISHING_LOG.asItem(), BotaniaBlocks.livingwoodLog.asItem()),

                new InfusionPureDaisyRecipe("cobblestone", Items.NETHERRACK, Items.COBBLESTONE),
                new InfusionPureDaisyRecipe("end_stone_to_cobbled_deepslate", Items.END_STONE, Items.COBBLED_DEEPSLATE),
                new InfusionPureDaisyRecipe("sand", Items.SOUL_SAND, Items.SAND),
                new InfusionPureDaisyRecipe("packed_ice", Items.ICE, Items.PACKED_ICE),
                new InfusionPureDaisyRecipe("blue_ice", Items.PACKED_ICE, Items.BLUE_ICE),
                new InfusionPureDaisyRecipe("obsidian", BotaniaBlocks.blazeBlock.asItem(), Items.OBSIDIAN));
        for (InfusionPureDaisyRecipe recipe : InfusionPureDaisy) {
            MANA_INFUSER_RECIPES.builder(recipe.id)
                    .notConsumable(BotaniaFlowerBlocks.pureDaisy.asItem())
                    .inputItems(recipe.input, 32)
                    .outputItems(recipe.output, 32)
                    .duration(600)
                    .circuitMeta(2)
                    .MANAt(1)
                    .save();
        }

        MANA_INFUSER_RECIPES.builder("bifrost_perm")
                .notConsumable("botania:rainbow_rod")
                .inputItems(BotaniaBlocks.elfGlass, 16)
                .outputItems(BotaniaBlocks.bifrostPerm, 16)
                .duration(200)
                .MANAt(1)
                .save();

        // 工业祭坛 - 符文祭坛
        record IndustrialAltarRecipe(
                                     int circuitMeta,
                                     String id,
                                     int mana,
                                     Item input,
                                     ItemStack output,
                                     Item[] inputs) {}
        List<IndustrialAltarRecipe> IndustrialAltar = List.of(
                new IndustrialAltarRecipe(1, "water_rune", 5200, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeWater, 2), new Item[] { BotaniaItems.manaPowder, BotaniaItems.manaSteel, Items.BONE_MEAL, Items.SUGAR_CANE, Items.FISHING_ROD }),
                new IndustrialAltarRecipe(1, "fire_rune", 5200, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeFire, 2), new Item[] { BotaniaItems.manaPowder, BotaniaItems.manaSteel, Items.STONE, Items.COAL_BLOCK, Items.NETHER_WART }),
                new IndustrialAltarRecipe(1, "earth_rune", 5200, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeEarth, 2), new Item[] { BotaniaItems.manaPowder, BotaniaItems.manaSteel, Items.BONE_MEAL, Items.SUGAR_CANE, Items.RED_MUSHROOM }),
                new IndustrialAltarRecipe(1, "air_rune", 5200, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeAir, 2), new Item[] { BotaniaItems.manaPowder, BotaniaItems.manaSteel, Items.WHITE_CARPET, Items.FEATHER, Items.STRING }),

                new IndustrialAltarRecipe(1, "spring_rune", 8000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeSpring), new Item[] { BotaniaItems.runeWater, BotaniaItems.runeFire, Items.OAK_SAPLING, Items.OAK_SAPLING, Items.OAK_SAPLING, Items.WHEAT }),
                new IndustrialAltarRecipe(1, "summer_rune", 8000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeSummer), new Item[] { BotaniaItems.runeEarth, BotaniaItems.runeAir, Items.SAND, Items.SAND, Items.SLIME_BALL, Items.MELON_SLICE }),
                new IndustrialAltarRecipe(1, "autumn_rune", 8000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeAutumn), new Item[] { BotaniaItems.runeFire, BotaniaItems.runeAir, Items.OAK_LEAVES, Items.OAK_LEAVES, Items.OAK_LEAVES, Items.SPIDER_EYE }),
                new IndustrialAltarRecipe(1, "winter_rune", 8000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeWinter), new Item[] { BotaniaItems.runeWater, BotaniaItems.runeEarth, Items.SNOW_BLOCK, Items.SNOW_BLOCK, Items.WHITE_WOOL, Items.CAKE }),

                new IndustrialAltarRecipe(1, "mana_rune", 8000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeMana), new Item[] { BotaniaItems.manaSteel, BotaniaItems.manaSteel, BotaniaItems.manaSteel, BotaniaItems.manaSteel, BotaniaItems.manaSteel, BotaniaItems.manaPearl }),

                new IndustrialAltarRecipe(1, "lust_rune", 12000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeLust), new Item[] { BotaniaItems.runeSummer, BotaniaItems.runeAir, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond }),
                new IndustrialAltarRecipe(1, "gluttony_rune", 12000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeGluttony), new Item[] { BotaniaItems.runeWinter, BotaniaItems.runeFire, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond }),
                new IndustrialAltarRecipe(1, "greed_rune", 12000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeGreed), new Item[] { BotaniaItems.runeSpring, BotaniaItems.runeWater, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond }),
                new IndustrialAltarRecipe(1, "sloth_rune", 12000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeSloth), new Item[] { BotaniaItems.runeAutumn, BotaniaItems.runeAir, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond }),
                new IndustrialAltarRecipe(1, "wrath_rune", 12000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeWrath), new Item[] { BotaniaItems.runeWinter, BotaniaItems.runeEarth, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond }),
                new IndustrialAltarRecipe(1, "envy_rune", 12000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeEnvy), new Item[] { BotaniaItems.runeWinter, BotaniaItems.runeWater, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond }),
                new IndustrialAltarRecipe(1, "pride_rune", 12000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runePride), new Item[] { BotaniaItems.runeSummer, BotaniaItems.runeFire, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond }),

                new IndustrialAltarRecipe(1, "midgar_runed", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:midgard_rune")), new Item[] { BotaniaItems.runeEarth, BotaniaItems.runeSpring, BotaniaItems.runeGreed, Items.GRASS_BLOCK, BotaniaItems.manaSteel }),
                new IndustrialAltarRecipe(1, "alfheim_rune", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:alfheim_rune")), new Item[] { BotaniaItems.runeAir, BotaniaItems.runeSummer, BotaniaItems.runeLust, Items.OAK_LEAVES, BotaniaItems.elementium }),
                new IndustrialAltarRecipe(1, "muspelheim_rune", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:muspelheim_rune")), new Item[] { BotaniaItems.runeFire, BotaniaItems.runeSummer, BotaniaItems.runeWrath, Items.MAGMA_BLOCK, Items.NETHER_BRICK }),
                new IndustrialAltarRecipe(1, "niflheim_rune", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:niflheim_rune")), new Item[] { BotaniaItems.runeWater, BotaniaItems.runeWinter, BotaniaItems.runeWrath, Items.BLUE_ICE, Items.IRON_INGOT }),
                new IndustrialAltarRecipe(1, "asgard_rune", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:asgard_rune")), new Item[] { BotaniaItems.runeAir, BotaniaItems.runeAutumn, BotaniaItems.runePride, BotaniaItems.rainbowRod, Items.NETHERITE_INGOT }),
                new IndustrialAltarRecipe(1, "vanaheim_rune", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:vanaheim_rune")), new Item[] { BotaniaItems.runeEarth, BotaniaItems.runeSpring, BotaniaItems.runePride, BotaniaBlocks.alfPortal.asItem(), BotaniaItems.terrasteel }),
                new IndustrialAltarRecipe(1, "helheim_rune", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:helheim_rune")), new Item[] { BotaniaItems.runeFire, BotaniaItems.runeAutumn, BotaniaItems.runeEnvy, Items.SKELETON_SKULL, Items.GOLD_INGOT }),
                new IndustrialAltarRecipe(1, "nidavellir_rune", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:nidavellir_rune")), new Item[] { BotaniaItems.runeEarth, BotaniaItems.runeWinter, BotaniaItems.runeSloth, Items.IRON_BLOCK, Items.COPPER_INGOT }),
                new IndustrialAltarRecipe(1, "joetunheim_rune", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:joetunheim_rune")), new Item[] { BotaniaItems.runeEarth, BotaniaItems.runeAutumn, BotaniaItems.runeGluttony, Items.BLACKSTONE, Items.BLACKSTONE }));

        for (IndustrialAltarRecipe recipe : IndustrialAltar) {
            var build = INDUSTRIAL_ALTAR_RECIPES.builder(recipe.id);
            build
                    .inputItems(recipe.input, 16)
                    .outputItems(recipe.output.copyWithCount(recipe.output.getCount() << 3))
                    .duration(300)
                    .circuitMeta(recipe.circuitMeta)
                    .MANAt(recipe.mana / 50);
            switch (recipe.inputs.length) {
                case 4 -> build
                        .inputItems(recipe.inputs[0])
                        .inputItems(recipe.inputs[1])
                        .inputItems(recipe.inputs[2])
                        .inputItems(recipe.inputs[3]);
                case 5 -> build
                        .inputItems(recipe.inputs[0])
                        .inputItems(recipe.inputs[1])
                        .inputItems(recipe.inputs[2])
                        .inputItems(recipe.inputs[3])
                        .inputItems(recipe.inputs[4]);
                case 6 -> build
                        .inputItems(recipe.inputs[0])
                        .inputItems(recipe.inputs[1])
                        .inputItems(recipe.inputs[2])
                        .inputItems(recipe.inputs[3])
                        .inputItems(recipe.inputs[4])
                        .inputItems(recipe.inputs[5]);
            }
            build.save(provider);
        }

        INDUSTRIAL_ALTAR_RECIPES.builder("runerock_block")
                .inputItems(BotaniaBlocks.livingrock.asItem(), 16)
                .outputItems(block, Runerock, 16)
                .duration(1200)
                .MANAt(4000)
                .inputItems(BotaniaItems.runeEarth, 4)
                .inputItems(BotaniaItems.runeAir, 4)
                .inputItems(BotaniaItems.runeFire, 4)
                .inputItems(BotaniaItems.runeWater, 4)
                .inputItems(BotaniaItems.runeSpring, 4)
                .inputItems(BotaniaItems.runeSummer, 4)
                .inputItems(BotaniaItems.runeAutumn, 4)
                .inputItems(BotaniaItems.runeWinter, 4)
                .inputItems(BotaniaItems.runeMana, 4)
                .inputItems(BotaniaItems.runeLust, 4)
                .inputItems(BotaniaItems.runeGluttony, 4)
                .inputItems(BotaniaItems.runeGreed, 4)
                .inputItems(BotaniaItems.runeSloth, 4)
                .inputItems(BotaniaItems.runeWrath, 4)
                .inputItems(BotaniaItems.runeEnvy, 4)
                .inputItems(BotaniaItems.runePride, 4)
                .save();

        INDUSTRIAL_ALTAR_RECIPES.builder("runerock_block_plas")
                .inputItems(BotaniaBlocks.livingrock.asItem(), 16)
                .outputItems(block, Runerock, 32)
                .duration(1200)
                .MANAt(4000)
                .inputItems("mythicbotany:asgard_rune", 4)
                .inputItems("mythicbotany:vanaheim_rune", 4)
                .inputItems("mythicbotany:alfheim_rune", 4)
                .inputItems("mythicbotany:midgard_rune", 4)
                .inputItems("mythicbotany:joetunheim_rune", 4)
                .inputItems("mythicbotany:muspelheim_rune", 4)
                .inputItems("mythicbotany:niflheim_rune", 4)
                .inputItems("mythicbotany:nidavellir_rune", 4)
                .inputItems("mythicbotany:helheim_rune", 4)
                .save();

        // 工业祭坛 - 花药台
        List<IndustrialAltarRecipe> IndustrialAltar2 = List.of(
                new IndustrialAltarRecipe(2, "pure_daisy_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.pureDaisy), new Item[] { BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.whitePetal }),
                new IndustrialAltarRecipe(2, "manastar_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.manastar), new Item[] { BotaniaItems.lightBluePetal, BotaniaItems.greenPetal, BotaniaItems.redPetal, BotaniaItems.cyanPetal }),
                new IndustrialAltarRecipe(2, "endoflame_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.endoflame), new Item[] { BotaniaItems.brownPetal, BotaniaItems.brownPetal, BotaniaItems.redPetal, BotaniaItems.lightGrayPetal }),
                new IndustrialAltarRecipe(2, "hydroangea_flowers", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.hydroangeas), new Item[] { BotaniaItems.bluePetal, BotaniaItems.bluePetal, BotaniaItems.cyanPetal, BotaniaItems.cyanPetal }),
                new IndustrialAltarRecipe(2, "thermalily_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.thermalily), new Item[] { BotaniaItems.redPetal, BotaniaItems.orangePetal, BotaniaItems.orangePetal, BotaniaItems.runeEarth, BotaniaItems.runeFire }),
                new IndustrialAltarRecipe(2, "rosa_arcana_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.rosaArcana), new Item[] { BotaniaItems.pinkPetal, BotaniaItems.pinkPetal, BotaniaItems.purplePetal, BotaniaItems.purplePetal, BotaniaItems.limePetal, BotaniaItems.runeMana }),
                new IndustrialAltarRecipe(2, "munchdew_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.munchdew), new Item[] { BotaniaItems.limePetal, BotaniaItems.limePetal, BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.greenPetal, BotaniaItems.runeGluttony }),
                new IndustrialAltarRecipe(3, "entropinnyum_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.entropinnyum), new Item[] { BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.grayPetal, BotaniaItems.grayPetal, BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.runeWrath, BotaniaItems.runeFire }),
                new IndustrialAltarRecipe(3, "kekimurus_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.kekimurus), new Item[] { BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.orangePetal, BotaniaItems.orangePetal, BotaniaItems.brownPetal, BotaniaItems.brownPetal, BotaniaItems.runeGluttony, BotaniaItems.pixieDust }),
                new IndustrialAltarRecipe(2, "gourmaryllis_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.gourmaryllis), new Item[] { BotaniaItems.lightGrayPetal, BotaniaItems.lightGrayPetal, BotaniaItems.yellowPetal, BotaniaItems.yellowPetal, BotaniaItems.redPetal, BotaniaItems.runeFire, BotaniaItems.runeSummer }),
                new IndustrialAltarRecipe(2, "narslimmus_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.narslimmus), new Item[] { BotaniaItems.limePetal, BotaniaItems.limePetal, BotaniaItems.greenPetal, BotaniaItems.greenPetal, BotaniaItems.blackPetal, BotaniaItems.runeSummer, BotaniaItems.runeWater }),
                new IndustrialAltarRecipe(3, "spectrolus_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.spectrolus), new Item[] { BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.greenPetal, BotaniaItems.greenPetal, BotaniaItems.bluePetal, BotaniaItems.bluePetal, BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.runeWinter, BotaniaItems.runeAir, BotaniaItems.pixieDust }),
                new IndustrialAltarRecipe(2, "rafflowsia_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.rafflowsia), new Item[] { BotaniaItems.purplePetal, BotaniaItems.purplePetal, BotaniaItems.greenPetal, BotaniaItems.greenPetal, BotaniaItems.blackPetal, BotaniaItems.runeEarth, BotaniaItems.runePride, BotaniaItems.pixieDust }),
                new IndustrialAltarRecipe(2, "shulk_me_not_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.shulkMeNot), new Item[] { BotaniaItems.purplePetal, BotaniaItems.purplePetal, BotaniaItems.magentaPetal, BotaniaItems.magentaPetal, BotaniaItems.lightGrayPetal, BotaniaItems.lifeEssence, BotaniaItems.runeEnvy, BotaniaItems.runeWrath }),
                new IndustrialAltarRecipe(2, "dandelifeon_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.dandelifeon), new Item[] { BotaniaItems.purplePetal, BotaniaItems.purplePetal, BotaniaItems.limePetal, BotaniaItems.greenPetal, BotaniaItems.runeWater, BotaniaItems.runeFire, BotaniaItems.runeEarth, BotaniaItems.runeAir, BotaniaItems.redstoneRoot, BotaniaItems.lifeEssence }),
                new IndustrialAltarRecipe(2, "jaded_amaranthus_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.jadedAmaranthus), new Item[] { BotaniaItems.purplePetal, BotaniaItems.limePetal, BotaniaItems.greenPetal, BotaniaItems.runeSpring, BotaniaItems.redstoneRoot }),
                new IndustrialAltarRecipe(2, "bellethorn_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.bellethorn), new Item[] { BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.cyanPetal, BotaniaItems.cyanPetal, BotaniaItems.redstoneRoot }),
                new IndustrialAltarRecipe(2, "dreadthorn_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.dreadthorn), new Item[] { BotaniaItems.blackPetal, BotaniaItems.blackPetal, BotaniaItems.blackPetal, BotaniaItems.cyanPetal, BotaniaItems.cyanPetal, BotaniaItems.redstoneRoot }),
                new IndustrialAltarRecipe(2, "heisei_dream_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.heiseiDream), new Item[] { BotaniaItems.magentaPetal, BotaniaItems.magentaPetal, BotaniaItems.purplePetal, BotaniaItems.pinkPetal, BotaniaItems.runeWrath, BotaniaItems.pixieDust }),
                new IndustrialAltarRecipe(2, "tigerseye_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.tigerseye), new Item[] { BotaniaItems.yellowPetal, BotaniaItems.brownPetal, BotaniaItems.orangePetal, BotaniaItems.limePetal, BotaniaItems.runeAutumn }),
                new IndustrialAltarRecipe(2, "orechid_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.orechid), new Item[] { BotaniaItems.grayPetal, BotaniaItems.grayPetal, BotaniaItems.yellowPetal, BotaniaItems.greenPetal, BotaniaItems.redPetal, BotaniaItems.runePride, BotaniaItems.runeGreed, BotaniaItems.redstoneRoot, BotaniaItems.pixieDust }),
                new IndustrialAltarRecipe(3, "orechid_ignem_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.orechidIgnem), new Item[] { BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.pinkPetal, BotaniaItems.runePride, BotaniaItems.runeGreed, BotaniaItems.redstoneRoot, BotaniaItems.pixieDust }),
                new IndustrialAltarRecipe(3, "fallen_kanade_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.fallenKanade), new Item[] { BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.yellowPetal, BotaniaItems.yellowPetal, BotaniaItems.orangePetal, BotaniaItems.runeSpring }),
                new IndustrialAltarRecipe(2, "exoflame_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.exoflame), new Item[] { BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.grayPetal, BotaniaItems.lightGrayPetal, BotaniaItems.runeFire, BotaniaItems.runeSummer }),
                new IndustrialAltarRecipe(2, "agricarnation_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.agricarnation), new Item[] { BotaniaItems.limePetal, BotaniaItems.limePetal, BotaniaItems.greenPetal, BotaniaItems.yellowPetal, BotaniaItems.runeSpring, BotaniaItems.redstoneRoot }),
                new IndustrialAltarRecipe(2, "hopperhock_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.hopperhock), new Item[] { BotaniaItems.grayPetal, BotaniaItems.grayPetal, BotaniaItems.lightGrayPetal, BotaniaItems.lightGrayPetal, BotaniaItems.runeAir, BotaniaItems.redstoneRoot }),
                new IndustrialAltarRecipe(3, "tangleberrie_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.tangleberrie), new Item[] { BotaniaItems.cyanPetal, BotaniaItems.cyanPetal, BotaniaItems.grayPetal, BotaniaItems.lightGrayPetal, BotaniaItems.runeAir, BotaniaItems.runeEarth }),
                new IndustrialAltarRecipe(2, "jiyuulia_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.jiyuulia), new Item[] { BotaniaItems.pinkPetal, BotaniaItems.pinkPetal, BotaniaItems.purplePetal, BotaniaItems.lightGrayPetal, BotaniaItems.runeWater, BotaniaItems.runeAir }),
                new IndustrialAltarRecipe(2, "rannuncarpus_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.rannuncarpus), new Item[] { BotaniaItems.orangePetal, BotaniaItems.orangePetal, BotaniaItems.yellowPetal, BotaniaItems.runeEarth, BotaniaItems.redstoneRoot }),
                new IndustrialAltarRecipe(2, "hyacidu_flowers", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.hyacidus), new Item[] { BotaniaItems.purplePetal, BotaniaItems.purplePetal, BotaniaItems.magentaPetal, BotaniaItems.magentaPetal, BotaniaItems.greenPetal, BotaniaItems.runeWater, BotaniaItems.runeAutumn, BotaniaItems.redstoneRoot }),
                new IndustrialAltarRecipe(2, "pollidisiac_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.pollidisiac), new Item[] { BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.pinkPetal, BotaniaItems.pinkPetal, BotaniaItems.orangePetal, BotaniaItems.runeLust, BotaniaItems.runeFire }),
                new IndustrialAltarRecipe(2, "clayconia_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.clayconia), new Item[] { BotaniaItems.lightGrayPetal, BotaniaItems.lightGrayPetal, BotaniaItems.grayPetal, BotaniaItems.cyanPetal, BotaniaItems.runeEarth }),
                new IndustrialAltarRecipe(2, "loonium_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.loonium), new Item[] { BotaniaItems.greenPetal, BotaniaItems.greenPetal, BotaniaItems.greenPetal, BotaniaItems.greenPetal, BotaniaItems.grayPetal, BotaniaItems.runeSloth, BotaniaItems.runeGluttony, BotaniaItems.runeEnvy, BotaniaItems.redstoneRoot, BotaniaItems.pixieDust }),
                new IndustrialAltarRecipe(3, "daffomill_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.daffomill), new Item[] { BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.brownPetal, BotaniaItems.yellowPetal, BotaniaItems.runeAir, BotaniaItems.redstoneRoot }),
                new IndustrialAltarRecipe(2, "vinculotus_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.vinculotus), new Item[] { BotaniaItems.blackPetal, BotaniaItems.blackPetal, BotaniaItems.purplePetal, BotaniaItems.purplePetal, BotaniaItems.greenPetal, BotaniaItems.runeWater, BotaniaItems.runeSloth, BotaniaItems.runeLust, BotaniaItems.redstoneRoot }),
                new IndustrialAltarRecipe(3, "spectranthemum_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.spectranthemum), new Item[] { BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.lightGrayPetal, BotaniaItems.lightGrayPetal, BotaniaItems.cyanPetal, BotaniaItems.runeEnvy, BotaniaItems.runeWater, BotaniaItems.redstoneRoot, BotaniaItems.pixieDust }),
                new IndustrialAltarRecipe(2, "medumone_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.medumone), new Item[] { BotaniaItems.brownPetal, BotaniaItems.brownPetal, BotaniaItems.grayPetal, BotaniaItems.grayPetal, BotaniaItems.runeEarth, BotaniaItems.redstoneRoot }),
                new IndustrialAltarRecipe(2, "marimorphosis_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.marimorphosis), new Item[] { BotaniaItems.grayPetal, BotaniaItems.yellowPetal, BotaniaItems.greenPetal, BotaniaItems.redPetal, BotaniaItems.runeEarth, BotaniaItems.runeFire, BotaniaItems.redstoneRoot }),
                new IndustrialAltarRecipe(3, "bubbell_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.bubbell), new Item[] { BotaniaItems.cyanPetal, BotaniaItems.cyanPetal, BotaniaItems.lightBluePetal, BotaniaItems.lightBluePetal, BotaniaItems.bluePetal, BotaniaItems.bluePetal, BotaniaItems.runeWater, BotaniaItems.runeSummer, BotaniaItems.pixieDust }),
                new IndustrialAltarRecipe(2, "solegnolia_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.solegnolia), new Item[] { BotaniaItems.brownPetal, BotaniaItems.brownPetal, BotaniaItems.redPetal, BotaniaItems.bluePetal, BotaniaItems.redstoneRoot }),
                new IndustrialAltarRecipe(2, "bergamute_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.bergamute), new Item[] { BotaniaItems.orangePetal, BotaniaItems.greenPetal, BotaniaItems.greenPetal, BotaniaItems.redstoneRoot }),
                new IndustrialAltarRecipe(3, "labellia_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.labellia), new Item[] { BotaniaItems.yellowPetal, BotaniaItems.yellowPetal, BotaniaItems.bluePetal, BotaniaItems.whitePetal, BotaniaItems.blackPetal, BotaniaItems.runeAutumn, BotaniaItems.redstoneRoot, BotaniaItems.pixieDust }),
                new IndustrialAltarRecipe(2, "motif_daybloom_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaBlocks.motifDaybloom), new Item[] { BotaniaItems.yellowPetal, BotaniaItems.yellowPetal, BotaniaItems.orangePetal, BotaniaItems.lightBluePetal }),
                new IndustrialAltarRecipe(2, "motif_nightshade_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaBlocks.motifNightshade), new Item[] { BotaniaItems.blackPetal, BotaniaItems.blackPetal, BotaniaItems.purplePetal, BotaniaItems.grayPetal }));

        for (IndustrialAltarRecipe recipe : IndustrialAltar2) {
            var build = INDUSTRIAL_ALTAR_RECIPES.builder(recipe.id);
            build
                    .inputItems(ForgeTags.SEEDS, 8)
                    .inputFluids(Water.getFluid(8000))
                    .outputItems(recipe.output, 8)
                    .duration(20)
                    .circuitMeta(recipe.circuitMeta)
                    .MANAt(32);
            for (int i = 0; i < recipe.inputs.length; i++) build.inputItems(recipe.inputs[i], 2);
            build.save(provider);
        }

        INDUSTRIAL_ALTAR_RECIPES.builder("colorful_mystical_flower")
                .inputItems(ForgeTags.SEEDS, 8)
                .inputFluids(Water.getFluid(8000))
                .outputItems(COLORFUL_MYSTICAL_FLOWER, 8)
                .duration(20)
                .MANAt(32)
                .inputItems(BotaniaItems.whitePetal, 8)
                .inputItems(BotaniaItems.lightGrayPetal, 8)
                .inputItems(BotaniaItems.grayPetal, 8)
                .inputItems(BotaniaItems.blackPetal, 8)
                .inputItems(BotaniaItems.brownPetal, 8)
                .inputItems(BotaniaItems.redPetal, 8)
                .inputItems(BotaniaItems.orangePetal, 8)
                .inputItems(BotaniaItems.yellowPetal, 8)
                .inputItems(BotaniaItems.limePetal, 8)
                .inputItems(BotaniaItems.greenPetal, 8)
                .inputItems(BotaniaItems.cyanPetal, 8)
                .inputItems(BotaniaItems.lightBluePetal, 8)
                .inputItems(BotaniaItems.bluePetal, 8)
                .inputItems(BotaniaItems.purplePetal, 8)
                .inputItems(BotaniaItems.magentaPetal, 8)
                .inputItems(BotaniaItems.pinkPetal, 8)
                .save();

        // 魔力凝聚

        String[] EndremEyes = {
                "black_eye", "cold_eye", "corrupted_eye", "lost_eye",
                "nether_eye", "old_eye", "rogue_eye", "cursed_eye",
                "guardian_eye", "magical_eye", "wither_eye"
        };
        String[] EndremEyes_input = {
                "minecraft:sculk_catalyst", "ad_astra:ice_shard", "enderio:plant_matter_brown", "enderio:redstone_alloy_grinding_ball",
                "botania:quartz_blaze", "botania:forest_eye", "botania:redstone_root", "botania:life_essence",
                "minecraft:prismarine_crystals", "botania:mana_bottle", "enderio:withering_powder"
        };

        for (int i = 0; i < EndremEyes.length; i++) {
            MANA_CONDENSER_RECIPES.builder(EndremEyes[i])
                    .inputItems(GTItems.QUANTUM_EYE.asStack())
                    .inputItems(EndremEyes_input[i])
                    .outputItems("endrem:" + EndremEyes[i])
                    .duration(200)
                    .MANAt(128)
                    .save();
        }

        MANA_CONDENSER_RECIPES.builder("enriched_naquadah_trinium_europium_duranide")
                .inputItems(GTOTagPrefix.SUPERCONDUCTOR_BASE, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide, 4)
                .outputItems(TagPrefix.wireGtSingle, GTMaterials.EnrichedNaquadahTriniumEuropiumDuranide, 4)
                .inputFluids(GTOMaterials.Aether.getFluid(1000))
                .duration(80)
                .MANAt(2048)
                .save();

        MANA_CONDENSER_RECIPES.builder("ruthenium_trinium_americium_neutronate")
                .inputItems(GTOTagPrefix.SUPERCONDUCTOR_BASE, GTMaterials.RutheniumTriniumAmericiumNeutronate, 4)
                .outputItems(TagPrefix.wireGtSingle, GTMaterials.RutheniumTriniumAmericiumNeutronate, 4)
                .inputFluids(GTOMaterials.Aether.getFluid(1000))
                .duration(80)
                .MANAt(8192)
                .save();

        MANA_CONDENSER_RECIPES.builder("nether_star")
                .chancedInput("mythicbotany:helheim_rune", 9500, 100)
                .inputItems("botania:quartz_elven", 4)
                .inputItems(dust, GTMaterials.Iridium)
                .outputItems(gem, GTMaterials.NetherStar, 2)
                .inputFluids(GTMaterials.NetherAir, 4000)
                .duration(200)
                .MANAt(512)
                .save();

        MANA_CONDENSER_RECIPES.builder("terrasteel_ingot")
                .inputItems(ingot, GTOMaterials.Manasteel)
                .inputItems("botania:mana_pearl")
                .inputItems(gem, GTOMaterials.ManaDiamond)
                .outputItems(ingot, GTOMaterials.Terrasteel, 3)
                .inputFluids(GTOMaterials.Terrasteel, 144)
                .MANAt(512)
                .duration(400)
                .save();

        MANA_CONDENSER_RECIPES.builder("thaumium_ingot")
                .inputItems(ingot, Livingsteel)
                .inputItems(ItemsRegistry.SOURCE_GEM)
                .inputItems(ingot, OriginalBronze)
                .inputItems(ItemsRegistry.MANIPULATION_ESSENCE)
                .outputItems(ingot, GTOMaterials.Thaumium, 3)
                .inputFluids(GTOMaterials.Thaumium, 144)
                .MANAt(512)
                .duration(400)
                .save();

        MANA_CONDENSER_RECIPES.builder("alfsteel_ingot")
                .inputItems(BotaniaItems.elementium)
                .inputItems(BotaniaItems.pixieDust)
                .inputItems(BotaniaItems.dragonstone)
                .outputItems(ingot, Alfsteel, 3)
                .inputFluids(GTOMaterials.Alfsteel, 144)
                .MANAt(1536)
                .duration(400)
                .save();

        MANA_CONDENSER_RECIPES.builder("gaiasteel_ingot")
                .inputItems(RegistriesUtils.getItemStack("mythicbotany:asgard_rune"))
                .inputItems(RegistriesUtils.getItemStack("mythicbotany:vanaheim_rune"))
                .inputItems(RegistriesUtils.getItemStack("mythicbotany:alfheim_rune"))
                .inputItems(RegistriesUtils.getItemStack("mythicbotany:midgard_rune"))
                .inputItems(RegistriesUtils.getItemStack("mythicbotany:joetunheim_rune"))
                .inputItems(RegistriesUtils.getItemStack("mythicbotany:muspelheim_rune"))
                .inputItems(RegistriesUtils.getItemStack("mythicbotany:niflheim_rune"))
                .inputItems(RegistriesUtils.getItemStack("mythicbotany:nidavellir_rune"))
                .inputItems(RegistriesUtils.getItemStack("mythicbotany:helheim_rune"))
                .inputItems(ingot, Alfsteel, 3)
                .inputItems(ingot, Runerock, 3)
                .outputItems(ingot, Gaiasteel, 9)
                .inputFluids(GTOMaterials.Gaiasteel, 576)
                .MANAt(2560)
                .duration(400)
                .save();

        MANA_CONDENSER_RECIPES.builder("gaia_ingot")
                .inputItems(ingot, Gaiasteel, 2)
                .inputItems(BotaniaItems.lifeEssence, 2)
                .outputItems(ingot, Gaia, 6)
                .inputFluids(GTOMaterials.Gaia, 288)
                .MANAt(5120)
                .duration(400)
                .save();

        MANA_CONDENSER_RECIPES.builder("aerialite_ingot")
                .inputItems(Items.PHANTOM_MEMBRANE.asItem())
                .inputItems("botania:ender_air_bottle")
                .inputItems(gem, Dragonstone)
                .outputItems(ingot, Aerialite, 1)
                .MANAt(256)
                .duration(100)
                .save();
    }
}
