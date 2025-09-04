package com.gtocore.api.ae2.stacks;

import com.gtolib.utils.ExpandedO2LMap;

import appeng.api.stacks.AEKey;
import it.unimi.dsi.fastutil.objects.Object2LongAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;

import java.util.Comparator;
import java.util.Iterator;

interface AEKey2LongMap extends Object2LongMap<AEKey>, Iterable<Object2LongMap.Entry<AEKey>> {

    void ensureCapacity(int capacity);

    void reset();

    long addTo(AEKey k, long incr);

    final class OpenHashMap extends ExpandedO2LMap<AEKey> implements AEKey2LongMap {

        OpenHashMap() {
            super();
        }
    }

    final class AVLTreeMap extends Object2LongAVLTreeMap<AEKey> implements AEKey2LongMap {

        AVLTreeMap(Comparator<? super AEKey> c) {
            super(c);
        }

        @Override
        public Iterator<Entry<AEKey>> iterator() {
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
