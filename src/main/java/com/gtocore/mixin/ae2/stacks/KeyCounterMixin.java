package com.gtocore.mixin.ae2.stacks;

import com.gtolib.IUnique;
import com.gtolib.api.ae2.stacks.IKeyCounter;
import com.gtolib.utils.ExpandedO2LMap;
import com.gtolib.utils.ExpandedR2LMap;

import appeng.api.config.FuzzyMode;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

@Mixin(KeyCounter.class)
public class KeyCounterMixin implements IKeyCounter {

    @Unique
    private ExpandedR2LMap<AEKey> gtolib$map;

    @Unique
    private Int2ObjectOpenHashMap<ExpandedO2LMap<AEKey>> gtolib$fuzzyMap;

    @Unique
    private boolean gtolib$fuzzyUpdate;

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public Collection<Object2LongMap.Entry<AEKey>> findFuzzy(AEKey key, FuzzyMode fuzzy) {
        if (gtolib$map == null) return Collections.emptyList();
        if (key.getPrimaryKey() instanceof IUnique unique) {
            if (gtolib$fuzzyUpdate || gtolib$fuzzyMap == null) {
                gtolib$fuzzyUpdate = false;
                if (gtolib$fuzzyMap == null) {
                    gtolib$fuzzyMap = new Int2ObjectOpenHashMap<>();
                } else {
                    gtolib$fuzzyMap.values().forEach(Object2LongOpenHashMap::clear);
                }
                gtolib$map.reference2LongEntrySet().fastForEach(e -> {
                    var k = e.getKey();
                    if (k.getPrimaryKey() instanceof IUnique u) {
                        gtolib$fuzzyMap.computeIfAbsent(u.getUid(), _k -> new ExpandedO2LMap<>()).addTo(k, e.getLongValue());
                    }
                });
            }
            var map = gtolib$fuzzyMap.get(unique.getUid());
            if (map != null) return map.object2LongEntrySet();
        }
        long value = gtolib$map.getOrDefault(key, Long.MIN_VALUE);
        if (value > Long.MIN_VALUE) return Collections.singleton(new Entry(value, key));
        return Collections.emptyList();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void removeZeros() {
        if (gtolib$map == null) return;
        var it = gtolib$map.iterator();
        while (it.hasNext()) {
            if (it.next().getLongValue() == 0) it.remove();
        }
        gtolib$fuzzyUpdate = true;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void removeEmptySubmaps() {}

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void addAll(KeyCounter other) {
        var l = IKeyCounter.of(other).gtolib$getMap();
        if (l == null) return;
        var size = l.size();
        if (size < 1) return;
        if (gtolib$map == null) {
            gtolib$map = new ExpandedR2LMap<>(l.size());
            l.reference2LongEntrySet().fastForEach(entry -> gtolib$map.addTo(entry.getKey(), entry.getLongValue()));
        } else {
            gtolib$map.ensureCapacity(size);
            l.reference2LongEntrySet().fastForEach(entry -> gtolib$map.addTo(entry.getKey(), entry.getLongValue()));
        }
        gtolib$fuzzyUpdate = true;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void removeAll(KeyCounter other) {
        var l = IKeyCounter.of(other).gtolib$getMap();
        if (l == null) return;
        var size = l.size();
        if (size < 1) return;
        if (gtolib$map == null) {
            gtolib$map = new ExpandedR2LMap<>(size);
        } else {
            gtolib$map.ensureCapacity(size);
        }
        l.reference2LongEntrySet().fastForEach(entry -> gtolib$map.addTo(entry.getKey(), -entry.getLongValue()));
        gtolib$fuzzyUpdate = true;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void add(AEKey key, long amount) {
        if (gtolib$map == null) gtolib$map = new ExpandedR2LMap<>();
        gtolib$map.addTo(key, amount);
        gtolib$fuzzyUpdate = true;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void remove(AEKey key, long amount) {
        if (gtolib$map == null) gtolib$map = new ExpandedR2LMap<>();
        gtolib$map.addTo(key, -amount);
        gtolib$fuzzyUpdate = true;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public long remove(AEKey key) {
        if (gtolib$map == null) return 0;
        gtolib$fuzzyUpdate = true;
        return gtolib$map.removeLong(key);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void set(AEKey key, long amount) {
        if (gtolib$map == null) gtolib$map = new ExpandedR2LMap<>();
        gtolib$map.put(key, amount);
        gtolib$fuzzyUpdate = true;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public long get(AEKey key) {
        if (gtolib$map == null) return 0;
        return gtolib$map.getLong(key);
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void reset() {
        if (gtolib$map == null) return;
        gtolib$map.reset();
        gtolib$fuzzyUpdate = true;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public void clear() {
        if (gtolib$map == null) return;
        gtolib$map.clear();
        gtolib$fuzzyUpdate = true;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public boolean isEmpty() {
        if (gtolib$map == null) return true;
        return gtolib$map.isEmpty();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public int size() {
        if (gtolib$map == null) return 0;
        return gtolib$map.size();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public Iterator<Reference2LongMap.Entry<AEKey>> iterator() {
        if (gtolib$map == null) return Collections.emptyIterator();
        return gtolib$map.iterator();
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public @Nullable AEKey getFirstKey() {
        var e = getFirstEntry();
        return e != null ? e.getKey() : null;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    @Nullable
    public <T extends AEKey> T getFirstKey(Class<T> keyClass) {
        var e = getFirstEntry(keyClass);
        return e != null ? keyClass.cast(e.getKey()) : null;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    @Nullable
    public Object2LongMap.Entry<AEKey> getFirstEntry() {
        if (gtolib$map == null) return null;
        for (var e : gtolib$map) {
            return new Entry(e.getLongValue(), e.getKey());
        }
        return null;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    @Nullable
    public <T extends AEKey> Object2LongMap.Entry<AEKey> getFirstEntry(Class<T> keyClass) {
        if (gtolib$map == null) return null;
        for (var e : gtolib$map) {
            if (keyClass.isInstance(e.getKey())) return new Entry(e.getLongValue(), e.getKey());
        }
        return null;
    }

    /**
     * @author .
     * @reason .
     */
    @Overwrite(remap = false)
    public Set<AEKey> keySet() {
        if (gtolib$map == null) return Collections.emptySet();
        return gtolib$map.keySet();
    }

    @Override
    public Reference2LongOpenHashMap<AEKey> gtolib$getMap() {
        return gtolib$map;
    }

    @Override
    public boolean gtolib$contains(AEKey key) {
        if (gtolib$map == null) return false;
        return gtolib$map.containsKey(key);
    }

    @Override
    public void gtolib$ensureCapacity(int capacity) {
        if (gtolib$map == null) {
            gtolib$map = new ExpandedR2LMap<>();
        }
        gtolib$map.ensureCapacity(capacity);
    }

    @Override
    public void gtolib$addAll(int size, Consumer<ExpandedR2LMap<AEKey>> consumer) {
        if (size < 1) return;
        if (this.gtolib$map == null) {
            this.gtolib$map = new ExpandedR2LMap<>(size);
        }
        gtolib$map.ensureCapacity(size);
        consumer.accept(gtolib$map);
        gtolib$fuzzyUpdate = true;
    }
}
