package com.gtocore.data.recipe.magic;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.data.recipe.builder.botania.*;
import com.gtocore.data.tag.Tags;

import com.gtolib.api.data.chemical.GTOChemicalHelper;
import com.gtolib.utils.RegistriesUtils;
import com.gtolib.utils.TagUtils;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.utils.collection.O2IOpenCacheHashMap;

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
import io.github.lounode.extrabotany.common.block.ExtraBotanyBlocks;
import io.github.lounode.extrabotany.common.block.flower.ExtrabotanyFlowerBlocks;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import io.github.lounode.extrabotany.common.lib.ExtraBotanyTags;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.brew.BotaniaBrews;
import vazkii.botania.common.crafting.StateIngredientHelper;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import vectorwing.farmersdelight.common.tag.ForgeTags;

import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.api.data.tag.GTOTagPrefix.SUPERCONDUCTOR_BASE;
import static com.gtocore.common.data.GTOItems.COLORFUL_MYSTICAL_FLOWER;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;
import static vazkii.botania.common.block.BotaniaBlocks.motifDaybloom;

public final class BotaniaRecipes {

    public static void init() {
        // 植物魔法系列测试配方
        if (GTCEu.isDev()) {
            // 植物酿造 - 好像只能酿造植物魔法的那些
            BrewRecipeBuilder.builder("iron100")
                    .brew(BotaniaBrews.clear)
                    .addIngredient(ItemTags.SMALL_FLOWERS)
                    .addIngredient(Items.DIAMOND_BLOCK)
                    .addIngredient(BotaniaTags.Items.RUNES)
                    .save();
        }

        // 白雏菊
        {
            PureDaisyRecipe("livingclay", StateIngredientHelper.of(BlockTags.DIRT), ChemicalHelper.getBlock(block, Livingclay));
            PureDaisyRecipe("livingwood", StateIngredientHelper.of(Tags.ARCHWOOD_LOG), BotaniaBlocks.livingwoodLog);

        }

        // 魔力池
        {
            ManaInfusionRecipe("pulsating", Ingredient.of(GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, PulsatingAlloy)), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, PulsatingAlloy)), 400, null, null);
            ManaInfusionRecipe("conductivee", Ingredient.of(GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, ConductiveAlloy)), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, ConductiveAlloy)), 1600, null, null);
            ManaInfusionRecipe("energeticalloy", Ingredient.of(GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, EnergeticAlloy)), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, EnergeticAlloy)), 6000, null, null);
            ManaInfusionRecipe("vibrantalloy", Ingredient.of(GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, VibrantAlloy)), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, VibrantAlloy)), 25600, null, null);
            ManaInfusionRecipe("endsteel", Ingredient.of(GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, EndSteel)), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, EndSteel)), 102400, null, null);

            ManaInfusionRecipe("manasteel", Ingredient.of(GTOChemicalHelper.getItem(ingot, Steel)), new ItemStack(GTOChemicalHelper.getItem(ingot, Manasteel)), 3000, null, null);
            ManaInfusionRecipe("manasteel_block", Ingredient.of(GTOChemicalHelper.getItem(block, Steel)), new ItemStack(GTOChemicalHelper.getItem(block, Manasteel)), 27000, null, null);
            ManaInfusionRecipe("mana_pearl", Ingredient.of(RegistriesUtils.getItem("torchmaster:frozen_pearl")), new ItemStack(BotaniaItems.manaPearl), 6000, null, null);
            ManaInfusionRecipe("mana_string", Ingredient.of(ItemsRegistry.MAGE_FIBER.asItem()), new ItemStack(BotaniaItems.manaString), 1250, null, null);

            ManaInfusionRecipe("life_essence", Ingredient.of(GTOItems.UNSTABLE_GAIA_SOUL.asItem()), new ItemStack(BotaniaItems.lifeEssence), 500000, ChemicalHelper.getBlock(block, Gaia), null);

            ManaInfusionRecipe("infused_gold", Ingredient.of(Items.GOLD_INGOT), new ItemStack(GTOChemicalHelper.getItem(ingot, InfusedGold)), 8000, null, null);
            ManaInfusionRecipe("original_bronze_dust", Ingredient.of(GTOChemicalHelper.getItem(ingot, Bronze)), new ItemStack(GTOChemicalHelper.getItem(ingot, OriginalBronze)), 6000, null, null);

        }

        // 花药台
        {
            ApothecaryRecipe("colorful_mystical_flower", Ingredient.of(ForgeTags.SEEDS), new ItemStack(COLORFUL_MYSTICAL_FLOWER),
                    new Ingredient[] { Ingredient.of(BotaniaItems.whitePetal), Ingredient.of(BotaniaItems.lightGrayPetal), Ingredient.of(BotaniaItems.grayPetal), Ingredient.of(BotaniaItems.blackPetal),
                            Ingredient.of(BotaniaItems.brownPetal), Ingredient.of(BotaniaItems.redPetal), Ingredient.of(BotaniaItems.orangePetal), Ingredient.of(BotaniaItems.yellowPetal),
                            Ingredient.of(BotaniaItems.limePetal), Ingredient.of(BotaniaItems.greenPetal), Ingredient.of(BotaniaItems.cyanPetal), Ingredient.of(BotaniaItems.lightBluePetal),
                            Ingredient.of(BotaniaItems.bluePetal), Ingredient.of(BotaniaItems.purplePetal), Ingredient.of(BotaniaItems.magentaPetal), Ingredient.of(BotaniaItems.pinkPetal) });

        }

        // 符文祭坛
        {
            RunicAltarRecipe("runerock_block", 1000000, new ItemStack(GTOChemicalHelper.getItem(block, Runerock), 8), false,
                    new Ingredient[] { Ingredient.of(BotaniaItems.runeEarth), Ingredient.of(BotaniaItems.runeAir), Ingredient.of(BotaniaItems.runeFire), Ingredient.of(BotaniaItems.runeWater),
                            Ingredient.of(BotaniaItems.runeSpring), Ingredient.of(BotaniaItems.runeSummer), Ingredient.of(BotaniaItems.runeAutumn), Ingredient.of(BotaniaItems.runeWinter),
                            Ingredient.of(BotaniaItems.runeMana), Ingredient.of(BotaniaItems.runeLust), Ingredient.of(BotaniaItems.runeGluttony), Ingredient.of(BotaniaItems.runeGreed),
                            Ingredient.of(BotaniaItems.runeSloth), Ingredient.of(BotaniaItems.runeWrath), Ingredient.of(BotaniaItems.runeEnvy), Ingredient.of(BotaniaItems.runePride) });
            RunicAltarRecipe("runerock_block_plas", 1000000, new ItemStack(GTOChemicalHelper.getItem(block, Runerock), 16), false,
                    new Ingredient[] { Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:asgard_rune")), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:vanaheim_rune")),
                            Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfheim_rune")), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:midgard_rune")),
                            Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:joetunheim_rune")), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:muspelheim_rune")),
                            Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:niflheim_rune")), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:nidavellir_rune")),
                            Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:helheim_rune")) });

            RunicAltarRecipe("transmutation_catalyst", 10000000, new ItemStack(GTOBlocks.TRANSMUTATION_CATALYST), false,
                    new Ingredient[] { Ingredient.of(GTOItems.PHILOSOPHERS_STONE), Ingredient.of(GTOItems.PHILOSOPHERS_STONE), Ingredient.of(GTOItems.PHILOSOPHERS_STONE),
                            Ingredient.of(GTOItems.PHILOSOPHERS_STONE), Ingredient.of(GTOItems.PHILOSOPHERS_STONE), Ingredient.of(GTOItems.PHILOSOPHERS_STONE) });
        }

        // 泰拉凝聚板
        {
            TAgglomerationRecipe("thaumium_ingot", 500000, new ItemStack(GTOChemicalHelper.getItem(ingot, Thaumium)),
                    new Ingredient[] { Ingredient.of(GTOChemicalHelper.getItem(ingot, Livingsteel)), Ingredient.of(ItemsRegistry.SOURCE_GEM), Ingredient.of(GTOChemicalHelper.getItem(ingot, OriginalBronze)), Ingredient.of(ItemsRegistry.MANIPULATION_ESSENCE) });
            TAgglomerationRecipe("gaiasteel_ingot", 2500000, new ItemStack(GTOChemicalHelper.getItem(ingot, Gaiasteel), 3),
                    new Ingredient[] { Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:asgard_rune")), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:vanaheim_rune")),
                            Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:alfheim_rune")), Ingredient.of(GTOChemicalHelper.getItem(ingot, Alfsteel)),
                            Ingredient.of(GTOChemicalHelper.getItem(ingot, Runerock)), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:midgard_rune")),
                            Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:joetunheim_rune")), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:muspelheim_rune")),
                            Ingredient.of(GTOChemicalHelper.getItem(ingot, Alfsteel)), Ingredient.of(GTOChemicalHelper.getItem(ingot, Runerock)),
                            Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:niflheim_rune")), Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:nidavellir_rune")),
                            Ingredient.of(RegistriesUtils.getItemStack("mythicbotany:helheim_rune")), Ingredient.of(GTOChemicalHelper.getItem(ingot, Alfsteel)),
                            Ingredient.of(GTOChemicalHelper.getItem(ingot, Runerock)) });
            TAgglomerationRecipe("gaia_ingot", 5000000, new ItemStack(GTOChemicalHelper.getItem(ingot, Gaia), 2),
                    new Ingredient[] { Ingredient.of(GTOChemicalHelper.getItem(ingot, Gaiasteel)), Ingredient.of(BotaniaItems.lifeEssence), Ingredient.of(GTOChemicalHelper.getItem(ingot, Gaiasteel)), Ingredient.of(BotaniaItems.lifeEssence) });

        }

        // 精灵门
        {
            ElvenTradeRecipeBuilder.builder("dragonstone")
                    .addInput(ItemsRegistry.SOURCE_GEM)
                    .addOutput(BotaniaItems.dragonstone)
                    .save();

            ElvenTradeRecipeBuilder.builder("dragonstone_block")
                    .addInput(BlockRegistry.SOURCE_GEM_BLOCK)
                    .addOutput(BotaniaBlocks.dragonstoneBlock)
                    .save();

            ElvenTradeRecipeBuilder.builder("colorful_mystical_flower")
                    .addInput(BotaniaItems.fertilizer)
                    .addOutput(COLORFUL_MYSTICAL_FLOWER)
                    .save();
        }

        // 凝矿兰
        {
            // Material组
            Material[][] materials = new Material[][] {
                    new Material[] { GTMaterials.Iron, GTMaterials.CassiteriteSand, GTMaterials.Galena, GTMaterials.GarnetRed, GTMaterials.Goethite, GTMaterials.Magnetite, GTMaterials.Gypsum, GTMaterials.Hematite, GTMaterials.Spessartine, GTMaterials.Silver, GTMaterials.Tin, GTMaterials.Gold, GTMaterials.Mica, GTMaterials.Lazurite, GTMaterials.Graphite, GTMaterials.TricalciumPhosphate, GTMaterials.Sodalite, GTMaterials.Kyanite, GTMaterials.Tantalite, GTMaterials.FullersEarth, GTMaterials.GarnetSand, GTMaterials.Cobaltite, GTMaterials.Diamond, GTMaterials.Ruby, GTMaterials.Apatite, GTMaterials.Garnierite, GTOMaterials.SalamanderCrystal, GTMaterials.Almandine, GTMaterials.Olivine, GTMaterials.Pyrochlore, GTMaterials.Realgar, GTMaterials.RockSalt, GTMaterials.Cassiterite, GTMaterials.Grossular, GTMaterials.Sapphire, GTMaterials.Coal, GTMaterials.Cinnabar, GTMaterials.Chalcopyrite, GTMaterials.YellowLimonite, GTMaterials.Lepidolite, GTOMaterials.PerditioCrystal, GTMaterials.Zeolite, GTMaterials.Redstone, GTMaterials.Pyrite, GTOMaterials.GnomeCrystal, GTMaterials.VanadiumMagnetite, GTMaterials.Pentlandite, GTMaterials.Amethyst, GTMaterials.Lapis, GTOMaterials.SylphCrystal, GTMaterials.GreenSapphire, GTMaterials.Soapstone, GTMaterials.Pyrope, GTMaterials.Bentonite, GTMaterials.Pollucite, GTMaterials.Talc, GTMaterials.Salt, GTMaterials.GarnetYellow, GTOMaterials.UndineCrystal, GTMaterials.Calcite, GTMaterials.Oilsands, GTMaterials.GraniticMineralSand, GTMaterials.Malachite, GTMaterials.Pyrolusite, GTMaterials.Opal, GTMaterials.Diatomite, GTMaterials.Asbestos, GTMaterials.BasalticMineralSand, GTMaterials.Nickel, GTMaterials.Spodumene, GTMaterials.GlauconiteSand, GTMaterials.Copper, GTMaterials.Lead },
                    new Material[] { GTMaterials.Barite, GTMaterials.Iron, GTMaterials.Lepidolite, GTMaterials.CertusQuartz, GTMaterials.Chalcocite, GTMaterials.Tetrahedrite, GTMaterials.Spessartine, GTMaterials.Bornite, GTMaterials.Sphalerite, GTMaterials.Alunite, GTMaterials.Topaz, GTMaterials.Powellite, GTMaterials.Lazurite, GTMaterials.NetherQuartz, GTMaterials.Stibnite, GTMaterials.Sodalite, GTMaterials.Tantalite, GTMaterials.Pyrite, GTMaterials.Saltpeter, GTMaterials.Pentlandite, GTMaterials.Lapis, GTMaterials.GreenSapphire, GTMaterials.Cobaltite, GTMaterials.Wulfenite, GTMaterials.Pyrope, GTMaterials.Garnierite, GTMaterials.Salt, GTMaterials.Quartzite, GTMaterials.Beryllium, GTMaterials.Almandine, GTMaterials.Calcite, GTMaterials.RockSalt, GTMaterials.Electrotine, GTMaterials.Pyrolusite, GTMaterials.Diatomite, GTMaterials.Molybdenum, GTMaterials.BlueTopaz, GTMaterials.Grossular, GTMaterials.Nickel, GTMaterials.Sapphire, GTMaterials.Molybdenite, GTMaterials.Spodumene, GTMaterials.Emerald, GTMaterials.Sulfur, GTMaterials.Copper, GTMaterials.Chalcopyrite },
                    new Material[] { GTMaterials.Barite, GTMaterials.Sulfur, GTMaterials.YellowLimonite, GTMaterials.CertusQuartz, GTMaterials.Chalcocite, GTMaterials.Tetrahedrite, GTMaterials.Spessartine, GTMaterials.Hematite, GTMaterials.Goethite, GTMaterials.Bornite, GTMaterials.Sphalerite, GTMaterials.Powellite, GTMaterials.Alunite, GTMaterials.Redstone, GTMaterials.Gold, GTMaterials.Topaz, GTMaterials.NetherQuartz, GTMaterials.Stibnite, GTMaterials.Tantalite, GTMaterials.Pyrite, GTMaterials.Saltpeter, GTMaterials.Wulfenite, GTMaterials.Ruby, GTMaterials.Quartzite, GTMaterials.Pyrolusite, GTMaterials.Diatomite, GTMaterials.Electrotine, GTMaterials.Grossular, GTMaterials.Beryllium, GTMaterials.BlueTopaz, GTMaterials.Molybdenum, GTMaterials.Molybdenite, GTMaterials.Emerald, GTMaterials.Cinnabar, GTMaterials.Copper },
                    new Material[] { GTMaterials.GarnetSand, GTMaterials.Uraninite, GTMaterials.Monazite, GTMaterials.Ilmenite, GTMaterials.CassiteriteSand, GTMaterials.Magnetite, GTMaterials.Aluminium, GTMaterials.Talc, GTMaterials.Tin, GTMaterials.Soapstone, GTMaterials.Gold, GTMaterials.Bauxite, GTMaterials.Diatomite, GTMaterials.Cassiterite, GTMaterials.Asbestos, GTMaterials.Neodymium, GTMaterials.Pitchblende, GTMaterials.Pentlandite, GTMaterials.VanadiumMagnetite, GTMaterials.Bastnasite, GTMaterials.GlauconiteSand },
                    new Material[] { GTMaterials.Iron, GTMaterials.YellowLimonite, GTMaterials.Tetrahedrite, GTMaterials.GarnetRed, GTMaterials.Goethite, GTMaterials.Magnetite, GTMaterials.Gypsum, GTMaterials.Hematite, GTMaterials.Bornite, GTMaterials.Palladium, GTMaterials.Alunite, GTMaterials.Gold, GTMaterials.Mica, GTMaterials.Stibnite, GTMaterials.TricalciumPhosphate, GTMaterials.Kyanite, GTMaterials.Pyrite, GTMaterials.Scheelite, GTMaterials.FullersEarth, GTMaterials.VanadiumMagnetite, GTMaterials.Saltpeter, GTMaterials.Amethyst, GTMaterials.Lithium, GTMaterials.Platinum, GTMaterials.Tungstate, GTMaterials.Pollucite, GTMaterials.GarnetYellow, GTMaterials.Cooperite, GTMaterials.Apatite, GTMaterials.Pyrochlore, GTMaterials.GraniticMineralSand, GTMaterials.Opal, GTMaterials.Malachite, GTMaterials.Diatomite, GTMaterials.Electrotine, GTMaterials.BasalticMineralSand, GTMaterials.Copper, GTMaterials.Chalcopyrite },
                    new Material[] { GTOMaterials.Desh, GTMaterials.Sulfur, GTMaterials.YellowLimonite, GTMaterials.Galena, GTMaterials.Magnetite, GTMaterials.Hematite, GTMaterials.Goethite, GTMaterials.Silver, GTMaterials.Powellite, GTMaterials.Sphalerite, GTMaterials.Gold, GTMaterials.Chromite, GTMaterials.Graphite, GTMaterials.Pyrite, GTMaterials.VanadiumMagnetite, GTMaterials.Wulfenite, GTMaterials.Diamond, GTMaterials.Bentonite, GTMaterials.Olivine, GTMaterials.Molybdenite, GTMaterials.Magnesite, GTMaterials.Molybdenum, GTMaterials.Coal, GTMaterials.GlauconiteSand, GTMaterials.Lead },
                    new Material[] { GTMaterials.Chalcocite, GTMaterials.Spessartine, GTOMaterials.Calorite, GTMaterials.Bornite, GTMaterials.Palladium, GTMaterials.Zeolite, GTMaterials.Alunite, GTMaterials.Topaz, GTMaterials.Tantalite, GTMaterials.Saltpeter, GTMaterials.Pentlandite, GTMaterials.Platinum, GTMaterials.Cobaltite, GTMaterials.Garnierite, GTMaterials.Cooperite, GTMaterials.Pyrochlore, GTMaterials.Realgar, GTMaterials.Pyrolusite, GTMaterials.Diatomite, GTMaterials.Electrotine, GTMaterials.Cassiterite, GTMaterials.Grossular, GTMaterials.Nickel, GTMaterials.BlueTopaz, GTMaterials.Cobalt, GTMaterials.Chalcopyrite },
                    new Material[] { GTMaterials.Monazite, GTMaterials.YellowLimonite, GTMaterials.Magnetite, GTMaterials.Hematite, GTMaterials.Goethite, GTMaterials.Spessartine, GTMaterials.Zeolite, GTMaterials.Gold, GTMaterials.Neodymium, GTMaterials.Tantalite, GTMaterials.Scheelite, GTMaterials.Bastnasite, GTMaterials.Pentlandite, GTMaterials.Lithium, GTMaterials.Tungstate, GTMaterials.Bentonite, GTMaterials.Talc, GTMaterials.Olivine, GTMaterials.Soapstone, GTMaterials.Realgar, GTMaterials.Malachite, GTMaterials.Pyrolusite, GTOMaterials.Ostrum, GTMaterials.Cassiterite, GTMaterials.Grossular, GTMaterials.GlauconiteSand, GTMaterials.Chalcopyrite },
                    new Material[] { GTMaterials.Sulfur, GTMaterials.YellowLimonite, GTMaterials.Naquadah, GTMaterials.Hematite, GTMaterials.Plutonium239, GTMaterials.Magnetite, GTMaterials.Gypsum, GTMaterials.Goethite, GTMaterials.Sphalerite, GTMaterials.Powellite, GTMaterials.Graphite, GTMaterials.Pyrite, GTMaterials.FullersEarth, GTMaterials.Diamond, GTOMaterials.Celestine, GTMaterials.Wulfenite, GTMaterials.Bentonite, GTMaterials.Cooperite, GTMaterials.Olivine, GTMaterials.GraniticMineralSand, GTMaterials.Malachite, GTMaterials.BasalticMineralSand, GTMaterials.Molybdenite, GTMaterials.Molybdenum, GTMaterials.Coal, GTMaterials.Trona, GTMaterials.GlauconiteSand },
                    new Material[] { GTMaterials.Cobaltite, GTMaterials.Ilmenite, GTMaterials.Pollucite, GTMaterials.Aluminium, GTMaterials.Talc, GTMaterials.Garnierite, GTMaterials.Zeolite, GTMaterials.Tin, GTMaterials.Soapstone, GTMaterials.Realgar, GTMaterials.Bauxite, GTMaterials.Mica, GTMaterials.Cassiterite, GTMaterials.Kyanite, GTMaterials.Nickel, GTMaterials.Beryllium, GTMaterials.Emerald, GTMaterials.Pentlandite, GTMaterials.GlauconiteSand, GTMaterials.Chalcopyrite },
                    new Material[] { GTMaterials.Ruby, GTMaterials.Iron, GTMaterials.Platinum, GTMaterials.Galena, GTMaterials.Wulfenite, GTMaterials.Titanium, GTMaterials.Chalcocite, GTMaterials.Palladium, GTMaterials.Ilmenite, GTMaterials.Bornite, GTMaterials.Cooperite, GTMaterials.Silver, GTMaterials.Redstone, GTMaterials.Powellite, GTMaterials.Topaz, GTMaterials.BlueTopaz, GTMaterials.Molybdenum, GTMaterials.Molybdenite, GTMaterials.Pyrite, GTMaterials.Cinnabar, GTMaterials.Lead, GTMaterials.Copper, GTMaterials.Chalcopyrite },
                    new Material[] { GTMaterials.Uraninite, GTOMaterials.Desh, GTMaterials.YellowLimonite, GTMaterials.Magnetite, GTMaterials.CassiteriteSand, GTMaterials.Hematite, GTMaterials.Goethite, GTMaterials.Tetrahedrite, GTMaterials.Chromite, GTMaterials.Stibnite, GTMaterials.TricalciumPhosphate, GTMaterials.VanadiumMagnetite, GTMaterials.GarnetSand, GTMaterials.GreenSapphire, GTMaterials.Pyrope, GTMaterials.Pyrochlore, GTMaterials.Apatite, GTMaterials.Almandine, GTMaterials.Malachite, GTMaterials.Diatomite, GTMaterials.Asbestos, GTMaterials.Sapphire, GTMaterials.Magnesite, GTMaterials.Pitchblende, GTMaterials.Copper },
                    new Material[] { GTMaterials.Amethyst, GTMaterials.Uraninite, GTMaterials.Naquadah, GTMaterials.GarnetRed, GTMaterials.Galena, GTMaterials.Plutonium239, GTMaterials.Pollucite, GTMaterials.Apatite, GTMaterials.Quartzite, GTMaterials.GarnetYellow, GTMaterials.Tungsten, GTMaterials.Silver, GTMaterials.Pyrochlore, GTMaterials.Mica, GTMaterials.Opal, GTMaterials.Pyrolusite, GTMaterials.TricalciumPhosphate, GTMaterials.Kyanite, GTMaterials.Tantalite, GTMaterials.Pitchblende, GTMaterials.CertusQuartz, GTMaterials.Barite, GTOMaterials.Zircon, GTMaterials.Lead },
                    new Material[] { GTMaterials.Monazite, GTMaterials.Lepidolite, GTMaterials.CassiteriteSand, GTMaterials.Bornite, GTMaterials.Tin, GTMaterials.Palladium, GTMaterials.Mica, GTMaterials.Gold, GTMaterials.Lazurite, GTMaterials.Kyanite, GTMaterials.Sodalite, GTMaterials.Neodymium, GTMaterials.Tantalite, GTMaterials.Scheelite, GTMaterials.Lithium, GTMaterials.Bastnasite, GTOMaterials.Zircon, GTMaterials.GarnetSand, GTMaterials.Lapis, GTMaterials.Platinum, GTOMaterials.Celestine, GTMaterials.Tungstate, GTMaterials.Pollucite, GTMaterials.Salt, GTMaterials.Cooperite, GTMaterials.Tungsten, GTMaterials.Oilsands, GTMaterials.Calcite, GTMaterials.RockSalt, GTOMaterials.Ostrum, GTMaterials.Pyrolusite, GTMaterials.Diatomite, GTMaterials.Cassiterite, GTMaterials.Asbestos, GTMaterials.Spodumene, GTMaterials.Coal, GTMaterials.Trona },
                    new Material[] { GTMaterials.Ruby, GTOMaterials.Calorite, GTMaterials.Naquadah, GTMaterials.Galena, GTMaterials.Lepidolite, GTMaterials.Plutonium239, GTMaterials.RockSalt, GTMaterials.Gypsum, GTMaterials.Salt, GTMaterials.Apatite, GTMaterials.Pyrochlore, GTMaterials.Silver, GTMaterials.Oilsands, GTMaterials.GraniticMineralSand, GTMaterials.Redstone, GTMaterials.BasalticMineralSand, GTMaterials.TricalciumPhosphate, GTMaterials.Spodumene, GTMaterials.Cinnabar, GTMaterials.FullersEarth, GTMaterials.Coal, GTMaterials.Cobalt, GTMaterials.Lead },
                    new Material[] { GTMaterials.Iron, GTOMaterials.Desh, GTMaterials.Spessartine, GTMaterials.Hematite, GTMaterials.Goethite, GTMaterials.Bornite, GTMaterials.Tin, GTMaterials.Alunite, GTMaterials.Mica, GTMaterials.Graphite, GTMaterials.TricalciumPhosphate, GTMaterials.Kyanite, GTMaterials.Neodymium, GTMaterials.Tantalite, GTMaterials.FullersEarth, GTMaterials.Scheelite, GTMaterials.Lithium, GTMaterials.Platinum, GTOMaterials.Celestine, GTMaterials.Ruby, GTMaterials.Olivine, GTMaterials.Almandine, GTMaterials.Electrotine, GTMaterials.Grossular, GTMaterials.BlueTopaz, GTMaterials.Molybdenite, GTMaterials.Beryllium, GTMaterials.Emerald, GTMaterials.Coal, GTMaterials.Chalcopyrite, GTMaterials.Monazite, GTMaterials.Chalcocite, GTMaterials.Naquadah, GTMaterials.Ilmenite, GTMaterials.Zeolite, GTMaterials.Chromite, GTMaterials.Stibnite, GTOMaterials.GnomeCrystal, GTMaterials.Lapis, GTMaterials.Bentonite, GTMaterials.Pyrope, GTMaterials.Pollucite, GTMaterials.Salt, GTMaterials.Soapstone, GTOMaterials.UndineCrystal, GTMaterials.Calcite, GTMaterials.Pyrolusite, GTMaterials.Opal, GTMaterials.Asbestos, GTMaterials.Nickel, GTMaterials.Magnesite, GTMaterials.Trona, GTMaterials.Pitchblende, GTMaterials.Copper, GTMaterials.Lead, GTMaterials.CertusQuartz, GTMaterials.Magnetite, GTMaterials.GarnetRed, GTMaterials.Galena, GTMaterials.Plutonium239, GTMaterials.CassiteriteSand, GTMaterials.Gypsum, GTMaterials.Aluminium, GTMaterials.Palladium, GTMaterials.Silver, GTMaterials.Topaz, GTMaterials.Gold, GTMaterials.Lazurite, GTMaterials.Sodalite, GTMaterials.Bastnasite, GTOMaterials.Zircon, GTMaterials.GarnetSand, GTMaterials.Wulfenite, GTMaterials.Cobaltite, GTMaterials.Tungstate, GTMaterials.Diamond, GTMaterials.Garnierite, GTMaterials.Cooperite, GTOMaterials.SalamanderCrystal, GTMaterials.Tungsten, GTMaterials.Apatite, GTMaterials.Pyrochlore, GTMaterials.Realgar, GTMaterials.RockSalt, GTMaterials.Cassiterite, GTMaterials.Sapphire, GTMaterials.Cinnabar, GTMaterials.Cobalt, GTMaterials.Barite, GTMaterials.Uraninite, GTMaterials.Sulfur, GTMaterials.YellowLimonite, GTMaterials.Lepidolite, GTMaterials.Titanium, GTMaterials.Tetrahedrite, GTOMaterials.Calorite, GTOMaterials.PerditioCrystal, GTMaterials.Powellite, GTMaterials.Redstone, GTMaterials.Sphalerite, GTMaterials.Bauxite, GTMaterials.NetherQuartz, GTMaterials.Pyrite, GTMaterials.Saltpeter, GTMaterials.VanadiumMagnetite, GTMaterials.Pentlandite, GTMaterials.Amethyst, GTMaterials.GreenSapphire, GTMaterials.Quartzite, GTMaterials.Talc, GTMaterials.GarnetYellow, GTOMaterials.SylphCrystal, GTMaterials.Oilsands, GTMaterials.GraniticMineralSand, GTMaterials.Diatomite, GTMaterials.Malachite, GTOMaterials.Ostrum, GTMaterials.BasalticMineralSand, GTMaterials.Molybdenum, GTMaterials.Spodumene, GTMaterials.GlauconiteSand },
            };

            // int组
            int[][] material_weights = new int[][] {
                    new int[] { 1454, 1200, 500, 1125, 6000, 725, 200, 2400, 375, 333, 5333, 200, 166, 1125, 544, 333, 750, 250, 187, 400, 800, 250, 272, 400, 500, 375, 474, 642, 125, 166, 500, 428, 3666, 562, 214, 2090, 200, 6136, 2400, 142, 1896, 1000, 600, 1454, 474, 400, 250, 750, 750, 474, 214, 375, 428, 187, 83, 250, 285, 750, 474, 375, 600, 400, 1200, 375, 375, 400, 800, 600, 250, 142, 312, 1454, 166 },
                    new int[] { 166, 1454, 142, 333, 437, 3999, 375, 218, 333, 125, 437, 17, 1125, 1200, 999, 750, 187, 2120, 375, 125, 750, 214, 250, 53, 428, 375, 285, 900, 964, 642, 375, 428, 250, 375, 250, 17, 656, 562, 250, 214, 35, 142, 1284, 1000, 3453, 3636 },
                    new int[] { 166, 1000, 750, 333, 437, 3999, 375, 750, 1125, 218, 333, 17, 125, 600, 375, 437, 1200, 999, 187, 666, 375, 53, 400, 900, 375, 250, 250, 562, 964, 656, 17, 35, 1284, 200, 1999 },
                    new int[] { 800, 93, 150, 300, 1200, 600, 300, 250, 5333, 375, 200, 600, 400, 2666, 800, 150, 561, 125, 400, 450, 250 },
                    new int[] { 1454, 2400, 3999, 1125, 6000, 600, 200, 2400, 75, 25, 125, 200, 166, 999, 333, 250, 1454, 699, 400, 400, 375, 750, 233, 50, 466, 83, 750, 50, 500, 166, 400, 375, 1200, 250, 250, 600, 3453, 3636 },
                    new int[] { 100, 1000, 750, 500, 293, 750, 1125, 333, 17, 333, 375, 225, 544, 666, 56, 53, 272, 187, 125, 35, 200, 17, 90, 62, 166 },
                    new int[] { 437, 375, 150, 293, 25, 1000, 125, 437, 187, 375, 125, 50, 250, 375, 50, 150, 500, 375, 250, 250, 1000, 562, 250, 656, 300, 2500 },
                    new int[] { 150, 2400, 125, 2400, 6000, 375, 1000, 266, 150, 187, 699, 450, 125, 233, 466, 187, 250, 125, 375, 500, 1200, 375, 133, 1000, 562, 312, 2500 },
                    new int[] { 1000, 2400, 561, 2400, 93, 125, 200, 6000, 333, 17, 544, 666, 400, 272, 150, 53, 187, 150, 125, 400, 1200, 600, 35, 17, 90, 300, 62 },
                    new int[] { 250, 300, 83, 300, 250, 375, 1000, 5333, 375, 500, 600, 166, 3666, 250, 250, 964, 1284, 250, 250, 2500 },
                    new int[] { 400, 1454, 50, 500, 53, 133, 437, 25, 266, 293, 50, 333, 600, 17, 437, 656, 17, 35, 1454, 200, 166, 1454, 3636 },
                    new int[] { 93, 100, 2400, 168, 1200, 2400, 6000, 3999, 225, 999, 333, 56, 800, 214, 428, 166, 500, 642, 1200, 400, 800, 214, 200, 561, 1999 },
                    new int[] { 750, 93, 561, 1125, 500, 93, 83, 500, 500, 750, 200, 333, 166, 166, 375, 200, 333, 250, 200, 561, 333, 166, 200, 166 },
                    new int[] { 150, 142, 1200, 75, 5333, 25, 166, 266, 1125, 250, 750, 150, 200, 699, 233, 450, 200, 800, 750, 50, 150, 466, 83, 285, 200, 200, 600, 375, 428, 133, 200, 400, 2666, 800, 142, 2000, 300 },
                    new int[] { 400, 150, 561, 500, 142, 93, 428, 200, 285, 500, 316, 333, 600, 400, 600, 600, 333, 142, 200, 400, 2000, 300, 166 },
                    new int[] { 1454, 100, 375, 3150, 7125, 293, 5333, 125, 166, 544, 333, 250, 150, 387, 400, 699, 233, 50, 150, 400, 125, 642, 250, 562, 656, 35, 964, 1284, 2090, 6136, 150, 437, 561, 566, 1000, 225, 999, 474, 750, 187, 428, 83, 285, 375, 474, 375, 575, 375, 800, 250, 200, 300, 561, 3453, 166, 333, 893, 1125, 500, 93, 1200, 200, 300, 25, 333, 437, 841, 1125, 750, 450, 200, 800, 53, 250, 466, 272, 375, 200, 474, 200, 500, 316, 500, 428, 3666, 214, 200, 300, 166, 93, 1000, 3150, 142, 133, 3999, 150, 1896, 17, 600, 333, 600, 1200, 2120, 375, 456, 250, 750, 214, 900, 250, 750, 474, 600, 400, 650, 1200, 133, 600, 17, 142, 312 },
            };

            OrechidRecipe(1, "overworld_", Blocks.STONE, TagPrefix.ore, materials[0], material_weights[0]);
            OrechidRecipe(2, "the_nether_", Blocks.NETHERRACK, TagPrefix.oreNetherrack, materials[2], material_weights[2]);
            OrechidRecipe(1, "moon_", ModBlocks.MOON_STONE.get(), GTOTagPrefix.MOON_STONE, materials[3], material_weights[3]);
            OrechidRecipe(2, "mars_", ModBlocks.MARS_STONE.get(), GTOTagPrefix.MARS_STONE, materials[4], material_weights[4]);
            OrechidRecipe(2, "venus_", ModBlocks.VENUS_STONE.get(), GTOTagPrefix.VENUS_STONE, materials[5], material_weights[5]);
            OrechidRecipe(2, "mercury_", ModBlocks.MERCURY_STONE.get(), GTOTagPrefix.MERCURY_STONE, materials[6], material_weights[6]);
            OrechidRecipe(1, "ceres_", GTOBlocks.CERES_STONE.get(), GTOTagPrefix.CERES_STONE, materials[7], material_weights[7]);
            OrechidRecipe(1, "io_", GTOBlocks.IO_STONE.get(), GTOTagPrefix.IO_STONE, materials[8], material_weights[8]);
            OrechidRecipe(1, "ganymede_", GTOBlocks.GANYMEDE_STONE.get(), GTOTagPrefix.GANYMEDE_STONE, materials[9], material_weights[9]);
            OrechidRecipe(1, "enceladus_", GTOBlocks.ENCELADUS_STONE.get(), GTOTagPrefix.ENCELADUS_STONE, materials[10], material_weights[10]);
            OrechidRecipe(1, "titan_", GTOBlocks.TITAN_STONE.get(), GTOTagPrefix.TITAN_STONE, materials[11], material_weights[11]);
            OrechidRecipe(1, "pluto_", GTOBlocks.PLUTO_STONE.get(), GTOTagPrefix.PLUTO_STONE, materials[12], material_weights[12]);
            OrechidRecipe(3, "glacio_", ModBlocks.GLACIO_STONE.get(), GTOTagPrefix.GLACIO_STONE, materials[13], material_weights[13]);
            OrechidRecipe(3, "otherside_", DDBlocks.SCULK_STONE.get(), GTOTagPrefix.SCULK_STONE, materials[15], material_weights[15]);

            OrechidIgnemRecipeBuilder.builder("the_nether_ancient_debris")
                    .input(Blocks.NETHERRACK)
                    .output(Blocks.ANCIENT_DEBRIS)
                    .weight(10)
                    .save();

            Material[] ores = { PerditioCrystal, GnomeCrystal, SylphCrystal, UndineCrystal, SalamanderCrystal };
            for (Material material : ores) {
                MarimorphosisRecipeBuilder.builder("living_rock_" + material.getName().toLowerCase())
                        .input(BotaniaBlocks.livingrock)
                        .output(ChemicalHelper.getBlock(GTOTagPrefix.LIVING_STONE, material))
                        .weight(10)
                        .biomeTag(BiomeTags.IS_FOREST)
                        .save();
            }

            Block[] stones = {
                    Blocks.NETHERRACK,
                    Blocks.END_STONE,
                    ModBlocks.MOON_STONE.get(),
                    ModBlocks.MARS_STONE.get(),
                    ModBlocks.VENUS_STONE.get(),
                    ModBlocks.MERCURY_STONE.get(),
                    GTOBlocks.CERES_STONE.get(),
                    GTOBlocks.IO_STONE.get(),
                    GTOBlocks.GANYMEDE_STONE.get(),
                    GTOBlocks.ENCELADUS_STONE.get(),
                    GTOBlocks.TITAN_STONE.get(),
                    GTOBlocks.PLUTO_STONE.get(),
                    ModBlocks.GLACIO_STONE.get(),
                    DDBlocks.SCULK_STONE.get(),
            };

            int[] stones_weights = { 1, 1, 1, 1, 1, 2, 4, 8, 20, 50, 150, 500, 2000, 8000, 2000, 500, 150, 50, 20, 8, 4, 2, 1, 1, 1, 1, 1, };

            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 14; j++) {
                    MarimorphosisRecipeBuilder.builder("star_stone_" + i + "_" + stones[j].getName())
                            .input(GTOBlocks.STAR_STONE[i].get())
                            .output(stones[j])
                            .weight(stones_weights[13 - i + j])
                            .biomeTag(BiomeTags.IS_OVERWORLD)
                            .save();
                }
            }

        }

        // 祭坛锻造
        {
            PedestalRecipeBuilder.builder("heros_soul")
                    .input(ExtraBotanyItems.heroMedal)
                    .output(GTOItems.HEROS_SOUL.asStack(4))
                    .smashTools(ExtraBotanyTags.Items.HAMMERS)
                    .strike(20)
                    .exp(10)
                    .save();
        }

        // 精灵交易
        {
            ElfExchangeRecipe("elf_quartz", new ItemStack(Items.QUARTZ, 4), new ItemStack(BotaniaItems.elfQuartz, 4));
            ElfExchangeRecipe("elf_glass", new ItemStack(BotaniaBlocks.manaGlass, 4), new ItemStack(BotaniaBlocks.elfGlass, 4));
            ElfExchangeRecipe("dreamwood_log", new ItemStack(BotaniaBlocks.livingwoodLog, 4), new ItemStack(BotaniaBlocks.dreamwoodLog, 4));
            ElfExchangeRecipe("dreamwood", new ItemStack(BotaniaBlocks.livingwood, 4), new ItemStack(BotaniaBlocks.dreamwood, 4));
            ElfExchangeRecipe("elementium", new ItemStack(BotaniaItems.manaSteel, 8), new ItemStack(BotaniaItems.elementium, 4));
            ElfExchangeRecipe("elementium_block", new ItemStack(BotaniaBlocks.manasteelBlock, 8), new ItemStack(BotaniaBlocks.elementiumBlock, 4));
            ElfExchangeRecipe("pixie_dust", new ItemStack(BotaniaItems.manaPearl, 4), new ItemStack(BotaniaItems.pixieDust, 4));
            ElfExchangeRecipe("dragonstone", new ItemStack(ItemsRegistry.SOURCE_GEM, 4), new ItemStack(BotaniaItems.dragonstone, 4));
            ElfExchangeRecipe("dragonstone_block", new ItemStack(BlockRegistry.SOURCE_GEM_BLOCK, 4), new ItemStack(BotaniaBlocks.dragonstoneBlock, 4));

            // 额外植物学
            ElfExchangeRecipe("elementium_quartz", new ItemStack(BotaniaItems.manaQuartz, 8), new ItemStack(ExtraBotanyItems.elementiumQuartz, 4));

            // GTO
            ElfExchangeRecipe("colorful_mystical_flower", new ItemStack(BotaniaItems.fertilizer, 4), new ItemStack(COLORFUL_MYSTICAL_FLOWER, 4));

        }

        // 魔力灌注 - 魔力池
        {
            // === 基础配方 ===//
            InfusionManaPoolRecipe("manasteel", GTOChemicalHelper.getItem(ingot, Steel), new ItemStack(BotaniaItems.manaSteel), 3000, null, null);
            InfusionManaPoolRecipe("manasteel_block", GTOChemicalHelper.getItem(block, Steel), new ItemStack(BotaniaBlocks.manasteelBlock), 27000, null, null);
            InfusionManaPoolRecipe("mana_pearl", RegistriesUtils.getItem("torchmaster:frozen_pearl"), new ItemStack(BotaniaItems.manaPearl), 6000, null, null);
            InfusionManaPoolRecipe("mana_diamond", Items.DIAMOND, new ItemStack(BotaniaItems.manaDiamond), 10000, null, null);
            InfusionManaPoolRecipe("mana_diamond_block", Items.DIAMOND_BLOCK, new ItemStack(BotaniaBlocks.manaDiamondBlock), 90000, null, null);
            InfusionManaPoolRecipe("piston_relay", Items.PISTON, new ItemStack(BotaniaBlocks.pistonRelay), 15000, null, null);
            InfusionManaPoolRecipe("mana_cookie", Items.COOKIE, new ItemStack(BotaniaItems.manaCookie), 20000, null, null);
            InfusionManaPoolRecipe("grass_seeds", Items.GRASS, new ItemStack(BotaniaItems.grassSeeds), 2500, null, null);
            InfusionManaPoolRecipe("podzol_seeds", Items.DEAD_BUSH, new ItemStack(BotaniaItems.podzolSeeds), 2500, null, null);
            InfusionManaPoolRecipe("mycel_seeds", Items.RED_MUSHROOM, new ItemStack(BotaniaItems.mycelSeeds), 6500, null, null);
            InfusionManaPoolRecipe("mana_quartz", Items.QUARTZ, new ItemStack(BotaniaItems.manaQuartz), 250, null, null);
            InfusionManaPoolRecipe("tiny_potato", Items.POTATO, new ItemStack(BotaniaBlocks.tinyPotato), 1337, null, null);
            InfusionManaPoolRecipe("mana_glass", Items.GLASS, new ItemStack(BotaniaBlocks.manaGlass), 150, null, null);
            InfusionManaPoolRecipe("mana_string", ItemsRegistry.MAGE_FIBER.asItem(), new ItemStack(BotaniaItems.manaString), 1250, null, null);
            InfusionManaPoolRecipe("mana_bottle", Items.GLASS_BOTTLE, new ItemStack(BotaniaItems.manaBottle), 5000, null, null);
            InfusionManaPoolRecipe("hydroangeas_motif", BotaniaFlowerBlocks.hydroangeas.asItem(), new ItemStack(BotaniaBlocks.motifHydroangeas), 2500, null, null);

            // === 炼药配方 ===//
            InfusionManaPoolRecipe("rotten_flesh_to_leather", Items.ROTTEN_FLESH, new ItemStack(Items.LEATHER), 600, BotaniaBlocks.alchemyCatalyst, null);
            InfusionManaPoolRecipe("chiseled_stone_bricks", Items.STONE_BRICKS, new ItemStack(Blocks.CHISELED_STONE_BRICKS), 150, BotaniaBlocks.alchemyCatalyst, null);
            InfusionManaPoolRecipe("ice", Items.SNOW_BLOCK, new ItemStack(Blocks.ICE), 2250, BotaniaBlocks.alchemyCatalyst, null);
            InfusionManaPoolRecipe("vine_to_lily_pad", Items.VINE, new ItemStack(Blocks.LILY_PAD), 320, BotaniaBlocks.alchemyCatalyst, "botania:vine_and_lily_pad_cycle");
            InfusionManaPoolRecipe("lily_pad_to_vine", Items.LILY_PAD, new ItemStack(Blocks.VINE), 320, BotaniaBlocks.alchemyCatalyst, "botania:vine_and_lily_pad_cycle");
            InfusionManaPoolRecipe("potato_unpoison", Items.POISONOUS_POTATO, new ItemStack(Items.POTATO), 1200, BotaniaBlocks.alchemyCatalyst, null);
            InfusionManaPoolRecipe("blaze_rod_to_nether_wart", Items.BLAZE_ROD, new ItemStack(Items.NETHER_WART), 4000, BotaniaBlocks.alchemyCatalyst, null);
            InfusionManaPoolRecipe("book_to_name_tag", Items.WRITABLE_BOOK, new ItemStack(Items.NAME_TAG), 6000, BotaniaBlocks.alchemyCatalyst, null);
            InfusionManaPoolRecipe("wool_deconstruct", Items.WHITE_WOOL, new ItemStack(Items.STRING, 3), 100, BotaniaBlocks.alchemyCatalyst, null);
            InfusionManaPoolRecipe("cactus_to_slime", Items.CACTUS, new ItemStack(Items.SLIME_BALL), 1200, BotaniaBlocks.alchemyCatalyst, "botania:cactus_and_slime_cycle");
            InfusionManaPoolRecipe("slime_to_cactus", Items.SLIME_BALL, new ItemStack(Blocks.CACTUS), 1200, BotaniaBlocks.alchemyCatalyst, "botania:cactus_and_slime_cycle");
            InfusionManaPoolRecipe("ender_pearl_from_ghast_tear", Items.GHAST_TEAR, new ItemStack(Items.ENDER_PEARL), 28000, BotaniaBlocks.alchemyCatalyst, null);
            InfusionManaPoolRecipe("cobble_to_sand", Items.COBBLESTONE, new ItemStack(Blocks.SAND), 50, BotaniaBlocks.alchemyCatalyst, null);
            InfusionManaPoolRecipe("terracotta_to_red_sand", Items.TERRACOTTA, new ItemStack(Blocks.RED_SAND), 50, BotaniaBlocks.alchemyCatalyst, null);
            InfusionManaPoolRecipe("coarse_dirt", Items.DIRT, new ItemStack(Blocks.COARSE_DIRT), 120, BotaniaBlocks.alchemyCatalyst, null);
            InfusionManaPoolRecipe("soul_soil", Items.SOUL_SAND, new ItemStack(Blocks.SOUL_SOIL), 120, BotaniaBlocks.alchemyCatalyst, null);
            InfusionManaPoolRecipe("stone_to_andesite", Items.STONE, new ItemStack(Blocks.ANDESITE), 200, BotaniaBlocks.alchemyCatalyst, null);
            InfusionManaPoolRecipe("dripleaf_shrinking", Items.BIG_DRIPLEAF, new ItemStack(Blocks.SMALL_DRIPLEAF), 500, BotaniaBlocks.alchemyCatalyst, null);
            InfusionManaPoolRecipe("chorus_fruit_to_flower", Items.POPPED_CHORUS_FRUIT, new ItemStack(Blocks.CHORUS_FLOWER), 10000, BotaniaBlocks.alchemyCatalyst, null);

            // === 方块分解 ===//
            InfusionManaPoolRecipe("glowstone_deconstruct", Items.GLOWSTONE, new ItemStack(Items.GLOWSTONE_DUST, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction");
            InfusionManaPoolRecipe("quartz_deconstruct", Items.QUARTZ_BLOCK, new ItemStack(Items.QUARTZ, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction");
            InfusionManaPoolRecipe("dark_quartz_deconstruct", BotaniaBlocks.darkQuartz.asItem(), new ItemStack(BotaniaItems.darkQuartz, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction");
            InfusionManaPoolRecipe("mana_quartz_deconstruct", BotaniaBlocks.manaQuartz.asItem(), new ItemStack(BotaniaItems.manaQuartz, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction");
            InfusionManaPoolRecipe("blaze_quartz_deconstruct", BotaniaBlocks.blazeQuartz.asItem(), new ItemStack(BotaniaItems.blazeQuartz, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction");
            InfusionManaPoolRecipe("lavender_quartz_deconstruct", BotaniaBlocks.lavenderQuartz.asItem(), new ItemStack(BotaniaItems.lavenderQuartz, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction");
            InfusionManaPoolRecipe("red_quartz_deconstruct", BotaniaBlocks.redQuartz.asItem(), new ItemStack(BotaniaItems.redQuartz, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction");
            InfusionManaPoolRecipe("elf_quartz_deconstruct", BotaniaBlocks.elfQuartz.asItem(), new ItemStack(BotaniaItems.elfQuartz, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction");
            InfusionManaPoolRecipe("sunny_quartz_deconstruct", BotaniaBlocks.sunnyQuartz.asItem(), new ItemStack(BotaniaItems.sunnyQuartz, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction");
            InfusionManaPoolRecipe("clay_deconstruct", Items.CLAY, new ItemStack(Items.CLAY_BALL, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction");
            InfusionManaPoolRecipe("brick_deconstruct", Items.BRICKS, new ItemStack(Items.BRICK, 4), 25, BotaniaBlocks.alchemyCatalyst, "botania:block_deconstruction");

            // === 循环配方 ===//
            // 原木循环 (log_cycle)
            InfusionManaPoolRecipe("oak_log_to_spruce_log", Items.OAK_LOG, new ItemStack(Blocks.SPRUCE_LOG), 40, BotaniaBlocks.alchemyCatalyst, "botania:log_cycle");
            InfusionManaPoolRecipe("spruce_log_to_birch_log", Items.SPRUCE_LOG, new ItemStack(Blocks.BIRCH_LOG), 40, BotaniaBlocks.alchemyCatalyst, "botania:log_cycle");
            InfusionManaPoolRecipe("birch_log_to_jungle_log", Items.BIRCH_LOG, new ItemStack(Blocks.JUNGLE_LOG), 40, BotaniaBlocks.alchemyCatalyst, "botania:log_cycle");
            InfusionManaPoolRecipe("jungle_log_to_acacia_log", Items.JUNGLE_LOG, new ItemStack(Blocks.ACACIA_LOG), 40, BotaniaBlocks.alchemyCatalyst, "botania:log_cycle");
            InfusionManaPoolRecipe("acacia_log_to_dark_oak_log", Items.ACACIA_LOG, new ItemStack(Blocks.DARK_OAK_LOG), 40, BotaniaBlocks.alchemyCatalyst, "botania:log_cycle");
            InfusionManaPoolRecipe("dark_oak_log_to_mangrove_log", Items.DARK_OAK_LOG, new ItemStack(Blocks.MANGROVE_LOG), 40, BotaniaBlocks.alchemyCatalyst, "botania:log_cycle");
            InfusionManaPoolRecipe("mangrove_log_to_cherry_log", Items.MANGROVE_LOG, new ItemStack(Blocks.CHERRY_LOG), 40, BotaniaBlocks.alchemyCatalyst, "botania:log_cycle");
            InfusionManaPoolRecipe("cherry_log_to_oak_log", Items.CHERRY_LOG, new ItemStack(Blocks.OAK_LOG), 40, BotaniaBlocks.alchemyCatalyst, "botania:log_cycle");

            // 青蛙灯循环 (froglight_cycle)
            InfusionManaPoolRecipe("ochre_froglight_to_verdant_froglight", Items.OCHRE_FROGLIGHT, new ItemStack(Blocks.VERDANT_FROGLIGHT), 120, BotaniaBlocks.alchemyCatalyst, "botania:froglight_cycle");
            InfusionManaPoolRecipe("verdant_froglight_to_pearlescent_froglight", Items.VERDANT_FROGLIGHT, new ItemStack(Blocks.PEARLESCENT_FROGLIGHT), 120, BotaniaBlocks.alchemyCatalyst, "botania:froglight_cycle");
            InfusionManaPoolRecipe("pearlescent_froglight_to_ochre_froglight", Items.PEARLESCENT_FROGLIGHT, new ItemStack(Blocks.OCHRE_FROGLIGHT), 120, BotaniaBlocks.alchemyCatalyst, "botania:froglight_cycle");

            // 树苗循环（sapling_cycle，共7种+红树和樱花）
            InfusionManaPoolRecipe("oak_sapling_to_spruce_sapling", Items.OAK_SAPLING, new ItemStack(Blocks.SPRUCE_SAPLING), 120, BotaniaBlocks.alchemyCatalyst, "botania:sapling_cycle");
            InfusionManaPoolRecipe("spruce_sapling_to_birch_sapling", Items.SPRUCE_SAPLING, new ItemStack(Blocks.BIRCH_SAPLING), 120, BotaniaBlocks.alchemyCatalyst, "botania:sapling_cycle");
            InfusionManaPoolRecipe("birch_sapling_to_jungle_sapling", Items.BIRCH_SAPLING, new ItemStack(Blocks.JUNGLE_SAPLING), 120, BotaniaBlocks.alchemyCatalyst, "botania:sapling_cycle");
            InfusionManaPoolRecipe("jungle_sapling_to_acacia_sapling", Items.JUNGLE_SAPLING, new ItemStack(Blocks.ACACIA_SAPLING), 120, BotaniaBlocks.alchemyCatalyst, "botania:sapling_cycle");
            InfusionManaPoolRecipe("acacia_sapling_to_dark_oak_sapling", Items.ACACIA_SAPLING, new ItemStack(Blocks.DARK_OAK_SAPLING), 120, BotaniaBlocks.alchemyCatalyst, "botania:sapling_cycle");
            InfusionManaPoolRecipe("dark_oak_sapling_to_mangrove_propagule", Items.DARK_OAK_SAPLING, new ItemStack(Blocks.MANGROVE_PROPAGULE), 120, BotaniaBlocks.alchemyCatalyst, "botania:sapling_cycle");
            InfusionManaPoolRecipe("mangrove_propagule_to_cherry_sapling", Items.MANGROVE_PROPAGULE, new ItemStack(Blocks.CHERRY_SAPLING), 120, BotaniaBlocks.alchemyCatalyst, "botania:sapling_cycle");
            InfusionManaPoolRecipe("cherry_sapling_to_oak_sapling", Items.CHERRY_SAPLING, new ItemStack(Blocks.OAK_SAPLING), 120, BotaniaBlocks.alchemyCatalyst, "botania:sapling_cycle");

            // 鱼类循环
            InfusionManaPoolRecipe("cod_to_salmon", Items.COD, new ItemStack(Items.SALMON), 200, BotaniaBlocks.alchemyCatalyst, "botania:fish_cycle");
            InfusionManaPoolRecipe("salmon_to_tropical_fish", Items.SALMON, new ItemStack(Items.TROPICAL_FISH), 200, BotaniaBlocks.alchemyCatalyst, "botania:fish_cycle");
            InfusionManaPoolRecipe("tropical_fish_to_pufferfish", Items.TROPICAL_FISH, new ItemStack(Items.PUFFERFISH), 200, BotaniaBlocks.alchemyCatalyst, "botania:fish_cycle");
            InfusionManaPoolRecipe("pufferfish_to_cod", Items.PUFFERFISH, new ItemStack(Items.COD), 200, BotaniaBlocks.alchemyCatalyst, "botania:fish_cycle");

            // 石头循环（stone_cycle）
            InfusionManaPoolRecipe("diorite_to_granite", Items.DIORITE, new ItemStack(Blocks.GRANITE), 200, BotaniaBlocks.alchemyCatalyst, "botania:stone_cycle");
            InfusionManaPoolRecipe("granite_to_andesite", Items.GRANITE, new ItemStack(Blocks.ANDESITE), 200, BotaniaBlocks.alchemyCatalyst, "botania:stone_cycle");
            InfusionManaPoolRecipe("andesite_to_diorite", Items.ANDESITE, new ItemStack(Blocks.DIORITE), 200, BotaniaBlocks.alchemyCatalyst, "botania:stone_cycle");

            // 1.17石头循环（117_stone_cycle）
            InfusionManaPoolRecipe("tuff_to_calcite", Items.TUFF, new ItemStack(Blocks.CALCITE), 200, BotaniaBlocks.alchemyCatalyst, "botania:117_stone_cycle");
            InfusionManaPoolRecipe("calcite_to_deepslate", Items.CALCITE, new ItemStack(Blocks.DEEPSLATE), 200, BotaniaBlocks.alchemyCatalyst, "botania:117_stone_cycle");
            InfusionManaPoolRecipe("deepslate_to_tuff", Items.DEEPSLATE, new ItemStack(Blocks.TUFF), 200, BotaniaBlocks.alchemyCatalyst, "botania:117_stone_cycle");

            // 花循环（flower_cycle）
            InfusionManaPoolRecipe("dandelion_to_poppy", Items.DANDELION, new ItemStack(Blocks.POPPY), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle");
            InfusionManaPoolRecipe("poppy_to_blue_orchid", Items.POPPY, new ItemStack(Blocks.BLUE_ORCHID), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle");
            InfusionManaPoolRecipe("blue_orchid_to_allium", Items.BLUE_ORCHID, new ItemStack(Blocks.ALLIUM), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle");
            InfusionManaPoolRecipe("allium_to_azure_bluet", Items.ALLIUM, new ItemStack(Blocks.AZURE_BLUET), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle");
            InfusionManaPoolRecipe("azure_bluet_to_red_tulip", Items.AZURE_BLUET, new ItemStack(Blocks.RED_TULIP), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle");
            InfusionManaPoolRecipe("red_tulip_to_orange_tulip", Items.RED_TULIP, new ItemStack(Blocks.ORANGE_TULIP), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle");
            InfusionManaPoolRecipe("orange_tulip_to_white_tulip", Items.ORANGE_TULIP, new ItemStack(Blocks.WHITE_TULIP), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle");
            InfusionManaPoolRecipe("white_tulip_to_pink_tulip", Items.WHITE_TULIP, new ItemStack(Blocks.PINK_TULIP), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle");
            InfusionManaPoolRecipe("pink_tulip_to_oxeye_daisy", Items.PINK_TULIP, new ItemStack(Blocks.OXEYE_DAISY), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle");
            InfusionManaPoolRecipe("oxeye_daisy_to_cornflower", Items.OXEYE_DAISY, new ItemStack(Blocks.CORNFLOWER), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle");
            InfusionManaPoolRecipe("cornflower_to_lily_of_the_valley", Items.CORNFLOWER, new ItemStack(Blocks.LILY_OF_THE_VALLEY), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle");
            InfusionManaPoolRecipe("lily_of_the_valley_to_sunflower", Items.LILY_OF_THE_VALLEY, new ItemStack(Blocks.SUNFLOWER), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle");
            InfusionManaPoolRecipe("sunflower_to_lilac", Items.SUNFLOWER, new ItemStack(Blocks.LILAC), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle");
            InfusionManaPoolRecipe("lilac_to_rose_bush", Items.LILAC, new ItemStack(Blocks.ROSE_BUSH), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle");
            InfusionManaPoolRecipe("rose_bush_to_peony", Items.ROSE_BUSH, new ItemStack(Blocks.PEONY), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle");
            InfusionManaPoolRecipe("peony_to_dandelion", Items.PEONY, new ItemStack(Blocks.DANDELION), 400, BotaniaBlocks.alchemyCatalyst, "botania:flower_cycle");

            // 种子循环（crop_cycle）
            InfusionManaPoolRecipe("cocoa_beans_to_wheat_seeds", Items.COCOA_BEANS, new ItemStack(Items.WHEAT_SEEDS), 6000, BotaniaBlocks.alchemyCatalyst, "botania:crop_cycle");
            InfusionManaPoolRecipe("wheat_seeds_to_potato", Items.WHEAT_SEEDS, new ItemStack(Items.POTATO), 6000, BotaniaBlocks.alchemyCatalyst, "botania:crop_cycle");
            InfusionManaPoolRecipe("potato_to_carrot", Items.POTATO, new ItemStack(Items.CARROT), 6000, BotaniaBlocks.alchemyCatalyst, "botania:crop_cycle");
            InfusionManaPoolRecipe("carrot_to_beetroot_seeds", Items.CARROT, new ItemStack(Items.BEETROOT_SEEDS), 6000, BotaniaBlocks.alchemyCatalyst, "botania:crop_cycle");
            InfusionManaPoolRecipe("beetroot_seeds_to_melon_seeds", Items.BEETROOT_SEEDS, new ItemStack(Items.MELON_SEEDS), 6000, BotaniaBlocks.alchemyCatalyst, "botania:crop_cycle");
            InfusionManaPoolRecipe("melon_seeds_to_pumpkin_seeds", Items.MELON_SEEDS, new ItemStack(Items.PUMPKIN_SEEDS), 6000, BotaniaBlocks.alchemyCatalyst, "botania:crop_cycle");
            InfusionManaPoolRecipe("pumpkin_seeds_to_cocoa_beans", Items.PUMPKIN_SEEDS, new ItemStack(Items.COCOA_BEANS), 6000, BotaniaBlocks.alchemyCatalyst, "botania:crop_cycle");

            // 萤石红石循环（glowstone_and_redstone_cycle）
            InfusionManaPoolRecipe("redstone_to_glowstone_dust", Items.REDSTONE, new ItemStack(Items.GLOWSTONE_DUST), 300, BotaniaBlocks.alchemyCatalyst, "botania:glowstone_and_redstone_cycle");
            InfusionManaPoolRecipe("glowstone_dust_to_redstone", Items.GLOWSTONE_DUST, new ItemStack(Items.REDSTONE), 300, BotaniaBlocks.alchemyCatalyst, "botania:glowstone_and_redstone_cycle");

            // 灌木循环（shrub_cycle）
            InfusionManaPoolRecipe("fern_to_dead_bush", Items.FERN, new ItemStack(Blocks.DEAD_BUSH), 500, BotaniaBlocks.alchemyCatalyst, "botania:shrub_cycle");
            InfusionManaPoolRecipe("dead_bush_to_grass", Items.DEAD_BUSH, new ItemStack(Blocks.GRASS), 500, BotaniaBlocks.alchemyCatalyst, "botania:shrub_cycle");
            InfusionManaPoolRecipe("grass_to_fern", Items.GRASS, new ItemStack(Blocks.FERN), 500, BotaniaBlocks.alchemyCatalyst, "botania:shrub_cycle");

            // 浆果循环（berry_cycle）
            InfusionManaPoolRecipe("apple_to_sweet_berries", Items.APPLE, new ItemStack(Items.SWEET_BERRIES), 240, BotaniaBlocks.alchemyCatalyst, "botania:berry_cycle");
            InfusionManaPoolRecipe("sweet_berries_to_glow_berries", Items.SWEET_BERRIES, new ItemStack(Items.GLOW_BERRIES), 240, BotaniaBlocks.alchemyCatalyst, "botania:berry_cycle");
            InfusionManaPoolRecipe("glow_berries_to_apple", Items.GLOW_BERRIES, new ItemStack(Items.APPLE), 240, BotaniaBlocks.alchemyCatalyst, "botania:berry_cycle");

            // 火药和燧石循环（gunpowder_and_flint_cycle）
            InfusionManaPoolRecipe("gunpowder_to_flint", Items.GUNPOWDER, new ItemStack(Items.FLINT), 200, BotaniaBlocks.alchemyCatalyst, "gunpowder_and_flint_cycle");
            InfusionManaPoolRecipe("flint_to_gunpowder", Items.FLINT, new ItemStack(Items.GUNPOWDER), 200, BotaniaBlocks.alchemyCatalyst, "gunpowder_and_flint_cycle");

            // === 迷你花配方 ===//
            InfusionManaPoolRecipe("agricarnation_chibi", BotaniaFlowerBlocks.agricarnation.asItem(), new ItemStack(BotaniaFlowerBlocks.agricarnationChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking");
            InfusionManaPoolRecipe("clayconia_chibi", BotaniaFlowerBlocks.clayconia.asItem(), new ItemStack(BotaniaFlowerBlocks.clayconiaChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking");
            InfusionManaPoolRecipe("bellethorn_chibi", BotaniaFlowerBlocks.bellethorn.asItem(), new ItemStack(BotaniaFlowerBlocks.bellethornChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking");
            InfusionManaPoolRecipe("bubbell_chibi", BotaniaFlowerBlocks.bubbell.asItem(), new ItemStack(BotaniaFlowerBlocks.bubbellChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking");
            InfusionManaPoolRecipe("hopperhock_chibi", BotaniaFlowerBlocks.hopperhock.asItem(), new ItemStack(BotaniaFlowerBlocks.hopperhockChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking");
            InfusionManaPoolRecipe("jiyuulia_chibi", BotaniaFlowerBlocks.jiyuulia.asItem(), new ItemStack(BotaniaFlowerBlocks.jiyuuliaChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking");
            InfusionManaPoolRecipe("tangleberrie_chibi", BotaniaFlowerBlocks.tangleberrie.asItem(), new ItemStack(BotaniaFlowerBlocks.tangleberrieChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking");
            InfusionManaPoolRecipe("marimorphosis_chibi", BotaniaFlowerBlocks.marimorphosis.asItem(), new ItemStack(BotaniaFlowerBlocks.marimorphosisChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking");
            InfusionManaPoolRecipe("rannuncarpus_chibi", BotaniaFlowerBlocks.rannuncarpus.asItem(), new ItemStack(BotaniaFlowerBlocks.rannuncarpusChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking");
            InfusionManaPoolRecipe("solegnolia_chibi", BotaniaFlowerBlocks.solegnolia.asItem(), new ItemStack(BotaniaFlowerBlocks.solegnoliaChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking");

            // === 复制配方 ===//
            InfusionManaPoolRecipe("redstone_dupe", Items.REDSTONE, new ItemStack(Items.REDSTONE, 2), 5000, BotaniaBlocks.conjurationCatalyst, null);
            InfusionManaPoolRecipe("glowstone_dupe", Items.GLOWSTONE_DUST, new ItemStack(Items.GLOWSTONE_DUST, 2), 5000, BotaniaBlocks.conjurationCatalyst, null);
            InfusionManaPoolRecipe("quartz_dupe", Items.QUARTZ, new ItemStack(Items.QUARTZ, 2), 2500, BotaniaBlocks.conjurationCatalyst, null);
            InfusionManaPoolRecipe("coal_dupe", Items.COAL, new ItemStack(Items.COAL, 2), 2100, BotaniaBlocks.conjurationCatalyst, null);
            InfusionManaPoolRecipe("snowball_dupe", Items.SNOWBALL, new ItemStack(Items.SNOWBALL, 2), 200, BotaniaBlocks.conjurationCatalyst, null);
            InfusionManaPoolRecipe("netherrack_dupe", Items.NETHERRACK, new ItemStack(Blocks.NETHERRACK, 2), 200, BotaniaBlocks.conjurationCatalyst, null);
            InfusionManaPoolRecipe("soul_sand_dupe", Items.SOUL_SAND, new ItemStack(Blocks.SOUL_SAND, 2), 1500, BotaniaBlocks.conjurationCatalyst, null);
            InfusionManaPoolRecipe("gravel_dupe", Items.GRAVEL, new ItemStack(Blocks.GRAVEL, 2), 720, BotaniaBlocks.conjurationCatalyst, null);
            InfusionManaPoolRecipe("grass_dupe", Items.GRASS, new ItemStack(Blocks.GRASS, 2), 800, BotaniaBlocks.conjurationCatalyst, null);

            // 树叶复制
            InfusionManaPoolRecipe("oak_leaves_dupe", Items.OAK_LEAVES, new ItemStack(Blocks.OAK_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null);
            InfusionManaPoolRecipe("birch_leaves_dupe", Items.BIRCH_LEAVES, new ItemStack(Blocks.BIRCH_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null);
            InfusionManaPoolRecipe("spruce_leaves_dupe", Items.SPRUCE_LEAVES, new ItemStack(Blocks.SPRUCE_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null);
            InfusionManaPoolRecipe("jungle_leaves_dupe", Items.JUNGLE_LEAVES, new ItemStack(Blocks.JUNGLE_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null);
            InfusionManaPoolRecipe("acacia_leaves_dupe", Items.ACACIA_LEAVES, new ItemStack(Blocks.ACACIA_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null);
            InfusionManaPoolRecipe("dark_oak_leaves_dupe", Items.DARK_OAK_LEAVES, new ItemStack(Blocks.DARK_OAK_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null);
            InfusionManaPoolRecipe("azalea_leaves_dupe", Items.AZALEA_LEAVES, new ItemStack(Blocks.AZALEA_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null);
            InfusionManaPoolRecipe("flowering_azalea_leaves_dupe", Items.FLOWERING_AZALEA_LEAVES, new ItemStack(Blocks.FLOWERING_AZALEA_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null);
            InfusionManaPoolRecipe("mangrove_leaves_dupe", Items.MANGROVE_LEAVES, new ItemStack(Blocks.MANGROVE_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null);
            InfusionManaPoolRecipe("cherry_leaves_dupe", Items.CHERRY_LEAVES, new ItemStack(Blocks.CHERRY_LEAVES, 2), 2000, BotaniaBlocks.conjurationCatalyst, null);

            // 神话植物学
            InfusionManaPoolRecipe("gjallar_horn_empty", BotaniaItems.grassHorn, RegistriesUtils.getItemStack("mythicbotany:gjallar_horn_empty"), 20000, null, null);

            // 额外植物学
            InfusionManaPoolRecipe("nightmare_fuel", Items.COAL, new ItemStack(ExtraBotanyItems.nightmareFuel), 2000, null, null);
            InfusionManaPoolRecipe("fried_chicken", Items.COOKED_CHICKEN, new ItemStack(ExtraBotanyItems.friedChicken), 600, null, null);
            InfusionManaPoolRecipe("snowball_to_ender_pearl", Items.SNOWBALL, new ItemStack(Items.ENDER_PEARL), 2000, ExtraBotanyBlocks.dimensionCatalyst, null);
            InfusionManaPoolRecipe("diamond_horse_armor_to_shulker_shell", Items.DIAMOND_HORSE_ARMOR, new ItemStack(Items.SHULKER_SHELL), 20000, ExtraBotanyBlocks.dimensionCatalyst, null);
            InfusionManaPoolRecipe("apple_to_chorus_fruit", Items.APPLE, new ItemStack(Items.CHORUS_FRUIT), 500, ExtraBotanyBlocks.dimensionCatalyst, null);
            InfusionManaPoolRecipe("stone_to_end_stone", Items.STONE, new ItemStack(Items.END_STONE), 500, ExtraBotanyBlocks.dimensionCatalyst, null);
            InfusionManaPoolRecipe("cobblestone_to_nether_rack", Items.COBBLESTONE, new ItemStack(Items.NETHERRACK), 500, ExtraBotanyBlocks.dimensionCatalyst, null);
            InfusionManaPoolRecipe("sand_to_soul_sand", Items.SAND, new ItemStack(Items.SOUL_SAND), 500, ExtraBotanyBlocks.dimensionCatalyst, null);
            InfusionManaPoolRecipe("iron_ore_to_quartz_ore", Items.IRON_ORE, new ItemStack(Items.NETHER_QUARTZ_ORE), 2000, ExtraBotanyBlocks.dimensionCatalyst, null);
            InfusionManaPoolRecipe("blaze_rod_dupe", Items.BLAZE_ROD, new ItemStack(Items.BLAZE_ROD, 2), 2000, ExtraBotanyBlocks.dimensionCatalyst, null);
            InfusionManaPoolRecipe("nether_star_to_totem_of_undying", Items.NETHER_STAR, new ItemStack(Items.TOTEM_OF_UNDYING), 50000, ExtraBotanyBlocks.dimensionCatalyst, null);
            InfusionManaPoolRecipe("the_origin_to_elytra", ExtraBotanyItems.theOrigin, new ItemStack(Items.ELYTRA), 50000, ExtraBotanyBlocks.dimensionCatalyst, null);
            InfusionManaPoolRecipe("necrofleur_chibi", ExtrabotanyFlowerBlocks.necrofleur.asItem(), new ItemStack(ExtrabotanyFlowerBlocks.necrofleurChibi), 2500, BotaniaBlocks.alchemyCatalyst, "botania:flower_shrinking");

            // GTO配方
            InfusionManaPoolRecipe("pulsating", GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, PulsatingAlloy), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, PulsatingAlloy)), 400, null, null);
            InfusionManaPoolRecipe("conductivee", GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, ConductiveAlloy), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, ConductiveAlloy)), 1600, null, null);
            InfusionManaPoolRecipe("energeticalloy", GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, EnergeticAlloy), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, EnergeticAlloy)), 6400, null, null);
            InfusionManaPoolRecipe("vibrantalloy", GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, VibrantAlloy), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, VibrantAlloy)), 25600, null, null);
            InfusionManaPoolRecipe("endsteel", GTOChemicalHelper.getItem(SUPERCONDUCTOR_BASE, EndSteel), new ItemStack(GTOChemicalHelper.getItem(wireGtSingle, EndSteel)), 102400, null, null);

            InfusionManaPoolRecipe("infused_gold", Items.GOLD_INGOT, new ItemStack(GTOChemicalHelper.getItem(ingot, InfusedGold)), 8000, null, null);
            InfusionManaPoolRecipe("original_bronze_dust", GTOChemicalHelper.getItem(ingot, Bronze), new ItemStack(GTOChemicalHelper.getItem(ingot, OriginalBronze)), 6000, null, null);

            InfusionManaPoolRecipe("life_essence", GTOItems.UNSTABLE_GAIA_SOUL.asItem(), new ItemStack(BotaniaItems.lifeEssence), 500000, ChemicalHelper.getBlock(block, Gaia), null);

            MANA_INFUSER_RECIPES.builder("mana_powder_dust")
                    .notConsumable(BotaniaBlocks.livingrock.asItem())
                    .inputItems(TagUtils.createTGTag("dusts"))
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

        }

        // 魔力灌注 - 白雏菊
        {
            InfusionPureDaisyRecipe("livingclay", Items.DIRT, GTOChemicalHelper.getItem(block, Livingclay));
            InfusionPureDaisyRecipe("livingrock", Items.STONE, BotaniaBlocks.livingrock.asItem());
            InfusionPureDaisyRecipe("livingwood1", BlockRegistry.BLAZING_LOG.asItem(), BotaniaBlocks.livingwoodLog.asItem());
            InfusionPureDaisyRecipe("livingwood2", BlockRegistry.CASCADING_LOG.asItem(), BotaniaBlocks.livingwoodLog.asItem());
            InfusionPureDaisyRecipe("livingwood3", BlockRegistry.VEXING_LOG.asItem(), BotaniaBlocks.livingwoodLog.asItem());
            InfusionPureDaisyRecipe("livingwood4", BlockRegistry.FLOURISHING_LOG.asItem(), BotaniaBlocks.livingwoodLog.asItem());

            InfusionPureDaisyRecipe("cobblestone", Items.NETHERRACK, Items.COBBLESTONE);
            InfusionPureDaisyRecipe("end_stone_to_cobbled_deepslate", Items.END_STONE, Items.COBBLED_DEEPSLATE);
            InfusionPureDaisyRecipe("sand", Items.SOUL_SAND, Items.SAND);
            InfusionPureDaisyRecipe("packed_ice", Items.ICE, Items.PACKED_ICE);
            InfusionPureDaisyRecipe("blue_ice", Items.PACKED_ICE, Items.BLUE_ICE);
            InfusionPureDaisyRecipe("obsidian", BotaniaBlocks.blazeBlock.asItem(), Items.OBSIDIAN);

        }

        MANA_INFUSER_RECIPES.builder("bifrost_perm")
                .notConsumable("botania:rainbow_rod")
                .inputItems(BotaniaBlocks.elfGlass, 16)
                .outputItems(BotaniaBlocks.bifrostPerm, 16)
                .duration(200)
                .MANAt(1)
                .save();

        MANA_INFUSER_RECIPES.builder("gjallar_horn_full")
                .notConsumable("mythicbotany:yggdrasil_branch")
                .inputItems("mythicbotany:gjallar_horn_empty", 16)
                .outputItems("mythicbotany:gjallar_horn_full", 16)
                .duration(400)
                .MANAt(240)
                .save();

        // 工业祭坛 - 符文祭坛
        {
            IndustrialAltarRecipe1(1, "water_rune", 5200, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeWater, 2), new Item[] { BotaniaItems.manaPowder, BotaniaItems.manaSteel, Items.BONE_MEAL, Items.SUGAR_CANE, Items.FISHING_ROD });
            IndustrialAltarRecipe1(1, "fire_rune", 5200, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeFire, 2), new Item[] { BotaniaItems.manaPowder, BotaniaItems.manaSteel, Items.GUNPOWDER, Items.NETHER_BRICK, Items.NETHER_WART });
            IndustrialAltarRecipe1(1, "earth_rune", 5200, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeEarth, 2), new Item[] { BotaniaItems.manaPowder, BotaniaItems.manaSteel, Items.COAL_BLOCK, Items.STONE, Items.RED_MUSHROOM });
            IndustrialAltarRecipe1(1, "air_rune", 5200, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeAir, 2), new Item[] { BotaniaItems.manaPowder, BotaniaItems.manaSteel, Items.WHITE_CARPET, Items.FEATHER, Items.STRING });

            IndustrialAltarRecipe1(1, "spring_rune", 8000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeSpring), new Item[] { BotaniaItems.runeWater, BotaniaItems.runeFire, Items.OAK_SAPLING, Items.OAK_SAPLING, Items.OAK_SAPLING, Items.WHEAT });
            IndustrialAltarRecipe1(1, "summer_rune", 8000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeSummer), new Item[] { BotaniaItems.runeEarth, BotaniaItems.runeAir, Items.SAND, Items.SAND, Items.SLIME_BALL, Items.MELON_SLICE });
            IndustrialAltarRecipe1(1, "autumn_rune", 8000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeAutumn), new Item[] { BotaniaItems.runeFire, BotaniaItems.runeAir, Items.OAK_LEAVES, Items.OAK_LEAVES, Items.OAK_LEAVES, Items.SPIDER_EYE });
            IndustrialAltarRecipe1(1, "winter_rune", 8000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeWinter), new Item[] { BotaniaItems.runeWater, BotaniaItems.runeEarth, Items.SNOW_BLOCK, Items.SNOW_BLOCK, Items.WHITE_WOOL, Items.CAKE });

            IndustrialAltarRecipe1(1, "mana_rune", 8000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeMana), new Item[] { BotaniaItems.manaSteel, BotaniaItems.manaSteel, BotaniaItems.manaSteel, BotaniaItems.manaSteel, BotaniaItems.manaSteel, BotaniaItems.manaPearl });

            IndustrialAltarRecipe1(1, "lust_rune", 12000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeLust), new Item[] { BotaniaItems.runeSummer, BotaniaItems.runeAir, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond });
            IndustrialAltarRecipe1(1, "gluttony_rune", 12000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeGluttony), new Item[] { BotaniaItems.runeWinter, BotaniaItems.runeFire, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond });
            IndustrialAltarRecipe1(1, "greed_rune", 12000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeGreed), new Item[] { BotaniaItems.runeSpring, BotaniaItems.runeWater, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond });
            IndustrialAltarRecipe1(1, "sloth_rune", 12000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeSloth), new Item[] { BotaniaItems.runeAutumn, BotaniaItems.runeAir, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond });
            IndustrialAltarRecipe1(1, "wrath_rune", 12000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeWrath), new Item[] { BotaniaItems.runeWinter, BotaniaItems.runeEarth, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond });
            IndustrialAltarRecipe1(1, "envy_rune", 12000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runeEnvy), new Item[] { BotaniaItems.runeWinter, BotaniaItems.runeWater, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond });
            IndustrialAltarRecipe1(1, "pride_rune", 12000, BotaniaBlocks.livingrock.asItem(), new ItemStack(BotaniaItems.runePride), new Item[] { BotaniaItems.runeSummer, BotaniaItems.runeFire, BotaniaItems.manaDiamond, BotaniaItems.manaDiamond });

            // 神话植物学
            IndustrialAltarRecipe1(1, "midgar_runed", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:midgard_rune")), new Item[] { BotaniaItems.runeEarth, BotaniaItems.runeSpring, BotaniaItems.runeGreed, Items.GRASS_BLOCK, BotaniaItems.manaSteel });
            IndustrialAltarRecipe1(1, "alfheim_rune", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:alfheim_rune")), new Item[] { BotaniaItems.runeAir, BotaniaItems.runeSummer, BotaniaItems.runeLust, Items.OAK_LEAVES, BotaniaItems.elementium });
            IndustrialAltarRecipe1(1, "muspelheim_rune", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:muspelheim_rune")), new Item[] { BotaniaItems.runeFire, BotaniaItems.runeSummer, BotaniaItems.runeWrath, Items.MAGMA_BLOCK, Items.NETHER_BRICK });
            IndustrialAltarRecipe1(1, "niflheim_rune", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:niflheim_rune")), new Item[] { BotaniaItems.runeWater, BotaniaItems.runeWinter, BotaniaItems.runeWrath, Items.BLUE_ICE, Items.IRON_INGOT });
            IndustrialAltarRecipe1(1, "asgard_rune", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:asgard_rune")), new Item[] { BotaniaItems.runeAir, BotaniaItems.runeAutumn, BotaniaItems.runePride, BotaniaItems.rainbowRod, Items.NETHERITE_INGOT });
            IndustrialAltarRecipe1(1, "vanaheim_rune", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:vanaheim_rune")), new Item[] { BotaniaItems.runeEarth, BotaniaItems.runeSpring, BotaniaItems.runePride, BotaniaBlocks.alfPortal.asItem(), BotaniaItems.terrasteel });
            IndustrialAltarRecipe1(1, "helheim_rune", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:helheim_rune")), new Item[] { BotaniaItems.runeFire, BotaniaItems.runeAutumn, BotaniaItems.runeEnvy, Items.SKELETON_SKULL, Items.GOLD_INGOT });
            IndustrialAltarRecipe1(1, "nidavellir_rune", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:nidavellir_rune")), new Item[] { BotaniaItems.runeEarth, BotaniaItems.runeWinter, BotaniaItems.runeSloth, Items.IRON_BLOCK, Items.COPPER_INGOT });
            IndustrialAltarRecipe1(1, "joetunheim_rune", 16000, BotaniaBlocks.livingrock.asItem(), new ItemStack(RegistriesUtils.getItem("mythicbotany:joetunheim_rune")), new Item[] { BotaniaItems.runeEarth, BotaniaItems.runeAutumn, BotaniaItems.runeGluttony, Items.BLACKSTONE, Items.BLACKSTONE });

            // 额外植物学
            IndustrialAltarRecipe2(1, "zadkiel", 500000, BotaniaBlocks.livingrock.asItem(), new ItemStack(ExtraBotanyItems.zadkiel, 4), new Item[] { Items.ICE, Items.BLUE_ICE, Items.PACKED_ICE, Items.SNOW_BLOCK, Items.POWDER_SNOW_BUCKET, Items.TOTEM_OF_UNDYING });
            IndustrialAltarRecipe2(1, "orichalcos_ingot", 150000, BotaniaBlocks.livingrock.asItem(), new ItemStack(ExtraBotanyItems.orichalcos, 8), new Item[] { ExtraBotanyItems.heroMedal, ExtraBotanyItems.gildedPotatoMashed, BotaniaItems.gaiaIngot, BotaniaItems.gaiaIngot, BotaniaItems.lifeEssence, BotaniaItems.lifeEssence, BotaniaItems.lifeEssence, BotaniaItems.lifeEssence });
            IndustrialAltarRecipe2(1, "shadowium_ingot", 4200, BotaniaBlocks.livingrock.asItem(), new ItemStack(ExtraBotanyItems.shadowium, 8), new Item[] { BotaniaItems.elementium, ExtraBotanyItems.gildedPotatoMashed, ExtraBotanyItems.nightmareFuel, ExtraBotanyItems.nightmareFuel, ExtraBotanyItems.nightmareFuel });
            IndustrialAltarRecipe2(1, "photonium_ingot", 4200, BotaniaBlocks.livingrock.asItem(), new ItemStack(ExtraBotanyItems.photonium, 8), new Item[] { BotaniaItems.elementium, ExtraBotanyItems.gildedPotatoMashed, ExtraBotanyItems.spiritFragment, ExtraBotanyItems.spiritFragment, ExtraBotanyItems.spiritFragment });
            IndustrialAltarRecipe2(1, "gilded_potato", 800, BotaniaBlocks.livingrock.asItem(), new ItemStack(ExtraBotanyItems.gildedPotato, 4), new Item[] { Items.POTATO, Items.GOLD_NUGGET });
            IndustrialAltarRecipe2(1, "orichalcos_hammer", 1000000, BotaniaBlocks.livingrock.asItem(), new ItemStack(ExtraBotanyItems.orichalcosHammer, 4), new Item[] { ExtraBotanyItems.orichalcos, ExtraBotanyItems.gildedPotatoMashed, ExtraBotanyItems.theChaos, ExtraBotanyItems.theOrigin, ExtraBotanyItems.theEnd });

            INDUSTRIAL_ALTAR_RECIPES.builder("runerock_block")
                    .inputItems(BotaniaBlocks.livingrock.asItem(), 32)
                    .outputItems(block, Runerock, 32)
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
                    .inputItems(BotaniaBlocks.livingrock.asItem(), 64)
                    .outputItems(block, Runerock, 64)
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

            INDUSTRIAL_ALTAR_RECIPES.builder("transmutation_catalyst")
                    .inputItems(BotaniaBlocks.livingrock.asItem())
                    .outputItems(GTOBlocks.TRANSMUTATION_CATALYST)
                    .duration(1200)
                    .MANAt(40000)
                    .inputItems(GTOItems.PHILOSOPHERS_STONE, 6)
                    .save();
        }

        // 工业祭坛 - 花药台
        {
            IndustrialAltarRecipe3(2, "pure_daisy_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.pureDaisy), new Item[] { BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.whitePetal });
            IndustrialAltarRecipe3(2, "manastar_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.manastar), new Item[] { BotaniaItems.lightBluePetal, BotaniaItems.greenPetal, BotaniaItems.redPetal, BotaniaItems.cyanPetal });
            IndustrialAltarRecipe3(2, "endoflame_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.endoflame), new Item[] { BotaniaItems.brownPetal, BotaniaItems.brownPetal, BotaniaItems.redPetal, BotaniaItems.lightGrayPetal });
            IndustrialAltarRecipe3(2, "hydroangea_flowers", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.hydroangeas), new Item[] { BotaniaItems.bluePetal, BotaniaItems.bluePetal, BotaniaItems.cyanPetal, BotaniaItems.cyanPetal });
            IndustrialAltarRecipe3(2, "thermalily_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.thermalily), new Item[] { BotaniaItems.redPetal, BotaniaItems.orangePetal, BotaniaItems.orangePetal, BotaniaItems.runeEarth, BotaniaItems.runeFire });
            IndustrialAltarRecipe3(2, "rosa_arcana_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.rosaArcana), new Item[] { BotaniaItems.pinkPetal, BotaniaItems.pinkPetal, BotaniaItems.purplePetal, BotaniaItems.purplePetal, BotaniaItems.limePetal, BotaniaItems.runeMana });
            IndustrialAltarRecipe3(2, "munchdew_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.munchdew), new Item[] { BotaniaItems.limePetal, BotaniaItems.limePetal, BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.greenPetal, BotaniaItems.runeGluttony });
            IndustrialAltarRecipe3(3, "entropinnyum_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.entropinnyum), new Item[] { BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.grayPetal, BotaniaItems.grayPetal, BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.runeWrath, BotaniaItems.runeFire });
            IndustrialAltarRecipe3(3, "kekimurus_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.kekimurus), new Item[] { BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.orangePetal, BotaniaItems.orangePetal, BotaniaItems.brownPetal, BotaniaItems.brownPetal, BotaniaItems.runeGluttony, BotaniaItems.pixieDust });
            IndustrialAltarRecipe3(2, "gourmaryllis_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.gourmaryllis), new Item[] { BotaniaItems.lightGrayPetal, BotaniaItems.lightGrayPetal, BotaniaItems.yellowPetal, BotaniaItems.yellowPetal, BotaniaItems.redPetal, BotaniaItems.runeFire, BotaniaItems.runeSummer });
            IndustrialAltarRecipe3(2, "narslimmus_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.narslimmus), new Item[] { BotaniaItems.limePetal, BotaniaItems.limePetal, BotaniaItems.greenPetal, BotaniaItems.greenPetal, BotaniaItems.blackPetal, BotaniaItems.runeSummer, BotaniaItems.runeWater });
            IndustrialAltarRecipe3(3, "spectrolus_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.spectrolus), new Item[] { BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.greenPetal, BotaniaItems.greenPetal, BotaniaItems.bluePetal, BotaniaItems.bluePetal, BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.runeWinter, BotaniaItems.runeAir, BotaniaItems.pixieDust });
            IndustrialAltarRecipe3(2, "rafflowsia_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.rafflowsia), new Item[] { BotaniaItems.purplePetal, BotaniaItems.purplePetal, BotaniaItems.greenPetal, BotaniaItems.greenPetal, BotaniaItems.blackPetal, BotaniaItems.runeEarth, BotaniaItems.runePride, BotaniaItems.pixieDust });
            IndustrialAltarRecipe3(2, "shulk_me_not_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.shulkMeNot), new Item[] { BotaniaItems.purplePetal, BotaniaItems.purplePetal, BotaniaItems.magentaPetal, BotaniaItems.magentaPetal, BotaniaItems.lightGrayPetal, BotaniaItems.lifeEssence, BotaniaItems.runeEnvy, BotaniaItems.runeWrath });
            IndustrialAltarRecipe3(2, "dandelifeon_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.dandelifeon), new Item[] { BotaniaItems.purplePetal, BotaniaItems.purplePetal, BotaniaItems.limePetal, BotaniaItems.greenPetal, BotaniaItems.runeWater, BotaniaItems.runeFire, BotaniaItems.runeEarth, BotaniaItems.runeAir, BotaniaItems.redstoneRoot, BotaniaItems.lifeEssence });
            IndustrialAltarRecipe3(2, "jaded_amaranthus_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.jadedAmaranthus), new Item[] { BotaniaItems.purplePetal, BotaniaItems.limePetal, BotaniaItems.greenPetal, BotaniaItems.runeSpring, BotaniaItems.redstoneRoot });
            IndustrialAltarRecipe3(2, "bellethorn_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.bellethorn), new Item[] { BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.cyanPetal, BotaniaItems.cyanPetal, BotaniaItems.redstoneRoot });
            IndustrialAltarRecipe3(2, "dreadthorn_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.dreadthorn), new Item[] { BotaniaItems.blackPetal, BotaniaItems.blackPetal, BotaniaItems.blackPetal, BotaniaItems.cyanPetal, BotaniaItems.cyanPetal, BotaniaItems.redstoneRoot });
            IndustrialAltarRecipe3(2, "heisei_dream_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.heiseiDream), new Item[] { BotaniaItems.magentaPetal, BotaniaItems.magentaPetal, BotaniaItems.purplePetal, BotaniaItems.pinkPetal, BotaniaItems.runeWrath, BotaniaItems.pixieDust });
            IndustrialAltarRecipe3(2, "tigerseye_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.tigerseye), new Item[] { BotaniaItems.yellowPetal, BotaniaItems.brownPetal, BotaniaItems.orangePetal, BotaniaItems.limePetal, BotaniaItems.runeAutumn });
            IndustrialAltarRecipe3(2, "orechid_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.orechid), new Item[] { BotaniaItems.grayPetal, BotaniaItems.grayPetal, BotaniaItems.yellowPetal, BotaniaItems.greenPetal, BotaniaItems.redPetal, BotaniaItems.runePride, BotaniaItems.runeGreed, BotaniaItems.redstoneRoot, BotaniaItems.pixieDust });
            IndustrialAltarRecipe3(3, "orechid_ignem_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.orechidIgnem), new Item[] { BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.pinkPetal, BotaniaItems.runePride, BotaniaItems.runeGreed, BotaniaItems.redstoneRoot, BotaniaItems.pixieDust });
            IndustrialAltarRecipe3(3, "fallen_kanade_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.fallenKanade), new Item[] { BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.yellowPetal, BotaniaItems.yellowPetal, BotaniaItems.orangePetal, BotaniaItems.runeSpring });
            IndustrialAltarRecipe3(2, "exoflame_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.exoflame), new Item[] { BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.grayPetal, BotaniaItems.lightGrayPetal, BotaniaItems.runeFire, BotaniaItems.runeSummer });
            IndustrialAltarRecipe3(2, "agricarnation_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.agricarnation), new Item[] { BotaniaItems.limePetal, BotaniaItems.limePetal, BotaniaItems.greenPetal, BotaniaItems.yellowPetal, BotaniaItems.runeSpring, BotaniaItems.redstoneRoot });
            IndustrialAltarRecipe3(2, "hopperhock_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.hopperhock), new Item[] { BotaniaItems.grayPetal, BotaniaItems.grayPetal, BotaniaItems.lightGrayPetal, BotaniaItems.lightGrayPetal, BotaniaItems.runeAir, BotaniaItems.redstoneRoot });
            IndustrialAltarRecipe3(3, "tangleberrie_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.tangleberrie), new Item[] { BotaniaItems.cyanPetal, BotaniaItems.cyanPetal, BotaniaItems.grayPetal, BotaniaItems.lightGrayPetal, BotaniaItems.runeAir, BotaniaItems.runeEarth });
            IndustrialAltarRecipe3(2, "jiyuulia_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.jiyuulia), new Item[] { BotaniaItems.pinkPetal, BotaniaItems.pinkPetal, BotaniaItems.purplePetal, BotaniaItems.lightGrayPetal, BotaniaItems.runeWater, BotaniaItems.runeAir });
            IndustrialAltarRecipe3(2, "rannuncarpus_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.rannuncarpus), new Item[] { BotaniaItems.orangePetal, BotaniaItems.orangePetal, BotaniaItems.yellowPetal, BotaniaItems.runeEarth, BotaniaItems.redstoneRoot });
            IndustrialAltarRecipe3(2, "hyacidu_flowers", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.hyacidus), new Item[] { BotaniaItems.purplePetal, BotaniaItems.purplePetal, BotaniaItems.magentaPetal, BotaniaItems.magentaPetal, BotaniaItems.greenPetal, BotaniaItems.runeWater, BotaniaItems.runeAutumn, BotaniaItems.redstoneRoot });
            IndustrialAltarRecipe3(2, "pollidisiac_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.pollidisiac), new Item[] { BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.pinkPetal, BotaniaItems.pinkPetal, BotaniaItems.orangePetal, BotaniaItems.runeLust, BotaniaItems.runeFire });
            IndustrialAltarRecipe3(2, "clayconia_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.clayconia), new Item[] { BotaniaItems.lightGrayPetal, BotaniaItems.lightGrayPetal, BotaniaItems.grayPetal, BotaniaItems.cyanPetal, BotaniaItems.runeEarth });
            IndustrialAltarRecipe3(2, "loonium_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.loonium), new Item[] { BotaniaItems.greenPetal, BotaniaItems.greenPetal, BotaniaItems.greenPetal, BotaniaItems.greenPetal, BotaniaItems.grayPetal, BotaniaItems.runeSloth, BotaniaItems.runeGluttony, BotaniaItems.runeEnvy, BotaniaItems.redstoneRoot, BotaniaItems.pixieDust });
            IndustrialAltarRecipe3(3, "daffomill_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.daffomill), new Item[] { BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.brownPetal, BotaniaItems.yellowPetal, BotaniaItems.runeAir, BotaniaItems.redstoneRoot });
            IndustrialAltarRecipe3(2, "vinculotus_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.vinculotus), new Item[] { BotaniaItems.blackPetal, BotaniaItems.blackPetal, BotaniaItems.purplePetal, BotaniaItems.purplePetal, BotaniaItems.greenPetal, BotaniaItems.runeWater, BotaniaItems.runeSloth, BotaniaItems.runeLust, BotaniaItems.redstoneRoot });
            IndustrialAltarRecipe3(3, "spectranthemum_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.spectranthemum), new Item[] { BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.lightGrayPetal, BotaniaItems.lightGrayPetal, BotaniaItems.cyanPetal, BotaniaItems.runeEnvy, BotaniaItems.runeWater, BotaniaItems.redstoneRoot, BotaniaItems.pixieDust });
            IndustrialAltarRecipe3(2, "medumone_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.medumone), new Item[] { BotaniaItems.brownPetal, BotaniaItems.brownPetal, BotaniaItems.grayPetal, BotaniaItems.grayPetal, BotaniaItems.runeEarth, BotaniaItems.redstoneRoot });
            IndustrialAltarRecipe3(2, "marimorphosis_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.marimorphosis), new Item[] { BotaniaItems.grayPetal, BotaniaItems.yellowPetal, BotaniaItems.greenPetal, BotaniaItems.redPetal, BotaniaItems.runeEarth, BotaniaItems.runeFire, BotaniaItems.redstoneRoot });
            IndustrialAltarRecipe3(3, "bubbell_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.bubbell), new Item[] { BotaniaItems.cyanPetal, BotaniaItems.cyanPetal, BotaniaItems.lightBluePetal, BotaniaItems.lightBluePetal, BotaniaItems.bluePetal, BotaniaItems.bluePetal, BotaniaItems.runeWater, BotaniaItems.runeSummer, BotaniaItems.pixieDust });
            IndustrialAltarRecipe3(2, "solegnolia_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.solegnolia), new Item[] { BotaniaItems.brownPetal, BotaniaItems.brownPetal, BotaniaItems.redPetal, BotaniaItems.bluePetal, BotaniaItems.redstoneRoot });
            IndustrialAltarRecipe3(2, "bergamute_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.bergamute), new Item[] { BotaniaItems.orangePetal, BotaniaItems.greenPetal, BotaniaItems.greenPetal, BotaniaItems.redstoneRoot });
            IndustrialAltarRecipe3(3, "labellia_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaFlowerBlocks.labellia), new Item[] { BotaniaItems.yellowPetal, BotaniaItems.yellowPetal, BotaniaItems.bluePetal, BotaniaItems.whitePetal, BotaniaItems.blackPetal, BotaniaItems.runeAutumn, BotaniaItems.redstoneRoot, BotaniaItems.pixieDust });
            IndustrialAltarRecipe3(2, "motif_daybloom_flower", 1, Items.WHEAT_SEEDS, new ItemStack(motifDaybloom), new Item[] { BotaniaItems.yellowPetal, BotaniaItems.yellowPetal, BotaniaItems.orangePetal, BotaniaItems.lightBluePetal });
            IndustrialAltarRecipe3(2, "motif_nightshade_flower", 1, Items.WHEAT_SEEDS, new ItemStack(BotaniaBlocks.motifNightshade), new Item[] { BotaniaItems.blackPetal, BotaniaItems.blackPetal, BotaniaItems.purplePetal, BotaniaItems.grayPetal });

            // 神话植物学
            IndustrialAltarRecipe3(2, "exoblaze_flower", 1, Items.WHEAT_SEEDS, RegistriesUtils.getItemStack("mythicbotany:exoblaze"), new Item[] { BotaniaItems.yellowPetal, BotaniaItems.yellowPetal, BotaniaItems.grayPetal, BotaniaItems.lightGrayPetal, BotaniaItems.runeFire, Items.BLAZE_POWDER });
            IndustrialAltarRecipe3(2, "wither_aconite_flower", 1, Items.WHEAT_SEEDS, RegistriesUtils.getItemStack("mythicbotany:wither_aconite"), new Item[] { BotaniaItems.blackPetal, BotaniaItems.blackPetal, BotaniaItems.runePride, Blocks.WITHER_ROSE.asItem() });
            IndustrialAltarRecipe3(3, "aquapanthus_flower", 1, Items.WHEAT_SEEDS, RegistriesUtils.getItemStack("mythicbotany:aquapanthus"), new Item[] { BotaniaItems.bluePetal, BotaniaItems.bluePetal, BotaniaItems.lightBluePetal, BotaniaItems.greenPetal, BotaniaItems.cyanPetal });
            IndustrialAltarRecipe3(2, "hellebore_flower", 1, Items.WHEAT_SEEDS, RegistriesUtils.getItemStack("mythicbotany:hellebore"), new Item[] { BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.purplePetal, BotaniaItems.cyanPetal, BotaniaItems.runeFire });
            IndustrialAltarRecipe3(3, "raindeletia_flower", 1, Items.WHEAT_SEEDS, RegistriesUtils.getItemStack("mythicbotany:raindeletia"), new Item[] { BotaniaItems.lightBluePetal, BotaniaItems.bluePetal, BotaniaItems.magentaPetal, BotaniaItems.whitePetal, BotaniaItems.runeWater });
            IndustrialAltarRecipe3(2, "petrunia_flower", 1, Items.WHEAT_SEEDS, RegistriesUtils.getItemStack("mythicbotany:petrunia"), new Item[] { BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.orangePetal, BotaniaItems.brownPetal, RegistriesUtils.getItem("mythicbotany:gjallar_horn_full"), BotaniaItems.phantomInk });

            // 额外植物学
            IndustrialAltarRecipe3(2, "trade_orchid", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.tradeOrchid), new Item[] { BotaniaItems.limePetal, BotaniaItems.limePetal, BotaniaItems.greenPetal, BotaniaItems.brownPetal, BotaniaItems.runeGreed, BotaniaItems.runeLust, BotaniaItems.redstoneRoot });
            IndustrialAltarRecipe3(2, "woodienia", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.woodienia), new Item[] { BotaniaItems.brownPetal, BotaniaItems.brownPetal, BotaniaItems.brownPetal, BotaniaItems.grayPetal, ExtraBotanyItems.elementiumQuartz, BotaniaItems.runeGluttony, BotaniaItems.redstoneRoot });
            IndustrialAltarRecipe3(3, "reikarlily", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.reikarlily), new Item[] { BotaniaItems.lightBluePetal, BotaniaItems.lightBluePetal, BotaniaItems.cyanPetal, BotaniaItems.cyanPetal, BotaniaItems.bluePetal, BotaniaItems.runePride, BotaniaItems.runeEnvy, BotaniaItems.runeSloth, BotaniaItems.lifeEssence });
            IndustrialAltarRecipe3(2, "bellflower", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.bellflower), new Item[] { BotaniaItems.yellowPetal, BotaniaItems.yellowPetal, BotaniaItems.limePetal, BotaniaItems.limePetal, ExtraBotanyItems.spiritFragment });
            IndustrialAltarRecipe3(3, "annoyingflower", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.annoyingflower), new Item[] { BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.pinkPetal, BotaniaItems.pinkPetal, BotaniaItems.greenPetal, BotaniaItems.runeMana, ExtraBotanyItems.spiritFragment });
            IndustrialAltarRecipe3(2, "stonesia", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.stonesia), new Item[] { BotaniaItems.grayPetal, BotaniaItems.grayPetal, BotaniaItems.blackPetal, BotaniaItems.lifeEssence, BotaniaItems.runeAutumn, BotaniaItems.runeGluttony });
            IndustrialAltarRecipe3(3, "edelweiss", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.edelweiss), new Item[] { BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.whitePetal, BotaniaItems.lightBluePetal, BotaniaItems.lightBluePetal, BotaniaItems.manaPowder, BotaniaItems.runeMana, BotaniaItems.runeWinter });
            IndustrialAltarRecipe3(2, "resoncund", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.resoncund), new Item[] { BotaniaItems.magentaPetal, BotaniaItems.magentaPetal, BotaniaItems.orangePetal, BotaniaItems.orangePetal, BotaniaItems.runeLust, BotaniaItems.runeGluttony });
            IndustrialAltarRecipe3(4, "sunshine_lily", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.sunshineLily), new Item[] { BotaniaItems.yellowPetal, BotaniaItems.yellowPetal, BotaniaItems.yellowPetal, BotaniaItems.orangePetal });
            IndustrialAltarRecipe3(3, "moonlight_lily", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.moonlightLily), new Item[] { BotaniaItems.blackPetal, BotaniaItems.blackPetal, BotaniaItems.purplePetal, BotaniaItems.grayPetal });
            IndustrialAltarRecipe3(2, "serenitian", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.serenitian), new Item[] { BotaniaItems.purplePetal, BotaniaItems.purplePetal, BotaniaItems.bluePetal, BotaniaItems.bluePetal, BotaniaItems.runeMana, BotaniaItems.runeSloth, BotaniaItems.runeGreed, BotaniaItems.lifeEssence, Items.WITHER_ROSE });
            IndustrialAltarRecipe3(2, "twinstar", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.twinstar), new Item[] { BotaniaItems.yellowPetal, BotaniaItems.yellowPetal, BotaniaItems.yellowPetal, BotaniaItems.orangePetal, BotaniaItems.orangePetal, BotaniaItems.orangePetal, BotaniaItems.manaPowder, BotaniaItems.manaPowder });
            IndustrialAltarRecipe3(2, "omniviolet", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.omniviolet), new Item[] { BotaniaItems.purplePetal, BotaniaItems.purplePetal, BotaniaItems.bluePetal, BotaniaItems.bluePetal, BotaniaItems.runeSpring, BotaniaItems.runeMana, BotaniaItems.runeLust });
            IndustrialAltarRecipe3(3, "tinkle", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.tinkle), new Item[] { BotaniaItems.yellowPetal, BotaniaItems.yellowPetal, BotaniaItems.greenPetal, BotaniaItems.limePetal, BotaniaItems.runeWater, BotaniaItems.runeEarth, BotaniaItems.manaPowder, ExtraBotanyItems.spiritFragment, ExtraBotanyItems.spiritFragment });
            IndustrialAltarRecipe3(2, "blood_enchantress", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.bloodEnchantress), new Item[] { BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.redPetal, BotaniaItems.runeFire, BotaniaItems.runeSummer, BotaniaItems.runeWrath });
            IndustrialAltarRecipe3(3, "mirrowtunia", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.mirrowtunia), new Item[] { BotaniaItems.cyanPetal, BotaniaItems.cyanPetal, BotaniaItems.lightBluePetal, BotaniaItems.bluePetal, BotaniaItems.runeWrath, BotaniaItems.runePride, BotaniaItems.runeAir, BotaniaItems.manaPowder });
            IndustrialAltarRecipe3(2, "manalink", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.manalink), new Item[] { BotaniaItems.cyanPetal, BotaniaItems.cyanPetal, BotaniaItems.cyanPetal, BotaniaItems.lightBluePetal, BotaniaItems.lightBluePetal, BotaniaItems.runeSloth, BotaniaItems.runeLust, BotaniaItems.lifeEssence });
            IndustrialAltarRecipe3(2, "necrofleur", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.necrofleur), new Item[] { BotaniaItems.grayPetal, BotaniaItems.grayPetal, BotaniaItems.pinkPetal, BotaniaItems.pinkPetal, BotaniaItems.redPetal, BotaniaItems.runeWrath, BotaniaItems.manaPowder });
            IndustrialAltarRecipe3(2, "enchanter", 1, Items.WHEAT_SEEDS, new ItemStack(ExtrabotanyFlowerBlocks.enchanter), new Item[] { BotaniaItems.purplePetal, BotaniaItems.purplePetal, BotaniaItems.magentaPetal, BotaniaItems.limePetal, BotaniaItems.limePetal, BotaniaItems.runePride, BotaniaItems.runeGreed, BotaniaItems.runeGluttony, BotaniaItems.lifeEssence });

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

        }

        // 工业祭坛 - 额外植物学 祭坛锻造
        {
            PedestalSmashRecipe(5, "gilded_potato_mashed", 180, Ingredient.of(ExtraBotanyTags.Items.HAMMERS), new ItemStack(ExtraBotanyItems.gildedPotato, 16), new ItemStack(ExtraBotanyItems.gildedPotatoMashed, 16));
            PedestalSmashRecipe(5, "spirit_fragment", 180, Ingredient.of(ExtraBotanyTags.Items.HAMMERS), new ItemStack(ExtraBotanyItems.spiritFuel, 16), new ItemStack(ExtraBotanyItems.spiritFragment, 16));
            PedestalSmashRecipe(5, "heros_soul", 360, Ingredient.of(ExtraBotanyTags.Items.HAMMERS), new ItemStack(ExtraBotanyItems.heroMedal, 16), new ItemStack(GTOItems.HEROS_SOUL, 64));
        }

        // 多方块特供配方
        {
            // 魔力凝聚
            {
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
                            .inputItems(GTItems.QUANTUM_EYE.asItem())
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
                        .inputItems(ingot, GTOMaterials.Manasteel, 2)
                        .inputItems("botania:mana_pearl", 2)
                        .inputItems(gem, GTOMaterials.ManaDiamond, 2)
                        .outputItems(ingot, GTOMaterials.Terrasteel, 3)
                        .notConsumableFluid(GTOMaterials.Terrasteel, 144)
                        .MANAt(512)
                        .duration(400)
                        .save();

                MANA_CONDENSER_RECIPES.builder("thaumium_ingot")
                        .inputItems(ingot, Livingsteel, 2)
                        .inputItems(ItemsRegistry.SOURCE_GEM, 2)
                        .inputItems(ingot, OriginalBronze, 2)
                        .inputItems(ItemsRegistry.MANIPULATION_ESSENCE, 2)
                        .outputItems(ingot, GTOMaterials.Thaumium, 3)
                        .notConsumableFluid(GTOMaterials.Thaumium, 144)
                        .MANAt(512)
                        .duration(400)
                        .save();

                MANA_CONDENSER_RECIPES.builder("alfsteel_ingot")
                        .inputItems(BotaniaItems.elementium, 2)
                        .inputItems(BotaniaItems.pixieDust, 2)
                        .inputItems(BotaniaItems.dragonstone, 2)
                        .outputItems(ingot, Alfsteel, 3)
                        .notConsumableFluid(GTOMaterials.Alfsteel, 144)
                        .MANAt(1536)
                        .duration(400)
                        .save();

                MANA_CONDENSER_RECIPES.builder("gaiasteel_ingot")
                        .inputItems(RegistriesUtils.getItemStack("mythicbotany:asgard_rune"), 2)
                        .inputItems(RegistriesUtils.getItemStack("mythicbotany:vanaheim_rune"), 2)
                        .inputItems(RegistriesUtils.getItemStack("mythicbotany:alfheim_rune"), 2)
                        .inputItems(RegistriesUtils.getItemStack("mythicbotany:midgard_rune"), 2)
                        .inputItems(RegistriesUtils.getItemStack("mythicbotany:joetunheim_rune"), 2)
                        .inputItems(RegistriesUtils.getItemStack("mythicbotany:muspelheim_rune"), 2)
                        .inputItems(RegistriesUtils.getItemStack("mythicbotany:niflheim_rune"), 2)
                        .inputItems(RegistriesUtils.getItemStack("mythicbotany:nidavellir_rune"), 2)
                        .inputItems(RegistriesUtils.getItemStack("mythicbotany:helheim_rune"), 2)
                        .inputItems(ingot, Alfsteel, 6)
                        .inputItems(ingot, Runerock, 6)
                        .outputItems(ingot, Gaiasteel, 9)
                        .notConsumableFluid(GTOMaterials.Gaiasteel, 576)
                        .MANAt(2560)
                        .duration(400)
                        .save();

                MANA_CONDENSER_RECIPES.builder("gaia_ingot")
                        .inputItems(ingot, Gaiasteel, 4)
                        .inputItems(BotaniaItems.lifeEssence, 4)
                        .outputItems(ingot, Gaia, 6)
                        .notConsumableFluid(GTOMaterials.Gaia, 288)
                        .MANAt(5120)
                        .duration(400)
                        .save();

                MANA_CONDENSER_RECIPES.builder("aerialite_ingot")
                        .inputItems(BotaniaItems.enderAirBottle, 2)
                        .inputItems(gem, Dragonstone, 2)
                        .inputItems(Items.PHANTOM_MEMBRANE.asItem(), 2)
                        .notConsumableFluid(GTOMaterials.Aerialite, 144)
                        .outputItems(ingot, Aerialite, 3)
                        .MANAt(256)
                        .duration(200)
                        .save();

                MANA_CONDENSER_RECIPES.builder("the_universe")
                        .inputItems(ExtraBotanyItems.theChaos, 2)
                        .inputItems(ExtraBotanyItems.theOrigin, 2)
                        .inputItems(ExtraBotanyItems.theEnd, 2)
                        .outputItems(ExtraBotanyItems.theUniverse, 3)
                        .MANAt(512)
                        .duration(200)
                        .save();
            }

            // 精灵交易
            {
                ElfExchangeRecipe(1, "pandoras_box", 20000, 100, new ItemStack(ExtraBotanyItems.heroMedal, 2), new ItemStack(ExtraBotanyItems.pandorasBox));
                ElfExchangeRecipe(2, "nine_and_three_quarters", 10000, 100, new ItemStack(ExtraBotanyItems.heroMedal), new ItemStack(ExtraBotanyItems.nineAndThreeQuartersRewardBag, 40));
                // 价值 4000 100 1 10 80 80(大概？)
                ElfExchangeRecipe(3, "eins", 2000, 100, new ItemStack(ExtraBotanyItems.heroMedal), new ItemStack(ExtraBotanyItems.einsRewardBag, 4000));
                ElfExchangeRecipe(4, "zwei", 4000, 100, new ItemStack(ExtraBotanyItems.heroMedal), new ItemStack(ExtraBotanyItems.zweiRewardBag, 400));
                ElfExchangeRecipe(5, "drei", 6000, 100, new ItemStack(ExtraBotanyItems.heroMedal), new ItemStack(ExtraBotanyItems.dreiRewardBag, 50));
                ElfExchangeRecipe(6, "vier", 6000, 100, new ItemStack(ExtraBotanyItems.heroMedal), new ItemStack(ExtraBotanyItems.vierRewardBag, 50));

                ElfExchangeRecipe(7, "philosophers_stone", 20000, 600, new ItemStack(ExtraBotanyItems.heroMedal, 64), new ItemStack(GTOItems.PHILOSOPHERS_STONE));
            }
        }
    }

    private static void PureDaisyRecipe(
                                        String id,
                                        StateIngredient input,
                                        Block output) {
        PureDaisyRecipeBuilder.builder(id)
                .input(input)
                .output(output)
                .save();
    }

    private static void ManaInfusionRecipe(
                                           String id,
                                           Ingredient input,
                                           ItemStack output,
                                           int mana,
                                           Block customCatalyst,
                                           String group) {
        ManaInfusionRecipeBuilder.builder(id)
                .input(input)
                .output(output)
                .mana(mana)
                .group(group)
                .customCatalyst(customCatalyst)
                .save();
    }

    private static void ApothecaryRecipe(
                                         String id,
                                         Ingredient catalyst,
                                         ItemStack output,
                                         Ingredient[] input) {
        var build = PetalApothecaryRecipeBuilder.builder(id);
        build
                .reagent(catalyst)
                .output(output);
        for (Ingredient ingredient : input) build.addIngredient(ingredient);
        build.save();
    }

    private static void RunicAltarRecipe(
                                         String id,
                                         int mana,
                                         ItemStack output,
                                         Boolean setHeadRecipe,
                                         Ingredient[] input) {
        var build = RunicAltarRecipeBuilder.builder(id);
        build
                .output(output)
                .mana(mana)
                .setHeadRecipe(setHeadRecipe);
        for (Ingredient ingredient : input) build.addIngredient(ingredient);
        build.save();
    }

    private static void TAgglomerationRecipe(
                                             String id,
                                             int mana,
                                             ItemStack output,
                                             Ingredient[] input) {
        var build = TerrestrialAgglomerationRecipeBuilder.builder(id);
        build
                .output(output)
                .mana(mana);
        for (Ingredient ingredient : input) build.addIngredient(ingredient);
        build.save();
    }

    private static void OrechidRecipe(
                                      int id,
                                      String name,
                                      Block input,
                                      TagPrefix output,
                                      Material[] material,
                                      int[] weight) {
        if (id == 1) {
            for (int k = 0; k < material.length; k++) {
                OrechidRecipeBuilder.builder(name + material[k].getName().toLowerCase())
                        .input(input)
                        .output(ChemicalHelper.getBlock(output, material[k]))
                        .weight(weight[k])
                        .save();
            }
        } else if (id == 2) {
            for (int k = 0; k < material.length; k++) {
                OrechidIgnemRecipeBuilder.builder(name + material[k].getName().toLowerCase())
                        .input(input)
                        .output(ChemicalHelper.getBlock(output, material[k]))
                        .weight(weight[k])
                        .save();
            }
        } else if (id == 3) {
            for (int k = 0; k < material.length; k++) {
                MarimorphosisRecipeBuilder.builder(name + material[k].getName().toLowerCase())
                        .input(input)
                        .output(ChemicalHelper.getBlock(output, material[k]))
                        .weight(weight[k])
                        .biomeTag(BiomeTags.IS_OVERWORLD)
                        .save();
            }
        }
    }

    private static void ElfExchangeRecipe(
                                          int circuitMeta,
                                          String id,
                                          int mana,
                                          int duration,
                                          ItemStack input,
                                          ItemStack output) {
        var build = ELF_EXCHANGE_RECIPES.builder(id);
        if (circuitMeta != 0) build.circuitMeta(circuitMeta);
        build
                .inputItems(input)
                .outputItems(output)
                .duration(duration)
                .MANAt(mana)
                .save();
    }

    private static void ElfExchangeRecipe(
                                          String id,
                                          ItemStack input,
                                          ItemStack output) {
        ElfExchangeRecipe(0, id, 8, 10, input, output);
    }

    private static void InfusionManaPoolRecipe(
                                               String id,
                                               Item input,
                                               ItemStack output,
                                               int mana,
                                               Block customCatalyst,
                                               String group) {
        Item customCatalystItem = (customCatalyst == null) ? BotaniaBlocks.livingrock.asItem() : customCatalyst.asItem();
        int manat = Math.max(mana / 500, 1);
        int count = 1;
        if (manat == 1) count = Math.max(500 / mana, 1);
        MANA_INFUSER_RECIPES.builder(id)
                .notConsumable(customCatalystItem)
                .inputItems(input, count)
                .outputItems(output.copyWithCount(output.getCount() * count))
                .duration(20)
                .circuitMeta(1)
                .MANAt(manat)
                .save();
    }

    private static void InfusionPureDaisyRecipe(
                                                String id,
                                                Item input,
                                                Item output) {
        MANA_INFUSER_RECIPES.builder(id)
                .notConsumable(BotaniaFlowerBlocks.pureDaisy.asItem())
                .inputItems(input, 32)
                .outputItems(output, 32)
                .duration(600)
                .circuitMeta(2)
                .MANAt(1)
                .save();
    }

    private static void IndustrialAltarRecipe1(
                                               int circuitMeta,
                                               String id,
                                               int mana,
                                               Item input,
                                               ItemStack output,
                                               Item[] inputs) {
        Object2IntMap<Item> CountMap = new O2IOpenCacheHashMap<>();
        for (Item item : inputs) CountMap.mergeInt(item, 1, Integer::sum);

        var build = INDUSTRIAL_ALTAR_RECIPES.builder(id);
        build
                .inputItems(input, 16)
                .outputItems(output.copyWithCount(output.getCount() << 3))
                .duration(300)
                .circuitMeta(circuitMeta)
                .MANAt(mana / 50);
        for (Object2IntMap.Entry<Item> entry : CountMap.object2IntEntrySet()) build.inputItems(entry.getKey(), entry.getIntValue());
        build.save();
    }

    private static void IndustrialAltarRecipe2(
                                               int circuitMeta,
                                               String id,
                                               int mana,
                                               Item input,
                                               ItemStack output,
                                               Item[] inputs) {
        Object2IntMap<Item> CountMap = new O2IOpenCacheHashMap<>();
        for (Item item : inputs) CountMap.mergeInt(item, 1, Integer::sum);

        var build = INDUSTRIAL_ALTAR_RECIPES.builder(id);
        build
                .inputItems(input, 4)
                .outputItems(output)
                .duration(300)
                .circuitMeta(circuitMeta)
                .MANAt(mana / 50);
        for (Object2IntMap.Entry<Item> entry : CountMap.object2IntEntrySet()) build.inputItems(entry.getKey(), entry.getIntValue() * 4);
        build.save();
    }

    private static void IndustrialAltarRecipe3(
                                               int circuitMeta,
                                               String id,
                                               int mana,
                                               Item input,
                                               ItemStack output,
                                               Item[] inputs) {
        Object2IntMap<Item> CountMap = new O2IOpenCacheHashMap<>();
        for (Item item : inputs) CountMap.mergeInt(item, 1, Integer::sum);

        var build = INDUSTRIAL_ALTAR_RECIPES.builder(id);
        build
                .inputItems(ForgeTags.SEEDS, 8)
                .inputFluids(Water.getFluid(8000))
                .outputItems(output, 8)
                .duration(20)
                .circuitMeta(circuitMeta)
                .MANAt(32);
        for (Object2IntMap.Entry<Item> entry : CountMap.object2IntEntrySet()) build.inputItems(entry.getKey(), entry.getIntValue() * 2);
        build.save();
    }

    private static void PedestalSmashRecipe(
                                            int circuitMeta,
                                            String id,
                                            int mana,
                                            Ingredient hammer,
                                            ItemStack input,
                                            ItemStack output) {
        INDUSTRIAL_ALTAR_RECIPES.builder(id)
                .notConsumable(hammer)
                .circuitMeta(circuitMeta)
                .inputItems(input)
                .outputItems(output)
                .duration(40)
                .MANAt(mana)
                .save();
    }
}
