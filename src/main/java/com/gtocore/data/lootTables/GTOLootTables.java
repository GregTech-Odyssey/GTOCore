package com.gtocore.data.lootTables;

import com.gtolib.GTOCore;
import com.gtolib.api.annotation.DataGeneratorScanned;
import com.gtolib.utils.RegistriesUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

@DataGeneratorScanned
public final class GTOLootTables {

    public static final ResourceLocation ADVENTURER_BAG = GTOCore.id("reward_bags/adventurer");
    public static final ResourceLocation BASIC_RESOURCES = GTOCore.id("reward_bags/basic_resources");
    private static final ResourceLocation VANILLA_DUNGEON_REFERENCE = new ResourceLocation("minecraft", "chests/simple_dungeon");

    private static final Map<ResourceLocation, LootTable> CUSTOM_LOOT_TABLES = new HashMap<>();

    static {
        CUSTOM_LOOT_TABLES.put(BASIC_RESOURCES, createBasicResourcesTable());
        CUSTOM_LOOT_TABLES.put(ADVENTURER_BAG, createAdventurerLootTable());
    }

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        ResourceLocation tableId = event.getName();
        if (CUSTOM_LOOT_TABLES.containsKey(tableId)) {
            event.setTable(CUSTOM_LOOT_TABLES.get(tableId));
            GTOCore.LOGGER.info("注入自定义战利品表: {}", tableId);
        }
    }

    // 构建基础资源表
    private static LootTable createBasicResourcesTable() {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(UniformGenerator.between(2, 4))
                .setBonusRolls(ConstantValue.exactly(0));

        pool.add(getLootItemWithRange(RegistriesUtils.getItem("minecraft:stone"), 60, 3, 6));
        pool.add(getLootItemWithRange(RegistriesUtils.getItem("minecraft:oak_planks"), 50, 2, 5));
        pool.add(getLootItemWithRange(RegistriesUtils.getItem("minecraft:coal"), 45, 1, 4));
        pool.add(getLootItemWithRange(RegistriesUtils.getItem("minecraft:iron_nugget"), 40, 2, 8));

        return LootTable.lootTable()
                .withPool(pool)
                .setRandomSequence(BASIC_RESOURCES)
                .setParamSet(LootContextParamSets.EMPTY)
                .build();
    }

    // 构建冒险家袋表
    private static LootTable createAdventurerLootTable() {
        LootPool.Builder basicPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(5))
                .add(getLootTableReference(BASIC_RESOURCES, 100));

        LootPool.Builder dungeonPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(getLootTableReference(VANILLA_DUNGEON_REFERENCE, 80));

        LootPool.Builder rarePool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(getLootItem(RegistriesUtils.getItem("minecraft:ender_pearl"), 30, 1))
                .add(getLootItem(RegistriesUtils.getItem("minecraft:gold_nugget"), 20, 5))
                .add(getEnchantedLootItem(RegistriesUtils.getItem("minecraft:iron_axe"), 25, 1));

        return LootTable.lootTable()
                .withPool(basicPool)
                .withPool(dungeonPool)
                .withPool(rarePool)
                .setRandomSequence(ADVENTURER_BAG)
                .setParamSet(LootContextParamSets.EMPTY)
                .build();
    }

    // 工具方法
    private static LootPoolEntryContainer.Builder<?> getLootItem(Item item, int weight, int count) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)));
    }

    private static LootPoolEntryContainer.Builder<?> getLootItemWithRange(Item item, int weight, int minCount, int maxCount) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount)));
    }

    private static LootPoolEntryContainer.Builder<?> getEnchantedLootItem(Item item, int weight, int count) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)))
                .apply(EnchantRandomlyFunction.randomEnchantment());
    }

    private static LootPoolEntryContainer.Builder<?> getLootTableReference(ResourceLocation tableId, int weight) {
        return LootTableReference.lootTableReference(tableId)
                .setWeight(weight);
    }
}
