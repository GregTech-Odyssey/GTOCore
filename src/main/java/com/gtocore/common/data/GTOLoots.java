package com.gtocore.common.data;

import com.gtocore.data.lootTables.GTDynamicLoot;
import com.gtocore.data.lootTables.RewardBagLoot;

import com.gtolib.utils.RLUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;

import com.fast.fastcollection.O2OOpenCacheHashMap;
import com.fast.fastcollection.OpenCacheHashSet;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.mojang.datafixers.util.Pair;
import dev.shadowsoffire.placebo.loot.LootSystem;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public final class GTOLoots {

    private GTOLoots() {}

    public static boolean modifyLoot = true;

    public static Set<Object> BLOCKS = new OpenCacheHashSet<>();

    public static boolean cache;

    public static ImmutableMap<LootDataId<?>, ?> ELEMENTS_CACHE;

    public static ImmutableMultimap<LootDataType<?>, ResourceLocation> TYPEKEYS_CACHE;

    public static Predicate<ResourceLocation> getFilter() {
        ObjectOpenHashSet<ResourceLocation> set = new OpenCacheHashSet<>();
        removal(set);
        return set::contains;
    }

    private static void removal(Set<ResourceLocation> filters) {
        filters.add(RLUtils.fromNamespaceAndPath("expatternprovider", "blocks/ex_emc_interface"));
        filters.add(RLUtils.fromNamespaceAndPath("farmersrespite", "blocks/kettle"));
    }

    public static Pair<ImmutableMap<LootDataId<?>, ?>, ImmutableMultimap<LootDataType<?>, ResourceLocation>> apply(Map<LootDataType<?>, Map<ResourceLocation, ?>> collectedElements) {
        Map<ResourceLocation, LootTable> lootTables = (Map<ResourceLocation, LootTable>) collectedElements.get(LootDataType.TABLE);
        lootTables.putAll(DYNAMIC_LOOT);
        DYNAMIC_LOOT = null;
        ImmutableMap.Builder<LootDataId<?>, Object> builder = ImmutableMap.builder();
        ImmutableMultimap.Builder<LootDataType<?>, ResourceLocation> builder1 = ImmutableMultimap.builder();
        collectedElements.forEach((p_279449_, p_279262_) -> p_279262_.forEach((p_279130_, p_279313_) -> {
            builder.put(new LootDataId<>(p_279449_, p_279130_), p_279313_);
            builder1.put(p_279449_, p_279130_);
        }));
        LootSystem.PLACEBO_TABLES.forEach((key, val) -> {
            builder.put(key, val);
            builder1.put(LootDataType.TABLE, key.location());
        });
        LootSystem.PLACEBO_TABLES.clear();
        builder.put(LootDataManager.EMPTY_LOOT_TABLE_KEY, LootTable.EMPTY);
        return Pair.of(builder.build(), builder1.build());
    }

    private static Map<ResourceLocation, LootTable> DYNAMIC_LOOT = new O2OOpenCacheHashMap<>();

    public static void addToot(ResourceLocation resource, LootTable lootTable) {
        DYNAMIC_LOOT.put(resource, lootTable);
    }

    public static void init() {
        RewardBagLoot.init();
        GTDynamicLoot.init();
    }
}
