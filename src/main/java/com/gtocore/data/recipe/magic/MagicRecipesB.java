package com.gtocore.data.recipe.magic;

import com.gtocore.common.data.*;
import com.gtocore.common.data.machines.ManaMachine;

import com.gtolib.GTOCore;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.gregtechceu.gtceu.data.recipe.VanillaRecipeHelper;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import static com.gregtechceu.gtceu.api.GTValues.VN;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.dust;
import static com.gregtechceu.gtceu.api.data.tag.TagPrefix.ingot;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Ethanol;
import static com.gregtechceu.gtceu.common.data.GTMaterials.Silver;
import static com.gtocore.common.data.GTOItems.*;
import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;

public class MagicRecipesB {

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
            ASSEMBLER_RECIPES.builder("affix_canvas")
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
    }
}
