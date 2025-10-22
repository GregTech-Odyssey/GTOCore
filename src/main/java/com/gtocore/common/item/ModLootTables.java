package com.gtocore.common.item;

import com.gtolib.GTOCore;

import net.minecraft.data.loot.LootTableSubProvider;
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
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import static com.gtolib.utils.RegistriesUtils.getItem;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModLootTables implements LootTableSubProvider {

    // 战利品表ID
    public static final ResourceLocation VIER_REWARD_BAG = GTOCore.id("reward_bags/vier");
    public static final ResourceLocation NINE_AND_THREE_QUARTERS = GTOCore.id("reward_bags/nine_and_three_quarters");
    public static final ResourceLocation ADVENTURER_BAG = GTOCore.id("reward_bags/adventurer");
    public static final ResourceLocation BASIC_RESOURCES = GTOCore.id("common/basic_resources");
    // 替换为1.20.1中存在的vanilla战利品表（废弃矿井）
    public static final ResourceLocation VANILLA_DUNGEON_REFERENCE = new ResourceLocation("minecraft:chests/bastion_treasure");

    // 必要的战利品表集合
    private static final Set<ResourceLocation> REQUIRED_TABLES = Sets.newHashSet(BASIC_RESOURCES);

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        List<net.minecraft.data.loot.LootTableProvider.SubProviderEntry> subProviders = Lists.newArrayList(
                new net.minecraft.data.loot.LootTableProvider.SubProviderEntry(
                        ModLootTables::new,
                        LootContextParamSets.EMPTY));

        event.getGenerator().addProvider(
                event.includeServer(),
                new net.minecraft.data.loot.LootTableProvider(
                        event.getGenerator().getPackOutput(),
                        REQUIRED_TABLES,
                        subProviders));
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> registrar) {
        registrar.accept(BASIC_RESOURCES, createBasicResourcesTable());
        registrar.accept(VIER_REWARD_BAG, createVierLootTable());
        registrar.accept(NINE_AND_THREE_QUARTERS, createNineAndThreeQuartersLootTable());
        registrar.accept(ADVENTURER_BAG, createAdventurerLootTable());
    }

    /**
     * 修复：将minecraft:wooden_planks替换为实际存在的橡木木板
     */
    private LootTable.Builder createBasicResourcesTable() {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(UniformGenerator.between(2, 4))
                .setBonusRolls(ConstantValue.exactly(0));

        pool.add(getLootItemWithRange(getItem("minecraft:stone"), 60, 3, 6));
        // 替换为minecraft:oak_planks（橡木木板，1.20.1中存在）
        pool.add(getLootItemWithRange(getItem("minecraft:oak_planks"), 50, 2, 5));
        pool.add(getLootItemWithRange(getItem("minecraft:coal"), 45, 1, 4));
        pool.add(getLootItemWithRange(getItem("minecraft:iron_nugget"), 40, 2, 8));

        return LootTable.lootTable()
                .withPool(pool)
                .setRandomSequence(BASIC_RESOURCES)
                .setParamSet(LootContextParamSets.EMPTY);
    }

    private LootTable.Builder createVierLootTable() {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .setBonusRolls(ConstantValue.exactly(0));

        pool.add(getLootItem(getItem("minecraft:coal"), 40, 6));
        pool.add(getLootItem(getItem("minecraft:iron_ingot"), 36, 4));
        pool.add(getLootItem(getItem("minecraft:gold_ingot"), 24, 4));
        pool.add(getLootItem(getItem("minecraft:redstone"), 22, 8));
        pool.add(getLootItem(getItem("minecraft:ender_pearl"), 20, 4));
        pool.add(getLootItem(getItem("minecraft:diamond"), 18, 1));
        pool.add(getLootItem(getItem("botania:blacker_lotus"), 16, 2));
        pool.add(getLootItem(getItem("botania:overgrowth_seed"), 12, 1));
        pool.add(getLootItem(getItem("extrabotany:void_archives"), 1, 1));

        return LootTable.lootTable()
                .withPool(pool)
                .setRandomSequence(VIER_REWARD_BAG)
                .setParamSet(LootContextParamSets.EMPTY);
    }

    private LootTable.Builder createNineAndThreeQuartersLootTable() {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .setBonusRolls(ConstantValue.exactly(0));

        pool.add(getLootItem(getItem("extrabotany:hero_medal"), 1, 1));
        pool.add(getLootItem(getItem("extrabotany:eins_reward_bag"), 30, 16));
        pool.add(getLootItem(getItem("extrabotany:zwei_reward_bag"), 20, 10));
        pool.add(getLootItem(getItem("extrabotany:drei_reward_bag"), 10, 6));
        pool.add(getLootItem(getItem("extrabotany:vier_reward_bag"), 10, 6));
        pool.add(getLootItem(getItem("botania:gaia_ingot"), 14, 1));
        pool.add(getLootItem(getItem("botania:life_essence"), 20, 4));
        pool.add(getLootItem(getItem("extrabotany:challenge_ticket"), 45, 1));

        return LootTable.lootTable()
                .withPool(pool)
                .setRandomSequence(NINE_AND_THREE_QUARTERS)
                .setParamSet(LootContextParamSets.EMPTY);
    }

    /**
     * 修复：替换gtocore:adventurer_token为已存在物品，修正引用的vanilla表
     */
    private LootTable.Builder createAdventurerLootTable() {
        LootPool.Builder basicPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(getLootTableReference(BASIC_RESOURCES, 100));

        // 引用修正后的vanilla战利品表（废弃矿井）
        LootPool.Builder dungeonPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(getLootTableReference(VANILLA_DUNGEON_REFERENCE, 80));

        LootPool.Builder rarePool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(getLootItem(getItem("minecraft:ender_pearl"), 30, 1))
                // 替换未注册的adventurer_token为minecraft:gold_nugget（临时解决方案）
                .add(getLootItem(getItem("minecraft:gold_nugget"), 20, 5))
                .add(getEnchantedLootItem(getItem("minecraft:iron_axe"), 10, 2))
                .add(getEnchantedLootItem(getItem("minecraft:iron_axe"), 15, 1));

        return LootTable.lootTable()
                .withPool(basicPool)
                .withPool(dungeonPool)
                .withPool(rarePool)
                .setRandomSequence(ADVENTURER_BAG)
                .setParamSet(LootContextParamSets.EMPTY);
    }

    // 工具方法（保持不变）
    private LootPoolEntryContainer.Builder<?> getLootItem(Item item, int weight, int count) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)));
    }

    private LootPoolEntryContainer.Builder<?> getLootItemWithRange(Item item, int weight, int minCount, int maxCount) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(minCount, maxCount)));
    }

    private LootPoolEntryContainer.Builder<?> getEnchantedLootItem(Item item, int weight, int count) {
        return LootItem.lootTableItem(item)
                .setWeight(weight)
                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)))
                .apply(EnchantRandomlyFunction.randomEnchantment());
    }

    private LootPoolEntryContainer.Builder<?> getLootTableReference(ResourceLocation tableId, int weight) {
        return LootTableReference.lootTableReference(tableId)
                .setWeight(weight);
    }
}
