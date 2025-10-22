package com.gtocore.data.lootTables;

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
public class GTOLootTables implements LootTableSubProvider {

    // 战利品表ID
    public static final ResourceLocation ADVENTURER_BAG = GTOCore.id("reward_bags/adventurer");
    public static final ResourceLocation BASIC_RESOURCES = GTOCore.id("reward_bags/basic_resources");

    private static final ResourceLocation VANILLA_DUNGEON_REFERENCE = new ResourceLocation("minecraft", "chests/simple_dungeon");
    // 必要的战利品表集合：包含所有引用的外部表
    // 数据生成器会验证这些表是否存在，若不存在则报错
    // TODO 不知道为什么为什么无法引用其他模组包括 MC本体的战利品表
    private static final Set<ResourceLocation> REQUIRED_TABLES = Sets.newHashSet(
            BASIC_RESOURCES
    // VANILLA_DUNGEON_REFERENCE // 包含引用的外部表，避免生成时报错
    );

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        List<net.minecraft.data.loot.LootTableProvider.SubProviderEntry> subProviders = Lists.newArrayList(
                new net.minecraft.data.loot.LootTableProvider.SubProviderEntry(
                        GTOLootTables::new,
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
        registrar.accept(ADVENTURER_BAG, createAdventurerLootTable());
    }

    private LootTable.Builder createBasicResourcesTable() {
        LootPool.Builder pool = LootPool.lootPool()
                .setRolls(UniformGenerator.between(2, 4))
                .setBonusRolls(ConstantValue.exactly(0));

        pool.add(getLootItemWithRange(getItem("minecraft:stone"), 60, 3, 6));
        pool.add(getLootItemWithRange(getItem("minecraft:oak_planks"), 50, 2, 5));
        pool.add(getLootItemWithRange(getItem("minecraft:coal"), 45, 1, 4));
        pool.add(getLootItemWithRange(getItem("minecraft:iron_nugget"), 40, 2, 8));

        return LootTable.lootTable()
                .withPool(pool)
                .setRandomSequence(BASIC_RESOURCES)
                .setParamSet(LootContextParamSets.EMPTY);
    }

    // 冒险家用战利品袋表
    private LootTable.Builder createAdventurerLootTable() {
        LootPool.Builder basicPool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(getLootTableReference(BASIC_RESOURCES, 100));

        /// LootPool.Builder dungeonPool = LootPool.lootPool()
        /// .setRolls(ConstantValue.exactly(1))
        /// .add(getLootTableReference(VANILLA_DUNGEON_REFERENCE, 80));

        LootPool.Builder rarePool = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(getLootItem(getItem("minecraft:ender_pearl"), 30, 1))
                .add(getLootItem(getItem("minecraft:gold_nugget"), 20, 5))
                .add(getEnchantedLootItem(getItem("minecraft:iron_axe"), 25, 1));

        return LootTable.lootTable()
                .withPool(basicPool)
                // .withPool(dungeonPool)
                .withPool(rarePool)
                .setRandomSequence(ADVENTURER_BAG)
                .setParamSet(LootContextParamSets.EMPTY);
    }

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
