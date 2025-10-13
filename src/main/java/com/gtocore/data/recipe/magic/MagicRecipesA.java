package com.gtocore.data.recipe.magic;

import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMachines;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.machines.GCYMMachines;
import com.gtocore.common.data.machines.ManaMachine;
import com.gtocore.common.data.machines.ManaMultiBlock;
import com.gtocore.data.record.Enchantment;
import com.gtocore.data.tag.Tags;

import com.gtolib.GTOCore;
import com.gtolib.api.GTOValues;
import com.gtolib.utils.RegistriesUtils;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.chemical.material.Material;
import com.gregtechceu.gtceu.api.data.chemical.material.stack.MaterialEntry;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.FluidStack;

import appeng.core.definitions.AEItems;
import com.enderio.base.common.init.EIOFluids;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import dev.shadowsoffire.apotheosis.adventure.Adventure;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys.GAS;
import static com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys.LIQUID;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOItems.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;
import static com.gtocore.common.machine.mana.multiblock.ThePrimordialReconstructor.getGem;
import static com.gtolib.GTOCore.id;

public final class MagicRecipesA {

    public static void init() {
        // 机器外壳
        Material[] ManaSteels = {
                OriginalBronze, Manasteel, Terrasteel, Elementium, Alfsteel, Gaiasteel, Orichalcos
        };
        Item[] ManaCasing = {
                GTOBlocks.ORIGINAL_BRONZE_CASING.asItem(), GTOBlocks.MANASTEEL_CASING.asItem(),
                GTOBlocks.TERRASTEEL_CASING.asItem(), GTOBlocks.ELEMENTIUM_CASING.asItem(),
                GTOBlocks.ALFSTEEL_CASING.asItem(), GTOBlocks.GAIASTEEL_CASING.asItem(),
                GTOBlocks.ORICHALCOS_CASING.asItem(),
        };
        for (int i = 0; i < ManaSteels.length; i++) {
            VanillaRecipeHelper.addShapedRecipe(id(GTOValues.MANAN[i].toLowerCase() + "_mana_hull"), ManaMachine.MANA_HULL[i].asItem(),
                    "CBC", "BAB", "CBC",
                    'A', ManaCasing[i], 'B', new MaterialEntry(plate, ManaSteels[i]), 'C', new MaterialEntry(rod, ManaSteels[i]));
        }

        // 炼金锅1
        {
            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("fractal_petal_solvent_slow")
                    .inputItems(COLORFUL_MYSTICAL_FLOWER)
                    .inputFluids(Water.getFluid(1000))
                    .chancedOutput(FractalPetalSolvent.getFluid(1000), 10, 0)
                    .duration(240)
                    .temperature(500)
                    .addData("param1", 80)
                    .addData("param2", 80)
                    .addData("param3", 80)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("cycle_of_blossoms_solvent")
                    .inputItems(COLORFUL_MYSTICAL_FLOWER, 16)
                    .inputItems(dust, Runerock, 4)
                    .inputItems(dust, Dragonstone, 4)
                    .inputItems(dust, Shimmerwood, 4)
                    .inputItems(dust, Shimmerrock, 4)
                    .inputItems(dust, BifrostPerm, 4)
                    .inputFluids(FractalPetalSolvent.getFluid(2000))
                    .inputFluids(Ethanol.getFluid(1000))
                    .chancedOutput(CycleofBlossomsSolvent.getFluid(1000), 10, 0)
                    .chancedOutput(FractalPetalSolvent.getFluid(250), 1, 0)
                    .duration(480)
                    .temperature(500)
                    .addData("param1", 50)
                    .addData("param2", 50)
                    .addData("param3", 50)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("gaia_solvent_slow")
                    .inputItems(dustTiny, Gaia)
                    .inputFluids(FractalPetalSolvent.getFluid(1000))
                    .chancedOutput(GaiaSolvent.getFluid(1000), 10, 0)
                    .duration(600)
                    .MANAt(8)
                    .temperature(800)
                    .addData("param1", 140)
                    .addData("param2", 60)
                    .addData("param3", 60)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("wilden_essence_1")
                    .inputItems(dust, GTOMaterials.SourceGem, 8)
                    .inputItems(BlockRegistry.SOURCEBERRY_BUSH, 2)
                    .inputItems(ItemsRegistry.WILDEN_HORN)
                    .inputItems(ItemsRegistry.WILDEN_SPIKE)
                    .inputItems(ItemsRegistry.WILDEN_WING)
                    .inputFluids(FractalPetalSolvent.getFluid(250))
                    .inputFluids(Ethanol.getFluid(750))
                    .chancedOutput(WildenEssence.getFluid(1000), 10, 0)
                    .duration(600)
                    .temperature(1200)
                    .addData("param1", 60)
                    .addData("param2", 140)
                    .addData("param3", 60)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("wilden_essence_2")
                    .inputItems(dust, GTOMaterials.SourceGem, 8)
                    .inputItems(BlockRegistry.SOURCEBERRY_BUSH, 2)
                    .chancedInput(new ItemStack(ItemsRegistry.WILDEN_TRIBUTE), 10, 0)
                    .inputFluids(FractalPetalSolvent.getFluid(250))
                    .inputFluids(Ethanol.getFluid(750))
                    .chancedOutput(WildenEssence.getFluid(1000), 10, 0)
                    .duration(600)
                    .temperature(1500)
                    .addData("param1", 60)
                    .addData("param2", 140)
                    .addData("param3", 60)
                    .save();

            String[] names = { "gnome", "sylph", "undine", "salamander" };
            Material[] Elements = { Gnome, Sylph, Undine, Salamander };
            Material[] Crystals = { GnomeCrystal, SylphCrystal, UndineCrystal, SalamanderCrystal };
            int[][] param = { { 110, 124, 126 }, { 110, 126, 124 }, { 130, 112, 118 }, { 130, 112, 118 } };
            for (int i = 0; i < 4; i++) {
                ALCHEMY_CAULDRON_RECIPES.recipeBuilder("alchemy_" + names[i] + "_gas")
                        .circuitMeta(i + 1)
                        .inputItems(dust, Crystals[i], 8)
                        .inputFluids(FractalPetalSolvent.getFluid(1000))
                        .chancedOutput(Elements[i].getFluid(GAS, 4 * L), 10, 0)
                        .chancedOutput(FractalPetalSolvent.getFluid(800), 8000, 0)
                        .duration(80)
                        .temperature(650)
                        .addData("param1", param[i][0])
                        .addData("param2", param[i][1])
                        .addData("param3", param[i][2])
                        .save();

                ALCHEMY_CAULDRON_RECIPES.recipeBuilder("alchemy_" + names[i] + 4 + "_gas")
                        .circuitMeta(i + 6)
                        .inputItems(dust, Crystals[i], 8)
                        .inputFluids(CycleofBlossomsSolvent.getFluid(1000))
                        .chancedOutput(Elements[i].getFluid(GAS, 8 * L), 10, 0)
                        .chancedOutput(CycleofBlossomsSolvent.getFluid(900), 9500, 0)
                        .duration(650)
                        .MANAt(2)
                        .temperature(800)
                        .addData("param1", param[i][0] + 20)
                        .addData("param2", param[i][1] + 20)
                        .addData("param3", param[i][2] + 20)
                        .save();
            }

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("alchemy_aether_1_gas")
                    .circuitMeta(5)
                    .inputItems(dust, PerditioCrystal, 8)
                    .inputFluids(FractalPetalSolvent.getFluid(1000))
                    .chancedOutput(Aether.getFluid(GAS, 2 * L), 1, 0)
                    .chancedOutput(FractalPetalSolvent.getFluid(600), 6000, 0)
                    .duration(80)
                    .temperature(650)
                    .addData("param1", 120)
                    .addData("param2", 120)
                    .addData("param3", 120)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("alchemy_aether_2_gas")
                    .circuitMeta(10)
                    .inputItems(dust, PerditioCrystal, 8)
                    .inputFluids(CycleofBlossomsSolvent.getFluid(1000))
                    .chancedOutput(Aether.getFluid(GAS, 4 * L), 1, 0)
                    .chancedOutput(CycleofBlossomsSolvent.getFluid(800), 8000, 0)
                    .duration(650)
                    .MANAt(2)
                    .temperature(800)
                    .addData("param1", 140)
                    .addData("param2", 140)
                    .addData("param3", 140)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("alchemy_ender_eye")
                    .inputItems(RegistriesUtils.getItem("torchmaster:frozen_pearl"))
                    .chancedInput(new ItemStack(RegistriesUtils.getItem("mythicbotany:muspelheim_rune")), 2000, 0)
                    .inputFluids(Salamander.getFluid(GAS, 8000))
                    .chancedOutput(new ItemStack(Items.ENDER_EYE), 3000, 0)
                    .duration(650)
                    .MANAt(1)
                    .temperature(800)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("alchemy_herbs_ingot")
                    .inputItems(BotaniaItems.terrasteel)
                    .inputItems(dust, GnomeCrystal)
                    .inputItems(dust, SylphCrystal)
                    .inputItems(dust, UndineCrystal)
                    .inputItems(dust, SalamanderCrystal)
                    .chancedInput(new ItemStack(ItemsRegistry.MANIPULATION_ESSENCE), 500, 0)
                    .inputFluids(CycleofBlossomsSolvent.getFluid(500))
                    .chancedOutput(CycleofBlossomsSolvent.getFluid(300), 5000, 0)
                    .outputItems(ingot, Herbs)
                    .duration(600)
                    .temperature(1200)
                    .MANAt(8)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("phantomic_electrolyte_buffer")
                    .inputItems(dust, EDTA)
                    .inputItems(Items.PHANTOM_MEMBRANE, 4)
                    .chancedInput(Animium.getFluid(1000), 500, 0)
                    .inputFluids(CycleofBlossomsSolvent.getFluid(500))
                    .chancedOutput(PhantomicElectrolyteBuffer.getFluid(300), 5000, 0)
                    .chancedOutput(Ethylenediamine.getFluid(50), 500, 0)
                    .duration(600)
                    .temperature(1200)
                    .MANAt(32)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("alchemy_unstable_gaia_soul")
                    .chancedInput(new ItemStack(BotaniaBlocks.gaiaPylon.asItem()), 500, 0)
                    .inputItems(GAIA_CORE)
                    .inputItems(ItemsRegistry.MANIPULATION_ESSENCE)
                    .inputItems(BotaniaItems.manaPowder, 4)
                    .inputItems(BotaniaItems.pixieDust, 4)
                    .inputFluids(GaiaSolvent.getFluid(8000))
                    .outputItems(UNSTABLE_GAIA_SOUL)
                    .duration(240)
                    .temperature(1600)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.builder("wilden_horn")
                    .notConsumable(BotaniaBlocks.spawnerClaw.asItem())
                    .notConsumable(ItemsRegistry.WILDEN_HORN)
                    .chancedInput(new ItemStack(GTOItems.WILDEN_SLATE), 5, 0)
                    .inputItems(dust, Meat, 16)
                    .inputFluids(WildenEssence.getFluid(1000))
                    .outputItems(ItemsRegistry.WILDEN_HORN, 16)
                    .MANAt(4)
                    .duration(650)
                    .temperature(1400)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.builder("wilden_spike")
                    .notConsumable(BotaniaBlocks.spawnerClaw.asItem())
                    .notConsumable(ItemsRegistry.WILDEN_SPIKE)
                    .chancedInput(new ItemStack(GTOItems.WILDEN_SLATE), 5, 0)
                    .inputItems(dust, Meat, 16)
                    .inputFluids(WildenEssence.getFluid(1000))
                    .outputItems(ItemsRegistry.WILDEN_SPIKE, 16)
                    .MANAt(4)
                    .duration(600)
                    .temperature(1400)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.builder("wilden_wing")
                    .notConsumable(BotaniaBlocks.spawnerClaw.asItem())
                    .notConsumable(ItemsRegistry.WILDEN_WING)
                    .chancedInput(new ItemStack(GTOItems.WILDEN_SLATE), 5, 0)
                    .inputItems(dust, Meat, 16)
                    .inputFluids(WildenEssence.getFluid(1000))
                    .outputItems(ItemsRegistry.WILDEN_WING, 16)
                    .MANAt(4)
                    .duration(600)
                    .temperature(1400)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("wilden_tribute")
                    .notConsumable(BotaniaItems.spawnerMover)
                    .inputItems(GTOItems.WILDEN_SLATE)
                    .inputItems(ItemsRegistry.MANIPULATION_ESSENCE)
                    .inputItems("apotheosis:epic_material")
                    .inputFluids(WildenEssence.getFluid(4000))
                    .outputItems(ItemsRegistry.WILDEN_TRIBUTE)
                    .duration(240)
                    .MANAt(8)
                    .temperature(1600)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("spirit_fragment")
                    .inputItems(ExtraBotanyItems.spiritFragment)
                    .inputFluids(new FluidStack(EIOFluids.XP_JUICE.get().getSource(), 100))
                    .inputFluids(TheWaterFromTheWellOfWisdom.getFluid(50))
                    .outputFluids(Animium.getFluid(100))
                    .duration(20)
                    .MANAt(16)
                    .temperature(1200)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("flowing_ciphers_1")
                    .notConsumable("mythicbotany:fimbultyr_tablet")
                    .inputItems(block, Runerock)
                    .inputFluids(TheWaterFromTheWellOfWisdom.getFluid(1000))
                    .inputFluids(Animium.getFluid(100))
                    .outputFluids(FlowingCiphers.getFluid(500))
                    .duration(400)
                    .MANAt(16)
                    .temperature(1600)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("flowing_ciphers_2")
                    .notConsumable("mythicbotany:fimbultyr_tablet")
                    .inputItems(ingot, Runerock)
                    .inputFluids(TheWaterFromTheWellOfWisdom.getFluid(100))
                    .inputFluids(Animium.getFluid(10))
                    .outputFluids(FlowingCiphers.getFluid(50))
                    .duration(40)
                    .MANAt(16)
                    .temperature(1600)
                    .save();
        }

        // 炼金锅2
        {
            String[] names = { "gnome", "sylph", "undine", "salamander", "aether" };
            Material[] Elements = { Gnome, Sylph, Undine, Salamander, Aether };
            for (int i = 0; i < 5; i++) {
                ALCHEMY_CAULDRON_RECIPES.recipeBuilder("alchemy_" + names[i] + "_fluid")
                        .circuitMeta(i + 1)
                        .inputFluids(Elements[i].getFluid(GAS, 1000))
                        .outputFluids(Elements[i].getFluid(LIQUID, 30))
                        .duration(1200)
                        .MANAt(24)
                        .temperature(1400)
                        .save();

                ALCHEMY_CAULDRON_RECIPES.recipeBuilder("alchemy_" + names[i] + "_fluid_fust")
                        .circuitMeta(i + 6)
                        .inputItems(HELIO_COAL)
                        .inputFluids(Elements[i].getFluid(GAS, 1000))
                        .outputFluids(Elements[i].getFluid(LIQUID, 50))
                        .duration(100)
                        .MANAt(24)
                        .temperature(1400)
                        .save();

            }

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("heros_brawlers")
                    .chancedInput(getGem(5, "apotheosis:core/brawlers"), 5, 0)
                    .inputItems(GTOItems.HEROS_SOUL)
                    .inputItems(Tags.ENCHANTMENT_ESSENCE)
                    .inputItems(ItemsRegistry.EARTH_ESSENCE)
                    .inputItems(ItemsRegistry.AIR_ESSENCE)
                    .inputFluids(TheWaterFromTheWellOfWisdom.getFluid(2000))
                    .inputFluids(Gnome.getFluid(LIQUID, 100))
                    .inputFluids(Sylph.getFluid(LIQUID, 100))
                    .chancedOutput(HerosBrawlers.getFluid(2000), 10, 0)
                    .addData("param1", 40)
                    .addData("param2", 40)
                    .addData("param3", 160)
                    .duration(200)
                    .MANAt(32)
                    .temperature(1600)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("heros_breach")
                    .chancedInput(getGem(5, "apotheosis:core/breach"), 5, 0)
                    .inputItems(GTOItems.HEROS_SOUL)
                    .inputItems(Tags.AFFIX_ESSENCE)
                    .inputItems(ItemsRegistry.WATER_ESSENCE)
                    .inputItems(ItemsRegistry.FIRE_ESSENCE)
                    .inputFluids(TheWaterFromTheWellOfWisdom.getFluid(2000))
                    .inputFluids(Undine.getFluid(LIQUID, 100))
                    .inputFluids(Salamander.getFluid(LIQUID, 100))
                    .chancedOutput(HerosBreach.getFluid(2000), 10, 0)
                    .addData("param1", 40)
                    .addData("param2", 60)
                    .addData("param3", 140)
                    .duration(200)
                    .MANAt(32)
                    .temperature(1600)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("heros_splendor")
                    .chancedInput(getGem(5, "apotheosis:core/splendor"), 5, 0)
                    .inputItems(GTOItems.HEROS_SOUL)
                    .inputItems(ItemsRegistry.MANIPULATION_ESSENCE)
                    .inputItems(ItemsRegistry.ABJURATION_ESSENCE)
                    .inputItems(ItemsRegistry.CONJURATION_ESSENCE)
                    .inputFluids(TheWaterFromTheWellOfWisdom.getFluid(2000))
                    .inputFluids(FlowingCiphers.getFluid(LIQUID, 500))
                    .inputFluids(Aether.getFluid(LIQUID, 100))
                    .chancedOutput(HerosSplendor.getFluid(2000), 10, 0)
                    .addData("param1", 60)
                    .addData("param2", 40)
                    .addData("param3", 140)
                    .duration(200)
                    .MANAt(32)
                    .temperature(1600)
                    .save();

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("hero_medal")
                    .notConsumable(ExtraBotanyItems.voidArchives)
                    .inputItems(GTOItems.HEROS_SOUL, 4)
                    .inputItems(GTOItems.GOLD_MEDAL)
                    .inputItems(Adventure.Items.MYTHIC_MATERIAL.get(), 4)
                    .inputFluids(HerosBrawlers.getFluid(1000))
                    .inputFluids(HerosBreach.getFluid(1000))
                    .inputFluids(HerosSplendor.getFluid(1000))
                    .outputItems(ExtraBotanyItems.heroMedal)
                    .duration(200)
                    .MANAt(32)
                    .temperature(1600)
                    .save();

        }

        // 产线的各种配方
        {

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("ribbon"), RIBBON.asItem(),
                    "ABA", "BAB", "ABA",
                    'A', new ItemStack(BotaniaItems.manaString), 'B', new ItemStack(BotaniaItems.spellCloth));

            FORMING_PRESS_RECIPES.builder("gold_medal")
                    .notConsumable(GTItems.SHAPE_MOLD_BALL.asItem())
                    .inputItems(GTOItems.RIBBON.asItem())
                    .inputItems("extrabotany:das_rheingold", 4)
                    .inputItems(ingot, Laureril, 4)
                    .inputItems(ingot, Quicksilver, 4)
                    .outputItems(GTOItems.GOLD_MEDAL.asItem())
                    .EUt(VA[LV])
                    .duration(20)
                    .save();
        }

        // 机械方块
        {
            ASSEMBLER_RECIPES.recipeBuilder("infused_gold_reinforced_wooden_casing")
                    .inputItems(TagPrefix.frameGt, GTMaterials.Wood)
                    .inputItems(RegistriesUtils.getItemStack("ars_nouveau:archwood_planks"), 4)
                    .inputItems(TagPrefix.screw, InfusedGold, 8)
                    .inputItems(TagPrefix.plate, InfusedGold, 2)
                    .circuitMeta(6)
                    .outputItems(GTOBlocks.INFUSED_GOLD_REINFORCED_WOODEN_CASING.asItem())
                    .EUt(16)
                    .duration(50)
                    .save();

            ASSEMBLER_RECIPES.builder("infused_gold_casing")
                    .inputItems(TagPrefix.plate, GTOMaterials.InfusedGold, 6)
                    .inputItems(TagPrefix.frameGt, GTOMaterials.InfusedGold)
                    .outputItems(GTOBlocks.INFUSED_GOLD_CASING.asItem())
                    .circuitMeta(6)
                    .duration(50)
                    .EUt(16)
                    .save();

            ASSEMBLER_RECIPES.recipeBuilder("source_stone_casing")
                    .inputItems(gem, GTOMaterials.SourceGem)
                    .inputItems(RegistriesUtils.getItemStack("ars_nouveau:smooth_sourcestone"), 4)
                    .inputItems(TagPrefix.screw, InfusedGold, 8)
                    .inputItems(TagPrefix.plate, InfusedGold, 2)
                    .circuitMeta(6)
                    .outputItems(GTOBlocks.SOURCE_STONE_CASING.asItem())
                    .EUt(16)
                    .duration(50)
                    .save();

            ASSEMBLER_RECIPES.recipeBuilder("source_fiber_mechanical_casing")
                    .inputItems(gem, GTOMaterials.SourceGem)
                    .inputItems(RegistriesUtils.getItemStack("ars_nouveau:sourcestone"), 2)
                    .inputItems(RegistriesUtils.getItemStack("ars_nouveau:magebloom_block"), 2)
                    .inputItems(ItemsRegistry.MAGE_FIBER, 4)
                    .circuitMeta(6)
                    .outputItems(GTOBlocks.SOURCE_FIBER_MECHANICAL_CASING.asItem())
                    .EUt(16)
                    .duration(50)
                    .save();

            ASSEMBLER_RECIPES.recipeBuilder("spell_prism_casing")
                    .inputItems(RegistriesUtils.getItemStack("ars_nouveau:magebloom_fiber"))
                    .inputItems(RegistriesUtils.getItemStack("ars_nouveau:spell_prism"), 4)
                    .inputItems(RegistriesUtils.getItemStack("ars_nouveau:archwood_planks"), 2)
                    .inputItems(TagPrefix.screw, InfusedGold, 8)
                    .circuitMeta(6)
                    .outputItems(GTOBlocks.SPELL_PRISM_CASING.asItem())
                    .EUt(16)
                    .duration(50)
                    .save();

            VanillaRecipeHelper.addShapedRecipe(id("original_bronze_casing"), GTOBlocks.ORIGINAL_BRONZE_CASING.asItem(),
                    "CCC", "CBC", "CCC",
                    'B', new MaterialEntry(frameGt, OriginalBronze), 'C', new MaterialEntry(plate, OriginalBronze));

            VanillaRecipeHelper.addShapedRecipe(id("manasteel_casing"), GTOBlocks.MANASTEEL_CASING.asItem(),
                    "CCC", "CBC", "CCC",
                    'B', new MaterialEntry(frameGt, Manasteel), 'C', new MaterialEntry(plate, Manasteel));

            ASSEMBLER_RECIPES.builder("terrasteel_casing")
                    .inputItems(TagPrefix.frameGt, GTOMaterials.Terrasteel)
                    .inputItems(gem, GTOMaterials.ManaDiamond, 4)
                    .inputItems(TagPrefix.plate, GTOMaterials.Terrasteel, 2)
                    .outputItems(GTOBlocks.TERRASTEEL_CASING.asItem())
                    .inputFluids(GTOMaterials.Mana, 288)
                    .circuitMeta(6)
                    .duration(200)
                    .MANAt(8)
                    .save();

            ASSEMBLER_RECIPES.builder("elementium_casing")
                    .inputItems(TagPrefix.frameGt, GTOMaterials.Elementium)
                    .inputItems(TagPrefix.plate, GTOMaterials.Dreamwood, 4)
                    .inputItems(TagPrefix.plate, GTOMaterials.Elementium, 2)
                    .outputItems(GTOBlocks.ELEMENTIUM_CASING.asItem())
                    .inputFluids(GTOMaterials.Manasteel, 288)
                    .circuitMeta(6)
                    .duration(200)
                    .MANAt(16)
                    .save();

            ASSEMBLER_RECIPES.builder("alfsteel_casing")
                    .inputItems(TagPrefix.frameGt, GTOMaterials.Alfsteel)
                    .inputItems("extrabotany:gaia_quartz_block")
                    .inputItems("extrabotany:elementium_quartz_block")
                    .inputItems(TagPrefix.plate, GTOMaterials.Alfsteel, 2)
                    .outputItems(GTOBlocks.ALFSTEEL_CASING.asItem())
                    .inputFluids(GTOMaterials.Terrasteel, 288)
                    .circuitMeta(6)
                    .duration(200)
                    .MANAt(32)
                    .save();

            ASSEMBLER_RECIPES.builder("gaiasteel_casing")
                    .inputItems(TagPrefix.frameGt, GTOMaterials.Gaiasteel)
                    .inputItems("botania:elf_quartz")
                    .inputItems(TagPrefix.plate, GTOMaterials.Gaiasteel, 2)
                    .outputItems(GTOBlocks.GAIASTEEL_CASING.asItem())
                    .inputFluids(GTOMaterials.Elementium, 288)
                    .circuitMeta(6)
                    .duration(200)
                    .MANAt(64)
                    .save();

            ASSEMBLER_RECIPES.builder("orichalcos_casing")
                    .inputItems(TagPrefix.frameGt, GTOMaterials.Orichalcos)
                    .inputItems("extrabotany:dimension_catalyst")
                    .inputItems(TagPrefix.plate, GTOMaterials.Orichalcos, 2)
                    .outputItems(GTOBlocks.ORICHALCOS_CASING.asItem())
                    .inputFluids(GTOMaterials.Alfsteel, 288)
                    .circuitMeta(6)
                    .duration(200)
                    .MANAt(128)
                    .save();

            ASSEMBLER_RECIPES.builder("heretical_mechanical_casing")
                    .inputItems(Enchantment.getEnchantedBookBySerialNumber(2, 1))
                    .inputItems(Enchantment.getEnchantedBookBySerialNumber(9, 1))
                    .inputItems(Enchantment.getEnchantedBookBySerialNumber(14, 1))
                    .inputItems(Enchantment.getEnchantedBookBySerialNumber(34, 1))
                    .inputItems(GTOBlocks.ORIGINAL_BRONZE_CASING, 4)
                    .inputItems(Enchantment.getEnchantedBookBySerialNumber(35, 1))
                    .inputItems(Enchantment.getEnchantedBookBySerialNumber(37, 1))
                    .inputItems(Enchantment.getEnchantedBookBySerialNumber(40, 1))
                    .inputItems(Enchantment.getEnchantedBookBySerialNumber(76, 1))
                    .outputItems(GTOBlocks.HERETICAL_MECHANICAL_CASING, 4)
                    .inputFluids(Aether.getFluid(LIQUID, 1000))
                    .duration(200)
                    .MANAt(32)
                    .save();

            ASSEMBLER_RECIPES.builder("heretical_mechanical_casing_2")
                    .inputItems(ENCHANTMENT_ESSENCE[2])
                    .inputItems(ENCHANTMENT_ESSENCE[9])
                    .inputItems(ENCHANTMENT_ESSENCE[14])
                    .inputItems(ENCHANTMENT_ESSENCE[34])
                    .inputItems(GTOBlocks.ORIGINAL_BRONZE_CASING, 4)
                    .inputItems(ENCHANTMENT_ESSENCE[35])
                    .inputItems(ENCHANTMENT_ESSENCE[37])
                    .inputItems(ENCHANTMENT_ESSENCE[40])
                    .inputItems(ENCHANTMENT_ESSENCE[76])
                    .outputItems(GTOBlocks.HERETICAL_MECHANICAL_CASING, 4)
                    .inputFluids(Aether.getFluid(LIQUID, 1000))
                    .duration(200)
                    .MANAt(32)
                    .save();
        }

        // 结构主方块
        {
            ASSEMBLER_RECIPES.builder("mana_infuser")
                    .inputItems(TagPrefix.frameGt, GTOMaterials.Manasteel)
                    .inputItems(CustomTags.MV_CIRCUITS)
                    .inputItems(GTItems.ELECTRIC_PUMP_MV.asItem(), 2)
                    .inputItems(TagPrefix.gem, GTOMaterials.ManaDiamond, 8)
                    .inputItems("botania:mana_pearl", 8)
                    .inputItems(TagPrefix.plate, GTOMaterials.Livingwood, 4)
                    .outputItems(ManaMultiBlock.MANA_INFUSER.asItem())
                    .inputFluids(GTOMaterials.Mana, 288)
                    .duration(200)
                    .MANAt(32)
                    .save();

            ASSEMBLER_RECIPES.builder("mana_condenser")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.IV].asItem())
                    .inputItems(GTItems.FIELD_GENERATOR_HV.asItem(), 2)
                    .inputItems(CustomTags.HV_CIRCUITS)
                    .inputItems(TagPrefix.gemExquisite, GTOMaterials.ManaDiamond, 4)
                    .inputItems(TagPrefix.gear, GTOMaterials.Terrasteel, 4)
                    .inputItems("botania:mana_pearl", 4)
                    .inputItems("mythicbotany:midgard_rune")
                    .inputItems("botania:rune_mana")
                    .inputItems("botania:rune_lust")
                    .outputItems(ManaMultiBlock.MANA_CONDENSER.asItem())
                    .inputFluids(Gaiasteel, 288)
                    .duration(200)
                    .MANAt(128)
                    .save();

            ASSEMBLER_RECIPES.builder("elf_exchange")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.HV].asItem())
                    .inputItems(GTItems.FIELD_GENERATOR_MV.asItem(), 2)
                    .inputItems(CustomTags.MV_CIRCUITS)
                    .inputItems(TagPrefix.gemExquisite, GTOMaterials.Dragonstone, 4)
                    .inputItems(TagPrefix.gear, GTOMaterials.Alfsteel, 4)
                    .inputItems(BotaniaItems.pixieDust, 4)
                    .inputItems(BotaniaBlocks.alfPortal, 4)
                    .inputItems("mythicbotany:alfheim_rune")
                    .inputItems("mythicbotany:asgard_rune")
                    .outputItems(ManaMultiBlock.ELF_EXCHANGE.asItem())
                    .inputFluids(GTOMaterials.Alfsteel, 288)
                    .duration(200)
                    .MANAt(128)
                    .save();

            ASSEMBLER_RECIPES.builder("industrial_altar")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.HV].asItem())
                    .inputItems(GTItems.FIELD_GENERATOR_MV.asItem(), 2)
                    .inputItems(CustomTags.MV_CIRCUITS)
                    .inputItems(block, GTOMaterials.Runerock, 4)
                    .inputItems(TagPrefix.gear, GTOMaterials.Elementium, 4)
                    .inputItems(BotaniaBlocks.pistonRelay, 4)
                    .inputItems("botania:runic_altar")
                    .inputItems("botania:apothecary_livingrock")
                    .inputItems("botania:brewery")
                    .outputItems(ManaMultiBlock.INDUSTRIAL_ALTAR.asItem())
                    .inputFluids(GTOMaterials.Elementium, 288)
                    .duration(200)
                    .MANAt(128)
                    .save();

            ASSEMBLER_RECIPES.builder("mana_garden")
                    .inputItems(TagPrefix.frameGt, GTOMaterials.Terrasteel)
                    .inputItems("botania:spawner_mover")
                    .inputItems("botania:mana_fluxfield", 16)
                    .inputItems(TagPrefix.plate, GTOMaterials.Elementium, 8)
                    .inputItems("botania:gaia_spreader", 4)
                    .inputItems("botania:prism", 4)
                    .inputItems("botania:pump", 8)
                    .inputItems("botania:conjuration_catalyst", 4)
                    .inputItems(TagPrefix.plateDouble, GTOMaterials.Terrasteel, 8)
                    .outputItems(ManaMultiBlock.MANA_GARDEN.asItem())
                    .duration(1600)
                    .MANAt(256)
                    .save();

            ASSEMBLER_RECIPES.builder("mana_greenhouse")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.EV].asItem())
                    .inputItems(GTItems.FIELD_GENERATOR_IV.asItem(), 8)
                    .inputItems(CustomTags.IV_CIRCUITS, 4)
                    .inputItems("botania:agricarnation", 32)
                    .inputItems("botania:hopperhock", 16)
                    .inputItems("botania:rune_spring", 4)
                    .inputItems(TagPrefix.gear, GTOMaterials.Elementium, 8)
                    .inputItems(TagPrefix.gemExquisite, GTOMaterials.Aerialite, 16)
                    .inputItems("botania:fertilizer", 64)
                    .outputItems(ManaMultiBlock.MANA_GREENHOUSE.asItem())
                    .inputFluids(GTOMaterials.Mana, 4608)
                    .duration(1200)
                    .MANAt(128)
                    .save();

            ASSEMBLER_RECIPES.builder("mana_alloy_blast_smelter")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.EV].asItem())
                    .inputItems(GTItems.FIELD_GENERATOR_IV.asItem(), 16)
                    .inputItems(CustomTags.IV_CIRCUITS, 16)
                    .inputItems(GCYMMachines.BLAST_ALLOY_SMELTER.asItem(), 4)
                    .inputItems("botania:exoflame", 32)
                    .inputItems("mythicbotany:exoblaze", 32)
                    .inputItems("botania:rune_pride", 8)
                    .inputItems("mythicbotany:nidavellir_rune", 8)
                    .inputItems(TagPrefix.gear, GTOMaterials.Orichalcos, 8)
                    .inputFluids(GTOMaterials.Aerialite, L * 16)
                    .outputItems(ManaMultiBlock.MANA_ALLOY_BLAST_SMELTER.asItem())
                    .duration(1200)
                    .MANAt(512)
                    .save();

            ASSEMBLER_RECIPES.builder("rune_engraving_chamber")
                    .inputItems("extrabotany:hero_medal", 4)
                    .inputItems(GTOBlocks.SPELL_PRISM_CASING.asItem(), 32)
                    .inputItems(GTOBlocks.ORICHALCOS_CASING.asItem(), 16)
                    .inputItems("botania:mana_bomb", 8)
                    .inputItems(GTItems.FIELD_GENERATOR_LuV.asItem(), 8)
                    .inputItems(GTItems.EMITTER_LuV.asItem(), 8)
                    .inputItems(TagPrefix.lens, GTOMaterials.BifrostPerm, 32)
                    .inputItems("ars_nouveau:manipulation_essence", 32)
                    .inputItems("ars_nouveau:wilden_tribute", 32)
                    .outputItems(ManaMultiBlock.RUNE_ENGRAVING_CHAMBER.asItem())
                    .inputFluids(GTOMaterials.TheWaterFromTheWellOfWisdom, 16000)
                    .duration(200)
                    .MANAt(4096)
                    .save();

            ASSEMBLER_RECIPES.builder("large_perfusion_device")
                    .inputItems(GTOBlocks.SOURCE_FIBER_MECHANICAL_CASING.asItem())
                    .inputItems(TagPrefix.block, GTOMaterials.SourceGem, 16)
                    .inputItems("ars_nouveau:summon_focus")
                    .inputItems("ars_nouveau:imbuement_chamber", 8)
                    .inputItems("ars_nouveau:arcane_core", 8)
                    .inputItems("ars_nouveau:enchanting_apparatus", 8)
                    .inputItems(GTItems.FIELD_GENERATOR_HV, 4)
                    .inputItems(CustomTags.IV_CIRCUITS, 4)
                    .inputItems("ars_nouveau:shapers_focus")
                    .outputItems(ManaMultiBlock.LARGE_PERFUSION_DEVICE.asItem())
                    .duration(800)
                    .MANAt(64)
                    .save();

            ASSEMBLER_RECIPES.builder("base_mana_distributor")
                    .inputItems(TagPrefix.frameGt, GTOMaterials.OriginalBronze)
                    .inputItems("botania:mana_distributor", 4)
                    .inputItems("botania:mana_gun", 4)
                    .inputItems(TagPrefix.bolt, GTOMaterials.Manasteel, 8)
                    .inputItems(TagPrefix.plate, GTOMaterials.Livingrock, 16)
                    .outputItems(ManaMultiBlock.BASE_MANA_DISTRIBUTOR.asItem())
                    .duration(200)
                    .MANAt(8)
                    .save();

            ASSEMBLER_RECIPES.builder("advanced_mana_distributor")
                    .inputItems(ManaMultiBlock.BASE_MANA_DISTRIBUTOR.asItem())
                    .inputItems(GTItems.FIELD_GENERATOR_MV.asItem(), 4)
                    .inputItems(TagPrefix.plate, GTOMaterials.Terrasteel, 8)
                    .inputItems(TagPrefix.screw, GTOMaterials.Manasteel, 32)
                    .inputItems("botania:rune_mana", 32)
                    .inputItems("botania:mana_tablet", 2)
                    .inputItems(BotaniaBlocks.manaBomb.asItem())
                    .outputItems(ManaMultiBlock.ADVANCED_MANA_DISTRIBUTOR.asItem())
                    .duration(800)
                    .MANAt(128)
                    .save();

            ASSEMBLER_RECIPES.builder("the_primordial_reconstructor")
                    .inputItems(Items.GRINDSTONE.asItem(), 16)
                    .inputItems("apotheosis:gem_cutting_table", 16)
                    .inputItems(Items.ANVIL.asItem(), 16)
                    .inputItems("apotheosis:reforging_table", 16)
                    .inputItems(GTOBlocks.HERETICAL_MECHANICAL_CASING.asItem(), 16)
                    .inputItems("apotheosis:augmenting_table", 16)
                    .inputItems("apotheosis:salvaging_table", 16)
                    .inputItems("apotheosis:ender_library", 16)
                    .inputItems(Items.SMITHING_TABLE.asItem(), 16)
                    .outputItems(ManaMultiBlock.THE_PRIMORDIAL_RECONSTRUCTOR.asItem())
                    .inputFluids(GTOMaterials.TheWaterFromTheWellOfWisdom, 8000)
                    .duration(200)
                    .MANAt(1024)
                    .save();
        }

        // 魔力仓室
        {
            ASSEMBLER_RECIPES.builder("lv_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.LV].asItem())
                    .inputItems(BotaniaBlocks.pump, 2)
                    .inputItems("botania:mana_pylon")
                    .inputItems(TagPrefix.screw, GTOMaterials.Manasteel, 4)
                    .circuitMeta(1)
                    .outputItems(ManaMachine.MANA_INPUT_HATCH[LV].asItem())
                    .duration(200)
                    .MANAt(4)
                    .save();

            ASSEMBLER_RECIPES.builder("mv_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.MV].asItem())
                    .inputItems(BotaniaBlocks.pump, 2)
                    .inputItems(TagPrefix.lens, GTOMaterials.ManaGlass)
                    .inputItems(TagPrefix.screw, GTOMaterials.Terrasteel, 4)
                    .circuitMeta(1)
                    .outputItems(ManaMachine.MANA_INPUT_HATCH[MV].asItem())
                    .duration(200)
                    .MANAt(16)
                    .save();

            ASSEMBLER_RECIPES.builder("hv_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.HV].asItem())
                    .inputItems(BotaniaBlocks.pump, 2)
                    .inputItems("botania:natura_pylon")
                    .inputItems(TagPrefix.lens, GTOMaterials.ElfGlass)
                    .inputItems(TagPrefix.screw, GTOMaterials.Elementium, 4)
                    .circuitMeta(1)
                    .outputItems(ManaMachine.MANA_INPUT_HATCH[HV].asItem())
                    .inputFluids(GTOMaterials.BifrostPerm, 1000)
                    .duration(200)
                    .MANAt(64)
                    .save();

            ASSEMBLER_RECIPES.builder("ev_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.EV].asItem())
                    .inputItems("botania:prism", 2)
                    .inputItems("mythicbotany:alfheim_rune")
                    .inputItems("endrem:magical_eye")
                    .inputItems(TagPrefix.screw, GTOMaterials.Aerialite, 4)
                    .circuitMeta(1)
                    .outputItems(ManaMachine.MANA_INPUT_HATCH[EV].asItem())
                    .inputFluids(GTOMaterials.BifrostPerm, 2000)
                    .duration(200)
                    .MANAt(256)
                    .save();

            ASSEMBLER_RECIPES.builder("iv_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.IV].asItem())
                    .inputItems("botania:prism", 2)
                    .inputItems("botania:gaia_pylon")
                    .inputItems(TagPrefix.screw, GTOMaterials.Gaia, 4)
                    .circuitMeta(1)
                    .outputItems(ManaMachine.MANA_INPUT_HATCH[IV].asItem())
                    .inputFluids(GTOMaterials.Animium, 1000)
                    .duration(200)
                    .MANAt(1024)
                    .save();

            ASSEMBLER_RECIPES.builder("luv_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.LuV].asItem())
                    .inputItems("botania:prism", 2)
                    .inputItems(ExtraBotanyItems.theUniverse)
                    .inputItems(TagPrefix.lens, GTOMaterials.BifrostPerm)
                    .inputItems(TagPrefix.screw, GTOMaterials.Orichalcos, 4)
                    .circuitMeta(1)
                    .outputItems(ManaMachine.MANA_INPUT_HATCH[LuV].asItem())
                    .inputFluids(GTOMaterials.Animium, 2000)
                    .duration(200)
                    .MANAt(4096)
                    .save();

            ASSEMBLER_RECIPES.builder("lv_wireless_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_INPUT_HATCH[LV].asItem())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:spark", 8)
                    .inputItems("botania:rune_mana", 4)
                    .outputItems(ManaMachine.WIRELESS_MANA_INPUT_HATCH[LV].asItem())
                    .inputFluids(FlowingCiphers.getFluid(100))
                    .duration(200)
                    .MANAt(4)
                    .save();

            ASSEMBLER_RECIPES.builder("mv_wireless_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_INPUT_HATCH[MV].asItem())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:spark", 16)
                    .inputItems("botania:rune_mana", 8)
                    .outputItems(ManaMachine.WIRELESS_MANA_INPUT_HATCH[MV].asItem())
                    .inputFluids(FlowingCiphers.getFluid(200))
                    .duration(200)
                    .MANAt(16)
                    .save();

            ASSEMBLER_RECIPES.builder("hv_wireless_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_INPUT_HATCH[HV].asItem())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:corporea_spark", 16)
                    .inputItems(ManaMultiBlock.BASE_MANA_DISTRIBUTOR, 4)
                    .outputItems(ManaMachine.WIRELESS_MANA_INPUT_HATCH[HV].asItem())
                    .inputFluids(FlowingCiphers.getFluid(400))
                    .duration(200)
                    .MANAt(64)
                    .save();

            ASSEMBLER_RECIPES.builder("ev_wireless_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_INPUT_HATCH[EV].asItem())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:corporea_spark", 32)
                    .inputItems(ManaMultiBlock.BASE_MANA_DISTRIBUTOR, 8)
                    .outputItems(ManaMachine.WIRELESS_MANA_INPUT_HATCH[EV].asItem())
                    .inputFluids(FlowingCiphers.getFluid(600))
                    .duration(200)
                    .MANAt(256)
                    .save();

            ASSEMBLER_RECIPES.builder("iv_wireless_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_INPUT_HATCH[IV].asItem())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:corporea_index", 8)
                    .inputItems(ManaMultiBlock.ADVANCED_MANA_DISTRIBUTOR, 2)
                    .outputItems(ManaMachine.WIRELESS_MANA_INPUT_HATCH[IV].asItem())
                    .inputFluids(FlowingCiphers.getFluid(800))
                    .duration(200)
                    .MANAt(1024)
                    .save();

            ASSEMBLER_RECIPES.builder("luv_wireless_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_INPUT_HATCH[LuV].asItem())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:corporea_index", 16)
                    .inputItems(ManaMultiBlock.ADVANCED_MANA_DISTRIBUTOR, 4)
                    .outputItems(ManaMachine.WIRELESS_MANA_INPUT_HATCH[LuV].asItem())
                    .inputFluids(FlowingCiphers.getFluid(1000))
                    .duration(200)
                    .MANAt(4096)
                    .save();

            ASSEMBLER_RECIPES.builder("lv_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.LV].asItem())
                    .inputItems(BotaniaBlocks.pump, 2)
                    .inputItems("botania:mana_pylon")
                    .inputItems(TagPrefix.screw, GTOMaterials.Manasteel, 4)
                    .circuitMeta(2)
                    .outputItems(ManaMachine.MANA_OUTPUT_HATCH[LV].asItem())
                    .duration(200)
                    .MANAt(4)
                    .save();

            ASSEMBLER_RECIPES.builder("mv_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.MV].asItem())
                    .inputItems(BotaniaBlocks.pump, 2)
                    .inputItems(TagPrefix.lens, GTOMaterials.ManaGlass)
                    .inputItems(TagPrefix.screw, GTOMaterials.Terrasteel, 4)
                    .circuitMeta(2)
                    .outputItems(ManaMachine.MANA_OUTPUT_HATCH[MV].asItem())
                    .duration(200)
                    .MANAt(16)
                    .save();

            ASSEMBLER_RECIPES.builder("hv_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.HV].asItem())
                    .inputItems(BotaniaBlocks.pump, 2)
                    .inputItems("botania:natura_pylon")
                    .inputItems(TagPrefix.lens, GTOMaterials.ElfGlass)
                    .inputItems(TagPrefix.screw, GTOMaterials.Elementium, 4)
                    .circuitMeta(2)
                    .outputItems(ManaMachine.MANA_OUTPUT_HATCH[HV].asItem())
                    .inputFluids(GTOMaterials.BifrostPerm, 1000)
                    .duration(200)
                    .MANAt(16)
                    .save();

            ASSEMBLER_RECIPES.builder("ev_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.EV].asItem())
                    .inputItems("botania:prism", 2)
                    .inputItems("mythicbotany:alfheim_rune")
                    .inputItems("endrem:magical_eye")
                    .inputItems(TagPrefix.screw, GTOMaterials.Aerialite, 4)
                    .circuitMeta(2)
                    .outputItems(ManaMachine.MANA_OUTPUT_HATCH[EV].asItem())
                    .inputFluids(GTOMaterials.BifrostPerm, 2000)
                    .duration(200)
                    .MANAt(256)
                    .save();

            ASSEMBLER_RECIPES.builder("iv_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.IV].asItem())
                    .inputItems("botania:prism", 2)
                    .inputItems("botania:gaia_pylon")
                    .inputItems(TagPrefix.screw, GTOMaterials.Gaia, 4)
                    .circuitMeta(2)
                    .outputItems(ManaMachine.MANA_OUTPUT_HATCH[IV].asItem())
                    .inputFluids(GTOMaterials.Animium, 1000)
                    .duration(200)
                    .MANAt(1024)
                    .save();

            ASSEMBLER_RECIPES.builder("luv_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.LuV].asItem())
                    .inputItems("botania:prism", 2)
                    .inputItems(ExtraBotanyItems.theUniverse)
                    .inputItems(TagPrefix.lens, GTOMaterials.BifrostPerm)
                    .inputItems(TagPrefix.screw, GTOMaterials.Orichalcos, 4)
                    .circuitMeta(2)
                    .outputItems(ManaMachine.MANA_OUTPUT_HATCH[LuV].asItem())
                    .inputFluids(GTOMaterials.Animium, 2000)
                    .duration(200)
                    .MANAt(4096)
                    .save();

            ASSEMBLER_RECIPES.builder("lv_wireless_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_OUTPUT_HATCH[LV].asItem())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:spark", 8)
                    .inputItems("botania:rune_mana", 4)
                    .outputItems(ManaMachine.WIRELESS_MANA_OUTPUT_HATCH[LV].asItem())
                    .inputFluids(FlowingCiphers.getFluid(100))
                    .duration(200)
                    .MANAt(4)
                    .save();

            ASSEMBLER_RECIPES.builder("mv_wireless_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_OUTPUT_HATCH[MV].asItem())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:spark", 16)
                    .inputItems("botania:rune_mana", 8)
                    .outputItems(ManaMachine.WIRELESS_MANA_OUTPUT_HATCH[MV].asItem())
                    .inputFluids(FlowingCiphers.getFluid(200))
                    .duration(200)
                    .MANAt(16)
                    .save();

            ASSEMBLER_RECIPES.builder("hv_wireless_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_OUTPUT_HATCH[HV].asItem())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:corporea_spark", 16)
                    .inputItems(ManaMultiBlock.BASE_MANA_DISTRIBUTOR, 4)
                    .outputItems(ManaMachine.WIRELESS_MANA_OUTPUT_HATCH[HV].asItem())
                    .inputFluids(FlowingCiphers.getFluid(400))
                    .duration(200)
                    .MANAt(64)
                    .save();

            ASSEMBLER_RECIPES.builder("ev_wireless_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_OUTPUT_HATCH[EV].asItem())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:corporea_spark", 32)
                    .inputItems(ManaMultiBlock.BASE_MANA_DISTRIBUTOR, 8)
                    .outputItems(ManaMachine.WIRELESS_MANA_OUTPUT_HATCH[EV].asItem())
                    .inputFluids(FlowingCiphers.getFluid(600))
                    .duration(200)
                    .MANAt(256)
                    .save();

            ASSEMBLER_RECIPES.builder("iv_wireless_mana_output_hatch")
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems(ManaMachine.MANA_OUTPUT_HATCH[IV].asItem())
                    .inputItems("botania:corporea_index", 8)
                    .inputItems(ManaMultiBlock.ADVANCED_MANA_DISTRIBUTOR, 2)
                    .outputItems(ManaMachine.WIRELESS_MANA_OUTPUT_HATCH[IV].asItem())
                    .inputFluids(FlowingCiphers.getFluid(800))
                    .duration(200)
                    .MANAt(1024)
                    .save();

            ASSEMBLER_RECIPES.builder("luv_wireless_mana_output_hatch")
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems(ManaMachine.MANA_OUTPUT_HATCH[LuV].asItem())
                    .inputItems("botania:corporea_index", 16)
                    .inputItems(ManaMultiBlock.ADVANCED_MANA_DISTRIBUTOR, 4)
                    .outputItems(ManaMachine.WIRELESS_MANA_OUTPUT_HATCH[LuV].asItem())
                    .inputFluids(FlowingCiphers.getFluid(1000))
                    .duration(200)
                    .MANAt(4096)
                    .save();

            ASSEMBLER_RECIPES.builder("lv_mana_extract_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.LV].asItem())
                    .inputItems(CustomTags.LV_CIRCUITS)
                    .inputItems(TagPrefix.lens, GTOMaterials.ManaGlass)
                    .inputItems(GTItems.SENSOR_LV.asItem())
                    .inputItems(TagPrefix.gem, GTOMaterials.ManaDiamond)
                    .inputItems("botania:mana_pearl")
                    .inputItems(TagPrefix.screw, GTOMaterials.Manasteel, 4)
                    .outputItems(ManaMachine.MANA_EXTRACT_HATCH[LV].asItem())
                    .inputFluids(GTOMaterials.Mana, 288)
                    .duration(200)
                    .MANAt(4)
                    .save();

            ASSEMBLER_RECIPES.builder("mv_mana_extract_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.MV].asItem())
                    .inputItems(CustomTags.MV_CIRCUITS)
                    .inputItems(TagPrefix.lens, GTOMaterials.ElfGlass)
                    .inputItems(GTItems.SENSOR_MV.asItem())
                    .inputItems(TagPrefix.gem, GTOMaterials.Dragonstone)
                    .inputItems("botania:pixie_dust")
                    .inputItems(TagPrefix.screw, GTOMaterials.Elementium, 4)
                    .outputItems(ManaMachine.MANA_EXTRACT_HATCH[MV].asItem())
                    .inputFluids(GTOMaterials.Mana, 288)
                    .duration(200)
                    .MANAt(16)
                    .save();

            ASSEMBLER_RECIPES.builder("hv_mana_extract_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.HV].asItem())
                    .inputItems(CustomTags.HV_CIRCUITS)
                    .inputItems(TagPrefix.lens, GTOMaterials.BifrostPerm)
                    .inputItems(GTItems.SENSOR_HV.asItem())
                    .inputItems(TagPrefix.gemExquisite, GTOMaterials.Dragonstone)
                    .inputItems(TagPrefix.rodLong, GTOMaterials.Alfsteel)
                    .inputItems(TagPrefix.screw, GTOMaterials.Terrasteel, 4)
                    .outputItems(ManaMachine.MANA_EXTRACT_HATCH[HV].asItem())
                    .inputFluids(GTOMaterials.Mana, 288)
                    .duration(200)
                    .MANAt(64)
                    .save();

            ASSEMBLER_RECIPES.builder("ev_mana_extract_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.EV].asItem())
                    .inputItems(CustomTags.EV_CIRCUITS)
                    .inputItems("botania:natura_pylon")
                    .inputItems(GTItems.SENSOR_EV.asItem())
                    .inputItems("mythicbotany:alfheim_rune")
                    .inputItems(TagPrefix.rodLong, GTOMaterials.Gaiasteel)
                    .inputItems(TagPrefix.screw, GTOMaterials.Alfsteel, 4)
                    .outputItems(ManaMachine.MANA_EXTRACT_HATCH[EV].asItem())
                    .inputFluids(GTOMaterials.Mana, 1152)
                    .duration(200)
                    .MANAt(256)
                    .save();

            ASSEMBLER_RECIPES.builder("iv_mana_extract_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.IV].asItem())
                    .inputItems(CustomTags.IV_CIRCUITS)
                    .inputItems("mythicbotany:alfsteel_pylon")
                    .inputItems(GTItems.SENSOR_IV.asItem())
                    .inputItems("endrem:magical_eye")
                    .inputItems(TagPrefix.rodLong, GTOMaterials.Gaia)
                    .inputItems(TagPrefix.screw, GTOMaterials.Aerialite, 4)
                    .outputItems(ManaMachine.MANA_EXTRACT_HATCH[IV].asItem())
                    .inputFluids(GTOMaterials.Mana, 1152)
                    .duration(200)
                    .MANAt(1024)
                    .save();

            ASSEMBLER_RECIPES.builder("luv_mana_extract_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.LuV].asItem())
                    .inputItems(CustomTags.LuV_CIRCUITS)
                    .inputItems("botania:gaia_pylon")
                    .inputItems(GTItems.SENSOR_LuV.asItem())
                    .inputItems("botania:life_essence")
                    .inputItems(TagPrefix.rodLong, GTOMaterials.Aerialite)
                    .inputItems(TagPrefix.screw, GTOMaterials.Orichalcos, 4)
                    .outputItems(ManaMachine.MANA_EXTRACT_HATCH[LuV].asItem())
                    .inputFluids(GTOMaterials.Mana, 1152)
                    .duration(200)
                    .MANAt(4096)
                    .save();

            ASSEMBLER_RECIPES.builder("mana_amplifier_hatch")
                    .notConsumable("botania:dice")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.LuV].asItem())
                    .inputItems("botania:laputa_shard")
                    .inputItems("extrabotany:the_universe", 8)
                    .outputItems(ManaMachine.MANA_AMPLIFIER_HATCH.asItem())
                    .inputFluids(TheWaterFromTheWellOfWisdom.getFluid(8000))
                    .duration(800)
                    .MANAt(8192)
                    .save();

            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("alchemy_cauldron"), ManaMachine.ALCHEMY_CAULDRON.asItem(),
                    "BBB", "ADA", "AAA",
                    'A', new MaterialEntry(TagPrefix.plate, Steel), 'B', new MaterialEntry(rod, Steel), 'D', Items.CAULDRON);

            VanillaRecipeHelper.addShapedRecipe(id("mana_heater"), ManaMachine.MANA_HEATER.asItem(),
                    "EEE", "CDC", "CBC",
                    'B', ManaMachine.MANA_HULL[2].asItem(), 'C', RegistriesUtils.getItemStack("botania:livingrock_bricks"), 'D', RegistriesUtils.getItemStack("botania:mana_pool"), 'E', new MaterialEntry(plate, Manasteel));

            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("lv_primitive_magic_energy"), ManaMachine.PRIMITIVE_MAGIC_ENERGY[GTValues.LV].asItem(),
                    "ABA", "BCB", "DBD",
                    'A', new MaterialEntry(TagPrefix.lens, GTOMaterials.ManaGlass), 'B', RegistriesUtils.getItemStack("botania:rune_mana"), 'C', GTOMachines.THERMAL_GENERATOR[GTValues.LV].asItem(), 'D', RegistriesUtils.getItemStack("botania:lens_bounce"));
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("mv_primitive_magic_energy"), ManaMachine.PRIMITIVE_MAGIC_ENERGY[GTValues.MV].asItem(),
                    "ABA", "CDC", "EFE",
                    'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Aluminium), 'B', RegistriesUtils.getItemStack("botania:mana_bomb"), 'C', GTOMachines.THERMAL_GENERATOR[MV].asItem(), 'D', ManaMachine.PRIMITIVE_MAGIC_ENERGY[GTValues.LV].asItem(), 'E', RegistriesUtils.getItemStack("botania:lens_piston"), 'F', new MaterialEntry(TagPrefix.plateDense, GTMaterials.SteelMagnetic));

            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("lv_mana_assembler"), ManaMachine.MANA_ASSEMBLER[GTValues.LV].asItem(),
                    "ABA", "CDC", "AEA",
                    'A', new MaterialEntry(TagPrefix.plate, GTOMaterials.Manasteel), 'B', GTItems.ROBOT_ARM_LV.asItem(), 'C', CustomTags.LV_CIRCUITS, 'D', ManaMachine.MANA_HULL[GTValues.LV].asItem(), 'E', GTItems.FIELD_GENERATOR_LV.asItem());
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("mv_mana_assembler"), ManaMachine.MANA_ASSEMBLER[GTValues.MV].asItem(),
                    "ABC", "DED", "FGH",
                    'A', RegistriesUtils.getItemStack("botania:rune_water"), 'B', GTItems.ROBOT_ARM_MV.asItem(), 'C', RegistriesUtils.getItemStack("botania:rune_fire"), 'D', CustomTags.MV_CIRCUITS, 'E', ManaMachine.MANA_HULL[GTValues.MV].asItem(), 'F', RegistriesUtils.getItemStack("botania:rune_earth"), 'G', GTItems.FIELD_GENERATOR_MV.asItem(), 'H', RegistriesUtils.getItemStack("botania:rune_air"));
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("hv_mana_assembler"), ManaMachine.MANA_ASSEMBLER[GTValues.HV].asItem(),
                    "ABA", "CDC", "AEA",
                    'A', new MaterialEntry(TagPrefix.plate, GTOMaterials.Terrasteel), 'B', GTItems.ROBOT_ARM_HV.asItem(), 'C', CustomTags.HV_CIRCUITS, 'D', ManaMachine.MANA_HULL[GTValues.HV].asItem(), 'E', GTItems.FIELD_GENERATOR_HV.asItem());
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ev_mana_assembler"), ManaMachine.MANA_ASSEMBLER[GTValues.EV].asItem(),
                    "ABA", "CDC", "AEA",
                    'A', new MaterialEntry(TagPrefix.plate, GTOMaterials.Elementium), 'B', GTItems.ROBOT_ARM_EV.asItem(), 'C', CustomTags.EV_CIRCUITS, 'D', ManaMachine.MANA_HULL[GTValues.EV].asItem(), 'E', GTItems.FIELD_GENERATOR_EV.asItem());
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("iv_mana_assembler"), ManaMachine.MANA_ASSEMBLER[GTValues.IV].asItem(),
                    "ABA", "CDC", "AEA",
                    'A', new MaterialEntry(TagPrefix.plate, GTOMaterials.Alfsteel), 'B', GTItems.ROBOT_ARM_IV.asItem(), 'C', CustomTags.IV_CIRCUITS, 'D', ManaMachine.MANA_HULL[GTValues.IV].asItem(), 'E', GTItems.FIELD_GENERATOR_IV.asItem());
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("luv_mana_assembler"), ManaMachine.MANA_ASSEMBLER[GTValues.LuV].asItem(),
                    "ABA", "CDC", "AEA",
                    'A', new MaterialEntry(TagPrefix.plate, GTOMaterials.Gaiasteel), 'B', GTItems.ROBOT_ARM_LuV.asItem(), 'C', CustomTags.LuV_CIRCUITS, 'D', ManaMachine.MANA_HULL[GTValues.LuV].asItem(), 'E', GTItems.FIELD_GENERATOR_LuV.asItem());

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("industrial_platform_deployment_tools"), GTOMachines.INDUSTRIAL_PLATFORM_DEPLOYMENT_TOOLS.asItem(),
                    "AAA", "ABA", "AAA",
                    'A', AEItems.MATTER_BALL.asItem(), 'B', RegistriesUtils.getItemStack("gtocore:standard_industrial_components_small"));
        }

        // 杂项配方
        {
            VanillaRecipeHelper.addShapedRecipe("mortar_grind_living_rock_block",
                    ChemicalHelper.get(dust, Livingrock, 9), "X", "m", 'X', BotaniaBlocks.livingrock);
            VanillaRecipeHelper.addShapedRecipe("mortar_grind_living_wood_planks",
                    ChemicalHelper.get(dust, Livingwood), "X", "m", 'X', BotaniaBlocks.livingwoodPlanks);
            VanillaRecipeHelper.addShapedRecipe("mortar_grind_living_clay_block",
                    ChemicalHelper.get(dust, Livingclay, 9), "X", "m", 'X', ChemicalHelper.get(block, Livingclay));
            VanillaRecipeHelper.addShapedRecipe("mortar_grind_living_steel",
                    ChemicalHelper.get(dust, Livingsteel), "X", "m", 'X', ChemicalHelper.get(ingot, Livingsteel));
            VanillaRecipeHelper.addShapedRecipe("mortar_grind_runerock_block",
                    ChemicalHelper.get(dust, Runerock, 9), "X", "m", 'X', ChemicalHelper.get(block, Runerock));

            VanillaRecipeHelper.addShapelessRecipe(id("living_dust"), ChemicalHelper.get(dust, Livingsteel, 6),
                    new MaterialEntry(dust, Steel), new MaterialEntry(dust, Steel), new MaterialEntry(dust, Steel),
                    new MaterialEntry(dust, Steel), new MaterialEntry(dust, Steel), new MaterialEntry(dust, Steel),
                    new MaterialEntry(dust, Livingclay), new MaterialEntry(dust, Livingwood), new MaterialEntry(dust, Livingrock));
            VanillaRecipeHelper.addShapelessRecipe(id("white_wax"), ChemicalHelper.get(dust, WhiteWax, 4),
                    Items.HONEYCOMB, Items.HONEYCOMB, BotaniaBlocks.whiteBuriedPetals.asItem(), BotaniaBlocks.whiteBuriedPetals.asItem(),
                    new MaterialEntry(dust, Livingrock), new MaterialEntry(dust, Livingrock), new MaterialEntry(dust, Livingrock),
                    new MaterialEntry(dust, Livingrock), new MaterialEntry(dust, Livingrock));

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("area_destruction_tools"), ManaMachine.AREA_DESTRUCTION_TOOLS.asItem(),
                    "ABA", "CDC", "ABA",
                    'A', Items.REPEATER.asItem(), 'B', GTBlocks.INDUSTRIAL_TNT.asItem(), 'C', GTOBlocks.NUKE_BOMB.asItem(), 'D', ManaMachine.MANA_HULL[GTValues.EV].asItem());

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("celestial_condenser"), ManaMachine.CELESTIAL_CONDENSER.asItem(),
                    "ABC", "DEF", "GHI",
                    'A', Items.YELLOW_STAINED_GLASS_PANE, 'B', Items.WHITE_STAINED_GLASS_PANE, 'C', Items.PINK_STAINED_GLASS_PANE,
                    'D', Items.RED_STAINED_GLASS_PANE, 'E', new MaterialEntry(TagPrefix.frameGt, GTOMaterials.Aerialite), 'F', Items.MAGENTA_STAINED_GLASS_PANE,
                    'G', RegistriesUtils.getItemStack("botania:tornado_rod"), 'H', Items.CHISELED_DEEPSLATE, 'I', RegistriesUtils.getItemStack("ars_nouveau:dominion_wand"));
        }

        // 工具配方
        {
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("livingwood_mallet"), RegistriesUtils.getItemStack("gtocore:livingwood_mallet"),
                    "AAA", "AAA", " B ",
                    'A', RegistriesUtils.getItemStack("botania:livingwood_planks"), 'B', new MaterialEntry(TagPrefix.rod, GTMaterials.Wood));
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("dreamwood_mallet"), RegistriesUtils.getItemStack("gtocore:dreamwood_mallet"),
                    "AAA", "AAA", " B ",
                    'A', RegistriesUtils.getItemStack("botania:dreamwood_planks"), 'B', new MaterialEntry(TagPrefix.rod, GTMaterials.Wood));
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("shimmerwood_mallet"), RegistriesUtils.getItemStack("gtocore:shimmerwood_mallet"),
                    "AAA", "AAA", " B ",
                    'A', new MaterialEntry(TagPrefix.block, GTOMaterials.Shimmerwood), 'B', new MaterialEntry(TagPrefix.rod, GTMaterials.Wood));
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("livingrock_mortar"), RegistriesUtils.getItemStack("gtocore:livingrock_mortar"),
                    " A ", "BAB", "BBB",
                    'A', new MaterialEntry(TagPrefix.block, GTOMaterials.Livingrock), 'B', new MaterialEntry(TagPrefix.rock, GTMaterials.Stone));
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("runerock_mortar"), RegistriesUtils.getItemStack("gtocore:runerock_mortar"),
                    " A ", "BAB", "BBB",
                    'A', new MaterialEntry(TagPrefix.block, GTOMaterials.Runerock), 'B', new MaterialEntry(TagPrefix.rock, GTMaterials.Stone));
            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("shimmerrock_mortar"), RegistriesUtils.getItemStack("gtocore:shimmerrock_mortar"),
                    " A ", "BAB", "BBB",
                    'A', new MaterialEntry(TagPrefix.block, GTOMaterials.Shimmerrock), 'B', new MaterialEntry(TagPrefix.rock, GTMaterials.Stone));
        }
    }
}
