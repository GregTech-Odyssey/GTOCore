package com.gtocore.data.recipe.magic;

import com.gtocore.common.data.GTOItems;
import com.gtocore.common.data.GTOMaterials;
import com.gtocore.common.data.GTORecipeCategories;

import net.minecraft.world.item.Item;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.*;
import static com.gtocore.data.record.ApotheosisAffix.getAffixSize;
import static com.gtocore.data.record.Enchantment.getEnchantmentSize;

public class MagicRecipesB {

    public static void init() {
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
                    .outputItems(GTOItems.AFFIX_CANVAS.asStack(16))
                    .inputFluids(GTOMaterials.Animium, 1000)
                    .duration(20)
                    .MANAt(1024)
                    .save();

        }

        // 精粹回收
        {
            for (int i = 1; i < getEnchantmentSize(); i++) {
                CHEMICAL_BATH_RECIPES.builder("enchantment_essence_recovery_" + i)
                        .inputItems(GTOItems.ENCHANTMENT_ESSENCE[i])
                        .inputFluids(TheWaterFromTheWellOfWisdom.getFluid(5))
                        .outputItems(GTOItems.ENCHANTMENT_ESSENCE[0])
                        .category(GTORecipeCategories.ESSENCE_RECOVERY)
                        .duration(20)
                        .EUt(8)
                        .save();
            }

            for (int i = 1; i < getAffixSize(); i++) {
                CHEMICAL_BATH_RECIPES.builder("affix_essence_recovery_" + i)
                        .inputItems(GTOItems.AFFIX_ESSENCE[i])
                        .inputFluids(TheWaterFromTheWellOfWisdom.getFluid(5))
                        .outputItems(GTOItems.AFFIX_ESSENCE[0])
                        .category(GTORecipeCategories.ESSENCE_RECOVERY)
                        .duration(20)
                        .EUt(8)
                        .save();
            }
        }
    }
}
