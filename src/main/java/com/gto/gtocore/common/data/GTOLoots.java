package com.gto.gtocore.common.data;

import com.gregtechceu.gtceu.core.MixinHelpers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.mojang.datafixers.util.Pair;
import dev.shadowsoffire.placebo.loot.LootSystem;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

import java.util.Map;
import java.util.Set;

public final class GTOLoots {

    private GTOLoots() {}

    public static Set<Object> BLOCKS = new ObjectOpenHashSet<>();

    public static boolean cache;

    public static ImmutableMap<LootDataId<?>, ?> ELEMENTS_CACHE;

    public static ImmutableMultimap<LootDataType<?>, ResourceLocation> TYPEKEYS_CACHE;

    public static void removal(Set<ResourceLocation> filters) {
        filters.add(new ResourceLocation("expatternprovider", "blocks/ex_emc_interface"));
        filters.add(new ResourceLocation("farmersrespite", "blocks/kettle"));
    }

    public static Pair<ImmutableMap<LootDataId<?>, ?>, ImmutableMultimap<LootDataType<?>, ResourceLocation>> apply(Map<LootDataType<?>, Map<ResourceLocation, ?>> collectedElements) {
        Map<ResourceLocation, LootTable> lootTables = (Map<ResourceLocation, LootTable>) collectedElements.get(LootDataType.TABLE);
        MixinHelpers.generateGTDynamicLoot(lootTables);
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
}
