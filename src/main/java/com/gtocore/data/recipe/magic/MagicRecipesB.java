package com.gtocore.data.recipe.magic;

import com.gtocore.common.data.GTOBlocks;
import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMachines;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.machines.ManaMachine;
import com.gtocore.common.data.machines.ManaMultiBlock;
import com.gtocore.data.tag.Tags;

import com.gtolib.GTOCore;
import com.gtolib.api.data.GTODimensions;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.api.data.tag.TagPrefix;
import com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.common.data.GTRecipeCategories;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import mythicbotany.register.ModItems;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import static com.gregtechceu.gtceu.api.GTValues.*;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.*;
import static com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys.GAS;
import static com.gregtechceu.gtceu.api.fluids.store.FluidStorageKeys.LIQUID;
import static com.gregtechceu.gtceu.common.data.GTMaterials.*;
import static com.gtocore.api.data.tag.GTOTagPrefix.CRYSTAL_SEED;
import static com.gtocore.common.data.GTOItems.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;
import static com.gtocore.common.machine.mana.multiblock.ResonanceFlowerMachine.toResonanceTag;
import static com.gtocore.utils.PlayerHeadUtils.itemStackAddNbtString;

public final class MagicRecipesB {

    public static void init() {
        // 炼金锅3
        {
            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("cycle_of_blossoms_solvent_fust")
                    .inputItems(COLORFUL_MYSTICAL_FLOWER, 32)
                    .inputItems(dust, StarStone, 4)
                    .inputFluids(FractalPetalSolvent, 2000)
                    .inputFluids(Ethanol, 1000)
                    .chancedOutput(CycleofBlossomsSolvent.getFluid(1500), 10, 0)
                    .chancedOutput(FractalPetalSolvent.getFluid(250), 1, 0)
                    .duration(240)
                    .temperature(1200)
                    .addData("param1", 20)
                    .addData("param2", 20)
                    .addData("param3", 20)
                    .save();
        }

        // 魔力组装的各种配方
        {
            // 铭刻之布
            ASSEMBLER_RECIPES.recipeBuilder("affix_canvas")
                    .notConsumable("ars_nouveau:wilden_tribute")
                    .notConsumable("botania:life_essence")
                    .notConsumable("extrabotany:hero_medal")
                    .inputItems("botania:manaweave_cloth", 16)
                    .inputItems("apotheosis:uncommon_material", 8)
                    .inputItems("apotheosis:epic_material", 4)
                    .inputItems("apotheosis:mythic_material", 2)
                    .inputItems("apotheosis:infused_breath", 2)
                    .inputItems("apotheosis:gem_dust", 64)
                    .outputItems(GTOItems.AFFIX_CANVAS, 16)
                    .inputFluids(GTOMaterials.Animium, 1000)
                    .duration(20)
                    .MANAt(1024)
                    .save();

        }

        // 苍穹凝聚器
        {
            CELESTIAL_CONDENSER_RECIPES.recipeBuilder("astral_silver")
                    .inputItems(ingot, Silver)
                    .outputItems(ingot, AstralSilver)
                    .addData("lunara", 1000)
                    .duration(10)
                    .save();

            CELESTIAL_CONDENSER_RECIPES.recipeBuilder("helio_coal")
                    .inputItems(Items.COAL)
                    .outputItems(HELIO_COAL)
                    .addData("solaris", 1000)
                    .duration(10)
                    .save();

            CELESTIAL_CONDENSER_RECIPES.recipeBuilder("ender_diamond")
                    .inputItems(Items.DIAMOND)
                    .outputItems(ENDER_DIAMOND)
                    .addData("voidflux", 1000)
                    .duration(10)
                    .save();

            CELESTIAL_CONDENSER_RECIPES.recipeBuilder("star_stone_0")
                    .inputItems(BotaniaBlocks.shimmerrock.asItem())
                    .outputItems(GTOBlocks.STAR_STONE[0].asItem())
                    .addData("any", 2000)
                    .duration(10)
                    .save();

            for (int i = 0; i < 11; i++) {
                CELESTIAL_CONDENSER_RECIPES.recipeBuilder("star_stone_" + (i + 1))
                        .inputItems(GTOBlocks.STAR_STONE[i].asItem())
                        .outputItems(GTOBlocks.STAR_STONE[i + 1].asItem())
                        .addData("any", 2000 * (i + 2))
                        .duration(10)
                        .save();
            }

            CELESTIAL_CONDENSER_RECIPES.recipeBuilder("nether_star")
                    .inputItems(ModItems.fadedNetherStar)
                    .outputItems(Items.NETHER_STAR)
                    .addData("voidflux", 4000)
                    .duration(10)
                    .save();
        }

        // 符文铭刻
        {
            Item[] runeItem1 = {
                    BotaniaItems.runeEarth, BotaniaItems.runeAir, BotaniaItems.runeFire, BotaniaItems.runeWater,
                    BotaniaItems.runeSpring, BotaniaItems.runeSummer, BotaniaItems.runeAutumn, BotaniaItems.runeWinter,
                    BotaniaItems.runeMana, BotaniaItems.runeLust, BotaniaItems.runeGluttony, BotaniaItems.runeGreed,
                    BotaniaItems.runeSloth, BotaniaItems.runeWrath, BotaniaItems.runeEnvy, BotaniaItems.runePride,
            };
            Item[] runeItem2 = {
                    ModItems.asgardRune, ModItems.vanaheimRune, ModItems.alfheimRune,
                    ModItems.midgardRune, ModItems.joetunheimRune, ModItems.muspelheimRune,
                    ModItems.niflheimRune, ModItems.nidavellirRune, ModItems.helheimRune,
            };

            for (Item rune : runeItem1) {
                RUNE_ENGRAVING_RECIPES.recipeBuilder("engraving_" + rune.toString())
                        .notConsumable(rune)
                        .inputItems(BotaniaBlocks.livingrock.asItem())
                        .inputFluids(Animium, 3000)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 1000)
                        .outputItems(rune, 9)
                        .MANAt(128)
                        .duration(200)
                        .save();
            }
            for (Item rune : runeItem2) {
                RUNE_ENGRAVING_RECIPES.recipeBuilder("engraving_" + rune.toString())
                        .notConsumable(rune)
                        .inputItems(BotaniaBlocks.livingrock.asItem())
                        .inputFluids(Animium, 9000)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 1000)
                        .outputItems(rune, 9)
                        .MANAt(256)
                        .duration(200)
                        .save();
            }
        }

        // 权宜之计
        {
            // 魔力输入输出
            {
                int[] values = { GTValues.ZPM, GTValues.UV, GTValues.UHV, GTValues.UEV, GTValues.UIV, GTValues.UXV, GTValues.OpV };
                for (int value : values) {
                    VanillaRecipeHelper.addShapedRecipe(GTOCore.id(VN[value].toLowerCase() + "_mana_extract_hatch"), ManaMachine.MANA_EXTRACT_HATCH[value],
                            "AEA", "CDC", "AEA",
                            'A', GTOItems.STOPGAP_MEASURES.asStack(), 'C', GTMachines.BATTERY_BUFFER_16[value].asStack(), 'D', GTMachines.CHARGER_4[value].asStack(), 'E', GTMachines.ENERGY_INPUT_HATCH[value].asStack());

                    VanillaRecipeHelper.addShapedRecipe(GTOCore.id(VN[value].toLowerCase() + "_mana_input_hatch"), ManaMachine.MANA_INPUT_HATCH[value],
                            "AAA", "ABA", "AAA",
                            'A', GTOItems.STOPGAP_MEASURES.asStack(), 'B', GTMachines.SUBSTATION_ENERGY_INPUT_HATCH[value].asStack());

                    VanillaRecipeHelper.addShapedRecipe(GTOCore.id(VN[value].toLowerCase() + "_mana_output_hatch"), ManaMachine.MANA_OUTPUT_HATCH[value],
                            "AAA", "ABA", "AAA",
                            'A', GTOItems.STOPGAP_MEASURES.asStack(), 'B', GTMachines.SUBSTATION_ENERGY_OUTPUT_HATCH[value].asStack());

                    VanillaRecipeHelper.addShapedRecipe(GTOCore.id(VN[value].toLowerCase() + "_wireless_mana_input_hatch"), ManaMachine.WIRELESS_MANA_INPUT_HATCH[value],
                            "AAA", "ABA", "AAA",
                            'A', GTOItems.STOPGAP_MEASURES.asStack(), 'B', GTOMachines.WIRELESS_INPUT_HATCH_64[value].asStack());

                    VanillaRecipeHelper.addShapedRecipe(GTOCore.id(VN[value].toLowerCase() + "_wireless_mana_output_hatch"), ManaMachine.WIRELESS_MANA_OUTPUT_HATCH[value],
                            "AAA", "ABA", "AAA",
                            'A', GTOItems.STOPGAP_MEASURES.asStack(), 'B', GTOMachines.WIRELESS_OUTPUT_HATCH_64[value].asStack());
                }
            }
        }

        // 凋零的下界之星
        ARC_GENERATOR_RECIPES.recipeBuilder("make_faded_nether_star")
                .inputItems(dust, NetherEmber, 64)
                .inputItems(dust, StarStone)
                .inputFluids(NetherAir, 8000)
                .inputFluids(Salamander.getFluid(LIQUID, 200))
                .inputFluids(Mana, 32000)
                .outputItems(itemStackAddNbtString(ModItems.fadedNetherStar.getDefaultInstance(), "{Damage:1100000}"), 4)
                .duration(20)
                .EUt(VA[IV])
                .save();

        ARC_GENERATOR_RECIPES.recipeBuilder("make_nether_star")
                .inputItems(ModItems.fadedNetherStar, 4)
                .inputItems(dustTiny, NetherStar)
                .outputItems(Items.NETHER_STAR, 4)
                .inputFluids(TheWaterFromTheWellOfWisdom, 1000)
                .duration(20)
                .EUt(VA[UV])
                .save();

        // 液态魔力&魔力结晶
        {
            VACUUM_RECIPES.recipeBuilder("vacuum_mana_liquid")
                    .inputFluids(Mana.getFluid(GAS, 100000))
                    .outputFluids(Mana.getFluid(LIQUID, 1000))
                    .duration(2000)
                    .EUt(VA[HV])
                    .save();

            AUTOCLAVE_RECIPES.recipeBuilder("mana_crystal_seed")
                    .inputItems(gemExquisite, ManaDiamond)
                    .inputItems(gemExquisite, SourceGem)
                    .inputFluids(Mana.getFluid(LIQUID, 500))
                    .outputItems(CRYSTAL_SEED, Mana, 64)
                    .duration(100)
                    .EUt(VA[ULV])
                    .save();

            CRYSTALLIZATION_RECIPES.recipeBuilder("mana_crystal")
                    .inputItems(CRYSTAL_SEED, Mana)
                    .inputFluids(TheWaterFromTheWellOfWisdom, 500)
                    .outputItems(MANA_CRYSTAL)
                    .blastFurnaceTemp(3200)
                    .duration(1000)
                    .EUt(VA[HV])
                    .save();

            FORGE_HAMMER_RECIPES.recipeBuilder("mana_crystal_to_mana_crystal_seed")
                    .inputItems(MANA_CRYSTAL)
                    .outputItems(CRYSTAL_SEED, Mana, 4)
                    .duration(100)
                    .EUt(VA[ULV])
                    .save();
        }

        // 精粹回收
        {
            CHEMICAL_BATH_RECIPES.builder("enchantment_essence_recovery")
                    .inputItems(Tags.ENCHANTMENT_ESSENCE)
                    .inputFluids(TheWaterFromTheWellOfWisdom, 5)
                    .outputItems("gtocore:enchantment_essence_original")
                    .duration(20)
                    .EUt(8)
                    .save();

            CHEMICAL_BATH_RECIPES.builder("affix_essence_recovery")
                    .inputItems(Tags.AFFIX_ESSENCE)
                    .inputFluids(TheWaterFromTheWellOfWisdom, 5)
                    .outputItems("gtocore:affix_essence_original")
                    .duration(20)
                    .EUt(8)
                    .save();

        }

        INFUSER_CORE_RECIPES.builder("resonance_flower")
                .notConsumable(ModItems.fimbultyrTablet)
                .notConsumable(ExtraBotanyItems.manaRingMaster)
                .notConsumable(BotaniaItems.dice)
                .notConsumable(ExtraBotanyItems.pandorasBox)
                .inputItems(GTOBlocks.THE_ORIGIN_CASING.asItem(), 16)
                .inputItems(GTOBlocks.THE_END_CASING.asItem(), 16)
                .inputItems(GTOBlocks.THE_CHAOS_CASING.asItem(), 16)
                .inputItems(TagPrefix.gemFlawless, GTOMaterials.OriginCoreCrystal, 16)
                .inputItems(TagPrefix.gemFlawless, GTOMaterials.StarBloodCrystal, 16)
                .inputItems(TagPrefix.gemFlawless, GTOMaterials.SoulJadeCrystal, 16)
                .inputItems(TagPrefix.gemFlawless, GTOMaterials.RemnantSpiritStone, 16)
                .inputItems(TagPrefix.block, GTOMaterials.Runerock, 64)
                .inputItems(GTOBlocks.STAR_STONE[4], 64)
                .inputItems(GTOItems.PHILOSOPHERS_STONE)
                .inputItems(ItemsRegistry.WILDEN_TRIBUTE, 64)
                .inputItems(ItemsRegistry.MANIPULATION_ESSENCE, 64)
                .inputItems(TagPrefix.gem, GTMaterials.NetherStar, 64)
                .inputItems(AFFIX_ESSENCE.get("apotheosis:sword/special/thunderstruck"), 16)
                .inputFluids(GTMaterials.MaragingSteel300, L * 9 * 16)
                .inputFluids(GTOMaterials.EnergySolidifier, 8000)
                .inputFluids(GTOMaterials.Aether, FluidStorageKeys.LIQUID, 8000)
                .outputItems(ManaMultiBlock.RESONANCE_FLOWER)
                .duration(1200)
                .MANAt(32768)
                .save();

        // 元素共鸣
        {
            ELEMENTAL_RESONANCE.recipeBuilder("fluctuation")
                    .inputItems(ManaMultiBlock.RESONANCE_FLOWER)
                    .outputItems(ManaMultiBlock.RESONANCE_FLOWER)
                    .addData("resonance", toResonanceTag(ChemicalHelper.get(dust, Livingrock), 5))
                    .MANAt(4)
                    .duration(10)
                    .circuitMeta(32)
                    .save();

            ELEMENTAL_RESONANCE.recipeBuilder("recycle_life_essence_from_gaia_dust")
                    .notConsumable(BotaniaItems.lifeEssence)
                    .inputItems(dust, Gaia, 16)
                    .inputItems(dust, StarStone)
                    .inputFluids(FinalPurifier, 250)
                    .outputItems(BotaniaItems.lifeEssence, 8)
                    .dimension(GTODimensions.ALFHEIM)
                    .addData("resonance", toResonanceTag(TheWaterFromTheWellOfWisdom.getFluid(100), 10))
                    .MANAt(128)
                    .duration(3600)
                    .circuitMeta(8)
                    .save();

            ELEMENTAL_RESONANCE.recipeBuilder("recycle_nether_star_from_netherember_dust")
                    .notConsumable(Items.NETHER_STAR)
                    .inputItems(dust, NetherEmber, 256)
                    .inputItems(dust, StarStone)
                    .inputFluids(EnergySolidifier, 250)
                    .outputItems(Items.NETHER_STAR, 16)
                    .dimension(GTODimensions.ALFHEIM)
                    .addData("resonance", toResonanceTag(TheWaterFromTheWellOfWisdom.getFluid(100), 10))
                    .MANAt(128)
                    .duration(3600)
                    .circuitMeta(8)
                    .save();
        }

        // 命树灵脉 - 处理线
        {
            // 基础副产
            {
                CHEMICAL_BATH_RECIPES.recipeBuilder("origin_core_crystal_crushed_ore_to_purified_ore")
                        .inputItems(crushed, OriginCoreCrystal)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 1000)
                        .outputItems(crushedPurified, OriginCoreCrystal)
                        .chancedOutput(SOURCE_SPIRIT_DEBRIS.asItem(), 5000, 300)
                        .chancedOutput(dust, OriginCoreCrystal, 2000, 0)
                        .duration(200).EUt(VA[LV])
                        .category(GTRecipeCategories.ORE_BATHING)
                        .save();

                ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder("origin_core_crystal_pure_dust_to_dust")
                        .inputItems(dustPure, OriginCoreCrystal)
                        .outputItems(dust, OriginCoreCrystal)
                        .chancedOutput(HOLY_ROOT_MYCELIUM.asItem(), 1000, 250)
                        .chancedOutput(dust, OriginCoreCrystal, 500, 0)
                        .duration(200).EUt(24)
                        .save();

                CHEMICAL_BATH_RECIPES.recipeBuilder("star_blood_crystal_crushed_ore_to_purified_ore")
                        .inputItems(crushed, StarBloodCrystal)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 1000)
                        .outputItems(crushedPurified, StarBloodCrystal)
                        .chancedOutput(STAR_DEBRIS_SAND.asItem(), 5000, 300)
                        .chancedOutput(dust, StarBloodCrystal, 2000, 0)
                        .duration(200).EUt(VA[LV])
                        .category(GTRecipeCategories.ORE_BATHING)
                        .save();

                ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder("star_blood_crystal_pure_dust_to_dust")
                        .inputItems(dustPure, StarBloodCrystal)
                        .outputItems(dust, StarBloodCrystal)
                        .chancedOutput(VEIN_BLOOD_MUCUS.asItem(), 1000, 250)
                        .chancedOutput(dust, StarBloodCrystal, 500, 0)
                        .duration(200).EUt(24)
                        .save();

                CHEMICAL_BATH_RECIPES.recipeBuilder("soul_jade_crystal_crushed_ore_to_purified_ore")
                        .inputItems(crushed, SoulJadeCrystal)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 1000)
                        .outputItems(crushedPurified, SoulJadeCrystal)
                        .chancedOutput(SOUL_SHADOW_DUST.asItem(), 5000, 300)
                        .chancedOutput(dust, SoulJadeCrystal, 2000, 0)
                        .duration(200).EUt(VA[LV])
                        .category(GTRecipeCategories.ORE_BATHING)
                        .save();

                ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder("soul_jade_crystal_pure_dust_to_dust")
                        .inputItems(dustPure, SoulJadeCrystal)
                        .outputItems(dust, SoulJadeCrystal)
                        .chancedOutput(CONSCIOUSNESS_THREAD.asItem(), 1000, 250)
                        .chancedOutput(dust, SoulJadeCrystal, 500, 0)
                        .duration(200).EUt(24)
                        .save();

                CHEMICAL_BATH_RECIPES.recipeBuilder("remnant_spirit_stone_crushed_ore_to_purified_ore")
                        .inputItems(crushed, RemnantSpiritStone)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 1000)
                        .outputItems(crushedPurified, RemnantSpiritStone)
                        .chancedOutput(BONE_ASH_GRANULE.asItem(), 5000, 300)
                        .chancedOutput(dust, RemnantSpiritStone, 2000, 0)
                        .duration(200).EUt(VA[LV])
                        .category(GTRecipeCategories.ORE_BATHING)
                        .save();

                ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder("remnant_spirit_stone_pure_dust_to_dust")
                        .inputItems(dustPure, RemnantSpiritStone)
                        .outputItems(dust, RemnantSpiritStone)
                        .chancedOutput(SPIRIT_BONE_FRAGMENT.asItem(), 1000, 250)
                        .chancedOutput(dust, RemnantSpiritStone, 500, 0)
                        .duration(200).EUt(24)
                        .save();
            }

            // 催化剂
            {
                MIXER_RECIPES.recipeBuilder("source_energy_extract")
                        .inputItems(dust, OriginCoreCrystal)
                        .inputItems(STAR_DEBRIS_SAND)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 2000)
                        .inputFluids(Mana.getFluid(LIQUID, 500))
                        .outputFluids(SourceEnergyExtract, 2500)
                        .outputItems(dust, ExtractionResidue)
                        .duration(8000)
                        .EUt(VA[LV])
                        .save();

                REACTION_FURNACE_RECIPES.recipeBuilder("star_vein_fusion")
                        .inputItems(dust, StarBloodCrystal)
                        .inputItems(SOUL_SHADOW_DUST)
                        .inputItems(MANA_CRYSTAL)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 3000)
                        .outputFluids(StarVeinFusion, 3200)
                        .outputItems(dust, FusionResidue)
                        .blastFurnaceTemp(4200)
                        .duration(8000)
                        .EUt(VA[LV])
                        .save();

                ARC_GENERATOR_RECIPES.recipeBuilder("star_vein_fusion")
                        .inputItems(dust, SoulJadeCrystal)
                        .inputItems(BONE_ASH_GRANULE)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 1800)
                        .inputFluids(Mana.getFluid(LIQUID, 500))
                        .outputFluids(SoulThoughtHarmony, 2200)
                        .outputItems(dust, HarmonyResidue)
                        .duration(8000)
                        .EUt(VA[LV])
                        .save();

                INCUBATOR_RECIPES.recipeBuilder("remnant_erosion_activate")
                        .inputItems(dust, RemnantSpiritStone)
                        .inputItems(SOURCE_SPIRIT_DEBRIS)
                        .inputItems(MANA_CRYSTAL)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 2500)
                        .outputFluids(RemnantErosionActivate, 2800)
                        .outputItems(dust, ErosionActivateResidue)
                        .duration(8000)
                        .EUt(VA[LV])
                        .save();

                ALCHEMY_CAULDRON_RECIPES.recipeBuilder("final_purifier")
                        .inputItems(dust, OriginCoreCrystal)
                        .inputItems(dust, StarBloodCrystal)
                        .inputItems(dust, SoulJadeCrystal)
                        .inputItems(dust, RemnantSpiritStone)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 1000)
                        .outputFluids(FinalPurifier, 800)
                        .duration(400)
                        .temperature(1200)
                        .MANAt(4)
                        .save();

                AUTOCLAVE_RECIPES.recipeBuilder("energy_solidifier")
                        .inputItems(SPIRIT_BONE_FRAGMENT)
                        .inputItems(CONSCIOUSNESS_THREAD)
                        .inputFluids(FinalPurifier, 600)
                        .outputFluids(EnergySolidifier, 800)
                        .duration(400)
                        .EUt(VA[EV])
                        .save();

            }

            // 循环催化剂
            {
                // 源核晶
                {
                    MIXER_RECIPES.recipeBuilder("origin_core_energy_body")
                            .inputItems(dust, OriginCoreCrystal, 2)
                            .inputItems(SOURCE_SPIRIT_DEBRIS, 3)
                            .inputItems(MANA_CRYSTAL)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 500)
                            .outputItems(ORIGIN_CORE_ENERGY_BODY)
                            .duration(600)
                            .EUt(VA[HV])
                            .save();

                    AUTOCLAVE_RECIPES.recipeBuilder("source_energy_catalyst_embryo")
                            .inputItems(ORIGIN_CORE_ENERGY_BODY)
                            .inputItems(HOLY_ROOT_MYCELIUM)
                            .inputFluids(SourceEnergyExtract, 300)
                            .outputItems(SOURCE_ENERGY_CATALYST_EMBRYO)
                            .duration(800)
                            .EUt(VA[EV])
                            .save();

                    DEHYDRATOR_RECIPES.recipeBuilder("source_energy_catalyst_crystal")
                            .inputItems(SOURCE_ENERGY_CATALYST_EMBRYO)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 500)
                            .inputFluids(Mana.getFluid(LIQUID, 500))
                            .outputItems(SOURCE_ENERGY_CATALYST_CRYSTAL)
                            .duration(400)
                            .EUt(VA[MV])
                            .save();

                    FORGE_HAMMER_RECIPES.recipeBuilder("regenerated_source_energy_body")
                            .inputItems(SOURCE_ENERGY_CATALYST_CRYSTAL_SHARD)
                            .outputItems(REGENERATED_SOURCE_ENERGY_BODY)
                            .duration(100)
                            .EUt(VA[ULV])
                            .save();

                    AUTOCLAVE_RECIPES.recipeBuilder("recycle_source_energy_catalyst_crystal")
                            .inputItems(REGENERATED_SOURCE_ENERGY_BODY)
                            .inputItems(dust, OriginCoreCrystalResidue)
                            .inputFluids(SourceEnergyExtract, 300)
                            .outputItems(SOURCE_ENERGY_CATALYST_CRYSTAL)
                            .duration(800)
                            .EUt(VA[EV])
                            .save();
                }

                // 星血晶
                {
                    REACTION_FURNACE_RECIPES.recipeBuilder("star_vein_base")
                            .inputItems(dust, StarBloodCrystal, 3)
                            .inputItems(STAR_DEBRIS_SAND, 2)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 2500)
                            .inputFluids(Mana.getFluid(LIQUID, 500))
                            .outputFluids(StarVeinBase, 2000)
                            .blastFurnaceTemp(3800)
                            .duration(1000)
                            .EUt(VA[HV])
                            .save();

                    DIGESTION_TREATMENT_RECIPES.recipeBuilder("star_vein_active")
                            .inputItems(VEIN_BLOOD_MUCUS)
                            .inputFluids(StarVeinBase, 2000)
                            .outputFluids(StarVeinActive, 1800)
                            .blastFurnaceTemp(4200)
                            .duration(6000)
                            .EUt(VA[MV])
                            .save();

                    CHEMICAL_RECIPES.recipeBuilder("star_vein_catalyst_precursor")
                            .inputItems(MANA_CRYSTAL)
                            .inputFluids(StarVeinActive, 180)
                            .inputFluids(StarVeinFusion, 500)
                            .outputFluids(StarVeinCatalystPrecursor, 1500)
                            .duration(600)
                            .EUt(VA[IV])
                            .save();

                    MIXER_RECIPES.recipeBuilder("star_vein_catalyst")
                            .inputItems(MANA_CRYSTAL)
                            .inputFluids(StarVeinCatalystPrecursor, 1500)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 300)
                            .inputFluids(Mana.getFluid(LIQUID, 500))
                            .outputFluids(StarVeinCatalyst, 1600)
                            .duration(4000)
                            .EUt(VA[MV])
                            .save();

                    DISTILLERY_RECIPES.recipeBuilder("purified_star_vein_catalyst_waste")
                            .inputFluids(StarVeinCatalystWaste, 1000)
                            .outputFluids(PurifiedStarVeinCatalystWaste, 600)
                            .duration(2000)
                            .EUt(VA[HV])
                            .save();

                    DIGESTION_TREATMENT_RECIPES.recipeBuilder("regenerated_star_vein_active")
                            .inputItems(dust, StarBloodCrystal)
                            .inputItems(MANA_CRYSTAL)
                            .inputFluids(PurifiedStarVeinCatalystWaste, 800)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 500)
                            .outputFluids(RegeneratedStarVeinActive, 900)
                            .blastFurnaceTemp(3600)
                            .duration(600)
                            .EUt(VA[IV])
                            .save();

                    MIXER_RECIPES.recipeBuilder("recycle_star_vein_catalyst")
                            .inputItems(VEIN_BLOOD_MUCUS)
                            .inputItems(dust, StarBloodCrystalResidue, 2)
                            .inputFluids(RegeneratedStarVeinActive, 1800)
                            .inputFluids(Mana.getFluid(LIQUID, 500))
                            .outputFluids(StarVeinCatalyst, 2000)
                            .duration(2000)
                            .EUt(VA[MV])
                            .save();
                }

                // 魂玉晶
                {
                    AUTOCLAVE_RECIPES.recipeBuilder("soul_thought_condensate")
                            .inputItems(dust, SoulJadeCrystal, 2)
                            .inputItems(SOUL_SHADOW_DUST, 3)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 600)
                            .outputItems(SOUL_THOUGHT_CONDENSATE)
                            .duration(600)
                            .EUt(VA[EV])
                            .save();

                    ALLOY_SMELTER_RECIPES.recipeBuilder("anchored_soul_core")
                            .inputItems(SOUL_THOUGHT_CONDENSATE)
                            .inputItems(CONSCIOUSNESS_THREAD)
                            .outputItems(ANCHORED_SOUL_CORE)
                            .duration(4000)
                            .EUt(VA[MV])
                            .save();

                    ARC_FURNACE_RECIPES.recipeBuilder("soul_thought_catalyst_embryo")
                            .inputItems(ANCHORED_SOUL_CORE)
                            .inputFluids(SoulThoughtHarmony, 400)
                            .outputItems(SOUL_THOUGHT_CATALYST_EMBRYO)
                            .duration(3000)
                            .EUt(VA[HV])
                            .save();

                    ISOSTATIC_PRESSING_RECIPES.recipeBuilder("soul_thought_catalyst_core")
                            .inputItems(SOUL_THOUGHT_CATALYST_EMBRYO)
                            .inputItems(STAR_DEBRIS_SAND)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 800)
                            .inputFluids(Mana.getFluid(LIQUID, 500))
                            .outputItems(SOUL_THOUGHT_CATALYST_CORE)
                            .duration(6000)
                            .EUt(VA[EV])
                            .save();

                    AUTOCLAVE_RECIPES.recipeBuilder("regenerated_soul_core")
                            .inputItems(SOUL_THOUGHT_CATALYST_CORE_SHARD)
                            .inputItems(MANA_CRYSTAL)
                            .inputFluids(SoulThoughtHarmony, 200)
                            .outputItems(REGENERATED_SOUL_CORE)
                            .duration(600)
                            .EUt(VA[EV])
                            .save();

                    ISOSTATIC_PRESSING_RECIPES.recipeBuilder("recycle_soul_thought_catalyst_core")
                            .inputItems(REGENERATED_SOUL_CORE)
                            .inputItems(dust, SoulJadeCrystalResidue, 2)
                            .inputItems(CONSCIOUSNESS_THREAD)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 800)
                            .inputFluids(Mana.getFluid(LIQUID, 500))
                            .outputItems(SOUL_THOUGHT_CATALYST_CORE)
                            .duration(2000)
                            .EUt(VA[HV])
                            .save();
                }

                // 骸灵石
                {

                    MIXER_RECIPES.recipeBuilder("remnant_energy_adsorber")
                            .inputItems(dust, RemnantSpiritStone, 3)
                            .inputItems(BONE_ASH_GRANULE, 4)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 700)
                            .outputItems(REMNANT_ENERGY_ADSORBER)
                            .duration(800)
                            .EUt(VA[MV])
                            .save();

                    CHEMICAL_RECIPES.recipeBuilder("remnant_erosion_catalyst_embryo")
                            .inputItems(REMNANT_ENERGY_ADSORBER)
                            .inputItems(SPIRIT_BONE_FRAGMENT)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 300)
                            .inputFluids(RemnantErosionActivate, 500)
                            .outputItems(REMNANT_EROSION_CATALYST_EMBRYO)
                            .inputFluids(Mana.getFluid(LIQUID, 500))
                            .duration(200)
                            .EUt(VA[EV])
                            .save();

                    MACERATOR_RECIPES.recipeBuilder("remnant_erosion_catalyst")
                            .inputItems(REMNANT_EROSION_CATALYST_EMBRYO)
                            .outputItems(dust, RemnantErosionCatalyst, 3)
                            .duration(4000)
                            .EUt(VA[EV])
                            .save();

                    MIXER_RECIPES.recipeBuilder("regenerated_remnant_energy_adsorber")
                            .inputItems(dust, InactiveRemnantErosionCatalyst, 5)
                            .inputItems(dust, RemnantSpiritStoneResidue, 2)
                            .inputFluids(Mana.getFluid(LIQUID, 500))
                            .outputItems(REGENERATED_REMNANT_ENERGY_ADSORBER)
                            .duration(800)
                            .EUt(VA[MV])
                            .save();

                    CHEMICAL_RECIPES.recipeBuilder("recycle_remnant_erosion_catalyst")
                            .inputItems(REGENERATED_REMNANT_ENERGY_ADSORBER)
                            .inputFluids(RemnantErosionActivate, 200)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 300)
                            .outputItems(REMNANT_EROSION_CATALYST_EMBRYO)
                            .duration(200)
                            .EUt(VA[EV])
                            .save();
                }
            }

            // 简化产线&残渣产线
            {
                int chance = GTOCore.isEasy() ? 500 : 50;
                int chanceBoost = GTOCore.isEasy() ? 100 : 10;
                // 源核晶
                {
                    CHEMICAL_BATH_RECIPES.recipeBuilder("purify_refined_origin_core_crystal_ore")
                            .inputItems(crushedRefined, OriginCoreCrystal)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 400)
                            .outputItems(PURIFY_REFINED_ORIGIN_CORE_CRYSTAL_ORE)
                            .duration(2000).EUt(VA[HV])
                            .category(GTRecipeCategories.ORE_BATHING)
                            .save();

                    INFUSER_CORE_RECIPES.recipeBuilder("crudely_purified_origin_core_crystal_ore")
                            .inputItems(PURIFY_REFINED_ORIGIN_CORE_CRYSTAL_ORE, 5)
                            .inputItems(dust, OriginCoreCrystal, 5)
                            .inputItems(HOLY_ROOT_MYCELIUM)
                            .inputFluids(SourceEnergyExtract, 2000)
                            .inputFluids(FinalPurifier, 800)
                            .inputFluids(CycleofBlossomsSolvent, 200)
                            .outputItems(CRUDELY_PURIFIED_ORIGIN_CORE_CRYSTAL_ORE, 5)
                            .duration(8000).MANAt(VA[MV])
                            .save();

                    SIFTER_RECIPES.recipeBuilder("sifter_crudely_purified_origin_core_crystal_ore")
                            .inputItems(CRUDELY_PURIFIED_ORIGIN_CORE_CRYSTAL_ORE)
                            .chancedOutput(gemFlawless, OriginCoreCrystal, chance, chanceBoost)
                            .chancedOutput(dust, OriginCoreCrystalResidue, 10000 - chance, -chanceBoost)
                            .chancedOutput(dustPure, OriginCoreCrystal, 5000, 0)
                            .duration(600).EUt(VA[HV])
                            .save();
                }

                // 星血晶
                {
                    CHEMICAL_BATH_RECIPES.recipeBuilder("purify_refined_star_blood_crystal_ore")
                            .inputItems(crushedRefined, StarBloodCrystal)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 400)
                            .outputItems(PURIFY_REFINED_STAR_BLOOD_CRYSTAL_ORE)
                            .duration(2000).EUt(VA[HV])
                            .category(GTRecipeCategories.ORE_BATHING)
                            .save();

                    INFUSER_CORE_RECIPES.recipeBuilder("crudely_fused_star_blood_crystal_ore")
                            .inputItems(PURIFY_REFINED_STAR_BLOOD_CRYSTAL_ORE, 5)
                            .inputItems(dust, StarBloodCrystal, 5)
                            .inputItems(VEIN_BLOOD_MUCUS)
                            .inputFluids(StarVeinFusion, 2000)
                            .inputFluids(EnergySolidifier, 800)
                            .inputFluids(CycleofBlossomsSolvent, 200)
                            .outputItems(CRUDELY_FUSED_STAR_BLOOD_CRYSTAL_ORE, 5)
                            .duration(8000).MANAt(VA[MV])
                            .save();

                    SIFTER_RECIPES.recipeBuilder("sifter_crudely_fused_star_blood_crystal_ore")
                            .inputItems(CRUDELY_FUSED_STAR_BLOOD_CRYSTAL_ORE)
                            .chancedOutput(gemFlawless, StarBloodCrystal, chance, chanceBoost)
                            .chancedOutput(dust, StarBloodCrystalResidue, 10000 - chance, -chanceBoost)
                            .chancedOutput(dustPure, StarBloodCrystal, 5000, 0)
                            .duration(600).EUt(VA[HV])
                            .save();
                }

                // 魂玉晶
                {
                    CHEMICAL_BATH_RECIPES.recipeBuilder("purify_refined_soul_jade_crystal_ore")
                            .inputItems(crushedRefined, SoulJadeCrystal)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 400)
                            .outputItems(PURIFY_REFINED_SOUL_JADE_CRYSTAL_ORE)
                            .duration(2000).EUt(VA[HV])
                            .category(GTRecipeCategories.ORE_BATHING)
                            .save();

                    INFUSER_CORE_RECIPES.recipeBuilder("crudely_harmonized_soul_jade_crystal_ore")
                            .inputItems(PURIFY_REFINED_SOUL_JADE_CRYSTAL_ORE, 5)
                            .inputItems(dust, SoulJadeCrystal, 5)
                            .inputItems(CONSCIOUSNESS_THREAD)
                            .inputFluids(SoulThoughtHarmony, 2000)
                            .inputFluids(FinalPurifier, 800)
                            .inputFluids(CycleofBlossomsSolvent, 200)
                            .outputItems(CRUDELY_HARMONIZED_SOUL_JADE_CRYSTAL_ORE, 5)
                            .duration(8000).MANAt(VA[MV])
                            .save();

                    SIFTER_RECIPES.recipeBuilder("sifter_crudely_harmonized_soul_jade_crystal_ore")
                            .inputItems(CRUDELY_HARMONIZED_SOUL_JADE_CRYSTAL_ORE)
                            .chancedOutput(gemFlawless, SoulJadeCrystal, chance, chanceBoost)
                            .chancedOutput(dust, SoulJadeCrystalResidue, 10000 - chance, -chanceBoost)
                            .chancedOutput(dustPure, SoulJadeCrystal, 5000, 0)
                            .duration(600).EUt(VA[HV])
                            .save();
                }

                // 骸灵石
                {
                    CHEMICAL_BATH_RECIPES.recipeBuilder("purify_refined_remnant_spirit_stone_ore")
                            .inputItems(crushedRefined, RemnantSpiritStone)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 400)
                            .outputItems(PURIFY_REFINED_REMNANT_SPIRIT_STONE_ORE)
                            .duration(2000).EUt(VA[HV])
                            .category(GTRecipeCategories.ORE_BATHING)
                            .save();

                    INFUSER_CORE_RECIPES.recipeBuilder("crudely_shaped_remnant_spirit_stone_ore")
                            .inputItems(PURIFY_REFINED_REMNANT_SPIRIT_STONE_ORE, 5)
                            .inputItems(dust, RemnantSpiritStone, 5)
                            .inputItems(SPIRIT_BONE_FRAGMENT)
                            .inputFluids(RemnantErosionActivate, 2000)
                            .inputFluids(EnergySolidifier, 800)
                            .inputFluids(CycleofBlossomsSolvent, 200)
                            .outputItems(CRUDELY_SHAPED_REMNANT_SPIRIT_STONE_ORE, 5)
                            .duration(8000).MANAt(VA[MV])
                            .save();

                    SIFTER_RECIPES.recipeBuilder("sifter_crudely_shaped_remnant_spirit_stone_ore")
                            .inputItems(CRUDELY_SHAPED_REMNANT_SPIRIT_STONE_ORE)
                            .chancedOutput(gemFlawless, RemnantSpiritStone, chance, chanceBoost)
                            .chancedOutput(dust, RemnantSpiritStoneResidue, 10000 - chance, -chanceBoost)
                            .chancedOutput(dustPure, RemnantSpiritStone, 5000, 0)
                            .duration(600).EUt(VA[HV])
                            .save();
                }
            }
        }
    }
}
