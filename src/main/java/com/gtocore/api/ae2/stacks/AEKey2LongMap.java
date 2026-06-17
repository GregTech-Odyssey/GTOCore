package com.gtocore.api.ae2.stacks;

import appeng.api.stacks.AEKey;

import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.objects.Object2LongAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Iterator;

interface AEKey2LongMap extends Object2LongMap<AEKey>, Iterable<Object2LongMap.Entry<AEKey>> {

    void ensureCapacity(int capacity);

    void reset();

    long addTo(AEKey k, long incr);

    final class OpenHashMap extends Object2LongOpenHashMap<AEKey> implements AEKey2LongMap {

        OpenHashMap() {
            super();
        }

        @Override
        public void ensureCapacity(int capacity) {
            int needed = (int) Math.min(1073741824L, Math.max(2L, HashCommon.nextPowerOfTwo((long) Math.ceil((float) (capacity + size) / this.f))));
            if (needed > this.n) {
                this.rehash(needed);
            }
        }

        @Override
        public void reset() {
            for (int i = 0, len = value.length; i < len; i++) value[i] = 0;
        }

        @Override
        public @NotNull Iterator<Entry<AEKey>> iterator() {
            return object2LongEntrySet().fastIterator();
        }
    }

    final class AVLTreeMap extends Object2LongAVLTreeMap<AEKey> implements AEKey2LongMap {

        AVLTreeMap(Comparator<? super AEKey> c) {
            super(c);
        }

        @Override
        public @NotNull Iterator<Entry<AEKey>> iterator() {
            return object2LongEntrySet().iterator();
        }

        @Override
        public void ensureCapacity(int capacity) {}

        @Override
        public void reset() {
            for (var e : this) {
                e.setValue(0);
            }
        }
    }
}
