package com.gtocore.data.lootTables;

import com.gtocore.data.lootTables.GTOLootItemFunction.CustomLogicFunction;

import com.gtolib.GTOCore;
import com.gtolib.utils.RegistriesUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import static com.gtocore.common.data.GTOLoots.addToot;
import static com.gtocore.data.lootTables.GTOLootItemFunction.lootRegistrationTool.*;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public final class RewardBagLoot {

    // 引用的
    private static final ResourceLocation VANILLA_DUNGEON_REFERENCE = fromNamespaceAndPath("minecraft", "chests/simple_dungeon");

    // 注册的
    public static final ResourceLocation[] ADVENTURER_BAG_LOOT = { GTOCore.id("reward_bags/adventurer"), GTOCore.id("reward_bags/adventurer"), GTOCore.id("reward_bags/adventurer") };
    public static final ResourceLocation BASIC_RESOURCES_LOOT = GTOCore.id("reward_bags/basic_resources");

    public static void init() {
        addToot(BASIC_RESOURCES_LOOT, createBasicResourcesTable());
        addToot(ADVENTURER_BAG_LOOT[0], createAdventurerLootTable());
    }

    // 构建基础资源表
    private static LootTable createBasicResourcesTable() {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(UniformGenerator.between(2, 4))
                .setBonusRolls(ConstantValue.exactly(0))
                .add(getLootItemWithRange(RegistriesUtils.getItem("minecraft:stone"), 60, 3, 6))
                .add(getLootItemWithRange(RegistriesUtils.getItem("minecraft:oak_planks"), 50, 2, 5))
                .add(getLootItemWithRange(RegistriesUtils.getItem("minecraft:coal"), 45, 1, 4))
                .add(getLootItemWithRange(RegistriesUtils.getItem("minecraft:iron_nugget"), 40, 2, 8));

        return LootTable.lootTable()
                .withPool(pool)
                .setParamSet(LootContextParamSets.EMPTY)
                .build();
    }

    // 构建冒险家袋表
    private static LootTable createAdventurerLootTable() {
        LootPool.Builder basicPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(5))
                .add(getLootTableReference(BASIC_RESOURCES_LOOT, 100));

        LootPool.Builder dungeonPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(getLootTableReference(VANILLA_DUNGEON_REFERENCE, 80).apply(new CustomLogicFunction.Builder((SPAWN_CHEST_LOGIC))));

        LootPool.Builder rarePool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(getLootItem(RegistriesUtils.getItem("minecraft:ender_pearl"), 30, 1))
                .add(getLootItem(RegistriesUtils.getItem("minecraft:gold_nugget"), 20, 5))
                .add(getEnchantedLootItem(RegistriesUtils.getItem("minecraft:iron_axe"), 25, 1));

        return LootTable.lootTable()
                .withPool(basicPool)
                .withPool(dungeonPool)
                .withPool(rarePool)
                .setParamSet(LootContextParamSets.EMPTY)
                .build();
    }

    public static final ResourceLocation[] LV_REWARD_BAG_LOOT = {
            GTOCore.id("reward_bags/lv_reward_bag_loot_1"),
            GTOCore.id("reward_bags/lv_reward_bag_loot_2"),
            GTOCore.id("reward_bags/lv_reward_bag_loot_3") };

    private static LootTable create_LV_REWARD_BAG_LOOT() {
        LootPool.Builder foodPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(5))
                .add(getLootTableReference(BASIC_RESOURCES_LOOT, 100));

        LootPool.Builder ingotPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(5))
                .add(getLootTableReference(BASIC_RESOURCES_LOOT, 100));

        LootPool.Builder toolPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(5))
                .add(getLootTableReference(BASIC_RESOURCES_LOOT, 100));

        LootPool.Builder blocksPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(5))
                .add(getLootTableReference(BASIC_RESOURCES_LOOT, 100));

        LootPool.Builder partsPoolA = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(5))
                .add(getLootTableReference(BASIC_RESOURCES_LOOT, 100));

        LootPool.Builder partsPoolB = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(5))
                .add(getLootTableReference(BASIC_RESOURCES_LOOT, 100));

        LootPool.Builder basicPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(5))
                .add(getLootTableReference(BASIC_RESOURCES_LOOT, 100));

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
                .setParamSet(LootContextParamSets.EMPTY)
                .build();
    }
}
