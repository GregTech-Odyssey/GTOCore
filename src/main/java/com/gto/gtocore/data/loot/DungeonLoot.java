package com.gto.gtocore.data.loot;

import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.data.loot.ChestGenHooks;

import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public interface DungeonLoot {

    static void init() {
        ChestGenHooks.addItem(BuiltInLootTables.VILLAGE_TOOLSMITH, GTItems.FLUID_CELL_GLASS_VIAL.asStack(), 1, 2, 1);
        ChestGenHooks.addItem(BuiltInLootTables.VILLAGE_TOOLSMITH, GTItems.TOOL_LIGHTER_INVAR.asStack(), 1, 1, 4);
        ChestGenHooks.addItem(BuiltInLootTables.VILLAGE_TOOLSMITH, GTBlocks.BRONZE_BRICKS_HULL.asStack(), 1, 4, 2);
        ChestGenHooks.addItem(BuiltInLootTables.VILLAGE_MASON, GTBlocks.STEEL_BRICKS_HULL.asStack(), 1, 2, 2);
    }
}
