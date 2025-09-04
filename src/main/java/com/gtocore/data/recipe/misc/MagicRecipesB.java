package com.gtocore.data.recipe.misc;

import net.minecraft.world.item.Item;

import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import static com.gtocore.common.data.GTOMaterials.*;
import static com.gtocore.common.data.GTORecipeTypes.RUNE_ENGRAVING_RECIPES;

public class MagicRecipesB {

    public static void init() {
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
}
