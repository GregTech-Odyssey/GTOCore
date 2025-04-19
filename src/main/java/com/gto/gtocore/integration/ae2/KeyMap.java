package com.gto.gtocore.integration.ae2;

import com.gregtechceu.gtceu.integration.ae2.utils.KeyStorage;

import net.minecraft.nbt.ListTag;

import appeng.api.stacks.AEKey;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public final class KeyMap extends KeyStorage {

    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public @NotNull ListTag serializeNBT() {
        lock.lock();
        try {
            return super.serializeNBT();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void deserializeNBT(ListTag tags) {
        for (int i = 0; i < tags.size(); i++) {
            var tag = tags.getCompound(i);
            var key = AEKey.fromTagGeneric(tag.getCompound("key"));
            long value = tag.getLong("value");
            lock.lock();
            try {
                storage.put(key, value);
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public @NotNull Iterator<Object2LongMap.Entry<AEKey>> iterator() {
        lock.lock();
        try {
            return super.iterator();
        } finally {
            lock.unlock();
        }
    }
}
