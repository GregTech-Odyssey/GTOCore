package com.gto.gtocore.integration.ae2;

import com.gregtechceu.gtceu.integration.ae2.utils.KeyStorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import appeng.api.stacks.AEKey;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public final class KeyMap extends KeyStorage {

    public void put(AEKey key, long value) {
        synchronized (storage) {
            storage.put(key, value);
        }
    }

    private ObjectSet<Object2LongMap.Entry<AEKey>> object2LongEntrySet() {
        synchronized (storage) {
            return storage.object2LongEntrySet();
        }
    }

    @Override
    public @NotNull ListTag serializeNBT() {
        var list = new ListTag();
        for (var entry : object2LongEntrySet()) {
            var tag = new CompoundTag();
            tag.put("key", entry.getKey().toTagGeneric());
            tag.putLong("value", entry.getLongValue());
            list.add(tag);
        }
        return list;
    }

    @Override
    public void deserializeNBT(ListTag tags) {
        for (int i = 0; i < tags.size(); i++) {
            var tag = tags.getCompound(i);
            var key = AEKey.fromTagGeneric(tag.getCompound("key"));
            long value = tag.getLong("value");
            put(key, value);
        }
    }

    @Override
    public @NotNull Iterator<Object2LongMap.Entry<AEKey>> iterator() {
        return object2LongEntrySet().iterator();
    }
}
