package com.gtocore.api.ae2.stacks;

import com.gtolib.api.misc.IUnique;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;

import java.util.Collection;
import java.util.Collections;

public final class FuzzyKeyCounter {

    public FuzzyKeyCounter(KeyCounter keyCounter) {
        var map = keyCounter.getMap();
        if (map.isEmpty()) return;
        lists = new Int2ObjectOpenHashMap<>(map.size());
        map.fastForEach((key, longValue) -> {
            if (key.getPrimaryKey() instanceof IUnique unique) {
                if (key.getFuzzySearchMaxValue() > 0) {
                    lists.computeIfAbsent(unique.getUid(), k -> new VariantCounter.FuzzyVariantMap()).add(key, longValue);
                } else {
                    lists.computeIfAbsent(unique.getUid(), k -> new VariantCounter.UnorderedVariantMap()).add(key, longValue);
                }
            }
        });
    }

    private Int2ObjectOpenHashMap<VariantCounter> lists;

    public Collection<Object2LongMap.Entry<AEKey>> findFuzzy(AEKey key, FuzzyMode fuzzy) {
        if (lists == null) return Collections.emptyList();
        if (key.getPrimaryKey() instanceof IUnique unique) {
            var subIndex = lists.get(unique.getUid());
            if (subIndex != null) return subIndex.findFuzzy(key, fuzzy);
        }
        return Collections.emptyList();
    }

    public void addAll(KeyCounter other) {
        var map = other.getMap();
        if (map.isEmpty()) return;
        if (lists == null) lists = new Int2ObjectOpenHashMap<>(map.size());
        map.fastForEach((key, longValue) -> {
            if (key.getPrimaryKey() instanceof IUnique unique) {
                if (key.getFuzzySearchMaxValue() > 0) {
                    lists.computeIfAbsent(unique.getUid(), k -> new VariantCounter.FuzzyVariantMap()).add(key, longValue);
                } else {
                    lists.computeIfAbsent(unique.getUid(), k -> new VariantCounter.UnorderedVariantMap()).add(key, longValue);
                }
            }
        });
    }

    public void clear() {
        if (lists == null) return;
        lists.values().forEach(VariantCounter::clear);
    }
}
