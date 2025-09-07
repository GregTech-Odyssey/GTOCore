package com.gtocore.data.recipe.magic;

import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMachines;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.machines.GCYMMachines;
import com.gtocore.common.data.machines.ManaMachine;
import com.gtocore.common.data.machines.ManaMultiBlock;
import com.gtocore.data.record.Enchantment;

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
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.fluids.FluidStack;

import com.enderio.base.common.init.EIOFluids;
import com.hollingsworth.arsnouveau.setup.registry.BlockRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys.GAS;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.common.data.GTOItems.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;
import static com.gtolib.GTOCore.id;

public final class MagicRecipesA {

    public static void init() {
        // 机器外壳
        Material[] ManaSteels = {
                OriginalBronze, Manasteel, Terrasteel, Elementium, Alfsteel, Gaiasteel, Orichalcos
        };
        ItemStack[] ManaCasing = {
                GTOBlocks.ORIGINAL_BRONZE_CASING.asStack(), GTOBlocks.MANASTEEL_CASING.asStack(),
                GTOBlocks.TERRASTEEL_CASING.asStack(), GTOBlocks.ELEMENTIUM_CASING.asStack(),
                GTOBlocks.ALFSTEEL_CASING.asStack(), GTOBlocks.GAIASTEEL_CASING.asStack(),
                GTOBlocks.ORICHALCOS_CASING.asStack(),
        };
        for (int i = 0; i < ManaSteels.length; i++) {
            VanillaRecipeHelper.addShapedRecipe(id(GTOValues.MANAN[i].toLowerCase() + "_mana_hull"), ManaMachine.MANA_HULL[i].asStack(),
                    "CBC", "BAB", "CBC",
                    'A', ManaCasing[i], 'B', new MaterialEntry(plate, ManaSteels[i]), 'C', new MaterialEntry(rod, ManaSteels[i]));
        }

        // 炼金锅
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
                        .circuitMeta(i + 5)
                        .inputItems(dust, Crystals[i], 8)
                        .inputFluids(CycleofBlossomsSolvent.getFluid(1000))
                        .chancedOutput(Elements[i].getFluid(GAS, 8 * L), 10, 0)
                        .chancedOutput(CycleofBlossomsSolvent.getFluid(900), 9500, 0)
                        .duration(650)
                        .MANAt(2)
                        .addData("param1", param[i][0] + 20)
                        .addData("param2", param[i][1] + 20)
                        .addData("param3", param[i][2] + 20)
                        .save();
            }

            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("alchemy_aether_1_gas")
                    .circuitMeta(1)
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
                    .circuitMeta(1)
                    .inputItems(dust, PerditioCrystal, 8)
                    .inputFluids(CycleofBlossomsSolvent.getFluid(1000))
                    .chancedOutput(Aether.getFluid(GAS, 4 * L), 1, 0)
                    .chancedOutput(CycleofBlossomsSolvent.getFluid(800), 8000, 0)
                    .duration(650)
                    .MANAt(2)
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

        // 机械方块
        {
            ASSEMBLER_RECIPES.recipeBuilder("infused_gold_reinforced_wooden_casing")
                    .inputItems(TagPrefix.frameGt, GTMaterials.Wood)
                    .inputItems(RegistriesUtils.getItemStack("ars_nouveau:archwood_planks"), 4)
                    .inputItems(TagPrefix.screw, InfusedGold, 8)
                    .inputItems(TagPrefix.plate, InfusedGold, 2)
                    .circuitMeta(6)
                    .outputItems(GTOBlocks.INFUSED_GOLD_REINFORCED_WOODEN_CASING.asStack())
                    .EUt(16)
                    .duration(50)
                    .save();

            ASSEMBLER_RECIPES.builder("infused_gold_casing")
                    .inputItems(TagPrefix.plate, GTOMaterials.InfusedGold, 6)
                    .inputItems(TagPrefix.frameGt, GTOMaterials.InfusedGold)
                    .outputItems(GTOBlocks.INFUSED_GOLD_CASING.asStack())
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
                    .outputItems(GTOBlocks.SOURCE_STONE_CASING.asStack())
                    .EUt(16)
                    .duration(50)
                    .save();

            ASSEMBLER_RECIPES.recipeBuilder("source_fiber_mechanical_casing")
                    .inputItems(gem, GTOMaterials.SourceGem)
                    .inputItems(RegistriesUtils.getItemStack("ars_nouveau:sourcestone"), 2)
                    .inputItems(RegistriesUtils.getItemStack("ars_nouveau:magebloom_block"), 2)
                    .inputItems(ItemsRegistry.MAGE_FIBER, 4)
                    .circuitMeta(6)
                    .outputItems(GTOBlocks.SOURCE_FIBER_MECHANICAL_CASING.asStack())
                    .EUt(16)
                    .duration(50)
                    .save();

            ASSEMBLER_RECIPES.recipeBuilder("spell_prism_casing")
                    .inputItems(RegistriesUtils.getItemStack("ars_nouveau:magebloom_fiber"))
                    .inputItems(RegistriesUtils.getItemStack("ars_nouveau:spell_prism"), 4)
                    .inputItems(RegistriesUtils.getItemStack("ars_nouveau:archwood_planks"), 2)
                    .inputItems(TagPrefix.screw, InfusedGold, 8)
                    .circuitMeta(6)
                    .outputItems(GTOBlocks.SPELL_PRISM_CASING.asStack())
                    .EUt(16)
                    .duration(50)
                    .save();

            VanillaRecipeHelper.addShapedRecipe(id("original_bronze_casing"), GTOBlocks.ORIGINAL_BRONZE_CASING.asStack(),
                    "CCC", "CBC", "CCC",
                    'B', new MaterialEntry(frameGt, OriginalBronze), 'C', new MaterialEntry(plate, OriginalBronze));

            VanillaRecipeHelper.addShapedRecipe(id("manasteel_casing"), GTOBlocks.MANASTEEL_CASING.asStack(),
                    "CCC", "CBC", "CCC",
                    'B', new MaterialEntry(frameGt, Manasteel), 'C', new MaterialEntry(plate, Manasteel));

            ASSEMBLER_RECIPES.builder("terrasteel_casing")
                    .inputItems(TagPrefix.frameGt, GTOMaterials.Terrasteel)
                    .inputItems(gem, GTOMaterials.ManaDiamond, 4)
                    .inputItems(TagPrefix.plate, GTOMaterials.Terrasteel, 2)
                    .outputItems(GTOBlocks.TERRASTEEL_CASING.asStack())
                    .inputFluids(GTOMaterials.Mana, 288)
                    .circuitMeta(6)
                    .duration(200)
                    .MANAt(8)
                    .save();

            ASSEMBLER_RECIPES.builder("elementium_casing")
                    .inputItems(TagPrefix.frameGt, GTOMaterials.Elementium)
                    .inputItems(TagPrefix.plate, GTOMaterials.Dreamwood, 4)
                    .inputItems(TagPrefix.plate, GTOMaterials.Elementium, 2)
                    .outputItems(GTOBlocks.ELEMENTIUM_CASING.asStack())
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
                    .outputItems(GTOBlocks.ALFSTEEL_CASING.asStack())
                    .inputFluids(GTOMaterials.Terrasteel, 288)
                    .circuitMeta(6)
                    .duration(200)
                    .MANAt(32)
                    .save();

            ASSEMBLER_RECIPES.builder("gaiasteel_casing")
                    .inputItems(TagPrefix.frameGt, GTOMaterials.Gaiasteel)
                    .inputItems("botania:elf_quartz")
                    .inputItems(TagPrefix.plate, GTOMaterials.Gaiasteel, 2)
                    .outputItems(GTOBlocks.GAIASTEEL_CASING.asStack())
                    .inputFluids(GTOMaterials.Elementium, 288)
                    .circuitMeta(6)
                    .duration(200)
                    .MANAt(64)
                    .save();

            ASSEMBLER_RECIPES.builder("orichalcos_casing")
                    .inputItems(TagPrefix.frameGt, GTOMaterials.Orichalcos)
                    .inputItems("extrabotany:dimension_catalyst")
                    .inputItems(TagPrefix.plate, GTOMaterials.Orichalcos, 2)
                    .outputItems(GTOBlocks.ORICHALCOS_CASING.asStack())
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
                    .inputFluids(Aether.getFluid(GAS, 1000))
                    .circuitMeta(6)
                    .duration(200)
                    .MANAt(32)
                    .save();
        }

        // 结构主方块
        {
            ASSEMBLER_RECIPES.builder("mana_infuser")
                    .inputItems(TagPrefix.frameGt, GTOMaterials.Manasteel)
                    .inputItems(CustomTags.MV_CIRCUITS)
                    .inputItems(GTItems.ELECTRIC_PUMP_MV.asStack(2))
                    .inputItems(TagPrefix.gem, GTOMaterials.ManaDiamond, 8)
                    .inputItems("botania:mana_pearl", 8)
                    .inputItems(TagPrefix.plate, GTOMaterials.Livingwood, 4)
                    .outputItems(ManaMultiBlock.MANA_INFUSER.asStack())
                    .inputFluids(GTOMaterials.Mana, 288)
                    .duration(200)
                    .MANAt(32)
                    .save();

            ASSEMBLER_RECIPES.builder("mana_condenser")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.EV].asStack())
                    .inputItems(GTItems.FIELD_GENERATOR_HV.asStack(2))
                    .inputItems(CustomTags.HV_CIRCUITS)
                    .inputItems(TagPrefix.gemExquisite, GTOMaterials.ManaDiamond, 4)
                    .inputItems(TagPrefix.gear, GTOMaterials.Terrasteel, 4)
                    .inputItems("botania:mana_pearl", 4)
                    .inputItems("mythicbotany:midgard_rune")
                    .inputItems("botania:rune_mana")
                    .inputItems("botania:rune_lust")
                    .outputItems(ManaMultiBlock.MANA_CONDENSER.asStack())
                    .inputFluids(Gaiasteel, 288)
                    .duration(200)
                    .MANAt(128)
                    .save();

            ASSEMBLER_RECIPES.builder("elf_exchange")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.HV].asStack())
                    .inputItems(GTItems.FIELD_GENERATOR_MV.asStack(2))
                    .inputItems(CustomTags.MV_CIRCUITS)
                    .inputItems(TagPrefix.gemExquisite, GTOMaterials.Dragonstone, 4)
                    .inputItems(TagPrefix.gear, GTOMaterials.Alfsteel, 4)
                    .inputItems(BotaniaItems.pixieDust, 4)
                    .inputItems(BotaniaBlocks.alfPortal, 4)
                    .inputItems("mythicbotany:alfheim_rune")
                    .inputItems("mythicbotany:asgard_rune")
                    .outputItems(ManaMultiBlock.ELF_EXCHANGE.asStack())
                    .inputFluids(GTOMaterials.Alfsteel, 288)
                    .duration(200)
                    .MANAt(128)
                    .save();

            ASSEMBLER_RECIPES.builder("industrial_altar")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.HV].asStack())
                    .inputItems(GTItems.FIELD_GENERATOR_MV.asStack(2))
                    .inputItems(CustomTags.MV_CIRCUITS)
                    .inputItems(block, GTOMaterials.Runerock, 4)
                    .inputItems(TagPrefix.gear, GTOMaterials.Elementium, 4)
                    .inputItems(BotaniaBlocks.pistonRelay, 4)
                    .inputItems("botania:runic_altar")
                    .inputItems("botania:apothecary_livingrock")
                    .inputItems("botania:brewery")
                    .outputItems(ManaMultiBlock.INDUSTRIAL_ALTAR.asStack())
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
                    .outputItems(ManaMultiBlock.MANA_GARDEN.asStack())
                    .duration(1600)
                    .MANAt(256)
                    .save();

            ASSEMBLER_RECIPES.builder("mana_greenhouse")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.EV].asStack())
                    .inputItems(GTItems.FIELD_GENERATOR_IV.asStack(8))
                    .inputItems(CustomTags.IV_CIRCUITS, 4)
                    .inputItems("botania:agricarnation", 32)
                    .inputItems("botania:hopperhock", 16)
                    .inputItems("botania:rune_spring", 4)
                    .inputItems(TagPrefix.gear, GTOMaterials.Gaia, 8)
                    .inputItems(TagPrefix.gemExquisite, GTOMaterials.Dragonstone, 16)
                    .inputItems("botania:fertilizer", 64)
                    .outputItems(ManaMultiBlock.MANA_GREENHOUSE.asStack())
                    .inputFluids(GTOMaterials.Mana, 4608)
                    .duration(1200)
                    .MANAt(128)
                    .save();

            ASSEMBLER_RECIPES.builder("mana_alloy_blast_smelter")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.EV].asStack())
                    .inputItems(GTItems.FIELD_GENERATOR_IV.asStack(16))
                    .inputItems(CustomTags.IV_CIRCUITS, 16)
                    .inputItems(GCYMMachines.BLAST_ALLOY_SMELTER.asStack(4))
                    .inputItems("botania:exoflame", 32)
                    .inputItems("mythicbotany:exoblaze", 32)
                    .inputItems("botania:rune_pride", 8)
                    .inputItems("mythicbotany:nidavellir_rune", 8)
                    .inputItems(TagPrefix.gear, GTOMaterials.Orichalcos, 8)
                    .inputFluids(GTOMaterials.Aerialite, L * 16)
                    .outputItems(ManaMultiBlock.MANA_ALLOY_BLAST_SMELTER.asStack())
                    .duration(1200)
                    .MANAt(512)
                    .save();

            ASSEMBLER_RECIPES.builder("rune_engraving_chamber")
                    .inputItems("extrabotany:hero_medal", 4)
                    .inputItems(GTOBlocks.SPELL_PRISM_CASING.asStack(32))
                    .inputItems(GTOBlocks.ORICHALCOS_CASING.asStack(16))
                    .inputItems("botania:mana_bomb", 8)
                    .inputItems(GTItems.FIELD_GENERATOR_LuV.asStack(8))
                    .inputItems(GTItems.EMITTER_LuV.asStack(8))
                    .inputItems(TagPrefix.lens, GTOMaterials.BifrostPerm, 32)
                    .inputItems("ars_nouveau:manipulation_essence", 32)
                    .inputItems("ars_nouveau:wilden_tribute", 32)
                    .outputItems(ManaMultiBlock.RUNE_ENGRAVING_CHAMBER.asStack())
                    .inputFluids(GTOMaterials.TheWaterFromTheWellOfWisdom, 16000)
                    .duration(200)
                    .MANAt(4096)
                    .save();

            ASSEMBLER_RECIPES.builder("large_perfusion_device")
                    .inputItems(GTOBlocks.SOURCE_FIBER_MECHANICAL_CASING.asStack())
                    .inputItems(TagPrefix.block, GTOMaterials.SourceGem, 16)
                    .inputItems("ars_nouveau:summon_focus")
                    .inputItems("ars_nouveau:imbuement_chamber", 8)
                    .inputItems("ars_nouveau:arcane_core", 8)
                    .inputItems("ars_nouveau:enchanting_apparatus", 8)
                    .inputItems(GTItems.FIELD_GENERATOR_HV, 4)
                    .inputItems(CustomTags.IV_CIRCUITS, 4)
                    .inputItems("ars_nouveau:shapers_focus")
                    .outputItems(ManaMultiBlock.LARGE_PERFUSION_DEVICE.asStack())
                    .duration(800)
                    .MANAt(64)
                    .save();

            ASSEMBLER_RECIPES.builder("base_mana_distributor")
                    .inputItems(TagPrefix.frameGt, GTOMaterials.OriginalBronze)
                    .inputItems("botania:mana_distributor", 4)
                    .inputItems("botania:mana_gun", 4)
                    .inputItems(TagPrefix.bolt, GTOMaterials.Manasteel, 8)
                    .inputItems(TagPrefix.plate, GTOMaterials.Livingrock, 16)
                    .outputItems(ManaMultiBlock.BASE_MANA_DISTRIBUTOR.asStack())
                    .duration(200)
                    .MANAt(8)
                    .save();

            ASSEMBLER_RECIPES.builder("advanced_mana_distributor")
                    .inputItems(ManaMultiBlock.BASE_MANA_DISTRIBUTOR.asStack())
                    .inputItems(GTItems.FIELD_GENERATOR_MV.asStack(4))
                    .inputItems(TagPrefix.plate, GTOMaterials.Terrasteel, 8)
                    .inputItems(TagPrefix.screw, GTOMaterials.Manasteel, 32)
                    .inputItems("botania:rune_mana", 32)
                    .inputItems("botania:mana_tablet", 2)
                    .inputItems(BotaniaBlocks.manaBomb.asItem())
                    .outputItems(ManaMultiBlock.ADVANCED_MANA_DISTRIBUTOR.asStack())
                    .duration(800)
                    .MANAt(128)
                    .save();

            ASSEMBLER_RECIPES.builder("the_primordial_reconstructor")
                    .inputItems(new ItemStack(Items.GRINDSTONE.asItem(), 16))
                    .inputItems("apotheosis:gem_cutting_table", 16)
                    .inputItems(new ItemStack(Items.ANVIL.asItem(), 16))
                    .inputItems("apotheosis:reforging_table", 16)
                    .inputItems(GTOBlocks.HERETICAL_MECHANICAL_CASING.asStack(16))
                    .inputItems("apotheosis:augmenting_table", 16)
                    .inputItems("apotheosis:salvaging_table", 16)
                    .inputItems("apotheosis:ender_library", 16)
                    .inputItems(new ItemStack(Items.SMITHING_TABLE.asItem(), 16))
                    .outputItems(ManaMultiBlock.THE_PRIMORDIAL_RECONSTRUCTOR.asStack())
                    .inputFluids(GTOMaterials.TheWaterFromTheWellOfWisdom, 8000)
                    .duration(200)
                    .MANAt(1024)
                    .save();
        }

        // 魔力仓室
        {
            ASSEMBLER_RECIPES.builder("lv_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.LV].asStack())
                    .inputItems(BotaniaBlocks.pump, 2)
                    .inputItems("botania:mana_pylon")
                    .inputItems(TagPrefix.screw, GTOMaterials.Manasteel, 4)
                    .circuitMeta(1)
                    .outputItems(ManaMachine.MANA_INPUT_HATCH[LV].asStack())
                    .duration(200)
                    .MANAt(4)
                    .save();

            ASSEMBLER_RECIPES.builder("mv_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.MV].asStack())
                    .inputItems(BotaniaBlocks.pump, 2)
                    .inputItems(TagPrefix.lens, GTOMaterials.ManaGlass)
                    .inputItems(TagPrefix.screw, GTOMaterials.Terrasteel, 4)
                    .circuitMeta(1)
                    .outputItems(ManaMachine.MANA_INPUT_HATCH[MV].asStack())
                    .duration(200)
                    .MANAt(16)
                    .save();

            ASSEMBLER_RECIPES.builder("hv_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.HV].asStack())
                    .inputItems(BotaniaBlocks.pump, 2)
                    .inputItems("botania:natura_pylon")
                    .inputItems(TagPrefix.lens, GTOMaterials.ElfGlass)
                    .inputItems(TagPrefix.screw, GTOMaterials.Elementium, 4)
                    .circuitMeta(1)
                    .outputItems(ManaMachine.MANA_INPUT_HATCH[HV].asStack())
                    .inputFluids(GTOMaterials.BifrostPerm, 1000)
                    .duration(200)
                    .MANAt(64)
                    .save();

            ASSEMBLER_RECIPES.builder("ev_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.EV].asStack())
                    .inputItems("botania:prism", 2)
                    .inputItems("mythicbotany:alfheim_rune")
                    .inputItems("endrem:magical_eye")
                    .inputItems(TagPrefix.screw, GTOMaterials.Aerialite, 4)
                    .circuitMeta(1)
                    .outputItems(ManaMachine.MANA_INPUT_HATCH[EV].asStack())
                    .inputFluids(GTOMaterials.BifrostPerm, 2000)
                    .duration(200)
                    .MANAt(256)
                    .save();

            ASSEMBLER_RECIPES.builder("iv_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.IV].asStack())
                    .inputItems("botania:prism", 2)
                    .inputItems("botania:gaia_pylon")
                    .inputItems(TagPrefix.screw, GTOMaterials.Gaia, 4)
                    .circuitMeta(1)
                    .outputItems(ManaMachine.MANA_INPUT_HATCH[IV].asStack())
                    .inputFluids(GTOMaterials.Animium, 1000)
                    .duration(200)
                    .MANAt(1024)
                    .save();

            ASSEMBLER_RECIPES.builder("luv_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.LuV].asStack())
                    .inputItems("botania:prism", 2)
                    .inputItems(ExtraBotanyItems.theUniverse)
                    .inputItems(TagPrefix.lens, GTOMaterials.BifrostPerm)
                    .inputItems(TagPrefix.screw, GTOMaterials.Orichalcos, 4)
                    .circuitMeta(1)
                    .outputItems(ManaMachine.MANA_INPUT_HATCH[LuV].asStack())
                    .inputFluids(GTOMaterials.Animium, 2000)
                    .duration(200)
                    .MANAt(4096)
                    .save();

            ASSEMBLER_RECIPES.builder("lv_wireless_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_INPUT_HATCH[LV].asStack())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:spark", 8)
                    .inputItems("botania:rune_mana", 4)
                    .outputItems(ManaMachine.WIRELESS_MANA_INPUT_HATCH[LV].asStack())
                    .inputFluids(FlowingCiphers.getFluid(100))
                    .duration(200)
                    .MANAt(4)
                    .save();

            ASSEMBLER_RECIPES.builder("mv_wireless_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_INPUT_HATCH[MV].asStack())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:spark", 16)
                    .inputItems("botania:rune_mana", 8)
                    .outputItems(ManaMachine.WIRELESS_MANA_INPUT_HATCH[MV].asStack())
                    .inputFluids(FlowingCiphers.getFluid(200))
                    .duration(200)
                    .MANAt(16)
                    .save();

            ASSEMBLER_RECIPES.builder("hv_wireless_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_INPUT_HATCH[HV].asStack())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:corporea_spark", 16)
                    .inputItems(ManaMultiBlock.BASE_MANA_DISTRIBUTOR, 4)
                    .outputItems(ManaMachine.WIRELESS_MANA_INPUT_HATCH[HV].asStack())
                    .inputFluids(FlowingCiphers.getFluid(400))
                    .duration(200)
                    .MANAt(64)
                    .save();

            ASSEMBLER_RECIPES.builder("ev_wireless_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_INPUT_HATCH[EV].asStack())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:corporea_spark", 32)
                    .inputItems(ManaMultiBlock.BASE_MANA_DISTRIBUTOR, 8)
                    .outputItems(ManaMachine.WIRELESS_MANA_INPUT_HATCH[EV].asStack())
                    .inputFluids(FlowingCiphers.getFluid(600))
                    .duration(200)
                    .MANAt(256)
                    .save();

            ASSEMBLER_RECIPES.builder("iv_wireless_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_INPUT_HATCH[IV].asStack())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:corporea_index", 8)
                    .inputItems(ManaMultiBlock.ADVANCED_MANA_DISTRIBUTOR, 2)
                    .outputItems(ManaMachine.WIRELESS_MANA_INPUT_HATCH[IV].asStack())
                    .inputFluids(FlowingCiphers.getFluid(800))
                    .duration(200)
                    .MANAt(1024)
                    .save();

            ASSEMBLER_RECIPES.builder("luv_wireless_mana_input_hatch")
                    .inputItems(ManaMachine.MANA_INPUT_HATCH[LuV].asStack())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:corporea_index", 16)
                    .inputItems(ManaMultiBlock.ADVANCED_MANA_DISTRIBUTOR, 4)
                    .outputItems(ManaMachine.WIRELESS_MANA_INPUT_HATCH[LuV].asStack())
                    .inputFluids(FlowingCiphers.getFluid(1000))
                    .duration(200)
                    .MANAt(4096)
                    .save();

            ASSEMBLER_RECIPES.builder("lv_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.LV].asStack())
                    .inputItems(BotaniaBlocks.pump, 2)
                    .inputItems("botania:mana_pylon")
                    .inputItems(TagPrefix.screw, GTOMaterials.Manasteel, 4)
                    .circuitMeta(2)
                    .outputItems(ManaMachine.MANA_OUTPUT_HATCH[LV].asStack())
                    .duration(200)
                    .MANAt(4)
                    .save();

            ASSEMBLER_RECIPES.builder("mv_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.MV].asStack())
                    .inputItems(BotaniaBlocks.pump, 2)
                    .inputItems(TagPrefix.lens, GTOMaterials.ManaGlass)
                    .inputItems(TagPrefix.screw, GTOMaterials.Terrasteel, 4)
                    .circuitMeta(2)
                    .outputItems(ManaMachine.MANA_OUTPUT_HATCH[MV].asStack())
                    .duration(200)
                    .MANAt(16)
                    .save();

            ASSEMBLER_RECIPES.builder("hv_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.HV].asStack())
                    .inputItems(BotaniaBlocks.pump, 2)
                    .inputItems("botania:natura_pylon")
                    .inputItems(TagPrefix.lens, GTOMaterials.ElfGlass)
                    .inputItems(TagPrefix.screw, GTOMaterials.Elementium, 4)
                    .circuitMeta(2)
                    .outputItems(ManaMachine.MANA_OUTPUT_HATCH[HV].asStack())
                    .inputFluids(GTOMaterials.BifrostPerm, 1000)
                    .duration(200)
                    .MANAt(16)
                    .save();

            ASSEMBLER_RECIPES.builder("ev_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.EV].asStack())
                    .inputItems("botania:prism", 2)
                    .inputItems("mythicbotany:alfheim_rune")
                    .inputItems("endrem:magical_eye")
                    .inputItems(TagPrefix.screw, GTOMaterials.Aerialite, 4)
                    .circuitMeta(2)
                    .outputItems(ManaMachine.MANA_OUTPUT_HATCH[EV].asStack())
                    .inputFluids(GTOMaterials.BifrostPerm, 2000)
                    .duration(200)
                    .MANAt(256)
                    .save();

            ASSEMBLER_RECIPES.builder("iv_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.IV].asStack())
                    .inputItems("botania:prism", 2)
                    .inputItems("botania:gaia_pylon")
                    .inputItems(TagPrefix.screw, GTOMaterials.Gaia, 4)
                    .circuitMeta(2)
                    .outputItems(ManaMachine.MANA_OUTPUT_HATCH[IV].asStack())
                    .inputFluids(GTOMaterials.Animium, 1000)
                    .duration(200)
                    .MANAt(1024)
                    .save();

            ASSEMBLER_RECIPES.builder("luv_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.LuV].asStack())
                    .inputItems("botania:prism", 2)
                    .inputItems(ExtraBotanyItems.theUniverse)
                    .inputItems(TagPrefix.lens, GTOMaterials.BifrostPerm)
                    .inputItems(TagPrefix.screw, GTOMaterials.Orichalcos, 4)
                    .circuitMeta(2)
                    .outputItems(ManaMachine.MANA_OUTPUT_HATCH[LuV].asStack())
                    .inputFluids(GTOMaterials.Animium, 2000)
                    .duration(200)
                    .MANAt(4096)
                    .save();

            ASSEMBLER_RECIPES.builder("lv_wireless_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_OUTPUT_HATCH[LV].asStack())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:spark", 8)
                    .inputItems("botania:rune_mana", 4)
                    .outputItems(ManaMachine.WIRELESS_MANA_OUTPUT_HATCH[LV].asStack())
                    .inputFluids(FlowingCiphers.getFluid(100))
                    .duration(200)
                    .MANAt(4)
                    .save();

            ASSEMBLER_RECIPES.builder("mv_wireless_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_OUTPUT_HATCH[MV].asStack())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:spark", 16)
                    .inputItems("botania:rune_mana", 8)
                    .outputItems(ManaMachine.WIRELESS_MANA_OUTPUT_HATCH[MV].asStack())
                    .inputFluids(FlowingCiphers.getFluid(200))
                    .duration(200)
                    .MANAt(16)
                    .save();

            ASSEMBLER_RECIPES.builder("hv_wireless_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_OUTPUT_HATCH[HV].asStack())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:corporea_spark", 16)
                    .inputItems(ManaMultiBlock.BASE_MANA_DISTRIBUTOR, 4)
                    .outputItems(ManaMachine.WIRELESS_MANA_OUTPUT_HATCH[HV].asStack())
                    .inputFluids(FlowingCiphers.getFluid(400))
                    .duration(200)
                    .MANAt(64)
                    .save();

            ASSEMBLER_RECIPES.builder("ev_wireless_mana_output_hatch")
                    .inputItems(ManaMachine.MANA_OUTPUT_HATCH[EV].asStack())
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems("botania:corporea_spark", 32)
                    .inputItems(ManaMultiBlock.BASE_MANA_DISTRIBUTOR, 8)
                    .outputItems(ManaMachine.WIRELESS_MANA_OUTPUT_HATCH[EV].asStack())
                    .inputFluids(FlowingCiphers.getFluid(600))
                    .duration(200)
                    .MANAt(256)
                    .save();

            ASSEMBLER_RECIPES.builder("iv_wireless_mana_output_hatch")
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems(ManaMachine.MANA_OUTPUT_HATCH[IV].asStack())
                    .inputItems("botania:corporea_index", 8)
                    .inputItems(ManaMultiBlock.ADVANCED_MANA_DISTRIBUTOR, 2)
                    .outputItems(ManaMachine.WIRELESS_MANA_OUTPUT_HATCH[IV].asStack())
                    .inputFluids(FlowingCiphers.getFluid(800))
                    .duration(200)
                    .MANAt(1024)
                    .save();

            ASSEMBLER_RECIPES.builder("luv_wireless_mana_output_hatch")
                    .inputItems("ars_nouveau:warp_scroll")
                    .inputItems(ManaMachine.MANA_OUTPUT_HATCH[LuV].asStack())
                    .inputItems("botania:corporea_index", 16)
                    .inputItems(ManaMultiBlock.ADVANCED_MANA_DISTRIBUTOR, 4)
                    .outputItems(ManaMachine.WIRELESS_MANA_OUTPUT_HATCH[LuV].asStack())
                    .inputFluids(FlowingCiphers.getFluid(1000))
                    .duration(200)
                    .MANAt(4096)
                    .save();

            ASSEMBLER_RECIPES.builder("lv_mana_extract_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.LV].asStack())
                    .inputItems(CustomTags.LV_CIRCUITS)
                    .inputItems(TagPrefix.lens, GTOMaterials.ManaGlass)
                    .inputItems(GTItems.SENSOR_LV.asStack())
                    .inputItems(TagPrefix.gem, GTOMaterials.ManaDiamond)
                    .inputItems("botania:mana_pearl")
                    .inputItems(TagPrefix.screw, GTOMaterials.Manasteel, 4)
                    .outputItems(ManaMachine.MANA_EXTRACT_HATCH[LV].asStack())
                    .inputFluids(GTOMaterials.Mana, 288)
                    .duration(200)
                    .MANAt(4)
                    .save();

            ASSEMBLER_RECIPES.builder("mv_mana_extract_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.MV].asStack())
                    .inputItems(CustomTags.MV_CIRCUITS)
                    .inputItems(TagPrefix.lens, GTOMaterials.ElfGlass)
                    .inputItems(GTItems.SENSOR_MV.asStack())
                    .inputItems(TagPrefix.gem, GTOMaterials.Dragonstone)
                    .inputItems("botania:pixie_dust")
                    .inputItems(TagPrefix.screw, GTOMaterials.Elementium, 4)
                    .outputItems(ManaMachine.MANA_EXTRACT_HATCH[MV].asStack())
                    .inputFluids(GTOMaterials.Mana, 288)
                    .duration(200)
                    .MANAt(16)
                    .save();

            ASSEMBLER_RECIPES.builder("hv_mana_extract_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.HV].asStack())
                    .inputItems(CustomTags.HV_CIRCUITS)
                    .inputItems(TagPrefix.lens, GTOMaterials.BifrostPerm)
                    .inputItems(GTItems.SENSOR_HV.asStack())
                    .inputItems(TagPrefix.gemExquisite, GTOMaterials.Dragonstone)
                    .inputItems(TagPrefix.rodLong, GTOMaterials.Alfsteel)
                    .inputItems(TagPrefix.screw, GTOMaterials.Terrasteel, 4)
                    .outputItems(ManaMachine.MANA_EXTRACT_HATCH[HV].asStack())
                    .inputFluids(GTOMaterials.Mana, 288)
                    .duration(200)
                    .MANAt(64)
                    .save();

            ASSEMBLER_RECIPES.builder("ev_mana_extract_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.EV].asStack())
                    .inputItems(CustomTags.EV_CIRCUITS)
                    .inputItems("botania:natura_pylon")
                    .inputItems(GTItems.SENSOR_EV.asStack())
                    .inputItems("mythicbotany:alfheim_rune")
                    .inputItems(TagPrefix.rodLong, GTOMaterials.Gaiasteel)
                    .inputItems(TagPrefix.screw, GTOMaterials.Alfsteel, 4)
                    .outputItems(ManaMachine.MANA_EXTRACT_HATCH[EV].asStack())
                    .inputFluids(GTOMaterials.Mana, 1152)
                    .duration(200)
                    .MANAt(256)
                    .save();

            ASSEMBLER_RECIPES.builder("iv_mana_extract_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.IV].asStack())
                    .inputItems(CustomTags.IV_CIRCUITS)
                    .inputItems("mythicbotany:alfsteel_pylon")
                    .inputItems(GTItems.SENSOR_IV.asStack())
                    .inputItems("endrem:magical_eye")
                    .inputItems(TagPrefix.rodLong, GTOMaterials.Gaia)
                    .inputItems(TagPrefix.screw, GTOMaterials.Aerialite, 4)
                    .outputItems(ManaMachine.MANA_EXTRACT_HATCH[IV].asStack())
                    .inputFluids(GTOMaterials.Mana, 1152)
                    .duration(200)
                    .MANAt(1024)
                    .save();

            ASSEMBLER_RECIPES.builder("luv_mana_extract_hatch")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.LuV].asStack())
                    .inputItems(CustomTags.LuV_CIRCUITS)
                    .inputItems("botania:gaia_pylon")
                    .inputItems(GTItems.SENSOR_LuV.asStack())
                    .inputItems("botania:life_essence")
                    .inputItems(TagPrefix.rodLong, GTOMaterials.Aerialite)
                    .inputItems(TagPrefix.screw, GTOMaterials.Orichalcos, 4)
                    .outputItems(ManaMachine.MANA_EXTRACT_HATCH[LuV].asStack())
                    .inputFluids(GTOMaterials.Mana, 1152)
                    .duration(200)
                    .MANAt(4096)
                    .save();

            ASSEMBLER_RECIPES.builder("mana_amplifier_hatch")
                    .notConsumable("botania:dice")
                    .inputItems(ManaMachine.MANA_HULL[GTValues.LuV].asStack())
                    .inputItems("botania:laputa_shard")
                    .inputItems("extrabotany:the_universe", 8)
                    .outputItems(ManaMachine.MANA_AMPLIFIER_HATCH.asStack())
                    .inputFluids(TheWaterFromTheWellOfWisdom.getFluid(8000))
                    .duration(800)
                    .MANAt(8192)
                    .save();

            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("alchemy_cauldron"), ManaMachine.ALCHEMY_CAULDRON.asStack(),
                    "BBB", "ADA", "AAA",
                    'A', new MaterialEntry(TagPrefix.plate, Steel), 'B', new MaterialEntry(rod, Steel), 'D', Items.CAULDRON);

            VanillaRecipeHelper.addShapedRecipe(id("mana_heater"), ManaMachine.MANA_HEATER.asStack(),
                    "EEE", "CDC", "CBC",
                    'B', ManaMachine.MANA_HULL[2].asStack(), 'C', RegistriesUtils.getItemStack("botania:livingrock_bricks"), 'D', RegistriesUtils.getItemStack("botania:mana_pool"), 'E', new MaterialEntry(plate, Manasteel));

            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("lv_primitive_magic_energy"), ManaMachine.PRIMITIVE_MAGIC_ENERGY[GTValues.LV].asStack(),
                    "ABA", "BCB", "DBD",
                    'A', new MaterialEntry(TagPrefix.lens, GTOMaterials.ManaGlass), 'B', RegistriesUtils.getItemStack("botania:rune_mana"), 'C', GTOMachines.THERMAL_GENERATOR[GTValues.LV].asStack(), 'D', RegistriesUtils.getItemStack("botania:lens_bounce"));
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("mv_primitive_magic_energy"), ManaMachine.PRIMITIVE_MAGIC_ENERGY[GTValues.MV].asStack(),
                    "ABA", "CDC", "EFE",
                    'A', new MaterialEntry(TagPrefix.plate, GTMaterials.Aluminium), 'B', RegistriesUtils.getItemStack("botania:mana_bomb"), 'C', GTMachines.ENERGY_CONVERTER_8A[GTValues.LV].asStack(), 'D', ManaMachine.PRIMITIVE_MAGIC_ENERGY[GTValues.LV].asStack(), 'E', RegistriesUtils.getItemStack("botania:lens_piston"), 'F', new MaterialEntry(TagPrefix.plateDense, GTMaterials.SteelMagnetic));

            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("lv_mana_assembler"), ManaMachine.MANA_ASSEMBLER[GTValues.LV].asStack(),
                    "ABA", "CDC", "AEA",
                    'A', new MaterialEntry(TagPrefix.plate, GTOMaterials.Manasteel), 'B', GTItems.ROBOT_ARM_LV.asStack(), 'C', CustomTags.LV_CIRCUITS, 'D', ManaMachine.MANA_HULL[GTValues.LV].asStack(), 'E', GTItems.FIELD_GENERATOR_LV.asStack());
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("mv_mana_assembler"), ManaMachine.MANA_ASSEMBLER[GTValues.MV].asStack(),
                    "ABC", "DED", "FGH",
                    'A', RegistriesUtils.getItemStack("botania:rune_water"), 'B', GTItems.ROBOT_ARM_MV.asStack(), 'C', RegistriesUtils.getItemStack("botania:rune_fire"), 'D', CustomTags.MV_CIRCUITS, 'E', ManaMachine.MANA_HULL[GTValues.MV].asStack(), 'F', RegistriesUtils.getItemStack("botania:rune_earth"), 'G', GTItems.FIELD_GENERATOR_MV.asStack(), 'H', RegistriesUtils.getItemStack("botania:rune_air"));
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("hv_mana_assembler"), ManaMachine.MANA_ASSEMBLER[GTValues.HV].asStack(),
                    "ABA", "CDC", "AEA",
                    'A', new MaterialEntry(TagPrefix.plate, GTOMaterials.Terrasteel), 'B', GTItems.ROBOT_ARM_HV.asStack(), 'C', CustomTags.HV_CIRCUITS, 'D', ManaMachine.MANA_HULL[GTValues.HV].asStack(), 'E', GTItems.FIELD_GENERATOR_HV.asStack());
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("ev_mana_assembler"), ManaMachine.MANA_ASSEMBLER[GTValues.EV].asStack(),
                    "ABA", "CDC", "AEA",
                    'A', new MaterialEntry(TagPrefix.plate, GTOMaterials.Elementium), 'B', GTItems.ROBOT_ARM_EV.asStack(), 'C', CustomTags.EV_CIRCUITS, 'D', ManaMachine.MANA_HULL[GTValues.EV].asStack(), 'E', GTItems.FIELD_GENERATOR_EV.asStack());
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("iv_mana_assembler"), ManaMachine.MANA_ASSEMBLER[GTValues.IV].asStack(),
                    "ABA", "CDC", "AEA",
                    'A', new MaterialEntry(TagPrefix.plate, GTOMaterials.Alfsteel), 'B', GTItems.ROBOT_ARM_IV.asStack(), 'C', CustomTags.IV_CIRCUITS, 'D', ManaMachine.MANA_HULL[GTValues.IV].asStack(), 'E', GTItems.FIELD_GENERATOR_IV.asStack());
            VanillaRecipeHelper.addShapedRecipe(true, GTOCore.id("luv_mana_assembler"), ManaMachine.MANA_ASSEMBLER[GTValues.LuV].asStack(),
                    "ABA", "CDC", "AEA",
                    'A', new MaterialEntry(TagPrefix.plate, GTOMaterials.Gaiasteel), 'B', GTItems.ROBOT_ARM_LuV.asStack(), 'C', CustomTags.LuV_CIRCUITS, 'D', ManaMachine.MANA_HULL[GTValues.LuV].asStack(), 'E', GTItems.FIELD_GENERATOR_LuV.asStack());

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

            VanillaRecipeHelper.addShapedRecipe(GTOCore.id("area_destruction_tools"), ManaMachine.AREA_DESTRUCTION_TOOLS.asStack(),
                    "ABA", "CDC", "ABA",
                    'A', new ItemStack(Items.REPEATER.asItem()), 'B', GTBlocks.INDUSTRIAL_TNT.asStack(), 'C', GTOBlocks.NUKE_BOMB.asStack(), 'D', ManaMachine.MANA_HULL[GTValues.LuV].asStack());
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
