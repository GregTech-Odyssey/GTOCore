package com.gtocore.data.loot;

import com.gtocore.api.data.tag.GTOTagPrefix;
import com.gtocore.common.data.GTOItems;

import com.gtolib.api.annotation.Register;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.data.chemical.ChemicalHelper;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.common.data.GTMaterials;
import com.gregtechceu.gtceu.data.loot.ChestGenHooks;

import net.minecraft.world.level.storage.loot.BuiltInLootTables;

@Register
public final class DungeonLoot {

    public static void init() {
        ChestGenHooks.addItem(BuiltInLootTables.VILLAGE_TOOLSMITH, GTItems.FLUID_CELL_GLASS_VIAL.asStack(), 1, 2, 1);
        ChestGenHooks.addItem(BuiltInLootTables.VILLAGE_TOOLSMITH, GTItems.TOOL_LIGHTER_INVAR.asStack(), 1, 1, 4);
        ChestGenHooks.addItem(BuiltInLootTables.VILLAGE_TOOLSMITH, GTBlocks.BRONZE_BRICKS_HULL.asStack(), 1, 4, 2);
        ChestGenHooks.addItem(BuiltInLootTables.VILLAGE_MASON, GTBlocks.STEEL_BRICKS_HULL.asStack(), 1, 2, 2);
        ChestGenHooks.addItem(BuiltInLootTables.SIMPLE_DUNGEON, GTOItems.MYSTERIOUS_BOOST_DRINK[GTValues.ULV].asStack(), 1, 4, 5);
        ChestGenHooks.addItem(BuiltInLootTables.SIMPLE_DUNGEON, GTOItems.MYSTERIOUS_BOOST_DRINK[GTValues.LV].asStack(), 1, 3, 4);
        ChestGenHooks.addItem(BuiltInLootTables.SIMPLE_DUNGEON, GTOItems.MYSTERIOUS_BOOST_DRINK[GTValues.MV].asStack(), 1, 2, 3);
        ChestGenHooks.addItem(BuiltInLootTables.SIMPLE_DUNGEON, GTOItems.RUNE1_REWARD_BAG.asStack(), 1, 1, 3);

        ChestGenHooks.addItem(BuiltInLootTables.SIMPLE_DUNGEON, GTOItems.RANDOM_POSITIVE_FOOD_1.asStack(), 1, 2, 6);
        ChestGenHooks.addItem(BuiltInLootTables.SIMPLE_DUNGEON, GTOItems.RANDOM_POSITIVE_FOOD_2.asStack(), 1, 2, 5);
        ChestGenHooks.addItem(BuiltInLootTables.SIMPLE_DUNGEON, GTOItems.RANDOM_POSITIVE_FOOD_3.asStack(), 1, 1, 4);
        ChestGenHooks.addItem(BuiltInLootTables.SIMPLE_DUNGEON, GTOItems.RANDOM_POSITIVE_FOOD_4.asStack(), 1, 1, 3);
        ChestGenHooks.addItem(BuiltInLootTables.SIMPLE_DUNGEON, GTOItems.CHOPPER_POPPER_FISH_HEAD_1.asStack(), 1, 1, 3);
        ChestGenHooks.addItem(BuiltInLootTables.SIMPLE_DUNGEON, GTOItems.CHOPPER_POPPER_FISH_HEAD_2.asStack(), 1, 1, 3);
        ChestGenHooks.addItem(BuiltInLootTables.SIMPLE_DUNGEON, GTOItems.CHOPPER_POPPER_FISH_HEAD_3.asStack(), 1, 1, 3);
        ChestGenHooks.addItem(BuiltInLootTables.SIMPLE_DUNGEON, GTOItems.CHOPPER_POPPER_FISH_HEAD_4.asStack(), 1, 1, 3);
        ChestGenHooks.addItem(BuiltInLootTables.SIMPLE_DUNGEON, GTOItems.CHOPPER_POPPER_FISH_HEAD_5.asStack(), 1, 1, 3);

        ChestGenHooks.addItem(BuiltInLootTables.SIMPLE_DUNGEON, ChemicalHelper.get(GTOTagPrefix.COIN, GTMaterials.Copper), 1, 12, 6);
        ChestGenHooks.addItem(BuiltInLootTables.IGLOO_CHEST, ChemicalHelper.get(GTOTagPrefix.COIN, GTMaterials.Copper), 1, 8, 3);
        ChestGenHooks.addItem(BuiltInLootTables.SHIPWRECK_TREASURE, ChemicalHelper.get(GTOTagPrefix.COIN, GTMaterials.Copper), 1, 8, 3);
        ChestGenHooks.addItem(BuiltInLootTables.BURIED_TREASURE, ChemicalHelper.get(GTOTagPrefix.COIN, GTMaterials.Copper), 4, 24, 30);

        ChestGenHooks.addItem(BuiltInLootTables.JUNGLE_TEMPLE, GTOItems.MYSTERIOUS_BOOST_DRINK[GTValues.ULV].asStack(), 1, 4, 4);
        ChestGenHooks.addItem(BuiltInLootTables.JUNGLE_TEMPLE, GTOItems.MYSTERIOUS_BOOST_DRINK[GTValues.LV].asStack(), 1, 3, 3);
        ChestGenHooks.addItem(BuiltInLootTables.JUNGLE_TEMPLE, GTOItems.MYSTERIOUS_BOOST_DRINK[GTValues.MV].asStack(), 1, 2, 2);
    }
}
