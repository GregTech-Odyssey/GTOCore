package com.gtocore.data.recipe.magic;

import com.gtocore.common.data.*;
import com.gtocore.common.data.machines.ManaMachine;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.common.data.GTRecipeCategories;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

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
import static com.gtocore.common.data.GTOItems.BoneAshGranule;
import static com.gtocore.common.data.GTOItems.HolyRootMycelium;
import static com.gtocore.common.data.GTOItems.SoulShadowDust;
import static com.gtocore.common.data.GTOItems.SourceSpiritDebris;
import static com.gtocore.common.data.GTOItems.StarDebrisSand;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;
import static com.gtocore.data.record.ApotheosisAffixRecord.getAffixSize;
import static com.gtocore.data.record.EnchantmentRecord.getEnchantmentSize;
import static com.gtocore.utils.PlayerHeadUtils.itemStackAddNbtString;

public final class MagicRecipesB {

    public static void init() {
        // 炼金锅3
        {
            ALCHEMY_CAULDRON_RECIPES.recipeBuilder("cycle_of_blossoms_solvent_fust")
                    .inputItems(COLORFUL_MYSTICAL_FLOWER, 32)
                    .inputItems(dust, StarStone, 4)
                    .inputFluids(FractalPetalSolvent.getFluid(2000))
                    .inputFluids(Ethanol.getFluid(1000))
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
                    .outputItems(GTOItems.AFFIX_CANVAS.asItem(), 16)
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
            Item[] runeItem = {
                    BotaniaItems.runeEarth, BotaniaItems.runeAir, BotaniaItems.runeFire, BotaniaItems.runeWater,
                    BotaniaItems.runeSpring, BotaniaItems.runeSummer, BotaniaItems.runeAutumn, BotaniaItems.runeWinter,
                    BotaniaItems.runeMana, BotaniaItems.runeLust, BotaniaItems.runeGluttony, BotaniaItems.runeGreed,
                    BotaniaItems.runeSloth, BotaniaItems.runeWrath, BotaniaItems.runeEnvy, BotaniaItems.runePride,
            };
            String[] runeString = {
                    "asgard_rune", "vanaheim_rune", "alfheim_rune",
                    "midgard_rune", "joetunheim_rune", "muspelheim_rune",
                    "niflheim_rune", "nidavellir_rune", "helheim_rune"
            };

            for (Item rune : runeItem) {
                RUNE_ENGRAVING_RECIPES.recipeBuilder("engraving_" + rune.toString())
                        .notConsumable(rune)
                        .inputItems(BotaniaBlocks.livingrock.asItem())
                        .inputFluids(Animium.getFluid(3000))
                        .inputFluids(TheWaterFromTheWellOfWisdom.getFluid(1000))
                        .outputItems(rune, 9)
                        .MANAt(128)
                        .duration(200)
                        .save();
            }
            for (String rune : runeString) {
                RUNE_ENGRAVING_RECIPES.recipeBuilder("engraving_" + rune)
                        .notConsumable("mythicbotany:" + rune)
                        .inputItems(BotaniaBlocks.livingrock.asItem())
                        .inputFluids(Animium.getFluid(9000))
                        .inputFluids(TheWaterFromTheWellOfWisdom.getFluid(1000))
                        .outputItems("mythicbotany:" + rune, 9)
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

        // 精粹回收
        {
            for (int i = 1; i < getEnchantmentSize(); i++) {
                CHEMICAL_BATH_RECIPES.recipeBuilder("enchantment_essence_recovery_" + i)
                        .inputItems(GTOItems.ENCHANTMENT_ESSENCE[i])
                        .inputFluids(TheWaterFromTheWellOfWisdom.getFluid(5))
                        .outputItems(GTOItems.ENCHANTMENT_ESSENCE[0])
                        .category(GTORecipeCategories.ESSENCE_RECOVERY)
                        .duration(20)
                        .EUt(8)
                        .save();
            }

            for (int i = 1; i < getAffixSize(); i++) {
                CHEMICAL_BATH_RECIPES.recipeBuilder("affix_essence_recovery_" + i)
                        .inputItems(GTOItems.AFFIX_ESSENCE[i])
                        .inputFluids(TheWaterFromTheWellOfWisdom.getFluid(5))
                        .outputItems(GTOItems.AFFIX_ESSENCE[0])
                        .category(GTORecipeCategories.ESSENCE_RECOVERY)
                        .duration(20)
                        .EUt(8)
                        .save();
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
                    .inputFluids(TheWaterFromTheWellOfWisdom, 50)
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

        // 命树灵脉 - 处理线
        {
            // 基础副产
            {
                CHEMICAL_BATH_RECIPES.recipeBuilder("origin_core_crystal_crushed_ore_to_purified_ore")
                        .inputItems(crushed, OriginCoreCrystal)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 1000)
                        .outputItems(crushedPurified, OriginCoreCrystal)
                        .chancedOutput(SourceSpiritDebris.asItem(), 5000, 300)
                        .chancedOutput(dust, OriginCoreCrystal, 2000, 0)
                        .duration(200).EUt(VA[LV])
                        .category(GTRecipeCategories.ORE_BATHING)
                        .save();

                ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder("origin_core_crystal_pure_dust_to_dust")
                        .inputItems(dustPure, OriginCoreCrystal)
                        .outputItems(dust, OriginCoreCrystal)
                        .chancedOutput(HolyRootMycelium.asItem(), 1000, 250)
                        .chancedOutput(dust, OriginCoreCrystal, 500, 0)
                        .duration(200).EUt(24)
                        .save();

                CHEMICAL_BATH_RECIPES.recipeBuilder("star_blood_crystal_crushed_ore_to_purified_ore")
                        .inputItems(crushed, StarBloodCrystal)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 1000)
                        .outputItems(crushedPurified, StarBloodCrystal)
                        .chancedOutput(StarDebrisSand.asItem(), 5000, 300)
                        .chancedOutput(dust, StarBloodCrystal, 2000, 0)
                        .duration(200).EUt(VA[LV])
                        .category(GTRecipeCategories.ORE_BATHING)
                        .save();

                ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder("star_blood_crystal_pure_dust_to_dust")
                        .inputItems(dustPure, StarBloodCrystal)
                        .outputItems(dust, StarBloodCrystal)
                        .chancedOutput(VeinBloodMucus.asItem(), 1000, 250)
                        .chancedOutput(dust, StarBloodCrystal, 500, 0)
                        .duration(200).EUt(24)
                        .save();

                CHEMICAL_BATH_RECIPES.recipeBuilder("soul_jade_crystal_crushed_ore_to_purified_ore")
                        .inputItems(crushed, SoulJadeCrystal)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 1000)
                        .outputItems(crushedPurified, SoulJadeCrystal)
                        .chancedOutput(SoulShadowDust.asItem(), 5000, 300)
                        .chancedOutput(dust, SoulJadeCrystal, 2000, 0)
                        .duration(200).EUt(VA[LV])
                        .category(GTRecipeCategories.ORE_BATHING)
                        .save();

                ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder("soul_jade_crystal_pure_dust_to_dust")
                        .inputItems(dustPure, SoulJadeCrystal)
                        .outputItems(dust, SoulJadeCrystal)
                        .chancedOutput(ConsciousnessThread.asItem(), 1000, 250)
                        .chancedOutput(dust, SoulJadeCrystal, 500, 0)
                        .duration(200).EUt(24)
                        .save();

                CHEMICAL_BATH_RECIPES.recipeBuilder("remnant_spirit_stone_crushed_ore_to_purified_ore")
                        .inputItems(crushed, RemnantSpiritStone)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 1000)
                        .outputItems(crushedPurified, RemnantSpiritStone)
                        .chancedOutput(BoneAshGranule.asItem(), 5000, 300)
                        .chancedOutput(dust, RemnantSpiritStone, 2000, 0)
                        .duration(200).EUt(VA[LV])
                        .category(GTRecipeCategories.ORE_BATHING)
                        .save();

                ELECTROMAGNETIC_SEPARATOR_RECIPES.recipeBuilder("remnant_spirit_stone_pure_dust_to_dust")
                        .inputItems(dustPure, RemnantSpiritStone)
                        .outputItems(dust, RemnantSpiritStone)
                        .chancedOutput(SpiritBoneFragment.asItem(), 1000, 250)
                        .chancedOutput(dust, RemnantSpiritStone, 500, 0)
                        .duration(200).EUt(24)
                        .save();
            }

            // 催化剂
            {
                MIXER_RECIPES.recipeBuilder("source_energy_extract")
                        .inputItems(dust, OriginCoreCrystal)
                        .inputItems(StarDebrisSand)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 200)
                        .inputFluids(Mana.getFluid(LIQUID, 50))
                        .outputFluids(SourceEnergyExtract, 250)
                        .outputItems(dust, ExtractionResidue)
                        .duration(8000)
                        .EUt(VA[LV])
                        .save();

                REACTION_FURNACE_RECIPES.recipeBuilder("star_vein_fusion")
                        .inputItems(dust, StarBloodCrystal)
                        .inputItems(SoulShadowDust)
                        .inputItems(MANA_CRYSTAL)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 300)
                        .outputFluids(StarVeinFusion, 320)
                        .outputItems(dust, FusionResidue)
                        .blastFurnaceTemp(4200)
                        .duration(8000)
                        .EUt(VA[LV])
                        .save();

                ARC_GENERATOR_RECIPES.recipeBuilder("star_vein_fusion")
                        .inputItems(dust, SoulJadeCrystal)
                        .inputItems(BoneAshGranule)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 180)
                        .inputFluids(Mana.getFluid(LIQUID, 50))
                        .outputFluids(SoulThoughtHarmony, 220)
                        .outputItems(dust, HarmonyResidue)
                        .duration(8000)
                        .EUt(VA[LV])
                        .save();

                INCUBATOR_RECIPES.recipeBuilder("remnant_erosion_activate")
                        .inputItems(dust, RemnantSpiritStone)
                        .inputItems(SourceSpiritDebris)
                        .inputItems(MANA_CRYSTAL)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 250)
                        .outputFluids(RemnantErosionActivate, 280)
                        .outputItems(dust, ErosionActivateResidue)
                        .duration(8000)
                        .EUt(VA[LV])
                        .save();

                ALCHEMY_CAULDRON_RECIPES.recipeBuilder("final_purifier")
                        .inputItems(dust, OriginCoreCrystal)
                        .inputItems(dust, StarBloodCrystal)
                        .inputItems(dust, SoulJadeCrystal)
                        .inputItems(dust, RemnantSpiritStone)
                        .inputFluids(TheWaterFromTheWellOfWisdom, 100)
                        .outputFluids(FinalPurifier, 80)
                        .duration(400)
                        .temperature(1200)
                        .MANAt(4)
                        .save();

                AUTOCLAVE_RECIPES.recipeBuilder("energy_solidifier")
                        .inputItems(SpiritBoneFragment)
                        .inputItems(ConsciousnessThread)
                        .inputFluids(FinalPurifier, 60)
                        .outputFluids(EnergySolidifier, 80)
                        .duration(400)
                        .EUt(VA[EV])
                        .save();

            }

            // 源核晶
            {
                // 循环催化剂
                {
                    MIXER_RECIPES.recipeBuilder("origin_core_energy_body")
                            .inputItems(dust, OriginCoreCrystal, 2)
                            .inputItems(SourceSpiritDebris, 3)
                            .inputItems(MANA_CRYSTAL)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 50)
                            .outputItems(ORIGIN_CORE_ENERGY_BODY)
                            .duration(600)
                            .EUt(VA[HV])
                            .save();

                    AUTOCLAVE_RECIPES.recipeBuilder("source_energy_catalyst_embryo")
                            .inputItems(ORIGIN_CORE_ENERGY_BODY)
                            .inputItems(HolyRootMycelium)
                            .inputFluids(SourceEnergyExtract, 30)
                            .outputItems(SOURCE_ENERGY_CATALYST_EMBRYO)
                            .duration(800)
                            .EUt(VA[EV])
                            .save();

                    DEHYDRATOR_RECIPES.recipeBuilder("source_energy_catalyst_crystal")
                            .inputItems(SOURCE_ENERGY_CATALYST_EMBRYO)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 50)
                            .inputFluids(Mana.getFluid(LIQUID, 50))
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
                            .inputFluids(SourceEnergyExtract, 30)
                            .outputItems(SOURCE_ENERGY_CATALYST_CRYSTAL)
                            .duration(800)
                            .EUt(VA[EV])
                            .save();
                }
            }

            // 星血晶
            {
                // 循环催化剂
                {
                    REACTION_FURNACE_RECIPES.recipeBuilder("star_vein_base")
                            .inputItems(dust, StarBloodCrystal, 3)
                            .inputItems(StarDebrisSand, 2)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 250)
                            .inputFluids(Mana.getFluid(LIQUID, 50))
                            .outputFluids(StarVeinBase, 200)
                            .blastFurnaceTemp(3800)
                            .duration(1000)
                            .EUt(VA[HV])
                            .save();

                    DIGESTION_TREATMENT_RECIPES.recipeBuilder("star_vein_active")
                            .inputItems(VeinBloodMucus)
                            .inputFluids(StarVeinBase, 200)
                            .outputFluids(StarVeinActive, 180)
                            .blastFurnaceTemp(4200)
                            .duration(6000)
                            .EUt(VA[MV])
                            .save();

                    CHEMICAL_RECIPES.recipeBuilder("star_vein_catalyst_precursor")
                            .inputItems(MANA_CRYSTAL)
                            .inputFluids(StarVeinActive, 180)
                            .inputFluids(StarVeinFusion, 50)
                            .outputFluids(StarVeinCatalystPrecursor, 150)
                            .duration(600)
                            .EUt(VA[IV])
                            .save();

                    MIXER_RECIPES.recipeBuilder("star_vein_catalyst")
                            .inputItems(MANA_CRYSTAL)
                            .inputFluids(StarVeinCatalystPrecursor, 150)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 30)
                            .inputFluids(Mana.getFluid(LIQUID, 50))
                            .outputFluids(StarVeinCatalyst, 160)
                            .duration(4000)
                            .EUt(VA[MV])
                            .save();

                    DISTILLERY_RECIPES.recipeBuilder("purified_star_vein_catalyst_waste")
                            .inputFluids(StarVeinCatalystWaste, 100)
                            .outputFluids(PurifiedStarVeinCatalystWaste, 60)
                            .duration(2000)
                            .EUt(VA[HV])
                            .save();

                    DIGESTION_TREATMENT_RECIPES.recipeBuilder("regenerated_star_vein_active")
                            .inputItems(dust, StarBloodCrystal)
                            .inputItems(MANA_CRYSTAL)
                            .inputFluids(PurifiedStarVeinCatalystWaste, 80)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 50)
                            .outputFluids(RegeneratedStarVeinActive, 90)
                            .blastFurnaceTemp(3600)
                            .duration(600)
                            .EUt(VA[IV])
                            .save();

                    MIXER_RECIPES.recipeBuilder("recycle_star_vein_catalyst")
                            .inputItems(VeinBloodMucus)
                            .inputItems(dust, StarBloodCrystalResidue, 2)
                            .inputFluids(RegeneratedStarVeinActive, 180)
                            .inputFluids(Mana.getFluid(LIQUID, 50))
                            .outputFluids(StarVeinCatalyst, 200)
                            .duration(2000)
                            .EUt(VA[MV])
                            .save();
                }
            }

            // 魂玉晶
            {
                // 循环催化剂
                {
                    AUTOCLAVE_RECIPES.recipeBuilder("soul_thought_condensate")
                            .inputItems(dust, SoulJadeCrystal, 2)
                            .inputItems(SoulShadowDust, 3)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 60)
                            .outputItems(SOUL_THOUGHT_CONDENSATE)
                            .duration(600)
                            .EUt(VA[EV])
                            .save();

                    ALLOY_SMELTER_RECIPES.recipeBuilder("anchored_soul_core")
                            .inputItems(SOUL_THOUGHT_CONDENSATE)
                            .inputItems(ConsciousnessThread)
                            .outputItems(ANCHORED_SOUL_CORE)
                            .duration(4000)
                            .EUt(VA[MV])
                            .save();

                    ARC_FURNACE_RECIPES.recipeBuilder("soul_thought_catalyst_embryo")
                            .inputItems(ANCHORED_SOUL_CORE)
                            .inputFluids(SoulThoughtHarmony, 40)
                            .outputItems(SOUL_THOUGHT_CATALYST_EMBRYO)
                            .duration(3000)
                            .EUt(VA[HV])
                            .save();

                    ISOSTATIC_PRESSING_RECIPES.recipeBuilder("soul_thought_catalyst_core")
                            .inputItems(SOUL_THOUGHT_CATALYST_EMBRYO)
                            .inputItems(StarDebrisSand)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 80)
                            .inputFluids(Mana.getFluid(LIQUID, 50))
                            .outputItems(SOUL_THOUGHT_CATALYST_CORE)
                            .duration(6000)
                            .EUt(VA[EV])
                            .save();

                    AUTOCLAVE_RECIPES.recipeBuilder("regenerated_soul_core")
                            .inputItems(SOUL_THOUGHT_CATALYST_CORE_SHARD)
                            .inputItems(MANA_CRYSTAL)
                            .inputFluids(SoulThoughtHarmony, 20)
                            .outputItems(REGENERATED_SOUL_CORE)
                            .duration(600)
                            .EUt(VA[EV])
                            .save();

                    ISOSTATIC_PRESSING_RECIPES.recipeBuilder("recycle_soul_thought_catalyst_core")
                            .inputItems(REGENERATED_SOUL_CORE)
                            .inputItems(dust, SoulJadeCrystalResidue, 2)
                            .inputItems(ConsciousnessThread)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 80)
                            .inputFluids(Mana.getFluid(LIQUID, 50))
                            .outputItems(SOUL_THOUGHT_CATALYST_CORE)
                            .duration(2000)
                            .EUt(VA[HV])
                            .save();
                }
            }

            // 骸灵石
            {
                // 循环催化剂
                {
                    MIXER_RECIPES.recipeBuilder("remnant_energy_adsorber")
                            .inputItems(dust, RemnantSpiritStone, 3)
                            .inputItems(BoneAshGranule, 4)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 70)
                            .outputItems(REMNANT_ENERGY_ADSORBER)
                            .duration(800)
                            .EUt(VA[MV])
                            .save();

                    CHEMICAL_RECIPES.recipeBuilder("remnant_erosion_catalyst_embryo")
                            .inputItems(REMNANT_ENERGY_ADSORBER)
                            .inputItems(SpiritBoneFragment)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 30)
                            .inputFluids(RemnantErosionActivate, 50)
                            .outputItems(REMNANT_EROSION_CATALYST_EMBRYO)
                            .inputFluids(Mana.getFluid(LIQUID, 50))
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
                            .inputFluids(Mana.getFluid(LIQUID, 50))
                            .outputItems(REGENERATED_REMNANT_ENERGY_ADSORBER)
                            .duration(800)
                            .EUt(VA[MV])
                            .save();

                    CHEMICAL_RECIPES.recipeBuilder("recycle_remnant_erosion_catalyst")
                            .inputItems(REGENERATED_REMNANT_ENERGY_ADSORBER)
                            .inputFluids(RemnantErosionActivate, 20)
                            .inputFluids(TheWaterFromTheWellOfWisdom, 30)
                            .outputItems(REMNANT_EROSION_CATALYST_EMBRYO)
                            .duration(200)
                            .EUt(VA[EV])
                            .save();
                }
            }
        }
    }
}
