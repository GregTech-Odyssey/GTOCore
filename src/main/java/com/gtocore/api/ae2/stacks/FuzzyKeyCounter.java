package com.gtocore.api.ae2.stacks;

import com.gtolib.api.ae2.IKeyCounter;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;

import java.util.Collection;
import java.util.Collections;

public final class FuzzyKeyCounter {

    public FuzzyKeyCounter(KeyCounter keyCounter) {
        var map = IKeyCounter.of(keyCounter).gtolib$getMap();
        if (map == null) return;
        lists = new Reference2ObjectOpenHashMap<>(map.size());
        map.object2LongEntrySet().fastForEach(entry -> {
            var value = entry.getLongValue();
            if (value == 0) return;
            var key = entry.getKey();
            if (key.getFuzzySearchMaxValue() > 0) {
                lists.computeIfAbsent(key.getPrimaryKey(), k -> new VariantCounter.FuzzyVariantMap()).add(key, value);
            } else {
                lists.computeIfAbsent(key.getPrimaryKey(), k -> new VariantCounter.UnorderedVariantMap()).add(key, value);
            }
        });
    }

    private Reference2ObjectOpenHashMap<Object, VariantCounter> lists;

    public Collection<Object2LongMap.Entry<AEKey>> findFuzzy(AEKey key, FuzzyMode fuzzy) {
        if (lists == null) return Collections.emptyList();
        var subIndex = lists.get(key.getPrimaryKey());
        if (subIndex != null) return subIndex.findFuzzy(key, fuzzy);
        return Collections.emptyList();
    }

    public void addAll(KeyCounter other) {
        var map = IKeyCounter.of(other).gtolib$getMap();
        if (map == null) return;
        if (lists == null) lists = new Reference2ObjectOpenHashMap<>(map.size());
        map.object2LongEntrySet().fastForEach(entry -> {
            var value = entry.getLongValue();
            if (value == 0) return;
            var key = entry.getKey();
            if (key.getFuzzySearchMaxValue() > 0) {
                lists.computeIfAbsent(key.getPrimaryKey(), k -> new VariantCounter.FuzzyVariantMap()).add(key, value);
            } else {
                lists.computeIfAbsent(key.getPrimaryKey(), k -> new VariantCounter.UnorderedVariantMap()).add(key, value);
            }
        });
    }

    public void clear() {
        for (var list : lists.values()) {
            list.clear();
        }
    }
}
