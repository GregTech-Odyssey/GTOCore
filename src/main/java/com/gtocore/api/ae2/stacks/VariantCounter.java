package com.gtocore.api.ae2.stacks;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.AEKey;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongSortedMap;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedMap;

abstract class VariantCounter implements Iterable<Object2LongMap.Entry<AEKey>> {

    abstract int size();

    abstract boolean isEmpty();

    abstract long get(AEKey key);

    abstract long remove(AEKey key);

    abstract Collection<Object2LongMap.Entry<AEKey>> findFuzzy(AEKey filter, FuzzyMode fuzzy);

    abstract AEKey2LongMap getRecords();

    abstract void reset();

    abstract void clear();

    abstract void removeZeros();

    abstract VariantCounter copy();

    void add(AEKey key, long amount) {
        this.getRecords().addTo(key, amount);
    }

    void set(AEKey key, long amount) {
        getRecords().put(key, amount);
    }

    void addAll(VariantCounter other) {
        var size = other.size();
        if (size == 0) return;
        ensureCapacity(size);
        for (Object2LongMap.Entry<AEKey> entry : other) {
            add(entry.getKey(), entry.getLongValue());
        }
    }

    void removeAll(VariantCounter other) {
        var size = other.size();
        if (size == 0) return;
        ensureCapacity(size);
        for (Object2LongMap.Entry<AEKey> entry : other) {
            add(entry.getKey(), -entry.getLongValue());
        }
    }

    void invert() {
        for (Object2LongMap.Entry<AEKey> entry : this) {
            entry.setValue(-entry.getLongValue());
        }
    }

    void ensureCapacity(int capacity) {
        getRecords().ensureCapacity(capacity);
    }

    static class UnorderedVariantMap extends VariantCounter {

        private final AEKey2LongMap.OpenHashMap records = new AEKey2LongMap.OpenHashMap();

        @Override
        int size() {
            return records.size();
        }

        @Override
        boolean isEmpty() {
            return records.isEmpty();
        }

        @Override
        long get(AEKey key) {
            return records.getLong(key);
        }

        @Override
        long remove(AEKey key) {
            return records.removeLong(key);
        }

        @Override
        Collection<Object2LongMap.Entry<AEKey>> findFuzzy(AEKey filter, FuzzyMode fuzzy) {
            return records.object2LongEntrySet();
        }

        @Override
        AEKey2LongMap getRecords() {
            return records;
        }

        @Override
        void reset() {
            records.reset();
        }

        @Override
        void clear() {
            records.clear();
        }

        @Override
        void removeZeros() {
            for (Iterator<Object2LongMap.Entry<AEKey>> it = records.iterator(); it.hasNext();) {
                var entry = it.next();
                if (entry.getLongValue() == 0) it.remove();
            }
        }

        @Override
        VariantCounter copy() {
            var result = new UnorderedVariantMap();
            result.records.putAll(records);
            return result;
        }

        @Override
        public @NotNull Iterator<Object2LongMap.Entry<AEKey>> iterator() {
            return records.iterator();
        }
    }

    static class FuzzyVariantMap extends VariantCounter {

        private static final KeyComparator COMPARATOR = new KeyComparator();

        private final AEKey2LongMap.AVLTreeMap records = new AEKey2LongMap.AVLTreeMap(COMPARATOR);

        @Override
        int size() {
            return records.size();
        }

        @Override
        boolean isEmpty() {
            return records.isEmpty();
        }

        @Override
        long get(AEKey key) {
            return records.getLong(key);
        }

        @Override
        long remove(AEKey key) {
            return records.removeLong(key);
        }

        @Override
        Collection<Object2LongMap.Entry<AEKey>> findFuzzy(AEKey key, FuzzyMode fuzzy) {
            return findFuzzy((Object2LongSortedMap<AEKey>) records, key, fuzzy).object2LongEntrySet();
        }

        @Override
        AEKey2LongMap getRecords() {
            return this.records;
        }

        @Override
        void reset() {
            records.reset();
        }

        @Override
        void clear() {
            records.clear();
        }

        @Override
        void removeZeros() {
            for (Iterator<Object2LongMap.Entry<AEKey>> it = records.iterator(); it.hasNext();) {
                var entry = it.next();
                if (entry.getLongValue() == 0) it.remove();
            }
        }

        @Override
        VariantCounter copy() {
            var result = new FuzzyVariantMap();
            result.records.putAll(records);
            return result;
        }

        @Override
        public @NotNull Iterator<Object2LongMap.Entry<AEKey>> iterator() {
            return records.iterator();
        }

        @SuppressWarnings({ "unchecked" })
        private static <T extends SortedMap<K, V>, K, V> T findFuzzy(T map, AEKey key, FuzzyMode fuzzy) {
            return (T) map.subMap((K) makeLowerBound(key, fuzzy), (K) makeUpperBound(key, fuzzy));
        }

        private static class KeyComparator implements Comparator<Object> {

            @Override
            public int compare(Object a, Object b) {
                Integer boundA = null;
                AEKey stackA = null;
                int fuzzyOrderB;
                if (a instanceof Integer) {
                    boundA = (Integer) a;
                    fuzzyOrderB = boundA;
                } else {
                    stackA = (AEKey) a;
                    fuzzyOrderB = stackA.getFuzzySearchValue();
                }
                Integer boundB = null;
                AEKey stackB = null;
                int fuzzyOrderA;
                if (b instanceof Integer) {
                    boundB = (Integer) b;
                    fuzzyOrderA = boundB;
                } else {
                    stackB = (AEKey) b;
                    fuzzyOrderA = stackB.getFuzzySearchValue();
                }

                if (boundA != null || boundB != null) {
                    return Integer.compare(fuzzyOrderA, fuzzyOrderB);
                }

                if (stackA.equals(stackB)) {
                    return 0;
                }

                final var fuzzyOrder = Integer.compare(fuzzyOrderA, fuzzyOrderB);
                if (fuzzyOrder != 0) {
                    return fuzzyOrder;
                }

                return Integer.compare(stackA.hashCode(), stackB.hashCode());
            }
        }

        private static final int MIN_DAMAGE_VALUE = -1;

        private static Integer makeLowerBound(AEKey key, FuzzyMode fuzzy) {
            var maxValue = key.getFuzzySearchMaxValue();

            int damage;
            if (fuzzy == FuzzyMode.IGNORE_ALL) {
                damage = maxValue;
            } else {
                var breakpoint = fuzzy.calculateBreakPoint(maxValue);
                damage = key.getFuzzySearchValue() <= breakpoint ? breakpoint : maxValue;
            }

            return damage;
        }

        private static Integer makeUpperBound(AEKey key, FuzzyMode fuzzy) {
            int damage;
            if (fuzzy == FuzzyMode.IGNORE_ALL) {
                damage = MIN_DAMAGE_VALUE;
            } else {
                final var breakpoint = fuzzy.calculateBreakPoint(key.getFuzzySearchMaxValue());
                damage = key.getFuzzySearchValue() <= breakpoint ? MIN_DAMAGE_VALUE : breakpoint;
            }

            return damage;
        }
    }
}
