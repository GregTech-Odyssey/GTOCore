package com.gtocore.data.lootTables.GTOLootTool;

import com.gtolib.utils.RegistriesUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.storage.loot.functions.SetItemCountFunction.setCount;

public class LootRegistrationTool {

    /**
     * 创建设定数量的物品战利品条目
     */
    public static @NotNull LootItem.Builder<?> getLootItem(Item item, int weight, NumberProvider countValue) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .apply(setCount(countValue));
    }

    public static @NotNull LootItem.Builder<?> getLootItem(String item, int weight, NumberProvider countValue) {
        return LootItem.lootTableItem(RegistriesUtils.getItem(item))
                .setWeight(weight)
                .apply(setCount(countValue));
    }

    /**
     * 创建带随机附魔的物品战利品条目
     */
    public static @NotNull LootItem.Builder<?> getEnchantedLootItem(Item item, int weight, NumberProvider countValue) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .apply(setCount(countValue))
                .apply(EnchantRandomlyFunction.randomEnchantment());
    }

    /**
     * 创建引用其他战利品表的条目
     */
    public static @NotNull LootTableReference.Builder<?> getLootTableReference(ResourceLocation tableId, int weight) {
        return LootTableReference.lootTableReference(tableId)
                .setWeight(weight);
    }
}
