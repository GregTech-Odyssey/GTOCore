package com.gtocore.api.ae2.stacks;

import com.gtolib.IUnique;
import com.gtolib.api.ae2.stacks.IKeyCounter;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;

import java.util.Collection;
import java.util.Collections;

public final class FuzzyKeyCounter {

    public FuzzyKeyCounter(KeyCounter keyCounter) {
        var map = IKeyCounter.of(keyCounter).gtolib$getMap();
        if (map == null) return;
        lists = new Int2ObjectOpenHashMap<>(map.size());
        map.reference2LongEntrySet().fastForEach(entry -> {
            var key = entry.getKey();
            if (key.getPrimaryKey() instanceof IUnique unique) {
                if (key.getFuzzySearchMaxValue() > 0) {
                    lists.computeIfAbsent(unique.getUid(), k -> new VariantCounter.FuzzyVariantMap()).add(key, entry.getLongValue());
                } else {
                    lists.computeIfAbsent(unique.getUid(), k -> new VariantCounter.UnorderedVariantMap()).add(key, entry.getLongValue());
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
        var map = IKeyCounter.of(other).gtolib$getMap();
        if (map == null) return;
        if (lists == null) lists = new Int2ObjectOpenHashMap<>(map.size());
        map.reference2LongEntrySet().fastForEach(entry -> {
            var key = entry.getKey();
            if (key.getPrimaryKey() instanceof IUnique unique) {
                if (key.getFuzzySearchMaxValue() > 0) {
                    lists.computeIfAbsent(unique.getUid(), k -> new VariantCounter.FuzzyVariantMap()).add(key, entry.getLongValue());
                } else {
                    lists.computeIfAbsent(unique.getUid(), k -> new VariantCounter.UnorderedVariantMap()).add(key, entry.getLongValue());
                }
            }
        });
    }

    public void clear() {
        if (lists == null) return;
        lists.values().forEach(VariantCounter::clear);
    }
}
